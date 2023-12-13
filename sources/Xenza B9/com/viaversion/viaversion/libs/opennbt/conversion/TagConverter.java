// 
// Decompiled by Procyon v0.6.0
// 

package com.viaversion.viaversion.libs.opennbt.conversion;

import com.viaversion.viaversion.libs.opennbt.tag.builtin.Tag;

public interface TagConverter<T extends Tag, V>
{
    V convert(final T p0);
    
    T convert(final V p0);
}
