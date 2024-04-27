/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.client.renderer.GlStateManager
 *  net.minecraftforge.client.GuiIngameForge
 */
package net.dev.important.injection.forge.mixins.bugfixes;

import net.minecraft.client.renderer.GlStateManager;
import net.minecraftforge.client.GuiIngameForge;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(value={GuiIngameForge.class})
public class GuiIngameForgeMixin_HotbarAlpha {
    @Inject(method={"renderExperience"}, at={@At(value="HEAD")}, remap=false)
    private void patcher$enableExperienceAlpha(int filled, int top, CallbackInfo ci) {
        GlStateManager.func_179141_d();
    }

    @Inject(method={"renderExperience"}, at={@At(value="RETURN")}, remap=false)
    private void patcher$disableExperienceAlpha(int filled, int top, CallbackInfo ci) {
        GlStateManager.func_179118_c();
    }
}

