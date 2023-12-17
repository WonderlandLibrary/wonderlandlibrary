// 
// Decompiled by Procyon v0.5.36
// 

package net.minecraft.util.datafix.walkers;

import org.apache.logging.log4j.LogManager;
import net.minecraft.util.datafix.IFixType;
import net.minecraft.util.datafix.FixTypes;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.datafix.IDataFixer;
import org.apache.logging.log4j.Logger;
import net.minecraft.util.datafix.IDataWalker;

public class EntityTag implements IDataWalker
{
    private static final Logger LOGGER;
    
    @Override
    public NBTTagCompound process(final IDataFixer fixer, final NBTTagCompound compound, final int versionIn) {
        final NBTTagCompound nbttagcompound = compound.getCompoundTag("tag");
        if (nbttagcompound.hasKey("EntityTag", 10)) {
            final NBTTagCompound nbttagcompound2 = nbttagcompound.getCompoundTag("EntityTag");
            final String s = compound.getString("id");
            String s2;
            if ("minecraft:armor_stand".equals(s)) {
                s2 = ((versionIn < 515) ? "ArmorStand" : "minecraft:armor_stand");
            }
            else {
                if (!"minecraft:spawn_egg".equals(s)) {
                    return compound;
                }
                s2 = nbttagcompound2.getString("id");
            }
            boolean flag;
            if (s2 == null) {
                EntityTag.LOGGER.warn("Unable to resolve Entity for ItemInstance: {}", (Object)s);
                flag = false;
            }
            else {
                flag = !nbttagcompound2.hasKey("id", 8);
                nbttagcompound2.setString("id", s2);
            }
            fixer.process(FixTypes.ENTITY, nbttagcompound2, versionIn);
            if (flag) {
                nbttagcompound2.removeTag("id");
            }
        }
        return compound;
    }
    
    static {
        LOGGER = LogManager.getLogger();
    }
}
