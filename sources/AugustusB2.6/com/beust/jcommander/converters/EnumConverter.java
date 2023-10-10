// 
// Decompiled by Procyon v0.5.36
// 

package com.beust.jcommander.converters;

import com.beust.jcommander.ParameterException;
import java.util.EnumSet;
import com.beust.jcommander.IStringConverter;

public class EnumConverter<T extends Enum<T>> implements IStringConverter<T>
{
    private final String optionName;
    private final Class<T> clazz;
    
    public EnumConverter(final String optionName, final Class<T> clazz) {
        this.optionName = optionName;
        this.clazz = clazz;
    }
    
    @Override
    public T convert(final String name) {
        try {
            try {
                return Enum.valueOf(this.clazz, name);
            }
            catch (IllegalArgumentException ex) {
                return Enum.valueOf(this.clazz, name.toUpperCase());
            }
        }
        catch (Exception ex2) {
            throw new ParameterException("Invalid value for " + this.optionName + " parameter. Allowed values:" + EnumSet.allOf(this.clazz));
        }
    }
}
