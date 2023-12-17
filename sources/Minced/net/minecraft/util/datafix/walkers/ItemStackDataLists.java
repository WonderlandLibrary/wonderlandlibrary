// 
// Decompiled by Procyon v0.5.36
// 

package net.minecraft.util.datafix.walkers;

import net.minecraft.util.datafix.DataFixesManager;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.datafix.IDataFixer;

public class ItemStackDataLists extends Filtered
{
    private final String[] matchingTags;
    
    public ItemStackDataLists(final Class<?> p_i47310_1_, final String... matchingTagsIn) {
        super(p_i47310_1_);
        this.matchingTags = matchingTagsIn;
    }
    
    @Override
    NBTTagCompound filteredProcess(final IDataFixer fixer, NBTTagCompound compound, final int versionIn) {
        for (final String s : this.matchingTags) {
            compound = DataFixesManager.processInventory(fixer, compound, versionIn, s);
        }
        return compound;
    }
}
