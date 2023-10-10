// 
// Decompiled by Procyon v0.5.36
// 

package com.beust.jcommander.converters;

import com.beust.jcommander.IStringConverter;

public abstract class BaseConverter<T> implements IStringConverter<T>
{
    private String optionName;
    
    public BaseConverter(final String optionName) {
        this.optionName = optionName;
    }
    
    public String getOptionName() {
        return this.optionName;
    }
    
    protected String getErrorString(final String str, final String str2) {
        return "\"" + this.getOptionName() + "\": couldn't convert \"" + str + "\" to " + str2;
    }
}
