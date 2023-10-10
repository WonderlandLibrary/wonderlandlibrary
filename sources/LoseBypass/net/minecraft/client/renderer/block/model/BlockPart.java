/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  org.lwjgl.util.vector.Vector3f
 */
package net.minecraft.client.renderer.block.model;

import com.google.common.collect.Maps;
import com.google.gson.JsonArray;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import java.lang.reflect.Type;
import java.util.EnumMap;
import java.util.Iterator;
import java.util.Map;
import net.minecraft.client.renderer.block.model.BlockPartFace;
import net.minecraft.client.renderer.block.model.BlockPartRotation;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.JsonUtils;
import net.minecraft.util.MathHelper;
import org.lwjgl.util.vector.Vector3f;

public class BlockPart {
    public final Vector3f positionFrom;
    public final Vector3f positionTo;
    public final Map<EnumFacing, BlockPartFace> mapFaces;
    public final BlockPartRotation partRotation;
    public final boolean shade;

    public BlockPart(Vector3f positionFromIn, Vector3f positionToIn, Map<EnumFacing, BlockPartFace> mapFacesIn, BlockPartRotation partRotationIn, boolean shadeIn) {
        this.positionFrom = positionFromIn;
        this.positionTo = positionToIn;
        this.mapFaces = mapFacesIn;
        this.partRotation = partRotationIn;
        this.shade = shadeIn;
        this.setDefaultUvs();
    }

    private void setDefaultUvs() {
        Iterator<Map.Entry<EnumFacing, BlockPartFace>> iterator = this.mapFaces.entrySet().iterator();
        while (iterator.hasNext()) {
            Map.Entry<EnumFacing, BlockPartFace> entry = iterator.next();
            float[] afloat = this.getFaceUvs(entry.getKey());
            entry.getValue().blockFaceUV.setUvs(afloat);
        }
    }

    private float[] getFaceUvs(EnumFacing p_178236_1_) {
        switch (1.$SwitchMap$net$minecraft$util$EnumFacing[p_178236_1_.ordinal()]) {
            case 1: 
            case 2: {
                return new float[]{this.positionFrom.x, this.positionFrom.z, this.positionTo.x, this.positionTo.z};
            }
            case 3: 
            case 4: {
                return new float[]{this.positionFrom.x, 16.0f - this.positionTo.y, this.positionTo.x, 16.0f - this.positionFrom.y};
            }
            case 5: 
            case 6: {
                return new float[]{this.positionFrom.z, 16.0f - this.positionTo.y, this.positionTo.z, 16.0f - this.positionFrom.y};
            }
        }
        throw new NullPointerException();
    }

    static class Deserializer
    implements JsonDeserializer<BlockPart> {
        Deserializer() {
        }

        @Override
        public BlockPart deserialize(JsonElement p_deserialize_1_, Type p_deserialize_2_, JsonDeserializationContext p_deserialize_3_) throws JsonParseException {
            JsonObject jsonobject = p_deserialize_1_.getAsJsonObject();
            Vector3f vector3f = this.parsePositionFrom(jsonobject);
            Vector3f vector3f1 = this.parsePositionTo(jsonobject);
            BlockPartRotation blockpartrotation = this.parseRotation(jsonobject);
            Map<EnumFacing, BlockPartFace> map = this.parseFacesCheck(p_deserialize_3_, jsonobject);
            if (jsonobject.has("shade") && !JsonUtils.isBoolean(jsonobject, "shade")) {
                throw new JsonParseException("Expected shade to be a Boolean");
            }
            boolean flag = JsonUtils.getBoolean(jsonobject, "shade", true);
            return new BlockPart(vector3f, vector3f1, map, blockpartrotation, flag);
        }

        private BlockPartRotation parseRotation(JsonObject p_178256_1_) {
            BlockPartRotation blockpartrotation = null;
            if (!p_178256_1_.has("rotation")) return blockpartrotation;
            JsonObject jsonobject = JsonUtils.getJsonObject(p_178256_1_, "rotation");
            Vector3f vector3f = this.parsePosition(jsonobject, "origin");
            vector3f.scale(0.0625f);
            EnumFacing.Axis enumfacing$axis = this.parseAxis(jsonobject);
            float f = this.parseAngle(jsonobject);
            boolean flag = JsonUtils.getBoolean(jsonobject, "rescale", false);
            return new BlockPartRotation(vector3f, enumfacing$axis, f, flag);
        }

        private float parseAngle(JsonObject p_178255_1_) {
            float f = JsonUtils.getFloat(p_178255_1_, "angle");
            if (f == 0.0f) return f;
            if (MathHelper.abs(f) == 22.5f) return f;
            if (MathHelper.abs(f) == 45.0f) return f;
            throw new JsonParseException("Invalid rotation " + f + " found, only -45/-22.5/0/22.5/45 allowed");
        }

        private EnumFacing.Axis parseAxis(JsonObject p_178252_1_) {
            String s = JsonUtils.getString(p_178252_1_, "axis");
            EnumFacing.Axis enumfacing$axis = EnumFacing.Axis.byName(s.toLowerCase());
            if (enumfacing$axis != null) return enumfacing$axis;
            throw new JsonParseException("Invalid rotation axis: " + s);
        }

        private Map<EnumFacing, BlockPartFace> parseFacesCheck(JsonDeserializationContext p_178250_1_, JsonObject p_178250_2_) {
            Map<EnumFacing, BlockPartFace> map = this.parseFaces(p_178250_1_, p_178250_2_);
            if (!map.isEmpty()) return map;
            throw new JsonParseException("Expected between 1 and 6 unique faces, got 0");
        }

        private Map<EnumFacing, BlockPartFace> parseFaces(JsonDeserializationContext p_178253_1_, JsonObject p_178253_2_) {
            EnumMap<EnumFacing, BlockPartFace> map = Maps.newEnumMap(EnumFacing.class);
            JsonObject jsonobject = JsonUtils.getJsonObject(p_178253_2_, "faces");
            Iterator<Map.Entry<String, JsonElement>> iterator = jsonobject.entrySet().iterator();
            while (iterator.hasNext()) {
                Map.Entry<String, JsonElement> entry = iterator.next();
                EnumFacing enumfacing = this.parseEnumFacing(entry.getKey());
                map.put(enumfacing, (BlockPartFace)p_178253_1_.deserialize(entry.getValue(), (Type)((Object)BlockPartFace.class)));
            }
            return map;
        }

        private EnumFacing parseEnumFacing(String name) {
            EnumFacing enumfacing = EnumFacing.byName(name);
            if (enumfacing != null) return enumfacing;
            throw new JsonParseException("Unknown facing: " + name);
        }

        private Vector3f parsePositionTo(JsonObject p_178247_1_) {
            Vector3f vector3f = this.parsePosition(p_178247_1_, "to");
            if (!(vector3f.x >= -16.0f)) throw new JsonParseException("'to' specifier exceeds the allowed boundaries: " + vector3f);
            if (!(vector3f.y >= -16.0f)) throw new JsonParseException("'to' specifier exceeds the allowed boundaries: " + vector3f);
            if (!(vector3f.z >= -16.0f)) throw new JsonParseException("'to' specifier exceeds the allowed boundaries: " + vector3f);
            if (!(vector3f.x <= 32.0f)) throw new JsonParseException("'to' specifier exceeds the allowed boundaries: " + vector3f);
            if (!(vector3f.y <= 32.0f)) throw new JsonParseException("'to' specifier exceeds the allowed boundaries: " + vector3f);
            if (!(vector3f.z <= 32.0f)) throw new JsonParseException("'to' specifier exceeds the allowed boundaries: " + vector3f);
            return vector3f;
        }

        private Vector3f parsePositionFrom(JsonObject p_178249_1_) {
            Vector3f vector3f = this.parsePosition(p_178249_1_, "from");
            if (!(vector3f.x >= -16.0f)) throw new JsonParseException("'from' specifier exceeds the allowed boundaries: " + vector3f);
            if (!(vector3f.y >= -16.0f)) throw new JsonParseException("'from' specifier exceeds the allowed boundaries: " + vector3f);
            if (!(vector3f.z >= -16.0f)) throw new JsonParseException("'from' specifier exceeds the allowed boundaries: " + vector3f);
            if (!(vector3f.x <= 32.0f)) throw new JsonParseException("'from' specifier exceeds the allowed boundaries: " + vector3f);
            if (!(vector3f.y <= 32.0f)) throw new JsonParseException("'from' specifier exceeds the allowed boundaries: " + vector3f);
            if (!(vector3f.z <= 32.0f)) throw new JsonParseException("'from' specifier exceeds the allowed boundaries: " + vector3f);
            return vector3f;
        }

        private Vector3f parsePosition(JsonObject p_178251_1_, String p_178251_2_) {
            JsonArray jsonarray = JsonUtils.getJsonArray(p_178251_1_, p_178251_2_);
            if (jsonarray.size() != 3) {
                throw new JsonParseException("Expected 3 " + p_178251_2_ + " values, found: " + jsonarray.size());
            }
            float[] afloat = new float[3];
            int i = 0;
            while (i < afloat.length) {
                afloat[i] = JsonUtils.getFloat(jsonarray.get(i), p_178251_2_ + "[" + i + "]");
                ++i;
            }
            return new Vector3f(afloat[0], afloat[1], afloat[2]);
        }
    }
}

