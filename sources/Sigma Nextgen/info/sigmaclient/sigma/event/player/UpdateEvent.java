package info.sigmaclient.sigma.event.player;

import info.sigmaclient.sigma.event.Event;

public class UpdateEvent extends Event {
    public double x, y, z;
    public float yaw;
    public float pitch;
    public float forcePitch;
    public boolean onGround, dontRotation, changeForce;
    public boolean send;
    public UpdateEvent(double x, double y, double z, float yaw, float pitch, boolean onGround){
        this.eventID = 16;
        this.x = x;
        this.y = y;
        this.z = z;
        this.yaw = yaw;
        this.pitch = pitch;
        this.onGround = onGround;
        this.pre = true;
        this.forcePitch = pitch;
        this.dontRotation = false;
        this.changeForce = false;
    }
    public boolean pre;
    public boolean isPre(){
        return pre;
    }
    public boolean isPost(){
        return !pre;
    }
}
