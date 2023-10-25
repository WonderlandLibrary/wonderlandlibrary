/*
 * Decompiled with CFR 0.150.
 */
package net.minecraft.world.chunk;

import javax.annotation.Nullable;
import net.minecraft.block.state.IBlockState;
import net.minecraft.network.PacketBuffer;

public interface IBlockStatePalette {
    public int idFor(IBlockState var1);

    @Nullable
    public IBlockState getBlockState(int var1);

    public void read(PacketBuffer var1);

    public void write(PacketBuffer var1);

    public int getSerializedState();
}

