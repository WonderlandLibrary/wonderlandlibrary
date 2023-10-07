package best.azura.client.impl.events;

import best.azura.scripting.event.NamedEvent;

public class EventMove implements NamedEvent {

    private double x, y, z;
    private boolean safeWalk = false, safeWalkInAir = false, noClip;

    public EventMove(double x, double y, double z, boolean noClip) {
        this.x = x;
        this.y = y;
        this.z = z;
        this.noClip = noClip;
    }
    public boolean isNoClip() {
        return noClip;
    }
    public void setNoClip(boolean noClip) {
        this.noClip = noClip;
    }
    public boolean isSafeWalk() {
        return safeWalk;
    }
    public boolean isSafeWalkInAir() {
        return safeWalkInAir;
    }
    public void setSafeWalk(boolean safeWalk) {
        this.safeWalk = safeWalk;
    }
    public void setSafeWalkInAir(boolean safeWalkInAir) {
        this.safeWalkInAir = safeWalkInAir;
    }
    public void setX(double x) {
        this.x = x;
    }
    public void setY(double y) {
        this.y = y;
    }
    public void setZ(double z) {
        this.z = z;
    }
    public double getX() {
        return x;
    }
    public double getY() {
        return y;
    }
    public double getZ() {
        return z;
    }

    @Override
    public String name() {
        return "move";
    }
}
