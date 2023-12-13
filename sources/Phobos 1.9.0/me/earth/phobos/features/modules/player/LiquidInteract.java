package me.earth.phobos.features.modules.player;

import me.earth.phobos.features.modules.Module;

public class LiquidInteract
        extends Module {
    private static LiquidInteract INSTANCE = new LiquidInteract();

    public LiquidInteract() {
        super("LiquidInteract", "Interact with liquids", Module.Category.PLAYER, false, false, false);
        this.setInstance();
    }

    public static LiquidInteract getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new LiquidInteract();
        }
        return INSTANCE;
    }

    private void setInstance() {
        INSTANCE = this;
    }
}

