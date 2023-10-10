// 
// Decompiled by Procyon v0.5.36
// 

package com.beust.jcommander.converters;

import com.beust.jcommander.ParameterException;

public class BooleanConverter extends BaseConverter<Boolean>
{
    public BooleanConverter(final String s) {
        super(s);
    }
    
    @Override
    public Boolean convert(final String s) {
        if ("false".equalsIgnoreCase(s) || "true".equalsIgnoreCase(s)) {
            return Boolean.parseBoolean(s);
        }
        throw new ParameterException(this.getErrorString(s, "a boolean"));
    }
}
