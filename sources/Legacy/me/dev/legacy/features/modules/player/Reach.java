package me.dev.legacy.features.modules.player;

import me.dev.legacy.features.modules.Module;
import me.dev.legacy.features.setting.Setting;

public class Reach
        extends Module {
    public Setting<Boolean> override = this.register(new Setting<Boolean>("Override", false));
    public Setting<Float> add = this.register(new Setting<Object>("Add", Float.valueOf(3.0f), v -> this.override.getValue() == false));
    public Setting<Float> reach = this.register(new Setting<Object>("Reach", Float.valueOf(6.0f), v -> this.override.getValue()));
    private static Reach INSTANCE = new Reach();

    public Reach() {
        super("Reach", "Extends your block reach", Module.Category.PLAYER, true, false, false);
        this.setInstance();
    }

    private void setInstance() {
        INSTANCE = this;
    }

    public static Reach getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new Reach();
        }
        return INSTANCE;
    }

    @Override
    public String getDisplayInfo() {
        return this.override.getValue() != false ? this.reach.getValue().toString() : this.add.getValue().toString();
    }
}
