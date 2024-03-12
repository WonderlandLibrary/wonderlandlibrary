// 
// Decompiled by Procyon v0.5.36
// 

package net.optifine;

import java.util.Set;
import java.util.HashSet;
import net.minecraft.util.MathHelper;
import net.minecraft.world.IBlockAccess;
import net.minecraft.block.state.IBlockState;
import net.optifine.config.Matches;
import net.minecraft.block.state.BlockStateBase;
import net.optifine.util.TextureUtils;
import java.awt.image.BufferedImage;
import java.io.InputStream;
import java.io.IOException;
import net.minecraft.client.renderer.texture.TextureUtil;
import net.minecraft.util.ResourceLocation;
import java.util.regex.Matcher;
import net.minecraft.src.Config;
import java.util.regex.Pattern;
import net.minecraft.block.Block;
import net.minecraft.util.BlockPos;
import net.minecraft.world.biome.BiomeGenBase;
import net.optifine.config.ConnectedParser;
import java.util.Properties;
import net.optifine.config.MatchBlock;

public class CustomColormap implements CustomColors.IColorizer
{
    public String name;
    public String basePath;
    private int format;
    private MatchBlock[] matchBlocks;
    private String source;
    private int color;
    private int yVariance;
    private int yOffset;
    private int width;
    private int height;
    private int[] colors;
    private float[][] colorsRgb;
    private static final int FORMAT_UNKNOWN = -1;
    private static final int FORMAT_VANILLA = 0;
    private static final int FORMAT_GRID = 1;
    private static final int FORMAT_FIXED = 2;
    public static final String FORMAT_VANILLA_STRING = "vanilla";
    public static final String FORMAT_GRID_STRING = "grid";
    public static final String FORMAT_FIXED_STRING = "fixed";
    public static final String[] FORMAT_STRINGS;
    public static final String KEY_FORMAT = "format";
    public static final String KEY_BLOCKS = "blocks";
    public static final String KEY_SOURCE = "source";
    public static final String KEY_COLOR = "color";
    public static final String KEY_Y_VARIANCE = "yVariance";
    public static final String KEY_Y_OFFSET = "yOffset";
    
    public CustomColormap(final Properties props, final String path, final int width, final int height, final String formatDefault) {
        this.name = null;
        this.basePath = null;
        this.format = -1;
        this.matchBlocks = null;
        this.source = null;
        this.color = -1;
        this.yVariance = 0;
        this.yOffset = 0;
        this.width = 0;
        this.height = 0;
        this.colors = null;
        this.colorsRgb = null;
        final ConnectedParser connectedparser = new ConnectedParser("Colormap");
        this.name = connectedparser.parseName(path);
        this.basePath = connectedparser.parseBasePath(path);
        this.format = this.parseFormat(props.getProperty("format", formatDefault));
        this.matchBlocks = connectedparser.parseMatchBlocks(props.getProperty("blocks"));
        this.source = parseTexture(props.getProperty("source"), path, this.basePath);
        this.color = ConnectedParser.parseColor(props.getProperty("color"), -1);
        this.yVariance = connectedparser.parseInt(props.getProperty("yVariance"), 0);
        this.yOffset = connectedparser.parseInt(props.getProperty("yOffset"), 0);
        this.width = width;
        this.height = height;
    }
    
    private int parseFormat(String str) {
        if (str == null) {
            return 0;
        }
        str = str.trim();
        if (str.equals("vanilla")) {
            return 0;
        }
        if (str.equals("grid")) {
            return 1;
        }
        if (str.equals("fixed")) {
            return 2;
        }
        warn("Unknown format: " + str);
        return -1;
    }
    
    public boolean isValid(final String path) {
        if (this.format != 0 && this.format != 1) {
            if (this.format != 2) {
                return false;
            }
            if (this.color < 0) {
                this.color = 16777215;
            }
        }
        else {
            if (this.source == null) {
                warn("Source not defined: " + path);
                return false;
            }
            this.readColors();
            if (this.colors == null) {
                return false;
            }
            if (this.color < 0) {
                if (this.format == 0) {
                    this.color = this.getColor(127, 127);
                }
                if (this.format == 1) {
                    this.color = this.getColorGrid(BiomeGenBase.plains, new BlockPos(0, 64, 0));
                }
            }
        }
        return true;
    }
    
    public boolean isValidMatchBlocks(final String path) {
        if (this.matchBlocks == null) {
            this.matchBlocks = this.detectMatchBlocks();
            if (this.matchBlocks == null) {
                warn("Match blocks not defined: " + path);
                return false;
            }
        }
        return true;
    }
    
    private MatchBlock[] detectMatchBlocks() {
        final Block block = Block.getBlockFromName(this.name);
        if (block != null) {
            return new MatchBlock[] { new MatchBlock(Block.getIdFromBlock(block)) };
        }
        final Pattern pattern = Pattern.compile("^block([0-9]+).*$");
        final Matcher matcher = pattern.matcher(this.name);
        if (matcher.matches()) {
            final String s = matcher.group(1);
            final int i = Config.parseInt(s, -1);
            if (i >= 0) {
                return new MatchBlock[] { new MatchBlock(i) };
            }
        }
        final ConnectedParser connectedparser = new ConnectedParser("Colormap");
        final MatchBlock[] amatchblock = connectedparser.parseMatchBlock(this.name);
        return (MatchBlock[])((amatchblock != null) ? amatchblock : null);
    }
    
    private void readColors() {
        try {
            this.colors = null;
            if (this.source == null) {
                return;
            }
            final String s = this.source + ".png";
            final ResourceLocation resourcelocation = new ResourceLocation(s);
            final InputStream inputstream = Config.getResourceStream(resourcelocation);
            if (inputstream == null) {
                return;
            }
            final BufferedImage bufferedimage = TextureUtil.readBufferedImage(inputstream);
            if (bufferedimage == null) {
                return;
            }
            final int i = bufferedimage.getWidth();
            final int j = bufferedimage.getHeight();
            final boolean flag = this.width < 0 || this.width == i;
            final boolean flag2 = this.height < 0 || this.height == j;
            if (!flag || !flag2) {
                dbg("Non-standard palette size: " + i + "x" + j + ", should be: " + this.width + "x" + this.height + ", path: " + s);
            }
            this.width = i;
            this.height = j;
            if (this.width <= 0 || this.height <= 0) {
                warn("Invalid palette size: " + i + "x" + j + ", path: " + s);
                return;
            }
            bufferedimage.getRGB(0, 0, i, j, this.colors = new int[i * j], 0, i);
        }
        catch (IOException ioexception) {
            ioexception.printStackTrace();
        }
    }
    
    private static void dbg(final String str) {
        Config.dbg("CustomColors: " + str);
    }
    
    private static void warn(final String str) {
        Config.warn("CustomColors: " + str);
    }
    
    private static String parseTexture(String texStr, final String path, final String basePath) {
        if (texStr != null) {
            texStr = texStr.trim();
            final String s1 = ".png";
            if (texStr.endsWith(s1)) {
                texStr = texStr.substring(0, texStr.length() - s1.length());
            }
            texStr = fixTextureName(texStr, basePath);
            return texStr;
        }
        String s2 = path;
        final int i = path.lastIndexOf(47);
        if (i >= 0) {
            s2 = path.substring(i + 1);
        }
        final int j = s2.lastIndexOf(46);
        if (j >= 0) {
            s2 = s2.substring(0, j);
        }
        s2 = fixTextureName(s2, basePath);
        return s2;
    }
    
    private static String fixTextureName(String iconName, final String basePath) {
        iconName = TextureUtils.fixResourcePath(iconName, basePath);
        if (!iconName.startsWith(basePath) && !iconName.startsWith("textures/") && !iconName.startsWith("mcpatcher/")) {
            iconName = basePath + "/" + iconName;
        }
        if (iconName.endsWith(".png")) {
            iconName = iconName.substring(0, iconName.length() - 4);
        }
        final String s = "textures/blocks/";
        if (iconName.startsWith(s)) {
            iconName = iconName.substring(s.length());
        }
        if (iconName.startsWith("/")) {
            iconName = iconName.substring(1);
        }
        return iconName;
    }
    
    public boolean matchesBlock(final BlockStateBase blockState) {
        return Matches.block(blockState, this.matchBlocks);
    }
    
    public int getColorRandom() {
        if (this.format == 2) {
            return this.color;
        }
        final int i = CustomColors.random.nextInt(this.colors.length);
        return this.colors[i];
    }
    
    public int getColor(int index) {
        index = Config.limit(index, 0, this.colors.length - 1);
        return this.colors[index] & 0xFFFFFF;
    }
    
    public int getColor(int cx, int cy) {
        cx = Config.limit(cx, 0, this.width - 1);
        cy = Config.limit(cy, 0, this.height - 1);
        return this.colors[cy * this.width + cx] & 0xFFFFFF;
    }
    
    public float[][] getColorsRgb() {
        if (this.colorsRgb == null) {
            this.colorsRgb = toRgb(this.colors);
        }
        return this.colorsRgb;
    }
    
    @Override
    public int getColor(final IBlockState blockState, final IBlockAccess blockAccess, final BlockPos blockPos) {
        return this.getColor(blockAccess, blockPos);
    }
    
    public int getColor(final IBlockAccess blockAccess, final BlockPos blockPos) {
        final BiomeGenBase biomegenbase = CustomColors.getColorBiome(blockAccess, blockPos);
        return this.getColor(biomegenbase, blockPos);
    }
    
    @Override
    public boolean isColorConstant() {
        return this.format == 2;
    }
    
    public int getColor(final BiomeGenBase biome, final BlockPos blockPos) {
        return (this.format == 0) ? this.getColorVanilla(biome, blockPos) : ((this.format == 1) ? this.getColorGrid(biome, blockPos) : this.color);
    }
    
    public int getColorSmooth(final IBlockAccess blockAccess, final double x, final double y, final double z, final int radius) {
        if (this.format == 2) {
            return this.color;
        }
        final int i = MathHelper.floor_double(x);
        final int j = MathHelper.floor_double(y);
        final int k = MathHelper.floor_double(z);
        int l = 0;
        int i2 = 0;
        int j2 = 0;
        int k2 = 0;
        final BlockPosM blockposm = new BlockPosM(0, 0, 0);
        for (int l2 = i - radius; l2 <= i + radius; ++l2) {
            for (int i3 = k - radius; i3 <= k + radius; ++i3) {
                blockposm.setXyz(l2, j, i3);
                final int j3 = this.getColor(blockAccess, blockposm);
                l += (j3 >> 16 & 0xFF);
                i2 += (j3 >> 8 & 0xFF);
                j2 += (j3 & 0xFF);
                ++k2;
            }
        }
        final int k3 = l / k2;
        final int l3 = i2 / k2;
        final int i4 = j2 / k2;
        return k3 << 16 | l3 << 8 | i4;
    }
    
    private int getColorVanilla(final BiomeGenBase biome, final BlockPos blockPos) {
        final double d0 = MathHelper.clamp_float(biome.getFloatTemperature(blockPos), 0.0f, 1.0f);
        double d2 = MathHelper.clamp_float(biome.getFloatRainfall(), 0.0f, 1.0f);
        d2 *= d0;
        final int i = (int)((1.0 - d0) * (this.width - 1));
        final int j = (int)((1.0 - d2) * (this.height - 1));
        return this.getColor(i, j);
    }
    
    private int getColorGrid(final BiomeGenBase biome, final BlockPos blockPos) {
        final int i = biome.biomeID;
        int j = blockPos.getY() - this.yOffset;
        if (this.yVariance > 0) {
            final int k = blockPos.getX() << 16 + blockPos.getZ();
            final int l = Config.intHash(k);
            final int i2 = this.yVariance * 2 + 1;
            final int j2 = (l & 0xFF) % i2 - this.yVariance;
            j += j2;
        }
        return this.getColor(i, j);
    }
    
    public int getLength() {
        return (this.format == 2) ? 1 : this.colors.length;
    }
    
    public int getWidth() {
        return this.width;
    }
    
    public int getHeight() {
        return this.height;
    }
    
    private static float[][] toRgb(final int[] cols) {
        final float[][] afloat = new float[cols.length][3];
        for (int i = 0; i < cols.length; ++i) {
            final int j = cols[i];
            final float f = (j >> 16 & 0xFF) / 255.0f;
            final float f2 = (j >> 8 & 0xFF) / 255.0f;
            final float f3 = (j & 0xFF) / 255.0f;
            final float[] afloat2 = afloat[i];
            afloat2[0] = f;
            afloat2[1] = f2;
            afloat2[2] = f3;
        }
        return afloat;
    }
    
    public void addMatchBlock(final MatchBlock mb) {
        if (this.matchBlocks == null) {
            this.matchBlocks = new MatchBlock[0];
        }
        this.matchBlocks = (MatchBlock[])Config.addObjectToArray(this.matchBlocks, mb);
    }
    
    public void addMatchBlock(final int blockId, final int metadata) {
        final MatchBlock matchblock = this.getMatchBlock(blockId);
        if (matchblock != null) {
            if (metadata >= 0) {
                matchblock.addMetadata(metadata);
            }
        }
        else {
            this.addMatchBlock(new MatchBlock(blockId, metadata));
        }
    }
    
    private MatchBlock getMatchBlock(final int blockId) {
        if (this.matchBlocks == null) {
            return null;
        }
        for (int i = 0; i < this.matchBlocks.length; ++i) {
            final MatchBlock matchblock = this.matchBlocks[i];
            if (matchblock.getBlockId() == blockId) {
                return matchblock;
            }
        }
        return null;
    }
    
    public int[] getMatchBlockIds() {
        if (this.matchBlocks == null) {
            return null;
        }
        final Set set = new HashSet();
        for (int i = 0; i < this.matchBlocks.length; ++i) {
            final MatchBlock matchblock = this.matchBlocks[i];
            if (matchblock.getBlockId() >= 0) {
                set.add(matchblock.getBlockId());
            }
        }
        final Integer[] ainteger = set.toArray(new Integer[set.size()]);
        final int[] aint = new int[ainteger.length];
        for (int j = 0; j < ainteger.length; ++j) {
            aint[j] = ainteger[j];
        }
        return aint;
    }
    
    @Override
    public String toString() {
        return "" + this.basePath + "/" + this.name + ", blocks: " + Config.arrayToString(this.matchBlocks) + ", source: " + this.source;
    }
    
    static {
        FORMAT_STRINGS = new String[] { "vanilla", "grid", "fixed" };
    }
}
