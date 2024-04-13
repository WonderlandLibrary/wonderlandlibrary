package com.canon.majik.api.utils.autocrystal;

import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;

public class CrystalPosition {
    private final BlockPos pos;
    private final float damage, distance;
    private Vec3d raytrace;

    public CrystalPosition(BlockPos pos, float damage, float distance, Vec3d raytrace){
        this.pos = pos;
        this.damage = damage;
        this.distance = distance;
        this.raytrace = raytrace;
    }

    public BlockPos getPos() {
        return pos;
    }

    public float getDamage() {
        return damage;
    }

    public float getDistance() {
        return distance;
    }

    public Vec3d getRaytrace() {
        return raytrace;
    }

    public void setRaytrace(Vec3d raytrace) {
        this.raytrace = raytrace;
    }
}
