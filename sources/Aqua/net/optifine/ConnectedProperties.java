package net.optifine;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Properties;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.init.Blocks;
import net.minecraft.src.Config;
import net.minecraft.util.EnumWorldBlockLayer;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.biome.BiomeGenBase;
import net.optifine.ConnectedTextures;
import net.optifine.config.ConnectedParser;
import net.optifine.config.MatchBlock;
import net.optifine.config.Matches;
import net.optifine.config.NbtTagValue;
import net.optifine.config.RangeInt;
import net.optifine.config.RangeListInt;
import net.optifine.util.MathUtils;
import net.optifine.util.TextureUtils;

public class ConnectedProperties {
    public String name = null;
    public String basePath = null;
    public MatchBlock[] matchBlocks = null;
    public int[] metadatas = null;
    public String[] matchTiles = null;
    public int method = 0;
    public String[] tiles = null;
    public int connect = 0;
    public int faces = 63;
    public BiomeGenBase[] biomes = null;
    public RangeListInt heights = null;
    public int renderPass = 0;
    public boolean innerSeams = false;
    public int[] ctmTileIndexes = null;
    public int width = 0;
    public int height = 0;
    public int[] weights = null;
    public int randomLoops = 0;
    public int symmetry = 1;
    public boolean linked = false;
    public NbtTagValue nbtName = null;
    public int[] sumWeights = null;
    public int sumAllWeights = 1;
    public TextureAtlasSprite[] matchTileIcons = null;
    public TextureAtlasSprite[] tileIcons = null;
    public MatchBlock[] connectBlocks = null;
    public String[] connectTiles = null;
    public TextureAtlasSprite[] connectTileIcons = null;
    public int tintIndex = -1;
    public IBlockState tintBlockState = Blocks.air.getDefaultState();
    public EnumWorldBlockLayer layer = null;
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

    public ConnectedProperties(Properties props, String path) {
        ConnectedParser connectedparser = new ConnectedParser("ConnectedTextures");
        this.name = connectedparser.parseName(path);
        this.basePath = connectedparser.parseBasePath(path);
        this.matchBlocks = connectedparser.parseMatchBlocks(props.getProperty("matchBlocks"));
        this.metadatas = connectedparser.parseIntList(props.getProperty("metadata"));
        this.matchTiles = this.parseMatchTiles(props.getProperty("matchTiles"));
        this.method = ConnectedProperties.parseMethod(props.getProperty("method"));
        this.tiles = this.parseTileNames(props.getProperty("tiles"));
        this.connect = ConnectedProperties.parseConnect(props.getProperty("connect"));
        this.faces = ConnectedProperties.parseFaces(props.getProperty("faces"));
        this.biomes = connectedparser.parseBiomes(props.getProperty("biomes"));
        this.heights = connectedparser.parseRangeListInt(props.getProperty("heights"));
        if (this.heights == null) {
            int i = connectedparser.parseInt(props.getProperty("minHeight"), -1);
            int j = connectedparser.parseInt(props.getProperty("maxHeight"), 1024);
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
        this.symmetry = ConnectedProperties.parseSymmetry(props.getProperty("symmetry"));
        this.linked = connectedparser.parseBoolean(props.getProperty("linked"), false);
        this.nbtName = connectedparser.parseNbtTagValue("name", props.getProperty("name"));
        this.connectBlocks = connectedparser.parseMatchBlocks(props.getProperty("connectBlocks"));
        this.connectTiles = this.parseMatchTiles(props.getProperty("connectTiles"));
        this.tintIndex = connectedparser.parseInt(props.getProperty("tintIndex"), -1);
        this.tintBlockState = connectedparser.parseBlockState(props.getProperty("tintBlock"), Blocks.air.getDefaultState());
        this.layer = connectedparser.parseBlockRenderLayer(props.getProperty("layer"), EnumWorldBlockLayer.CUTOUT_MIPPED);
    }

    private int[] parseCtmTileIndexes(Properties props) {
        if (this.tiles == null) {
            return null;
        }
        HashMap map = new HashMap();
        for (Object object : props.keySet()) {
            String s1;
            String s;
            if (!(object instanceof String) || !(s = (String)object).startsWith(s1 = "ctm.")) continue;
            String s2 = s.substring(s1.length());
            String s3 = props.getProperty(s);
            if (s3 == null) continue;
            s3 = s3.trim();
            int i = Config.parseInt((String)s2, (int)-1);
            if (i >= 0 && i <= 46) {
                int j = Config.parseInt((String)s3, (int)-1);
                if (j >= 0 && j < this.tiles.length) {
                    map.put((Object)i, (Object)j);
                    continue;
                }
                Config.warn((String)("Invalid CTM tile index: " + s3));
                continue;
            }
            Config.warn((String)("Invalid CTM index: " + s2));
        }
        if (map.isEmpty()) {
            return null;
        }
        int[] aint = new int[47];
        for (int k = 0; k < aint.length; ++k) {
            aint[k] = -1;
            if (!map.containsKey((Object)k)) continue;
            aint[k] = (Integer)map.get((Object)k);
        }
        return aint;
    }

    private String[] parseMatchTiles(String str) {
        if (str == null) {
            return null;
        }
        String[] astring = Config.tokenize((String)str, (String)" ");
        for (int i = 0; i < astring.length; ++i) {
            String s = astring[i];
            if (s.endsWith(".png")) {
                s = s.substring(0, s.length() - 4);
            }
            astring[i] = s = TextureUtils.fixResourcePath((String)s, (String)this.basePath);
        }
        return astring;
    }

    private static String parseName(String path) {
        int j;
        String s = path;
        int i = path.lastIndexOf(47);
        if (i >= 0) {
            s = path.substring(i + 1);
        }
        if ((j = s.lastIndexOf(46)) >= 0) {
            s = s.substring(0, j);
        }
        return s;
    }

    private static String parseBasePath(String path) {
        int i = path.lastIndexOf(47);
        return i < 0 ? "" : path.substring(0, i);
    }

    private String[] parseTileNames(String str) {
        if (str == null) {
            return null;
        }
        ArrayList list = new ArrayList();
        String[] astring = Config.tokenize((String)str, (String)" ,");
        for (int i = 0; i < astring.length; ++i) {
            String[] astring1;
            String s = astring[i];
            if (s.contains((CharSequence)"-") && (astring1 = Config.tokenize((String)s, (String)"-")).length == 2) {
                int j = Config.parseInt((String)astring1[0], (int)-1);
                int k = Config.parseInt((String)astring1[1], (int)-1);
                if (j >= 0 && k >= 0) {
                    if (j > k) {
                        Config.warn((String)("Invalid interval: " + s + ", when parsing: " + str));
                        continue;
                    }
                    for (int l = j; l <= k; ++l) {
                        list.add((Object)String.valueOf((int)l));
                    }
                    continue;
                }
            }
            list.add((Object)s);
        }
        String[] astring2 = (String[])list.toArray((Object[])new String[list.size()]);
        for (int i1 = 0; i1 < astring2.length; ++i1) {
            String s1 = astring2[i1];
            if (!((s1 = TextureUtils.fixResourcePath((String)s1, (String)this.basePath)).startsWith(this.basePath) || s1.startsWith("textures/") || s1.startsWith("mcpatcher/"))) {
                s1 = this.basePath + "/" + s1;
            }
            if (s1.endsWith(".png")) {
                s1 = s1.substring(0, s1.length() - 4);
            }
            if (s1.startsWith("/")) {
                s1 = s1.substring(1);
            }
            astring2[i1] = s1;
        }
        return astring2;
    }

    private static int parseSymmetry(String str) {
        if (str == null) {
            return 1;
        }
        if ((str = str.trim()).equals((Object)"opposite")) {
            return 2;
        }
        if (str.equals((Object)"all")) {
            return 6;
        }
        Config.warn((String)("Unknown symmetry: " + str));
        return 1;
    }

    private static int parseFaces(String str) {
        if (str == null) {
            return 63;
        }
        String[] astring = Config.tokenize((String)str, (String)" ,");
        int i = 0;
        for (int j = 0; j < astring.length; ++j) {
            String s = astring[j];
            int k = ConnectedProperties.parseFace(s);
            i |= k;
        }
        return i;
    }

    private static int parseFace(String str) {
        if (!(str = str.toLowerCase()).equals((Object)"bottom") && !str.equals((Object)"down")) {
            if (!str.equals((Object)"top") && !str.equals((Object)"up")) {
                if (str.equals((Object)"north")) {
                    return 4;
                }
                if (str.equals((Object)"south")) {
                    return 8;
                }
                if (str.equals((Object)"east")) {
                    return 32;
                }
                if (str.equals((Object)"west")) {
                    return 16;
                }
                if (str.equals((Object)"sides")) {
                    return 60;
                }
                if (str.equals((Object)"all")) {
                    return 63;
                }
                Config.warn((String)("Unknown face: " + str));
                return 128;
            }
            return 2;
        }
        return 1;
    }

    private static int parseConnect(String str) {
        if (str == null) {
            return 0;
        }
        if ((str = str.trim()).equals((Object)"block")) {
            return 1;
        }
        if (str.equals((Object)"tile")) {
            return 2;
        }
        if (str.equals((Object)"material")) {
            return 3;
        }
        Config.warn((String)("Unknown connect: " + str));
        return 128;
    }

    public static IProperty getProperty(String key, Collection<IProperty> properties) {
        for (IProperty iproperty : properties) {
            if (!key.equals((Object)iproperty.getName())) continue;
            return iproperty;
        }
        return null;
    }

    private static int parseMethod(String str) {
        if (str == null) {
            return 1;
        }
        if (!(str = str.trim()).equals((Object)"ctm") && !str.equals((Object)"glass")) {
            if (str.equals((Object)"ctm_compact")) {
                return 10;
            }
            if (!str.equals((Object)"horizontal") && !str.equals((Object)"bookshelf")) {
                if (str.equals((Object)"vertical")) {
                    return 6;
                }
                if (str.equals((Object)"top")) {
                    return 3;
                }
                if (str.equals((Object)"random")) {
                    return 4;
                }
                if (str.equals((Object)"repeat")) {
                    return 5;
                }
                if (str.equals((Object)"fixed")) {
                    return 7;
                }
                if (!str.equals((Object)"horizontal+vertical") && !str.equals((Object)"h+v")) {
                    if (!str.equals((Object)"vertical+horizontal") && !str.equals((Object)"v+h")) {
                        if (str.equals((Object)"overlay")) {
                            return 11;
                        }
                        if (str.equals((Object)"overlay_fixed")) {
                            return 12;
                        }
                        if (str.equals((Object)"overlay_random")) {
                            return 13;
                        }
                        if (str.equals((Object)"overlay_repeat")) {
                            return 14;
                        }
                        if (str.equals((Object)"overlay_ctm")) {
                            return 15;
                        }
                        Config.warn((String)("Unknown method: " + str));
                        return 0;
                    }
                    return 9;
                }
                return 8;
            }
            return 2;
        }
        return 1;
    }

    public boolean isValid(String path) {
        if (this.name != null && this.name.length() > 0) {
            if (this.basePath == null) {
                Config.warn((String)("No base path found: " + path));
                return false;
            }
            if (this.matchBlocks == null) {
                this.matchBlocks = this.detectMatchBlocks();
            }
            if (this.matchTiles == null && this.matchBlocks == null) {
                this.matchTiles = this.detectMatchTiles();
            }
            if (this.matchBlocks == null && this.matchTiles == null) {
                Config.warn((String)("No matchBlocks or matchTiles specified: " + path));
                return false;
            }
            if (this.method == 0) {
                Config.warn((String)("No method: " + path));
                return false;
            }
            if (this.tiles != null && this.tiles.length > 0) {
                if (this.connect == 0) {
                    this.connect = this.detectConnect();
                }
                if (this.connect == 128) {
                    Config.warn((String)("Invalid connect in: " + path));
                    return false;
                }
                if (this.renderPass > 0) {
                    Config.warn((String)("Render pass not supported: " + this.renderPass));
                    return false;
                }
                if ((this.faces & 0x80) != 0) {
                    Config.warn((String)("Invalid faces in: " + path));
                    return false;
                }
                if ((this.symmetry & 0x80) != 0) {
                    Config.warn((String)("Invalid symmetry in: " + path));
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
                }
                Config.warn((String)("Unknown method: " + path));
                return false;
            }
            Config.warn((String)("No tiles specified: " + path));
            return false;
        }
        Config.warn((String)("No name found: " + path));
        return false;
    }

    private int detectConnect() {
        return this.matchBlocks != null ? 1 : (this.matchTiles != null ? 2 : 128);
    }

    private MatchBlock[] detectMatchBlocks() {
        int[] aint = this.detectMatchBlockIds();
        if (aint == null) {
            return null;
        }
        MatchBlock[] amatchblock = new MatchBlock[aint.length];
        for (int i = 0; i < amatchblock.length; ++i) {
            amatchblock[i] = new MatchBlock(aint[i]);
        }
        return amatchblock;
    }

    private int[] detectMatchBlockIds() {
        int[] nArray;
        int i;
        char c0;
        int j;
        if (!this.name.startsWith("block")) {
            return null;
        }
        for (j = i = "block".length(); j < this.name.length() && (c0 = this.name.charAt(j)) >= '0' && c0 <= '9'; ++j) {
        }
        if (j == i) {
            return null;
        }
        String s = this.name.substring(i, j);
        int k = Config.parseInt((String)s, (int)-1);
        if (k < 0) {
            nArray = null;
        } else {
            int[] nArray2 = new int[1];
            nArray = nArray2;
            nArray2[0] = k;
        }
        return nArray;
    }

    private String[] detectMatchTiles() {
        String[] stringArray;
        TextureAtlasSprite textureatlassprite = ConnectedProperties.getIcon(this.name);
        if (textureatlassprite == null) {
            stringArray = null;
        } else {
            String[] stringArray2 = new String[1];
            stringArray = stringArray2;
            stringArray2[0] = this.name;
        }
        return stringArray;
    }

    private static TextureAtlasSprite getIcon(String iconName) {
        TextureMap texturemap = Minecraft.getMinecraft().getTextureMapBlocks();
        TextureAtlasSprite textureatlassprite = texturemap.getSpriteSafe(iconName);
        if (textureatlassprite != null) {
            return textureatlassprite;
        }
        textureatlassprite = texturemap.getSpriteSafe("blocks/" + iconName);
        return textureatlassprite;
    }

    private boolean isValidCtm(String path) {
        if (this.tiles == null) {
            this.tiles = this.parseTileNames("0-11 16-27 32-43 48-58");
        }
        if (this.tiles.length < 47) {
            Config.warn((String)("Invalid tiles, must be at least 47: " + path));
            return false;
        }
        return true;
    }

    private boolean isValidCtmCompact(String path) {
        if (this.tiles == null) {
            this.tiles = this.parseTileNames("0-4");
        }
        if (this.tiles.length < 5) {
            Config.warn((String)("Invalid tiles, must be at least 5: " + path));
            return false;
        }
        return true;
    }

    private boolean isValidOverlay(String path) {
        if (this.tiles == null) {
            this.tiles = this.parseTileNames("0-16");
        }
        if (this.tiles.length < 17) {
            Config.warn((String)("Invalid tiles, must be at least 17: " + path));
            return false;
        }
        if (this.layer != null && this.layer != EnumWorldBlockLayer.SOLID) {
            return true;
        }
        Config.warn((String)("Invalid overlay layer: " + this.layer));
        return false;
    }

    private boolean isValidOverlayFixed(String path) {
        if (!this.isValidFixed(path)) {
            return false;
        }
        if (this.layer != null && this.layer != EnumWorldBlockLayer.SOLID) {
            return true;
        }
        Config.warn((String)("Invalid overlay layer: " + this.layer));
        return false;
    }

    private boolean isValidOverlayRandom(String path) {
        if (!this.isValidRandom(path)) {
            return false;
        }
        if (this.layer != null && this.layer != EnumWorldBlockLayer.SOLID) {
            return true;
        }
        Config.warn((String)("Invalid overlay layer: " + this.layer));
        return false;
    }

    private boolean isValidOverlayRepeat(String path) {
        if (!this.isValidRepeat(path)) {
            return false;
        }
        if (this.layer != null && this.layer != EnumWorldBlockLayer.SOLID) {
            return true;
        }
        Config.warn((String)("Invalid overlay layer: " + this.layer));
        return false;
    }

    private boolean isValidOverlayCtm(String path) {
        if (!this.isValidCtm(path)) {
            return false;
        }
        if (this.layer != null && this.layer != EnumWorldBlockLayer.SOLID) {
            return true;
        }
        Config.warn((String)("Invalid overlay layer: " + this.layer));
        return false;
    }

    private boolean isValidHorizontal(String path) {
        if (this.tiles == null) {
            this.tiles = this.parseTileNames("12-15");
        }
        if (this.tiles.length != 4) {
            Config.warn((String)("Invalid tiles, must be exactly 4: " + path));
            return false;
        }
        return true;
    }

    private boolean isValidVertical(String path) {
        if (this.tiles == null) {
            Config.warn((String)("No tiles defined for vertical: " + path));
            return false;
        }
        if (this.tiles.length != 4) {
            Config.warn((String)("Invalid tiles, must be exactly 4: " + path));
            return false;
        }
        return true;
    }

    private boolean isValidHorizontalVertical(String path) {
        if (this.tiles == null) {
            Config.warn((String)("No tiles defined for horizontal+vertical: " + path));
            return false;
        }
        if (this.tiles.length != 7) {
            Config.warn((String)("Invalid tiles, must be exactly 7: " + path));
            return false;
        }
        return true;
    }

    private boolean isValidVerticalHorizontal(String path) {
        if (this.tiles == null) {
            Config.warn((String)("No tiles defined for vertical+horizontal: " + path));
            return false;
        }
        if (this.tiles.length != 7) {
            Config.warn((String)("Invalid tiles, must be exactly 7: " + path));
            return false;
        }
        return true;
    }

    private boolean isValidRandom(String path) {
        if (this.tiles != null && this.tiles.length > 0) {
            if (this.weights != null) {
                if (this.weights.length > this.tiles.length) {
                    Config.warn((String)("More weights defined than tiles, trimming weights: " + path));
                    int[] aint = new int[this.tiles.length];
                    System.arraycopy((Object)this.weights, (int)0, (Object)aint, (int)0, (int)aint.length);
                    this.weights = aint;
                }
                if (this.weights.length < this.tiles.length) {
                    Config.warn((String)("Less weights defined than tiles, expanding weights: " + path));
                    int[] aint1 = new int[this.tiles.length];
                    System.arraycopy((Object)this.weights, (int)0, (Object)aint1, (int)0, (int)this.weights.length);
                    int i = MathUtils.getAverage((int[])this.weights);
                    for (int j = this.weights.length; j < aint1.length; ++j) {
                        aint1[j] = i;
                    }
                    this.weights = aint1;
                }
                this.sumWeights = new int[this.weights.length];
                int k = 0;
                for (int l = 0; l < this.weights.length; ++l) {
                    this.sumWeights[l] = k += this.weights[l];
                }
                this.sumAllWeights = k;
                if (this.sumAllWeights <= 0) {
                    Config.warn((String)("Invalid sum of all weights: " + k));
                    this.sumAllWeights = 1;
                }
            }
            if (this.randomLoops >= 0 && this.randomLoops <= 9) {
                return true;
            }
            Config.warn((String)("Invalid randomLoops: " + this.randomLoops));
            return false;
        }
        Config.warn((String)("Tiles not defined: " + path));
        return false;
    }

    private boolean isValidRepeat(String path) {
        if (this.tiles == null) {
            Config.warn((String)("Tiles not defined: " + path));
            return false;
        }
        if (this.width <= 0) {
            Config.warn((String)("Invalid width: " + path));
            return false;
        }
        if (this.height <= 0) {
            Config.warn((String)("Invalid height: " + path));
            return false;
        }
        if (this.tiles.length != this.width * this.height) {
            Config.warn((String)("Number of tiles does not equal width x height: " + path));
            return false;
        }
        return true;
    }

    private boolean isValidFixed(String path) {
        if (this.tiles == null) {
            Config.warn((String)("Tiles not defined: " + path));
            return false;
        }
        if (this.tiles.length != 1) {
            Config.warn((String)"Number of tiles should be 1 for method: fixed.");
            return false;
        }
        return true;
    }

    private boolean isValidTop(String path) {
        if (this.tiles == null) {
            this.tiles = this.parseTileNames("66");
        }
        if (this.tiles.length != 1) {
            Config.warn((String)("Invalid tiles, must be exactly 1: " + path));
            return false;
        }
        return true;
    }

    public void updateIcons(TextureMap textureMap) {
        if (this.matchTiles != null) {
            this.matchTileIcons = ConnectedProperties.registerIcons(this.matchTiles, textureMap, false, false);
        }
        if (this.connectTiles != null) {
            this.connectTileIcons = ConnectedProperties.registerIcons(this.connectTiles, textureMap, false, false);
        }
        if (this.tiles != null) {
            this.tileIcons = ConnectedProperties.registerIcons(this.tiles, textureMap, true, !ConnectedProperties.isMethodOverlay(this.method));
        }
    }

    private static boolean isMethodOverlay(int method) {
        switch (method) {
            case 11: 
            case 12: 
            case 13: 
            case 14: 
            case 15: {
                return true;
            }
        }
        return false;
    }

    private static TextureAtlasSprite[] registerIcons(String[] tileNames, TextureMap textureMap, boolean skipTiles, boolean defaultTiles) {
        if (tileNames == null) {
            return null;
        }
        ArrayList list = new ArrayList();
        for (int i = 0; i < tileNames.length; ++i) {
            String s = tileNames[i];
            ResourceLocation resourcelocation = new ResourceLocation(s);
            String s1 = resourcelocation.getResourceDomain();
            String s2 = resourcelocation.getResourcePath();
            if (!s2.contains((CharSequence)"/")) {
                s2 = "textures/blocks/" + s2;
            }
            String s3 = s2 + ".png";
            if (skipTiles && s3.endsWith(TILE_SKIP_PNG)) {
                list.add(null);
                continue;
            }
            if (defaultTiles && s3.endsWith(TILE_DEFAULT_PNG)) {
                list.add((Object)ConnectedTextures.SPRITE_DEFAULT);
                continue;
            }
            ResourceLocation resourcelocation1 = new ResourceLocation(s1, s3);
            boolean flag = Config.hasResource((ResourceLocation)resourcelocation1);
            if (!flag) {
                Config.warn((String)("File not found: " + s3));
            }
            String s4 = "textures/";
            String s5 = s2;
            if (s2.startsWith(s4)) {
                s5 = s2.substring(s4.length());
            }
            ResourceLocation resourcelocation2 = new ResourceLocation(s1, s5);
            TextureAtlasSprite textureatlassprite = textureMap.registerSprite(resourcelocation2);
            list.add((Object)textureatlassprite);
        }
        TextureAtlasSprite[] atextureatlassprite = (TextureAtlasSprite[])list.toArray((Object[])new TextureAtlasSprite[list.size()]);
        return atextureatlassprite;
    }

    public boolean matchesBlockId(int blockId) {
        return Matches.blockId((int)blockId, (MatchBlock[])this.matchBlocks);
    }

    public boolean matchesBlock(int blockId, int metadata) {
        return !Matches.block((int)blockId, (int)metadata, (MatchBlock[])this.matchBlocks) ? false : Matches.metadata((int)metadata, (int[])this.metadatas);
    }

    public boolean matchesIcon(TextureAtlasSprite icon) {
        return Matches.sprite((TextureAtlasSprite)icon, (TextureAtlasSprite[])this.matchTileIcons);
    }

    public String toString() {
        return "CTM name: " + this.name + ", basePath: " + this.basePath + ", matchBlocks: " + Config.arrayToString((Object[])this.matchBlocks) + ", matchTiles: " + Config.arrayToString((Object[])this.matchTiles);
    }

    public boolean matchesBiome(BiomeGenBase biome) {
        return Matches.biome((BiomeGenBase)biome, (BiomeGenBase[])this.biomes);
    }

    public int getMetadataMax() {
        int i = -1;
        i = this.getMax(this.metadatas, i);
        if (this.matchBlocks != null) {
            for (int j = 0; j < this.matchBlocks.length; ++j) {
                MatchBlock matchblock = this.matchBlocks[j];
                i = this.getMax(matchblock.getMetadatas(), i);
            }
        }
        return i;
    }

    private int getMax(int[] mds, int max) {
        if (mds == null) {
            return max;
        }
        for (int i = 0; i < mds.length; ++i) {
            int j = mds[i];
            if (j <= max) continue;
            max = j;
        }
        return max;
    }
}
