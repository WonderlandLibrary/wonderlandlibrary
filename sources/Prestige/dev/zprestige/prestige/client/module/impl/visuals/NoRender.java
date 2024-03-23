package dev.zprestige.prestige.client.module.impl.visuals;

import dev.zprestige.prestige.client.event.EventListener;
import dev.zprestige.prestige.client.event.impl.BackgroundEvent;
import dev.zprestige.prestige.client.event.impl.FireOverlayEvent;
import dev.zprestige.prestige.client.event.impl.LightEvent;
import dev.zprestige.prestige.client.event.impl.ParticleEvent;
import dev.zprestige.prestige.client.event.impl.TiltEvent;
import dev.zprestige.prestige.client.module.Category;
import dev.zprestige.prestige.client.module.Module;
import dev.zprestige.prestige.client.setting.impl.BooleanSetting;

public class NoRender
extends Module {
    public BooleanSetting hurtcam;
    public BooleanSetting fire;
    public BooleanSetting particles;
    public BooleanSetting background;
    public BooleanSetting fullbright;

    public NoRender() {
        super("No Render", Category.Visual, "Removes certain visuals from your screen");
        hurtcam = setting("Hurt Cam", false).description("Removes the hurt cam effect when you take damage");
        fire = setting("Fire", false).description("Removes fire from your screen when you are on fire");
        particles = setting("Particles", false).description("Removes all particles");
        background = setting("Background Tint", false).description("Removes the dark background tint behind screens");
        fullbright = setting("Full Bright", false).description("View in the dark :O");
    }

    @EventListener
    public void event(BackgroundEvent event) {
        if (background.getObject() && getMc().world != null) {
            event.setCancelled();
        }
    }

    @EventListener
    public void event(FireOverlayEvent event) {
        if (fire.getObject()) {
            event.setCancelled();
        }
    }

    @EventListener
    public void event(LightEvent event) {
        if (fullbright.getObject()) {
            event.setCancelled();
        }
    }

    @EventListener
    public void event(ParticleEvent event) {
        if (particles.getObject()) {
            event.setCancelled();
        }
    }

    @EventListener
    public void event(TiltEvent event) {
        if (hurtcam.getObject()) {
            event.setCancelled();
        }
    }
}