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
    public static final TileEntity unwrap(ITileEntity $this$unwrap) {
        int $i$f$unwrap = 0;
        return ((TileEntityImpl)$this$unwrap).getWrapped();
    }

    public static final ITileEntity wrap(TileEntity $this$wrap) {
        int $i$f$wrap = 0;
        return new TileEntityImpl($this$wrap);
    }
}

