package info.sigmaclient.sigma.gui;

import info.sigmaclient.sigma.utils.render.RenderUtils;
import net.minecraft.client.renderer.texture.TextureManager;

import java.awt.*;

public class SplashScreen {
    private final TextureManager renderManager;
    private final int steps;
    private int currentStep = 0;
    public boolean done = false;
    private float progress = 10;

    private SplashScreen(TextureManager renderManager, int steps) {
        this.renderManager = renderManager;
        this.steps = steps;
    }

    private void drawProgressBar() {
        // draw rectangle with rounded corners (circle)

        RenderUtils.drawRoundedRect(0, 0, 1000, 1000, 360, new Color(255, 255, 255, 255).getRGB());
    }

    public void done() {
        if (done) {
            return;
        }
        synchronized (this) {
            done = true;
        }
    }

    public static SplashScreen initSplashScreen(TextureManager renderManager, int steps) {
        return new SplashScreen(renderManager, steps);
    }
}
