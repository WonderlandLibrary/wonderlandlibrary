// 
// Decompiled by Procyon v0.5.36
// 

package com.viaversion.viaversion.libs.gson;

import java.util.Iterator;
import com.viaversion.viaversion.libs.gson.internal.bind.util.ISO8601Utils;
import java.text.ParsePosition;
import java.text.ParseException;
import com.viaversion.viaversion.libs.gson.stream.JsonToken;
import com.viaversion.viaversion.libs.gson.stream.JsonReader;
import java.io.IOException;
import com.viaversion.viaversion.libs.gson.stream.JsonWriter;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import com.viaversion.viaversion.libs.gson.internal.PreJava9DateFormatProvider;
import com.viaversion.viaversion.libs.gson.internal.JavaVersion;
import java.util.Locale;
import java.util.ArrayList;
import java.text.DateFormat;
import java.util.List;
import java.util.Date;

final class DefaultDateTypeAdapter extends TypeAdapter<Date>
{
    private static final String SIMPLE_NAME = "DefaultDateTypeAdapter";
    private final Class<? extends Date> dateType;
    private final List<DateFormat> dateFormats;
    
    DefaultDateTypeAdapter(final Class<? extends Date> dateType) {
        this.dateFormats = new ArrayList<DateFormat>();
        this.dateType = verifyDateType(dateType);
        this.dateFormats.add(DateFormat.getDateTimeInstance(2, 2, Locale.US));
        if (!Locale.getDefault().equals(Locale.US)) {
            this.dateFormats.add(DateFormat.getDateTimeInstance(2, 2));
        }
        if (JavaVersion.isJava9OrLater()) {
            this.dateFormats.add(PreJava9DateFormatProvider.getUSDateTimeFormat(2, 2));
        }
    }
    
    DefaultDateTypeAdapter(final Class<? extends Date> dateType, final String datePattern) {
        this.dateFormats = new ArrayList<DateFormat>();
        this.dateType = verifyDateType(dateType);
        this.dateFormats.add(new SimpleDateFormat(datePattern, Locale.US));
        if (!Locale.getDefault().equals(Locale.US)) {
            this.dateFormats.add(new SimpleDateFormat(datePattern));
        }
    }
    
    DefaultDateTypeAdapter(final Class<? extends Date> dateType, final int style) {
        this.dateFormats = new ArrayList<DateFormat>();
        this.dateType = verifyDateType(dateType);
        this.dateFormats.add(DateFormat.getDateInstance(style, Locale.US));
        if (!Locale.getDefault().equals(Locale.US)) {
            this.dateFormats.add(DateFormat.getDateInstance(style));
        }
        if (JavaVersion.isJava9OrLater()) {
            this.dateFormats.add(PreJava9DateFormatProvider.getUSDateFormat(style));
        }
    }
    
    public DefaultDateTypeAdapter(final int dateStyle, final int timeStyle) {
        this(Date.class, dateStyle, timeStyle);
    }
    
    public DefaultDateTypeAdapter(final Class<? extends Date> dateType, final int dateStyle, final int timeStyle) {
        this.dateFormats = new ArrayList<DateFormat>();
        this.dateType = verifyDateType(dateType);
        this.dateFormats.add(DateFormat.getDateTimeInstance(dateStyle, timeStyle, Locale.US));
        if (!Locale.getDefault().equals(Locale.US)) {
            this.dateFormats.add(DateFormat.getDateTimeInstance(dateStyle, timeStyle));
        }
        if (JavaVersion.isJava9OrLater()) {
            this.dateFormats.add(PreJava9DateFormatProvider.getUSDateTimeFormat(dateStyle, timeStyle));
        }
    }
    
    private static Class<? extends Date> verifyDateType(final Class<? extends Date> dateType) {
        if (dateType != Date.class && dateType != java.sql.Date.class && dateType != Timestamp.class) {
            throw new IllegalArgumentException("Date type must be one of " + Date.class + ", " + Timestamp.class + ", or " + java.sql.Date.class + " but was " + dateType);
        }
        return dateType;
    }
    
    @Override
    public void write(final JsonWriter out, final Date value) throws IOException {
        if (value == null) {
            out.nullValue();
            return;
        }
        synchronized (this.dateFormats) {
            final String dateFormatAsString = this.dateFormats.get(0).format(value);
            out.value(dateFormatAsString);
        }
    }
    
    @Override
    public Date read(final JsonReader in) throws IOException {
        if (in.peek() == JsonToken.NULL) {
            in.nextNull();
            return null;
        }
        final Date date = this.deserializeToDate(in.nextString());
        if (this.dateType == Date.class) {
            return date;
        }
        if (this.dateType == Timestamp.class) {
            return new Timestamp(date.getTime());
        }
        if (this.dateType == java.sql.Date.class) {
            return new java.sql.Date(date.getTime());
        }
        throw new AssertionError();
    }
    
    private Date deserializeToDate(final String s) {
        synchronized (this.dateFormats) {
            for (final DateFormat dateFormat : this.dateFormats) {
                try {
                    return dateFormat.parse(s);
                }
                catch (ParseException ex) {
                    continue;
                }
                break;
            }
            try {
                return ISO8601Utils.parse(s, new ParsePosition(0));
            }
            catch (ParseException e) {
                throw new JsonSyntaxException(s, e);
            }
        }
    }
    
    @Override
    public String toString() {
        final DateFormat defaultFormat = this.dateFormats.get(0);
        if (defaultFormat instanceof SimpleDateFormat) {
            return "DefaultDateTypeAdapter(" + ((SimpleDateFormat)defaultFormat).toPattern() + ')';
        }
        return "DefaultDateTypeAdapter(" + defaultFormat.getClass().getSimpleName() + ')';
    }
}
