package net.minecraft.world.gen.structure;

import java.util.Random;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;

public static abstract class StructureComponent.BlockSelector {
    protected IBlockState blockstate = Blocks.air.getDefaultState();

    public abstract void selectBlocks(Random var1, int var2, int var3, int var4, boolean var5);

    public IBlockState getBlockState() {
        return this.blockstate;
    }
}
