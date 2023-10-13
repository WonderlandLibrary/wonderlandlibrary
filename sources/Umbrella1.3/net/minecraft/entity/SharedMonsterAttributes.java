/*
 * Decompiled with CFR 0.150.
 * 
 * Could not load the following classes:
 *  org.apache.logging.log4j.LogManager
 *  org.apache.logging.log4j.Logger
 */
package net.minecraft.entity;

import java.util.Collection;
import java.util.UUID;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.entity.ai.attributes.BaseAttributeMap;
import net.minecraft.entity.ai.attributes.IAttribute;
import net.minecraft.entity.ai.attributes.IAttributeInstance;
import net.minecraft.entity.ai.attributes.RangedAttribute;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class SharedMonsterAttributes {
    private static final Logger logger = LogManager.getLogger();
    public static final IAttribute maxHealth = new RangedAttribute(null, "generic.maxHealth", 20.0, 0.0, Double.MAX_VALUE).setDescription("Max Health").setShouldWatch(true);
    public static final IAttribute followRange = new RangedAttribute(null, "generic.followRange", 32.0, 0.0, 2048.0).setDescription("Follow Range");
    public static final IAttribute knockbackResistance = new RangedAttribute(null, "generic.knockbackResistance", 0.0, 0.0, 1.0).setDescription("Knockback Resistance");
    public static final IAttribute movementSpeed = new RangedAttribute(null, "generic.movementSpeed", 0.7f, 0.0, Double.MAX_VALUE).setDescription("Movement Speed").setShouldWatch(true);
    public static final IAttribute attackDamage = new RangedAttribute(null, "generic.attackDamage", 2.0, 0.0, Double.MAX_VALUE);
    private static final String __OBFID = "CL_00001695";

    public static NBTTagList writeBaseAttributeMapToNBT(BaseAttributeMap p_111257_0_) {
        NBTTagList var1 = new NBTTagList();
        for (IAttributeInstance var3 : p_111257_0_.getAllAttributes()) {
            var1.appendTag(SharedMonsterAttributes.writeAttributeInstanceToNBT(var3));
        }
        return var1;
    }

    private static NBTTagCompound writeAttributeInstanceToNBT(IAttributeInstance p_111261_0_) {
        NBTTagCompound var1 = new NBTTagCompound();
        IAttribute var2 = p_111261_0_.getAttribute();
        var1.setString("Name", var2.getAttributeUnlocalizedName());
        var1.setDouble("Base", p_111261_0_.getBaseValue());
        Collection var3 = p_111261_0_.func_111122_c();
        if (var3 != null && !var3.isEmpty()) {
            NBTTagList var4 = new NBTTagList();
            for (AttributeModifier var6 : var3) {
                if (!var6.isSaved()) continue;
                var4.appendTag(SharedMonsterAttributes.writeAttributeModifierToNBT(var6));
            }
            var1.setTag("Modifiers", var4);
        }
        return var1;
    }

    private static NBTTagCompound writeAttributeModifierToNBT(AttributeModifier p_111262_0_) {
        NBTTagCompound var1 = new NBTTagCompound();
        var1.setString("Name", p_111262_0_.getName());
        var1.setDouble("Amount", p_111262_0_.getAmount());
        var1.setInteger("Operation", p_111262_0_.getOperation());
        var1.setLong("UUIDMost", p_111262_0_.getID().getMostSignificantBits());
        var1.setLong("UUIDLeast", p_111262_0_.getID().getLeastSignificantBits());
        return var1;
    }

    public static void func_151475_a(BaseAttributeMap p_151475_0_, NBTTagList p_151475_1_) {
        for (int var2 = 0; var2 < p_151475_1_.tagCount(); ++var2) {
            NBTTagCompound var3 = p_151475_1_.getCompoundTagAt(var2);
            IAttributeInstance var4 = p_151475_0_.getAttributeInstanceByName(var3.getString("Name"));
            if (var4 != null) {
                SharedMonsterAttributes.applyModifiersToAttributeInstance(var4, var3);
                continue;
            }
            logger.warn("Ignoring unknown attribute '" + var3.getString("Name") + "'");
        }
    }

    private static void applyModifiersToAttributeInstance(IAttributeInstance p_111258_0_, NBTTagCompound p_111258_1_) {
        p_111258_0_.setBaseValue(p_111258_1_.getDouble("Base"));
        if (p_111258_1_.hasKey("Modifiers", 9)) {
            NBTTagList var2 = p_111258_1_.getTagList("Modifiers", 10);
            for (int var3 = 0; var3 < var2.tagCount(); ++var3) {
                AttributeModifier var4 = SharedMonsterAttributes.readAttributeModifierFromNBT(var2.getCompoundTagAt(var3));
                if (var4 == null) continue;
                AttributeModifier var5 = p_111258_0_.getModifier(var4.getID());
                if (var5 != null) {
                    p_111258_0_.removeModifier(var5);
                }
                p_111258_0_.applyModifier(var4);
            }
        }
    }

    public static AttributeModifier readAttributeModifierFromNBT(NBTTagCompound p_111259_0_) {
        UUID var1 = new UUID(p_111259_0_.getLong("UUIDMost"), p_111259_0_.getLong("UUIDLeast"));
        try {
            return new AttributeModifier(var1, p_111259_0_.getString("Name"), p_111259_0_.getDouble("Amount"), p_111259_0_.getInteger("Operation"));
        }
        catch (Exception var3) {
            logger.warn("Unable to create attribute: " + var3.getMessage());
            return null;
        }
    }
}

