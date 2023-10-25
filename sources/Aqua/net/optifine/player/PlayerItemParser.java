package net.optifine.player;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.google.gson.JsonParser;
import java.awt.Dimension;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import net.minecraft.client.model.ModelBase;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.src.Config;
import net.minecraft.util.MathHelper;
import net.minecraft.util.ResourceLocation;
import net.optifine.entity.model.CustomEntityModelParser;
import net.optifine.player.ModelPlayerItem;
import net.optifine.player.PlayerItemModel;
import net.optifine.player.PlayerItemRenderer;
import net.optifine.util.Json;

public class PlayerItemParser {
    private static JsonParser jsonParser = new JsonParser();
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

    public static PlayerItemModel parseItemModel(JsonObject obj) {
        String s = Json.getString((JsonObject)obj, (String)"type");
        if (!Config.equals((Object)s, (Object)ITEM_TYPE_MODEL)) {
            throw new JsonParseException("Unknown model type: " + s);
        }
        int[] aint = Json.parseIntArray((JsonElement)obj.get("textureSize"), (int)2);
        PlayerItemParser.checkNull(aint, "Missing texture size");
        Dimension dimension = new Dimension(aint[0], aint[1]);
        boolean flag = Json.getBoolean((JsonObject)obj, (String)ITEM_USE_PLAYER_TEXTURE, (boolean)false);
        JsonArray jsonarray = (JsonArray)obj.get(ITEM_MODELS);
        PlayerItemParser.checkNull(jsonarray, "Missing elements");
        HashMap map = new HashMap();
        ArrayList list = new ArrayList();
        new ArrayList();
        for (int i = 0; i < jsonarray.size(); ++i) {
            PlayerItemRenderer playeritemrenderer;
            String s2;
            JsonObject jsonobject = (JsonObject)jsonarray.get(i);
            String s1 = Json.getString((JsonObject)jsonobject, (String)MODEL_BASE_ID);
            if (s1 != null) {
                JsonObject jsonobject1 = (JsonObject)map.get((Object)s1);
                if (jsonobject1 == null) {
                    Config.warn((String)("BaseID not found: " + s1));
                    continue;
                }
                for (Map.Entry entry : jsonobject1.entrySet()) {
                    if (jsonobject.has((String)entry.getKey())) continue;
                    jsonobject.add((String)entry.getKey(), (JsonElement)entry.getValue());
                }
            }
            if ((s2 = Json.getString((JsonObject)jsonobject, (String)MODEL_ID)) != null) {
                if (!map.containsKey((Object)s2)) {
                    map.put((Object)s2, (Object)jsonobject);
                } else {
                    Config.warn((String)("Duplicate model ID: " + s2));
                }
            }
            if ((playeritemrenderer = PlayerItemParser.parseItemRenderer(jsonobject, dimension)) == null) continue;
            list.add((Object)playeritemrenderer);
        }
        PlayerItemRenderer[] aplayeritemrenderer = (PlayerItemRenderer[])list.toArray((Object[])new PlayerItemRenderer[list.size()]);
        return new PlayerItemModel(dimension, flag, aplayeritemrenderer);
    }

    private static void checkNull(Object obj, String msg) {
        if (obj == null) {
            throw new JsonParseException(msg);
        }
    }

    private static ResourceLocation makeResourceLocation(String texture) {
        int i = texture.indexOf(58);
        if (i < 0) {
            return new ResourceLocation(texture);
        }
        String s = texture.substring(0, i);
        String s1 = texture.substring(i + 1);
        return new ResourceLocation(s, s1);
    }

    private static int parseAttachModel(String attachModelStr) {
        if (attachModelStr == null) {
            return 0;
        }
        if (attachModelStr.equals((Object)"body")) {
            return 0;
        }
        if (attachModelStr.equals((Object)"head")) {
            return 1;
        }
        if (attachModelStr.equals((Object)"leftArm")) {
            return 2;
        }
        if (attachModelStr.equals((Object)"rightArm")) {
            return 3;
        }
        if (attachModelStr.equals((Object)"leftLeg")) {
            return 4;
        }
        if (attachModelStr.equals((Object)"rightLeg")) {
            return 5;
        }
        if (attachModelStr.equals((Object)"cape")) {
            return 6;
        }
        Config.warn((String)("Unknown attachModel: " + attachModelStr));
        return 0;
    }

    public static PlayerItemRenderer parseItemRenderer(JsonObject elem, Dimension textureDim) {
        String s = Json.getString((JsonObject)elem, (String)"type");
        if (!Config.equals((Object)s, (Object)MODEL_TYPE_BOX)) {
            Config.warn((String)("Unknown model type: " + s));
            return null;
        }
        String s1 = Json.getString((JsonObject)elem, (String)MODEL_ATTACH_TO);
        int i = PlayerItemParser.parseAttachModel(s1);
        ModelPlayerItem modelbase = new ModelPlayerItem();
        modelbase.textureWidth = textureDim.width;
        modelbase.textureHeight = textureDim.height;
        ModelRenderer modelrenderer = PlayerItemParser.parseModelRenderer(elem, (ModelBase)modelbase, null, null);
        PlayerItemRenderer playeritemrenderer = new PlayerItemRenderer(i, modelrenderer);
        return playeritemrenderer;
    }

    public static ModelRenderer parseModelRenderer(JsonObject elem, ModelBase modelBase, int[] parentTextureSize, String basePath) {
        JsonArray jsonarray2;
        JsonObject jsonobject1;
        JsonArray jsonarray1;
        JsonArray jsonarray;
        int[] aint;
        float f;
        ModelRenderer modelrenderer = new ModelRenderer(modelBase);
        String s = Json.getString((JsonObject)elem, (String)MODEL_ID);
        modelrenderer.setId(s);
        modelrenderer.scaleX = f = Json.getFloat((JsonObject)elem, (String)MODEL_SCALE, (float)1.0f);
        modelrenderer.scaleY = f;
        modelrenderer.scaleZ = f;
        String s1 = Json.getString((JsonObject)elem, (String)MODEL_TEXTURE);
        if (s1 != null) {
            modelrenderer.setTextureLocation(CustomEntityModelParser.getResourceLocation((String)basePath, (String)s1, (String)".png"));
        }
        if ((aint = Json.parseIntArray((JsonElement)elem.get("textureSize"), (int)2)) == null) {
            aint = parentTextureSize;
        }
        if (aint != null) {
            modelrenderer.setTextureSize(aint[0], aint[1]);
        }
        String s2 = Json.getString((JsonObject)elem, (String)MODEL_INVERT_AXIS, (String)"").toLowerCase();
        boolean flag = s2.contains((CharSequence)"x");
        boolean flag1 = s2.contains((CharSequence)"y");
        boolean flag2 = s2.contains((CharSequence)"z");
        float[] afloat = Json.parseFloatArray((JsonElement)elem.get(MODEL_TRANSLATE), (int)3, (float[])new float[3]);
        if (flag) {
            afloat[0] = -afloat[0];
        }
        if (flag1) {
            afloat[1] = -afloat[1];
        }
        if (flag2) {
            afloat[2] = -afloat[2];
        }
        float[] afloat1 = Json.parseFloatArray((JsonElement)elem.get(MODEL_ROTATE), (int)3, (float[])new float[3]);
        for (int i = 0; i < afloat1.length; ++i) {
            afloat1[i] = afloat1[i] / 180.0f * MathHelper.PI;
        }
        if (flag) {
            afloat1[0] = -afloat1[0];
        }
        if (flag1) {
            afloat1[1] = -afloat1[1];
        }
        if (flag2) {
            afloat1[2] = -afloat1[2];
        }
        modelrenderer.setRotationPoint(afloat[0], afloat[1], afloat[2]);
        modelrenderer.rotateAngleX = afloat1[0];
        modelrenderer.rotateAngleY = afloat1[1];
        modelrenderer.rotateAngleZ = afloat1[2];
        String s3 = Json.getString((JsonObject)elem, (String)MODEL_MIRROR_TEXTURE, (String)"").toLowerCase();
        boolean flag3 = s3.contains((CharSequence)"u");
        boolean flag4 = s3.contains((CharSequence)"v");
        if (flag3) {
            modelrenderer.mirror = true;
        }
        if (flag4) {
            modelrenderer.mirrorV = true;
        }
        if ((jsonarray = elem.getAsJsonArray(MODEL_BOXES)) != null) {
            for (int j = 0; j < jsonarray.size(); ++j) {
                JsonObject jsonobject = jsonarray.get(j).getAsJsonObject();
                int[] aint1 = Json.parseIntArray((JsonElement)jsonobject.get(BOX_TEXTURE_OFFSET), (int)2);
                int[][] aint2 = PlayerItemParser.parseFaceUvs(jsonobject);
                if (aint1 == null && aint2 == null) {
                    throw new JsonParseException("Texture offset not specified");
                }
                float[] afloat2 = Json.parseFloatArray((JsonElement)jsonobject.get(BOX_COORDINATES), (int)6);
                if (afloat2 == null) {
                    throw new JsonParseException("Coordinates not specified");
                }
                if (flag) {
                    afloat2[0] = -afloat2[0] - afloat2[3];
                }
                if (flag1) {
                    afloat2[1] = -afloat2[1] - afloat2[4];
                }
                if (flag2) {
                    afloat2[2] = -afloat2[2] - afloat2[5];
                }
                float f1 = Json.getFloat((JsonObject)jsonobject, (String)BOX_SIZE_ADD, (float)0.0f);
                if (aint2 != null) {
                    modelrenderer.addBox(aint2, afloat2[0], afloat2[1], afloat2[2], afloat2[3], afloat2[4], afloat2[5], f1);
                    continue;
                }
                modelrenderer.setTextureOffset(aint1[0], aint1[1]);
                modelrenderer.addBox(afloat2[0], afloat2[1], afloat2[2], (int)afloat2[3], (int)afloat2[4], (int)afloat2[5], f1);
            }
        }
        if ((jsonarray1 = elem.getAsJsonArray(MODEL_SPRITES)) != null) {
            for (int k = 0; k < jsonarray1.size(); ++k) {
                JsonObject jsonobject2 = jsonarray1.get(k).getAsJsonObject();
                int[] aint3 = Json.parseIntArray((JsonElement)jsonobject2.get(BOX_TEXTURE_OFFSET), (int)2);
                if (aint3 == null) {
                    throw new JsonParseException("Texture offset not specified");
                }
                float[] afloat3 = Json.parseFloatArray((JsonElement)jsonobject2.get(BOX_COORDINATES), (int)6);
                if (afloat3 == null) {
                    throw new JsonParseException("Coordinates not specified");
                }
                if (flag) {
                    afloat3[0] = -afloat3[0] - afloat3[3];
                }
                if (flag1) {
                    afloat3[1] = -afloat3[1] - afloat3[4];
                }
                if (flag2) {
                    afloat3[2] = -afloat3[2] - afloat3[5];
                }
                float f2 = Json.getFloat((JsonObject)jsonobject2, (String)BOX_SIZE_ADD, (float)0.0f);
                modelrenderer.setTextureOffset(aint3[0], aint3[1]);
                modelrenderer.addSprite(afloat3[0], afloat3[1], afloat3[2], (int)afloat3[3], (int)afloat3[4], (int)afloat3[5], f2);
            }
        }
        if ((jsonobject1 = (JsonObject)elem.get(MODEL_SUBMODEL)) != null) {
            ModelRenderer modelrenderer2 = PlayerItemParser.parseModelRenderer(jsonobject1, modelBase, aint, basePath);
            modelrenderer.addChild(modelrenderer2);
        }
        if ((jsonarray2 = (JsonArray)elem.get(MODEL_SUBMODELS)) != null) {
            for (int l = 0; l < jsonarray2.size(); ++l) {
                ModelRenderer modelrenderer1;
                JsonObject jsonobject3 = (JsonObject)jsonarray2.get(l);
                ModelRenderer modelrenderer3 = PlayerItemParser.parseModelRenderer(jsonobject3, modelBase, aint, basePath);
                if (modelrenderer3.getId() != null && (modelrenderer1 = modelrenderer.getChild(modelrenderer3.getId())) != null) {
                    Config.warn((String)("Duplicate model ID: " + modelrenderer3.getId()));
                }
                modelrenderer.addChild(modelrenderer3);
            }
        }
        return modelrenderer;
    }

    private static int[][] parseFaceUvs(JsonObject box) {
        int[][] aint = new int[][]{Json.parseIntArray((JsonElement)box.get(BOX_UV_DOWN), (int)4), Json.parseIntArray((JsonElement)box.get(BOX_UV_UP), (int)4), Json.parseIntArray((JsonElement)box.get(BOX_UV_NORTH), (int)4), Json.parseIntArray((JsonElement)box.get(BOX_UV_SOUTH), (int)4), Json.parseIntArray((JsonElement)box.get(BOX_UV_WEST), (int)4), Json.parseIntArray((JsonElement)box.get(BOX_UV_EAST), (int)4)};
        if (aint[2] == null) {
            aint[2] = Json.parseIntArray((JsonElement)box.get(BOX_UV_FRONT), (int)4);
        }
        if (aint[3] == null) {
            aint[3] = Json.parseIntArray((JsonElement)box.get(BOX_UV_BACK), (int)4);
        }
        if (aint[4] == null) {
            aint[4] = Json.parseIntArray((JsonElement)box.get(BOX_UV_LEFT), (int)4);
        }
        if (aint[5] == null) {
            aint[5] = Json.parseIntArray((JsonElement)box.get(BOX_UV_RIGHT), (int)4);
        }
        boolean flag = false;
        for (int i = 0; i < aint.length; ++i) {
            if (aint[i] == null) continue;
            flag = true;
        }
        if (!flag) {
            return null;
        }
        return aint;
    }
}
