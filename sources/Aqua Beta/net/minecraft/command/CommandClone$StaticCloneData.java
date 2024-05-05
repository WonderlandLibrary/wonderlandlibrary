package net.minecraft.command;

import net.minecraft.block.state.IBlockState;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.BlockPos;

static class CommandClone.StaticCloneData {
    public final BlockPos pos;
    public final IBlockState blockState;
    public final NBTTagCompound compound;

    public CommandClone.StaticCloneData(BlockPos posIn, IBlockState stateIn, NBTTagCompound compoundIn) {
        this.pos = posIn;
        this.blockState = stateIn;
        this.compound = compoundIn;
    }
}
