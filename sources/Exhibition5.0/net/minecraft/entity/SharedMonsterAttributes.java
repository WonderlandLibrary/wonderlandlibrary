// 
// Decompiled by Procyon v0.5.30
// 

package net.minecraft.entity;

import net.minecraft.entity.ai.attributes.RangedAttribute;
import org.apache.logging.log4j.LogManager;
import java.util.UUID;
import java.util.Collection;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.nbt.NBTTagCompound;
import java.util.Iterator;
import net.minecraft.nbt.NBTBase;
import net.minecraft.entity.ai.attributes.IAttributeInstance;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.entity.ai.attributes.BaseAttributeMap;
import net.minecraft.entity.ai.attributes.IAttribute;
import org.apache.logging.log4j.Logger;

public class SharedMonsterAttributes
{
    private static final Logger logger;
    public static final IAttribute maxHealth;
    public static final IAttribute followRange;
    public static final IAttribute knockbackResistance;
    public static final IAttribute movementSpeed;
    public static final IAttribute attackDamage;
    private static final String __OBFID = "CL_00001695";
    
    public static NBTTagList writeBaseAttributeMapToNBT(final BaseAttributeMap p_111257_0_) {
        final NBTTagList var1 = new NBTTagList();
        for (final IAttributeInstance var3 : p_111257_0_.getAllAttributes()) {
            var1.appendTag(writeAttributeInstanceToNBT(var3));
        }
        return var1;
    }
    
    private static NBTTagCompound writeAttributeInstanceToNBT(final IAttributeInstance p_111261_0_) {
        final NBTTagCompound var1 = new NBTTagCompound();
        final IAttribute var2 = p_111261_0_.getAttribute();
        var1.setString("Name", var2.getAttributeUnlocalizedName());
        var1.setDouble("Base", p_111261_0_.getBaseValue());
        final Collection var3 = p_111261_0_.func_111122_c();
        if (var3 != null && !var3.isEmpty()) {
            final NBTTagList var4 = new NBTTagList();
            for (final AttributeModifier var6 : var3) {
                if (var6.isSaved()) {
                    var4.appendTag(writeAttributeModifierToNBT(var6));
                }
            }
            var1.setTag("Modifiers", var4);
        }
        return var1;
    }
    
    private static NBTTagCompound writeAttributeModifierToNBT(final AttributeModifier p_111262_0_) {
        final NBTTagCompound var1 = new NBTTagCompound();
        var1.setString("Name", p_111262_0_.getName());
        var1.setDouble("Amount", p_111262_0_.getAmount());
        var1.setInteger("Operation", p_111262_0_.getOperation());
        var1.setLong("UUIDMost", p_111262_0_.getID().getMostSignificantBits());
        var1.setLong("UUIDLeast", p_111262_0_.getID().getLeastSignificantBits());
        return var1;
    }
    
    public static void func_151475_a(final BaseAttributeMap p_151475_0_, final NBTTagList p_151475_1_) {
        for (int var2 = 0; var2 < p_151475_1_.tagCount(); ++var2) {
            final NBTTagCompound var3 = p_151475_1_.getCompoundTagAt(var2);
            final IAttributeInstance var4 = p_151475_0_.getAttributeInstanceByName(var3.getString("Name"));
            if (var4 != null) {
                applyModifiersToAttributeInstance(var4, var3);
            }
            else {
                SharedMonsterAttributes.logger.warn("Ignoring unknown attribute '" + var3.getString("Name") + "'");
            }
        }
    }
    
    private static void applyModifiersToAttributeInstance(final IAttributeInstance p_111258_0_, final NBTTagCompound p_111258_1_) {
        p_111258_0_.setBaseValue(p_111258_1_.getDouble("Base"));
        if (p_111258_1_.hasKey("Modifiers", 9)) {
            final NBTTagList var2 = p_111258_1_.getTagList("Modifiers", 10);
            for (int var3 = 0; var3 < var2.tagCount(); ++var3) {
                final AttributeModifier var4 = readAttributeModifierFromNBT(var2.getCompoundTagAt(var3));
                if (var4 != null) {
                    final AttributeModifier var5 = p_111258_0_.getModifier(var4.getID());
                    if (var5 != null) {
                        p_111258_0_.removeModifier(var5);
                    }
                    p_111258_0_.applyModifier(var4);
                }
            }
        }
    }
    
    public static AttributeModifier readAttributeModifierFromNBT(final NBTTagCompound p_111259_0_) {
        final UUID var1 = new UUID(p_111259_0_.getLong("UUIDMost"), p_111259_0_.getLong("UUIDLeast"));
        try {
            return new AttributeModifier(var1, p_111259_0_.getString("Name"), p_111259_0_.getDouble("Amount"), p_111259_0_.getInteger("Operation"));
        }
        catch (Exception var2) {
            SharedMonsterAttributes.logger.warn("Unable to create attribute: " + var2.getMessage());
            return null;
        }
    }
    
    static {
        logger = LogManager.getLogger();
        maxHealth = new RangedAttribute(null, "generic.maxHealth", 20.0, 0.0, Double.MAX_VALUE).setDescription("Max Health").setShouldWatch(true);
        followRange = new RangedAttribute(null, "generic.followRange", 32.0, 0.0, 2048.0).setDescription("Follow Range");
        knockbackResistance = new RangedAttribute(null, "generic.knockbackResistance", 0.0, 0.0, 1.0).setDescription("Knockback Resistance");
        movementSpeed = new RangedAttribute(null, "generic.movementSpeed", 0.699999988079071, 0.0, Double.MAX_VALUE).setDescription("Movement Speed").setShouldWatch(true);
        attackDamage = new RangedAttribute(null, "generic.attackDamage", 2.0, 0.0, Double.MAX_VALUE);
    }
}
