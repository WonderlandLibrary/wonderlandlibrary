// 
// Decompiled by Procyon v0.5.36
// 

package com.viaversion.viaversion.libs.opennbt.conversion.builtin;

import com.viaversion.viaversion.libs.opennbt.tag.builtin.Tag;
import com.viaversion.viaversion.libs.opennbt.tag.builtin.ByteTag;
import com.viaversion.viaversion.libs.opennbt.conversion.TagConverter;

public class ByteTagConverter implements TagConverter<ByteTag, Byte>
{
    @Override
    public Byte convert(final ByteTag tag) {
        return tag.getValue();
    }
    
    @Override
    public ByteTag convert(final Byte value) {
        return new ByteTag(value);
    }
}
