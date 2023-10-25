package fun.expensive.client.feature.impl.movement;

import fun.rich.client.feature.Feature;
import fun.rich.client.feature.impl.FeatureCategory;

import java.util.concurrent.TimeUnit;

public class HighJump extends Feature {
    public HighJump() {
        super("HighJump","Подкидывает высоко вверх", FeatureCategory.Movement);
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
