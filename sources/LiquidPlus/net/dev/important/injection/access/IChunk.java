/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.block.state.IBlockState
 *  net.minecraft.world.EnumSkyBlock
 */
package net.dev.important.injection.access;

import net.minecraft.block.state.IBlockState;
import net.minecraft.world.EnumSkyBlock;

public interface IChunk {
    public int getLightFor(EnumSkyBlock var1, int var2, int var3, int var4);

    public int getLightSubtracted(int var1, int var2, int var3, int var4);

    public boolean canSeeSky(int var1, int var2, int var3);

    public void setLightFor(EnumSkyBlock var1, int var2, int var3, int var4, int var5);

    public IBlockState getBlockState(int var1, int var2, int var3);
}

