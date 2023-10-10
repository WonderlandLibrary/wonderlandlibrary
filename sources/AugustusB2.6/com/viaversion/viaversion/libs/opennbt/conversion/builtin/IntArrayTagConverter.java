// 
// Decompiled by Procyon v0.5.36
// 

package com.viaversion.viaversion.libs.opennbt.conversion.builtin;

import com.viaversion.viaversion.libs.opennbt.tag.builtin.Tag;
import com.viaversion.viaversion.libs.opennbt.tag.builtin.IntArrayTag;
import com.viaversion.viaversion.libs.opennbt.conversion.TagConverter;

public class IntArrayTagConverter implements TagConverter<IntArrayTag, int[]>
{
    @Override
    public int[] convert(final IntArrayTag tag) {
        return tag.getValue();
    }
    
    @Override
    public IntArrayTag convert(final int[] value) {
        return new IntArrayTag(value);
    }
}
