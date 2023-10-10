// 
// Decompiled by Procyon v0.5.36
// 

package com.viaversion.viaversion.libs.opennbt.conversion.builtin;

import java.util.Iterator;
import com.viaversion.viaversion.libs.opennbt.conversion.ConverterRegistry;
import com.viaversion.viaversion.libs.opennbt.tag.builtin.Tag;
import java.util.ArrayList;
import java.util.List;
import com.viaversion.viaversion.libs.opennbt.tag.builtin.ListTag;
import com.viaversion.viaversion.libs.opennbt.conversion.TagConverter;

public class ListTagConverter implements TagConverter<ListTag, List>
{
    @Override
    public List convert(final ListTag tag) {
        final List<Object> ret = new ArrayList<Object>();
        final List<? extends Tag> tags = tag.getValue();
        for (final Tag t : tags) {
            ret.add(ConverterRegistry.convertToValue(t));
        }
        return ret;
    }
    
    @Override
    public ListTag convert(final List value) {
        final List<Tag> tags = new ArrayList<Tag>();
        for (final Object o : value) {
            tags.add(ConverterRegistry.convertToTag(o));
        }
        return new ListTag(tags);
    }
}
