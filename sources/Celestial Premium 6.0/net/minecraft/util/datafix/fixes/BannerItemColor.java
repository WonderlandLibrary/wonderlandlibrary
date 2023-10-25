/*
 * Decompiled with CFR 0.150.
 */
package net.minecraft.util.datafix.fixes;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.util.datafix.IFixableData;

public class BannerItemColor
implements IFixableData {
    @Override
    public int getFixVersion() {
        return 804;
    }

    @Override
    public NBTTagCompound fixTagCompound(NBTTagCompound compound) {
        NBTTagCompound nbttagcompound1;
        NBTTagCompound nbttagcompound;
        if ("minecraft:banner".equals(compound.getString("id")) && compound.hasKey("tag", 10) && (nbttagcompound = compound.getCompoundTag("tag")).hasKey("BlockEntityTag", 10) && (nbttagcompound1 = nbttagcompound.getCompoundTag("BlockEntityTag")).hasKey("Base", 99)) {
            NBTTagList nbttaglist;
            NBTTagCompound nbttagcompound2;
            compound.setShort("Damage", (short)(nbttagcompound1.getShort("Base") & 0xF));
            if (nbttagcompound.hasKey("display", 10) && (nbttagcompound2 = nbttagcompound.getCompoundTag("display")).hasKey("Lore", 9) && (nbttaglist = nbttagcompound2.getTagList("Lore", 8)).tagCount() == 1 && "(+NBT)".equals(nbttaglist.getStringTagAt(0))) {
                return compound;
            }
            nbttagcompound1.removeTag("Base");
            if (nbttagcompound1.hasNoTags()) {
                nbttagcompound.removeTag("BlockEntityTag");
            }
            if (nbttagcompound.hasNoTags()) {
                compound.removeTag("tag");
            }
        }
        return compound;
    }
}

