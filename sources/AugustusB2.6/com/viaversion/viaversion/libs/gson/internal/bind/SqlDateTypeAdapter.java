// 
// Decompiled by Procyon v0.5.36
// 

package com.viaversion.viaversion.libs.gson.internal.bind;

import com.viaversion.viaversion.libs.gson.reflect.TypeToken;
import com.viaversion.viaversion.libs.gson.Gson;
import com.viaversion.viaversion.libs.gson.stream.JsonWriter;
import java.io.IOException;
import java.text.ParseException;
import com.viaversion.viaversion.libs.gson.JsonSyntaxException;
import com.viaversion.viaversion.libs.gson.stream.JsonToken;
import com.viaversion.viaversion.libs.gson.stream.JsonReader;
import java.text.SimpleDateFormat;
import java.text.DateFormat;
import com.viaversion.viaversion.libs.gson.TypeAdapterFactory;
import java.sql.Date;
import com.viaversion.viaversion.libs.gson.TypeAdapter;

public final class SqlDateTypeAdapter extends TypeAdapter<Date>
{
    public static final TypeAdapterFactory FACTORY;
    private final DateFormat format;
    
    public SqlDateTypeAdapter() {
        this.format = new SimpleDateFormat("MMM d, yyyy");
    }
    
    @Override
    public synchronized Date read(final JsonReader in) throws IOException {
        if (in.peek() == JsonToken.NULL) {
            in.nextNull();
            return null;
        }
        try {
            final long utilDate = this.format.parse(in.nextString()).getTime();
            return new Date(utilDate);
        }
        catch (ParseException e) {
            throw new JsonSyntaxException(e);
        }
    }
    
    @Override
    public synchronized void write(final JsonWriter out, final Date value) throws IOException {
        out.value((value == null) ? null : this.format.format(value));
    }
    
    static {
        FACTORY = new TypeAdapterFactory() {
            @Override
            public <T> TypeAdapter<T> create(final Gson gson, final TypeToken<T> typeToken) {
                return (TypeAdapter<T>)((typeToken.getRawType() == Date.class) ? new SqlDateTypeAdapter() : null);
            }
        };
    }
}
