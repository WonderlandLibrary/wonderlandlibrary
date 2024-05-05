package me.earth.earthhack.impl.core.mixins.minecraft.entity;

import net.minecraft.entity.Entity;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.util.math.AxisAlignedBB;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

@Mixin(Entity.class)
public abstract class MixinEntity
{
    @Shadow
    public float rotationYaw;

    @Shadow
    public float rotationPitch;

    @Shadow
    public abstract AxisAlignedBB getEntityBoundingBox();

    @Shadow
    protected EntityDataManager dataManager;

}
