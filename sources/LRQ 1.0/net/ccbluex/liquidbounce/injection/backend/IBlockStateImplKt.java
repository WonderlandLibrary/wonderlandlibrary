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
    public static final IBlockState unwrap(IIBlockState $this$unwrap) {
        int $i$f$unwrap = 0;
        return ((IBlockStateImpl)$this$unwrap).getWrapped();
    }

    public static final IIBlockState wrap(IBlockState $this$wrap) {
        int $i$f$wrap = 0;
        return new IBlockStateImpl($this$wrap);
    }
}

