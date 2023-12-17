// 
// Decompiled by Procyon v0.5.36
// 

package net.minecraft.util;

import com.google.gson.JsonPrimitive;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonParseException;
import com.google.gson.JsonDeserializationContext;
import java.lang.reflect.Type;
import com.google.gson.JsonElement;
import com.google.gson.JsonSerializer;
import com.google.gson.JsonDeserializer;
import org.apache.commons.lang3.Validate;
import java.util.Locale;
import org.apache.commons.lang3.StringUtils;

public class ResourceLocation implements Comparable<ResourceLocation>
{
    protected final String namespace;
    protected final String path;
    
    protected ResourceLocation(final int unused, final String... resourceName) {
        this.namespace = (StringUtils.isEmpty((CharSequence)resourceName[0]) ? "minecraft" : resourceName[0].toLowerCase(Locale.ROOT));
        Validate.notNull((Object)(this.path = resourceName[1].toLowerCase(Locale.ROOT)));
    }
    
    public ResourceLocation(final String resourceName) {
        this(0, splitObjectName(resourceName));
    }
    
    public ResourceLocation(final String namespaceIn, final String pathIn) {
        this(0, new String[] { namespaceIn, pathIn });
    }
    
    protected static String[] splitObjectName(final String toSplit) {
        final String[] astring = { "minecraft", toSplit };
        final int i = toSplit.indexOf(58);
        if (i >= 0) {
            astring[1] = toSplit.substring(i + 1, toSplit.length());
            if (i > 1) {
                astring[0] = toSplit.substring(0, i);
            }
        }
        return astring;
    }
    
    public String getPath() {
        return this.path;
    }
    
    public String getNamespace() {
        return this.namespace;
    }
    
    @Override
    public String toString() {
        return this.namespace + ':' + this.path;
    }
    
    @Override
    public boolean equals(final Object p_equals_1_) {
        if (this == p_equals_1_) {
            return true;
        }
        if (!(p_equals_1_ instanceof ResourceLocation)) {
            return false;
        }
        final ResourceLocation resourcelocation = (ResourceLocation)p_equals_1_;
        return this.namespace.equals(resourcelocation.namespace) && this.path.equals(resourcelocation.path);
    }
    
    @Override
    public int hashCode() {
        return 31 * this.namespace.hashCode() + this.path.hashCode();
    }
    
    @Override
    public int compareTo(final ResourceLocation p_compareTo_1_) {
        int i = this.namespace.compareTo(p_compareTo_1_.namespace);
        if (i == 0) {
            i = this.path.compareTo(p_compareTo_1_.path);
        }
        return i;
    }
    
    public static class Serializer implements JsonDeserializer<ResourceLocation>, JsonSerializer<ResourceLocation>
    {
        public ResourceLocation deserialize(final JsonElement p_deserialize_1_, final Type p_deserialize_2_, final JsonDeserializationContext p_deserialize_3_) throws JsonParseException {
            return new ResourceLocation(JsonUtils.getString(p_deserialize_1_, "location"));
        }
        
        public JsonElement serialize(final ResourceLocation p_serialize_1_, final Type p_serialize_2_, final JsonSerializationContext p_serialize_3_) {
            return (JsonElement)new JsonPrimitive(p_serialize_1_.toString());
        }
    }
}
