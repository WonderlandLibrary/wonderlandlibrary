// 
// Decompiled by Procyon v0.5.36
// 

package com.beust.jcommander.converters;

import com.beust.jcommander.ParameterException;

public class LongConverter extends BaseConverter<Long>
{
    public LongConverter(final String s) {
        super(s);
    }
    
    @Override
    public Long convert(final String s) {
        try {
            return Long.parseLong(s);
        }
        catch (NumberFormatException ex) {
            throw new ParameterException(this.getErrorString(s, "a long"));
        }
    }
}
