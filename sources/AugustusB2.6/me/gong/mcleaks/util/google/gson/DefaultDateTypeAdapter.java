// 
// Decompiled by Procyon v0.5.36
// 

package me.gong.mcleaks.util.google.gson;

import java.text.ParseException;
import me.gong.mcleaks.util.google.gson.internal.bind.util.ISO8601Utils;
import java.text.ParsePosition;
import java.sql.Timestamp;
import java.lang.reflect.Type;
import java.text.SimpleDateFormat;
import java.util.Locale;
import java.text.DateFormat;
import java.util.Date;

final class DefaultDateTypeAdapter implements JsonSerializer<Date>, JsonDeserializer<Date>
{
    private final DateFormat enUsFormat;
    private final DateFormat localFormat;
    
    DefaultDateTypeAdapter() {
        this(DateFormat.getDateTimeInstance(2, 2, Locale.US), DateFormat.getDateTimeInstance(2, 2));
    }
    
    DefaultDateTypeAdapter(final String datePattern) {
        this(new SimpleDateFormat(datePattern, Locale.US), new SimpleDateFormat(datePattern));
    }
    
    DefaultDateTypeAdapter(final int style) {
        this(DateFormat.getDateInstance(style, Locale.US), DateFormat.getDateInstance(style));
    }
    
    public DefaultDateTypeAdapter(final int dateStyle, final int timeStyle) {
        this(DateFormat.getDateTimeInstance(dateStyle, timeStyle, Locale.US), DateFormat.getDateTimeInstance(dateStyle, timeStyle));
    }
    
    DefaultDateTypeAdapter(final DateFormat enUsFormat, final DateFormat localFormat) {
        this.enUsFormat = enUsFormat;
        this.localFormat = localFormat;
    }
    
    @Override
    public JsonElement serialize(final Date src, final Type typeOfSrc, final JsonSerializationContext context) {
        synchronized (this.localFormat) {
            final String dateFormatAsString = this.enUsFormat.format(src);
            return new JsonPrimitive(dateFormatAsString);
        }
    }
    
    @Override
    public Date deserialize(final JsonElement json, final Type typeOfT, final JsonDeserializationContext context) throws JsonParseException {
        if (!(json instanceof JsonPrimitive)) {
            throw new JsonParseException("The date should be a string value");
        }
        final Date date = this.deserializeToDate(json);
        if (typeOfT == Date.class) {
            return date;
        }
        if (typeOfT == Timestamp.class) {
            return new Timestamp(date.getTime());
        }
        if (typeOfT == java.sql.Date.class) {
            return new java.sql.Date(date.getTime());
        }
        throw new IllegalArgumentException(this.getClass() + " cannot deserialize to " + typeOfT);
    }
    
    private Date deserializeToDate(final JsonElement json) {
        synchronized (this.localFormat) {
            try {
                return this.localFormat.parse(json.getAsString());
            }
            catch (ParseException ex) {
                try {
                    return this.enUsFormat.parse(json.getAsString());
                }
                catch (ParseException ex2) {
                    try {
                        // monitorexit(this.localFormat)
                        return ISO8601Utils.parse(json.getAsString(), new ParsePosition(0));
                    }
                    catch (ParseException e) {
                        throw new JsonSyntaxException(json.getAsString(), e);
                    }
                }
            }
        }
    }
    
    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder();
        sb.append(DefaultDateTypeAdapter.class.getSimpleName());
        sb.append('(').append(this.localFormat.getClass().getSimpleName()).append(')');
        return sb.toString();
    }
}
