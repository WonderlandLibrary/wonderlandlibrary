/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.block.Block
 */
package net.dev.important.injection.forge.mixins.accessors;

import net.minecraft.block.Block;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(value={Block.class})
public interface BlockAccessor {
    @Accessor
    public void setMaxY(double var1);
}

