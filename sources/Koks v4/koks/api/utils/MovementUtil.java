package koks.api.utils;

import koks.api.Methods;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.network.play.client.C03PacketPlayer;
import net.minecraft.util.BlockPos;
import net.minecraft.util.MathHelper;

/**
 * Copyright 2020, Koks Team
 * Please don't use the code
 */
public class MovementUtil implements Methods {

    private static MovementUtil movementUtil;

    public float getDirection(float rotationYaw) {
        float left = Minecraft.getMinecraft().gameSettings.keyBindLeft.pressed ? mc.gameSettings.keyBindBack.pressed ? 45 : mc.gameSettings.keyBindForward.pressed ? -45 : -90 : 0;
        float right = Minecraft.getMinecraft().gameSettings.keyBindRight.pressed ? mc.gameSettings.keyBindBack.pressed ? -45 : mc.gameSettings.keyBindForward.pressed ? 45 : 90 : 0;
        float back = Minecraft.getMinecraft().gameSettings.keyBindBack.pressed ? + 180 : 0;
        float yaw = left + right + back;
        return rotationYaw + yaw;
    }

    public double getSpeed(EntityPlayer player) {
        return Math.sqrt(player.motionX * player.motionX + player.motionZ * player.motionZ);
    }

    public double[] getSpeed(double speed, float yaw, boolean direction) {
        final double motionX = -Math.sin(Math.toRadians(direction ? getDirection(yaw) : yaw)) * speed;
        final double motionZ = Math.cos(Math.toRadians(direction ? getDirection(yaw) : yaw)) * speed;
        return new double[] {motionX, motionZ};
    }

    public void setSpeed(double speed) {
        this.setSpeed(speed, getPlayer().rotationYaw);
    }

    public void setSpeed(double speed, float yaw) {
        this.setSpeed(speed, yaw, true);
    }

    public void setSpeed(double speed, float yaw, boolean direction) {
        getPlayer().motionX = -Math.sin(Math.toRadians(direction ? getDirection(yaw) : yaw)) * speed;
        getPlayer().motionZ = Math.cos(Math.toRadians(direction ? getDirection(yaw) : yaw)) * speed;
    }

    public void addMotion(double speed) {
        this.addMotion(speed, getPlayer().rotationYaw);
    }

    public void addMotion(double speed, float yaw) {
        getPlayer().motionX -= (MathHelper.sin((float) Math.toRadians(yaw)) * speed);
        getPlayer().motionZ += (MathHelper.cos((float) Math.toRadians(yaw)) * speed);
    }

    public void teleportTo(double speed, double y, float yaw) {
        double motionX = -Math.sin(Math.toRadians(getDirection(yaw))) * speed;
        double motionZ = Math.cos(Math.toRadians(getDirection(yaw))) * speed;
        getPlayer().setPosition(getX() + motionX, y, getZ() + motionZ);
    }

    public void teleportTo(double speed, double y) {
        this.teleportTo(speed, y, getYaw());
    }

    public void teleportTo(double speed) {
        this.teleportTo(speed, getY(), getYaw());
    }

    public void teleportTo(Entity entity, double stepDistance) {
        double distanceToEntity = getPlayer().getDistanceToEntity(entity);
        for(double step = 0; step < distanceToEntity; step+=stepDistance) {
            double stepX = getX() - ((getX() - entity.posX) / distanceToEntity * step);
            double stepY = getY() - ((getY() - entity.posY) / distanceToEntity * step);
            double stepZ = getZ() - ((getZ() - entity.posZ) / distanceToEntity * step);
            setPosition(stepX, stepY, stepZ);
        }
        setPosition(entity.posX, entity.posY, entity.posZ);
    }

    public void teleportTo(BlockPos blockPos, double stepDistance) {
        BlockPos startPos = new BlockPos(getX(), getY(), getZ());
        double distanceToBlockPos = getPlayer().getDistance(blockPos.getX(), blockPos.getY(), blockPos.getZ());
        for(double step = 0; step < distanceToBlockPos; step+=stepDistance) {
            double stepX = startPos.getX() - ((startPos.getX() - blockPos.getX()) / distanceToBlockPos * step);
            double stepY = startPos.getY() - ((startPos.getY() - blockPos.getY()) / distanceToBlockPos * step);
            double stepZ = startPos.getZ() - ((startPos.getZ() - blockPos.getZ()) / distanceToBlockPos * step);
            setPosition(stepX, stepY, stepZ);
        }
        setPosition(blockPos.getX(), blockPos.getY(), blockPos.getZ());
    }

    public void teleportTo(double x, double y, double z, double stepDistance) {
        double distanceToPosition = getPlayer().getDistance(x, y, z);
        for(double step = 0; step < distanceToPosition; step+=stepDistance) {
            double stepX = getX() - ((getX() - x) / distanceToPosition * step);
            double stepY = getY() - ((getY() - y) / distanceToPosition * step);
            double stepZ = getZ() - ((getZ() - z) / distanceToPosition * step);
            setPosition(stepX, stepY, stepZ);
        }
        setPosition(x, y, z);
    }

    public void blinkTo(Entity entity, double stepDistance) {
        double distanceToEntity = getPlayer().getDistanceToEntity(entity);
        for(double step = 0; step < distanceToEntity; step+=stepDistance) {
            double stepX = getX() - ((getX() - entity.posX) / distanceToEntity * step);
            double stepY = getY() - ((getY() - entity.posY) / distanceToEntity * step);
            double stepZ = getZ() - ((getZ() - entity.posZ) / distanceToEntity * step);
            sendPacket(new C03PacketPlayer.C04PacketPlayerPosition(stepX, stepY, stepZ, getPlayer().onGround));
        }
        sendPacket(new C03PacketPlayer.C04PacketPlayerPosition(entity.posX, entity.posY, entity.posZ, getPlayer().onGround));
    }

    public void blinkTo(BlockPos blockPos, double stepDistance) {
        double distanceToEntity = getPlayer().getDistance(blockPos.getX(), blockPos.getY(), blockPos.getZ());
        for(double step = 0; step < distanceToEntity; step+=stepDistance) {
            double stepX = getX() - ((getX() - blockPos.getX()) / distanceToEntity * step);
            double stepY = getY() - ((getY() - blockPos.getY()) / distanceToEntity * step);
            double stepZ = getZ() - ((getZ() - blockPos.getZ()) / distanceToEntity * step);
            sendPacket(new C03PacketPlayer.C04PacketPlayerPosition(stepX, stepY, stepZ, getPlayer().onGround));
        }
        sendPacket(new C03PacketPlayer.C04PacketPlayerPosition(blockPos.getX(), blockPos.getY(), blockPos.getZ(), getPlayer().onGround));
    }

    public void blinkTo(double x, double y, double z, double stepDistance) {
        double distanceToEntity = getPlayer().getDistance(x, y, z);
        for(double step = 0; step < distanceToEntity; step+=stepDistance) {
            double stepX = getX() - ((getX() - x) / distanceToEntity * step);
            double stepY = getY() - ((getY() - y) / distanceToEntity * step);
            double stepZ = getZ() - ((getZ() - z) / distanceToEntity * step);
            sendPacket(new C03PacketPlayer.C04PacketPlayerPosition(stepX, stepY, stepZ, getPlayer().onGround));
        }
        sendPacket(new C03PacketPlayer.C04PacketPlayerPosition(x, y, z, getPlayer().onGround));
    }

    public void blinkTo(double speed) {
        this.blinkTo(speed, getPlayer().rotationYaw);
    }

    public void blinkTo(double speed, float yaw) {
        this.blinkTo(speed, getY(), yaw, getPlayer().onGround);
    }

    public void blinkTo(double speed, double y, float yaw, boolean ground) {
        double motionX = -Math.sin(Math.toRadians(getDirection(yaw))) * speed;
        double motionZ = Math.cos(Math.toRadians(getDirection(yaw))) * speed;
        sendPacket(new C03PacketPlayer.C04PacketPlayerPosition(getX() + motionX, y, getZ() + motionZ, ground));
    }

    public void blinkToAndUpdate(double speed) {
        this.blinkToAndUpdate(speed, getPlayer().rotationYaw);
    }

    public void blinkToAndUpdate(double speed, float yaw) {
        this.blinkToAndUpdate(speed,getY(),yaw);
    }

    public void blinkToAndUpdate(double speed, double y, float yaw) {
        double motionX = -Math.sin(Math.toRadians(getDirection(yaw))) * speed;
        double motionZ = Math.cos(Math.toRadians(getDirection(yaw))) * speed;

        getPlayer().setPositionAndUpdate(getX() + motionX, y, getZ() + motionZ);
    }

    public static MovementUtil getInstance() {
        if(movementUtil == null) {
            movementUtil = new MovementUtil();
        }
        return movementUtil;
    }
}
