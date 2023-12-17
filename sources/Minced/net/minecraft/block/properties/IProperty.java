// 
// Decompiled by Procyon v0.5.36
// 

package net.minecraft.block.properties;

import com.google.common.base.Optional;
import java.util.Collection;

public interface IProperty<T extends Comparable<T>>
{
    String getName();
    
    Collection<T> getAllowedValues();
    
    Class<T> getValueClass();
    
    Optional<T> parseValue(final String p0);
    
    String getName(final T p0);
}
