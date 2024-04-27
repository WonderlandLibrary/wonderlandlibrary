/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.entity.projectile.EntityArrow
 */
package net.dev.important.injection.forge.mixins.accessors;

import net.minecraft.entity.projectile.EntityArrow;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(value={EntityArrow.class})
public interface EntityArrowAccessor {
    @Accessor
    public boolean getInGround();
}

