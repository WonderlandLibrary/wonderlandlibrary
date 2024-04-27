/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.block.state.IBlockState
 */
package net.ccbluex.liquidbounce.injection.backend;

import net.ccbluex.liquidbounce.api.minecraft.block.state.IIBlockState;
import net.ccbluex.liquidbounce.injection.backend.IBlockStateImpl;
import net.minecraft.block.state.IBlockState;

public final class IBlockStateImplKt {
    public static final IBlockState unwrap(IIBlockState iIBlockState) {
        boolean bl = false;
        return ((IBlockStateImpl)iIBlockState).getWrapped();
    }

    public static final IIBlockState wrap(IBlockState iBlockState) {
        boolean bl = false;
        return new IBlockStateImpl(iBlockState);
    }
}

