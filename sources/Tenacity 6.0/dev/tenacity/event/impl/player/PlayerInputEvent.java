package dev.tenacity.event.impl.player;

import dev.tenacity.event.Event;

public class PlayerInputEvent extends Event {

    private float forward, strafe;
    private boolean jump, sneak;

    public PlayerInputEvent(float forward, float strafe, boolean jump, boolean sneak) {
        this.forward = forward;
        this.strafe = strafe;
        this.jump = jump;
        this.sneak = sneak;
    }

    public void setForward(float forward) {
        this.forward = forward;
    }

    public void setStrafe(float strafe) {
        this.strafe = strafe;
    }

    public void setJump(boolean jump) {
        this.jump = jump;
    }

    public void setSneak(boolean sneak) {
        this.sneak = sneak;
    }

    public float getForward() {
        return forward;
    }

    public float getStrafe() {
        return strafe;
    }

    public boolean getJumping() {
        return jump;
    }

    public boolean getSneaking() {
        return sneak;
    }

    public void stop() {
        this.forward = 0;
        this.strafe = 0;
        this.jump = false;
        this.sneak = false;
    }
}
