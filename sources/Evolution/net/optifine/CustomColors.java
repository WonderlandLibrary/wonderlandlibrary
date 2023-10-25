package net.optifine;

import java.awt.image.BufferedImage;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
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
import net.minecraft.client.particle.Particle;
import net.minecraft.client.renderer.block.model.BakedQuad;
import net.minecraft.client.renderer.color.BlockColors;
import net.minecraft.entity.Entity;
import net.minecraft.init.Biomes;
import net.minecraft.init.Blocks;
import net.minecraft.item.EnumDyeColor;
import net.minecraft.item.Item;
import net.minecraft.item.ItemMonsterPlacer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.potion.Potion;
import net.minecraft.src.Config;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.ColorizerFoliage;
import net.minecraft.world.DimensionType;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraft.world.biome.Biome;
import net.optifine.config.ConnectedParser;
import net.optifine.config.MatchBlock;
import net.optifine.reflect.Reflector;
import net.optifine.render.RenderEnv;
import net.optifine.util.EntityUtils;
import net.optifine.util.PropertiesOrdered;
import net.optifine.util.ResUtils;
import net.optifine.util.StrUtils;
import net.optifine.util.TextureUtils;
import org.apache.commons.lang3.tuple.ImmutablePair;
import org.apache.commons.lang3.tuple.Pair;

public class CustomColors
{
    private static String paletteFormatDefault = "vanilla";
    private static CustomColormap waterColors = null;
    private static CustomColormap foliagePineColors = null;
    private static CustomColormap foliageBirchColors = null;
    private static CustomColormap swampFoliageColors = null;
    private static CustomColormap swampGrassColors = null;
    private static CustomColormap[] colorsBlockColormaps = null;
    private static CustomColormap[][] blockColormaps = (CustomColormap[][])null;
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
    private static Vec3d fogColorNether = null;
    private static Vec3d fogColorEnd = null;
    private static Vec3d skyColorEnd = null;
    private static int[] spawnEggPrimaryColors = null;
    private static int[] spawnEggSecondaryColors = null;
    private static float[][] wolfCollarColors = (float[][])null;
    private static float[][] sheepColors = (float[][])null;
    private static int[] textColors = null;
    private static int[] mapColorsOriginal = null;
    private static int[] potionColors = null;
    private static final IBlockState BLOCK_STATE_DIRT = Blocks.DIRT.getDefaultState();
    private static final IBlockState BLOCK_STATE_WATER = Blocks.WATER.getDefaultState();
    public static Random random = new Random();
    private static final IColorizer COLORIZER_GRASS = new IColorizer()
    {
        public int getColor(IBlockState blockState, IBlockAccess blockAccess, BlockPos blockPos)
        {
            Biome biome = CustomColors.getColorBiome(blockAccess, blockPos);
            return CustomColors.swampGrassColors != null && biome == Biomes.SWAMPLAND ? CustomColors.swampGrassColors.getColor(biome, blockPos) : biome.getGrassColorAtPos(blockPos);
        }
        public boolean isColorConstant()
        {
            return false;
        }
    };
    private static final IColorizer COLORIZER_FOLIAGE = new IColorizer()
    {
        public int getColor(IBlockState blockState, IBlockAccess blockAccess, BlockPos blockPos)
        {
            Biome biome = CustomColors.getColorBiome(blockAccess, blockPos);
            return CustomColors.swampFoliageColors != null && biome == Biomes.SWAMPLAND ? CustomColors.swampFoliageColors.getColor(biome, blockPos) : biome.getFoliageColorAtPos(blockPos);
        }
        public boolean isColorConstant()
        {
            return false;
        }
    };
    private static final IColorizer COLORIZER_FOLIAGE_PINE = new IColorizer()
    {
        public int getColor(IBlockState blockState, IBlockAccess blockAccess, BlockPos blockPos)
        {
            return CustomColors.foliagePineColors != null ? CustomColors.foliagePineColors.getColor(blockAccess, blockPos) : ColorizerFoliage.getFoliageColorPine();
        }
        public boolean isColorConstant()
        {
            return CustomColors.foliagePineColors == null;
        }
    };
    private static final IColorizer COLORIZER_FOLIAGE_BIRCH = new IColorizer()
    {
        public int getColor(IBlockState blockState, IBlockAccess blockAccess, BlockPos blockPos)
        {
            return CustomColors.foliageBirchColors != null ? CustomColors.foliageBirchColors.getColor(blockAccess, blockPos) : ColorizerFoliage.getFoliageColorBirch();
        }
        public boolean isColorConstant()
        {
            return CustomColors.foliageBirchColors == null;
        }
    };
    private static final IColorizer COLORIZER_WATER = new IColorizer()
    {
        public int getColor(IBlockState blockState, IBlockAccess blockAccess, BlockPos blockPos)
        {
            Biome biome = CustomColors.getColorBiome(blockAccess, blockPos);

            if (CustomColors.waterColors != null)
            {
                return CustomColors.waterColors.getColor(biome, blockPos);
            }
            else
            {
                return Reflector.ForgeBiome_getWaterColorMultiplier.exists() ? Reflector.callInt(biome, Reflector.ForgeBiome_getWaterColorMultiplier) : biome.getWaterColor();
            }
        }
        public boolean isColorConstant()
        {
            return false;
        }
    };

    public static void update()
    {
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
        blockColormaps = (CustomColormap[][])null;
        useDefaultGrassFoliageColors = true;
        spawnEggPrimaryColors = null;
        spawnEggSecondaryColors = null;
        wolfCollarColors = (float[][])null;
        sheepColors = (float[][])null;
        textColors = null;
        setMapColors(mapColorsOriginal);
        potionColors = null;
        paletteFormatDefault = getValidProperty("mcpatcher/color.properties", "palette.format", CustomColormap.FORMAT_STRINGS, "vanilla");
        String s = "mcpatcher/colormap/";
        String[] astring = new String[] {"water.png", "watercolorX.png"};
        waterColors = getCustomColors(s, astring, 256, 256);
        updateUseDefaultGrassFoliageColors();

        if (Config.isCustomColors())
        {
            String[] astring1 = new String[] {"pine.png", "pinecolor.png"};
            foliagePineColors = getCustomColors(s, astring1, 256, 256);
            String[] astring2 = new String[] {"birch.png", "birchcolor.png"};
            foliageBirchColors = getCustomColors(s, astring2, 256, 256);
            String[] astring3 = new String[] {"swampgrass.png", "swampgrasscolor.png"};
            swampGrassColors = getCustomColors(s, astring3, 256, 256);
            String[] astring4 = new String[] {"swampfoliage.png", "swampfoliagecolor.png"};
            swampFoliageColors = getCustomColors(s, astring4, 256, 256);
            String[] astring5 = new String[] {"sky0.png", "skycolor0.png"};
            skyColors = getCustomColors(s, astring5, 256, 256);
            String[] astring6 = new String[] {"fog0.png", "fogcolor0.png"};
            fogColors = getCustomColors(s, astring6, 256, 256);
            String[] astring7 = new String[] {"underwater.png", "underwatercolor.png"};
            underwaterColors = getCustomColors(s, astring7, 256, 256);
            String[] astring8 = new String[] {"underlava.png", "underlavacolor.png"};
            underlavaColors = getCustomColors(s, astring8, 256, 256);
            String[] astring9 = new String[] {"redstone.png", "redstonecolor.png"};
            redstoneColors = getCustomColors(s, astring9, 16, 1);
            xpOrbColors = getCustomColors(s + "xporb.png", -1, -1);
            durabilityColors = getCustomColors(s + "durability.png", -1, -1);
            String[] astring10 = new String[] {"stem.png", "stemcolor.png"};
            stemColors = getCustomColors(s, astring10, 8, 1);
            stemPumpkinColors = getCustomColors(s + "pumpkinstem.png", 8, 1);
            stemMelonColors = getCustomColors(s + "melonstem.png", 8, 1);
            String[] astring11 = new String[] {"myceliumparticle.png", "myceliumparticlecolor.png"};
            myceliumParticleColors = getCustomColors(s, astring11, -1, -1);
            Pair<LightMapPack[], Integer> pair = parseLightMapPacks();
            lightMapPacks = pair.getLeft();
            lightmapMinDimensionId = ((Integer)pair.getRight()).intValue();
            readColorProperties("mcpatcher/color.properties");
            blockColormaps = readBlockColormaps(new String[] {s + "custom/", s + "blocks/"}, colorsBlockColormaps, 256, 256);
            updateUseDefaultGrassFoliageColors();
        }
    }

    private static String getValidProperty(String fileName, String key, String[] validValues, String valDef)
    {
        try
        {
            ResourceLocation resourcelocation = new ResourceLocation(fileName);
            InputStream inputstream = Config.getResourceStream(resourcelocation);

            if (inputstream == null)
            {
                return valDef;
            }
            else
            {
                Properties properties = new PropertiesOrdered();
                properties.load(inputstream);
                inputstream.close();
                String s = properties.getProperty(key);

                if (s == null)
                {
                    return valDef;
                }
                else
                {
                    List<String> list = Arrays.<String>asList(validValues);

                    if (!list.contains(s))
                    {
                        warn("Invalid value: " + key + "=" + s);
                        warn("Expected values: " + Config.arrayToString((Object[])validValues));
                        return valDef;
                    }
                    else
                    {
                        dbg("" + key + "=" + s);
                        return s;
                    }
                }
            }
        }
        catch (FileNotFoundException var9)
        {
            return valDef;
        }
        catch (IOException ioexception)
        {
            ioexception.printStackTrace();
            return valDef;
        }
    }

    private static Pair<LightMapPack[], Integer> parseLightMapPacks()
    {
        String s = "mcpatcher/lightmap/world";
        String s1 = ".png";
        String[] astring = ResUtils.collectFiles(s, s1);
        Map<Integer, String> map = new HashMap<Integer, String>();

        for (int i = 0; i < astring.length; ++i)
        {
            String s2 = astring[i];
            String s3 = StrUtils.removePrefixSuffix(s2, s, s1);
            int j = Config.parseInt(s3, Integer.MIN_VALUE);

            if (j == Integer.MIN_VALUE)
            {
                warn("Invalid dimension ID: " + s3 + ", path: " + s2);
            }
            else
            {
                map.put(Integer.valueOf(j), s2);
            }
        }

        Set<Integer> set = map.keySet();
        Integer[] ainteger = (Integer[])set.toArray(new Integer[set.size()]);
        Arrays.sort((Object[])ainteger);

        if (ainteger.length <= 0)
        {
            return new ImmutablePair<LightMapPack[], Integer>(null, Integer.valueOf(0));
        }
        else
        {
            int j1 = ainteger[0].intValue();
            int k1 = ainteger[ainteger.length - 1].intValue();
            int k = k1 - j1 + 1;
            CustomColormap[] acustomcolormap = new CustomColormap[k];

            for (int l = 0; l < ainteger.length; ++l)
            {
                Integer integer = ainteger[l];
                String s4 = map.get(integer);
                CustomColormap customcolormap = getCustomColors(s4, -1, -1);

                if (customcolormap != null)
                {
                    if (customcolormap.getWidth() < 16)
                    {
                        warn("Invalid lightmap width: " + customcolormap.getWidth() + ", path: " + s4);
                    }
                    else
                    {
                        int i1 = integer.intValue() - j1;
                        acustomcolormap[i1] = customcolormap;
                    }
                }
            }

            LightMapPack[] alightmappack = new LightMapPack[acustomcolormap.length];

            for (int l1 = 0; l1 < acustomcolormap.length; ++l1)
            {
                CustomColormap customcolormap3 = acustomcolormap[l1];

                if (customcolormap3 != null)
                {
                    String s5 = customcolormap3.name;
                    String s6 = customcolormap3.basePath;
                    CustomColormap customcolormap1 = getCustomColors(s6 + "/" + s5 + "_rain.png", -1, -1);
                    CustomColormap customcolormap2 = getCustomColors(s6 + "/" + s5 + "_thunder.png", -1, -1);
                    LightMap lightmap = new LightMap(customcolormap3);
                    LightMap lightmap1 = customcolormap1 != null ? new LightMap(customcolormap1) : null;
                    LightMap lightmap2 = customcolormap2 != null ? new LightMap(customcolormap2) : null;
                    LightMapPack lightmappack = new LightMapPack(lightmap, lightmap1, lightmap2);
                    alightmappack[l1] = lightmappack;
                }
            }

            return new ImmutablePair<LightMapPack[], Integer>(alightmappack, j1);
        }
    }

    private static int getTextureHeight(String path, int defHeight)
    {
        try
        {
            InputStream inputstream = Config.getResourceStream(new ResourceLocation(path));

            if (inputstream == null)
            {
                return defHeight;
            }
            else
            {
                BufferedImage bufferedimage = ImageIO.read(inputstream);
                inputstream.close();
                return bufferedimage == null ? defHeight : bufferedimage.getHeight();
            }
        }
        catch (IOException var4)
        {
            return defHeight;
        }
    }

    private static void readColorProperties(String fileName)
    {
        try
        {
            ResourceLocation resourcelocation = new ResourceLocation(fileName);
            InputStream inputstream = Config.getResourceStream(resourcelocation);

            if (inputstream == null)
            {
                return;
            }

            dbg("Loading " + fileName);
            Properties properties = new PropertiesOrdered();
            properties.load(inputstream);
            inputstream.close();
            particleWaterColor = readColor(properties, new String[] {"particle.water", "drop.water"});
            particlePortalColor = readColor(properties, "particle.portal");
            lilyPadColor = readColor(properties, "lilypad");
            expBarTextColor = readColor(properties, "text.xpbar");
            bossTextColor = readColor(properties, "text.boss");
            signTextColor = readColor(properties, "text.sign");
            fogColorNether = readColorVec3(properties, "fog.nether");
            fogColorEnd = readColorVec3(properties, "fog.end");
            skyColorEnd = readColorVec3(properties, "sky.end");
            colorsBlockColormaps = readCustomColormaps(properties, fileName);
            spawnEggPrimaryColors = readSpawnEggColors(properties, fileName, "egg.shell.", "Spawn egg shell");
            spawnEggSecondaryColors = readSpawnEggColors(properties, fileName, "egg.spots.", "Spawn egg spot");
            wolfCollarColors = readDyeColors(properties, fileName, "collar.", "Wolf collar");
            sheepColors = readDyeColors(properties, fileName, "sheep.", "Sheep");
            textColors = readTextColors(properties, fileName, "text.code.", "Text");
            int[] aint = readMapColors(properties, fileName, "map.", "Map");

            if (aint != null)
            {
                if (mapColorsOriginal == null)
                {
                    mapColorsOriginal = getMapColors();
                }

                setMapColors(aint);
            }

            potionColors = readPotionColors(properties, fileName, "potion.", "Potion");
            xpOrbTime = Config.parseInt(properties.getProperty("xporb.time"), -1);
        }
        catch (FileNotFoundException var5)
        {
            return;
        }
        catch (IOException ioexception)
        {
            ioexception.printStackTrace();
        }
    }

    private static CustomColormap[] readCustomColormaps(Properties props, String fileName)
    {
        List list = new ArrayList();
        String s = "palette.block.";
        Map map = new HashMap();

        for (Object s11 : props.keySet())
        {
            String s1 = (String) s11;
            String s2 = props.getProperty(s1);

            if (s1.startsWith(s))
            {
                map.put(s1, s2);
            }
        }

        String[] astring = (String[])map.keySet().toArray(new String[map.size()]);

        for (int j = 0; j < astring.length; ++j)
        {
            String s6 = astring[j];
            String s3 = props.getProperty(s6);
            dbg("Block palette: " + s6 + " = " + s3);
            String s4 = s6.substring(s.length());
            String s5 = TextureUtils.getBasePath(fileName);
            s4 = TextureUtils.fixResourcePath(s4, s5);
            CustomColormap customcolormap = getCustomColors(s4, 256, 256);

            if (customcolormap == null)
            {
                warn("Colormap not found: " + s4);
            }
            else
            {
                ConnectedParser connectedparser = new ConnectedParser("CustomColors");
                MatchBlock[] amatchblock = connectedparser.parseMatchBlocks(s3);

                if (amatchblock != null && amatchblock.length > 0)
                {
                    for (int i = 0; i < amatchblock.length; ++i)
                    {
                        MatchBlock matchblock = amatchblock[i];
                        customcolormap.addMatchBlock(matchblock);
                    }

                    list.add(customcolormap);
                }
                else
                {
                    warn("Invalid match blocks: " + s3);
                }
            }
        }

        if (list.size() <= 0)
        {
            return null;
        }
        else
        {
            CustomColormap[] acustomcolormap = (CustomColormap[])list.toArray(new CustomColormap[list.size()]);
            return acustomcolormap;
        }
    }

    private static CustomColormap[][] readBlockColormaps(String[] basePaths, CustomColormap[] basePalettes, int width, int height)
    {
        String[] astring = ResUtils.collectFiles(basePaths, new String[] {".properties"});
        Arrays.sort((Object[])astring);
        List list = new ArrayList();

        for (int i = 0; i < astring.length; ++i)
        {
            String s = astring[i];
            dbg("Block colormap: " + s);

            try
            {
                ResourceLocation resourcelocation = new ResourceLocation("minecraft", s);
                InputStream inputstream = Config.getResourceStream(resourcelocation);

                if (inputstream == null)
                {
                    warn("File not found: " + s);
                }
                else
                {
                    Properties properties = new PropertiesOrdered();
                    properties.load(inputstream);
                    inputstream.close();
                    CustomColormap customcolormap = new CustomColormap(properties, s, width, height, paletteFormatDefault);

                    if (customcolormap.isValid(s) && customcolormap.isValidMatchBlocks(s))
                    {
                        addToBlockList(customcolormap, list);
                    }
                }
            }
            catch (FileNotFoundException var12)
            {
                warn("File not found: " + s);
            }
            catch (Exception exception)
            {
                exception.printStackTrace();
            }
        }

        if (basePalettes != null)
        {
            for (int j = 0; j < basePalettes.length; ++j)
            {
                CustomColormap customcolormap1 = basePalettes[j];
                addToBlockList(customcolormap1, list);
            }
        }

        if (list.size() <= 0)
        {
            return (CustomColormap[][])null;
        }
        else
        {
            CustomColormap[][] acustomcolormap = blockListToArray(list);
            return acustomcolormap;
        }
    }

    private static void addToBlockList(CustomColormap cm, List blockList)
    {
        int[] aint = cm.getMatchBlockIds();

        if (aint != null && aint.length > 0)
        {
            for (int i = 0; i < aint.length; ++i)
            {
                int j = aint[i];

                if (j < 0)
                {
                    warn("Invalid block ID: " + j);
                }
                else
                {
                    addToList(cm, blockList, j);
                }
            }
        }
        else
        {
            warn("No match blocks: " + Config.arrayToString(aint));
        }
    }

    private static void addToList(CustomColormap cm, List list, int id)
    {
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

    private static CustomColormap[][] blockListToArray(List list)
    {
        CustomColormap[][] colArr = new CustomColormap[list.size()][];
        for (int i2 = 0; i2 < list.size(); ++i2) {
            List<CustomColormap> subList = (List<CustomColormap>) list.get(i2);
            if (subList == null) continue;
            CustomColormap[] subArr = subList.toArray(new CustomColormap[subList.size()]);
            colArr[i2] = subArr;
        }
        return colArr;
    }

    private static int readColor(Properties props, String[] names)
    {
        for (int i = 0; i < names.length; ++i)
        {
            String s = names[i];
            int j = readColor(props, s);

            if (j >= 0)
            {
                return j;
            }
        }

        return -1;
    }

    private static int readColor(Properties props, String name)
    {
        String s = props.getProperty(name);

        if (s == null)
        {
            return -1;
        }
        else
        {
            s = s.trim();
            int i = parseColor(s);

            if (i < 0)
            {
                warn("Invalid color: " + name + " = " + s);
                return i;
            }
            else
            {
                dbg(name + " = " + s);
                return i;
            }
        }
    }

    private static int parseColor(String str)
    {
        if (str == null)
        {
            return -1;
        }
        else
        {
            str = str.trim();

            try
            {
                int i = Integer.parseInt(str, 16) & 16777215;
                return i;
            }
            catch (NumberFormatException var2)
            {
                return -1;
            }
        }
    }

    private static Vec3d readColorVec3(Properties props, String name)
    {
        int i = readColor(props, name);

        if (i < 0)
        {
            return null;
        }
        else
        {
            int j = i >> 16 & 255;
            int k = i >> 8 & 255;
            int l = i & 255;
            float f = (float)j / 255.0F;
            float f1 = (float)k / 255.0F;
            float f2 = (float)l / 255.0F;
            return new Vec3d((double)f, (double)f1, (double)f2);
        }
    }

    private static CustomColormap getCustomColors(String basePath, String[] paths, int width, int height)
    {
        for (int i = 0; i < paths.length; ++i)
        {
            String s = paths[i];
            s = basePath + s;
            CustomColormap customcolormap = getCustomColors(s, width, height);

            if (customcolormap != null)
            {
                return customcolormap;
            }
        }

        return null;
    }

    public static CustomColormap getCustomColors(String pathImage, int width, int height)
    {
        try
        {
            ResourceLocation resourcelocation = new ResourceLocation(pathImage);

            if (!Config.hasResource(resourcelocation))
            {
                return null;
            }
            else
            {
                dbg("Colormap " + pathImage);
                Properties properties = new PropertiesOrdered();
                String s = StrUtils.replaceSuffix(pathImage, ".png", ".properties");
                ResourceLocation resourcelocation1 = new ResourceLocation(s);

                if (Config.hasResource(resourcelocation1))
                {
                    InputStream inputstream = Config.getResourceStream(resourcelocation1);
                    properties.load(inputstream);
                    inputstream.close();
                    dbg("Colormap properties: " + s);
                }
                else
                {
                    properties.put("format", paletteFormatDefault);
                    properties.put("source", pathImage);
                    s = pathImage;
                }

                CustomColormap customcolormap = new CustomColormap(properties, s, width, height, paletteFormatDefault);
                return !customcolormap.isValid(s) ? null : customcolormap;
            }
        }
        catch (Exception exception)
        {
            exception.printStackTrace();
            return null;
        }
    }

    public static void updateUseDefaultGrassFoliageColors()
    {
        useDefaultGrassFoliageColors = foliageBirchColors == null && foliagePineColors == null && swampGrassColors == null && swampFoliageColors == null && Config.isSwampColors() && Config.isSmoothBiomes();
    }

    public static int getColorMultiplier(BakedQuad quad, IBlockState blockState, IBlockAccess blockAccess, BlockPos blockPos, RenderEnv renderEnv)
    {
        Block block = blockState.getBlock();
        IBlockState iblockstate = renderEnv.getBlockState();

        if (blockColormaps != null)
        {
            if (!quad.hasTintIndex())
            {
                if (block == Blocks.GRASS)
                {
                    iblockstate = BLOCK_STATE_DIRT;
                }

                if (block == Blocks.REDSTONE_WIRE)
                {
                    return -1;
                }
            }

            if (block == Blocks.DOUBLE_PLANT && renderEnv.getMetadata() >= 8)
            {
                blockPos = blockPos.down();
                iblockstate = blockAccess.getBlockState(blockPos);
            }

            CustomColormap customcolormap = getBlockColormap(iblockstate);

            if (customcolormap != null)
            {
                if (Config.isSmoothBiomes() && !customcolormap.isColorConstant())
                {
                    return getSmoothColorMultiplier(blockState, blockAccess, blockPos, customcolormap, renderEnv.getColorizerBlockPosM());
                }

                return customcolormap.getColor(blockAccess, blockPos);
            }
        }

        if (!quad.hasTintIndex())
        {
            return -1;
        }
        else if (block == Blocks.WATERLILY)
        {
            return getLilypadColorMultiplier(blockAccess, blockPos);
        }
        else if (block == Blocks.REDSTONE_WIRE)
        {
            return getRedstoneColor(renderEnv.getBlockState());
        }
        else if (block instanceof BlockStem)
        {
            return getStemColorMultiplier(block, blockAccess, blockPos, renderEnv);
        }
        else if (useDefaultGrassFoliageColors)
        {
            return -1;
        }
        else
        {
            int i = renderEnv.getMetadata();
            IColorizer customcolors$icolorizer;

            if (block != Blocks.GRASS && block != Blocks.TALLGRASS && block != Blocks.DOUBLE_PLANT)
            {
                if (block == Blocks.DOUBLE_PLANT)
                {
                    customcolors$icolorizer = COLORIZER_GRASS;

                    if (i >= 8)
                    {
                        blockPos = blockPos.down();
                    }
                }
                else if (block == Blocks.LEAVES)
                {
                    switch (i & 3)
                    {
                        case 0:
                            customcolors$icolorizer = COLORIZER_FOLIAGE;
                            break;

                        case 1:
                            customcolors$icolorizer = COLORIZER_FOLIAGE_PINE;
                            break;

                        case 2:
                            customcolors$icolorizer = COLORIZER_FOLIAGE_BIRCH;
                            break;

                        default:
                            customcolors$icolorizer = COLORIZER_FOLIAGE;
                    }
                }
                else if (block == Blocks.LEAVES2)
                {
                    customcolors$icolorizer = COLORIZER_FOLIAGE;
                }
                else
                {
                    if (block != Blocks.VINE)
                    {
                        return -1;
                    }

                    customcolors$icolorizer = COLORIZER_FOLIAGE;
                }
            }
            else
            {
                customcolors$icolorizer = COLORIZER_GRASS;
            }

            return Config.isSmoothBiomes() && !customcolors$icolorizer.isColorConstant() ? getSmoothColorMultiplier(blockState, blockAccess, blockPos, customcolors$icolorizer, renderEnv.getColorizerBlockPosM()) : customcolors$icolorizer.getColor(iblockstate, blockAccess, blockPos);
        }
    }

    protected static Biome getColorBiome(IBlockAccess blockAccess, BlockPos blockPos)
    {
        Biome biome = blockAccess.getBiome(blockPos);

        if (biome == Biomes.SWAMPLAND && !Config.isSwampColors())
        {
            biome = Biomes.PLAINS;
        }

        return biome;
    }

    private static CustomColormap getBlockColormap(IBlockState blockState)
    {
        if (blockColormaps == null)
        {
            return null;
        }
        else if (!(blockState instanceof BlockStateBase))
        {
            return null;
        }
        else
        {
            BlockStateBase blockstatebase = (BlockStateBase)blockState;
            int i = blockstatebase.getBlockId();

            if (i >= 0 && i < blockColormaps.length)
            {
                CustomColormap[] acustomcolormap = blockColormaps[i];

                if (acustomcolormap == null)
                {
                    return null;
                }
                else
                {
                    for (int j = 0; j < acustomcolormap.length; ++j)
                    {
                        CustomColormap customcolormap = acustomcolormap[j];

                        if (customcolormap.matchesBlock(blockstatebase))
                        {
                            return customcolormap;
                        }
                    }

                    return null;
                }
            }
            else
            {
                return null;
            }
        }
    }

    private static int getSmoothColorMultiplier(IBlockState blockState, IBlockAccess blockAccess, BlockPos blockPos, IColorizer colorizer, BlockPosM blockPosM)
    {
        int i = 0;
        int j = 0;
        int k = 0;
        int l = blockPos.getX();
        int i1 = blockPos.getY();
        int j1 = blockPos.getZ();
        BlockPosM blockposm = blockPosM;

        for (int k1 = l - 1; k1 <= l + 1; ++k1)
        {
            for (int l1 = j1 - 1; l1 <= j1 + 1; ++l1)
            {
                blockposm.setXyz(k1, i1, l1);
                int i2 = colorizer.getColor(blockState, blockAccess, blockposm);
                i += i2 >> 16 & 255;
                j += i2 >> 8 & 255;
                k += i2 & 255;
            }
        }

        int j2 = i / 9;
        int k2 = j / 9;
        int l2 = k / 9;
        return j2 << 16 | k2 << 8 | l2;
    }

    public static int getFluidColor(IBlockAccess blockAccess, IBlockState blockState, BlockPos blockPos, RenderEnv renderEnv)
    {
        Block block = blockState.getBlock();
        IColorizer customcolors$icolorizer = getBlockColormap(blockState);

        if (customcolors$icolorizer == null && blockState.getMaterial() == Material.WATER)
        {
            customcolors$icolorizer = COLORIZER_WATER;
        }

        if (customcolors$icolorizer == null)
        {
            return getBlockColors().colorMultiplier(blockState, blockAccess, blockPos, 0);
        }
        else
        {
            return Config.isSmoothBiomes() && !customcolors$icolorizer.isColorConstant() ? getSmoothColorMultiplier(blockState, blockAccess, blockPos, customcolors$icolorizer, renderEnv.getColorizerBlockPosM()) : customcolors$icolorizer.getColor(blockState, blockAccess, blockPos);
        }
    }

    public static BlockColors getBlockColors()
    {
        return Minecraft.getMinecraft().getBlockColors();
    }

    public static void updatePortalFX(Particle fx)
    {
        if (particlePortalColor >= 0)
        {
            int i = particlePortalColor;
            int j = i >> 16 & 255;
            int k = i >> 8 & 255;
            int l = i & 255;
            float f = (float)j / 255.0F;
            float f1 = (float)k / 255.0F;
            float f2 = (float)l / 255.0F;
            fx.setRBGColorF(f, f1, f2);
        }
    }

    public static void updateMyceliumFX(Particle fx)
    {
        if (myceliumParticleColors != null)
        {
            int i = myceliumParticleColors.getColorRandom();
            int j = i >> 16 & 255;
            int k = i >> 8 & 255;
            int l = i & 255;
            float f = (float)j / 255.0F;
            float f1 = (float)k / 255.0F;
            float f2 = (float)l / 255.0F;
            fx.setRBGColorF(f, f1, f2);
        }
    }

    private static int getRedstoneColor(IBlockState blockState)
    {
        if (redstoneColors == null)
        {
            return -1;
        }
        else
        {
            int i = getRedstoneLevel(blockState, 15);
            int j = redstoneColors.getColor(i);
            return j;
        }
    }

    public static void updateReddustFX(Particle fx, IBlockAccess blockAccess, double x, double y, double z)
    {
        if (redstoneColors != null)
        {
            IBlockState iblockstate = blockAccess.getBlockState(new BlockPos(x, y, z));
            int i = getRedstoneLevel(iblockstate, 15);
            int j = redstoneColors.getColor(i);
            int k = j >> 16 & 255;
            int l = j >> 8 & 255;
            int i1 = j & 255;
            float f = (float)k / 255.0F;
            float f1 = (float)l / 255.0F;
            float f2 = (float)i1 / 255.0F;
            fx.setRBGColorF(f, f1, f2);
        }
    }

    private static int getRedstoneLevel(IBlockState state, int def)
    {
        Block block = state.getBlock();

        if (!(block instanceof BlockRedstoneWire))
        {
            return def;
        }
        else
        {
            Object object = state.getValue(BlockRedstoneWire.POWER);

            if (!(object instanceof Integer))
            {
                return def;
            }
            else
            {
                Integer integer = (Integer)object;
                return integer.intValue();
            }
        }
    }

    public static float getXpOrbTimer(float timer)
    {
        if (xpOrbTime <= 0)
        {
            return timer;
        }
        else
        {
            float f = 628.0F / (float)xpOrbTime;
            return timer * f;
        }
    }

    public static int getXpOrbColor(float timer)
    {
        if (xpOrbColors == null)
        {
            return -1;
        }
        else
        {
            int i = (int)Math.round((double)((MathHelper.sin(timer) + 1.0F) * (float)(xpOrbColors.getLength() - 1)) / 2.0D);
            int j = xpOrbColors.getColor(i);
            return j;
        }
    }

    public static int getDurabilityColor(float dur, int color)
    {
        if (durabilityColors == null)
        {
            return color;
        }
        else
        {
            int i = (int)(dur * (float)durabilityColors.getLength());
            int j = durabilityColors.getColor(i);
            return j;
        }
    }

    public static void updateWaterFX(Particle fx, IBlockAccess blockAccess, double x, double y, double z, RenderEnv renderEnv)
    {
        if (waterColors != null || blockColormaps != null || particleWaterColor >= 0)
        {
            BlockPos blockpos = new BlockPos(x, y, z);
            renderEnv.reset(BLOCK_STATE_WATER, blockpos);
            int i = getFluidColor(blockAccess, BLOCK_STATE_WATER, blockpos, renderEnv);
            int j = i >> 16 & 255;
            int k = i >> 8 & 255;
            int l = i & 255;
            float f = (float)j / 255.0F;
            float f1 = (float)k / 255.0F;
            float f2 = (float)l / 255.0F;

            if (particleWaterColor >= 0)
            {
                int i1 = particleWaterColor >> 16 & 255;
                int j1 = particleWaterColor >> 8 & 255;
                int k1 = particleWaterColor & 255;
                f *= (float)i1 / 255.0F;
                f1 *= (float)j1 / 255.0F;
                f2 *= (float)k1 / 255.0F;
            }

            fx.setRBGColorF(f, f1, f2);
        }
    }

    private static int getLilypadColorMultiplier(IBlockAccess blockAccess, BlockPos blockPos)
    {
        return lilyPadColor < 0 ? getBlockColors().colorMultiplier(Blocks.WATERLILY.getDefaultState(), blockAccess, blockPos, 0) : lilyPadColor;
    }

    private static Vec3d getFogColorNether(Vec3d col)
    {
        return fogColorNether == null ? col : fogColorNether;
    }

    private static Vec3d getFogColorEnd(Vec3d col)
    {
        return fogColorEnd == null ? col : fogColorEnd;
    }

    private static Vec3d getSkyColorEnd(Vec3d col)
    {
        return skyColorEnd == null ? col : skyColorEnd;
    }

    public static Vec3d getSkyColor(Vec3d skyColor3d, IBlockAccess blockAccess, double x, double y, double z)
    {
        if (skyColors == null)
        {
            return skyColor3d;
        }
        else
        {
            int i = skyColors.getColorSmooth(blockAccess, x, y, z, 3);
            int j = i >> 16 & 255;
            int k = i >> 8 & 255;
            int l = i & 255;
            float f = (float)j / 255.0F;
            float f1 = (float)k / 255.0F;
            float f2 = (float)l / 255.0F;
            float f3 = (float)skyColor3d.x / 0.5F;
            float f4 = (float)skyColor3d.y / 0.66275F;
            float f5 = (float)skyColor3d.z;
            f = f * f3;
            f1 = f1 * f4;
            f2 = f2 * f5;
            Vec3d vec3d = skyColorFader.getColor((double)f, (double)f1, (double)f2);
            return vec3d;
        }
    }

    private static Vec3d getFogColor(Vec3d fogColor3d, IBlockAccess blockAccess, double x, double y, double z)
    {
        if (fogColors == null)
        {
            return fogColor3d;
        }
        else
        {
            int i = fogColors.getColorSmooth(blockAccess, x, y, z, 3);
            int j = i >> 16 & 255;
            int k = i >> 8 & 255;
            int l = i & 255;
            float f = (float)j / 255.0F;
            float f1 = (float)k / 255.0F;
            float f2 = (float)l / 255.0F;
            float f3 = (float)fogColor3d.x / 0.753F;
            float f4 = (float)fogColor3d.y / 0.8471F;
            float f5 = (float)fogColor3d.z;
            f = f * f3;
            f1 = f1 * f4;
            f2 = f2 * f5;
            Vec3d vec3d = fogColorFader.getColor((double)f, (double)f1, (double)f2);
            return vec3d;
        }
    }

    public static Vec3d getUnderwaterColor(IBlockAccess blockAccess, double x, double y, double z)
    {
        return getUnderFluidColor(blockAccess, x, y, z, underwaterColors, underwaterColorFader);
    }

    public static Vec3d getUnderlavaColor(IBlockAccess blockAccess, double x, double y, double z)
    {
        return getUnderFluidColor(blockAccess, x, y, z, underlavaColors, underlavaColorFader);
    }

    public static Vec3d getUnderFluidColor(IBlockAccess blockAccess, double x, double y, double z, CustomColormap underFluidColors, CustomColorFader underFluidColorFader)
    {
        if (underFluidColors == null)
        {
            return null;
        }
        else
        {
            int i = underFluidColors.getColorSmooth(blockAccess, x, y, z, 3);
            int j = i >> 16 & 255;
            int k = i >> 8 & 255;
            int l = i & 255;
            float f = (float)j / 255.0F;
            float f1 = (float)k / 255.0F;
            float f2 = (float)l / 255.0F;
            Vec3d vec3d = underFluidColorFader.getColor((double)f, (double)f1, (double)f2);
            return vec3d;
        }
    }

    private static int getStemColorMultiplier(Block blockStem, IBlockAccess blockAccess, BlockPos blockPos, RenderEnv renderEnv)
    {
        CustomColormap customcolormap = stemColors;

        if (blockStem == Blocks.PUMPKIN_STEM && stemPumpkinColors != null)
        {
            customcolormap = stemPumpkinColors;
        }

        if (blockStem == Blocks.MELON_STEM && stemMelonColors != null)
        {
            customcolormap = stemMelonColors;
        }

        if (customcolormap == null)
        {
            return -1;
        }
        else
        {
            int i = renderEnv.getMetadata();
            return customcolormap.getColor(i);
        }
    }

    public static boolean updateLightmap(World world, float torchFlickerX, int[] lmColors, boolean nightvision, float partialTicks)
    {
        if (world == null)
        {
            return false;
        }
        else if (lightMapPacks == null)
        {
            return false;
        }
        else
        {
            int i = world.provider.getDimensionType().getId();
            int j = i - lightmapMinDimensionId;

            if (j >= 0 && j < lightMapPacks.length)
            {
                LightMapPack lightmappack = lightMapPacks[j];
                return lightmappack == null ? false : lightmappack.updateLightmap(world, torchFlickerX, lmColors, nightvision, partialTicks);
            }
            else
            {
                return false;
            }
        }
    }

    public static Vec3d getWorldFogColor(Vec3d fogVec, World world, Entity renderViewEntity, float partialTicks)
    {
        DimensionType dimensiontype = world.provider.getDimensionType();
        Minecraft minecraft = Minecraft.getMinecraft();

        if (dimensiontype == DimensionType.NETHER)
        {
            return getFogColorNether(fogVec);
        }
        else if (dimensiontype == DimensionType.OVERWORLD)
        {
            return getFogColor(fogVec, minecraft.world, renderViewEntity.posX, renderViewEntity.posY + 1.0D, renderViewEntity.posZ);
        }
        else
        {
            return dimensiontype == DimensionType.THE_END ? getFogColorEnd(fogVec) : fogVec;
        }
    }

    public static Vec3d getWorldSkyColor(Vec3d skyVec, World world, Entity renderViewEntity, float partialTicks)
    {
        DimensionType dimensiontype = world.provider.getDimensionType();
        Minecraft minecraft = Minecraft.getMinecraft();

        if (dimensiontype == DimensionType.OVERWORLD)
        {
            return getSkyColor(skyVec, minecraft.world, renderViewEntity.posX, renderViewEntity.posY + 1.0D, renderViewEntity.posZ);
        }
        else
        {
            return dimensiontype == DimensionType.THE_END ? getSkyColorEnd(skyVec) : skyVec;
        }
    }

    private static int[] readSpawnEggColors(Properties props, String fileName, String prefix, String logName)
    {
        List<Integer> list = new ArrayList<Integer>();
        Set set = props.keySet();
        int i = 0;

        for (Object ss : set)
        {
            String s = (String) ss;
            String s1 = props.getProperty(s);

            if (s.startsWith(prefix))
            {
                String s2 = StrUtils.removePrefix(s, prefix);
                int j = EntityUtils.getEntityIdByName(s2);

                if (j < 0)
                {
                    j = EntityUtils.getEntityIdByLocation((new ResourceLocation(s2)).toString());
                }

                if (j < 0)
                {
                    warn("Invalid spawn egg name: " + s);
                }
                else
                {
                    int k = parseColor(s1);

                    if (k < 0)
                    {
                        warn("Invalid spawn egg color: " + s + " = " + s1);
                    }
                    else
                    {
                        while (list.size() <= j)
                        {
                            list.add(Integer.valueOf(-1));
                        }

                        list.set(j, Integer.valueOf(k));
                        ++i;
                    }
                }
            }
        }

        if (i <= 0)
        {
            return null;
        }
        else
        {
            dbg(logName + " colors: " + i);
            int[] aint = new int[list.size()];

            for (int l = 0; l < aint.length; ++l)
            {
                aint[l] = ((Integer)list.get(l)).intValue();
            }

            return aint;
        }
    }

    private static int getSpawnEggColor(ItemMonsterPlacer item, ItemStack itemStack, int layer, int color)
    {
        if (spawnEggPrimaryColors == null && spawnEggSecondaryColors == null)
        {
            return color;
        }
        else
        {
            NBTTagCompound nbttagcompound = itemStack.getTagCompound();

            if (nbttagcompound == null)
            {
                return color;
            }
            else
            {
                NBTTagCompound nbttagcompound1 = nbttagcompound.getCompoundTag("EntityTag");

                if (nbttagcompound1 == null)
                {
                    return color;
                }
                else
                {
                    String s = nbttagcompound1.getString("id");
                    int i = EntityUtils.getEntityIdByLocation(s);
                    int[] aint = layer == 0 ? spawnEggPrimaryColors : spawnEggSecondaryColors;

                    if (aint == null)
                    {
                        return color;
                    }
                    else if (i >= 0 && i < aint.length)
                    {
                        int j = aint[i];
                        return j < 0 ? color : j;
                    }
                    else
                    {
                        return color;
                    }
                }
            }
        }
    }

    public static int getColorFromItemStack(ItemStack itemStack, int layer, int color)
    {
        if (itemStack == null)
        {
            return color;
        }
        else
        {
            Item item = itemStack.getItem();

            if (item == null)
            {
                return color;
            }
            else
            {
                return item instanceof ItemMonsterPlacer ? getSpawnEggColor((ItemMonsterPlacer)item, itemStack, layer, color) : color;
            }
        }
    }

    private static float[][] readDyeColors(Properties props, String fileName, String prefix, String logName)
    {
        EnumDyeColor[] aenumdyecolor = EnumDyeColor.values();
        Map<String, EnumDyeColor> map = new HashMap<String, EnumDyeColor>();

        for (int i = 0; i < aenumdyecolor.length; ++i)
        {
            EnumDyeColor enumdyecolor = aenumdyecolor[i];
            map.put(enumdyecolor.getName(), enumdyecolor);
        }

        float[][] afloat1 = new float[aenumdyecolor.length][];
        int k = 0;

        for (Object ss : props.keySet())
        {
            String s = (String) ss;
            String s1 = props.getProperty(s);

            if (s.startsWith(prefix))
            {
                String s2 = StrUtils.removePrefix(s, prefix);

                if (s2.equals("lightBlue"))
                {
                    s2 = "light_blue";
                }

                EnumDyeColor enumdyecolor1 = map.get(s2);
                int j = parseColor(s1);

                if (enumdyecolor1 != null && j >= 0)
                {
                    float[] afloat = new float[] {(float)(j >> 16 & 255) / 255.0F, (float)(j >> 8 & 255) / 255.0F, (float)(j & 255) / 255.0F};
                    afloat1[enumdyecolor1.ordinal()] = afloat;
                    ++k;
                }
                else
                {
                    warn("Invalid color: " + s + " = " + s1);
                }
            }
        }

        if (k <= 0)
        {
            return (float[][])null;
        }
        else
        {
            dbg(logName + " colors: " + k);
            return afloat1;
        }
    }

    private static float[] getDyeColors(EnumDyeColor dye, float[][] dyeColors, float[] colors)
    {
        if (dyeColors == null)
        {
            return colors;
        }
        else if (dye == null)
        {
            return colors;
        }
        else
        {
            float[] afloat = dyeColors[dye.ordinal()];
            return afloat == null ? colors : afloat;
        }
    }

    public static float[] getWolfCollarColors(EnumDyeColor dye, float[] colors)
    {
        return getDyeColors(dye, wolfCollarColors, colors);
    }

    public static float[] getSheepColors(EnumDyeColor dye, float[] colors)
    {
        return getDyeColors(dye, sheepColors, colors);
    }

    private static int[] readTextColors(Properties props, String fileName, String prefix, String logName)
    {
        int[] aint = new int[32];
        Arrays.fill(aint, -1);
        int i = 0;

        for (Object ss : props.keySet())
        {
            String s = (String) ss;
            String s1 = props.getProperty(s);

            if (s.startsWith(prefix))
            {
                String s2 = StrUtils.removePrefix(s, prefix);
                int j = Config.parseInt(s2, -1);
                int k = parseColor(s1);

                if (j >= 0 && j < aint.length && k >= 0)
                {
                    aint[j] = k;
                    ++i;
                }
                else
                {
                    warn("Invalid color: " + s + " = " + s1);
                }
            }
        }

        if (i <= 0)
        {
            return null;
        }
        else
        {
            dbg(logName + " colors: " + i);
            return aint;
        }
    }

    public static int getTextColor(int index, int color)
    {
        if (textColors == null)
        {
            return color;
        }
        else if (index >= 0 && index < textColors.length)
        {
            int i = textColors[index];
            return i < 0 ? color : i;
        }
        else
        {
            return color;
        }
    }

    private static int[] readMapColors(Properties props, String fileName, String prefix, String logName)
    {
        int[] aint = new int[MapColor.COLORS.length];
        Arrays.fill(aint, -1);
        int i = 0;

        for (Object ss : props.keySet())
        {
            String s = (String) ss;
            String s1 = props.getProperty(s);

            if (s.startsWith(prefix))
            {
                String s2 = StrUtils.removePrefix(s, prefix);
                int j = getMapColorIndex(s2);
                int k = parseColor(s1);

                if (j >= 0 && j < aint.length && k >= 0)
                {
                    aint[j] = k;
                    ++i;
                }
                else
                {
                    warn("Invalid color: " + s + " = " + s1);
                }
            }
        }

        if (i <= 0)
        {
            return null;
        }
        else
        {
            dbg(logName + " colors: " + i);
            return aint;
        }
    }

    private static int[] readPotionColors(Properties props, String fileName, String prefix, String logName)
    {
        int[] aint = new int[getMaxPotionId()];
        Arrays.fill(aint, -1);
        int i = 0;

        for (Object ss : props.keySet())
        {
            String s = (String) ss;
            String s1 = props.getProperty(s);

            if (s.startsWith(prefix))
            {
                int j = getPotionId(s);
                int k = parseColor(s1);

                if (j >= 0 && j < aint.length && k >= 0)
                {
                    aint[j] = k;
                    ++i;
                }
                else
                {
                    warn("Invalid color: " + s + " = " + s1);
                }
            }
        }

        if (i <= 0)
        {
            return null;
        }
        else
        {
            dbg(logName + " colors: " + i);
            return aint;
        }
    }

    private static int getMaxPotionId()
    {
        int i = 0;

        for (ResourceLocation resourcelocation : Potion.REGISTRY.getKeys())
        {
            Potion potion = Potion.REGISTRY.getObject(resourcelocation);
            int j = Potion.getIdFromPotion(potion);

            if (j > i)
            {
                i = j;
            }
        }

        return i;
    }

    private static int getPotionId(String name)
    {
        if (name.equals("potion.water"))
        {
            return 0;
        }
        else
        {
            name = StrUtils.replacePrefix(name, "potion.", "effect.");

            for (ResourceLocation resourcelocation : Potion.REGISTRY.getKeys())
            {
                Potion potion = Potion.REGISTRY.getObject(resourcelocation);

                if (potion.getName().equals(name))
                {
                    return Potion.getIdFromPotion(potion);
                }
            }

            return -1;
        }
    }

    public static int getPotionColor(Potion potion, int color)
    {
        int i = 0;

        if (potion != null)
        {
            i = Potion.getIdFromPotion(potion);
        }

        return getPotionColor(i, color);
    }

    public static int getPotionColor(int potionId, int color)
    {
        if (potionColors == null)
        {
            return color;
        }
        else if (potionId >= 0 && potionId < potionColors.length)
        {
            int i = potionColors[potionId];
            return i < 0 ? color : i;
        }
        else
        {
            return color;
        }
    }

    private static int getMapColorIndex(String name)
    {
        if (name == null)
        {
            return -1;
        }
        else if (name.equals("air"))
        {
            return MapColor.AIR.colorIndex;
        }
        else if (name.equals("grass"))
        {
            return MapColor.GRASS.colorIndex;
        }
        else if (name.equals("sand"))
        {
            return MapColor.SAND.colorIndex;
        }
        else if (name.equals("cloth"))
        {
            return MapColor.CLOTH.colorIndex;
        }
        else if (name.equals("tnt"))
        {
            return MapColor.TNT.colorIndex;
        }
        else if (name.equals("ice"))
        {
            return MapColor.ICE.colorIndex;
        }
        else if (name.equals("iron"))
        {
            return MapColor.IRON.colorIndex;
        }
        else if (name.equals("foliage"))
        {
            return MapColor.FOLIAGE.colorIndex;
        }
        else if (name.equals("clay"))
        {
            return MapColor.CLAY.colorIndex;
        }
        else if (name.equals("dirt"))
        {
            return MapColor.DIRT.colorIndex;
        }
        else if (name.equals("stone"))
        {
            return MapColor.STONE.colorIndex;
        }
        else if (name.equals("water"))
        {
            return MapColor.WATER.colorIndex;
        }
        else if (name.equals("wood"))
        {
            return MapColor.WOOD.colorIndex;
        }
        else if (name.equals("quartz"))
        {
            return MapColor.QUARTZ.colorIndex;
        }
        else if (name.equals("gold"))
        {
            return MapColor.GOLD.colorIndex;
        }
        else if (name.equals("diamond"))
        {
            return MapColor.DIAMOND.colorIndex;
        }
        else if (name.equals("lapis"))
        {
            return MapColor.LAPIS.colorIndex;
        }
        else if (name.equals("emerald"))
        {
            return MapColor.EMERALD.colorIndex;
        }
        else if (name.equals("podzol"))
        {
            return MapColor.OBSIDIAN.colorIndex;
        }
        else if (name.equals("netherrack"))
        {
            return MapColor.NETHERRACK.colorIndex;
        }
        else if (!name.equals("snow") && !name.equals("white"))
        {
            if (!name.equals("adobe") && !name.equals("orange"))
            {
                if (name.equals("magenta"))
                {
                    return MapColor.MAGENTA.colorIndex;
                }
                else if (!name.equals("light_blue") && !name.equals("lightBlue"))
                {
                    if (name.equals("yellow"))
                    {
                        return MapColor.YELLOW.colorIndex;
                    }
                    else if (name.equals("lime"))
                    {
                        return MapColor.LIME.colorIndex;
                    }
                    else if (name.equals("pink"))
                    {
                        return MapColor.PINK.colorIndex;
                    }
                    else if (name.equals("gray"))
                    {
                        return MapColor.GRAY.colorIndex;
                    }
                    else if (name.equals("silver"))
                    {
                        return MapColor.SILVER.colorIndex;
                    }
                    else if (name.equals("cyan"))
                    {
                        return MapColor.CYAN.colorIndex;
                    }
                    else if (name.equals("purple"))
                    {
                        return MapColor.PURPLE.colorIndex;
                    }
                    else if (name.equals("blue"))
                    {
                        return MapColor.BLUE.colorIndex;
                    }
                    else if (name.equals("brown"))
                    {
                        return MapColor.BROWN.colorIndex;
                    }
                    else if (name.equals("green"))
                    {
                        return MapColor.GREEN.colorIndex;
                    }
                    else if (name.equals("red"))
                    {
                        return MapColor.RED.colorIndex;
                    }
                    else
                    {
                        return name.equals("black") ? MapColor.BLACK.colorIndex : -1;
                    }
                }
                else
                {
                    return MapColor.LIGHT_BLUE.colorIndex;
                }
            }
            else
            {
                return MapColor.ADOBE.colorIndex;
            }
        }
        else
        {
            return MapColor.SNOW.colorIndex;
        }
    }

    private static int[] getMapColors()
    {
        MapColor[] amapcolor = MapColor.COLORS;
        int[] aint = new int[amapcolor.length];
        Arrays.fill(aint, -1);

        for (int i = 0; i < amapcolor.length && i < aint.length; ++i)
        {
            MapColor mapcolor = amapcolor[i];

            if (mapcolor != null)
            {
                aint[i] = mapcolor.colorValue;
            }
        }

        return aint;
    }

    private static void setMapColors(int[] colors)
    {
        if (colors != null)
        {
            MapColor[] amapcolor = MapColor.COLORS;
            boolean flag = false;

            for (int i = 0; i < amapcolor.length && i < colors.length; ++i)
            {
                MapColor mapcolor = amapcolor[i];

                if (mapcolor != null)
                {
                    int j = colors[i];

                    if (j >= 0 && mapcolor.colorValue != j)
                    {
                        mapcolor.colorValue = j;
                        flag = true;
                    }
                }
            }

            if (flag)
            {
                Minecraft.getMinecraft().getTextureManager().reloadBannerTextures();
            }
        }
    }

    private static void dbg(String str)
    {
        Config.dbg("CustomColors: " + str);
    }

    private static void warn(String str)
    {
        Config.warn("CustomColors: " + str);
    }

    public static int getExpBarTextColor(int color)
    {
        return expBarTextColor < 0 ? color : expBarTextColor;
    }

    public static int getBossTextColor(int color)
    {
        return bossTextColor < 0 ? color : bossTextColor;
    }

    public static int getSignTextColor(int color)
    {
        return signTextColor < 0 ? color : signTextColor;
    }

    public interface IColorizer
    {
        int getColor(IBlockState var1, IBlockAccess var2, BlockPos var3);

        boolean isColorConstant();
    }
}
