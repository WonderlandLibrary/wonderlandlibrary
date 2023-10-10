// 
// Decompiled by Procyon v0.5.36
// 

package com.beust.jcommander.converters;

import java.util.Iterator;
import com.beust.jcommander.internal.Lists;
import java.util.List;
import com.beust.jcommander.IStringConverter;

public class DefaultListConverter<T> implements IStringConverter<List<T>>
{
    private final IParameterSplitter splitter;
    private final IStringConverter<T> converter;
    
    public DefaultListConverter(final IParameterSplitter splitter, final IStringConverter<T> converter) {
        this.splitter = splitter;
        this.converter = converter;
    }
    
    @Override
    public List<T> convert(final String s) {
        final List<T> arrayList = Lists.newArrayList();
        final Iterator<String> iterator = this.splitter.split(s).iterator();
        while (iterator.hasNext()) {
            arrayList.add(this.converter.convert(iterator.next()));
        }
        return arrayList;
    }
}
