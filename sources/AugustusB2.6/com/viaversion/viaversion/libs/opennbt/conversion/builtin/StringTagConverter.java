// 
// Decompiled by Procyon v0.5.36
// 

package com.viaversion.viaversion.libs.opennbt.conversion.builtin;

import com.viaversion.viaversion.libs.opennbt.tag.builtin.Tag;
import com.viaversion.viaversion.libs.opennbt.tag.builtin.StringTag;
import com.viaversion.viaversion.libs.opennbt.conversion.TagConverter;

public class StringTagConverter implements TagConverter<StringTag, String>
{
    @Override
    public String convert(final StringTag tag) {
        return tag.getValue();
    }
    
    @Override
    public StringTag convert(final String value) {
        return new StringTag(value);
    }
}
