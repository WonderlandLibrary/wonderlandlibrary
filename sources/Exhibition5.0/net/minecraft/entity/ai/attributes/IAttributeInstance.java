// 
// Decompiled by Procyon v0.5.30
// 

package net.minecraft.entity.ai.attributes;

import java.util.UUID;
import java.util.Collection;

public interface IAttributeInstance
{
    IAttribute getAttribute();
    
    double getBaseValue();
    
    void setBaseValue(final double p0);
    
    Collection getModifiersByOperation(final int p0);
    
    Collection func_111122_c();
    
    boolean func_180374_a(final AttributeModifier p0);
    
    AttributeModifier getModifier(final UUID p0);
    
    void applyModifier(final AttributeModifier p0);
    
    void removeModifier(final AttributeModifier p0);
    
    void removeAllModifiers();
    
    double getAttributeValue();
}
