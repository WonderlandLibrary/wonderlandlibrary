// 
// Decompiled by Procyon v0.5.36
// 

package net.minecraft.client.renderer.block.model;

import com.google.gson.JsonArray;
import com.google.common.collect.Maps;
import java.util.Locale;
import net.minecraft.util.math.MathHelper;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import net.minecraft.util.JsonUtils;
import com.google.gson.JsonDeserializationContext;
import java.lang.reflect.Type;
import com.google.gson.JsonElement;
import com.google.gson.JsonDeserializer;
import java.util.Iterator;
import javax.annotation.Nullable;
import net.minecraft.util.EnumFacing;
import java.util.Map;
import org.lwjgl.util.vector.Vector3f;

public class BlockPart
{
    public final Vector3f positionFrom;
    public final Vector3f positionTo;
    public final Map<EnumFacing, BlockPartFace> mapFaces;
    public final BlockPartRotation partRotation;
    public final boolean shade;
    
    public BlockPart(final Vector3f positionFromIn, final Vector3f positionToIn, final Map<EnumFacing, BlockPartFace> mapFacesIn, @Nullable final BlockPartRotation partRotationIn, final boolean shadeIn) {
        this.positionFrom = positionFromIn;
        this.positionTo = positionToIn;
        this.mapFaces = mapFacesIn;
        this.partRotation = partRotationIn;
        this.shade = shadeIn;
        this.setDefaultUvs();
    }
    
    private void setDefaultUvs() {
        for (final Map.Entry<EnumFacing, BlockPartFace> entry : this.mapFaces.entrySet()) {
            final float[] afloat = this.getFaceUvs(entry.getKey());
            entry.getValue().blockFaceUV.setUvs(afloat);
        }
    }
    
    private float[] getFaceUvs(final EnumFacing facing) {
        switch (facing) {
            case DOWN: {
                return new float[] { this.positionFrom.x, 16.0f - this.positionTo.z, this.positionTo.x, 16.0f - this.positionFrom.z };
            }
            case UP: {
                return new float[] { this.positionFrom.x, this.positionFrom.z, this.positionTo.x, this.positionTo.z };
            }
            default: {
                return new float[] { 16.0f - this.positionTo.x, 16.0f - this.positionTo.y, 16.0f - this.positionFrom.x, 16.0f - this.positionFrom.y };
            }
            case SOUTH: {
                return new float[] { this.positionFrom.x, 16.0f - this.positionTo.y, this.positionTo.x, 16.0f - this.positionFrom.y };
            }
            case WEST: {
                return new float[] { this.positionFrom.z, 16.0f - this.positionTo.y, this.positionTo.z, 16.0f - this.positionFrom.y };
            }
            case EAST: {
                return new float[] { 16.0f - this.positionTo.z, 16.0f - this.positionTo.y, 16.0f - this.positionFrom.z, 16.0f - this.positionFrom.y };
            }
        }
    }
    
    static class Deserializer implements JsonDeserializer<BlockPart>
    {
        public BlockPart deserialize(final JsonElement p_deserialize_1_, final Type p_deserialize_2_, final JsonDeserializationContext p_deserialize_3_) throws JsonParseException {
            final JsonObject jsonobject = p_deserialize_1_.getAsJsonObject();
            final Vector3f vector3f = this.parsePositionFrom(jsonobject);
            final Vector3f vector3f2 = this.parsePositionTo(jsonobject);
            final BlockPartRotation blockpartrotation = this.parseRotation(jsonobject);
            final Map<EnumFacing, BlockPartFace> map = this.parseFacesCheck(p_deserialize_3_, jsonobject);
            if (jsonobject.has("shade") && !JsonUtils.isBoolean(jsonobject, "shade")) {
                throw new JsonParseException("Expected shade to be a Boolean");
            }
            final boolean flag = JsonUtils.getBoolean(jsonobject, "shade", true);
            return new BlockPart(vector3f, vector3f2, map, blockpartrotation, flag);
        }
        
        @Nullable
        private BlockPartRotation parseRotation(final JsonObject object) {
            BlockPartRotation blockpartrotation = null;
            if (object.has("rotation")) {
                final JsonObject jsonobject = JsonUtils.getJsonObject(object, "rotation");
                final Vector3f vector3f = this.parsePosition(jsonobject, "origin");
                vector3f.scale(0.0625f);
                final EnumFacing.Axis enumfacing$axis = this.parseAxis(jsonobject);
                final float f = this.parseAngle(jsonobject);
                final boolean flag = JsonUtils.getBoolean(jsonobject, "rescale", false);
                blockpartrotation = new BlockPartRotation(vector3f, enumfacing$axis, f, flag);
            }
            return blockpartrotation;
        }
        
        private float parseAngle(final JsonObject object) {
            final float f = JsonUtils.getFloat(object, "angle");
            if (f != 0.0f && MathHelper.abs(f) != 22.5f && MathHelper.abs(f) != 45.0f) {
                throw new JsonParseException("Invalid rotation " + f + " found, only -45/-22.5/0/22.5/45 allowed");
            }
            return f;
        }
        
        private EnumFacing.Axis parseAxis(final JsonObject object) {
            final String s = JsonUtils.getString(object, "axis");
            final EnumFacing.Axis enumfacing$axis = EnumFacing.Axis.byName(s.toLowerCase(Locale.ROOT));
            if (enumfacing$axis == null) {
                throw new JsonParseException("Invalid rotation axis: " + s);
            }
            return enumfacing$axis;
        }
        
        private Map<EnumFacing, BlockPartFace> parseFacesCheck(final JsonDeserializationContext deserializationContext, final JsonObject object) {
            final Map<EnumFacing, BlockPartFace> map = this.parseFaces(deserializationContext, object);
            if (map.isEmpty()) {
                throw new JsonParseException("Expected between 1 and 6 unique faces, got 0");
            }
            return map;
        }
        
        private Map<EnumFacing, BlockPartFace> parseFaces(final JsonDeserializationContext deserializationContext, final JsonObject object) {
            final Map<EnumFacing, BlockPartFace> map = (Map<EnumFacing, BlockPartFace>)Maps.newEnumMap((Class)EnumFacing.class);
            final JsonObject jsonobject = JsonUtils.getJsonObject(object, "faces");
            for (final Map.Entry<String, JsonElement> entry : jsonobject.entrySet()) {
                final EnumFacing enumfacing = this.parseEnumFacing(entry.getKey());
                map.put(enumfacing, (BlockPartFace)deserializationContext.deserialize((JsonElement)entry.getValue(), (Type)BlockPartFace.class));
            }
            return map;
        }
        
        private EnumFacing parseEnumFacing(final String name) {
            final EnumFacing enumfacing = EnumFacing.byName(name);
            if (enumfacing == null) {
                throw new JsonParseException("Unknown facing: " + name);
            }
            return enumfacing;
        }
        
        private Vector3f parsePositionTo(final JsonObject object) {
            final Vector3f vector3f = this.parsePosition(object, "to");
            if (vector3f.x >= -16.0f && vector3f.y >= -16.0f && vector3f.z >= -16.0f && vector3f.x <= 32.0f && vector3f.y <= 32.0f && vector3f.z <= 32.0f) {
                return vector3f;
            }
            throw new JsonParseException("'to' specifier exceeds the allowed boundaries: " + vector3f);
        }
        
        private Vector3f parsePositionFrom(final JsonObject object) {
            final Vector3f vector3f = this.parsePosition(object, "from");
            if (vector3f.x >= -16.0f && vector3f.y >= -16.0f && vector3f.z >= -16.0f && vector3f.x <= 32.0f && vector3f.y <= 32.0f && vector3f.z <= 32.0f) {
                return vector3f;
            }
            throw new JsonParseException("'from' specifier exceeds the allowed boundaries: " + vector3f);
        }
        
        private Vector3f parsePosition(final JsonObject object, final String memberName) {
            final JsonArray jsonarray = JsonUtils.getJsonArray(object, memberName);
            if (jsonarray.size() != 3) {
                throw new JsonParseException("Expected 3 " + memberName + " values, found: " + jsonarray.size());
            }
            final float[] afloat = new float[3];
            for (int i = 0; i < afloat.length; ++i) {
                afloat[i] = JsonUtils.getFloat(jsonarray.get(i), memberName + "[" + i + "]");
            }
            return new Vector3f(afloat[0], afloat[1], afloat[2]);
        }
    }
}
