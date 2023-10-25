/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.google.common.collect.Maps
 *  com.google.gson.JsonArray
 *  com.google.gson.JsonDeserializationContext
 *  com.google.gson.JsonDeserializer
 *  com.google.gson.JsonElement
 *  com.google.gson.JsonObject
 *  com.google.gson.JsonParseException
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
import java.util.Map;
import net.minecraft.client.renderer.block.model.BlockPartFace;
import net.minecraft.client.renderer.block.model.BlockPartRotation;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.JsonUtils;
import net.minecraft.util.MathHelper;
import org.lwjgl.util.vector.Vector3f;

public class BlockPart {
    public final boolean shade;
    public final BlockPartRotation partRotation;
    public final Vector3f positionFrom;
    public final Map<EnumFacing, BlockPartFace> mapFaces;
    public final Vector3f positionTo;

    public BlockPart(Vector3f vector3f, Vector3f vector3f2, Map<EnumFacing, BlockPartFace> map, BlockPartRotation blockPartRotation, boolean bl) {
        this.positionFrom = vector3f;
        this.positionTo = vector3f2;
        this.mapFaces = map;
        this.partRotation = blockPartRotation;
        this.shade = bl;
        this.setDefaultUvs();
    }

    private float[] getFaceUvs(EnumFacing enumFacing) {
        float[] fArray;
        switch (enumFacing) {
            case DOWN: 
            case UP: {
                fArray = new float[]{this.positionFrom.x, this.positionFrom.z, this.positionTo.x, this.positionTo.z};
                break;
            }
            case NORTH: 
            case SOUTH: {
                fArray = new float[]{this.positionFrom.x, 16.0f - this.positionTo.y, this.positionTo.x, 16.0f - this.positionFrom.y};
                break;
            }
            case WEST: 
            case EAST: {
                fArray = new float[]{this.positionFrom.z, 16.0f - this.positionTo.y, this.positionTo.z, 16.0f - this.positionFrom.y};
                break;
            }
            default: {
                throw new NullPointerException();
            }
        }
        return fArray;
    }

    private void setDefaultUvs() {
        for (Map.Entry<EnumFacing, BlockPartFace> entry : this.mapFaces.entrySet()) {
            float[] fArray = this.getFaceUvs(entry.getKey());
            entry.getValue().blockFaceUV.setUvs(fArray);
        }
    }

    static class Deserializer
    implements JsonDeserializer<BlockPart> {
        private Map<EnumFacing, BlockPartFace> parseFaces(JsonDeserializationContext jsonDeserializationContext, JsonObject jsonObject) {
            EnumMap enumMap = Maps.newEnumMap(EnumFacing.class);
            JsonObject jsonObject2 = JsonUtils.getJsonObject(jsonObject, "faces");
            for (Map.Entry entry : jsonObject2.entrySet()) {
                EnumFacing enumFacing = this.parseEnumFacing((String)entry.getKey());
                enumMap.put(enumFacing, (BlockPartFace)jsonDeserializationContext.deserialize((JsonElement)entry.getValue(), BlockPartFace.class));
            }
            return enumMap;
        }

        public BlockPart deserialize(JsonElement jsonElement, Type type, JsonDeserializationContext jsonDeserializationContext) throws JsonParseException {
            JsonObject jsonObject = jsonElement.getAsJsonObject();
            Vector3f vector3f = this.parsePositionFrom(jsonObject);
            Vector3f vector3f2 = this.parsePositionTo(jsonObject);
            BlockPartRotation blockPartRotation = this.parseRotation(jsonObject);
            Map<EnumFacing, BlockPartFace> map = this.parseFacesCheck(jsonDeserializationContext, jsonObject);
            if (jsonObject.has("shade") && !JsonUtils.isBoolean(jsonObject, "shade")) {
                throw new JsonParseException("Expected shade to be a Boolean");
            }
            boolean bl = JsonUtils.getBoolean(jsonObject, "shade", true);
            return new BlockPart(vector3f, vector3f2, map, blockPartRotation, bl);
        }

        private Vector3f parsePositionTo(JsonObject jsonObject) {
            Vector3f vector3f = this.parsePosition(jsonObject, "to");
            if (vector3f.x >= -16.0f && vector3f.y >= -16.0f && vector3f.z >= -16.0f && vector3f.x <= 32.0f && vector3f.y <= 32.0f && vector3f.z <= 32.0f) {
                return vector3f;
            }
            throw new JsonParseException("'to' specifier exceeds the allowed boundaries: " + vector3f);
        }

        private EnumFacing.Axis parseAxis(JsonObject jsonObject) {
            String string = JsonUtils.getString(jsonObject, "axis");
            EnumFacing.Axis axis = EnumFacing.Axis.byName(string.toLowerCase());
            if (axis == null) {
                throw new JsonParseException("Invalid rotation axis: " + string);
            }
            return axis;
        }

        private float parseAngle(JsonObject jsonObject) {
            float f = JsonUtils.getFloat(jsonObject, "angle");
            if (f != 0.0f && MathHelper.abs(f) != 22.5f && MathHelper.abs(f) != 45.0f) {
                throw new JsonParseException("Invalid rotation " + f + " found, only -45/-22.5/0/22.5/45 allowed");
            }
            return f;
        }

        private Vector3f parsePositionFrom(JsonObject jsonObject) {
            Vector3f vector3f = this.parsePosition(jsonObject, "from");
            if (vector3f.x >= -16.0f && vector3f.y >= -16.0f && vector3f.z >= -16.0f && vector3f.x <= 32.0f && vector3f.y <= 32.0f && vector3f.z <= 32.0f) {
                return vector3f;
            }
            throw new JsonParseException("'from' specifier exceeds the allowed boundaries: " + vector3f);
        }

        private BlockPartRotation parseRotation(JsonObject jsonObject) {
            BlockPartRotation blockPartRotation = null;
            if (jsonObject.has("rotation")) {
                JsonObject jsonObject2 = JsonUtils.getJsonObject(jsonObject, "rotation");
                Vector3f vector3f = this.parsePosition(jsonObject2, "origin");
                vector3f.scale(0.0625f);
                EnumFacing.Axis axis = this.parseAxis(jsonObject2);
                float f = this.parseAngle(jsonObject2);
                boolean bl = JsonUtils.getBoolean(jsonObject2, "rescale", false);
                blockPartRotation = new BlockPartRotation(vector3f, axis, f, bl);
            }
            return blockPartRotation;
        }

        private Map<EnumFacing, BlockPartFace> parseFacesCheck(JsonDeserializationContext jsonDeserializationContext, JsonObject jsonObject) {
            Map<EnumFacing, BlockPartFace> map = this.parseFaces(jsonDeserializationContext, jsonObject);
            if (map.isEmpty()) {
                throw new JsonParseException("Expected between 1 and 6 unique faces, got 0");
            }
            return map;
        }

        private EnumFacing parseEnumFacing(String string) {
            EnumFacing enumFacing = EnumFacing.byName(string);
            if (enumFacing == null) {
                throw new JsonParseException("Unknown facing: " + string);
            }
            return enumFacing;
        }

        Deserializer() {
        }

        private Vector3f parsePosition(JsonObject jsonObject, String string) {
            JsonArray jsonArray = JsonUtils.getJsonArray(jsonObject, string);
            if (jsonArray.size() != 3) {
                throw new JsonParseException("Expected 3 " + string + " values, found: " + jsonArray.size());
            }
            float[] fArray = new float[3];
            int n = 0;
            while (n < fArray.length) {
                fArray[n] = JsonUtils.getFloat(jsonArray.get(n), String.valueOf(string) + "[" + n + "]");
                ++n;
            }
            return new Vector3f(fArray[0], fArray[1], fArray[2]);
        }
    }
}

