// 
// Decompiled by Procyon v0.5.36
// 

package com.beust.jcommander.converters;

import com.beust.jcommander.ParameterException;
import java.math.BigDecimal;

public class BigDecimalConverter extends BaseConverter<BigDecimal>
{
    public BigDecimalConverter(final String s) {
        super(s);
    }
    
    @Override
    public BigDecimal convert(final String val) {
        try {
            return new BigDecimal(val);
        }
        catch (NumberFormatException ex) {
            throw new ParameterException(this.getErrorString(val, "a BigDecimal"));
        }
    }
}
