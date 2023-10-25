/*
 * Decompiled with CFR 0.152.
 */
package net.minecraft.world;

import net.minecraft.util.IChatComponent;

public interface IWorldNameable {
    public boolean hasCustomName();

    public String getName();

    public IChatComponent getDisplayName();
}

