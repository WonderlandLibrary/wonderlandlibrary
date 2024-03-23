package dev.zprestige.prestige.api.mixin;

import dev.zprestige.prestige.api.interfaces.IRaycastContext;
import net.minecraft.block.ShapeContext;
import net.minecraft.entity.Entity;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.RaycastContext;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Mutable;
import org.spongepowered.asm.mixin.Shadow;

@Mixin(value={RaycastContext.class})
public class MixinRaycastContext implements IRaycastContext {

    @Shadow
    @Final
    @Mutable
    public Vec3d start;
    @Shadow
    @Final
    @Mutable
    public Vec3d end;
    @Shadow
    @Final
    @Mutable
    public RaycastContext.ShapeType shapeType;
    @Shadow
    @Final
    @Mutable
    public RaycastContext.FluidHandling fluid;
    @Shadow
    @Final
    @Mutable
    public ShapeContext entityPosition;

    @Override
    public void set(Vec3d start, Vec3d end, RaycastContext.ShapeType shapeType, RaycastContext.FluidHandling fluid, Entity entity) {
        this.start = start;
        this.end = end;
        this.shapeType = shapeType;
        this.fluid = fluid;
        this.entityPosition = ShapeContext.of(entity);
    }
}
