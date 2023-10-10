/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
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
    private static final Logger LOGGER = LogManager.getLogger();
    static final Gson SERIALIZER = new GsonBuilder().registerTypeAdapter((Type)((Object)ModelBlock.class), new Deserializer()).registerTypeAdapter((Type)((Object)BlockPart.class), new BlockPart.Deserializer()).registerTypeAdapter((Type)((Object)BlockPartFace.class), new BlockPartFace.Deserializer()).registerTypeAdapter((Type)((Object)BlockFaceUV.class), new BlockFaceUV.Deserializer()).registerTypeAdapter((Type)((Object)ItemTransformVec3f.class), new ItemTransformVec3f.Deserializer()).registerTypeAdapter((Type)((Object)ItemCameraTransforms.class), new ItemCameraTransforms.Deserializer()).create();
    private final List<BlockPart> elements;
    private final boolean gui3d;
    private final boolean ambientOcclusion;
    private ItemCameraTransforms cameraTransforms;
    public String name = "";
    protected final Map<String, String> textures;
    protected ModelBlock parent;
    protected ResourceLocation parentLocation;

    public static ModelBlock deserialize(Reader p_178307_0_) {
        return SERIALIZER.fromJson(p_178307_0_, ModelBlock.class);
    }

    public static ModelBlock deserialize(String p_178294_0_) {
        return ModelBlock.deserialize(new StringReader(p_178294_0_));
    }

    protected ModelBlock(List<BlockPart> p_i46225_1_, Map<String, String> p_i46225_2_, boolean p_i46225_3_, boolean p_i46225_4_, ItemCameraTransforms p_i46225_5_) {
        this(null, p_i46225_1_, p_i46225_2_, p_i46225_3_, p_i46225_4_, p_i46225_5_);
    }

    protected ModelBlock(ResourceLocation p_i46226_1_, Map<String, String> p_i46226_2_, boolean p_i46226_3_, boolean p_i46226_4_, ItemCameraTransforms p_i46226_5_) {
        this(p_i46226_1_, Collections.emptyList(), p_i46226_2_, p_i46226_3_, p_i46226_4_, p_i46226_5_);
    }

    private ModelBlock(ResourceLocation parentLocationIn, List<BlockPart> elementsIn, Map<String, String> texturesIn, boolean ambientOcclusionIn, boolean gui3dIn, ItemCameraTransforms cameraTransformsIn) {
        this.elements = elementsIn;
        this.ambientOcclusion = ambientOcclusionIn;
        this.gui3d = gui3dIn;
        this.textures = texturesIn;
        this.parentLocation = parentLocationIn;
        this.cameraTransforms = cameraTransformsIn;
    }

    public List<BlockPart> getElements() {
        List<BlockPart> list;
        if (this.hasParent()) {
            list = this.parent.getElements();
            return list;
        }
        list = this.elements;
        return list;
    }

    private boolean hasParent() {
        if (this.parent == null) return false;
        return true;
    }

    public boolean isAmbientOcclusion() {
        boolean bl;
        if (this.hasParent()) {
            bl = this.parent.isAmbientOcclusion();
            return bl;
        }
        bl = this.ambientOcclusion;
        return bl;
    }

    public boolean isGui3d() {
        return this.gui3d;
    }

    public boolean isResolved() {
        if (this.parentLocation == null) return true;
        if (this.parent == null) return false;
        if (!this.parent.isResolved()) return false;
        return true;
    }

    public void getParentFromMap(Map<ResourceLocation, ModelBlock> p_178299_1_) {
        if (this.parentLocation == null) return;
        this.parent = p_178299_1_.get(this.parentLocation);
    }

    public boolean isTexturePresent(String textureName) {
        if ("missingno".equals(this.resolveTextureName(textureName))) return false;
        return true;
    }

    public String resolveTextureName(String textureName) {
        if (this.startsWithHash(textureName)) return this.resolveTextureName(textureName, new Bookkeep(this));
        textureName = '#' + textureName;
        return this.resolveTextureName(textureName, new Bookkeep(this));
    }

    private String resolveTextureName(String textureName, Bookkeep p_178302_2_) {
        if (!this.startsWithHash(textureName)) return textureName;
        if (this == p_178302_2_.modelExt) {
            LOGGER.warn("Unable to resolve texture due to upward reference: " + textureName + " in " + this.name);
            return "missingno";
        }
        String s = this.textures.get(textureName.substring(1));
        if (s == null && this.hasParent()) {
            s = this.parent.resolveTextureName(textureName, p_178302_2_);
        }
        p_178302_2_.modelExt = this;
        if (s != null && this.startsWithHash(s)) {
            s = p_178302_2_.model.resolveTextureName(s, p_178302_2_);
        }
        if (s == null) return "missingno";
        if (this.startsWithHash(s)) return "missingno";
        String string = s;
        return string;
    }

    private boolean startsWithHash(String hash) {
        if (hash.charAt(0) != '#') return false;
        return true;
    }

    public ResourceLocation getParentLocation() {
        return this.parentLocation;
    }

    public ModelBlock getRootModel() {
        ModelBlock modelBlock;
        if (this.hasParent()) {
            modelBlock = this.parent.getRootModel();
            return modelBlock;
        }
        modelBlock = this;
        return modelBlock;
    }

    public ItemCameraTransforms func_181682_g() {
        ItemTransformVec3f itemtransformvec3f = this.func_181681_a(ItemCameraTransforms.TransformType.THIRD_PERSON);
        ItemTransformVec3f itemtransformvec3f1 = this.func_181681_a(ItemCameraTransforms.TransformType.FIRST_PERSON);
        ItemTransformVec3f itemtransformvec3f2 = this.func_181681_a(ItemCameraTransforms.TransformType.HEAD);
        ItemTransformVec3f itemtransformvec3f3 = this.func_181681_a(ItemCameraTransforms.TransformType.GUI);
        ItemTransformVec3f itemtransformvec3f4 = this.func_181681_a(ItemCameraTransforms.TransformType.GROUND);
        ItemTransformVec3f itemtransformvec3f5 = this.func_181681_a(ItemCameraTransforms.TransformType.FIXED);
        return new ItemCameraTransforms(itemtransformvec3f, itemtransformvec3f1, itemtransformvec3f2, itemtransformvec3f3, itemtransformvec3f4, itemtransformvec3f5);
    }

    private ItemTransformVec3f func_181681_a(ItemCameraTransforms.TransformType p_181681_1_) {
        ItemTransformVec3f itemTransformVec3f;
        if (this.parent != null && !this.cameraTransforms.func_181687_c(p_181681_1_)) {
            itemTransformVec3f = this.parent.func_181681_a(p_181681_1_);
            return itemTransformVec3f;
        }
        itemTransformVec3f = this.cameraTransforms.getTransform(p_181681_1_);
        return itemTransformVec3f;
    }

    public static void checkModelHierarchy(Map<ResourceLocation, ModelBlock> p_178312_0_) {
        Iterator<ModelBlock> iterator = p_178312_0_.values().iterator();
        while (iterator.hasNext()) {
            ModelBlock modelblock = iterator.next();
            try {
                ModelBlock modelblock1 = modelblock.parent;
                ModelBlock modelblock2 = modelblock1.parent;
                while (modelblock1 != modelblock2) {
                    modelblock1 = modelblock1.parent;
                    modelblock2 = modelblock2.parent.parent;
                }
                throw new LoopException();
            }
            catch (NullPointerException nullPointerException) {
            }
        }
    }

    public static class LoopException
    extends RuntimeException {
    }

    public static class Deserializer
    implements JsonDeserializer<ModelBlock> {
        @Override
        public ModelBlock deserialize(JsonElement p_deserialize_1_, Type p_deserialize_2_, JsonDeserializationContext p_deserialize_3_) throws JsonParseException {
            ModelBlock modelBlock;
            JsonObject jsonobject = p_deserialize_1_.getAsJsonObject();
            List<BlockPart> list = this.getModelElements(p_deserialize_3_, jsonobject);
            String s = this.getParent(jsonobject);
            boolean flag = StringUtils.isEmpty(s);
            boolean flag1 = list.isEmpty();
            if (flag1 && flag) {
                throw new JsonParseException("BlockModel requires either elements or parent, found neither");
            }
            if (!flag && !flag1) {
                throw new JsonParseException("BlockModel requires either elements or parent, found both");
            }
            Map<String, String> map = this.getTextures(jsonobject);
            boolean flag2 = this.getAmbientOcclusionEnabled(jsonobject);
            ItemCameraTransforms itemcameratransforms = ItemCameraTransforms.DEFAULT;
            if (jsonobject.has("display")) {
                JsonObject jsonobject1 = JsonUtils.getJsonObject(jsonobject, "display");
                itemcameratransforms = (ItemCameraTransforms)p_deserialize_3_.deserialize(jsonobject1, (Type)((Object)ItemCameraTransforms.class));
            }
            if (flag1) {
                modelBlock = new ModelBlock(new ResourceLocation(s), map, flag2, true, itemcameratransforms);
                return modelBlock;
            }
            modelBlock = new ModelBlock(list, map, flag2, true, itemcameratransforms);
            return modelBlock;
        }

        private Map<String, String> getTextures(JsonObject p_178329_1_) {
            HashMap<String, String> map = Maps.newHashMap();
            if (!p_178329_1_.has("textures")) return map;
            JsonObject jsonobject = p_178329_1_.getAsJsonObject("textures");
            Iterator<Map.Entry<String, JsonElement>> iterator = jsonobject.entrySet().iterator();
            while (iterator.hasNext()) {
                Map.Entry<String, JsonElement> entry = iterator.next();
                map.put(entry.getKey(), entry.getValue().getAsString());
            }
            return map;
        }

        private String getParent(JsonObject p_178326_1_) {
            return JsonUtils.getString(p_178326_1_, "parent", "");
        }

        protected boolean getAmbientOcclusionEnabled(JsonObject p_178328_1_) {
            return JsonUtils.getBoolean(p_178328_1_, "ambientocclusion", true);
        }

        protected List<BlockPart> getModelElements(JsonDeserializationContext p_178325_1_, JsonObject p_178325_2_) {
            ArrayList<BlockPart> list = Lists.newArrayList();
            if (!p_178325_2_.has("elements")) return list;
            Iterator<JsonElement> iterator = JsonUtils.getJsonArray(p_178325_2_, "elements").iterator();
            while (iterator.hasNext()) {
                JsonElement jsonelement = iterator.next();
                list.add((BlockPart)p_178325_1_.deserialize(jsonelement, (Type)((Object)BlockPart.class)));
            }
            return list;
        }
    }

    static final class Bookkeep {
        public final ModelBlock model;
        public ModelBlock modelExt;

        private Bookkeep(ModelBlock p_i46223_1_) {
            this.model = p_i46223_1_;
        }
    }
}

