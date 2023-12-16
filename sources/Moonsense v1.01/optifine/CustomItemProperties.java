// 
// Decompiled by Procyon v0.5.36
// 

package optifine;

import net.minecraft.client.resources.model.ModelResourceLocation;
import net.minecraft.client.renderer.texture.ITextureObject;
import org.lwjgl.opengl.GL11;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.texture.TextureManager;
import net.minecraft.client.renderer.block.model.FaceBakery;
import net.minecraft.client.renderer.block.model.BakedQuad;
import net.minecraft.client.renderer.block.model.BlockPartFace;
import net.minecraft.util.EnumFacing;
import net.minecraft.client.renderer.block.model.BlockPart;
import net.minecraft.client.resources.model.SimpleBakedModel;
import net.minecraft.client.resources.model.ModelRotation;
import net.minecraft.item.ItemArmor;
import net.minecraft.init.Items;
import net.minecraft.client.renderer.block.model.ModelBlock;
import net.minecraft.client.renderer.block.model.ItemModelGenerator;
import java.util.HashMap;
import net.minecraft.client.renderer.texture.TextureMap;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Set;
import java.util.LinkedHashMap;
import net.minecraft.item.Item;
import java.util.TreeSet;
import java.util.Properties;
import net.minecraft.client.resources.model.IBakedModel;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.util.ResourceLocation;
import java.util.Map;

public class CustomItemProperties
{
    public String name;
    public String basePath;
    public int type;
    public int[] items;
    public String texture;
    public Map<String, String> mapTextures;
    public RangeListInt damage;
    public boolean damagePercent;
    public int damageMask;
    public RangeListInt stackSize;
    public RangeListInt enchantmentIds;
    public RangeListInt enchantmentLevels;
    public NbtTagValue[] nbtTagValues;
    public int blend;
    public float speed;
    public float rotation;
    public int layer;
    public float duration;
    public int weight;
    public ResourceLocation textureLocation;
    public Map mapTextureLocations;
    public TextureAtlasSprite sprite;
    public Map mapSprites;
    public IBakedModel model;
    public Map<String, IBakedModel> mapModels;
    private int textureWidth;
    private int textureHeight;
    public static final int TYPE_UNKNOWN = 0;
    public static final int TYPE_ITEM = 1;
    public static final int TYPE_ENCHANTMENT = 2;
    public static final int TYPE_ARMOR = 3;
    
    public CustomItemProperties(final Properties props, final String path) {
        this.name = null;
        this.basePath = null;
        this.type = 1;
        this.items = null;
        this.texture = null;
        this.mapTextures = null;
        this.damage = null;
        this.damagePercent = false;
        this.damageMask = 0;
        this.stackSize = null;
        this.enchantmentIds = null;
        this.enchantmentLevels = null;
        this.nbtTagValues = null;
        this.blend = 1;
        this.speed = 0.0f;
        this.rotation = 0.0f;
        this.layer = 0;
        this.duration = 1.0f;
        this.weight = 0;
        this.textureLocation = null;
        this.mapTextureLocations = null;
        this.sprite = null;
        this.mapSprites = null;
        this.model = null;
        this.mapModels = null;
        this.textureWidth = 0;
        this.textureHeight = 0;
        this.name = parseName(path);
        this.basePath = parseBasePath(path);
        this.type = this.parseType(props.getProperty("type"));
        this.items = this.parseItems(props.getProperty("items"), props.getProperty("matchItems"));
        this.mapTextures = (Map<String, String>)parseTextures(props, this.basePath);
        this.texture = parseTexture(props.getProperty("texture"), props.getProperty("tile"), props.getProperty("source"), path, this.basePath, this.type, this.mapTextures);
        final String damageStr = props.getProperty("damage");
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
    
    private static String parseName(final String path) {
        String str = path;
        final int pos = path.lastIndexOf(47);
        if (pos >= 0) {
            str = path.substring(pos + 1);
        }
        final int pos2 = str.lastIndexOf(46);
        if (pos2 >= 0) {
            str = str.substring(0, pos2);
        }
        return str;
    }
    
    private static String parseBasePath(final String path) {
        final int pos = path.lastIndexOf(47);
        return (pos < 0) ? "" : path.substring(0, pos);
    }
    
    private int parseType(final String str) {
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
    
    private int[] parseItems(String str, final String str2) {
        if (str == null) {
            str = str2;
        }
        if (str == null) {
            return null;
        }
        str = str.trim();
        final TreeSet setItemIds = new TreeSet();
        final String[] tokens = Config.tokenize(str, " ");
        for (int integers = 0; integers < tokens.length; ++integers) {
            final String ints = tokens[integers];
            final int i = Config.parseInt(ints, -1);
            if (i >= 0) {
                setItemIds.add(new Integer(i));
            }
            else {
                if (ints.contains("-")) {
                    final String[] item = Config.tokenize(ints, "-");
                    if (item.length == 2) {
                        final int id = Config.parseInt(item[0], -1);
                        final int val2 = Config.parseInt(item[1], -1);
                        if (id >= 0 && val2 >= 0) {
                            final int min = Math.min(id, val2);
                            for (int max = Math.max(id, val2), x = min; x <= max; ++x) {
                                setItemIds.add(new Integer(x));
                            }
                            continue;
                        }
                    }
                }
                final Item var16 = Item.getByNameOrId(ints);
                if (var16 == null) {
                    Config.warn("Item not found: " + ints);
                }
                else {
                    final int id = Item.getIdFromItem(var16);
                    if (id < 0) {
                        Config.warn("Item not found: " + ints);
                    }
                    else {
                        setItemIds.add(new Integer(id));
                    }
                }
            }
        }
        final Integer[] var17 = (Integer[])setItemIds.toArray(new Integer[setItemIds.size()]);
        int i;
        int[] var18;
        for (var18 = new int[var17.length], i = 0; i < var18.length; ++i) {
            var18[i] = var17[i];
        }
        return var18;
    }
    
    private static String parseTexture(String texStr, final String texStr2, final String texStr3, final String path, final String basePath, final int type, final Map<String, String> mapTexs) {
        if (texStr == null) {
            texStr = texStr2;
        }
        if (texStr == null) {
            texStr = texStr3;
        }
        if (texStr != null) {
            final String str = ".png";
            if (texStr.endsWith(str)) {
                texStr = texStr.substring(0, texStr.length() - str.length());
            }
            texStr = fixTextureName(texStr, basePath);
            return texStr;
        }
        if (type == 3) {
            return null;
        }
        if (mapTexs != null) {
            final String str = mapTexs.get("texture.bow_standby");
            if (str != null) {
                return str;
            }
        }
        String str = path;
        final int pos = path.lastIndexOf(47);
        if (pos >= 0) {
            str = path.substring(pos + 1);
        }
        final int pos2 = str.lastIndexOf(46);
        if (pos2 >= 0) {
            str = str.substring(0, pos2);
        }
        str = fixTextureName(str, basePath);
        return str;
    }
    
    private static Map parseTextures(final Properties props, final String basePath) {
        final String prefix = "texture.";
        final Map mapProps = getMatchingProperties(props, prefix);
        if (mapProps.size() <= 0) {
            return null;
        }
        final Set keySet = mapProps.keySet();
        final LinkedHashMap mapTex = new LinkedHashMap();
        for (final String key : keySet) {
            String val = mapProps.get(key);
            val = fixTextureName(val, basePath);
            mapTex.put(key, val);
        }
        return mapTex;
    }
    
    private static String fixTextureName(String iconName, final String basePath) {
        iconName = TextureUtils.fixResourcePath(iconName, basePath);
        if (!iconName.startsWith(basePath) && !iconName.startsWith("textures/") && !iconName.startsWith("mcpatcher/")) {
            iconName = String.valueOf(basePath) + "/" + iconName;
        }
        if (iconName.endsWith(".png")) {
            iconName = iconName.substring(0, iconName.length() - 4);
        }
        final String pathBlocks = "textures/blocks/";
        if (iconName.startsWith(pathBlocks)) {
            iconName = iconName.substring(pathBlocks.length());
        }
        if (iconName.startsWith("/")) {
            iconName = iconName.substring(1);
        }
        return iconName;
    }
    
    private int parseInt(String str, final int defVal) {
        if (str == null) {
            return defVal;
        }
        str = str.trim();
        final int val = Config.parseInt(str, Integer.MIN_VALUE);
        if (val == Integer.MIN_VALUE) {
            Config.warn("Invalid integer: " + str);
            return defVal;
        }
        return val;
    }
    
    private float parseFloat(String str, final float defVal) {
        if (str == null) {
            return defVal;
        }
        str = str.trim();
        final float val = Config.parseFloat(str, Float.MIN_VALUE);
        if (val == Float.MIN_VALUE) {
            Config.warn("Invalid float: " + str);
            return defVal;
        }
        return val;
    }
    
    private RangeListInt parseRangeListInt(final String str) {
        if (str == null) {
            return null;
        }
        final String[] tokens = Config.tokenize(str, " ");
        final RangeListInt rangeList = new RangeListInt();
        for (int i = 0; i < tokens.length; ++i) {
            final String token = tokens[i];
            final RangeInt range = this.parseRangeInt(token);
            if (range == null) {
                Config.warn("Invalid range list: " + str);
                return null;
            }
            rangeList.addRange(range);
        }
        return rangeList;
    }
    
    private RangeInt parseRangeInt(String str) {
        if (str == null) {
            return null;
        }
        str = str.trim();
        final int countMinus = str.length() - str.replace("-", "").length();
        if (countMinus > 1) {
            Config.warn("Invalid range: " + str);
            return null;
        }
        final String[] tokens = Config.tokenize(str, "- ");
        final int[] vals = new int[tokens.length];
        for (int min = 0; min < tokens.length; ++min) {
            final String max = tokens[min];
            final int val = Config.parseInt(max, -1);
            if (val < 0) {
                Config.warn("Invalid range: " + str);
                return null;
            }
            vals[min] = val;
        }
        if (vals.length == 1) {
            final int min = vals[0];
            if (str.startsWith("-")) {
                return new RangeInt(0, min);
            }
            if (str.endsWith("-")) {
                return new RangeInt(min, 255);
            }
            return new RangeInt(min, min);
        }
        else {
            if (vals.length == 2) {
                final int min = Math.min(vals[0], vals[1]);
                final int var8 = Math.max(vals[0], vals[1]);
                return new RangeInt(min, var8);
            }
            Config.warn("Invalid range: " + str);
            return null;
        }
    }
    
    private NbtTagValue[] parseNbtTagValues(final Properties props) {
        final String PREFIX_NBT = "nbt.";
        final Map mapNbt = getMatchingProperties(props, PREFIX_NBT);
        if (mapNbt.size() <= 0) {
            return null;
        }
        final ArrayList listNbts = new ArrayList();
        final Set keySet = mapNbt.keySet();
        for (final String key : keySet) {
            final String val = mapNbt.get(key);
            final String id = key.substring(PREFIX_NBT.length());
            final NbtTagValue nbt = new NbtTagValue(id, val);
            listNbts.add(nbt);
        }
        final NbtTagValue[] nbts2 = listNbts.toArray(new NbtTagValue[listNbts.size()]);
        return nbts2;
    }
    
    private static Map getMatchingProperties(final Properties props, final String keyPrefix) {
        final LinkedHashMap map = new LinkedHashMap();
        final Set keySet = props.keySet();
        for (final String key : keySet) {
            final String val = props.getProperty(key);
            if (key.startsWith(keyPrefix)) {
                map.put(key, val);
            }
        }
        return map;
    }
    
    public boolean isValid(final String path) {
        if (this.name == null || this.name.length() <= 0) {
            Config.warn("No name found: " + path);
            return false;
        }
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
    
    public void updateIcons(final TextureMap textureMap) {
        if (this.texture != null) {
            this.textureLocation = this.getTextureLocation(this.texture);
            if (this.type == 1) {
                final ResourceLocation keySet = this.getSpriteLocation(this.textureLocation);
                this.sprite = textureMap.func_174942_a(keySet);
            }
        }
        if (this.mapTextures != null) {
            this.mapTextureLocations = new HashMap();
            this.mapSprites = new HashMap();
            final Set keySet2 = this.mapTextures.keySet();
            for (final String key : keySet2) {
                final String val = this.mapTextures.get(key);
                final ResourceLocation locTex = this.getTextureLocation(val);
                this.mapTextureLocations.put(key, locTex);
                if (this.type == 1) {
                    final ResourceLocation locSprite = this.getSpriteLocation(locTex);
                    final TextureAtlasSprite icon = textureMap.func_174942_a(locSprite);
                    this.mapSprites.put(key, icon);
                }
            }
        }
    }
    
    private ResourceLocation getTextureLocation(final String texName) {
        if (texName == null) {
            return null;
        }
        final ResourceLocation resLoc = new ResourceLocation(texName);
        final String domain = resLoc.getResourceDomain();
        String path = resLoc.getResourcePath();
        if (!path.contains("/")) {
            path = "textures/blocks/" + path;
        }
        final String filePath = String.valueOf(path) + ".png";
        final ResourceLocation locFile = new ResourceLocation(domain, filePath);
        final boolean exists = Config.hasResource(locFile);
        if (!exists) {
            Config.warn("File not found: " + filePath);
        }
        return locFile;
    }
    
    private ResourceLocation getSpriteLocation(final ResourceLocation resLoc) {
        String pathTex = resLoc.getResourcePath();
        pathTex = StrUtils.removePrefix(pathTex, "textures/");
        pathTex = StrUtils.removeSuffix(pathTex, ".png");
        final ResourceLocation locTex = new ResourceLocation(resLoc.getResourceDomain(), pathTex);
        return locTex;
    }
    
    public void updateModel(final TextureMap textureMap, final ItemModelGenerator itemModelGenerator) {
        final String[] textures = this.getModelTextures();
        final boolean useTint = this.isUseTint();
        this.model = makeBakedModel(textureMap, itemModelGenerator, textures, useTint);
        if (this.type == 1 && this.mapTextures != null) {
            final Set keySet = this.mapTextures.keySet();
            for (final String key : keySet) {
                final String tex = this.mapTextures.get(key);
                final String path = StrUtils.removePrefix(key, "texture.");
                if (path.startsWith("bow") || path.startsWith("fishing_rod")) {
                    final String[] texNames = { tex };
                    final IBakedModel modelTex = makeBakedModel(textureMap, itemModelGenerator, texNames, useTint);
                    if (this.mapModels == null) {
                        this.mapModels = new HashMap<String, IBakedModel>();
                    }
                    this.mapModels.put(path, modelTex);
                }
            }
        }
    }
    
    private boolean isUseTint() {
        return true;
    }
    
    private static IBakedModel makeBakedModel(final TextureMap textureMap, final ItemModelGenerator itemModelGenerator, final String[] textures, final boolean useTint) {
        final ModelBlock modelBlockBase = makeModelBlock(textures);
        final ModelBlock modelBlock = itemModelGenerator.func_178392_a(textureMap, modelBlockBase);
        final IBakedModel model = bakeModel(textureMap, modelBlock, useTint);
        return model;
    }
    
    private String[] getModelTextures() {
        if (this.type == 1 && this.items.length == 1) {
            final Item item = Item.getItemById(this.items[0]);
            if (item == Items.potionitem && this.damage != null && this.damage.getCountRanges() > 0) {
                final RangeInt itemArmor1 = this.damage.getRange(0);
                final int material1 = itemArmor1.getMin();
                final boolean type1 = (material1 & 0x4000) != 0x0;
                final String key = this.getMapTexture(this.mapTextures, "texture.potion_overlay", "items/potion_overlay");
                String texMain = null;
                if (type1) {
                    texMain = this.getMapTexture(this.mapTextures, "texture.potion_bottle_splash", "items/potion_bottle_splash");
                }
                else {
                    texMain = this.getMapTexture(this.mapTextures, "texture.potion_bottle_drinkable", "items/potion_bottle_drinkable");
                }
                return new String[] { key, texMain };
            }
            if (item instanceof ItemArmor) {
                final ItemArmor itemArmor2 = (ItemArmor)item;
                if (itemArmor2.getArmorMaterial() == ItemArmor.ArmorMaterial.LEATHER) {
                    final String material2 = "leather";
                    String type2 = "helmet";
                    if (itemArmor2.armorType == 0) {
                        type2 = "helmet";
                    }
                    if (itemArmor2.armorType == 1) {
                        type2 = "chestplate";
                    }
                    if (itemArmor2.armorType == 2) {
                        type2 = "leggings";
                    }
                    if (itemArmor2.armorType == 3) {
                        type2 = "boots";
                    }
                    final String key = String.valueOf(material2) + "_" + type2;
                    final String texMain = this.getMapTexture(this.mapTextures, "texture." + key, "items/" + key);
                    final String texOverlay = this.getMapTexture(this.mapTextures, "texture." + key + "_overlay", "items/" + key + "_overlay");
                    return new String[] { texMain, texOverlay };
                }
            }
        }
        return new String[] { this.texture };
    }
    
    private String getMapTexture(final Map<String, String> map, final String key, final String def) {
        if (map == null) {
            return def;
        }
        final String str = map.get(key);
        return (str == null) ? def : str;
    }
    
    private static ModelBlock makeModelBlock(final String[] modelTextures) {
        final StringBuffer sb = new StringBuffer();
        sb.append("{\"parent\": \"builtin/generated\",\"textures\": {");
        for (int modelStr = 0; modelStr < modelTextures.length; ++modelStr) {
            final String model = modelTextures[modelStr];
            if (modelStr > 0) {
                sb.append(", ");
            }
            sb.append("\"layer" + modelStr + "\": \"" + model + "\"");
        }
        sb.append("}}");
        final String var4 = sb.toString();
        final ModelBlock var5 = ModelBlock.deserialize(var4);
        return var5;
    }
    
    private static IBakedModel bakeModel(final TextureMap textureMap, final ModelBlock modelBlockIn, final boolean useTint) {
        final ModelRotation modelRotationIn = ModelRotation.X0_Y0;
        final boolean uvLocked = false;
        final TextureAtlasSprite var4 = textureMap.getSpriteSafe(modelBlockIn.resolveTextureName("particle"));
        final SimpleBakedModel.Builder var5 = new SimpleBakedModel.Builder(modelBlockIn).func_177646_a(var4);
        for (final BlockPart var7 : modelBlockIn.getElements()) {
            for (final EnumFacing var9 : var7.field_178240_c.keySet()) {
                BlockPartFace var10 = var7.field_178240_c.get(var9);
                if (!useTint) {
                    var10 = new BlockPartFace(var10.field_178244_b, -1, var10.field_178242_d, var10.field_178243_e);
                }
                final TextureAtlasSprite var11 = textureMap.getSpriteSafe(modelBlockIn.resolveTextureName(var10.field_178242_d));
                final BakedQuad quad = makeBakedQuad(var7, var10, var11, var9, modelRotationIn, uvLocked);
                if (var10.field_178244_b == null) {
                    var5.func_177648_a(quad);
                }
                else {
                    var5.func_177650_a(modelRotationIn.func_177523_a(var10.field_178244_b), quad);
                }
            }
        }
        return var5.func_177645_b();
    }
    
    private static BakedQuad makeBakedQuad(final BlockPart blockPart, final BlockPartFace blockPartFace, final TextureAtlasSprite textureAtlasSprite, final EnumFacing enumFacing, final ModelRotation modelRotation, final boolean uvLocked) {
        final FaceBakery faceBakery = new FaceBakery();
        return faceBakery.func_178414_a(blockPart.field_178241_a, blockPart.field_178239_b, blockPartFace, textureAtlasSprite, enumFacing, modelRotation, blockPart.field_178237_d, uvLocked, blockPart.field_178238_e);
    }
    
    @Override
    public String toString() {
        return this.basePath + "/" + this.name + ", type: " + this.type + ", items: [" + Config.arrayToString(this.items) + "], textture: " + this.texture;
    }
    
    public float getTextureWidth(final TextureManager textureManager) {
        if (this.textureWidth <= 0) {
            if (this.textureLocation != null) {
                final ITextureObject tex = textureManager.getTexture(this.textureLocation);
                final int texId = tex.getGlTextureId();
                final int prevTexId = GlStateManager.getBoundTexture();
                GlStateManager.func_179144_i(texId);
                this.textureWidth = GL11.glGetTexLevelParameteri(3553, 0, 4096);
                GlStateManager.func_179144_i(prevTexId);
            }
            if (this.textureWidth <= 0) {
                this.textureWidth = 16;
            }
        }
        return (float)this.textureWidth;
    }
    
    public float getTextureHeight(final TextureManager textureManager) {
        if (this.textureHeight <= 0) {
            if (this.textureLocation != null) {
                final ITextureObject tex = textureManager.getTexture(this.textureLocation);
                final int texId = tex.getGlTextureId();
                final int prevTexId = GlStateManager.getBoundTexture();
                GlStateManager.func_179144_i(texId);
                this.textureHeight = GL11.glGetTexLevelParameteri(3553, 0, 4097);
                GlStateManager.func_179144_i(prevTexId);
            }
            if (this.textureHeight <= 0) {
                this.textureHeight = 16;
            }
        }
        return (float)this.textureHeight;
    }
    
    public IBakedModel getModel(final ModelResourceLocation modelLocation) {
        if (modelLocation != null && this.mapTextures != null) {
            final String modelPath = modelLocation.getResourcePath();
            if (this.mapModels != null) {
                final IBakedModel customModel = this.mapModels.get(modelPath);
                if (customModel != null) {
                    return customModel;
                }
            }
        }
        return this.model;
    }
}
