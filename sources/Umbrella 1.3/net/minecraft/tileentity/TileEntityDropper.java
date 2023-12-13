/*
 * Decompiled with CFR 0.150.
 */
package net.minecraft.tileentity;

import net.minecraft.tileentity.TileEntityDispenser;

public class TileEntityDropper
extends TileEntityDispenser {
    private static final String __OBFID = "CL_00000353";

    @Override
    public String getName() {
        return this.hasCustomName() ? this.field_146020_a : "container.dropper";
    }

    @Override
    public String getGuiID() {
        return "minecraft:dropper";
    }
}

