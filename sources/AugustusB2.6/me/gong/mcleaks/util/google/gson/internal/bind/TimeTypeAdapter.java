// 
// Decompiled by Procyon v0.5.36
// 

package me.gong.mcleaks.util.google.gson.internal.bind;

import me.gong.mcleaks.util.google.gson.reflect.TypeToken;
import me.gong.mcleaks.util.google.gson.Gson;
import me.gong.mcleaks.util.google.gson.stream.JsonWriter;
import java.io.IOException;
import java.util.Date;
import java.text.ParseException;
import me.gong.mcleaks.util.google.gson.JsonSyntaxException;
import me.gong.mcleaks.util.google.gson.stream.JsonToken;
import me.gong.mcleaks.util.google.gson.stream.JsonReader;
import java.text.SimpleDateFormat;
import java.text.DateFormat;
import me.gong.mcleaks.util.google.gson.TypeAdapterFactory;
import java.sql.Time;
import me.gong.mcleaks.util.google.gson.TypeAdapter;

public final class TimeTypeAdapter extends TypeAdapter<Time>
{
    public static final TypeAdapterFactory FACTORY;
    private final DateFormat format;
    
    public TimeTypeAdapter() {
        this.format = new SimpleDateFormat("hh:mm:ss a");
    }
    
    @Override
    public synchronized Time read(final JsonReader in) throws IOException {
        if (in.peek() == JsonToken.NULL) {
            in.nextNull();
            return null;
        }
        try {
            final Date date = this.format.parse(in.nextString());
            return new Time(date.getTime());
        }
        catch (ParseException e) {
            throw new JsonSyntaxException(e);
        }
    }
    
    @Override
    public synchronized void write(final JsonWriter out, final Time value) throws IOException {
        out.value((value == null) ? null : this.format.format(value));
    }
    
    static {
        FACTORY = new TypeAdapterFactory() {
            @Override
            public <T> TypeAdapter<T> create(final Gson gson, final TypeToken<T> typeToken) {
                return (TypeAdapter<T>)((typeToken.getRawType() == Time.class) ? new TimeTypeAdapter() : null);
            }
        };
    }
}
