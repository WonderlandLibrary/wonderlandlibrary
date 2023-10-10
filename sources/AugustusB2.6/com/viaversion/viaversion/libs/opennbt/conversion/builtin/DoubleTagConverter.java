// 
// Decompiled by Procyon v0.5.36
// 

package com.viaversion.viaversion.libs.opennbt.conversion.builtin;

import com.viaversion.viaversion.libs.opennbt.tag.builtin.Tag;
import com.viaversion.viaversion.libs.opennbt.tag.builtin.DoubleTag;
import com.viaversion.viaversion.libs.opennbt.conversion.TagConverter;

public class DoubleTagConverter implements TagConverter<DoubleTag, Double>
{
    @Override
    public Double convert(final DoubleTag tag) {
        return tag.getValue();
    }
    
    @Override
    public DoubleTag convert(final Double value) {
        return new DoubleTag(value);
    }
}
