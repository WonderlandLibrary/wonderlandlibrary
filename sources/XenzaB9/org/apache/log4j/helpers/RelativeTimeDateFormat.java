// 
// Decompiled by Procyon v0.6.0
// 

package org.apache.log4j.helpers;

import java.text.ParsePosition;
import java.text.FieldPosition;
import java.util.Date;
import java.text.DateFormat;

public class RelativeTimeDateFormat extends DateFormat
{
    private static final long serialVersionUID = 7055751607085611984L;
    protected final long startTime;
    
    public RelativeTimeDateFormat() {
        this.startTime = System.currentTimeMillis();
    }
    
    @Override
    public StringBuffer format(final Date date, final StringBuffer sbuf, final FieldPosition fieldPosition) {
        return sbuf.append(date.getTime() - this.startTime);
    }
    
    @Override
    public Date parse(final String s, final ParsePosition pos) {
        return null;
    }
}
