package ru.smertnix.celestial.feature.impl.movement;

import java.util.concurrent.TimeUnit;

import ru.smertnix.celestial.feature.Feature;
import ru.smertnix.celestial.feature.impl.FeatureCategory;

public class HighJump extends Feature {
    public HighJump() {
        super("High Jump","Автоматически прыгает в небо", FeatureCategory.Movement);
    }
    @Override
    public void onEnable() {
        if (mc.player.onGround) {
            mc.player.jump();
        }
        new Thread(() -> {
            mc.player.motionY = 9f;
            try {
                TimeUnit.MILLISECONDS.sleep(200);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            mc.player.motionY = 8.742f;
            this.toggle();
        }).start();
    }
}
