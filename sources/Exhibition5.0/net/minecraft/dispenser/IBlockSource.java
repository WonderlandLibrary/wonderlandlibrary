// 
// Decompiled by Procyon v0.5.30
// 

package net.minecraft.dispenser;

import net.minecraft.tileentity.TileEntity;
import net.minecraft.block.Block;
import net.minecraft.util.BlockPos;

public interface IBlockSource extends ILocatableSource
{
    double getX();
    
    double getY();
    
    double getZ();
    
    BlockPos getBlockPos();
    
    Block getBlock();
    
    int getBlockMetadata();
    
    TileEntity getBlockTileEntity();
}
