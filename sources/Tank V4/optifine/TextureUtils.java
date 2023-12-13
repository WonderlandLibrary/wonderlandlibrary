package optifine;

import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;
import java.awt.image.ImageObserver;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.IntBuffer;
import java.util.Iterator;
import javax.imageio.ImageIO;
import javax.imageio.ImageReader;
import javax.imageio.stream.ImageInputStream;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GLAllocation;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.texture.ITextureObject;
import net.minecraft.client.renderer.texture.ITickableTextureObject;
import net.minecraft.client.renderer.texture.SimpleTexture;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.client.resources.IReloadableResourceManager;
import net.minecraft.client.resources.IResourceManager;
import net.minecraft.client.resources.IResourceManagerReloadListener;
import net.minecraft.util.MathHelper;
import net.minecraft.util.ResourceLocation;
import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GLContext;
import shadersmod.client.MultiTexID;
import shadersmod.client.Shaders;

public class TextureUtils {
   public static final String texGrassTop = "grass_top";
   public static final String texStone = "stone";
   public static final String texDirt = "dirt";
   public static final String texCoarseDirt = "coarse_dirt";
   public static final String texGrassSide = "grass_side";
   public static final String texStoneslabSide = "stone_slab_side";
   public static final String texStoneslabTop = "stone_slab_top";
   public static final String texBedrock = "bedrock";
   public static final String texSand = "sand";
   public static final String texGravel = "gravel";
   public static final String texLogOak = "log_oak";
   public static final String texLogBigOak = "log_big_oak";
   public static final String texLogAcacia = "log_acacia";
   public static final String texLogSpruce = "log_spruce";
   public static final String texLogBirch = "log_birch";
   public static final String texLogJungle = "log_jungle";
   public static final String texLogOakTop = "log_oak_top";
   public static final String texLogBigOakTop = "log_big_oak_top";
   public static final String texLogAcaciaTop = "log_acacia_top";
   public static final String texLogSpruceTop = "log_spruce_top";
   public static final String texLogBirchTop = "log_birch_top";
   public static final String texLogJungleTop = "log_jungle_top";
   public static final String texLeavesOak = "leaves_oak";
   public static final String texLeavesBigOak = "leaves_big_oak";
   public static final String texLeavesAcacia = "leaves_acacia";
   public static final String texLeavesBirch = "leaves_birch";
   public static final String texLeavesSpuce = "leaves_spruce";
   public static final String texLeavesJungle = "leaves_jungle";
   public static final String texGoldOre = "gold_ore";
   public static final String texIronOre = "iron_ore";
   public static final String texCoalOre = "coal_ore";
   public static final String texObsidian = "obsidian";
   public static final String texGrassSideOverlay = "grass_side_overlay";
   public static final String texSnow = "snow";
   public static final String texGrassSideSnowed = "grass_side_snowed";
   public static final String texMyceliumSide = "mycelium_side";
   public static final String texMyceliumTop = "mycelium_top";
   public static final String texDiamondOre = "diamond_ore";
   public static final String texRedstoneOre = "redstone_ore";
   public static final String texLapisOre = "lapis_ore";
   public static final String texCactusSide = "cactus_side";
   public static final String texClay = "clay";
   public static final String texFarmlandWet = "farmland_wet";
   public static final String texFarmlandDry = "farmland_dry";
   public static final String texNetherrack = "netherrack";
   public static final String texSoulSand = "soul_sand";
   public static final String texGlowstone = "glowstone";
   public static final String texLeavesSpruce = "leaves_spruce";
   public static final String texLeavesSpruceOpaque = "leaves_spruce_opaque";
   public static final String texEndStone = "end_stone";
   public static final String texSandstoneTop = "sandstone_top";
   public static final String texSandstoneBottom = "sandstone_bottom";
   public static final String texRedstoneLampOff = "redstone_lamp_off";
   public static final String texRedstoneLampOn = "redstone_lamp_on";
   public static final String texWaterStill = "water_still";
   public static final String texWaterFlow = "water_flow";
   public static final String texLavaStill = "lava_still";
   public static final String texLavaFlow = "lava_flow";
   public static final String texFireLayer0 = "fire_layer_0";
   public static final String texFireLayer1 = "fire_layer_1";
   public static final String texPortal = "portal";
   public static final String texGlass = "glass";
   public static final String texGlassPaneTop = "glass_pane_top";
   public static final String texCompass = "compass";
   public static final String texClock = "clock";
   public static TextureAtlasSprite iconGrassTop;
   public static TextureAtlasSprite iconGrassSide;
   public static TextureAtlasSprite iconGrassSideOverlay;
   public static TextureAtlasSprite iconSnow;
   public static TextureAtlasSprite iconGrassSideSnowed;
   public static TextureAtlasSprite iconMyceliumSide;
   public static TextureAtlasSprite iconMyceliumTop;
   public static TextureAtlasSprite iconWaterStill;
   public static TextureAtlasSprite iconWaterFlow;
   public static TextureAtlasSprite iconLavaStill;
   public static TextureAtlasSprite iconLavaFlow;
   public static TextureAtlasSprite iconPortal;
   public static TextureAtlasSprite iconFireLayer0;
   public static TextureAtlasSprite iconFireLayer1;
   public static TextureAtlasSprite iconGlass;
   public static TextureAtlasSprite iconGlassPaneTop;
   public static TextureAtlasSprite iconCompass;
   public static TextureAtlasSprite iconClock;
   public static final String SPRITE_PREFIX_BLOCKS = "minecraft:blocks/";
   public static final String SPRITE_PREFIX_ITEMS = "minecraft:items/";
   private static IntBuffer staticBuffer = GLAllocation.createDirectIntBuffer(256);

   public static void update() {
      TextureMap var0 = getTextureMapBlocks();
      if (var0 != null) {
         String var1 = "minecraft:blocks/";
         iconGrassTop = var0.getSpriteSafe(var1 + "grass_top");
         iconGrassSide = var0.getSpriteSafe(var1 + "grass_side");
         iconGrassSideOverlay = var0.getSpriteSafe(var1 + "grass_side_overlay");
         iconSnow = var0.getSpriteSafe(var1 + "snow");
         iconGrassSideSnowed = var0.getSpriteSafe(var1 + "grass_side_snowed");
         iconMyceliumSide = var0.getSpriteSafe(var1 + "mycelium_side");
         iconMyceliumTop = var0.getSpriteSafe(var1 + "mycelium_top");
         iconWaterStill = var0.getSpriteSafe(var1 + "water_still");
         iconWaterFlow = var0.getSpriteSafe(var1 + "water_flow");
         iconLavaStill = var0.getSpriteSafe(var1 + "lava_still");
         iconLavaFlow = var0.getSpriteSafe(var1 + "lava_flow");
         iconFireLayer0 = var0.getSpriteSafe(var1 + "fire_layer_0");
         iconFireLayer1 = var0.getSpriteSafe(var1 + "fire_layer_1");
         iconPortal = var0.getSpriteSafe(var1 + "portal");
         iconGlass = var0.getSpriteSafe(var1 + "glass");
         iconGlassPaneTop = var0.getSpriteSafe(var1 + "glass_pane_top");
         String var2 = "minecraft:items/";
         iconCompass = var0.getSpriteSafe(var2 + "compass");
         iconClock = var0.getSpriteSafe(var2 + "clock");
      }

   }

   public static BufferedImage fixTextureDimensions(String var0, BufferedImage var1) {
      if (var0.startsWith("/mob/zombie") || var0.startsWith("/mob/pigzombie")) {
         int var2 = var1.getWidth();
         int var3 = var1.getHeight();
         if (var2 == var3 * 2) {
            BufferedImage var4 = new BufferedImage(var2, var3 * 2, 2);
            Graphics2D var5 = var4.createGraphics();
            var5.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);
            var5.drawImage(var1, 0, 0, var2, var3, (ImageObserver)null);
            return var4;
         }
      }

      return var1;
   }

   public static int ceilPowerOfTwo(int var0) {
      int var1;
      for(var1 = 1; var1 < var0; var1 *= 2) {
      }

      return var1;
   }

   public static int getPowerOfTwo(int var0) {
      int var1 = 1;

      int var2;
      for(var2 = 0; var1 < var0; ++var2) {
         var1 *= 2;
      }

      return var2;
   }

   public static int twoToPower(int var0) {
      int var1 = 1;

      for(int var2 = 0; var2 < var0; ++var2) {
         var1 *= 2;
      }

      return var1;
   }

   public static ITextureObject getTexture(ResourceLocation var0) {
      ITextureObject var1 = Config.getTextureManager().getTexture(var0);
      if (var1 != null) {
         return var1;
      } else if (!Config.hasResource(var0)) {
         return null;
      } else {
         SimpleTexture var2 = new SimpleTexture(var0);
         Config.getTextureManager().loadTexture(var0, var2);
         return var2;
      }
   }

   public static void resourcesReloaded(IResourceManager var0) {
      if (getTextureMapBlocks() != null) {
         Config.dbg("*** Reloading custom textures ***");
         CustomSky.reset();
         TextureAnimations.reset();
         update();
         NaturalTextures.update();
         BetterGrass.update();
         BetterSnow.update();
         TextureAnimations.update();
         CustomColors.update();
         CustomSky.update();
         RandomMobs.resetTextures();
         CustomItems.updateModels();
         Shaders.resourcesReloaded();
         Lang.resourcesReloaded();
         Config.updateTexturePackClouds();
         SmartLeaves.updateLeavesModels();
         Config.getTextureManager().tick();
      }

   }

   public static TextureMap getTextureMapBlocks() {
      return Minecraft.getMinecraft().getTextureMapBlocks();
   }

   public static void registerResourceListener() {
      IResourceManager var0 = Config.getResourceManager();
      if (var0 instanceof IReloadableResourceManager) {
         IReloadableResourceManager var1 = (IReloadableResourceManager)var0;
         IResourceManagerReloadListener var2 = new IResourceManagerReloadListener() {
            public void onResourceManagerReload(IResourceManager var1) {
               TextureUtils.resourcesReloaded(var1);
            }
         };
         var1.registerReloadListener(var2);
      }

      ITickableTextureObject var3 = new ITickableTextureObject() {
         public void tick() {
            TextureAnimations.updateCustomAnimations();
         }

         public void loadTexture(IResourceManager var1) throws IOException {
         }

         public int getGlTextureId() {
            return 0;
         }

         public void setBlurMipmap(boolean var1, boolean var2) {
         }

         public void restoreLastBlurMipmap() {
         }

         public MultiTexID getMultiTexID() {
            return null;
         }
      };
      ResourceLocation var4 = new ResourceLocation("optifine/TickableTextures");
      Config.getTextureManager().loadTickableTexture(var4, var3);
   }

   public static String fixResourcePath(String var0, String var1) {
      String var2 = "assets/minecraft/";
      if (var0.startsWith(var2)) {
         var0 = var0.substring(var2.length());
         return var0;
      } else if (var0.startsWith("./")) {
         var0 = var0.substring(2);
         if (!var1.endsWith("/")) {
            var1 = var1 + "/";
         }

         var0 = var1 + var0;
         return var0;
      } else {
         if (var0.startsWith("/~")) {
            var0 = var0.substring(1);
         }

         String var3 = "mcpatcher/";
         if (var0.startsWith("~/")) {
            var0 = var0.substring(2);
            var0 = var3 + var0;
            return var0;
         } else if (var0.startsWith("/")) {
            var0 = var3 + var0.substring(1);
            return var0;
         } else {
            return var0;
         }
      }
   }

   public static String getBasePath(String var0) {
      int var1 = var0.lastIndexOf(47);
      return var1 < 0 ? "" : var0.substring(0, var1);
   }

   public static void applyAnisotropicLevel() {
      if (GLContext.getCapabilities().GL_EXT_texture_filter_anisotropic) {
         float var0 = GL11.glGetFloat(34047);
         float var1 = (float)Config.getAnisotropicFilterLevel();
         var1 = Math.min(var1, var0);
         GL11.glTexParameterf(3553, 34046, var1);
      }

   }

   public static void bindTexture(int var0) {
      GlStateManager.bindTexture(var0);
   }

   public static boolean isPowerOfTwo(int var0) {
      int var1 = MathHelper.roundUpToPowerOfTwo(var0);
      return var1 == var0;
   }

   public static BufferedImage scaleImage(BufferedImage var0, int var1) {
      int var2 = var0.getWidth();
      int var3 = var0.getHeight();
      int var4 = var3 * var1 / var2;
      BufferedImage var5 = new BufferedImage(var1, var4, 2);
      Graphics2D var6 = var5.createGraphics();
      Object var7 = RenderingHints.VALUE_INTERPOLATION_NEAREST_NEIGHBOR;
      if (var1 < var2 || var1 % var2 != 0) {
         var7 = RenderingHints.VALUE_INTERPOLATION_BILINEAR;
      }

      var6.setRenderingHint(RenderingHints.KEY_INTERPOLATION, var7);
      var6.drawImage(var0, 0, 0, var1, var4, (ImageObserver)null);
      return var5;
   }

   public static BufferedImage scaleToPowerOfTwo(BufferedImage var0, int var1) {
      if (var0 == null) {
         return var0;
      } else {
         int var2 = var0.getWidth();
         int var3 = var0.getHeight();
         int var4 = Math.max(var2, var1);
         var4 = MathHelper.roundUpToPowerOfTwo(var4);
         if (var4 == var2) {
            return var0;
         } else {
            int var5 = var3 * var4 / var2;
            BufferedImage var6 = new BufferedImage(var4, var5, 2);
            Graphics2D var7 = var6.createGraphics();
            Object var8 = RenderingHints.VALUE_INTERPOLATION_NEAREST_NEIGHBOR;
            if (var4 % var2 != 0) {
               var8 = RenderingHints.VALUE_INTERPOLATION_BILINEAR;
            }

            var7.setRenderingHint(RenderingHints.KEY_INTERPOLATION, var8);
            var7.drawImage(var0, 0, 0, var4, var5, (ImageObserver)null);
            return var6;
         }
      }
   }

   public static BufferedImage scaleMinTo(BufferedImage var0, int var1) {
      if (var0 == null) {
         return var0;
      } else {
         int var2 = var0.getWidth();
         int var3 = var0.getHeight();
         if (var2 >= var1) {
            return var0;
         } else {
            int var4;
            for(var4 = var2; var4 < var1; var4 *= 2) {
            }

            int var5 = var3 * var4 / var2;
            BufferedImage var6 = new BufferedImage(var4, var5, 2);
            Graphics2D var7 = var6.createGraphics();
            Object var8 = RenderingHints.VALUE_INTERPOLATION_NEAREST_NEIGHBOR;
            var7.setRenderingHint(RenderingHints.KEY_INTERPOLATION, var8);
            var7.drawImage(var0, 0, 0, var4, var5, (ImageObserver)null);
            return var6;
         }
      }
   }

   public static Dimension getImageSize(InputStream var0, String var1) {
      Iterator var2 = ImageIO.getImageReadersBySuffix(var1);

      while(true) {
         if (var2.hasNext()) {
            ImageReader var3 = (ImageReader)var2.next();

            Dimension var4;
            try {
               ImageInputStream var5 = ImageIO.createImageInputStream(var0);
               var3.setInput(var5);
               int var6 = var3.getWidth(var3.getMinIndex());
               int var7 = var3.getHeight(var3.getMinIndex());
               var4 = new Dimension(var6, var7);
            } catch (IOException var9) {
               var3.dispose();
               continue;
            }

            var3.dispose();
            return var4;
         }

         return null;
      }
   }

   public static void dbgMipmaps(TextureAtlasSprite var0) {
      int[][] var1 = var0.getFrameTextureData(0);

      for(int var2 = 0; var2 < var1.length; ++var2) {
         int[] var3 = var1[var2];
         if (var3 == null) {
            Config.dbg(var2 + ": " + var3);
         } else {
            Config.dbg(var2 + ": " + var3.length);
         }
      }

   }

   public static void saveGlTexture(String var0, int var1, int var2, int var3, int var4) {
      bindTexture(var1);
      GL11.glPixelStorei(3333, 1);
      GL11.glPixelStorei(3317, 1);
      File var5 = new File(var0);
      File var6 = var5.getParentFile();
      if (var6 != null) {
         var6.mkdirs();
      }

      int var7;
      File var8;
      for(var7 = 0; var7 < 16; ++var7) {
         var8 = new File(var0 + "_" + var7 + ".png");
         var8.delete();
      }

      for(var7 = 0; var7 <= var2; ++var7) {
         var8 = new File(var0 + "_" + var7 + ".png");
         int var9 = var3 >> var7;
         int var10 = var4 >> var7;
         int var11 = var9 * var10;
         IntBuffer var12 = BufferUtils.createIntBuffer(var11);
         int[] var13 = new int[var11];
         GL11.glGetTexImage(3553, var7, 32993, 33639, (IntBuffer)var12);
         var12.get(var13);
         BufferedImage var14 = new BufferedImage(var9, var10, 2);
         var14.setRGB(0, 0, var9, var10, var13, 0, var9);

         try {
            ImageIO.write(var14, "png", var8);
            Config.dbg("Exported: " + var8);
         } catch (Exception var16) {
            Config.warn("Error writing: " + var8);
            Config.warn(var16.getClass().getName() + ": " + var16.getMessage());
         }
      }

   }

   public static int getGLMaximumTextureSize() {
      for(int var0 = 65536; var0 > 0; var0 >>= 1) {
         GL11.glTexImage2D(32868, 0, 6408, var0, var0, 0, 6408, 5121, (IntBuffer)null);
         int var1 = GL11.glGetError();
         int var2 = GL11.glGetTexLevelParameteri(32868, 0, 4096);
         if (var2 != 0) {
            return var0;
         }
      }

      return -1;
   }
}
