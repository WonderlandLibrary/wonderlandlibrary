package net.minecraft.block;

import net.minecraft.block.Block;

static final class Block.2
extends Block.SoundType {
    Block.2(String name, float volume, float frequency) {
        super(name, volume, frequency);
    }

    public String getBreakSound() {
        return "dig.wood";
    }
}
