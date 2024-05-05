package net.minecraft.item;

import net.minecraft.block.BlockDirt;

static class ItemHoe.1 {
    static final /* synthetic */ int[] $SwitchMap$net$minecraft$block$BlockDirt$DirtType;

    static {
        $SwitchMap$net$minecraft$block$BlockDirt$DirtType = new int[BlockDirt.DirtType.values().length];
        try {
            ItemHoe.1.$SwitchMap$net$minecraft$block$BlockDirt$DirtType[BlockDirt.DirtType.DIRT.ordinal()] = 1;
        }
        catch (NoSuchFieldError noSuchFieldError) {
            // empty catch block
        }
        try {
            ItemHoe.1.$SwitchMap$net$minecraft$block$BlockDirt$DirtType[BlockDirt.DirtType.COARSE_DIRT.ordinal()] = 2;
        }
        catch (NoSuchFieldError noSuchFieldError) {
            // empty catch block
        }
    }
}
