package net.optifine;

import java.awt.image.BufferedImage;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.List;
import java.util.Properties;
import java.util.Random;
import java.util.Set;
import javax.imageio.ImageIO;

import net.minecraft.block.Block;
import net.minecraft.block.BlockDoublePlant;
import net.minecraft.block.BlockGrass;
import net.minecraft.block.BlockLeaves;
import net.minecraft.block.BlockRedstoneWire;
import net.minecraft.block.BlockStaticLiquid;
import net.minecraft.block.BlockStem;
import net.minecraft.block.BlockTallGrass;
import net.minecraft.block.material.MapColor;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.properties.PropertyInteger;
import net.minecraft.block.state.BlockStateBase;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.WorldClient;
import net.minecraft.client.particle.EntityFX;
import net.minecraft.client.renderer.block.model.BakedQuad;
import net.minecraft.client.renderer.texture.TextureManager;
import net.minecraft.entity.Entity;
import net.minecraft.init.Blocks;
import net.minecraft.item.EnumDyeColor;
import net.minecraft.item.Item;
import net.minecraft.item.ItemMonsterPlacer;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.Potion;
import net.minecraft.src.Config;
import net.minecraft.util.BlockPos;
import net.minecraft.util.MathHelper;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.Vec3;
import net.minecraft.world.ColorizerFoliage;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraft.world.WorldProvider;
import net.minecraft.world.biome.BiomeGenBase;
import net.optifine.BlockPosM;
import net.optifine.CustomColorFader;
import net.optifine.CustomColormap;
import net.optifine.LightMap;
import net.optifine.LightMapPack;
import net.optifine.config.ConnectedParser;
import net.optifine.config.MatchBlock;
import net.optifine.reflect.Reflector;
import net.optifine.reflect.ReflectorMethod;
import net.optifine.render.RenderEnv;
import net.optifine.util.EntityUtils;
import net.optifine.util.PropertiesOrdered;
import net.optifine.util.ResUtils;
import net.optifine.util.StrUtils;
import net.optifine.util.TextureUtils;

import org.apache.commons.lang3.tuple.ImmutablePair;
import org.apache.commons.lang3.tuple.Pair;

public class CustomColors {
    private static String paletteFormatDefault = "vanilla";
    private static CustomColormap waterColors = null;
    private static CustomColormap foliagePineColors = null;
    private static CustomColormap foliageBirchColors = null;
    private static CustomColormap swampFoliageColors = null;
    private static CustomColormap swampGrassColors = null;
    private static CustomColormap[] colorsBlockColormaps = null;
    private static CustomColormap[][] blockColormaps = null;
    private static CustomColormap skyColors = null;
    private static CustomColorFader skyColorFader = new CustomColorFader();
    private static CustomColormap fogColors = null;
    private static CustomColorFader fogColorFader = new CustomColorFader();
    private static CustomColormap underwaterColors = null;
    private static CustomColorFader underwaterColorFader = new CustomColorFader();
    private static CustomColormap underlavaColors = null;
    private static CustomColorFader underlavaColorFader = new CustomColorFader();
    private static LightMapPack[] lightMapPacks = null;
    private static int lightmapMinDimensionId = 0;
    private static CustomColormap redstoneColors = null;
    private static CustomColormap xpOrbColors = null;
    private static int xpOrbTime = -1;
    private static CustomColormap durabilityColors = null;
    private static CustomColormap stemColors = null;
    private static CustomColormap stemMelonColors = null;
    private static CustomColormap stemPumpkinColors = null;
    private static CustomColormap myceliumParticleColors = null;
    private static boolean useDefaultGrassFoliageColors = true;
    private static int particleWaterColor = -1;
    private static int particlePortalColor = -1;
    private static int lilyPadColor = -1;
    private static int expBarTextColor = -1;
    private static int bossTextColor = -1;
    private static int signTextColor = -1;
    private static Vec3 fogColorNether = null;
    private static Vec3 fogColorEnd = null;
    private static Vec3 skyColorEnd = null;
    private static int[] spawnEggPrimaryColors = null;
    private static int[] spawnEggSecondaryColors = null;
    private static float[][] wolfCollarColors = null;
    private static float[][] sheepColors = null;
    private static int[] textColors = null;
    private static int[] mapColorsOriginal = null;
    private static int[] potionColors = null;
    private static final IBlockState BLOCK_STATE_DIRT = Blocks.dirt.getDefaultState();
    private static final IBlockState BLOCK_STATE_WATER = Blocks.water.getDefaultState();
    public static Random random = new Random();
    private static final IColorizer COLORIZER_GRASS = new IColorizer(){

        @Override
        public int getColor(IBlockState blockState, IBlockAccess blockAccess, BlockPos blockPos) {
            BiomeGenBase biome = CustomColors.getColorBiome(blockAccess, blockPos);
            if (swampGrassColors != null && biome == BiomeGenBase.swampland) {
                return swampGrassColors.getColor(biome, blockPos);
            }
            return biome.getGrassColorAtPos(blockPos);
        }

        @Override
        public boolean isColorConstant() {
            return false;
        }
    };
    private static final IColorizer COLORIZER_FOLIAGE = new IColorizer(){

        @Override
        public int getColor(IBlockState blockState, IBlockAccess blockAccess, BlockPos blockPos) {
            BiomeGenBase biome = CustomColors.getColorBiome(blockAccess, blockPos);
            if (swampFoliageColors != null && biome == BiomeGenBase.swampland) {
                return swampFoliageColors.getColor(biome, blockPos);
            }
            return biome.getFoliageColorAtPos(blockPos);
        }

        @Override
        public boolean isColorConstant() {
            return false;
        }
    };
    private static final IColorizer COLORIZER_FOLIAGE_PINE = new IColorizer(){

        @Override
        public int getColor(IBlockState blockState, IBlockAccess blockAccess, BlockPos blockPos) {
            if (foliagePineColors != null) {
                return foliagePineColors.getColor(blockAccess, blockPos);
            }
            return ColorizerFoliage.getFoliageColorPine();
        }

        @Override
        public boolean isColorConstant() {
            return foliagePineColors == null;
        }
    };
    private static final IColorizer COLORIZER_FOLIAGE_BIRCH = new IColorizer(){

        @Override
        public int getColor(IBlockState blockState, IBlockAccess blockAccess, BlockPos blockPos) {
            if (foliageBirchColors != null) {
                return foliageBirchColors.getColor(blockAccess, blockPos);
            }
            return ColorizerFoliage.getFoliageColorBirch();
        }

        @Override
        public boolean isColorConstant() {
            return foliageBirchColors == null;
        }
    };
    private static final IColorizer COLORIZER_WATER = new IColorizer(){

        @Override
        public int getColor(IBlockState blockState, IBlockAccess blockAccess, BlockPos blockPos) {
            BiomeGenBase biome = CustomColors.getColorBiome(blockAccess, blockPos);
            if (waterColors != null) {
                return waterColors.getColor(biome, blockPos);
            }
            if (Reflector.ForgeBiome_getWaterColorMultiplier.exists()) {
                return Reflector.callInt((Object)biome, Reflector.ForgeBiome_getWaterColorMultiplier, new Object[0]);
            }
            return biome.waterColorMultiplier;
        }

        @Override
        public boolean isColorConstant() {
            return false;
        }
    };

    public static void update() {
        paletteFormatDefault = "vanilla";
        waterColors = null;
        foliageBirchColors = null;
        foliagePineColors = null;
        swampGrassColors = null;
        swampFoliageColors = null;
        skyColors = null;
        fogColors = null;
        underwaterColors = null;
        underlavaColors = null;
        redstoneColors = null;
        xpOrbColors = null;
        xpOrbTime = -1;
        durabilityColors = null;
        stemColors = null;
        myceliumParticleColors = null;
        lightMapPacks = null;
        particleWaterColor = -1;
        particlePortalColor = -1;
        lilyPadColor = -1;
        expBarTextColor = -1;
        bossTextColor = -1;
        signTextColor = -1;
        fogColorNether = null;
        fogColorEnd = null;
        skyColorEnd = null;
        colorsBlockColormaps = null;
        blockColormaps = null;
        useDefaultGrassFoliageColors = true;
        spawnEggPrimaryColors = null;
        spawnEggSecondaryColors = null;
        wolfCollarColors = null;
        sheepColors = null;
        textColors = null;
        CustomColors.setMapColors(mapColorsOriginal);
        potionColors = null;
        paletteFormatDefault = CustomColors.getValidProperty("mcpatcher/color.properties", "palette.format", CustomColormap.FORMAT_STRINGS, "vanilla");
        String mcpColormap = "mcpatcher/colormap/";
        String[] waterPaths = new String[]{"water.png", "watercolorX.png"};
        waterColors = CustomColors.getCustomColors(mcpColormap, waterPaths, 256, 256);
        CustomColors.updateUseDefaultGrassFoliageColors();
        if (!Config.isCustomColors()) {
            return;
        }
        String[] pinePaths = new String[]{"pine.png", "pinecolor.png"};
        foliagePineColors = CustomColors.getCustomColors(mcpColormap, pinePaths, 256, 256);
        String[] birchPaths = new String[]{"birch.png", "birchcolor.png"};
        foliageBirchColors = CustomColors.getCustomColors(mcpColormap, birchPaths, 256, 256);
        String[] swampGrassPaths = new String[]{"swampgrass.png", "swampgrasscolor.png"};
        swampGrassColors = CustomColors.getCustomColors(mcpColormap, swampGrassPaths, 256, 256);
        String[] swampFoliagePaths = new String[]{"swampfoliage.png", "swampfoliagecolor.png"};
        swampFoliageColors = CustomColors.getCustomColors(mcpColormap, swampFoliagePaths, 256, 256);
        String[] sky0Paths = new String[]{"sky0.png", "skycolor0.png"};
        skyColors = CustomColors.getCustomColors(mcpColormap, sky0Paths, 256, 256);
        String[] fog0Paths = new String[]{"fog0.png", "fogcolor0.png"};
        fogColors = CustomColors.getCustomColors(mcpColormap, fog0Paths, 256, 256);
        String[] underwaterPaths = new String[]{"underwater.png", "underwatercolor.png"};
        underwaterColors = CustomColors.getCustomColors(mcpColormap, underwaterPaths, 256, 256);
        String[] underlavaPaths = new String[]{"underlava.png", "underlavacolor.png"};
        underlavaColors = CustomColors.getCustomColors(mcpColormap, underlavaPaths, 256, 256);
        String[] redstonePaths = new String[]{"redstone.png", "redstonecolor.png"};
        redstoneColors = CustomColors.getCustomColors(mcpColormap, redstonePaths, 16, 1);
        xpOrbColors = CustomColors.getCustomColors(mcpColormap + "xporb.png", -1, -1);
        durabilityColors = CustomColors.getCustomColors(mcpColormap + "durability.png", -1, -1);
        String[] stemPaths = new String[]{"stem.png", "stemcolor.png"};
        stemColors = CustomColors.getCustomColors(mcpColormap, stemPaths, 8, 1);
        stemPumpkinColors = CustomColors.getCustomColors(mcpColormap + "pumpkinstem.png", 8, 1);
        stemMelonColors = CustomColors.getCustomColors(mcpColormap + "melonstem.png", 8, 1);
        String[] myceliumPaths = new String[]{"myceliumparticle.png", "myceliumparticlecolor.png"};
        myceliumParticleColors = CustomColors.getCustomColors(mcpColormap, myceliumPaths, -1, -1);
        Pair<LightMapPack[], Integer> lightMaps = CustomColors.parseLightMapPacks();
        lightMapPacks = (LightMapPack[])lightMaps.getLeft();
        lightmapMinDimensionId = (Integer)lightMaps.getRight();
        CustomColors.readColorProperties("mcpatcher/color.properties");
        blockColormaps = CustomColors.readBlockColormaps(new String[]{mcpColormap + "custom/", mcpColormap + "blocks/"}, colorsBlockColormaps, 256, 256);
        CustomColors.updateUseDefaultGrassFoliageColors();
    }

    private static String getValidProperty(String fileName, String key, String[] validValues, String valDef) {
        try {
            ResourceLocation loc = new ResourceLocation(fileName);
            InputStream in = Config.getResourceStream((ResourceLocation)loc);
            if (in == null) {
                return valDef;
            }
            PropertiesOrdered props = new PropertiesOrdered();
            props.load(in);
            in.close();
            String val = props.getProperty(key);
            if (val == null) {
                return valDef;
            }
            List<String> listValidValues = Arrays.asList(validValues);
            if (!listValidValues.contains(val)) {
                CustomColors.warn("Invalid value: " + key + "=" + val);
                CustomColors.warn("Expected values: " + Config.arrayToString((Object[])validValues));
                return valDef;
            }
            CustomColors.dbg("" + key + "=" + val);
            return val;
        }
        catch (FileNotFoundException e) {
            return valDef;
        }
        catch (IOException e) {
            e.printStackTrace();
            return valDef;
        }
    }

    private static Pair<LightMapPack[], Integer> parseLightMapPacks() {
        String lightmapPrefix = "mcpatcher/lightmap/world";
        String lightmapSuffix = ".png";
        String[] pathsLightmap = ResUtils.collectFiles(lightmapPrefix, lightmapSuffix);
        HashMap<Integer, String> mapLightmaps = new HashMap<Integer, String>();
        for (int i = 0; i < pathsLightmap.length; ++i) {
            String path = pathsLightmap[i];
            String dimIdStr = StrUtils.removePrefixSuffix(path, lightmapPrefix, lightmapSuffix);
            int dimId = Config.parseInt((String)dimIdStr, (int)Integer.MIN_VALUE);
            if (dimId == Integer.MIN_VALUE) {
                CustomColors.warn("Invalid dimension ID: " + dimIdStr + ", path: " + path);
                continue;
            }
            mapLightmaps.put(dimId, path);
        }
        Set setDimIds = mapLightmaps.keySet();
        Object[] dimIds = setDimIds.toArray(new Integer[setDimIds.size()]);
        Arrays.sort(dimIds);
        if (dimIds.length <= 0) {
            return new ImmutablePair(null, (Object)0);
        }
        int minDimId = (Integer)dimIds[0];
        int maxDimId = (Integer)dimIds[dimIds.length - 1];
        int countDim = maxDimId - minDimId + 1;
        CustomColormap[] colormaps = new CustomColormap[countDim];
        for (int i = 0; i < dimIds.length; ++i) {
            Object dimId = dimIds[i];
            String path = (String)mapLightmaps.get(dimId);
            CustomColormap colors = CustomColors.getCustomColors(path, -1, -1);
            if (colors == null) continue;
            if (colors.getWidth() < 16) {
                CustomColors.warn("Invalid lightmap width: " + colors.getWidth() + ", path: " + path);
                continue;
            }
            int lightmapIndex = (Integer)dimId - minDimId;
            colormaps[lightmapIndex] = colors;
        }
        LightMapPack[] lmps = new LightMapPack[colormaps.length];
        for (int i = 0; i < colormaps.length; ++i) {
            LightMapPack lmp;
            CustomColormap cm = colormaps[i];
            if (cm == null) continue;
            String name = cm.name;
            String basePath = cm.basePath;
            CustomColormap cmRain = CustomColors.getCustomColors(basePath + "/" + name + "_rain.png", -1, -1);
            CustomColormap cmThunder = CustomColors.getCustomColors(basePath + "/" + name + "_thunder.png", -1, -1);
            LightMap lm = new LightMap(cm);
            LightMap lmRain = cmRain != null ? new LightMap(cmRain) : null;
            LightMap lmThunder = cmThunder != null ? new LightMap(cmThunder) : null;
            lmps[i] = lmp = new LightMapPack(lm, lmRain, lmThunder);
        }
        return new ImmutablePair((Object)lmps, (Object)minDimId);
    }

    private static int getTextureHeight(String path, int defHeight) {
        try {
            InputStream in = Config.getResourceStream((ResourceLocation)new ResourceLocation(path));
            if (in == null) {
                return defHeight;
            }
            BufferedImage bi = ImageIO.read(in);
            in.close();
            if (bi == null) {
                return defHeight;
            }
            return bi.getHeight();
        }
        catch (IOException e) {
            return defHeight;
        }
    }

    private static void readColorProperties(String fileName) {
        try {
            ResourceLocation loc = new ResourceLocation(fileName);
            InputStream in = Config.getResourceStream((ResourceLocation)loc);
            if (in == null) {
                return;
            }
            CustomColors.dbg("Loading " + fileName);
            PropertiesOrdered props = new PropertiesOrdered();
            props.load(in);
            in.close();
            particleWaterColor = CustomColors.readColor((Properties)props, new String[]{"particle.water", "drop.water"});
            particlePortalColor = CustomColors.readColor((Properties)props, "particle.portal");
            lilyPadColor = CustomColors.readColor((Properties)props, "lilypad");
            expBarTextColor = CustomColors.readColor((Properties)props, "text.xpbar");
            bossTextColor = CustomColors.readColor((Properties)props, "text.boss");
            signTextColor = CustomColors.readColor((Properties)props, "text.sign");
            fogColorNether = CustomColors.readColorVec3(props, "fog.nether");
            fogColorEnd = CustomColors.readColorVec3(props, "fog.end");
            skyColorEnd = CustomColors.readColorVec3(props, "sky.end");
            colorsBlockColormaps = CustomColors.readCustomColormaps(props, fileName);
            spawnEggPrimaryColors = CustomColors.readSpawnEggColors(props, fileName, "egg.shell.", "Spawn egg shell");
            spawnEggSecondaryColors = CustomColors.readSpawnEggColors(props, fileName, "egg.spots.", "Spawn egg spot");
            wolfCollarColors = CustomColors.readDyeColors(props, fileName, "collar.", "Wolf collar");
            sheepColors = CustomColors.readDyeColors(props, fileName, "sheep.", "Sheep");
            textColors = CustomColors.readTextColors(props, fileName, "text.code.", "Text");
            int[] mapColors = CustomColors.readMapColors(props, fileName, "map.", "Map");
            if (mapColors != null) {
                if (mapColorsOriginal == null) {
                    mapColorsOriginal = CustomColors.getMapColors();
                }
                CustomColors.setMapColors(mapColors);
            }
            potionColors = CustomColors.readPotionColors(props, fileName, "potion.", "Potion");
            xpOrbTime = Config.parseInt((String)props.getProperty("xporb.time"), (int)-1);
        }
        catch (FileNotFoundException e) {
            return;
        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static CustomColormap[] readCustomColormaps(Properties props, String fileName) {
        ArrayList<CustomColormap> list = new ArrayList<CustomColormap>();
        String palettePrefix = "palette.block.";
        HashMap<String, String> map = new HashMap<String, String>();
        Set keys = props.keySet();
        for (Object key : keys) {
            String value = props.getProperty((String) key);
            if (!((String) key).startsWith(palettePrefix)) continue;
            map.put((String) key, value);
        }
        String[] propNames = map.keySet().toArray(new String[map.size()]);
        for (int i = 0; i < propNames.length; ++i) {
            String name = propNames[i];
            String value = props.getProperty(name);
            CustomColors.dbg("Block palette: " + name + " = " + value);
            String path = name.substring(palettePrefix.length());
            String basePath = TextureUtils.getBasePath(fileName);
            path = TextureUtils.fixResourcePath(path, basePath);
            CustomColormap colors = CustomColors.getCustomColors(path, 256, 256);
            if (colors == null) {
                CustomColors.warn("Colormap not found: " + path);
                continue;
            }
            ConnectedParser cp = new ConnectedParser("CustomColors");
            MatchBlock[] mbs = cp.parseMatchBlocks(value);
            if (mbs == null || mbs.length <= 0) {
                CustomColors.warn("Invalid match blocks: " + value);
                continue;
            }
            for (int m = 0; m < mbs.length; ++m) {
                MatchBlock mb = mbs[m];
                colors.addMatchBlock(mb);
            }
            list.add(colors);
        }
        if (list.size() <= 0) {
            return null;
        }
        CustomColormap[] cms = list.toArray(new CustomColormap[list.size()]);
        return cms;
    }

    private static CustomColormap[][] readBlockColormaps(String[] basePaths, CustomColormap[] basePalettes, int width, int height) {
        int i;
        Object[] paths = ResUtils.collectFiles(basePaths, new String[]{".properties"});
        Arrays.sort(paths);
        ArrayList blockList = new ArrayList();
        for (i = 0; i < paths.length; ++i) {
            Object path = paths[i];
            CustomColors.dbg("Block colormap: " + (String)path);
            try {
                ResourceLocation locFile = new ResourceLocation("minecraft", (String)path);
                InputStream in = Config.getResourceStream((ResourceLocation)locFile);
                if (in == null) {
                    CustomColors.warn("File not found: " + (String)path);
                    continue;
                }
                PropertiesOrdered props = new PropertiesOrdered();
                props.load(in);
                CustomColormap cm = new CustomColormap(props, (String)path, width, height, paletteFormatDefault);
                if (!cm.isValid((String)path) || !cm.isValidMatchBlocks((String)path)) continue;
                CustomColors.addToBlockList(cm, blockList);
                continue;
            }
            catch (FileNotFoundException e) {
                CustomColors.warn("File not found: " + (String)path);
                continue;
            }
            catch (Exception e) {
                e.printStackTrace();
            }
        }
        if (basePalettes != null) {
            for (i = 0; i < basePalettes.length; ++i) {
                CustomColormap cm = basePalettes[i];
                CustomColors.addToBlockList(cm, blockList);
            }
        }
        if (blockList.size() <= 0) {
            return null;
        }
        CustomColormap[][] cmArr = CustomColors.blockListToArray(blockList);
        return cmArr;
    }

    private static void addToBlockList(CustomColormap cm, List blockList) {
        int[] ids = cm.getMatchBlockIds();
        if (ids == null || ids.length <= 0) {
            CustomColors.warn("No match blocks: " + Config.arrayToString((int[])ids));
            return;
        }
        for (int i = 0; i < ids.length; ++i) {
            int blockId = ids[i];
            if (blockId < 0) {
                CustomColors.warn("Invalid block ID: " + blockId);
                continue;
            }
            CustomColors.addToList(cm, blockList, blockId);
        }
    }

    private static void addToList(CustomColormap cm, List list, int id) {
        while (id >= list.size()) {
            list.add(null);
        }
        ArrayList<CustomColormap> subList = (ArrayList<CustomColormap>)list.get(id);
        if (subList == null) {
            subList = new ArrayList<CustomColormap>();
            list.set(id, subList);
        }
        subList.add(cm);
    }

    private static CustomColormap[][] blockListToArray(List list) {
        CustomColormap[][] colArr = new CustomColormap[list.size()][];
        for (int i = 0; i < list.size(); ++i) {
            List subList = (List)list.get(i);
            if (subList == null) continue;
            CustomColormap[] subArr = (CustomColormap[]) subList.toArray(new CustomColormap[subList.size()]);
            colArr[i] = subArr;
        }
        return colArr;
    }

    private static int readColor(Properties props, String[] names) {
        for (int i = 0; i < names.length; ++i) {
            String name = names[i];
            int col = CustomColors.readColor(props, name);
            if (col < 0) continue;
            return col;
        }
        return -1;
    }

    private static int readColor(Properties props, String name) {
        String str = props.getProperty(name);
        if (str == null) {
            return -1;
        }
        int color = CustomColors.parseColor(str = str.trim());
        if (color < 0) {
            CustomColors.warn("Invalid color: " + name + " = " + str);
            return color;
        }
        CustomColors.dbg(name + " = " + str);
        return color;
    }

    private static int parseColor(String str) {
        if (str == null) {
            return -1;
        }
        str = str.trim();
        try {
            int val = Integer.parseInt(str, 16) & 0xFFFFFF;
            return val;
        }
        catch (NumberFormatException e) {
            return -1;
        }
    }

    private static Vec3 readColorVec3(Properties props, String name) {
        int col = CustomColors.readColor(props, name);
        if (col < 0) {
            return null;
        }
        int red = col >> 16 & 0xFF;
        int green = col >> 8 & 0xFF;
        int blue = col & 0xFF;
        float redF = (float)red / 255.0f;
        float greenF = (float)green / 255.0f;
        float blueF = (float)blue / 255.0f;
        return new Vec3((double)redF, (double)greenF, (double)blueF);
    }

    private static CustomColormap getCustomColors(String basePath, String[] paths, int width, int height) {
        for (int i = 0; i < paths.length; ++i) {
            String path = paths[i];
            path = basePath + path;
            CustomColormap cols = CustomColors.getCustomColors(path, width, height);
            if (cols == null) continue;
            return cols;
        }
        return null;
    }

    public static CustomColormap getCustomColors(String pathImage, int width, int height) {
        try {
            ResourceLocation loc = new ResourceLocation(pathImage);
            if (!Config.hasResource((ResourceLocation)loc)) {
                return null;
            }
            CustomColors.dbg("Colormap " + pathImage);
            PropertiesOrdered props = new PropertiesOrdered();
            String pathProps = StrUtils.replaceSuffix(pathImage, ".png", ".properties");
            ResourceLocation locProps = new ResourceLocation(pathProps);
            if (Config.hasResource((ResourceLocation)locProps)) {
                InputStream in = Config.getResourceStream((ResourceLocation)locProps);
                props.load(in);
                in.close();
                CustomColors.dbg("Colormap properties: " + pathProps);
            } else {
                ((Hashtable)props).put("format", paletteFormatDefault);
                ((Hashtable)props).put("source", pathImage);
                pathProps = pathImage;
            }
            CustomColormap cm = new CustomColormap(props, pathProps, width, height, paletteFormatDefault);
            if (!cm.isValid(pathProps)) {
                return null;
            }
            return cm;
        }
        catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public static void updateUseDefaultGrassFoliageColors() {
        useDefaultGrassFoliageColors = foliageBirchColors == null && foliagePineColors == null && swampGrassColors == null && swampFoliageColors == null && Config.isSwampColors() && Config.isSmoothBiomes();
    }

    public static int getColorMultiplier(BakedQuad quad, IBlockState blockState, IBlockAccess blockAccess, BlockPos blockPos, RenderEnv renderEnv) {
        IColorizer colorizer;
        Block block = blockState.getBlock();
        IBlockState bs = renderEnv.getBlockState();
        if (blockColormaps != null) {
            CustomColormap cm;
            if (!quad.hasTintIndex()) {
                if (block == Blocks.grass) {
                    bs = BLOCK_STATE_DIRT;
                }
                if (block == Blocks.redstone_wire) {
                    return -1;
                }
            }
            if (block == Blocks.double_plant && renderEnv.getMetadata() >= 8) {
                blockPos = blockPos.down();
                bs = blockAccess.getBlockState(blockPos);
            }
            if ((cm = CustomColors.getBlockColormap(bs)) != null) {
                if (Config.isSmoothBiomes() && !cm.isColorConstant()) {
                    return CustomColors.getSmoothColorMultiplier(blockState, blockAccess, blockPos, cm, renderEnv.getColorizerBlockPosM());
                }
                return cm.getColor(blockAccess, blockPos);
            }
        }
        if (!quad.hasTintIndex()) {
            return -1;
        }
        if (block == Blocks.waterlily) {
            return CustomColors.getLilypadColorMultiplier(blockAccess, blockPos);
        }
        if (block == Blocks.redstone_wire) {
            return CustomColors.getRedstoneColor(renderEnv.getBlockState());
        }
        if (block instanceof BlockStem) {
            return CustomColors.getStemColorMultiplier(block, blockAccess, blockPos, renderEnv);
        }
        if (useDefaultGrassFoliageColors) {
            return -1;
        }
        int metadata = renderEnv.getMetadata();
        if (block == Blocks.grass || block == Blocks.tallgrass || block == Blocks.double_plant) {
            colorizer = COLORIZER_GRASS;
        } else if (block == Blocks.double_plant) {
            colorizer = COLORIZER_GRASS;
            if (metadata >= 8) {
                blockPos = blockPos.down();
            }
        } else if (block == Blocks.leaves) {
            switch (metadata & 3) {
                case 0: {
                    colorizer = COLORIZER_FOLIAGE;
                    break;
                }
                case 1: {
                    colorizer = COLORIZER_FOLIAGE_PINE;
                    break;
                }
                case 2: {
                    colorizer = COLORIZER_FOLIAGE_BIRCH;
                    break;
                }
                default: {
                    colorizer = COLORIZER_FOLIAGE;
                    break;
                }
            }
        } else if (block == Blocks.leaves2) {
            colorizer = COLORIZER_FOLIAGE;
        } else if (block == Blocks.vine) {
            colorizer = COLORIZER_FOLIAGE;
        } else {
            return -1;
        }
        if (Config.isSmoothBiomes() && !colorizer.isColorConstant()) {
            return CustomColors.getSmoothColorMultiplier(blockState, blockAccess, blockPos, colorizer, renderEnv.getColorizerBlockPosM());
        }
        return colorizer.getColor(bs, blockAccess, blockPos);
    }

    protected static BiomeGenBase getColorBiome(IBlockAccess blockAccess, BlockPos blockPos) {
        BiomeGenBase biome = blockAccess.getBiomeGenForCoords(blockPos);
        if (biome == BiomeGenBase.swampland && !Config.isSwampColors()) {
            biome = BiomeGenBase.plains;
        }
        return biome;
    }

    private static CustomColormap getBlockColormap(IBlockState blockState) {
        if (blockColormaps == null) {
            return null;
        }
        if (!(blockState instanceof BlockStateBase)) {
            return null;
        }
        BlockStateBase bs = (BlockStateBase)blockState;
        int blockId = bs.getBlockId();
        if (blockId < 0 || blockId >= blockColormaps.length) {
            return null;
        }
        CustomColormap[] cms = blockColormaps[blockId];
        if (cms == null) {
            return null;
        }
        for (int i = 0; i < cms.length; ++i) {
            CustomColormap cm = cms[i];
            if (!cm.matchesBlock(bs)) continue;
            return cm;
        }
        return null;
    }

    private static int getSmoothColorMultiplier(IBlockState blockState, IBlockAccess blockAccess, BlockPos blockPos, IColorizer colorizer, BlockPosM blockPosM) {
        int sumRed = 0;
        int sumGreen = 0;
        int sumBlue = 0;
        int x = blockPos.getX();
        int y = blockPos.getY();
        int z = blockPos.getZ();
        BlockPosM posM = blockPosM;
        for (int ix = x - 1; ix <= x + 1; ++ix) {
            for (int iz = z - 1; iz <= z + 1; ++iz) {
                posM.setXyz(ix, y, iz);
                int col = colorizer.getColor(blockState, blockAccess, posM);
                sumRed += col >> 16 & 0xFF;
                sumGreen += col >> 8 & 0xFF;
                sumBlue += col & 0xFF;
            }
        }
        int r = sumRed / 9;
        int g = sumGreen / 9;
        int b = sumBlue / 9;
        return r << 16 | g << 8 | b;
    }

    public static int getFluidColor(IBlockAccess blockAccess, IBlockState blockState, BlockPos blockPos, RenderEnv renderEnv) {
        Block block = blockState.getBlock();
        IColorizer colorizer = CustomColors.getBlockColormap(blockState);
        if (colorizer == null && blockState.getBlock().getMaterial() == Material.water) {
            colorizer = COLORIZER_WATER;
        }
        if (colorizer == null) {
            return block.colorMultiplier(blockAccess, blockPos, 0);
        }
        if (Config.isSmoothBiomes() && !colorizer.isColorConstant()) {
            return CustomColors.getSmoothColorMultiplier(blockState, blockAccess, blockPos, colorizer, renderEnv.getColorizerBlockPosM());
        }
        return colorizer.getColor(blockState, blockAccess, blockPos);
    }

    public static void updatePortalFX(EntityFX fx) {
        if (particlePortalColor < 0) {
            return;
        }
        int col = particlePortalColor;
        int red = col >> 16 & 0xFF;
        int green = col >> 8 & 0xFF;
        int blue = col & 0xFF;
        float redF = (float)red / 255.0f;
        float greenF = (float)green / 255.0f;
        float blueF = (float)blue / 255.0f;
        fx.setRBGColorF(redF, greenF, blueF);
    }

    public static void updateMyceliumFX(EntityFX fx) {
        if (myceliumParticleColors == null) {
            return;
        }
        int col = myceliumParticleColors.getColorRandom();
        int red = col >> 16 & 0xFF;
        int green = col >> 8 & 0xFF;
        int blue = col & 0xFF;
        float redF = (float)red / 255.0f;
        float greenF = (float)green / 255.0f;
        float blueF = (float)blue / 255.0f;
        fx.setRBGColorF(redF, greenF, blueF);
    }

    private static int getRedstoneColor(IBlockState blockState) {
        if (redstoneColors == null) {
            return -1;
        }
        int level = CustomColors.getRedstoneLevel(blockState, 15);
        int col = redstoneColors.getColor(level);
        return col;
    }

    public static void updateReddustFX(EntityFX fx, IBlockAccess blockAccess, double x, double y, double z) {
        if (redstoneColors == null) {
            return;
        }
        IBlockState state = blockAccess.getBlockState(new BlockPos(x, y, z));
        int level = CustomColors.getRedstoneLevel(state, 15);
        int col = redstoneColors.getColor(level);
        int red = col >> 16 & 0xFF;
        int green = col >> 8 & 0xFF;
        int blue = col & 0xFF;
        float redF = (float)red / 255.0f;
        float greenF = (float)green / 255.0f;
        float blueF = (float)blue / 255.0f;
        fx.setRBGColorF(redF, greenF, blueF);
    }

    private static int getRedstoneLevel(IBlockState state, int def) {
        Block block = state.getBlock();
        if (!(block instanceof BlockRedstoneWire)) {
            return def;
        }
        Comparable val = state.getValue((IProperty)BlockRedstoneWire.POWER);
        if (!(val instanceof Integer)) {
            return def;
        }
        Integer valInt = (Integer)val;
        return valInt;
    }

    public static float getXpOrbTimer(float timer) {
        if (xpOrbTime <= 0) {
            return timer;
        }
        float kt = 628.0f / (float)xpOrbTime;
        return timer * kt;
    }

    public static int getXpOrbColor(float timer) {
        if (xpOrbColors == null) {
            return -1;
        }
        int index = (int)Math.round((double)((MathHelper.sin((float)timer) + 1.0f) * (float)(xpOrbColors.getLength() - 1)) / 2.0);
        int col = xpOrbColors.getColor(index);
        return col;
    }

    public static int getDurabilityColor(int dur255) {
        if (durabilityColors == null) {
            return -1;
        }
        int index = dur255 * durabilityColors.getLength() / 255;
        int col = durabilityColors.getColor(index);
        return col;
    }

    public static void updateWaterFX(EntityFX fx, IBlockAccess blockAccess, double x, double y, double z, RenderEnv renderEnv) {
        if (waterColors == null && blockColormaps == null && particleWaterColor < 0) {
            return;
        }
        BlockPos blockPos = new BlockPos(x, y, z);
        renderEnv.reset(BLOCK_STATE_WATER, blockPos);
        int col = CustomColors.getFluidColor(blockAccess, BLOCK_STATE_WATER, blockPos, renderEnv);
        int red = col >> 16 & 0xFF;
        int green = col >> 8 & 0xFF;
        int blue = col & 0xFF;
        float redF = (float)red / 255.0f;
        float greenF = (float)green / 255.0f;
        float blueF = (float)blue / 255.0f;
        if (particleWaterColor >= 0) {
            int redDrop = particleWaterColor >> 16 & 0xFF;
            int greenDrop = particleWaterColor >> 8 & 0xFF;
            int blueDrop = particleWaterColor & 0xFF;
            redF *= (float)redDrop / 255.0f;
            greenF *= (float)greenDrop / 255.0f;
            blueF *= (float)blueDrop / 255.0f;
        }
        fx.setRBGColorF(redF, greenF, blueF);
    }

    private static int getLilypadColorMultiplier(IBlockAccess blockAccess, BlockPos blockPos) {
        if (lilyPadColor < 0) {
            return Blocks.waterlily.colorMultiplier(blockAccess, blockPos);
        }
        return lilyPadColor;
    }

    private static Vec3 getFogColorNether(Vec3 col) {
        if (fogColorNether == null) {
            return col;
        }
        return fogColorNether;
    }

    private static Vec3 getFogColorEnd(Vec3 col) {
        if (fogColorEnd == null) {
            return col;
        }
        return fogColorEnd;
    }

    private static Vec3 getSkyColorEnd(Vec3 col) {
        if (skyColorEnd == null) {
            return col;
        }
        return skyColorEnd;
    }

    public static Vec3 getSkyColor(Vec3 skyColor3d, IBlockAccess blockAccess, double x, double y, double z) {
        if (skyColors == null) {
            return skyColor3d;
        }
        int col = skyColors.getColorSmooth(blockAccess, x, y, z, 3);
        int red = col >> 16 & 0xFF;
        int green = col >> 8 & 0xFF;
        int blue = col & 0xFF;
        float redF = (float)red / 255.0f;
        float greenF = (float)green / 255.0f;
        float blueF = (float)blue / 255.0f;
        float cRed = (float)skyColor3d.xCoord / 0.5f;
        float cGreen = (float)skyColor3d.yCoord / 0.66275f;
        float cBlue = (float)skyColor3d.zCoord;
        Vec3 newCol = skyColorFader.getColor(redF *= cRed, greenF *= cGreen, blueF *= cBlue);
        return newCol;
    }

    private static Vec3 getFogColor(Vec3 fogColor3d, IBlockAccess blockAccess, double x, double y, double z) {
        if (fogColors == null) {
            return fogColor3d;
        }
        int col = fogColors.getColorSmooth(blockAccess, x, y, z, 3);
        int red = col >> 16 & 0xFF;
        int green = col >> 8 & 0xFF;
        int blue = col & 0xFF;
        float redF = (float)red / 255.0f;
        float greenF = (float)green / 255.0f;
        float blueF = (float)blue / 255.0f;
        float cRed = (float)fogColor3d.xCoord / 0.753f;
        float cGreen = (float)fogColor3d.yCoord / 0.8471f;
        float cBlue = (float)fogColor3d.zCoord;
        Vec3 newCol = fogColorFader.getColor(redF *= cRed, greenF *= cGreen, blueF *= cBlue);
        return newCol;
    }

    public static Vec3 getUnderwaterColor(IBlockAccess blockAccess, double x, double y, double z) {
        return CustomColors.getUnderFluidColor(blockAccess, x, y, z, underwaterColors, underwaterColorFader);
    }

    public static Vec3 getUnderlavaColor(IBlockAccess blockAccess, double x, double y, double z) {
        return CustomColors.getUnderFluidColor(blockAccess, x, y, z, underlavaColors, underlavaColorFader);
    }

    public static Vec3 getUnderFluidColor(IBlockAccess blockAccess, double x, double y, double z, CustomColormap underFluidColors, CustomColorFader underFluidColorFader) {
        if (underFluidColors == null) {
            return null;
        }
        int col = underFluidColors.getColorSmooth(blockAccess, x, y, z, 3);
        int red = col >> 16 & 0xFF;
        int green = col >> 8 & 0xFF;
        int blue = col & 0xFF;
        float redF = (float)red / 255.0f;
        float greenF = (float)green / 255.0f;
        float blueF = (float)blue / 255.0f;
        Vec3 newCol = underFluidColorFader.getColor(redF, greenF, blueF);
        return newCol;
    }

    private static int getStemColorMultiplier(Block blockStem, IBlockAccess blockAccess, BlockPos blockPos, RenderEnv renderEnv) {
        CustomColormap colors = stemColors;
        if (blockStem == Blocks.pumpkin_stem && stemPumpkinColors != null) {
            colors = stemPumpkinColors;
        }
        if (blockStem == Blocks.melon_stem && stemMelonColors != null) {
            colors = stemMelonColors;
        }
        if (colors == null) {
            return -1;
        }
        int level = renderEnv.getMetadata();
        return colors.getColor(level);
    }

    public static boolean updateLightmap(World world, float torchFlickerX, int[] lmColors, boolean nightvision, float partialTicks) {
        if (world == null) {
            return false;
        }
        if (lightMapPacks == null) {
            return false;
        }
        int dimensionId = world.provider.getDimensionId();
        int lightMapIndex = dimensionId - lightmapMinDimensionId;
        if (lightMapIndex < 0 || lightMapIndex >= lightMapPacks.length) {
            return false;
        }
        LightMapPack lightMapPack = lightMapPacks[lightMapIndex];
        if (lightMapPack == null) {
            return false;
        }
        return lightMapPack.updateLightmap(world, torchFlickerX, lmColors, nightvision, partialTicks);
    }

    public static Vec3 getWorldFogColor(Vec3 fogVec, World world, Entity renderViewEntity, float partialTicks) {
        int worldType = world.provider.getDimensionId();
        switch (worldType) {
            case -1: {
                fogVec = CustomColors.getFogColorNether(fogVec);
                break;
            }
            case 0: {
                Minecraft mc = Minecraft.getMinecraft();
                fogVec = CustomColors.getFogColor(fogVec, (IBlockAccess)mc.theWorld, renderViewEntity.posX, renderViewEntity.posY + 1.0, renderViewEntity.posZ);
                break;
            }
            case 1: {
                fogVec = CustomColors.getFogColorEnd(fogVec);
            }
        }
        return fogVec;
    }

    public static Vec3 getWorldSkyColor(Vec3 skyVec, World world, Entity renderViewEntity, float partialTicks) {
        int worldType = world.provider.getDimensionId();
        switch (worldType) {
            case 0: {
                Minecraft mc = Minecraft.getMinecraft();
                skyVec = CustomColors.getSkyColor(skyVec, (IBlockAccess)mc.theWorld, renderViewEntity.posX, renderViewEntity.posY + 1.0, renderViewEntity.posZ);
                break;
            }
            case 1: {
                skyVec = CustomColors.getSkyColorEnd(skyVec);
            }
        }
        return skyVec;
    }

    private static int[] readSpawnEggColors(Properties props, String fileName, String prefix, String logName) {
        ArrayList<Integer> list = new ArrayList<Integer>();
        Set keys = props.keySet();
        int countColors = 0;
        for (Object key : keys) {
            String value = props.getProperty((String) key);
            if (!((String) key).startsWith(prefix)) continue;
            String name = StrUtils.removePrefix((String) key, prefix);
            int id = EntityUtils.getEntityIdByName(name);
            if (id < 0) {
                CustomColors.warn("Invalid spawn egg name: " + key);
                continue;
            }
            int color = CustomColors.parseColor(value);
            if (color < 0) {
                CustomColors.warn("Invalid spawn egg color: " + key + " = " + value);
                continue;
            }
            while (list.size() <= id) {
                list.add(-1);
            }
            list.set(id, color);
            ++countColors;
        }
        if (countColors <= 0) {
            return null;
        }
        CustomColors.dbg(logName + " colors: " + countColors);
        int[] colors = new int[list.size()];
        for (int i = 0; i < colors.length; ++i) {
            colors[i] = (Integer)list.get(i);
        }
        return colors;
    }

    private static int getSpawnEggColor(ItemMonsterPlacer item, ItemStack itemStack, int layer, int color) {
        int[] eggColors;
        int id = itemStack.getMetadata();
        int[] arrn = eggColors = layer == 0 ? spawnEggPrimaryColors : spawnEggSecondaryColors;
        if (eggColors == null) {
            return color;
        }
        if (id < 0 || id >= eggColors.length) {
            return color;
        }
        int eggColor = eggColors[id];
        if (eggColor < 0) {
            return color;
        }
        return eggColor;
    }

    public static int getColorFromItemStack(ItemStack itemStack, int layer, int color) {
        if (itemStack == null) {
            return color;
        }
        Item item = itemStack.getItem();
        if (item == null) {
            return color;
        }
        if (item instanceof ItemMonsterPlacer) {
            return CustomColors.getSpawnEggColor((ItemMonsterPlacer)item, itemStack, layer, color);
        }
        return color;
    }

    private static float[][] readDyeColors(Properties props, String fileName, String prefix, String logName) {
        EnumDyeColor[] dyeValues = EnumDyeColor.values();
        HashMap<String, EnumDyeColor> mapDyes = new HashMap<String, EnumDyeColor>();
        for (int i = 0; i < dyeValues.length; ++i) {
            EnumDyeColor dye = dyeValues[i];
            mapDyes.put(dye.getName(), dye);
        }
        float[][] colors = new float[dyeValues.length][];
        int countColors = 0;
        Set keys = props.keySet();
        for (Object key : keys) {
            String value = props.getProperty((String) key);
            if (!((String) key).startsWith(prefix)) continue;
            String name = StrUtils.removePrefix((String) key, prefix);
            if (name.equals("lightBlue")) {
                name = "light_blue";
            }
            EnumDyeColor dye = (EnumDyeColor)mapDyes.get(name);
            int color = CustomColors.parseColor(value);
            if (dye == null || color < 0) {
                CustomColors.warn("Invalid color: " + key + " = " + value);
                continue;
            }
            float[] rgb = new float[]{(float)(color >> 16 & 0xFF) / 255.0f, (float)(color >> 8 & 0xFF) / 255.0f, (float)(color & 0xFF) / 255.0f};
            colors[dye.ordinal()] = rgb;
            ++countColors;
        }
        if (countColors <= 0) {
            return null;
        }
        CustomColors.dbg(logName + " colors: " + countColors);
        return colors;
    }

    private static float[] getDyeColors(EnumDyeColor dye, float[][] dyeColors, float[] colors) {
        if (dyeColors == null) {
            return colors;
        }
        if (dye == null) {
            return colors;
        }
        float[] customColors = dyeColors[dye.ordinal()];
        if (customColors == null) {
            return colors;
        }
        return customColors;
    }

    public static float[] getWolfCollarColors(EnumDyeColor dye, float[] colors) {
        return CustomColors.getDyeColors(dye, wolfCollarColors, colors);
    }

    public static float[] getSheepColors(EnumDyeColor dye, float[] colors) {
        return CustomColors.getDyeColors(dye, sheepColors, colors);
    }

    private static int[] readTextColors(Properties props, String fileName, String prefix, String logName) {
        int[] colors = new int[32];
        Arrays.fill(colors, -1);
        int countColors = 0;
        Set keys = props.keySet();
        for (Object key : keys) {
            String value = props.getProperty((String) key);
            if (!((String) key).startsWith(prefix)) continue;
            String name = StrUtils.removePrefix((String) key, prefix);
            int code = Config.parseInt((String)name, (int)-1);
            int color = CustomColors.parseColor(value);
            if (code < 0 || code >= colors.length || color < 0) {
                CustomColors.warn("Invalid color: " + key + " = " + value);
                continue;
            }
            colors[code] = color;
            ++countColors;
        }
        if (countColors <= 0) {
            return null;
        }
        CustomColors.dbg(logName + " colors: " + countColors);
        return colors;
    }

    public static int getTextColor(int index, int color) {
        if (textColors == null) {
            return color;
        }
        if (index < 0 || index >= textColors.length) {
            return color;
        }
        int customColor = textColors[index];
        if (customColor < 0) {
            return color;
        }
        return customColor;
    }

    private static int[] readMapColors(Properties props, String fileName, String prefix, String logName) {
        int[] colors = new int[MapColor.mapColorArray.length];
        Arrays.fill(colors, -1);
        int countColors = 0;
        Set keys = props.keySet();
        for (Object key : keys) {
            String value = props.getProperty((String) key);
            if (!((String) key).startsWith(prefix)) continue;
            String name = StrUtils.removePrefix((String) key, prefix);
            int index = CustomColors.getMapColorIndex(name);
            int color = CustomColors.parseColor(value);
            if (index < 0 || index >= colors.length || color < 0) {
                CustomColors.warn("Invalid color: " + key + " = " + value);
                continue;
            }
            colors[index] = color;
            ++countColors;
        }
        if (countColors <= 0) {
            return null;
        }
        CustomColors.dbg(logName + " colors: " + countColors);
        return colors;
    }

    private static int[] readPotionColors(Properties props, String fileName, String prefix, String logName) {
        int[] colors = new int[Potion.potionTypes.length];
        Arrays.fill(colors, -1);
        int countColors = 0;
        Set keys = props.keySet();
        for (Object key : keys) {
            String value = props.getProperty((String) key);
            if (!((String) key).startsWith(prefix)) continue;
            String name = (String) key;
            int index = CustomColors.getPotionId(name);
            int color = CustomColors.parseColor(value);
            if (index < 0 || index >= colors.length || color < 0) {
                CustomColors.warn("Invalid color: " + key + " = " + value);
                continue;
            }
            colors[index] = color;
            ++countColors;
        }
        if (countColors <= 0) {
            return null;
        }
        CustomColors.dbg(logName + " colors: " + countColors);
        return colors;
    }

    private static int getPotionId(String name) {
        if (name.equals("potion.water")) {
            return 0;
        }
        Potion[] potions = Potion.potionTypes;
        for (int i = 0; i < potions.length; ++i) {
            Potion potion = potions[i];
            if (potion == null || !potion.getName().equals(name)) continue;
            return potion.getId();
        }
        return -1;
    }

    public static int getPotionColor(int potionId, int color) {
        if (potionColors == null) {
            return color;
        }
        if (potionId < 0 || potionId >= potionColors.length) {
            return color;
        }
        int potionColor = potionColors[potionId];
        if (potionColor < 0) {
            return color;
        }
        return potionColor;
    }

    private static int getMapColorIndex(String name) {
        if (name == null) {
            return -1;
        }
        if (name.equals("air")) {
            return MapColor.airColor.colorIndex;
        }
        if (name.equals("grass")) {
            return MapColor.grassColor.colorIndex;
        }
        if (name.equals("sand")) {
            return MapColor.sandColor.colorIndex;
        }
        if (name.equals("cloth")) {
            return MapColor.clothColor.colorIndex;
        }
        if (name.equals("tnt")) {
            return MapColor.tntColor.colorIndex;
        }
        if (name.equals("ice")) {
            return MapColor.iceColor.colorIndex;
        }
        if (name.equals("iron")) {
            return MapColor.ironColor.colorIndex;
        }
        if (name.equals("foliage")) {
            return MapColor.foliageColor.colorIndex;
        }
        if (name.equals("clay")) {
            return MapColor.clayColor.colorIndex;
        }
        if (name.equals("dirt")) {
            return MapColor.dirtColor.colorIndex;
        }
        if (name.equals("stone")) {
            return MapColor.stoneColor.colorIndex;
        }
        if (name.equals("water")) {
            return MapColor.waterColor.colorIndex;
        }
        if (name.equals("wood")) {
            return MapColor.woodColor.colorIndex;
        }
        if (name.equals("quartz")) {
            return MapColor.quartzColor.colorIndex;
        }
        if (name.equals("gold")) {
            return MapColor.goldColor.colorIndex;
        }
        if (name.equals("diamond")) {
            return MapColor.diamondColor.colorIndex;
        }
        if (name.equals("lapis")) {
            return MapColor.lapisColor.colorIndex;
        }
        if (name.equals("emerald")) {
            return MapColor.emeraldColor.colorIndex;
        }
        if (name.equals("podzol")) {
            return MapColor.obsidianColor.colorIndex;
        }
        if (name.equals("netherrack")) {
            return MapColor.netherrackColor.colorIndex;
        }
        if (name.equals("snow") || name.equals("white")) {
            return MapColor.snowColor.colorIndex;
        }
        if (name.equals("adobe") || name.equals("orange")) {
            return MapColor.adobeColor.colorIndex;
        }
        if (name.equals("magenta")) {
            return MapColor.magentaColor.colorIndex;
        }
        if (name.equals("light_blue") || name.equals("lightBlue")) {
            return MapColor.lightBlueColor.colorIndex;
        }
        if (name.equals("yellow")) {
            return MapColor.yellowColor.colorIndex;
        }
        if (name.equals("lime")) {
            return MapColor.limeColor.colorIndex;
        }
        if (name.equals("pink")) {
            return MapColor.pinkColor.colorIndex;
        }
        if (name.equals("gray")) {
            return MapColor.grayColor.colorIndex;
        }
        if (name.equals("silver")) {
            return MapColor.silverColor.colorIndex;
        }
        if (name.equals("cyan")) {
            return MapColor.cyanColor.colorIndex;
        }
        if (name.equals("purple")) {
            return MapColor.purpleColor.colorIndex;
        }
        if (name.equals("blue")) {
            return MapColor.blueColor.colorIndex;
        }
        if (name.equals("brown")) {
            return MapColor.brownColor.colorIndex;
        }
        if (name.equals("green")) {
            return MapColor.greenColor.colorIndex;
        }
        if (name.equals("red")) {
            return MapColor.redColor.colorIndex;
        }
        if (name.equals("black")) {
            return MapColor.blackColor.colorIndex;
        }
        return -1;
    }

    private static int[] getMapColors() {
        MapColor[] mapColors = MapColor.mapColorArray;
        int[] colors = new int[mapColors.length];
        Arrays.fill(colors, -1);
        for (int i = 0; i < mapColors.length && i < colors.length; ++i) {
            MapColor mapColor = mapColors[i];
            if (mapColor == null) continue;
            colors[i] = mapColor.colorValue;
        }
        return colors;
    }

    private static void setMapColors(int[] colors) {
        if (colors == null) {
            return;
        }
        MapColor[] mapColors = MapColor.mapColorArray;
        boolean changed = false;
        for (int i = 0; i < mapColors.length && i < colors.length; ++i) {
            int color;
            MapColor mapColor = mapColors[i];
            if (mapColor == null || (color = colors[i]) < 0 || mapColor.colorValue == color) continue;
            mapColor.colorValue = color;
            changed = true;
        }
        if (changed) {
            Minecraft.getMinecraft().getTextureManager().reloadBannerTextures();
        }
    }

    private static void dbg(String str) {
        Config.dbg((String)("CustomColors: " + str));
    }

    private static void warn(String str) {
        Config.warn((String)("CustomColors: " + str));
    }

    public static int getExpBarTextColor(int color) {
        if (expBarTextColor < 0) {
            return color;
        }
        return expBarTextColor;
    }

    public static int getBossTextColor(int color) {
        if (bossTextColor < 0) {
            return color;
        }
        return bossTextColor;
    }

    public static int getSignTextColor(int color) {
        if (signTextColor < 0) {
            return color;
        }
        return signTextColor;
    }

    public static interface IColorizer {
        public int getColor(IBlockState var1, IBlockAccess var2, BlockPos var3);

        public boolean isColorConstant();
    }

}

