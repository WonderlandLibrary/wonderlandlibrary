package me.aquavit.liquidsense.utils.extensions;

import net.minecraft.entity.Entity;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.Vec3;

public class PlayerExtensionUtils {

    public static double getDistanceToEntityBox(Entity entity, Entity target) {
        Vec3 eyes = entity.getPositionEyes(0f);
        Vec3 pos = getNearestPointBB(eyes, target.getEntityBoundingBox());
        double xDist = Math.abs(pos.xCoord - eyes.xCoord);
        double yDist = Math.abs(pos.yCoord - eyes.yCoord);
        double zDist = Math.abs(pos.zCoord - eyes.zCoord);
        return Math.sqrt(Math.pow(xDist, 2) + Math.pow(yDist, 2) + Math.pow(zDist, 2));
    }


    public static Vec3 getNearestPointBB(Vec3 eye, AxisAlignedBB box) {

        double[] origin = {eye.xCoord, eye.yCoord, eye.zCoord};
        double[] destMins = {box.minX, box.minY, box.minZ};
        double[] destMaxs = {box.maxX, box.maxY, box.maxZ};

        for (int i = 0; i < 3; i++) {
            if (origin[i] > destMaxs[i]) {
                origin[i] = destMaxs[i];
            } else if (origin[i] < destMins[i]) {
                origin[i] = destMins[i];
            }

        }
        return new Vec3(origin[0], origin[1], origin[2]);
    }
}
