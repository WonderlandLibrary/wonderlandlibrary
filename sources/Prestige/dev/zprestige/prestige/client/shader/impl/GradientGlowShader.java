package dev.zprestige.prestige.client.shader.impl;

import dev.zprestige.prestige.client.Prestige;
import dev.zprestige.prestige.client.event.EventListener;
import dev.zprestige.prestige.client.event.impl.ResolutionChangeEvent;
import dev.zprestige.prestige.client.shader.GlProgram;
import java.awt.Color;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gl.Framebuffer;
import net.minecraft.client.gl.GlUniform;
import net.minecraft.client.gl.SimpleFramebuffer;
import net.minecraft.client.render.VertexFormats;
import net.minecraft.client.util.Window;
import net.minecraft.util.Identifier;
import org.lwjgl.opengl.GL30;

public class GradientGlowShader extends GlProgram {

    public GlUniform uSize;
    public GlUniform uLocation;
    public GlUniform softness;
    public GlUniform radius;
    public GlUniform color1;
    public GlUniform color2;
    public GlUniform color3;
    public GlUniform color4;
    public Framebuffer input;

    public GradientGlowShader() {
        super(new Identifier("prestige", "gradientglow"), VertexFormats.POSITION);
        Prestige.Companion.getEventBus().registerListener(this);
    }

    public void setParameters(float f, float f2, float f3, float f4, float f5, float f6, Color color, Color color2, Color color3, Color color4) {
        this.radius.set(f5 * 2.0f);
        this.uSize.set(f3 * 2.0f, f4 * 2.0f);
        this.softness.set(f6);
        this.uLocation.set(f * 2.0f, -f2 * 2.0f + (float)(MinecraftClient.getInstance().getWindow().getScaledHeight() * 2) - f4 * 2.0f);
        this.color1.set((float)color.getRed() / 255.0f, (float)color.getGreen() / 255.0f, (float)color.getBlue() / 255.0f, (float)color.getAlpha() / 255.0f);
        this.color2.set((float)color2.getRed() / 255.0f, (float)color2.getGreen() / 255.0f, (float)color2.getBlue() / 255.0f, (float)color2.getAlpha() / 255.0f);
        this.color3.set((float)color3.getRed() / 255.0f, (float)color3.getGreen() / 255.0f, (float)color3.getBlue() / 255.0f, (float)color3.getAlpha() / 255.0f);
        this.color4.set((float)color4.getRed() / 255.0f, (float)color4.getGreen() / 255.0f, (float)color4.getBlue() / 255.0f, (float)color4.getAlpha() / 255.0f);
    }

    @Override
    public void use() {
        Framebuffer framebuffer = MinecraftClient.getInstance().getFramebuffer();
        input.beginWrite(false);
        GL30.glBindFramebuffer(36008, framebuffer.fbo);
        GL30.glBlitFramebuffer(0, 0, framebuffer.textureWidth, framebuffer.textureHeight, 0, 0, framebuffer.textureWidth, framebuffer.textureHeight, 16384, 9729);
        framebuffer.beginWrite(false);
        super.use();
    }

    @EventListener
    public void event(ResolutionChangeEvent event) {
        if (input == null) {
            return;
        }
        input.resize(event.getWindow().getFramebufferWidth(), event.getWindow().getFramebufferHeight(), MinecraftClient.IS_SYSTEM_MAC);
    }

    protected void setup() {
        uSize = findUniform("uSize");
        uLocation = findUniform("uLocation");
        softness = findUniform("softness");
        radius = findUniform("radius");
        color1 = findUniform("color1");
        color2 = findUniform("color2");
        color3 = findUniform("color3");
        color4 = findUniform("color4");
        Window window = MinecraftClient.getInstance().getWindow();
        input = new SimpleFramebuffer(window.getFramebufferWidth(), window.getFramebufferHeight(), false, MinecraftClient.IS_SYSTEM_MAC);
    }
}
