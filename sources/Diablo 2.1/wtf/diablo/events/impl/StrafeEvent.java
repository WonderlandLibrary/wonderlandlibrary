package wtf.diablo.events.impl;

import wtf.diablo.events.Event;

/**
 * @author Divine
 * @since 4/11/2022
 * Hooked in Entity#moveFlying
 */
public class StrafeEvent extends Event {

    private float strafe, foward, friction;

    public StrafeEvent(float strafe, float foward, float friction) {
        this.strafe = strafe;
        this.foward = foward;
        this.friction = friction;
    }

    public float getStrafe() {
        return strafe;
    }

    public void setStrafe(float strafe) {
        this.strafe = strafe;
    }

    public float getFoward() {
        return foward;
    }

    public void setFoward(float foward) {
        this.foward = foward;
    }

    public float getFriction() {
        return friction;
    }

    public void setFriction(float friction) {
        this.friction = friction;
    }

    public void setMotion(float speed) {
        speed *= foward != 0.0F || strafe != 0.0F ? 0.91F : 1F;
        setFriction(speed);
    }
}
