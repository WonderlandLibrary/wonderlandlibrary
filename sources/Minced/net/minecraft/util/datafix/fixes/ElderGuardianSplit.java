// 
// Decompiled by Procyon v0.5.36
// 

package net.minecraft.util.datafix.fixes;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.datafix.IFixableData;

public class ElderGuardianSplit implements IFixableData
{
    @Override
    public int getFixVersion() {
        return 700;
    }
    
    @Override
    public NBTTagCompound fixTagCompound(final NBTTagCompound compound) {
        if ("Guardian".equals(compound.getString("id"))) {
            if (compound.getBoolean("Elder")) {
                compound.setString("id", "ElderGuardian");
            }
            compound.removeTag("Elder");
        }
        return compound;
    }
}
