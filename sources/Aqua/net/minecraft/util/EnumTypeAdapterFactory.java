package net.minecraft.util;

import com.google.common.collect.Maps;
import com.google.gson.Gson;
import com.google.gson.TypeAdapter;
import com.google.gson.TypeAdapterFactory;
import com.google.gson.reflect.TypeToken;
import java.util.HashMap;
import java.util.Locale;

public class EnumTypeAdapterFactory
implements TypeAdapterFactory {
    public <T> TypeAdapter<T> create(Gson p_create_1_, TypeToken<T> p_create_2_) {
        Class oclass = p_create_2_.getRawType();
        if (!oclass.isEnum()) {
            return null;
        }
        HashMap map = Maps.newHashMap();
        for (Object t : oclass.getEnumConstants()) {
            map.put((Object)this.func_151232_a(t), t);
        }
        return new /* Unavailable Anonymous Inner Class!! */;
    }

    private String func_151232_a(Object p_151232_1_) {
        return p_151232_1_ instanceof Enum ? ((Enum)p_151232_1_).name().toLowerCase(Locale.US) : p_151232_1_.toString().toLowerCase(Locale.US);
    }

    static /* synthetic */ String access$000(EnumTypeAdapterFactory x0, Object x1) {
        return x0.func_151232_a(x1);
    }
}
