package tech.atani.client.feature.anticheat.data.tracker.impl;

import net.minecraft.network.Packet;
import net.minecraft.network.play.server.S14PacketEntity;
import tech.atani.client.feature.anticheat.data.PlayerData;
import tech.atani.client.feature.anticheat.data.tracker.Tracker;

public class AimTracker extends Tracker {

    private float yaw, pitch, lastYaw, lastPitch, yawDiff, pitchDiff;

    private boolean rotating;

    public AimTracker(PlayerData data) {
        super(data);

        this.yaw = data.getPlayer().rotationYaw;
        this.pitch = data.getPlayer().rotationPitch;

        this.rotating = false;
    }

    @Override
    public void handle(Packet<?> packet) {
        if(packet instanceof S14PacketEntity.S16PacketEntityLook) {
            S14PacketEntity.S16PacketEntityLook packetEntity = (S14PacketEntity.S16PacketEntityLook) packet;

            if(packetEntity.getEntityId() == getData().getPlayer().getEntityId()) {

                this.lastYaw = yaw;
                this.lastPitch = pitch;

                this.yaw = packetEntity.func_149066_f();
                this.pitch = packetEntity.func_149063_g();

                this.yawDiff = Math.max(yaw, lastYaw) -  Math.min(yaw, lastYaw);
                this.pitchDiff = Math.max(pitch, lastPitch) -  Math.min(pitch, lastPitch);

                this.rotating = yaw != lastYaw || pitch != lastPitch;
            }
        }
    }

    public float getYaw() {
        return yaw;
    }

    public float getPitch() {
        return pitch;
    }

    public float getLastYaw() {
        return lastYaw;
    }

    public float getLastPitch() {
        return lastPitch;
    }

    public float getYawDiff() {
        return yawDiff;
    }

    public float getPitchDiff() {
        return pitchDiff;
    }

    public boolean isRotating() {
        return rotating;
    }
}
