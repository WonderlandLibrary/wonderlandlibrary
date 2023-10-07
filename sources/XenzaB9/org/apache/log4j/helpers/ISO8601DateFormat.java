// 
// Decompiled by Procyon v0.6.0
// 

package org.apache.log4j.helpers;

import java.text.ParsePosition;
import java.text.FieldPosition;
import java.util.Date;
import java.util.TimeZone;

public class ISO8601DateFormat extends AbsoluteTimeDateFormat
{
    private static final long serialVersionUID = -759840745298755296L;
    private static long lastTime;
    private static char[] lastTimeString;
    
    public ISO8601DateFormat() {
    }
    
    public ISO8601DateFormat(final TimeZone timeZone) {
        super(timeZone);
    }
    
    @Override
    public StringBuffer format(final Date date, final StringBuffer sbuf, final FieldPosition fieldPosition) {
        final long now = date.getTime();
        final int millis = (int)(now % 1000L);
        if (now - millis != ISO8601DateFormat.lastTime || ISO8601DateFormat.lastTimeString[0] == '\0') {
            this.calendar.setTime(date);
            final int start = sbuf.length();
            final int year = this.calendar.get(1);
            sbuf.append(year);
            String month = null;
            switch (this.calendar.get(2)) {
                case 0: {
                    month = "-01-";
                    break;
                }
                case 1: {
                    month = "-02-";
                    break;
                }
                case 2: {
                    month = "-03-";
                    break;
                }
                case 3: {
                    month = "-04-";
                    break;
                }
                case 4: {
                    month = "-05-";
                    break;
                }
                case 5: {
                    month = "-06-";
                    break;
                }
                case 6: {
                    month = "-07-";
                    break;
                }
                case 7: {
                    month = "-08-";
                    break;
                }
                case 8: {
                    month = "-09-";
                    break;
                }
                case 9: {
                    month = "-10-";
                    break;
                }
                case 10: {
                    month = "-11-";
                    break;
                }
                case 11: {
                    month = "-12-";
                    break;
                }
                default: {
                    month = "-NA-";
                    break;
                }
            }
            sbuf.append(month);
            final int day = this.calendar.get(5);
            if (day < 10) {
                sbuf.append('0');
            }
            sbuf.append(day);
            sbuf.append(' ');
            final int hour = this.calendar.get(11);
            if (hour < 10) {
                sbuf.append('0');
            }
            sbuf.append(hour);
            sbuf.append(':');
            final int mins = this.calendar.get(12);
            if (mins < 10) {
                sbuf.append('0');
            }
            sbuf.append(mins);
            sbuf.append(':');
            final int secs = this.calendar.get(13);
            if (secs < 10) {
                sbuf.append('0');
            }
            sbuf.append(secs);
            sbuf.append(',');
            sbuf.getChars(start, sbuf.length(), ISO8601DateFormat.lastTimeString, 0);
            ISO8601DateFormat.lastTime = now - millis;
        }
        else {
            sbuf.append(ISO8601DateFormat.lastTimeString);
        }
        if (millis < 100) {
            sbuf.append('0');
        }
        if (millis < 10) {
            sbuf.append('0');
        }
        sbuf.append(millis);
        return sbuf;
    }
    
    @Override
    public Date parse(final String s, final ParsePosition pos) {
        return null;
    }
    
    static {
        ISO8601DateFormat.lastTimeString = new char[20];
    }
}
