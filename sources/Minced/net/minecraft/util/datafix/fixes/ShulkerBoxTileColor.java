// 
// Decompiled by Procyon v0.5.36
// 

package net.minecraft.util.datafix.fixes;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.datafix.IFixableData;

public class ShulkerBoxTileColor implements IFixableData
{
    @Override
    public int getFixVersion() {
        return 813;
    }
    
    @Override
    public NBTTagCompound fixTagCompound(final NBTTagCompound compound) {
        if ("minecraft:shulker".equals(compound.getString("id"))) {
            compound.removeTag("Color");
        }
        return compound;
    }
}
