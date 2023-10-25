/*
 * Decompiled with CFR 0.150.
 */
package net.minecraft.block;

import javax.annotation.Nullable;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;

public interface ITileEntityProvider {
    @Nullable
    public TileEntity createNewTileEntity(World var1, int var2);
}

