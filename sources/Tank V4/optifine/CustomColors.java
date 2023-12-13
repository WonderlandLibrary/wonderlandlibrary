package optifine;

import java.awt.image.BufferedImage;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
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
   private static final IBlockState BLOCK_STATE_DIRT;
   private static final IBlockState BLOCK_STATE_WATER;
   public static Random random;
   private static final CustomColors.IColorizer COLORIZER_GRASS;
   private static final CustomColors.IColorizer COLORIZER_FOLIAGE;
   private static final CustomColors.IColorizer COLORIZER_FOLIAGE_PINE;
   private static final CustomColors.IColorizer COLORIZER_FOLIAGE_BIRCH;
   private static final CustomColors.IColorizer COLORIZER_WATER;

   static {
      BLOCK_STATE_DIRT = Blocks.dirt.getDefaultState();
      BLOCK_STATE_WATER = Blocks.water.getDefaultState();
      random = new Random();
      COLORIZER_GRASS = new CustomColors.IColorizer() {
         public int getColor(IBlockAccess var1, BlockPos var2) {
            BiomeGenBase var3 = CustomColors.getColorBiome(var1, var2);
            return CustomColors.access$0() != null && var3 == BiomeGenBase.swampland ? CustomColors.access$0().getColor(var3, var2) : var3.getGrassColorAtPos(var2);
         }

         public boolean isColorConstant() {
            return false;
         }
      };
      COLORIZER_FOLIAGE = new CustomColors.IColorizer() {
         public int getColor(IBlockAccess var1, BlockPos var2) {
            BiomeGenBase var3 = CustomColors.getColorBiome(var1, var2);
            return CustomColors.access$1() != null && var3 == BiomeGenBase.swampland ? CustomColors.access$1().getColor(var3, var2) : var3.getFoliageColorAtPos(var2);
         }

         public boolean isColorConstant() {
            return false;
         }
      };
      COLORIZER_FOLIAGE_PINE = new CustomColors.IColorizer() {
         public int getColor(IBlockAccess var1, BlockPos var2) {
            return CustomColors.access$2() != null ? CustomColors.access$2().getColor(var1, var2) : ColorizerFoliage.getFoliageColorPine();
         }

         public boolean isColorConstant() {
            return CustomColors.access$2() == null;
         }
      };
      COLORIZER_FOLIAGE_BIRCH = new CustomColors.IColorizer() {
         public int getColor(IBlockAccess var1, BlockPos var2) {
            return CustomColors.access$3() != null ? CustomColors.access$3().getColor(var1, var2) : ColorizerFoliage.getFoliageColorBirch();
         }

         public boolean isColorConstant() {
            return CustomColors.access$3() == null;
         }
      };
      COLORIZER_WATER = new CustomColors.IColorizer() {
         public int getColor(IBlockAccess var1, BlockPos var2) {
            BiomeGenBase var3 = CustomColors.getColorBiome(var1, var2);
            return CustomColors.access$4() != null ? CustomColors.access$4().getColor(var3, var2) : (Reflector.ForgeBiome_getWaterColorMultiplier.exists() ? Reflector.callInt(var3, Reflector.ForgeBiome_getWaterColorMultiplier) : var3.waterColorMultiplier);
         }

         public boolean isColorConstant() {
            return false;
         }
      };
   }

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
      setMapColors(mapColorsOriginal);
      potionColors = null;
      PotionHelper.clearPotionColorCache();
      paletteFormatDefault = getValidProperty("mcpatcher/color.properties", "palette.format", CustomColormap.FORMAT_STRINGS, "vanilla");
      String var0 = "mcpatcher/colormap/";
      String[] var1 = new String[]{"water.png", "watercolorX.png"};
      waterColors = getCustomColors(var0, var1, 256, 256);
      updateUseDefaultGrassFoliageColors();
      if (Config.isCustomColors()) {
         String[] var2 = new String[]{"pine.png", "pinecolor.png"};
         foliagePineColors = getCustomColors(var0, var2, 256, 256);
         String[] var3 = new String[]{"birch.png", "birchcolor.png"};
         foliageBirchColors = getCustomColors(var0, var3, 256, 256);
         String[] var4 = new String[]{"swampgrass.png", "swampgrasscolor.png"};
         swampGrassColors = getCustomColors(var0, var4, 256, 256);
         String[] var5 = new String[]{"swampfoliage.png", "swampfoliagecolor.png"};
         swampFoliageColors = getCustomColors(var0, var5, 256, 256);
         String[] var6 = new String[]{"sky0.png", "skycolor0.png"};
         skyColors = getCustomColors(var0, var6, 256, 256);
         String[] var7 = new String[]{"fog0.png", "fogcolor0.png"};
         fogColors = getCustomColors(var0, var7, 256, 256);
         String[] var8 = new String[]{"underwater.png", "underwatercolor.png"};
         underwaterColors = getCustomColors(var0, var8, 256, 256);
         String[] var9 = new String[]{"redstone.png", "redstonecolor.png"};
         redstoneColors = getCustomColors(var0, var9, 16, 1);
         xpOrbColors = getCustomColors(var0 + "xporb.png", -1, -1);
         durabilityColors = getCustomColors(var0 + "durability.png", -1, -1);
         String[] var10 = new String[]{"stem.png", "stemcolor.png"};
         stemColors = getCustomColors(var0, var10, 8, 1);
         stemPumpkinColors = getCustomColors(var0 + "pumpkinstem.png", 8, 1);
         stemMelonColors = getCustomColors(var0 + "melonstem.png", 8, 1);
         String[] var11 = new String[]{"myceliumparticle.png", "myceliumparticlecolor.png"};
         myceliumParticleColors = getCustomColors(var0, var11, -1, -1);
         Pair var12 = parseLightmapsRgb();
         lightMapsColorsRgb = (CustomColormap[])var12.getLeft();
         lightmapMinDimensionId = (Integer)var12.getRight();
         readColorProperties("mcpatcher/color.properties");
         blockColormaps = readBlockColormaps(new String[]{var0 + "custom/", var0 + "blocks/"}, colorsBlockColormaps, 256, 256);
         updateUseDefaultGrassFoliageColors();
      }

   }

   private static String getValidProperty(String var0, String var1, String[] var2, String var3) {
      try {
         ResourceLocation var4 = new ResourceLocation(var0);
         InputStream var5 = Config.getResourceStream(var4);
         if (var5 == null) {
            return var3;
         } else {
            Properties var6 = new Properties();
            var6.load(var5);
            var5.close();
            String var7 = var6.getProperty(var1);
            if (var7 == null) {
               return var3;
            } else {
               List var8 = Arrays.asList(var2);
               if (!var8.contains(var7)) {
                  warn("Invalid value: " + var1 + "=" + var7);
                  warn("Expected values: " + Config.arrayToString((Object[])var2));
                  return var3;
               } else {
                  dbg(var1 + "=" + var7);
                  return var7;
               }
            }
         }
      } catch (FileNotFoundException var9) {
         return var3;
      } catch (IOException var10) {
         var10.printStackTrace();
         return var3;
      }
   }

   private static Pair parseLightmapsRgb() {
      String var0 = "mcpatcher/lightmap/world";
      String var1 = ".png";
      String[] var2 = ResUtils.collectFiles(var0, var1);
      HashMap var3 = new HashMap();

      int var7;
      for(int var4 = 0; var4 < var2.length; ++var4) {
         String var5 = var2[var4];
         String var6 = StrUtils.removePrefixSuffix(var5, var0, var1);
         var7 = Config.parseInt(var6, Integer.MIN_VALUE);
         if (var7 == Integer.MIN_VALUE) {
            warn("Invalid dimension ID: " + var6 + ", path: " + var5);
         } else {
            var3.put(var7, var5);
         }
      }

      Set var15 = var3.keySet();
      Integer[] var16 = (Integer[])var15.toArray(new Integer[var15.size()]);
      Arrays.sort(var16);
      if (var16.length <= 0) {
         return new ImmutablePair((Object)null, 0);
      } else {
         int var17 = var16[0];
         var7 = var16[var16.length - 1];
         int var8 = var7 - var17 + 1;
         CustomColormap[] var9 = new CustomColormap[var8];

         for(int var10 = 0; var10 < var16.length; ++var10) {
            Integer var11 = var16[var10];
            String var12 = (String)var3.get(var11);
            CustomColormap var13 = getCustomColors(var12, -1, -1);
            if (var13 != null) {
               if (var13.getWidth() < 16) {
                  warn("Invalid lightmap width: " + var13.getWidth() + ", path: " + var12);
               } else {
                  int var14 = var11 - var17;
                  var9[var14] = var13;
               }
            }
         }

         return new ImmutablePair(var9, var17);
      }
   }

   private static int getTextureHeight(String var0, int var1) {
      try {
         InputStream var2 = Config.getResourceStream(new ResourceLocation(var0));
         if (var2 == null) {
            return var1;
         } else {
            BufferedImage var3 = ImageIO.read(var2);
            var2.close();
            return var3 == null ? var1 : var3.getHeight();
         }
      } catch (IOException var4) {
         return var1;
      }
   }

   private static void readColorProperties(String var0) {
      try {
         ResourceLocation var1 = new ResourceLocation(var0);
         InputStream var2 = Config.getResourceStream(var1);
         if (var2 == null) {
            return;
         }

         dbg("Loading " + var0);
         Properties var3 = new Properties();
         var3.load(var2);
         var2.close();
         particleWaterColor = readColor(var3, new String[]{"particle.water", "drop.water"});
         particlePortalColor = readColor(var3, "particle.portal");
         lilyPadColor = readColor(var3, "lilypad");
         expBarTextColor = readColor(var3, "text.xpbar");
         bossTextColor = readColor(var3, "text.boss");
         signTextColor = readColor(var3, "text.sign");
         fogColorNether = readColorVec3(var3, "fog.nether");
         fogColorEnd = readColorVec3(var3, "fog.end");
         skyColorEnd = readColorVec3(var3, "sky.end");
         colorsBlockColormaps = readCustomColormaps(var3, var0);
         spawnEggPrimaryColors = readSpawnEggColors(var3, var0, "egg.shell.", "Spawn egg shell");
         spawnEggSecondaryColors = readSpawnEggColors(var3, var0, "egg.spots.", "Spawn egg spot");
         wolfCollarColors = readDyeColors(var3, var0, "collar.", "Wolf collar");
         sheepColors = readDyeColors(var3, var0, "sheep.", "Sheep");
         textColors = readTextColors(var3, var0, "text.code.", "Text");
         int[] var4 = readMapColors(var3, var0, "map.", "Map");
         if (var4 != null) {
            if (mapColorsOriginal == null) {
               mapColorsOriginal = getMapColors();
            }

            setMapColors(var4);
         }

         potionColors = readPotionColors(var3, var0, "potion.", "Potion");
         xpOrbTime = Config.parseInt(var3.getProperty("xporb.time"), -1);
      } catch (FileNotFoundException var5) {
         return;
      } catch (IOException var6) {
         var6.printStackTrace();
      }

   }

   private static CustomColormap[] readCustomColormaps(Properties var0, String var1) {
      ArrayList var2 = new ArrayList();
      String var3 = "palette.block.";
      HashMap var4 = new HashMap();
      Iterator var6 = var0.keySet().iterator();

      String var7;
      while(var6.hasNext()) {
         Object var5 = var6.next();
         var7 = var0.getProperty((String)var5);
         if (((String)var5).startsWith(var3)) {
            var4.put(var5, var7);
         }
      }

      String[] var16 = (String[])var4.keySet().toArray(new String[var4.size()]);

      for(int var17 = 0; var17 < var16.length; ++var17) {
         var7 = var16[var17];
         String var8 = var0.getProperty(var7);
         dbg("Block palette: " + var7 + " = " + var8);
         String var9 = var7.substring(var3.length());
         String var10 = TextureUtils.getBasePath(var1);
         var9 = TextureUtils.fixResourcePath(var9, var10);
         CustomColormap var11 = getCustomColors(var9, 256, 256);
         if (var11 == null) {
            warn("Colormap not found: " + var9);
         } else {
            ConnectedParser var12 = new ConnectedParser("CustomColors");
            MatchBlock[] var13 = var12.parseMatchBlocks(var8);
            if (var13 != null && var13.length > 0) {
               for(int var14 = 0; var14 < var13.length; ++var14) {
                  MatchBlock var15 = var13[var14];
                  var11.addMatchBlock(var15);
               }

               var2.add(var11);
            } else {
               warn("Invalid match blocks: " + var8);
            }
         }
      }

      if (var2.size() <= 0) {
         return null;
      } else {
         CustomColormap[] var18 = (CustomColormap[])var2.toArray(new CustomColormap[var2.size()]);
         return var18;
      }
   }

   private static CustomColormap[][] readBlockColormaps(String[] var0, CustomColormap[] var1, int var2, int var3) {
      String[] var4 = ResUtils.collectFiles(var0, new String[]{".properties"});
      Arrays.sort(var4);
      ArrayList var5 = new ArrayList();

      int var6;
      for(var6 = 0; var6 < var4.length; ++var6) {
         String var7 = var4[var6];
         dbg("Block colormap: " + var7);

         try {
            ResourceLocation var8 = new ResourceLocation("minecraft", var7);
            InputStream var9 = Config.getResourceStream(var8);
            if (var9 == null) {
               warn("File not found: " + var7);
            } else {
               Properties var10 = new Properties();
               var10.load(var9);
               CustomColormap var11 = new CustomColormap(var10, var7, var2, var3, paletteFormatDefault);
               if (var11.isValid(var7) && var11.isValidMatchBlocks(var7)) {
                  addToBlockList(var11, var5);
               }
            }
         } catch (FileNotFoundException var12) {
            warn("File not found: " + var7);
         } catch (Exception var13) {
            var13.printStackTrace();
         }
      }

      if (var1 != null) {
         for(var6 = 0; var6 < var1.length; ++var6) {
            CustomColormap var15 = var1[var6];
            addToBlockList(var15, var5);
         }
      }

      if (var5.size() <= 0) {
         return null;
      } else {
         CustomColormap[][] var14 = blockListToArray(var5);
         return var14;
      }
   }

   private static void addToBlockList(CustomColormap var0, List var1) {
      int[] var2 = var0.getMatchBlockIds();
      if (var2 != null && var2.length > 0) {
         for(int var3 = 0; var3 < var2.length; ++var3) {
            int var4 = var2[var3];
            if (var4 < 0) {
               warn("Invalid block ID: " + var4);
            } else {
               addToList(var0, var1, var4);
            }
         }
      } else {
         warn("No match blocks: " + Config.arrayToString(var2));
      }

   }

   private static void addToList(CustomColormap var0, List var1, int var2) {
      while(var2 >= var1.size()) {
         var1.add((Object)null);
      }

      Object var3 = (List)var1.get(var2);
      if (var3 == null) {
         var3 = new ArrayList();
         var1.set(var2, var3);
      }

      ((List)var3).add(var0);
   }

   private static CustomColormap[][] blockListToArray(List var0) {
      CustomColormap[][] var1 = new CustomColormap[var0.size()][];

      for(int var2 = 0; var2 < var0.size(); ++var2) {
         List var3 = (List)var0.get(var2);
         if (var3 != null) {
            CustomColormap[] var4 = (CustomColormap[])var3.toArray(new CustomColormap[var3.size()]);
            var1[var2] = var4;
         }
      }

      return var1;
   }

   private static int readColor(Properties var0, String[] var1) {
      for(int var2 = 0; var2 < var1.length; ++var2) {
         String var3 = var1[var2];
         int var4 = readColor(var0, var3);
         if (var4 >= 0) {
            return var4;
         }
      }

      return -1;
   }

   private static int readColor(Properties var0, String var1) {
      String var2 = var0.getProperty(var1);
      if (var2 == null) {
         return -1;
      } else {
         var2 = var2.trim();
         int var3 = parseColor(var2);
         if (var3 < 0) {
            warn("Invalid color: " + var1 + " = " + var2);
            return var3;
         } else {
            dbg(var1 + " = " + var2);
            return var3;
         }
      }
   }

   private static int parseColor(String var0) {
      if (var0 == null) {
         return -1;
      } else {
         var0 = var0.trim();

         try {
            int var1 = Integer.parseInt(var0, 16) & 16777215;
            return var1;
         } catch (NumberFormatException var2) {
            return -1;
         }
      }
   }

   private static Vec3 readColorVec3(Properties var0, String var1) {
      int var2 = readColor(var0, var1);
      if (var2 < 0) {
         return null;
      } else {
         int var3 = var2 >> 16 & 255;
         int var4 = var2 >> 8 & 255;
         int var5 = var2 & 255;
         float var6 = (float)var3 / 255.0F;
         float var7 = (float)var4 / 255.0F;
         float var8 = (float)var5 / 255.0F;
         return new Vec3((double)var6, (double)var7, (double)var8);
      }
   }

   private static CustomColormap getCustomColors(String var0, String[] var1, int var2, int var3) {
      for(int var4 = 0; var4 < var1.length; ++var4) {
         String var5 = var1[var4];
         var5 = var0 + var5;
         CustomColormap var6 = getCustomColors(var5, var2, var3);
         if (var6 != null) {
            return var6;
         }
      }

      return null;
   }

   public static CustomColormap getCustomColors(String var0, int var1, int var2) {
      try {
         ResourceLocation var3 = new ResourceLocation(var0);
         if (!Config.hasResource(var3)) {
            return null;
         } else {
            dbg("Colormap " + var0);
            Properties var4 = new Properties();
            String var5 = StrUtils.replaceSuffix(var0, ".png", ".properties");
            ResourceLocation var6 = new ResourceLocation(var5);
            if (Config.hasResource(var6)) {
               InputStream var7 = Config.getResourceStream(var6);
               var4.load(var7);
               var7.close();
               dbg("Colormap properties: " + var5);
            } else {
               var4.put("format", paletteFormatDefault);
               var4.put("source", var0);
               var5 = var0;
            }

            CustomColormap var9 = new CustomColormap(var4, var5, var1, var2, paletteFormatDefault);
            return !var9.isValid(var5) ? null : var9;
         }
      } catch (Exception var8) {
         var8.printStackTrace();
         return null;
      }
   }

   public static void updateUseDefaultGrassFoliageColors() {
      useDefaultGrassFoliageColors = foliageBirchColors == null && foliagePineColors == null && swampGrassColors == null && swampFoliageColors == null && Config.isSwampColors() && Config.isSmoothBiomes();
   }

   public static int getColorMultiplier(BakedQuad var0, Block var1, IBlockAccess var2, BlockPos var3, RenderEnv var4) {
      if (blockColormaps != null) {
         IBlockState var5 = var4.getBlockState();
         if (!var0.hasTintIndex()) {
            if (var1 == Blocks.grass) {
               var5 = BLOCK_STATE_DIRT;
            }

            if (var1 == Blocks.redstone_wire) {
               return -1;
            }
         }

         if (var1 == Blocks.double_plant && var4.getMetadata() >= 8) {
            var3 = var3.down();
            var5 = var2.getBlockState(var3);
         }

         CustomColormap var6 = getBlockColormap(var5);
         if (var6 != null) {
            if (Config.isSmoothBiomes() && !var6.isColorConstant()) {
               return getSmoothColorMultiplier(var2, var3, var6, var4.getColorizerBlockPosM());
            }

            return var6.getColor(var2, var3);
         }
      }

      if (!var0.hasTintIndex()) {
         return -1;
      } else if (var1 == Blocks.waterlily) {
         return getLilypadColorMultiplier(var2, var3);
      } else if (var1 == Blocks.redstone_wire) {
         return getRedstoneColor(var4.getBlockState());
      } else if (var1 instanceof BlockStem) {
         return getStemColorMultiplier(var1, var2, var3, var4);
      } else if (useDefaultGrassFoliageColors) {
         return -1;
      } else {
         int var7 = var4.getMetadata();
         CustomColors.IColorizer var8;
         if (var1 != Blocks.grass && var1 != Blocks.tallgrass && var1 != Blocks.double_plant) {
            if (var1 == Blocks.double_plant) {
               var8 = COLORIZER_GRASS;
               if (var7 >= 8) {
                  var3 = var3.down();
               }
            } else if (var1 == Blocks.leaves) {
               switch(var7 & 3) {
               case 0:
                  var8 = COLORIZER_FOLIAGE;
                  break;
               case 1:
                  var8 = COLORIZER_FOLIAGE_PINE;
                  break;
               case 2:
                  var8 = COLORIZER_FOLIAGE_BIRCH;
                  break;
               default:
                  var8 = COLORIZER_FOLIAGE;
               }
            } else if (var1 == Blocks.leaves2) {
               var8 = COLORIZER_FOLIAGE;
            } else {
               if (var1 != Blocks.vine) {
                  return -1;
               }

               var8 = COLORIZER_FOLIAGE;
            }
         } else {
            var8 = COLORIZER_GRASS;
         }

         return Config.isSmoothBiomes() && !var8.isColorConstant() ? getSmoothColorMultiplier(var2, var3, var8, var4.getColorizerBlockPosM()) : var8.getColor(var2, var3);
      }
   }

   protected static BiomeGenBase getColorBiome(IBlockAccess var0, BlockPos var1) {
      BiomeGenBase var2 = var0.getBiomeGenForCoords(var1);
      if (var2 == BiomeGenBase.swampland && !Config.isSwampColors()) {
         var2 = BiomeGenBase.plains;
      }

      return var2;
   }

   private static CustomColormap getBlockColormap(IBlockState var0) {
      if (blockColormaps == null) {
         return null;
      } else if (!(var0 instanceof BlockStateBase)) {
         return null;
      } else {
         BlockStateBase var1 = (BlockStateBase)var0;
         int var2 = var1.getBlockId();
         if (var2 >= 0 && var2 < blockColormaps.length) {
            CustomColormap[] var3 = blockColormaps[var2];
            if (var3 == null) {
               return null;
            } else {
               for(int var4 = 0; var4 < var3.length; ++var4) {
                  CustomColormap var5 = var3[var4];
                  if (var5.matchesBlock(var1)) {
                     return var5;
                  }
               }

               return null;
            }
         } else {
            return null;
         }
      }
   }

   private static int getSmoothColorMultiplier(IBlockAccess var0, BlockPos var1, CustomColors.IColorizer var2, BlockPosM var3) {
      int var4 = 0;
      int var5 = 0;
      int var6 = 0;
      int var7 = var1.getX();
      int var8 = var1.getY();
      int var9 = var1.getZ();
      BlockPosM var10 = var3;

      int var11;
      int var12;
      int var13;
      for(var11 = var7 - 1; var11 <= var7 + 1; ++var11) {
         for(var12 = var9 - 1; var12 <= var9 + 1; ++var12) {
            var10.setXyz(var11, var8, var12);
            var13 = var2.getColor(var0, var10);
            var4 += var13 >> 16 & 255;
            var5 += var13 >> 8 & 255;
            var6 += var13 & 255;
         }
      }

      var11 = var4 / 9;
      var12 = var5 / 9;
      var13 = var6 / 9;
      return var11 << 16 | var12 << 8 | var13;
   }

   public static int getFluidColor(IBlockAccess var0, IBlockState var1, BlockPos var2, RenderEnv var3) {
      Block var4 = var1.getBlock();
      Object var5 = getBlockColormap(var1);
      if (var5 == null && var4.getMaterial() == Material.water) {
         var5 = COLORIZER_WATER;
      }

      return var5 == null ? var4.colorMultiplier(var0, var2) : (Config.isSmoothBiomes() && !((CustomColors.IColorizer)var5).isColorConstant() ? getSmoothColorMultiplier(var0, var2, (CustomColors.IColorizer)var5, var3.getColorizerBlockPosM()) : ((CustomColors.IColorizer)var5).getColor(var0, var2));
   }

   public static void updatePortalFX(EntityFX var0) {
      if (particlePortalColor >= 0) {
         int var1 = particlePortalColor;
         int var2 = var1 >> 16 & 255;
         int var3 = var1 >> 8 & 255;
         int var4 = var1 & 255;
         float var5 = (float)var2 / 255.0F;
         float var6 = (float)var3 / 255.0F;
         float var7 = (float)var4 / 255.0F;
         var0.setRBGColorF(var5, var6, var7);
      }

   }

   public static void updateMyceliumFX(EntityFX var0) {
      if (myceliumParticleColors != null) {
         int var1 = myceliumParticleColors.getColorRandom();
         int var2 = var1 >> 16 & 255;
         int var3 = var1 >> 8 & 255;
         int var4 = var1 & 255;
         float var5 = (float)var2 / 255.0F;
         float var6 = (float)var3 / 255.0F;
         float var7 = (float)var4 / 255.0F;
         var0.setRBGColorF(var5, var6, var7);
      }

   }

   private static int getRedstoneColor(IBlockState var0) {
      if (redstoneColors == null) {
         return -1;
      } else {
         int var1 = getRedstoneLevel(var0, 15);
         int var2 = redstoneColors.getColor(var1);
         return var2;
      }
   }

   public static void updateReddustFX(EntityFX var0, IBlockAccess var1, double var2, double var4, double var6) {
      if (redstoneColors != null) {
         IBlockState var8 = var1.getBlockState(new BlockPos(var2, var4, var6));
         int var9 = getRedstoneLevel(var8, 15);
         int var10 = redstoneColors.getColor(var9);
         int var11 = var10 >> 16 & 255;
         int var12 = var10 >> 8 & 255;
         int var13 = var10 & 255;
         float var14 = (float)var11 / 255.0F;
         float var15 = (float)var12 / 255.0F;
         float var16 = (float)var13 / 255.0F;
         var0.setRBGColorF(var14, var15, var16);
      }

   }

   private static int getRedstoneLevel(IBlockState var0, int var1) {
      Block var2 = var0.getBlock();
      if (!(var2 instanceof BlockRedstoneWire)) {
         return var1;
      } else {
         Comparable var3 = var0.getValue(BlockRedstoneWire.POWER);
         if (!(var3 instanceof Integer)) {
            return var1;
         } else {
            Integer var4 = (Integer)var3;
            return var4;
         }
      }
   }

   public static float getXpOrbTimer(float var0) {
      if (xpOrbTime <= 0) {
         return var0;
      } else {
         float var1 = 628.0F / (float)xpOrbTime;
         return var0 * var1;
      }
   }

   public static int getXpOrbColor(float var0) {
      if (xpOrbColors == null) {
         return -1;
      } else {
         int var1 = (int)Math.round((double)((MathHelper.sin(var0) + 1.0F) * (float)(xpOrbColors.getLength() - 1)) / 2.0D);
         int var2 = xpOrbColors.getColor(var1);
         return var2;
      }
   }

   public static int getDurabilityColor(int var0) {
      if (durabilityColors == null) {
         return -1;
      } else {
         int var1 = var0 * durabilityColors.getLength() / 255;
         int var2 = durabilityColors.getColor(var1);
         return var2;
      }
   }

   public static void updateWaterFX(EntityFX var0, IBlockAccess var1, double var2, double var4, double var6) {
      if (waterColors != null || blockColormaps != null) {
         BlockPos var8 = new BlockPos(var2, var4, var6);
         RenderEnv var9 = RenderEnv.getInstance(var1, BLOCK_STATE_WATER, var8);
         int var10 = getFluidColor(var1, BLOCK_STATE_WATER, var8, var9);
         int var11 = var10 >> 16 & 255;
         int var12 = var10 >> 8 & 255;
         int var13 = var10 & 255;
         float var14 = (float)var11 / 255.0F;
         float var15 = (float)var12 / 255.0F;
         float var16 = (float)var13 / 255.0F;
         if (particleWaterColor >= 0) {
            int var17 = particleWaterColor >> 16 & 255;
            int var18 = particleWaterColor >> 8 & 255;
            int var19 = particleWaterColor & 255;
            var14 *= (float)var17 / 255.0F;
            var15 *= (float)var18 / 255.0F;
            var16 *= (float)var19 / 255.0F;
         }

         var0.setRBGColorF(var14, var15, var16);
      }

   }

   private static int getLilypadColorMultiplier(IBlockAccess var0, BlockPos var1) {
      return lilyPadColor < 0 ? Blocks.waterlily.colorMultiplier(var0, var1) : lilyPadColor;
   }

   private static Vec3 getFogColorNether(Vec3 var0) {
      return fogColorNether == null ? var0 : fogColorNether;
   }

   private static Vec3 getFogColorEnd(Vec3 var0) {
      return fogColorEnd == null ? var0 : fogColorEnd;
   }

   private static Vec3 getSkyColorEnd(Vec3 var0) {
      return skyColorEnd == null ? var0 : skyColorEnd;
   }

   public static Vec3 getSkyColor(Vec3 var0, IBlockAccess var1, double var2, double var4, double var6) {
      if (skyColors == null) {
         return var0;
      } else {
         int var8 = skyColors.getColorSmooth(var1, var2, var4, var6, 3);
         int var9 = var8 >> 16 & 255;
         int var10 = var8 >> 8 & 255;
         int var11 = var8 & 255;
         float var12 = (float)var9 / 255.0F;
         float var13 = (float)var10 / 255.0F;
         float var14 = (float)var11 / 255.0F;
         float var15 = (float)var0.xCoord / 0.5F;
         float var16 = (float)var0.yCoord / 0.66275F;
         float var17 = (float)var0.zCoord;
         var12 *= var15;
         var13 *= var16;
         var14 *= var17;
         Vec3 var18 = skyColorFader.getColor((double)var12, (double)var13, (double)var14);
         return var18;
      }
   }

   private static Vec3 getFogColor(Vec3 var0, IBlockAccess var1, double var2, double var4, double var6) {
      if (fogColors == null) {
         return var0;
      } else {
         int var8 = fogColors.getColorSmooth(var1, var2, var4, var6, 3);
         int var9 = var8 >> 16 & 255;
         int var10 = var8 >> 8 & 255;
         int var11 = var8 & 255;
         float var12 = (float)var9 / 255.0F;
         float var13 = (float)var10 / 255.0F;
         float var14 = (float)var11 / 255.0F;
         float var15 = (float)var0.xCoord / 0.753F;
         float var16 = (float)var0.yCoord / 0.8471F;
         float var17 = (float)var0.zCoord;
         var12 *= var15;
         var13 *= var16;
         var14 *= var17;
         Vec3 var18 = fogColorFader.getColor((double)var12, (double)var13, (double)var14);
         return var18;
      }
   }

   public static Vec3 getUnderwaterColor(IBlockAccess var0, double var1, double var3, double var5) {
      if (underwaterColors == null) {
         return null;
      } else {
         int var7 = underwaterColors.getColorSmooth(var0, var1, var3, var5, 3);
         int var8 = var7 >> 16 & 255;
         int var9 = var7 >> 8 & 255;
         int var10 = var7 & 255;
         float var11 = (float)var8 / 255.0F;
         float var12 = (float)var9 / 255.0F;
         float var13 = (float)var10 / 255.0F;
         Vec3 var14 = underwaterColorFader.getColor((double)var11, (double)var12, (double)var13);
         return var14;
      }
   }

   private static int getStemColorMultiplier(Block var0, IBlockAccess var1, BlockPos var2, RenderEnv var3) {
      CustomColormap var4 = stemColors;
      if (var0 == Blocks.pumpkin_stem && stemPumpkinColors != null) {
         var4 = stemPumpkinColors;
      }

      if (var0 == Blocks.melon_stem && stemMelonColors != null) {
         var4 = stemMelonColors;
      }

      if (var4 == null) {
         return -1;
      } else {
         int var5 = var3.getMetadata();
         return var4.getColor(var5);
      }
   }

   public static boolean updateLightmap(World var0, float var1, int[] var2, boolean var3) {
      if (var0 == null) {
         return false;
      } else if (lightMapsColorsRgb == null) {
         return false;
      } else {
         int var4 = var0.provider.getDimensionId();
         int var5 = var4 - lightmapMinDimensionId;
         if (var5 >= 0 && var5 < lightMapsColorsRgb.length) {
            CustomColormap var6 = lightMapsColorsRgb[var5];
            if (var6 == null) {
               return false;
            } else {
               int var7 = var6.getHeight();
               if (var3 && var7 < 64) {
                  return false;
               } else {
                  int var8 = var6.getWidth();
                  if (var8 < 16) {
                     warn("Invalid lightmap width: " + var8 + " for dimension: " + var4);
                     lightMapsColorsRgb[var5] = null;
                     return false;
                  } else {
                     int var9 = 0;
                     if (var3) {
                        var9 = var8 * 16 * 2;
                     }

                     float var10 = 1.1666666F * (var0.getSunBrightness(1.0F) - 0.2F);
                     if (var0.getLastLightningBolt() > 0) {
                        var10 = 1.0F;
                     }

                     var10 = Config.limitTo1(var10);
                     float var11 = var10 * (float)(var8 - 1);
                     float var12 = Config.limitTo1(var1 + 0.5F) * (float)(var8 - 1);
                     float var13 = Config.limitTo1(Config.getGameSettings().gammaSetting);
                     boolean var14 = var13 > 1.0E-4F;
                     float[][] var15 = var6.getColorsRgb();
                     getLightMapColumn(var15, var11, var9, var8, sunRgbs);
                     getLightMapColumn(var15, var12, var9 + 16 * var8, var8, torchRgbs);
                     float[] var16 = new float[3];

                     for(int var17 = 0; var17 < 16; ++var17) {
                        for(int var18 = 0; var18 < 16; ++var18) {
                           int var19;
                           for(var19 = 0; var19 < 3; ++var19) {
                              float var20 = Config.limitTo1(sunRgbs[var17][var19] + torchRgbs[var18][var19]);
                              if (var14) {
                                 float var21 = 1.0F - var20;
                                 var21 = 1.0F - var21 * var21 * var21 * var21;
                                 var20 = var13 * var21 + (1.0F - var13) * var20;
                              }

                              var16[var19] = var20;
                           }

                           var19 = (int)(var16[0] * 255.0F);
                           int var22 = (int)(var16[1] * 255.0F);
                           int var23 = (int)(var16[2] * 255.0F);
                           var2[var17 * 16 + var18] = -16777216 | var19 << 16 | var22 << 8 | var23;
                        }
                     }

                     return true;
                  }
               }
            }
         } else {
            return false;
         }
      }
   }

   private static void getLightMapColumn(float[][] var0, float var1, int var2, int var3, float[][] var4) {
      int var5 = (int)Math.floor((double)var1);
      int var6 = (int)Math.ceil((double)var1);
      if (var5 == var6) {
         for(int var7 = 0; var7 < 16; ++var7) {
            float[] var8 = var0[var2 + var7 * var3 + var5];
            float[] var9 = var4[var7];

            for(int var10 = 0; var10 < 3; ++var10) {
               var9[var10] = var8[var10];
            }
         }
      } else {
         float var14 = 1.0F - (var1 - (float)var5);
         float var15 = 1.0F - ((float)var6 - var1);

         for(int var16 = 0; var16 < 16; ++var16) {
            float[] var17 = var0[var2 + var16 * var3 + var5];
            float[] var11 = var0[var2 + var16 * var3 + var6];
            float[] var12 = var4[var16];

            for(int var13 = 0; var13 < 3; ++var13) {
               var12[var13] = var17[var13] * var14 + var11[var13] * var15;
            }
         }
      }

   }

   public static Vec3 getWorldFogColor(Vec3 var0, WorldClient var1, Entity var2, float var3) {
      int var4 = var1.provider.getDimensionId();
      switch(var4) {
      case -1:
         var0 = getFogColorNether(var0);
         break;
      case 0:
         Minecraft var5 = Minecraft.getMinecraft();
         var0 = getFogColor(var0, Minecraft.theWorld, var2.posX, var2.posY + 1.0D, var2.posZ);
         break;
      case 1:
         var0 = getFogColorEnd(var0);
      }

      return var0;
   }

   public static Vec3 getWorldSkyColor(Vec3 var0, World var1, Entity var2, float var3) {
      int var4 = var1.provider.getDimensionId();
      switch(var4) {
      case 0:
         Minecraft var5 = Minecraft.getMinecraft();
         var0 = getSkyColor(var0, Minecraft.theWorld, var2.posX, var2.posY + 1.0D, var2.posZ);
         break;
      case 1:
         var0 = getSkyColorEnd(var0);
      }

      return var0;
   }

   private static int[] readSpawnEggColors(Properties var0, String var1, String var2, String var3) {
      ArrayList var4 = new ArrayList();
      Set var5 = var0.keySet();
      int var6 = 0;
      Iterator var8 = var5.iterator();

      while(true) {
         while(true) {
            Object var7;
            String var9;
            do {
               if (!var8.hasNext()) {
                  if (var6 <= 0) {
                     return null;
                  }

                  dbg(var3 + " colors: " + var6);
                  int[] var13 = new int[var4.size()];

                  for(int var14 = 0; var14 < var13.length; ++var14) {
                     var13[var14] = (Integer)var4.get(var14);
                  }

                  return var13;
               }

               var7 = var8.next();
               var9 = var0.getProperty((String)var7);
            } while(!((String)var7).startsWith(var2));

            String var10 = StrUtils.removePrefix((String)var7, var2);
            int var11 = getEntityId(var10);
            int var12 = parseColor(var9);
            if (var11 >= 0 && var12 >= 0) {
               while(var4.size() <= var11) {
                  var4.add(-1);
               }

               var4.set(var11, var12);
               ++var6;
            } else {
               warn("Invalid spawn egg color: " + var7 + " = " + var9);
            }
         }
      }
   }

   private static int getSpawnEggColor(ItemMonsterPlacer var0, ItemStack var1, int var2, int var3) {
      int var4 = var1.getMetadata();
      int[] var5 = var2 == 0 ? spawnEggPrimaryColors : spawnEggSecondaryColors;
      if (var5 == null) {
         return var3;
      } else if (var4 >= 0 && var4 < var5.length) {
         int var6 = var5[var4];
         return var6 < 0 ? var3 : var6;
      } else {
         return var3;
      }
   }

   public static int getColorFromItemStack(ItemStack var0, int var1, int var2) {
      if (var0 == null) {
         return var2;
      } else {
         Item var3 = var0.getItem();
         return var3 == null ? var2 : (var3 instanceof ItemMonsterPlacer ? getSpawnEggColor((ItemMonsterPlacer)var3, var0, var1, var2) : var2);
      }
   }

   private static float[][] readDyeColors(Properties var0, String var1, String var2, String var3) {
      EnumDyeColor[] var4 = EnumDyeColor.values();
      HashMap var5 = new HashMap();

      for(int var6 = 0; var6 < var4.length; ++var6) {
         EnumDyeColor var7 = var4[var6];
         var5.put(var7.getName(), var7);
      }

      float[][] var15 = new float[var4.length][];
      int var16 = 0;
      Iterator var9 = var0.keySet().iterator();

      while(true) {
         while(true) {
            Object var8;
            String var10;
            do {
               if (!var9.hasNext()) {
                  if (var16 <= 0) {
                     return null;
                  }

                  dbg(var3 + " colors: " + var16);
                  return var15;
               }

               var8 = var9.next();
               var10 = var0.getProperty((String)var8);
            } while(!((String)var8).startsWith(var2));

            String var11 = StrUtils.removePrefix((String)var8, var2);
            if (var11.equals("lightBlue")) {
               var11 = "light_blue";
            }

            EnumDyeColor var12 = (EnumDyeColor)var5.get(var11);
            int var13 = parseColor(var10);
            if (var12 != null && var13 >= 0) {
               float[] var14 = new float[]{(float)(var13 >> 16 & 255) / 255.0F, (float)(var13 >> 8 & 255) / 255.0F, (float)(var13 & 255) / 255.0F};
               var15[var12.ordinal()] = var14;
               ++var16;
            } else {
               warn("Invalid color: " + var8 + " = " + var10);
            }
         }
      }
   }

   private static float[] getDyeColors(EnumDyeColor var0, float[][] var1, float[] var2) {
      if (var1 == null) {
         return var2;
      } else if (var0 == null) {
         return var2;
      } else {
         float[] var3 = var1[var0.ordinal()];
         return var3 == null ? var2 : var3;
      }
   }

   public static float[] getWolfCollarColors(EnumDyeColor var0, float[] var1) {
      return getDyeColors(var0, wolfCollarColors, var1);
   }

   public static float[] getSheepColors(EnumDyeColor var0, float[] var1) {
      return getDyeColors(var0, sheepColors, var1);
   }

   private static int[] readTextColors(Properties var0, String var1, String var2, String var3) {
      int[] var4 = new int[32];
      Arrays.fill(var4, -1);
      int var5 = 0;
      Iterator var7 = var0.keySet().iterator();

      while(true) {
         while(true) {
            Object var6;
            String var8;
            do {
               if (!var7.hasNext()) {
                  if (var5 <= 0) {
                     return null;
                  }

                  dbg(var3 + " colors: " + var5);
                  return var4;
               }

               var6 = var7.next();
               var8 = var0.getProperty((String)var6);
            } while(!((String)var6).startsWith(var2));

            String var9 = StrUtils.removePrefix((String)var6, var2);
            int var10 = Config.parseInt(var9, -1);
            int var11 = parseColor(var8);
            if (var10 >= 0 && var10 < var4.length && var11 >= 0) {
               var4[var10] = var11;
               ++var5;
            } else {
               warn("Invalid color: " + var6 + " = " + var8);
            }
         }
      }
   }

   public static int getTextColor(int var0, int var1) {
      if (textColors == null) {
         return var1;
      } else if (var0 >= 0 && var0 < textColors.length) {
         int var2 = textColors[var0];
         return var2 < 0 ? var1 : var2;
      } else {
         return var1;
      }
   }

   private static int[] readMapColors(Properties var0, String var1, String var2, String var3) {
      int[] var4 = new int[MapColor.mapColorArray.length];
      Arrays.fill(var4, -1);
      int var5 = 0;
      Iterator var7 = var0.keySet().iterator();

      while(true) {
         while(true) {
            Object var6;
            String var8;
            do {
               if (!var7.hasNext()) {
                  if (var5 <= 0) {
                     return null;
                  }

                  dbg(var3 + " colors: " + var5);
                  return var4;
               }

               var6 = var7.next();
               var8 = var0.getProperty((String)var6);
            } while(!((String)var6).startsWith(var2));

            String var9 = StrUtils.removePrefix((String)var6, var2);
            int var10 = getMapColorIndex(var9);
            int var11 = parseColor(var8);
            if (var10 >= 0 && var10 < var4.length && var11 >= 0) {
               var4[var10] = var11;
               ++var5;
            } else {
               warn("Invalid color: " + var6 + " = " + var8);
            }
         }
      }
   }

   private static int[] readPotionColors(Properties var0, String var1, String var2, String var3) {
      int[] var4 = new int[Potion.potionTypes.length];
      Arrays.fill(var4, -1);
      int var5 = 0;
      Iterator var7 = var0.keySet().iterator();

      while(true) {
         while(true) {
            Object var6;
            String var8;
            do {
               if (!var7.hasNext()) {
                  if (var5 <= 0) {
                     return null;
                  }

                  dbg(var3 + " colors: " + var5);
                  return var4;
               }

               var6 = var7.next();
               var8 = var0.getProperty((String)var6);
            } while(!((String)var6).startsWith(var2));

            int var9 = getPotionId((String)var6);
            int var10 = parseColor(var8);
            if (var9 >= 0 && var9 < var4.length && var10 >= 0) {
               var4[var9] = var10;
               ++var5;
            } else {
               warn("Invalid color: " + var6 + " = " + var8);
            }
         }
      }
   }

   private static int getPotionId(String var0) {
      if (var0.equals("potion.water")) {
         return 0;
      } else {
         Potion[] var1 = Potion.potionTypes;

         for(int var2 = 0; var2 < var1.length; ++var2) {
            Potion var3 = var1[var2];
            if (var3 != null && var3.getName().equals(var0)) {
               return var3.getId();
            }
         }

         return -1;
      }
   }

   public static int getPotionColor(int var0, int var1) {
      if (potionColors == null) {
         return var1;
      } else if (var0 >= 0 && var0 < potionColors.length) {
         int var2 = potionColors[var0];
         return var2 < 0 ? var1 : var2;
      } else {
         return var1;
      }
   }

   private static int getMapColorIndex(String var0) {
      return var0 == null ? -1 : (var0.equals("air") ? MapColor.airColor.colorIndex : (var0.equals("grass") ? MapColor.grassColor.colorIndex : (var0.equals("sand") ? MapColor.sandColor.colorIndex : (var0.equals("cloth") ? MapColor.clothColor.colorIndex : (var0.equals("tnt") ? MapColor.tntColor.colorIndex : (var0.equals("ice") ? MapColor.iceColor.colorIndex : (var0.equals("iron") ? MapColor.ironColor.colorIndex : (var0.equals("foliage") ? MapColor.foliageColor.colorIndex : (var0.equals("clay") ? MapColor.clayColor.colorIndex : (var0.equals("dirt") ? MapColor.dirtColor.colorIndex : (var0.equals("stone") ? MapColor.stoneColor.colorIndex : (var0.equals("water") ? MapColor.waterColor.colorIndex : (var0.equals("wood") ? MapColor.woodColor.colorIndex : (var0.equals("quartz") ? MapColor.quartzColor.colorIndex : (var0.equals("gold") ? MapColor.goldColor.colorIndex : (var0.equals("diamond") ? MapColor.diamondColor.colorIndex : (var0.equals("lapis") ? MapColor.lapisColor.colorIndex : (var0.equals("emerald") ? MapColor.emeraldColor.colorIndex : (var0.equals("podzol") ? MapColor.obsidianColor.colorIndex : (var0.equals("netherrack") ? MapColor.netherrackColor.colorIndex : (!var0.equals("snow") && !var0.equals("white") ? (!var0.equals("adobe") && !var0.equals("orange") ? (var0.equals("magenta") ? MapColor.magentaColor.colorIndex : (!var0.equals("light_blue") && !var0.equals("lightBlue") ? (var0.equals("yellow") ? MapColor.yellowColor.colorIndex : (var0.equals("lime") ? MapColor.limeColor.colorIndex : (var0.equals("pink") ? MapColor.pinkColor.colorIndex : (var0.equals("gray") ? MapColor.grayColor.colorIndex : (var0.equals("silver") ? MapColor.silverColor.colorIndex : (var0.equals("cyan") ? MapColor.cyanColor.colorIndex : (var0.equals("purple") ? MapColor.purpleColor.colorIndex : (var0.equals("blue") ? MapColor.blueColor.colorIndex : (var0.equals("brown") ? MapColor.brownColor.colorIndex : (var0.equals("green") ? MapColor.greenColor.colorIndex : (var0.equals("red") ? MapColor.redColor.colorIndex : (var0.equals("black") ? MapColor.blackColor.colorIndex : -1)))))))))))) : MapColor.lightBlueColor.colorIndex)) : MapColor.adobeColor.colorIndex) : MapColor.snowColor.colorIndex)))))))))))))))))))));
   }

   private static int[] getMapColors() {
      MapColor[] var0 = MapColor.mapColorArray;
      int[] var1 = new int[var0.length];
      Arrays.fill(var1, -1);

      for(int var2 = 0; var2 < var0.length && var2 < var1.length; ++var2) {
         MapColor var3 = var0[var2];
         if (var3 != null) {
            var1[var2] = var3.colorValue;
         }
      }

      return var1;
   }

   private static void setMapColors(int[] var0) {
      if (var0 != null) {
         MapColor[] var1 = MapColor.mapColorArray;
         boolean var2 = false;

         for(int var3 = 0; var3 < var1.length && var3 < var0.length; ++var3) {
            MapColor var4 = var1[var3];
            if (var4 != null) {
               int var5 = var0[var3];
               if (var5 >= 0 && var4.colorValue != var5) {
                  var4.colorValue = var5;
                  var2 = true;
               }
            }
         }

         if (var2) {
            Minecraft.getMinecraft().getTextureManager().reloadBannerTextures();
         }
      }

   }

   private static int getEntityId(String var0) {
      if (var0 == null) {
         return -1;
      } else {
         int var1 = EntityList.getIDFromString(var0);
         if (var1 < 0) {
            return -1;
         } else {
            String var2 = EntityList.getStringFromID(var1);
            return !Config.equals(var0, var2) ? -1 : var1;
         }
      }
   }

   private static void dbg(String var0) {
      Config.dbg("CustomColors: " + var0);
   }

   private static void warn(String var0) {
      Config.warn("CustomColors: " + var0);
   }

   public static int getExpBarTextColor(int var0) {
      return expBarTextColor < 0 ? var0 : expBarTextColor;
   }

   public static int getBossTextColor(int var0) {
      return bossTextColor < 0 ? var0 : bossTextColor;
   }

   public static int getSignTextColor(int var0) {
      return signTextColor < 0 ? var0 : signTextColor;
   }

   static CustomColormap access$0() {
      return swampGrassColors;
   }

   static CustomColormap access$1() {
      return swampFoliageColors;
   }

   static CustomColormap access$2() {
      return foliagePineColors;
   }

   static CustomColormap access$3() {
      return foliageBirchColors;
   }

   static CustomColormap access$4() {
      return waterColors;
   }

   public interface IColorizer {
      int getColor(IBlockAccess var1, BlockPos var2);

      boolean isColorConstant();
   }
}
