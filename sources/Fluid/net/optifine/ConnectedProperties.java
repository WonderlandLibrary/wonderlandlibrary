// 
// Decompiled by Procyon v0.6.0
// 

package net.optifine;

import java.util.Hashtable;
import net.optifine.config.Matches;
import net.minecraft.util.ResourceLocation;
import net.optifine.util.MathUtils;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.client.Minecraft;
import net.minecraft.block.properties.IProperty;
import java.util.Collection;
import java.util.List;
import java.util.ArrayList;
import net.optifine.util.TextureUtils;
import java.util.Iterator;
import java.util.Map;
import net.minecraft.src.Config;
import java.util.HashMap;
import net.optifine.config.RangeInt;
import net.optifine.config.ConnectedParser;
import net.minecraft.init.Blocks;
import java.util.Properties;
import net.minecraft.util.EnumWorldBlockLayer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.optifine.config.NbtTagValue;
import net.optifine.config.RangeListInt;
import net.minecraft.world.biome.BiomeGenBase;
import net.optifine.config.MatchBlock;

public class ConnectedProperties
{
    public String name;
    public String basePath;
    public MatchBlock[] matchBlocks;
    public int[] metadatas;
    public String[] matchTiles;
    public int method;
    public String[] tiles;
    public int connect;
    public int faces;
    public BiomeGenBase[] biomes;
    public RangeListInt heights;
    public int renderPass;
    public boolean innerSeams;
    public int[] ctmTileIndexes;
    public int width;
    public int height;
    public int[] weights;
    public int randomLoops;
    public int symmetry;
    public boolean linked;
    public NbtTagValue nbtName;
    public int[] sumWeights;
    public int sumAllWeights;
    public TextureAtlasSprite[] matchTileIcons;
    public TextureAtlasSprite[] tileIcons;
    public MatchBlock[] connectBlocks;
    public String[] connectTiles;
    public TextureAtlasSprite[] connectTileIcons;
    public int tintIndex;
    public IBlockState tintBlockState;
    public EnumWorldBlockLayer layer;
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
    public static final int METHOD_CTM_COMPACT = 10;
    public static final int METHOD_OVERLAY = 11;
    public static final int METHOD_OVERLAY_FIXED = 12;
    public static final int METHOD_OVERLAY_RANDOM = 13;
    public static final int METHOD_OVERLAY_REPEAT = 14;
    public static final int METHOD_OVERLAY_CTM = 15;
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
    public static final String TILE_SKIP_PNG = "<skip>.png";
    public static final String TILE_DEFAULT_PNG = "<default>.png";
    
    public ConnectedProperties(final Properties props, final String path) {
        this.name = null;
        this.basePath = null;
        this.matchBlocks = null;
        this.metadatas = null;
        this.matchTiles = null;
        this.method = 0;
        this.tiles = null;
        this.connect = 0;
        this.faces = 63;
        this.biomes = null;
        this.heights = null;
        this.renderPass = 0;
        this.innerSeams = false;
        this.ctmTileIndexes = null;
        this.width = 0;
        this.height = 0;
        this.weights = null;
        this.randomLoops = 0;
        this.symmetry = 1;
        this.linked = false;
        this.nbtName = null;
        this.sumWeights = null;
        this.sumAllWeights = 1;
        this.matchTileIcons = null;
        this.tileIcons = null;
        this.connectBlocks = null;
        this.connectTiles = null;
        this.connectTileIcons = null;
        this.tintIndex = -1;
        this.tintBlockState = Blocks.air.getDefaultState();
        this.layer = null;
        final ConnectedParser connectedparser = new ConnectedParser("ConnectedTextures");
        this.name = connectedparser.parseName(path);
        this.basePath = connectedparser.parseBasePath(path);
        this.matchBlocks = connectedparser.parseMatchBlocks(props.getProperty("matchBlocks"));
        this.metadatas = connectedparser.parseIntList(props.getProperty("metadata"));
        this.matchTiles = this.parseMatchTiles(props.getProperty("matchTiles"));
        this.method = parseMethod(props.getProperty("method"));
        this.tiles = this.parseTileNames(props.getProperty("tiles"));
        this.connect = parseConnect(props.getProperty("connect"));
        this.faces = parseFaces(props.getProperty("faces"));
        this.biomes = connectedparser.parseBiomes(props.getProperty("biomes"));
        this.heights = connectedparser.parseRangeListInt(props.getProperty("heights"));
        if (this.heights == null) {
            final int i = connectedparser.parseInt(props.getProperty("minHeight"), -1);
            final int j = connectedparser.parseInt(props.getProperty("maxHeight"), 1024);
            if (i != -1 || j != 1024) {
                this.heights = new RangeListInt(new RangeInt(i, j));
            }
        }
        this.renderPass = connectedparser.parseInt(props.getProperty("renderPass"), -1);
        this.innerSeams = connectedparser.parseBoolean(props.getProperty("innerSeams"), false);
        this.ctmTileIndexes = this.parseCtmTileIndexes(props);
        this.width = connectedparser.parseInt(props.getProperty("width"), -1);
        this.height = connectedparser.parseInt(props.getProperty("height"), -1);
        this.weights = connectedparser.parseIntList(props.getProperty("weights"));
        this.randomLoops = connectedparser.parseInt(props.getProperty("randomLoops"), 0);
        this.symmetry = parseSymmetry(props.getProperty("symmetry"));
        this.linked = connectedparser.parseBoolean(props.getProperty("linked"), false);
        this.nbtName = connectedparser.parseNbtTagValue("name", props.getProperty("name"));
        this.connectBlocks = connectedparser.parseMatchBlocks(props.getProperty("connectBlocks"));
        this.connectTiles = this.parseMatchTiles(props.getProperty("connectTiles"));
        this.tintIndex = connectedparser.parseInt(props.getProperty("tintIndex"), -1);
        this.tintBlockState = connectedparser.parseBlockState(props.getProperty("tintBlock"), Blocks.air.getDefaultState());
        this.layer = connectedparser.parseBlockRenderLayer(props.getProperty("layer"), EnumWorldBlockLayer.CUTOUT_MIPPED);
    }
    
    private int[] parseCtmTileIndexes(final Properties props) {
        if (this.tiles == null) {
            return null;
        }
        final Map<Integer, Integer> map = new HashMap<Integer, Integer>();
        for (final Object object : ((Hashtable<Object, V>)props).keySet()) {
            if (object instanceof String) {
                final String s = (String)object;
                final String s2 = "ctm.";
                if (!s.startsWith(s2)) {
                    continue;
                }
                final String s3 = s.substring(s2.length());
                String s4 = props.getProperty(s);
                if (s4 == null) {
                    continue;
                }
                s4 = s4.trim();
                final int i = Config.parseInt(s3, -1);
                if (i >= 0 && i <= 46) {
                    final int j = Config.parseInt(s4, -1);
                    if (j >= 0 && j < this.tiles.length) {
                        map.put(i, j);
                    }
                    else {
                        Config.warn("Invalid CTM tile index: " + s4);
                    }
                }
                else {
                    Config.warn("Invalid CTM index: " + s3);
                }
            }
        }
        if (map.isEmpty()) {
            return null;
        }
        final int[] aint = new int[47];
        for (int k = 0; k < aint.length; ++k) {
            aint[k] = -1;
            if (map.containsKey(k)) {
                aint[k] = map.get(k);
            }
        }
        return aint;
    }
    
    private String[] parseMatchTiles(final String str) {
        if (str == null) {
            return null;
        }
        final String[] astring = Config.tokenize(str, " ");
        for (int i = 0; i < astring.length; ++i) {
            String s = astring[i];
            if (s.endsWith(".png")) {
                s = s.substring(0, s.length() - 4);
            }
            s = TextureUtils.fixResourcePath(s, this.basePath);
            astring[i] = s;
        }
        return astring;
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
    
    private String[] parseTileNames(final String str) {
        if (str == null) {
            return null;
        }
        final List list = new ArrayList();
        final String[] astring = Config.tokenize(str, " ,");
        for (int i = 0; i < astring.length; ++i) {
            final String s = astring[i];
            if (s.contains("-")) {
                final String[] astring2 = Config.tokenize(s, "-");
                if (astring2.length == 2) {
                    final int j = Config.parseInt(astring2[0], -1);
                    final int k = Config.parseInt(astring2[1], -1);
                    if (j >= 0 && k >= 0) {
                        if (j > k) {
                            Config.warn("Invalid interval: " + s + ", when parsing: " + str);
                            continue;
                        }
                        for (int l = j; l <= k; ++l) {
                            list.add(String.valueOf(l));
                        }
                        continue;
                    }
                }
            }
            list.add(s);
        }
        final String[] astring3 = list.toArray(new String[list.size()]);
        for (int i2 = 0; i2 < astring3.length; ++i2) {
            String s2 = astring3[i2];
            s2 = TextureUtils.fixResourcePath(s2, this.basePath);
            if (!s2.startsWith(this.basePath) && !s2.startsWith("textures/") && !s2.startsWith("mcpatcher/")) {
                s2 = String.valueOf(this.basePath) + "/" + s2;
            }
            if (s2.endsWith(".png")) {
                s2 = s2.substring(0, s2.length() - 4);
            }
            if (s2.startsWith("/")) {
                s2 = s2.substring(1);
            }
            astring3[i2] = s2;
        }
        return astring3;
    }
    
    private static int parseSymmetry(String str) {
        if (str == null) {
            return 1;
        }
        str = str.trim();
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
        final String[] astring = Config.tokenize(str, " ,");
        int i = 0;
        for (int j = 0; j < astring.length; ++j) {
            final String s = astring[j];
            final int k = parseFace(s);
            i |= k;
        }
        return i;
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
    
    private static int parseConnect(String str) {
        if (str == null) {
            return 0;
        }
        str = str.trim();
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
    
    public static IProperty getProperty(final String key, final Collection properties) {
        for (final Object o : properties) {
            final IProperty iproperty = (IProperty)o;
            if (key.equals(iproperty.getName())) {
                return iproperty;
            }
        }
        return null;
    }
    
    private static int parseMethod(String str) {
        if (str == null) {
            return 1;
        }
        str = str.trim();
        if (str.equals("ctm") || str.equals("glass")) {
            return 1;
        }
        if (str.equals("ctm_compact")) {
            return 10;
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
        if (str.equals("vertical+horizontal") || str.equals("v+h")) {
            return 9;
        }
        if (str.equals("overlay")) {
            return 11;
        }
        if (str.equals("overlay_fixed")) {
            return 12;
        }
        if (str.equals("overlay_random")) {
            return 13;
        }
        if (str.equals("overlay_repeat")) {
            return 14;
        }
        if (str.equals("overlay_ctm")) {
            return 15;
        }
        Config.warn("Unknown method: " + str);
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
            case 10: {
                return this.isValidCtmCompact(path);
            }
            case 11: {
                return this.isValidOverlay(path);
            }
            case 12: {
                return this.isValidOverlayFixed(path);
            }
            case 13: {
                return this.isValidOverlayRandom(path);
            }
            case 14: {
                return this.isValidOverlayRepeat(path);
            }
            case 15: {
                return this.isValidOverlayCtm(path);
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
    
    private MatchBlock[] detectMatchBlocks() {
        final int[] aint = this.detectMatchBlockIds();
        if (aint == null) {
            return null;
        }
        final MatchBlock[] amatchblock = new MatchBlock[aint.length];
        for (int i = 0; i < amatchblock.length; ++i) {
            amatchblock[i] = new MatchBlock(aint[i]);
        }
        return amatchblock;
    }
    
    private int[] detectMatchBlockIds() {
        if (!this.name.startsWith("block")) {
            return null;
        }
        int j;
        int i;
        for (i = (j = "block".length()); j < this.name.length(); ++j) {
            final char c0 = this.name.charAt(j);
            if (c0 < '0') {
                break;
            }
            if (c0 > '9') {
                break;
            }
        }
        if (j == i) {
            return null;
        }
        final String s = this.name.substring(i, j);
        final int k = Config.parseInt(s, -1);
        return (int[])((k < 0) ? null : new int[] { k });
    }
    
    private String[] detectMatchTiles() {
        final TextureAtlasSprite textureatlassprite = getIcon(this.name);
        return (String[])((textureatlassprite == null) ? null : new String[] { this.name });
    }
    
    private static TextureAtlasSprite getIcon(final String iconName) {
        final TextureMap texturemap = Minecraft.getMinecraft().getTextureMapBlocks();
        TextureAtlasSprite textureatlassprite = texturemap.getSpriteSafe(iconName);
        if (textureatlassprite != null) {
            return textureatlassprite;
        }
        textureatlassprite = texturemap.getSpriteSafe("blocks/" + iconName);
        return textureatlassprite;
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
    
    private boolean isValidCtmCompact(final String path) {
        if (this.tiles == null) {
            this.tiles = this.parseTileNames("0-4");
        }
        if (this.tiles.length < 5) {
            Config.warn("Invalid tiles, must be at least 5: " + path);
            return false;
        }
        return true;
    }
    
    private boolean isValidOverlay(final String path) {
        if (this.tiles == null) {
            this.tiles = this.parseTileNames("0-16");
        }
        if (this.tiles.length < 17) {
            Config.warn("Invalid tiles, must be at least 17: " + path);
            return false;
        }
        if (this.layer != null && this.layer != EnumWorldBlockLayer.SOLID) {
            return true;
        }
        Config.warn("Invalid overlay layer: " + this.layer);
        return false;
    }
    
    private boolean isValidOverlayFixed(final String path) {
        if (!this.isValidFixed(path)) {
            return false;
        }
        if (this.layer != null && this.layer != EnumWorldBlockLayer.SOLID) {
            return true;
        }
        Config.warn("Invalid overlay layer: " + this.layer);
        return false;
    }
    
    private boolean isValidOverlayRandom(final String path) {
        if (!this.isValidRandom(path)) {
            return false;
        }
        if (this.layer != null && this.layer != EnumWorldBlockLayer.SOLID) {
            return true;
        }
        Config.warn("Invalid overlay layer: " + this.layer);
        return false;
    }
    
    private boolean isValidOverlayRepeat(final String path) {
        if (!this.isValidRepeat(path)) {
            return false;
        }
        if (this.layer != null && this.layer != EnumWorldBlockLayer.SOLID) {
            return true;
        }
        Config.warn("Invalid overlay layer: " + this.layer);
        return false;
    }
    
    private boolean isValidOverlayCtm(final String path) {
        if (!this.isValidCtm(path)) {
            return false;
        }
        if (this.layer != null && this.layer != EnumWorldBlockLayer.SOLID) {
            return true;
        }
        Config.warn("Invalid overlay layer: " + this.layer);
        return false;
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
        if (this.tiles == null || this.tiles.length <= 0) {
            Config.warn("Tiles not defined: " + path);
            return false;
        }
        if (this.weights != null) {
            if (this.weights.length > this.tiles.length) {
                Config.warn("More weights defined than tiles, trimming weights: " + path);
                final int[] aint = new int[this.tiles.length];
                System.arraycopy(this.weights, 0, aint, 0, aint.length);
                this.weights = aint;
            }
            if (this.weights.length < this.tiles.length) {
                Config.warn("Less weights defined than tiles, expanding weights: " + path);
                final int[] aint2 = new int[this.tiles.length];
                System.arraycopy(this.weights, 0, aint2, 0, this.weights.length);
                final int i = MathUtils.getAverage(this.weights);
                for (int j = this.weights.length; j < aint2.length; ++j) {
                    aint2[j] = i;
                }
                this.weights = aint2;
            }
            this.sumWeights = new int[this.weights.length];
            int k = 0;
            for (int l = 0; l < this.weights.length; ++l) {
                k += this.weights[l];
                this.sumWeights[l] = k;
            }
            this.sumAllWeights = k;
            if (this.sumAllWeights <= 0) {
                Config.warn("Invalid sum of all weights: " + k);
                this.sumAllWeights = 1;
            }
        }
        if (this.randomLoops >= 0 && this.randomLoops <= 9) {
            return true;
        }
        Config.warn("Invalid randomLoops: " + this.randomLoops);
        return false;
    }
    
    private boolean isValidRepeat(final String path) {
        if (this.tiles == null) {
            Config.warn("Tiles not defined: " + path);
            return false;
        }
        if (this.width <= 0) {
            Config.warn("Invalid width: " + path);
            return false;
        }
        if (this.height <= 0) {
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
            this.matchTileIcons = registerIcons(this.matchTiles, textureMap, false, false);
        }
        if (this.connectTiles != null) {
            this.connectTileIcons = registerIcons(this.connectTiles, textureMap, false, false);
        }
        if (this.tiles != null) {
            this.tileIcons = registerIcons(this.tiles, textureMap, true, !isMethodOverlay(this.method));
        }
    }
    
    private static boolean isMethodOverlay(final int method) {
        switch (method) {
            case 11:
            case 12:
            case 13:
            case 14:
            case 15: {
                return true;
            }
            default: {
                return false;
            }
        }
    }
    
    private static TextureAtlasSprite[] registerIcons(final String[] tileNames, final TextureMap textureMap, final boolean skipTiles, final boolean defaultTiles) {
        if (tileNames == null) {
            return null;
        }
        final List list = new ArrayList();
        for (int i = 0; i < tileNames.length; ++i) {
            final String s = tileNames[i];
            final ResourceLocation resourcelocation = new ResourceLocation(s);
            final String s2 = resourcelocation.getResourceDomain();
            String s3 = resourcelocation.getResourcePath();
            if (!s3.contains("/")) {
                s3 = "textures/blocks/" + s3;
            }
            final String s4 = String.valueOf(s3) + ".png";
            if (skipTiles && s4.endsWith("<skip>.png")) {
                list.add(null);
            }
            else if (defaultTiles && s4.endsWith("<default>.png")) {
                list.add(ConnectedTextures.SPRITE_DEFAULT);
            }
            else {
                final ResourceLocation resourcelocation2 = new ResourceLocation(s2, s4);
                final boolean flag = Config.hasResource(resourcelocation2);
                if (!flag) {
                    Config.warn("File not found: " + s4);
                }
                final String s5 = "textures/";
                String s6 = s3;
                if (s3.startsWith(s5)) {
                    s6 = s3.substring(s5.length());
                }
                final ResourceLocation resourcelocation3 = new ResourceLocation(s2, s6);
                final TextureAtlasSprite textureatlassprite = textureMap.registerSprite(resourcelocation3);
                list.add(textureatlassprite);
            }
        }
        final TextureAtlasSprite[] atextureatlassprite = list.toArray(new TextureAtlasSprite[list.size()]);
        return atextureatlassprite;
    }
    
    public boolean matchesBlockId(final int blockId) {
        return Matches.blockId(blockId, this.matchBlocks);
    }
    
    public boolean matchesBlock(final int blockId, final int metadata) {
        return Matches.block(blockId, metadata, this.matchBlocks) && Matches.metadata(metadata, this.metadatas);
    }
    
    public boolean matchesIcon(final TextureAtlasSprite icon) {
        return Matches.sprite(icon, this.matchTileIcons);
    }
    
    @Override
    public String toString() {
        return "CTM name: " + this.name + ", basePath: " + this.basePath + ", matchBlocks: " + Config.arrayToString(this.matchBlocks) + ", matchTiles: " + Config.arrayToString(this.matchTiles);
    }
    
    public boolean matchesBiome(final BiomeGenBase biome) {
        return Matches.biome(biome, this.biomes);
    }
    
    public int getMetadataMax() {
        int i = -1;
        i = this.getMax(this.metadatas, i);
        if (this.matchBlocks != null) {
            for (int j = 0; j < this.matchBlocks.length; ++j) {
                final MatchBlock matchblock = this.matchBlocks[j];
                i = this.getMax(matchblock.getMetadatas(), i);
            }
        }
        return i;
    }
    
    private int getMax(final int[] mds, int max) {
        if (mds == null) {
            return max;
        }
        for (int i = 0; i < mds.length; ++i) {
            final int j = mds[i];
            if (j > max) {
                max = j;
            }
        }
        return max;
    }
}
