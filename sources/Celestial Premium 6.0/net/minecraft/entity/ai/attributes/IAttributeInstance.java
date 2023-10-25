/*
 * Decompiled with CFR 0.150.
 */
package net.minecraft.entity.ai.attributes;

import java.util.Collection;
import java.util.UUID;
import javax.annotation.Nullable;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.entity.ai.attributes.IAttribute;

public interface IAttributeInstance {
    public IAttribute getAttribute();

    public double getBaseValue();

    public void setBaseValue(double var1);

    public Collection<AttributeModifier> getModifiersByOperation(int var1);

    public Collection<AttributeModifier> getModifiers();

    public boolean hasModifier(AttributeModifier var1);

    @Nullable
    public AttributeModifier getModifier(UUID var1);

    public void applyModifier(AttributeModifier var1);

    public void removeModifier(AttributeModifier var1);

    public void removeModifier(UUID var1);

    public void removeAllModifiers();

    public double getAttributeValue();
}

