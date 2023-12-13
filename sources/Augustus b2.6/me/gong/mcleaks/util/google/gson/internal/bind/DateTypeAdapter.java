// 
// Decompiled by Procyon v0.5.36
// 

package me.gong.mcleaks.util.google.gson.internal.bind;

import me.gong.mcleaks.util.google.gson.reflect.TypeToken;
import me.gong.mcleaks.util.google.gson.Gson;
import me.gong.mcleaks.util.google.gson.stream.JsonWriter;
import java.text.ParseException;
import me.gong.mcleaks.util.google.gson.JsonSyntaxException;
import me.gong.mcleaks.util.google.gson.internal.bind.util.ISO8601Utils;
import java.text.ParsePosition;
import java.io.IOException;
import me.gong.mcleaks.util.google.gson.stream.JsonToken;
import me.gong.mcleaks.util.google.gson.stream.JsonReader;
import java.util.Locale;
import java.text.DateFormat;
import me.gong.mcleaks.util.google.gson.TypeAdapterFactory;
import java.util.Date;
import me.gong.mcleaks.util.google.gson.TypeAdapter;

public final class DateTypeAdapter extends TypeAdapter<Date>
{
    public static final TypeAdapterFactory FACTORY;
    private final DateFormat enUsFormat;
    private final DateFormat localFormat;
    
    public DateTypeAdapter() {
        this.enUsFormat = DateFormat.getDateTimeInstance(2, 2, Locale.US);
        this.localFormat = DateFormat.getDateTimeInstance(2, 2);
    }
    
    @Override
    public Date read(final JsonReader in) throws IOException {
        if (in.peek() == JsonToken.NULL) {
            in.nextNull();
            return null;
        }
        return this.deserializeToDate(in.nextString());
    }
    
    private synchronized Date deserializeToDate(final String json) {
        try {
            return this.localFormat.parse(json);
        }
        catch (ParseException ex) {
            try {
                return this.enUsFormat.parse(json);
            }
            catch (ParseException ex2) {
                try {
                    return ISO8601Utils.parse(json, new ParsePosition(0));
                }
                catch (ParseException e) {
                    throw new JsonSyntaxException(json, e);
                }
            }
        }
    }
    
    @Override
    public synchronized void write(final JsonWriter out, final Date value) throws IOException {
        if (value == null) {
            out.nullValue();
            return;
        }
        final String dateFormatAsString = this.enUsFormat.format(value);
        out.value(dateFormatAsString);
    }
    
    static {
        FACTORY = new TypeAdapterFactory() {
            @Override
            public <T> TypeAdapter<T> create(final Gson gson, final TypeToken<T> typeToken) {
                return (TypeAdapter<T>)((typeToken.getRawType() == Date.class) ? new DateTypeAdapter() : null);
            }
        };
    }
}
