/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.client.gui.Gui
 *  org.spongepowered.asm.mixin.Mixin
 *  org.spongepowered.asm.mixin.Shadow
 */
package net.ccbluex.liquidbounce.injection.forge.mixins.gui;

import net.minecraft.client.gui.Gui;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

@Mixin(value={Gui.class})
public abstract class MixinGui {
    @Shadow
    protected float field_73735_i;

    @Shadow
    public abstract void func_73729_b(int var1, int var2, int var3, int var4, int var5, int var6);
}

