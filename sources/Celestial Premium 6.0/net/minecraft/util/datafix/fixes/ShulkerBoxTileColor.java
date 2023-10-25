/*
 * Decompiled with CFR 0.150.
 */
package net.minecraft.util.datafix.fixes;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.datafix.IFixableData;

public class ShulkerBoxTileColor
implements IFixableData {
    @Override
    public int getFixVersion() {
        return 813;
    }

    @Override
    public NBTTagCompound fixTagCompound(NBTTagCompound compound) {
        if ("minecraft:shulker".equals(compound.getString("id"))) {
            compound.removeTag("Color");
        }
        return compound;
    }
}

