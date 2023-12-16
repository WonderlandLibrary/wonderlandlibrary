// 
// Decompiled by Procyon v0.5.36
// 

package optifine;

import net.minecraft.entity.EntityList;
import net.minecraft.potion.Potion;
import net.minecraft.block.material.MapColor;
import net.minecraft.item.EnumDyeColor;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemMonsterPlacer;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.Entity;
import net.minecraft.client.multiplayer.WorldClient;
import net.minecraft.world.World;
import net.minecraft.util.MathHelper;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.BlockRedstoneWire;
import net.minecraft.client.particle.EntityFX;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.BlockStateBase;
import net.minecraft.block.BlockStem;
import net.minecraft.block.Block;
import net.minecraft.client.renderer.block.model.BakedQuad;
import java.util.List;
import java.util.Iterator;
import java.util.ArrayList;
import java.io.FileNotFoundException;
import java.util.Properties;
import java.awt.image.BufferedImage;
import java.io.InputStream;
import java.io.IOException;
import javax.imageio.ImageIO;
import net.minecraft.util.ResourceLocation;
import java.util.Set;
import org.apache.commons.lang3.tuple.ImmutablePair;
import java.util.Arrays;
import java.util.HashMap;
import org.apache.commons.lang3.tuple.Pair;
import net.minecraft.potion.PotionHelper;
import net.minecraft.world.ColorizerFoliage;
import net.minecraft.world.biome.BiomeGenBase;
import net.minecraft.util.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.init.Blocks;
import java.util.Random;
import net.minecraft.block.state.IBlockState;
import net.minecraft.util.Vec3;

public class CustomColors
{
    private static CustomColormap waterColors;
    private static CustomColormap foliagePineColors;
    private static CustomColormap foliageBirchColors;
    private static CustomColormap swampFoliageColors;
    private static CustomColormap swampGrassColors;
    private static CustomColormap[] colorsBlockColormaps;
    private static CustomColormap[][] blockColormaps;
    private static CustomColormap skyColors;
    private static CustomColorFader skyColorFader;
    private static CustomColormap fogColors;
    private static CustomColorFader fogColorFader;
    private static CustomColormap underwaterColors;
    private static CustomColorFader underwaterColorFader;
    private static CustomColormap[] lightMapsColorsRgb;
    private static int lightmapMinDimensionId;
    private static float[][] sunRgbs;
    private static float[][] torchRgbs;
    private static CustomColormap redstoneColors;
    private static CustomColormap xpOrbColors;
    private static CustomColormap stemColors;
    private static CustomColormap stemMelonColors;
    private static CustomColormap stemPumpkinColors;
    private static CustomColormap myceliumParticleColors;
    private static boolean useDefaultGrassFoliageColors;
    private static int particleWaterColor;
    private static int particlePortalColor;
    private static int lilyPadColor;
    private static int expBarTextColor;
    private static int bossTextColor;
    private static int signTextColor;
    private static Vec3 fogColorNether;
    private static Vec3 fogColorEnd;
    private static Vec3 skyColorEnd;
    private static int[] spawnEggPrimaryColors;
    private static int[] spawnEggSecondaryColors;
    private static float[][] wolfCollarColors;
    private static float[][] sheepColors;
    private static int[] textColors;
    private static int[] mapColorsOriginal;
    private static int[] potionColors;
    private static final IBlockState BLOCK_STATE_DIRT;
    private static final IBlockState BLOCK_STATE_WATER;
    public static Random random;
    private static final IColorizer COLORIZER_GRASS;
    private static final IColorizer COLORIZER_FOLIAGE;
    private static final IColorizer COLORIZER_FOLIAGE_PINE;
    private static final IColorizer COLORIZER_FOLIAGE_BIRCH;
    private static final IColorizer COLORIZER_WATER;
    
    static {
        CustomColors.waterColors = null;
        CustomColors.foliagePineColors = null;
        CustomColors.foliageBirchColors = null;
        CustomColors.swampFoliageColors = null;
        CustomColors.swampGrassColors = null;
        CustomColors.colorsBlockColormaps = null;
        CustomColors.blockColormaps = null;
        CustomColors.skyColors = null;
        CustomColors.skyColorFader = new CustomColorFader();
        CustomColors.fogColors = null;
        CustomColors.fogColorFader = new CustomColorFader();
        CustomColors.underwaterColors = null;
        CustomColors.underwaterColorFader = new CustomColorFader();
        CustomColors.lightMapsColorsRgb = null;
        CustomColors.lightmapMinDimensionId = 0;
        CustomColors.sunRgbs = new float[16][3];
        CustomColors.torchRgbs = new float[16][3];
        CustomColors.redstoneColors = null;
        CustomColors.xpOrbColors = null;
        CustomColors.stemColors = null;
        CustomColors.stemMelonColors = null;
        CustomColors.stemPumpkinColors = null;
        CustomColors.myceliumParticleColors = null;
        CustomColors.useDefaultGrassFoliageColors = true;
        CustomColors.particleWaterColor = -1;
        CustomColors.particlePortalColor = -1;
        CustomColors.lilyPadColor = -1;
        CustomColors.expBarTextColor = -1;
        CustomColors.bossTextColor = -1;
        CustomColors.signTextColor = -1;
        CustomColors.fogColorNether = null;
        CustomColors.fogColorEnd = null;
        CustomColors.skyColorEnd = null;
        CustomColors.spawnEggPrimaryColors = null;
        CustomColors.spawnEggSecondaryColors = null;
        CustomColors.wolfCollarColors = null;
        CustomColors.sheepColors = null;
        CustomColors.textColors = null;
        CustomColors.mapColorsOriginal = null;
        CustomColors.potionColors = null;
        BLOCK_STATE_DIRT = Blocks.dirt.getDefaultState();
        BLOCK_STATE_WATER = Blocks.water.getDefaultState();
        CustomColors.random = new Random();
        COLORIZER_GRASS = new IColorizer() {
            @Override
            public int getColor(final IBlockAccess blockAccess, final BlockPos blockPos) {
                final BiomeGenBase biome = CustomColors.getColorBiome(blockAccess, blockPos);
                return (CustomColors.swampGrassColors != null && biome == BiomeGenBase.swampland) ? CustomColors.swampGrassColors.getColor(biome, blockPos) : biome.func_180627_b(blockPos);
            }
            
            @Override
            public boolean isColorConstant() {
                return false;
            }
        };
        COLORIZER_FOLIAGE = new IColorizer() {
            @Override
            public int getColor(final IBlockAccess blockAccess, final BlockPos blockPos) {
                final BiomeGenBase biome = CustomColors.getColorBiome(blockAccess, blockPos);
                return (CustomColors.swampFoliageColors != null && biome == BiomeGenBase.swampland) ? CustomColors.swampFoliageColors.getColor(biome, blockPos) : biome.func_180625_c(blockPos);
            }
            
            @Override
            public boolean isColorConstant() {
                return false;
            }
        };
        COLORIZER_FOLIAGE_PINE = new IColorizer() {
            @Override
            public int getColor(final IBlockAccess blockAccess, final BlockPos blockPos) {
                return (CustomColors.foliagePineColors != null) ? CustomColors.foliagePineColors.getColor(blockAccess, blockPos) : ColorizerFoliage.getFoliageColorPine();
            }
            
            @Override
            public boolean isColorConstant() {
                return CustomColors.foliagePineColors == null;
            }
        };
        COLORIZER_FOLIAGE_BIRCH = new IColorizer() {
            @Override
            public int getColor(final IBlockAccess blockAccess, final BlockPos blockPos) {
                return (CustomColors.foliageBirchColors != null) ? CustomColors.foliageBirchColors.getColor(blockAccess, blockPos) : ColorizerFoliage.getFoliageColorBirch();
            }
            
            @Override
            public boolean isColorConstant() {
                return CustomColors.foliageBirchColors == null;
            }
        };
        COLORIZER_WATER = new IColorizer() {
            @Override
            public int getColor(final IBlockAccess blockAccess, final BlockPos blockPos) {
                final BiomeGenBase biome = CustomColors.getColorBiome(blockAccess, blockPos);
                return (CustomColors.waterColors != null) ? CustomColors.waterColors.getColor(biome, blockPos) : (Reflector.ForgeBiomeGenBase_getWaterColorMultiplier.exists() ? Reflector.callInt(biome, Reflector.ForgeBiomeGenBase_getWaterColorMultiplier, new Object[0]) : biome.waterColorMultiplier);
            }
            
            @Override
            public boolean isColorConstant() {
                return false;
            }
        };
    }
    
    public static void update() {
        CustomColors.waterColors = null;
        CustomColors.foliageBirchColors = null;
        CustomColors.foliagePineColors = null;
        CustomColors.swampGrassColors = null;
        CustomColors.swampFoliageColors = null;
        CustomColors.skyColors = null;
        CustomColors.fogColors = null;
        CustomColors.underwaterColors = null;
        CustomColors.redstoneColors = null;
        CustomColors.xpOrbColors = null;
        CustomColors.stemColors = null;
        CustomColors.myceliumParticleColors = null;
        CustomColors.lightMapsColorsRgb = null;
        CustomColors.particleWaterColor = -1;
        CustomColors.particlePortalColor = -1;
        CustomColors.lilyPadColor = -1;
        CustomColors.expBarTextColor = -1;
        CustomColors.bossTextColor = -1;
        CustomColors.signTextColor = -1;
        CustomColors.fogColorNether = null;
        CustomColors.fogColorEnd = null;
        CustomColors.skyColorEnd = null;
        CustomColors.colorsBlockColormaps = null;
        CustomColors.blockColormaps = null;
        CustomColors.useDefaultGrassFoliageColors = true;
        CustomColors.spawnEggPrimaryColors = null;
        CustomColors.spawnEggSecondaryColors = null;
        CustomColors.wolfCollarColors = null;
        CustomColors.sheepColors = null;
        CustomColors.textColors = null;
        setMapColors(CustomColors.mapColorsOriginal);
        CustomColors.potionColors = null;
        PotionHelper.clearPotionColorCache();
        final String mcpColormap = "mcpatcher/colormap/";
        final String[] waterPaths = { "water.png", "watercolorX.png" };
        CustomColors.waterColors = getCustomColors(mcpColormap, waterPaths, 256, 256);
        updateUseDefaultGrassFoliageColors();
        if (Config.isCustomColors()) {
            final String[] pinePaths = { "pine.png", "pinecolor.png" };
            CustomColors.foliagePineColors = getCustomColors(mcpColormap, pinePaths, 256, 256);
            final String[] birchPaths = { "birch.png", "birchcolor.png" };
            CustomColors.foliageBirchColors = getCustomColors(mcpColormap, birchPaths, 256, 256);
            final String[] swampGrassPaths = { "swampgrass.png", "swampgrasscolor.png" };
            CustomColors.swampGrassColors = getCustomColors(mcpColormap, swampGrassPaths, 256, 256);
            final String[] swampFoliagePaths = { "swampfoliage.png", "swampfoliagecolor.png" };
            CustomColors.swampFoliageColors = getCustomColors(mcpColormap, swampFoliagePaths, 256, 256);
            final String[] sky0Paths = { "sky0.png", "skycolor0.png" };
            CustomColors.skyColors = getCustomColors(mcpColormap, sky0Paths, 256, 256);
            final String[] fog0Paths = { "fog0.png", "fogcolor0.png" };
            CustomColors.fogColors = getCustomColors(mcpColormap, fog0Paths, 256, 256);
            final String[] underwaterPaths = { "underwater.png", "underwatercolor.png" };
            CustomColors.underwaterColors = getCustomColors(mcpColormap, underwaterPaths, 256, 256);
            final String[] redstonePaths = { "redstone.png", "redstonecolor.png" };
            CustomColors.redstoneColors = getCustomColors(mcpColormap, redstonePaths, 16, 1);
            CustomColors.xpOrbColors = getCustomColors(String.valueOf(mcpColormap) + "xporb.png", -1, -1);
            final String[] stemPaths = { "stem.png", "stemcolor.png" };
            CustomColors.stemColors = getCustomColors(mcpColormap, stemPaths, 8, 1);
            CustomColors.stemPumpkinColors = getCustomColors(String.valueOf(mcpColormap) + "pumpkinstem.png", 8, 1);
            CustomColors.stemMelonColors = getCustomColors(String.valueOf(mcpColormap) + "melonstem.png", 8, 1);
            final String[] myceliumPaths = { "myceliumparticle.png", "myceliumparticlecolor.png" };
            CustomColors.myceliumParticleColors = getCustomColors(mcpColormap, myceliumPaths, -1, -1);
            final Pair lightMaps = parseLightmapsRgb();
            CustomColors.lightMapsColorsRgb = (CustomColormap[])lightMaps.getLeft();
            CustomColors.lightmapMinDimensionId = (int)lightMaps.getRight();
            readColorProperties("mcpatcher/color.properties");
            CustomColors.blockColormaps = readBlockColormaps(new String[] { String.valueOf(mcpColormap) + "custom/", String.valueOf(mcpColormap) + "blocks/" }, CustomColors.colorsBlockColormaps, 256, 256);
            updateUseDefaultGrassFoliageColors();
        }
    }
    
    private static Pair<CustomColormap[], Integer> parseLightmapsRgb() {
        final String lightmapPrefix = "mcpatcher/lightmap/world";
        final String lightmapSuffix = ".png";
        final String[] pathsLightmap = ResUtils.collectFiles(lightmapPrefix, lightmapSuffix);
        final HashMap mapLightmaps = new HashMap();
        for (int setDimIds = 0; setDimIds < pathsLightmap.length; ++setDimIds) {
            final String dimIds = pathsLightmap[setDimIds];
            final String minDimId = StrUtils.removePrefixSuffix(dimIds, lightmapPrefix, lightmapSuffix);
            final int maxDimId = Config.parseInt(minDimId, Integer.MIN_VALUE);
            if (maxDimId == Integer.MIN_VALUE) {
                warn("Invalid dimension ID: " + minDimId + ", path: " + dimIds);
            }
            else {
                mapLightmaps.put(maxDimId, dimIds);
            }
        }
        final Set var15 = mapLightmaps.keySet();
        final Integer[] var16 = var15.toArray(new Integer[var15.size()]);
        Arrays.sort(var16);
        if (var16.length <= 0) {
            return (Pair<CustomColormap[], Integer>)new ImmutablePair((Object)null, (Object)0);
        }
        final int var17 = var16[0];
        final int maxDimId = var16[var16.length - 1];
        final int countDim = maxDimId - var17 + 1;
        final CustomColormap[] colormaps = new CustomColormap[countDim];
        for (int i = 0; i < var16.length; ++i) {
            final Integer dimId = var16[i];
            final String path = mapLightmaps.get(dimId);
            final CustomColormap colors = getCustomColors(path, -1, -1);
            if (colors != null) {
                if (colors.getWidth() < 16) {
                    warn("Invalid lightmap width: " + colors.getWidth() + ", path: " + path);
                }
                else {
                    final int lightmapIndex = dimId - var17;
                    colormaps[lightmapIndex] = colors;
                }
            }
        }
        return (Pair<CustomColormap[], Integer>)new ImmutablePair((Object)colormaps, (Object)var17);
    }
    
    private static int getTextureHeight(final String path, final int defHeight) {
        try {
            final InputStream e = Config.getResourceStream(new ResourceLocation(path));
            if (e == null) {
                return defHeight;
            }
            final BufferedImage bi = ImageIO.read(e);
            e.close();
            return (bi == null) ? defHeight : bi.getHeight();
        }
        catch (IOException var4) {
            return defHeight;
        }
    }
    
    private static void readColorProperties(final String fileName) {
        try {
            final ResourceLocation e = new ResourceLocation(fileName);
            final InputStream in = Config.getResourceStream(e);
            if (in == null) {
                return;
            }
            dbg("Loading " + fileName);
            final Properties props = new Properties();
            props.load(in);
            in.close();
            CustomColors.particleWaterColor = readColor(props, new String[] { "particle.water", "drop.water" });
            CustomColors.particlePortalColor = readColor(props, "particle.portal");
            CustomColors.lilyPadColor = readColor(props, "lilypad");
            CustomColors.expBarTextColor = readColor(props, "text.xpbar");
            CustomColors.bossTextColor = readColor(props, "text.boss");
            CustomColors.signTextColor = readColor(props, "text.sign");
            CustomColors.fogColorNether = readColorVec3(props, "fog.nether");
            CustomColors.fogColorEnd = readColorVec3(props, "fog.end");
            CustomColors.skyColorEnd = readColorVec3(props, "sky.end");
            CustomColors.colorsBlockColormaps = readCustomColormaps(props, fileName);
            CustomColors.spawnEggPrimaryColors = readSpawnEggColors(props, fileName, "egg.shell.", "Spawn egg shell");
            CustomColors.spawnEggSecondaryColors = readSpawnEggColors(props, fileName, "egg.spots.", "Spawn egg spot");
            CustomColors.wolfCollarColors = readDyeColors(props, fileName, "collar.", "Wolf collar");
            CustomColors.sheepColors = readDyeColors(props, fileName, "sheep.", "Sheep");
            CustomColors.textColors = readTextColors(props, fileName, "text.code.", "Text");
            final int[] mapColors = readMapColors(props, fileName, "map.", "Map");
            if (mapColors != null) {
                if (CustomColors.mapColorsOriginal == null) {
                    CustomColors.mapColorsOriginal = getMapColors();
                }
                setMapColors(mapColors);
            }
            CustomColors.potionColors = readPotionColors(props, fileName, "potion.", "Potion");
        }
        catch (FileNotFoundException var7) {}
        catch (IOException var6) {
            var6.printStackTrace();
        }
    }
    
    private static CustomColormap[] readCustomColormaps(final Properties props, final String fileName) {
        final ArrayList list = new ArrayList();
        final String palettePrefix = "palette.block.";
        final HashMap map = new HashMap();
        final Set keys = props.keySet();
        for (final String cms : keys) {
            final String name = props.getProperty(cms);
            if (cms.startsWith(palettePrefix)) {
                map.put(cms, name);
            }
        }
        final String[] var17 = (String[])map.keySet().toArray(new String[map.size()]);
        for (int var18 = 0; var18 < var17.length; ++var18) {
            final String name = var17[var18];
            final String value = props.getProperty(name);
            dbg("Block palette: " + name + " = " + value);
            String path = name.substring(palettePrefix.length());
            final String basePath = TextureUtils.getBasePath(fileName);
            path = TextureUtils.fixResourcePath(path, basePath);
            final CustomColormap colors = getCustomColors(path, 256, 256);
            if (colors == null) {
                warn("Colormap not found: " + path);
            }
            else {
                final ConnectedParser cp = new ConnectedParser("CustomColors");
                final MatchBlock[] mbs = cp.parseMatchBlocks(value);
                if (mbs != null && mbs.length > 0) {
                    for (int m = 0; m < mbs.length; ++m) {
                        final MatchBlock mb = mbs[m];
                        colors.addMatchBlock(mb);
                    }
                    list.add(colors);
                }
                else {
                    warn("Invalid match blocks: " + value);
                }
            }
        }
        if (list.size() <= 0) {
            return null;
        }
        final CustomColormap[] var19 = list.toArray(new CustomColormap[list.size()]);
        return var19;
    }
    
    private static CustomColormap[][] readBlockColormaps(final String[] basePaths, final CustomColormap[] basePalettes, final int width, final int height) {
        final String[] paths = ResUtils.collectFiles(basePaths, new String[] { ".properties" });
        Arrays.sort(paths);
        final ArrayList blockList = new ArrayList();
        for (int cmArr = 0; cmArr < paths.length; ++cmArr) {
            final String cm = paths[cmArr];
            dbg("Block colormap: " + cm);
            try {
                final ResourceLocation e = new ResourceLocation("minecraft", cm);
                final InputStream in = Config.getResourceStream(e);
                if (in == null) {
                    warn("File not found: " + cm);
                }
                else {
                    final Properties props = new Properties();
                    props.load(in);
                    final CustomColormap cm2 = new CustomColormap(props, cm, width, height);
                    if (cm2.isValid(cm) && cm2.isValidMatchBlocks(cm)) {
                        addToBlockList(cm2, blockList);
                    }
                }
            }
            catch (FileNotFoundException var16) {
                warn("File not found: " + cm);
            }
            catch (Exception var13) {
                var13.printStackTrace();
            }
        }
        if (basePalettes != null) {
            for (int cmArr = 0; cmArr < basePalettes.length; ++cmArr) {
                final CustomColormap var14 = basePalettes[cmArr];
                addToBlockList(var14, blockList);
            }
        }
        if (blockList.size() <= 0) {
            return null;
        }
        final CustomColormap[][] var15 = blockListToArray(blockList);
        return var15;
    }
    
    private static void addToBlockList(final CustomColormap cm, final List blockList) {
        final int[] ids = cm.getMatchBlockIds();
        if (ids != null && ids.length > 0) {
            for (int i = 0; i < ids.length; ++i) {
                final int blockId = ids[i];
                if (blockId < 0) {
                    warn("Invalid block ID: " + blockId);
                }
                else {
                    addToList(cm, blockList, blockId);
                }
            }
        }
        else {
            warn("No match blocks: " + Config.arrayToString(ids));
        }
    }
    
    private static void addToList(final CustomColormap cm, final List list, final int id) {
        while (id >= list.size()) {
            list.add(null);
        }
        Object subList = list.get(id);
        if (subList == null) {
            subList = new ArrayList();
            list.set(id, subList);
        }
        ((List)subList).add(cm);
    }
    
    private static CustomColormap[][] blockListToArray(final List list) {
        final CustomColormap[][] colArr = new CustomColormap[list.size()][];
        for (int i = 0; i < list.size(); ++i) {
            final List subList = list.get(i);
            if (subList != null) {
                final CustomColormap[] subArr = subList.toArray(new CustomColormap[subList.size()]);
                colArr[i] = subArr;
            }
        }
        return colArr;
    }
    
    private static int readColor(final Properties props, final String[] names) {
        for (int i = 0; i < names.length; ++i) {
            final String name = names[i];
            final int col = readColor(props, name);
            if (col >= 0) {
                return col;
            }
        }
        return -1;
    }
    
    private static int readColor(final Properties props, final String name) {
        String str = props.getProperty(name);
        if (str == null) {
            return -1;
        }
        str = str.trim();
        final int color = parseColor(str);
        if (color < 0) {
            warn("Invalid color: " + name + " = " + str);
            return color;
        }
        dbg(String.valueOf(name) + " = " + str);
        return color;
    }
    
    private static int parseColor(String str) {
        if (str == null) {
            return -1;
        }
        str = str.trim();
        try {
            final int e = Integer.parseInt(str, 16) & 0xFFFFFF;
            return e;
        }
        catch (NumberFormatException var2) {
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
    
    private static CustomColormap getCustomColors(final String basePath, final String[] paths, final int width, final int height) {
        for (int i = 0; i < paths.length; ++i) {
            String path = paths[i];
            path = String.valueOf(basePath) + path;
            final CustomColormap cols = getCustomColors(path, width, height);
            if (cols != null) {
                return cols;
            }
        }
        return null;
    }
    
    public static CustomColormap getCustomColors(final String pathImage, final int width, final int height) {
        try {
            final ResourceLocation e = new ResourceLocation(pathImage);
            if (!Config.hasResource(e)) {
                return null;
            }
            dbg("Colormap " + pathImage);
            final Properties props = new Properties();
            String pathProps = StrUtils.replaceSuffix(pathImage, ".png", ".properties");
            final ResourceLocation locProps = new ResourceLocation(pathProps);
            if (Config.hasResource(locProps)) {
                final InputStream cm = Config.getResourceStream(locProps);
                props.load(cm);
                cm.close();
                dbg("Colormap properties: " + pathProps);
            }
            else {
                props.put("format", "vanilla");
                props.put("source", pathImage);
                pathProps = pathImage;
            }
            final CustomColormap cm2 = new CustomColormap(props, pathProps, width, height);
            return cm2.isValid(pathProps) ? cm2 : null;
        }
        catch (Exception var8) {
            var8.printStackTrace();
            return null;
        }
    }
    
    public static void updateUseDefaultGrassFoliageColors() {
        CustomColors.useDefaultGrassFoliageColors = (CustomColors.foliageBirchColors == null && CustomColors.foliagePineColors == null && CustomColors.swampGrassColors == null && CustomColors.swampFoliageColors == null && Config.isSwampColors() && Config.isSmoothBiomes());
    }
    
    public static int getColorMultiplier(final BakedQuad quad, final Block block, final IBlockAccess blockAccess, BlockPos blockPos, final RenderEnv renderEnv) {
        if (CustomColors.blockColormaps != null) {
            IBlockState metadata = renderEnv.getBlockState();
            if (!quad.func_178212_b()) {
                if (block == Blocks.grass) {
                    metadata = CustomColors.BLOCK_STATE_DIRT;
                }
                if (block == Blocks.redstone_wire) {
                    return -1;
                }
            }
            if (block == Blocks.double_plant && renderEnv.getMetadata() >= 8) {
                blockPos = blockPos.offsetDown();
                metadata = blockAccess.getBlockState(blockPos);
            }
            final CustomColormap colorizer = getBlockColormap(metadata);
            if (colorizer != null) {
                if (Config.isSmoothBiomes() && !colorizer.isColorConstant()) {
                    return getSmoothColorMultiplier(blockAccess, blockPos, colorizer, renderEnv.getColorizerBlockPosM());
                }
                return colorizer.getColor(blockAccess, blockPos);
            }
        }
        if (!quad.func_178212_b()) {
            return -1;
        }
        if (block == Blocks.waterlily) {
            return getLilypadColorMultiplier(blockAccess, blockPos);
        }
        if (block == Blocks.redstone_wire) {
            return getRedstoneColor(renderEnv.getBlockState());
        }
        if (block instanceof BlockStem) {
            return getStemColorMultiplier(block, blockAccess, blockPos, renderEnv);
        }
        if (CustomColors.useDefaultGrassFoliageColors) {
            return -1;
        }
        final int metadata2 = renderEnv.getMetadata();
        IColorizer colorizer2;
        if (block != Blocks.grass && block != Blocks.tallgrass && block != Blocks.double_plant) {
            if (block == Blocks.double_plant) {
                colorizer2 = CustomColors.COLORIZER_GRASS;
                if (metadata2 >= 8) {
                    blockPos = blockPos.offsetDown();
                }
            }
            else if (block == Blocks.leaves) {
                switch (metadata2 & 0x3) {
                    case 0: {
                        colorizer2 = CustomColors.COLORIZER_FOLIAGE;
                        break;
                    }
                    case 1: {
                        colorizer2 = CustomColors.COLORIZER_FOLIAGE_PINE;
                        break;
                    }
                    case 2: {
                        colorizer2 = CustomColors.COLORIZER_FOLIAGE_BIRCH;
                        break;
                    }
                    default: {
                        colorizer2 = CustomColors.COLORIZER_FOLIAGE;
                        break;
                    }
                }
            }
            else if (block == Blocks.leaves2) {
                colorizer2 = CustomColors.COLORIZER_FOLIAGE;
            }
            else {
                if (block != Blocks.vine) {
                    return -1;
                }
                colorizer2 = CustomColors.COLORIZER_FOLIAGE;
            }
        }
        else {
            colorizer2 = CustomColors.COLORIZER_GRASS;
        }
        return (Config.isSmoothBiomes() && !colorizer2.isColorConstant()) ? getSmoothColorMultiplier(blockAccess, blockPos, colorizer2, renderEnv.getColorizerBlockPosM()) : colorizer2.getColor(blockAccess, blockPos);
    }
    
    protected static BiomeGenBase getColorBiome(final IBlockAccess blockAccess, final BlockPos blockPos) {
        BiomeGenBase biome = blockAccess.getBiomeGenForCoords(blockPos);
        if (biome == BiomeGenBase.swampland && !Config.isSwampColors()) {
            biome = BiomeGenBase.plains;
        }
        return biome;
    }
    
    private static CustomColormap getBlockColormap(final IBlockState blockState) {
        if (CustomColors.blockColormaps == null) {
            return null;
        }
        if (!(blockState instanceof BlockStateBase)) {
            return null;
        }
        final BlockStateBase bs = (BlockStateBase)blockState;
        final int blockId = bs.getBlockId();
        if (blockId < 0 || blockId >= CustomColors.blockColormaps.length) {
            return null;
        }
        final CustomColormap[] cms = CustomColors.blockColormaps[blockId];
        if (cms == null) {
            return null;
        }
        for (int i = 0; i < cms.length; ++i) {
            final CustomColormap cm = cms[i];
            if (cm.matchesBlock(bs)) {
                return cm;
            }
        }
        return null;
    }
    
    private static int getSmoothColorMultiplier(final IBlockAccess blockAccess, final BlockPos blockPos, final IColorizer colorizer, final BlockPosM blockPosM) {
        int sumRed = 0;
        int sumGreen = 0;
        int sumBlue = 0;
        final int x = blockPos.getX();
        final int y = blockPos.getY();
        final int z = blockPos.getZ();
        final BlockPosM posM = blockPosM;
        for (int r = x - 1; r <= x + 1; ++r) {
            for (int g = z - 1; g <= z + 1; ++g) {
                posM.setXyz(r, y, g);
                final int b = colorizer.getColor(blockAccess, posM);
                sumRed += (b >> 16 & 0xFF);
                sumGreen += (b >> 8 & 0xFF);
                sumBlue += (b & 0xFF);
            }
        }
        int r = sumRed / 9;
        int g = sumGreen / 9;
        final int b = sumBlue / 9;
        return r << 16 | g << 8 | b;
    }
    
    public static int getFluidColor(final IBlockAccess blockAccess, final IBlockState blockState, final BlockPos blockPos, final RenderEnv renderEnv) {
        final Block block = blockState.getBlock();
        Object colorizer = getBlockColormap(blockState);
        if (colorizer == null && block.getMaterial() == Material.water) {
            colorizer = CustomColors.COLORIZER_WATER;
        }
        return (colorizer == null) ? block.colorMultiplier(blockAccess, blockPos) : ((Config.isSmoothBiomes() && !((IColorizer)colorizer).isColorConstant()) ? getSmoothColorMultiplier(blockAccess, blockPos, (IColorizer)colorizer, renderEnv.getColorizerBlockPosM()) : ((IColorizer)colorizer).getColor(blockAccess, blockPos));
    }
    
    public static void updatePortalFX(final EntityFX fx) {
        if (CustomColors.particlePortalColor >= 0) {
            final int col = CustomColors.particlePortalColor;
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
        if (CustomColors.myceliumParticleColors != null) {
            final int col = CustomColors.myceliumParticleColors.getColorRandom();
            final int red = col >> 16 & 0xFF;
            final int green = col >> 8 & 0xFF;
            final int blue = col & 0xFF;
            final float redF = red / 255.0f;
            final float greenF = green / 255.0f;
            final float blueF = blue / 255.0f;
            fx.setRBGColorF(redF, greenF, blueF);
        }
    }
    
    private static int getRedstoneColor(final IBlockState blockState) {
        if (CustomColors.redstoneColors == null) {
            return -1;
        }
        final int level = getRedstoneLevel(blockState, 15);
        final int col = CustomColors.redstoneColors.getColor(level);
        return col;
    }
    
    public static void updateReddustFX(final EntityFX fx, final IBlockAccess blockAccess, final double x, final double y, final double z) {
        if (CustomColors.redstoneColors != null) {
            final IBlockState state = blockAccess.getBlockState(new BlockPos(x, y, z));
            final int level = getRedstoneLevel(state, 15);
            final int col = CustomColors.redstoneColors.getColor(level);
            final int red = col >> 16 & 0xFF;
            final int green = col >> 8 & 0xFF;
            final int blue = col & 0xFF;
            final float redF = red / 255.0f;
            final float greenF = green / 255.0f;
            final float blueF = blue / 255.0f;
            fx.setRBGColorF(redF, greenF, blueF);
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
    
    public static int getXpOrbColor(final float timer) {
        if (CustomColors.xpOrbColors == null) {
            return -1;
        }
        final int index = (int)((MathHelper.sin(timer) + 1.0f) * (CustomColors.xpOrbColors.getLength() - 1) / 2.0);
        final int col = CustomColors.xpOrbColors.getColor(index);
        return col;
    }
    
    public static void updateWaterFX(final EntityFX fx, final IBlockAccess blockAccess, final double x, final double y, final double z) {
        if (CustomColors.waterColors != null || CustomColors.blockColormaps != null) {
            final BlockPos blockPos = new BlockPos(x, y, z);
            final RenderEnv renderEnv = RenderEnv.getInstance(blockAccess, CustomColors.BLOCK_STATE_WATER, blockPos);
            final int col = getFluidColor(blockAccess, CustomColors.BLOCK_STATE_WATER, blockPos, renderEnv);
            final int red = col >> 16 & 0xFF;
            final int green = col >> 8 & 0xFF;
            final int blue = col & 0xFF;
            float redF = red / 255.0f;
            float greenF = green / 255.0f;
            float blueF = blue / 255.0f;
            if (CustomColors.particleWaterColor >= 0) {
                final int redDrop = CustomColors.particleWaterColor >> 16 & 0xFF;
                final int greenDrop = CustomColors.particleWaterColor >> 8 & 0xFF;
                final int blueDrop = CustomColors.particleWaterColor & 0xFF;
                redF *= redDrop / 255.0f;
                greenF *= greenDrop / 255.0f;
                blueF *= blueDrop / 255.0f;
            }
            fx.setRBGColorF(redF, greenF, blueF);
        }
    }
    
    private static int getLilypadColorMultiplier(final IBlockAccess blockAccess, final BlockPos blockPos) {
        return (CustomColors.lilyPadColor < 0) ? Blocks.waterlily.colorMultiplier(blockAccess, blockPos) : CustomColors.lilyPadColor;
    }
    
    private static Vec3 getFogColorNether(final Vec3 col) {
        return (CustomColors.fogColorNether == null) ? col : CustomColors.fogColorNether;
    }
    
    private static Vec3 getFogColorEnd(final Vec3 col) {
        return (CustomColors.fogColorEnd == null) ? col : CustomColors.fogColorEnd;
    }
    
    private static Vec3 getSkyColorEnd(final Vec3 col) {
        return (CustomColors.skyColorEnd == null) ? col : CustomColors.skyColorEnd;
    }
    
    public static Vec3 getSkyColor(final Vec3 skyColor3d, final IBlockAccess blockAccess, final double x, final double y, final double z) {
        if (CustomColors.skyColors == null) {
            return skyColor3d;
        }
        final int col = CustomColors.skyColors.getColorSmooth(blockAccess, x, y, z, 3);
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
        final Vec3 newCol = CustomColors.skyColorFader.getColor(redF, greenF, blueF);
        return newCol;
    }
    
    private static Vec3 getFogColor(final Vec3 fogColor3d, final IBlockAccess blockAccess, final double x, final double y, final double z) {
        if (CustomColors.fogColors == null) {
            return fogColor3d;
        }
        final int col = CustomColors.fogColors.getColorSmooth(blockAccess, x, y, z, 3);
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
        final Vec3 newCol = CustomColors.fogColorFader.getColor(redF, greenF, blueF);
        return newCol;
    }
    
    public static Vec3 getUnderwaterColor(final IBlockAccess blockAccess, final double x, final double y, final double z) {
        if (CustomColors.underwaterColors == null) {
            return null;
        }
        final int col = CustomColors.underwaterColors.getColorSmooth(blockAccess, x, y, z, 3);
        final int red = col >> 16 & 0xFF;
        final int green = col >> 8 & 0xFF;
        final int blue = col & 0xFF;
        final float redF = red / 255.0f;
        final float greenF = green / 255.0f;
        final float blueF = blue / 255.0f;
        final Vec3 newCol = CustomColors.underwaterColorFader.getColor(redF, greenF, blueF);
        return newCol;
    }
    
    private static int getStemColorMultiplier(final Block blockStem, final IBlockAccess blockAccess, final BlockPos blockPos, final RenderEnv renderEnv) {
        CustomColormap colors = CustomColors.stemColors;
        if (blockStem == Blocks.pumpkin_stem && CustomColors.stemPumpkinColors != null) {
            colors = CustomColors.stemPumpkinColors;
        }
        if (blockStem == Blocks.melon_stem && CustomColors.stemMelonColors != null) {
            colors = CustomColors.stemMelonColors;
        }
        if (colors == null) {
            return -1;
        }
        final int level = renderEnv.getMetadata();
        return colors.getColor(level);
    }
    
    public static boolean updateLightmap(final World world, final float torchFlickerX, final int[] lmColors, final boolean nightvision) {
        if (world == null) {
            return false;
        }
        if (CustomColors.lightMapsColorsRgb == null) {
            return false;
        }
        final int dimensionId = world.provider.getDimensionId();
        final int lightMapIndex = dimensionId - CustomColors.lightmapMinDimensionId;
        if (lightMapIndex < 0 || lightMapIndex >= CustomColors.lightMapsColorsRgb.length) {
            return false;
        }
        final CustomColormap lightMapRgb = CustomColors.lightMapsColorsRgb[lightMapIndex];
        if (lightMapRgb == null) {
            return false;
        }
        final int height = lightMapRgb.getHeight();
        if (nightvision && height < 64) {
            return false;
        }
        final int width = lightMapRgb.getWidth();
        if (width < 16) {
            warn("Invalid lightmap width: " + width + " for dimension: " + dimensionId);
            CustomColors.lightMapsColorsRgb[lightMapIndex] = null;
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
        final float[][] colorsRgb = lightMapRgb.getColorsRgb();
        getLightMapColumn(colorsRgb, sunX, startIndex, width, CustomColors.sunRgbs);
        getLightMapColumn(colorsRgb, torchX, startIndex + 16 * width, width, CustomColors.torchRgbs);
        final float[] rgb = new float[3];
        for (int is = 0; is < 16; ++is) {
            for (int it = 0; it < 16; ++it) {
                for (int r = 0; r < 3; ++r) {
                    float g = Config.limitTo1(CustomColors.sunRgbs[is][r] + CustomColors.torchRgbs[it][r]);
                    if (hasGamma) {
                        float b = 1.0f - g;
                        b = 1.0f - b * b * b * b;
                        g = gamma * b + (1.0f - gamma) * g;
                    }
                    rgb[r] = g;
                }
                int r = (int)(rgb[0] * 255.0f);
                final int var22 = (int)(rgb[1] * 255.0f);
                final int var23 = (int)(rgb[2] * 255.0f);
                lmColors[is * 16 + it] = (0xFF000000 | r << 16 | var22 << 8 | var23);
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
    
    private static int[] readSpawnEggColors(final Properties props, final String fileName, final String prefix, final String logName) {
        final ArrayList list = new ArrayList();
        final Set keys = props.keySet();
        int countColors = 0;
        for (final String i : keys) {
            final String value = props.getProperty(i);
            if (i.startsWith(prefix)) {
                final String name = StrUtils.removePrefix(i, prefix);
                final int id = getEntityId(name);
                final int color = parseColor(value);
                if (id >= 0 && color >= 0) {
                    while (list.size() <= id) {
                        list.add(-1);
                    }
                    list.set(id, color);
                    ++countColors;
                }
                else {
                    warn("Invalid spawn egg color: " + i + " = " + value);
                }
            }
        }
        if (countColors <= 0) {
            return null;
        }
        dbg(String.valueOf(logName) + " colors: " + countColors);
        final int[] var13 = new int[list.size()];
        for (int var14 = 0; var14 < var13.length; ++var14) {
            var13[var14] = list.get(var14);
        }
        return var13;
    }
    
    private static int getSpawnEggColor(final ItemMonsterPlacer item, final ItemStack itemStack, final int layer, final int color) {
        final int id = itemStack.getMetadata();
        final int[] eggColors = (layer == 0) ? CustomColors.spawnEggPrimaryColors : CustomColors.spawnEggSecondaryColors;
        if (eggColors == null) {
            return color;
        }
        if (id >= 0 && id < eggColors.length) {
            final int eggColor = eggColors[id];
            return (eggColor < 0) ? color : eggColor;
        }
        return color;
    }
    
    public static int getColorFromItemStack(final ItemStack itemStack, final int layer, final int color) {
        if (itemStack == null) {
            return color;
        }
        final Item item = itemStack.getItem();
        return (item == null) ? color : ((item instanceof ItemMonsterPlacer) ? getSpawnEggColor((ItemMonsterPlacer)item, itemStack, layer, color) : color);
    }
    
    private static float[][] readDyeColors(final Properties props, final String fileName, final String prefix, final String logName) {
        final EnumDyeColor[] dyeValues = EnumDyeColor.values();
        final HashMap mapDyes = new HashMap();
        for (int colors = 0; colors < dyeValues.length; ++colors) {
            final EnumDyeColor countColors = dyeValues[colors];
            mapDyes.put(countColors.getName(), countColors);
        }
        final float[][] var16 = new float[dyeValues.length][];
        int var17 = 0;
        final Set keys = props.keySet();
        for (final String key : keys) {
            final String value = props.getProperty(key);
            if (key.startsWith(prefix)) {
                String name = StrUtils.removePrefix(key, prefix);
                if (name.equals("lightBlue")) {
                    name = "light_blue";
                }
                final EnumDyeColor dye = mapDyes.get(name);
                final int color = parseColor(value);
                if (dye != null && color >= 0) {
                    final float[] rgb = { (color >> 16 & 0xFF) / 255.0f, (color >> 8 & 0xFF) / 255.0f, (color & 0xFF) / 255.0f };
                    var16[dye.ordinal()] = rgb;
                    ++var17;
                }
                else {
                    warn("Invalid color: " + key + " = " + value);
                }
            }
        }
        if (var17 <= 0) {
            return null;
        }
        dbg(String.valueOf(logName) + " colors: " + var17);
        return var16;
    }
    
    private static float[] getDyeColors(final EnumDyeColor dye, final float[][] dyeColors, final float[] colors) {
        if (dyeColors == null) {
            return colors;
        }
        if (dye == null) {
            return colors;
        }
        final float[] customColors = dyeColors[dye.ordinal()];
        return (customColors == null) ? colors : customColors;
    }
    
    public static float[] getWolfCollarColors(final EnumDyeColor dye, final float[] colors) {
        return getDyeColors(dye, CustomColors.wolfCollarColors, colors);
    }
    
    public static float[] getSheepColors(final EnumDyeColor dye, final float[] colors) {
        return getDyeColors(dye, CustomColors.sheepColors, colors);
    }
    
    private static int[] readTextColors(final Properties props, final String fileName, final String prefix, final String logName) {
        final int[] colors = new int[32];
        Arrays.fill(colors, -1);
        int countColors = 0;
        final Set keys = props.keySet();
        for (final String key : keys) {
            final String value = props.getProperty(key);
            if (key.startsWith(prefix)) {
                final String name = StrUtils.removePrefix(key, prefix);
                final int code = Config.parseInt(name, -1);
                final int color = parseColor(value);
                if (code >= 0 && code < colors.length && color >= 0) {
                    colors[code] = color;
                    ++countColors;
                }
                else {
                    warn("Invalid color: " + key + " = " + value);
                }
            }
        }
        if (countColors <= 0) {
            return null;
        }
        dbg(String.valueOf(logName) + " colors: " + countColors);
        return colors;
    }
    
    public static int getTextColor(final int index, final int color) {
        if (CustomColors.textColors == null) {
            return color;
        }
        if (index >= 0 && index < CustomColors.textColors.length) {
            final int customColor = CustomColors.textColors[index];
            return (customColor < 0) ? color : customColor;
        }
        return color;
    }
    
    private static int[] readMapColors(final Properties props, final String fileName, final String prefix, final String logName) {
        final int[] colors = new int[MapColor.mapColorArray.length];
        Arrays.fill(colors, -1);
        int countColors = 0;
        final Set keys = props.keySet();
        for (final String key : keys) {
            final String value = props.getProperty(key);
            if (key.startsWith(prefix)) {
                final String name = StrUtils.removePrefix(key, prefix);
                final int index = getMapColorIndex(name);
                final int color = parseColor(value);
                if (index >= 0 && index < colors.length && color >= 0) {
                    colors[index] = color;
                    ++countColors;
                }
                else {
                    warn("Invalid color: " + key + " = " + value);
                }
            }
        }
        if (countColors <= 0) {
            return null;
        }
        dbg(String.valueOf(logName) + " colors: " + countColors);
        return colors;
    }
    
    private static int[] readPotionColors(final Properties props, final String fileName, final String prefix, final String logName) {
        final int[] colors = new int[Potion.potionTypes.length];
        Arrays.fill(colors, -1);
        int countColors = 0;
        final Set keys = props.keySet();
        for (final String key : keys) {
            final String value = props.getProperty(key);
            if (key.startsWith(prefix)) {
                final int index = getPotionId(key);
                final int color = parseColor(value);
                if (index >= 0 && index < colors.length && color >= 0) {
                    colors[index] = color;
                    ++countColors;
                }
                else {
                    warn("Invalid color: " + key + " = " + value);
                }
            }
        }
        if (countColors <= 0) {
            return null;
        }
        dbg(String.valueOf(logName) + " colors: " + countColors);
        return colors;
    }
    
    private static int getPotionId(final String name) {
        if (name.equals("potion.water")) {
            return 0;
        }
        final Potion[] potions = Potion.potionTypes;
        for (int i = 0; i < potions.length; ++i) {
            final Potion potion = potions[i];
            if (potion != null && potion.getName().equals(name)) {
                return potion.getId();
            }
        }
        return -1;
    }
    
    public static int getPotionColor(final int potionId, final int color) {
        if (CustomColors.potionColors == null) {
            return color;
        }
        if (potionId >= 0 && potionId < CustomColors.potionColors.length) {
            final int potionColor = CustomColors.potionColors[potionId];
            return (potionColor < 0) ? color : potionColor;
        }
        return color;
    }
    
    private static int getMapColorIndex(final String name) {
        return (name == null) ? -1 : (name.equals("air") ? MapColor.airColor.colorIndex : (name.equals("grass") ? MapColor.grassColor.colorIndex : (name.equals("sand") ? MapColor.sandColor.colorIndex : (name.equals("cloth") ? MapColor.clothColor.colorIndex : (name.equals("tnt") ? MapColor.tntColor.colorIndex : (name.equals("ice") ? MapColor.iceColor.colorIndex : (name.equals("iron") ? MapColor.ironColor.colorIndex : (name.equals("foliage") ? MapColor.foliageColor.colorIndex : (name.equals("snow") ? MapColor.snowColor.colorIndex : (name.equals("clay") ? MapColor.clayColor.colorIndex : (name.equals("dirt") ? MapColor.dirtColor.colorIndex : (name.equals("stone") ? MapColor.stoneColor.colorIndex : (name.equals("water") ? MapColor.waterColor.colorIndex : (name.equals("wood") ? MapColor.woodColor.colorIndex : (name.equals("quartz") ? MapColor.quartzColor.colorIndex : (name.equals("adobe") ? MapColor.adobeColor.colorIndex : (name.equals("magenta") ? MapColor.magentaColor.colorIndex : (name.equals("lightBlue") ? MapColor.lightBlueColor.colorIndex : (name.equals("light_blue") ? MapColor.lightBlueColor.colorIndex : (name.equals("yellow") ? MapColor.yellowColor.colorIndex : (name.equals("lime") ? MapColor.limeColor.colorIndex : (name.equals("pink") ? MapColor.pinkColor.colorIndex : (name.equals("gray") ? MapColor.grayColor.colorIndex : (name.equals("silver") ? MapColor.silverColor.colorIndex : (name.equals("cyan") ? MapColor.cyanColor.colorIndex : (name.equals("purple") ? MapColor.purpleColor.colorIndex : (name.equals("blue") ? MapColor.blueColor.colorIndex : (name.equals("brown") ? MapColor.brownColor.colorIndex : (name.equals("green") ? MapColor.greenColor.colorIndex : (name.equals("red") ? MapColor.redColor.colorIndex : (name.equals("black") ? MapColor.blackColor.colorIndex : (name.equals("gold") ? MapColor.goldColor.colorIndex : (name.equals("diamond") ? MapColor.diamondColor.colorIndex : (name.equals("lapis") ? MapColor.lapisColor.colorIndex : (name.equals("emerald") ? MapColor.emeraldColor.colorIndex : (name.equals("obsidian") ? MapColor.obsidianColor.colorIndex : (name.equals("netherrack") ? MapColor.netherrackColor.colorIndex : -1)))))))))))))))))))))))))))))))))))));
    }
    
    private static int[] getMapColors() {
        final MapColor[] mapColors = MapColor.mapColorArray;
        final int[] colors = new int[mapColors.length];
        Arrays.fill(colors, -1);
        for (int i = 0; i < mapColors.length && i < colors.length; ++i) {
            final MapColor mapColor = mapColors[i];
            if (mapColor != null) {
                colors[i] = mapColor.colorValue;
            }
        }
        return colors;
    }
    
    private static void setMapColors(final int[] colors) {
        if (colors != null) {
            final MapColor[] mapColors = MapColor.mapColorArray;
            for (int i = 0; i < mapColors.length && i < colors.length; ++i) {
                final MapColor mapColor = mapColors[i];
                if (mapColor != null) {
                    final int color = colors[i];
                    if (color >= 0) {
                        mapColor.colorValue = color;
                    }
                }
            }
        }
    }
    
    private static int getEntityId(final String name) {
        if (name == null) {
            return -1;
        }
        final int id = EntityList.func_180122_a(name);
        if (id < 0) {
            return -1;
        }
        final String idName = EntityList.getStringFromID(id);
        return Config.equals(name, idName) ? id : -1;
    }
    
    private static void dbg(final String str) {
        Config.dbg("CustomColors: " + str);
    }
    
    private static void warn(final String str) {
        Config.warn("CustomColors: " + str);
    }
    
    public static int getExpBarTextColor(final int color) {
        return (CustomColors.expBarTextColor < 0) ? color : CustomColors.expBarTextColor;
    }
    
    public static int getBossTextColor(final int color) {
        return (CustomColors.bossTextColor < 0) ? color : CustomColors.bossTextColor;
    }
    
    public static int getSignTextColor(final int color) {
        return (CustomColors.signTextColor < 0) ? color : CustomColors.signTextColor;
    }
    
    public interface IColorizer
    {
        int getColor(final IBlockAccess p0, final BlockPos p1);
        
        boolean isColorConstant();
    }
}
