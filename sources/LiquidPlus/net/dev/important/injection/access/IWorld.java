/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.block.state.IBlockState
 *  net.minecraft.world.EnumSkyBlock
 *  net.minecraft.world.chunk.Chunk
 */
package net.dev.important.injection.access;

import net.minecraft.block.state.IBlockState;
import net.minecraft.world.EnumSkyBlock;
import net.minecraft.world.chunk.Chunk;

public interface IWorld {
    public boolean isAreaLoaded(int var1, int var2, int var3, int var4, boolean var5);

    public boolean isBlockLoaded(int var1, int var2, int var3);

    public boolean isBlockLoaded(int var1, int var2, int var3, boolean var4);

    public boolean isValid(int var1, int var2, int var3);

    public boolean canSeeSky(int var1, int var2, int var3);

    public int getCombinedLight(int var1, int var2, int var3, int var4);

    public int getRawLight(int var1, int var2, int var3, EnumSkyBlock var4);

    public int getLight(int var1, int var2, int var3, boolean var4);

    public int getLightFor(EnumSkyBlock var1, int var2, int var3, int var4);

    public int getLightFromNeighbors(int var1, int var2, int var3);

    public int getLightFromNeighborsFor(EnumSkyBlock var1, int var2, int var3, int var4);

    public void setLightFor(EnumSkyBlock var1, int var2, int var3, int var4, int var5);

    public boolean checkLight(int var1, int var2, int var3);

    public boolean checkLightFor(EnumSkyBlock var1, int var2, int var3, int var4);

    public float getLightBrightness(int var1, int var2, int var3);

    public IBlockState getBlockState(int var1, int var2, int var3);

    public boolean setBlockState(int var1, int var2, int var3, IBlockState var4, int var5);

    public void markBlockForUpdate(int var1, int var2, int var3);

    public void markAndNotifyBlock(int var1, int var2, int var3, Chunk var4, IBlockState var5, IBlockState var6, int var7);

    public void notifyLightSet(int var1, int var2, int var3);

    public Chunk getChunkFromBlockCoords(int var1, int var2, int var3);
}

