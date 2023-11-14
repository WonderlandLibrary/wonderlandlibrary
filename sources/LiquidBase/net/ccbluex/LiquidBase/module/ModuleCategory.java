package net.ccbluex.LiquidBase.module;

/**
 * Project: LiquidBase
 * -----------------------------------------------------------
 * Copyright © 2017 | CCBlueX | All rights reserved.
 */
public enum ModuleCategory {

    PLAYER("Player"), COMBAT("Combat"), MOVEMENT("Movement"), WORLD("World"), MISC("Misc"), EXPLOIT("Exploit"), RENDER("Render");

    private String name;

    ModuleCategory(final String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }
}