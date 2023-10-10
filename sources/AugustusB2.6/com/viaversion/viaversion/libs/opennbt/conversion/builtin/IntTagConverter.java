// 
// Decompiled by Procyon v0.5.36
// 

package com.viaversion.viaversion.libs.opennbt.conversion.builtin;

import com.viaversion.viaversion.libs.opennbt.tag.builtin.Tag;
import com.viaversion.viaversion.libs.opennbt.tag.builtin.IntTag;
import com.viaversion.viaversion.libs.opennbt.conversion.TagConverter;

public class IntTagConverter implements TagConverter<IntTag, Integer>
{
    @Override
    public Integer convert(final IntTag tag) {
        return tag.getValue();
    }
    
    @Override
    public IntTag convert(final Integer value) {
        return new IntTag(value);
    }
}
