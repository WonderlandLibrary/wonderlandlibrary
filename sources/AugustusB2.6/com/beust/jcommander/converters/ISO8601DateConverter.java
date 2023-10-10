// 
// Decompiled by Procyon v0.5.36
// 

package com.beust.jcommander.converters;

import java.text.ParseException;
import com.beust.jcommander.ParameterException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class ISO8601DateConverter extends BaseConverter<Date>
{
    private static final SimpleDateFormat DATE_FORMAT;
    
    public ISO8601DateConverter(final String s) {
        super(s);
    }
    
    @Override
    public Date convert(final String source) {
        try {
            return ISO8601DateConverter.DATE_FORMAT.parse(source);
        }
        catch (ParseException ex) {
            throw new ParameterException(this.getErrorString(source, String.format("an ISO-8601 formatted date (%s)", ISO8601DateConverter.DATE_FORMAT.toPattern())));
        }
    }
    
    static {
        DATE_FORMAT = new SimpleDateFormat("yyyy-MM-dd");
    }
}
