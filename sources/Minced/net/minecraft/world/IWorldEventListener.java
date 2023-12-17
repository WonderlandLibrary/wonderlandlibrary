// 
// Decompiled by Procyon v0.5.36
// 

package net.minecraft.world;

import net.minecraft.entity.Entity;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvent;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.util.math.BlockPos;

public interface IWorldEventListener
{
    void notifyBlockUpdate(final World p0, final BlockPos p1, final IBlockState p2, final IBlockState p3, final int p4);
    
    void notifyLightSet(final BlockPos p0);
    
    void markBlockRangeForRenderUpdate(final int p0, final int p1, final int p2, final int p3, final int p4, final int p5);
    
    void playSoundToAllNearExcept(final EntityPlayer p0, final SoundEvent p1, final SoundCategory p2, final double p3, final double p4, final double p5, final float p6, final float p7);
    
    void playRecord(final SoundEvent p0, final BlockPos p1);
    
    void spawnParticle(final int p0, final boolean p1, final double p2, final double p3, final double p4, final double p5, final double p6, final double p7, final int... p8);
    
    void spawnParticle(final int p0, final boolean p1, final boolean p2, final double p3, final double p4, final double p5, final double p6, final double p7, final double p8, final int... p9);
    
    void onEntityAdded(final Entity p0);
    
    void onEntityRemoved(final Entity p0);
    
    void broadcastSound(final int p0, final BlockPos p1, final int p2);
    
    void playEvent(final EntityPlayer p0, final int p1, final BlockPos p2, final int p3);
    
    void sendBlockBreakProgress(final int p0, final BlockPos p1, final int p2);
}
