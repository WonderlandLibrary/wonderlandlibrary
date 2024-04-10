package net.minecraft.potion;

import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.ai.attributes.AttributeModifierManager;

public class HealthBoostEffect extends Effect
{
    public HealthBoostEffect(EffectType type, int liquidColor)
    {
        super(type, liquidColor);
    }

    public void removeAttributesModifiersFromEntity(LivingEntity LivingEntityIn, AttributeModifierManager attributeMapIn, int amplifier)
    {
        super.removeAttributesModifiersFromEntity(LivingEntityIn, attributeMapIn, amplifier);

        if (LivingEntityIn.getHealth() > LivingEntityIn.getMaxHealth())
        {
            LivingEntityIn.setHealth(LivingEntityIn.getMaxHealth());
        }
    }
}
