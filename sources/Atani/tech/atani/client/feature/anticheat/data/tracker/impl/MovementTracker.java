package tech.atani.client.feature.anticheat.data.tracker.impl;

import net.minecraft.network.Packet;
import net.minecraft.network.play.server.S14PacketEntity;
import net.minecraft.network.play.server.S18PacketEntityTeleport;
import tech.atani.client.feature.anticheat.data.PlayerData;
import tech.atani.client.feature.anticheat.data.tracker.Tracker;

public class MovementTracker extends Tracker {

    private double x, y, z, lastX, lastY, lastZ;
    private double xDiff, yDiff, zDiff, speed, lastSpeed;

    private boolean onGround, lastOnGround;

    public MovementTracker(PlayerData data) {
        super(data);

        this.x = data.getPlayer().posX;
        this.y = data.getPlayer().posY;
        this.z = data.getPlayer().posZ;
    }

    @Override
    public void handle(Packet<?> packet) {
        if(packet instanceof S14PacketEntity) {
            S14PacketEntity packetEntity = (S14PacketEntity) packet;

            if(packetEntity.getEntityId() == getData().getPlayer().getEntityId()) {

                this.lastX = x;
                this.lastY = y;
                this.lastZ = z;

                this.x = getData().getPlayer().posX;
                this.y = getData().getPlayer().posY;
                this.z = getData().getPlayer().posZ;

                this.xDiff = Math.abs(x - lastX);
                this.zDiff = Math.abs(z - lastZ);

                this.yDiff = y - lastY;

                this.lastSpeed = lastSpeed;
                this.speed = Math.sqrt(xDiff * xDiff + zDiff * zDiff);

                this.lastOnGround = onGround;
                this.onGround = packetEntity.getOnGround();
            }
        } else if(packet instanceof S18PacketEntityTeleport) {
            S18PacketEntityTeleport packetTeleport = (S18PacketEntityTeleport) packet;

            if(packetTeleport.getEntityId() == getData().getPlayer().getEntityId()) {
                this.lastX = x;
                this.lastY = y;
                this.lastZ = z;

                this.x = getData().getPlayer().posX;
                this.y = getData().getPlayer().posY;
                this.z = getData().getPlayer().posZ;

                this.xDiff = Math.abs(x - lastX);
                this.yDiff = Math.abs(y - lastY);
                this.zDiff = Math.abs(z - lastZ);

                this.speed = Math.abs(xDiff - zDiff);

                this.lastOnGround = onGround;
                this.onGround = packetTeleport.getOnGround();
            }
        }
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

    public double getLastX() {
        return lastX;
    }

    public double getLastY() {
        return lastY;
    }

    public double getLastZ() {
        return lastZ;
    }

    public double getxDiff() {
        return xDiff;
    }

    public double getyDiff() {
        return yDiff;
    }

    public double getzDiff() {
        return zDiff;
    }

    public double getSpeed() {
        return speed;
    }

    public double getLastSpeed() {
        return lastSpeed;
    }

    public boolean isOnGround() {
        return onGround;
    }

    public boolean isLastOnGround() {
        return lastOnGround;
    }
}
