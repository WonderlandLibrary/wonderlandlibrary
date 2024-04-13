package com.canon.majik.api.mixin.impl;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.util.Timer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(EntityLivingBase.class)
public interface IEntityLivingBase {

    @Accessor("lastDamage")
    float getLastDamage();

}