/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.world.IBlockAccess
 */
package net.dev.important.injection.access;

import net.minecraft.world.IBlockAccess;

public interface IBlock {
    public int getLightValue(IBlockAccess var1, int var2, int var3, int var4);

    public int getLightOpacity(IBlockAccess var1, int var2, int var3, int var4);
}

