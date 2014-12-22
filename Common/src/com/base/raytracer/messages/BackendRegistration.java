package com.base.raytracer.messages;

import akka.actor.ActorRef;

import java.io.Serializable;

/**
 * @author Róbert Dóczi
 *         Date: 2014.12.22.
 */
public class BackendRegistration implements Serializable {
    private final ActorRef renderRouter;
    private final int      nrOfWorkers;

    public BackendRegistration(ActorRef renderRouter, int nrOfWorkers) {

        this.renderRouter = renderRouter;
        this.nrOfWorkers = nrOfWorkers;
    }

    public ActorRef getRenderRouter() {
        return renderRouter;
    }

    public int getNrOfWorkers() {
        return nrOfWorkers;
    }
}
