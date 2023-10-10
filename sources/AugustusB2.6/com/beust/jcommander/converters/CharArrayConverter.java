// 
// Decompiled by Procyon v0.5.36
// 

package com.beust.jcommander.converters;

import com.beust.jcommander.IStringConverter;

public class CharArrayConverter implements IStringConverter<char[]>
{
    @Override
    public char[] convert(final String s) {
        return s.toCharArray();
    }
}
