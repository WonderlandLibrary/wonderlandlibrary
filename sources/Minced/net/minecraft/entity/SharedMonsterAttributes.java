// 
// Decompiled by Procyon v0.5.36
// 

package net.minecraft.entity;

import net.minecraft.entity.ai.attributes.RangedAttribute;
import org.apache.logging.log4j.LogManager;
import javax.annotation.Nullable;
import java.util.UUID;
import java.util.Collection;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.nbt.NBTTagCompound;
import java.util.Iterator;
import net.minecraft.nbt.NBTBase;
import net.minecraft.entity.ai.attributes.IAttributeInstance;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.entity.ai.attributes.AbstractAttributeMap;
import net.minecraft.entity.ai.attributes.IAttribute;
import org.apache.logging.log4j.Logger;

public class SharedMonsterAttributes
{
    private static final Logger LOGGER;
    public static final IAttribute MAX_HEALTH;
    public static final IAttribute FOLLOW_RANGE;
    public static final IAttribute KNOCKBACK_RESISTANCE;
    public static final IAttribute MOVEMENT_SPEED;
    public static final IAttribute FLYING_SPEED;
    public static final IAttribute ATTACK_DAMAGE;
    public static final IAttribute ATTACK_SPEED;
    public static final IAttribute ARMOR;
    public static final IAttribute ARMOR_TOUGHNESS;
    public static final IAttribute LUCK;
    
    public static NBTTagList writeBaseAttributeMapToNBT(final AbstractAttributeMap map) {
        final NBTTagList nbttaglist = new NBTTagList();
        for (final IAttributeInstance iattributeinstance : map.getAllAttributes()) {
            nbttaglist.appendTag(writeAttributeInstanceToNBT(iattributeinstance));
        }
        return nbttaglist;
    }
    
    private static NBTTagCompound writeAttributeInstanceToNBT(final IAttributeInstance instance) {
        final NBTTagCompound nbttagcompound = new NBTTagCompound();
        final IAttribute iattribute = instance.getAttribute();
        nbttagcompound.setString("Name", iattribute.getName());
        nbttagcompound.setDouble("Base", instance.getBaseValue());
        final Collection<AttributeModifier> collection = instance.getModifiers();
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
    
    public static NBTTagCompound writeAttributeModifierToNBT(final AttributeModifier modifier) {
        final NBTTagCompound nbttagcompound = new NBTTagCompound();
        nbttagcompound.setString("Name", modifier.getName());
        nbttagcompound.setDouble("Amount", modifier.getAmount());
        nbttagcompound.setInteger("Operation", modifier.getOperation());
        nbttagcompound.setUniqueId("UUID", modifier.getID());
        return nbttagcompound;
    }
    
    public static void setAttributeModifiers(final AbstractAttributeMap map, final NBTTagList list) {
        for (int i = 0; i < list.tagCount(); ++i) {
            final NBTTagCompound nbttagcompound = list.getCompoundTagAt(i);
            final IAttributeInstance iattributeinstance = map.getAttributeInstanceByName(nbttagcompound.getString("Name"));
            if (iattributeinstance == null) {
                SharedMonsterAttributes.LOGGER.warn("Ignoring unknown attribute '{}'", (Object)nbttagcompound.getString("Name"));
            }
            else {
                applyModifiersToAttributeInstance(iattributeinstance, nbttagcompound);
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
    
    @Nullable
    public static AttributeModifier readAttributeModifierFromNBT(final NBTTagCompound compound) {
        final UUID uuid = compound.getUniqueId("UUID");
        try {
            return new AttributeModifier(uuid, compound.getString("Name"), compound.getDouble("Amount"), compound.getInteger("Operation"));
        }
        catch (Exception exception) {
            SharedMonsterAttributes.LOGGER.warn("Unable to create attribute: {}", (Object)exception.getMessage());
            return null;
        }
    }
    
    static {
        LOGGER = LogManager.getLogger();
        MAX_HEALTH = new RangedAttribute(null, "generic.maxHealth", 20.0, 0.0, 1024.0).setDescription("Max Health").setShouldWatch(true);
        FOLLOW_RANGE = new RangedAttribute(null, "generic.followRange", 32.0, 0.0, 2048.0).setDescription("Follow Range");
        KNOCKBACK_RESISTANCE = new RangedAttribute(null, "generic.knockbackResistance", 0.0, 0.0, 1.0).setDescription("Knockback Resistance");
        MOVEMENT_SPEED = new RangedAttribute(null, "generic.movementSpeed", 0.699999988079071, 0.0, 1024.0).setDescription("Movement Speed").setShouldWatch(true);
        FLYING_SPEED = new RangedAttribute(null, "generic.flyingSpeed", 0.4000000059604645, 0.0, 1024.0).setDescription("Flying Speed").setShouldWatch(true);
        ATTACK_DAMAGE = new RangedAttribute(null, "generic.attackDamage", 2.0, 0.0, 2048.0);
        ATTACK_SPEED = new RangedAttribute(null, "generic.attackSpeed", 4.0, 0.0, 1024.0).setShouldWatch(true);
        ARMOR = new RangedAttribute(null, "generic.armor", 0.0, 0.0, 30.0).setShouldWatch(true);
        ARMOR_TOUGHNESS = new RangedAttribute(null, "generic.armorToughness", 0.0, 0.0, 20.0).setShouldWatch(true);
        LUCK = new RangedAttribute(null, "generic.luck", 0.0, -1024.0, 1024.0).setShouldWatch(true);
    }
}
