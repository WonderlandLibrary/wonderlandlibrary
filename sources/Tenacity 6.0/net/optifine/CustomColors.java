// 
// Decompiled by Procyon v0.5.36
// 

package net.optifine;

import net.minecraft.world.ColorizerFoliage;
import net.minecraft.potion.Potion;
import net.minecraft.block.material.MapColor;
import net.minecraft.item.EnumDyeColor;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemMonsterPlacer;
import net.optifine.util.EntityUtils;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.Entity;
import net.minecraft.world.World;
import net.minecraft.util.MathHelper;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.BlockRedstoneWire;
import net.minecraft.client.particle.EntityFX;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.BlockStateBase;
import net.minecraft.world.biome.BiomeGenBase;
import net.minecraft.block.Block;
import net.minecraft.block.BlockStem;
import net.minecraft.init.Blocks;
import net.optifine.render.RenderEnv;
import net.minecraft.util.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.client.renderer.block.model.BakedQuad;
import net.optifine.config.MatchBlock;
import java.util.Iterator;
import net.optifine.config.ConnectedParser;
import net.optifine.util.TextureUtils;
import java.util.ArrayList;
import java.awt.image.BufferedImage;
import javax.imageio.ImageIO;
import java.util.Set;
import java.util.Map;
import org.apache.commons.lang3.tuple.ImmutablePair;
import net.optifine.util.StrUtils;
import java.util.HashMap;
import net.optifine.util.ResUtils;
import java.util.List;
import java.util.Properties;
import java.io.InputStream;
import java.io.IOException;
import java.io.FileNotFoundException;
import java.util.Arrays;
import net.optifine.util.PropertiesOrdered;
import net.minecraft.util.ResourceLocation;
import org.apache.commons.lang3.tuple.Pair;
import net.minecraft.src.Config;
import java.util.Random;
import net.minecraft.block.state.IBlockState;
import net.minecraft.util.Vec3;

public class CustomColors
{
    private static String paletteFormatDefault;
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
    private static CustomColormap underlavaColors;
    private static CustomColorFader underlavaColorFader;
    private static LightMapPack[] lightMapPacks;
    private static int lightmapMinDimensionId;
    private static CustomColormap redstoneColors;
    private static CustomColormap xpOrbColors;
    private static int xpOrbTime;
    private static CustomColormap durabilityColors;
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
    
    public static void update() {
        CustomColors.paletteFormatDefault = "vanilla";
        CustomColors.waterColors = null;
        CustomColors.foliageBirchColors = null;
        CustomColors.foliagePineColors = null;
        CustomColors.swampGrassColors = null;
        CustomColors.swampFoliageColors = null;
        CustomColors.skyColors = null;
        CustomColors.fogColors = null;
        CustomColors.underwaterColors = null;
        CustomColors.underlavaColors = null;
        CustomColors.redstoneColors = null;
        CustomColors.xpOrbColors = null;
        CustomColors.xpOrbTime = -1;
        CustomColors.durabilityColors = null;
        CustomColors.stemColors = null;
        CustomColors.myceliumParticleColors = null;
        CustomColors.lightMapPacks = null;
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
        CustomColors.paletteFormatDefault = getValidProperty("mcpatcher/color.properties", "palette.format", CustomColormap.FORMAT_STRINGS, "vanilla");
        final String s = "mcpatcher/colormap/";
        final String[] astring = { "water.png", "watercolorX.png" };
        CustomColors.waterColors = getCustomColors(s, astring, 256, 256);
        updateUseDefaultGrassFoliageColors();
        if (Config.isCustomColors()) {
            final String[] astring2 = { "pine.png", "pinecolor.png" };
            CustomColors.foliagePineColors = getCustomColors(s, astring2, 256, 256);
            final String[] astring3 = { "birch.png", "birchcolor.png" };
            CustomColors.foliageBirchColors = getCustomColors(s, astring3, 256, 256);
            final String[] astring4 = { "swampgrass.png", "swampgrasscolor.png" };
            CustomColors.swampGrassColors = getCustomColors(s, astring4, 256, 256);
            final String[] astring5 = { "swampfoliage.png", "swampfoliagecolor.png" };
            CustomColors.swampFoliageColors = getCustomColors(s, astring5, 256, 256);
            final String[] astring6 = { "sky0.png", "skycolor0.png" };
            CustomColors.skyColors = getCustomColors(s, astring6, 256, 256);
            final String[] astring7 = { "fog0.png", "fogcolor0.png" };
            CustomColors.fogColors = getCustomColors(s, astring7, 256, 256);
            final String[] astring8 = { "underwater.png", "underwatercolor.png" };
            CustomColors.underwaterColors = getCustomColors(s, astring8, 256, 256);
            final String[] astring9 = { "underlava.png", "underlavacolor.png" };
            CustomColors.underlavaColors = getCustomColors(s, astring9, 256, 256);
            final String[] astring10 = { "redstone.png", "redstonecolor.png" };
            CustomColors.redstoneColors = getCustomColors(s, astring10, 16, 1);
            CustomColors.xpOrbColors = getCustomColors(s + "xporb.png", -1, -1);
            CustomColors.durabilityColors = getCustomColors(s + "durability.png", -1, -1);
            final String[] astring11 = { "stem.png", "stemcolor.png" };
            CustomColors.stemColors = getCustomColors(s, astring11, 8, 1);
            CustomColors.stemPumpkinColors = getCustomColors(s + "pumpkinstem.png", 8, 1);
            CustomColors.stemMelonColors = getCustomColors(s + "melonstem.png", 8, 1);
            final String[] astring12 = { "myceliumparticle.png", "myceliumparticlecolor.png" };
            CustomColors.myceliumParticleColors = getCustomColors(s, astring12, -1, -1);
            final Pair<LightMapPack[], Integer> pair = parseLightMapPacks();
            CustomColors.lightMapPacks = (LightMapPack[])pair.getLeft();
            CustomColors.lightmapMinDimensionId = (int)pair.getRight();
            readColorProperties("mcpatcher/color.properties");
            CustomColors.blockColormaps = readBlockColormaps(new String[] { s + "custom/", s + "blocks/" }, CustomColors.colorsBlockColormaps, 256, 256);
            updateUseDefaultGrassFoliageColors();
        }
    }
    
    private static String getValidProperty(final String fileName, final String key, final String[] validValues, final String valDef) {
        try {
            final ResourceLocation resourcelocation = new ResourceLocation(fileName);
            final InputStream inputstream = Config.getResourceStream(resourcelocation);
            if (inputstream == null) {
                return valDef;
            }
            final Properties properties = new PropertiesOrdered();
            properties.load(inputstream);
            inputstream.close();
            final String s = properties.getProperty(key);
            if (s == null) {
                return valDef;
            }
            final List<String> list = Arrays.asList(validValues);
            if (!list.contains(s)) {
                warn("Invalid value: " + key + "=" + s);
                warn("Expected values: " + Config.arrayToString(validValues));
                return valDef;
            }
            dbg("" + key + "=" + s);
            return s;
        }
        catch (FileNotFoundException var9) {
            return valDef;
        }
        catch (IOException ioexception) {
            ioexception.printStackTrace();
            return valDef;
        }
    }
    
    private static Pair<LightMapPack[], Integer> parseLightMapPacks() {
        final String s = "mcpatcher/lightmap/world";
        final String s2 = ".png";
        final String[] astring = ResUtils.collectFiles(s, s2);
        final Map<Integer, String> map = new HashMap<Integer, String>();
        for (int i = 0; i < astring.length; ++i) {
            final String s3 = astring[i];
            final String s4 = StrUtils.removePrefixSuffix(s3, s, s2);
            final int j = Config.parseInt(s4, Integer.MIN_VALUE);
            if (j == Integer.MIN_VALUE) {
                warn("Invalid dimension ID: " + s4 + ", path: " + s3);
            }
            else {
                map.put(j, s3);
            }
        }
        final Set<Integer> set = map.keySet();
        final Integer[] ainteger = set.toArray(new Integer[set.size()]);
        Arrays.sort(ainteger);
        if (ainteger.length <= 0) {
            return (Pair<LightMapPack[], Integer>)new ImmutablePair((Object)null, (Object)0);
        }
        final int j2 = ainteger[0];
        final int k1 = ainteger[ainteger.length - 1];
        final int l = k1 - j2 + 1;
        final CustomColormap[] acustomcolormap = new CustomColormap[l];
        for (int m = 0; m < ainteger.length; ++m) {
            final Integer integer = ainteger[m];
            final String s5 = map.get(integer);
            final CustomColormap customcolormap = getCustomColors(s5, -1, -1);
            if (customcolormap != null) {
                if (customcolormap.getWidth() < 16) {
                    warn("Invalid lightmap width: " + customcolormap.getWidth() + ", path: " + s5);
                }
                else {
                    final int i2 = integer - j2;
                    acustomcolormap[i2] = customcolormap;
                }
            }
        }
        final LightMapPack[] alightmappack = new LightMapPack[acustomcolormap.length];
        for (int l2 = 0; l2 < acustomcolormap.length; ++l2) {
            final CustomColormap customcolormap2 = acustomcolormap[l2];
            if (customcolormap2 != null) {
                final String s6 = customcolormap2.name;
                final String s7 = customcolormap2.basePath;
                final CustomColormap customcolormap3 = getCustomColors(s7 + "/" + s6 + "_rain.png", -1, -1);
                final CustomColormap customcolormap4 = getCustomColors(s7 + "/" + s6 + "_thunder.png", -1, -1);
                final LightMap lightmap = new LightMap(customcolormap2);
                final LightMap lightmap2 = (customcolormap3 != null) ? new LightMap(customcolormap3) : null;
                final LightMap lightmap3 = (customcolormap4 != null) ? new LightMap(customcolormap4) : null;
                final LightMapPack lightmappack = new LightMapPack(lightmap, lightmap2, lightmap3);
                alightmappack[l2] = lightmappack;
            }
        }
        return (Pair<LightMapPack[], Integer>)new ImmutablePair((Object)alightmappack, (Object)j2);
    }
    
    private static int getTextureHeight(final String path, final int defHeight) {
        try {
            final InputStream inputstream = Config.getResourceStream(new ResourceLocation(path));
            if (inputstream == null) {
                return defHeight;
            }
            final BufferedImage bufferedimage = ImageIO.read(inputstream);
            inputstream.close();
            return (bufferedimage == null) ? defHeight : bufferedimage.getHeight();
        }
        catch (IOException var4) {
            return defHeight;
        }
    }
    
    private static void readColorProperties(final String fileName) {
        try {
            final ResourceLocation resourcelocation = new ResourceLocation(fileName);
            final InputStream inputstream = Config.getResourceStream(resourcelocation);
            if (inputstream == null) {
                return;
            }
            dbg("Loading " + fileName);
            final Properties properties = new PropertiesOrdered();
            properties.load(inputstream);
            inputstream.close();
            CustomColors.particleWaterColor = readColor(properties, new String[] { "particle.water", "drop.water" });
            CustomColors.particlePortalColor = readColor(properties, "particle.portal");
            CustomColors.lilyPadColor = readColor(properties, "lilypad");
            CustomColors.expBarTextColor = readColor(properties, "text.xpbar");
            CustomColors.bossTextColor = readColor(properties, "text.boss");
            CustomColors.signTextColor = readColor(properties, "text.sign");
            CustomColors.fogColorNether = readColorVec3(properties, "fog.nether");
            CustomColors.fogColorEnd = readColorVec3(properties, "fog.end");
            CustomColors.skyColorEnd = readColorVec3(properties, "sky.end");
            CustomColors.colorsBlockColormaps = readCustomColormaps(properties, fileName);
            CustomColors.spawnEggPrimaryColors = readSpawnEggColors(properties, fileName, "egg.shell.", "Spawn egg shell");
            CustomColors.spawnEggSecondaryColors = readSpawnEggColors(properties, fileName, "egg.spots.", "Spawn egg spot");
            CustomColors.wolfCollarColors = readDyeColors(properties, fileName, "collar.", "Wolf collar");
            CustomColors.sheepColors = readDyeColors(properties, fileName, "sheep.", "Sheep");
            CustomColors.textColors = readTextColors(properties, fileName, "text.code.", "Text");
            final int[] aint = readMapColors(properties, fileName, "map.", "Map");
            if (aint != null) {
                if (CustomColors.mapColorsOriginal == null) {
                    CustomColors.mapColorsOriginal = getMapColors();
                }
                setMapColors(aint);
            }
            CustomColors.potionColors = readPotionColors(properties, fileName, "potion.", "Potion");
            CustomColors.xpOrbTime = Config.parseInt(properties.getProperty("xporb.time"), -1);
        }
        catch (FileNotFoundException var5) {}
        catch (IOException ioexception) {
            ioexception.printStackTrace();
        }
    }
    
    private static CustomColormap[] readCustomColormaps(final Properties props, final String fileName) {
        final List list = new ArrayList();
        final String s = "palette.block.";
        final Map map = new HashMap();
        for (final Object e : props.keySet()) {
            final String s2 = (String)e;
            final String s3 = props.getProperty(s2);
            if (s2.startsWith(s)) {
                map.put(s2, s3);
            }
        }
        final String[] astring = (String[])map.keySet().toArray(new String[map.size()]);
        for (int j = 0; j < astring.length; ++j) {
            final String s4 = astring[j];
            final String s5 = props.getProperty(s4);
            dbg("Block palette: " + s4 + " = " + s5);
            String s6 = s4.substring(s.length());
            final String s7 = TextureUtils.getBasePath(fileName);
            s6 = TextureUtils.fixResourcePath(s6, s7);
            final CustomColormap customcolormap = getCustomColors(s6, 256, 256);
            if (customcolormap == null) {
                warn("Colormap not found: " + s6);
            }
            else {
                final ConnectedParser connectedparser = new ConnectedParser("CustomColors");
                final MatchBlock[] amatchblock = connectedparser.parseMatchBlocks(s5);
                if (amatchblock != null && amatchblock.length > 0) {
                    for (int i = 0; i < amatchblock.length; ++i) {
                        final MatchBlock matchblock = amatchblock[i];
                        customcolormap.addMatchBlock(matchblock);
                    }
                    list.add(customcolormap);
                }
                else {
                    warn("Invalid match blocks: " + s5);
                }
            }
        }
        if (list.size() <= 0) {
            return null;
        }
        final CustomColormap[] acustomcolormap = list.toArray(new CustomColormap[list.size()]);
        return acustomcolormap;
    }
    
    private static CustomColormap[][] readBlockColormaps(final String[] basePaths, final CustomColormap[] basePalettes, final int width, final int height) {
        final String[] astring = ResUtils.collectFiles(basePaths, new String[] { ".properties" });
        Arrays.sort(astring);
        final List list = new ArrayList();
        for (int i = 0; i < astring.length; ++i) {
            final String s = astring[i];
            dbg("Block colormap: " + s);
            try {
                final ResourceLocation resourcelocation = new ResourceLocation("minecraft", s);
                final InputStream inputstream = Config.getResourceStream(resourcelocation);
                if (inputstream == null) {
                    warn("File not found: " + s);
                }
                else {
                    final Properties properties = new PropertiesOrdered();
                    properties.load(inputstream);
                    inputstream.close();
                    final CustomColormap customcolormap = new CustomColormap(properties, s, width, height, CustomColors.paletteFormatDefault);
                    if (customcolormap.isValid(s) && customcolormap.isValidMatchBlocks(s)) {
                        addToBlockList(customcolormap, list);
                    }
                }
            }
            catch (FileNotFoundException var12) {
                warn("File not found: " + s);
            }
            catch (Exception exception) {
                exception.printStackTrace();
            }
        }
        if (basePalettes != null) {
            for (int j = 0; j < basePalettes.length; ++j) {
                final CustomColormap customcolormap2 = basePalettes[j];
                addToBlockList(customcolormap2, list);
            }
        }
        if (list.size() <= 0) {
            return null;
        }
        final CustomColormap[][] acustomcolormap = blockListToArray(list);
        return acustomcolormap;
    }
    
    private static void addToBlockList(final CustomColormap cm, final List blockList) {
        final int[] aint = cm.getMatchBlockIds();
        if (aint != null && aint.length > 0) {
            for (int i = 0; i < aint.length; ++i) {
                final int j = aint[i];
                if (j < 0) {
                    warn("Invalid block ID: " + j);
                }
                else {
                    addToList(cm, blockList, j);
                }
            }
        }
        else {
            warn("No match blocks: " + Config.arrayToString(aint));
        }
    }
    
    private static void addToList(final CustomColormap cm, final List list, final int id) {
        while (id >= list.size()) {
            list.add(null);
        }
        List list2 = list.get(id);
        if (list2 == null) {
            list2 = new ArrayList();
            list.set(id, list2);
        }
        list2.add(cm);
    }
    
    private static CustomColormap[][] blockListToArray(final List list) {
        final CustomColormap[][] acustomcolormap = new CustomColormap[list.size()][];
        for (int i = 0; i < list.size(); ++i) {
            final List list2 = list.get(i);
            if (list2 != null) {
                final CustomColormap[] acustomcolormap2 = list2.toArray(new CustomColormap[list2.size()]);
                acustomcolormap[i] = acustomcolormap2;
            }
        }
        return acustomcolormap;
    }
    
    private static int readColor(final Properties props, final String[] names) {
        for (int i = 0; i < names.length; ++i) {
            final String s = names[i];
            final int j = readColor(props, s);
            if (j >= 0) {
                return j;
            }
        }
        return -1;
    }
    
    private static int readColor(final Properties props, final String name) {
        String s = props.getProperty(name);
        if (s == null) {
            return -1;
        }
        s = s.trim();
        final int i = parseColor(s);
        if (i < 0) {
            warn("Invalid color: " + name + " = " + s);
            return i;
        }
        dbg(name + " = " + s);
        return i;
    }
    
    private static int parseColor(String str) {
        if (str == null) {
            return -1;
        }
        str = str.trim();
        try {
            final int i = Integer.parseInt(str, 16) & 0xFFFFFF;
            return i;
        }
        catch (NumberFormatException var2) {
            return -1;
        }
    }
    
    private static Vec3 readColorVec3(final Properties props, final String name) {
        final int i = readColor(props, name);
        if (i < 0) {
            return null;
        }
        final int j = i >> 16 & 0xFF;
        final int k = i >> 8 & 0xFF;
        final int l = i & 0xFF;
        final float f = j / 255.0f;
        final float f2 = k / 255.0f;
        final float f3 = l / 255.0f;
        return new Vec3(f, f2, f3);
    }
    
    private static CustomColormap getCustomColors(final String basePath, final String[] paths, final int width, final int height) {
        for (int i = 0; i < paths.length; ++i) {
            String s = paths[i];
            s = basePath + s;
            final CustomColormap customcolormap = getCustomColors(s, width, height);
            if (customcolormap != null) {
                return customcolormap;
            }
        }
        return null;
    }
    
    public static CustomColormap getCustomColors(final String pathImage, final int width, final int height) {
        try {
            final ResourceLocation resourcelocation = new ResourceLocation(pathImage);
            if (!Config.hasResource(resourcelocation)) {
                return null;
            }
            dbg("Colormap " + pathImage);
            final Properties properties = new PropertiesOrdered();
            String s = StrUtils.replaceSuffix(pathImage, ".png", ".properties");
            final ResourceLocation resourcelocation2 = new ResourceLocation(s);
            if (Config.hasResource(resourcelocation2)) {
                final InputStream inputstream = Config.getResourceStream(resourcelocation2);
                properties.load(inputstream);
                inputstream.close();
                dbg("Colormap properties: " + s);
            }
            else {
                properties.put("format", CustomColors.paletteFormatDefault);
                properties.put("source", pathImage);
                s = pathImage;
            }
            final CustomColormap customcolormap = new CustomColormap(properties, s, width, height, CustomColors.paletteFormatDefault);
            return customcolormap.isValid(s) ? customcolormap : null;
        }
        catch (Exception exception) {
            exception.printStackTrace();
            return null;
        }
    }
    
    public static void updateUseDefaultGrassFoliageColors() {
        CustomColors.useDefaultGrassFoliageColors = (CustomColors.foliageBirchColors == null && CustomColors.foliagePineColors == null && CustomColors.swampGrassColors == null && CustomColors.swampFoliageColors == null && Config.isSwampColors() && Config.isSmoothBiomes());
    }
    
    public static int getColorMultiplier(final BakedQuad quad, final IBlockState blockState, final IBlockAccess blockAccess, BlockPos blockPos, final RenderEnv renderEnv) {
        final Block block = blockState.getBlock();
        IBlockState iblockstate = renderEnv.getBlockState();
        if (CustomColors.blockColormaps != null) {
            if (!quad.hasTintIndex()) {
                if (block == Blocks.grass) {
                    iblockstate = CustomColors.BLOCK_STATE_DIRT;
                }
                if (block == Blocks.redstone_wire) {
                    return -1;
                }
            }
            if (block == Blocks.double_plant && renderEnv.getMetadata() >= 8) {
                blockPos = blockPos.down();
                iblockstate = blockAccess.getBlockState(blockPos);
            }
            final CustomColormap customcolormap = getBlockColormap(iblockstate);
            if (customcolormap != null) {
                if (Config.isSmoothBiomes() && !customcolormap.isColorConstant()) {
                    return getSmoothColorMultiplier(blockState, blockAccess, blockPos, customcolormap, renderEnv.getColorizerBlockPosM());
                }
                return customcolormap.getColor(blockAccess, blockPos);
            }
        }
        if (!quad.hasTintIndex()) {
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
        final int i = renderEnv.getMetadata();
        IColorizer customcolors$icolorizer;
        if (block != Blocks.grass && block != Blocks.tallgrass && block != Blocks.double_plant) {
            if (block == Blocks.double_plant) {
                customcolors$icolorizer = CustomColors.COLORIZER_GRASS;
                if (i >= 8) {
                    blockPos = blockPos.down();
                }
            }
            else if (block == Blocks.leaves) {
                switch (i & 0x3) {
                    case 0: {
                        customcolors$icolorizer = CustomColors.COLORIZER_FOLIAGE;
                        break;
                    }
                    case 1: {
                        customcolors$icolorizer = CustomColors.COLORIZER_FOLIAGE_PINE;
                        break;
                    }
                    case 2: {
                        customcolors$icolorizer = CustomColors.COLORIZER_FOLIAGE_BIRCH;
                        break;
                    }
                    default: {
                        customcolors$icolorizer = CustomColors.COLORIZER_FOLIAGE;
                        break;
                    }
                }
            }
            else if (block == Blocks.leaves2) {
                customcolors$icolorizer = CustomColors.COLORIZER_FOLIAGE;
            }
            else {
                if (block != Blocks.vine) {
                    return -1;
                }
                customcolors$icolorizer = CustomColors.COLORIZER_FOLIAGE;
            }
        }
        else {
            customcolors$icolorizer = CustomColors.COLORIZER_GRASS;
        }
        return (Config.isSmoothBiomes() && !customcolors$icolorizer.isColorConstant()) ? getSmoothColorMultiplier(blockState, blockAccess, blockPos, customcolors$icolorizer, renderEnv.getColorizerBlockPosM()) : customcolors$icolorizer.getColor(iblockstate, blockAccess, blockPos);
    }
    
    protected static BiomeGenBase getColorBiome(final IBlockAccess blockAccess, final BlockPos blockPos) {
        BiomeGenBase biomegenbase = blockAccess.getBiomeGenForCoords(blockPos);
        if (biomegenbase == BiomeGenBase.swampland && !Config.isSwampColors()) {
            biomegenbase = BiomeGenBase.plains;
        }
        return biomegenbase;
    }
    
    private static CustomColormap getBlockColormap(final IBlockState blockState) {
        if (CustomColors.blockColormaps == null) {
            return null;
        }
        if (!(blockState instanceof BlockStateBase)) {
            return null;
        }
        final BlockStateBase blockstatebase = (BlockStateBase)blockState;
        final int i = blockstatebase.getBlockId();
        if (i < 0 || i >= CustomColors.blockColormaps.length) {
            return null;
        }
        final CustomColormap[] acustomcolormap = CustomColors.blockColormaps[i];
        if (acustomcolormap == null) {
            return null;
        }
        for (int j = 0; j < acustomcolormap.length; ++j) {
            final CustomColormap customcolormap = acustomcolormap[j];
            if (customcolormap.matchesBlock(blockstatebase)) {
                return customcolormap;
            }
        }
        return null;
    }
    
    private static int getSmoothColorMultiplier(final IBlockState blockState, final IBlockAccess blockAccess, final BlockPos blockPos, final IColorizer colorizer, final BlockPosM blockPosM) {
        int i = 0;
        int j = 0;
        int k = 0;
        final int l = blockPos.getX();
        final int i2 = blockPos.getY();
        final int j2 = blockPos.getZ();
        final BlockPosM blockposm = blockPosM;
        for (int k2 = l - 1; k2 <= l + 1; ++k2) {
            for (int l2 = j2 - 1; l2 <= j2 + 1; ++l2) {
                blockposm.setXyz(k2, i2, l2);
                final int i3 = colorizer.getColor(blockState, blockAccess, blockposm);
                i += (i3 >> 16 & 0xFF);
                j += (i3 >> 8 & 0xFF);
                k += (i3 & 0xFF);
            }
        }
        final int j3 = i / 9;
        final int k3 = j / 9;
        final int l3 = k / 9;
        return j3 << 16 | k3 << 8 | l3;
    }
    
    public static int getFluidColor(final IBlockAccess blockAccess, final IBlockState blockState, final BlockPos blockPos, final RenderEnv renderEnv) {
        final Block block = blockState.getBlock();
        IColorizer customcolors$icolorizer = getBlockColormap(blockState);
        if (customcolors$icolorizer == null && blockState.getBlock().getMaterial() == Material.water) {
            customcolors$icolorizer = CustomColors.COLORIZER_WATER;
        }
        return (customcolors$icolorizer == null) ? block.colorMultiplier(blockAccess, blockPos, 0) : ((Config.isSmoothBiomes() && !customcolors$icolorizer.isColorConstant()) ? getSmoothColorMultiplier(blockState, blockAccess, blockPos, customcolors$icolorizer, renderEnv.getColorizerBlockPosM()) : customcolors$icolorizer.getColor(blockState, blockAccess, blockPos));
    }
    
    public static void updatePortalFX(final EntityFX fx) {
        if (CustomColors.particlePortalColor >= 0) {
            final int i = CustomColors.particlePortalColor;
            final int j = i >> 16 & 0xFF;
            final int k = i >> 8 & 0xFF;
            final int l = i & 0xFF;
            final float f = j / 255.0f;
            final float f2 = k / 255.0f;
            final float f3 = l / 255.0f;
            fx.setRBGColorF(f, f2, f3);
        }
    }
    
    public static void updateMyceliumFX(final EntityFX fx) {
        if (CustomColors.myceliumParticleColors != null) {
            final int i = CustomColors.myceliumParticleColors.getColorRandom();
            final int j = i >> 16 & 0xFF;
            final int k = i >> 8 & 0xFF;
            final int l = i & 0xFF;
            final float f = j / 255.0f;
            final float f2 = k / 255.0f;
            final float f3 = l / 255.0f;
            fx.setRBGColorF(f, f2, f3);
        }
    }
    
    private static int getRedstoneColor(final IBlockState blockState) {
        if (CustomColors.redstoneColors == null) {
            return -1;
        }
        final int i = getRedstoneLevel(blockState, 15);
        final int j = CustomColors.redstoneColors.getColor(i);
        return j;
    }
    
    public static void updateReddustFX(final EntityFX fx, final IBlockAccess blockAccess, final double x, final double y, final double z) {
        if (CustomColors.redstoneColors != null) {
            final IBlockState iblockstate = blockAccess.getBlockState(new BlockPos(x, y, z));
            final int i = getRedstoneLevel(iblockstate, 15);
            final int j = CustomColors.redstoneColors.getColor(i);
            final int k = j >> 16 & 0xFF;
            final int l = j >> 8 & 0xFF;
            final int i2 = j & 0xFF;
            final float f = k / 255.0f;
            final float f2 = l / 255.0f;
            final float f3 = i2 / 255.0f;
            fx.setRBGColorF(f, f2, f3);
        }
    }
    
    private static int getRedstoneLevel(final IBlockState state, final int def) {
        final Block block = state.getBlock();
        if (!(block instanceof BlockRedstoneWire)) {
            return def;
        }
        final Object object = state.getValue((IProperty<Object>)BlockRedstoneWire.POWER);
        if (!(object instanceof Integer)) {
            return def;
        }
        final Integer integer = (Integer)object;
        return integer;
    }
    
    public static float getXpOrbTimer(final float timer) {
        if (CustomColors.xpOrbTime <= 0) {
            return timer;
        }
        final float f = 628.0f / CustomColors.xpOrbTime;
        return timer * f;
    }
    
    public static int getXpOrbColor(final float timer) {
        if (CustomColors.xpOrbColors == null) {
            return -1;
        }
        final int i = (int)Math.round((MathHelper.sin(timer) + 1.0f) * (CustomColors.xpOrbColors.getLength() - 1) / 2.0);
        final int j = CustomColors.xpOrbColors.getColor(i);
        return j;
    }
    
    public static int getDurabilityColor(final int dur255) {
        if (CustomColors.durabilityColors == null) {
            return -1;
        }
        final int i = dur255 * CustomColors.durabilityColors.getLength() / 255;
        final int j = CustomColors.durabilityColors.getColor(i);
        return j;
    }
    
    public static void updateWaterFX(final EntityFX fx, final IBlockAccess blockAccess, final double x, final double y, final double z, final RenderEnv renderEnv) {
        if (CustomColors.waterColors != null || CustomColors.blockColormaps != null || CustomColors.particleWaterColor >= 0) {
            final BlockPos blockpos = new BlockPos(x, y, z);
            renderEnv.reset(CustomColors.BLOCK_STATE_WATER, blockpos);
            final int i = getFluidColor(blockAccess, CustomColors.BLOCK_STATE_WATER, blockpos, renderEnv);
            final int j = i >> 16 & 0xFF;
            final int k = i >> 8 & 0xFF;
            final int l = i & 0xFF;
            float f = j / 255.0f;
            float f2 = k / 255.0f;
            float f3 = l / 255.0f;
            if (CustomColors.particleWaterColor >= 0) {
                final int i2 = CustomColors.particleWaterColor >> 16 & 0xFF;
                final int j2 = CustomColors.particleWaterColor >> 8 & 0xFF;
                final int k2 = CustomColors.particleWaterColor & 0xFF;
                f *= i2 / 255.0f;
                f2 *= j2 / 255.0f;
                f3 *= k2 / 255.0f;
            }
            fx.setRBGColorF(f, f2, f3);
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
        final int i = CustomColors.skyColors.getColorSmooth(blockAccess, x, y, z, 3);
        final int j = i >> 16 & 0xFF;
        final int k = i >> 8 & 0xFF;
        final int l = i & 0xFF;
        float f = j / 255.0f;
        float f2 = k / 255.0f;
        float f3 = l / 255.0f;
        final float f4 = (float)skyColor3d.xCoord / 0.5f;
        final float f5 = (float)skyColor3d.yCoord / 0.66275f;
        final float f6 = (float)skyColor3d.zCoord;
        f *= f4;
        f2 *= f5;
        f3 *= f6;
        final Vec3 vec3 = CustomColors.skyColorFader.getColor(f, f2, f3);
        return vec3;
    }
    
    private static Vec3 getFogColor(final Vec3 fogColor3d, final IBlockAccess blockAccess, final double x, final double y, final double z) {
        if (CustomColors.fogColors == null) {
            return fogColor3d;
        }
        final int i = CustomColors.fogColors.getColorSmooth(blockAccess, x, y, z, 3);
        final int j = i >> 16 & 0xFF;
        final int k = i >> 8 & 0xFF;
        final int l = i & 0xFF;
        float f = j / 255.0f;
        float f2 = k / 255.0f;
        float f3 = l / 255.0f;
        final float f4 = (float)fogColor3d.xCoord / 0.753f;
        final float f5 = (float)fogColor3d.yCoord / 0.8471f;
        final float f6 = (float)fogColor3d.zCoord;
        f *= f4;
        f2 *= f5;
        f3 *= f6;
        final Vec3 vec3 = CustomColors.fogColorFader.getColor(f, f2, f3);
        return vec3;
    }
    
    public static Vec3 getUnderwaterColor(final IBlockAccess blockAccess, final double x, final double y, final double z) {
        return getUnderFluidColor(blockAccess, x, y, z, CustomColors.underwaterColors, CustomColors.underwaterColorFader);
    }
    
    public static Vec3 getUnderlavaColor(final IBlockAccess blockAccess, final double x, final double y, final double z) {
        return getUnderFluidColor(blockAccess, x, y, z, CustomColors.underlavaColors, CustomColors.underlavaColorFader);
    }
    
    public static Vec3 getUnderFluidColor(final IBlockAccess blockAccess, final double x, final double y, final double z, final CustomColormap underFluidColors, final CustomColorFader underFluidColorFader) {
        if (underFluidColors == null) {
            return null;
        }
        final int i = underFluidColors.getColorSmooth(blockAccess, x, y, z, 3);
        final int j = i >> 16 & 0xFF;
        final int k = i >> 8 & 0xFF;
        final int l = i & 0xFF;
        final float f = j / 255.0f;
        final float f2 = k / 255.0f;
        final float f3 = l / 255.0f;
        final Vec3 vec3 = underFluidColorFader.getColor(f, f2, f3);
        return vec3;
    }
    
    private static int getStemColorMultiplier(final Block blockStem, final IBlockAccess blockAccess, final BlockPos blockPos, final RenderEnv renderEnv) {
        CustomColormap customcolormap = CustomColors.stemColors;
        if (blockStem == Blocks.pumpkin_stem && CustomColors.stemPumpkinColors != null) {
            customcolormap = CustomColors.stemPumpkinColors;
        }
        if (blockStem == Blocks.melon_stem && CustomColors.stemMelonColors != null) {
            customcolormap = CustomColors.stemMelonColors;
        }
        if (customcolormap == null) {
            return -1;
        }
        final int i = renderEnv.getMetadata();
        return customcolormap.getColor(i);
    }
    
    public static boolean updateLightmap(final World world, final float torchFlickerX, final int[] lmColors, final boolean nightvision, final float partialTicks) {
        if (world == null) {
            return false;
        }
        if (CustomColors.lightMapPacks == null) {
            return false;
        }
        final int i = world.provider.getDimensionId();
        final int j = i - CustomColors.lightmapMinDimensionId;
        if (j >= 0 && j < CustomColors.lightMapPacks.length) {
            final LightMapPack lightmappack = CustomColors.lightMapPacks[j];
            return lightmappack != null && lightmappack.updateLightmap(world, torchFlickerX, lmColors, nightvision, partialTicks);
        }
        return false;
    }
    
    public static Vec3 getWorldFogColor(Vec3 fogVec, final World world, final Entity renderViewEntity, final float partialTicks) {
        final int i = world.provider.getDimensionId();
        switch (i) {
            case -1: {
                fogVec = getFogColorNether(fogVec);
                break;
            }
            case 0: {
                final Minecraft minecraft = Minecraft.getMinecraft();
                fogVec = getFogColor(fogVec, minecraft.theWorld, renderViewEntity.posX, renderViewEntity.posY + 1.0, renderViewEntity.posZ);
                break;
            }
            case 1: {
                fogVec = getFogColorEnd(fogVec);
                break;
            }
        }
        return fogVec;
    }
    
    public static Vec3 getWorldSkyColor(Vec3 skyVec, final World world, final Entity renderViewEntity, final float partialTicks) {
        final int i = world.provider.getDimensionId();
        switch (i) {
            case 0: {
                final Minecraft minecraft = Minecraft.getMinecraft();
                skyVec = getSkyColor(skyVec, minecraft.theWorld, renderViewEntity.posX, renderViewEntity.posY + 1.0, renderViewEntity.posZ);
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
        final List<Integer> list = new ArrayList<Integer>();
        final Set set = props.keySet();
        int i = 0;
        for (final Object e : set) {
            final String s = (String)e;
            final String s2 = props.getProperty(s);
            if (s.startsWith(prefix)) {
                final String s3 = StrUtils.removePrefix(s, prefix);
                final int j = EntityUtils.getEntityIdByName(s3);
                if (j < 0) {
                    warn("Invalid spawn egg name: " + s);
                }
                else {
                    final int k = parseColor(s2);
                    if (k < 0) {
                        warn("Invalid spawn egg color: " + s + " = " + s2);
                    }
                    else {
                        while (list.size() <= j) {
                            list.add(-1);
                        }
                        list.set(j, k);
                        ++i;
                    }
                }
            }
        }
        if (i <= 0) {
            return null;
        }
        dbg(logName + " colors: " + i);
        final int[] aint = new int[list.size()];
        for (int l = 0; l < aint.length; ++l) {
            aint[l] = list.get(l);
        }
        return aint;
    }
    
    private static int getSpawnEggColor(final ItemMonsterPlacer item, final ItemStack itemStack, final int layer, final int color) {
        final int i = itemStack.getMetadata();
        final int[] aint = (layer == 0) ? CustomColors.spawnEggPrimaryColors : CustomColors.spawnEggSecondaryColors;
        if (aint == null) {
            return color;
        }
        if (i >= 0 && i < aint.length) {
            final int j = aint[i];
            return (j < 0) ? color : j;
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
        final EnumDyeColor[] aenumdyecolor = EnumDyeColor.values();
        final Map<String, EnumDyeColor> map = new HashMap<String, EnumDyeColor>();
        for (int i = 0; i < aenumdyecolor.length; ++i) {
            final EnumDyeColor enumdyecolor = aenumdyecolor[i];
            map.put(enumdyecolor.getName(), enumdyecolor);
        }
        final float[][] afloat1 = new float[aenumdyecolor.length][];
        int k = 0;
        for (final Object e : props.keySet()) {
            final String s = (String)e;
            final String s2 = props.getProperty(s);
            if (s.startsWith(prefix)) {
                String s3 = StrUtils.removePrefix(s, prefix);
                if (s3.equals("lightBlue")) {
                    s3 = "light_blue";
                }
                final EnumDyeColor enumdyecolor2 = map.get(s3);
                final int j = parseColor(s2);
                if (enumdyecolor2 != null && j >= 0) {
                    final float[] afloat2 = { (j >> 16 & 0xFF) / 255.0f, (j >> 8 & 0xFF) / 255.0f, (j & 0xFF) / 255.0f };
                    afloat1[enumdyecolor2.ordinal()] = afloat2;
                    ++k;
                }
                else {
                    warn("Invalid color: " + s + " = " + s2);
                }
            }
        }
        if (k <= 0) {
            return null;
        }
        dbg(logName + " colors: " + k);
        return afloat1;
    }
    
    private static float[] getDyeColors(final EnumDyeColor dye, final float[][] dyeColors, final float[] colors) {
        if (dyeColors == null) {
            return colors;
        }
        if (dye == null) {
            return colors;
        }
        final float[] afloat = dyeColors[dye.ordinal()];
        return (afloat == null) ? colors : afloat;
    }
    
    public static float[] getWolfCollarColors(final EnumDyeColor dye, final float[] colors) {
        return getDyeColors(dye, CustomColors.wolfCollarColors, colors);
    }
    
    public static float[] getSheepColors(final EnumDyeColor dye, final float[] colors) {
        return getDyeColors(dye, CustomColors.sheepColors, colors);
    }
    
    private static int[] readTextColors(final Properties props, final String fileName, final String prefix, final String logName) {
        final int[] aint = new int[32];
        Arrays.fill(aint, -1);
        int i = 0;
        for (final Object e : props.keySet()) {
            final String s = (String)e;
            final String s2 = props.getProperty(s);
            if (s.startsWith(prefix)) {
                final String s3 = StrUtils.removePrefix(s, prefix);
                final int j = Config.parseInt(s3, -1);
                final int k = parseColor(s2);
                if (j >= 0 && j < aint.length && k >= 0) {
                    aint[j] = k;
                    ++i;
                }
                else {
                    warn("Invalid color: " + s + " = " + s2);
                }
            }
        }
        if (i <= 0) {
            return null;
        }
        dbg(logName + " colors: " + i);
        return aint;
    }
    
    public static int getTextColor(final int index, final int color) {
        if (CustomColors.textColors == null) {
            return color;
        }
        if (index >= 0 && index < CustomColors.textColors.length) {
            final int i = CustomColors.textColors[index];
            return (i < 0) ? color : i;
        }
        return color;
    }
    
    private static int[] readMapColors(final Properties props, final String fileName, final String prefix, final String logName) {
        final int[] aint = new int[MapColor.mapColorArray.length];
        Arrays.fill(aint, -1);
        int i = 0;
        for (final Object o : props.keySet()) {
            final String s = (String)o;
            final String s2 = props.getProperty(s);
            if (s.startsWith(prefix)) {
                final String s3 = StrUtils.removePrefix(s, prefix);
                final int j = getMapColorIndex(s3);
                final int k = parseColor(s2);
                if (j >= 0 && j < aint.length && k >= 0) {
                    aint[j] = k;
                    ++i;
                }
                else {
                    warn("Invalid color: " + s + " = " + s2);
                }
            }
        }
        if (i <= 0) {
            return null;
        }
        dbg(logName + " colors: " + i);
        return aint;
    }
    
    private static int[] readPotionColors(final Properties props, final String fileName, final String prefix, final String logName) {
        final int[] aint = new int[Potion.potionTypes.length];
        Arrays.fill(aint, -1);
        int i = 0;
        for (final Object e : props.keySet()) {
            final String s = (String)e;
            final String s2 = props.getProperty(s);
            if (s.startsWith(prefix)) {
                final int j = getPotionId(s);
                final int k = parseColor(s2);
                if (j >= 0 && j < aint.length && k >= 0) {
                    aint[j] = k;
                    ++i;
                }
                else {
                    warn("Invalid color: " + s + " = " + s2);
                }
            }
        }
        if (i <= 0) {
            return null;
        }
        dbg(logName + " colors: " + i);
        return aint;
    }
    
    private static int getPotionId(final String name) {
        if (name.equals("potion.water")) {
            return 0;
        }
        final Potion[] apotion = Potion.potionTypes;
        for (int i = 0; i < apotion.length; ++i) {
            final Potion potion = apotion[i];
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
            final int i = CustomColors.potionColors[potionId];
            return (i < 0) ? color : i;
        }
        return color;
    }
    
    private static int getMapColorIndex(final String name) {
        return (name == null) ? -1 : (name.equals("air") ? MapColor.airColor.colorIndex : (name.equals("grass") ? MapColor.grassColor.colorIndex : (name.equals("sand") ? MapColor.sandColor.colorIndex : (name.equals("cloth") ? MapColor.clothColor.colorIndex : (name.equals("tnt") ? MapColor.tntColor.colorIndex : (name.equals("ice") ? MapColor.iceColor.colorIndex : (name.equals("iron") ? MapColor.ironColor.colorIndex : (name.equals("foliage") ? MapColor.foliageColor.colorIndex : (name.equals("clay") ? MapColor.clayColor.colorIndex : (name.equals("dirt") ? MapColor.dirtColor.colorIndex : (name.equals("stone") ? MapColor.stoneColor.colorIndex : (name.equals("water") ? MapColor.waterColor.colorIndex : (name.equals("wood") ? MapColor.woodColor.colorIndex : (name.equals("quartz") ? MapColor.quartzColor.colorIndex : (name.equals("gold") ? MapColor.goldColor.colorIndex : (name.equals("diamond") ? MapColor.diamondColor.colorIndex : (name.equals("lapis") ? MapColor.lapisColor.colorIndex : (name.equals("emerald") ? MapColor.emeraldColor.colorIndex : (name.equals("podzol") ? MapColor.obsidianColor.colorIndex : (name.equals("netherrack") ? MapColor.netherrackColor.colorIndex : ((!name.equals("snow") && !name.equals("white")) ? ((!name.equals("adobe") && !name.equals("orange")) ? (name.equals("magenta") ? MapColor.magentaColor.colorIndex : ((!name.equals("light_blue") && !name.equals("lightBlue")) ? (name.equals("yellow") ? MapColor.yellowColor.colorIndex : (name.equals("lime") ? MapColor.limeColor.colorIndex : (name.equals("pink") ? MapColor.pinkColor.colorIndex : (name.equals("gray") ? MapColor.grayColor.colorIndex : (name.equals("silver") ? MapColor.silverColor.colorIndex : (name.equals("cyan") ? MapColor.cyanColor.colorIndex : (name.equals("purple") ? MapColor.purpleColor.colorIndex : (name.equals("blue") ? MapColor.blueColor.colorIndex : (name.equals("brown") ? MapColor.brownColor.colorIndex : (name.equals("green") ? MapColor.greenColor.colorIndex : (name.equals("red") ? MapColor.redColor.colorIndex : (name.equals("black") ? MapColor.blackColor.colorIndex : -1)))))))))))) : MapColor.lightBlueColor.colorIndex)) : MapColor.adobeColor.colorIndex) : MapColor.snowColor.colorIndex)))))))))))))))))))));
    }
    
    private static int[] getMapColors() {
        final MapColor[] amapcolor = MapColor.mapColorArray;
        final int[] aint = new int[amapcolor.length];
        Arrays.fill(aint, -1);
        for (int i = 0; i < amapcolor.length && i < aint.length; ++i) {
            final MapColor mapcolor = amapcolor[i];
            if (mapcolor != null) {
                aint[i] = mapcolor.colorValue;
            }
        }
        return aint;
    }
    
    private static void setMapColors(final int[] colors) {
        if (colors != null) {
            final MapColor[] amapcolor = MapColor.mapColorArray;
            boolean flag = false;
            for (int i = 0; i < amapcolor.length && i < colors.length; ++i) {
                final MapColor mapcolor = amapcolor[i];
                if (mapcolor != null) {
                    final int j = colors[i];
                    if (j >= 0 && mapcolor.colorValue != j) {
                        mapcolor.colorValue = j;
                        flag = true;
                    }
                }
            }
            if (flag) {
                Minecraft.getMinecraft().getTextureManager().reloadBannerTextures();
            }
        }
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
    
    static {
        CustomColors.paletteFormatDefault = "vanilla";
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
        CustomColors.underlavaColors = null;
        CustomColors.underlavaColorFader = new CustomColorFader();
        CustomColors.lightMapPacks = null;
        CustomColors.lightmapMinDimensionId = 0;
        CustomColors.redstoneColors = null;
        CustomColors.xpOrbColors = null;
        CustomColors.xpOrbTime = -1;
        CustomColors.durabilityColors = null;
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
            public int getColor(final IBlockState blockState, final IBlockAccess blockAccess, final BlockPos blockPos) {
                final BiomeGenBase biomegenbase = CustomColors.getColorBiome(blockAccess, blockPos);
                return (CustomColors.swampGrassColors != null && biomegenbase == BiomeGenBase.swampland) ? CustomColors.swampGrassColors.getColor(biomegenbase, blockPos) : biomegenbase.getGrassColorAtPos(blockPos);
            }
            
            @Override
            public boolean isColorConstant() {
                return false;
            }
        };
        COLORIZER_FOLIAGE = new IColorizer() {
            @Override
            public int getColor(final IBlockState blockState, final IBlockAccess blockAccess, final BlockPos blockPos) {
                final BiomeGenBase biomegenbase = CustomColors.getColorBiome(blockAccess, blockPos);
                return (CustomColors.swampFoliageColors != null && biomegenbase == BiomeGenBase.swampland) ? CustomColors.swampFoliageColors.getColor(biomegenbase, blockPos) : biomegenbase.getFoliageColorAtPos(blockPos);
            }
            
            @Override
            public boolean isColorConstant() {
                return false;
            }
        };
        COLORIZER_FOLIAGE_PINE = new IColorizer() {
            @Override
            public int getColor(final IBlockState blockState, final IBlockAccess blockAccess, final BlockPos blockPos) {
                return (CustomColors.foliagePineColors != null) ? CustomColors.foliagePineColors.getColor(blockAccess, blockPos) : ColorizerFoliage.getFoliageColorPine();
            }
            
            @Override
            public boolean isColorConstant() {
                return CustomColors.foliagePineColors == null;
            }
        };
        COLORIZER_FOLIAGE_BIRCH = new IColorizer() {
            @Override
            public int getColor(final IBlockState blockState, final IBlockAccess blockAccess, final BlockPos blockPos) {
                return (CustomColors.foliageBirchColors != null) ? CustomColors.foliageBirchColors.getColor(blockAccess, blockPos) : ColorizerFoliage.getFoliageColorBirch();
            }
            
            @Override
            public boolean isColorConstant() {
                return CustomColors.foliageBirchColors == null;
            }
        };
        COLORIZER_WATER = new IColorizer() {
            @Override
            public int getColor(final IBlockState blockState, final IBlockAccess blockAccess, final BlockPos blockPos) {
                final BiomeGenBase biomegenbase = CustomColors.getColorBiome(blockAccess, blockPos);
                return (CustomColors.waterColors != null) ? CustomColors.waterColors.getColor(biomegenbase, blockPos) : biomegenbase.waterColorMultiplier;
            }
            
            @Override
            public boolean isColorConstant() {
                return false;
            }
        };
    }
    
    public interface IColorizer
    {
        int getColor(final IBlockState p0, final IBlockAccess p1, final BlockPos p2);
        
        boolean isColorConstant();
    }
}
