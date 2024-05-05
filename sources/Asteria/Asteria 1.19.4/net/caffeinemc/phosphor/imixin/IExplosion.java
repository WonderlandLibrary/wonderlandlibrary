package net.caffeinemc.phosphor.imixin;

import net.minecraft.util.math.Vec3d;

public interface IExplosion {
    void set(Vec3d explosionPos, float power, boolean createFire);
}
