/*
 * Decompiled with CFR 0.152.
 */
package net.minecraft.potion;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.ai.attributes.BaseAttributeMap;
import net.minecraft.potion.Potion;
import net.minecraft.util.ResourceLocation;

public class PotionAbsorption
extends Potion {
    protected PotionAbsorption(int n, ResourceLocation resourceLocation, boolean bl, int n2) {
        super(n, resourceLocation, bl, n2);
    }

    @Override
    public void applyAttributesModifiersToEntity(EntityLivingBase entityLivingBase, BaseAttributeMap baseAttributeMap, int n) {
        entityLivingBase.setAbsorptionAmount(entityLivingBase.getAbsorptionAmount() + (float)(4 * (n + 1)));
        super.applyAttributesModifiersToEntity(entityLivingBase, baseAttributeMap, n);
    }

    @Override
    public void removeAttributesModifiersFromEntity(EntityLivingBase entityLivingBase, BaseAttributeMap baseAttributeMap, int n) {
        entityLivingBase.setAbsorptionAmount(entityLivingBase.getAbsorptionAmount() - (float)(4 * (n + 1)));
        super.removeAttributesModifiersFromEntity(entityLivingBase, baseAttributeMap, n);
    }
}

