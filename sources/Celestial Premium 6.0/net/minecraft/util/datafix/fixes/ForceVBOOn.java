/*
 * Decompiled with CFR 0.150.
 */
package net.minecraft.util.datafix.fixes;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.datafix.IFixableData;

public class ForceVBOOn
implements IFixableData {
    @Override
    public int getFixVersion() {
        return 505;
    }

    @Override
    public NBTTagCompound fixTagCompound(NBTTagCompound compound) {
        compound.setString("useVbo", "true");
        return compound;
    }
}

