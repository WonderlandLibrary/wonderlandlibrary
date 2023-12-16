// 
// Decompiled by Procyon v0.5.36
// 

package optifine;

import java.util.HashSet;
import net.minecraft.util.MathHelper;
import net.minecraft.world.IBlockAccess;
import net.minecraft.block.state.BlockStateBase;
import java.awt.image.BufferedImage;
import java.io.InputStream;
import java.io.IOException;
import net.minecraft.client.renderer.texture.TextureUtil;
import net.minecraft.util.ResourceLocation;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import net.minecraft.block.Block;
import net.minecraft.util.BlockPos;
import net.minecraft.world.biome.BiomeGenBase;
import java.util.Properties;

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
    public static final String KEY_FORMAT = "format";
    public static final String KEY_BLOCKS = "blocks";
    public static final String KEY_SOURCE = "source";
    public static final String KEY_COLOR = "color";
    public static final String KEY_Y_VARIANCE = "yVariance";
    public static final String KEY_Y_OFFSET = "yOffset";
    
    public CustomColormap(final Properties props, final String path, final int width, final int height) {
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
        final ConnectedParser cp = new ConnectedParser("Colormap");
        this.name = cp.parseName(path);
        this.basePath = cp.parseBasePath(path);
        this.format = this.parseFormat(props.getProperty("format"));
        this.matchBlocks = cp.parseMatchBlocks(props.getProperty("blocks"));
        this.source = parseTexture(props.getProperty("source"), path, this.basePath);
        this.color = ConnectedParser.parseColor(props.getProperty("color"), -1);
        this.yVariance = cp.parseInt(props.getProperty("yVariance"), 0);
        this.yOffset = cp.parseInt(props.getProperty("yOffset"), 0);
        this.width = width;
        this.height = height;
    }
    
    private int parseFormat(final String str) {
        if (str == null) {
            return 0;
        }
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
        final Pattern p = Pattern.compile("^block([0-9]+).*$");
        final Matcher m = p.matcher(this.name);
        if (m.matches()) {
            final String cp = m.group(1);
            final int mbs = Config.parseInt(cp, -1);
            if (mbs >= 0) {
                return new MatchBlock[] { new MatchBlock(mbs) };
            }
        }
        final ConnectedParser cp2 = new ConnectedParser("Colormap");
        final MatchBlock[] mbs2 = cp2.parseMatchBlock(this.name);
        return (MatchBlock[])((mbs2 != null) ? mbs2 : null);
    }
    
    private void readColors() {
        try {
            this.colors = null;
            if (this.source == null) {
                return;
            }
            final String e = String.valueOf(this.source) + ".png";
            final ResourceLocation loc = new ResourceLocation(e);
            final InputStream is = Config.getResourceStream(loc);
            if (is == null) {
                return;
            }
            final BufferedImage img = TextureUtil.func_177053_a(is);
            if (img == null) {
                return;
            }
            final int imgWidth = img.getWidth();
            final int imgHeight = img.getHeight();
            final boolean widthOk = this.width < 0 || this.width == imgWidth;
            final boolean heightOk = this.height < 0 || this.height == imgHeight;
            if (!widthOk || !heightOk) {
                dbg("Non-standard palette size: " + imgWidth + "x" + imgHeight + ", should be: " + this.width + "x" + this.height + ", path: " + e);
            }
            this.width = imgWidth;
            this.height = imgHeight;
            if (this.width <= 0 || this.height <= 0) {
                warn("Invalid palette size: " + imgWidth + "x" + imgHeight + ", path: " + e);
                return;
            }
            img.getRGB(0, 0, imgWidth, imgHeight, this.colors = new int[imgWidth * imgHeight], 0, imgWidth);
        }
        catch (IOException var9) {
            var9.printStackTrace();
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
            final String str = ".png";
            if (texStr.endsWith(str)) {
                texStr = texStr.substring(0, texStr.length() - str.length());
            }
            texStr = fixTextureName(texStr, basePath);
            return texStr;
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
    
    public boolean matchesBlock(final BlockStateBase blockState) {
        return Matches.block(blockState, this.matchBlocks);
    }
    
    public int getColorRandom() {
        if (this.format == 2) {
            return this.color;
        }
        final int index = CustomColors.random.nextInt(this.colors.length);
        return this.colors[index];
    }
    
    public int getColor(int index) {
        index = Config.limit(index, 0, this.colors.length);
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
    public int getColor(final IBlockAccess blockAccess, final BlockPos blockPos) {
        final BiomeGenBase biome = CustomColors.getColorBiome(blockAccess, blockPos);
        return this.getColor(biome, blockPos);
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
        final int x2 = MathHelper.floor_double(x);
        final int y2 = MathHelper.floor_double(y);
        final int z2 = MathHelper.floor_double(z);
        int sumRed = 0;
        int sumGreen = 0;
        int sumBlue = 0;
        int count = 0;
        final BlockPosM blockPosM = new BlockPosM(0, 0, 0);
        for (int r = x2 - radius; r <= x2 + radius; ++r) {
            for (int g = z2 - radius; g <= z2 + radius; ++g) {
                blockPosM.setXyz(r, y2, g);
                final int b = this.getColor(blockAccess, blockPosM);
                sumRed += (b >> 16 & 0xFF);
                sumGreen += (b >> 8 & 0xFF);
                sumBlue += (b & 0xFF);
                ++count;
            }
        }
        int r = sumRed / count;
        int g = sumGreen / count;
        final int b = sumBlue / count;
        return r << 16 | g << 8 | b;
    }
    
    private int getColorVanilla(final BiomeGenBase biome, final BlockPos blockPos) {
        final double temperature = MathHelper.clamp_float(biome.func_180626_a(blockPos), 0.0f, 1.0f);
        double rainfall = MathHelper.clamp_float(biome.getFloatRainfall(), 0.0f, 1.0f);
        rainfall *= temperature;
        final int cx = (int)((1.0 - temperature) * (this.width - 1));
        final int cy = (int)((1.0 - rainfall) * (this.height - 1));
        return this.getColor(cx, cy);
    }
    
    private int getColorGrid(final BiomeGenBase biome, final BlockPos blockPos) {
        final int cx = biome.biomeID;
        int cy = blockPos.getY() - this.yOffset;
        if (this.yVariance > 0) {
            final int seed = blockPos.getX() << 16 + blockPos.getZ();
            final int rand = Config.intHash(seed);
            final int range = this.yVariance * 2 + 1;
            final int diff = (rand & 0xFF) % range - this.yVariance;
            cy += diff;
        }
        return this.getColor(cx, cy);
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
        final float[][] colsRgb = new float[cols.length][3];
        for (int i = 0; i < cols.length; ++i) {
            final int col = cols[i];
            final float rf = (col >> 16 & 0xFF) / 255.0f;
            final float gf = (col >> 8 & 0xFF) / 255.0f;
            final float bf = (col & 0xFF) / 255.0f;
            final float[] colRgb = colsRgb[i];
            colRgb[0] = rf;
            colRgb[1] = gf;
            colRgb[2] = bf;
        }
        return colsRgb;
    }
    
    public void addMatchBlock(final MatchBlock mb) {
        if (this.matchBlocks == null) {
            this.matchBlocks = new MatchBlock[0];
        }
        this.matchBlocks = (MatchBlock[])Config.addObjectToArray(this.matchBlocks, mb);
    }
    
    public void addMatchBlock(final int blockId, final int metadata) {
        final MatchBlock mb = this.getMatchBlock(blockId);
        if (mb != null) {
            if (metadata >= 0) {
                mb.addMetadata(metadata);
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
            final MatchBlock mb = this.matchBlocks[i];
            if (mb.getBlockId() == blockId) {
                return mb;
            }
        }
        return null;
    }
    
    public int[] getMatchBlockIds() {
        if (this.matchBlocks == null) {
            return null;
        }
        final HashSet setIds = new HashSet();
        for (int ints = 0; ints < this.matchBlocks.length; ++ints) {
            final MatchBlock ids = this.matchBlocks[ints];
            if (ids.getBlockId() >= 0) {
                setIds.add(ids.getBlockId());
            }
        }
        final Integer[] var5 = (Integer[])setIds.toArray(new Integer[setIds.size()]);
        final int[] var6 = new int[var5.length];
        for (int i = 0; i < var5.length; ++i) {
            var6[i] = var5[i];
        }
        return var6;
    }
    
    @Override
    public String toString() {
        return this.basePath + "/" + this.name + ", blocks: " + Config.arrayToString(this.matchBlocks) + ", source: " + this.source;
    }
}
