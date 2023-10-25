/*
 * Decompiled with CFR 0.150.
 */
package net.minecraft.util.datafix.fixes;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.datafix.IFixableData;

public class HorseSplit
implements IFixableData {
    @Override
    public int getFixVersion() {
        return 703;
    }

    @Override
    public NBTTagCompound fixTagCompound(NBTTagCompound compound) {
        if ("EntityHorse".equals(compound.getString("id"))) {
            int i = compound.getInteger("Type");
            switch (i) {
                default: {
                    compound.setString("id", "Horse");
                    break;
                }
                case 1: {
                    compound.setString("id", "Donkey");
                    break;
                }
                case 2: {
                    compound.setString("id", "Mule");
                    break;
                }
                case 3: {
                    compound.setString("id", "ZombieHorse");
                    break;
                }
                case 4: {
                    compound.setString("id", "SkeletonHorse");
                }
            }
            compound.removeTag("Type");
        }
        return compound;
    }
}

