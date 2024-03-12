// 
// Decompiled by Procyon v0.5.36
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
    
    public static NBTTagList writeBaseAttributeMapToNBT(final BaseAttributeMap map) {
        final NBTTagList nbttaglist = new NBTTagList();
        for (final IAttributeInstance iattributeinstance : map.getAllAttributes()) {
            nbttaglist.appendTag(writeAttributeInstanceToNBT(iattributeinstance));
        }
        return nbttaglist;
    }
    
    private static NBTTagCompound writeAttributeInstanceToNBT(final IAttributeInstance instance) {
        final NBTTagCompound nbttagcompound = new NBTTagCompound();
        final IAttribute iattribute = instance.getAttribute();
        nbttagcompound.setString("Name", iattribute.getAttributeUnlocalizedName());
        nbttagcompound.setDouble("Base", instance.getBaseValue());
        final Collection<AttributeModifier> collection = instance.func_111122_c();
        if (collection != null && !collection.isEmpty()) {
            final NBTTagList nbttaglist = new NBTTagList();
            for (final AttributeModifier attributemodifier : collection) {
                if (attributemodifier.isSaved()) {
                    nbttaglist.appendTag(writeAttributeModifierToNBT(attributemodifier));
                }
            }
            nbttagcompound.setTag("Modifiers", nbttaglist);
        }
        return nbttagcompound;
    }
    
    private static NBTTagCompound writeAttributeModifierToNBT(final AttributeModifier modifier) {
        final NBTTagCompound nbttagcompound = new NBTTagCompound();
        nbttagcompound.setString("Name", modifier.getName());
        nbttagcompound.setDouble("Amount", modifier.getAmount());
        nbttagcompound.setInteger("Operation", modifier.getOperation());
        nbttagcompound.setLong("UUIDMost", modifier.getID().getMostSignificantBits());
        nbttagcompound.setLong("UUIDLeast", modifier.getID().getLeastSignificantBits());
        return nbttagcompound;
    }
    
    public static void setAttributeModifiers(final BaseAttributeMap map, final NBTTagList list) {
        for (int i = 0; i < list.tagCount(); ++i) {
            final NBTTagCompound nbttagcompound = list.getCompoundTagAt(i);
            final IAttributeInstance iattributeinstance = map.getAttributeInstanceByName(nbttagcompound.getString("Name"));
            if (iattributeinstance != null) {
                applyModifiersToAttributeInstance(iattributeinstance, nbttagcompound);
            }
            else {
                SharedMonsterAttributes.logger.warn("Ignoring unknown attribute '" + nbttagcompound.getString("Name") + "'");
            }
        }
    }
    
    private static void applyModifiersToAttributeInstance(final IAttributeInstance instance, final NBTTagCompound compound) {
        instance.setBaseValue(compound.getDouble("Base"));
        if (compound.hasKey("Modifiers", 9)) {
            final NBTTagList nbttaglist = compound.getTagList("Modifiers", 10);
            for (int i = 0; i < nbttaglist.tagCount(); ++i) {
                final AttributeModifier attributemodifier = readAttributeModifierFromNBT(nbttaglist.getCompoundTagAt(i));
                if (attributemodifier != null) {
                    final AttributeModifier attributemodifier2 = instance.getModifier(attributemodifier.getID());
                    if (attributemodifier2 != null) {
                        instance.removeModifier(attributemodifier2);
                    }
                    instance.applyModifier(attributemodifier);
                }
            }
        }
    }
    
    public static AttributeModifier readAttributeModifierFromNBT(final NBTTagCompound compound) {
        final UUID uuid = new UUID(compound.getLong("UUIDMost"), compound.getLong("UUIDLeast"));
        try {
            return new AttributeModifier(uuid, compound.getString("Name"), compound.getDouble("Amount"), compound.getInteger("Operation"));
        }
        catch (Exception exception) {
            SharedMonsterAttributes.logger.warn("Unable to create attribute: " + exception.getMessage());
            return null;
        }
    }
    
    static {
        logger = LogManager.getLogger();
        maxHealth = new RangedAttribute(null, "generic.maxHealth", 20.0, 0.0, 1024.0).setDescription("Max Health").setShouldWatch(true);
        followRange = new RangedAttribute(null, "generic.followRange", 32.0, 0.0, 2048.0).setDescription("Follow Range");
        knockbackResistance = new RangedAttribute(null, "generic.knockbackResistance", 0.0, 0.0, 1.0).setDescription("Knockback Resistance");
        movementSpeed = new RangedAttribute(null, "generic.movementSpeed", 0.699999988079071, 0.0, 1024.0).setDescription("Movement Speed").setShouldWatch(true);
        attackDamage = new RangedAttribute(null, "generic.attackDamage", 2.0, 0.0, 2048.0);
    }
}
