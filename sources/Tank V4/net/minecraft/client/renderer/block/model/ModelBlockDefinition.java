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
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import net.minecraft.client.resources.model.ModelRotation;
import net.minecraft.util.JsonUtils;
import net.minecraft.util.ResourceLocation;

public class ModelBlockDefinition {
   static final Gson GSON = (new GsonBuilder()).registerTypeAdapter(ModelBlockDefinition.class, new ModelBlockDefinition.Deserializer()).registerTypeAdapter(ModelBlockDefinition.Variant.class, new ModelBlockDefinition.Variant.Deserializer()).create();
   private final Map mapVariants = Maps.newHashMap();

   public ModelBlockDefinition.Variants getVariants(String var1) {
      ModelBlockDefinition.Variants var2 = (ModelBlockDefinition.Variants)this.mapVariants.get(var1);
      if (var2 == null) {
         throw new ModelBlockDefinition.MissingVariantException(this);
      } else {
         return var2;
      }
   }

   public ModelBlockDefinition(Collection var1) {
      Iterator var3 = var1.iterator();

      while(var3.hasNext()) {
         ModelBlockDefinition.Variants var2 = (ModelBlockDefinition.Variants)var3.next();
         this.mapVariants.put(ModelBlockDefinition.Variants.access$0(var2), var2);
      }

   }

   public static ModelBlockDefinition parseFromReader(Reader var0) {
      return (ModelBlockDefinition)GSON.fromJson(var0, ModelBlockDefinition.class);
   }

   public int hashCode() {
      return this.mapVariants.hashCode();
   }

   public boolean equals(Object var1) {
      if (this == var1) {
         return true;
      } else if (var1 instanceof ModelBlockDefinition) {
         ModelBlockDefinition var2 = (ModelBlockDefinition)var1;
         return this.mapVariants.equals(var2.mapVariants);
      } else {
         return false;
      }
   }

   public ModelBlockDefinition(List var1) {
      Iterator var3 = var1.iterator();

      while(var3.hasNext()) {
         ModelBlockDefinition var2 = (ModelBlockDefinition)var3.next();
         this.mapVariants.putAll(var2.mapVariants);
      }

   }

   public class MissingVariantException extends RuntimeException {
      final ModelBlockDefinition this$0;

      public MissingVariantException(ModelBlockDefinition var1) {
         this.this$0 = var1;
      }
   }

   public static class Deserializer implements JsonDeserializer {
      protected List parseVariantsList(JsonDeserializationContext var1, JsonObject var2) {
         JsonObject var3 = JsonUtils.getJsonObject(var2, "variants");
         ArrayList var4 = Lists.newArrayList();
         Iterator var6 = var3.entrySet().iterator();

         while(var6.hasNext()) {
            Entry var5 = (Entry)var6.next();
            var4.add(this.parseVariants(var1, var5));
         }

         return var4;
      }

      public ModelBlockDefinition deserialize(JsonElement var1, Type var2, JsonDeserializationContext var3) throws JsonParseException {
         JsonObject var4 = var1.getAsJsonObject();
         List var5 = this.parseVariantsList(var3, var4);
         return new ModelBlockDefinition(var5);
      }

      public Object deserialize(JsonElement var1, Type var2, JsonDeserializationContext var3) throws JsonParseException {
         return this.deserialize(var1, var2, var3);
      }

      protected ModelBlockDefinition.Variants parseVariants(JsonDeserializationContext var1, Entry var2) {
         String var3 = (String)var2.getKey();
         ArrayList var4 = Lists.newArrayList();
         JsonElement var5 = (JsonElement)var2.getValue();
         if (var5.isJsonArray()) {
            Iterator var7 = var5.getAsJsonArray().iterator();

            while(var7.hasNext()) {
               JsonElement var6 = (JsonElement)var7.next();
               var4.add((ModelBlockDefinition.Variant)var1.deserialize(var6, ModelBlockDefinition.Variant.class));
            }
         } else {
            var4.add((ModelBlockDefinition.Variant)var1.deserialize(var5, ModelBlockDefinition.Variant.class));
         }

         return new ModelBlockDefinition.Variants(var3, var4);
      }
   }

   public static class Variants {
      private final String name;
      private final List listVariants;

      public List getVariants() {
         return this.listVariants;
      }

      public boolean equals(Object var1) {
         if (this == var1) {
            return true;
         } else if (!(var1 instanceof ModelBlockDefinition.Variants)) {
            return false;
         } else {
            ModelBlockDefinition.Variants var2 = (ModelBlockDefinition.Variants)var1;
            return !this.name.equals(var2.name) ? false : this.listVariants.equals(var2.listVariants);
         }
      }

      public Variants(String var1, List var2) {
         this.name = var1;
         this.listVariants = var2;
      }

      static String access$0(ModelBlockDefinition.Variants var0) {
         return var0.name;
      }

      public int hashCode() {
         int var1 = this.name.hashCode();
         var1 = 31 * var1 + this.listVariants.hashCode();
         return var1;
      }
   }

   public static class Variant {
      private final ResourceLocation modelLocation;
      private final int weight;
      private final boolean uvLock;
      private final ModelRotation modelRotation;

      public boolean equals(Object var1) {
         if (this == var1) {
            return true;
         } else if (!(var1 instanceof ModelBlockDefinition.Variant)) {
            return false;
         } else {
            ModelBlockDefinition.Variant var2 = (ModelBlockDefinition.Variant)var1;
            return this.modelLocation.equals(var2.modelLocation) && this.modelRotation == var2.modelRotation && this.uvLock == var2.uvLock;
         }
      }

      public boolean isUvLocked() {
         return this.uvLock;
      }

      public ResourceLocation getModelLocation() {
         return this.modelLocation;
      }

      public int getWeight() {
         return this.weight;
      }

      public int hashCode() {
         int var1 = this.modelLocation.hashCode();
         var1 = 31 * var1 + (this.modelRotation != null ? this.modelRotation.hashCode() : 0);
         var1 = 31 * var1 + (this.uvLock ? 1 : 0);
         return var1;
      }

      public ModelRotation getRotation() {
         return this.modelRotation;
      }

      public Variant(ResourceLocation var1, ModelRotation var2, boolean var3, int var4) {
         this.modelLocation = var1;
         this.modelRotation = var2;
         this.uvLock = var3;
         this.weight = var4;
      }

      public static class Deserializer implements JsonDeserializer {
         protected ModelRotation parseRotation(JsonObject var1) {
            int var2 = JsonUtils.getInt(var1, "x", 0);
            int var3 = JsonUtils.getInt(var1, "y", 0);
            ModelRotation var4 = ModelRotation.getModelRotation(var2, var3);
            if (var4 == null) {
               throw new JsonParseException("Invalid BlockModelRotation x: " + var2 + ", y: " + var3);
            } else {
               return var4;
            }
         }

         protected String parseModel(JsonObject var1) {
            return JsonUtils.getString(var1, "model");
         }

         protected int parseWeight(JsonObject var1) {
            return JsonUtils.getInt(var1, "weight", 1);
         }

         private ResourceLocation makeModelLocation(String var1) {
            ResourceLocation var2 = new ResourceLocation(var1);
            var2 = new ResourceLocation(var2.getResourceDomain(), "block/" + var2.getResourcePath());
            return var2;
         }

         public ModelBlockDefinition.Variant deserialize(JsonElement var1, Type var2, JsonDeserializationContext var3) throws JsonParseException {
            JsonObject var4 = var1.getAsJsonObject();
            String var5 = this.parseModel(var4);
            ModelRotation var6 = this.parseRotation(var4);
            boolean var7 = this.parseUvLock(var4);
            int var8 = this.parseWeight(var4);
            return new ModelBlockDefinition.Variant(this.makeModelLocation(var5), var6, var7, var8);
         }

         public Object deserialize(JsonElement var1, Type var2, JsonDeserializationContext var3) throws JsonParseException {
            return this.deserialize(var1, var2, var3);
         }

         private boolean parseUvLock(JsonObject var1) {
            return JsonUtils.getBoolean(var1, "uvlock", false);
         }
      }
   }
}
