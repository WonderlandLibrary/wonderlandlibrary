/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.client.gui.GuiSpectator
 *  net.minecraft.client.gui.ScaledResolution
 *  org.spongepowered.asm.mixin.Mixin
 *  org.spongepowered.asm.mixin.injection.At
 *  org.spongepowered.asm.mixin.injection.Inject
 *  org.spongepowered.asm.mixin.injection.callback.CallbackInfo
 */
package net.ccbluex.liquidbounce.injection.forge.mixins.gui;

import net.ccbluex.liquidbounce.LiquidBounce;
import net.ccbluex.liquidbounce.event.Render2DEvent;
import net.ccbluex.liquidbounce.ui.font.AWTFontRenderer;
import net.minecraft.client.gui.GuiSpectator;
import net.minecraft.client.gui.ScaledResolution;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(value={GuiSpectator.class})
public class MixinGuiSpectator {
    @Inject(method={"renderTooltip"}, at={@At(value="RETURN")})
    private void renderTooltipPost(ScaledResolution scaledResolution, float f, CallbackInfo callbackInfo) {
        LiquidBounce.eventManager.callEvent(new Render2DEvent(f));
        AWTFontRenderer.Companion.garbageCollectionTick();
    }
}

