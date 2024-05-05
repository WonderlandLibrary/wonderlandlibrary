package net.minecraft.client.renderer.block.model;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import java.lang.reflect.Type;
import net.minecraft.client.renderer.block.model.ItemCameraTransforms;
import net.minecraft.client.renderer.block.model.ItemTransformVec3f;

static class ItemCameraTransforms.Deserializer
implements JsonDeserializer<ItemCameraTransforms> {
    ItemCameraTransforms.Deserializer() {
    }

    public ItemCameraTransforms deserialize(JsonElement p_deserialize_1_, Type p_deserialize_2_, JsonDeserializationContext p_deserialize_3_) throws JsonParseException {
        JsonObject jsonobject = p_deserialize_1_.getAsJsonObject();
        ItemTransformVec3f itemtransformvec3f = this.func_181683_a(p_deserialize_3_, jsonobject, "thirdperson");
        ItemTransformVec3f itemtransformvec3f1 = this.func_181683_a(p_deserialize_3_, jsonobject, "firstperson");
        ItemTransformVec3f itemtransformvec3f2 = this.func_181683_a(p_deserialize_3_, jsonobject, "head");
        ItemTransformVec3f itemtransformvec3f3 = this.func_181683_a(p_deserialize_3_, jsonobject, "gui");
        ItemTransformVec3f itemtransformvec3f4 = this.func_181683_a(p_deserialize_3_, jsonobject, "ground");
        ItemTransformVec3f itemtransformvec3f5 = this.func_181683_a(p_deserialize_3_, jsonobject, "fixed");
        return new ItemCameraTransforms(itemtransformvec3f, itemtransformvec3f1, itemtransformvec3f2, itemtransformvec3f3, itemtransformvec3f4, itemtransformvec3f5);
    }

    private ItemTransformVec3f func_181683_a(JsonDeserializationContext p_181683_1_, JsonObject p_181683_2_, String p_181683_3_) {
        return p_181683_2_.has(p_181683_3_) ? (ItemTransformVec3f)p_181683_1_.deserialize(p_181683_2_.get(p_181683_3_), ItemTransformVec3f.class) : ItemTransformVec3f.DEFAULT;
    }
}
