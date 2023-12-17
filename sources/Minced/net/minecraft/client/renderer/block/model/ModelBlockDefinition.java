// 
// Decompiled by Procyon v0.5.36
// 

package net.minecraft.client.renderer.block.model;

import javax.annotation.Nullable;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonElement;
import com.google.gson.JsonDeserializer;
import net.minecraft.client.renderer.block.model.multipart.Selector;
import java.lang.reflect.Type;
import com.google.gson.GsonBuilder;
import java.util.Collection;
import com.google.common.collect.Sets;
import java.util.Set;
import java.util.Iterator;
import java.util.List;
import com.google.common.collect.Maps;
import net.minecraft.util.JsonUtils;
import java.io.Reader;
import net.minecraft.client.renderer.block.model.multipart.Multipart;
import java.util.Map;
import com.google.common.annotations.VisibleForTesting;
import com.google.gson.Gson;

public class ModelBlockDefinition
{
    @VisibleForTesting
    static final Gson GSON;
    private final Map<String, VariantList> mapVariants;
    private Multipart multipart;
    
    public static ModelBlockDefinition parseFromReader(final Reader reader) {
        return JsonUtils.fromJson(ModelBlockDefinition.GSON, reader, ModelBlockDefinition.class);
    }
    
    public ModelBlockDefinition(final Map<String, VariantList> variants, final Multipart multipartIn) {
        this.mapVariants = (Map<String, VariantList>)Maps.newHashMap();
        this.multipart = multipartIn;
        this.mapVariants.putAll(variants);
    }
    
    public ModelBlockDefinition(final List<ModelBlockDefinition> p_i46222_1_) {
        this.mapVariants = (Map<String, VariantList>)Maps.newHashMap();
        ModelBlockDefinition modelblockdefinition = null;
        for (final ModelBlockDefinition modelblockdefinition2 : p_i46222_1_) {
            if (modelblockdefinition2.hasMultipartData()) {
                this.mapVariants.clear();
                modelblockdefinition = modelblockdefinition2;
            }
            this.mapVariants.putAll(modelblockdefinition2.mapVariants);
        }
        if (modelblockdefinition != null) {
            this.multipart = modelblockdefinition.multipart;
        }
    }
    
    public boolean hasVariant(final String p_188000_1_) {
        return this.mapVariants.get(p_188000_1_) != null;
    }
    
    public VariantList getVariant(final String p_188004_1_) {
        final VariantList variantlist = this.mapVariants.get(p_188004_1_);
        if (variantlist == null) {
            throw new MissingVariantException();
        }
        return variantlist;
    }
    
    @Override
    public boolean equals(final Object p_equals_1_) {
        if (this == p_equals_1_) {
            return true;
        }
        if (p_equals_1_ instanceof ModelBlockDefinition) {
            final ModelBlockDefinition modelblockdefinition = (ModelBlockDefinition)p_equals_1_;
            if (this.mapVariants.equals(modelblockdefinition.mapVariants)) {
                return this.hasMultipartData() ? this.multipart.equals(modelblockdefinition.multipart) : (!modelblockdefinition.hasMultipartData());
            }
        }
        return false;
    }
    
    @Override
    public int hashCode() {
        return 31 * this.mapVariants.hashCode() + (this.hasMultipartData() ? this.multipart.hashCode() : 0);
    }
    
    public Set<VariantList> getMultipartVariants() {
        final Set<VariantList> set = (Set<VariantList>)Sets.newHashSet((Iterable)this.mapVariants.values());
        if (this.hasMultipartData()) {
            set.addAll(this.multipart.getVariants());
        }
        return set;
    }
    
    public boolean hasMultipartData() {
        return this.multipart != null;
    }
    
    public Multipart getMultipartData() {
        return this.multipart;
    }
    
    static {
        GSON = new GsonBuilder().registerTypeAdapter((Type)ModelBlockDefinition.class, (Object)new Deserializer()).registerTypeAdapter((Type)Variant.class, (Object)new Variant.Deserializer()).registerTypeAdapter((Type)VariantList.class, (Object)new VariantList.Deserializer()).registerTypeAdapter((Type)Multipart.class, (Object)new Multipart.Deserializer()).registerTypeAdapter((Type)Selector.class, (Object)new Selector.Deserializer()).create();
    }
    
    public static class Deserializer implements JsonDeserializer<ModelBlockDefinition>
    {
        public ModelBlockDefinition deserialize(final JsonElement p_deserialize_1_, final Type p_deserialize_2_, final JsonDeserializationContext p_deserialize_3_) throws JsonParseException {
            final JsonObject jsonobject = p_deserialize_1_.getAsJsonObject();
            final Map<String, VariantList> map = this.parseMapVariants(p_deserialize_3_, jsonobject);
            final Multipart multipart = this.parseMultipart(p_deserialize_3_, jsonobject);
            if (!map.isEmpty() || (multipart != null && !multipart.getVariants().isEmpty())) {
                return new ModelBlockDefinition(map, multipart);
            }
            throw new JsonParseException("Neither 'variants' nor 'multipart' found");
        }
        
        protected Map<String, VariantList> parseMapVariants(final JsonDeserializationContext deserializationContext, final JsonObject object) {
            final Map<String, VariantList> map = (Map<String, VariantList>)Maps.newHashMap();
            if (object.has("variants")) {
                final JsonObject jsonobject = JsonUtils.getJsonObject(object, "variants");
                for (final Map.Entry<String, JsonElement> entry : jsonobject.entrySet()) {
                    map.put(entry.getKey(), (VariantList)deserializationContext.deserialize((JsonElement)entry.getValue(), (Type)VariantList.class));
                }
            }
            return map;
        }
        
        @Nullable
        protected Multipart parseMultipart(final JsonDeserializationContext deserializationContext, final JsonObject object) {
            if (!object.has("multipart")) {
                return null;
            }
            final JsonArray jsonarray = JsonUtils.getJsonArray(object, "multipart");
            return (Multipart)deserializationContext.deserialize((JsonElement)jsonarray, (Type)Multipart.class);
        }
    }
    
    public class MissingVariantException extends RuntimeException
    {
    }
}
