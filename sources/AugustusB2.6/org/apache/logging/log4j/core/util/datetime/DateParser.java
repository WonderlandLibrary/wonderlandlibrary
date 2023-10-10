// 
// Decompiled by Procyon v0.5.36
// 

package org.apache.logging.log4j.core.util.datetime;

import java.util.Locale;
import java.util.TimeZone;
import java.util.Calendar;
import java.text.ParsePosition;
import java.text.ParseException;
import java.util.Date;

public interface DateParser
{
    Date parse(final String source) throws ParseException;
    
    Date parse(final String source, final ParsePosition pos);
    
    boolean parse(final String source, final ParsePosition pos, final Calendar calendar);
    
    String getPattern();
    
    TimeZone getTimeZone();
    
    Locale getLocale();
    
    Object parseObject(final String source) throws ParseException;
    
    Object parseObject(final String source, final ParsePosition pos);
}
