package vip.radium.event.impl.world;

import vip.radium.event.CancellableEvent;

public final class PlaySoundEvent extends CancellableEvent {

    private final double posX;
    private final double posY;
    private final double posZ;
    private final double dist;
    private final String soundName;

    public PlaySoundEvent(double posX, double posY, double posZ, double dist, String soundName) {
        this.posX = posX;
        this.posY = posY;
        this.posZ = posZ;
        this.dist = dist;
        this.soundName = soundName;
    }

    public double getDist() {
        return dist;
    }

    public double getPosX() {
        return posX;
    }

    public double getPosY() {
        return posY;
    }

    public double getPosZ() {
        return posZ;
    }

    public String getSoundName() {
        return soundName;
    }
}
