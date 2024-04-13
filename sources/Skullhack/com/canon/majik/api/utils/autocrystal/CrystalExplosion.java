package com.canon.majik.api.utils.autocrystal;

import net.minecraft.entity.item.EntityEnderCrystal;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;

public class CrystalExplosion {
    private final EntityEnderCrystal entity;
    private final float damage, distance;
    private Vec3d raytrace;

    public CrystalExplosion(EntityEnderCrystal entity, float damage, float distance, Vec3d raytrace) {
        this.entity = entity;
        this.damage = damage;
        this.distance = distance;
        this.raytrace = raytrace;
    }

    public EntityEnderCrystal getEntity() {
        return entity;
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
