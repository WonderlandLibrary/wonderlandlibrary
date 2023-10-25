package net.minecraft.block;

import net.minecraft.block.Block;

static final class Block.1
extends Block.SoundType {
    Block.1(String name, float volume, float frequency) {
        super(name, volume, frequency);
    }

    public String getBreakSound() {
        return "dig.glass";
    }

    public String getPlaceSound() {
        return "step.stone";
    }
}
