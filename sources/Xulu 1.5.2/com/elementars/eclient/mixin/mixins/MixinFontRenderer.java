// 
// Decompiled by Procyon v0.5.36
// 

package com.elementars.eclient.mixin.mixins;

import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;
import com.elementars.eclient.module.core.Global;
import org.spongepowered.asm.mixin.Shadow;
import net.minecraft.client.gui.FontRenderer;
import org.spongepowered.asm.mixin.Mixin;

@Mixin({ FontRenderer.class })
public class MixinFontRenderer
{
    @Shadow
    private int renderString(final String text, final float x, final float y, final int color, final boolean dropShadow) {
        return 0;
    }
    
    @Redirect(method = { "drawStringWithShadow" }, at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/FontRenderer;drawString(Ljava/lang/String;FFIZ)I"))
    private int drawStringMaybeWithShadow(final FontRenderer fontRenderer, final String text, final float x, final float y, final int color, final boolean dropShadow) {
        if (Global.textShadow.getValue()) {
            return fontRenderer.drawString(text, x, y, color, true);
        }
        return fontRenderer.drawString(text, x, y, color, false);
    }
    
    @Redirect(method = { "drawString(Ljava/lang/String;FFIZ)I" }, at = @At(value = "INVOKE", ordinal = 0, target = "Lnet/minecraft/client/gui/FontRenderer;renderString(Ljava/lang/String;FFIZ)I"))
    private int drawStringMore(final FontRenderer fontRenderer, final String text, float x, float y, final int color, final boolean dropShadow) {
        if (Global.shortShadow != null && Global.shortShadow.getValue()) {
            x -= 0.4f;
            y -= 0.4f;
        }
        return this.renderString(text, x, y, color, dropShadow);
    }
}
