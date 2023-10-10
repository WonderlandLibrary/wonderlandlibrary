// 
// Decompiled by Procyon v0.5.36
// 

package com.viaversion.viaversion.libs.gson.internal.bind;

import com.viaversion.viaversion.libs.gson.reflect.TypeToken;
import com.viaversion.viaversion.libs.gson.Gson;
import com.viaversion.viaversion.libs.gson.stream.JsonWriter;
import java.util.Iterator;
import com.viaversion.viaversion.libs.gson.JsonSyntaxException;
import com.viaversion.viaversion.libs.gson.internal.bind.util.ISO8601Utils;
import java.text.ParsePosition;
import java.text.ParseException;
import java.io.IOException;
import com.viaversion.viaversion.libs.gson.stream.JsonToken;
import com.viaversion.viaversion.libs.gson.stream.JsonReader;
import com.viaversion.viaversion.libs.gson.internal.PreJava9DateFormatProvider;
import com.viaversion.viaversion.libs.gson.internal.JavaVersion;
import java.util.Locale;
import java.util.ArrayList;
import java.text.DateFormat;
import java.util.List;
import com.viaversion.viaversion.libs.gson.TypeAdapterFactory;
import java.util.Date;
import com.viaversion.viaversion.libs.gson.TypeAdapter;

public final class DateTypeAdapter extends TypeAdapter<Date>
{
    public static final TypeAdapterFactory FACTORY;
    private final List<DateFormat> dateFormats;
    
    public DateTypeAdapter() {
        (this.dateFormats = new ArrayList<DateFormat>()).add(DateFormat.getDateTimeInstance(2, 2, Locale.US));
        if (!Locale.getDefault().equals(Locale.US)) {
            this.dateFormats.add(DateFormat.getDateTimeInstance(2, 2));
        }
        if (JavaVersion.isJava9OrLater()) {
            this.dateFormats.add(PreJava9DateFormatProvider.getUSDateTimeFormat(2, 2));
        }
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
        for (final DateFormat dateFormat : this.dateFormats) {
            try {
                return dateFormat.parse(json);
            }
            catch (ParseException ex) {
                continue;
            }
            break;
        }
        try {
            return ISO8601Utils.parse(json, new ParsePosition(0));
        }
        catch (ParseException e) {
            throw new JsonSyntaxException(json, e);
        }
    }
    
    @Override
    public synchronized void write(final JsonWriter out, final Date value) throws IOException {
        if (value == null) {
            out.nullValue();
            return;
        }
        final String dateFormatAsString = this.dateFormats.get(0).format(value);
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
