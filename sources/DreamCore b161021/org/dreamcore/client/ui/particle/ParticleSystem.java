package org.dreamcore.client.ui.particle;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.GlStateManager;
import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.GL11;
import org.dreamcore.client.helpers.render.RenderHelper;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class ParticleSystem {

    public List<Particle> particles = new ArrayList<>();
    public float lastMouseX;
    public float lastMouseY;

    public void render() {
        GlStateManager.enableBlend();
        GlStateManager.disableAlpha();
        GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
        ScaledResolution sr = new ScaledResolution(Minecraft.getInstance());
        float xOffset = 0.0F;
        float yOffset = 0.0F;
        if (this.particles.size() < sr.getScaledWidth() / 8.0F) {
            this.particles.add(new Particle(sr, new Random().nextFloat() + 0.5F, new Random().nextFloat() * 5 + 5));
        }
        List<Particle> toRemove = new ArrayList<>();

        int maxOpacity = 52;
        int color = -570425345;
        for (Particle particle : this.particles) {
            double particleX = particle.x + Math.sin((particle.ticks / 2.0F)) * 50 + (-xOffset / 5F) * Mouse.getX();
            double particleY = (particle.ticks * particle.speed * particle.ticks / 10. + -yOffset / 2.0F);
            if (particleY < sr.getScaledHeight()) {

                if (particle.opacity < maxOpacity) {
                    particle.opacity += 0.5F;
                }
                if (particle.opacity > maxOpacity) {
                    particle.opacity = maxOpacity;
                }
                GlStateManager.enableBlend();
                drawBorderedCircle((float) particleX, (float) particleY, particle.radius * particle.opacity / maxOpacity, color);
            }
            particle.ticks = (float) (particle.ticks + 0.025);
            if (particleY > sr.getScaledHeight() / 4F || particleY < 0 || particleX > sr.getScaledWidth() || particleX < 0) {
                toRemove.add(particle);
            }
        }

        this.particles.removeAll(toRemove);
        GlStateManager.color(1.0F, 1.0F, 1.0F, 1);
        GlStateManager.enableBlend();
        GlStateManager.disableAlpha();
        this.lastMouseX = Mouse.getX();
        this.lastMouseY = Mouse.getY();
    }

    public void drawBorderedCircle(float x, float y, float radius, int insideC) {
        GL11.glDisable(GL11.GL_TEXTURE_2D);
        GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
        GL11.glEnable(GL11.GL_LINE_SMOOTH);
        GL11.glPushMatrix();
        GL11.glScalef(0.1F, 0.1F, 0.1F);
        RenderHelper.drawCircle(x * 10, y * 10, radius * 10, true, new Color(insideC));
        GL11.glScalef(10, 10, 10);
        GL11.glPopMatrix();
        GL11.glEnable(GL11.GL_TEXTURE_2D);
        GL11.glDisable(GL11.GL_LINE_SMOOTH);
    }
}