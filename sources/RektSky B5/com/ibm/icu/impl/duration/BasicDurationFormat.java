/*
 * Decompiled with CFR 0.152.
 */
package com.ibm.icu.impl.duration;

import com.ibm.icu.impl.duration.BasicPeriodFormatterService;
import com.ibm.icu.impl.duration.DurationFormatter;
import com.ibm.icu.impl.duration.Period;
import com.ibm.icu.impl.duration.PeriodFormatter;
import com.ibm.icu.impl.duration.PeriodFormatterService;
import com.ibm.icu.impl.duration.TimeUnit;
import com.ibm.icu.text.DurationFormat;
import com.ibm.icu.util.ULocale;
import java.text.FieldPosition;
import java.util.Date;
import javax.xml.datatype.DatatypeConstants;
import javax.xml.datatype.Duration;

public class BasicDurationFormat
extends DurationFormat {
    private static final long serialVersionUID = -3146984141909457700L;
    transient DurationFormatter formatter;
    transient PeriodFormatter pformatter;
    transient PeriodFormatterService pfs = BasicPeriodFormatterService.getInstance();

    public static BasicDurationFormat getInstance(ULocale locale) {
        return new BasicDurationFormat(locale);
    }

    @Override
    public StringBuffer format(Object object, StringBuffer toAppend, FieldPosition pos) {
        if (object instanceof Long) {
            String res = this.formatDurationFromNow((Long)object);
            return toAppend.append(res);
        }
        if (object instanceof Date) {
            String res = this.formatDurationFromNowTo((Date)object);
            return toAppend.append(res);
        }
        if (object instanceof Duration) {
            String res = this.formatDuration(object);
            return toAppend.append(res);
        }
        throw new IllegalArgumentException("Cannot format given Object as a Duration");
    }

    public BasicDurationFormat() {
        this.formatter = this.pfs.newDurationFormatterFactory().getFormatter();
        this.pformatter = this.pfs.newPeriodFormatterFactory().setDisplayPastFuture(false).getFormatter();
    }

    public BasicDurationFormat(ULocale locale) {
        super(locale);
        this.formatter = this.pfs.newDurationFormatterFactory().setLocale(locale.getName()).getFormatter();
        this.pformatter = this.pfs.newPeriodFormatterFactory().setDisplayPastFuture(false).setLocale(locale.getName()).getFormatter();
    }

    @Override
    public String formatDurationFrom(long duration, long referenceDate) {
        return this.formatter.formatDurationFrom(duration, referenceDate);
    }

    @Override
    public String formatDurationFromNow(long duration) {
        return this.formatter.formatDurationFromNow(duration);
    }

    @Override
    public String formatDurationFromNowTo(Date targetDate) {
        return this.formatter.formatDurationFromNowTo(targetDate);
    }

    public String formatDuration(Object obj) {
        DatatypeConstants.Field[] inFields = new DatatypeConstants.Field[]{DatatypeConstants.YEARS, DatatypeConstants.MONTHS, DatatypeConstants.DAYS, DatatypeConstants.HOURS, DatatypeConstants.MINUTES, DatatypeConstants.SECONDS};
        TimeUnit[] outFields = new TimeUnit[]{TimeUnit.YEAR, TimeUnit.MONTH, TimeUnit.DAY, TimeUnit.HOUR, TimeUnit.MINUTE, TimeUnit.SECOND};
        Duration inDuration = (Duration)obj;
        Period p2 = null;
        Duration duration = inDuration;
        boolean inPast = false;
        if (inDuration.getSign() < 0) {
            duration = inDuration.negate();
            inPast = true;
        }
        boolean sawNonZero = false;
        for (int i2 = 0; i2 < inFields.length; ++i2) {
            double intSeconds;
            double fullSeconds;
            double millis;
            Number n2;
            if (!duration.isSet(inFields[i2]) || (n2 = duration.getField(inFields[i2])).intValue() == 0 && !sawNonZero) continue;
            sawNonZero = true;
            float floatVal = n2.floatValue();
            TimeUnit alternateUnit = null;
            float alternateVal = 0.0f;
            if (outFields[i2] == TimeUnit.SECOND && (millis = ((fullSeconds = (double)floatVal) - (intSeconds = Math.floor(floatVal))) * 1000.0) > 0.0) {
                alternateUnit = TimeUnit.MILLISECOND;
                alternateVal = (float)millis;
                floatVal = (float)intSeconds;
            }
            p2 = p2 == null ? Period.at(floatVal, outFields[i2]) : p2.and(floatVal, outFields[i2]);
            if (alternateUnit == null) continue;
            p2 = p2.and(alternateVal, alternateUnit);
        }
        if (p2 == null) {
            return this.formatDurationFromNow(0L);
        }
        p2 = inPast ? p2.inPast() : p2.inFuture();
        return this.pformatter.format(p2);
    }
}

