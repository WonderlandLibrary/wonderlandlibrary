package net.minecraft.block;

import net.minecraft.block.BlockPressurePlate;

/*
 * Exception performing whole class analysis ignored.
 */
static class BlockPressurePlate.1 {
    static final /* synthetic */ int[] $SwitchMap$net$minecraft$block$BlockPressurePlate$Sensitivity;

    static {
        $SwitchMap$net$minecraft$block$BlockPressurePlate$Sensitivity = new int[BlockPressurePlate.Sensitivity.values().length];
        try {
            BlockPressurePlate.1.$SwitchMap$net$minecraft$block$BlockPressurePlate$Sensitivity[BlockPressurePlate.Sensitivity.EVERYTHING.ordinal()] = 1;
        }
        catch (NoSuchFieldError noSuchFieldError) {
            // empty catch block
        }
        try {
            BlockPressurePlate.1.$SwitchMap$net$minecraft$block$BlockPressurePlate$Sensitivity[BlockPressurePlate.Sensitivity.MOBS.ordinal()] = 2;
        }
        catch (NoSuchFieldError noSuchFieldError) {
            // empty catch block
        }
    }
}
