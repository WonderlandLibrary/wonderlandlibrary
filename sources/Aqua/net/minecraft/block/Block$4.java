package net.minecraft.block;

import net.minecraft.block.Block;

static final class Block.4
extends Block.SoundType {
    Block.4(String name, float volume, float frequency) {
        super(name, volume, frequency);
    }

    public String getBreakSound() {
        return "mob.slime.big";
    }

    public String getPlaceSound() {
        return "mob.slime.big";
    }

    public String getStepSound() {
        return "mob.slime.small";
    }
}
