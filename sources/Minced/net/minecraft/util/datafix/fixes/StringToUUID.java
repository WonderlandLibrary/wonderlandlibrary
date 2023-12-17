// 
// Decompiled by Procyon v0.5.36
// 

package net.minecraft.util.datafix.fixes;

import java.util.UUID;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.datafix.IFixableData;

public class StringToUUID implements IFixableData
{
    @Override
    public int getFixVersion() {
        return 108;
    }
    
    @Override
    public NBTTagCompound fixTagCompound(final NBTTagCompound compound) {
        if (compound.hasKey("UUID", 8)) {
            compound.setUniqueId("UUID", UUID.fromString(compound.getString("UUID")));
        }
        return compound;
    }
}
