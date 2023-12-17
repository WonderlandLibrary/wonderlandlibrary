// 
// Decompiled by Procyon v0.5.36
// 

package net.optifine.player;

import net.minecraft.util.math.MathHelper;
import net.optifine.entity.model.CustomEntityModelParser;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.client.model.ModelBase;
import net.minecraft.util.ResourceLocation;
import java.util.Iterator;
import java.util.List;
import com.google.gson.JsonElement;
import java.util.Map;
import java.util.ArrayList;
import java.util.HashMap;
import com.google.gson.JsonArray;
import java.awt.Dimension;
import com.google.gson.JsonParseException;
import net.minecraft.src.Config;
import net.optifine.util.Json;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

public class PlayerItemParser
{
    private static JsonParser jsonParser;
    public static final String ITEM_TYPE = "type";
    public static final String ITEM_TEXTURE_SIZE = "textureSize";
    public static final String ITEM_USE_PLAYER_TEXTURE = "usePlayerTexture";
    public static final String ITEM_MODELS = "models";
    public static final String MODEL_ID = "id";
    public static final String MODEL_BASE_ID = "baseId";
    public static final String MODEL_TYPE = "type";
    public static final String MODEL_TEXTURE = "texture";
    public static final String MODEL_TEXTURE_SIZE = "textureSize";
    public static final String MODEL_ATTACH_TO = "attachTo";
    public static final String MODEL_INVERT_AXIS = "invertAxis";
    public static final String MODEL_MIRROR_TEXTURE = "mirrorTexture";
    public static final String MODEL_TRANSLATE = "translate";
    public static final String MODEL_ROTATE = "rotate";
    public static final String MODEL_SCALE = "scale";
    public static final String MODEL_BOXES = "boxes";
    public static final String MODEL_SPRITES = "sprites";
    public static final String MODEL_SUBMODEL = "submodel";
    public static final String MODEL_SUBMODELS = "submodels";
    public static final String BOX_TEXTURE_OFFSET = "textureOffset";
    public static final String BOX_COORDINATES = "coordinates";
    public static final String BOX_SIZE_ADD = "sizeAdd";
    public static final String BOX_UV_DOWN = "uvDown";
    public static final String BOX_UV_UP = "uvUp";
    public static final String BOX_UV_NORTH = "uvNorth";
    public static final String BOX_UV_SOUTH = "uvSouth";
    public static final String BOX_UV_WEST = "uvWest";
    public static final String BOX_UV_EAST = "uvEast";
    public static final String BOX_UV_FRONT = "uvFront";
    public static final String BOX_UV_BACK = "uvBack";
    public static final String BOX_UV_LEFT = "uvLeft";
    public static final String BOX_UV_RIGHT = "uvRight";
    public static final String ITEM_TYPE_MODEL = "PlayerItem";
    public static final String MODEL_TYPE_BOX = "ModelBox";
    
    public static PlayerItemModel parseItemModel(final JsonObject obj) {
        final String s = Json.getString(obj, "type");
        if (!Config.equals(s, "PlayerItem")) {
            throw new JsonParseException("Unknown model type: " + s);
        }
        final int[] aint = Json.parseIntArray(obj.get("textureSize"), 2);
        checkNull(aint, "Missing texture size");
        final Dimension dimension = new Dimension(aint[0], aint[1]);
        final boolean flag = Json.getBoolean(obj, "usePlayerTexture", false);
        final JsonArray jsonarray = (JsonArray)obj.get("models");
        checkNull(jsonarray, "Missing elements");
        final Map map = new HashMap();
        final List list = new ArrayList();
        new ArrayList();
        for (int i = 0; i < jsonarray.size(); ++i) {
            final JsonObject jsonobject = (JsonObject)jsonarray.get(i);
            final String s2 = Json.getString(jsonobject, "baseId");
            if (s2 != null) {
                final JsonObject jsonobject2 = map.get(s2);
                if (jsonobject2 == null) {
                    Config.warn("BaseID not found: " + s2);
                    continue;
                }
                for (final Map.Entry<String, JsonElement> entry : jsonobject2.entrySet()) {
                    if (!jsonobject.has((String)entry.getKey())) {
                        jsonobject.add((String)entry.getKey(), (JsonElement)entry.getValue());
                    }
                }
            }
            final String s3 = Json.getString(jsonobject, "id");
            if (s3 != null) {
                if (!map.containsKey(s3)) {
                    map.put(s3, jsonobject);
                }
                else {
                    Config.warn("Duplicate model ID: " + s3);
                }
            }
            final PlayerItemRenderer playeritemrenderer = parseItemRenderer(jsonobject, dimension);
            if (playeritemrenderer != null) {
                list.add(playeritemrenderer);
            }
        }
        final PlayerItemRenderer[] aplayeritemrenderer = list.toArray(new PlayerItemRenderer[list.size()]);
        return new PlayerItemModel(dimension, flag, aplayeritemrenderer);
    }
    
    private static void checkNull(final Object obj, final String msg) {
        if (obj == null) {
            throw new JsonParseException(msg);
        }
    }
    
    private static ResourceLocation makeResourceLocation(final String texture) {
        final int i = texture.indexOf(58);
        if (i < 0) {
            return new ResourceLocation(texture);
        }
        final String s = texture.substring(0, i);
        final String s2 = texture.substring(i + 1);
        return new ResourceLocation(s, s2);
    }
    
    private static int parseAttachModel(final String attachModelStr) {
        if (attachModelStr == null) {
            return 0;
        }
        if (attachModelStr.equals("body")) {
            return 0;
        }
        if (attachModelStr.equals("head")) {
            return 1;
        }
        if (attachModelStr.equals("leftArm")) {
            return 2;
        }
        if (attachModelStr.equals("rightArm")) {
            return 3;
        }
        if (attachModelStr.equals("leftLeg")) {
            return 4;
        }
        if (attachModelStr.equals("rightLeg")) {
            return 5;
        }
        if (attachModelStr.equals("cape")) {
            return 6;
        }
        Config.warn("Unknown attachModel: " + attachModelStr);
        return 0;
    }
    
    public static PlayerItemRenderer parseItemRenderer(final JsonObject elem, final Dimension textureDim) {
        final String s = Json.getString(elem, "type");
        if (!Config.equals(s, "ModelBox")) {
            Config.warn("Unknown model type: " + s);
            return null;
        }
        final String s2 = Json.getString(elem, "attachTo");
        final int i = parseAttachModel(s2);
        final ModelBase modelbase = new ModelPlayerItem();
        modelbase.textureWidth = textureDim.width;
        modelbase.textureHeight = textureDim.height;
        final ModelRenderer modelrenderer = parseModelRenderer(elem, modelbase, null, null);
        final PlayerItemRenderer playeritemrenderer = new PlayerItemRenderer(i, modelrenderer);
        return playeritemrenderer;
    }
    
    public static ModelRenderer parseModelRenderer(final JsonObject elem, final ModelBase modelBase, final int[] parentTextureSize, final String basePath) {
        final ModelRenderer modelrenderer = new ModelRenderer(modelBase);
        final String s = Json.getString(elem, "id");
        modelrenderer.setId(s);
        final float f = Json.getFloat(elem, "scale", 1.0f);
        modelrenderer.scaleX = f;
        modelrenderer.scaleY = f;
        modelrenderer.scaleZ = f;
        final String s2 = Json.getString(elem, "texture");
        if (s2 != null) {
            modelrenderer.setTextureLocation(CustomEntityModelParser.getResourceLocation(basePath, s2, ".png"));
        }
        int[] aint = Json.parseIntArray(elem.get("textureSize"), 2);
        if (aint == null) {
            aint = parentTextureSize;
        }
        if (aint != null) {
            modelrenderer.setTextureSize(aint[0], aint[1]);
        }
        final String s3 = Json.getString(elem, "invertAxis", "").toLowerCase();
        final boolean flag = s3.contains("x");
        final boolean flag2 = s3.contains("y");
        final boolean flag3 = s3.contains("z");
        final float[] afloat = Json.parseFloatArray(elem.get("translate"), 3, new float[3]);
        if (flag) {
            afloat[0] = -afloat[0];
        }
        if (flag2) {
            afloat[1] = -afloat[1];
        }
        if (flag3) {
            afloat[2] = -afloat[2];
        }
        final float[] afloat2 = Json.parseFloatArray(elem.get("rotate"), 3, new float[3]);
        for (int i = 0; i < afloat2.length; ++i) {
            afloat2[i] = afloat2[i] / 180.0f * MathHelper.PI;
        }
        if (flag) {
            afloat2[0] = -afloat2[0];
        }
        if (flag2) {
            afloat2[1] = -afloat2[1];
        }
        if (flag3) {
            afloat2[2] = -afloat2[2];
        }
        modelrenderer.setRotationPoint(afloat[0], afloat[1], afloat[2]);
        modelrenderer.rotateAngleX = afloat2[0];
        modelrenderer.rotateAngleY = afloat2[1];
        modelrenderer.rotateAngleZ = afloat2[2];
        final String s4 = Json.getString(elem, "mirrorTexture", "").toLowerCase();
        final boolean flag4 = s4.contains("u");
        final boolean flag5 = s4.contains("v");
        if (flag4) {
            modelrenderer.mirror = true;
        }
        if (flag5) {
            modelrenderer.mirrorV = true;
        }
        final JsonArray jsonarray = elem.getAsJsonArray("boxes");
        if (jsonarray != null) {
            for (int j = 0; j < jsonarray.size(); ++j) {
                final JsonObject jsonobject = jsonarray.get(j).getAsJsonObject();
                final int[] aint2 = Json.parseIntArray(jsonobject.get("textureOffset"), 2);
                final int[][] aint3 = parseFaceUvs(jsonobject);
                if (aint2 == null && aint3 == null) {
                    throw new JsonParseException("Texture offset not specified");
                }
                final float[] afloat3 = Json.parseFloatArray(jsonobject.get("coordinates"), 6);
                if (afloat3 == null) {
                    throw new JsonParseException("Coordinates not specified");
                }
                if (flag) {
                    afloat3[0] = -afloat3[0] - afloat3[3];
                }
                if (flag2) {
                    afloat3[1] = -afloat3[1] - afloat3[4];
                }
                if (flag3) {
                    afloat3[2] = -afloat3[2] - afloat3[5];
                }
                final float f2 = Json.getFloat(jsonobject, "sizeAdd", 0.0f);
                if (aint3 != null) {
                    modelrenderer.addBox(aint3, afloat3[0], afloat3[1], afloat3[2], afloat3[3], afloat3[4], afloat3[5], f2);
                }
                else {
                    modelrenderer.setTextureOffset(aint2[0], aint2[1]);
                    modelrenderer.addBox(afloat3[0], afloat3[1], afloat3[2], (int)afloat3[3], (int)afloat3[4], (int)afloat3[5], f2);
                }
            }
        }
        final JsonArray jsonarray2 = elem.getAsJsonArray("sprites");
        if (jsonarray2 != null) {
            for (int k = 0; k < jsonarray2.size(); ++k) {
                final JsonObject jsonobject2 = jsonarray2.get(k).getAsJsonObject();
                final int[] aint4 = Json.parseIntArray(jsonobject2.get("textureOffset"), 2);
                if (aint4 == null) {
                    throw new JsonParseException("Texture offset not specified");
                }
                final float[] afloat4 = Json.parseFloatArray(jsonobject2.get("coordinates"), 6);
                if (afloat4 == null) {
                    throw new JsonParseException("Coordinates not specified");
                }
                if (flag) {
                    afloat4[0] = -afloat4[0] - afloat4[3];
                }
                if (flag2) {
                    afloat4[1] = -afloat4[1] - afloat4[4];
                }
                if (flag3) {
                    afloat4[2] = -afloat4[2] - afloat4[5];
                }
                final float f3 = Json.getFloat(jsonobject2, "sizeAdd", 0.0f);
                modelrenderer.setTextureOffset(aint4[0], aint4[1]);
                modelrenderer.addSprite(afloat4[0], afloat4[1], afloat4[2], (int)afloat4[3], (int)afloat4[4], (int)afloat4[5], f3);
            }
        }
        final JsonObject jsonobject3 = (JsonObject)elem.get("submodel");
        if (jsonobject3 != null) {
            final ModelRenderer modelrenderer2 = parseModelRenderer(jsonobject3, modelBase, aint, basePath);
            modelrenderer.addChild(modelrenderer2);
        }
        final JsonArray jsonarray3 = (JsonArray)elem.get("submodels");
        if (jsonarray3 != null) {
            for (int l = 0; l < jsonarray3.size(); ++l) {
                final JsonObject jsonobject4 = (JsonObject)jsonarray3.get(l);
                final ModelRenderer modelrenderer3 = parseModelRenderer(jsonobject4, modelBase, aint, basePath);
                if (modelrenderer3.getId() != null) {
                    final ModelRenderer modelrenderer4 = modelrenderer.getChild(modelrenderer3.getId());
                    if (modelrenderer4 != null) {
                        Config.warn("Duplicate model ID: " + modelrenderer3.getId());
                    }
                }
                modelrenderer.addChild(modelrenderer3);
            }
        }
        return modelrenderer;
    }
    
    private static int[][] parseFaceUvs(final JsonObject box) {
        final int[][] aint = { Json.parseIntArray(box.get("uvDown"), 4), Json.parseIntArray(box.get("uvUp"), 4), Json.parseIntArray(box.get("uvNorth"), 4), Json.parseIntArray(box.get("uvSouth"), 4), Json.parseIntArray(box.get("uvWest"), 4), Json.parseIntArray(box.get("uvEast"), 4) };
        if (aint[2] == null) {
            aint[2] = Json.parseIntArray(box.get("uvFront"), 4);
        }
        if (aint[3] == null) {
            aint[3] = Json.parseIntArray(box.get("uvBack"), 4);
        }
        if (aint[4] == null) {
            aint[4] = Json.parseIntArray(box.get("uvLeft"), 4);
        }
        if (aint[5] == null) {
            aint[5] = Json.parseIntArray(box.get("uvRight"), 4);
        }
        boolean flag = false;
        for (int i = 0; i < aint.length; ++i) {
            if (aint[i] != null) {
                flag = true;
            }
        }
        if (!flag) {
            return null;
        }
        return aint;
    }
    
    static {
        PlayerItemParser.jsonParser = new JsonParser();
    }
}
