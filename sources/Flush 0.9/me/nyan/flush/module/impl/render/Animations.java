package me.nyan.flush.module.impl.render;

import me.nyan.flush.module.Module;
import me.nyan.flush.module.settings.ModeSetting;
import me.nyan.flush.module.settings.NumberSetting;

public class Animations extends Module {
    public final ModeSetting blockAnimation = new ModeSetting("Block Animation", this, "1.7", "1.7", "1.8", "Down", "Flush", "Slide",
            "Exhibition", "Sigma", "Spin", "Tap", "Swing"),
            hitAnimation = new ModeSetting("Hit Animation", this, "Vanilla", "Vanilla", "Flush");
    public final NumberSetting itemsSize = new NumberSetting("Items Size", this, 35, 10, 100),
            itemsHeight = new NumberSetting("Items Height", this, 0, 0, 100),
            spinSpeed = new NumberSetting("Spin Speed", this, 0.5, 0.1, 2, 0.1,
                    () -> blockAnimation.is("spin")),
            handSwingSpeed = new NumberSetting("Swing Slowdown", this, 1, 0, 10);

    public Animations() {
        super("Animations", Category.RENDER);
    }

    @Override
    public String getSuffix() {
        return blockAnimation.getValue();
    }
}
