/*
 * Decompiled with CFR 0_122.
 */
package me.arithmo.util.misc;

import java.util.List;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.client.multiplayer.WorldClient;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.pathfinding.PathEntity;
import net.minecraft.pathfinding.PathFinder;
import net.minecraft.pathfinding.PathPoint;
import net.minecraft.util.Vec3;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.pathfinder.NodeProcessor;
import net.minecraft.world.pathfinder.WalkNodeProcessor;

public class PathFind {
    public EntityPlayer pos;
    public PathFinder pathFinder = new PathFinder(new WalkNodeProcessor());
    private float yaw;
    public static float fakeYaw;
    public static float fakePitch;

    public PathFind(String name) {
        for (Object i : Minecraft.getMinecraft().theWorld.loadedEntityList) {
            EntityPlayer player;
            if (!(i instanceof EntityPlayer) || i == null || !(player = (EntityPlayer)i).getName().contains(name)) continue;
            this.pos = player;
        }
        if (this.pos != null) {
            this.move();
            float[] rot = this.getRotationTo(this.pos.getPositionVector());
            fakeYaw = rot[0];
            fakePitch = rot[1];
        }
    }

    public void move() {
        PathEntity pe;
        if (Minecraft.getMinecraft().thePlayer.getDistance(this.pos.posX + 0.5, this.pos.posY + 0.5, this.pos.posZ + 0.5) > 0.3 && (pe = this.pathFinder.func_176188_a(Minecraft.getMinecraft().theWorld, Minecraft.getMinecraft().thePlayer, this.pos, 40.0f)) != null && pe.getCurrentPathLength() > 1) {
            PathPoint point = pe.getPathPointFromIndex(1);
            float[] rot = this.getRotationTo(new Vec3((double)point.xCoord + 0.5, (double)point.yCoord + 0.5, (double)point.zCoord + 0.5));
            this.yaw = rot[0];
            EntityPlayerSP player = Minecraft.getMinecraft().thePlayer;
            EntityPlayerSP player2 = Minecraft.getMinecraft().thePlayer;
            double n = 0.0;
            player2.motionZ = 0.0;
            player.motionX = 0.0;
            double offset = 0.26;
            double newx = Math.sin(this.yaw * 3.1415927f / 180.0f) * 0.26;
            double newz = Math.cos(this.yaw * 3.1415927f / 180.0f) * 0.26;
            EntityPlayerSP player3 = Minecraft.getMinecraft().thePlayer;
            player3.motionX -= newx;
            EntityPlayerSP player4 = Minecraft.getMinecraft().thePlayer;
            player4.motionZ += newz;
            if (Minecraft.getMinecraft().thePlayer.onGround && Minecraft.getMinecraft().thePlayer.isCollidedHorizontally) {
                Minecraft.getMinecraft().thePlayer.jump();
            }
        }
    }

    public static float angleDifference(float to, float from) {
        return ((to - from) % 360.0f + 540.0f) % 360.0f - 180.0f;
    }

    public float[] getRotationTo(Vec3 pos) {
        double xD = Minecraft.getMinecraft().thePlayer.posX - pos.xCoord;
        double yD = Minecraft.getMinecraft().thePlayer.posY + (double)Minecraft.getMinecraft().thePlayer.getEyeHeight() - pos.yCoord;
        double zD = Minecraft.getMinecraft().thePlayer.posZ - pos.zCoord;
        double yaw = Math.atan2(zD, xD);
        double pitch = Math.atan2(yD, Math.sqrt(Math.pow(xD, 2.0) + Math.pow(zD, 2.0)));
        return new float[]{(float)Math.toDegrees(yaw) + 90.0f, (float)Math.toDegrees(pitch)};
    }
}

