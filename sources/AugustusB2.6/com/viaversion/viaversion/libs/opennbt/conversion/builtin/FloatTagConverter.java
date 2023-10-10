// 
// Decompiled by Procyon v0.5.36
// 

package com.viaversion.viaversion.libs.opennbt.conversion.builtin;

import com.viaversion.viaversion.libs.opennbt.tag.builtin.Tag;
import com.viaversion.viaversion.libs.opennbt.tag.builtin.FloatTag;
import com.viaversion.viaversion.libs.opennbt.conversion.TagConverter;

public class FloatTagConverter implements TagConverter<FloatTag, Float>
{
    @Override
    public Float convert(final FloatTag tag) {
        return tag.getValue();
    }
    
    @Override
    public FloatTag convert(final Float value) {
        return new FloatTag(value);
    }
}
