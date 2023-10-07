// 
// Decompiled by Procyon v0.5.30
// 

package net.minecraft.optifine;

import net.minecraft.client.Minecraft;
import net.minecraft.entity.Entity;
import net.minecraft.client.multiplayer.WorldClient;
import net.minecraft.world.World;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.BlockRedstoneWire;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.particle.EntityFX;
import net.minecraft.util.MathHelper;
import net.minecraft.block.material.Material;
import net.minecraft.world.ColorizerFoliage;
import net.minecraft.world.biome.BiomeGenBase;
import net.minecraft.block.BlockStem;
import net.minecraft.util.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.client.renderer.block.model.BakedQuad;
import net.minecraft.client.renderer.texture.TextureUtil;
import java.util.Iterator;
import java.util.Set;
import java.util.Arrays;
import net.minecraft.block.Block;
import net.minecraft.init.Blocks;
import java.util.HashMap;
import java.io.FileNotFoundException;
import java.util.Properties;
import java.awt.image.BufferedImage;
import java.io.InputStream;
import java.io.IOException;
import javax.imageio.ImageIO;
import net.minecraft.util.ResourceLocation;
import java.util.Random;
import net.minecraft.util.Vec3;

public class CustomColorizer
{
    private static int[] grassColors;
    private static int[] waterColors;
    private static int[] foliageColors;
    private static int[] foliagePineColors;
    private static int[] foliageBirchColors;
    private static int[] swampFoliageColors;
    private static int[] swampGrassColors;
    private static int[][] blockPalettes;
    private static int[][] paletteColors;
    private static int[] skyColors;
    private static int[] fogColors;
    private static int[] underwaterColors;
    private static float[][][] lightMapsColorsRgb;
    private static int[] lightMapsHeight;
    private static float[][] sunRgbs;
    private static float[][] torchRgbs;
    private static int[] redstoneColors;
    private static int[] stemColors;
    private static int[] myceliumParticleColors;
    private static boolean useDefaultColorMultiplier;
    private static int particleWaterColor;
    private static int particlePortalColor;
    private static int lilyPadColor;
    private static Vec3 fogColorNether;
    private static Vec3 fogColorEnd;
    private static Vec3 skyColorEnd;
    private static final int TYPE_NONE = 0;
    private static final int TYPE_GRASS = 1;
    private static final int TYPE_FOLIAGE = 2;
    private static Random random;
    
    public static void update() {
        CustomColorizer.grassColors = null;
        CustomColorizer.waterColors = null;
        CustomColorizer.foliageColors = null;
        CustomColorizer.foliageBirchColors = null;
        CustomColorizer.foliagePineColors = null;
        CustomColorizer.swampGrassColors = null;
        CustomColorizer.swampFoliageColors = null;
        CustomColorizer.skyColors = null;
        CustomColorizer.fogColors = null;
        CustomColorizer.underwaterColors = null;
        CustomColorizer.redstoneColors = null;
        CustomColorizer.stemColors = null;
        CustomColorizer.myceliumParticleColors = null;
        CustomColorizer.lightMapsColorsRgb = null;
        CustomColorizer.lightMapsHeight = null;
        CustomColorizer.lilyPadColor = -1;
        CustomColorizer.particleWaterColor = -1;
        CustomColorizer.particlePortalColor = -1;
        CustomColorizer.fogColorNether = null;
        CustomColorizer.fogColorEnd = null;
        CustomColorizer.skyColorEnd = null;
        CustomColorizer.blockPalettes = null;
        CustomColorizer.paletteColors = null;
        CustomColorizer.useDefaultColorMultiplier = true;
        final String mcpColormap = "mcpatcher/colormap/";
        CustomColorizer.grassColors = getCustomColors("textures/colormap/grass.png", 65536);
        CustomColorizer.foliageColors = getCustomColors("textures/colormap/foliage.png", 65536);
        final String[] waterPaths = { "water.png", "watercolorX.png" };
        CustomColorizer.waterColors = getCustomColors(mcpColormap, waterPaths, 65536);
        if (Config.isCustomColors()) {
            final String[] pinePaths = { "pine.png", "pinecolor.png" };
            CustomColorizer.foliagePineColors = getCustomColors(mcpColormap, pinePaths, 65536);
            final String[] birchPaths = { "birch.png", "birchcolor.png" };
            CustomColorizer.foliageBirchColors = getCustomColors(mcpColormap, birchPaths, 65536);
            final String[] swampGrassPaths = { "swampgrass.png", "swampgrasscolor.png" };
            CustomColorizer.swampGrassColors = getCustomColors(mcpColormap, swampGrassPaths, 65536);
            final String[] swampFoliagePaths = { "swampfoliage.png", "swampfoliagecolor.png" };
            CustomColorizer.swampFoliageColors = getCustomColors(mcpColormap, swampFoliagePaths, 65536);
            final String[] sky0Paths = { "sky0.png", "skycolor0.png" };
            CustomColorizer.skyColors = getCustomColors(mcpColormap, sky0Paths, 65536);
            final String[] fog0Paths = { "fog0.png", "fogcolor0.png" };
            CustomColorizer.fogColors = getCustomColors(mcpColormap, fog0Paths, 65536);
            final String[] underwaterPaths = { "underwater.png", "underwatercolor.png" };
            CustomColorizer.underwaterColors = getCustomColors(mcpColormap, underwaterPaths, 65536);
            final String[] redstonePaths = { "redstone.png", "redstonecolor.png" };
            CustomColorizer.redstoneColors = getCustomColors(mcpColormap, redstonePaths, 16);
            final String[] stemPaths = { "stem.png", "stemcolor.png" };
            CustomColorizer.stemColors = getCustomColors(mcpColormap, stemPaths, 8);
            final String[] myceliumPaths = { "myceliumparticle.png", "myceliumparticlecolor.png" };
            CustomColorizer.myceliumParticleColors = getCustomColors(mcpColormap, myceliumPaths, -1);
            final int[][] lightMapsColors = new int[3][];
            CustomColorizer.lightMapsColorsRgb = new float[3][][];
            CustomColorizer.lightMapsHeight = new int[3];
            for (int i = 0; i < lightMapsColors.length; ++i) {
                final String path = "mcpatcher/lightmap/world" + (i - 1) + ".png";
                lightMapsColors[i] = getCustomColors(path, -1);
                if (lightMapsColors[i] != null) {
                    CustomColorizer.lightMapsColorsRgb[i] = toRgb(lightMapsColors[i]);
                }
                CustomColorizer.lightMapsHeight[i] = getTextureHeight(path, 32);
            }
            readColorProperties("mcpatcher/color.properties");
            updateUseDefaultColorMultiplier();
        }
    }
    
    private static int getTextureHeight(final String path, final int defHeight) {
        try {
            final InputStream e = Config.getResourceStream(new ResourceLocation(path));
            if (e == null) {
                return defHeight;
            }
            final BufferedImage bi = ImageIO.read(e);
            return (bi == null) ? defHeight : bi.getHeight();
        }
        catch (IOException var4) {
            return defHeight;
        }
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
    
    private static void readColorProperties(final String fileName) {
        try {
            final ResourceLocation e = new ResourceLocation(fileName);
            final InputStream in = Config.getResourceStream(e);
            if (in == null) {
                return;
            }
            Config.log("Loading " + fileName);
            final Properties props = new Properties();
            props.load(in);
            CustomColorizer.lilyPadColor = readColor(props, "lilypad");
            CustomColorizer.particleWaterColor = readColor(props, new String[] { "particle.water", "drop.water" });
            CustomColorizer.particlePortalColor = readColor(props, "particle.portal");
            CustomColorizer.fogColorNether = readColorVec3(props, "fog.nether");
            CustomColorizer.fogColorEnd = readColorVec3(props, "fog.end");
            CustomColorizer.skyColorEnd = readColorVec3(props, "sky.end");
            readCustomPalettes(props, fileName);
        }
        catch (FileNotFoundException var6) {}
        catch (IOException var5) {
            var5.printStackTrace();
        }
    }
    
    private static void readCustomPalettes(final Properties props, final String fileName) {
        CustomColorizer.blockPalettes = new int[256][1];
        for (int palettePrefix = 0; palettePrefix < 256; ++palettePrefix) {
            CustomColorizer.blockPalettes[palettePrefix][0] = -1;
        }
        final String var18 = "palette.block.";
        final HashMap map = new HashMap();
        final Set keys = props.keySet();
        for (final String i : keys) {
            final String name = props.getProperty(i);
            if (i.startsWith(var18)) {
                map.put(i, name);
            }
        }
        final String[] var19 = (String[])map.keySet().toArray(new String[map.size()]);
        CustomColorizer.paletteColors = new int[var19.length][];
        for (int var20 = 0; var20 < var19.length; ++var20) {
            final String name = var19[var20];
            final String value = props.getProperty(name);
            Config.log("Block palette: " + name + " = " + value);
            String path = name.substring(var18.length());
            final String basePath = TextureUtils.getBasePath(fileName);
            path = TextureUtils.fixResourcePath(path, basePath);
            final int[] colors = getCustomColors(path, 65536);
            CustomColorizer.paletteColors[var20] = colors;
            final String[] tokenize;
            final String[] indexStrs = tokenize = Config.tokenize(value, " ,;");
            for (String blockStr : tokenize) {
                final String indexStr = blockStr;
                int metadata = -1;
                Label_0538: {
                    if (blockStr.contains(":")) {
                        final String[] blockIndex = Config.tokenize(blockStr, ":");
                        blockStr = blockIndex[0];
                        final String metadataStr = blockIndex[1];
                        metadata = Config.parseInt(metadataStr, -1);
                        if (metadata < 0 || metadata > 15) {
                            Config.log("Invalid block metadata: " + blockStr + " in palette: " + name);
                            break Label_0538;
                        }
                    }
                    final int var21 = Config.parseInt(blockStr, -1);
                    if (var21 >= 0 && var21 <= 255) {
                        if (var21 != Block.getIdFromBlock(Blocks.grass) && var21 != Block.getIdFromBlock(Blocks.tallgrass) && var21 != Block.getIdFromBlock(Blocks.leaves) && var21 != Block.getIdFromBlock(Blocks.vine)) {
                            if (metadata == -1) {
                                CustomColorizer.blockPalettes[var21][0] = var20;
                            }
                            else {
                                if (CustomColorizer.blockPalettes[var21].length < 16) {
                                    Arrays.fill(CustomColorizer.blockPalettes[var21] = new int[16], -1);
                                }
                                CustomColorizer.blockPalettes[var21][metadata] = var20;
                            }
                        }
                    }
                    else {
                        Config.log("Invalid block index: " + var21 + " in palette: " + name);
                    }
                }
            }
        }
    }
    
    private static int readColor(final Properties props, final String[] names) {
        for (final String name : names) {
            final int col = readColor(props, name);
            if (col >= 0) {
                return col;
            }
        }
        return -1;
    }
    
    private static int readColor(final Properties props, final String name) {
        final String str = props.getProperty(name);
        if (str == null) {
            return -1;
        }
        try {
            final int e = Integer.parseInt(str, 16) & 0xFFFFFF;
            Config.log("Custom color: " + name + " = " + str);
            return e;
        }
        catch (NumberFormatException var4) {
            Config.log("Invalid custom color: " + name + " = " + str);
            return -1;
        }
    }
    
    private static Vec3 readColorVec3(final Properties props, final String name) {
        final int col = readColor(props, name);
        if (col < 0) {
            return null;
        }
        final int red = col >> 16 & 0xFF;
        final int green = col >> 8 & 0xFF;
        final int blue = col & 0xFF;
        final float redF = red / 255.0f;
        final float greenF = green / 255.0f;
        final float blueF = blue / 255.0f;
        return new Vec3(redF, greenF, blueF);
    }
    
    private static int[] getCustomColors(final String basePath, final String[] paths, final int length) {
        for (String path3 : paths) {
            final String path2 = path3;
            path3 = basePath + path3;
            final int[] cols = getCustomColors(path3, length);
            if (cols != null) {
                return cols;
            }
        }
        return null;
    }
    
    private static int[] getCustomColors(final String path, final int length) {
        try {
            final ResourceLocation e = new ResourceLocation(path);
            final InputStream in = Config.getResourceStream(e);
            if (in == null) {
                return null;
            }
            final int[] colors = TextureUtil.readImageData(Config.getResourceManager(), e);
            if (colors == null) {
                return null;
            }
            if (length > 0 && colors.length != length) {
                Config.log("Invalid custom colors length: " + colors.length + ", path: " + path);
                return null;
            }
            Config.log("Loading custom colors: " + path);
            return colors;
        }
        catch (FileNotFoundException var7) {
            return null;
        }
        catch (IOException var6) {
            var6.printStackTrace();
            return null;
        }
    }
    
    public static void updateUseDefaultColorMultiplier() {
        CustomColorizer.useDefaultColorMultiplier = (CustomColorizer.foliageBirchColors == null && CustomColorizer.foliagePineColors == null && CustomColorizer.swampGrassColors == null && CustomColorizer.swampFoliageColors == null && CustomColorizer.blockPalettes == null && Config.isSwampColors() && Config.isSmoothBiomes());
    }
    
    public static int getColorMultiplier(final BakedQuad quad, final Block block, final IBlockAccess blockAccess, final BlockPos blockPos, final RenderEnv renderEnv) {
        if (CustomColorizer.useDefaultColorMultiplier) {
            return -1;
        }
        int[] colors = null;
        int[] swampColors = null;
        if (CustomColorizer.blockPalettes != null) {
            final int useSwampColors = renderEnv.getBlockId();
            if (useSwampColors >= 0 && useSwampColors < 256) {
                final int[] smoothColors = CustomColorizer.blockPalettes[useSwampColors];
                final boolean type = true;
                int type2;
                if (smoothColors.length > 1) {
                    final int metadata = renderEnv.getMetadata();
                    type2 = smoothColors[metadata];
                }
                else {
                    type2 = smoothColors[0];
                }
                if (type2 >= 0) {
                    colors = CustomColorizer.paletteColors[type2];
                }
            }
            if (colors != null) {
                if (Config.isSmoothBiomes()) {
                    return getSmoothColorMultiplier(block, blockAccess, blockPos, colors, colors, 0, 0, renderEnv);
                }
                return getCustomColor(colors, blockAccess, blockPos);
            }
        }
        if (!quad.func_178212_b()) {
            return -1;
        }
        if (block == Blocks.waterlily) {
            return getLilypadColorMultiplier(blockAccess, blockPos);
        }
        if (block instanceof BlockStem) {
            return getStemColorMultiplier(block, blockAccess, blockPos, renderEnv);
        }
        final boolean useSwampColors2 = Config.isSwampColors();
        boolean smoothColors2 = false;
        byte type3 = 0;
        int metadata = 0;
        if (block != Blocks.grass && block != Blocks.tallgrass) {
            if (block == Blocks.leaves) {
                type3 = 2;
                smoothColors2 = Config.isSmoothBiomes();
                metadata = renderEnv.getMetadata();
                if ((metadata & 0x3) == 0x1) {
                    colors = CustomColorizer.foliagePineColors;
                }
                else if ((metadata & 0x3) == 0x2) {
                    colors = CustomColorizer.foliageBirchColors;
                }
                else {
                    colors = CustomColorizer.foliageColors;
                    if (useSwampColors2) {
                        swampColors = CustomColorizer.swampFoliageColors;
                    }
                    else {
                        swampColors = colors;
                    }
                }
            }
            else if (block == Blocks.vine) {
                type3 = 2;
                smoothColors2 = Config.isSmoothBiomes();
                colors = CustomColorizer.foliageColors;
                if (useSwampColors2) {
                    swampColors = CustomColorizer.swampFoliageColors;
                }
                else {
                    swampColors = colors;
                }
            }
        }
        else {
            type3 = 1;
            smoothColors2 = Config.isSmoothBiomes();
            colors = CustomColorizer.grassColors;
            if (useSwampColors2) {
                swampColors = CustomColorizer.swampGrassColors;
            }
            else {
                swampColors = colors;
            }
        }
        if (smoothColors2) {
            return getSmoothColorMultiplier(block, blockAccess, blockPos, colors, swampColors, type3, metadata, renderEnv);
        }
        if (swampColors != colors && blockAccess.getBiomeGenForCoords(blockPos) == BiomeGenBase.swampland) {
            colors = swampColors;
        }
        return (colors != null) ? getCustomColor(colors, blockAccess, blockPos) : -1;
    }
    
    private static int getSmoothColorMultiplier(final Block block, final IBlockAccess blockAccess, final BlockPos blockPos, final int[] colors, final int[] swampColors, final int type, final int metadata, final RenderEnv renderEnv) {
        int sumRed = 0;
        int sumGreen = 0;
        int sumBlue = 0;
        final int x = blockPos.getX();
        final int y = blockPos.getY();
        final int z = blockPos.getZ();
        final BlockPosM posM = renderEnv.getColorizerBlockPos();
        for (int r = x - 1; r <= x + 1; ++r) {
            for (int g = z - 1; g <= z + 1; ++g) {
                posM.setXyz(r, y, g);
                int[] b = colors;
                if (swampColors != colors && blockAccess.getBiomeGenForCoords(posM) == BiomeGenBase.swampland) {
                    b = swampColors;
                }
                final boolean col = false;
                int var20 = 0;
                if (b == null) {
                    switch (type) {
                        case 1: {
                            var20 = blockAccess.getBiomeGenForCoords(posM).func_180627_b(posM);
                            break;
                        }
                        case 2: {
                            if ((metadata & 0x3) == 0x1) {
                                var20 = ColorizerFoliage.getFoliageColorPine();
                                break;
                            }
                            if ((metadata & 0x3) == 0x2) {
                                var20 = ColorizerFoliage.getFoliageColorBirch();
                                break;
                            }
                            var20 = blockAccess.getBiomeGenForCoords(posM).func_180625_c(posM);
                            break;
                        }
                        default: {
                            var20 = block.colorMultiplier(blockAccess, posM);
                            break;
                        }
                    }
                }
                else {
                    var20 = getCustomColor(b, blockAccess, posM);
                }
                sumRed += (var20 >> 16 & 0xFF);
                sumGreen += (var20 >> 8 & 0xFF);
                sumBlue += (var20 & 0xFF);
            }
        }
        int r = sumRed / 9;
        int g = sumGreen / 9;
        final int var21 = sumBlue / 9;
        return r << 16 | g << 8 | var21;
    }
    
    public static int getFluidColor(final Block block, final IBlockAccess blockAccess, final BlockPos blockPos) {
        return (block.getMaterial() != Material.water) ? block.colorMultiplier(blockAccess, blockPos) : ((CustomColorizer.waterColors != null) ? (Config.isSmoothBiomes() ? getSmoothColor(CustomColorizer.waterColors, blockAccess, blockPos.getX(), blockPos.getY(), blockPos.getZ(), 3, 1) : getCustomColor(CustomColorizer.waterColors, blockAccess, blockPos)) : (Config.isSwampColors() ? block.colorMultiplier(blockAccess, blockPos) : 16777215));
    }
    
    private static int getCustomColor(final int[] colors, final IBlockAccess blockAccess, final BlockPos blockPos) {
        final BiomeGenBase bgb = blockAccess.getBiomeGenForCoords(blockPos);
        final double temperature = MathHelper.clamp_float(bgb.func_180626_a(blockPos), 0.0f, 1.0f);
        double rainfall = MathHelper.clamp_float(bgb.getFloatRainfall(), 0.0f, 1.0f);
        rainfall *= temperature;
        final int cx = (int)((1.0 - temperature) * 255.0);
        final int cy = (int)((1.0 - rainfall) * 255.0);
        return colors[cy << 8 | cx] & 0xFFFFFF;
    }
    
    public static void updatePortalFX(final EntityFX fx) {
        if (CustomColorizer.particlePortalColor >= 0) {
            final int col = CustomColorizer.particlePortalColor;
            final int red = col >> 16 & 0xFF;
            final int green = col >> 8 & 0xFF;
            final int blue = col & 0xFF;
            final float redF = red / 255.0f;
            final float greenF = green / 255.0f;
            final float blueF = blue / 255.0f;
            fx.setRBGColorF(redF, greenF, blueF);
        }
    }
    
    public static void updateMyceliumFX(final EntityFX fx) {
        if (CustomColorizer.myceliumParticleColors != null) {
            final int col = CustomColorizer.myceliumParticleColors[CustomColorizer.random.nextInt(CustomColorizer.myceliumParticleColors.length)];
            final int red = col >> 16 & 0xFF;
            final int green = col >> 8 & 0xFF;
            final int blue = col & 0xFF;
            final float redF = red / 255.0f;
            final float greenF = green / 255.0f;
            final float blueF = blue / 255.0f;
            fx.setRBGColorF(redF, greenF, blueF);
        }
    }
    
    public static void updateReddustFX(final EntityFX fx, final IBlockAccess blockAccess, final double x, final double y, final double z) {
        if (CustomColorizer.redstoneColors != null) {
            final IBlockState state = blockAccess.getBlockState(new BlockPos(x, y, z));
            final int level = getRedstoneLevel(state, 15);
            final int col = getRedstoneColor(level);
            if (col != -1) {
                final int red = col >> 16 & 0xFF;
                final int green = col >> 8 & 0xFF;
                final int blue = col & 0xFF;
                final float redF = red / 255.0f;
                final float greenF = green / 255.0f;
                final float blueF = blue / 255.0f;
                fx.setRBGColorF(redF, greenF, blueF);
            }
        }
    }
    
    private static int getRedstoneLevel(final IBlockState state, final int def) {
        final Block block = state.getBlock();
        if (!(block instanceof BlockRedstoneWire)) {
            return def;
        }
        final Comparable val = state.getValue(BlockRedstoneWire.POWER);
        if (!(val instanceof Integer)) {
            return def;
        }
        final Integer valInt = (Integer)val;
        return valInt;
    }
    
    public static int getRedstoneColor(final int level) {
        return (CustomColorizer.redstoneColors == null) ? -1 : ((level >= 0 && level <= 15) ? (CustomColorizer.redstoneColors[level] & 0xFFFFFF) : -1);
    }
    
    public static void updateWaterFX(final EntityFX fx, final IBlockAccess blockAccess, final double x, final double y, final double z) {
        if (CustomColorizer.waterColors != null) {
            final int col = getFluidColor(Blocks.water, blockAccess, new BlockPos(x, y, z));
            final int red = col >> 16 & 0xFF;
            final int green = col >> 8 & 0xFF;
            final int blue = col & 0xFF;
            float redF = red / 255.0f;
            float greenF = green / 255.0f;
            float blueF = blue / 255.0f;
            if (CustomColorizer.particleWaterColor >= 0) {
                final int redDrop = CustomColorizer.particleWaterColor >> 16 & 0xFF;
                final int greenDrop = CustomColorizer.particleWaterColor >> 8 & 0xFF;
                final int blueDrop = CustomColorizer.particleWaterColor & 0xFF;
                redF *= redDrop / 255.0f;
                greenF *= greenDrop / 255.0f;
                blueF *= blueDrop / 255.0f;
            }
            fx.setRBGColorF(redF, greenF, blueF);
        }
    }
    
    public static int getLilypadColorMultiplier(final IBlockAccess blockAccess, final BlockPos blockPos) {
        return (CustomColorizer.lilyPadColor < 0) ? Blocks.waterlily.colorMultiplier(blockAccess, blockPos) : CustomColorizer.lilyPadColor;
    }
    
    public static Vec3 getFogColorNether(final Vec3 col) {
        return (CustomColorizer.fogColorNether == null) ? col : CustomColorizer.fogColorNether;
    }
    
    public static Vec3 getFogColorEnd(final Vec3 col) {
        return (CustomColorizer.fogColorEnd == null) ? col : CustomColorizer.fogColorEnd;
    }
    
    public static Vec3 getSkyColorEnd(final Vec3 col) {
        return (CustomColorizer.skyColorEnd == null) ? col : CustomColorizer.skyColorEnd;
    }
    
    public static Vec3 getSkyColor(final Vec3 skyColor3d, final IBlockAccess blockAccess, final double x, final double y, final double z) {
        if (CustomColorizer.skyColors == null) {
            return skyColor3d;
        }
        final int col = getSmoothColor(CustomColorizer.skyColors, blockAccess, x, y, z, 7, 1);
        final int red = col >> 16 & 0xFF;
        final int green = col >> 8 & 0xFF;
        final int blue = col & 0xFF;
        float redF = red / 255.0f;
        float greenF = green / 255.0f;
        float blueF = blue / 255.0f;
        final float cRed = (float)skyColor3d.xCoord / 0.5f;
        final float cGreen = (float)skyColor3d.yCoord / 0.66275f;
        final float cBlue = (float)skyColor3d.zCoord;
        redF *= cRed;
        greenF *= cGreen;
        blueF *= cBlue;
        return new Vec3(redF, greenF, blueF);
    }
    
    public static Vec3 getFogColor(final Vec3 fogColor3d, final IBlockAccess blockAccess, final double x, final double y, final double z) {
        if (CustomColorizer.fogColors == null) {
            return fogColor3d;
        }
        final int col = getSmoothColor(CustomColorizer.fogColors, blockAccess, x, y, z, 7, 1);
        final int red = col >> 16 & 0xFF;
        final int green = col >> 8 & 0xFF;
        final int blue = col & 0xFF;
        float redF = red / 255.0f;
        float greenF = green / 255.0f;
        float blueF = blue / 255.0f;
        final float cRed = (float)fogColor3d.xCoord / 0.753f;
        final float cGreen = (float)fogColor3d.yCoord / 0.8471f;
        final float cBlue = (float)fogColor3d.zCoord;
        redF *= cRed;
        greenF *= cGreen;
        blueF *= cBlue;
        return new Vec3(redF, greenF, blueF);
    }
    
    public static Vec3 getUnderwaterColor(final IBlockAccess blockAccess, final double x, final double y, final double z) {
        if (CustomColorizer.underwaterColors == null) {
            return null;
        }
        final int col = getSmoothColor(CustomColorizer.underwaterColors, blockAccess, x, y, z, 7, 1);
        final int red = col >> 16 & 0xFF;
        final int green = col >> 8 & 0xFF;
        final int blue = col & 0xFF;
        final float redF = red / 255.0f;
        final float greenF = green / 255.0f;
        final float blueF = blue / 255.0f;
        return new Vec3(redF, greenF, blueF);
    }
    
    public static int getSmoothColor(final int[] colors, final IBlockAccess blockAccess, final double x, final double y, final double z, final int samples, final int step) {
        if (colors == null) {
            return -1;
        }
        final int x2 = MathHelper.floor_double(x);
        final int y2 = MathHelper.floor_double(y);
        final int z2 = MathHelper.floor_double(z);
        final int n = samples * step / 2;
        int sumRed = 0;
        int sumGreen = 0;
        int sumBlue = 0;
        int count = 0;
        final BlockPosM blockPosM = new BlockPosM(0, 0, 0);
        for (int r = x2 - n; r <= x2 + n; r += step) {
            for (int g = z2 - n; g <= z2 + n; g += step) {
                blockPosM.setXyz(r, y2, g);
                final int b = getCustomColor(colors, blockAccess, blockPosM);
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
    
    public static int mixColors(final int c1, final int c2, final float w1) {
        if (w1 <= 0.0f) {
            return c2;
        }
        if (w1 >= 1.0f) {
            return c1;
        }
        final float w2 = 1.0f - w1;
        final int r1 = c1 >> 16 & 0xFF;
        final int g1 = c1 >> 8 & 0xFF;
        final int b1 = c1 & 0xFF;
        final int r2 = c2 >> 16 & 0xFF;
        final int g2 = c2 >> 8 & 0xFF;
        final int b2 = c2 & 0xFF;
        final int r3 = (int)(r1 * w1 + r2 * w2);
        final int g3 = (int)(g1 * w1 + g2 * w2);
        final int b3 = (int)(b1 * w1 + b2 * w2);
        return r3 << 16 | g3 << 8 | b3;
    }
    
    private static int averageColor(final int c1, final int c2) {
        final int r1 = c1 >> 16 & 0xFF;
        final int g1 = c1 >> 8 & 0xFF;
        final int b1 = c1 & 0xFF;
        final int r2 = c2 >> 16 & 0xFF;
        final int g2 = c2 >> 8 & 0xFF;
        final int b2 = c2 & 0xFF;
        final int r3 = (r1 + r2) / 2;
        final int g3 = (g1 + g2) / 2;
        final int b3 = (b1 + b2) / 2;
        return r3 << 16 | g3 << 8 | b3;
    }
    
    public static int getStemColorMultiplier(final Block blockStem, final IBlockAccess blockAccess, final BlockPos blockPos, final RenderEnv renderEnv) {
        if (CustomColorizer.stemColors == null) {
            return blockStem.colorMultiplier(blockAccess, blockPos);
        }
        int level = renderEnv.getMetadata();
        if (level < 0) {
            level = 0;
        }
        if (level >= CustomColorizer.stemColors.length) {
            level = CustomColorizer.stemColors.length - 1;
        }
        return CustomColorizer.stemColors[level];
    }
    
    public static boolean updateLightmap(final World world, final float torchFlickerX, final int[] lmColors, final boolean nightvision) {
        if (world == null) {
            return false;
        }
        if (CustomColorizer.lightMapsColorsRgb == null) {
            return false;
        }
        if (!Config.isCustomColors()) {
            return false;
        }
        final int worldType = world.provider.getDimensionId();
        if (worldType < -1 || worldType > 1) {
            return false;
        }
        final int lightMapIndex = worldType + 1;
        final float[][] lightMapRgb = CustomColorizer.lightMapsColorsRgb[lightMapIndex];
        if (lightMapRgb == null) {
            return false;
        }
        final int height = CustomColorizer.lightMapsHeight[lightMapIndex];
        if (nightvision && height < 64) {
            return false;
        }
        final int width = lightMapRgb.length / height;
        if (width < 16) {
            Config.warn("Invalid lightmap width: " + width + " for: /environment/lightmap" + worldType + ".png");
            CustomColorizer.lightMapsColorsRgb[lightMapIndex] = null;
            return false;
        }
        int startIndex = 0;
        if (nightvision) {
            startIndex = width * 16 * 2;
        }
        float sun = 1.1666666f * (world.getSunBrightness(1.0f) - 0.2f);
        if (world.func_175658_ac() > 0) {
            sun = 1.0f;
        }
        sun = Config.limitTo1(sun);
        final float sunX = sun * (width - 1);
        final float torchX = Config.limitTo1(torchFlickerX + 0.5f) * (width - 1);
        final float gamma = Config.limitTo1(Config.getGameSettings().gammaSetting);
        final boolean hasGamma = gamma > 1.0E-4f;
        getLightMapColumn(lightMapRgb, sunX, startIndex, width, CustomColorizer.sunRgbs);
        getLightMapColumn(lightMapRgb, torchX, startIndex + 16 * width, width, CustomColorizer.torchRgbs);
        final float[] rgb = new float[3];
        for (int is = 0; is < 16; ++is) {
            for (int it = 0; it < 16; ++it) {
                for (int r = 0; r < 3; ++r) {
                    float g = Config.limitTo1(CustomColorizer.sunRgbs[is][r] + CustomColorizer.torchRgbs[it][r]);
                    if (hasGamma) {
                        float b = 1.0f - g;
                        b = 1.0f - b * b * b * b;
                        g = gamma * b + (1.0f - gamma) * g;
                    }
                    rgb[r] = g;
                }
                int r = (int)(rgb[0] * 255.0f);
                final int var21 = (int)(rgb[1] * 255.0f);
                final int var22 = (int)(rgb[2] * 255.0f);
                lmColors[is * 16 + it] = (0xFF000000 | r << 16 | var21 << 8 | var22);
            }
        }
        return true;
    }
    
    private static void getLightMapColumn(final float[][] origMap, final float x, final int offset, final int width, final float[][] colRgb) {
        final int xLow = (int)Math.floor(x);
        final int xHigh = (int)Math.ceil(x);
        if (xLow == xHigh) {
            for (int var14 = 0; var14 < 16; ++var14) {
                final float[] var15 = origMap[offset + var14 * width + xLow];
                final float[] var16 = colRgb[var14];
                for (int var17 = 0; var17 < 3; ++var17) {
                    var16[var17] = var15[var17];
                }
            }
        }
        else {
            final float dLow = 1.0f - (x - xLow);
            final float dHigh = 1.0f - (xHigh - x);
            for (int y = 0; y < 16; ++y) {
                final float[] rgbLow = origMap[offset + y * width + xLow];
                final float[] rgbHigh = origMap[offset + y * width + xHigh];
                final float[] rgb = colRgb[y];
                for (int i = 0; i < 3; ++i) {
                    rgb[i] = rgbLow[i] * dLow + rgbHigh[i] * dHigh;
                }
            }
        }
    }
    
    public static Vec3 getWorldFogColor(Vec3 fogVec, final WorldClient world, final Entity renderViewEntity, final float partialTicks) {
        final int worldType = world.provider.getDimensionId();
        switch (worldType) {
            case -1: {
                fogVec = getFogColorNether(fogVec);
                break;
            }
            case 0: {
                final Minecraft mc = Minecraft.getMinecraft();
                fogVec = getFogColor(fogVec, mc.theWorld, renderViewEntity.posX, renderViewEntity.posY + 1.0, renderViewEntity.posZ);
                break;
            }
            case 1: {
                fogVec = getFogColorEnd(fogVec);
                break;
            }
        }
        return fogVec;
    }
    
    public static Vec3 getWorldSkyColor(Vec3 skyVec, final WorldClient world, final Entity renderViewEntity, final float partialTicks) {
        final int worldType = world.provider.getDimensionId();
        switch (worldType) {
            case 0: {
                final Minecraft mc = Minecraft.getMinecraft();
                skyVec = getSkyColor(skyVec, mc.theWorld, renderViewEntity.posX, renderViewEntity.posY + 1.0, renderViewEntity.posZ);
                break;
            }
            case 1: {
                skyVec = getSkyColorEnd(skyVec);
                break;
            }
        }
        return skyVec;
    }
    
    static {
        CustomColorizer.grassColors = null;
        CustomColorizer.waterColors = null;
        CustomColorizer.foliageColors = null;
        CustomColorizer.foliagePineColors = null;
        CustomColorizer.foliageBirchColors = null;
        CustomColorizer.swampFoliageColors = null;
        CustomColorizer.swampGrassColors = null;
        CustomColorizer.blockPalettes = null;
        CustomColorizer.paletteColors = null;
        CustomColorizer.skyColors = null;
        CustomColorizer.fogColors = null;
        CustomColorizer.underwaterColors = null;
        CustomColorizer.lightMapsColorsRgb = null;
        CustomColorizer.lightMapsHeight = null;
        CustomColorizer.sunRgbs = new float[16][3];
        CustomColorizer.torchRgbs = new float[16][3];
        CustomColorizer.redstoneColors = null;
        CustomColorizer.stemColors = null;
        CustomColorizer.myceliumParticleColors = null;
        CustomColorizer.useDefaultColorMultiplier = true;
        CustomColorizer.particleWaterColor = -1;
        CustomColorizer.particlePortalColor = -1;
        CustomColorizer.lilyPadColor = -1;
        CustomColorizer.fogColorNether = null;
        CustomColorizer.fogColorEnd = null;
        CustomColorizer.skyColorEnd = null;
        CustomColorizer.random = new Random();
    }
}
