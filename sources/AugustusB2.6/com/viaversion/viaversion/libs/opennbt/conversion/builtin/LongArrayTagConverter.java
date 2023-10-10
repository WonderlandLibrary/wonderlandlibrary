// 
// Decompiled by Procyon v0.5.36
// 

package com.viaversion.viaversion.libs.opennbt.conversion.builtin;

import com.viaversion.viaversion.libs.opennbt.tag.builtin.Tag;
import com.viaversion.viaversion.libs.opennbt.tag.builtin.LongArrayTag;
import com.viaversion.viaversion.libs.opennbt.conversion.TagConverter;

public class LongArrayTagConverter implements TagConverter<LongArrayTag, long[]>
{
    @Override
    public long[] convert(final LongArrayTag tag) {
        return tag.getValue();
    }
    
    @Override
    public LongArrayTag convert(final long[] value) {
        return new LongArrayTag(value);
    }
}
