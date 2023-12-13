/*
 * Decompiled with CFR 0.150.
 * 
 * Could not load the following classes:
 *  org.lwjgl.opengl.GL11
 */
package optifine;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Properties;
import java.util.Set;
import java.util.TreeSet;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.block.model.BakedQuad;
import net.minecraft.client.renderer.block.model.BlockPart;
import net.minecraft.client.renderer.block.model.BlockPartFace;
import net.minecraft.client.renderer.block.model.FaceBakery;
import net.minecraft.client.renderer.block.model.ItemModelGenerator;
import net.minecraft.client.renderer.block.model.ModelBlock;
import net.minecraft.client.renderer.texture.ITextureObject;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.renderer.texture.TextureManager;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.client.resources.model.IBakedModel;
import net.minecraft.client.resources.model.ModelResourceLocation;
import net.minecraft.client.resources.model.ModelRotation;
import net.minecraft.client.resources.model.SimpleBakedModel;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemArmor;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ResourceLocation;
import optifine.Blender;
import optifine.Config;
import optifine.NbtTagValue;
import optifine.RangeInt;
import optifine.RangeListInt;
import optifine.StrUtils;
import optifine.TextureUtils;
import org.lwjgl.opengl.GL11;

public class CustomItemProperties {
    public String name = null;
    public String basePath = null;
    public int type = 1;
    public int[] items = null;
    public String texture = null;
    public Map<String, String> mapTextures = null;
    public RangeListInt damage = null;
    public boolean damagePercent = false;
    public int damageMask = 0;
    public RangeListInt stackSize = null;
    public RangeListInt enchantmentIds = null;
    public RangeListInt enchantmentLevels = null;
    public NbtTagValue[] nbtTagValues = null;
    public int blend = 1;
    public float speed = 0.0f;
    public float rotation = 0.0f;
    public int layer = 0;
    public float duration = 1.0f;
    public int weight = 0;
    public ResourceLocation textureLocation = null;
    public Map mapTextureLocations = null;
    public TextureAtlasSprite sprite = null;
    public Map mapSprites = null;
    public IBakedModel model = null;
    public Map<String, IBakedModel> mapModels = null;
    private int textureWidth = 0;
    private int textureHeight = 0;
    public static final int TYPE_UNKNOWN = 0;
    public static final int TYPE_ITEM = 1;
    public static final int TYPE_ENCHANTMENT = 2;
    public static final int TYPE_ARMOR = 3;

    public CustomItemProperties(Properties props, String path) {
        this.name = CustomItemProperties.parseName(path);
        this.basePath = CustomItemProperties.parseBasePath(path);
        this.type = this.parseType(props.getProperty("type"));
        this.items = this.parseItems(props.getProperty("items"), props.getProperty("matchItems"));
        this.mapTextures = CustomItemProperties.parseTextures(props, this.basePath);
        this.texture = CustomItemProperties.parseTexture(props.getProperty("texture"), props.getProperty("tile"), props.getProperty("source"), path, this.basePath, this.type, this.mapTextures);
        String damageStr = props.getProperty("damage");
        if (damageStr != null) {
            this.damagePercent = damageStr.contains("%");
            damageStr.replace("%", "");
            this.damage = this.parseRangeListInt(damageStr);
            this.damageMask = this.parseInt(props.getProperty("damageMask"), 0);
        }
        this.stackSize = this.parseRangeListInt(props.getProperty("stackSize"));
        this.enchantmentIds = this.parseRangeListInt(props.getProperty("enchantmentIDs"));
        this.enchantmentLevels = this.parseRangeListInt(props.getProperty("enchantmentLevels"));
        this.nbtTagValues = this.parseNbtTagValues(props);
        this.blend = Blender.parseBlend(props.getProperty("blend"));
        this.speed = this.parseFloat(props.getProperty("speed"), 0.0f);
        this.rotation = this.parseFloat(props.getProperty("rotation"), 0.0f);
        this.layer = this.parseInt(props.getProperty("layer"), 0);
        this.weight = this.parseInt(props.getProperty("weight"), 0);
        this.duration = this.parseFloat(props.getProperty("duration"), 1.0f);
    }

    private static String parseName(String path) {
        int pos2;
        String str = path;
        int pos = path.lastIndexOf(47);
        if (pos >= 0) {
            str = path.substring(pos + 1);
        }
        if ((pos2 = str.lastIndexOf(46)) >= 0) {
            str = str.substring(0, pos2);
        }
        return str;
    }

    private static String parseBasePath(String path) {
        int pos = path.lastIndexOf(47);
        return pos < 0 ? "" : path.substring(0, pos);
    }

    private int parseType(String str) {
        if (str == null) {
            return 1;
        }
        if (str.equals("item")) {
            return 1;
        }
        if (str.equals("enchantment")) {
            return 2;
        }
        if (str.equals("armor")) {
            return 3;
        }
        Config.warn("Unknown method: " + str);
        return 0;
    }

    private int[] parseItems(String str, String str2) {
        int i;
        if (str == null) {
            str = str2;
        }
        if (str == null) {
            return null;
        }
        str = str.trim();
        TreeSet<Integer> setItemIds = new TreeSet<Integer>();
        String[] tokens = Config.tokenize(str, " ");
        for (int integers = 0; integers < tokens.length; ++integers) {
            Item var16;
            int id;
            String[] item;
            String ints = tokens[integers];
            i = Config.parseInt(ints, -1);
            if (i >= 0) {
                setItemIds.add(new Integer(i));
                continue;
            }
            if (ints.contains("-") && (item = Config.tokenize(ints, "-")).length == 2) {
                id = Config.parseInt(item[0], -1);
                int val2 = Config.parseInt(item[1], -1);
                if (id >= 0 && val2 >= 0) {
                    int min = Math.min(id, val2);
                    int max = Math.max(id, val2);
                    for (int x = min; x <= max; ++x) {
                        setItemIds.add(new Integer(x));
                    }
                    continue;
                }
            }
            if ((var16 = Item.getByNameOrId(ints)) == null) {
                Config.warn("Item not found: " + ints);
                continue;
            }
            id = Item.getIdFromItem(var16);
            if (id < 0) {
                Config.warn("Item not found: " + ints);
                continue;
            }
            setItemIds.add(new Integer(id));
        }
        Integer[] var14 = setItemIds.toArray(new Integer[setItemIds.size()]);
        int[] var15 = new int[var14.length];
        for (i = 0; i < var15.length; ++i) {
            var15[i] = var14[i];
        }
        return var15;
    }

    private static String parseTexture(String texStr, String texStr2, String texStr3, String path, String basePath, int type, Map<String, String> mapTexs) {
        int pos2;
        String str;
        if (texStr == null) {
            texStr = texStr2;
        }
        if (texStr == null) {
            texStr = texStr3;
        }
        if (texStr != null) {
            String str2 = ".png";
            if (texStr.endsWith(str2)) {
                texStr = texStr.substring(0, texStr.length() - str2.length());
            }
            texStr = CustomItemProperties.fixTextureName(texStr, basePath);
            return texStr;
        }
        if (type == 3) {
            return null;
        }
        if (mapTexs != null && (str = mapTexs.get("texture.bow_standby")) != null) {
            return str;
        }
        str = path;
        int pos = path.lastIndexOf(47);
        if (pos >= 0) {
            str = path.substring(pos + 1);
        }
        if ((pos2 = str.lastIndexOf(46)) >= 0) {
            str = str.substring(0, pos2);
        }
        str = CustomItemProperties.fixTextureName(str, basePath);
        return str;
    }

    private static Map parseTextures(Properties props, String basePath) {
        String prefix = "texture.";
        Map mapProps = CustomItemProperties.getMatchingProperties(props, prefix);
        if (mapProps.size() <= 0) {
            return null;
        }
        Set keySet = mapProps.keySet();
        LinkedHashMap<String, String> mapTex = new LinkedHashMap<String, String>();
        for (String key : keySet) {
            String val = (String)mapProps.get(key);
            val = CustomItemProperties.fixTextureName(val, basePath);
            mapTex.put(key, val);
        }
        return mapTex;
    }

    private static String fixTextureName(String iconName, String basePath) {
        String pathBlocks;
        if (!((iconName = TextureUtils.fixResourcePath(iconName, basePath)).startsWith(basePath) || iconName.startsWith("textures/") || iconName.startsWith("mcpatcher/"))) {
            iconName = String.valueOf(basePath) + "/" + iconName;
        }
        if (iconName.endsWith(".png")) {
            iconName = iconName.substring(0, iconName.length() - 4);
        }
        if (iconName.startsWith(pathBlocks = "textures/blocks/")) {
            iconName = iconName.substring(pathBlocks.length());
        }
        if (iconName.startsWith("/")) {
            iconName = iconName.substring(1);
        }
        return iconName;
    }

    private int parseInt(String str, int defVal) {
        if (str == null) {
            return defVal;
        }
        int val = Config.parseInt(str = str.trim(), Integer.MIN_VALUE);
        if (val == Integer.MIN_VALUE) {
            Config.warn("Invalid integer: " + str);
            return defVal;
        }
        return val;
    }

    private float parseFloat(String str, float defVal) {
        if (str == null) {
            return defVal;
        }
        float val = Config.parseFloat(str = str.trim(), Float.MIN_VALUE);
        if (val == Float.MIN_VALUE) {
            Config.warn("Invalid float: " + str);
            return defVal;
        }
        return val;
    }

    private RangeListInt parseRangeListInt(String str) {
        if (str == null) {
            return null;
        }
        String[] tokens = Config.tokenize(str, " ");
        RangeListInt rangeList = new RangeListInt();
        for (int i = 0; i < tokens.length; ++i) {
            String token = tokens[i];
            RangeInt range = this.parseRangeInt(token);
            if (range == null) {
                Config.warn("Invalid range list: " + str);
                return null;
            }
            rangeList.addRange(range);
        }
        return rangeList;
    }

    private RangeInt parseRangeInt(String str) {
        int min;
        if (str == null) {
            return null;
        }
        int countMinus = (str = str.trim()).length() - str.replace("-", "").length();
        if (countMinus > 1) {
            Config.warn("Invalid range: " + str);
            return null;
        }
        String[] tokens = Config.tokenize(str, "- ");
        int[] vals = new int[tokens.length];
        for (min = 0; min < tokens.length; ++min) {
            String max = tokens[min];
            int val = Config.parseInt(max, -1);
            if (val < 0) {
                Config.warn("Invalid range: " + str);
                return null;
            }
            vals[min] = val;
        }
        if (vals.length == 1) {
            min = vals[0];
            if (str.startsWith("-")) {
                return new RangeInt(0, min);
            }
            if (str.endsWith("-")) {
                return new RangeInt(min, 255);
            }
            return new RangeInt(min, min);
        }
        if (vals.length == 2) {
            min = Math.min(vals[0], vals[1]);
            int var8 = Math.max(vals[0], vals[1]);
            return new RangeInt(min, var8);
        }
        Config.warn("Invalid range: " + str);
        return null;
    }

    private NbtTagValue[] parseNbtTagValues(Properties props) {
        String PREFIX_NBT = "nbt.";
        Map mapNbt = CustomItemProperties.getMatchingProperties(props, PREFIX_NBT);
        if (mapNbt.size() <= 0) {
            return null;
        }
        ArrayList<NbtTagValue> listNbts = new ArrayList<NbtTagValue>();
        Set keySet = mapNbt.keySet();
        for (String key : keySet) {
            String val = (String)mapNbt.get(key);
            String id = key.substring(PREFIX_NBT.length());
            NbtTagValue nbt = new NbtTagValue(id, val);
            listNbts.add(nbt);
        }
        NbtTagValue[] nbts1 = listNbts.toArray(new NbtTagValue[listNbts.size()]);
        return nbts1;
    }

    private static Map getMatchingProperties(Properties props, String keyPrefix) {
        LinkedHashMap<String, String> map = new LinkedHashMap<String, String>();
        Set<Object> keySet = props.keySet();
        for (String string : keySet) {
            String val = props.getProperty(string);
            if (!string.startsWith(keyPrefix)) continue;
            map.put(string, val);
        }
        return map;
    }

    public boolean isValid(String path) {
        if (this.name != null && this.name.length() > 0) {
            if (this.basePath == null) {
                Config.warn("No base path found: " + path);
                return false;
            }
            if (this.type == 0) {
                Config.warn("No type defined: " + path);
                return false;
            }
            if ((this.type == 1 || this.type == 3) && this.items == null) {
                Config.warn("No items defined: " + path);
                return false;
            }
            if (this.texture == null && this.mapTextures == null) {
                Config.warn("No texture specified: " + path);
                return false;
            }
            if (this.type == 2 && this.enchantmentIds == null) {
                Config.warn("No enchantmentIDs specified: " + path);
                return false;
            }
            return true;
        }
        Config.warn("No name found: " + path);
        return false;
    }

    public void updateIcons(TextureMap textureMap) {
        if (this.texture != null) {
            this.textureLocation = this.getTextureLocation(this.texture);
            if (this.type == 1) {
                ResourceLocation keySet = this.getSpriteLocation(this.textureLocation);
                this.sprite = textureMap.func_174942_a(keySet);
            }
        }
        if (this.mapTextures != null) {
            this.mapTextureLocations = new HashMap();
            this.mapSprites = new HashMap();
            Set<String> keySet1 = this.mapTextures.keySet();
            for (String key : keySet1) {
                String val = this.mapTextures.get(key);
                ResourceLocation locTex = this.getTextureLocation(val);
                this.mapTextureLocations.put(key, locTex);
                if (this.type != 1) continue;
                ResourceLocation locSprite = this.getSpriteLocation(locTex);
                TextureAtlasSprite icon = textureMap.func_174942_a(locSprite);
                this.mapSprites.put(key, icon);
            }
        }
    }

    private ResourceLocation getTextureLocation(String texName) {
        String filePath;
        ResourceLocation locFile;
        boolean exists;
        if (texName == null) {
            return null;
        }
        ResourceLocation resLoc = new ResourceLocation(texName);
        String domain = resLoc.getResourceDomain();
        String path = resLoc.getResourcePath();
        if (!path.contains("/")) {
            path = "textures/blocks/" + path;
        }
        if (!(exists = Config.hasResource(locFile = new ResourceLocation(domain, filePath = String.valueOf(path) + ".png")))) {
            Config.warn("File not found: " + filePath);
        }
        return locFile;
    }

    private ResourceLocation getSpriteLocation(ResourceLocation resLoc) {
        String pathTex = resLoc.getResourcePath();
        pathTex = StrUtils.removePrefix(pathTex, "textures/");
        pathTex = StrUtils.removeSuffix(pathTex, ".png");
        ResourceLocation locTex = new ResourceLocation(resLoc.getResourceDomain(), pathTex);
        return locTex;
    }

    public void updateModel(TextureMap textureMap, ItemModelGenerator itemModelGenerator) {
        String[] textures = this.getModelTextures();
        boolean useTint = this.isUseTint();
        this.model = CustomItemProperties.makeBakedModel(textureMap, itemModelGenerator, textures, useTint);
        if (this.type == 1 && this.mapTextures != null) {
            Set<String> keySet = this.mapTextures.keySet();
            for (String key : keySet) {
                String tex = this.mapTextures.get(key);
                String path = StrUtils.removePrefix(key, "texture.");
                if (!path.startsWith("bow") && !path.startsWith("fishing_rod")) continue;
                String[] texNames = new String[]{tex};
                IBakedModel modelTex = CustomItemProperties.makeBakedModel(textureMap, itemModelGenerator, texNames, useTint);
                if (this.mapModels == null) {
                    this.mapModels = new HashMap<String, IBakedModel>();
                }
                this.mapModels.put(path, modelTex);
            }
        }
    }

    private boolean isUseTint() {
        return true;
    }

    private static IBakedModel makeBakedModel(TextureMap textureMap, ItemModelGenerator itemModelGenerator, String[] textures, boolean useTint) {
        ModelBlock modelBlockBase = CustomItemProperties.makeModelBlock(textures);
        ModelBlock modelBlock = itemModelGenerator.func_178392_a(textureMap, modelBlockBase);
        IBakedModel model = CustomItemProperties.bakeModel(textureMap, modelBlock, useTint);
        return model;
    }

    private String[] getModelTextures() {
        if (this.type == 1 && this.items.length == 1) {
            ItemArmor itemArmor;
            Item item = Item.getItemById(this.items[0]);
            if (item == Items.potionitem && this.damage != null && this.damage.getCountRanges() > 0) {
                RangeInt itemArmor1 = this.damage.getRange(0);
                int material1 = itemArmor1.getMin();
                boolean type1 = (material1 & 0x4000) != 0;
                String key = this.getMapTexture(this.mapTextures, "texture.potion_overlay", "items/potion_overlay");
                String texMain = null;
                texMain = type1 ? this.getMapTexture(this.mapTextures, "texture.potion_bottle_splash", "items/potion_bottle_splash") : this.getMapTexture(this.mapTextures, "texture.potion_bottle_drinkable", "items/potion_bottle_drinkable");
                return new String[]{key, texMain};
            }
            if (item instanceof ItemArmor && (itemArmor = (ItemArmor)item).getArmorMaterial() == ItemArmor.ArmorMaterial.LEATHER) {
                String material = "leather";
                String type = "helmet";
                if (itemArmor.armorType == 0) {
                    type = "helmet";
                }
                if (itemArmor.armorType == 1) {
                    type = "chestplate";
                }
                if (itemArmor.armorType == 2) {
                    type = "leggings";
                }
                if (itemArmor.armorType == 3) {
                    type = "boots";
                }
                String key = String.valueOf(material) + "_" + type;
                String texMain = this.getMapTexture(this.mapTextures, "texture." + key, "items/" + key);
                String texOverlay = this.getMapTexture(this.mapTextures, "texture." + key + "_overlay", "items/" + key + "_overlay");
                return new String[]{texMain, texOverlay};
            }
        }
        return new String[]{this.texture};
    }

    private String getMapTexture(Map<String, String> map, String key, String def) {
        if (map == null) {
            return def;
        }
        String str = map.get(key);
        return str == null ? def : str;
    }

    private static ModelBlock makeModelBlock(String[] modelTextures) {
        StringBuffer sb = new StringBuffer();
        sb.append("{\"parent\": \"builtin/generated\",\"textures\": {");
        for (int modelStr = 0; modelStr < modelTextures.length; ++modelStr) {
            String model = modelTextures[modelStr];
            if (modelStr > 0) {
                sb.append(", ");
            }
            sb.append("\"layer" + modelStr + "\": \"" + model + "\"");
        }
        sb.append("}}");
        String var4 = sb.toString();
        ModelBlock var5 = ModelBlock.deserialize(var4);
        return var5;
    }

    private static IBakedModel bakeModel(TextureMap textureMap, ModelBlock modelBlockIn, boolean useTint) {
        ModelRotation modelRotationIn = ModelRotation.X0_Y0;
        boolean uvLocked = false;
        TextureAtlasSprite var4 = textureMap.getSpriteSafe(modelBlockIn.resolveTextureName("particle"));
        SimpleBakedModel.Builder var5 = new SimpleBakedModel.Builder(modelBlockIn).func_177646_a(var4);
        for (BlockPart var7 : modelBlockIn.getElements()) {
            for (EnumFacing var9 : var7.field_178240_c.keySet()) {
                BlockPartFace var10 = (BlockPartFace)var7.field_178240_c.get(var9);
                if (!useTint) {
                    var10 = new BlockPartFace(var10.field_178244_b, -1, var10.field_178242_d, var10.field_178243_e);
                }
                TextureAtlasSprite var11 = textureMap.getSpriteSafe(modelBlockIn.resolveTextureName(var10.field_178242_d));
                BakedQuad quad = CustomItemProperties.makeBakedQuad(var7, var10, var11, var9, modelRotationIn, uvLocked);
                if (var10.field_178244_b == null) {
                    var5.func_177648_a(quad);
                    continue;
                }
                var5.func_177650_a(modelRotationIn.func_177523_a(var10.field_178244_b), quad);
            }
        }
        return var5.func_177645_b();
    }

    private static BakedQuad makeBakedQuad(BlockPart blockPart, BlockPartFace blockPartFace, TextureAtlasSprite textureAtlasSprite, EnumFacing enumFacing, ModelRotation modelRotation, boolean uvLocked) {
        FaceBakery faceBakery = new FaceBakery();
        return faceBakery.func_178414_a(blockPart.field_178241_a, blockPart.field_178239_b, blockPartFace, textureAtlasSprite, enumFacing, modelRotation, blockPart.field_178237_d, uvLocked, blockPart.field_178238_e);
    }

    public String toString() {
        return this.basePath + "/" + this.name + ", type: " + this.type + ", items: [" + Config.arrayToString(this.items) + "], textture: " + this.texture;
    }

    public float getTextureWidth(TextureManager textureManager) {
        if (this.textureWidth <= 0) {
            if (this.textureLocation != null) {
                ITextureObject tex = textureManager.getTexture(this.textureLocation);
                int texId = tex.getGlTextureId();
                int prevTexId = GlStateManager.getBoundTexture();
                GlStateManager.func_179144_i(texId);
                this.textureWidth = GL11.glGetTexLevelParameteri((int)3553, (int)0, (int)4096);
                GlStateManager.func_179144_i(prevTexId);
            }
            if (this.textureWidth <= 0) {
                this.textureWidth = 16;
            }
        }
        return this.textureWidth;
    }

    public float getTextureHeight(TextureManager textureManager) {
        if (this.textureHeight <= 0) {
            if (this.textureLocation != null) {
                ITextureObject tex = textureManager.getTexture(this.textureLocation);
                int texId = tex.getGlTextureId();
                int prevTexId = GlStateManager.getBoundTexture();
                GlStateManager.func_179144_i(texId);
                this.textureHeight = GL11.glGetTexLevelParameteri((int)3553, (int)0, (int)4097);
                GlStateManager.func_179144_i(prevTexId);
            }
            if (this.textureHeight <= 0) {
                this.textureHeight = 16;
            }
        }
        return this.textureHeight;
    }

    public IBakedModel getModel(ModelResourceLocation modelLocation) {
        if (modelLocation != null && this.mapTextures != null) {
            IBakedModel customModel;
            String modelPath = modelLocation.getResourcePath();
            if (this.mapModels != null && (customModel = this.mapModels.get(modelPath)) != null) {
                return customModel;
            }
        }
        return this.model;
    }
}

