/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.tileentity.TileEntity
 */
package net.ccbluex.liquidbounce.injection.backend;

import net.ccbluex.liquidbounce.api.minecraft.tileentity.ITileEntity;
import net.ccbluex.liquidbounce.injection.backend.TileEntityImpl;
import net.minecraft.tileentity.TileEntity;

public final class TileEntityImplKt {
    public static final TileEntity unwrap(ITileEntity iTileEntity) {
        boolean bl = false;
        return ((TileEntityImpl)iTileEntity).getWrapped();
    }

    public static final ITileEntity wrap(TileEntity tileEntity) {
        boolean bl = false;
        return new TileEntityImpl(tileEntity);
    }
}

