// 
// Decompiled by Procyon v0.5.36
// 

package org.apache.logging.log4j.core.util.datetime;

import java.text.FieldPosition;
import java.util.Locale;
import java.util.TimeZone;
import java.util.Calendar;
import java.util.Date;

public interface DatePrinter
{
    String format(final long millis);
    
    String format(final Date date);
    
    String format(final Calendar calendar);
    
     <B extends Appendable> B format(final long millis, final B buf);
    
     <B extends Appendable> B format(final Date date, final B buf);
    
     <B extends Appendable> B format(final Calendar calendar, final B buf);
    
    String getPattern();
    
    TimeZone getTimeZone();
    
    Locale getLocale();
    
    StringBuilder format(final Object obj, final StringBuilder toAppendTo, final FieldPosition pos);
}
