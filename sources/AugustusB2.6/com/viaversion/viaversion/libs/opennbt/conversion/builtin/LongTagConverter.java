// 
// Decompiled by Procyon v0.5.36
// 

package com.viaversion.viaversion.libs.opennbt.conversion.builtin;

import com.viaversion.viaversion.libs.opennbt.tag.builtin.Tag;
import com.viaversion.viaversion.libs.opennbt.tag.builtin.LongTag;
import com.viaversion.viaversion.libs.opennbt.conversion.TagConverter;

public class LongTagConverter implements TagConverter<LongTag, Long>
{
    @Override
    public Long convert(final LongTag tag) {
        return tag.getValue();
    }
    
    @Override
    public LongTag convert(final Long value) {
        return new LongTag(value);
    }
}
