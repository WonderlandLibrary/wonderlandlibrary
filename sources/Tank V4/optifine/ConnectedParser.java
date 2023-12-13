package optifine;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.EnumSet;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import net.minecraft.block.Block;
import net.minecraft.block.BlockDoublePlant;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.util.EnumFacing;
import net.minecraft.world.biome.BiomeGenBase;

public class ConnectedParser {
   private String context = null;
   private static final MatchBlock[] NO_MATCH_BLOCKS = new MatchBlock[0];

   public ConnectedParser(String var1) {
      this.context = var1;
   }

   public String parseName(String var1) {
      String var2 = var1;
      int var3 = var1.lastIndexOf(47);
      if (var3 >= 0) {
         var2 = var1.substring(var3 + 1);
      }

      int var4 = var2.lastIndexOf(46);
      if (var4 >= 0) {
         var2 = var2.substring(0, var4);
      }

      return var2;
   }

   public String parseBasePath(String var1) {
      int var2 = var1.lastIndexOf(47);
      return var2 < 0 ? "" : var1.substring(0, var2);
   }

   public MatchBlock[] parseMatchBlocks(String var1) {
      if (var1 == null) {
         return null;
      } else {
         ArrayList var2 = new ArrayList();
         String[] var3 = Config.tokenize(var1, " ");

         for(int var4 = 0; var4 < var3.length; ++var4) {
            String var5 = var3[var4];
            MatchBlock[] var6 = this.parseMatchBlock(var5);
            if (var6 == null) {
               return NO_MATCH_BLOCKS;
            }

            var2.addAll(Arrays.asList(var6));
         }

         MatchBlock[] var7 = (MatchBlock[])var2.toArray(new MatchBlock[var2.size()]);
         return var7;
      }
   }

   public MatchBlock[] parseMatchBlock(String var1) {
      if (var1 == null) {
         return null;
      } else {
         var1 = var1.trim();
         if (var1.length() <= 0) {
            return null;
         } else {
            String[] var2 = Config.tokenize(var1, ":");
            String var3 = "minecraft";
            boolean var4 = false;
            byte var14;
            if (var2.length > 1 && this < var2) {
               var3 = var2[0];
               var14 = 1;
            } else {
               var3 = "minecraft";
               var14 = 0;
            }

            String var5 = var2[var14];
            String[] var6 = (String[])Arrays.copyOfRange(var2, var14 + 1, var2.length);
            Block[] var7 = this.parseBlockPart(var3, var5);
            if (var7 == null) {
               return null;
            } else {
               MatchBlock[] var8 = new MatchBlock[var7.length];

               for(int var9 = 0; var9 < var7.length; ++var9) {
                  Block var10 = var7[var9];
                  int var11 = Block.getIdFromBlock(var10);
                  int[] var12 = null;
                  if (var6.length > 0) {
                     var12 = this.parseBlockMetadatas(var10, var6);
                     if (var12 == null) {
                        return null;
                     }
                  }

                  MatchBlock var13 = new MatchBlock(var11, var12);
                  var8[var9] = var13;
               }

               return var8;
            }
         }
      }
   }

   public Block[] parseBlockPart(String var1, String var2) {
      if (var2 != null) {
         String var8 = var1 + ":" + var2;
         Block var9 = Block.getBlockFromName(var8);
         if (var9 == null) {
            this.warn("Block not found for name: " + var8);
            return null;
         } else {
            Block[] var10 = new Block[]{var9};
            return var10;
         }
      } else {
         int[] var3 = this.parseIntList(var2);
         if (var3 == null) {
            return null;
         } else {
            Block[] var4 = new Block[var3.length];

            for(int var5 = 0; var5 < var3.length; ++var5) {
               int var6 = var3[var5];
               Block var7 = Block.getBlockById(var6);
               if (var7 == null) {
                  this.warn("Block not found for id: " + var6);
                  return null;
               }

               var4[var5] = var7;
            }

            return var4;
         }
      }
   }

   public int[] parseBlockMetadatas(Block var1, String[] var2) {
      if (var2.length <= 0) {
         return null;
      } else {
         String var3 = var2[0];
         if (var3 == null) {
            int[] var19 = this.parseIntList(var3);
            return var19;
         } else {
            IBlockState var4 = var1.getDefaultState();
            Collection var5 = var4.getPropertyNames();
            HashMap var6 = new HashMap();

            for(int var7 = 0; var7 < var2.length; ++var7) {
               String var8 = var2[var7];
               if (var8.length() > 0) {
                  String[] var9 = Config.tokenize(var8, "=");
                  if (var9.length != 2) {
                     this.warn("Invalid block property: " + var8);
                     return null;
                  }

                  String var10 = var9[0];
                  String var11 = var9[1];
                  IProperty var12 = ConnectedProperties.getProperty(var10, var5);
                  if (var12 == null) {
                     this.warn("Property not found: " + var10 + ", block: " + var1);
                     return null;
                  }

                  Object var13 = (List)var6.get(var10);
                  if (var13 == null) {
                     var13 = new ArrayList();
                     var6.put(var12, var13);
                  }

                  String[] var14 = Config.tokenize(var11, ",");

                  for(int var15 = 0; var15 < var14.length; ++var15) {
                     String var16 = var14[var15];
                     Comparable var17 = parsePropertyValue(var12, var16);
                     if (var17 == null) {
                        this.warn("Property value not found: " + var16 + ", property: " + var10 + ", block: " + var1);
                        return null;
                     }

                     ((List)var13).add(var17);
                  }
               }
            }

            if (var6.isEmpty()) {
               return null;
            } else {
               ArrayList var20 = new ArrayList();

               int var23;
               for(int var21 = 0; var21 < 16; ++var21) {
                  var23 = var21;

                  try {
                     IBlockState var24 = this.getStateFromMeta(var1, var23);
                     if (var6 == null) {
                        var20.add(var23);
                     }
                  } catch (IllegalArgumentException var18) {
                  }
               }

               if (var20.size() == 16) {
                  return null;
               } else {
                  int[] var22 = new int[var20.size()];

                  for(var23 = 0; var23 < var22.length; ++var23) {
                     var22[var23] = (Integer)var20.get(var23);
                  }

                  return var22;
               }
            }
         }
      }
   }

   private IBlockState getStateFromMeta(Block var1, int var2) {
      try {
         IBlockState var3 = var1.getStateFromMeta(var2);
         if (var1 == Blocks.double_plant && var2 > 7) {
            IBlockState var4 = var1.getStateFromMeta(var2 & 7);
            var3 = var3.withProperty(BlockDoublePlant.VARIANT, (BlockDoublePlant.EnumPlantType)var4.getValue(BlockDoublePlant.VARIANT));
         }

         return var3;
      } catch (IllegalArgumentException var5) {
         return var1.getDefaultState();
      }
   }

   public static Comparable parsePropertyValue(IProperty var0, String var1) {
      Class var2 = var0.getValueClass();
      Comparable var3 = parseValue(var1, var2);
      if (var3 == null) {
         Collection var4 = var0.getAllowedValues();
         var3 = getPropertyValue(var1, var4);
      }

      return var3;
   }

   public static Comparable getPropertyValue(String var0, Collection var1) {
      Iterator var3 = var1.iterator();

      while(var3.hasNext()) {
         Object var2 = var3.next();
         if (String.valueOf(var2).equals(var0)) {
            return (Comparable)var2;
         }
      }

      return null;
   }

   public static Comparable parseValue(String var0, Class var1) {
      return (Comparable)(var1 == String.class ? var0 : (var1 == Boolean.class ? Boolean.valueOf(var0) : var1 == Float.class ? (double)Float.valueOf(var0) : (var1 == Double.class ? Double.valueOf(var0) : (double)(var1 == Integer.class ? (long)Integer.valueOf(var0) : var1 == Long.class ? Long.valueOf(var0) : null))));
   }

   public BiomeGenBase[] parseBiomes(String var1) {
      if (var1 == null) {
         return null;
      } else {
         String[] var2 = Config.tokenize(var1, " ");
         ArrayList var3 = new ArrayList();

         for(int var4 = 0; var4 < var2.length; ++var4) {
            String var5 = var2[var4];
            BiomeGenBase var6 = this.findBiome(var5);
            if (var6 == null) {
               this.warn("Biome not found: " + var5);
            } else {
               var3.add(var6);
            }
         }

         BiomeGenBase[] var7 = (BiomeGenBase[])var3.toArray(new BiomeGenBase[var3.size()]);
         return var7;
      }
   }

   public BiomeGenBase findBiome(String var1) {
      var1 = var1.toLowerCase();
      if (var1.equals("nether")) {
         return BiomeGenBase.hell;
      } else {
         BiomeGenBase[] var2 = BiomeGenBase.getBiomeGenArray();

         for(int var3 = 0; var3 < var2.length; ++var3) {
            BiomeGenBase var4 = var2[var3];
            if (var4 != null) {
               String var5 = var4.biomeName.replace(" ", "").toLowerCase();
               if (var5.equals(var1)) {
                  return var4;
               }
            }
         }

         return null;
      }
   }

   public int parseInt(String var1) {
      if (var1 == null) {
         return -1;
      } else {
         int var2 = Config.parseInt(var1, -1);
         if (var2 < 0) {
            this.warn("Invalid number: " + var1);
         }

         return var2;
      }
   }

   public int parseInt(String var1, int var2) {
      if (var1 == null) {
         return var2;
      } else {
         int var3 = Config.parseInt(var1, -1);
         if (var3 < 0) {
            this.warn("Invalid number: " + var1);
            return var2;
         } else {
            return var3;
         }
      }
   }

   public int[] parseIntList(String var1) {
      if (var1 == null) {
         return null;
      } else {
         ArrayList var2 = new ArrayList();
         String[] var3 = Config.tokenize(var1, " ,");

         for(int var4 = 0; var4 < var3.length; ++var4) {
            String var5 = var3[var4];
            if (var5.contains("-")) {
               String[] var12 = Config.tokenize(var5, "-");
               if (var12.length != 2) {
                  this.warn("Invalid interval: " + var5 + ", when parsing: " + var1);
               } else {
                  int var7 = Config.parseInt(var12[0], -1);
                  int var8 = Config.parseInt(var12[1], -1);
                  if (var7 >= 0 && var8 >= 0 && var7 <= var8) {
                     for(int var9 = var7; var9 <= var8; ++var9) {
                        var2.add(var9);
                     }
                  } else {
                     this.warn("Invalid interval: " + var5 + ", when parsing: " + var1);
                  }
               }
            } else {
               int var6 = Config.parseInt(var5, -1);
               if (var6 < 0) {
                  this.warn("Invalid number: " + var5 + ", when parsing: " + var1);
               } else {
                  var2.add(var6);
               }
            }
         }

         int[] var10 = new int[var2.size()];

         for(int var11 = 0; var11 < var10.length; ++var11) {
            var10[var11] = (Integer)var2.get(var11);
         }

         return var10;
      }
   }

   public boolean[] parseFaces(String var1, boolean[] var2) {
      if (var1 == null) {
         return var2;
      } else {
         EnumSet var3 = EnumSet.allOf(EnumFacing.class);
         String[] var4 = Config.tokenize(var1, " ,");

         for(int var5 = 0; var5 < var4.length; ++var5) {
            String var6 = var4[var5];
            if (var6.equals("sides")) {
               var3.add(EnumFacing.NORTH);
               var3.add(EnumFacing.SOUTH);
               var3.add(EnumFacing.WEST);
               var3.add(EnumFacing.EAST);
            } else if (var6.equals("all")) {
               var3.addAll(Arrays.asList(EnumFacing.VALUES));
            } else {
               EnumFacing var7 = this.parseFace(var6);
               if (var7 != null) {
                  var3.add(var7);
               }
            }
         }

         boolean[] var8 = new boolean[EnumFacing.VALUES.length];

         for(int var9 = 0; var9 < var8.length; ++var9) {
            var8[var9] = var3.contains(EnumFacing.VALUES[var9]);
         }

         return var8;
      }
   }

   public EnumFacing parseFace(String var1) {
      var1 = var1.toLowerCase();
      if (!var1.equals("bottom") && !var1.equals("down")) {
         if (!var1.equals("top") && !var1.equals("up")) {
            if (var1.equals("north")) {
               return EnumFacing.NORTH;
            } else if (var1.equals("south")) {
               return EnumFacing.SOUTH;
            } else if (var1.equals("east")) {
               return EnumFacing.EAST;
            } else if (var1.equals("west")) {
               return EnumFacing.WEST;
            } else {
               Config.warn("Unknown face: " + var1);
               return null;
            }
         } else {
            return EnumFacing.UP;
         }
      } else {
         return EnumFacing.DOWN;
      }
   }

   public void dbg(String var1) {
      Config.dbg(this.context + ": " + var1);
   }

   public void warn(String var1) {
      Config.warn(this.context + ": " + var1);
   }

   public RangeListInt parseRangeListInt(String var1) {
      if (var1 == null) {
         return null;
      } else {
         RangeListInt var2 = new RangeListInt();
         String[] var3 = Config.tokenize(var1, " ,");

         for(int var4 = 0; var4 < var3.length; ++var4) {
            String var5 = var3[var4];
            RangeInt var6 = this.parseRangeInt(var5);
            if (var6 == null) {
               return null;
            }

            var2.addRange(var6);
         }

         return var2;
      }
   }

   private RangeInt parseRangeInt(String var1) {
      if (var1 == null) {
         return null;
      } else if (var1.indexOf(45) >= 0) {
         String[] var5 = Config.tokenize(var1, "-");
         if (var5.length != 2) {
            this.warn("Invalid range: " + var1);
            return null;
         } else {
            int var3 = Config.parseInt(var5[0], -1);
            int var4 = Config.parseInt(var5[1], -1);
            if (var3 >= 0 && var4 >= 0) {
               return new RangeInt(var3, var4);
            } else {
               this.warn("Invalid range: " + var1);
               return null;
            }
         }
      } else {
         int var2 = Config.parseInt(var1, -1);
         if (var2 < 0) {
            this.warn("Invalid integer: " + var1);
            return null;
         } else {
            return new RangeInt(var2, var2);
         }
      }
   }

   public static boolean parseBoolean(String var0) {
      return var0 == null ? false : var0.toLowerCase().equals("true");
   }

   public static int parseColor(String var0, int var1) {
      if (var0 == null) {
         return var1;
      } else {
         var0 = var0.trim();

         try {
            int var2 = Integer.parseInt(var0, 16) & 16777215;
            return var2;
         } catch (NumberFormatException var3) {
            return var1;
         }
      }
   }
}
