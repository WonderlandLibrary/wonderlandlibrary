// 
// Decompiled by Procyon v0.6.0
// 

package net.optifine;

import java.util.Hashtable;
import java.util.Collection;
import net.optifine.reflect.Reflector;
import net.minecraft.client.resources.model.ModelManager;
import net.minecraft.client.resources.model.ModelResourceLocation;
import net.minecraft.client.resources.model.ModelBakery;
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
import net.optifine.util.StrUtils;
import java.util.HashMap;
import net.minecraft.client.renderer.texture.TextureMap;
import java.util.List;
import java.util.ArrayList;
import net.optifine.config.RangeInt;
import net.optifine.util.TextureUtils;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Set;
import net.minecraft.item.Item;
import java.util.TreeSet;
import net.minecraft.src.Config;
import net.optifine.render.Blender;
import net.optifine.config.IParserInt;
import net.optifine.config.ParserEnchantmentId;
import java.util.Properties;
import net.minecraft.client.resources.model.IBakedModel;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.util.ResourceLocation;
import net.optifine.config.NbtTagValue;
import net.optifine.config.RangeListInt;
import java.util.Map;

public class CustomItemProperties
{
    public String name;
    public String basePath;
    public int type;
    public int[] items;
    public String texture;
    public Map<String, String> mapTextures;
    public String model;
    public Map<String, String> mapModels;
    public RangeListInt damage;
    public boolean damagePercent;
    public int damageMask;
    public RangeListInt stackSize;
    public RangeListInt enchantmentIds;
    public RangeListInt enchantmentLevels;
    public NbtTagValue[] nbtTagValues;
    public int hand;
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
    public IBakedModel bakedModelTexture;
    public Map<String, IBakedModel> mapBakedModelsTexture;
    public IBakedModel bakedModelFull;
    public Map<String, IBakedModel> mapBakedModelsFull;
    private int textureWidth;
    private int textureHeight;
    public static final int TYPE_UNKNOWN = 0;
    public static final int TYPE_ITEM = 1;
    public static final int TYPE_ENCHANTMENT = 2;
    public static final int TYPE_ARMOR = 3;
    public static final int HAND_ANY = 0;
    public static final int HAND_MAIN = 1;
    public static final int HAND_OFF = 2;
    public static final String INVENTORY = "inventory";
    
    public CustomItemProperties(final Properties props, final String path) {
        this.name = null;
        this.basePath = null;
        this.type = 1;
        this.items = null;
        this.texture = null;
        this.mapTextures = null;
        this.model = null;
        this.mapModels = null;
        this.damage = null;
        this.damagePercent = false;
        this.damageMask = 0;
        this.stackSize = null;
        this.enchantmentIds = null;
        this.enchantmentLevels = null;
        this.nbtTagValues = null;
        this.hand = 0;
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
        this.bakedModelTexture = null;
        this.mapBakedModelsTexture = null;
        this.bakedModelFull = null;
        this.mapBakedModelsFull = null;
        this.textureWidth = 0;
        this.textureHeight = 0;
        this.name = parseName(path);
        this.basePath = parseBasePath(path);
        this.type = this.parseType(props.getProperty("type"));
        this.items = this.parseItems(props.getProperty("items"), props.getProperty("matchItems"));
        this.mapModels = parseModels(props, this.basePath);
        this.model = parseModel(props.getProperty("model"), path, this.basePath, this.type, this.mapModels);
        this.mapTextures = parseTextures(props, this.basePath);
        final boolean flag = this.mapModels == null && this.model == null;
        this.texture = parseTexture(props.getProperty("texture"), props.getProperty("tile"), props.getProperty("source"), path, this.basePath, this.type, this.mapTextures, flag);
        String s = props.getProperty("damage");
        if (s != null) {
            this.damagePercent = s.contains("%");
            s = s.replace("%", "");
            this.damage = this.parseRangeListInt(s);
            this.damageMask = this.parseInt(props.getProperty("damageMask"), 0);
        }
        this.stackSize = this.parseRangeListInt(props.getProperty("stackSize"));
        this.enchantmentIds = this.parseRangeListInt(props.getProperty("enchantmentIDs"), new ParserEnchantmentId());
        this.enchantmentLevels = this.parseRangeListInt(props.getProperty("enchantmentLevels"));
        this.nbtTagValues = this.parseNbtTagValues(props);
        this.hand = this.parseHand(props.getProperty("hand"));
        this.blend = Blender.parseBlend(props.getProperty("blend"));
        this.speed = this.parseFloat(props.getProperty("speed"), 0.0f);
        this.rotation = this.parseFloat(props.getProperty("rotation"), 0.0f);
        this.layer = this.parseInt(props.getProperty("layer"), 0);
        this.weight = this.parseInt(props.getProperty("weight"), 0);
        this.duration = this.parseFloat(props.getProperty("duration"), 1.0f);
    }
    
    private static String parseName(final String path) {
        String s = path;
        final int i = path.lastIndexOf(47);
        if (i >= 0) {
            s = path.substring(i + 1);
        }
        final int j = s.lastIndexOf(46);
        if (j >= 0) {
            s = s.substring(0, j);
        }
        return s;
    }
    
    private static String parseBasePath(final String path) {
        final int i = path.lastIndexOf(47);
        return (i < 0) ? "" : path.substring(0, i);
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
        final Set set = new TreeSet();
        final String[] astring = Config.tokenize(str, " ");
        for (int i = 0; i < astring.length; ++i) {
            final String s = astring[i];
            final int j = Config.parseInt(s, -1);
            if (j >= 0) {
                set.add(new Integer(j));
            }
            else {
                if (s.contains("-")) {
                    final String[] astring2 = Config.tokenize(s, "-");
                    if (astring2.length == 2) {
                        final int k = Config.parseInt(astring2[0], -1);
                        final int l = Config.parseInt(astring2[1], -1);
                        if (k >= 0 && l >= 0) {
                            final int i2 = Math.min(k, l);
                            for (int j2 = Math.max(k, l), k2 = i2; k2 <= j2; ++k2) {
                                set.add(new Integer(k2));
                            }
                            continue;
                        }
                    }
                }
                final Item item = Item.getByNameOrId(s);
                if (item == null) {
                    Config.warn("Item not found: " + s);
                }
                else {
                    final int i3 = Item.getIdFromItem(item);
                    if (i3 <= 0) {
                        Config.warn("Item not found: " + s);
                    }
                    else {
                        set.add(new Integer(i3));
                    }
                }
            }
        }
        final Integer[] ainteger = set.toArray(new Integer[set.size()]);
        final int[] aint = new int[ainteger.length];
        for (int l2 = 0; l2 < aint.length; ++l2) {
            aint[l2] = ainteger[l2];
        }
        return aint;
    }
    
    private static String parseTexture(String texStr, final String texStr2, final String texStr3, final String path, final String basePath, final int type, final Map<String, String> mapTexs, final boolean textureFromPath) {
        if (texStr == null) {
            texStr = texStr2;
        }
        if (texStr == null) {
            texStr = texStr3;
        }
        if (texStr != null) {
            final String s2 = ".png";
            if (texStr.endsWith(s2)) {
                texStr = texStr.substring(0, texStr.length() - s2.length());
            }
            texStr = fixTextureName(texStr, basePath);
            return texStr;
        }
        if (type == 3) {
            return null;
        }
        if (mapTexs != null) {
            final String s3 = mapTexs.get("texture.bow_standby");
            if (s3 != null) {
                return s3;
            }
        }
        if (!textureFromPath) {
            return null;
        }
        String s4 = path;
        final int i = path.lastIndexOf(47);
        if (i >= 0) {
            s4 = path.substring(i + 1);
        }
        final int j = s4.lastIndexOf(46);
        if (j >= 0) {
            s4 = s4.substring(0, j);
        }
        s4 = fixTextureName(s4, basePath);
        return s4;
    }
    
    private static Map parseTextures(final Properties props, final String basePath) {
        final String s = "texture.";
        final Map map = getMatchingProperties(props, s);
        if (map.size() <= 0) {
            return null;
        }
        final Set set = map.keySet();
        final Map map2 = new LinkedHashMap();
        for (final Object o : set) {
            final String s2 = (String)o;
            String s3 = map.get(s2);
            s3 = fixTextureName(s3, basePath);
            map2.put(s2, s3);
        }
        return map2;
    }
    
    private static String fixTextureName(String iconName, final String basePath) {
        iconName = TextureUtils.fixResourcePath(iconName, basePath);
        if (!iconName.startsWith(basePath) && !iconName.startsWith("textures/") && !iconName.startsWith("mcpatcher/")) {
            iconName = String.valueOf(basePath) + "/" + iconName;
        }
        if (iconName.endsWith(".png")) {
            iconName = iconName.substring(0, iconName.length() - 4);
        }
        if (iconName.startsWith("/")) {
            iconName = iconName.substring(1);
        }
        return iconName;
    }
    
    private static String parseModel(String modelStr, final String path, final String basePath, final int type, final Map<String, String> mapModelNames) {
        if (modelStr != null) {
            final String s1 = ".json";
            if (modelStr.endsWith(s1)) {
                modelStr = modelStr.substring(0, modelStr.length() - s1.length());
            }
            modelStr = fixModelName(modelStr, basePath);
            return modelStr;
        }
        if (type == 3) {
            return null;
        }
        if (mapModelNames != null) {
            final String s2 = mapModelNames.get("model.bow_standby");
            if (s2 != null) {
                return s2;
            }
        }
        return modelStr;
    }
    
    private static Map parseModels(final Properties props, final String basePath) {
        final String s = "model.";
        final Map map = getMatchingProperties(props, s);
        if (map.size() <= 0) {
            return null;
        }
        final Set set = map.keySet();
        final Map map2 = new LinkedHashMap();
        for (final Object o : set) {
            final String s2 = (String)o;
            String s3 = map.get(s2);
            s3 = fixModelName(s3, basePath);
            map2.put(s2, s3);
        }
        return map2;
    }
    
    private static String fixModelName(String modelName, final String basePath) {
        modelName = TextureUtils.fixResourcePath(modelName, basePath);
        final boolean flag = modelName.startsWith("block/") || modelName.startsWith("item/");
        if (!modelName.startsWith(basePath) && !flag && !modelName.startsWith("mcpatcher/")) {
            modelName = String.valueOf(basePath) + "/" + modelName;
        }
        final String s = ".json";
        if (modelName.endsWith(s)) {
            modelName = modelName.substring(0, modelName.length() - s.length());
        }
        if (modelName.startsWith("/")) {
            modelName = modelName.substring(1);
        }
        return modelName;
    }
    
    private int parseInt(String str, final int defVal) {
        if (str == null) {
            return defVal;
        }
        str = str.trim();
        final int i = Config.parseInt(str, Integer.MIN_VALUE);
        if (i == Integer.MIN_VALUE) {
            Config.warn("Invalid integer: " + str);
            return defVal;
        }
        return i;
    }
    
    private float parseFloat(String str, final float defVal) {
        if (str == null) {
            return defVal;
        }
        str = str.trim();
        final float f = Config.parseFloat(str, Float.MIN_VALUE);
        if (f == Float.MIN_VALUE) {
            Config.warn("Invalid float: " + str);
            return defVal;
        }
        return f;
    }
    
    private RangeListInt parseRangeListInt(final String str) {
        return this.parseRangeListInt(str, null);
    }
    
    private RangeListInt parseRangeListInt(final String str, final IParserInt parser) {
        if (str == null) {
            return null;
        }
        final String[] astring = Config.tokenize(str, " ");
        final RangeListInt rangelistint = new RangeListInt();
        for (int i = 0; i < astring.length; ++i) {
            final String s = astring[i];
            if (parser != null) {
                final int j = parser.parse(s, Integer.MIN_VALUE);
                if (j != Integer.MIN_VALUE) {
                    rangelistint.addRange(new RangeInt(j, j));
                    continue;
                }
            }
            final RangeInt rangeint = this.parseRangeInt(s);
            if (rangeint == null) {
                Config.warn("Invalid range list: " + str);
                return null;
            }
            rangelistint.addRange(rangeint);
        }
        return rangelistint;
    }
    
    private RangeInt parseRangeInt(String str) {
        if (str == null) {
            return null;
        }
        str = str.trim();
        final int i = str.length() - str.replace("-", "").length();
        if (i > 1) {
            Config.warn("Invalid range: " + str);
            return null;
        }
        final String[] astring = Config.tokenize(str, "- ");
        final int[] aint = new int[astring.length];
        for (int j = 0; j < astring.length; ++j) {
            final String s = astring[j];
            final int k = Config.parseInt(s, -1);
            if (k < 0) {
                Config.warn("Invalid range: " + str);
                return null;
            }
            aint[j] = k;
        }
        if (aint.length == 1) {
            final int i2 = aint[0];
            if (str.startsWith("-")) {
                return new RangeInt(0, i2);
            }
            if (str.endsWith("-")) {
                return new RangeInt(i2, 65535);
            }
            return new RangeInt(i2, i2);
        }
        else {
            if (aint.length == 2) {
                final int l = Math.min(aint[0], aint[1]);
                final int j2 = Math.max(aint[0], aint[1]);
                return new RangeInt(l, j2);
            }
            Config.warn("Invalid range: " + str);
            return null;
        }
    }
    
    private NbtTagValue[] parseNbtTagValues(final Properties props) {
        final String s = "nbt.";
        final Map map = getMatchingProperties(props, s);
        if (map.size() <= 0) {
            return null;
        }
        final List list = new ArrayList();
        for (final Object o : map.keySet()) {
            final String s2 = (String)o;
            final String s3 = map.get(s2);
            final String s4 = s2.substring(s.length());
            final NbtTagValue nbttagvalue = new NbtTagValue(s4, s3);
            list.add(nbttagvalue);
        }
        final NbtTagValue[] anbttagvalue = list.toArray(new NbtTagValue[list.size()]);
        return anbttagvalue;
    }
    
    private static Map getMatchingProperties(final Properties props, final String keyPrefix) {
        final Map map = new LinkedHashMap();
        for (final Object o : ((Hashtable<Object, V>)props).keySet()) {
            final String s = (String)o;
            final String s2 = props.getProperty(s);
            if (s.startsWith(keyPrefix)) {
                map.put(s, s2);
            }
        }
        return map;
    }
    
    private int parseHand(String str) {
        if (str == null) {
            return 0;
        }
        str = str.toLowerCase();
        if (str.equals("any")) {
            return 0;
        }
        if (str.equals("main")) {
            return 1;
        }
        if (str.equals("off")) {
            return 2;
        }
        Config.warn("Invalid hand: " + str);
        return 0;
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
        if (this.type == 1 || this.type == 3) {
            if (this.items == null) {
                this.items = this.detectItems();
            }
            if (this.items == null) {
                Config.warn("No items defined: " + path);
                return false;
            }
        }
        if (this.texture == null && this.mapTextures == null && this.model == null && this.mapModels == null) {
            Config.warn("No texture or model specified: " + path);
            return false;
        }
        if (this.type == 2 && this.enchantmentIds == null) {
            Config.warn("No enchantmentIDs specified: " + path);
            return false;
        }
        return true;
    }
    
    private int[] detectItems() {
        final Item item = Item.getByNameOrId(this.name);
        if (item == null) {
            return null;
        }
        final int i = Item.getIdFromItem(item);
        return (int[])((i <= 0) ? null : new int[] { i });
    }
    
    public void updateIcons(final TextureMap textureMap) {
        if (this.texture != null) {
            this.textureLocation = this.getTextureLocation(this.texture);
            if (this.type == 1) {
                final ResourceLocation resourcelocation = this.getSpriteLocation(this.textureLocation);
                this.sprite = textureMap.registerSprite(resourcelocation);
            }
        }
        if (this.mapTextures != null) {
            this.mapTextureLocations = new HashMap();
            this.mapSprites = new HashMap();
            for (final String s : this.mapTextures.keySet()) {
                final String s2 = this.mapTextures.get(s);
                final ResourceLocation resourcelocation2 = this.getTextureLocation(s2);
                this.mapTextureLocations.put(s, resourcelocation2);
                if (this.type == 1) {
                    final ResourceLocation resourcelocation3 = this.getSpriteLocation(resourcelocation2);
                    final TextureAtlasSprite textureatlassprite = textureMap.registerSprite(resourcelocation3);
                    this.mapSprites.put(s, textureatlassprite);
                }
            }
        }
    }
    
    private ResourceLocation getTextureLocation(final String texName) {
        if (texName == null) {
            return null;
        }
        final ResourceLocation resourcelocation = new ResourceLocation(texName);
        final String s = resourcelocation.getResourceDomain();
        String s2 = resourcelocation.getResourcePath();
        if (!s2.contains("/")) {
            s2 = "textures/items/" + s2;
        }
        final String s3 = String.valueOf(s2) + ".png";
        final ResourceLocation resourcelocation2 = new ResourceLocation(s, s3);
        final boolean flag = Config.hasResource(resourcelocation2);
        if (!flag) {
            Config.warn("File not found: " + s3);
        }
        return resourcelocation2;
    }
    
    private ResourceLocation getSpriteLocation(final ResourceLocation resLoc) {
        String s = resLoc.getResourcePath();
        s = StrUtils.removePrefix(s, "textures/");
        s = StrUtils.removeSuffix(s, ".png");
        final ResourceLocation resourcelocation = new ResourceLocation(resLoc.getResourceDomain(), s);
        return resourcelocation;
    }
    
    public void updateModelTexture(final TextureMap textureMap, final ItemModelGenerator itemModelGenerator) {
        if (this.texture != null || this.mapTextures != null) {
            final String[] astring = this.getModelTextures();
            final boolean flag = this.isUseTint();
            this.bakedModelTexture = makeBakedModel(textureMap, itemModelGenerator, astring, flag);
            if (this.type == 1 && this.mapTextures != null) {
                for (final String s : this.mapTextures.keySet()) {
                    final String s2 = this.mapTextures.get(s);
                    final String s3 = StrUtils.removePrefix(s, "texture.");
                    if (s3.startsWith("bow") || s3.startsWith("fishing_rod") || s3.startsWith("shield")) {
                        final String[] astring2 = { s2 };
                        final IBakedModel ibakedmodel = makeBakedModel(textureMap, itemModelGenerator, astring2, flag);
                        if (this.mapBakedModelsTexture == null) {
                            this.mapBakedModelsTexture = new HashMap<String, IBakedModel>();
                        }
                        this.mapBakedModelsTexture.put(s3, ibakedmodel);
                    }
                }
            }
        }
    }
    
    private boolean isUseTint() {
        return true;
    }
    
    private static IBakedModel makeBakedModel(final TextureMap textureMap, final ItemModelGenerator itemModelGenerator, final String[] textures, final boolean useTint) {
        final String[] astring = new String[textures.length];
        for (int i = 0; i < astring.length; ++i) {
            final String s = textures[i];
            astring[i] = StrUtils.removePrefix(s, "textures/");
        }
        final ModelBlock modelblock = makeModelBlock(astring);
        final ModelBlock modelblock2 = itemModelGenerator.makeItemModel(textureMap, modelblock);
        final IBakedModel ibakedmodel = bakeModel(textureMap, modelblock2, useTint);
        return ibakedmodel;
    }
    
    private String[] getModelTextures() {
        if (this.type == 1 && this.items.length == 1) {
            final Item item = Item.getItemById(this.items[0]);
            if (item == Items.potionitem && this.damage != null && this.damage.getCountRanges() > 0) {
                final RangeInt rangeint = this.damage.getRange(0);
                final int i = rangeint.getMin();
                final boolean flag = (i & 0x4000) != 0x0;
                final String s5 = this.getMapTexture(this.mapTextures, "texture.potion_overlay", "items/potion_overlay");
                String s6 = null;
                if (flag) {
                    s6 = this.getMapTexture(this.mapTextures, "texture.potion_bottle_splash", "items/potion_bottle_splash");
                }
                else {
                    s6 = this.getMapTexture(this.mapTextures, "texture.potion_bottle_drinkable", "items/potion_bottle_drinkable");
                }
                return new String[] { s5, s6 };
            }
            if (item instanceof ItemArmor) {
                final ItemArmor itemarmor = (ItemArmor)item;
                if (itemarmor.getArmorMaterial() == ItemArmor.ArmorMaterial.LEATHER) {
                    final String s7 = "leather";
                    String s8 = "helmet";
                    if (itemarmor.armorType == 0) {
                        s8 = "helmet";
                    }
                    if (itemarmor.armorType == 1) {
                        s8 = "chestplate";
                    }
                    if (itemarmor.armorType == 2) {
                        s8 = "leggings";
                    }
                    if (itemarmor.armorType == 3) {
                        s8 = "boots";
                    }
                    final String s9 = String.valueOf(s7) + "_" + s8;
                    final String s10 = this.getMapTexture(this.mapTextures, "texture." + s9, "items/" + s9);
                    final String s11 = this.getMapTexture(this.mapTextures, "texture." + s9 + "_overlay", "items/" + s9 + "_overlay");
                    return new String[] { s10, s11 };
                }
            }
        }
        return new String[] { this.texture };
    }
    
    private String getMapTexture(final Map<String, String> map, final String key, final String def) {
        if (map == null) {
            return def;
        }
        final String s = map.get(key);
        return (s == null) ? def : s;
    }
    
    private static ModelBlock makeModelBlock(final String[] modelTextures) {
        final StringBuffer stringbuffer = new StringBuffer();
        stringbuffer.append("{\"parent\": \"builtin/generated\",\"textures\": {");
        for (int i = 0; i < modelTextures.length; ++i) {
            final String s = modelTextures[i];
            if (i > 0) {
                stringbuffer.append(", ");
            }
            stringbuffer.append("\"layer" + i + "\": \"" + s + "\"");
        }
        stringbuffer.append("}}");
        final String s2 = stringbuffer.toString();
        final ModelBlock modelblock = ModelBlock.deserialize(s2);
        return modelblock;
    }
    
    private static IBakedModel bakeModel(final TextureMap textureMap, final ModelBlock modelBlockIn, final boolean useTint) {
        final ModelRotation modelrotation = ModelRotation.X0_Y0;
        final boolean flag = false;
        final String s = modelBlockIn.resolveTextureName("particle");
        final TextureAtlasSprite textureatlassprite = textureMap.getAtlasSprite(new ResourceLocation(s).toString());
        final SimpleBakedModel.Builder simplebakedmodel$builder = new SimpleBakedModel.Builder(modelBlockIn).setTexture(textureatlassprite);
        for (final BlockPart blockpart : modelBlockIn.getElements()) {
            for (final EnumFacing enumfacing : blockpart.mapFaces.keySet()) {
                BlockPartFace blockpartface = blockpart.mapFaces.get(enumfacing);
                if (!useTint) {
                    blockpartface = new BlockPartFace(blockpartface.cullFace, -1, blockpartface.texture, blockpartface.blockFaceUV);
                }
                final String s2 = modelBlockIn.resolveTextureName(blockpartface.texture);
                final TextureAtlasSprite textureatlassprite2 = textureMap.getAtlasSprite(new ResourceLocation(s2).toString());
                final BakedQuad bakedquad = makeBakedQuad(blockpart, blockpartface, textureatlassprite2, enumfacing, modelrotation, flag);
                if (blockpartface.cullFace == null) {
                    simplebakedmodel$builder.addGeneralQuad(bakedquad);
                }
                else {
                    simplebakedmodel$builder.addFaceQuad(modelrotation.rotateFace(blockpartface.cullFace), bakedquad);
                }
            }
        }
        return simplebakedmodel$builder.makeBakedModel();
    }
    
    private static BakedQuad makeBakedQuad(final BlockPart blockPart, final BlockPartFace blockPartFace, final TextureAtlasSprite textureAtlasSprite, final EnumFacing enumFacing, final ModelRotation modelRotation, final boolean uvLocked) {
        final FaceBakery facebakery = new FaceBakery();
        return facebakery.makeBakedQuad(blockPart.positionFrom, blockPart.positionTo, blockPartFace, textureAtlasSprite, enumFacing, modelRotation, blockPart.partRotation, uvLocked, blockPart.shade);
    }
    
    @Override
    public String toString() {
        return this.basePath + "/" + this.name + ", type: " + this.type + ", items: [" + Config.arrayToString(this.items) + "], textture: " + this.texture;
    }
    
    public float getTextureWidth(final TextureManager textureManager) {
        if (this.textureWidth <= 0) {
            if (this.textureLocation != null) {
                final ITextureObject itextureobject = textureManager.getTexture(this.textureLocation);
                final int i = itextureobject.getGlTextureId();
                final int j = GlStateManager.getBoundTexture();
                GlStateManager.bindTexture(i);
                this.textureWidth = GL11.glGetTexLevelParameteri(3553, 0, 4096);
                GlStateManager.bindTexture(j);
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
                final ITextureObject itextureobject = textureManager.getTexture(this.textureLocation);
                final int i = itextureobject.getGlTextureId();
                final int j = GlStateManager.getBoundTexture();
                GlStateManager.bindTexture(i);
                this.textureHeight = GL11.glGetTexLevelParameteri(3553, 0, 4097);
                GlStateManager.bindTexture(j);
            }
            if (this.textureHeight <= 0) {
                this.textureHeight = 16;
            }
        }
        return (float)this.textureHeight;
    }
    
    public IBakedModel getBakedModel(final ResourceLocation modelLocation, final boolean fullModel) {
        IBakedModel ibakedmodel;
        Map<String, IBakedModel> map;
        if (fullModel) {
            ibakedmodel = this.bakedModelFull;
            map = this.mapBakedModelsFull;
        }
        else {
            ibakedmodel = this.bakedModelTexture;
            map = this.mapBakedModelsTexture;
        }
        if (modelLocation != null && map != null) {
            final String s = modelLocation.getResourcePath();
            final IBakedModel ibakedmodel2 = map.get(s);
            if (ibakedmodel2 != null) {
                return ibakedmodel2;
            }
        }
        return ibakedmodel;
    }
    
    public void loadModels(final ModelBakery modelBakery) {
        if (this.model != null) {
            loadItemModel(modelBakery, this.model);
        }
        if (this.type == 1 && this.mapModels != null) {
            for (final String s : this.mapModels.keySet()) {
                final String s2 = this.mapModels.get(s);
                final String s3 = StrUtils.removePrefix(s, "model.");
                if (s3.startsWith("bow") || s3.startsWith("fishing_rod") || s3.startsWith("shield")) {
                    loadItemModel(modelBakery, s2);
                }
            }
        }
    }
    
    public void updateModelsFull() {
        final ModelManager modelmanager = Config.getModelManager();
        final IBakedModel ibakedmodel = modelmanager.getMissingModel();
        if (this.model != null) {
            final ResourceLocation resourcelocation = getModelLocation(this.model);
            final ModelResourceLocation modelresourcelocation = new ModelResourceLocation(resourcelocation, "inventory");
            this.bakedModelFull = modelmanager.getModel(modelresourcelocation);
            if (this.bakedModelFull == ibakedmodel) {
                Config.warn("Custom Items: Model not found " + modelresourcelocation.getResourcePath());
                this.bakedModelFull = null;
            }
        }
        if (this.type == 1 && this.mapModels != null) {
            for (final String s : this.mapModels.keySet()) {
                final String s2 = this.mapModels.get(s);
                final String s3 = StrUtils.removePrefix(s, "model.");
                if (s3.startsWith("bow") || s3.startsWith("fishing_rod") || s3.startsWith("shield")) {
                    final ResourceLocation resourcelocation2 = getModelLocation(s2);
                    final ModelResourceLocation modelresourcelocation2 = new ModelResourceLocation(resourcelocation2, "inventory");
                    final IBakedModel ibakedmodel2 = modelmanager.getModel(modelresourcelocation2);
                    if (ibakedmodel2 == ibakedmodel) {
                        Config.warn("Custom Items: Model not found " + modelresourcelocation2.getResourcePath());
                    }
                    else {
                        if (this.mapBakedModelsFull == null) {
                            this.mapBakedModelsFull = new HashMap<String, IBakedModel>();
                        }
                        this.mapBakedModelsFull.put(s3, ibakedmodel2);
                    }
                }
            }
        }
    }
    
    private static void loadItemModel(final ModelBakery modelBakery, final String model) {
        final ResourceLocation resourcelocation = getModelLocation(model);
        final ModelResourceLocation modelresourcelocation = new ModelResourceLocation(resourcelocation, "inventory");
        if (Reflector.ModelLoader.exists()) {
            try {
                final Object object = Reflector.ModelLoader_VanillaLoader_INSTANCE.getValue();
                checkNull(object, "vanillaLoader is null");
                final Object object2 = Reflector.call(object, Reflector.ModelLoader_VanillaLoader_loadModel, modelresourcelocation);
                checkNull(object2, "iModel is null");
                final Map map = (Map)Reflector.getFieldValue(modelBakery, Reflector.ModelLoader_stateModels);
                checkNull(map, "stateModels is null");
                map.put(modelresourcelocation, object2);
                final Set set = (Set)Reflector.getFieldValue(modelBakery, Reflector.ModelLoader_textures);
                checkNull(set, "registryTextures is null");
                final Collection collection = (Collection)Reflector.call(object2, Reflector.IModel_getTextures, new Object[0]);
                checkNull(collection, "modelTextures is null");
                set.addAll(collection);
            }
            catch (final Exception exception) {
                Config.warn("Error registering model with ModelLoader: " + modelresourcelocation + ", " + exception.getClass().getName() + ": " + exception.getMessage());
            }
        }
        else {
            modelBakery.loadItemModel(resourcelocation.toString(), modelresourcelocation, resourcelocation);
        }
    }
    
    private static void checkNull(final Object obj, final String msg) throws NullPointerException {
        if (obj == null) {
            throw new NullPointerException(msg);
        }
    }
    
    private static ResourceLocation getModelLocation(final String modelName) {
        return (Reflector.ModelLoader.exists() && !modelName.startsWith("mcpatcher/") && !modelName.startsWith("optifine/")) ? new ResourceLocation("models/" + modelName) : new ResourceLocation(modelName);
    }
}
