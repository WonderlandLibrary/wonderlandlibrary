/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.client.gui.FontRenderer
 *  net.minecraftforge.fml.relauncher.Side
 *  net.minecraftforge.fml.relauncher.SideOnly
 *  org.lwjgl.opengl.GL20
 *  org.spongepowered.asm.mixin.Debug
 *  org.spongepowered.asm.mixin.Mixin
 *  org.spongepowered.asm.mixin.injection.At
 *  org.spongepowered.asm.mixin.injection.Inject
 *  org.spongepowered.asm.mixin.injection.ModifyVariable
 *  org.spongepowered.asm.mixin.injection.callback.CallbackInfo
 *  org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable
 */
package net.ccbluex.liquidbounce.injection.forge.mixins.render;

import net.ccbluex.liquidbounce.LiquidBounce;
import net.ccbluex.liquidbounce.event.TextEvent;
import net.ccbluex.liquidbounce.utils.render.shader.shaders.RainbowFontShader;
import net.minecraft.client.gui.FontRenderer;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.lwjgl.opengl.GL20;
import org.spongepowered.asm.mixin.Debug;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyVariable;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@SideOnly(value=Side.CLIENT)
@Debug(export=true, print=true)
@Mixin(value={FontRenderer.class})
public class MixinFontRenderer {
    private boolean rainbowEnabled0 = false;
    private boolean rainbowEnabled1 = false;

    @Debug(print=true)
    @Inject(method={"renderStringAtPos"}, at={@At(value="RETURN")}, require=1, allow=1)
    private void injectRainbow6(String string, boolean bl, CallbackInfo callbackInfo) {
        if (this.rainbowEnabled1) {
            GL20.glUseProgram((int)RainbowFontShader.INSTANCE.getProgramId());
        }
    }

    @Debug(print=true)
    @Inject(method={"renderStringAtPos"}, at={@At(value="INVOKE", target="Lnet/minecraft/client/gui/FontRenderer;setColor(FFFF)V", ordinal=1)}, require=1, allow=1)
    private void injectRainbow4(String string, boolean bl, CallbackInfo callbackInfo) {
        if (this.rainbowEnabled1) {
            GL20.glUseProgram((int)RainbowFontShader.INSTANCE.getProgramId());
        }
    }

    @ModifyVariable(method={"renderString"}, at=@At(value="HEAD"), require=1, ordinal=0, argsOnly=true)
    private String renderString(String string) {
        if (string == null) {
            return null;
        }
        if (LiquidBounce.eventManager == null) {
            return string;
        }
        TextEvent textEvent = new TextEvent(string);
        LiquidBounce.eventManager.callEvent(textEvent);
        return textEvent.getText();
    }

    @Debug(print=true)
    @Inject(method={"drawString(Ljava/lang/String;FFIZ)I"}, at={@At(value="INVOKE", target="Lnet/minecraft/client/gui/FontRenderer;renderString(Ljava/lang/String;FFIZ)I", ordinal=1)}, require=1, allow=1)
    private void injectShadow2(String string, float f, float f2, int n, boolean bl, CallbackInfoReturnable callbackInfoReturnable) {
        if (this.rainbowEnabled0) {
            GL20.glUseProgram((int)RainbowFontShader.INSTANCE.getProgramId());
        }
    }

    @Debug(print=true)
    @Inject(method={"renderStringAtPos"}, at={@At(value="INVOKE", target="Lnet/minecraft/client/gui/FontRenderer;setColor(FFFF)V", ordinal=0)}, require=1, allow=1)
    private void injectRainbow3(String string, boolean bl, CallbackInfo callbackInfo) {
        if (this.rainbowEnabled1) {
            GL20.glUseProgram((int)0);
        }
    }

    @Debug(print=true)
    @Inject(method={"renderStringAtPos"}, at={@At(value="HEAD")}, require=1, allow=1)
    private void injectRainbow5(String string, boolean bl, CallbackInfo callbackInfo) {
        this.rainbowEnabled1 = RainbowFontShader.INSTANCE.isInUse();
    }

    @Debug(print=true)
    @Inject(method={"drawString(Ljava/lang/String;FFIZ)I"}, at={@At(value="INVOKE", target="Lnet/minecraft/client/gui/FontRenderer;renderString(Ljava/lang/String;FFIZ)I", ordinal=0)}, require=1, allow=1)
    private void injectShadow1(String string, float f, float f2, int n, boolean bl, CallbackInfoReturnable callbackInfoReturnable) {
        this.rainbowEnabled0 = RainbowFontShader.INSTANCE.isInUse();
        if (this.rainbowEnabled0) {
            GL20.glUseProgram((int)0);
        }
    }

    @ModifyVariable(method={"getStringWidth"}, at=@At(value="HEAD"), require=1, ordinal=0, argsOnly=true)
    private String getStringWidth(String string) {
        if (string == null) {
            return null;
        }
        if (LiquidBounce.eventManager == null) {
            return string;
        }
        TextEvent textEvent = new TextEvent(string);
        LiquidBounce.eventManager.callEvent(textEvent);
        return textEvent.getText();
    }
}

