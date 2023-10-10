// 
// Decompiled by Procyon v0.5.36
// 

package com.viaversion.viaversion.libs.opennbt.conversion.builtin;

import com.viaversion.viaversion.libs.opennbt.tag.builtin.Tag;
import com.viaversion.viaversion.libs.opennbt.tag.builtin.ByteArrayTag;
import com.viaversion.viaversion.libs.opennbt.conversion.TagConverter;

public class ByteArrayTagConverter implements TagConverter<ByteArrayTag, byte[]>
{
    @Override
    public byte[] convert(final ByteArrayTag tag) {
        return tag.getValue();
    }
    
    @Override
    public ByteArrayTag convert(final byte[] value) {
        return new ByteArrayTag(value);
    }
}
