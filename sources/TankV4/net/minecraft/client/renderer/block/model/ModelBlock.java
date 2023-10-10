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
import java.io.StringReader;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import net.minecraft.util.JsonUtils;
import net.minecraft.util.ResourceLocation;
import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class ModelBlock {
   static final Gson SERIALIZER = (new GsonBuilder()).registerTypeAdapter(ModelBlock.class, new ModelBlock.Deserializer()).registerTypeAdapter(BlockPart.class, new BlockPart.Deserializer()).registerTypeAdapter(BlockPartFace.class, new BlockPartFace.Deserializer()).registerTypeAdapter(BlockFaceUV.class, new BlockFaceUV.Deserializer()).registerTypeAdapter(ItemTransformVec3f.class, new ItemTransformVec3f.Deserializer()).registerTypeAdapter(ItemCameraTransforms.class, new ItemCameraTransforms.Deserializer()).create();
   private final boolean gui3d;
   protected ResourceLocation parentLocation;
   protected ModelBlock parent;
   private static final Logger LOGGER = LogManager.getLogger();
   public String name;
   protected final Map textures;
   private final List elements;
   private final boolean ambientOcclusion;
   private ItemCameraTransforms cameraTransforms;

   protected ModelBlock(List var1, Map var2, boolean var3, boolean var4, ItemCameraTransforms var5) {
      this((ResourceLocation)null, var1, var2, var3, var4, var5);
   }

   public boolean isGui3d() {
      return this.gui3d;
   }

   public List getElements() {
      return this != false ? this.parent.getElements() : this.elements;
   }

   public String resolveTextureName(String var1) {
      if (var1 != false) {
         var1 = '#' + var1;
      }

      return this.resolveTextureName(var1, new ModelBlock.Bookkeep(this, (ModelBlock.Bookkeep)null));
   }

   private ModelBlock(ResourceLocation var1, List var2, Map var3, boolean var4, boolean var5, ItemCameraTransforms var6) {
      this.name = "";
      this.elements = var2;
      this.ambientOcclusion = var4;
      this.gui3d = var5;
      this.textures = var3;
      this.parentLocation = var1;
      this.cameraTransforms = var6;
   }

   public static ModelBlock deserialize(Reader var0) {
      return (ModelBlock)SERIALIZER.fromJson(var0, ModelBlock.class);
   }

   protected ModelBlock(ResourceLocation var1, Map var2, boolean var3, boolean var4, ItemCameraTransforms var5) {
      this(var1, Collections.emptyList(), var2, var3, var4, var5);
   }

   public boolean isTexturePresent(String var1) {
      return !"missingno".equals(this.resolveTextureName(var1));
   }

   public void getParentFromMap(Map var1) {
      if (this.parentLocation != null) {
         this.parent = (ModelBlock)var1.get(this.parentLocation);
      }

   }

   private String resolveTextureName(String var1, ModelBlock.Bookkeep var2) {
      if (var1 != false) {
         if (this == var2.modelExt) {
            LOGGER.warn("Unable to resolve texture due to upward reference: " + var1 + " in " + this.name);
            return "missingno";
         } else {
            String var3 = (String)this.textures.get(var1.substring(1));
            if (var3 == null && this != null) {
               var3 = this.parent.resolveTextureName(var1, var2);
            }

            var2.modelExt = this;
            if (var3 != null && var3 != false) {
               var3 = var2.model.resolveTextureName(var3, var2);
            }

            Object var10001;
            if (var3 != null) {
               var10001 = this;
               if (var3 != false) {
                  return (String)var10001;
               }
            }

            var10001 = "missingno";
            return (String)var10001;
         }
      } else {
         return var1;
      }
   }

   public ItemCameraTransforms func_181682_g() {
      ItemTransformVec3f var1 = this.func_181681_a(ItemCameraTransforms.TransformType.THIRD_PERSON);
      ItemTransformVec3f var2 = this.func_181681_a(ItemCameraTransforms.TransformType.FIRST_PERSON);
      ItemTransformVec3f var3 = this.func_181681_a(ItemCameraTransforms.TransformType.HEAD);
      ItemTransformVec3f var4 = this.func_181681_a(ItemCameraTransforms.TransformType.GUI);
      ItemTransformVec3f var5 = this.func_181681_a(ItemCameraTransforms.TransformType.GROUND);
      ItemTransformVec3f var6 = this.func_181681_a(ItemCameraTransforms.TransformType.FIXED);
      return new ItemCameraTransforms(var1, var2, var3, var4, var5, var6);
   }

   public static ModelBlock deserialize(String var0) {
      return deserialize((Reader)(new StringReader(var0)));
   }

   public ModelBlock getRootModel() {
      return this != null ? this.parent.getRootModel() : this;
   }

   public static void checkModelHierarchy(Map var0) {
      Iterator var2 = var0.values().iterator();
      if (var2.hasNext()) {
         ModelBlock var1 = (ModelBlock)var2.next();
         ModelBlock var3 = var1.parent;

         for(ModelBlock var4 = var3.parent; var3 != var4; var4 = var4.parent.parent) {
            var3 = var3.parent;
         }

         throw new ModelBlock.LoopException();
      }
   }

   public ResourceLocation getParentLocation() {
      return this.parentLocation;
   }

   public boolean isAmbientOcclusion() {
      return this != null ? this.parent.isAmbientOcclusion() : this.ambientOcclusion;
   }

   private ItemTransformVec3f func_181681_a(ItemCameraTransforms.TransformType var1) {
      return this.parent != null && !this.cameraTransforms.func_181687_c(var1) ? this.parent.func_181681_a(var1) : this.cameraTransforms.getTransform(var1);
   }

   public static class LoopException extends RuntimeException {
   }

   static final class Bookkeep {
      public final ModelBlock model;
      public ModelBlock modelExt;

      private Bookkeep(ModelBlock var1) {
         this.model = var1;
      }

      Bookkeep(ModelBlock var1, ModelBlock.Bookkeep var2) {
         this(var1);
      }
   }

   public static class Deserializer implements JsonDeserializer {
      protected List getModelElements(JsonDeserializationContext var1, JsonObject var2) {
         ArrayList var3 = Lists.newArrayList();
         if (var2.has("elements")) {
            Iterator var5 = JsonUtils.getJsonArray(var2, "elements").iterator();

            while(var5.hasNext()) {
               JsonElement var4 = (JsonElement)var5.next();
               var3.add((BlockPart)var1.deserialize(var4, BlockPart.class));
            }
         }

         return var3;
      }

      private Map getTextures(JsonObject var1) {
         HashMap var2 = Maps.newHashMap();
         if (var1.has("textures")) {
            JsonObject var3 = var1.getAsJsonObject("textures");
            Iterator var5 = var3.entrySet().iterator();

            while(var5.hasNext()) {
               Entry var4 = (Entry)var5.next();
               var2.put((String)var4.getKey(), ((JsonElement)var4.getValue()).getAsString());
            }
         }

         return var2;
      }

      public Object deserialize(JsonElement var1, Type var2, JsonDeserializationContext var3) throws JsonParseException {
         return this.deserialize(var1, var2, var3);
      }

      protected boolean getAmbientOcclusionEnabled(JsonObject var1) {
         return JsonUtils.getBoolean(var1, "ambientocclusion", true);
      }

      private String getParent(JsonObject var1) {
         return JsonUtils.getString(var1, "parent", "");
      }

      public ModelBlock deserialize(JsonElement var1, Type var2, JsonDeserializationContext var3) throws JsonParseException {
         JsonObject var4 = var1.getAsJsonObject();
         List var5 = this.getModelElements(var3, var4);
         String var6 = this.getParent(var4);
         boolean var7 = StringUtils.isEmpty(var6);
         boolean var8 = var5.isEmpty();
         if (var8 && var7) {
            throw new JsonParseException("BlockModel requires either elements or parent, found neither");
         } else if (!var7 && !var8) {
            throw new JsonParseException("BlockModel requires either elements or parent, found both");
         } else {
            Map var9 = this.getTextures(var4);
            boolean var10 = this.getAmbientOcclusionEnabled(var4);
            ItemCameraTransforms var11 = ItemCameraTransforms.DEFAULT;
            if (var4.has("display")) {
               JsonObject var12 = JsonUtils.getJsonObject(var4, "display");
               var11 = (ItemCameraTransforms)var3.deserialize(var12, ItemCameraTransforms.class);
            }

            return var8 ? new ModelBlock(new ResourceLocation(var6), var9, var10, true, var11) : new ModelBlock(var5, var9, var10, true, var11);
         }
      }
   }
}
