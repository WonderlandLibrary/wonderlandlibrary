// 
// Decompiled by Procyon v0.5.36
// 

package net.minecraft.util.datafix.fixes;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.datafix.IFixableData;

public class SkeletonSplit implements IFixableData
{
    @Override
    public int getFixVersion() {
        return 701;
    }
    
    @Override
    public NBTTagCompound fixTagCompound(final NBTTagCompound compound) {
        final String s = compound.getString("id");
        if ("Skeleton".equals(s)) {
            final int i = compound.getInteger("SkeletonType");
            if (i == 1) {
                compound.setString("id", "WitherSkeleton");
            }
            else if (i == 2) {
                compound.setString("id", "Stray");
            }
            compound.removeTag("SkeletonType");
        }
        return compound;
    }
}
