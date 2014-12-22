package com.base.raytracer.actors;

import akka.actor.AbstractActor;
import akka.actor.ActorRef;
import akka.actor.Props;
import akka.cluster.Cluster;
import akka.cluster.ClusterEvent.CurrentClusterState;
import akka.cluster.ClusterEvent.MemberUp;
import akka.cluster.Member;
import akka.cluster.MemberStatus;
import akka.japi.pf.ReceiveBuilder;
import akka.routing.RoundRobinPool;
import com.base.raytracer.Scene;
import com.base.raytracer.messages.BackendRegistration;
import com.base.raytracer.messages.RenderScene;
import com.base.raytracer.messages.RenderTask;
import com.base.raytracer.messages.Shutdown;

import java.util.stream.StreamSupport;

/**
 * @author Róbert Dóczi
 *         Date: 2014.12.22.
 */
public class BackendMaster extends AbstractActor {

    private Cluster cluster = Cluster.get(getContext().system());
    private ActorRef renderRouter;

    public BackendMaster() {
        receive(ReceiveBuilder
                .match(CurrentClusterState.class, currentClusterState -> {
                    StreamSupport.stream(currentClusterState.getMembers().spliterator(), false).filter(member -> member.status().equals(MemberStatus.up())).forEach(this::register);
                }).match(MemberUp.class, memberUp -> register(memberUp.member()))
                .match(RenderScene.class, sceneInfo -> initializeCluster(sceneInfo.getScene()))
                .match(RenderTask.class, renderTask -> renderRouter.forward(renderTask, context()))
                .match(Shutdown.class, shutdown -> context().system().shutdown()).build());
    }

    public static Props props() {
        return Props.create(BackendMaster.class, BackendMaster::new);
    }

    private void initializeCluster(Scene scene) {
        renderRouter = getContext().actorOf(RenderActor.props(scene).withRouter(new RoundRobinPool(Runtime.getRuntime().availableProcessors())), "RenderRouter");
    }

    private void register(Member member) {
        if (member.hasRole("frontend")) {
            getContext().actorSelection(member.address() + "/user/frontend").tell(new BackendRegistration(self(), Runtime.getRuntime().availableProcessors()), self());
        }
    }

    @Override
    public void preStart() throws Exception {
        cluster.subscribe(self(), MemberUp.class);
    }

    @Override
    public void postStop() throws Exception {
        cluster.unsubscribe(self());
    }
}
