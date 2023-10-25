/*
 * Decompiled with CFR 0.150.
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
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.ColorizerFoliage;
import net.minecraft.world.DimensionType;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraft.world.biome.Biome;
import optifine.BlockPosM;
import optifine.Config;
import optifine.ConnectedParser;
import optifine.CustomColorFader;
import optifine.CustomColormap;
import optifine.EntityUtils;
import optifine.MatchBlock;
import optifine.Reflector;
import optifine.RenderEnv;
import optifine.ResUtils;
import optifine.StrUtils;
import optifine.TextureUtils;
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
    private static CustomColormap[] lightMapsColorsRgb = null;
    private static int lightmapMinDimensionId = 0;
    private static float[][] sunRgbs = new float[16][3];
    private static float[][] torchRgbs = new float[16][3];
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
    private static float[][] wolfCollarColors = null;
    private static float[][] sheepColors = null;
    private static int[] textColors = null;
    private static int[] mapColorsOriginal = null;
    private static int[] potionColors = null;
    private static final IBlockState BLOCK_STATE_DIRT = Blocks.DIRT.getDefaultState();
    private static final IBlockState BLOCK_STATE_WATER = Blocks.WATER.getDefaultState();
    public static Random random = new Random();
    private static final IColorizer COLORIZER_GRASS = new IColorizer(){

        @Override
        public int getColor(IBlockState p_getColor_1_, IBlockAccess p_getColor_2_, BlockPos p_getColor_3_) {
            Biome biome = CustomColors.getColorBiome(p_getColor_2_, p_getColor_3_);
            return swampGrassColors != null && biome == Biomes.SWAMPLAND ? swampGrassColors.getColor(biome, p_getColor_3_) : biome.getGrassColorAtPos(p_getColor_3_);
        }

        @Override
        public boolean isColorConstant() {
            return false;
        }
    };
    private static final IColorizer COLORIZER_FOLIAGE = new IColorizer(){

        @Override
        public int getColor(IBlockState p_getColor_1_, IBlockAccess p_getColor_2_, BlockPos p_getColor_3_) {
            Biome biome = CustomColors.getColorBiome(p_getColor_2_, p_getColor_3_);
            return swampFoliageColors != null && biome == Biomes.SWAMPLAND ? swampFoliageColors.getColor(biome, p_getColor_3_) : biome.getFoliageColorAtPos(p_getColor_3_);
        }

        @Override
        public boolean isColorConstant() {
            return false;
        }
    };
    private static final IColorizer COLORIZER_FOLIAGE_PINE = new IColorizer(){

        @Override
        public int getColor(IBlockState p_getColor_1_, IBlockAccess p_getColor_2_, BlockPos p_getColor_3_) {
            return foliagePineColors != null ? foliagePineColors.getColor(p_getColor_2_, p_getColor_3_) : ColorizerFoliage.getFoliageColorPine();
        }

        @Override
        public boolean isColorConstant() {
            return foliagePineColors == null;
        }
    };
    private static final IColorizer COLORIZER_FOLIAGE_BIRCH = new IColorizer(){

        @Override
        public int getColor(IBlockState p_getColor_1_, IBlockAccess p_getColor_2_, BlockPos p_getColor_3_) {
            return foliageBirchColors != null ? foliageBirchColors.getColor(p_getColor_2_, p_getColor_3_) : ColorizerFoliage.getFoliageColorBirch();
        }

        @Override
        public boolean isColorConstant() {
            return foliageBirchColors == null;
        }
    };
    private static final IColorizer COLORIZER_WATER = new IColorizer(){

        @Override
        public int getColor(IBlockState p_getColor_1_, IBlockAccess p_getColor_2_, BlockPos p_getColor_3_) {
            Biome biome = CustomColors.getColorBiome(p_getColor_2_, p_getColor_3_);
            if (waterColors != null) {
                return waterColors.getColor(biome, p_getColor_3_);
            }
            return Reflector.ForgeBiome_getWaterColorMultiplier.exists() ? Reflector.callInt(biome, Reflector.ForgeBiome_getWaterColorMultiplier, new Object[0]) : biome.getWaterColor();
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
            Pair<CustomColormap[], Integer> pair = CustomColors.parseLightmapsRgb();
            lightMapsColorsRgb = pair.getLeft();
            lightmapMinDimensionId = pair.getRight();
            CustomColors.readColorProperties("mcpatcher/color.properties");
            blockColormaps = CustomColors.readBlockColormaps(new String[]{s + "custom/", s + "blocks/"}, colorsBlockColormaps, 256, 256);
            CustomColors.updateUseDefaultGrassFoliageColors();
        }
    }

    private static String getValidProperty(String p_getValidProperty_0_, String p_getValidProperty_1_, String[] p_getValidProperty_2_, String p_getValidProperty_3_) {
        try {
            ResourceLocation resourcelocation = new ResourceLocation(p_getValidProperty_0_);
            InputStream inputstream = Config.getResourceStream(resourcelocation);
            if (inputstream == null) {
                return p_getValidProperty_3_;
            }
            Properties properties = new Properties();
            properties.load(inputstream);
            inputstream.close();
            String s = properties.getProperty(p_getValidProperty_1_);
            if (s == null) {
                return p_getValidProperty_3_;
            }
            List<String> list = Arrays.asList(p_getValidProperty_2_);
            if (!list.contains(s)) {
                CustomColors.warn("Invalid value: " + p_getValidProperty_1_ + "=" + s);
                CustomColors.warn("Expected values: " + Config.arrayToString(p_getValidProperty_2_));
                return p_getValidProperty_3_;
            }
            CustomColors.dbg("" + p_getValidProperty_1_ + "=" + s);
            return s;
        }
        catch (FileNotFoundException var9) {
            return p_getValidProperty_3_;
        }
        catch (IOException ioexception) {
            ioexception.printStackTrace();
            return p_getValidProperty_3_;
        }
    }

    private static Pair<CustomColormap[], Integer> parseLightmapsRgb() {
        String s = "mcpatcher/lightmap/world";
        String s1 = ".png";
        String[] astring = ResUtils.collectFiles(s, s1);
        HashMap<Integer, String> map = new HashMap<Integer, String>();
        for (int i = 0; i < astring.length; ++i) {
            String s2 = astring[i];
            String s3 = StrUtils.removePrefixSuffix(s2, s, s1);
            int j = Config.parseInt(s3, Integer.MIN_VALUE);
            if (j == Integer.MIN_VALUE) {
                CustomColors.warn("Invalid dimension ID: " + s3 + ", path: " + s2);
                continue;
            }
            map.put(j, s2);
        }
        Set set = map.keySet();
        Integer[] ainteger = set.toArray(new Integer[set.size()]);
        Arrays.sort((Object[])ainteger);
        if (ainteger.length <= 0) {
            return new ImmutablePair<CustomColormap[], Integer>(null, 0);
        }
        int j1 = ainteger[0];
        int k1 = ainteger[ainteger.length - 1];
        int k = k1 - j1 + 1;
        CustomColormap[] acustomcolormap = new CustomColormap[k];
        for (int l = 0; l < ainteger.length; ++l) {
            Integer integer = ainteger[l];
            String s4 = (String)map.get(integer);
            CustomColormap customcolormap = CustomColors.getCustomColors(s4, -1, -1);
            if (customcolormap == null) continue;
            if (customcolormap.getWidth() < 16) {
                CustomColors.warn("Invalid lightmap width: " + customcolormap.getWidth() + ", path: " + s4);
                continue;
            }
            int i1 = integer - j1;
            acustomcolormap[i1] = customcolormap;
        }
        return new ImmutablePair<CustomColormap[], Integer>(acustomcolormap, j1);
    }

    private static int getTextureHeight(String p_getTextureHeight_0_, int p_getTextureHeight_1_) {
        try {
            InputStream inputstream = Config.getResourceStream(new ResourceLocation(p_getTextureHeight_0_));
            if (inputstream == null) {
                return p_getTextureHeight_1_;
            }
            BufferedImage bufferedimage = ImageIO.read(inputstream);
            inputstream.close();
            return bufferedimage == null ? p_getTextureHeight_1_ : bufferedimage.getHeight();
        }
        catch (IOException var4) {
            return p_getTextureHeight_1_;
        }
    }

    private static void readColorProperties(String p_readColorProperties_0_) {
        try {
            ResourceLocation resourcelocation = new ResourceLocation(p_readColorProperties_0_);
            InputStream inputstream = Config.getResourceStream(resourcelocation);
            if (inputstream == null) {
                return;
            }
            CustomColors.dbg("Loading " + p_readColorProperties_0_);
            Properties properties = new Properties();
            properties.load(inputstream);
            inputstream.close();
            particleWaterColor = CustomColors.readColor(properties, new String[]{"particle.water", "drop.water"});
            particlePortalColor = CustomColors.readColor(properties, "particle.portal");
            lilyPadColor = CustomColors.readColor(properties, "lilypad");
            expBarTextColor = CustomColors.readColor(properties, "text.xpbar");
            bossTextColor = CustomColors.readColor(properties, "text.boss");
            signTextColor = CustomColors.readColor(properties, "text.sign");
            fogColorNether = CustomColors.readColorVec3(properties, "fog.nether");
            fogColorEnd = CustomColors.readColorVec3(properties, "fog.end");
            skyColorEnd = CustomColors.readColorVec3(properties, "sky.end");
            colorsBlockColormaps = CustomColors.readCustomColormaps(properties, p_readColorProperties_0_);
            spawnEggPrimaryColors = CustomColors.readSpawnEggColors(properties, p_readColorProperties_0_, "egg.shell.", "Spawn egg shell");
            spawnEggSecondaryColors = CustomColors.readSpawnEggColors(properties, p_readColorProperties_0_, "egg.spots.", "Spawn egg spot");
            wolfCollarColors = CustomColors.readDyeColors(properties, p_readColorProperties_0_, "collar.", "Wolf collar");
            sheepColors = CustomColors.readDyeColors(properties, p_readColorProperties_0_, "sheep.", "Sheep");
            textColors = CustomColors.readTextColors(properties, p_readColorProperties_0_, "text.code.", "Text");
            int[] aint = CustomColors.readMapColors(properties, p_readColorProperties_0_, "map.", "Map");
            if (aint != null) {
                if (mapColorsOriginal == null) {
                    mapColorsOriginal = CustomColors.getMapColors();
                }
                CustomColors.setMapColors(aint);
            }
            potionColors = CustomColors.readPotionColors(properties, p_readColorProperties_0_, "potion.", "Potion");
            xpOrbTime = Config.parseInt(properties.getProperty("xporb.time"), -1);
        }
        catch (FileNotFoundException var5) {
            return;
        }
        catch (IOException ioexception) {
            ioexception.printStackTrace();
        }
    }

    private static CustomColormap[] readCustomColormaps(Properties p_readCustomColormaps_0_, String p_readCustomColormaps_1_) {
        ArrayList<CustomColormap> list = new ArrayList<CustomColormap>();
        String s = "palette.block.";
        HashMap<Object, String> map = new HashMap<Object, String>();
        for (Object s1 : p_readCustomColormaps_0_.keySet()) {
            String s2 = p_readCustomColormaps_0_.getProperty((String)s1);
            if (!((String)s1).startsWith(s)) continue;
            map.put(s1, s2);
        }
        String[] astring = map.keySet().toArray(new String[map.size()]);
        for (int j = 0; j < astring.length; ++j) {
            String s6 = astring[j];
            String s3 = p_readCustomColormaps_0_.getProperty(s6);
            CustomColors.dbg("Block palette: " + s6 + " = " + s3);
            String s4 = s6.substring(s.length());
            String s5 = TextureUtils.getBasePath(p_readCustomColormaps_1_);
            s4 = TextureUtils.fixResourcePath(s4, s5);
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
                list.add(customcolormap);
                continue;
            }
            CustomColors.warn("Invalid match blocks: " + s3);
        }
        if (list.size() <= 0) {
            return null;
        }
        CustomColormap[] acustomcolormap = list.toArray(new CustomColormap[list.size()]);
        return acustomcolormap;
    }

    private static CustomColormap[][] readBlockColormaps(String[] p_readBlockColormaps_0_, CustomColormap[] p_readBlockColormaps_1_, int p_readBlockColormaps_2_, int p_readBlockColormaps_3_) {
        String[] astring = ResUtils.collectFiles(p_readBlockColormaps_0_, new String[]{".properties"});
        Arrays.sort(astring);
        ArrayList list = new ArrayList();
        for (int i = 0; i < astring.length; ++i) {
            String s = astring[i];
            CustomColors.dbg("Block colormap: " + s);
            try {
                ResourceLocation resourcelocation = new ResourceLocation("minecraft", s);
                InputStream inputstream = Config.getResourceStream(resourcelocation);
                if (inputstream == null) {
                    CustomColors.warn("File not found: " + s);
                    continue;
                }
                Properties properties = new Properties();
                properties.load(inputstream);
                CustomColormap customcolormap = new CustomColormap(properties, s, p_readBlockColormaps_2_, p_readBlockColormaps_3_, paletteFormatDefault);
                if (!customcolormap.isValid(s) || !customcolormap.isValidMatchBlocks(s)) continue;
                CustomColors.addToBlockList(customcolormap, list);
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
        if (p_readBlockColormaps_1_ != null) {
            for (int j = 0; j < p_readBlockColormaps_1_.length; ++j) {
                CustomColormap customcolormap1 = p_readBlockColormaps_1_[j];
                CustomColors.addToBlockList(customcolormap1, list);
            }
        }
        if (list.size() <= 0) {
            return null;
        }
        CustomColormap[][] acustomcolormap = CustomColors.blockListToArray(list);
        return acustomcolormap;
    }

    private static void addToBlockList(CustomColormap p_addToBlockList_0_, List p_addToBlockList_1_) {
        int[] aint = p_addToBlockList_0_.getMatchBlockIds();
        if (aint != null && aint.length > 0) {
            for (int i = 0; i < aint.length; ++i) {
                int j = aint[i];
                if (j < 0) {
                    CustomColors.warn("Invalid block ID: " + j);
                    continue;
                }
                CustomColors.addToList(p_addToBlockList_0_, p_addToBlockList_1_, j);
            }
        } else {
            CustomColors.warn("No match blocks: " + Config.arrayToString(aint));
        }
    }

    private static void addToList(CustomColormap p_addToList_0_, List p_addToList_1_, int p_addToList_2_) {
        while (p_addToList_2_ >= p_addToList_1_.size()) {
            p_addToList_1_.add(null);
        }
        ArrayList<CustomColormap> list = (ArrayList<CustomColormap>)p_addToList_1_.get(p_addToList_2_);
        if (list == null) {
            list = new ArrayList<CustomColormap>();
            p_addToList_1_.set(p_addToList_2_, list);
        }
        list.add(p_addToList_0_);
    }

    private static CustomColormap[][] blockListToArray(List p_blockListToArray_0_) {
        CustomColormap[][] acustomcolormap = new CustomColormap[p_blockListToArray_0_.size()][];
        for (int i = 0; i < p_blockListToArray_0_.size(); ++i) {
            List list = (List)p_blockListToArray_0_.get(i);
            if (list == null) continue;
            CustomColormap[] acustomcolormap1 = list.toArray(new CustomColormap[list.size()]);
            acustomcolormap[i] = acustomcolormap1;
        }
        return acustomcolormap;
    }

    private static int readColor(Properties p_readColor_0_, String[] p_readColor_1_) {
        for (int i = 0; i < p_readColor_1_.length; ++i) {
            String s = p_readColor_1_[i];
            int j = CustomColors.readColor(p_readColor_0_, s);
            if (j < 0) continue;
            return j;
        }
        return -1;
    }

    private static int readColor(Properties p_readColor_0_, String p_readColor_1_) {
        String s = p_readColor_0_.getProperty(p_readColor_1_);
        if (s == null) {
            return -1;
        }
        int i = CustomColors.parseColor(s = s.trim());
        if (i < 0) {
            CustomColors.warn("Invalid color: " + p_readColor_1_ + " = " + s);
            return i;
        }
        CustomColors.dbg(p_readColor_1_ + " = " + s);
        return i;
    }

    private static int parseColor(String p_parseColor_0_) {
        if (p_parseColor_0_ == null) {
            return -1;
        }
        p_parseColor_0_ = p_parseColor_0_.trim();
        try {
            int i = Integer.parseInt(p_parseColor_0_, 16) & 0xFFFFFF;
            return i;
        }
        catch (NumberFormatException var2) {
            return -1;
        }
    }

    private static Vec3d readColorVec3(Properties p_readColorVec3_0_, String p_readColorVec3_1_) {
        int i = CustomColors.readColor(p_readColorVec3_0_, p_readColorVec3_1_);
        if (i < 0) {
            return null;
        }
        int j = i >> 16 & 0xFF;
        int k = i >> 8 & 0xFF;
        int l = i & 0xFF;
        float f = (float)j / 255.0f;
        float f1 = (float)k / 255.0f;
        float f2 = (float)l / 255.0f;
        return new Vec3d(f, f1, f2);
    }

    private static CustomColormap getCustomColors(String p_getCustomColors_0_, String[] p_getCustomColors_1_, int p_getCustomColors_2_, int p_getCustomColors_3_) {
        for (int i = 0; i < p_getCustomColors_1_.length; ++i) {
            String s = p_getCustomColors_1_[i];
            s = p_getCustomColors_0_ + s;
            CustomColormap customcolormap = CustomColors.getCustomColors(s, p_getCustomColors_2_, p_getCustomColors_3_);
            if (customcolormap == null) continue;
            return customcolormap;
        }
        return null;
    }

    public static CustomColormap getCustomColors(String p_getCustomColors_0_, int p_getCustomColors_1_, int p_getCustomColors_2_) {
        try {
            ResourceLocation resourcelocation = new ResourceLocation(p_getCustomColors_0_);
            if (!Config.hasResource(resourcelocation)) {
                return null;
            }
            CustomColors.dbg("Colormap " + p_getCustomColors_0_);
            Properties properties = new Properties();
            String s = StrUtils.replaceSuffix(p_getCustomColors_0_, ".png", ".properties");
            ResourceLocation resourcelocation1 = new ResourceLocation(s);
            if (Config.hasResource(resourcelocation1)) {
                InputStream inputstream = Config.getResourceStream(resourcelocation1);
                properties.load(inputstream);
                inputstream.close();
                CustomColors.dbg("Colormap properties: " + s);
            } else {
                properties.put("format", paletteFormatDefault);
                properties.put("source", p_getCustomColors_0_);
                s = p_getCustomColors_0_;
            }
            CustomColormap customcolormap = new CustomColormap(properties, s, p_getCustomColors_1_, p_getCustomColors_2_, paletteFormatDefault);
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

    public static int getColorMultiplier(BakedQuad p_getColorMultiplier_0_, IBlockState p_getColorMultiplier_1_, IBlockAccess p_getColorMultiplier_2_, BlockPos p_getColorMultiplier_3_, RenderEnv p_getColorMultiplier_4_) {
        IColorizer customcolors$icolorizer;
        Block block = p_getColorMultiplier_1_.getBlock();
        IBlockState iblockstate = p_getColorMultiplier_4_.getBlockState();
        if (blockColormaps != null) {
            CustomColormap customcolormap;
            if (!p_getColorMultiplier_0_.hasTintIndex()) {
                if (block == Blocks.GRASS) {
                    iblockstate = BLOCK_STATE_DIRT;
                }
                if (block == Blocks.REDSTONE_WIRE) {
                    return -1;
                }
            }
            if (block == Blocks.DOUBLE_PLANT && p_getColorMultiplier_4_.getMetadata() >= 8) {
                p_getColorMultiplier_3_ = p_getColorMultiplier_3_.down();
                iblockstate = p_getColorMultiplier_2_.getBlockState(p_getColorMultiplier_3_);
            }
            if ((customcolormap = CustomColors.getBlockColormap(iblockstate)) != null) {
                if (Config.isSmoothBiomes() && !customcolormap.isColorConstant()) {
                    return CustomColors.getSmoothColorMultiplier(p_getColorMultiplier_1_, p_getColorMultiplier_2_, p_getColorMultiplier_3_, customcolormap, p_getColorMultiplier_4_.getColorizerBlockPosM());
                }
                return customcolormap.getColor(p_getColorMultiplier_2_, p_getColorMultiplier_3_);
            }
        }
        if (!p_getColorMultiplier_0_.hasTintIndex()) {
            return -1;
        }
        if (block == Blocks.WATERLILY) {
            return CustomColors.getLilypadColorMultiplier(p_getColorMultiplier_2_, p_getColorMultiplier_3_);
        }
        if (block == Blocks.REDSTONE_WIRE) {
            return CustomColors.getRedstoneColor(p_getColorMultiplier_4_.getBlockState());
        }
        if (block instanceof BlockStem) {
            return CustomColors.getStemColorMultiplier(block, p_getColorMultiplier_2_, p_getColorMultiplier_3_, p_getColorMultiplier_4_);
        }
        if (useDefaultGrassFoliageColors) {
            return -1;
        }
        int i = p_getColorMultiplier_4_.getMetadata();
        if (block != Blocks.GRASS && block != Blocks.TALLGRASS && block != Blocks.DOUBLE_PLANT) {
            if (block == Blocks.DOUBLE_PLANT) {
                customcolors$icolorizer = COLORIZER_GRASS;
                if (i >= 8) {
                    p_getColorMultiplier_3_ = p_getColorMultiplier_3_.down();
                }
            } else if (block == Blocks.LEAVES) {
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
            } else if (block == Blocks.LEAVES2) {
                customcolors$icolorizer = COLORIZER_FOLIAGE;
            } else {
                if (block != Blocks.VINE) {
                    return -1;
                }
                customcolors$icolorizer = COLORIZER_FOLIAGE;
            }
        } else {
            customcolors$icolorizer = COLORIZER_GRASS;
        }
        return Config.isSmoothBiomes() && !customcolors$icolorizer.isColorConstant() ? CustomColors.getSmoothColorMultiplier(p_getColorMultiplier_1_, p_getColorMultiplier_2_, p_getColorMultiplier_3_, customcolors$icolorizer, p_getColorMultiplier_4_.getColorizerBlockPosM()) : customcolors$icolorizer.getColor(iblockstate, p_getColorMultiplier_2_, p_getColorMultiplier_3_);
    }

    protected static Biome getColorBiome(IBlockAccess p_getColorBiome_0_, BlockPos p_getColorBiome_1_) {
        Biome biome = p_getColorBiome_0_.getBiome(p_getColorBiome_1_);
        if (biome == Biomes.SWAMPLAND && !Config.isSwampColors()) {
            biome = Biomes.PLAINS;
        }
        return biome;
    }

    private static CustomColormap getBlockColormap(IBlockState p_getBlockColormap_0_) {
        if (blockColormaps == null) {
            return null;
        }
        if (!(p_getBlockColormap_0_ instanceof BlockStateBase)) {
            return null;
        }
        BlockStateBase blockstatebase = (BlockStateBase)p_getBlockColormap_0_;
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

    private static int getSmoothColorMultiplier(IBlockState p_getSmoothColorMultiplier_0_, IBlockAccess p_getSmoothColorMultiplier_1_, BlockPos p_getSmoothColorMultiplier_2_, IColorizer p_getSmoothColorMultiplier_3_, BlockPosM p_getSmoothColorMultiplier_4_) {
        int i = 0;
        int j = 0;
        int k = 0;
        int l = p_getSmoothColorMultiplier_2_.getX();
        int i1 = p_getSmoothColorMultiplier_2_.getY();
        int j1 = p_getSmoothColorMultiplier_2_.getZ();
        BlockPosM blockposm = p_getSmoothColorMultiplier_4_;
        for (int k1 = l - 1; k1 <= l + 1; ++k1) {
            for (int l1 = j1 - 1; l1 <= j1 + 1; ++l1) {
                blockposm.setXyz(k1, i1, l1);
                int i2 = p_getSmoothColorMultiplier_3_.getColor(p_getSmoothColorMultiplier_0_, p_getSmoothColorMultiplier_1_, blockposm);
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

    public static int getFluidColor(IBlockAccess p_getFluidColor_0_, IBlockState p_getFluidColor_1_, BlockPos p_getFluidColor_2_, RenderEnv p_getFluidColor_3_) {
        Block block = p_getFluidColor_1_.getBlock();
        IColorizer customcolors$icolorizer = CustomColors.getBlockColormap(p_getFluidColor_1_);
        if (customcolors$icolorizer == null && p_getFluidColor_1_.getMaterial() == Material.WATER) {
            customcolors$icolorizer = COLORIZER_WATER;
        }
        if (customcolors$icolorizer == null) {
            return CustomColors.getBlockColors().colorMultiplier(p_getFluidColor_1_, p_getFluidColor_0_, p_getFluidColor_2_, 0);
        }
        return Config.isSmoothBiomes() && !customcolors$icolorizer.isColorConstant() ? CustomColors.getSmoothColorMultiplier(p_getFluidColor_1_, p_getFluidColor_0_, p_getFluidColor_2_, customcolors$icolorizer, p_getFluidColor_3_.getColorizerBlockPosM()) : customcolors$icolorizer.getColor(p_getFluidColor_1_, p_getFluidColor_0_, p_getFluidColor_2_);
    }

    public static BlockColors getBlockColors() {
        return Minecraft.getMinecraft().getBlockColors();
    }

    public static void updatePortalFX(Particle p_updatePortalFX_0_) {
        if (particlePortalColor >= 0) {
            int i = particlePortalColor;
            int j = i >> 16 & 0xFF;
            int k = i >> 8 & 0xFF;
            int l = i & 0xFF;
            float f = (float)j / 255.0f;
            float f1 = (float)k / 255.0f;
            float f2 = (float)l / 255.0f;
            p_updatePortalFX_0_.setRBGColorF(f, f1, f2);
        }
    }

    public static void updateMyceliumFX(Particle p_updateMyceliumFX_0_) {
        if (myceliumParticleColors != null) {
            int i = myceliumParticleColors.getColorRandom();
            int j = i >> 16 & 0xFF;
            int k = i >> 8 & 0xFF;
            int l = i & 0xFF;
            float f = (float)j / 255.0f;
            float f1 = (float)k / 255.0f;
            float f2 = (float)l / 255.0f;
            p_updateMyceliumFX_0_.setRBGColorF(f, f1, f2);
        }
    }

    private static int getRedstoneColor(IBlockState p_getRedstoneColor_0_) {
        if (redstoneColors == null) {
            return -1;
        }
        int i = CustomColors.getRedstoneLevel(p_getRedstoneColor_0_, 15);
        int j = redstoneColors.getColor(i);
        return j;
    }

    public static void updateReddustFX(Particle p_updateReddustFX_0_, IBlockAccess p_updateReddustFX_1_, double p_updateReddustFX_2_, double p_updateReddustFX_4_, double p_updateReddustFX_6_) {
        if (redstoneColors != null) {
            IBlockState iblockstate = p_updateReddustFX_1_.getBlockState(new BlockPos(p_updateReddustFX_2_, p_updateReddustFX_4_, p_updateReddustFX_6_));
            int i = CustomColors.getRedstoneLevel(iblockstate, 15);
            int j = redstoneColors.getColor(i);
            int k = j >> 16 & 0xFF;
            int l = j >> 8 & 0xFF;
            int i1 = j & 0xFF;
            float f = (float)k / 255.0f;
            float f1 = (float)l / 255.0f;
            float f2 = (float)i1 / 255.0f;
            p_updateReddustFX_0_.setRBGColorF(f, f1, f2);
        }
    }

    private static int getRedstoneLevel(IBlockState p_getRedstoneLevel_0_, int p_getRedstoneLevel_1_) {
        Block block = p_getRedstoneLevel_0_.getBlock();
        if (!(block instanceof BlockRedstoneWire)) {
            return p_getRedstoneLevel_1_;
        }
        Integer object = p_getRedstoneLevel_0_.getValue(BlockRedstoneWire.POWER);
        if (!(object instanceof Integer)) {
            return p_getRedstoneLevel_1_;
        }
        Integer integer = object;
        return integer;
    }

    public static float getXpOrbTimer(float p_getXpOrbTimer_0_) {
        if (xpOrbTime <= 0) {
            return p_getXpOrbTimer_0_;
        }
        float f = 628.0f / (float)xpOrbTime;
        return p_getXpOrbTimer_0_ * f;
    }

    public static int getXpOrbColor(float p_getXpOrbColor_0_) {
        if (xpOrbColors == null) {
            return -1;
        }
        int i = (int)Math.round((double)((MathHelper.sin(p_getXpOrbColor_0_) + 1.0f) * (float)(xpOrbColors.getLength() - 1)) / 2.0);
        int j = xpOrbColors.getColor(i);
        return j;
    }

    public static int getDurabilityColor(float p_getDurabilityColor_0_, int p_getDurabilityColor_1_) {
        if (durabilityColors == null) {
            return p_getDurabilityColor_1_;
        }
        int i = (int)(p_getDurabilityColor_0_ * (float)durabilityColors.getLength());
        int j = durabilityColors.getColor(i);
        return j;
    }

    public static void updateWaterFX(Particle p_updateWaterFX_0_, IBlockAccess p_updateWaterFX_1_, double p_updateWaterFX_2_, double p_updateWaterFX_4_, double p_updateWaterFX_6_, RenderEnv p_updateWaterFX_8_) {
        if (waterColors != null || blockColormaps != null) {
            BlockPos blockpos = new BlockPos(p_updateWaterFX_2_, p_updateWaterFX_4_, p_updateWaterFX_6_);
            p_updateWaterFX_8_.reset(p_updateWaterFX_1_, BLOCK_STATE_WATER, blockpos);
            int i = CustomColors.getFluidColor(p_updateWaterFX_1_, BLOCK_STATE_WATER, blockpos, p_updateWaterFX_8_);
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
            p_updateWaterFX_0_.setRBGColorF(f, f1, f2);
        }
    }

    private static int getLilypadColorMultiplier(IBlockAccess p_getLilypadColorMultiplier_0_, BlockPos p_getLilypadColorMultiplier_1_) {
        return lilyPadColor < 0 ? CustomColors.getBlockColors().colorMultiplier(Blocks.WATERLILY.getDefaultState(), p_getLilypadColorMultiplier_0_, p_getLilypadColorMultiplier_1_, 0) : lilyPadColor;
    }

    private static Vec3d getFogColorNether(Vec3d p_getFogColorNether_0_) {
        return fogColorNether == null ? p_getFogColorNether_0_ : fogColorNether;
    }

    private static Vec3d getFogColorEnd(Vec3d p_getFogColorEnd_0_) {
        return fogColorEnd == null ? p_getFogColorEnd_0_ : fogColorEnd;
    }

    private static Vec3d getSkyColorEnd(Vec3d p_getSkyColorEnd_0_) {
        return skyColorEnd == null ? p_getSkyColorEnd_0_ : skyColorEnd;
    }

    public static Vec3d getSkyColor(Vec3d p_getSkyColor_0_, IBlockAccess p_getSkyColor_1_, double p_getSkyColor_2_, double p_getSkyColor_4_, double p_getSkyColor_6_) {
        if (skyColors == null) {
            return p_getSkyColor_0_;
        }
        int i = skyColors.getColorSmooth(p_getSkyColor_1_, p_getSkyColor_2_, p_getSkyColor_4_, p_getSkyColor_6_, 3);
        int j = i >> 16 & 0xFF;
        int k = i >> 8 & 0xFF;
        int l = i & 0xFF;
        float f = (float)j / 255.0f;
        float f1 = (float)k / 255.0f;
        float f2 = (float)l / 255.0f;
        float f3 = (float)p_getSkyColor_0_.x / 0.5f;
        float f4 = (float)p_getSkyColor_0_.y / 0.66275f;
        float f5 = (float)p_getSkyColor_0_.z;
        Vec3d vec3d = skyColorFader.getColor(f *= f3, f1 *= f4, f2 *= f5);
        return vec3d;
    }

    private static Vec3d getFogColor(Vec3d p_getFogColor_0_, IBlockAccess p_getFogColor_1_, double p_getFogColor_2_, double p_getFogColor_4_, double p_getFogColor_6_) {
        if (fogColors == null) {
            return p_getFogColor_0_;
        }
        int i = fogColors.getColorSmooth(p_getFogColor_1_, p_getFogColor_2_, p_getFogColor_4_, p_getFogColor_6_, 3);
        int j = i >> 16 & 0xFF;
        int k = i >> 8 & 0xFF;
        int l = i & 0xFF;
        float f = (float)j / 255.0f;
        float f1 = (float)k / 255.0f;
        float f2 = (float)l / 255.0f;
        float f3 = (float)p_getFogColor_0_.x / 0.753f;
        float f4 = (float)p_getFogColor_0_.y / 0.8471f;
        float f5 = (float)p_getFogColor_0_.z;
        Vec3d vec3d = fogColorFader.getColor(f *= f3, f1 *= f4, f2 *= f5);
        return vec3d;
    }

    public static Vec3d getUnderwaterColor(IBlockAccess p_getUnderwaterColor_0_, double p_getUnderwaterColor_1_, double p_getUnderwaterColor_3_, double p_getUnderwaterColor_5_) {
        return CustomColors.getUnderFluidColor(p_getUnderwaterColor_0_, p_getUnderwaterColor_1_, p_getUnderwaterColor_3_, p_getUnderwaterColor_5_, underwaterColors, underwaterColorFader);
    }

    public static Vec3d getUnderlavaColor(IBlockAccess p_getUnderlavaColor_0_, double p_getUnderlavaColor_1_, double p_getUnderlavaColor_3_, double p_getUnderlavaColor_5_) {
        return CustomColors.getUnderFluidColor(p_getUnderlavaColor_0_, p_getUnderlavaColor_1_, p_getUnderlavaColor_3_, p_getUnderlavaColor_5_, underlavaColors, underlavaColorFader);
    }

    public static Vec3d getUnderFluidColor(IBlockAccess p_getUnderFluidColor_0_, double p_getUnderFluidColor_1_, double p_getUnderFluidColor_3_, double p_getUnderFluidColor_5_, CustomColormap p_getUnderFluidColor_7_, CustomColorFader p_getUnderFluidColor_8_) {
        if (p_getUnderFluidColor_7_ == null) {
            return null;
        }
        int i = p_getUnderFluidColor_7_.getColorSmooth(p_getUnderFluidColor_0_, p_getUnderFluidColor_1_, p_getUnderFluidColor_3_, p_getUnderFluidColor_5_, 3);
        int j = i >> 16 & 0xFF;
        int k = i >> 8 & 0xFF;
        int l = i & 0xFF;
        float f = (float)j / 255.0f;
        float f1 = (float)k / 255.0f;
        float f2 = (float)l / 255.0f;
        Vec3d vec3d = p_getUnderFluidColor_8_.getColor(f, f1, f2);
        return vec3d;
    }

    private static int getStemColorMultiplier(Block p_getStemColorMultiplier_0_, IBlockAccess p_getStemColorMultiplier_1_, BlockPos p_getStemColorMultiplier_2_, RenderEnv p_getStemColorMultiplier_3_) {
        CustomColormap customcolormap = stemColors;
        if (p_getStemColorMultiplier_0_ == Blocks.PUMPKIN_STEM && stemPumpkinColors != null) {
            customcolormap = stemPumpkinColors;
        }
        if (p_getStemColorMultiplier_0_ == Blocks.MELON_STEM && stemMelonColors != null) {
            customcolormap = stemMelonColors;
        }
        if (customcolormap == null) {
            return -1;
        }
        int i = p_getStemColorMultiplier_3_.getMetadata();
        return customcolormap.getColor(i);
    }

    public static boolean updateLightmap(World p_updateLightmap_0_, float p_updateLightmap_1_, int[] p_updateLightmap_2_, boolean p_updateLightmap_3_) {
        if (p_updateLightmap_0_ == null) {
            return false;
        }
        if (lightMapsColorsRgb == null) {
            return false;
        }
        int i = p_updateLightmap_0_.provider.getDimensionType().getId();
        int j = i - lightmapMinDimensionId;
        if (j >= 0 && j < lightMapsColorsRgb.length) {
            CustomColormap customcolormap = lightMapsColorsRgb[j];
            if (customcolormap == null) {
                return false;
            }
            int k = customcolormap.getHeight();
            if (p_updateLightmap_3_ && k < 64) {
                return false;
            }
            int l = customcolormap.getWidth();
            if (l < 16) {
                CustomColors.warn("Invalid lightmap width: " + l + " for dimension: " + i);
                CustomColors.lightMapsColorsRgb[j] = null;
                return false;
            }
            int i1 = 0;
            if (p_updateLightmap_3_) {
                i1 = l * 16 * 2;
            }
            float f = 1.1666666f * (p_updateLightmap_0_.getSunBrightness(1.0f) - 0.2f);
            if (p_updateLightmap_0_.getLastLightningBolt() > 0) {
                f = 1.0f;
            }
            f = Config.limitTo1(f);
            float f1 = f * (float)(l - 1);
            float f2 = Config.limitTo1(p_updateLightmap_1_ + 0.5f) * (float)(l - 1);
            float f3 = Config.limitTo1(Config.getGameSettings().gammaSetting);
            boolean flag = f3 > 1.0E-4f;
            float[][] afloat = customcolormap.getColorsRgb();
            CustomColors.getLightMapColumn(afloat, f1, i1, l, sunRgbs);
            CustomColors.getLightMapColumn(afloat, f2, i1 + 16 * l, l, torchRgbs);
            float[] afloat1 = new float[3];
            for (int j1 = 0; j1 < 16; ++j1) {
                for (int k1 = 0; k1 < 16; ++k1) {
                    for (int l1 = 0; l1 < 3; ++l1) {
                        float f4 = Config.limitTo1(sunRgbs[j1][l1] + torchRgbs[k1][l1]);
                        if (flag) {
                            float f5 = 1.0f - f4;
                            f5 = 1.0f - f5 * f5 * f5 * f5;
                            f4 = f3 * f5 + (1.0f - f3) * f4;
                        }
                        afloat1[l1] = f4;
                    }
                    int i2 = (int)(afloat1[0] * 255.0f);
                    int j2 = (int)(afloat1[1] * 255.0f);
                    int k2 = (int)(afloat1[2] * 255.0f);
                    p_updateLightmap_2_[j1 * 16 + k1] = 0xFF000000 | i2 << 16 | j2 << 8 | k2;
                }
            }
            return true;
        }
        return false;
    }

    private static void getLightMapColumn(float[][] p_getLightMapColumn_0_, float p_getLightMapColumn_1_, int p_getLightMapColumn_2_, int p_getLightMapColumn_3_, float[][] p_getLightMapColumn_4_) {
        int j;
        int i = (int)Math.floor(p_getLightMapColumn_1_);
        if (i == (j = (int)Math.ceil(p_getLightMapColumn_1_))) {
            for (int i1 = 0; i1 < 16; ++i1) {
                float[] afloat3 = p_getLightMapColumn_0_[p_getLightMapColumn_2_ + i1 * p_getLightMapColumn_3_ + i];
                float[] afloat4 = p_getLightMapColumn_4_[i1];
                for (int j1 = 0; j1 < 3; ++j1) {
                    afloat4[j1] = afloat3[j1];
                }
            }
        } else {
            float f = 1.0f - (p_getLightMapColumn_1_ - (float)i);
            float f1 = 1.0f - ((float)j - p_getLightMapColumn_1_);
            for (int k = 0; k < 16; ++k) {
                float[] afloat = p_getLightMapColumn_0_[p_getLightMapColumn_2_ + k * p_getLightMapColumn_3_ + i];
                float[] afloat1 = p_getLightMapColumn_0_[p_getLightMapColumn_2_ + k * p_getLightMapColumn_3_ + j];
                float[] afloat2 = p_getLightMapColumn_4_[k];
                for (int l = 0; l < 3; ++l) {
                    afloat2[l] = afloat[l] * f + afloat1[l] * f1;
                }
            }
        }
    }

    public static Vec3d getWorldFogColor(Vec3d p_getWorldFogColor_0_, World p_getWorldFogColor_1_, Entity p_getWorldFogColor_2_, float p_getWorldFogColor_3_) {
        DimensionType dimensiontype = p_getWorldFogColor_1_.provider.getDimensionType();
        switch (dimensiontype) {
            case NETHER: {
                p_getWorldFogColor_0_ = CustomColors.getFogColorNether(p_getWorldFogColor_0_);
                break;
            }
            case OVERWORLD: {
                Minecraft minecraft = Minecraft.getMinecraft();
                p_getWorldFogColor_0_ = CustomColors.getFogColor(p_getWorldFogColor_0_, minecraft.world, p_getWorldFogColor_2_.posX, p_getWorldFogColor_2_.posY + 1.0, p_getWorldFogColor_2_.posZ);
                break;
            }
            case THE_END: {
                p_getWorldFogColor_0_ = CustomColors.getFogColorEnd(p_getWorldFogColor_0_);
            }
        }
        return p_getWorldFogColor_0_;
    }

    public static Vec3d getWorldSkyColor(Vec3d p_getWorldSkyColor_0_, World p_getWorldSkyColor_1_, Entity p_getWorldSkyColor_2_, float p_getWorldSkyColor_3_) {
        DimensionType dimensiontype = p_getWorldSkyColor_1_.provider.getDimensionType();
        switch (dimensiontype) {
            case OVERWORLD: {
                Minecraft minecraft = Minecraft.getMinecraft();
                p_getWorldSkyColor_0_ = CustomColors.getSkyColor(p_getWorldSkyColor_0_, minecraft.world, p_getWorldSkyColor_2_.posX, p_getWorldSkyColor_2_.posY + 1.0, p_getWorldSkyColor_2_.posZ);
                break;
            }
            case THE_END: {
                p_getWorldSkyColor_0_ = CustomColors.getSkyColorEnd(p_getWorldSkyColor_0_);
            }
        }
        return p_getWorldSkyColor_0_;
    }

    private static int[] readSpawnEggColors(Properties p_readSpawnEggColors_0_, String p_readSpawnEggColors_1_, String p_readSpawnEggColors_2_, String p_readSpawnEggColors_3_) {
        ArrayList<Integer> list = new ArrayList<Integer>();
        Set<Object> set = p_readSpawnEggColors_0_.keySet();
        int i = 0;
        for (Object s : set) {
            String s1 = p_readSpawnEggColors_0_.getProperty((String)s);
            if (!((String)s).startsWith(p_readSpawnEggColors_2_)) continue;
            String s2 = StrUtils.removePrefix((String)s, p_readSpawnEggColors_2_);
            int j = EntityUtils.getEntityIdByName(s2);
            if (j < 0) {
                j = EntityUtils.getEntityIdByLocation(new ResourceLocation(s2).toString());
            }
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
                list.add(-1);
            }
            list.set(j, k);
            ++i;
        }
        if (i <= 0) {
            return null;
        }
        CustomColors.dbg(p_readSpawnEggColors_3_ + " colors: " + i);
        int[] aint = new int[list.size()];
        for (int l = 0; l < aint.length; ++l) {
            aint[l] = (Integer)list.get(l);
        }
        return aint;
    }

    private static int getSpawnEggColor(ItemMonsterPlacer p_getSpawnEggColor_0_, ItemStack p_getSpawnEggColor_1_, int p_getSpawnEggColor_2_, int p_getSpawnEggColor_3_) {
        int[] aint;
        if (spawnEggPrimaryColors == null && spawnEggSecondaryColors == null) {
            return p_getSpawnEggColor_3_;
        }
        NBTTagCompound nbttagcompound = p_getSpawnEggColor_1_.getTagCompound();
        if (nbttagcompound == null) {
            return p_getSpawnEggColor_3_;
        }
        NBTTagCompound nbttagcompound1 = nbttagcompound.getCompoundTag("EntityTag");
        if (nbttagcompound1 == null) {
            return p_getSpawnEggColor_3_;
        }
        String s = nbttagcompound1.getString("id");
        int i = EntityUtils.getEntityIdByLocation(s);
        int[] arrn = aint = p_getSpawnEggColor_2_ == 0 ? spawnEggPrimaryColors : spawnEggSecondaryColors;
        if (aint == null) {
            return p_getSpawnEggColor_3_;
        }
        if (i >= 0 && i < aint.length) {
            int j = aint[i];
            return j < 0 ? p_getSpawnEggColor_3_ : j;
        }
        return p_getSpawnEggColor_3_;
    }

    public static int getColorFromItemStack(ItemStack p_getColorFromItemStack_0_, int p_getColorFromItemStack_1_, int p_getColorFromItemStack_2_) {
        if (p_getColorFromItemStack_0_ == null) {
            return p_getColorFromItemStack_2_;
        }
        Item item = p_getColorFromItemStack_0_.getItem();
        if (item == null) {
            return p_getColorFromItemStack_2_;
        }
        return item instanceof ItemMonsterPlacer ? CustomColors.getSpawnEggColor((ItemMonsterPlacer)item, p_getColorFromItemStack_0_, p_getColorFromItemStack_1_, p_getColorFromItemStack_2_) : p_getColorFromItemStack_2_;
    }

    private static float[][] readDyeColors(Properties p_readDyeColors_0_, String p_readDyeColors_1_, String p_readDyeColors_2_, String p_readDyeColors_3_) {
        EnumDyeColor[] aenumdyecolor = EnumDyeColor.values();
        HashMap<String, EnumDyeColor> map = new HashMap<String, EnumDyeColor>();
        for (int i = 0; i < aenumdyecolor.length; ++i) {
            EnumDyeColor enumdyecolor = aenumdyecolor[i];
            map.put(enumdyecolor.getName(), enumdyecolor);
        }
        float[][] afloat1 = new float[aenumdyecolor.length][];
        int k = 0;
        for (Object s : p_readDyeColors_0_.keySet()) {
            String s1 = p_readDyeColors_0_.getProperty((String)s);
            if (!((String)s).startsWith(p_readDyeColors_2_)) continue;
            String s2 = StrUtils.removePrefix((String)s, p_readDyeColors_2_);
            if (s2.equals("lightBlue")) {
                s2 = "light_blue";
            }
            EnumDyeColor enumdyecolor1 = (EnumDyeColor)map.get(s2);
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
        CustomColors.dbg(p_readDyeColors_3_ + " colors: " + k);
        return afloat1;
    }

    private static float[] getDyeColors(EnumDyeColor p_getDyeColors_0_, float[][] p_getDyeColors_1_, float[] p_getDyeColors_2_) {
        if (p_getDyeColors_1_ == null) {
            return p_getDyeColors_2_;
        }
        if (p_getDyeColors_0_ == null) {
            return p_getDyeColors_2_;
        }
        float[] afloat = p_getDyeColors_1_[p_getDyeColors_0_.ordinal()];
        return afloat == null ? p_getDyeColors_2_ : afloat;
    }

    public static float[] getWolfCollarColors(EnumDyeColor p_getWolfCollarColors_0_, float[] p_getWolfCollarColors_1_) {
        return CustomColors.getDyeColors(p_getWolfCollarColors_0_, wolfCollarColors, p_getWolfCollarColors_1_);
    }

    public static float[] getSheepColors(EnumDyeColor p_getSheepColors_0_, float[] p_getSheepColors_1_) {
        return CustomColors.getDyeColors(p_getSheepColors_0_, sheepColors, p_getSheepColors_1_);
    }

    private static int[] readTextColors(Properties p_readTextColors_0_, String p_readTextColors_1_, String p_readTextColors_2_, String p_readTextColors_3_) {
        int[] aint = new int[32];
        Arrays.fill(aint, -1);
        int i = 0;
        for (Object s : p_readTextColors_0_.keySet()) {
            String s1 = p_readTextColors_0_.getProperty((String)s);
            if (!((String)s).startsWith(p_readTextColors_2_)) continue;
            String s2 = StrUtils.removePrefix((String)s, p_readTextColors_2_);
            int j = Config.parseInt(s2, -1);
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
        CustomColors.dbg(p_readTextColors_3_ + " colors: " + i);
        return aint;
    }

    public static int getTextColor(int p_getTextColor_0_, int p_getTextColor_1_) {
        if (textColors == null) {
            return p_getTextColor_1_;
        }
        if (p_getTextColor_0_ >= 0 && p_getTextColor_0_ < textColors.length) {
            int i = textColors[p_getTextColor_0_];
            return i < 0 ? p_getTextColor_1_ : i;
        }
        return p_getTextColor_1_;
    }

    private static int[] readMapColors(Properties p_readMapColors_0_, String p_readMapColors_1_, String p_readMapColors_2_, String p_readMapColors_3_) {
        int[] aint = new int[MapColor.COLORS.length];
        Arrays.fill(aint, -1);
        int i = 0;
        for (Object s : p_readMapColors_0_.keySet()) {
            String s1 = p_readMapColors_0_.getProperty((String)s);
            if (!((String)s).startsWith(p_readMapColors_2_)) continue;
            String s2 = StrUtils.removePrefix((String)s, p_readMapColors_2_);
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
        CustomColors.dbg(p_readMapColors_3_ + " colors: " + i);
        return aint;
    }

    private static int[] readPotionColors(Properties p_readPotionColors_0_, String p_readPotionColors_1_, String p_readPotionColors_2_, String p_readPotionColors_3_) {
        int[] aint = new int[CustomColors.getMaxPotionId()];
        Arrays.fill(aint, -1);
        int i = 0;
        for (Object s : p_readPotionColors_0_.keySet()) {
            String s1 = p_readPotionColors_0_.getProperty((String)s);
            if (!((String)s).startsWith(p_readPotionColors_2_)) continue;
            int j = CustomColors.getPotionId((String)s);
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
        CustomColors.dbg(p_readPotionColors_3_ + " colors: " + i);
        return aint;
    }

    private static int getMaxPotionId() {
        int i = 0;
        for (ResourceLocation resourcelocation : Potion.REGISTRY.getKeys()) {
            Potion potion = Potion.REGISTRY.getObject(resourcelocation);
            int j = Potion.getIdFromPotion(potion);
            if (j <= i) continue;
            i = j;
        }
        return i;
    }

    public static int getPotionId(String p_getPotionId_0_) {
        if (p_getPotionId_0_.equals("potion.water")) {
            return 0;
        }
        p_getPotionId_0_ = StrUtils.replacePrefix(p_getPotionId_0_, "potion.", "effect.");
        for (ResourceLocation resourcelocation : Potion.REGISTRY.getKeys()) {
            Potion potion = Potion.REGISTRY.getObject(resourcelocation);
            if (!potion.getName().equals(p_getPotionId_0_)) continue;
            return Potion.getIdFromPotion(potion);
        }
        return -1;
    }

    public static int getPotionColor(Potion p_getPotionColor_0_, int p_getPotionColor_1_) {
        int i = 0;
        if (p_getPotionColor_0_ != null) {
            i = Potion.getIdFromPotion(p_getPotionColor_0_);
        }
        return CustomColors.getPotionColor(i, p_getPotionColor_1_);
    }

    public static int getPotionColor(int p_getPotionColor_0_, int p_getPotionColor_1_) {
        if (potionColors == null) {
            return p_getPotionColor_1_;
        }
        if (p_getPotionColor_0_ >= 0 && p_getPotionColor_0_ < potionColors.length) {
            int i = potionColors[p_getPotionColor_0_];
            return i < 0 ? p_getPotionColor_1_ : i;
        }
        return p_getPotionColor_1_;
    }

    private static int getMapColorIndex(String p_getMapColorIndex_0_) {
        if (p_getMapColorIndex_0_ == null) {
            return -1;
        }
        if (p_getMapColorIndex_0_.equals("air")) {
            return MapColor.AIR.colorIndex;
        }
        if (p_getMapColorIndex_0_.equals("grass")) {
            return MapColor.GRASS.colorIndex;
        }
        if (p_getMapColorIndex_0_.equals("sand")) {
            return MapColor.SAND.colorIndex;
        }
        if (p_getMapColorIndex_0_.equals("cloth")) {
            return MapColor.CLOTH.colorIndex;
        }
        if (p_getMapColorIndex_0_.equals("tnt")) {
            return MapColor.TNT.colorIndex;
        }
        if (p_getMapColorIndex_0_.equals("ice")) {
            return MapColor.ICE.colorIndex;
        }
        if (p_getMapColorIndex_0_.equals("iron")) {
            return MapColor.IRON.colorIndex;
        }
        if (p_getMapColorIndex_0_.equals("foliage")) {
            return MapColor.FOLIAGE.colorIndex;
        }
        if (p_getMapColorIndex_0_.equals("clay")) {
            return MapColor.CLAY.colorIndex;
        }
        if (p_getMapColorIndex_0_.equals("dirt")) {
            return MapColor.DIRT.colorIndex;
        }
        if (p_getMapColorIndex_0_.equals("stone")) {
            return MapColor.STONE.colorIndex;
        }
        if (p_getMapColorIndex_0_.equals("water")) {
            return MapColor.WATER.colorIndex;
        }
        if (p_getMapColorIndex_0_.equals("wood")) {
            return MapColor.WOOD.colorIndex;
        }
        if (p_getMapColorIndex_0_.equals("quartz")) {
            return MapColor.QUARTZ.colorIndex;
        }
        if (p_getMapColorIndex_0_.equals("gold")) {
            return MapColor.GOLD.colorIndex;
        }
        if (p_getMapColorIndex_0_.equals("diamond")) {
            return MapColor.DIAMOND.colorIndex;
        }
        if (p_getMapColorIndex_0_.equals("lapis")) {
            return MapColor.LAPIS.colorIndex;
        }
        if (p_getMapColorIndex_0_.equals("emerald")) {
            return MapColor.EMERALD.colorIndex;
        }
        if (p_getMapColorIndex_0_.equals("podzol")) {
            return MapColor.OBSIDIAN.colorIndex;
        }
        if (p_getMapColorIndex_0_.equals("netherrack")) {
            return MapColor.NETHERRACK.colorIndex;
        }
        if (!p_getMapColorIndex_0_.equals("snow") && !p_getMapColorIndex_0_.equals("white")) {
            if (!p_getMapColorIndex_0_.equals("adobe") && !p_getMapColorIndex_0_.equals("orange")) {
                if (p_getMapColorIndex_0_.equals("magenta")) {
                    return MapColor.MAGENTA.colorIndex;
                }
                if (!p_getMapColorIndex_0_.equals("light_blue") && !p_getMapColorIndex_0_.equals("lightBlue")) {
                    if (p_getMapColorIndex_0_.equals("yellow")) {
                        return MapColor.YELLOW.colorIndex;
                    }
                    if (p_getMapColorIndex_0_.equals("lime")) {
                        return MapColor.LIME.colorIndex;
                    }
                    if (p_getMapColorIndex_0_.equals("pink")) {
                        return MapColor.PINK.colorIndex;
                    }
                    if (p_getMapColorIndex_0_.equals("gray")) {
                        return MapColor.GRAY.colorIndex;
                    }
                    if (p_getMapColorIndex_0_.equals("silver")) {
                        return MapColor.SILVER.colorIndex;
                    }
                    if (p_getMapColorIndex_0_.equals("cyan")) {
                        return MapColor.CYAN.colorIndex;
                    }
                    if (p_getMapColorIndex_0_.equals("purple")) {
                        return MapColor.PURPLE.colorIndex;
                    }
                    if (p_getMapColorIndex_0_.equals("blue")) {
                        return MapColor.BLUE.colorIndex;
                    }
                    if (p_getMapColorIndex_0_.equals("brown")) {
                        return MapColor.BROWN.colorIndex;
                    }
                    if (p_getMapColorIndex_0_.equals("green")) {
                        return MapColor.GREEN.colorIndex;
                    }
                    if (p_getMapColorIndex_0_.equals("red")) {
                        return MapColor.RED.colorIndex;
                    }
                    return p_getMapColorIndex_0_.equals("black") ? MapColor.BLACK.colorIndex : -1;
                }
                return MapColor.LIGHT_BLUE.colorIndex;
            }
            return MapColor.ADOBE.colorIndex;
        }
        return MapColor.SNOW.colorIndex;
    }

    private static int[] getMapColors() {
        MapColor[] amapcolor = MapColor.COLORS;
        int[] aint = new int[amapcolor.length];
        Arrays.fill(aint, -1);
        for (int i = 0; i < amapcolor.length && i < aint.length; ++i) {
            MapColor mapcolor = amapcolor[i];
            if (mapcolor == null) continue;
            aint[i] = mapcolor.colorValue;
        }
        return aint;
    }

    private static void setMapColors(int[] p_setMapColors_0_) {
        if (p_setMapColors_0_ != null) {
            MapColor[] amapcolor = MapColor.COLORS;
            boolean flag = false;
            for (int i = 0; i < amapcolor.length && i < p_setMapColors_0_.length; ++i) {
                int j;
                MapColor mapcolor = amapcolor[i];
                if (mapcolor == null || (j = p_setMapColors_0_[i]) < 0 || mapcolor.colorValue == j) continue;
                mapcolor.colorValue = j;
                flag = true;
            }
            if (flag) {
                Minecraft.getMinecraft().getTextureManager().reloadBannerTextures();
            }
        }
    }

    private static void dbg(String p_dbg_0_) {
        Config.dbg("CustomColors: " + p_dbg_0_);
    }

    private static void warn(String p_warn_0_) {
        Config.warn("CustomColors: " + p_warn_0_);
    }

    public static int getExpBarTextColor(int p_getExpBarTextColor_0_) {
        return expBarTextColor < 0 ? p_getExpBarTextColor_0_ : expBarTextColor;
    }

    public static int getBossTextColor(int p_getBossTextColor_0_) {
        return bossTextColor < 0 ? p_getBossTextColor_0_ : bossTextColor;
    }

    public static int getSignTextColor(int p_getSignTextColor_0_) {
        return signTextColor < 0 ? p_getSignTextColor_0_ : signTextColor;
    }

    public static interface IColorizer {
        public int getColor(IBlockState var1, IBlockAccess var2, BlockPos var3);

        public boolean isColorConstant();
    }
}

