package net.minecraft.client.renderer.block.model;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import java.lang.reflect.Type;
import net.minecraft.client.renderer.block.model.BlockFaceUV;
import net.minecraft.client.renderer.block.model.BlockPartFace;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.JsonUtils;

static class BlockPartFace.Deserializer
implements JsonDeserializer<BlockPartFace> {
    BlockPartFace.Deserializer() {
    }

    public BlockPartFace deserialize(JsonElement p_deserialize_1_, Type p_deserialize_2_, JsonDeserializationContext p_deserialize_3_) throws JsonParseException {
        JsonObject jsonobject = p_deserialize_1_.getAsJsonObject();
        EnumFacing enumfacing = this.parseCullFace(jsonobject);
        int i = this.parseTintIndex(jsonobject);
        String s = this.parseTexture(jsonobject);
        BlockFaceUV blockfaceuv = (BlockFaceUV)p_deserialize_3_.deserialize((JsonElement)jsonobject, BlockFaceUV.class);
        return new BlockPartFace(enumfacing, i, s, blockfaceuv);
    }

    protected int parseTintIndex(JsonObject p_178337_1_) {
        return JsonUtils.getInt((JsonObject)p_178337_1_, (String)"tintindex", (int)-1);
    }

    private String parseTexture(JsonObject p_178340_1_) {
        return JsonUtils.getString((JsonObject)p_178340_1_, (String)"texture");
    }

    private EnumFacing parseCullFace(JsonObject p_178339_1_) {
        String s = JsonUtils.getString((JsonObject)p_178339_1_, (String)"cullface", (String)"");
        return EnumFacing.byName((String)s);
    }
}
