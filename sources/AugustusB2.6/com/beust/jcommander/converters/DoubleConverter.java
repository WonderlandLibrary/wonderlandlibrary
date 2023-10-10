// 
// Decompiled by Procyon v0.5.36
// 

package com.beust.jcommander.converters;

import com.beust.jcommander.ParameterException;

public class DoubleConverter extends BaseConverter<Double>
{
    public DoubleConverter(final String s) {
        super(s);
    }
    
    @Override
    public Double convert(final String s) {
        try {
            return Double.parseDouble(s);
        }
        catch (NumberFormatException ex) {
            throw new ParameterException(this.getErrorString(s, "a double"));
        }
    }
}
