/*
 * Decompiled with CFR 0.150.
 */
package net.minecraft.tileentity;

import net.minecraft.tileentity.TileEntityDispenser;
import net.minecraft.util.datafix.DataFixer;
import net.minecraft.util.datafix.FixTypes;
import net.minecraft.util.datafix.walkers.ItemStackDataLists;

public class TileEntityDropper
extends TileEntityDispenser {
    public static void registerFixesDropper(DataFixer fixer) {
        fixer.registerWalker(FixTypes.BLOCK_ENTITY, new ItemStackDataLists(TileEntityDropper.class, "Items"));
    }

    @Override
    public String getName() {
        return this.hasCustomName() ? this.field_190577_o : "container.dropper";
    }

    @Override
    public String getGuiID() {
        return "minecraft:dropper";
    }
}

