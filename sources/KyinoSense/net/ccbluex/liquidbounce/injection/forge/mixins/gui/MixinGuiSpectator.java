/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.client.gui.GuiSpectator
 *  net.minecraft.client.gui.ScaledResolution
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
    private void renderTooltipPost(ScaledResolution p_175264_1_, float p_175264_2_, CallbackInfo callbackInfo) {
        LiquidBounce.eventManager.callEvent(new Render2DEvent(p_175264_2_));
        AWTFontRenderer.Companion.garbageCollectionTick();
    }
}

