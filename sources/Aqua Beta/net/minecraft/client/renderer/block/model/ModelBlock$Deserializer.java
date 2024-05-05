package net.minecraft.client.renderer.block.model;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import net.minecraft.client.renderer.block.model.BlockPart;
import net.minecraft.client.renderer.block.model.ItemCameraTransforms;
import net.minecraft.client.renderer.block.model.ModelBlock;
import net.minecraft.util.JsonUtils;
import net.minecraft.util.ResourceLocation;
import org.apache.commons.lang3.StringUtils;

public static class ModelBlock.Deserializer
implements JsonDeserializer<ModelBlock> {
    public ModelBlock deserialize(JsonElement p_deserialize_1_, Type p_deserialize_2_, JsonDeserializationContext p_deserialize_3_) throws JsonParseException {
        JsonObject jsonobject = p_deserialize_1_.getAsJsonObject();
        List<BlockPart> list = this.getModelElements(p_deserialize_3_, jsonobject);
        String s = this.getParent(jsonobject);
        boolean flag = StringUtils.isEmpty((CharSequence)s);
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
            JsonObject jsonobject1 = JsonUtils.getJsonObject((JsonObject)jsonobject, (String)"display");
            itemcameratransforms = (ItemCameraTransforms)p_deserialize_3_.deserialize((JsonElement)jsonobject1, ItemCameraTransforms.class);
        }
        return flag1 ? new ModelBlock(new ResourceLocation(s), map, flag2, true, itemcameratransforms) : new ModelBlock(list, map, flag2, true, itemcameratransforms);
    }

    private Map<String, String> getTextures(JsonObject p_178329_1_) {
        HashMap map = Maps.newHashMap();
        if (p_178329_1_.has("textures")) {
            JsonObject jsonobject = p_178329_1_.getAsJsonObject("textures");
            for (Map.Entry entry : jsonobject.entrySet()) {
                map.put(entry.getKey(), (Object)((JsonElement)entry.getValue()).getAsString());
            }
        }
        return map;
    }

    private String getParent(JsonObject p_178326_1_) {
        return JsonUtils.getString((JsonObject)p_178326_1_, (String)"parent", (String)"");
    }

    protected boolean getAmbientOcclusionEnabled(JsonObject p_178328_1_) {
        return JsonUtils.getBoolean((JsonObject)p_178328_1_, (String)"ambientocclusion", (boolean)true);
    }

    protected List<BlockPart> getModelElements(JsonDeserializationContext p_178325_1_, JsonObject p_178325_2_) {
        ArrayList list = Lists.newArrayList();
        if (p_178325_2_.has("elements")) {
            for (JsonElement jsonelement : JsonUtils.getJsonArray((JsonObject)p_178325_2_, (String)"elements")) {
                list.add((Object)((BlockPart)p_178325_1_.deserialize(jsonelement, BlockPart.class)));
            }
        }
        return list;
    }
}
