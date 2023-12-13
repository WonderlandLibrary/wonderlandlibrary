/*
 * Decompiled with CFR 0.152.
 */
package net.minecraft.client.renderer.block.model;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import java.io.Reader;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import net.minecraft.client.resources.model.ModelRotation;
import net.minecraft.util.JsonUtils;
import net.minecraft.util.ResourceLocation;

public class ModelBlockDefinition {
    static final Gson GSON = new GsonBuilder().registerTypeAdapter((Type)((Object)ModelBlockDefinition.class), new Deserializer()).registerTypeAdapter((Type)((Object)Variant.class), new Variant.Deserializer()).create();
    private final Map<String, Variants> mapVariants = Maps.newHashMap();

    public static ModelBlockDefinition parseFromReader(Reader p_178331_0_) {
        return GSON.fromJson(p_178331_0_, ModelBlockDefinition.class);
    }

    public ModelBlockDefinition(Collection<Variants> p_i46221_1_) {
        for (Variants modelblockdefinition$variants : p_i46221_1_) {
            this.mapVariants.put(modelblockdefinition$variants.name, modelblockdefinition$variants);
        }
    }

    public ModelBlockDefinition(List<ModelBlockDefinition> p_i46222_1_) {
        for (ModelBlockDefinition modelblockdefinition : p_i46222_1_) {
            this.mapVariants.putAll(modelblockdefinition.mapVariants);
        }
    }

    public Variants getVariants(String p_178330_1_) {
        Variants modelblockdefinition$variants = this.mapVariants.get(p_178330_1_);
        if (modelblockdefinition$variants == null) {
            throw new MissingVariantException();
        }
        return modelblockdefinition$variants;
    }

    public boolean equals(Object p_equals_1_) {
        if (this == p_equals_1_) {
            return true;
        }
        if (p_equals_1_ instanceof ModelBlockDefinition) {
            ModelBlockDefinition modelblockdefinition = (ModelBlockDefinition)p_equals_1_;
            return this.mapVariants.equals(modelblockdefinition.mapVariants);
        }
        return false;
    }

    public int hashCode() {
        return this.mapVariants.hashCode();
    }

    public static class Variants {
        private final String name;
        private final List<Variant> listVariants;

        public Variants(String nameIn, List<Variant> listVariantsIn) {
            this.name = nameIn;
            this.listVariants = listVariantsIn;
        }

        public List<Variant> getVariants() {
            return this.listVariants;
        }

        public boolean equals(Object p_equals_1_) {
            if (this == p_equals_1_) {
                return true;
            }
            if (!(p_equals_1_ instanceof Variants)) {
                return false;
            }
            Variants modelblockdefinition$variants = (Variants)p_equals_1_;
            return !this.name.equals(modelblockdefinition$variants.name) ? false : this.listVariants.equals(modelblockdefinition$variants.listVariants);
        }

        public int hashCode() {
            int i2 = this.name.hashCode();
            i2 = 31 * i2 + this.listVariants.hashCode();
            return i2;
        }
    }

    public static class Variant {
        private final ResourceLocation modelLocation;
        private final ModelRotation modelRotation;
        private final boolean uvLock;
        private final int weight;

        public Variant(ResourceLocation modelLocationIn, ModelRotation modelRotationIn, boolean uvLockIn, int weightIn) {
            this.modelLocation = modelLocationIn;
            this.modelRotation = modelRotationIn;
            this.uvLock = uvLockIn;
            this.weight = weightIn;
        }

        public ResourceLocation getModelLocation() {
            return this.modelLocation;
        }

        public ModelRotation getRotation() {
            return this.modelRotation;
        }

        public boolean isUvLocked() {
            return this.uvLock;
        }

        public int getWeight() {
            return this.weight;
        }

        public boolean equals(Object p_equals_1_) {
            if (this == p_equals_1_) {
                return true;
            }
            if (!(p_equals_1_ instanceof Variant)) {
                return false;
            }
            Variant modelblockdefinition$variant = (Variant)p_equals_1_;
            return this.modelLocation.equals(modelblockdefinition$variant.modelLocation) && this.modelRotation == modelblockdefinition$variant.modelRotation && this.uvLock == modelblockdefinition$variant.uvLock;
        }

        public int hashCode() {
            int i2 = this.modelLocation.hashCode();
            i2 = 31 * i2 + (this.modelRotation != null ? this.modelRotation.hashCode() : 0);
            i2 = 31 * i2 + (this.uvLock ? 1 : 0);
            return i2;
        }

        public static class Deserializer
        implements JsonDeserializer<Variant> {
            @Override
            public Variant deserialize(JsonElement p_deserialize_1_, Type p_deserialize_2_, JsonDeserializationContext p_deserialize_3_) throws JsonParseException {
                JsonObject jsonobject = p_deserialize_1_.getAsJsonObject();
                String s2 = this.parseModel(jsonobject);
                ModelRotation modelrotation = this.parseRotation(jsonobject);
                boolean flag = this.parseUvLock(jsonobject);
                int i2 = this.parseWeight(jsonobject);
                return new Variant(this.makeModelLocation(s2), modelrotation, flag, i2);
            }

            private ResourceLocation makeModelLocation(String p_178426_1_) {
                ResourceLocation resourcelocation = new ResourceLocation(p_178426_1_);
                resourcelocation = new ResourceLocation(resourcelocation.getResourceDomain(), "block/" + resourcelocation.getResourcePath());
                return resourcelocation;
            }

            private boolean parseUvLock(JsonObject p_178429_1_) {
                return JsonUtils.getBoolean(p_178429_1_, "uvlock", false);
            }

            protected ModelRotation parseRotation(JsonObject p_178428_1_) {
                int j2;
                int i2 = JsonUtils.getInt(p_178428_1_, "x", 0);
                ModelRotation modelrotation = ModelRotation.getModelRotation(i2, j2 = JsonUtils.getInt(p_178428_1_, "y", 0));
                if (modelrotation == null) {
                    throw new JsonParseException("Invalid BlockModelRotation x: " + i2 + ", y: " + j2);
                }
                return modelrotation;
            }

            protected String parseModel(JsonObject p_178424_1_) {
                return JsonUtils.getString(p_178424_1_, "model");
            }

            protected int parseWeight(JsonObject p_178427_1_) {
                return JsonUtils.getInt(p_178427_1_, "weight", 1);
            }
        }
    }

    public class MissingVariantException
    extends RuntimeException {
    }

    public static class Deserializer
    implements JsonDeserializer<ModelBlockDefinition> {
        @Override
        public ModelBlockDefinition deserialize(JsonElement p_deserialize_1_, Type p_deserialize_2_, JsonDeserializationContext p_deserialize_3_) throws JsonParseException {
            JsonObject jsonobject = p_deserialize_1_.getAsJsonObject();
            List<Variants> list = this.parseVariantsList(p_deserialize_3_, jsonobject);
            return new ModelBlockDefinition((Collection<Variants>)list);
        }

        protected List<Variants> parseVariantsList(JsonDeserializationContext p_178334_1_, JsonObject p_178334_2_) {
            JsonObject jsonobject = JsonUtils.getJsonObject(p_178334_2_, "variants");
            ArrayList<Variants> list = Lists.newArrayList();
            for (Map.Entry<String, JsonElement> entry : jsonobject.entrySet()) {
                list.add(this.parseVariants(p_178334_1_, entry));
            }
            return list;
        }

        protected Variants parseVariants(JsonDeserializationContext p_178335_1_, Map.Entry<String, JsonElement> p_178335_2_) {
            String s2 = p_178335_2_.getKey();
            ArrayList<Variant> list = Lists.newArrayList();
            JsonElement jsonelement = p_178335_2_.getValue();
            if (jsonelement.isJsonArray()) {
                for (JsonElement jsonelement1 : jsonelement.getAsJsonArray()) {
                    list.add((Variant)p_178335_1_.deserialize(jsonelement1, (Type)((Object)Variant.class)));
                }
            } else {
                list.add((Variant)p_178335_1_.deserialize(jsonelement, (Type)((Object)Variant.class)));
            }
            return new Variants(s2, list);
        }
    }
}

