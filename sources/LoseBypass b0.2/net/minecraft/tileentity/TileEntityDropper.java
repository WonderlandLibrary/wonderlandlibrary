/*
 * Decompiled with CFR 0.152.
 */
package net.minecraft.tileentity;

import net.minecraft.tileentity.TileEntityDispenser;

public class TileEntityDropper
extends TileEntityDispenser {
    @Override
    public String getName() {
        if (!this.hasCustomName()) return "container.dropper";
        String string = this.customName;
        return string;
    }

    @Override
    public String getGuiID() {
        return "minecraft:dropper";
    }
}

