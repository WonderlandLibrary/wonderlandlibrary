// 
// Decompiled by Procyon v0.5.36
// 

package ru.tuskevich.util.shader;

import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.Minecraft;
import java.awt.Color;
import net.minecraft.client.shader.Framebuffer;
import ru.tuskevich.util.Utility;

public class BlurUtility implements Utility
{
    public static Framebuffer bloomFramebuffer;
    
    public static void drawBlur(final float radius, final Runnable data) {
        StencilUtil.initStencilToWrite();
        data.run();
        StencilUtil.readStencilBuffer(1);
        GaussianBlur.renderBlur(radius);
        StencilUtil.uninitStencilBuffer();
    }
    
    public static void drawWhiteBlack(final Runnable data) {
        StencilUtil.initStencilToWrite();
        data.run();
        StencilUtil.readStencilBuffer(1);
        WhiteBlackUtil.renderColor(3.0f);
        StencilUtil.uninitStencilBuffer();
    }
    
    public static void drawDistortion(final Runnable data) {
        StencilUtil.initStencilToWrite();
        data.run();
        StencilUtil.readStencilBuffer(1);
        ReverseUtil.renderReerse();
        StencilUtil.uninitStencilBuffer();
    }
    
    public static void drawPixel(final Runnable data) {
        StencilUtil.initStencilToWrite();
        data.run();
        StencilUtil.readStencilBuffer(1);
        PixelUtil.renderReerse();
        StencilUtil.uninitStencilBuffer();
    }
    
    public static void drawShadow(final float radius, final float des, final Runnable data, final Color c, final boolean fill) {
        (BlurUtility.bloomFramebuffer = ShaderUtility.createFrameBuffer(BlurUtility.bloomFramebuffer)).framebufferClear();
        BlurUtility.bloomFramebuffer.bindFramebuffer(true);
        data.run();
        BlurUtility.bloomFramebuffer.unbindFramebuffer();
        BloomUtil.renderBlur(BlurUtility.bloomFramebuffer.framebufferTexture, (int)radius, 1, c, des, !fill);
    }
    
    static {
        BlurUtility.bloomFramebuffer = ShaderUtility.createFrameBuffer(new Framebuffer(new ScaledResolution(Minecraft.getMinecraft()).getScaledWidth(), new ScaledResolution(Minecraft.getMinecraft()).getScaledHeight(), true));
    }
}
