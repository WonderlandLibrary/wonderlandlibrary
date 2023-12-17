// 
// Decompiled by Procyon v0.5.36
// 

package net.minecraft.util.datafix.fixes;

import net.minecraft.nbt.NBTTagFloat;
import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.datafix.IFixableData;

public class EntityArmorAndHeld implements IFixableData
{
    @Override
    public int getFixVersion() {
        return 100;
    }
    
    @Override
    public NBTTagCompound fixTagCompound(final NBTTagCompound compound) {
        final NBTTagList nbttaglist = compound.getTagList("Equipment", 10);
        if (!nbttaglist.isEmpty() && !compound.hasKey("HandItems", 10)) {
            final NBTTagList nbttaglist2 = new NBTTagList();
            nbttaglist2.appendTag(nbttaglist.get(0));
            nbttaglist2.appendTag(new NBTTagCompound());
            compound.setTag("HandItems", nbttaglist2);
        }
        if (nbttaglist.tagCount() > 1 && !compound.hasKey("ArmorItem", 10)) {
            final NBTTagList nbttaglist3 = new NBTTagList();
            nbttaglist3.appendTag(nbttaglist.getCompoundTagAt(1));
            nbttaglist3.appendTag(nbttaglist.getCompoundTagAt(2));
            nbttaglist3.appendTag(nbttaglist.getCompoundTagAt(3));
            nbttaglist3.appendTag(nbttaglist.getCompoundTagAt(4));
            compound.setTag("ArmorItems", nbttaglist3);
        }
        compound.removeTag("Equipment");
        if (compound.hasKey("DropChances", 9)) {
            final NBTTagList nbttaglist4 = compound.getTagList("DropChances", 5);
            if (!compound.hasKey("HandDropChances", 10)) {
                final NBTTagList nbttaglist5 = new NBTTagList();
                nbttaglist5.appendTag(new NBTTagFloat(nbttaglist4.getFloatAt(0)));
                nbttaglist5.appendTag(new NBTTagFloat(0.0f));
                compound.setTag("HandDropChances", nbttaglist5);
            }
            if (!compound.hasKey("ArmorDropChances", 10)) {
                final NBTTagList nbttaglist6 = new NBTTagList();
                nbttaglist6.appendTag(new NBTTagFloat(nbttaglist4.getFloatAt(1)));
                nbttaglist6.appendTag(new NBTTagFloat(nbttaglist4.getFloatAt(2)));
                nbttaglist6.appendTag(new NBTTagFloat(nbttaglist4.getFloatAt(3)));
                nbttaglist6.appendTag(new NBTTagFloat(nbttaglist4.getFloatAt(4)));
                compound.setTag("ArmorDropChances", nbttaglist6);
            }
            compound.removeTag("DropChances");
        }
        return compound;
    }
}
