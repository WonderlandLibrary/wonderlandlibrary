// 
// Decompiled by Procyon v0.5.36
// 

package net.minecraft.entity.ai.attributes;

import javax.annotation.Nullable;

public interface IAttribute
{
    String getName();
    
    double clampValue(final double p0);
    
    double getDefaultValue();
    
    boolean getShouldWatch();
    
    @Nullable
    IAttribute getParent();
}
