/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraftforge.client.GuiIngameForge
 */
package net.dev.important.injection.forge.mixins.accessors;

import net.minecraftforge.client.GuiIngameForge;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Invoker;

@Mixin(value={GuiIngameForge.class})
public interface GuiIngameForgeAccessor {
    @Invoker(remap=false)
    public void callRenderCrosshairs(int var1, int var2);
}

