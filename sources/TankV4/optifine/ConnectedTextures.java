package optifine;

import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.IdentityHashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import net.minecraft.block.Block;
import net.minecraft.block.BlockPane;
import net.minecraft.block.BlockQuartz;
import net.minecraft.block.BlockRotatedPillar;
import net.minecraft.block.state.BlockStateBase;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.block.model.BakedQuad;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.client.resources.IResourcePack;
import net.minecraft.client.resources.model.IBakedModel;
import net.minecraft.init.Blocks;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.biome.BiomeGenBase;

public class ConnectedTextures {
   private static Map[] spriteQuadMaps = null;
   private static ConnectedProperties[][] blockProperties = null;
   private static ConnectedProperties[][] tileProperties = null;
   private static boolean multipass = false;
   private static final int Y_NEG_DOWN = 0;
   private static final int Y_POS_UP = 1;
   private static final int Z_NEG_NORTH = 2;
   private static final int Z_POS_SOUTH = 3;
   private static final int X_NEG_WEST = 4;
   private static final int X_POS_EAST = 5;
   private static final int Y_AXIS = 0;
   private static final int Z_AXIS = 1;
   private static final int X_AXIS = 2;
   private static final String[] propSuffixes = new String[]{"", "a", "b", "c", "d", "e", "f", "g", "h", "i", "j", "k", "l", "m", "n", "o", "p", "q", "r", "s", "t", "u", "v", "w", "x", "y", "z"};
   private static final int[] ctmIndexes = new int[]{0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 0, 0, 0, 0, 12, 13, 14, 15, 16, 17, 18, 19, 20, 21, 22, 23, 0, 0, 0, 0, 24, 25, 26, 27, 28, 29, 30, 31, 32, 33, 34, 35, 0, 0, 0, 0, 36, 37, 38, 39, 40, 41, 42, 43, 44, 45, 46, 0, 0, 0, 0, 0};
   public static final IBlockState AIR_DEFAULT_STATE;
   private static TextureAtlasSprite emptySprite;
   private static volatile int[] $SWITCH_TABLE$net$minecraft$util$EnumFacing;

   static {
      AIR_DEFAULT_STATE = Blocks.air.getDefaultState();
      emptySprite = null;
   }

   public static synchronized BakedQuad getConnectedTexture(IBlockAccess var0, IBlockState var1, BlockPos var2, BakedQuad var3, RenderEnv var4) {
      TextureAtlasSprite var5 = var3.getSprite();
      if (var5 == null) {
         return var3;
      } else {
         Block var6 = var1.getBlock();
         EnumFacing var7 = var3.getFace();
         if (var6 instanceof BlockPane && var5.getIconName().startsWith("minecraft:blocks/glass_pane_top")) {
            IBlockState var8 = var0.getBlockState(var2.offset(var3.getFace()));
            if (var8 == var1) {
               return getQuad(emptySprite, var6, var1, var3);
            }
         }

         TextureAtlasSprite var9 = getConnectedTextureMultiPass(var0, var1, var2, var7, var5, var4);
         return var9 == var5 ? var3 : getQuad(var9, var6, var1, var3);
      }
   }

   private static BakedQuad getQuad(TextureAtlasSprite var0, Block var1, IBlockState var2, BakedQuad var3) {
      if (spriteQuadMaps == null) {
         return var3;
      } else {
         int var4 = var0.getIndexInMap();
         if (var4 >= 0 && var4 < spriteQuadMaps.length) {
            Object var5 = spriteQuadMaps[var4];
            if (var5 == null) {
               var5 = new IdentityHashMap(1);
               spriteQuadMaps[var4] = (Map)var5;
            }

            BakedQuad var6 = (BakedQuad)((Map)var5).get(var3);
            if (var6 == null) {
               var6 = makeSpriteQuad(var3, var0);
               ((Map)var5).put(var3, var6);
            }

            return var6;
         } else {
            return var3;
         }
      }
   }

   private static BakedQuad makeSpriteQuad(BakedQuad var0, TextureAtlasSprite var1) {
      int[] var2 = (int[])var0.getVertexData().clone();
      TextureAtlasSprite var3 = var0.getSprite();

      for(int var4 = 0; var4 < 4; ++var4) {
         fixVertex(var2, var4, var3, var1);
      }

      BakedQuad var5 = new BakedQuad(var2, var0.getTintIndex(), var0.getFace(), var1);
      return var5;
   }

   private static void fixVertex(int[] var0, int var1, TextureAtlasSprite var2, TextureAtlasSprite var3) {
      int var4 = var0.length / 4;
      int var5 = var4 * var1;
      float var6 = Float.intBitsToFloat(var0[var5 + 4]);
      float var7 = Float.intBitsToFloat(var0[var5 + 4 + 1]);
      double var8 = var2.getSpriteU16(var6);
      double var10 = var2.getSpriteV16(var7);
      var0[var5 + 4] = Float.floatToRawIntBits(var3.getInterpolatedU(var8));
      var0[var5 + 4 + 1] = Float.floatToRawIntBits(var3.getInterpolatedV(var10));
   }

   private static TextureAtlasSprite getConnectedTextureMultiPass(IBlockAccess var0, IBlockState var1, BlockPos var2, EnumFacing var3, TextureAtlasSprite var4, RenderEnv var5) {
      TextureAtlasSprite var6 = getConnectedTextureSingle(var0, var1, var2, var3, var4, true, var5);
      if (!multipass) {
         return var6;
      } else if (var6 == var4) {
         return var6;
      } else {
         TextureAtlasSprite var7 = var6;

         for(int var8 = 0; var8 < 3; ++var8) {
            TextureAtlasSprite var9 = getConnectedTextureSingle(var0, var1, var2, var3, var7, false, var5);
            if (var9 == var7) {
               break;
            }

            var7 = var9;
         }

         return var7;
      }
   }

   public static TextureAtlasSprite getConnectedTextureSingle(IBlockAccess var0, IBlockState var1, BlockPos var2, EnumFacing var3, TextureAtlasSprite var4, boolean var5, RenderEnv var6) {
      Block var7 = var1.getBlock();
      if (!(var1 instanceof BlockStateBase)) {
         return var4;
      } else {
         BlockStateBase var8 = (BlockStateBase)var1;
         int var9;
         ConnectedProperties[] var10;
         int var11;
         int var12;
         ConnectedProperties var13;
         TextureAtlasSprite var14;
         if (tileProperties != null) {
            var9 = var4.getIndexInMap();
            if (var9 >= 0 && var9 < tileProperties.length) {
               var10 = tileProperties[var9];
               if (var10 != null) {
                  var11 = getSide(var3);

                  for(var12 = 0; var12 < var10.length; ++var12) {
                     var13 = var10[var12];
                     if (var13 != null && var13.matchesBlockId(var8.getBlockId())) {
                        var14 = getConnectedTexture(var13, var0, var8, var2, var11, var4, var6);
                        if (var14 != null) {
                           return var14;
                        }
                     }
                  }
               }
            }
         }

         if (blockProperties != null && var5) {
            var9 = var6.getBlockId();
            if (var9 >= 0 && var9 < blockProperties.length) {
               var10 = blockProperties[var9];
               if (var10 != null) {
                  var11 = getSide(var3);

                  for(var12 = 0; var12 < var10.length; ++var12) {
                     var13 = var10[var12];
                     if (var13 != null && var13.matchesIcon(var4)) {
                        var14 = getConnectedTexture(var13, var0, var8, var2, var11, var4, var6);
                        if (var14 != null) {
                           return var14;
                        }
                     }
                  }
               }
            }
         }

         return var4;
      }
   }

   public static int getSide(EnumFacing var0) {
      if (var0 == null) {
         return -1;
      } else {
         switch($SWITCH_TABLE$net$minecraft$util$EnumFacing()[var0.ordinal()]) {
         case 1:
            return 0;
         case 2:
            return 1;
         case 3:
            return 2;
         case 4:
            return 3;
         case 5:
            return 4;
         case 6:
            return 5;
         default:
            return -1;
         }
      }
   }

   private static EnumFacing getFacing(int var0) {
      switch(var0) {
      case 0:
         return EnumFacing.DOWN;
      case 1:
         return EnumFacing.UP;
      case 2:
         return EnumFacing.NORTH;
      case 3:
         return EnumFacing.SOUTH;
      case 4:
         return EnumFacing.WEST;
      case 5:
         return EnumFacing.EAST;
      default:
         return EnumFacing.UP;
      }
   }

   private static TextureAtlasSprite getConnectedTexture(ConnectedProperties var0, IBlockAccess var1, BlockStateBase var2, BlockPos var3, int var4, TextureAtlasSprite var5, RenderEnv var6) {
      int var7 = 0;
      int var8 = var2.getMetadata();
      int var9 = var8;
      Block var10 = var2.getBlock();
      if (var10 instanceof BlockRotatedPillar) {
         var7 = getWoodAxis(var4, var8);
         if (var0.getMetadataMax() <= 3) {
            var9 = var8 & 3;
         }
      }

      if (var10 instanceof BlockQuartz) {
         var7 = getQuartzAxis(var4, var8);
         if (var0.getMetadataMax() <= 2 && var9 > 2) {
            var9 = 2;
         }
      }

      if (!var0.matchesBlock(var2.getBlockId(), var9)) {
         return null;
      } else {
         int var11;
         if (var4 >= 0 && var0.faces != 63) {
            var11 = var4;
            if (var7 != 0) {
               var11 = fixSideByAxis(var4, var7);
            }

            if ((1 << var11 & var0.faces) == 0) {
               return null;
            }
         }

         var11 = var3.getY();
         if (var11 >= var0.minHeight && var11 <= var0.maxHeight) {
            if (var0.biomes != null) {
               BiomeGenBase var12 = var1.getBiomeGenForCoords(var3);
               if (!var0.matchesBiome(var12)) {
                  return null;
               }
            }

            switch(var0.method) {
            case 1:
               return getConnectedTextureCtm(var0, var1, var2, var3, var7, var4, var5, var8, var6);
            case 2:
               return getConnectedTextureHorizontal(var0, var1, var2, var3, var7, var4, var5, var8);
            case 3:
               return getConnectedTextureTop(var0, var1, var2, var3, var7, var4, var5, var8);
            case 4:
               return getConnectedTextureRandom(var0, var3, var4);
            case 5:
               return getConnectedTextureRepeat(var0, var3, var4);
            case 6:
               return getConnectedTextureVertical(var0, var1, var2, var3, var7, var4, var5, var8);
            case 7:
               return getConnectedTextureFixed(var0);
            case 8:
               return getConnectedTextureHorizontalVertical(var0, var1, var2, var3, var7, var4, var5, var8);
            case 9:
               return getConnectedTextureVerticalHorizontal(var0, var1, var2, var3, var7, var4, var5, var8);
            default:
               return null;
            }
         } else {
            return null;
         }
      }
   }

   private static int fixSideByAxis(int var0, int var1) {
      switch(var1) {
      case 0:
         return var0;
      case 1:
         switch(var0) {
         case 0:
            return 2;
         case 1:
            return 3;
         case 2:
            return 1;
         case 3:
            return 0;
         default:
            return var0;
         }
      case 2:
         switch(var0) {
         case 0:
            return 4;
         case 1:
            return 5;
         case 2:
         case 3:
         default:
            return var0;
         case 4:
            return 1;
         case 5:
            return 0;
         }
      default:
         return var0;
      }
   }

   private static int getWoodAxis(int var0, int var1) {
      int var2 = (var1 & 12) >> 2;
      switch(var2) {
      case 1:
         return 2;
      case 2:
         return 1;
      default:
         return 0;
      }
   }

   private static int getQuartzAxis(int var0, int var1) {
      switch(var1) {
      case 3:
         return 2;
      case 4:
         return 1;
      default:
         return 0;
      }
   }

   private static TextureAtlasSprite getConnectedTextureRandom(ConnectedProperties var0, BlockPos var1, int var2) {
      if (var0.tileIcons.length == 1) {
         return var0.tileIcons[0];
      } else {
         int var3 = var2 / var0.symmetry * var0.symmetry;
         int var4 = Config.getRandom(var1, var3) & Integer.MAX_VALUE;
         int var5 = 0;
         if (var0.weights == null) {
            var5 = var4 % var0.tileIcons.length;
         } else {
            int var6 = var4 % var0.sumAllWeights;
            int[] var7 = var0.sumWeights;

            for(int var8 = 0; var8 < var7.length; ++var8) {
               if (var6 < var7[var8]) {
                  var5 = var8;
                  break;
               }
            }
         }

         return var0.tileIcons[var5];
      }
   }

   private static TextureAtlasSprite getConnectedTextureFixed(ConnectedProperties var0) {
      return var0.tileIcons[0];
   }

   private static TextureAtlasSprite getConnectedTextureRepeat(ConnectedProperties var0, BlockPos var1, int var2) {
      if (var0.tileIcons.length == 1) {
         return var0.tileIcons[0];
      } else {
         int var3 = var1.getX();
         int var4 = var1.getY();
         int var5 = var1.getZ();
         int var6 = 0;
         int var7 = 0;
         switch(var2) {
         case 0:
            var6 = var3;
            var7 = var5;
            break;
         case 1:
            var6 = var3;
            var7 = var5;
            break;
         case 2:
            var6 = -var3 - 1;
            var7 = -var4;
            break;
         case 3:
            var6 = var3;
            var7 = -var4;
            break;
         case 4:
            var6 = var5;
            var7 = -var4;
            break;
         case 5:
            var6 = -var5 - 1;
            var7 = -var4;
         }

         var6 %= var0.width;
         var7 %= var0.height;
         if (var6 < 0) {
            var6 += var0.width;
         }

         if (var7 < 0) {
            var7 += var0.height;
         }

         int var8 = var7 * var0.width + var6;
         return var0.tileIcons[var8];
      }
   }

   private static TextureAtlasSprite getConnectedTextureCtm(ConnectedProperties var0, IBlockAccess var1, IBlockState var2, BlockPos var3, int var4, int var5, TextureAtlasSprite var6, int var7, RenderEnv var8) {
      boolean[] var9 = var8.getBorderFlags();
      switch(var5) {
      case 0:
         var9[0] = isNeighbour(var0, var1, var2, var3.west(), var5, var6, var7);
         var9[1] = isNeighbour(var0, var1, var2, var3.east(), var5, var6, var7);
         var9[2] = isNeighbour(var0, var1, var2, var3.north(), var5, var6, var7);
         var9[3] = isNeighbour(var0, var1, var2, var3.south(), var5, var6, var7);
         break;
      case 1:
         var9[0] = isNeighbour(var0, var1, var2, var3.west(), var5, var6, var7);
         var9[1] = isNeighbour(var0, var1, var2, var3.east(), var5, var6, var7);
         var9[2] = isNeighbour(var0, var1, var2, var3.south(), var5, var6, var7);
         var9[3] = isNeighbour(var0, var1, var2, var3.north(), var5, var6, var7);
         break;
      case 2:
         var9[0] = isNeighbour(var0, var1, var2, var3.east(), var5, var6, var7);
         var9[1] = isNeighbour(var0, var1, var2, var3.west(), var5, var6, var7);
         var9[2] = isNeighbour(var0, var1, var2, var3.down(), var5, var6, var7);
         var9[3] = isNeighbour(var0, var1, var2, var3.up(), var5, var6, var7);
         break;
      case 3:
         var9[0] = isNeighbour(var0, var1, var2, var3.west(), var5, var6, var7);
         var9[1] = isNeighbour(var0, var1, var2, var3.east(), var5, var6, var7);
         var9[2] = isNeighbour(var0, var1, var2, var3.down(), var5, var6, var7);
         var9[3] = isNeighbour(var0, var1, var2, var3.up(), var5, var6, var7);
         break;
      case 4:
         var9[0] = isNeighbour(var0, var1, var2, var3.north(), var5, var6, var7);
         var9[1] = isNeighbour(var0, var1, var2, var3.south(), var5, var6, var7);
         var9[2] = isNeighbour(var0, var1, var2, var3.down(), var5, var6, var7);
         var9[3] = isNeighbour(var0, var1, var2, var3.up(), var5, var6, var7);
         break;
      case 5:
         var9[0] = isNeighbour(var0, var1, var2, var3.south(), var5, var6, var7);
         var9[1] = isNeighbour(var0, var1, var2, var3.north(), var5, var6, var7);
         var9[2] = isNeighbour(var0, var1, var2, var3.down(), var5, var6, var7);
         var9[3] = isNeighbour(var0, var1, var2, var3.up(), var5, var6, var7);
      }

      byte var10 = 0;
      if (var9[0] & !var9[1] & !var9[2] & !var9[3]) {
         var10 = 3;
      } else if (!var9[0] & var9[1] & !var9[2] & !var9[3]) {
         var10 = 1;
      } else if (!var9[0] & !var9[1] & var9[2] & !var9[3]) {
         var10 = 12;
      } else if (!var9[0] & !var9[1] & !var9[2] & var9[3]) {
         var10 = 36;
      } else if (var9[0] & var9[1] & !var9[2] & !var9[3]) {
         var10 = 2;
      } else if (!var9[0] & !var9[1] & var9[2] & var9[3]) {
         var10 = 24;
      } else if (var9[0] & !var9[1] & var9[2] & !var9[3]) {
         var10 = 15;
      } else if (var9[0] & !var9[1] & !var9[2] & var9[3]) {
         var10 = 39;
      } else if (!var9[0] & var9[1] & var9[2] & !var9[3]) {
         var10 = 13;
      } else if (!var9[0] & var9[1] & !var9[2] & var9[3]) {
         var10 = 37;
      } else if (!var9[0] & var9[1] & var9[2] & var9[3]) {
         var10 = 25;
      } else if (var9[0] & !var9[1] & var9[2] & var9[3]) {
         var10 = 27;
      } else if (var9[0] & var9[1] & !var9[2] & var9[3]) {
         var10 = 38;
      } else if (var9[0] & var9[1] & var9[2] & !var9[3]) {
         var10 = 14;
      } else if (var9[0] & var9[1] & var9[2] & var9[3]) {
         var10 = 26;
      }

      if (var10 == 0) {
         return var0.tileIcons[var10];
      } else if (!Config.isConnectedTexturesFancy()) {
         return var0.tileIcons[var10];
      } else {
         boolean var10016;
         boolean var10001;
         boolean var10006;
         boolean var10011;
         switch(var5) {
         case 0:
            var10001 = false;
            ((Object[])var3.east().north())[var5] = var6 != var7;
            var10006 = true;
            ((Object[])var3.west().north())[var5] = var6 != var7;
            var10011 = true;
            ((Object[])var3.east().south())[var5] = var6 != var7;
            var10016 = true;
            ((Object[])var3.west().south())[var5] = var6 != var7;
            break;
         case 1:
            var10001 = false;
            ((Object[])var3.east().south())[var5] = var6 != var7;
            var10006 = true;
            ((Object[])var3.west().south())[var5] = var6 != var7;
            var10011 = true;
            ((Object[])var3.east().north())[var5] = var6 != var7;
            var10016 = true;
            ((Object[])var3.west().north())[var5] = var6 != var7;
            break;
         case 2:
            var10001 = false;
            ((Object[])var3.west().down())[var5] = var6 != var7;
            var10006 = true;
            ((Object[])var3.east().down())[var5] = var6 != var7;
            var10011 = true;
            ((Object[])var3.west().up())[var5] = var6 != var7;
            var10016 = true;
            ((Object[])var3.east().up())[var5] = var6 != var7;
            break;
         case 3:
            var10001 = false;
            ((Object[])var3.east().down())[var5] = var6 != var7;
            var10006 = true;
            ((Object[])var3.west().down())[var5] = var6 != var7;
            var10011 = true;
            ((Object[])var3.east().up())[var5] = var6 != var7;
            var10016 = true;
            ((Object[])var3.west().up())[var5] = var6 != var7;
            break;
         case 4:
            var10001 = false;
            ((Object[])var3.down().south())[var5] = var6 != var7;
            var10006 = true;
            ((Object[])var3.down().north())[var5] = var6 != var7;
            var10011 = true;
            ((Object[])var3.up().south())[var5] = var6 != var7;
            var10016 = true;
            ((Object[])var3.up().north())[var5] = var6 != var7;
            break;
         case 5:
            var10001 = false;
            ((Object[])var3.down().north())[var5] = var6 != var7;
            var10006 = true;
            ((Object[])var3.down().south())[var5] = var6 != var7;
            var10011 = true;
            ((Object[])var3.up().north())[var5] = var6 != var7;
            var10016 = true;
            ((Object[])var3.up().south())[var5] = var6 != var7;
         }

         if (var10 == 13 && var9[0]) {
            var10 = 4;
         } else if (var10 == 15 && var9[1]) {
            var10 = 5;
         } else if (var10 == 37 && var9[2]) {
            var10 = 16;
         } else if (var10 == 39 && var9[3]) {
            var10 = 17;
         } else if (var10 == 14 && var9[0] && var9[1]) {
            var10 = 7;
         } else if (var10 == 25 && var9[0] && var9[2]) {
            var10 = 6;
         } else if (var10 == 27 && var9[3] && var9[1]) {
            var10 = 19;
         } else if (var10 == 38 && var9[3] && var9[2]) {
            var10 = 18;
         } else if (var10 == 14 && !var9[0] && var9[1]) {
            var10 = 31;
         } else if (var10 == 25 && var9[0] && !var9[2]) {
            var10 = 30;
         } else if (var10 == 27 && !var9[3] && var9[1]) {
            var10 = 41;
         } else if (var10 == 38 && var9[3] && !var9[2]) {
            var10 = 40;
         } else if (var10 == 14 && var9[0] && !var9[1]) {
            var10 = 29;
         } else if (var10 == 25 && !var9[0] && var9[2]) {
            var10 = 28;
         } else if (var10 == 27 && var9[3] && !var9[1]) {
            var10 = 43;
         } else if (var10 == 38 && !var9[3] && var9[2]) {
            var10 = 42;
         } else if (var10 == 26 && var9[0] && var9[1] && var9[2] && var9[3]) {
            var10 = 46;
         } else if (var10 == 26 && !var9[0] && var9[1] && var9[2] && var9[3]) {
            var10 = 9;
         } else if (var10 == 26 && var9[0] && !var9[1] && var9[2] && var9[3]) {
            var10 = 21;
         } else if (var10 == 26 && var9[0] && var9[1] && !var9[2] && var9[3]) {
            var10 = 8;
         } else if (var10 == 26 && var9[0] && var9[1] && var9[2] && !var9[3]) {
            var10 = 20;
         } else if (var10 == 26 && var9[0] && var9[1] && !var9[2] && !var9[3]) {
            var10 = 11;
         } else if (var10 == 26 && !var9[0] && !var9[1] && var9[2] && var9[3]) {
            var10 = 22;
         } else if (var10 == 26 && !var9[0] && var9[1] && !var9[2] && var9[3]) {
            var10 = 23;
         } else if (var10 == 26 && var9[0] && !var9[1] && var9[2] && !var9[3]) {
            var10 = 10;
         } else if (var10 == 26 && var9[0] && !var9[1] && !var9[2] && var9[3]) {
            var10 = 34;
         } else if (var10 == 26 && !var9[0] && var9[1] && var9[2] && !var9[3]) {
            var10 = 35;
         } else if (var10 == 26 && var9[0] && !var9[1] && !var9[2] && !var9[3]) {
            var10 = 32;
         } else if (var10 == 26 && !var9[0] && var9[1] && !var9[2] && !var9[3]) {
            var10 = 33;
         } else if (var10 == 26 && !var9[0] && !var9[1] && var9[2] && !var9[3]) {
            var10 = 44;
         } else if (var10 == 26 && !var9[0] && !var9[1] && !var9[2] && var9[3]) {
            var10 = 45;
         }

         return var0.tileIcons[var10];
      }
   }

   private static TextureAtlasSprite getNeighbourIcon(IBlockAccess var0, BlockPos var1, IBlockState var2, int var3) {
      var2 = var2.getBlock().getActualState(var2, var0, var1);
      IBakedModel var4 = Minecraft.getMinecraft().getBlockRendererDispatcher().getBlockModelShapes().getModelForState(var2);
      if (var4 == null) {
         return null;
      } else {
         EnumFacing var5 = getFacing(var3);
         List var6 = var4.getFaceQuads(var5);
         if (var6.size() > 0) {
            BakedQuad var10 = (BakedQuad)var6.get(0);
            return var10.getSprite();
         } else {
            List var7 = var4.getGeneralQuads();

            for(int var8 = 0; var8 < var7.size(); ++var8) {
               BakedQuad var9 = (BakedQuad)var7.get(var8);
               if (var9.getFace() == var5) {
                  return var9.getSprite();
               }
            }

            return null;
         }
      }
   }

   private static TextureAtlasSprite getConnectedTextureHorizontal(ConnectedProperties var0, IBlockAccess var1, IBlockState var2, BlockPos var3, int var4, int var5, TextureAtlasSprite var6, int var7) {
      boolean var8;
      boolean var9;
      var8 = false;
      var9 = false;
      label46:
      switch(var4) {
      case 0:
         switch(var5) {
         case 0:
         case 1:
            return null;
         case 2:
            var8 = isNeighbour(var0, var1, var2, var3.east(), var5, var6, var7);
            var9 = isNeighbour(var0, var1, var2, var3.west(), var5, var6, var7);
            break label46;
         case 3:
            var8 = isNeighbour(var0, var1, var2, var3.west(), var5, var6, var7);
            var9 = isNeighbour(var0, var1, var2, var3.east(), var5, var6, var7);
            break label46;
         case 4:
            var8 = isNeighbour(var0, var1, var2, var3.north(), var5, var6, var7);
            var9 = isNeighbour(var0, var1, var2, var3.south(), var5, var6, var7);
            break label46;
         case 5:
            var8 = isNeighbour(var0, var1, var2, var3.south(), var5, var6, var7);
            var9 = isNeighbour(var0, var1, var2, var3.north(), var5, var6, var7);
         default:
            break label46;
         }
      case 1:
         switch(var5) {
         case 0:
            var8 = isNeighbour(var0, var1, var2, var3.west(), var5, var6, var7);
            var9 = isNeighbour(var0, var1, var2, var3.east(), var5, var6, var7);
            break label46;
         case 1:
            var8 = isNeighbour(var0, var1, var2, var3.west(), var5, var6, var7);
            var9 = isNeighbour(var0, var1, var2, var3.east(), var5, var6, var7);
            break label46;
         case 2:
         case 3:
            return null;
         case 4:
            var8 = isNeighbour(var0, var1, var2, var3.down(), var5, var6, var7);
            var9 = isNeighbour(var0, var1, var2, var3.up(), var5, var6, var7);
            break label46;
         case 5:
            var8 = isNeighbour(var0, var1, var2, var3.up(), var5, var6, var7);
            var9 = isNeighbour(var0, var1, var2, var3.down(), var5, var6, var7);
         default:
            break label46;
         }
      case 2:
         switch(var5) {
         case 0:
            var8 = isNeighbour(var0, var1, var2, var3.north(), var5, var6, var7);
            var9 = isNeighbour(var0, var1, var2, var3.south(), var5, var6, var7);
            break;
         case 1:
            var8 = isNeighbour(var0, var1, var2, var3.north(), var5, var6, var7);
            var9 = isNeighbour(var0, var1, var2, var3.south(), var5, var6, var7);
            break;
         case 2:
            var8 = isNeighbour(var0, var1, var2, var3.down(), var5, var6, var7);
            var9 = isNeighbour(var0, var1, var2, var3.up(), var5, var6, var7);
            break;
         case 3:
            var8 = isNeighbour(var0, var1, var2, var3.up(), var5, var6, var7);
            var9 = isNeighbour(var0, var1, var2, var3.down(), var5, var6, var7);
            break;
         case 4:
         case 5:
            return null;
         }
      }

      boolean var10 = true;
      byte var11;
      if (var8) {
         if (var9) {
            var11 = 1;
         } else {
            var11 = 2;
         }
      } else if (var9) {
         var11 = 0;
      } else {
         var11 = 3;
      }

      return var0.tileIcons[var11];
   }

   private static TextureAtlasSprite getConnectedTextureVertical(ConnectedProperties var0, IBlockAccess var1, IBlockState var2, BlockPos var3, int var4, int var5, TextureAtlasSprite var6, int var7) {
      boolean var8 = false;
      boolean var9 = false;
      switch(var4) {
      case 0:
         if (var5 == 1 || var5 == 0) {
            return null;
         }

         var8 = isNeighbour(var0, var1, var2, var3.down(), var5, var6, var7);
         var9 = isNeighbour(var0, var1, var2, var3.up(), var5, var6, var7);
         break;
      case 1:
         if (var5 == 3 || var5 == 2) {
            return null;
         }

         var8 = isNeighbour(var0, var1, var2, var3.south(), var5, var6, var7);
         var9 = isNeighbour(var0, var1, var2, var3.north(), var5, var6, var7);
         break;
      case 2:
         if (var5 == 5 || var5 == 4) {
            return null;
         }

         var8 = isNeighbour(var0, var1, var2, var3.west(), var5, var6, var7);
         var9 = isNeighbour(var0, var1, var2, var3.east(), var5, var6, var7);
      }

      boolean var10 = true;
      byte var11;
      if (var8) {
         if (var9) {
            var11 = 1;
         } else {
            var11 = 2;
         }
      } else if (var9) {
         var11 = 0;
      } else {
         var11 = 3;
      }

      return var0.tileIcons[var11];
   }

   private static TextureAtlasSprite getConnectedTextureHorizontalVertical(ConnectedProperties var0, IBlockAccess var1, IBlockState var2, BlockPos var3, int var4, int var5, TextureAtlasSprite var6, int var7) {
      TextureAtlasSprite[] var8 = var0.tileIcons;
      TextureAtlasSprite var9 = getConnectedTextureHorizontal(var0, var1, var2, var3, var4, var5, var6, var7);
      if (var9 != null && var9 != var6 && var9 != var8[3]) {
         return var9;
      } else {
         TextureAtlasSprite var10 = getConnectedTextureVertical(var0, var1, var2, var3, var4, var5, var6, var7);
         return var10 == var8[0] ? var8[4] : (var10 == var8[1] ? var8[5] : (var10 == var8[2] ? var8[6] : var10));
      }
   }

   private static TextureAtlasSprite getConnectedTextureVerticalHorizontal(ConnectedProperties var0, IBlockAccess var1, IBlockState var2, BlockPos var3, int var4, int var5, TextureAtlasSprite var6, int var7) {
      TextureAtlasSprite[] var8 = var0.tileIcons;
      TextureAtlasSprite var9 = getConnectedTextureVertical(var0, var1, var2, var3, var4, var5, var6, var7);
      if (var9 != null && var9 != var6 && var9 != var8[3]) {
         return var9;
      } else {
         TextureAtlasSprite var10 = getConnectedTextureHorizontal(var0, var1, var2, var3, var4, var5, var6, var7);
         return var10 == var8[0] ? var8[4] : (var10 == var8[1] ? var8[5] : (var10 == var8[2] ? var8[6] : var10));
      }
   }

   private static TextureAtlasSprite getConnectedTextureTop(ConnectedProperties var0, IBlockAccess var1, IBlockState var2, BlockPos var3, int var4, int var5, TextureAtlasSprite var6, int var7) {
      boolean var8 = false;
      switch(var4) {
      case 0:
         if (var5 == 1 || var5 == 0) {
            return null;
         }

         var8 = isNeighbour(var0, var1, var2, var3.up(), var5, var6, var7);
         break;
      case 1:
         if (var5 != 3 && var5 != 2) {
            var8 = isNeighbour(var0, var1, var2, var3.south(), var5, var6, var7);
            break;
         }

         return null;
      case 2:
         if (var5 == 5 || var5 == 4) {
            return null;
         }

         var8 = isNeighbour(var0, var1, var2, var3.east(), var5, var6, var7);
      }

      if (var8) {
         return var0.tileIcons[0];
      } else {
         return null;
      }
   }

   public static void updateIcons(TextureMap var0) {
      blockProperties = null;
      tileProperties = null;
      spriteQuadMaps = null;
      if (Config.isConnectedTextures()) {
         IResourcePack[] var1 = Config.getResourcePacks();

         for(int var2 = var1.length - 1; var2 >= 0; --var2) {
            IResourcePack var3 = var1[var2];
            updateIcons(var0, var3);
         }

         updateIcons(var0, Config.getDefaultResourcePack());
         ResourceLocation var4 = new ResourceLocation("mcpatcher/ctm/default/empty");
         emptySprite = var0.registerSprite(var4);
         spriteQuadMaps = new Map[var0.getCountRegisteredSprites() + 1];
         if (blockProperties.length <= 0) {
            blockProperties = null;
         }

         if (tileProperties.length <= 0) {
            tileProperties = null;
         }
      }

   }

   private static void updateIconEmpty(TextureMap var0) {
   }

   public static void updateIcons(TextureMap var0, IResourcePack var1) {
      String[] var2 = ResUtils.collectFiles(var1, "mcpatcher/ctm/", ".properties", getDefaultCtmPaths());
      Arrays.sort(var2);
      List var3 = makePropertyList(tileProperties);
      List var4 = makePropertyList(blockProperties);

      for(int var5 = 0; var5 < var2.length; ++var5) {
         String var6 = var2[var5];
         Config.dbg("ConnectedTextures: " + var6);

         try {
            ResourceLocation var7 = new ResourceLocation(var6);
            InputStream var8 = var1.getInputStream(var7);
            if (var8 == null) {
               Config.warn("ConnectedTextures file not found: " + var6);
            } else {
               Properties var9 = new Properties();
               var9.load(var8);
               ConnectedProperties var10 = new ConnectedProperties(var9, var6);
               if (var10.isValid(var6)) {
                  var10.updateIcons(var0);
                  addToTileList(var10, var3);
                  addToBlockList(var10, var4);
               }
            }
         } catch (FileNotFoundException var11) {
            Config.warn("ConnectedTextures file not found: " + var6);
         } catch (Exception var12) {
            var12.printStackTrace();
         }
      }

      blockProperties = propertyListToArray(var4);
      tileProperties = propertyListToArray(var3);
      multipass = detectMultipass();
      Config.dbg("Multipass connected textures: " + multipass);
   }

   private static List makePropertyList(ConnectedProperties[][] var0) {
      ArrayList var1 = new ArrayList();
      if (var0 != null) {
         for(int var2 = 0; var2 < var0.length; ++var2) {
            ConnectedProperties[] var3 = var0[var2];
            ArrayList var4 = null;
            if (var3 != null) {
               var4 = new ArrayList(Arrays.asList(var3));
            }

            var1.add(var4);
         }
      }

      return var1;
   }

   private static boolean detectMultipass() {
      ArrayList var0 = new ArrayList();

      int var1;
      ConnectedProperties[] var2;
      for(var1 = 0; var1 < tileProperties.length; ++var1) {
         var2 = tileProperties[var1];
         if (var2 != null) {
            var0.addAll(Arrays.asList(var2));
         }
      }

      for(var1 = 0; var1 < blockProperties.length; ++var1) {
         var2 = blockProperties[var1];
         if (var2 != null) {
            var0.addAll(Arrays.asList(var2));
         }
      }

      ConnectedProperties[] var6 = (ConnectedProperties[])var0.toArray(new ConnectedProperties[var0.size()]);
      HashSet var7 = new HashSet();
      HashSet var3 = new HashSet();

      for(int var4 = 0; var4 < var6.length; ++var4) {
         ConnectedProperties var5 = var6[var4];
         if (var5.matchTileIcons != null) {
            var7.addAll(Arrays.asList(var5.matchTileIcons));
         }

         if (var5.tileIcons != null) {
            var3.addAll(Arrays.asList(var5.tileIcons));
         }
      }

      var7.retainAll(var3);
      return !var7.isEmpty();
   }

   private static ConnectedProperties[][] propertyListToArray(List var0) {
      ConnectedProperties[][] var1 = new ConnectedProperties[var0.size()][];

      for(int var2 = 0; var2 < var0.size(); ++var2) {
         List var3 = (List)var0.get(var2);
         if (var3 != null) {
            ConnectedProperties[] var4 = (ConnectedProperties[])var3.toArray(new ConnectedProperties[var3.size()]);
            var1[var2] = var4;
         }
      }

      return var1;
   }

   private static void addToTileList(ConnectedProperties var0, List var1) {
      if (var0.matchTileIcons != null) {
         for(int var2 = 0; var2 < var0.matchTileIcons.length; ++var2) {
            TextureAtlasSprite var3 = var0.matchTileIcons[var2];
            if (!(var3 instanceof TextureAtlasSprite)) {
               Config.warn("TextureAtlasSprite is not TextureAtlasSprite: " + var3 + ", name: " + var3.getIconName());
            } else {
               int var4 = var3.getIndexInMap();
               if (var4 < 0) {
                  Config.warn("Invalid tile ID: " + var4 + ", icon: " + var3.getIconName());
               } else {
                  addToList(var0, var1, var4);
               }
            }
         }
      }

   }

   private static void addToBlockList(ConnectedProperties var0, List var1) {
      if (var0.matchBlocks != null) {
         for(int var2 = 0; var2 < var0.matchBlocks.length; ++var2) {
            int var3 = var0.matchBlocks[var2].getBlockId();
            if (var3 < 0) {
               Config.warn("Invalid block ID: " + var3);
            } else {
               addToList(var0, var1, var3);
            }
         }
      }

   }

   private static void addToList(ConnectedProperties var0, List var1, int var2) {
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

   private static String[] getDefaultCtmPaths() {
      ArrayList var0 = new ArrayList();
      String var1 = "mcpatcher/ctm/default/";
      if (Config.isFromDefaultResourcePack(new ResourceLocation("textures/blocks/glass.png"))) {
         var0.add(var1 + "glass.properties");
         var0.add(var1 + "glasspane.properties");
      }

      if (Config.isFromDefaultResourcePack(new ResourceLocation("textures/blocks/bookshelf.png"))) {
         var0.add(var1 + "bookshelf.properties");
      }

      if (Config.isFromDefaultResourcePack(new ResourceLocation("textures/blocks/sandstone_normal.png"))) {
         var0.add(var1 + "sandstone.properties");
      }

      String[] var2 = new String[]{"white", "orange", "magenta", "light_blue", "yellow", "lime", "pink", "gray", "silver", "cyan", "purple", "blue", "brown", "green", "red", "black"};

      for(int var3 = 0; var3 < var2.length; ++var3) {
         String var4 = var2[var3];
         if (Config.isFromDefaultResourcePack(new ResourceLocation("textures/blocks/glass_" + var4 + ".png"))) {
            var0.add(var1 + var3 + "_glass_" + var4 + "/glass_" + var4 + ".properties");
            var0.add(var1 + var3 + "_glass_" + var4 + "/glass_pane_" + var4 + ".properties");
         }
      }

      String[] var5 = (String[])var0.toArray(new String[var0.size()]);
      return var5;
   }

   public static int getPaneTextureIndex(boolean var0, boolean var1, boolean var2, boolean var3) {
      return var1 && var0 ? (var2 ? (var3 ? 34 : 50) : (var3 ? 18 : 2)) : (var1 && !var0 ? (var2 ? (var3 ? 35 : 51) : (var3 ? 19 : 3)) : (!var1 && var0 ? (var2 ? (var3 ? 33 : 49) : (var3 ? 17 : 1)) : (var2 ? (var3 ? 32 : 48) : (var3 ? 16 : 0))));
   }

   public static int getReversePaneTextureIndex(int var0) {
      int var1 = var0 % 16;
      return var1 == 1 ? var0 + 2 : (var1 == 3 ? var0 - 2 : var0);
   }

   public static TextureAtlasSprite getCtmTexture(ConnectedProperties var0, int var1, TextureAtlasSprite var2) {
      if (var0.method != 1) {
         return var2;
      } else if (var1 >= 0 && var1 < ctmIndexes.length) {
         int var3 = ctmIndexes[var1];
         TextureAtlasSprite[] var4 = var0.tileIcons;
         return var3 >= 0 && var3 < var4.length ? var4[var3] : var2;
      } else {
         return var2;
      }
   }

   static int[] $SWITCH_TABLE$net$minecraft$util$EnumFacing() {
      int[] var10000 = $SWITCH_TABLE$net$minecraft$util$EnumFacing;
      if (var10000 != null) {
         return var10000;
      } else {
         int[] var0 = new int[EnumFacing.values().length];

         try {
            var0[EnumFacing.DOWN.ordinal()] = 1;
         } catch (NoSuchFieldError var6) {
         }

         try {
            var0[EnumFacing.EAST.ordinal()] = 6;
         } catch (NoSuchFieldError var5) {
         }

         try {
            var0[EnumFacing.NORTH.ordinal()] = 3;
         } catch (NoSuchFieldError var4) {
         }

         try {
            var0[EnumFacing.SOUTH.ordinal()] = 4;
         } catch (NoSuchFieldError var3) {
         }

         try {
            var0[EnumFacing.UP.ordinal()] = 2;
         } catch (NoSuchFieldError var2) {
         }

         try {
            var0[EnumFacing.WEST.ordinal()] = 5;
         } catch (NoSuchFieldError var1) {
         }

         $SWITCH_TABLE$net$minecraft$util$EnumFacing = var0;
         return var0;
      }
   }
}
