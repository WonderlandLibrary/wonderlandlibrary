package net.minecraft.world;

import net.minecraft.world.border.WorldBorder;

class WorldProviderHell.1
extends WorldBorder {
    WorldProviderHell.1() {
    }

    public double getCenterX() {
        return super.getCenterX() / 8.0;
    }

    public double getCenterZ() {
        return super.getCenterZ() / 8.0;
    }
}
