// 
// Decompiled by Procyon v0.5.36
// 

package net.minecraft.util.datafix.fixes;

import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.datafix.IFixableData;

public class HorseSaddle implements IFixableData
{
    @Override
    public int getFixVersion() {
        return 110;
    }
    
    @Override
    public NBTTagCompound fixTagCompound(final NBTTagCompound compound) {
        if ("EntityHorse".equals(compound.getString("id")) && !compound.hasKey("SaddleItem", 10) && compound.getBoolean("Saddle")) {
            final NBTTagCompound nbttagcompound = new NBTTagCompound();
            nbttagcompound.setString("id", "minecraft:saddle");
            nbttagcompound.setByte("Count", (byte)1);
            nbttagcompound.setShort("Damage", (short)0);
            compound.setTag("SaddleItem", nbttagcompound);
            compound.removeTag("Saddle");
        }
        return compound;
    }
}
