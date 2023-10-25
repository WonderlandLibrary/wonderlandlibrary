/*
 * Decompiled with CFR 0.150.
 */
package net.minecraft.util.datafix.fixes;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.util.datafix.IFixableData;

public class RedundantChanceTags
implements IFixableData {
    @Override
    public int getFixVersion() {
        return 113;
    }

    @Override
    public NBTTagCompound fixTagCompound(NBTTagCompound compound) {
        NBTTagList nbttaglist1;
        NBTTagList nbttaglist;
        if (compound.hasKey("HandDropChances", 9) && (nbttaglist = compound.getTagList("HandDropChances", 5)).tagCount() == 2 && nbttaglist.getFloatAt(0) == 0.0f && nbttaglist.getFloatAt(1) == 0.0f) {
            compound.removeTag("HandDropChances");
        }
        if (compound.hasKey("ArmorDropChances", 9) && (nbttaglist1 = compound.getTagList("ArmorDropChances", 5)).tagCount() == 4 && nbttaglist1.getFloatAt(0) == 0.0f && nbttaglist1.getFloatAt(1) == 0.0f && nbttaglist1.getFloatAt(2) == 0.0f && nbttaglist1.getFloatAt(3) == 0.0f) {
            compound.removeTag("ArmorDropChances");
        }
        return compound;
    }
}

