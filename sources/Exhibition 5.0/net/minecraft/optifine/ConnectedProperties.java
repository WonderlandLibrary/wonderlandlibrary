// 
// Decompiled by Procyon v0.5.30
// 

package net.minecraft.optifine;

import net.minecraft.util.ResourceLocation;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.client.Minecraft;
import net.minecraft.block.Block;
import java.util.ArrayList;
import java.util.Properties;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.world.biome.BiomeGenBase;

public class ConnectedProperties
{
    public String name;
    public String basePath;
    public int[] matchBlocks;
    public String[] matchTiles;
    public int method;
    public String[] tiles;
    public int connect;
    public int faces;
    public int[] metadatas;
    public BiomeGenBase[] biomes;
    public int minHeight;
    public int maxHeight;
    public int renderPass;
    public boolean innerSeams;
    public int width;
    public int height;
    public int[] weights;
    public int symmetry;
    public int[] sumWeights;
    public int sumAllWeights;
    public TextureAtlasSprite[] matchTileIcons;
    public TextureAtlasSprite[] tileIcons;
    public static final int METHOD_NONE = 0;
    public static final int METHOD_CTM = 1;
    public static final int METHOD_HORIZONTAL = 2;
    public static final int METHOD_TOP = 3;
    public static final int METHOD_RANDOM = 4;
    public static final int METHOD_REPEAT = 5;
    public static final int METHOD_VERTICAL = 6;
    public static final int METHOD_FIXED = 7;
    public static final int METHOD_HORIZONTAL_VERTICAL = 8;
    public static final int METHOD_VERTICAL_HORIZONTAL = 9;
    public static final int CONNECT_NONE = 0;
    public static final int CONNECT_BLOCK = 1;
    public static final int CONNECT_TILE = 2;
    public static final int CONNECT_MATERIAL = 3;
    public static final int CONNECT_UNKNOWN = 128;
    public static final int FACE_BOTTOM = 1;
    public static final int FACE_TOP = 2;
    public static final int FACE_NORTH = 4;
    public static final int FACE_SOUTH = 8;
    public static final int FACE_WEST = 16;
    public static final int FACE_EAST = 32;
    public static final int FACE_SIDES = 60;
    public static final int FACE_ALL = 63;
    public static final int FACE_UNKNOWN = 128;
    public static final int SYMMETRY_NONE = 1;
    public static final int SYMMETRY_OPPOSITE = 2;
    public static final int SYMMETRY_ALL = 6;
    public static final int SYMMETRY_UNKNOWN = 128;
    
    public ConnectedProperties(final Properties props, final String path) {
        this.name = null;
        this.basePath = null;
        this.matchBlocks = null;
        this.matchTiles = null;
        this.method = 0;
        this.tiles = null;
        this.connect = 0;
        this.faces = 63;
        this.metadatas = null;
        this.biomes = null;
        this.minHeight = 0;
        this.maxHeight = 1024;
        this.renderPass = 0;
        this.innerSeams = false;
        this.width = 0;
        this.height = 0;
        this.weights = null;
        this.symmetry = 1;
        this.sumWeights = null;
        this.sumAllWeights = 1;
        this.matchTileIcons = null;
        this.tileIcons = null;
        this.name = parseName(path);
        this.basePath = parseBasePath(path);
        this.matchBlocks = parseBlockIds(props.getProperty("matchBlocks"));
        this.matchTiles = this.parseMatchTiles(props.getProperty("matchTiles"));
        this.method = parseMethod(props.getProperty("method"));
        this.tiles = this.parseTileNames(props.getProperty("tiles"));
        this.connect = parseConnect(props.getProperty("connect"));
        this.faces = parseFaces(props.getProperty("faces"));
        this.metadatas = parseInts(props.getProperty("metadata"));
        this.biomes = parseBiomes(props.getProperty("biomes"));
        this.minHeight = parseInt(props.getProperty("minHeight"), -1);
        this.maxHeight = parseInt(props.getProperty("maxHeight"), 1024);
        this.renderPass = parseInt(props.getProperty("renderPass"));
        this.innerSeams = parseBoolean(props.getProperty("innerSeams"));
        this.width = parseInt(props.getProperty("width"));
        this.height = parseInt(props.getProperty("height"));
        this.weights = parseInts(props.getProperty("weights"));
        this.symmetry = parseSymmetry(props.getProperty("symmetry"));
    }
    
    private String[] parseMatchTiles(final String str) {
        if (str == null) {
            return null;
        }
        final String[] names = Config.tokenize(str, " ");
        for (int i = 0; i < names.length; ++i) {
            String iconName = names[i];
            if (iconName.endsWith(".png")) {
                iconName = iconName.substring(0, iconName.length() - 4);
            }
            iconName = TextureUtils.fixResourcePath(iconName, this.basePath);
            names[i] = iconName;
        }
        return names;
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
    
    private static BiomeGenBase[] parseBiomes(final String str) {
        if (str == null) {
            return null;
        }
        final String[] biomeNames = Config.tokenize(str, " ");
        final ArrayList list = new ArrayList();
        for (final String biomeName : biomeNames) {
            final BiomeGenBase biome = findBiome(biomeName);
            if (biome == null) {
                Config.warn("Biome not found: " + biomeName);
            }
            else {
                list.add(biome);
            }
        }
        final BiomeGenBase[] var6 = list.toArray(new BiomeGenBase[list.size()]);
        return var6;
    }
    
    private static BiomeGenBase findBiome(String biomeName) {
        biomeName = biomeName.toLowerCase();
        final BiomeGenBase[] biomeGenArray;
        final BiomeGenBase[] biomeList = biomeGenArray = BiomeGenBase.getBiomeGenArray();
        for (final BiomeGenBase biome : biomeGenArray) {
            if (biome != null) {
                final String name = biome.biomeName.replace(" ", "").toLowerCase();
                if (name.equals(biomeName)) {
                    return biome;
                }
            }
        }
        return null;
    }
    
    private String[] parseTileNames(final String str) {
        if (str == null) {
            return null;
        }
        final ArrayList list = new ArrayList();
        final String[] iconStrs = Config.tokenize(str, " ,");
        for (int names = 0; names < iconStrs.length; ++names) {
            final String i = iconStrs[names];
            if (i.contains("-")) {
                final String[] iconName = Config.tokenize(i, "-");
                if (iconName.length == 2) {
                    final int pathBlocks = Config.parseInt(iconName[0], -1);
                    final int max = Config.parseInt(iconName[1], -1);
                    if (pathBlocks >= 0 && max >= 0) {
                        if (pathBlocks <= max) {
                            for (int n = pathBlocks; n <= max; ++n) {
                                list.add(String.valueOf(n));
                            }
                            continue;
                        }
                        Config.warn("Invalid interval: " + i + ", when parsing: " + str);
                        continue;
                    }
                }
            }
            list.add(i);
        }
        final String[] var10 = list.toArray(new String[list.size()]);
        for (int var11 = 0; var11 < var10.length; ++var11) {
            String var12 = var10[var11];
            var12 = TextureUtils.fixResourcePath(var12, this.basePath);
            if (!var12.startsWith(this.basePath) && !var12.startsWith("textures/") && !var12.startsWith("mcpatcher/")) {
                var12 = this.basePath + "/" + var12;
            }
            if (var12.endsWith(".png")) {
                var12 = var12.substring(0, var12.length() - 4);
            }
            final String var13 = "textures/blocks/";
            if (var12.startsWith(var13)) {
                var12 = var12.substring(var13.length());
            }
            if (var12.startsWith("/")) {
                var12 = var12.substring(1);
            }
            var10[var11] = var12;
        }
        return var10;
    }
    
    private static int parseInt(final String str) {
        if (str == null) {
            return -1;
        }
        final int num = Config.parseInt(str, -1);
        if (num < 0) {
            Config.warn("Invalid number: " + str);
        }
        return num;
    }
    
    private static int parseInt(final String str, final int defVal) {
        if (str == null) {
            return defVal;
        }
        final int num = Config.parseInt(str, -1);
        if (num < 0) {
            Config.warn("Invalid number: " + str);
            return defVal;
        }
        return num;
    }
    
    private static boolean parseBoolean(final String str) {
        return str != null && str.toLowerCase().equals("true");
    }
    
    private static int parseSymmetry(final String str) {
        if (str == null) {
            return 1;
        }
        if (str.equals("opposite")) {
            return 2;
        }
        if (str.equals("all")) {
            return 6;
        }
        Config.warn("Unknown symmetry: " + str);
        return 1;
    }
    
    private static int parseFaces(final String str) {
        if (str == null) {
            return 63;
        }
        final String[] faceStrs = Config.tokenize(str, " ,");
        int facesMask = 0;
        for (final String faceStr : faceStrs) {
            final int faceMask = parseFace(faceStr);
            facesMask |= faceMask;
        }
        return facesMask;
    }
    
    private static int parseFace(String str) {
        str = str.toLowerCase();
        if (str.equals("bottom") || str.equals("down")) {
            return 1;
        }
        if (str.equals("top") || str.equals("up")) {
            return 2;
        }
        if (str.equals("north")) {
            return 4;
        }
        if (str.equals("south")) {
            return 8;
        }
        if (str.equals("east")) {
            return 32;
        }
        if (str.equals("west")) {
            return 16;
        }
        if (str.equals("sides")) {
            return 60;
        }
        if (str.equals("all")) {
            return 63;
        }
        Config.warn("Unknown face: " + str);
        return 128;
    }
    
    private static int parseConnect(final String str) {
        if (str == null) {
            return 0;
        }
        if (str.equals("block")) {
            return 1;
        }
        if (str.equals("tile")) {
            return 2;
        }
        if (str.equals("material")) {
            return 3;
        }
        Config.warn("Unknown connect: " + str);
        return 128;
    }
    
    private static int[] parseInts(final String str) {
        if (str == null) {
            return null;
        }
        final ArrayList list = new ArrayList();
        final String[] tokenize;
        final String[] intStrs = tokenize = Config.tokenize(str, " ,");
        for (final String i : tokenize) {
            if (i.contains("-")) {
                final String[] val = Config.tokenize(i, "-");
                if (val.length != 2) {
                    Config.warn("Invalid interval: " + i + ", when parsing: " + str);
                }
                else {
                    final int min = Config.parseInt(val[0], -1);
                    final int max = Config.parseInt(val[1], -1);
                    if (min >= 0 && max >= 0 && min <= max) {
                        for (int n = min; n <= max; ++n) {
                            list.add(n);
                        }
                    }
                    else {
                        Config.warn("Invalid interval: " + i + ", when parsing: " + str);
                    }
                }
            }
            else {
                final int var11 = Config.parseInt(i, -1);
                if (var11 < 0) {
                    Config.warn("Invalid number: " + i + ", when parsing: " + str);
                }
                else {
                    list.add(var11);
                }
            }
        }
        final int[] var12 = new int[list.size()];
        for (int var13 = 0; var13 < var12.length; ++var13) {
            var12[var13] = list.get(var13);
        }
        return var12;
    }
    
    private static int[] parseBlockIds(final String str) {
        if (str == null) {
            return null;
        }
        final ArrayList list = new ArrayList();
        final String[] tokenize;
        final String[] intStrs = tokenize = Config.tokenize(str, " ,");
        for (final String i : tokenize) {
            if (i.contains("-")) {
                final String[] val = Config.tokenize(i, "-");
                if (val.length != 2) {
                    Config.warn("Invalid interval: " + i + ", when parsing: " + str);
                }
                else {
                    final int min = parseBlockId(val[0]);
                    final int max = parseBlockId(val[1]);
                    if (min >= 0 && max >= 0 && min <= max) {
                        for (int n = min; n <= max; ++n) {
                            list.add(n);
                        }
                    }
                    else {
                        Config.warn("Invalid interval: " + i + ", when parsing: " + str);
                    }
                }
            }
            else {
                final int var11 = parseBlockId(i);
                if (var11 < 0) {
                    Config.warn("Invalid block ID: " + i + ", when parsing: " + str);
                }
                else {
                    list.add(var11);
                }
            }
        }
        final int[] var12 = new int[list.size()];
        for (int var13 = 0; var13 < var12.length; ++var13) {
            var12[var13] = list.get(var13);
        }
        return var12;
    }
    
    private static int parseBlockId(final String blockStr) {
        final int val = Config.parseInt(blockStr, -1);
        if (val >= 0) {
            return val;
        }
        final Block block = Block.getBlockFromName(blockStr);
        return (block != null) ? Block.getIdFromBlock(block) : -1;
    }
    
    private static int parseMethod(final String str) {
        if (str == null) {
            return 1;
        }
        if (str.equals("ctm") || str.equals("glass")) {
            return 1;
        }
        if (str.equals("horizontal") || str.equals("bookshelf")) {
            return 2;
        }
        if (str.equals("vertical")) {
            return 6;
        }
        if (str.equals("top")) {
            return 3;
        }
        if (str.equals("random")) {
            return 4;
        }
        if (str.equals("repeat")) {
            return 5;
        }
        if (str.equals("fixed")) {
            return 7;
        }
        if (str.equals("horizontal+vertical") || str.equals("h+v")) {
            return 8;
        }
        if (!str.equals("vertical+horizontal") && !str.equals("v+h")) {
            Config.warn("Unknown method: " + str);
            return 0;
        }
        return 9;
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
        if (this.matchBlocks == null) {
            this.matchBlocks = this.detectMatchBlocks();
        }
        if (this.matchTiles == null && this.matchBlocks == null) {
            this.matchTiles = this.detectMatchTiles();
        }
        if (this.matchBlocks == null && this.matchTiles == null) {
            Config.warn("No matchBlocks or matchTiles specified: " + path);
            return false;
        }
        if (this.method == 0) {
            Config.warn("No method: " + path);
            return false;
        }
        if (this.tiles == null || this.tiles.length <= 0) {
            Config.warn("No tiles specified: " + path);
            return false;
        }
        if (this.connect == 0) {
            this.connect = this.detectConnect();
        }
        if (this.connect == 128) {
            Config.warn("Invalid connect in: " + path);
            return false;
        }
        if (this.renderPass > 0) {
            Config.warn("Render pass not supported: " + this.renderPass);
            return false;
        }
        if ((this.faces & 0x80) != 0x0) {
            Config.warn("Invalid faces in: " + path);
            return false;
        }
        if ((this.symmetry & 0x80) != 0x0) {
            Config.warn("Invalid symmetry in: " + path);
            return false;
        }
        switch (this.method) {
            case 1: {
                return this.isValidCtm(path);
            }
            case 2: {
                return this.isValidHorizontal(path);
            }
            case 3: {
                return this.isValidTop(path);
            }
            case 4: {
                return this.isValidRandom(path);
            }
            case 5: {
                return this.isValidRepeat(path);
            }
            case 6: {
                return this.isValidVertical(path);
            }
            case 7: {
                return this.isValidFixed(path);
            }
            case 8: {
                return this.isValidHorizontalVertical(path);
            }
            case 9: {
                return this.isValidVerticalHorizontal(path);
            }
            default: {
                Config.warn("Unknown method: " + path);
                return false;
            }
        }
    }
    
    private int detectConnect() {
        return (this.matchBlocks != null) ? 1 : ((this.matchTiles != null) ? 2 : 128);
    }
    
    private int[] detectMatchBlocks() {
        if (!this.name.startsWith("block")) {
            return null;
        }
        int pos;
        int startPos;
        for (startPos = (pos = "block".length()); pos < this.name.length(); ++pos) {
            final char idStr = this.name.charAt(pos);
            if (idStr < '0') {
                break;
            }
            if (idStr > '9') {
                break;
            }
        }
        if (pos == startPos) {
            return null;
        }
        final String var5 = this.name.substring(startPos, pos);
        final int id = Config.parseInt(var5, -1);
        final int[] array;
        if (id >= 0) {
            array = new int[] { id };
        }
        return array;
    }
    
    private String[] detectMatchTiles() {
        final TextureAtlasSprite icon = getIcon(this.name);
        final String[] array;
        if (icon != null) {
            array = new String[] { this.name };
        }
        return array;
    }
    
    private static TextureAtlasSprite getIcon(final String iconName) {
        final TextureMap textureMapBlocks = Minecraft.getMinecraft().getTextureMapBlocks();
        TextureAtlasSprite icon = textureMapBlocks.getSpriteSafe(iconName);
        if (icon != null) {
            return icon;
        }
        icon = textureMapBlocks.getSpriteSafe("blocks/" + iconName);
        return icon;
    }
    
    private boolean isValidCtm(final String path) {
        if (this.tiles == null) {
            this.tiles = this.parseTileNames("0-11 16-27 32-43 48-58");
        }
        if (this.tiles.length < 47) {
            Config.warn("Invalid tiles, must be at least 47: " + path);
            return false;
        }
        return true;
    }
    
    private boolean isValidHorizontal(final String path) {
        if (this.tiles == null) {
            this.tiles = this.parseTileNames("12-15");
        }
        if (this.tiles.length != 4) {
            Config.warn("Invalid tiles, must be exactly 4: " + path);
            return false;
        }
        return true;
    }
    
    private boolean isValidVertical(final String path) {
        if (this.tiles == null) {
            Config.warn("No tiles defined for vertical: " + path);
            return false;
        }
        if (this.tiles.length != 4) {
            Config.warn("Invalid tiles, must be exactly 4: " + path);
            return false;
        }
        return true;
    }
    
    private boolean isValidHorizontalVertical(final String path) {
        if (this.tiles == null) {
            Config.warn("No tiles defined for horizontal+vertical: " + path);
            return false;
        }
        if (this.tiles.length != 7) {
            Config.warn("Invalid tiles, must be exactly 7: " + path);
            return false;
        }
        return true;
    }
    
    private boolean isValidVerticalHorizontal(final String path) {
        if (this.tiles == null) {
            Config.warn("No tiles defined for vertical+horizontal: " + path);
            return false;
        }
        if (this.tiles.length != 7) {
            Config.warn("Invalid tiles, must be exactly 7: " + path);
            return false;
        }
        return true;
    }
    
    private boolean isValidRandom(final String path) {
        if (this.tiles != null && this.tiles.length > 0) {
            if (this.weights != null) {
                if (this.weights.length > this.tiles.length) {
                    Config.warn("More weights defined than tiles, trimming weights: " + path);
                    final int[] sum = new int[this.tiles.length];
                    System.arraycopy(this.weights, 0, sum, 0, sum.length);
                    this.weights = sum;
                }
                if (this.weights.length < this.tiles.length) {
                    Config.warn("Less weights defined than tiles, expanding weights: " + path);
                    final int[] sum = new int[this.tiles.length];
                    System.arraycopy(this.weights, 0, sum, 0, this.weights.length);
                    final int i = this.getAverage(this.weights);
                    for (int i2 = this.weights.length; i2 < sum.length; ++i2) {
                        sum[i2] = i;
                    }
                    this.weights = sum;
                }
                this.sumWeights = new int[this.weights.length];
                int var5 = 0;
                for (int i = 0; i < this.weights.length; ++i) {
                    var5 += this.weights[i];
                    this.sumWeights[i] = var5;
                }
                this.sumAllWeights = var5;
                if (this.sumAllWeights <= 0) {
                    Config.warn("Invalid sum of all weights: " + var5);
                    this.sumAllWeights = 1;
                }
            }
            return true;
        }
        Config.warn("Tiles not defined: " + path);
        return false;
    }
    
    private int getAverage(final int[] vals) {
        if (vals.length <= 0) {
            return 0;
        }
        int sum = 0;
        for (int avg = 0; avg < vals.length; ++avg) {
            final int val = vals[avg];
            sum += val;
        }
        int avg = sum / vals.length;
        return avg;
    }
    
    private boolean isValidRepeat(final String path) {
        if (this.tiles == null) {
            Config.warn("Tiles not defined: " + path);
            return false;
        }
        if (this.width <= 0 || this.width > 16) {
            Config.warn("Invalid width: " + path);
            return false;
        }
        if (this.height <= 0 || this.height > 16) {
            Config.warn("Invalid height: " + path);
            return false;
        }
        if (this.tiles.length != this.width * this.height) {
            Config.warn("Number of tiles does not equal width x height: " + path);
            return false;
        }
        return true;
    }
    
    private boolean isValidFixed(final String path) {
        if (this.tiles == null) {
            Config.warn("Tiles not defined: " + path);
            return false;
        }
        if (this.tiles.length != 1) {
            Config.warn("Number of tiles should be 1 for method: fixed.");
            return false;
        }
        return true;
    }
    
    private boolean isValidTop(final String path) {
        if (this.tiles == null) {
            this.tiles = this.parseTileNames("66");
        }
        if (this.tiles.length != 1) {
            Config.warn("Invalid tiles, must be exactly 1: " + path);
            return false;
        }
        return true;
    }
    
    public void updateIcons(final TextureMap textureMap) {
        if (this.matchTiles != null) {
            this.matchTileIcons = registerIcons(this.matchTiles, textureMap);
        }
        if (this.tiles != null) {
            this.tileIcons = registerIcons(this.tiles, textureMap);
        }
    }
    
    private static TextureAtlasSprite[] registerIcons(final String[] tileNames, final TextureMap textureMap) {
        if (tileNames == null) {
            return null;
        }
        final ArrayList iconList = new ArrayList();
        for (final String iconName : tileNames) {
            final ResourceLocation resLoc = new ResourceLocation(iconName);
            final String domain = resLoc.getResourceDomain();
            String path = resLoc.getResourcePath();
            if (!path.contains("/")) {
                path = "textures/blocks/" + path;
            }
            final String filePath = path + ".png";
            final ResourceLocation locFile = new ResourceLocation(domain, filePath);
            final boolean exists = Config.hasResource(locFile);
            if (!exists) {
                Config.warn("File not found: " + filePath);
            }
            final String prefixTextures = "textures/";
            String pathSprite = path;
            if (path.startsWith(prefixTextures)) {
                pathSprite = path.substring(prefixTextures.length());
            }
            final ResourceLocation locSprite = new ResourceLocation(domain, pathSprite);
            final TextureAtlasSprite icon = textureMap.func_174942_a(locSprite);
            iconList.add(icon);
        }
        final TextureAtlasSprite[] var15 = iconList.toArray(new TextureAtlasSprite[iconList.size()]);
        return var15;
    }
    
    public boolean matchesBlock(final int blockId) {
        if (this.matchBlocks != null && this.matchBlocks.length > 0) {
            for (final int matchId : this.matchBlocks) {
                if (matchId == blockId) {
                    return true;
                }
            }
            return false;
        }
        return true;
    }
    
    public boolean matchesIcon(final TextureAtlasSprite icon) {
        if (this.matchTileIcons != null && this.matchTileIcons.length > 0) {
            for (final TextureAtlasSprite matchTileIcon : this.matchTileIcons) {
                if (matchTileIcon == icon) {
                    return true;
                }
            }
            return false;
        }
        return true;
    }
    
    @Override
    public String toString() {
        return "CTM name: " + this.name + ", basePath: " + this.basePath + ", matchBlocks: " + Config.arrayToString(this.matchBlocks) + ", matchTiles: " + Config.arrayToString(this.matchTiles);
    }
}
