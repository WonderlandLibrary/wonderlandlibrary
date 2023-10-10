// 
// Decompiled by Procyon v0.5.36
// 

package com.beust.jcommander.converters;

import com.beust.jcommander.ParameterException;

public class IntegerConverter extends BaseConverter<Integer>
{
    public IntegerConverter(final String s) {
        super(s);
    }
    
    @Override
    public Integer convert(final String s) {
        try {
            return Integer.parseInt(s);
        }
        catch (NumberFormatException ex) {
            throw new ParameterException(this.getErrorString(s, "an integer"));
        }
    }
}
