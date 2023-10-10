/*
 * Decompiled with CFR 0.152.
 */
package joptsimple.util;

import java.text.DateFormat;
import java.text.ParsePosition;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import joptsimple.ValueConversionException;
import joptsimple.ValueConverter;
import joptsimple.internal.Messages;

/*
 * This class specifies class file version 49.0 but uses Java 6 signatures.  Assumed Java 6.
 */
public class DateConverter
implements ValueConverter<Date> {
    private final DateFormat formatter;

    public DateConverter(DateFormat formatter) {
        if (formatter == null) {
            throw new NullPointerException("illegal null formatter");
        }
        this.formatter = formatter;
    }

    public static DateConverter datePattern(String pattern) {
        SimpleDateFormat formatter = new SimpleDateFormat(pattern);
        formatter.setLenient(false);
        return new DateConverter(formatter);
    }

    @Override
    public Date convert(String value) {
        ParsePosition position = new ParsePosition(0);
        Date date = this.formatter.parse(value, position);
        if (position.getIndex() != value.length()) {
            throw new ValueConversionException(this.message(value));
        }
        return date;
    }

    @Override
    public Class<Date> valueType() {
        return Date.class;
    }

    @Override
    public String valuePattern() {
        return this.formatter instanceof SimpleDateFormat ? ((SimpleDateFormat)this.formatter).toPattern() : "";
    }

    private String message(String value) {
        Object[] arguments;
        String key;
        if (this.formatter instanceof SimpleDateFormat) {
            key = "with.pattern.message";
            arguments = new Object[]{value, ((SimpleDateFormat)this.formatter).toPattern()};
        } else {
            key = "without.pattern.message";
            arguments = new Object[]{value};
        }
        return Messages.message(Locale.getDefault(), "joptsimple.ExceptionMessages", DateConverter.class, key, arguments);
    }
}

