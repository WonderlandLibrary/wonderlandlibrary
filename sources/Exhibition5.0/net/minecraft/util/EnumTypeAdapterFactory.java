// 
// Decompiled by Procyon v0.5.30
// 

package net.minecraft.util;

import java.util.Locale;
import com.google.gson.stream.JsonToken;
import com.google.gson.stream.JsonReader;
import java.io.IOException;
import com.google.gson.stream.JsonWriter;
import java.util.HashMap;
import com.google.common.collect.Maps;
import com.google.gson.TypeAdapter;
import com.google.gson.reflect.TypeToken;
import com.google.gson.Gson;
import com.google.gson.TypeAdapterFactory;

public class EnumTypeAdapterFactory implements TypeAdapterFactory
{
    private static final String __OBFID = "CL_00001494";
    
    public TypeAdapter create(final Gson p_create_1_, final TypeToken p_create_2_) {
        final Class var3 = p_create_2_.getRawType();
        if (!var3.isEnum()) {
            return null;
        }
        final HashMap var4 = Maps.newHashMap();
        for (final Object var8 : var3.getEnumConstants()) {
            var4.put(this.func_151232_a(var8), var8);
        }
        return new TypeAdapter() {
            private static final String __OBFID = "CL_00001495";
            
            public void write(final JsonWriter p_write_1_, final Object p_write_2_) throws IOException {
                if (p_write_2_ == null) {
                    p_write_1_.nullValue();
                }
                else {
                    p_write_1_.value(EnumTypeAdapterFactory.this.func_151232_a(p_write_2_));
                }
            }
            
            public Object read(final JsonReader p_read_1_) throws IOException {
                if (p_read_1_.peek() == JsonToken.NULL) {
                    p_read_1_.nextNull();
                    return null;
                }
                return var4.get(p_read_1_.nextString());
            }
        };
    }
    
    private String func_151232_a(final Object p_151232_1_) {
        return (p_151232_1_ instanceof Enum) ? ((Enum)p_151232_1_).name().toLowerCase(Locale.US) : p_151232_1_.toString().toLowerCase(Locale.US);
    }
}
