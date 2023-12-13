/*
 * Decompiled with CFR 0.150.
 * 
 * Could not load the following classes:
 *  org.apache.commons.lang3.tuple.ImmutablePair
 *  org.apache.commons.lang3.tuple.Pair
 */
package optifine;

import java.awt.image.BufferedImage;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Properties;
import java.util.Random;
import java.util.Set;
import javax.imageio.ImageIO;
import net.minecraft.block.Block;
import net.minecraft.block.BlockRedstoneWire;
import net.minecraft.block.BlockStem;
import net.minecraft.block.material.MapColor;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.BlockStateBase;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.WorldClient;
import net.minecraft.client.particle.EntityFX;
import net.minecraft.client.renderer.block.model.BakedQuad;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityList;
import net.minecraft.init.Blocks;
import net.minecraft.item.EnumDyeColor;
import net.minecraft.item.Item;
import net.minecraft.item.ItemMonsterPlacer;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionHelper;
import net.minecraft.util.BlockPos;
import net.minecraft.util.MathHelper;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.Vec3;
import net.minecraft.world.ColorizerFoliage;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraft.world.biome.BiomeGenBase;
import optifine.BlockPosM;
import optifine.Config;
import optifine.ConnectedParser;
import optifine.CustomColorFader;
import optifine.CustomColormap;
import optifine.MatchBlock;
import optifine.Reflector;
import optifine.RenderEnv;
import optifine.ResUtils;
import optifine.StrUtils;
import optifine.TextureUtils;
import org.apache.commons.lang3.tuple.ImmutablePair;
import org.apache.commons.lang3.tuple.Pair;

public class CustomColors {
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
    private static CustomColormap[] lightMapsColorsRgb = null;
    private static int lightmapMinDimensionId = 0;
    private static float[][] sunRgbs = new float[16][3];
    private static float[][] torchRgbs = new float[16][3];
    private static CustomColormap redstoneColors = null;
    private static CustomColormap xpOrbColors = null;
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
        public int getColor(IBlockAccess blockAccess, BlockPos blockPos) {
            BiomeGenBase biome = CustomColors.getColorBiome(blockAccess, blockPos);
            return swampGrassColors != null && biome == BiomeGenBase.swampland ? swampGrassColors.getColor(biome, blockPos) : biome.func_180627_b(blockPos);
        }

        @Override
        public boolean isColorConstant() {
            return false;
        }
    };
    private static final IColorizer COLORIZER_FOLIAGE = new IColorizer(){

        @Override
        public int getColor(IBlockAccess blockAccess, BlockPos blockPos) {
            BiomeGenBase biome = CustomColors.getColorBiome(blockAccess, blockPos);
            return swampFoliageColors != null && biome == BiomeGenBase.swampland ? swampFoliageColors.getColor(biome, blockPos) : biome.func_180625_c(blockPos);
        }

        @Override
        public boolean isColorConstant() {
            return false;
        }
    };
    private static final IColorizer COLORIZER_FOLIAGE_PINE = new IColorizer(){

        @Override
        public int getColor(IBlockAccess blockAccess, BlockPos blockPos) {
            return foliagePineColors != null ? foliagePineColors.getColor(blockAccess, blockPos) : ColorizerFoliage.getFoliageColorPine();
        }

        @Override
        public boolean isColorConstant() {
            return foliagePineColors == null;
        }
    };
    private static final IColorizer COLORIZER_FOLIAGE_BIRCH = new IColorizer(){

        @Override
        public int getColor(IBlockAccess blockAccess, BlockPos blockPos) {
            return foliageBirchColors != null ? foliageBirchColors.getColor(blockAccess, blockPos) : ColorizerFoliage.getFoliageColorBirch();
        }

        @Override
        public boolean isColorConstant() {
            return foliageBirchColors == null;
        }
    };
    private static final IColorizer COLORIZER_WATER = new IColorizer(){

        @Override
        public int getColor(IBlockAccess blockAccess, BlockPos blockPos) {
            BiomeGenBase biome = CustomColors.getColorBiome(blockAccess, blockPos);
            return waterColors != null ? waterColors.getColor(biome, blockPos) : (Reflector.ForgeBiomeGenBase_getWaterColorMultiplier.exists() ? Reflector.callInt(biome, Reflector.ForgeBiomeGenBase_getWaterColorMultiplier, new Object[0]) : biome.waterColorMultiplier);
        }

        @Override
        public boolean isColorConstant() {
            return false;
        }
    };

    public static void update() {
        waterColors = null;
        foliageBirchColors = null;
        foliagePineColors = null;
        swampGrassColors = null;
        swampFoliageColors = null;
        skyColors = null;
        fogColors = null;
        underwaterColors = null;
        redstoneColors = null;
        xpOrbColors = null;
        stemColors = null;
        myceliumParticleColors = null;
        lightMapsColorsRgb = null;
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
        PotionHelper.clearPotionColorCache();
        String mcpColormap = "mcpatcher/colormap/";
        String[] waterPaths = new String[]{"water.png", "watercolorX.png"};
        waterColors = CustomColors.getCustomColors(mcpColormap, waterPaths, 256, 256);
        CustomColors.updateUseDefaultGrassFoliageColors();
        if (Config.isCustomColors()) {
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
            String[] redstonePaths = new String[]{"redstone.png", "redstonecolor.png"};
            redstoneColors = CustomColors.getCustomColors(mcpColormap, redstonePaths, 16, 1);
            xpOrbColors = CustomColors.getCustomColors(String.valueOf(mcpColormap) + "xporb.png", -1, -1);
            String[] stemPaths = new String[]{"stem.png", "stemcolor.png"};
            stemColors = CustomColors.getCustomColors(mcpColormap, stemPaths, 8, 1);
            stemPumpkinColors = CustomColors.getCustomColors(String.valueOf(mcpColormap) + "pumpkinstem.png", 8, 1);
            stemMelonColors = CustomColors.getCustomColors(String.valueOf(mcpColormap) + "melonstem.png", 8, 1);
            String[] myceliumPaths = new String[]{"myceliumparticle.png", "myceliumparticlecolor.png"};
            myceliumParticleColors = CustomColors.getCustomColors(mcpColormap, myceliumPaths, -1, -1);
            Pair<CustomColormap[], Integer> lightMaps = CustomColors.parseLightmapsRgb();
            lightMapsColorsRgb = (CustomColormap[])lightMaps.getLeft();
            lightmapMinDimensionId = (Integer)lightMaps.getRight();
            CustomColors.readColorProperties("mcpatcher/color.properties");
            blockColormaps = CustomColors.readBlockColormaps(new String[]{String.valueOf(mcpColormap) + "custom/", String.valueOf(mcpColormap) + "blocks/"}, colorsBlockColormaps, 256, 256);
            CustomColors.updateUseDefaultGrassFoliageColors();
        }
    }

    private static Pair<CustomColormap[], Integer> parseLightmapsRgb() {
        int maxDimId;
        String lightmapPrefix = "mcpatcher/lightmap/world";
        String lightmapSuffix = ".png";
        String[] pathsLightmap = ResUtils.collectFiles(lightmapPrefix, lightmapSuffix);
        HashMap<Integer, String> mapLightmaps = new HashMap<Integer, String>();
        for (int setDimIds = 0; setDimIds < pathsLightmap.length; ++setDimIds) {
            String dimIds = pathsLightmap[setDimIds];
            String minDimId = StrUtils.removePrefixSuffix(dimIds, lightmapPrefix, lightmapSuffix);
            maxDimId = Config.parseInt(minDimId, Integer.MIN_VALUE);
            if (maxDimId == Integer.MIN_VALUE) {
                CustomColors.warn("Invalid dimension ID: " + minDimId + ", path: " + dimIds);
                continue;
            }
            mapLightmaps.put(maxDimId, dimIds);
        }
        Set var15 = mapLightmaps.keySet();
        Object[] var16 = var15.toArray(new Integer[var15.size()]);
        Arrays.sort(var16);
        if (var16.length <= 0) {
            return new ImmutablePair(null, (Object)0);
        }
        int var17 = (Integer)var16[0];
        maxDimId = (Integer)var16[var16.length - 1];
        int countDim = maxDimId - var17 + 1;
        CustomColormap[] colormaps = new CustomColormap[countDim];
        for (int i = 0; i < var16.length; ++i) {
            Object dimId = var16[i];
            String path = (String)mapLightmaps.get(dimId);
            CustomColormap colors = CustomColors.getCustomColors(path, -1, -1);
            if (colors == null) continue;
            if (colors.getWidth() < 16) {
                CustomColors.warn("Invalid lightmap width: " + colors.getWidth() + ", path: " + path);
                continue;
            }
            int lightmapIndex = (Integer)dimId - var17;
            colormaps[lightmapIndex] = colors;
        }
        return new ImmutablePair((Object)colormaps, (Object)var17);
    }

    private static int getTextureHeight(String path, int defHeight) {
        try {
            InputStream e = Config.getResourceStream(new ResourceLocation(path));
            if (e == null) {
                return defHeight;
            }
            BufferedImage bi = ImageIO.read(e);
            e.close();
            return bi == null ? defHeight : bi.getHeight();
        }
        catch (IOException var4) {
            return defHeight;
        }
    }

    private static void readColorProperties(String fileName) {
        try {
            ResourceLocation e = new ResourceLocation(fileName);
            InputStream in = Config.getResourceStream(e);
            if (in == null) {
                return;
            }
            CustomColors.dbg("Loading " + fileName);
            Properties props = new Properties();
            props.load(in);
            in.close();
            particleWaterColor = CustomColors.readColor(props, new String[]{"particle.water", "drop.water"});
            particlePortalColor = CustomColors.readColor(props, "particle.portal");
            lilyPadColor = CustomColors.readColor(props, "lilypad");
            expBarTextColor = CustomColors.readColor(props, "text.xpbar");
            bossTextColor = CustomColors.readColor(props, "text.boss");
            signTextColor = CustomColors.readColor(props, "text.sign");
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
        }
        catch (FileNotFoundException var5) {
            return;
        }
        catch (IOException var6) {
            var6.printStackTrace();
        }
    }

    private static CustomColormap[] readCustomColormaps(Properties props, String fileName) {
        String name;
        ArrayList<CustomColormap> list = new ArrayList<CustomColormap>();
        String palettePrefix = "palette.block.";
        HashMap<String, String> map = new HashMap<String, String>();
        Set<Object> keys = props.keySet();
        for (String string : keys) {
            name = props.getProperty(string);
            if (!string.startsWith(palettePrefix)) continue;
            map.put(string, name);
        }
        String[] arrstring = map.keySet().toArray(new String[map.size()]);
        for (int var18 = 0; var18 < arrstring.length; ++var18) {
            name = arrstring[var18];
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
            if (mbs != null && mbs.length > 0) {
                for (int m = 0; m < mbs.length; ++m) {
                    MatchBlock mb = mbs[m];
                    colors.addMatchBlock(mb);
                }
                list.add(colors);
                continue;
            }
            CustomColors.warn("Invalid match blocks: " + value);
        }
        if (list.size() <= 0) {
            return null;
        }
        CustomColormap[] var19 = list.toArray(new CustomColormap[list.size()]);
        return var19;
    }

    private static CustomColormap[][] readBlockColormaps(String[] basePaths, CustomColormap[] basePalettes, int width, int height) {
        int cmArr;
        Object[] paths = ResUtils.collectFiles(basePaths, new String[]{".properties"});
        Arrays.sort(paths);
        ArrayList blockList = new ArrayList();
        for (cmArr = 0; cmArr < paths.length; ++cmArr) {
            Object cm = paths[cmArr];
            CustomColors.dbg("Block colormap: " + (String)cm);
            try {
                ResourceLocation e = new ResourceLocation("minecraft", (String)cm);
                InputStream in = Config.getResourceStream(e);
                if (in == null) {
                    CustomColors.warn("File not found: " + (String)cm);
                    continue;
                }
                Properties props = new Properties();
                props.load(in);
                CustomColormap cm1 = new CustomColormap(props, (String)cm, width, height);
                if (!cm1.isValid((String)cm) || !cm1.isValidMatchBlocks((String)cm)) continue;
                CustomColors.addToBlockList(cm1, blockList);
                continue;
            }
            catch (FileNotFoundException var12) {
                CustomColors.warn("File not found: " + (String)cm);
                continue;
            }
            catch (Exception var13) {
                var13.printStackTrace();
            }
        }
        if (basePalettes != null) {
            for (cmArr = 0; cmArr < basePalettes.length; ++cmArr) {
                CustomColormap var15 = basePalettes[cmArr];
                CustomColors.addToBlockList(var15, blockList);
            }
        }
        if (blockList.size() <= 0) {
            return null;
        }
        CustomColormap[][] var14 = CustomColors.blockListToArray(blockList);
        return var14;
    }

    private static void addToBlockList(CustomColormap cm, List blockList) {
        int[] ids = cm.getMatchBlockIds();
        if (ids != null && ids.length > 0) {
            for (int i = 0; i < ids.length; ++i) {
                int blockId = ids[i];
                if (blockId < 0) {
                    CustomColors.warn("Invalid block ID: " + blockId);
                    continue;
                }
                CustomColors.addToList(cm, blockList, blockId);
            }
        } else {
            CustomColors.warn("No match blocks: " + Config.arrayToString(ids));
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
            CustomColormap[] subArr = subList.toArray(new CustomColormap[subList.size()]);
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
        CustomColors.dbg(String.valueOf(name) + " = " + str);
        return color;
    }

    private static int parseColor(String str) {
        if (str == null) {
            return -1;
        }
        str = str.trim();
        try {
            int e = Integer.parseInt(str, 16) & 0xFFFFFF;
            return e;
        }
        catch (NumberFormatException var2) {
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
        return new Vec3(redF, greenF, blueF);
    }

    private static CustomColormap getCustomColors(String basePath, String[] paths, int width, int height) {
        for (int i = 0; i < paths.length; ++i) {
            String path = paths[i];
            path = String.valueOf(basePath) + path;
            CustomColormap cols = CustomColors.getCustomColors(path, width, height);
            if (cols == null) continue;
            return cols;
        }
        return null;
    }

    public static CustomColormap getCustomColors(String pathImage, int width, int height) {
        block5: {
            try {
                ResourceLocation e = new ResourceLocation(pathImage);
                if (Config.hasResource(e)) break block5;
                return null;
            }
            catch (Exception var8) {
                var8.printStackTrace();
                return null;
            }
        }
        CustomColors.dbg("Colormap " + pathImage);
        Properties props = new Properties();
        String pathProps = StrUtils.replaceSuffix(pathImage, ".png", ".properties");
        ResourceLocation locProps = new ResourceLocation(pathProps);
        if (Config.hasResource(locProps)) {
            InputStream cm = Config.getResourceStream(locProps);
            props.load(cm);
            cm.close();
            CustomColors.dbg("Colormap properties: " + pathProps);
        } else {
            props.put("format", "vanilla");
            props.put("source", pathImage);
            pathProps = pathImage;
        }
        CustomColormap cm1 = new CustomColormap(props, pathProps, width, height);
        return !cm1.isValid(pathProps) ? null : cm1;
    }

    public static void updateUseDefaultGrassFoliageColors() {
        useDefaultGrassFoliageColors = foliageBirchColors == null && foliagePineColors == null && swampGrassColors == null && swampFoliageColors == null && Config.isSwampColors() && Config.isSmoothBiomes();
    }

    public static int getColorMultiplier(BakedQuad quad, Block block, IBlockAccess blockAccess, BlockPos blockPos, RenderEnv renderEnv) {
        IColorizer colorizer1;
        if (blockColormaps != null) {
            CustomColormap colorizer;
            IBlockState metadata = renderEnv.getBlockState();
            if (!quad.func_178212_b()) {
                if (block == Blocks.grass) {
                    metadata = BLOCK_STATE_DIRT;
                }
                if (block == Blocks.redstone_wire) {
                    return -1;
                }
            }
            if (block == Blocks.double_plant && renderEnv.getMetadata() >= 8) {
                blockPos = blockPos.offsetDown();
                metadata = blockAccess.getBlockState(blockPos);
            }
            if ((colorizer = CustomColors.getBlockColormap(metadata)) != null) {
                if (Config.isSmoothBiomes() && !colorizer.isColorConstant()) {
                    return CustomColors.getSmoothColorMultiplier(blockAccess, blockPos, colorizer, renderEnv.getColorizerBlockPosM());
                }
                return colorizer.getColor(blockAccess, blockPos);
            }
        }
        if (!quad.func_178212_b()) {
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
        int metadata1 = renderEnv.getMetadata();
        if (block != Blocks.grass && block != Blocks.tallgrass && block != Blocks.double_plant) {
            if (block == Blocks.double_plant) {
                colorizer1 = COLORIZER_GRASS;
                if (metadata1 >= 8) {
                    blockPos = blockPos.offsetDown();
                }
            } else if (block == Blocks.leaves) {
                switch (metadata1 & 3) {
                    case 0: {
                        colorizer1 = COLORIZER_FOLIAGE;
                        break;
                    }
                    case 1: {
                        colorizer1 = COLORIZER_FOLIAGE_PINE;
                        break;
                    }
                    case 2: {
                        colorizer1 = COLORIZER_FOLIAGE_BIRCH;
                        break;
                    }
                    default: {
                        colorizer1 = COLORIZER_FOLIAGE;
                        break;
                    }
                }
            } else if (block == Blocks.leaves2) {
                colorizer1 = COLORIZER_FOLIAGE;
            } else {
                if (block != Blocks.vine) {
                    return -1;
                }
                colorizer1 = COLORIZER_FOLIAGE;
            }
        } else {
            colorizer1 = COLORIZER_GRASS;
        }
        return Config.isSmoothBiomes() && !colorizer1.isColorConstant() ? CustomColors.getSmoothColorMultiplier(blockAccess, blockPos, colorizer1, renderEnv.getColorizerBlockPosM()) : colorizer1.getColor(blockAccess, blockPos);
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
        if (blockId >= 0 && blockId < blockColormaps.length) {
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
        return null;
    }

    private static int getSmoothColorMultiplier(IBlockAccess blockAccess, BlockPos blockPos, IColorizer colorizer, BlockPosM blockPosM) {
        int b;
        int g;
        int r;
        int sumRed = 0;
        int sumGreen = 0;
        int sumBlue = 0;
        int x = blockPos.getX();
        int y = blockPos.getY();
        int z = blockPos.getZ();
        BlockPosM posM = blockPosM;
        for (r = x - 1; r <= x + 1; ++r) {
            for (g = z - 1; g <= z + 1; ++g) {
                posM.setXyz(r, y, g);
                b = colorizer.getColor(blockAccess, posM);
                sumRed += b >> 16 & 0xFF;
                sumGreen += b >> 8 & 0xFF;
                sumBlue += b & 0xFF;
            }
        }
        r = sumRed / 9;
        g = sumGreen / 9;
        b = sumBlue / 9;
        return r << 16 | g << 8 | b;
    }

    public static int getFluidColor(IBlockAccess blockAccess, IBlockState blockState, BlockPos blockPos, RenderEnv renderEnv) {
        Block block = blockState.getBlock();
        IColorizer colorizer = CustomColors.getBlockColormap(blockState);
        if (colorizer == null && block.getMaterial() == Material.water) {
            colorizer = COLORIZER_WATER;
        }
        return colorizer == null ? block.colorMultiplier(blockAccess, blockPos) : (Config.isSmoothBiomes() && !((IColorizer)colorizer).isColorConstant() ? CustomColors.getSmoothColorMultiplier(blockAccess, blockPos, colorizer, renderEnv.getColorizerBlockPosM()) : ((IColorizer)colorizer).getColor(blockAccess, blockPos));
    }

    public static void updatePortalFX(EntityFX fx) {
        if (particlePortalColor >= 0) {
            int col = particlePortalColor;
            int red = col >> 16 & 0xFF;
            int green = col >> 8 & 0xFF;
            int blue = col & 0xFF;
            float redF = (float)red / 255.0f;
            float greenF = (float)green / 255.0f;
            float blueF = (float)blue / 255.0f;
            fx.setRBGColorF(redF, greenF, blueF);
        }
    }

    public static void updateMyceliumFX(EntityFX fx) {
        if (myceliumParticleColors != null) {
            int col = myceliumParticleColors.getColorRandom();
            int red = col >> 16 & 0xFF;
            int green = col >> 8 & 0xFF;
            int blue = col & 0xFF;
            float redF = (float)red / 255.0f;
            float greenF = (float)green / 255.0f;
            float blueF = (float)blue / 255.0f;
            fx.setRBGColorF(redF, greenF, blueF);
        }
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
        if (redstoneColors != null) {
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
    }

    private static int getRedstoneLevel(IBlockState state, int def) {
        Block block = state.getBlock();
        if (!(block instanceof BlockRedstoneWire)) {
            return def;
        }
        Comparable val = state.getValue(BlockRedstoneWire.POWER);
        if (!(val instanceof Integer)) {
            return def;
        }
        Integer valInt = (Integer)val;
        return valInt;
    }

    public static int getXpOrbColor(float timer) {
        if (xpOrbColors == null) {
            return -1;
        }
        int index = (int)((double)((MathHelper.sin(timer) + 1.0f) * (float)(xpOrbColors.getLength() - 1)) / 2.0);
        int col = xpOrbColors.getColor(index);
        return col;
    }

    public static void updateWaterFX(EntityFX fx, IBlockAccess blockAccess, double x, double y, double z) {
        if (waterColors != null || blockColormaps != null) {
            BlockPos blockPos = new BlockPos(x, y, z);
            RenderEnv renderEnv = RenderEnv.getInstance(blockAccess, BLOCK_STATE_WATER, blockPos);
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
    }

    private static int getLilypadColorMultiplier(IBlockAccess blockAccess, BlockPos blockPos) {
        return lilyPadColor < 0 ? Blocks.waterlily.colorMultiplier(blockAccess, blockPos) : lilyPadColor;
    }

    private static Vec3 getFogColorNether(Vec3 col) {
        return fogColorNether == null ? col : fogColorNether;
    }

    private static Vec3 getFogColorEnd(Vec3 col) {
        return fogColorEnd == null ? col : fogColorEnd;
    }

    private static Vec3 getSkyColorEnd(Vec3 col) {
        return skyColorEnd == null ? col : skyColorEnd;
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
        if (underwaterColors == null) {
            return null;
        }
        int col = underwaterColors.getColorSmooth(blockAccess, x, y, z, 3);
        int red = col >> 16 & 0xFF;
        int green = col >> 8 & 0xFF;
        int blue = col & 0xFF;
        float redF = (float)red / 255.0f;
        float greenF = (float)green / 255.0f;
        float blueF = (float)blue / 255.0f;
        Vec3 newCol = underwaterColorFader.getColor(redF, greenF, blueF);
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

    public static boolean updateLightmap(World world, float torchFlickerX, int[] lmColors, boolean nightvision) {
        if (world == null) {
            return false;
        }
        if (lightMapsColorsRgb == null) {
            return false;
        }
        int dimensionId = world.provider.getDimensionId();
        int lightMapIndex = dimensionId - lightmapMinDimensionId;
        if (lightMapIndex >= 0 && lightMapIndex < lightMapsColorsRgb.length) {
            CustomColormap lightMapRgb = lightMapsColorsRgb[lightMapIndex];
            if (lightMapRgb == null) {
                return false;
            }
            int height = lightMapRgb.getHeight();
            if (nightvision && height < 64) {
                return false;
            }
            int width = lightMapRgb.getWidth();
            if (width < 16) {
                CustomColors.warn("Invalid lightmap width: " + width + " for dimension: " + dimensionId);
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
            float sunX = sun * (float)(width - 1);
            float torchX = Config.limitTo1(torchFlickerX + 0.5f) * (float)(width - 1);
            float gamma = Config.limitTo1(Config.getGameSettings().gammaSetting);
            boolean hasGamma = gamma > 1.0E-4f;
            float[][] colorsRgb = lightMapRgb.getColorsRgb();
            CustomColors.getLightMapColumn(colorsRgb, sunX, startIndex, width, sunRgbs);
            CustomColors.getLightMapColumn(colorsRgb, torchX, startIndex + 16 * width, width, torchRgbs);
            float[] rgb = new float[3];
            for (int is = 0; is < 16; ++is) {
                for (int it = 0; it < 16; ++it) {
                    int r;
                    for (r = 0; r < 3; ++r) {
                        float g = Config.limitTo1(sunRgbs[is][r] + torchRgbs[it][r]);
                        if (hasGamma) {
                            float b = 1.0f - g;
                            b = 1.0f - b * b * b * b;
                            g = gamma * b + (1.0f - gamma) * g;
                        }
                        rgb[r] = g;
                    }
                    r = (int)(rgb[0] * 255.0f);
                    int var22 = (int)(rgb[1] * 255.0f);
                    int var23 = (int)(rgb[2] * 255.0f);
                    lmColors[is * 16 + it] = 0xFF000000 | r << 16 | var22 << 8 | var23;
                }
            }
            return true;
        }
        return false;
    }

    private static void getLightMapColumn(float[][] origMap, float x, int offset, int width, float[][] colRgb) {
        int xHigh;
        int xLow = (int)Math.floor(x);
        if (xLow == (xHigh = (int)Math.ceil(x))) {
            for (int var14 = 0; var14 < 16; ++var14) {
                float[] var15 = origMap[offset + var14 * width + xLow];
                float[] var16 = colRgb[var14];
                for (int var17 = 0; var17 < 3; ++var17) {
                    var16[var17] = var15[var17];
                }
            }
        } else {
            float dLow = 1.0f - (x - (float)xLow);
            float dHigh = 1.0f - ((float)xHigh - x);
            for (int y = 0; y < 16; ++y) {
                float[] rgbLow = origMap[offset + y * width + xLow];
                float[] rgbHigh = origMap[offset + y * width + xHigh];
                float[] rgb = colRgb[y];
                for (int i = 0; i < 3; ++i) {
                    rgb[i] = rgbLow[i] * dLow + rgbHigh[i] * dHigh;
                }
            }
        }
    }

    public static Vec3 getWorldFogColor(Vec3 fogVec, WorldClient world, Entity renderViewEntity, float partialTicks) {
        int worldType = world.provider.getDimensionId();
        switch (worldType) {
            case -1: {
                fogVec = CustomColors.getFogColorNether(fogVec);
                break;
            }
            case 0: {
                Minecraft mc = Minecraft.getMinecraft();
                fogVec = CustomColors.getFogColor(fogVec, mc.theWorld, renderViewEntity.posX, renderViewEntity.posY + 1.0, renderViewEntity.posZ);
                break;
            }
            case 1: {
                fogVec = CustomColors.getFogColorEnd(fogVec);
            }
        }
        return fogVec;
    }

    public static Vec3 getWorldSkyColor(Vec3 skyVec, WorldClient world, Entity renderViewEntity, float partialTicks) {
        int worldType = world.provider.getDimensionId();
        switch (worldType) {
            case 0: {
                Minecraft mc = Minecraft.getMinecraft();
                skyVec = CustomColors.getSkyColor(skyVec, mc.theWorld, renderViewEntity.posX, renderViewEntity.posY + 1.0, renderViewEntity.posZ);
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
        Set<Object> keys = props.keySet();
        int countColors = 0;
        for (String string : keys) {
            String value = props.getProperty(string);
            if (!string.startsWith(prefix)) continue;
            String name = StrUtils.removePrefix(string, prefix);
            int id = CustomColors.getEntityId(name);
            int color = CustomColors.parseColor(value);
            if (id >= 0 && color >= 0) {
                while (list.size() <= id) {
                    list.add(-1);
                }
                list.set(id, color);
                ++countColors;
                continue;
            }
            CustomColors.warn("Invalid spawn egg color: " + string + " = " + value);
        }
        if (countColors <= 0) {
            return null;
        }
        CustomColors.dbg(String.valueOf(logName) + " colors: " + countColors);
        int[] arrn = new int[list.size()];
        for (int var14 = 0; var14 < arrn.length; ++var14) {
            arrn[var14] = (Integer)list.get(var14);
        }
        return arrn;
    }

    private static int getSpawnEggColor(ItemMonsterPlacer item, ItemStack itemStack, int layer, int color) {
        int[] eggColors;
        int id = itemStack.getMetadata();
        int[] arrn = eggColors = layer == 0 ? spawnEggPrimaryColors : spawnEggSecondaryColors;
        if (eggColors == null) {
            return color;
        }
        if (id >= 0 && id < eggColors.length) {
            int eggColor = eggColors[id];
            return eggColor < 0 ? color : eggColor;
        }
        return color;
    }

    public static int getColorFromItemStack(ItemStack itemStack, int layer, int color) {
        if (itemStack == null) {
            return color;
        }
        Item item = itemStack.getItem();
        return item == null ? color : (item instanceof ItemMonsterPlacer ? CustomColors.getSpawnEggColor((ItemMonsterPlacer)item, itemStack, layer, color) : color);
    }

    private static float[][] readDyeColors(Properties props, String fileName, String prefix, String logName) {
        EnumDyeColor[] dyeValues = EnumDyeColor.values();
        HashMap<String, EnumDyeColor> mapDyes = new HashMap<String, EnumDyeColor>();
        for (int colors = 0; colors < dyeValues.length; ++colors) {
            EnumDyeColor countColors = dyeValues[colors];
            mapDyes.put(countColors.getName(), countColors);
        }
        float[][] var16 = new float[dyeValues.length][];
        int var17 = 0;
        Set<Object> keys = props.keySet();
        for (String string : keys) {
            String value = props.getProperty(string);
            if (!string.startsWith(prefix)) continue;
            String name = StrUtils.removePrefix(string, prefix);
            if (name.equals("lightBlue")) {
                name = "light_blue";
            }
            EnumDyeColor dye = (EnumDyeColor)mapDyes.get(name);
            int color = CustomColors.parseColor(value);
            if (dye != null && color >= 0) {
                float[] rgb = new float[]{(float)(color >> 16 & 0xFF) / 255.0f, (float)(color >> 8 & 0xFF) / 255.0f, (float)(color & 0xFF) / 255.0f};
                var16[dye.ordinal()] = rgb;
                ++var17;
                continue;
            }
            CustomColors.warn("Invalid color: " + string + " = " + value);
        }
        if (var17 <= 0) {
            return null;
        }
        CustomColors.dbg(String.valueOf(logName) + " colors: " + var17);
        return var16;
    }

    private static float[] getDyeColors(EnumDyeColor dye, float[][] dyeColors, float[] colors) {
        if (dyeColors == null) {
            return colors;
        }
        if (dye == null) {
            return colors;
        }
        float[] customColors = dyeColors[dye.ordinal()];
        return customColors == null ? colors : customColors;
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
        Set<Object> keys = props.keySet();
        for (String string : keys) {
            String value = props.getProperty(string);
            if (!string.startsWith(prefix)) continue;
            String name = StrUtils.removePrefix(string, prefix);
            int code = Config.parseInt(name, -1);
            int color = CustomColors.parseColor(value);
            if (code >= 0 && code < colors.length && color >= 0) {
                colors[code] = color;
                ++countColors;
                continue;
            }
            CustomColors.warn("Invalid color: " + string + " = " + value);
        }
        if (countColors <= 0) {
            return null;
        }
        CustomColors.dbg(String.valueOf(logName) + " colors: " + countColors);
        return colors;
    }

    public static int getTextColor(int index, int color) {
        if (textColors == null) {
            return color;
        }
        if (index >= 0 && index < textColors.length) {
            int customColor = textColors[index];
            return customColor < 0 ? color : customColor;
        }
        return color;
    }

    private static int[] readMapColors(Properties props, String fileName, String prefix, String logName) {
        int[] colors = new int[MapColor.mapColorArray.length];
        Arrays.fill(colors, -1);
        int countColors = 0;
        Set<Object> keys = props.keySet();
        for (String string : keys) {
            String value = props.getProperty(string);
            if (!string.startsWith(prefix)) continue;
            String name = StrUtils.removePrefix(string, prefix);
            int index = CustomColors.getMapColorIndex(name);
            int color = CustomColors.parseColor(value);
            if (index >= 0 && index < colors.length && color >= 0) {
                colors[index] = color;
                ++countColors;
                continue;
            }
            CustomColors.warn("Invalid color: " + string + " = " + value);
        }
        if (countColors <= 0) {
            return null;
        }
        CustomColors.dbg(String.valueOf(logName) + " colors: " + countColors);
        return colors;
    }

    private static int[] readPotionColors(Properties props, String fileName, String prefix, String logName) {
        int[] colors = new int[Potion.potionTypes.length];
        Arrays.fill(colors, -1);
        int countColors = 0;
        Set<Object> keys = props.keySet();
        for (String string : keys) {
            String value = props.getProperty(string);
            if (!string.startsWith(prefix)) continue;
            int index = CustomColors.getPotionId(string);
            int color = CustomColors.parseColor(value);
            if (index >= 0 && index < colors.length && color >= 0) {
                colors[index] = color;
                ++countColors;
                continue;
            }
            CustomColors.warn("Invalid color: " + string + " = " + value);
        }
        if (countColors <= 0) {
            return null;
        }
        CustomColors.dbg(String.valueOf(logName) + " colors: " + countColors);
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
        if (potionId >= 0 && potionId < potionColors.length) {
            int potionColor = potionColors[potionId];
            return potionColor < 0 ? color : potionColor;
        }
        return color;
    }

    private static int getMapColorIndex(String name) {
        return name == null ? -1 : (name.equals("air") ? MapColor.airColor.colorIndex : (name.equals("grass") ? MapColor.grassColor.colorIndex : (name.equals("sand") ? MapColor.sandColor.colorIndex : (name.equals("cloth") ? MapColor.clothColor.colorIndex : (name.equals("tnt") ? MapColor.tntColor.colorIndex : (name.equals("ice") ? MapColor.iceColor.colorIndex : (name.equals("iron") ? MapColor.ironColor.colorIndex : (name.equals("foliage") ? MapColor.foliageColor.colorIndex : (name.equals("snow") ? MapColor.snowColor.colorIndex : (name.equals("clay") ? MapColor.clayColor.colorIndex : (name.equals("dirt") ? MapColor.dirtColor.colorIndex : (name.equals("stone") ? MapColor.stoneColor.colorIndex : (name.equals("water") ? MapColor.waterColor.colorIndex : (name.equals("wood") ? MapColor.woodColor.colorIndex : (name.equals("quartz") ? MapColor.quartzColor.colorIndex : (name.equals("adobe") ? MapColor.adobeColor.colorIndex : (name.equals("magenta") ? MapColor.magentaColor.colorIndex : (name.equals("lightBlue") ? MapColor.lightBlueColor.colorIndex : (name.equals("light_blue") ? MapColor.lightBlueColor.colorIndex : (name.equals("yellow") ? MapColor.yellowColor.colorIndex : (name.equals("lime") ? MapColor.limeColor.colorIndex : (name.equals("pink") ? MapColor.pinkColor.colorIndex : (name.equals("gray") ? MapColor.grayColor.colorIndex : (name.equals("silver") ? MapColor.silverColor.colorIndex : (name.equals("cyan") ? MapColor.cyanColor.colorIndex : (name.equals("purple") ? MapColor.purpleColor.colorIndex : (name.equals("blue") ? MapColor.blueColor.colorIndex : (name.equals("brown") ? MapColor.brownColor.colorIndex : (name.equals("green") ? MapColor.greenColor.colorIndex : (name.equals("red") ? MapColor.redColor.colorIndex : (name.equals("black") ? MapColor.blackColor.colorIndex : (name.equals("gold") ? MapColor.goldColor.colorIndex : (name.equals("diamond") ? MapColor.diamondColor.colorIndex : (name.equals("lapis") ? MapColor.lapisColor.colorIndex : (name.equals("emerald") ? MapColor.emeraldColor.colorIndex : (name.equals("obsidian") ? MapColor.obsidianColor.colorIndex : (name.equals("netherrack") ? MapColor.netherrackColor.colorIndex : -1)))))))))))))))))))))))))))))))))))));
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
        if (colors != null) {
            MapColor[] mapColors = MapColor.mapColorArray;
            for (int i = 0; i < mapColors.length && i < colors.length; ++i) {
                int color;
                MapColor mapColor = mapColors[i];
                if (mapColor == null || (color = colors[i]) < 0) continue;
                mapColor.colorValue = color;
            }
        }
    }

    private static int getEntityId(String name) {
        if (name == null) {
            return -1;
        }
        int id = EntityList.func_180122_a(name);
        if (id < 0) {
            return -1;
        }
        String idName = EntityList.getStringFromID(id);
        return !Config.equals(name, idName) ? -1 : id;
    }

    private static void dbg(String str) {
        Config.dbg("CustomColors: " + str);
    }

    private static void warn(String str) {
        Config.warn("CustomColors: " + str);
    }

    public static int getExpBarTextColor(int color) {
        return expBarTextColor < 0 ? color : expBarTextColor;
    }

    public static int getBossTextColor(int color) {
        return bossTextColor < 0 ? color : bossTextColor;
    }

    public static int getSignTextColor(int color) {
        return signTextColor < 0 ? color : signTextColor;
    }

    public static interface IColorizer {
        public int getColor(IBlockAccess var1, BlockPos var2);

        public boolean isColorConstant();
    }
}

