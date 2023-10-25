/*
 * Decompiled with CFR 0.152.
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
    public static final IAttribute knockbackResistance;
    public static final IAttribute attackDamage;
    public static final IAttribute followRange;
    public static final IAttribute movementSpeed;
    public static final IAttribute maxHealth;

    private static NBTTagCompound writeAttributeInstanceToNBT(IAttributeInstance iAttributeInstance) {
        NBTTagCompound nBTTagCompound = new NBTTagCompound();
        IAttribute iAttribute = iAttributeInstance.getAttribute();
        nBTTagCompound.setString("Name", iAttribute.getAttributeUnlocalizedName());
        nBTTagCompound.setDouble("Base", iAttributeInstance.getBaseValue());
        Collection<AttributeModifier> collection = iAttributeInstance.func_111122_c();
        if (collection != null && !collection.isEmpty()) {
            NBTTagList nBTTagList = new NBTTagList();
            for (AttributeModifier attributeModifier : collection) {
                if (!attributeModifier.isSaved()) continue;
                nBTTagList.appendTag(SharedMonsterAttributes.writeAttributeModifierToNBT(attributeModifier));
            }
            nBTTagCompound.setTag("Modifiers", nBTTagList);
        }
        return nBTTagCompound;
    }

    private static NBTTagCompound writeAttributeModifierToNBT(AttributeModifier attributeModifier) {
        NBTTagCompound nBTTagCompound = new NBTTagCompound();
        nBTTagCompound.setString("Name", attributeModifier.getName());
        nBTTagCompound.setDouble("Amount", attributeModifier.getAmount());
        nBTTagCompound.setInteger("Operation", attributeModifier.getOperation());
        nBTTagCompound.setLong("UUIDMost", attributeModifier.getID().getMostSignificantBits());
        nBTTagCompound.setLong("UUIDLeast", attributeModifier.getID().getLeastSignificantBits());
        return nBTTagCompound;
    }

    public static void func_151475_a(BaseAttributeMap baseAttributeMap, NBTTagList nBTTagList) {
        int n = 0;
        while (n < nBTTagList.tagCount()) {
            NBTTagCompound nBTTagCompound = nBTTagList.getCompoundTagAt(n);
            IAttributeInstance iAttributeInstance = baseAttributeMap.getAttributeInstanceByName(nBTTagCompound.getString("Name"));
            if (iAttributeInstance != null) {
                SharedMonsterAttributes.applyModifiersToAttributeInstance(iAttributeInstance, nBTTagCompound);
            } else {
                logger.warn("Ignoring unknown attribute '" + nBTTagCompound.getString("Name") + "'");
            }
            ++n;
        }
    }

    private static void applyModifiersToAttributeInstance(IAttributeInstance iAttributeInstance, NBTTagCompound nBTTagCompound) {
        iAttributeInstance.setBaseValue(nBTTagCompound.getDouble("Base"));
        if (nBTTagCompound.hasKey("Modifiers", 9)) {
            NBTTagList nBTTagList = nBTTagCompound.getTagList("Modifiers", 10);
            int n = 0;
            while (n < nBTTagList.tagCount()) {
                AttributeModifier attributeModifier = SharedMonsterAttributes.readAttributeModifierFromNBT(nBTTagList.getCompoundTagAt(n));
                if (attributeModifier != null) {
                    AttributeModifier attributeModifier2 = iAttributeInstance.getModifier(attributeModifier.getID());
                    if (attributeModifier2 != null) {
                        iAttributeInstance.removeModifier(attributeModifier2);
                    }
                    iAttributeInstance.applyModifier(attributeModifier);
                }
                ++n;
            }
        }
    }

    public static AttributeModifier readAttributeModifierFromNBT(NBTTagCompound nBTTagCompound) {
        UUID uUID = new UUID(nBTTagCompound.getLong("UUIDMost"), nBTTagCompound.getLong("UUIDLeast"));
        try {
            return new AttributeModifier(uUID, nBTTagCompound.getString("Name"), nBTTagCompound.getDouble("Amount"), nBTTagCompound.getInteger("Operation"));
        }
        catch (Exception exception) {
            logger.warn("Unable to create attribute: " + exception.getMessage());
            return null;
        }
    }

    public static NBTTagList writeBaseAttributeMapToNBT(BaseAttributeMap baseAttributeMap) {
        NBTTagList nBTTagList = new NBTTagList();
        for (IAttributeInstance iAttributeInstance : baseAttributeMap.getAllAttributes()) {
            nBTTagList.appendTag(SharedMonsterAttributes.writeAttributeInstanceToNBT(iAttributeInstance));
        }
        return nBTTagList;
    }

    static {
        maxHealth = new RangedAttribute(null, "generic.maxHealth", 20.0, 0.0, 1024.0).setDescription("Max Health").setShouldWatch(true);
        followRange = new RangedAttribute(null, "generic.followRange", 32.0, 0.0, 2048.0).setDescription("Follow Range");
        knockbackResistance = new RangedAttribute(null, "generic.knockbackResistance", 0.0, 0.0, 1.0).setDescription("Knockback Resistance");
        movementSpeed = new RangedAttribute(null, "generic.movementSpeed", 0.7f, 0.0, 1024.0).setDescription("Movement Speed").setShouldWatch(true);
        attackDamage = new RangedAttribute(null, "generic.attackDamage", 2.0, 0.0, 2048.0);
    }
}

