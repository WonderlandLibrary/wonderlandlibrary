// 
// Decompiled by Procyon v0.5.36
// 

package net.minecraft.entity.ai.attributes;

import javax.annotation.Nullable;
import java.util.UUID;
import java.util.Collection;

public interface IAttributeInstance
{
    IAttribute getAttribute();
    
    double getBaseValue();
    
    void setBaseValue(final double p0);
    
    Collection<AttributeModifier> getModifiersByOperation(final int p0);
    
    Collection<AttributeModifier> getModifiers();
    
    boolean hasModifier(final AttributeModifier p0);
    
    @Nullable
    AttributeModifier getModifier(final UUID p0);
    
    void applyModifier(final AttributeModifier p0);
    
    void removeModifier(final AttributeModifier p0);
    
    void removeModifier(final UUID p0);
    
    void removeAllModifiers();
    
    double getAttributeValue();
}
