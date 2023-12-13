// 
// Decompiled by Procyon v0.5.30
// 

package net.minecraft.block.properties;

import java.util.Collection;

public interface IProperty
{
    String getName();
    
    Collection getAllowedValues();
    
    Class getValueClass();
    
    String getName(final Comparable p0);
}
