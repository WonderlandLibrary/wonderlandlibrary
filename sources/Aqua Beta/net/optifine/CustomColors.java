package net.optifine;

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
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.state.BlockStateBase;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.particle.EntityFX;
import net.minecraft.client.renderer.block.model.BakedQuad;
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
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraft.world.biome.BiomeGenBase;
import net.optifine.BlockPosM;
import net.optifine.CustomColorFader;
import net.optifine.CustomColormap;
import net.optifine.CustomColors;
import net.optifine.LightMap;
import net.optifine.LightMapPack;
import net.optifine.config.ConnectedParser;
import net.optifine.config.MatchBlock;
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
    private static final IColorizer COLORIZER_GRASS = new /* Unavailable Anonymous Inner Class!! */;
    private static final IColorizer COLORIZER_FOLIAGE = new /* Unavailable Anonymous Inner Class!! */;
    private static final IColorizer COLORIZER_FOLIAGE_PINE = new /* Unavailable Anonymous Inner Class!! */;
    private static final IColorizer COLORIZER_FOLIAGE_BIRCH = new /* Unavailable Anonymous Inner Class!! */;
    private static final IColorizer COLORIZER_WATER = new /* Unavailable Anonymous Inner Class!! */;

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
        String s = "mcpatcher/colormap/";
        String[] astring = new String[]{"water.png", "watercolorX.png"};
        waterColors = CustomColors.getCustomColors(s, astring, 256, 256);
        CustomColors.updateUseDefaultGrassFoliageColors();
        if (Config.isCustomColors()) {
            String[] astring1 = new String[]{"pine.png", "pinecolor.png"};
            foliagePineColors = CustomColors.getCustomColors(s, astring1, 256, 256);
            String[] astring2 = new String[]{"birch.png", "birchcolor.png"};
            foliageBirchColors = CustomColors.getCustomColors(s, astring2, 256, 256);
            String[] astring3 = new String[]{"swampgrass.png", "swampgrasscolor.png"};
            swampGrassColors = CustomColors.getCustomColors(s, astring3, 256, 256);
            String[] astring4 = new String[]{"swampfoliage.png", "swampfoliagecolor.png"};
            swampFoliageColors = CustomColors.getCustomColors(s, astring4, 256, 256);
            String[] astring5 = new String[]{"sky0.png", "skycolor0.png"};
            skyColors = CustomColors.getCustomColors(s, astring5, 256, 256);
            String[] astring6 = new String[]{"fog0.png", "fogcolor0.png"};
            fogColors = CustomColors.getCustomColors(s, astring6, 256, 256);
            String[] astring7 = new String[]{"underwater.png", "underwatercolor.png"};
            underwaterColors = CustomColors.getCustomColors(s, astring7, 256, 256);
            String[] astring8 = new String[]{"underlava.png", "underlavacolor.png"};
            underlavaColors = CustomColors.getCustomColors(s, astring8, 256, 256);
            String[] astring9 = new String[]{"redstone.png", "redstonecolor.png"};
            redstoneColors = CustomColors.getCustomColors(s, astring9, 16, 1);
            xpOrbColors = CustomColors.getCustomColors(s + "xporb.png", -1, -1);
            durabilityColors = CustomColors.getCustomColors(s + "durability.png", -1, -1);
            String[] astring10 = new String[]{"stem.png", "stemcolor.png"};
            stemColors = CustomColors.getCustomColors(s, astring10, 8, 1);
            stemPumpkinColors = CustomColors.getCustomColors(s + "pumpkinstem.png", 8, 1);
            stemMelonColors = CustomColors.getCustomColors(s + "melonstem.png", 8, 1);
            String[] astring11 = new String[]{"myceliumparticle.png", "myceliumparticlecolor.png"};
            myceliumParticleColors = CustomColors.getCustomColors(s, astring11, -1, -1);
            Pair<LightMapPack[], Integer> pair = CustomColors.parseLightMapPacks();
            lightMapPacks = (LightMapPack[])pair.getLeft();
            lightmapMinDimensionId = (Integer)pair.getRight();
            CustomColors.readColorProperties("mcpatcher/color.properties");
            blockColormaps = CustomColors.readBlockColormaps(new String[]{s + "custom/", s + "blocks/"}, colorsBlockColormaps, 256, 256);
            CustomColors.updateUseDefaultGrassFoliageColors();
        }
    }

    private static String getValidProperty(String fileName, String key, String[] validValues, String valDef) {
        try {
            ResourceLocation resourcelocation = new ResourceLocation(fileName);
            InputStream inputstream = Config.getResourceStream((ResourceLocation)resourcelocation);
            if (inputstream == null) {
                return valDef;
            }
            PropertiesOrdered properties = new PropertiesOrdered();
            properties.load(inputstream);
            inputstream.close();
            String s = properties.getProperty(key);
            if (s == null) {
                return valDef;
            }
            List list = Arrays.asList((Object[])validValues);
            if (!list.contains((Object)s)) {
                CustomColors.warn("Invalid value: " + key + "=" + s);
                CustomColors.warn("Expected values: " + Config.arrayToString((Object[])validValues));
                return valDef;
            }
            CustomColors.dbg("" + key + "=" + s);
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
        String s = "mcpatcher/lightmap/world";
        String s1 = ".png";
        String[] astring = ResUtils.collectFiles((String)s, (String)s1);
        HashMap map = new HashMap();
        for (int i = 0; i < astring.length; ++i) {
            String s2 = astring[i];
            String s3 = StrUtils.removePrefixSuffix((String)s2, (String)s, (String)s1);
            int j = Config.parseInt((String)s3, (int)Integer.MIN_VALUE);
            if (j == Integer.MIN_VALUE) {
                CustomColors.warn("Invalid dimension ID: " + s3 + ", path: " + s2);
                continue;
            }
            map.put((Object)j, (Object)s2);
        }
        Set set = map.keySet();
        Integer[] ainteger = (Integer[])set.toArray((Object[])new Integer[set.size()]);
        Arrays.sort((Object[])ainteger);
        if (ainteger.length <= 0) {
            return new ImmutablePair(null, (Object)0);
        }
        int j1 = ainteger[0];
        int k1 = ainteger[ainteger.length - 1];
        int k = k1 - j1 + 1;
        CustomColormap[] acustomcolormap = new CustomColormap[k];
        for (int l = 0; l < ainteger.length; ++l) {
            Integer integer = ainteger[l];
            String s4 = (String)map.get((Object)integer);
            CustomColormap customcolormap = CustomColors.getCustomColors(s4, -1, -1);
            if (customcolormap == null) continue;
            if (customcolormap.getWidth() < 16) {
                CustomColors.warn("Invalid lightmap width: " + customcolormap.getWidth() + ", path: " + s4);
                continue;
            }
            int i1 = integer - j1;
            acustomcolormap[i1] = customcolormap;
        }
        LightMapPack[] alightmappack = new LightMapPack[acustomcolormap.length];
        for (int l1 = 0; l1 < acustomcolormap.length; ++l1) {
            LightMapPack lightmappack;
            CustomColormap customcolormap3 = acustomcolormap[l1];
            if (customcolormap3 == null) continue;
            String s5 = customcolormap3.name;
            String s6 = customcolormap3.basePath;
            CustomColormap customcolormap1 = CustomColors.getCustomColors(s6 + "/" + s5 + "_rain.png", -1, -1);
            CustomColormap customcolormap2 = CustomColors.getCustomColors(s6 + "/" + s5 + "_thunder.png", -1, -1);
            LightMap lightmap = new LightMap(customcolormap3);
            LightMap lightmap1 = customcolormap1 != null ? new LightMap(customcolormap1) : null;
            LightMap lightmap2 = customcolormap2 != null ? new LightMap(customcolormap2) : null;
            alightmappack[l1] = lightmappack = new LightMapPack(lightmap, lightmap1, lightmap2);
        }
        return new ImmutablePair((Object)alightmappack, (Object)j1);
    }

    private static int getTextureHeight(String path, int defHeight) {
        try {
            InputStream inputstream = Config.getResourceStream((ResourceLocation)new ResourceLocation(path));
            if (inputstream == null) {
                return defHeight;
            }
            BufferedImage bufferedimage = ImageIO.read((InputStream)inputstream);
            inputstream.close();
            return bufferedimage == null ? defHeight : bufferedimage.getHeight();
        }
        catch (IOException var4) {
            return defHeight;
        }
    }

    private static void readColorProperties(String fileName) {
        try {
            ResourceLocation resourcelocation = new ResourceLocation(fileName);
            InputStream inputstream = Config.getResourceStream((ResourceLocation)resourcelocation);
            if (inputstream == null) {
                return;
            }
            CustomColors.dbg("Loading " + fileName);
            PropertiesOrdered properties = new PropertiesOrdered();
            properties.load(inputstream);
            inputstream.close();
            particleWaterColor = CustomColors.readColor((Properties)properties, new String[]{"particle.water", "drop.water"});
            particlePortalColor = CustomColors.readColor((Properties)properties, "particle.portal");
            lilyPadColor = CustomColors.readColor((Properties)properties, "lilypad");
            expBarTextColor = CustomColors.readColor((Properties)properties, "text.xpbar");
            bossTextColor = CustomColors.readColor((Properties)properties, "text.boss");
            signTextColor = CustomColors.readColor((Properties)properties, "text.sign");
            fogColorNether = CustomColors.readColorVec3((Properties)properties, "fog.nether");
            fogColorEnd = CustomColors.readColorVec3((Properties)properties, "fog.end");
            skyColorEnd = CustomColors.readColorVec3((Properties)properties, "sky.end");
            colorsBlockColormaps = CustomColors.readCustomColormaps((Properties)properties, fileName);
            spawnEggPrimaryColors = CustomColors.readSpawnEggColors((Properties)properties, fileName, "egg.shell.", "Spawn egg shell");
            spawnEggSecondaryColors = CustomColors.readSpawnEggColors((Properties)properties, fileName, "egg.spots.", "Spawn egg spot");
            wolfCollarColors = CustomColors.readDyeColors((Properties)properties, fileName, "collar.", "Wolf collar");
            sheepColors = CustomColors.readDyeColors((Properties)properties, fileName, "sheep.", "Sheep");
            textColors = CustomColors.readTextColors((Properties)properties, fileName, "text.code.", "Text");
            int[] aint = CustomColors.readMapColors((Properties)properties, fileName, "map.", "Map");
            if (aint != null) {
                if (mapColorsOriginal == null) {
                    mapColorsOriginal = CustomColors.getMapColors();
                }
                CustomColors.setMapColors(aint);
            }
            potionColors = CustomColors.readPotionColors((Properties)properties, fileName, "potion.", "Potion");
            xpOrbTime = Config.parseInt((String)properties.getProperty("xporb.time"), (int)-1);
        }
        catch (FileNotFoundException var5) {
            return;
        }
        catch (IOException ioexception) {
            ioexception.printStackTrace();
        }
    }

    private static CustomColormap[] readCustomColormaps(Properties props, String fileName) {
        ArrayList list = new ArrayList();
        String s = "palette.block.";
        HashMap map = new HashMap();
        for (String s1 : props.keySet()) {
            String s2 = props.getProperty(s1);
            if (!s1.startsWith(s)) continue;
            map.put((Object)s1, (Object)s2);
        }
        String[] astring = (String[])map.keySet().toArray((Object[])new String[map.size()]);
        for (int j = 0; j < astring.length; ++j) {
            String s6 = astring[j];
            String s3 = props.getProperty(s6);
            CustomColors.dbg("Block palette: " + s6 + " = " + s3);
            String s4 = s6.substring(s.length());
            String s5 = TextureUtils.getBasePath((String)fileName);
            s4 = TextureUtils.fixResourcePath((String)s4, (String)s5);
            CustomColormap customcolormap = CustomColors.getCustomColors(s4, 256, 256);
            if (customcolormap == null) {
                CustomColors.warn("Colormap not found: " + s4);
                continue;
            }
            ConnectedParser connectedparser = new ConnectedParser("CustomColors");
            MatchBlock[] amatchblock = connectedparser.parseMatchBlocks(s3);
            if (amatchblock != null && amatchblock.length > 0) {
                for (int i = 0; i < amatchblock.length; ++i) {
                    MatchBlock matchblock = amatchblock[i];
                    customcolormap.addMatchBlock(matchblock);
                }
                list.add((Object)customcolormap);
                continue;
            }
            CustomColors.warn("Invalid match blocks: " + s3);
        }
        if (list.size() <= 0) {
            return null;
        }
        CustomColormap[] acustomcolormap = (CustomColormap[])list.toArray((Object[])new CustomColormap[list.size()]);
        return acustomcolormap;
    }

    private static CustomColormap[][] readBlockColormaps(String[] basePaths, CustomColormap[] basePalettes, int width, int height) {
        String[] astring = ResUtils.collectFiles((String[])basePaths, (String[])new String[]{".properties"});
        Arrays.sort((Object[])astring);
        ArrayList list = new ArrayList();
        for (int i = 0; i < astring.length; ++i) {
            String s = astring[i];
            CustomColors.dbg("Block colormap: " + s);
            try {
                ResourceLocation resourcelocation = new ResourceLocation("minecraft", s);
                InputStream inputstream = Config.getResourceStream((ResourceLocation)resourcelocation);
                if (inputstream == null) {
                    CustomColors.warn("File not found: " + s);
                    continue;
                }
                PropertiesOrdered properties = new PropertiesOrdered();
                properties.load(inputstream);
                inputstream.close();
                CustomColormap customcolormap = new CustomColormap((Properties)properties, s, width, height, paletteFormatDefault);
                if (!customcolormap.isValid(s) || !customcolormap.isValidMatchBlocks(s)) continue;
                CustomColors.addToBlockList(customcolormap, (List)list);
                continue;
            }
            catch (FileNotFoundException var12) {
                CustomColors.warn("File not found: " + s);
                continue;
            }
            catch (Exception exception) {
                exception.printStackTrace();
            }
        }
        if (basePalettes != null) {
            for (int j = 0; j < basePalettes.length; ++j) {
                CustomColormap customcolormap1 = basePalettes[j];
                CustomColors.addToBlockList(customcolormap1, (List)list);
            }
        }
        if (list.size() <= 0) {
            return null;
        }
        CustomColormap[][] acustomcolormap = CustomColors.blockListToArray((List)list);
        return acustomcolormap;
    }

    private static void addToBlockList(CustomColormap cm, List blockList) {
        int[] aint = cm.getMatchBlockIds();
        if (aint != null && aint.length > 0) {
            for (int i = 0; i < aint.length; ++i) {
                int j = aint[i];
                if (j < 0) {
                    CustomColors.warn("Invalid block ID: " + j);
                    continue;
                }
                CustomColors.addToList(cm, blockList, j);
            }
        } else {
            CustomColors.warn("No match blocks: " + Config.arrayToString((int[])aint));
        }
    }

    private static void addToList(CustomColormap cm, List list, int id) {
        while (id >= list.size()) {
            list.add(null);
        }
        List subList = (List)list.get(id);
        if (subList == null) {
            subList = new ArrayList();
            list.set(id, (Object)subList);
        }
        subList.add((Object)cm);
    }

    private static CustomColormap[][] blockListToArray(List list) {
        CustomColormap[][] acustomcolormap = new CustomColormap[list.size()][];
        for (int i = 0; i < list.size(); ++i) {
            CustomColormap[] acustomcolormap1;
            List subList = (List)list.get(i);
            if (subList == null) continue;
            acustomcolormap[i] = acustomcolormap1 = (CustomColormap[])subList.toArray((Object[])new CustomColormap[subList.size()]);
        }
        return acustomcolormap;
    }

    private static int readColor(Properties props, String[] names) {
        for (int i = 0; i < names.length; ++i) {
            String s = names[i];
            int j = CustomColors.readColor(props, s);
            if (j < 0) continue;
            return j;
        }
        return -1;
    }

    private static int readColor(Properties props, String name) {
        String s = props.getProperty(name);
        if (s == null) {
            return -1;
        }
        int i = CustomColors.parseColor(s = s.trim());
        if (i < 0) {
            CustomColors.warn("Invalid color: " + name + " = " + s);
            return i;
        }
        CustomColors.dbg(name + " = " + s);
        return i;
    }

    private static int parseColor(String str) {
        if (str == null) {
            return -1;
        }
        str = str.trim();
        try {
            int i = Integer.parseInt((String)str, (int)16) & 0xFFFFFF;
            return i;
        }
        catch (NumberFormatException var2) {
            return -1;
        }
    }

    private static Vec3 readColorVec3(Properties props, String name) {
        int i = CustomColors.readColor(props, name);
        if (i < 0) {
            return null;
        }
        int j = i >> 16 & 0xFF;
        int k = i >> 8 & 0xFF;
        int l = i & 0xFF;
        float f = (float)j / 255.0f;
        float f1 = (float)k / 255.0f;
        float f2 = (float)l / 255.0f;
        return new Vec3((double)f, (double)f1, (double)f2);
    }

    private static CustomColormap getCustomColors(String basePath, String[] paths, int width, int height) {
        for (int i = 0; i < paths.length; ++i) {
            String s = paths[i];
            s = basePath + s;
            CustomColormap customcolormap = CustomColors.getCustomColors(s, width, height);
            if (customcolormap == null) continue;
            return customcolormap;
        }
        return null;
    }

    public static CustomColormap getCustomColors(String pathImage, int width, int height) {
        try {
            ResourceLocation resourcelocation = new ResourceLocation(pathImage);
            if (!Config.hasResource((ResourceLocation)resourcelocation)) {
                return null;
            }
            CustomColors.dbg("Colormap " + pathImage);
            PropertiesOrdered properties = new PropertiesOrdered();
            String s = StrUtils.replaceSuffix((String)pathImage, (String)".png", (String)".properties");
            ResourceLocation resourcelocation1 = new ResourceLocation(s);
            if (Config.hasResource((ResourceLocation)resourcelocation1)) {
                InputStream inputstream = Config.getResourceStream((ResourceLocation)resourcelocation1);
                properties.load(inputstream);
                inputstream.close();
                CustomColors.dbg("Colormap properties: " + s);
            } else {
                properties.put((Object)"format", (Object)paletteFormatDefault);
                properties.put((Object)"source", (Object)pathImage);
                s = pathImage;
            }
            CustomColormap customcolormap = new CustomColormap((Properties)properties, s, width, height, paletteFormatDefault);
            return !customcolormap.isValid(s) ? null : customcolormap;
        }
        catch (Exception exception) {
            exception.printStackTrace();
            return null;
        }
    }

    public static void updateUseDefaultGrassFoliageColors() {
        useDefaultGrassFoliageColors = foliageBirchColors == null && foliagePineColors == null && swampGrassColors == null && swampFoliageColors == null && Config.isSwampColors() && Config.isSmoothBiomes();
    }

    public static int getColorMultiplier(BakedQuad quad, IBlockState blockState, IBlockAccess blockAccess, BlockPos blockPos, RenderEnv renderEnv) {
        IColorizer customcolors$icolorizer;
        Block block = blockState.getBlock();
        IBlockState iblockstate = renderEnv.getBlockState();
        if (blockColormaps != null) {
            CustomColormap customcolormap;
            if (!quad.hasTintIndex()) {
                if (block == Blocks.grass) {
                    iblockstate = BLOCK_STATE_DIRT;
                }
                if (block == Blocks.redstone_wire) {
                    return -1;
                }
            }
            if (block == Blocks.double_plant && renderEnv.getMetadata() >= 8) {
                blockPos = blockPos.down();
                iblockstate = blockAccess.getBlockState(blockPos);
            }
            if ((customcolormap = CustomColors.getBlockColormap(iblockstate)) != null) {
                if (Config.isSmoothBiomes() && !customcolormap.isColorConstant()) {
                    return CustomColors.getSmoothColorMultiplier(blockState, blockAccess, blockPos, (IColorizer)customcolormap, renderEnv.getColorizerBlockPosM());
                }
                return customcolormap.getColor(blockAccess, blockPos);
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
        int i = renderEnv.getMetadata();
        if (block != Blocks.grass && block != Blocks.tallgrass && block != Blocks.double_plant) {
            if (block == Blocks.double_plant) {
                customcolors$icolorizer = COLORIZER_GRASS;
                if (i >= 8) {
                    blockPos = blockPos.down();
                }
            } else if (block == Blocks.leaves) {
                switch (i & 3) {
                    case 0: {
                        customcolors$icolorizer = COLORIZER_FOLIAGE;
                        break;
                    }
                    case 1: {
                        customcolors$icolorizer = COLORIZER_FOLIAGE_PINE;
                        break;
                    }
                    case 2: {
                        customcolors$icolorizer = COLORIZER_FOLIAGE_BIRCH;
                        break;
                    }
                    default: {
                        customcolors$icolorizer = COLORIZER_FOLIAGE;
                        break;
                    }
                }
            } else if (block == Blocks.leaves2) {
                customcolors$icolorizer = COLORIZER_FOLIAGE;
            } else {
                if (block != Blocks.vine) {
                    return -1;
                }
                customcolors$icolorizer = COLORIZER_FOLIAGE;
            }
        } else {
            customcolors$icolorizer = COLORIZER_GRASS;
        }
        return Config.isSmoothBiomes() && !customcolors$icolorizer.isColorConstant() ? CustomColors.getSmoothColorMultiplier(blockState, blockAccess, blockPos, customcolors$icolorizer, renderEnv.getColorizerBlockPosM()) : customcolors$icolorizer.getColor(iblockstate, blockAccess, blockPos);
    }

    protected static BiomeGenBase getColorBiome(IBlockAccess blockAccess, BlockPos blockPos) {
        BiomeGenBase biomegenbase = blockAccess.getBiomeGenForCoords(blockPos);
        if (biomegenbase == BiomeGenBase.swampland && !Config.isSwampColors()) {
            biomegenbase = BiomeGenBase.plains;
        }
        return biomegenbase;
    }

    private static CustomColormap getBlockColormap(IBlockState blockState) {
        if (blockColormaps == null) {
            return null;
        }
        if (!(blockState instanceof BlockStateBase)) {
            return null;
        }
        BlockStateBase blockstatebase = (BlockStateBase)blockState;
        int i = blockstatebase.getBlockId();
        if (i >= 0 && i < blockColormaps.length) {
            CustomColormap[] acustomcolormap = blockColormaps[i];
            if (acustomcolormap == null) {
                return null;
            }
            for (int j = 0; j < acustomcolormap.length; ++j) {
                CustomColormap customcolormap = acustomcolormap[j];
                if (!customcolormap.matchesBlock(blockstatebase)) continue;
                return customcolormap;
            }
            return null;
        }
        return null;
    }

    private static int getSmoothColorMultiplier(IBlockState blockState, IBlockAccess blockAccess, BlockPos blockPos, IColorizer colorizer, BlockPosM blockPosM) {
        int i = 0;
        int j = 0;
        int k = 0;
        int l = blockPos.getX();
        int i1 = blockPos.getY();
        int j1 = blockPos.getZ();
        BlockPosM blockposm = blockPosM;
        for (int k1 = l - 1; k1 <= l + 1; ++k1) {
            for (int l1 = j1 - 1; l1 <= j1 + 1; ++l1) {
                blockposm.setXyz(k1, i1, l1);
                int i2 = colorizer.getColor(blockState, blockAccess, (BlockPos)blockposm);
                i += i2 >> 16 & 0xFF;
                j += i2 >> 8 & 0xFF;
                k += i2 & 0xFF;
            }
        }
        int j2 = i / 9;
        int k2 = j / 9;
        int l2 = k / 9;
        return j2 << 16 | k2 << 8 | l2;
    }

    public static int getFluidColor(IBlockAccess blockAccess, IBlockState blockState, BlockPos blockPos, RenderEnv renderEnv) {
        Block block = blockState.getBlock();
        CustomColormap customcolors$icolorizer = CustomColors.getBlockColormap(blockState);
        if (customcolors$icolorizer == null && blockState.getBlock().getMaterial() == Material.water) {
            customcolors$icolorizer = COLORIZER_WATER;
        }
        return customcolors$icolorizer == null ? block.colorMultiplier(blockAccess, blockPos, 0) : (Config.isSmoothBiomes() && !customcolors$icolorizer.isColorConstant() ? CustomColors.getSmoothColorMultiplier(blockState, blockAccess, blockPos, (IColorizer)customcolors$icolorizer, renderEnv.getColorizerBlockPosM()) : customcolors$icolorizer.getColor(blockState, blockAccess, blockPos));
    }

    public static void updatePortalFX(EntityFX fx) {
        if (particlePortalColor >= 0) {
            int i = particlePortalColor;
            int j = i >> 16 & 0xFF;
            int k = i >> 8 & 0xFF;
            int l = i & 0xFF;
            float f = (float)j / 255.0f;
            float f1 = (float)k / 255.0f;
            float f2 = (float)l / 255.0f;
            fx.setRBGColorF(f, f1, f2);
        }
    }

    public static void updateMyceliumFX(EntityFX fx) {
        if (myceliumParticleColors != null) {
            int i = myceliumParticleColors.getColorRandom();
            int j = i >> 16 & 0xFF;
            int k = i >> 8 & 0xFF;
            int l = i & 0xFF;
            float f = (float)j / 255.0f;
            float f1 = (float)k / 255.0f;
            float f2 = (float)l / 255.0f;
            fx.setRBGColorF(f, f1, f2);
        }
    }

    private static int getRedstoneColor(IBlockState blockState) {
        if (redstoneColors == null) {
            return -1;
        }
        int i = CustomColors.getRedstoneLevel(blockState, 15);
        int j = redstoneColors.getColor(i);
        return j;
    }

    public static void updateReddustFX(EntityFX fx, IBlockAccess blockAccess, double x, double y, double z) {
        if (redstoneColors != null) {
            IBlockState iblockstate = blockAccess.getBlockState(new BlockPos(x, y, z));
            int i = CustomColors.getRedstoneLevel(iblockstate, 15);
            int j = redstoneColors.getColor(i);
            int k = j >> 16 & 0xFF;
            int l = j >> 8 & 0xFF;
            int i1 = j & 0xFF;
            float f = (float)k / 255.0f;
            float f1 = (float)l / 255.0f;
            float f2 = (float)i1 / 255.0f;
            fx.setRBGColorF(f, f1, f2);
        }
    }

    private static int getRedstoneLevel(IBlockState state, int def) {
        Block block = state.getBlock();
        if (!(block instanceof BlockRedstoneWire)) {
            return def;
        }
        Comparable object = state.getValue((IProperty)BlockRedstoneWire.POWER);
        if (!(object instanceof Integer)) {
            return def;
        }
        Integer integer = (Integer)object;
        return integer;
    }

    public static float getXpOrbTimer(float timer) {
        if (xpOrbTime <= 0) {
            return timer;
        }
        float f = 628.0f / (float)xpOrbTime;
        return timer * f;
    }

    public static int getXpOrbColor(float timer) {
        if (xpOrbColors == null) {
            return -1;
        }
        int i = (int)Math.round((double)((double)((MathHelper.sin((float)timer) + 1.0f) * (float)(xpOrbColors.getLength() - 1)) / 2.0));
        int j = xpOrbColors.getColor(i);
        return j;
    }

    public static int getDurabilityColor(int dur255) {
        if (durabilityColors == null) {
            return -1;
        }
        int i = dur255 * durabilityColors.getLength() / 255;
        int j = durabilityColors.getColor(i);
        return j;
    }

    public static void updateWaterFX(EntityFX fx, IBlockAccess blockAccess, double x, double y, double z, RenderEnv renderEnv) {
        if (waterColors != null || blockColormaps != null || particleWaterColor >= 0) {
            BlockPos blockpos = new BlockPos(x, y, z);
            renderEnv.reset(BLOCK_STATE_WATER, blockpos);
            int i = CustomColors.getFluidColor(blockAccess, BLOCK_STATE_WATER, blockpos, renderEnv);
            int j = i >> 16 & 0xFF;
            int k = i >> 8 & 0xFF;
            int l = i & 0xFF;
            float f = (float)j / 255.0f;
            float f1 = (float)k / 255.0f;
            float f2 = (float)l / 255.0f;
            if (particleWaterColor >= 0) {
                int i1 = particleWaterColor >> 16 & 0xFF;
                int j1 = particleWaterColor >> 8 & 0xFF;
                int k1 = particleWaterColor & 0xFF;
                f *= (float)i1 / 255.0f;
                f1 *= (float)j1 / 255.0f;
                f2 *= (float)k1 / 255.0f;
            }
            fx.setRBGColorF(f, f1, f2);
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
        int i = skyColors.getColorSmooth(blockAccess, x, y, z, 3);
        int j = i >> 16 & 0xFF;
        int k = i >> 8 & 0xFF;
        int l = i & 0xFF;
        float f = (float)j / 255.0f;
        float f1 = (float)k / 255.0f;
        float f2 = (float)l / 255.0f;
        float f3 = (float)skyColor3d.xCoord / 0.5f;
        float f4 = (float)skyColor3d.yCoord / 0.66275f;
        float f5 = (float)skyColor3d.zCoord;
        Vec3 vec3 = skyColorFader.getColor((double)(f *= f3), (double)(f1 *= f4), (double)(f2 *= f5));
        return vec3;
    }

    private static Vec3 getFogColor(Vec3 fogColor3d, IBlockAccess blockAccess, double x, double y, double z) {
        if (fogColors == null) {
            return fogColor3d;
        }
        int i = fogColors.getColorSmooth(blockAccess, x, y, z, 3);
        int j = i >> 16 & 0xFF;
        int k = i >> 8 & 0xFF;
        int l = i & 0xFF;
        float f = (float)j / 255.0f;
        float f1 = (float)k / 255.0f;
        float f2 = (float)l / 255.0f;
        float f3 = (float)fogColor3d.xCoord / 0.753f;
        float f4 = (float)fogColor3d.yCoord / 0.8471f;
        float f5 = (float)fogColor3d.zCoord;
        Vec3 vec3 = fogColorFader.getColor((double)(f *= f3), (double)(f1 *= f4), (double)(f2 *= f5));
        return vec3;
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
        int i = underFluidColors.getColorSmooth(blockAccess, x, y, z, 3);
        int j = i >> 16 & 0xFF;
        int k = i >> 8 & 0xFF;
        int l = i & 0xFF;
        float f = (float)j / 255.0f;
        float f1 = (float)k / 255.0f;
        float f2 = (float)l / 255.0f;
        Vec3 vec3 = underFluidColorFader.getColor((double)f, (double)f1, (double)f2);
        return vec3;
    }

    private static int getStemColorMultiplier(Block blockStem, IBlockAccess blockAccess, BlockPos blockPos, RenderEnv renderEnv) {
        CustomColormap customcolormap = stemColors;
        if (blockStem == Blocks.pumpkin_stem && stemPumpkinColors != null) {
            customcolormap = stemPumpkinColors;
        }
        if (blockStem == Blocks.melon_stem && stemMelonColors != null) {
            customcolormap = stemMelonColors;
        }
        if (customcolormap == null) {
            return -1;
        }
        int i = renderEnv.getMetadata();
        return customcolormap.getColor(i);
    }

    public static boolean updateLightmap(World world, float torchFlickerX, int[] lmColors, boolean nightvision, float partialTicks) {
        if (world == null) {
            return false;
        }
        if (lightMapPacks == null) {
            return false;
        }
        int i = world.provider.getDimensionId();
        int j = i - lightmapMinDimensionId;
        if (j >= 0 && j < lightMapPacks.length) {
            LightMapPack lightmappack = lightMapPacks[j];
            return lightmappack == null ? false : lightmappack.updateLightmap(world, torchFlickerX, lmColors, nightvision, partialTicks);
        }
        return false;
    }

    public static Vec3 getWorldFogColor(Vec3 fogVec, World world, Entity renderViewEntity, float partialTicks) {
        int i = world.provider.getDimensionId();
        switch (i) {
            case -1: {
                fogVec = CustomColors.getFogColorNether(fogVec);
                break;
            }
            case 0: {
                Minecraft minecraft = Minecraft.getMinecraft();
                fogVec = CustomColors.getFogColor(fogVec, (IBlockAccess)minecraft.theWorld, renderViewEntity.posX, renderViewEntity.posY + 1.0, renderViewEntity.posZ);
                break;
            }
            case 1: {
                fogVec = CustomColors.getFogColorEnd(fogVec);
            }
        }
        return fogVec;
    }

    public static Vec3 getWorldSkyColor(Vec3 skyVec, World world, Entity renderViewEntity, float partialTicks) {
        int i = world.provider.getDimensionId();
        switch (i) {
            case 0: {
                Minecraft minecraft = Minecraft.getMinecraft();
                skyVec = CustomColors.getSkyColor(skyVec, (IBlockAccess)minecraft.theWorld, renderViewEntity.posX, renderViewEntity.posY + 1.0, renderViewEntity.posZ);
                break;
            }
            case 1: {
                skyVec = CustomColors.getSkyColorEnd(skyVec);
            }
        }
        return skyVec;
    }

    private static int[] readSpawnEggColors(Properties props, String fileName, String prefix, String logName) {
        ArrayList list = new ArrayList();
        Set set = props.keySet();
        int i = 0;
        for (String s : set) {
            String s1 = props.getProperty(s);
            if (!s.startsWith(prefix)) continue;
            String s2 = StrUtils.removePrefix((String)s, (String)prefix);
            int j = EntityUtils.getEntityIdByName((String)s2);
            if (j < 0) {
                CustomColors.warn("Invalid spawn egg name: " + s);
                continue;
            }
            int k = CustomColors.parseColor(s1);
            if (k < 0) {
                CustomColors.warn("Invalid spawn egg color: " + s + " = " + s1);
                continue;
            }
            while (list.size() <= j) {
                list.add((Object)-1);
            }
            list.set(j, (Object)k);
            ++i;
        }
        if (i <= 0) {
            return null;
        }
        CustomColors.dbg(logName + " colors: " + i);
        int[] aint = new int[list.size()];
        for (int l = 0; l < aint.length; ++l) {
            aint[l] = (Integer)list.get(l);
        }
        return aint;
    }

    private static int getSpawnEggColor(ItemMonsterPlacer item, ItemStack itemStack, int layer, int color) {
        int[] aint;
        int i = itemStack.getMetadata();
        int[] nArray = aint = layer == 0 ? spawnEggPrimaryColors : spawnEggSecondaryColors;
        if (aint == null) {
            return color;
        }
        if (i >= 0 && i < aint.length) {
            int j = aint[i];
            return j < 0 ? color : j;
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
        EnumDyeColor[] aenumdyecolor = EnumDyeColor.values();
        HashMap map = new HashMap();
        for (int i = 0; i < aenumdyecolor.length; ++i) {
            EnumDyeColor enumdyecolor = aenumdyecolor[i];
            map.put((Object)enumdyecolor.getName(), (Object)enumdyecolor);
        }
        float[][] afloat1 = new float[aenumdyecolor.length][];
        int k = 0;
        for (String s : props.keySet()) {
            String s1 = props.getProperty(s);
            if (!s.startsWith(prefix)) continue;
            String s2 = StrUtils.removePrefix((String)s, (String)prefix);
            if (s2.equals((Object)"lightBlue")) {
                s2 = "light_blue";
            }
            EnumDyeColor enumdyecolor1 = (EnumDyeColor)map.get((Object)s2);
            int j = CustomColors.parseColor(s1);
            if (enumdyecolor1 != null && j >= 0) {
                float[] afloat = new float[]{(float)(j >> 16 & 0xFF) / 255.0f, (float)(j >> 8 & 0xFF) / 255.0f, (float)(j & 0xFF) / 255.0f};
                afloat1[enumdyecolor1.ordinal()] = afloat;
                ++k;
                continue;
            }
            CustomColors.warn("Invalid color: " + s + " = " + s1);
        }
        if (k <= 0) {
            return null;
        }
        CustomColors.dbg(logName + " colors: " + k);
        return afloat1;
    }

    private static float[] getDyeColors(EnumDyeColor dye, float[][] dyeColors, float[] colors) {
        if (dyeColors == null) {
            return colors;
        }
        if (dye == null) {
            return colors;
        }
        float[] afloat = dyeColors[dye.ordinal()];
        return afloat == null ? colors : afloat;
    }

    public static float[] getWolfCollarColors(EnumDyeColor dye, float[] colors) {
        return CustomColors.getDyeColors(dye, wolfCollarColors, colors);
    }

    public static float[] getSheepColors(EnumDyeColor dye, float[] colors) {
        return CustomColors.getDyeColors(dye, sheepColors, colors);
    }

    private static int[] readTextColors(Properties props, String fileName, String prefix, String logName) {
        int[] aint = new int[32];
        Arrays.fill((int[])aint, (int)-1);
        int i = 0;
        for (String s : props.keySet()) {
            String s1 = props.getProperty(s);
            if (!s.startsWith(prefix)) continue;
            String s2 = StrUtils.removePrefix((String)s, (String)prefix);
            int j = Config.parseInt((String)s2, (int)-1);
            int k = CustomColors.parseColor(s1);
            if (j >= 0 && j < aint.length && k >= 0) {
                aint[j] = k;
                ++i;
                continue;
            }
            CustomColors.warn("Invalid color: " + s + " = " + s1);
        }
        if (i <= 0) {
            return null;
        }
        CustomColors.dbg(logName + " colors: " + i);
        return aint;
    }

    public static int getTextColor(int index, int color) {
        if (textColors == null) {
            return color;
        }
        if (index >= 0 && index < textColors.length) {
            int i = textColors[index];
            return i < 0 ? color : i;
        }
        return color;
    }

    private static int[] readMapColors(Properties props, String fileName, String prefix, String logName) {
        int[] aint = new int[MapColor.mapColorArray.length];
        Arrays.fill((int[])aint, (int)-1);
        int i = 0;
        for (String s : props.keySet()) {
            String s1 = props.getProperty(s);
            if (!s.startsWith(prefix)) continue;
            String s2 = StrUtils.removePrefix((String)s, (String)prefix);
            int j = CustomColors.getMapColorIndex(s2);
            int k = CustomColors.parseColor(s1);
            if (j >= 0 && j < aint.length && k >= 0) {
                aint[j] = k;
                ++i;
                continue;
            }
            CustomColors.warn("Invalid color: " + s + " = " + s1);
        }
        if (i <= 0) {
            return null;
        }
        CustomColors.dbg(logName + " colors: " + i);
        return aint;
    }

    private static int[] readPotionColors(Properties props, String fileName, String prefix, String logName) {
        int[] aint = new int[Potion.potionTypes.length];
        Arrays.fill((int[])aint, (int)-1);
        int i = 0;
        for (String s : props.keySet()) {
            String s1 = props.getProperty(s);
            if (!s.startsWith(prefix)) continue;
            int j = CustomColors.getPotionId(s);
            int k = CustomColors.parseColor(s1);
            if (j >= 0 && j < aint.length && k >= 0) {
                aint[j] = k;
                ++i;
                continue;
            }
            CustomColors.warn("Invalid color: " + s + " = " + s1);
        }
        if (i <= 0) {
            return null;
        }
        CustomColors.dbg(logName + " colors: " + i);
        return aint;
    }

    private static int getPotionId(String name) {
        if (name.equals((Object)"potion.water")) {
            return 0;
        }
        Potion[] apotion = Potion.potionTypes;
        for (int i = 0; i < apotion.length; ++i) {
            Potion potion = apotion[i];
            if (potion == null || !potion.getName().equals((Object)name)) continue;
            return potion.getId();
        }
        return -1;
    }

    public static int getPotionColor(int potionId, int color) {
        if (potionColors == null) {
            return color;
        }
        if (potionId >= 0 && potionId < potionColors.length) {
            int i = potionColors[potionId];
            return i < 0 ? color : i;
        }
        return color;
    }

    private static int getMapColorIndex(String name) {
        return name == null ? -1 : (name.equals((Object)"air") ? MapColor.airColor.colorIndex : (name.equals((Object)"grass") ? MapColor.grassColor.colorIndex : (name.equals((Object)"sand") ? MapColor.sandColor.colorIndex : (name.equals((Object)"cloth") ? MapColor.clothColor.colorIndex : (name.equals((Object)"tnt") ? MapColor.tntColor.colorIndex : (name.equals((Object)"ice") ? MapColor.iceColor.colorIndex : (name.equals((Object)"iron") ? MapColor.ironColor.colorIndex : (name.equals((Object)"foliage") ? MapColor.foliageColor.colorIndex : (name.equals((Object)"clay") ? MapColor.clayColor.colorIndex : (name.equals((Object)"dirt") ? MapColor.dirtColor.colorIndex : (name.equals((Object)"stone") ? MapColor.stoneColor.colorIndex : (name.equals((Object)"water") ? MapColor.waterColor.colorIndex : (name.equals((Object)"wood") ? MapColor.woodColor.colorIndex : (name.equals((Object)"quartz") ? MapColor.quartzColor.colorIndex : (name.equals((Object)"gold") ? MapColor.goldColor.colorIndex : (name.equals((Object)"diamond") ? MapColor.diamondColor.colorIndex : (name.equals((Object)"lapis") ? MapColor.lapisColor.colorIndex : (name.equals((Object)"emerald") ? MapColor.emeraldColor.colorIndex : (name.equals((Object)"podzol") ? MapColor.obsidianColor.colorIndex : (name.equals((Object)"netherrack") ? MapColor.netherrackColor.colorIndex : (!name.equals((Object)"snow") && !name.equals((Object)"white") ? (!name.equals((Object)"adobe") && !name.equals((Object)"orange") ? (name.equals((Object)"magenta") ? MapColor.magentaColor.colorIndex : (!name.equals((Object)"light_blue") && !name.equals((Object)"lightBlue") ? (name.equals((Object)"yellow") ? MapColor.yellowColor.colorIndex : (name.equals((Object)"lime") ? MapColor.limeColor.colorIndex : (name.equals((Object)"pink") ? MapColor.pinkColor.colorIndex : (name.equals((Object)"gray") ? MapColor.grayColor.colorIndex : (name.equals((Object)"silver") ? MapColor.silverColor.colorIndex : (name.equals((Object)"cyan") ? MapColor.cyanColor.colorIndex : (name.equals((Object)"purple") ? MapColor.purpleColor.colorIndex : (name.equals((Object)"blue") ? MapColor.blueColor.colorIndex : (name.equals((Object)"brown") ? MapColor.brownColor.colorIndex : (name.equals((Object)"green") ? MapColor.greenColor.colorIndex : (name.equals((Object)"red") ? MapColor.redColor.colorIndex : (name.equals((Object)"black") ? MapColor.blackColor.colorIndex : -1)))))))))))) : MapColor.lightBlueColor.colorIndex)) : MapColor.adobeColor.colorIndex) : MapColor.snowColor.colorIndex)))))))))))))))))))));
    }

    private static int[] getMapColors() {
        MapColor[] amapcolor = MapColor.mapColorArray;
        int[] aint = new int[amapcolor.length];
        Arrays.fill((int[])aint, (int)-1);
        for (int i = 0; i < amapcolor.length && i < aint.length; ++i) {
            MapColor mapcolor = amapcolor[i];
            if (mapcolor == null) continue;
            aint[i] = mapcolor.colorValue;
        }
        return aint;
    }

    private static void setMapColors(int[] colors) {
        if (colors != null) {
            MapColor[] amapcolor = MapColor.mapColorArray;
            boolean flag = false;
            for (int i = 0; i < amapcolor.length && i < colors.length; ++i) {
                int j;
                MapColor mapcolor = amapcolor[i];
                if (mapcolor == null || (j = colors[i]) < 0 || mapcolor.colorValue == j) continue;
                mapcolor.colorValue = j;
                flag = true;
            }
            if (flag) {
                Minecraft.getMinecraft().getTextureManager().reloadBannerTextures();
            }
        }
    }

    private static void dbg(String str) {
        Config.dbg((String)("CustomColors: " + str));
    }

    private static void warn(String str) {
        Config.warn((String)("CustomColors: " + str));
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

    static /* synthetic */ CustomColormap access$000() {
        return swampGrassColors;
    }

    static /* synthetic */ CustomColormap access$100() {
        return swampFoliageColors;
    }

    static /* synthetic */ CustomColormap access$200() {
        return foliagePineColors;
    }

    static /* synthetic */ CustomColormap access$300() {
        return foliageBirchColors;
    }

    static /* synthetic */ CustomColormap access$400() {
        return waterColors;
    }
}
