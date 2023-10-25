/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.google.common.collect.Lists
 *  com.google.common.collect.Maps
 *  com.google.gson.Gson
 *  com.google.gson.GsonBuilder
 *  com.google.gson.JsonDeserializationContext
 *  com.google.gson.JsonDeserializer
 *  com.google.gson.JsonElement
 *  com.google.gson.JsonObject
 *  com.google.gson.JsonParseException
 *  org.apache.commons.lang3.StringUtils
 *  org.apache.logging.log4j.LogManager
 *  org.apache.logging.log4j.Logger
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
import java.io.StringReader;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import net.minecraft.client.renderer.block.model.BlockFaceUV;
import net.minecraft.client.renderer.block.model.BlockPart;
import net.minecraft.client.renderer.block.model.BlockPartFace;
import net.minecraft.client.renderer.block.model.ItemCameraTransforms;
import net.minecraft.client.renderer.block.model.ItemTransformVec3f;
import net.minecraft.util.JsonUtils;
import net.minecraft.util.ResourceLocation;
import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class ModelBlock {
    public String name = "";
    private final List<BlockPart> elements;
    private static final Logger LOGGER = LogManager.getLogger();
    private final boolean gui3d;
    private ItemCameraTransforms cameraTransforms;
    private final boolean ambientOcclusion;
    protected ModelBlock parent;
    protected ResourceLocation parentLocation;
    protected final Map<String, String> textures;
    static final Gson SERIALIZER = new GsonBuilder().registerTypeAdapter(ModelBlock.class, (Object)new Deserializer()).registerTypeAdapter(BlockPart.class, (Object)new BlockPart.Deserializer()).registerTypeAdapter(BlockPartFace.class, (Object)new BlockPartFace.Deserializer()).registerTypeAdapter(BlockFaceUV.class, (Object)new BlockFaceUV.Deserializer()).registerTypeAdapter(ItemTransformVec3f.class, (Object)new ItemTransformVec3f.Deserializer()).registerTypeAdapter(ItemCameraTransforms.class, (Object)new ItemCameraTransforms.Deserializer()).create();

    protected ModelBlock(List<BlockPart> list, Map<String, String> map, boolean bl, boolean bl2, ItemCameraTransforms itemCameraTransforms) {
        this(null, list, map, bl, bl2, itemCameraTransforms);
    }

    public boolean isAmbientOcclusion() {
        return this.hasParent() ? this.parent.isAmbientOcclusion() : this.ambientOcclusion;
    }

    public boolean isResolved() {
        return this.parentLocation == null || this.parent != null && this.parent.isResolved();
    }

    private ModelBlock(ResourceLocation resourceLocation, List<BlockPart> list, Map<String, String> map, boolean bl, boolean bl2, ItemCameraTransforms itemCameraTransforms) {
        this.elements = list;
        this.ambientOcclusion = bl;
        this.gui3d = bl2;
        this.textures = map;
        this.parentLocation = resourceLocation;
        this.cameraTransforms = itemCameraTransforms;
    }

    public boolean isTexturePresent(String string) {
        return !"missingno".equals(this.resolveTextureName(string));
    }

    public ResourceLocation getParentLocation() {
        return this.parentLocation;
    }

    protected ModelBlock(ResourceLocation resourceLocation, Map<String, String> map, boolean bl, boolean bl2, ItemCameraTransforms itemCameraTransforms) {
        this(resourceLocation, Collections.emptyList(), map, bl, bl2, itemCameraTransforms);
    }

    public List<BlockPart> getElements() {
        return this.hasParent() ? this.parent.getElements() : this.elements;
    }

    public boolean isGui3d() {
        return this.gui3d;
    }

    public static ModelBlock deserialize(String string) {
        return ModelBlock.deserialize(new StringReader(string));
    }

    private boolean startsWithHash(String string) {
        return string.charAt(0) == '#';
    }

    public String resolveTextureName(String string) {
        if (!this.startsWithHash(string)) {
            string = String.valueOf('#') + string;
        }
        return this.resolveTextureName(string, new Bookkeep(this));
    }

    private boolean hasParent() {
        return this.parent != null;
    }

    public static ModelBlock deserialize(Reader reader) {
        return (ModelBlock)SERIALIZER.fromJson(reader, ModelBlock.class);
    }

    public ItemCameraTransforms func_181682_g() {
        ItemTransformVec3f itemTransformVec3f = this.func_181681_a(ItemCameraTransforms.TransformType.THIRD_PERSON);
        ItemTransformVec3f itemTransformVec3f2 = this.func_181681_a(ItemCameraTransforms.TransformType.FIRST_PERSON);
        ItemTransformVec3f itemTransformVec3f3 = this.func_181681_a(ItemCameraTransforms.TransformType.HEAD);
        ItemTransformVec3f itemTransformVec3f4 = this.func_181681_a(ItemCameraTransforms.TransformType.GUI);
        ItemTransformVec3f itemTransformVec3f5 = this.func_181681_a(ItemCameraTransforms.TransformType.GROUND);
        ItemTransformVec3f itemTransformVec3f6 = this.func_181681_a(ItemCameraTransforms.TransformType.FIXED);
        return new ItemCameraTransforms(itemTransformVec3f, itemTransformVec3f2, itemTransformVec3f3, itemTransformVec3f4, itemTransformVec3f5, itemTransformVec3f6);
    }

    public static void checkModelHierarchy(Map<ResourceLocation, ModelBlock> map) {
        Iterator<ModelBlock> iterator = map.values().iterator();
        if (iterator.hasNext()) {
            ModelBlock modelBlock = iterator.next();
            ModelBlock modelBlock2 = modelBlock.parent;
            ModelBlock modelBlock3 = modelBlock2.parent;
            while (modelBlock2 != modelBlock3) {
                modelBlock2 = modelBlock2.parent;
                modelBlock3 = modelBlock3.parent.parent;
            }
            throw new LoopException();
        }
    }

    public void getParentFromMap(Map<ResourceLocation, ModelBlock> map) {
        if (this.parentLocation != null) {
            this.parent = map.get(this.parentLocation);
        }
    }

    private String resolveTextureName(String string, Bookkeep bookkeep) {
        if (this.startsWithHash(string)) {
            if (this == bookkeep.modelExt) {
                LOGGER.warn("Unable to resolve texture due to upward reference: " + string + " in " + this.name);
                return "missingno";
            }
            String string2 = this.textures.get(string.substring(1));
            if (string2 == null && this.hasParent()) {
                string2 = this.parent.resolveTextureName(string, bookkeep);
            }
            bookkeep.modelExt = this;
            if (string2 != null && this.startsWithHash(string2)) {
                string2 = bookkeep.model.resolveTextureName(string2, bookkeep);
            }
            return string2 != null && !this.startsWithHash(string2) ? string2 : "missingno";
        }
        return string;
    }

    private ItemTransformVec3f func_181681_a(ItemCameraTransforms.TransformType transformType) {
        return this.parent != null && !this.cameraTransforms.func_181687_c(transformType) ? this.parent.func_181681_a(transformType) : this.cameraTransforms.getTransform(transformType);
    }

    public ModelBlock getRootModel() {
        return this.hasParent() ? this.parent.getRootModel() : this;
    }

    public static class Deserializer
    implements JsonDeserializer<ModelBlock> {
        protected List<BlockPart> getModelElements(JsonDeserializationContext jsonDeserializationContext, JsonObject jsonObject) {
            ArrayList arrayList = Lists.newArrayList();
            if (jsonObject.has("elements")) {
                for (JsonElement jsonElement : JsonUtils.getJsonArray(jsonObject, "elements")) {
                    arrayList.add((BlockPart)jsonDeserializationContext.deserialize(jsonElement, BlockPart.class));
                }
            }
            return arrayList;
        }

        private Map<String, String> getTextures(JsonObject jsonObject) {
            HashMap hashMap = Maps.newHashMap();
            if (jsonObject.has("textures")) {
                JsonObject jsonObject2 = jsonObject.getAsJsonObject("textures");
                for (Map.Entry entry : jsonObject2.entrySet()) {
                    hashMap.put((String)entry.getKey(), ((JsonElement)entry.getValue()).getAsString());
                }
            }
            return hashMap;
        }

        private String getParent(JsonObject jsonObject) {
            return JsonUtils.getString(jsonObject, "parent", "");
        }

        protected boolean getAmbientOcclusionEnabled(JsonObject jsonObject) {
            return JsonUtils.getBoolean(jsonObject, "ambientocclusion", true);
        }

        public ModelBlock deserialize(JsonElement jsonElement, Type type, JsonDeserializationContext jsonDeserializationContext) throws JsonParseException {
            JsonObject jsonObject = jsonElement.getAsJsonObject();
            List<BlockPart> list = this.getModelElements(jsonDeserializationContext, jsonObject);
            String string = this.getParent(jsonObject);
            boolean bl = StringUtils.isEmpty((CharSequence)string);
            boolean bl2 = list.isEmpty();
            if (bl2 && bl) {
                throw new JsonParseException("BlockModel requires either elements or parent, found neither");
            }
            if (!bl && !bl2) {
                throw new JsonParseException("BlockModel requires either elements or parent, found both");
            }
            Map<String, String> map = this.getTextures(jsonObject);
            boolean bl3 = this.getAmbientOcclusionEnabled(jsonObject);
            ItemCameraTransforms itemCameraTransforms = ItemCameraTransforms.DEFAULT;
            if (jsonObject.has("display")) {
                JsonObject jsonObject2 = JsonUtils.getJsonObject(jsonObject, "display");
                itemCameraTransforms = (ItemCameraTransforms)jsonDeserializationContext.deserialize((JsonElement)jsonObject2, ItemCameraTransforms.class);
            }
            return bl2 ? new ModelBlock(new ResourceLocation(string), map, bl3, true, itemCameraTransforms) : new ModelBlock(list, map, bl3, true, itemCameraTransforms);
        }
    }

    public static class LoopException
    extends RuntimeException {
    }

    static final class Bookkeep {
        public final ModelBlock model;
        public ModelBlock modelExt;

        private Bookkeep(ModelBlock modelBlock) {
            this.model = modelBlock;
        }
    }
}

