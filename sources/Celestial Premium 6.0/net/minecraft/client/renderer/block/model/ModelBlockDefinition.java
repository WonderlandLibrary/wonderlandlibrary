/*
 * Decompiled with CFR 0.150.
 */
package net.minecraft.client.renderer.block.model;

import com.google.common.annotations.VisibleForTesting;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import java.io.Reader;
import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import javax.annotation.Nullable;
import net.minecraft.client.renderer.block.model.Variant;
import net.minecraft.client.renderer.block.model.VariantList;
import net.minecraft.client.renderer.block.model.multipart.Multipart;
import net.minecraft.client.renderer.block.model.multipart.Selector;
import net.minecraft.util.JsonUtils;

public class ModelBlockDefinition {
    @VisibleForTesting
    static final Gson GSON = new GsonBuilder().registerTypeAdapter((Type)((Object)ModelBlockDefinition.class), new Deserializer()).registerTypeAdapter((Type)((Object)Variant.class), new Variant.Deserializer()).registerTypeAdapter((Type)((Object)VariantList.class), new VariantList.Deserializer()).registerTypeAdapter((Type)((Object)Multipart.class), new Multipart.Deserializer()).registerTypeAdapter((Type)((Object)Selector.class), new Selector.Deserializer()).create();
    private final Map<String, VariantList> mapVariants = Maps.newHashMap();
    private Multipart multipart;

    public static ModelBlockDefinition parseFromReader(Reader reader) {
        return JsonUtils.func_193839_a(GSON, reader, ModelBlockDefinition.class);
    }

    public ModelBlockDefinition(Map<String, VariantList> variants, Multipart multipartIn) {
        this.multipart = multipartIn;
        this.mapVariants.putAll(variants);
    }

    public ModelBlockDefinition(List<ModelBlockDefinition> p_i46222_1_) {
        ModelBlockDefinition modelblockdefinition = null;
        for (ModelBlockDefinition modelblockdefinition1 : p_i46222_1_) {
            if (modelblockdefinition1.hasMultipartData()) {
                this.mapVariants.clear();
                modelblockdefinition = modelblockdefinition1;
            }
            this.mapVariants.putAll(modelblockdefinition1.mapVariants);
        }
        if (modelblockdefinition != null) {
            this.multipart = modelblockdefinition.multipart;
        }
    }

    public boolean hasVariant(String p_188000_1_) {
        return this.mapVariants.get(p_188000_1_) != null;
    }

    public VariantList getVariant(String p_188004_1_) {
        VariantList variantlist = this.mapVariants.get(p_188004_1_);
        if (variantlist == null) {
            throw new MissingVariantException();
        }
        return variantlist;
    }

    public boolean equals(Object p_equals_1_) {
        if (this == p_equals_1_) {
            return true;
        }
        if (p_equals_1_ instanceof ModelBlockDefinition) {
            ModelBlockDefinition modelblockdefinition = (ModelBlockDefinition)p_equals_1_;
            if (this.mapVariants.equals(modelblockdefinition.mapVariants)) {
                return this.hasMultipartData() ? this.multipart.equals(modelblockdefinition.multipart) : !modelblockdefinition.hasMultipartData();
            }
        }
        return false;
    }

    public int hashCode() {
        return 31 * this.mapVariants.hashCode() + (this.hasMultipartData() ? this.multipart.hashCode() : 0);
    }

    public Set<VariantList> getMultipartVariants() {
        HashSet<VariantList> set = Sets.newHashSet(this.mapVariants.values());
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

    public class MissingVariantException
    extends RuntimeException {
    }

    public static class Deserializer
    implements JsonDeserializer<ModelBlockDefinition> {
        @Override
        public ModelBlockDefinition deserialize(JsonElement p_deserialize_1_, Type p_deserialize_2_, JsonDeserializationContext p_deserialize_3_) throws JsonParseException {
            JsonObject jsonobject = p_deserialize_1_.getAsJsonObject();
            Map<String, VariantList> map = this.parseMapVariants(p_deserialize_3_, jsonobject);
            Multipart multipart = this.parseMultipart(p_deserialize_3_, jsonobject);
            if (!map.isEmpty() || multipart != null && !multipart.getVariants().isEmpty()) {
                return new ModelBlockDefinition(map, multipart);
            }
            throw new JsonParseException("Neither 'variants' nor 'multipart' found");
        }

        protected Map<String, VariantList> parseMapVariants(JsonDeserializationContext deserializationContext, JsonObject object) {
            HashMap<String, VariantList> map = Maps.newHashMap();
            if (object.has("variants")) {
                JsonObject jsonobject = JsonUtils.getJsonObject(object, "variants");
                for (Map.Entry<String, JsonElement> entry : jsonobject.entrySet()) {
                    map.put(entry.getKey(), (VariantList)deserializationContext.deserialize(entry.getValue(), (Type)((Object)VariantList.class)));
                }
            }
            return map;
        }

        @Nullable
        protected Multipart parseMultipart(JsonDeserializationContext deserializationContext, JsonObject object) {
            if (!object.has("multipart")) {
                return null;
            }
            JsonArray jsonarray = JsonUtils.getJsonArray(object, "multipart");
            return (Multipart)deserializationContext.deserialize(jsonarray, (Type)((Object)Multipart.class));
        }
    }
}

