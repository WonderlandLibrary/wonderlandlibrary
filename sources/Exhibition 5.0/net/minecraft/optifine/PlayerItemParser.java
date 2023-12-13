// 
// Decompiled by Procyon v0.5.30
// 

package net.minecraft.optifine;

import net.minecraft.client.model.ModelRenderer;
import net.minecraft.client.model.ModelBase;
import net.minecraft.util.ResourceLocation;
import java.util.Iterator;
import java.util.Set;
import com.google.gson.JsonElement;
import java.util.Map;
import java.util.ArrayList;
import java.util.HashMap;
import com.google.gson.JsonArray;
import java.awt.Dimension;
import com.google.gson.JsonParseException;
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
    public static final String ITEM_TYPE_MODEL = "PlayerItem";
    public static final String MODEL_TYPE_BOX = "ModelBox";
    
    public static PlayerItemModel parseItemModel(final JsonObject obj) {
        final String type = Json.getString(obj, "type");
        if (!Config.equals(type, "PlayerItem")) {
            throw new JsonParseException("Unknown model type: " + type);
        }
        final int[] textureSize = Json.parseIntArray(obj.get("textureSize"), 2);
        checkNull(textureSize, "Missing texture size");
        final Dimension textureDim = new Dimension(textureSize[0], textureSize[1]);
        final boolean usePlayerTexture = Json.getBoolean(obj, "usePlayerTexture", false);
        final JsonArray models = (JsonArray)obj.get("models");
        checkNull(models, "Missing elements");
        final HashMap mapModelJsons = new HashMap();
        final ArrayList listModels = new ArrayList();
        new ArrayList();
        for (int modelRenderers = 0; modelRenderers < models.size(); ++modelRenderers) {
            final JsonObject elem = (JsonObject)models.get(modelRenderers);
            final String baseId = Json.getString(elem, "baseId");
            if (baseId != null) {
                final JsonObject id = mapModelJsons.get(baseId);
                if (id == null) {
                    Config.warn("BaseID not found: " + baseId);
                    continue;
                }
                final Set mr = id.entrySet();
                for (final Map.Entry entry : mr) {
                    if (!elem.has((String)entry.getKey())) {
                        elem.add((String)entry.getKey(), (JsonElement)entry.getValue());
                    }
                }
            }
            final String var17 = Json.getString(elem, "id");
            if (var17 != null) {
                if (!mapModelJsons.containsKey(var17)) {
                    mapModelJsons.put(var17, elem);
                }
                else {
                    Config.warn("Duplicate model ID: " + var17);
                }
            }
            final PlayerItemRenderer var18 = parseItemRenderer(elem, textureDim);
            if (var18 != null) {
                listModels.add(var18);
            }
        }
        final PlayerItemRenderer[] var19 = listModels.toArray(new PlayerItemRenderer[listModels.size()]);
        return new PlayerItemModel(textureDim, usePlayerTexture, var19);
    }
    
    private static void checkNull(final Object obj, final String msg) {
        if (obj == null) {
            throw new JsonParseException(msg);
        }
    }
    
    private static ResourceLocation makeResourceLocation(final String texture) {
        final int pos = texture.indexOf(58);
        if (pos < 0) {
            return new ResourceLocation(texture);
        }
        final String domain = texture.substring(0, pos);
        final String path = texture.substring(pos + 1);
        return new ResourceLocation(domain, path);
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
    
    private static PlayerItemRenderer parseItemRenderer(final JsonObject elem, final Dimension textureDim) {
        final String type = Json.getString(elem, "type");
        if (!Config.equals(type, "ModelBox")) {
            Config.warn("Unknown model type: " + type);
            return null;
        }
        final String attachToStr = Json.getString(elem, "attachTo");
        final int attachTo = parseAttachModel(attachToStr);
        final float scale = Json.getFloat(elem, "scale", 1.0f);
        final ModelPlayerItem modelBase = new ModelPlayerItem();
        modelBase.textureWidth = textureDim.width;
        modelBase.textureHeight = textureDim.height;
        final ModelRenderer mr = parseModelRenderer(elem, modelBase);
        final PlayerItemRenderer pir = new PlayerItemRenderer(attachTo, scale, mr);
        return pir;
    }
    
    private static ModelRenderer parseModelRenderer(final JsonObject elem, final ModelBase modelBase) {
        final ModelRenderer mr = new ModelRenderer(modelBase);
        final String invertAxis = Json.getString(elem, "invertAxis", "").toLowerCase();
        final boolean invertX = invertAxis.contains("x");
        final boolean invertY = invertAxis.contains("y");
        final boolean invertZ = invertAxis.contains("z");
        final float[] translate = Json.parseFloatArray(elem.get("translate"), 3, new float[3]);
        if (invertX) {
            translate[0] = -translate[0];
        }
        if (invertY) {
            translate[1] = -translate[1];
        }
        if (invertZ) {
            translate[2] = -translate[2];
        }
        final float[] rotateAngles = Json.parseFloatArray(elem.get("rotate"), 3, new float[3]);
        for (int mirrorTexture = 0; mirrorTexture < rotateAngles.length; ++mirrorTexture) {
            rotateAngles[mirrorTexture] = rotateAngles[mirrorTexture] / 180.0f * 3.1415927f;
        }
        if (invertX) {
            rotateAngles[0] = -rotateAngles[0];
        }
        if (invertY) {
            rotateAngles[1] = -rotateAngles[1];
        }
        if (invertZ) {
            rotateAngles[2] = -rotateAngles[2];
        }
        mr.setRotationPoint(translate[0], translate[1], translate[2]);
        mr.rotateAngleX = rotateAngles[0];
        mr.rotateAngleY = rotateAngles[1];
        mr.rotateAngleZ = rotateAngles[2];
        final String var19 = Json.getString(elem, "mirrorTexture", "").toLowerCase();
        final boolean invertU = var19.contains("u");
        final boolean invertV = var19.contains("v");
        if (invertU) {
            mr.mirror = true;
        }
        if (invertV) {
            mr.mirrorV = true;
        }
        final JsonArray boxes = elem.getAsJsonArray("boxes");
        if (boxes != null) {
            for (int sprites = 0; sprites < boxes.size(); ++sprites) {
                final JsonObject submodel = boxes.get(sprites).getAsJsonObject();
                final int[] submodels = Json.parseIntArray(submodel.get("textureOffset"), 2);
                if (submodels == null) {
                    throw new JsonParseException("Texture offset not specified");
                }
                final float[] i = Json.parseFloatArray(submodel.get("coordinates"), 6);
                if (i == null) {
                    throw new JsonParseException("Coordinates not specified");
                }
                if (invertX) {
                    i[0] = -i[0] - i[3];
                }
                if (invertY) {
                    i[1] = -i[1] - i[4];
                }
                if (invertZ) {
                    i[2] = -i[2] - i[5];
                }
                final float sm = Json.getFloat(submodel, "sizeAdd", 0.0f);
                mr.setTextureOffset(submodels[0], submodels[1]);
                mr.addBox(i[0], i[1], i[2], (int)i[3], (int)i[4], (int)i[5], sm);
            }
        }
        final JsonArray var20 = elem.getAsJsonArray("sprites");
        if (var20 != null) {
            for (int var21 = 0; var21 < var20.size(); ++var21) {
                final JsonObject var22 = var20.get(var21).getAsJsonObject();
                final int[] var23 = Json.parseIntArray(var22.get("textureOffset"), 2);
                if (var23 == null) {
                    throw new JsonParseException("Texture offset not specified");
                }
                final float[] var24 = Json.parseFloatArray(var22.get("coordinates"), 6);
                if (var24 == null) {
                    throw new JsonParseException("Coordinates not specified");
                }
                if (invertX) {
                    var24[0] = -var24[0] - var24[3];
                }
                if (invertY) {
                    var24[1] = -var24[1] - var24[4];
                }
                if (invertZ) {
                    var24[2] = -var24[2] - var24[5];
                }
                final float subMr = Json.getFloat(var22, "sizeAdd", 0.0f);
                mr.setTextureOffset(var23[0], var23[1]);
                mr.addSprite(var24[0], var24[1], var24[2], (int)var24[3], (int)var24[4], (int)var24[5], subMr);
            }
        }
        final JsonObject submodel = (JsonObject)elem.get("submodel");
        if (submodel != null) {
            final ModelRenderer var25 = parseModelRenderer(submodel, modelBase);
            mr.addChild(var25);
        }
        final JsonArray var26 = (JsonArray)elem.get("submodels");
        if (var26 != null) {
            for (int var27 = 0; var27 < var26.size(); ++var27) {
                final JsonObject var28 = (JsonObject)var26.get(var27);
                final ModelRenderer var29 = parseModelRenderer(var28, modelBase);
                mr.addChild(var29);
            }
        }
        return mr;
    }
    
    static {
        PlayerItemParser.jsonParser = new JsonParser();
    }
}
