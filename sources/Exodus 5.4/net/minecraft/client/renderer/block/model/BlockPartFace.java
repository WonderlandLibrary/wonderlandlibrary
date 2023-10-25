/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.google.gson.JsonDeserializationContext
 *  com.google.gson.JsonDeserializer
 *  com.google.gson.JsonElement
 *  com.google.gson.JsonObject
 *  com.google.gson.JsonParseException
 */
package net.minecraft.client.renderer.block.model;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import java.lang.reflect.Type;
import net.minecraft.client.renderer.block.model.BlockFaceUV;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.JsonUtils;

public class BlockPartFace {
    public final BlockFaceUV blockFaceUV;
    public final EnumFacing cullFace;
    public static final EnumFacing FACING_DEFAULT = null;
    public final String texture;
    public final int tintIndex;

    public BlockPartFace(EnumFacing enumFacing, int n, String string, BlockFaceUV blockFaceUV) {
        this.cullFace = enumFacing;
        this.tintIndex = n;
        this.texture = string;
        this.blockFaceUV = blockFaceUV;
    }

    static class Deserializer
    implements JsonDeserializer<BlockPartFace> {
        private EnumFacing parseCullFace(JsonObject jsonObject) {
            String string = JsonUtils.getString(jsonObject, "cullface", "");
            return EnumFacing.byName(string);
        }

        private String parseTexture(JsonObject jsonObject) {
            return JsonUtils.getString(jsonObject, "texture");
        }

        Deserializer() {
        }

        protected int parseTintIndex(JsonObject jsonObject) {
            return JsonUtils.getInt(jsonObject, "tintindex", -1);
        }

        public BlockPartFace deserialize(JsonElement jsonElement, Type type, JsonDeserializationContext jsonDeserializationContext) throws JsonParseException {
            JsonObject jsonObject = jsonElement.getAsJsonObject();
            EnumFacing enumFacing = this.parseCullFace(jsonObject);
            int n = this.parseTintIndex(jsonObject);
            String string = this.parseTexture(jsonObject);
            BlockFaceUV blockFaceUV = (BlockFaceUV)jsonDeserializationContext.deserialize((JsonElement)jsonObject, BlockFaceUV.class);
            return new BlockPartFace(enumFacing, n, string, blockFaceUV);
        }
    }
}

