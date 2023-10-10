// 
// Decompiled by Procyon v0.5.36
// 

package com.beust.jcommander.converters;

import com.beust.jcommander.ParameterException;

public class FloatConverter extends BaseConverter<Float>
{
    public FloatConverter(final String s) {
        super(s);
    }
    
    @Override
    public Float convert(final String s) {
        try {
            return Float.parseFloat(s);
        }
        catch (NumberFormatException ex) {
            throw new ParameterException(this.getErrorString(s, "a float"));
        }
    }
}
