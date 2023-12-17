// 
// Decompiled by Procyon v0.5.36
// 

package net.minecraft.potion;

import net.minecraft.entity.ai.attributes.AbstractAttributeMap;
import net.minecraft.entity.EntityLivingBase;

public class PotionAbsorption extends Potion
{
    protected PotionAbsorption(final boolean isBadEffectIn, final int liquidColorIn) {
        super(isBadEffectIn, liquidColorIn);
    }
    
    @Override
    public void removeAttributesModifiersFromEntity(final EntityLivingBase entityLivingBaseIn, final AbstractAttributeMap attributeMapIn, final int amplifier) {
        entityLivingBaseIn.setAbsorptionAmount(entityLivingBaseIn.getAbsorptionAmount() - 4 * (amplifier + 1));
        super.removeAttributesModifiersFromEntity(entityLivingBaseIn, attributeMapIn, amplifier);
    }
    
    @Override
    public void applyAttributesModifiersToEntity(final EntityLivingBase entityLivingBaseIn, final AbstractAttributeMap attributeMapIn, final int amplifier) {
        entityLivingBaseIn.setAbsorptionAmount(entityLivingBaseIn.getAbsorptionAmount() + 4 * (amplifier + 1));
        super.applyAttributesModifiersToEntity(entityLivingBaseIn, attributeMapIn, amplifier);
    }
}
