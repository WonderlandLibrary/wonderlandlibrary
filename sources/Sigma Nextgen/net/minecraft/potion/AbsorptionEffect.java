package net.minecraft.potion;

import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.ai.attributes.AttributeModifierManager;

public class AbsorptionEffect extends Effect
{
    protected AbsorptionEffect(EffectType type, int liquidColor)
    {
        super(type, liquidColor);
    }

    public void removeAttributesModifiersFromEntity(LivingEntity LivingEntityIn, AttributeModifierManager attributeMapIn, int amplifier)
    {
        LivingEntityIn.setAbsorptionAmount(LivingEntityIn.getAbsorptionAmount() - (float)(4 * (amplifier + 1)));
        super.removeAttributesModifiersFromEntity(LivingEntityIn, attributeMapIn, amplifier);
    }

    public void applyAttributesModifiersToEntity(LivingEntity LivingEntityIn, AttributeModifierManager attributeMapIn, int amplifier)
    {
        LivingEntityIn.setAbsorptionAmount(LivingEntityIn.getAbsorptionAmount() + (float)(4 * (amplifier + 1)));
        super.applyAttributesModifiersToEntity(LivingEntityIn, attributeMapIn, amplifier);
    }
}
