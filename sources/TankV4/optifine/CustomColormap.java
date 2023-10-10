package optifine;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashSet;
import java.util.Properties;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import net.minecraft.block.Block;
import net.minecraft.block.state.BlockStateBase;
import net.minecraft.client.renderer.texture.TextureUtil;
import net.minecraft.util.BlockPos;
import net.minecraft.util.MathHelper;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.biome.BiomeGenBase;

public class CustomColormap implements CustomColors.IColorizer {
   public String name = null;
   public String basePath = null;
   private int format = -1;
   private MatchBlock[] matchBlocks = null;
   private String source = null;
   private int color = -1;
   private int yVariance = 0;
   private int yOffset = 0;
   private int width = 0;
   private int height = 0;
   private int[] colors = null;
   private float[][] colorsRgb = null;
   private static final int FORMAT_UNKNOWN = -1;
   private static final int FORMAT_VANILLA = 0;
   private static final int FORMAT_GRID = 1;
   private static final int FORMAT_FIXED = 2;
   public static final String FORMAT_VANILLA_STRING = "vanilla";
   public static final String FORMAT_GRID_STRING = "grid";
   public static final String FORMAT_FIXED_STRING = "fixed";
   public static final String[] FORMAT_STRINGS = new String[]{"vanilla", "grid", "fixed"};
   public static final String KEY_FORMAT = "format";
   public static final String KEY_BLOCKS = "blocks";
   public static final String KEY_SOURCE = "source";
   public static final String KEY_COLOR = "color";
   public static final String KEY_Y_VARIANCE = "yVariance";
   public static final String KEY_Y_OFFSET = "yOffset";

   public CustomColormap(Properties var1, String var2, int var3, int var4, String var5) {
      ConnectedParser var6 = new ConnectedParser("Colormap");
      this.name = var6.parseName(var2);
      this.basePath = var6.parseBasePath(var2);
      this.format = this.parseFormat(var1.getProperty("format", var5));
      this.matchBlocks = var6.parseMatchBlocks(var1.getProperty("blocks"));
      this.source = parseTexture(var1.getProperty("source"), var2, this.basePath);
      this.color = ConnectedParser.parseColor(var1.getProperty("color"), -1);
      this.yVariance = var6.parseInt(var1.getProperty("yVariance"), 0);
      this.yOffset = var6.parseInt(var1.getProperty("yOffset"), 0);
      this.width = var3;
      this.height = var4;
   }

   private int parseFormat(String var1) {
      if (var1 == null) {
         return 0;
      } else if (var1.equals("vanilla")) {
         return 0;
      } else if (var1.equals("grid")) {
         return 1;
      } else if (var1.equals("fixed")) {
         return 2;
      } else {
         warn("Unknown format: " + var1);
         return -1;
      }
   }

   public boolean isValid(String var1) {
      if (this.format != 0 && this.format != 1) {
         if (this.format != 2) {
            return false;
         }

         if (this.color < 0) {
            this.color = 16777215;
         }
      } else {
         if (this.source == null) {
            warn("Source not defined: " + var1);
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

   public boolean isValidMatchBlocks(String var1) {
      if (this.matchBlocks == null) {
         this.matchBlocks = this.detectMatchBlocks();
         if (this.matchBlocks == null) {
            warn("Match blocks not defined: " + var1);
            return false;
         }
      }

      return true;
   }

   private MatchBlock[] detectMatchBlocks() {
      Block var1 = Block.getBlockFromName(this.name);
      if (var1 != null) {
         return new MatchBlock[]{new MatchBlock(Block.getIdFromBlock(var1))};
      } else {
         Pattern var2 = Pattern.compile("^block([0-9]+).*$");
         Matcher var3 = var2.matcher(this.name);
         if (var3.matches()) {
            String var4 = var3.group(1);
            int var5 = Config.parseInt(var4, -1);
            if (var5 >= 0) {
               return new MatchBlock[]{new MatchBlock(var5)};
            }
         }

         ConnectedParser var6 = new ConnectedParser("Colormap");
         MatchBlock[] var7 = var6.parseMatchBlock(this.name);
         return var7 != null ? var7 : null;
      }
   }

   private void readColors() {
      try {
         this.colors = null;
         if (this.source == null) {
            return;
         }

         String var1 = this.source + ".png";
         ResourceLocation var2 = new ResourceLocation(var1);
         InputStream var3 = Config.getResourceStream(var2);
         if (var3 == null) {
            return;
         }

         BufferedImage var4 = TextureUtil.readBufferedImage(var3);
         if (var4 == null) {
            return;
         }

         int var5 = var4.getWidth();
         int var6 = var4.getHeight();
         boolean var7 = this.width < 0 || this.width == var5;
         boolean var8 = this.height < 0 || this.height == var6;
         if (!var7 || !var8) {
            dbg("Non-standard palette size: " + var5 + "x" + var6 + ", should be: " + this.width + "x" + this.height + ", path: " + var1);
         }

         this.width = var5;
         this.height = var6;
         if (this.width <= 0 || this.height <= 0) {
            warn("Invalid palette size: " + var5 + "x" + var6 + ", path: " + var1);
            return;
         }

         this.colors = new int[var5 * var6];
         var4.getRGB(0, 0, var5, var6, this.colors, 0, var5);
      } catch (IOException var9) {
         var9.printStackTrace();
      }

   }

   private static void dbg(String var0) {
      Config.dbg("CustomColors: " + var0);
   }

   private static void warn(String var0) {
      Config.warn("CustomColors: " + var0);
   }

   private static String parseTexture(String var0, String var1, String var2) {
      String var3;
      if (var0 != null) {
         var3 = ".png";
         if (var0.endsWith(var3)) {
            var0 = var0.substring(0, var0.length() - var3.length());
         }

         var0 = fixTextureName(var0, var2);
         return var0;
      } else {
         var3 = var1;
         int var4 = var1.lastIndexOf(47);
         if (var4 >= 0) {
            var3 = var1.substring(var4 + 1);
         }

         int var5 = var3.lastIndexOf(46);
         if (var5 >= 0) {
            var3 = var3.substring(0, var5);
         }

         var3 = fixTextureName(var3, var2);
         return var3;
      }
   }

   private static String fixTextureName(String var0, String var1) {
      var0 = TextureUtils.fixResourcePath(var0, var1);
      if (!var0.startsWith(var1) && !var0.startsWith("textures/") && !var0.startsWith("mcpatcher/")) {
         var0 = var1 + "/" + var0;
      }

      if (var0.endsWith(".png")) {
         var0 = var0.substring(0, var0.length() - 4);
      }

      String var2 = "textures/blocks/";
      if (var0.startsWith(var2)) {
         var0 = var0.substring(var2.length());
      }

      if (var0.startsWith("/")) {
         var0 = var0.substring(1);
      }

      return var0;
   }

   public boolean matchesBlock(BlockStateBase var1) {
      return Matches.block(var1, this.matchBlocks);
   }

   public int getColorRandom() {
      if (this.format == 2) {
         return this.color;
      } else {
         int var1 = CustomColors.random.nextInt(this.colors.length);
         return this.colors[var1];
      }
   }

   public int getColor(int var1) {
      var1 = Config.limit(var1, 0, this.colors.length - 1);
      return this.colors[var1] & 16777215;
   }

   public int getColor(int var1, int var2) {
      var1 = Config.limit(var1, 0, this.width - 1);
      var2 = Config.limit(var2, 0, this.height - 1);
      return this.colors[var2 * this.width + var1] & 16777215;
   }

   public float[][] getColorsRgb() {
      if (this.colorsRgb == null) {
         this.colorsRgb = toRgb(this.colors);
      }

      return this.colorsRgb;
   }

   public int getColor(IBlockAccess var1, BlockPos var2) {
      BiomeGenBase var3 = CustomColors.getColorBiome(var1, var2);
      return this.getColor(var3, var2);
   }

   public boolean isColorConstant() {
      return this.format == 2;
   }

   public int getColor(BiomeGenBase var1, BlockPos var2) {
      return this.format == 0 ? this.getColorVanilla(var1, var2) : (this.format == 1 ? this.getColorGrid(var1, var2) : this.color);
   }

   public int getColorSmooth(IBlockAccess var1, double var2, double var4, double var6, int var8) {
      if (this.format == 2) {
         return this.color;
      } else {
         int var9 = MathHelper.floor_double(var2);
         int var10 = MathHelper.floor_double(var4);
         int var11 = MathHelper.floor_double(var6);
         int var12 = 0;
         int var13 = 0;
         int var14 = 0;
         int var15 = 0;
         BlockPosM var16 = new BlockPosM(0, 0, 0);

         int var17;
         int var18;
         int var19;
         for(var17 = var9 - var8; var17 <= var9 + var8; ++var17) {
            for(var18 = var11 - var8; var18 <= var11 + var8; ++var18) {
               var16.setXyz(var17, var10, var18);
               var19 = this.getColor((IBlockAccess)var1, var16);
               var12 += var19 >> 16 & 255;
               var13 += var19 >> 8 & 255;
               var14 += var19 & 255;
               ++var15;
            }
         }

         var17 = var12 / var15;
         var18 = var13 / var15;
         var19 = var14 / var15;
         return var17 << 16 | var18 << 8 | var19;
      }
   }

   private int getColorVanilla(BiomeGenBase var1, BlockPos var2) {
      double var3 = (double)MathHelper.clamp_float(var1.getFloatTemperature(var2), 0.0F, 1.0F);
      double var5 = (double)MathHelper.clamp_float(var1.getFloatRainfall(), 0.0F, 1.0F);
      var5 *= var3;
      int var7 = (int)((1.0D - var3) * (double)(this.width - 1));
      int var8 = (int)((1.0D - var5) * (double)(this.height - 1));
      return this.getColor(var7, var8);
   }

   private int getColorGrid(BiomeGenBase var1, BlockPos var2) {
      int var3 = var1.biomeID;
      int var4 = var2.getY() - this.yOffset;
      if (this.yVariance > 0) {
         int var5 = var2.getX() << 16 + var2.getZ();
         int var6 = Config.intHash(var5);
         int var7 = this.yVariance * 2 + 1;
         int var8 = (var6 & 255) % var7 - this.yVariance;
         var4 += var8;
      }

      return this.getColor(var3, var4);
   }

   public int getLength() {
      return this.format == 2 ? 1 : this.colors.length;
   }

   public int getWidth() {
      return this.width;
   }

   public int getHeight() {
      return this.height;
   }

   private static float[][] toRgb(int[] var0) {
      float[][] var1 = new float[var0.length][3];

      for(int var2 = 0; var2 < var0.length; ++var2) {
         int var3 = var0[var2];
         float var4 = (float)(var3 >> 16 & 255) / 255.0F;
         float var5 = (float)(var3 >> 8 & 255) / 255.0F;
         float var6 = (float)(var3 & 255) / 255.0F;
         float[] var7 = var1[var2];
         var7[0] = var4;
         var7[1] = var5;
         var7[2] = var6;
      }

      return var1;
   }

   public void addMatchBlock(MatchBlock var1) {
      if (this.matchBlocks == null) {
         this.matchBlocks = new MatchBlock[0];
      }

      this.matchBlocks = (MatchBlock[])Config.addObjectToArray(this.matchBlocks, var1);
   }

   public void addMatchBlock(int var1, int var2) {
      MatchBlock var3 = this.getMatchBlock(var1);
      if (var3 != null) {
         if (var2 >= 0) {
            var3.addMetadata(var2);
         }
      } else {
         this.addMatchBlock(new MatchBlock(var1, var2));
      }

   }

   private MatchBlock getMatchBlock(int var1) {
      if (this.matchBlocks == null) {
         return null;
      } else {
         for(int var2 = 0; var2 < this.matchBlocks.length; ++var2) {
            MatchBlock var3 = this.matchBlocks[var2];
            if (var3.getBlockId() == var1) {
               return var3;
            }
         }

         return null;
      }
   }

   public int[] getMatchBlockIds() {
      if (this.matchBlocks == null) {
         return null;
      } else {
         HashSet var1 = new HashSet();

         for(int var2 = 0; var2 < this.matchBlocks.length; ++var2) {
            MatchBlock var3 = this.matchBlocks[var2];
            if (var3.getBlockId() >= 0) {
               var1.add(var3.getBlockId());
            }
         }

         Integer[] var5 = (Integer[])var1.toArray(new Integer[var1.size()]);
         int[] var6 = new int[var5.length];

         for(int var4 = 0; var4 < var5.length; ++var4) {
            var6[var4] = var5[var4];
         }

         return var6;
      }
   }

   public String toString() {
      return this.basePath + "/" + this.name + ", blocks: " + Config.arrayToString((Object[])this.matchBlocks) + ", source: " + this.source;
   }
}
