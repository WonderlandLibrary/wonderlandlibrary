// 
// Decompiled by Procyon v0.5.36
// 

package net.minecraft.util.datafix.fixes;

import net.minecraft.nbt.NBTTagList;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.datafix.IFixableData;

public class BannerItemColor implements IFixableData
{
    @Override
    public int getFixVersion() {
        return 804;
    }
    
    @Override
    public NBTTagCompound fixTagCompound(final NBTTagCompound compound) {
        if ("minecraft:banner".equals(compound.getString("id")) && compound.hasKey("tag", 10)) {
            final NBTTagCompound nbttagcompound = compound.getCompoundTag("tag");
            if (nbttagcompound.hasKey("BlockEntityTag", 10)) {
                final NBTTagCompound nbttagcompound2 = nbttagcompound.getCompoundTag("BlockEntityTag");
                if (nbttagcompound2.hasKey("Base", 99)) {
                    compound.setShort("Damage", (short)(nbttagcompound2.getShort("Base") & 0xF));
                    if (nbttagcompound.hasKey("display", 10)) {
                        final NBTTagCompound nbttagcompound3 = nbttagcompound.getCompoundTag("display");
                        if (nbttagcompound3.hasKey("Lore", 9)) {
                            final NBTTagList nbttaglist = nbttagcompound3.getTagList("Lore", 8);
                            if (nbttaglist.tagCount() == 1 && "(+NBT)".equals(nbttaglist.getStringTagAt(0))) {
                                return compound;
                            }
                        }
                    }
                    nbttagcompound2.removeTag("Base");
                    if (nbttagcompound2.isEmpty()) {
                        nbttagcompound.removeTag("BlockEntityTag");
                    }
                    if (nbttagcompound.isEmpty()) {
                        compound.removeTag("tag");
                    }
                }
            }
        }
        return compound;
    }
}
