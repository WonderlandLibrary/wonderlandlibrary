/*
 * Decompiled with CFR 0.150.
 */
package net.minecraft.entity.ai.attributes;

import java.util.Collection;
import java.util.UUID;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.entity.ai.attributes.IAttribute;

public interface IAttributeInstance {
    public IAttribute getAttribute();

    public double getBaseValue();

    public void setBaseValue(double var1);

    public Collection getModifiersByOperation(int var1);

    public Collection func_111122_c();

    public boolean func_180374_a(AttributeModifier var1);

    public AttributeModifier getModifier(UUID var1);

    public void applyModifier(AttributeModifier var1);

    public void removeModifier(AttributeModifier var1);

    public void removeAllModifiers();

    public double getAttributeValue();
}

