package koks.api.manager.particle;

import koks.api.utils.Resolution;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;
import org.lwjgl.input.Mouse;

import java.util.ArrayList;

public class ParticleManager {
    private final ArrayList<Particle> particles = new ArrayList<>();

    public ParticleManager() {
        init();
    }

    public void init() {
        particles.clear();
        int particles1 = (int) (8000f / Math.sqrt(1920 * 1920 + 1080 * 1080) * Math.sqrt(Minecraft.getMinecraft().displayWidth * Minecraft.getMinecraft().displayWidth + Minecraft.getMinecraft().displayHeight * Minecraft.getMinecraft().displayHeight));
        for (int i = 0; i < particles1; i++) {
            particles.add(new Particle());
        }
    }

    public void draw(double scrollOffset, double scrollSpeed) {
        float gravity = -0.0001f;
        if (Mouse.isButtonDown(0)) gravity = 1;
        if (Mouse.isButtonDown(1)) gravity = -1;
        final Resolution resolution = Resolution.getResolution();
        int l = resolution.getWidth();
        int i1 = resolution.getHeight();
        final int mx = Mouse.getX() * l / Minecraft.getMinecraft().displayWidth;
        final int my = i1 - Mouse.getY() * i1 / Minecraft.getMinecraft().displayHeight - 1;
        GlStateManager.enableBlend();
        GlStateManager.disableTexture2D();
        GlStateManager.tryBlendFuncSeparate(770, 771, 1, 0);
        for (Particle particle : particles) {
            particle.update(resolution, gravity, mx, my, scrollOffset, scrollSpeed);
            particle.render(scrollOffset);
        }
        GlStateManager.enableTexture2D();
        GlStateManager.disableBlend();
    }

    public Particle getNearest(Particle particle) {
        double bestd = 1000000;
        Particle best = null;
        for (Particle particle1 : particles) {
            if (particle == particle1) continue;
            double x = particle.getX() - particle1.getX();
            double y = particle.getY() - particle1.getY();
            double dist = Math.sqrt(x * x + y * y);
            if (bestd > dist) {
                bestd = dist;
                best = particle1;
            }
            if (dist < 1) return particle1;
        }
        return best;
    }
}
