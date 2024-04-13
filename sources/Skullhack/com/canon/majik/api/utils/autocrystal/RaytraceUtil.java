package com.canon.majik.api.utils.autocrystal;

import com.canon.majik.api.utils.Globals;
import net.minecraft.entity.Entity;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;

import java.util.TreeMap;

public class RaytraceUtil implements Globals {

    public static boolean hasVisibleVec(BlockPos pos) {
        return getRaytraceSides(pos) != null;
    }


    public static Vec3d getRaytraceSides(BlockPos pos) {
        AxisAlignedBB bb = new AxisAlignedBB(pos);
        TreeMap<Double, Vec3d> map = new TreeMap<>();
        for (int x = 0; x <= 2; x++) {
            for (int y = 0; y <= 2; y++) {
                for (int z = 0; z <= 2; z++) {
                    if (x == 1 && y == 1 && z == 1) {
                        continue;
                    }
                    float x1 = (float) (bb.minX + x / 2.0f), y1 = (float) (bb.minY + y / 2.0f), z1 = (float) (bb.minZ + z / 2.0f);
                    Vec3d vec = new Vec3d(x1, y1, z1);
                    if (raytrace(vec)) {
                        map.put(Math.sqrt(mc.player.getPositionEyes(mc.getRenderPartialTicks()).squareDistanceTo(x1, y1, z1)), vec);
                    }
                }
            }
        }
        if (!map.isEmpty()) {
            return map.firstEntry().getValue();
        }

        return null;
    }

    public static boolean raytrace(Entity entity) {
        return mc.world.rayTraceBlocks(new Vec3d(mc.player.posX, mc.player.posY + mc.player.getEyeHeight(), mc.player.posZ), new Vec3d(entity.posX, entity.posY + entity.getEyeHeight() + entity.height / 2.0f, entity.posZ)) == null;
    }

    public static boolean raytrace(Vec3d vec) {
        return mc.world.rayTraceBlocks(new Vec3d(mc.player.posX, mc.player.posY + mc.player.getEyeHeight(), mc.player.posZ), vec, false, true, false) == null;
    }

    public static boolean raytrace(BlockPos pos) {
        return raytrace(new Vec3d(pos.getX() + 0.5f, pos.getY() + 0.5f, pos.getZ() + 0.5f));
    }
}