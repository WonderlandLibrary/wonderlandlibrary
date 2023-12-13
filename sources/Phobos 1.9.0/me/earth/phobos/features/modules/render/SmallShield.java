package me.earth.phobos.features.modules.render;

import me.earth.phobos.features.modules.Module;
import me.earth.phobos.features.setting.Setting;

public class SmallShield
        extends Module {
    private static SmallShield INSTANCE = new SmallShield();
    public Setting<Boolean> normalOffset = this.register(new Setting<Boolean>("OffNormal", false));
    public Setting<Float> offset = this.register(new Setting<Object>("Offset", Float.valueOf(0.7f), Float.valueOf(0.0f), Float.valueOf(1.0f), v -> this.normalOffset.getValue()));
    public Setting<Float> offX = this.register(new Setting<Object>("OffX", Float.valueOf(0.0f), Float.valueOf(-1.0f), Float.valueOf(1.0f), v -> this.normalOffset.getValue() == false));
    public Setting<Float> offY = this.register(new Setting<Object>("OffY", Float.valueOf(0.0f), Float.valueOf(-1.0f), Float.valueOf(1.0f), v -> this.normalOffset.getValue() == false));
    public Setting<Float> mainX = this.register(new Setting<Float>("MainX", Float.valueOf(0.0f), Float.valueOf(-1.0f), Float.valueOf(1.0f)));
    public Setting<Float> mainY = this.register(new Setting<Float>("MainY", Float.valueOf(0.0f), Float.valueOf(-1.0f), Float.valueOf(1.0f)));

    public SmallShield() {
        super("SmallShield", "Makes you offhand lower.", Module.Category.RENDER, false, false, false);
        this.setInstance();
    }

    public static SmallShield getINSTANCE() {
        if (INSTANCE == null) {
            INSTANCE = new SmallShield();
        }
        return INSTANCE;
    }

    private void setInstance() {
        INSTANCE = this;
    }

    @Override
    public void onUpdate() {
        if (this.normalOffset.getValue().booleanValue()) {
            SmallShield.mc.entityRenderer.itemRenderer.equippedProgressOffHand = this.offset.getValue().floatValue();
        }
    }
}

