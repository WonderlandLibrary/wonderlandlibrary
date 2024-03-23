package dev.zprestige.prestige.api.interfaces;

import net.minecraft.entity.Entity;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.RaycastContext;


public interface IRaycastContext {
    void set(Vec3d var1, Vec3d var2, RaycastContext.ShapeType var3, RaycastContext.FluidHandling var4, Entity var5);
}
