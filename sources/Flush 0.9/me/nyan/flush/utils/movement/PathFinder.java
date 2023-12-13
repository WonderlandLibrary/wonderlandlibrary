package me.nyan.flush.utils.movement;

import net.minecraft.client.Minecraft;
import net.minecraft.util.Vec3;

import java.util.ArrayList;

public class PathFinder {
    public static ArrayList<Vec3> findPathTo(Vec3 start, Vec3 end, int distanceBetweenTps) {
        start = new Vec3(Math.floor(start.xCoord), Math.floor(start.yCoord), Math.floor(start.zCoord));
        end = new Vec3(Math.floor(end.xCoord), Math.floor(end.yCoord), Math.floor(end.zCoord));

        ArrayList<Vec3> vecs = new ArrayList<>();

        int yTpTimes = (int) Math.abs(end.yCoord - start.yCoord) / distanceBetweenTps;
        int yMultiplier = end.yCoord - start.yCoord < 0 ? -distanceBetweenTps : distanceBetweenTps;
        for (int yCount = 0; yCount < yTpTimes; yCount++) {
            vecs.add(start = start.addVector(0, yMultiplier, 0));
        }
        vecs.add(start = new Vec3(start.xCoord, end.yCoord, start.zCoord));

        int xTpTimes = (int) Math.abs(end.xCoord - start.xCoord) / distanceBetweenTps;
        int xMultiplier = end.xCoord - start.xCoord < 0 ? -distanceBetweenTps : distanceBetweenTps;
        for (int xCount = 0; xCount < xTpTimes; xCount++) {
            vecs.add(start = start.addVector(xMultiplier, 0, 0));
        }
        vecs.add(start = new Vec3(end.xCoord, start.yCoord, start.zCoord));

        int zTpTimes = (int) Math.abs(end.zCoord - start.zCoord) / distanceBetweenTps;
        int zMultiplier = end.zCoord - start.zCoord < 0 ? -distanceBetweenTps : distanceBetweenTps;
        for (int zCount = 0; zCount < zTpTimes; zCount++) {
            vecs.add(start = start.addVector(0, 0, zMultiplier));
        }
        vecs.add(end);

        return vecs;
    }

    public static ArrayList<Vec3> findPathTo(Vec3 start, Vec3 end) {
        return findPathTo(start, end, 5);
    }

    public static ArrayList<Vec3> findPathTo(Vec3 position) {
        return findPathTo(position, 5);
    }

    public static ArrayList<Vec3> findPathTo(Vec3 position, int distanceBetweenTps) {
        return findPathTo(Minecraft.getMinecraft().thePlayer.getPositionVector(), position, distanceBetweenTps);
    }
}