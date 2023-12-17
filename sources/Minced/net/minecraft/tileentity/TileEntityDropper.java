// 
// Decompiled by Procyon v0.5.36
// 

package net.minecraft.tileentity;

import net.minecraft.util.datafix.IDataWalker;
import net.minecraft.util.datafix.walkers.ItemStackDataLists;
import net.minecraft.util.datafix.FixTypes;
import net.minecraft.util.datafix.DataFixer;

public class TileEntityDropper extends TileEntityDispenser
{
    public static void registerFixesDropper(final DataFixer fixer) {
        fixer.registerWalker(FixTypes.BLOCK_ENTITY, new ItemStackDataLists(TileEntityDropper.class, new String[] { "Items" }));
    }
    
    @Override
    public String getName() {
        return this.hasCustomName() ? this.customName : "container.dropper";
    }
    
    @Override
    public String getGuiID() {
        return "minecraft:dropper";
    }
}
