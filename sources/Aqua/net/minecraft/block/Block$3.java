package net.minecraft.block;

import net.minecraft.block.Block;

static final class Block.3
extends Block.SoundType {
    Block.3(String name, float volume, float frequency) {
        super(name, volume, frequency);
    }

    public String getBreakSound() {
        return "dig.stone";
    }

    public String getPlaceSound() {
        return "random.anvil_land";
    }
}
