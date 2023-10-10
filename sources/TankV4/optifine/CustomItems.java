package optifine;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.Set;
import net.minecraft.client.Minecraft;
import net.minecraft.client.model.ModelBase;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.block.model.ItemModelGenerator;
import net.minecraft.client.renderer.entity.RenderItem;
import net.minecraft.client.renderer.texture.TextureManager;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.client.resources.IResourcePack;
import net.minecraft.client.resources.model.IBakedModel;
import net.minecraft.client.resources.model.ModelResourceLocation;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemArmor;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.potion.Potion;
import net.minecraft.util.ResourceLocation;
import shadersmod.client.Shaders;
import shadersmod.client.ShadersRender;

public class CustomItems {
   private static CustomItemProperties[][] itemProperties = null;
   private static CustomItemProperties[][] enchantmentProperties = null;
   private static Map mapPotionIds = null;
   private static ItemModelGenerator itemModelGenerator = new ItemModelGenerator();
   private static boolean useGlint = true;
   public static final int MASK_POTION_SPLASH = 16384;
   public static final int MASK_POTION_NAME = 63;
   public static final String KEY_TEXTURE_OVERLAY = "texture.potion_overlay";
   public static final String KEY_TEXTURE_SPLASH = "texture.potion_bottle_splash";
   public static final String KEY_TEXTURE_DRINKABLE = "texture.potion_bottle_drinkable";
   public static final String DEFAULT_TEXTURE_OVERLAY = "items/potion_overlay";
   public static final String DEFAULT_TEXTURE_SPLASH = "items/potion_bottle_splash";
   public static final String DEFAULT_TEXTURE_DRINKABLE = "items/potion_bottle_drinkable";
   private static final int[] EMPTY_INT_ARRAY = new int[0];
   private static final int[][] EMPTY_INT2_ARRAY = new int[0][];

   public static void updateIcons(TextureMap var0) {
      itemProperties = null;
      enchantmentProperties = null;
      useGlint = true;
      if (Config.isCustomItems()) {
         readCitProperties("mcpatcher/cit.properties");
         IResourcePack[] var1 = Config.getResourcePacks();

         for(int var2 = var1.length - 1; var2 >= 0; --var2) {
            IResourcePack var3 = var1[var2];
            updateIcons(var0, var3);
         }

         updateIcons(var0, Config.getDefaultResourcePack());
         if (itemProperties.length <= 0) {
            itemProperties = null;
         }

         if (enchantmentProperties.length <= 0) {
            enchantmentProperties = null;
         }
      }

   }

   private static void readCitProperties(String var0) {
      try {
         ResourceLocation var1 = new ResourceLocation(var0);
         InputStream var2 = Config.getResourceStream(var1);
         if (var2 == null) {
            return;
         }

         Config.dbg("CustomItems: Loading " + var0);
         Properties var3 = new Properties();
         var3.load(var2);
         var2.close();
         useGlint = Config.parseBoolean(var3.getProperty("useGlint"), true);
      } catch (FileNotFoundException var4) {
         return;
      } catch (IOException var5) {
         var5.printStackTrace();
      }

   }

   private static void updateIcons(TextureMap var0, IResourcePack var1) {
      String[] var2 = ResUtils.collectFiles(var1, (String)"mcpatcher/cit/", (String)".properties", (String[])null);
      Map var3 = makeAutoImageProperties(var1);
      if (var3.size() > 0) {
         Set var4 = var3.keySet();
         String[] var5 = (String[])var4.toArray(new String[var4.size()]);
         var2 = (String[])Config.addObjectsToArray(var2, var5);
      }

      Arrays.sort(var2);
      List var14 = makePropertyList(itemProperties);
      List var15 = makePropertyList(enchantmentProperties);

      for(int var6 = 0; var6 < var2.length; ++var6) {
         String var7 = var2[var6];
         Config.dbg("CustomItems: " + var7);

         try {
            CustomItemProperties var8 = null;
            if (var3.containsKey(var7)) {
               var8 = (CustomItemProperties)var3.get(var7);
            }

            if (var8 == null) {
               ResourceLocation var9 = new ResourceLocation(var7);
               InputStream var10 = var1.getInputStream(var9);
               if (var10 == null) {
                  Config.warn("CustomItems file not found: " + var7);
                  continue;
               }

               Properties var11 = new Properties();
               var11.load(var10);
               var8 = new CustomItemProperties(var11, var7);
            }

            if (var8.isValid(var7)) {
               var8.updateIcons(var0);
               addToItemList(var8, var14);
               addToEnchantmentList(var8, var15);
            }
         } catch (FileNotFoundException var12) {
            Config.warn("CustomItems file not found: " + var7);
         } catch (Exception var13) {
            var13.printStackTrace();
         }
      }

      itemProperties = propertyListToArray(var14);
      enchantmentProperties = propertyListToArray(var15);
      Comparator var16 = getPropertiesComparator();

      int var17;
      CustomItemProperties[] var18;
      for(var17 = 0; var17 < itemProperties.length; ++var17) {
         var18 = itemProperties[var17];
         if (var18 != null) {
            Arrays.sort(var18, var16);
         }
      }

      for(var17 = 0; var17 < enchantmentProperties.length; ++var17) {
         var18 = enchantmentProperties[var17];
         if (var18 != null) {
            Arrays.sort(var18, var16);
         }
      }

   }

   private static Comparator getPropertiesComparator() {
      Comparator var0 = new Comparator() {
         public int compare(Object var1, Object var2) {
            CustomItemProperties var3 = (CustomItemProperties)var1;
            CustomItemProperties var4 = (CustomItemProperties)var2;
            return var3.layer != var4.layer ? var3.layer - var4.layer : (var3.weight != var4.weight ? var4.weight - var3.weight : (!var3.basePath.equals(var4.basePath) ? var3.basePath.compareTo(var4.basePath) : var3.name.compareTo(var4.name)));
         }
      };
      return var0;
   }

   public static void updateModels() {
      if (itemProperties != null) {
         for(int var0 = 0; var0 < itemProperties.length; ++var0) {
            CustomItemProperties[] var1 = itemProperties[var0];
            if (var1 != null) {
               for(int var2 = 0; var2 < var1.length; ++var2) {
                  CustomItemProperties var3 = var1[var2];
                  if (var3 != null && var3.type == 1) {
                     TextureMap var4 = Minecraft.getMinecraft().getTextureMapBlocks();
                     var3.updateModel(var4, itemModelGenerator);
                  }
               }
            }
         }
      }

   }

   private static Map makeAutoImageProperties(IResourcePack var0) {
      HashMap var1 = new HashMap();
      var1.putAll(makePotionImageProperties(var0, false));
      var1.putAll(makePotionImageProperties(var0, true));
      return var1;
   }

   private static Map makePotionImageProperties(IResourcePack var0, boolean var1) {
      HashMap var2 = new HashMap();
      String var3 = var1 ? "splash/" : "normal/";
      String[] var4 = new String[]{"mcpatcher/cit/potion/" + var3, "mcpatcher/cit/Potion/" + var3};
      String[] var5 = new String[]{".png"};
      String[] var6 = ResUtils.collectFiles(var0, var4, var5);

      for(int var7 = 0; var7 < var6.length; ++var7) {
         String var8 = var6[var7];
         String var9 = StrUtils.removePrefixSuffix(var8, var4, var5);
         Properties var10 = makePotionProperties(var9, var1, var8);
         if (var10 != null) {
            String var11 = StrUtils.removeSuffix(var8, var5) + ".properties";
            CustomItemProperties var12 = new CustomItemProperties(var10, var11);
            var2.put(var11, var12);
         }
      }

      return var2;
   }

   private static Properties makePotionProperties(String var0, boolean var1, String var2) {
      if (StrUtils.endsWith(var0, new String[]{"_n", "_s"})) {
         return null;
      } else {
         int var3;
         if (var0.equals("empty") && !var1) {
            var3 = Item.getIdFromItem(Items.glass_bottle);
            Properties var8 = new Properties();
            var8.put("type", "item");
            var8.put("items", "" + var3);
            return var8;
         } else {
            var3 = Item.getIdFromItem(Items.potionitem);
            int[] var4 = (int[])getMapPotionIds().get(var0);
            if (var4 == null) {
               Config.warn("Potion not found for image: " + var2);
               return null;
            } else {
               StringBuffer var5 = new StringBuffer();

               for(int var6 = 0; var6 < var4.length; ++var6) {
                  int var7 = var4[var6];
                  if (var1) {
                     var7 |= 16384;
                  }

                  if (var6 > 0) {
                     var5.append(" ");
                  }

                  var5.append(var7);
               }

               short var9 = 16447;
               Properties var10 = new Properties();
               var10.put("type", "item");
               var10.put("items", "" + var3);
               var10.put("damage", var5.toString());
               var10.put("damageMask", "" + var9);
               if (var1) {
                  var10.put("texture.potion_bottle_splash", var0);
               } else {
                  var10.put("texture.potion_bottle_drinkable", var0);
               }

               return var10;
            }
         }
      }
   }

   private static Map getMapPotionIds() {
      if (mapPotionIds == null) {
         mapPotionIds = new LinkedHashMap();
         mapPotionIds.put("water", new int[1]);
         mapPotionIds.put("awkward", new int[]{16});
         mapPotionIds.put("thick", new int[]{32});
         mapPotionIds.put("potent", new int[]{48});
         mapPotionIds.put("regeneration", getPotionIds(1));
         mapPotionIds.put("moveSpeed", getPotionIds(2));
         mapPotionIds.put("fireResistance", getPotionIds(3));
         mapPotionIds.put("poison", getPotionIds(4));
         mapPotionIds.put("heal", getPotionIds(5));
         mapPotionIds.put("nightVision", getPotionIds(6));
         mapPotionIds.put("clear", getPotionIds(7));
         mapPotionIds.put("bungling", getPotionIds(23));
         mapPotionIds.put("charming", getPotionIds(39));
         mapPotionIds.put("rank", getPotionIds(55));
         mapPotionIds.put("weakness", getPotionIds(8));
         mapPotionIds.put("damageBoost", getPotionIds(9));
         mapPotionIds.put("moveSlowdown", getPotionIds(10));
         mapPotionIds.put("diffuse", getPotionIds(11));
         mapPotionIds.put("smooth", getPotionIds(27));
         mapPotionIds.put("refined", getPotionIds(43));
         mapPotionIds.put("acrid", getPotionIds(59));
         mapPotionIds.put("harm", getPotionIds(12));
         mapPotionIds.put("waterBreathing", getPotionIds(13));
         mapPotionIds.put("invisibility", getPotionIds(14));
         mapPotionIds.put("thin", getPotionIds(15));
         mapPotionIds.put("debonair", getPotionIds(31));
         mapPotionIds.put("sparkling", getPotionIds(47));
         mapPotionIds.put("stinky", getPotionIds(63));
      }

      return mapPotionIds;
   }

   private static int[] getPotionIds(int var0) {
      return new int[]{var0, var0 + 16, var0 + 32, var0 + 48};
   }

   private static int getPotionNameDamage(String var0) {
      String var1 = "potion." + var0;
      Potion[] var2 = Potion.potionTypes;

      for(int var3 = 0; var3 < var2.length; ++var3) {
         Potion var4 = var2[var3];
         if (var4 != null) {
            String var5 = var4.getName();
            if (var1.equals(var5)) {
               return var4.getId();
            }
         }
      }

      return -1;
   }

   private static List makePropertyList(CustomItemProperties[][] var0) {
      ArrayList var1 = new ArrayList();
      if (var0 != null) {
         for(int var2 = 0; var2 < var0.length; ++var2) {
            CustomItemProperties[] var3 = var0[var2];
            ArrayList var4 = null;
            if (var3 != null) {
               var4 = new ArrayList(Arrays.asList(var3));
            }

            var1.add(var4);
         }
      }

      return var1;
   }

   private static CustomItemProperties[][] propertyListToArray(List var0) {
      CustomItemProperties[][] var1 = new CustomItemProperties[var0.size()][];

      for(int var2 = 0; var2 < var0.size(); ++var2) {
         List var3 = (List)var0.get(var2);
         if (var3 != null) {
            CustomItemProperties[] var4 = (CustomItemProperties[])var3.toArray(new CustomItemProperties[var3.size()]);
            Arrays.sort(var4, new CustomItemsComparator());
            var1[var2] = var4;
         }
      }

      return var1;
   }

   private static void addToItemList(CustomItemProperties var0, List var1) {
      if (var0.items != null) {
         for(int var2 = 0; var2 < var0.items.length; ++var2) {
            int var3 = var0.items[var2];
            if (var3 <= 0) {
               Config.warn("Invalid item ID: " + var3);
            } else {
               addToList(var0, var1, var3);
            }
         }
      }

   }

   private static void addToEnchantmentList(CustomItemProperties var0, List var1) {
      if (var0.type == 2 && var0.enchantmentIds != null) {
         for(int var2 = 0; var2 < 256; ++var2) {
            if (var0.enchantmentIds.isInRange(var2)) {
               addToList(var0, var1, var2);
            }
         }
      }

   }

   private static void addToList(CustomItemProperties var0, List var1, int var2) {
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

   public static IBakedModel getCustomItemModel(ItemStack var0, IBakedModel var1, ModelResourceLocation var2) {
      if (var1.isGui3d()) {
         return var1;
      } else if (itemProperties == null) {
         return var1;
      } else {
         CustomItemProperties var3 = getCustomItemProperties(var0, 1);
         return var3 == null ? var1 : var3.getModel(var2);
      }
   }

   public static boolean bindCustomArmorTexture(ItemStack var0, int var1, String var2) {
      if (itemProperties == null) {
         return false;
      } else {
         ResourceLocation var3 = getCustomArmorLocation(var0, var1, var2);
         if (var3 == null) {
            return false;
         } else {
            Config.getTextureManager().bindTexture(var3);
            return true;
         }
      }
   }

   private static ResourceLocation getCustomArmorLocation(ItemStack var0, int var1, String var2) {
      CustomItemProperties var3 = getCustomItemProperties(var0, 3);
      if (var3 == null) {
         return null;
      } else if (var3.mapTextureLocations == null) {
         return null;
      } else {
         Item var4 = var0.getItem();
         if (!(var4 instanceof ItemArmor)) {
            return null;
         } else {
            ItemArmor var5 = (ItemArmor)var4;
            String var6 = var5.getArmorMaterial().getName();
            StringBuffer var7 = new StringBuffer();
            var7.append("texture.");
            var7.append(var6);
            var7.append("_layer_");
            var7.append(var1);
            if (var2 != null) {
               var7.append("_");
               var7.append(var2);
            }

            String var8 = var7.toString();
            ResourceLocation var9 = (ResourceLocation)var3.mapTextureLocations.get(var8);
            return var9;
         }
      }
   }

   private static CustomItemProperties getCustomItemProperties(ItemStack var0, int var1) {
      if (itemProperties == null) {
         return null;
      } else if (var0 == null) {
         return null;
      } else {
         Item var2 = var0.getItem();
         int var3 = Item.getIdFromItem(var2);
         if (var3 >= 0 && var3 < itemProperties.length) {
            CustomItemProperties[] var4 = itemProperties[var3];
            if (var4 != null) {
               for(int var5 = 0; var5 < var4.length; ++var5) {
                  CustomItemProperties var6 = var4[var5];
                  if (var6.type == var1) {
                     ;
                  }
               }
            }
         }

         return null;
      }
   }

   private static int[][] getEnchantmentIdLevels(ItemStack var0) {
      Item var1 = var0.getItem();
      NBTTagList var2 = var1 == Items.enchanted_book ? Items.enchanted_book.getEnchantments(var0) : var0.getEnchantmentTagList();
      if (var2 != null && var2.tagCount() > 0) {
         int[][] var3 = new int[var2.tagCount()][2];

         for(int var4 = 0; var4 < var2.tagCount(); ++var4) {
            NBTTagCompound var5 = var2.getCompoundTagAt(var4);
            short var6 = var5.getShort("id");
            short var7 = var5.getShort("lvl");
            var3[var4][0] = var6;
            var3[var4][1] = var7;
         }

         return var3;
      } else {
         return EMPTY_INT2_ARRAY;
      }
   }

   public static boolean renderCustomEffect(RenderItem var0, ItemStack var1, IBakedModel var2) {
      if (enchantmentProperties == null) {
         return false;
      } else if (var1 == null) {
         return false;
      } else {
         int[][] var3 = getEnchantmentIdLevels(var1);
         if (var3.length <= 0) {
            return false;
         } else {
            HashSet var4 = null;
            boolean var5 = false;
            TextureManager var6 = Config.getTextureManager();

            for(int var7 = 0; var7 < var3.length; ++var7) {
               int var8 = var3[var7][0];
               if (var8 >= 0 && var8 < enchantmentProperties.length) {
                  CustomItemProperties[] var9 = enchantmentProperties[var8];
                  if (var9 != null) {
                     for(int var10 = 0; var10 < var9.length; ++var10) {
                        CustomItemProperties var11 = var9[var10];
                        if (var4 == null) {
                           var4 = new HashSet();
                        }

                        if (var4.add(var8) && var3 != null && var11.textureLocation != null) {
                           var6.bindTexture(var11.textureLocation);
                           float var12 = var11.getTextureWidth(var6);
                           if (!var5) {
                              var5 = true;
                              GlStateManager.depthMask(false);
                              GlStateManager.depthFunc(514);
                              GlStateManager.disableLighting();
                              GlStateManager.matrixMode(5890);
                           }

                           Blender.setupBlend(var11.blend, 1.0F);
                           GlStateManager.pushMatrix();
                           GlStateManager.scale(var12 / 2.0F, var12 / 2.0F, var12 / 2.0F);
                           float var13 = var11.speed * (float)(Minecraft.getSystemTime() % 3000L) / 3000.0F / 8.0F;
                           GlStateManager.translate(var13, 0.0F, 0.0F);
                           GlStateManager.rotate(var11.rotation, 0.0F, 0.0F, 1.0F);
                           var0.renderModel(var2, -1);
                           GlStateManager.popMatrix();
                        }
                     }
                  }
               }
            }

            if (var5) {
               GlStateManager.enableAlpha();
               GlStateManager.enableBlend();
               GlStateManager.blendFunc(770, 771);
               GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
               GlStateManager.matrixMode(5888);
               GlStateManager.enableLighting();
               GlStateManager.depthFunc(515);
               GlStateManager.depthMask(true);
               var6.bindTexture(TextureMap.locationBlocksTexture);
            }

            return var5;
         }
      }
   }

   public static boolean renderCustomArmorEffect(EntityLivingBase var0, ItemStack var1, ModelBase var2, float var3, float var4, float var5, float var6, float var7, float var8, float var9) {
      if (enchantmentProperties == null) {
         return false;
      } else if (Config.isShaders() && Shaders.isShadowPass) {
         return false;
      } else if (var1 == null) {
         return false;
      } else {
         int[][] var10 = getEnchantmentIdLevels(var1);
         if (var10.length <= 0) {
            return false;
         } else {
            HashSet var11 = null;
            boolean var12 = false;
            TextureManager var13 = Config.getTextureManager();

            for(int var14 = 0; var14 < var10.length; ++var14) {
               int var15 = var10[var14][0];
               if (var15 >= 0 && var15 < enchantmentProperties.length) {
                  CustomItemProperties[] var16 = enchantmentProperties[var15];
                  if (var16 != null) {
                     for(int var17 = 0; var17 < var16.length; ++var17) {
                        CustomItemProperties var18 = var16[var17];
                        if (var11 == null) {
                           var11 = new HashSet();
                        }

                        if (var11.add(var15) && var10 != null && var18.textureLocation != null) {
                           var13.bindTexture(var18.textureLocation);
                           float var19 = var18.getTextureWidth(var13);
                           if (!var12) {
                              var12 = true;
                              if (Config.isShaders()) {
                                 ShadersRender.renderEnchantedGlintBegin();
                              }

                              GlStateManager.enableBlend();
                              GlStateManager.depthFunc(514);
                              GlStateManager.depthMask(false);
                           }

                           Blender.setupBlend(var18.blend, 1.0F);
                           GlStateManager.disableLighting();
                           GlStateManager.matrixMode(5890);
                           GlStateManager.loadIdentity();
                           GlStateManager.rotate(var18.rotation, 0.0F, 0.0F, 1.0F);
                           float var20 = var19 / 8.0F;
                           GlStateManager.scale(var20, var20 / 2.0F, var20);
                           float var21 = var18.speed * (float)(Minecraft.getSystemTime() % 3000L) / 3000.0F / 8.0F;
                           GlStateManager.translate(0.0F, var21, 0.0F);
                           GlStateManager.matrixMode(5888);
                           var2.render(var0, var3, var4, var6, var7, var8, var9);
                        }
                     }
                  }
               }
            }

            if (var12) {
               GlStateManager.enableAlpha();
               GlStateManager.enableBlend();
               GlStateManager.blendFunc(770, 771);
               GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
               GlStateManager.matrixMode(5890);
               GlStateManager.loadIdentity();
               GlStateManager.matrixMode(5888);
               GlStateManager.enableLighting();
               GlStateManager.depthMask(true);
               GlStateManager.depthFunc(515);
               GlStateManager.disableBlend();
               if (Config.isShaders()) {
                  ShadersRender.renderEnchantedGlintEnd();
               }
            }

            return var12;
         }
      }
   }

   public static boolean isUseGlint() {
      return useGlint;
   }
}
