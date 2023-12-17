// 
// Decompiled by Procyon v0.5.36
// 

package net.minecraft.util.datafix.fixes;

import net.minecraft.nbt.NBTTagList;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.datafix.IFixableData;

public class RedundantChanceTags implements IFixableData
{
    @Override
    public int getFixVersion() {
        return 113;
    }
    
    @Override
    public NBTTagCompound fixTagCompound(final NBTTagCompound compound) {
        if (compound.hasKey("HandDropChances", 9)) {
            final NBTTagList nbttaglist = compound.getTagList("HandDropChances", 5);
            if (nbttaglist.tagCount() == 2 && nbttaglist.getFloatAt(0) == 0.0f && nbttaglist.getFloatAt(1) == 0.0f) {
                compound.removeTag("HandDropChances");
            }
        }
        if (compound.hasKey("ArmorDropChances", 9)) {
            final NBTTagList nbttaglist2 = compound.getTagList("ArmorDropChances", 5);
            if (nbttaglist2.tagCount() == 4 && nbttaglist2.getFloatAt(0) == 0.0f && nbttaglist2.getFloatAt(1) == 0.0f && nbttaglist2.getFloatAt(2) == 0.0f && nbttaglist2.getFloatAt(3) == 0.0f) {
                compound.removeTag("ArmorDropChances");
            }
        }
        return compound;
    }
}
