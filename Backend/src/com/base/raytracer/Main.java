package com.base.raytracer;

import akka.actor.ActorSystem;
import com.base.raytracer.actors.BackendMaster;
import com.typesafe.config.Config;
import com.typesafe.config.ConfigFactory;

/**
 * @author Róbert Dóczi
 *         Date: 2014.12.22.
 */
public class Main {

    public static void main(String[] args) {
        final String port = "5060";

        Config config = ConfigFactory.parseString("akka.remote.netty.tcp.port=" + port)
                .withFallback(ConfigFactory.parseString("akka.cluster.roles = [backend]"))
                .withFallback(ConfigFactory.load());

        ActorSystem system = ActorSystem.create("RenderCluster", config);
        system.actorOf(BackendMaster.props());
    }

}
