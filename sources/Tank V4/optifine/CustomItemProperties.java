package optifine;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Properties;
import java.util.Set;
import java.util.TreeSet;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.block.model.BakedQuad;
import net.minecraft.client.renderer.block.model.BlockPart;
import net.minecraft.client.renderer.block.model.BlockPartFace;
import net.minecraft.client.renderer.block.model.FaceBakery;
import net.minecraft.client.renderer.block.model.ItemModelGenerator;
import net.minecraft.client.renderer.block.model.ModelBlock;
import net.minecraft.client.renderer.texture.ITextureObject;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.renderer.texture.TextureManager;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.client.resources.model.IBakedModel;
import net.minecraft.client.resources.model.ModelResourceLocation;
import net.minecraft.client.resources.model.ModelRotation;
import net.minecraft.client.resources.model.SimpleBakedModel;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemArmor;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ResourceLocation;
import org.lwjgl.opengl.GL11;

public class CustomItemProperties {
   public String name = null;
   public String basePath = null;
   public int type = 1;
   public int[] items = null;
   public String texture = null;
   public Map mapTextures = null;
   public RangeListInt damage = null;
   public boolean damagePercent = false;
   public int damageMask = 0;
   public RangeListInt stackSize = null;
   public RangeListInt enchantmentIds = null;
   public RangeListInt enchantmentLevels = null;
   public NbtTagValue[] nbtTagValues = null;
   public int blend = 1;
   public float speed = 0.0F;
   public float rotation = 0.0F;
   public int layer = 0;
   public float duration = 1.0F;
   public int weight = 0;
   public ResourceLocation textureLocation = null;
   public Map mapTextureLocations = null;
   public TextureAtlasSprite sprite = null;
   public Map mapSprites = null;
   public IBakedModel model = null;
   public Map mapModels = null;
   private int textureWidth = 0;
   private int textureHeight = 0;
   public static final int TYPE_UNKNOWN = 0;
   public static final int TYPE_ITEM = 1;
   public static final int TYPE_ENCHANTMENT = 2;
   public static final int TYPE_ARMOR = 3;

   public CustomItemProperties(Properties var1, String var2) {
      this.name = parseName(var2);
      this.basePath = parseBasePath(var2);
      this.type = this.parseType(var1.getProperty("type"));
      this.items = this.parseItems(var1.getProperty("items"), var1.getProperty("matchItems"));
      this.mapTextures = parseTextures(var1, this.basePath);
      this.texture = parseTexture(var1.getProperty("texture"), var1.getProperty("tile"), var1.getProperty("source"), var2, this.basePath, this.type, this.mapTextures);
      String var3 = var1.getProperty("damage");
      if (var3 != null) {
         this.damagePercent = var3.contains("%");
         var3.replace("%", "");
         this.damage = this.parseRangeListInt(var3);
         this.damageMask = this.parseInt(var1.getProperty("damageMask"), 0);
      }

      this.stackSize = this.parseRangeListInt(var1.getProperty("stackSize"));
      this.enchantmentIds = this.parseRangeListInt(var1.getProperty("enchantmentIDs"));
      this.enchantmentLevels = this.parseRangeListInt(var1.getProperty("enchantmentLevels"));
      this.nbtTagValues = this.parseNbtTagValues(var1);
      this.blend = Blender.parseBlend(var1.getProperty("blend"));
      this.speed = this.parseFloat(var1.getProperty("speed"), 0.0F);
      this.rotation = this.parseFloat(var1.getProperty("rotation"), 0.0F);
      this.layer = this.parseInt(var1.getProperty("layer"), 0);
      this.weight = this.parseInt(var1.getProperty("weight"), 0);
      this.duration = this.parseFloat(var1.getProperty("duration"), 1.0F);
   }

   private static String parseName(String var0) {
      String var1 = var0;
      int var2 = var0.lastIndexOf(47);
      if (var2 >= 0) {
         var1 = var0.substring(var2 + 1);
      }

      int var3 = var1.lastIndexOf(46);
      if (var3 >= 0) {
         var1 = var1.substring(0, var3);
      }

      return var1;
   }

   private static String parseBasePath(String var0) {
      int var1 = var0.lastIndexOf(47);
      return var1 < 0 ? "" : var0.substring(0, var1);
   }

   private int parseType(String var1) {
      if (var1 == null) {
         return 1;
      } else if (var1.equals("item")) {
         return 1;
      } else if (var1.equals("enchantment")) {
         return 2;
      } else if (var1.equals("armor")) {
         return 3;
      } else {
         Config.warn("Unknown method: " + var1);
         return 0;
      }
   }

   private int[] parseItems(String var1, String var2) {
      if (var1 == null) {
         var1 = var2;
      }

      if (var1 == null) {
         return null;
      } else {
         var1 = var1.trim();
         TreeSet var3 = new TreeSet();
         String[] var4 = Config.tokenize(var1, " ");

         int var7;
         label57:
         for(int var5 = 0; var5 < var4.length; ++var5) {
            String var6 = var4[var5];
            var7 = Config.parseInt(var6, -1);
            if (var7 >= 0) {
               var3.add(new Integer(var7));
            } else {
               int var9;
               if (var6.contains("-")) {
                  String[] var8 = Config.tokenize(var6, "-");
                  if (var8.length == 2) {
                     var9 = Config.parseInt(var8[0], -1);
                     int var10 = Config.parseInt(var8[1], -1);
                     if (var9 >= 0 && var10 >= 0) {
                        int var11 = Math.min(var9, var10);
                        int var12 = Math.max(var9, var10);
                        int var13 = var11;

                        while(true) {
                           if (var13 > var12) {
                              continue label57;
                           }

                           var3.add(new Integer(var13));
                           ++var13;
                        }
                     }
                  }
               }

               Item var16 = Item.getByNameOrId(var6);
               if (var16 == null) {
                  Config.warn("Item not found: " + var6);
               } else {
                  var9 = Item.getIdFromItem(var16);
                  if (var9 < 0) {
                     Config.warn("Item not found: " + var6);
                  } else {
                     var3.add(new Integer(var9));
                  }
               }
            }
         }

         Integer[] var14 = (Integer[])var3.toArray(new Integer[var3.size()]);
         int[] var15 = new int[var14.length];

         for(var7 = 0; var7 < var15.length; ++var7) {
            var15[var7] = var14[var7];
         }

         return var15;
      }
   }

   private static String parseTexture(String var0, String var1, String var2, String var3, String var4, int var5, Map var6) {
      if (var0 == null) {
         var0 = var1;
      }

      if (var0 == null) {
         var0 = var2;
      }

      String var7;
      if (var0 != null) {
         var7 = ".png";
         if (var0.endsWith(var7)) {
            var0 = var0.substring(0, var0.length() - var7.length());
         }

         var0 = fixTextureName(var0, var4);
         return var0;
      } else if (var5 == 3) {
         return null;
      } else {
         if (var6 != null) {
            var7 = (String)var6.get("texture.bow_standby");
            if (var7 != null) {
               return var7;
            }
         }

         var7 = var3;
         int var8 = var3.lastIndexOf(47);
         if (var8 >= 0) {
            var7 = var3.substring(var8 + 1);
         }

         int var9 = var7.lastIndexOf(46);
         if (var9 >= 0) {
            var7 = var7.substring(0, var9);
         }

         var7 = fixTextureName(var7, var4);
         return var7;
      }
   }

   private static Map parseTextures(Properties var0, String var1) {
      String var2 = "texture.";
      Map var3 = getMatchingProperties(var0, var2);
      if (var3.size() <= 0) {
         return null;
      } else {
         Set var4 = var3.keySet();
         LinkedHashMap var5 = new LinkedHashMap();
         Iterator var7 = var4.iterator();

         while(var7.hasNext()) {
            Object var6 = var7.next();
            String var8 = (String)var3.get(var6);
            var8 = fixTextureName(var8, var1);
            var5.put(var6, var8);
         }

         return var5;
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

   private int parseInt(String var1, int var2) {
      if (var1 == null) {
         return var2;
      } else {
         var1 = var1.trim();
         int var3 = Config.parseInt(var1, Integer.MIN_VALUE);
         if (var3 == Integer.MIN_VALUE) {
            Config.warn("Invalid integer: " + var1);
            return var2;
         } else {
            return var3;
         }
      }
   }

   private float parseFloat(String var1, float var2) {
      if (var1 == null) {
         return var2;
      } else {
         var1 = var1.trim();
         float var3 = Config.parseFloat(var1, Float.MIN_VALUE);
         if (var3 == Float.MIN_VALUE) {
            Config.warn("Invalid float: " + var1);
            return var2;
         } else {
            return var3;
         }
      }
   }

   private RangeListInt parseRangeListInt(String var1) {
      if (var1 == null) {
         return null;
      } else {
         String[] var2 = Config.tokenize(var1, " ");
         RangeListInt var3 = new RangeListInt();

         for(int var4 = 0; var4 < var2.length; ++var4) {
            String var5 = var2[var4];
            RangeInt var6 = this.parseRangeInt(var5);
            if (var6 == null) {
               Config.warn("Invalid range list: " + var1);
               return null;
            }

            var3.addRange(var6);
         }

         return var3;
      }
   }

   private RangeInt parseRangeInt(String var1) {
      if (var1 == null) {
         return null;
      } else {
         var1 = var1.trim();
         int var2 = var1.length() - var1.replace("-", "").length();
         if (var2 > 1) {
            Config.warn("Invalid range: " + var1);
            return null;
         } else {
            String[] var3 = Config.tokenize(var1, "- ");
            int[] var4 = new int[var3.length];

            int var5;
            for(var5 = 0; var5 < var3.length; ++var5) {
               String var6 = var3[var5];
               int var7 = Config.parseInt(var6, -1);
               if (var7 < 0) {
                  Config.warn("Invalid range: " + var1);
                  return null;
               }

               var4[var5] = var7;
            }

            if (var4.length == 1) {
               var5 = var4[0];
               if (var1.startsWith("-")) {
                  return new RangeInt(0, var5);
               } else {
                  return var1.endsWith("-") ? new RangeInt(var5, 255) : new RangeInt(var5, var5);
               }
            } else if (var4.length == 2) {
               var5 = Math.min(var4[0], var4[1]);
               int var8 = Math.max(var4[0], var4[1]);
               return new RangeInt(var5, var8);
            } else {
               Config.warn("Invalid range: " + var1);
               return null;
            }
         }
      }
   }

   private NbtTagValue[] parseNbtTagValues(Properties var1) {
      String var2 = "nbt.";
      Map var3 = getMatchingProperties(var1, var2);
      if (var3.size() <= 0) {
         return null;
      } else {
         ArrayList var4 = new ArrayList();
         Iterator var6 = var3.keySet().iterator();

         while(var6.hasNext()) {
            Object var5 = var6.next();
            String var7 = (String)var3.get(var5);
            String var8 = ((String)var5).substring(var2.length());
            NbtTagValue var9 = new NbtTagValue(var8, var7);
            var4.add(var9);
         }

         NbtTagValue[] var10 = (NbtTagValue[])var4.toArray(new NbtTagValue[var4.size()]);
         return var10;
      }
   }

   private static Map getMatchingProperties(Properties var0, String var1) {
      LinkedHashMap var2 = new LinkedHashMap();
      Iterator var4 = var0.keySet().iterator();

      while(var4.hasNext()) {
         Object var3 = var4.next();
         String var5 = var0.getProperty((String)var3);
         if (((String)var3).startsWith(var1)) {
            var2.put(var3, var5);
         }
      }

      return var2;
   }

   public boolean isValid(String var1) {
      if (this.name != null && this.name.length() > 0) {
         if (this.basePath == null) {
            Config.warn("No base path found: " + var1);
            return false;
         } else if (this.type == 0) {
            Config.warn("No type defined: " + var1);
            return false;
         } else if ((this.type == 1 || this.type == 3) && this.items == null) {
            Config.warn("No items defined: " + var1);
            return false;
         } else if (this.texture == null && this.mapTextures == null) {
            Config.warn("No texture specified: " + var1);
            return false;
         } else if (this.type == 2 && this.enchantmentIds == null) {
            Config.warn("No enchantmentIDs specified: " + var1);
            return false;
         } else {
            return true;
         }
      } else {
         Config.warn("No name found: " + var1);
         return false;
      }
   }

   public void updateIcons(TextureMap var1) {
      if (this.texture != null) {
         this.textureLocation = this.getTextureLocation(this.texture);
         if (this.type == 1) {
            ResourceLocation var2 = this.getSpriteLocation(this.textureLocation);
            this.sprite = var1.registerSprite(var2);
         }
      }

      if (this.mapTextures != null) {
         this.mapTextureLocations = new HashMap();
         this.mapSprites = new HashMap();
         Iterator var3 = this.mapTextures.keySet().iterator();

         while(var3.hasNext()) {
            String var8 = (String)var3.next();
            String var4 = (String)this.mapTextures.get(var8);
            ResourceLocation var5 = this.getTextureLocation(var4);
            this.mapTextureLocations.put(var8, var5);
            if (this.type == 1) {
               ResourceLocation var6 = this.getSpriteLocation(var5);
               TextureAtlasSprite var7 = var1.registerSprite(var6);
               this.mapSprites.put(var8, var7);
            }
         }
      }

   }

   private ResourceLocation getTextureLocation(String var1) {
      if (var1 == null) {
         return null;
      } else {
         ResourceLocation var2 = new ResourceLocation(var1);
         String var3 = var2.getResourceDomain();
         String var4 = var2.getResourcePath();
         if (!var4.contains("/")) {
            var4 = "textures/blocks/" + var4;
         }

         String var5 = var4 + ".png";
         ResourceLocation var6 = new ResourceLocation(var3, var5);
         boolean var7 = Config.hasResource(var6);
         if (!var7) {
            Config.warn("File not found: " + var5);
         }

         return var6;
      }
   }

   private ResourceLocation getSpriteLocation(ResourceLocation var1) {
      String var2 = var1.getResourcePath();
      var2 = StrUtils.removePrefix(var2, "textures/");
      var2 = StrUtils.removeSuffix(var2, ".png");
      ResourceLocation var3 = new ResourceLocation(var1.getResourceDomain(), var2);
      return var3;
   }

   public void updateModel(TextureMap var1, ItemModelGenerator var2) {
      String[] var3 = this.getModelTextures();
      boolean var4 = this.isUseTint();
      this.model = makeBakedModel(var1, var2, var3, var4);
      if (this.type == 1 && this.mapTextures != null) {
         Iterator var6 = this.mapTextures.keySet().iterator();

         while(true) {
            String var7;
            String var8;
            do {
               if (!var6.hasNext()) {
                  return;
               }

               String var5 = (String)var6.next();
               var7 = (String)this.mapTextures.get(var5);
               var8 = StrUtils.removePrefix(var5, "texture.");
            } while(!var8.startsWith("bow") && !var8.startsWith("fishing_rod"));

            String[] var9 = new String[]{var7};
            IBakedModel var10 = makeBakedModel(var1, var2, var9, var4);
            if (this.mapModels == null) {
               this.mapModels = new HashMap();
            }

            this.mapModels.put(var8, var10);
         }
      }
   }

   private boolean isUseTint() {
      return true;
   }

   private static IBakedModel makeBakedModel(TextureMap var0, ItemModelGenerator var1, String[] var2, boolean var3) {
      ModelBlock var4 = makeModelBlock(var2);
      ModelBlock var5 = var1.makeItemModel(var0, var4);
      IBakedModel var6 = bakeModel(var0, var5, var3);
      return var6;
   }

   private String[] getModelTextures() {
      if (this.type == 1 && this.items.length == 1) {
         Item var1 = Item.getItemById(this.items[0]);
         String var5;
         String var6;
         if (var1 == Items.potionitem && this.damage != null && this.damage.getCountRanges() > 0) {
            RangeInt var8 = this.damage.getRange(0);
            int var9 = var8.getMin();
            boolean var10 = (var9 & 16384) != 0;
            var5 = this.getMapTexture(this.mapTextures, "texture.potion_overlay", "items/potion_overlay");
            var6 = null;
            if (var10) {
               var6 = this.getMapTexture(this.mapTextures, "texture.potion_bottle_splash", "items/potion_bottle_splash");
            } else {
               var6 = this.getMapTexture(this.mapTextures, "texture.potion_bottle_drinkable", "items/potion_bottle_drinkable");
            }

            return new String[]{var5, var6};
         }

         if (var1 instanceof ItemArmor) {
            ItemArmor var2 = (ItemArmor)var1;
            if (var2.getArmorMaterial() == ItemArmor.ArmorMaterial.LEATHER) {
               String var3 = "leather";
               String var4 = "helmet";
               if (var2.armorType == 0) {
                  var4 = "helmet";
               }

               if (var2.armorType == 1) {
                  var4 = "chestplate";
               }

               if (var2.armorType == 2) {
                  var4 = "leggings";
               }

               if (var2.armorType == 3) {
                  var4 = "boots";
               }

               var5 = var3 + "_" + var4;
               var6 = this.getMapTexture(this.mapTextures, "texture." + var5, "items/" + var5);
               String var7 = this.getMapTexture(this.mapTextures, "texture." + var5 + "_overlay", "items/" + var5 + "_overlay");
               return new String[]{var6, var7};
            }
         }
      }

      return new String[]{this.texture};
   }

   private String getMapTexture(Map var1, String var2, String var3) {
      if (var1 == null) {
         return var3;
      } else {
         String var4 = (String)var1.get(var2);
         return var4 == null ? var3 : var4;
      }
   }

   private static ModelBlock makeModelBlock(String[] var0) {
      StringBuffer var1 = new StringBuffer();
      var1.append("{\"parent\": \"builtin/generated\",\"textures\": {");

      for(int var2 = 0; var2 < var0.length; ++var2) {
         String var3 = var0[var2];
         if (var2 > 0) {
            var1.append(", ");
         }

         var1.append("\"layer" + var2 + "\": \"" + var3 + "\"");
      }

      var1.append("}}");
      String var4 = var1.toString();
      ModelBlock var5 = ModelBlock.deserialize(var4);
      return var5;
   }

   private static IBakedModel bakeModel(TextureMap var0, ModelBlock var1, boolean var2) {
      ModelRotation var3 = ModelRotation.X0_Y0;
      boolean var4 = false;
      TextureAtlasSprite var5 = var0.getSpriteSafe(var1.resolveTextureName("particle"));
      SimpleBakedModel.Builder var6 = (new SimpleBakedModel.Builder(var1)).setTexture(var5);
      Iterator var8 = var1.getElements().iterator();

      while(var8.hasNext()) {
         BlockPart var7 = (BlockPart)var8.next();
         Iterator var10 = var7.mapFaces.keySet().iterator();

         while(var10.hasNext()) {
            EnumFacing var9 = (EnumFacing)var10.next();
            BlockPartFace var11 = (BlockPartFace)var7.mapFaces.get(var9);
            if (!var2) {
               var11 = new BlockPartFace(var11.cullFace, -1, var11.texture, var11.blockFaceUV);
            }

            TextureAtlasSprite var12 = var0.getSpriteSafe(var1.resolveTextureName(var11.texture));
            BakedQuad var13 = makeBakedQuad(var7, var11, var12, var9, var3, var4);
            if (var11.cullFace == null) {
               var6.addGeneralQuad(var13);
            } else {
               var6.addFaceQuad(var3.rotateFace(var11.cullFace), var13);
            }
         }
      }

      return var6.makeBakedModel();
   }

   private static BakedQuad makeBakedQuad(BlockPart var0, BlockPartFace var1, TextureAtlasSprite var2, EnumFacing var3, ModelRotation var4, boolean var5) {
      FaceBakery var6 = new FaceBakery();
      return var6.makeBakedQuad(var0.positionFrom, var0.positionTo, var1, var2, var3, var4, var0.partRotation, var5, var0.shade);
   }

   public String toString() {
      return this.basePath + "/" + this.name + ", type: " + this.type + ", items: [" + Config.arrayToString(this.items) + "], textture: " + this.texture;
   }

   public float getTextureWidth(TextureManager var1) {
      if (this.textureWidth <= 0) {
         if (this.textureLocation != null) {
            ITextureObject var2 = var1.getTexture(this.textureLocation);
            int var3 = var2.getGlTextureId();
            int var4 = GlStateManager.getBoundTexture();
            GlStateManager.bindTexture(var3);
            this.textureWidth = GL11.glGetTexLevelParameteri(3553, 0, 4096);
            GlStateManager.bindTexture(var4);
         }

         if (this.textureWidth <= 0) {
            this.textureWidth = 16;
         }
      }

      return (float)this.textureWidth;
   }

   public float getTextureHeight(TextureManager var1) {
      if (this.textureHeight <= 0) {
         if (this.textureLocation != null) {
            ITextureObject var2 = var1.getTexture(this.textureLocation);
            int var3 = var2.getGlTextureId();
            int var4 = GlStateManager.getBoundTexture();
            GlStateManager.bindTexture(var3);
            this.textureHeight = GL11.glGetTexLevelParameteri(3553, 0, 4097);
            GlStateManager.bindTexture(var4);
         }

         if (this.textureHeight <= 0) {
            this.textureHeight = 16;
         }
      }

      return (float)this.textureHeight;
   }

   public IBakedModel getModel(ModelResourceLocation var1) {
      if (var1 != null && this.mapTextures != null) {
         String var2 = var1.getResourcePath();
         if (this.mapModels != null) {
            IBakedModel var3 = (IBakedModel)this.mapModels.get(var2);
            if (var3 != null) {
               return var3;
            }
         }
      }

      return this.model;
   }
}
