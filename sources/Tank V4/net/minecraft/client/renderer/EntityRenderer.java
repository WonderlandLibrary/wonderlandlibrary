package net.minecraft.client.renderer;

import com.google.common.base.Predicates;
import com.google.gson.JsonSyntaxException;
import java.io.IOException;
import java.lang.reflect.Field;
import java.nio.FloatBuffer;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Random;
import java.util.concurrent.Callable;
import my.NewSnake.Tank.module.modules.PLAYER.NoHurtCam;
import my.NewSnake.event.events.Render3DEvent;
import net.minecraft.block.Block;
import net.minecraft.block.BlockBed;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.AbstractClientPlayer;
import net.minecraft.client.gui.GuiDownloadTerrain;
import net.minecraft.client.gui.GuiMainMenu;
import net.minecraft.client.gui.MapItemRenderer;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.multiplayer.WorldClient;
import net.minecraft.client.particle.EffectRenderer;
import net.minecraft.client.renderer.culling.ClippingHelperImpl;
import net.minecraft.client.renderer.culling.Frustum;
import net.minecraft.client.renderer.texture.DynamicTexture;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.client.renderer.tileentity.TileEntityRendererDispatcher;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.client.resources.I18n;
import net.minecraft.client.resources.IResourceManager;
import net.minecraft.client.resources.IResourceManagerReloadListener;
import net.minecraft.client.settings.GameSettings;
import net.minecraft.client.shader.ShaderGroup;
import net.minecraft.client.shader.ShaderLinkHelper;
import net.minecraft.crash.CrashReport;
import net.minecraft.crash.CrashReportCategory;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.boss.BossStatus;
import net.minecraft.entity.item.EntityItemFrame;
import net.minecraft.entity.monster.EntityCreeper;
import net.minecraft.entity.monster.EntityEnderman;
import net.minecraft.entity.monster.EntitySpider;
import net.minecraft.entity.passive.EntityAnimal;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.Potion;
import net.minecraft.server.integrated.IntegratedServer;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.BlockPos;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.EntitySelectors;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.EnumWorldBlockLayer;
import net.minecraft.util.MathHelper;
import net.minecraft.util.MouseFilter;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.util.ReportedException;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.Vec3;
import net.minecraft.world.World;
import net.minecraft.world.WorldProvider;
import net.minecraft.world.WorldSettings;
import net.minecraft.world.biome.BiomeGenBase;
import optifine.Config;
import optifine.CustomColors;
import optifine.Lagometer;
import optifine.RandomMobs;
import optifine.Reflector;
import optifine.ReflectorForge;
import optifine.TextureUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GLContext;
import org.lwjgl.util.glu.GLU;
import org.lwjgl.util.glu.Project;
import shadersmod.client.Shaders;
import shadersmod.client.ShadersRender;

public class EntityRenderer implements IResourceManagerReloadListener {
   public float fogColorGreen;
   private float avgServerTimeDiff = 0.0F;
   public static final int shaderCount;
   private float torchFlickerX;
   private static final String __OBFID = "CL_00000947";
   private MouseFilter mouseFilterYAxis = new MouseFilter();
   private float[] rainXCoords = new float[1024];
   private float fogColor2;
   private boolean debugView = false;
   public static boolean anaglyphEnable;
   public ItemRenderer itemRenderer;
   private float[] rainYCoords = new float[1024];
   public boolean fogStandard = false;
   private final DynamicTexture lightmapTexture;
   private final MapItemRenderer theMapItemRenderer;
   private Entity pointedEntity;
   private World updatedWorld = null;
   private double cameraPitch;
   private float smoothCamYaw;
   private ShaderGroup[] fxaaShaders = new ShaderGroup[10];
   private boolean showDebugInfo = false;
   public float fogColorRed;
   public int frameCount;
   private final ResourceLocation locationLightMap;
   private static final Logger logger = LogManager.getLogger();
   private boolean useShader;
   public static int anaglyphField;
   private int lastServerTicks = 0;
   private int serverWaitTimeCurrent = 0;
   private long lastErrorCheckTimeMs = 0L;
   private float smoothCamFilterY;
   private double cameraYaw;
   private int serverWaitTime = 0;
   private float fovModifierHandPrev;
   private float bossColorModifierPrev;
   private float thirdPersonDistanceTemp = 4.0F;
   public static final ResourceLocation[] shaderResourceLocations = new ResourceLocation[]{new ResourceLocation("shaders/post/notch.json"), new ResourceLocation("shaders/post/fxaa.json"), new ResourceLocation("shaders/post/art.json"), new ResourceLocation("shaders/post/bumpy.json"), new ResourceLocation("shaders/post/blobs2.json"), new ResourceLocation("shaders/post/pencil.json"), new ResourceLocation("shaders/post/color_convolve.json"), new ResourceLocation("shaders/post/deconverge.json"), new ResourceLocation("shaders/post/flip.json"), new ResourceLocation("shaders/post/invert.json"), new ResourceLocation("shaders/post/ntsc.json"), new ResourceLocation("shaders/post/outline.json"), new ResourceLocation("shaders/post/phosphor.json"), new ResourceLocation("shaders/post/scan_pincushion.json"), new ResourceLocation("shaders/post/sobel.json"), new ResourceLocation("shaders/post/bits.json"), new ResourceLocation("shaders/post/desaturate.json"), new ResourceLocation("shaders/post/green.json"), new ResourceLocation("shaders/post/blur.json"), new ResourceLocation("shaders/post/wobble.json"), new ResourceLocation("shaders/post/blobs.json"), new ResourceLocation("shaders/post/antialias.json"), new ResourceLocation("shaders/post/creeper.json"), new ResourceLocation("shaders/post/spider.json")};
   private Minecraft mc;
   private float smoothCamPitch;
   private double cameraZoom = 1.0D;
   private float farPlaneDistance;
   private long renderEndNanoTime;
   private int shaderIndex;
   private MouseFilter mouseFilterXAxis = new MouseFilter();
   private float bossColorModifier;
   private float smoothCamFilterX;
   private boolean drawBlockOutline = true;
   private int debugViewDirection = 0;
   private boolean lightmapUpdateNeeded;
   private final int[] lightmapColors;
   private float fovModifierHand;
   private int rendererUpdateCount;
   private int rainSoundCounter;
   private boolean initialized = false;
   private float smoothCamPartialTicks;
   private float fogColor1;
   private boolean renderHand = true;
   private float avgServerTickDiff = 0.0F;
   private static final ResourceLocation locationSnowPng = new ResourceLocation("textures/environment/snow.png");
   private float thirdPersonDistance = 4.0F;
   private Random random = new Random();
   public ShaderGroup theShaderGroup;
   private final IResourceManager resourceManager;
   private float clipDistance = 128.0F;
   private float torchFlickerDX;
   private boolean cloudFog;
   private long prevFrameTime = Minecraft.getSystemTime();
   public float fogColorBlue;
   private long lastServerTime = 0L;
   private static final ResourceLocation locationRainPng = new ResourceLocation("textures/environment/rain.png");
   private FloatBuffer fogColorBuffer = GLAllocation.createDirectFloatBuffer(16);

   private void updateFogColor(float var1) {
      WorldClient var2 = Minecraft.theWorld;
      Entity var3 = this.mc.getRenderViewEntity();
      float var4 = 0.25F + 0.75F * (float)this.mc.gameSettings.renderDistanceChunks / 32.0F;
      var4 = 1.0F - (float)Math.pow((double)var4, 0.25D);
      Vec3 var5 = var2.getSkyColor(this.mc.getRenderViewEntity(), var1);
      var5 = CustomColors.getWorldSkyColor(var5, var2, this.mc.getRenderViewEntity(), var1);
      float var6 = (float)var5.xCoord;
      float var7 = (float)var5.yCoord;
      float var8 = (float)var5.zCoord;
      Vec3 var9 = var2.getFogColor(var1);
      var9 = CustomColors.getWorldFogColor(var9, var2, this.mc.getRenderViewEntity(), var1);
      this.fogColorRed = (float)var9.xCoord;
      this.fogColorGreen = (float)var9.yCoord;
      this.fogColorBlue = (float)var9.zCoord;
      float var13;
      if (this.mc.gameSettings.renderDistanceChunks >= 4) {
         double var10 = -1.0D;
         Vec3 var12 = MathHelper.sin(var2.getCelestialAngleRadians(var1)) > 0.0F ? new Vec3(var10, 0.0D, 0.0D) : new Vec3(1.0D, 0.0D, 0.0D);
         var13 = (float)var3.getLook(var1).dotProduct(var12);
         if (var13 < 0.0F) {
            var13 = 0.0F;
         }

         if (var13 > 0.0F) {
            float[] var14 = var2.provider.calcSunriseSunsetColors(var2.getCelestialAngle(var1), var1);
            if (var14 != null) {
               var13 *= var14[3];
               this.fogColorRed = this.fogColorRed * (1.0F - var13) + var14[0] * var13;
               this.fogColorGreen = this.fogColorGreen * (1.0F - var13) + var14[1] * var13;
               this.fogColorBlue = this.fogColorBlue * (1.0F - var13) + var14[2] * var13;
            }
         }
      }

      this.fogColorRed += (var6 - this.fogColorRed) * var4;
      this.fogColorGreen += (var7 - this.fogColorGreen) * var4;
      this.fogColorBlue += (var8 - this.fogColorBlue) * var4;
      float var21 = var2.getRainStrength(var1);
      float var11;
      float var22;
      if (var21 > 0.0F) {
         var11 = 1.0F - var21 * 0.5F;
         var22 = 1.0F - var21 * 0.4F;
         this.fogColorRed *= var11;
         this.fogColorGreen *= var11;
         this.fogColorBlue *= var22;
      }

      var11 = var2.getThunderStrength(var1);
      if (var11 > 0.0F) {
         var22 = 1.0F - var11 * 0.5F;
         this.fogColorRed *= var22;
         this.fogColorGreen *= var22;
         this.fogColorBlue *= var22;
      }

      Block var23 = ActiveRenderInfo.getBlockAtEntityViewpoint(Minecraft.theWorld, var3, var1);
      if (this.cloudFog) {
         Vec3 var24 = var2.getCloudColour(var1);
         this.fogColorRed = (float)var24.xCoord;
         this.fogColorGreen = (float)var24.yCoord;
         this.fogColorBlue = (float)var24.zCoord;
      } else if (var23.getMaterial() == Material.water) {
         var13 = (float)EnchantmentHelper.getRespiration(var3) * 0.2F;
         if (var3 instanceof EntityLivingBase && ((EntityLivingBase)var3).isPotionActive(Potion.waterBreathing)) {
            var13 = var13 * 0.3F + 0.6F;
         }

         this.fogColorRed = 0.02F + var13;
         this.fogColorGreen = 0.02F + var13;
         this.fogColorBlue = 0.2F + var13;
         Vec3 var25 = CustomColors.getUnderwaterColor(Minecraft.theWorld, this.mc.getRenderViewEntity().posX, this.mc.getRenderViewEntity().posY + 1.0D, this.mc.getRenderViewEntity().posZ);
         if (var25 != null) {
            this.fogColorRed = (float)var25.xCoord;
            this.fogColorGreen = (float)var25.yCoord;
            this.fogColorBlue = (float)var25.zCoord;
         }
      } else if (var23.getMaterial() == Material.lava) {
         this.fogColorRed = 0.6F;
         this.fogColorGreen = 0.1F;
         this.fogColorBlue = 0.0F;
      }

      var13 = this.fogColor2 + (this.fogColor1 - this.fogColor2) * var1;
      this.fogColorRed *= var13;
      this.fogColorGreen *= var13;
      this.fogColorBlue *= var13;
      double var26 = var2.provider.getVoidFogYFactor();
      double var16 = (var3.lastTickPosY + (var3.posY - var3.lastTickPosY) * (double)var1) * var26;
      if (var3 instanceof EntityLivingBase && ((EntityLivingBase)var3).isPotionActive(Potion.blindness)) {
         int var18 = ((EntityLivingBase)var3).getActivePotionEffect(Potion.blindness).getDuration();
         if (var18 < 20) {
            var16 *= (double)(1.0F - (float)var18 / 20.0F);
         } else {
            var16 = 0.0D;
         }
      }

      if (var16 < 1.0D) {
         if (var16 < 0.0D) {
            var16 = 0.0D;
         }

         var16 *= var16;
         this.fogColorRed = (float)((double)this.fogColorRed * var16);
         this.fogColorGreen = (float)((double)this.fogColorGreen * var16);
         this.fogColorBlue = (float)((double)this.fogColorBlue * var16);
      }

      float var27;
      if (this.bossColorModifier > 0.0F) {
         var27 = this.bossColorModifierPrev + (this.bossColorModifier - this.bossColorModifierPrev) * var1;
         this.fogColorRed = this.fogColorRed * (1.0F - var27) + this.fogColorRed * 0.7F * var27;
         this.fogColorGreen = this.fogColorGreen * (1.0F - var27) + this.fogColorGreen * 0.6F * var27;
         this.fogColorBlue = this.fogColorBlue * (1.0F - var27) + this.fogColorBlue * 0.6F * var27;
      }

      float var19;
      if (var3 instanceof EntityLivingBase && ((EntityLivingBase)var3).isPotionActive(Potion.nightVision)) {
         var27 = this.getNightVisionBrightness((EntityLivingBase)var3, var1);
         var19 = 1.0F / this.fogColorRed;
         if (var19 > 1.0F / this.fogColorGreen) {
            var19 = 1.0F / this.fogColorGreen;
         }

         if (var19 > 1.0F / this.fogColorBlue) {
            var19 = 1.0F / this.fogColorBlue;
         }

         this.fogColorRed = this.fogColorRed * (1.0F - var27) + this.fogColorRed * var19 * var27;
         this.fogColorGreen = this.fogColorGreen * (1.0F - var27) + this.fogColorGreen * var19 * var27;
         this.fogColorBlue = this.fogColorBlue * (1.0F - var27) + this.fogColorBlue * var19 * var27;
      }

      if (this.mc.gameSettings.anaglyph) {
         var27 = (this.fogColorRed * 30.0F + this.fogColorGreen * 59.0F + this.fogColorBlue * 11.0F) / 100.0F;
         var19 = (this.fogColorRed * 30.0F + this.fogColorGreen * 70.0F) / 100.0F;
         float var20 = (this.fogColorRed * 30.0F + this.fogColorBlue * 70.0F) / 100.0F;
         this.fogColorRed = var27;
         this.fogColorGreen = var19;
         this.fogColorBlue = var20;
      }

      if (Reflector.EntityViewRenderEvent_FogColors_Constructor.exists()) {
         Object var28 = Reflector.newInstance(Reflector.EntityViewRenderEvent_FogColors_Constructor, this, var3, var23, var1, this.fogColorRed, this.fogColorGreen, this.fogColorBlue);
         Reflector.postForgeBusEvent(var28);
         this.fogColorRed = Reflector.getFieldValueFloat(var28, Reflector.EntityViewRenderEvent_FogColors_red, this.fogColorRed);
         this.fogColorGreen = Reflector.getFieldValueFloat(var28, Reflector.EntityViewRenderEvent_FogColors_green, this.fogColorGreen);
         this.fogColorBlue = Reflector.getFieldValueFloat(var28, Reflector.EntityViewRenderEvent_FogColors_blue, this.fogColorBlue);
      }

      Shaders.setClearColor(this.fogColorRed, this.fogColorGreen, this.fogColorBlue, 0.0F);
   }

   public MapItemRenderer getMapItemRenderer() {
      return this.theMapItemRenderer;
   }

   public void func_181022_b() {
      if (this.theShaderGroup != null) {
         this.theShaderGroup.deleteShaderGroup();
      }

      this.theShaderGroup = null;
      this.shaderIndex = shaderCount;
   }

   private void frameInit() {
      if (!this.initialized) {
         TextureUtils.registerResourceListener();
         if (Config.getBitsOs() == 64 && Config.getBitsJre() == 32) {
            Config.setNotify64BitJava(true);
         }

         this.initialized = true;
      }

      Config.checkDisplayMode();
      WorldClient var1 = Minecraft.theWorld;
      if (var1 != null) {
         if (Config.getNewRelease() != null) {
            String var2 = "HD_U".replace("HD_U", "HD Ultra").replace("L", "Light");
            String var3 = var2 + " " + Config.getNewRelease();
            ChatComponentText var4 = new ChatComponentText(I18n.format("of.message.newVersion", var3));
            this.mc.ingameGUI.getChatGUI().printChatMessage(var4);
            Config.setNewRelease((String)null);
         }

         if (Config.isNotify64BitJava()) {
            Config.setNotify64BitJava(false);
            ChatComponentText var5 = new ChatComponentText(I18n.format("of.message.java64Bit"));
            this.mc.ingameGUI.getChatGUI().printChatMessage(var5);
         }
      }

      if (this.mc.currentScreen instanceof GuiMainMenu) {
         this.updateMainMenu((GuiMainMenu)this.mc.currentScreen);
      }

      if (this.updatedWorld != var1) {
         RandomMobs.worldChanged(this.updatedWorld, var1);
         Config.updateThreadPriorities();
         this.lastServerTime = 0L;
         this.lastServerTicks = 0;
         this.updatedWorld = var1;
      }

      if (Shaders.configAntialiasingLevel != 0) {
         Shaders.configAntialiasingLevel = 0;
      }

   }

   private FloatBuffer setFogColorBuffer(float var1, float var2, float var3, float var4) {
      if (Config.isShaders()) {
         Shaders.setFogColor(var1, var2, var3);
      }

      this.fogColorBuffer.clear();
      this.fogColorBuffer.put(var1).put(var2).put(var3).put(var4);
      this.fogColorBuffer.flip();
      return this.fogColorBuffer;
   }

   static Minecraft access$0(EntityRenderer var0) {
      return var0.mc;
   }

   public void renderWorld(ScaledResolution var1, float var2, long var3) {
      this.updateLightmap(var2);
      if (this.mc.getRenderViewEntity() == null) {
         this.mc.setRenderViewEntity(Minecraft.thePlayer);
      }

      this.getMouseOver(var2);
      if (Config.isShaders()) {
         Shaders.beginRender(this.mc, var2, var3);
      }

      GlStateManager.enableDepth();
      GlStateManager.enableAlpha();
      GlStateManager.alphaFunc(516, 0.1F);
      this.mc.mcProfiler.startSection("center");
      if (this.mc.gameSettings.anaglyph) {
         anaglyphField = 0;
         GlStateManager.colorMask(false, true, true, false);
         this.renderWorldPass(var1, 0, var2, var3);
         anaglyphField = 1;
         GlStateManager.colorMask(true, false, false, false);
         this.renderWorldPass(var1, 1, var2, var3);
         GlStateManager.colorMask(true, true, true, false);
      } else {
         this.renderWorldPass(var1, 2, var2, var3);
      }

      this.mc.mcProfiler.endSection();
   }

   private float getFOVModifier(float var1, boolean var2) {
      if (this.debugView) {
         return 90.0F;
      } else {
         Entity var3 = this.mc.getRenderViewEntity();
         float var4 = 70.0F;
         if (var2) {
            var4 = this.mc.gameSettings.fovSetting;
            if (Config.isDynamicFov()) {
               var4 *= this.fovModifierHandPrev + (this.fovModifierHand - this.fovModifierHandPrev) * var1;
            }
         }

         boolean var5 = false;
         if (this.mc.currentScreen == null) {
            GameSettings var6 = this.mc.gameSettings;
            var5 = GameSettings.isKeyDown(this.mc.gameSettings.ofKeyBindZoom);
         }

         if (var5) {
            if (!Config.zoomMode) {
               Config.zoomMode = true;
               this.mc.gameSettings.smoothCamera = true;
            }

            if (Config.zoomMode) {
               var4 /= 4.0F;
            }
         } else if (Config.zoomMode) {
            Config.zoomMode = false;
            this.mc.gameSettings.smoothCamera = false;
            this.mouseFilterXAxis = new MouseFilter();
            this.mouseFilterYAxis = new MouseFilter();
            this.mc.renderGlobal.displayListEntitiesDirty = true;
         }

         if (var3 instanceof EntityLivingBase && ((EntityLivingBase)var3).getHealth() <= 0.0F) {
            float var7 = (float)((EntityLivingBase)var3).deathTime + var1;
            var4 /= (1.0F - 500.0F / (var7 + 500.0F)) * 2.0F + 1.0F;
         }

         Block var8 = ActiveRenderInfo.getBlockAtEntityViewpoint(Minecraft.theWorld, var3, var1);
         if (var8.getMaterial() == Material.water) {
            var4 = var4 * 60.0F / 70.0F;
         }

         return var4;
      }
   }

   private void renderCloudsCheck(RenderGlobal var1, float var2, int var3) {
      if (this.mc.gameSettings.renderDistanceChunks >= 4 && !Config.isCloudsOff() && Shaders.shouldRenderClouds(this.mc.gameSettings)) {
         this.mc.mcProfiler.endStartSection("clouds");
         GlStateManager.matrixMode(5889);
         GlStateManager.loadIdentity();
         Project.gluPerspective(this.getFOVModifier(var2, true), (float)this.mc.displayWidth / (float)this.mc.displayHeight, 0.05F, this.clipDistance * 4.0F);
         GlStateManager.matrixMode(5888);
         GlStateManager.pushMatrix();
         this.setupFog(0, var2);
         var1.renderClouds(var2, var3);
         GlStateManager.disableFog();
         GlStateManager.popMatrix();
         GlStateManager.matrixMode(5889);
         GlStateManager.loadIdentity();
         Project.gluPerspective(this.getFOVModifier(var2, true), (float)this.mc.displayWidth / (float)this.mc.displayHeight, 0.05F, this.clipDistance);
         GlStateManager.matrixMode(5888);
      }

   }

   public void onResourceManagerReload(IResourceManager var1) {
      if (this.theShaderGroup != null) {
         this.theShaderGroup.deleteShaderGroup();
      }

      this.theShaderGroup = null;
      if (this.shaderIndex != shaderCount) {
         this.loadShader(shaderResourceLocations[this.shaderIndex]);
      } else {
         this.loadEntityShader(this.mc.getRenderViewEntity());
      }

   }

   private void updateLightmap(float var1) {
      if (this.lightmapUpdateNeeded) {
         this.mc.mcProfiler.startSection("lightTex");
         WorldClient var2 = Minecraft.theWorld;
         if (var2 != null) {
            if (Config.isCustomColors() && CustomColors.updateLightmap(var2, this.torchFlickerX, this.lightmapColors, Minecraft.thePlayer.isPotionActive(Potion.nightVision))) {
               this.lightmapTexture.updateDynamicTexture();
               this.lightmapUpdateNeeded = false;
               this.mc.mcProfiler.endSection();
               return;
            }

            float var3 = var2.getSunBrightness(1.0F);
            float var4 = var3 * 0.95F + 0.05F;

            for(int var5 = 0; var5 < 256; ++var5) {
               float var6 = var2.provider.getLightBrightnessTable()[var5 / 16] * var4;
               float var7 = var2.provider.getLightBrightnessTable()[var5 % 16] * (this.torchFlickerX * 0.1F + 1.5F);
               if (var2.getLastLightningBolt() > 0) {
                  var6 = var2.provider.getLightBrightnessTable()[var5 / 16];
               }

               float var8 = var6 * (var3 * 0.65F + 0.35F);
               float var9 = var6 * (var3 * 0.65F + 0.35F);
               float var10 = var7 * ((var7 * 0.6F + 0.4F) * 0.6F + 0.4F);
               float var11 = var7 * (var7 * var7 * 0.6F + 0.4F);
               float var12 = var8 + var7;
               float var13 = var9 + var10;
               float var14 = var6 + var11;
               var12 = var12 * 0.96F + 0.03F;
               var13 = var13 * 0.96F + 0.03F;
               var14 = var14 * 0.96F + 0.03F;
               float var15;
               if (this.bossColorModifier > 0.0F) {
                  var15 = this.bossColorModifierPrev + (this.bossColorModifier - this.bossColorModifierPrev) * var1;
                  var12 = var12 * (1.0F - var15) + var12 * 0.7F * var15;
                  var13 = var13 * (1.0F - var15) + var13 * 0.6F * var15;
                  var14 = var14 * (1.0F - var15) + var14 * 0.6F * var15;
               }

               if (var2.provider.getDimensionId() == 1) {
                  var12 = 0.22F + var7 * 0.75F;
                  var13 = 0.28F + var10 * 0.75F;
                  var14 = 0.25F + var11 * 0.75F;
               }

               float var16;
               if (Minecraft.thePlayer.isPotionActive(Potion.nightVision)) {
                  var15 = this.getNightVisionBrightness(Minecraft.thePlayer, var1);
                  var16 = 1.0F / var12;
                  if (var16 > 1.0F / var13) {
                     var16 = 1.0F / var13;
                  }

                  if (var16 > 1.0F / var14) {
                     var16 = 1.0F / var14;
                  }

                  var12 = var12 * (1.0F - var15) + var12 * var16 * var15;
                  var13 = var13 * (1.0F - var15) + var13 * var16 * var15;
                  var14 = var14 * (1.0F - var15) + var14 * var16 * var15;
               }

               if (var12 > 1.0F) {
                  var12 = 1.0F;
               }

               if (var13 > 1.0F) {
                  var13 = 1.0F;
               }

               if (var14 > 1.0F) {
                  var14 = 1.0F;
               }

               var15 = this.mc.gameSettings.gammaSetting;
               var16 = 1.0F - var12;
               float var17 = 1.0F - var13;
               float var18 = 1.0F - var14;
               var16 = 1.0F - var16 * var16 * var16 * var16;
               var17 = 1.0F - var17 * var17 * var17 * var17;
               var18 = 1.0F - var18 * var18 * var18 * var18;
               var12 = var12 * (1.0F - var15) + var16 * var15;
               var13 = var13 * (1.0F - var15) + var17 * var15;
               var14 = var14 * (1.0F - var15) + var18 * var15;
               var12 = var12 * 0.96F + 0.03F;
               var13 = var13 * 0.96F + 0.03F;
               var14 = var14 * 0.96F + 0.03F;
               if (var12 > 1.0F) {
                  var12 = 1.0F;
               }

               if (var13 > 1.0F) {
                  var13 = 1.0F;
               }

               if (var14 > 1.0F) {
                  var14 = 1.0F;
               }

               if (var12 < 0.0F) {
                  var12 = 0.0F;
               }

               if (var13 < 0.0F) {
                  var13 = 0.0F;
               }

               if (var14 < 0.0F) {
                  var14 = 0.0F;
               }

               short var19 = 255;
               int var20 = (int)(var12 * 255.0F);
               int var21 = (int)(var13 * 255.0F);
               int var22 = (int)(var14 * 255.0F);
               this.lightmapColors[var5] = var19 << 24 | var20 << 16 | var21 << 8 | var22;
            }

            this.lightmapTexture.updateDynamicTexture();
            this.lightmapUpdateNeeded = false;
            this.mc.mcProfiler.endSection();
         }
      }

   }

   private void updateMainMenu(GuiMainMenu var1) {
      try {
         String var2 = null;
         Calendar var3 = Calendar.getInstance();
         var3.setTime(new Date());
         int var4 = var3.get(5);
         int var5 = var3.get(2) + 1;
         if (var4 == 8 && var5 == 4) {
            var2 = "Happy birthday, OptiFine!";
         }

         if (var4 == 14 && var5 == 8) {
            var2 = "Happy birthday, sp614x!";
         }

         if (var2 == null) {
            return;
         }

         Field[] var6 = GuiMainMenu.class.getDeclaredFields();

         for(int var7 = 0; var7 < var6.length; ++var7) {
            if (var6[var7].getType() == String.class) {
               var6[var7].setAccessible(true);
               var6[var7].set(var1, var2);
               break;
            }
         }
      } catch (Throwable var9) {
      }

   }

   private void orientCamera(float var1) {
      Entity var2 = this.mc.getRenderViewEntity();
      float var3 = var2.getEyeHeight();
      double var4 = var2.prevPosX + (var2.posX - var2.prevPosX) * (double)var1;
      double var6 = var2.prevPosY + (var2.posY - var2.prevPosY) * (double)var1 + (double)var3;
      double var8 = var2.prevPosZ + (var2.posZ - var2.prevPosZ) * (double)var1;
      float var12;
      if (var2 instanceof EntityLivingBase && ((EntityLivingBase)var2).isPlayerSleeping()) {
         var3 = (float)((double)var3 + 1.0D);
         GlStateManager.translate(0.0F, 0.3F, 0.0F);
         if (!this.mc.gameSettings.debugCamEnable) {
            BlockPos var27 = new BlockPos(var2);
            IBlockState var11 = Minecraft.theWorld.getBlockState(var27);
            Block var30 = var11.getBlock();
            if (Reflector.ForgeHooksClient_orientBedCamera.exists()) {
               Reflector.callVoid(Reflector.ForgeHooksClient_orientBedCamera, Minecraft.theWorld, var27, var11, var2);
            } else if (var30 == Blocks.bed) {
               int var31 = ((EnumFacing)var11.getValue(BlockBed.FACING)).getHorizontalIndex();
               GlStateManager.rotate((float)(var31 * 90), 0.0F, 1.0F, 0.0F);
            }

            GlStateManager.rotate(var2.prevRotationYaw + (var2.rotationYaw - var2.prevRotationYaw) * var1 + 180.0F, 0.0F, -1.0F, 0.0F);
            GlStateManager.rotate(var2.prevRotationPitch + (var2.rotationPitch - var2.prevRotationPitch) * var1, -1.0F, 0.0F, 0.0F);
         }
      } else if (this.mc.gameSettings.thirdPersonView > 0) {
         double var10 = (double)(this.thirdPersonDistanceTemp + (this.thirdPersonDistance - this.thirdPersonDistanceTemp) * var1);
         if (this.mc.gameSettings.debugCamEnable) {
            GlStateManager.translate(0.0F, 0.0F, (float)(-var10));
         } else {
            var12 = var2.rotationYaw;
            float var13 = var2.rotationPitch;
            if (this.mc.gameSettings.thirdPersonView == 2) {
               var13 += 180.0F;
            }

            double var14 = (double)(-MathHelper.sin(var12 / 180.0F * 3.1415927F) * MathHelper.cos(var13 / 180.0F * 3.1415927F)) * var10;
            double var16 = (double)(MathHelper.cos(var12 / 180.0F * 3.1415927F) * MathHelper.cos(var13 / 180.0F * 3.1415927F)) * var10;
            double var18 = (double)(-MathHelper.sin(var13 / 180.0F * 3.1415927F)) * var10;

            for(int var20 = 0; var20 < 8; ++var20) {
               float var21 = (float)((var20 & 1) * 2 - 1);
               float var22 = (float)((var20 >> 1 & 1) * 2 - 1);
               float var23 = (float)((var20 >> 2 & 1) * 2 - 1);
               var21 *= 0.1F;
               var22 *= 0.1F;
               var23 *= 0.1F;
               MovingObjectPosition var24 = Minecraft.theWorld.rayTraceBlocks(new Vec3(var4 + (double)var21, var6 + (double)var22, var8 + (double)var23), new Vec3(var4 - var14 + (double)var21 + (double)var23, var6 - var18 + (double)var22, var8 - var16 + (double)var23));
               if (var24 != null) {
                  double var25 = var24.hitVec.distanceTo(new Vec3(var4, var6, var8));
                  if (var25 < var10) {
                     var10 = var25;
                  }
               }
            }

            if (this.mc.gameSettings.thirdPersonView == 2) {
               GlStateManager.rotate(180.0F, 0.0F, 1.0F, 0.0F);
            }

            GlStateManager.rotate(var2.rotationPitch - var13, 1.0F, 0.0F, 0.0F);
            GlStateManager.rotate(var2.rotationYaw - var12, 0.0F, 1.0F, 0.0F);
            GlStateManager.translate(0.0F, 0.0F, (float)(-var10));
            GlStateManager.rotate(var12 - var2.rotationYaw, 0.0F, 1.0F, 0.0F);
            GlStateManager.rotate(var13 - var2.rotationPitch, 1.0F, 0.0F, 0.0F);
         }
      } else {
         GlStateManager.translate(0.0F, 0.0F, -0.1F);
      }

      if (!this.mc.gameSettings.debugCamEnable) {
         float var28 = var2.prevRotationYaw + (var2.rotationYaw - var2.prevRotationYaw) * var1 + 180.0F;
         float var29 = var2.prevRotationPitch + (var2.rotationPitch - var2.prevRotationPitch) * var1;
         var12 = 0.0F;
         if (var2 instanceof EntityAnimal) {
            EntityAnimal var32 = (EntityAnimal)var2;
            var28 = var32.prevRotationYawHead + (var32.rotationYawHead - var32.prevRotationYawHead) * var1 + 180.0F;
         }

         Block var34 = ActiveRenderInfo.getBlockAtEntityViewpoint(Minecraft.theWorld, var2, var1);
         Object var33 = Reflector.newInstance(Reflector.EntityViewRenderEvent_CameraSetup_Constructor, this, var2, var34, var1, var28, var29, var12);
         Reflector.postForgeBusEvent(var33);
         var12 = Reflector.getFieldValueFloat(var33, Reflector.EntityViewRenderEvent_CameraSetup_roll, var12);
         var29 = Reflector.getFieldValueFloat(var33, Reflector.EntityViewRenderEvent_CameraSetup_pitch, var29);
         var28 = Reflector.getFieldValueFloat(var33, Reflector.EntityViewRenderEvent_CameraSetup_yaw, var28);
         GlStateManager.rotate(var12, 0.0F, 0.0F, 1.0F);
         GlStateManager.rotate(var29, 1.0F, 0.0F, 0.0F);
         GlStateManager.rotate(var28, 0.0F, 1.0F, 0.0F);
      }

      GlStateManager.translate(0.0F, -var3, 0.0F);
      var4 = var2.prevPosX + (var2.posX - var2.prevPosX) * (double)var1;
      var6 = var2.prevPosY + (var2.posY - var2.prevPosY) * (double)var1 + (double)var3;
      var8 = var2.prevPosZ + (var2.posZ - var2.prevPosZ) * (double)var1;
      this.cloudFog = this.mc.renderGlobal.hasCloudFog(var4, var6, var8, var1);
   }

   public void disableLightmap() {
      GlStateManager.setActiveTexture(OpenGlHelper.lightmapTexUnit);
      GlStateManager.disableTexture2D();
      GlStateManager.setActiveTexture(OpenGlHelper.defaultTexUnit);
      if (Config.isShaders()) {
         Shaders.disableLightmap();
      }

   }

   private void setupViewBobbing(float var1) {
      if (this.mc.getRenderViewEntity() instanceof EntityPlayer) {
         EntityPlayer var2 = (EntityPlayer)this.mc.getRenderViewEntity();
         float var3 = var2.distanceWalkedModified - var2.prevDistanceWalkedModified;
         float var4 = -(var2.distanceWalkedModified + var3 * var1);
         float var5 = var2.prevCameraYaw + (var2.cameraYaw - var2.prevCameraYaw) * var1;
         float var6 = var2.prevCameraPitch + (var2.cameraPitch - var2.prevCameraPitch) * var1;
         GlStateManager.translate(MathHelper.sin(var4 * 3.1415927F) * var5 * 0.5F, -Math.abs(MathHelper.cos(var4 * 3.1415927F) * var5), 0.0F);
         GlStateManager.rotate(MathHelper.sin(var4 * 3.1415927F) * var5 * 3.0F, 0.0F, 0.0F, 1.0F);
         GlStateManager.rotate(Math.abs(MathHelper.cos(var4 * 3.1415927F - 0.2F) * var5) * 5.0F, 1.0F, 0.0F, 0.0F);
         GlStateManager.rotate(var6, 1.0F, 0.0F, 0.0F);
      }

   }

   private void addRainParticles() {
      float var1 = Minecraft.theWorld.getRainStrength(1.0F);
      if (!Config.isRainFancy()) {
         var1 /= 2.0F;
      }

      if (var1 != 0.0F && Config.isRainSplash()) {
         this.random.setSeed((long)this.rendererUpdateCount * 312987231L);
         Entity var2 = this.mc.getRenderViewEntity();
         WorldClient var3 = Minecraft.theWorld;
         BlockPos var4 = new BlockPos(var2);
         byte var5 = 10;
         double var6 = 0.0D;
         double var8 = 0.0D;
         double var10 = 0.0D;
         int var12 = 0;
         int var13 = (int)(100.0F * var1 * var1);
         if (this.mc.gameSettings.particleSetting == 1) {
            var13 >>= 1;
         } else if (this.mc.gameSettings.particleSetting == 2) {
            var13 = 0;
         }

         for(int var14 = 0; var14 < var13; ++var14) {
            BlockPos var15 = var3.getPrecipitationHeight(var4.add(this.random.nextInt(var5) - this.random.nextInt(var5), 0, this.random.nextInt(var5) - this.random.nextInt(var5)));
            BiomeGenBase var16 = var3.getBiomeGenForCoords(var15);
            BlockPos var17 = var15.down();
            Block var18 = var3.getBlockState(var17).getBlock();
            if (var15.getY() <= var4.getY() + var5 && var15.getY() >= var4.getY() - var5 && var16.canSpawnLightningBolt() && var16.getFloatTemperature(var15) >= 0.15F) {
               double var19 = this.random.nextDouble();
               double var21 = this.random.nextDouble();
               if (var18.getMaterial() == Material.lava) {
                  Minecraft.theWorld.spawnParticle(EnumParticleTypes.SMOKE_NORMAL, (double)var15.getX() + var19, (double)((float)var15.getY() + 0.1F) - var18.getBlockBoundsMinY(), (double)var15.getZ() + var21, 0.0D, 0.0D, 0.0D, new int[0]);
               } else if (var18.getMaterial() != Material.air) {
                  var18.setBlockBoundsBasedOnState(var3, var17);
                  ++var12;
                  if (this.random.nextInt(var12) == 0) {
                     var6 = (double)var17.getX() + var19;
                     var8 = (double)((float)var17.getY() + 0.1F) + var18.getBlockBoundsMaxY() - 1.0D;
                     var10 = (double)var17.getZ() + var21;
                  }

                  Minecraft.theWorld.spawnParticle(EnumParticleTypes.WATER_DROP, (double)var17.getX() + var19, (double)((float)var17.getY() + 0.1F) + var18.getBlockBoundsMaxY(), (double)var17.getZ() + var21, 0.0D, 0.0D, 0.0D, new int[0]);
               }
            }
         }

         if (var12 > 0 && this.random.nextInt(3) < this.rainSoundCounter++) {
            this.rainSoundCounter = 0;
            if (var8 > (double)(var4.getY() + 1) && var3.getPrecipitationHeight(var4).getY() > MathHelper.floor_float((float)var4.getY())) {
               Minecraft.theWorld.playSound(var6, var8, var10, "ambient.weather.rain", 0.1F, 0.5F, false);
            } else {
               Minecraft.theWorld.playSound(var6, var8, var10, "ambient.weather.rain", 0.2F, 1.0F, false);
            }
         }
      }

   }

   public void renderStreamIndicator(float var1) {
      this.setupOverlayRendering();
      this.mc.ingameGUI.renderStreamIndicator(new ScaledResolution(this.mc));
   }

   public boolean isShaderActive() {
      return OpenGlHelper.shadersSupported && this.theShaderGroup != null;
   }

   public void func_181560_a(float var1, long var2) {
      this.frameInit();
      boolean var4 = Display.isActive();
      if (!var4 && this.mc.gameSettings.pauseOnLostFocus && (!this.mc.gameSettings.touchscreen || !Mouse.isButtonDown(1))) {
         if (Minecraft.getSystemTime() - this.prevFrameTime > 500L) {
            this.mc.displayInGameMenu();
         }
      } else {
         this.prevFrameTime = Minecraft.getSystemTime();
      }

      this.mc.mcProfiler.startSection("mouse");
      if (var4 && Minecraft.isRunningOnMac && this.mc.inGameHasFocus && !Mouse.isInsideWindow()) {
         Mouse.setGrabbed(false);
         Mouse.setCursorPosition(Display.getWidth() / 2, Display.getHeight() / 2);
         Mouse.setGrabbed(true);
      }

      if (this.mc.inGameHasFocus && var4) {
         this.mc.mouseHelper.mouseXYChange();
         float var5 = this.mc.gameSettings.mouseSensitivity * 0.6F + 0.2F;
         float var6 = var5 * var5 * var5 * 8.0F;
         float var7 = (float)this.mc.mouseHelper.deltaX * var6;
         float var8 = (float)this.mc.mouseHelper.deltaY * var6;
         byte var9 = 1;
         if (this.mc.gameSettings.invertMouse) {
            var9 = -1;
         }

         if (this.mc.gameSettings.smoothCamera) {
            this.smoothCamYaw += var7;
            this.smoothCamPitch += var8;
            float var10 = var1 - this.smoothCamPartialTicks;
            this.smoothCamPartialTicks = var1;
            var7 = this.smoothCamFilterX * var10;
            var8 = this.smoothCamFilterY * var10;
            Minecraft.thePlayer.setAngles(var7, var8 * (float)var9);
         } else {
            this.smoothCamYaw = 0.0F;
            this.smoothCamPitch = 0.0F;
            Minecraft.thePlayer.setAngles(var7, var8 * (float)var9);
         }
      }

      this.mc.mcProfiler.endSection();
      if (!this.mc.skipRenderWorld) {
         anaglyphEnable = this.mc.gameSettings.anaglyph;
         ScaledResolution var17 = new ScaledResolution(this.mc);
         int var18 = var17.getScaledWidth();
         int var19 = ScaledResolution.getScaledHeight();
         int var20 = Mouse.getX() * var18 / this.mc.displayWidth;
         int var21 = var19 - Mouse.getY() * var19 / this.mc.displayHeight - 1;
         int var22 = this.mc.gameSettings.limitFramerate;
         if (Minecraft.theWorld != null) {
            this.mc.mcProfiler.startSection("level");
            int var11 = Math.min(Minecraft.getDebugFPS(), var22);
            var11 = Math.max(var11, 60);
            long var12 = System.nanoTime() - var2;
            long var14 = Math.max((long)(1000000000 / var11 / 4) - var12, 0L);
            this.renderWorld(var17, var1, System.nanoTime() + var14);
            if (OpenGlHelper.shadersSupported) {
               this.mc.renderGlobal.renderEntityOutlineFramebuffer();
               if (this.theShaderGroup != null && this.useShader) {
                  GlStateManager.matrixMode(5890);
                  GlStateManager.pushMatrix();
                  GlStateManager.loadIdentity();
                  this.theShaderGroup.loadShaderGroup(var1);
                  GlStateManager.popMatrix();
               }

               this.mc.getFramebuffer().bindFramebuffer(true);
            }

            this.renderEndNanoTime = System.nanoTime();
            this.mc.mcProfiler.endStartSection("gui");
            if (!this.mc.gameSettings.hideGUI || this.mc.currentScreen != null) {
               GlStateManager.alphaFunc(516, 0.1F);
               this.mc.ingameGUI.renderGameOverlay(var1);
               if (this.mc.gameSettings.ofShowFps && !this.mc.gameSettings.showDebugInfo) {
                  Config.drawFps();
               }

               if (this.mc.gameSettings.showDebugInfo) {
                  Lagometer.showLagometer(var17);
               }
            }

            this.mc.mcProfiler.endSection();
         } else {
            GlStateManager.viewport(0, 0, this.mc.displayWidth, this.mc.displayHeight);
            GlStateManager.matrixMode(5889);
            GlStateManager.loadIdentity();
            GlStateManager.matrixMode(5888);
            GlStateManager.loadIdentity();
            this.setupOverlayRendering();
            this.renderEndNanoTime = System.nanoTime();
            TileEntityRendererDispatcher.instance.renderEngine = this.mc.getTextureManager();
         }

         if (this.mc.currentScreen != null) {
            GlStateManager.clear(256);

            try {
               if (Reflector.ForgeHooksClient_drawScreen.exists()) {
                  Reflector.callVoid(Reflector.ForgeHooksClient_drawScreen, this.mc.currentScreen, var20, var21, var1);
               } else {
                  this.mc.currentScreen.drawScreen(var20, var21, var1);
               }
            } catch (Throwable var16) {
               CrashReport var23 = CrashReport.makeCrashReport(var16, "Rendering screen");
               CrashReportCategory var13 = var23.makeCategory("Screen render details");
               var13.addCrashSectionCallable("Mouse location", new Callable(this, var20, var21) {
                  private final int val$k1;
                  final EntityRenderer this$0;
                  private static final String __OBFID = "CL_00000950";
                  private final int val$j1;

                  public Object call() throws Exception {
                     return this.call();
                  }

                  public String call() throws Exception {
                     return String.format("Scaled: (%d, %d). Absolute: (%d, %d)", this.val$j1, this.val$k1, Mouse.getX(), Mouse.getY());
                  }

                  {
                     this.this$0 = var1;
                     this.val$j1 = var2;
                     this.val$k1 = var3;
                  }
               });
               var13.addCrashSectionCallable("Screen size", new Callable(this, var17) {
                  private static final String __OBFID = "CL_00000951";
                  private final ScaledResolution val$scaledresolution;
                  final EntityRenderer this$0;

                  {
                     this.this$0 = var1;
                     this.val$scaledresolution = var2;
                  }

                  public Object call() throws Exception {
                     return this.call();
                  }

                  public String call() throws Exception {
                     return String.format("Scaled: (%d, %d). Absolute: (%d, %d). Scale factor of %d", this.val$scaledresolution.getScaledWidth(), ScaledResolution.getScaledHeight(), EntityRenderer.access$0(this.this$0).displayWidth, EntityRenderer.access$0(this.this$0).displayHeight, this.val$scaledresolution.getScaleFactor());
                  }
               });
               throw new ReportedException(var23);
            }
         }
      }

      this.frameFinish();
      this.waitForServerThread();
      Lagometer.updateLagometer();
      if (this.mc.gameSettings.ofProfiler) {
         this.mc.gameSettings.showDebugProfilerChart = true;
      }

   }

   private void updateTorchFlicker() {
      this.torchFlickerDX = (float)((double)this.torchFlickerDX + (Math.random() - Math.random()) * Math.random() * Math.random());
      this.torchFlickerDX = (float)((double)this.torchFlickerDX * 0.9D);
      this.torchFlickerX += (this.torchFlickerDX - this.torchFlickerX) * 1.0F;
      this.lightmapUpdateNeeded = true;
   }

   public void renderHand(float var1, int var2, boolean var3, boolean var4, boolean var5) {
      if (!this.debugView) {
         GlStateManager.matrixMode(5889);
         GlStateManager.loadIdentity();
         float var6 = 0.07F;
         if (this.mc.gameSettings.anaglyph) {
            GlStateManager.translate((float)(-(var2 * 2 - 1)) * var6, 0.0F, 0.0F);
         }

         if (Config.isShaders()) {
            Shaders.applyHandDepth();
         }

         Project.gluPerspective(this.getFOVModifier(var1, false), (float)this.mc.displayWidth / (float)this.mc.displayHeight, 0.05F, this.farPlaneDistance * 2.0F);
         GlStateManager.matrixMode(5888);
         GlStateManager.loadIdentity();
         if (this.mc.gameSettings.anaglyph) {
            GlStateManager.translate((float)(var2 * 2 - 1) * 0.1F, 0.0F, 0.0F);
         }

         boolean var7 = false;
         if (var3) {
            GlStateManager.pushMatrix();
            this.hurtCameraEffect(var1);
            if (this.mc.gameSettings.viewBobbing) {
               this.setupViewBobbing(var1);
            }

            var7 = this.mc.getRenderViewEntity() instanceof EntityLivingBase && ((EntityLivingBase)this.mc.getRenderViewEntity()).isPlayerSleeping();
            boolean var8 = !ReflectorForge.renderFirstPersonHand(this.mc.renderGlobal, var1, var2);
            if (var8 && this.mc.gameSettings.thirdPersonView == 0 && !var7 && !this.mc.gameSettings.hideGUI && !Minecraft.playerController.isSpectator()) {
               this.enableLightmap();
               if (Config.isShaders()) {
                  ShadersRender.renderItemFP(this.itemRenderer, var1, var5);
               } else {
                  this.itemRenderer.renderItemInFirstPerson(var1);
               }

               this.disableLightmap();
            }

            GlStateManager.popMatrix();
         }

         if (!var4) {
            return;
         }

         this.disableLightmap();
         if (this.mc.gameSettings.thirdPersonView == 0 && !var7) {
            this.itemRenderer.renderOverlays(var1);
            this.hurtCameraEffect(var1);
         }

         if (this.mc.gameSettings.viewBobbing) {
            this.setupViewBobbing(var1);
         }
      }

   }

   public float getNightVisionBrightness(EntityLivingBase var1, float var2) {
      int var3 = var1.getActivePotionEffect(Potion.nightVision).getDuration();
      return var3 > 200 ? 1.0F : 0.7F + MathHelper.sin(((float)var3 - var2) * 3.1415927F * 0.2F) * 0.3F;
   }

   private void updateFovModifierHand() {
      float var1 = 1.0F;
      if (this.mc.getRenderViewEntity() instanceof AbstractClientPlayer) {
         AbstractClientPlayer var2 = (AbstractClientPlayer)this.mc.getRenderViewEntity();
         var1 = var2.getFovModifier();
      }

      this.fovModifierHandPrev = this.fovModifierHand;
      this.fovModifierHand += (var1 - this.fovModifierHand) * 0.5F;
      if (this.fovModifierHand > 1.5F) {
         this.fovModifierHand = 1.5F;
      }

      if (this.fovModifierHand < 0.1F) {
         this.fovModifierHand = 0.1F;
      }

   }

   public void getMouseOver(float var1) {
      Entity var2 = this.mc.getRenderViewEntity();
      if (var2 != null && Minecraft.theWorld != null) {
         this.mc.mcProfiler.startSection("pick");
         this.mc.pointedEntity = null;
         double var3 = (double)Minecraft.playerController.getBlockReachDistance();
         this.mc.objectMouseOver = var2.rayTrace(var3, var1);
         double var5 = var3;
         Vec3 var7 = var2.getPositionEyes(var1);
         boolean var8 = false;
         boolean var9 = true;
         if (Minecraft.playerController.extendedReach()) {
            var3 = 6.0D;
            var5 = 6.0D;
         } else {
            if (var3 > 3.0D) {
               var8 = true;
            }

            var3 = var3;
         }

         if (this.mc.objectMouseOver != null) {
            var5 = this.mc.objectMouseOver.hitVec.distanceTo(var7);
         }

         Vec3 var10 = var2.getLook(var1);
         Vec3 var11 = var7.addVector(var10.xCoord * var3, var10.yCoord * var3, var10.zCoord * var3);
         this.pointedEntity = null;
         Vec3 var12 = null;
         float var13 = 1.0F;
         List var14 = Minecraft.theWorld.getEntitiesInAABBexcluding(var2, var2.getEntityBoundingBox().addCoord(var10.xCoord * var3, var10.yCoord * var3, var10.zCoord * var3).expand((double)var13, (double)var13, (double)var13), Predicates.and(EntitySelectors.NOT_SPECTATING));
         double var15 = var5;

         for(int var17 = 0; var17 < var14.size(); ++var17) {
            Entity var18 = (Entity)var14.get(var17);
            float var19 = var18.getCollisionBorderSize();
            AxisAlignedBB var20 = var18.getEntityBoundingBox().expand((double)var19, (double)var19, (double)var19);
            MovingObjectPosition var21 = var20.calculateIntercept(var7, var11);
            if (var20.isVecInside(var7)) {
               if (var15 >= 0.0D) {
                  this.pointedEntity = var18;
                  var12 = var21 == null ? var7 : var21.hitVec;
                  var15 = 0.0D;
               }
            } else if (var21 != null) {
               double var22 = var7.distanceTo(var21.hitVec);
               if (var22 < var15 || var15 == 0.0D) {
                  boolean var24 = false;
                  if (Reflector.ForgeEntity_canRiderInteract.exists()) {
                     var24 = Reflector.callBoolean(var18, Reflector.ForgeEntity_canRiderInteract);
                  }

                  if (var18 == var2.ridingEntity && !var24) {
                     if (var15 == 0.0D) {
                        this.pointedEntity = var18;
                        var12 = var21.hitVec;
                     }
                  } else {
                     this.pointedEntity = var18;
                     var12 = var21.hitVec;
                     var15 = var22;
                  }
               }
            }
         }

         if (this.pointedEntity != null && var8 && var7.distanceTo(var12) > 3.0D) {
            this.pointedEntity = null;
            this.mc.objectMouseOver = new MovingObjectPosition(MovingObjectPosition.MovingObjectType.MISS, var12, (EnumFacing)null, new BlockPos(var12));
         }

         if (this.pointedEntity != null && (var15 < var5 || this.mc.objectMouseOver == null)) {
            this.mc.objectMouseOver = new MovingObjectPosition(this.pointedEntity, var12);
            if (this.pointedEntity instanceof EntityLivingBase || this.pointedEntity instanceof EntityItemFrame) {
               this.mc.pointedEntity = this.pointedEntity;
            }
         }

         this.mc.mcProfiler.endSection();
      }

   }

   public void activateNextShader() {
      if (OpenGlHelper.shadersSupported && this.mc.getRenderViewEntity() instanceof EntityPlayer) {
         if (this.theShaderGroup != null) {
            this.theShaderGroup.deleteShaderGroup();
         }

         this.shaderIndex = (this.shaderIndex + 1) % (shaderResourceLocations.length + 1);
         if (this.shaderIndex != shaderCount) {
            this.loadShader(shaderResourceLocations[this.shaderIndex]);
         } else {
            this.theShaderGroup = null;
         }
      }

   }

   public void switchUseShader() {
      this.useShader = !this.useShader;
   }

   protected void renderRainSnow(float var1) {
      if (Reflector.ForgeWorldProvider_getWeatherRenderer.exists()) {
         WorldProvider var2 = Minecraft.theWorld.provider;
         Object var3 = Reflector.call(var2, Reflector.ForgeWorldProvider_getWeatherRenderer);
         if (var3 != null) {
            Reflector.callVoid(var3, Reflector.IRenderHandler_render, var1, Minecraft.theWorld, this.mc);
            return;
         }
      }

      float var49 = Minecraft.theWorld.getRainStrength(var1);
      if (var49 > 0.0F) {
         if (Config.isRainOff()) {
            return;
         }

         this.enableLightmap();
         Entity var50 = this.mc.getRenderViewEntity();
         WorldClient var4 = Minecraft.theWorld;
         int var5 = MathHelper.floor_double(var50.posX);
         int var6 = MathHelper.floor_double(var50.posY);
         int var7 = MathHelper.floor_double(var50.posZ);
         Tessellator var8 = Tessellator.getInstance();
         WorldRenderer var9 = var8.getWorldRenderer();
         GlStateManager.disableCull();
         GL11.glNormal3f(0.0F, 1.0F, 0.0F);
         GlStateManager.enableBlend();
         GlStateManager.tryBlendFuncSeparate(770, 771, 1, 0);
         GlStateManager.alphaFunc(516, 0.1F);
         double var10 = var50.lastTickPosX + (var50.posX - var50.lastTickPosX) * (double)var1;
         double var12 = var50.lastTickPosY + (var50.posY - var50.lastTickPosY) * (double)var1;
         double var14 = var50.lastTickPosZ + (var50.posZ - var50.lastTickPosZ) * (double)var1;
         int var16 = MathHelper.floor_double(var12);
         byte var17 = 5;
         if (Config.isRainFancy()) {
            var17 = 10;
         }

         byte var18 = -1;
         float var19 = (float)this.rendererUpdateCount + var1;
         var9.setTranslation(-var10, -var12, -var14);
         if (Config.isRainFancy()) {
            var17 = 10;
         }

         GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
         BlockPos.MutableBlockPos var20 = new BlockPos.MutableBlockPos();

         for(int var21 = var7 - var17; var21 <= var7 + var17; ++var21) {
            for(int var22 = var5 - var17; var22 <= var5 + var17; ++var22) {
               int var23 = (var21 - var7 + 16) * 32 + var22 - var5 + 16;
               double var24 = (double)this.rainXCoords[var23] * 0.5D;
               double var26 = (double)this.rainYCoords[var23] * 0.5D;
               var20.func_181079_c(var22, 0, var21);
               BiomeGenBase var28 = var4.getBiomeGenForCoords(var20);
               if (var28.canSpawnLightningBolt() || var28.getEnableSnow()) {
                  int var29 = var4.getPrecipitationHeight(var20).getY();
                  int var30 = var6 - var17;
                  int var31 = var6 + var17;
                  if (var30 < var29) {
                     var30 = var29;
                  }

                  if (var31 < var29) {
                     var31 = var29;
                  }

                  int var32 = var29;
                  if (var29 < var16) {
                     var32 = var16;
                  }

                  if (var30 != var31) {
                     this.random.setSeed((long)(var22 * var22 * 3121 + var22 * 45238971 ^ var21 * var21 * 418711 + var21 * 13761));
                     var20.func_181079_c(var22, var30, var21);
                     float var33 = var28.getFloatTemperature(var20);
                     double var34;
                     double var36;
                     double var38;
                     if (var4.getWorldChunkManager().getTemperatureAtHeight(var33, var29) >= 0.15F) {
                        if (var18 != 0) {
                           if (var18 >= 0) {
                              var8.draw();
                           }

                           var18 = 0;
                           this.mc.getTextureManager().bindTexture(locationRainPng);
                           var9.begin(7, DefaultVertexFormats.PARTICLE_POSITION_TEX_COLOR_LMAP);
                        }

                        var34 = ((double)(this.rendererUpdateCount + var22 * var22 * 3121 + var22 * 45238971 + var21 * var21 * 418711 + var21 * 13761 & 31) + (double)var1) / 32.0D * (3.0D + this.random.nextDouble());
                        var36 = (double)((float)var22 + 0.5F) - var50.posX;
                        var38 = (double)((float)var21 + 0.5F) - var50.posZ;
                        float var40 = MathHelper.sqrt_double(var36 * var36 + var38 * var38) / (float)var17;
                        float var41 = ((1.0F - var40 * var40) * 0.5F + 0.5F) * var49;
                        var20.func_181079_c(var22, var32, var21);
                        int var42 = var4.getCombinedLight(var20, 0);
                        int var43 = var42 >> 16 & '\uffff';
                        int var44 = var42 & '\uffff';
                        var9.pos((double)var22 - var24 + 0.5D, (double)var30, (double)var21 - var26 + 0.5D).tex(0.0D, (double)var30 * 0.25D + var34).color(1.0F, 1.0F, 1.0F, var41).lightmap(var43, var44).endVertex();
                        var9.pos((double)var22 + var24 + 0.5D, (double)var30, (double)var21 + var26 + 0.5D).tex(1.0D, (double)var30 * 0.25D + var34).color(1.0F, 1.0F, 1.0F, var41).lightmap(var43, var44).endVertex();
                        var9.pos((double)var22 + var24 + 0.5D, (double)var31, (double)var21 + var26 + 0.5D).tex(1.0D, (double)var31 * 0.25D + var34).color(1.0F, 1.0F, 1.0F, var41).lightmap(var43, var44).endVertex();
                        var9.pos((double)var22 - var24 + 0.5D, (double)var31, (double)var21 - var26 + 0.5D).tex(0.0D, (double)var31 * 0.25D + var34).color(1.0F, 1.0F, 1.0F, var41).lightmap(var43, var44).endVertex();
                     } else {
                        if (var18 != 1) {
                           if (var18 >= 0) {
                              var8.draw();
                           }

                           var18 = 1;
                           this.mc.getTextureManager().bindTexture(locationSnowPng);
                           var9.begin(7, DefaultVertexFormats.PARTICLE_POSITION_TEX_COLOR_LMAP);
                        }

                        var34 = (double)(((float)(this.rendererUpdateCount & 511) + var1) / 512.0F);
                        var36 = this.random.nextDouble() + (double)var19 * 0.01D * (double)((float)this.random.nextGaussian());
                        var38 = this.random.nextDouble() + (double)(var19 * (float)this.random.nextGaussian()) * 0.001D;
                        double var51 = (double)((float)var22 + 0.5F) - var50.posX;
                        double var52 = (double)((float)var21 + 0.5F) - var50.posZ;
                        float var53 = MathHelper.sqrt_double(var51 * var51 + var52 * var52) / (float)var17;
                        float var45 = ((1.0F - var53 * var53) * 0.3F + 0.5F) * var49;
                        var20.func_181079_c(var22, var32, var21);
                        int var46 = (var4.getCombinedLight(var20, 0) * 3 + 15728880) / 4;
                        int var47 = var46 >> 16 & '\uffff';
                        int var48 = var46 & '\uffff';
                        var9.pos((double)var22 - var24 + 0.5D, (double)var30, (double)var21 - var26 + 0.5D).tex(0.0D + var36, (double)var30 * 0.25D + var34 + var38).color(1.0F, 1.0F, 1.0F, var45).lightmap(var47, var48).endVertex();
                        var9.pos((double)var22 + var24 + 0.5D, (double)var30, (double)var21 + var26 + 0.5D).tex(1.0D + var36, (double)var30 * 0.25D + var34 + var38).color(1.0F, 1.0F, 1.0F, var45).lightmap(var47, var48).endVertex();
                        var9.pos((double)var22 + var24 + 0.5D, (double)var31, (double)var21 + var26 + 0.5D).tex(1.0D + var36, (double)var31 * 0.25D + var34 + var38).color(1.0F, 1.0F, 1.0F, var45).lightmap(var47, var48).endVertex();
                        var9.pos((double)var22 - var24 + 0.5D, (double)var31, (double)var21 - var26 + 0.5D).tex(0.0D + var36, (double)var31 * 0.25D + var34 + var38).color(1.0F, 1.0F, 1.0F, var45).lightmap(var47, var48).endVertex();
                     }
                  }
               }
            }
         }

         if (var18 >= 0) {
            var8.draw();
         }

         var9.setTranslation(0.0D, 0.0D, 0.0D);
         GlStateManager.enableCull();
         GlStateManager.disableBlend();
         GlStateManager.alphaFunc(516, 0.1F);
         this.disableLightmap();
      }

   }

   private void hurtCameraEffect(float var1) {
      if (!(new NoHurtCam()).getInstance().isEnabled()) {
         if (this.mc.getRenderViewEntity() instanceof EntityLivingBase) {
            EntityLivingBase var2 = (EntityLivingBase)this.mc.getRenderViewEntity();
            float var3 = (float)var2.hurtTime - var1;
            float var4;
            if (var2.getHealth() <= 0.0F) {
               var4 = (float)var2.deathTime + var1;
               GlStateManager.rotate(40.0F - 8000.0F / (var4 + 200.0F), 0.0F, 0.0F, 1.0F);
            }

            if (var3 < 0.0F) {
               return;
            }

            var3 /= (float)var2.maxHurtTime;
            var3 = MathHelper.sin(var3 * var3 * var3 * var3 * 3.1415927F);
            var4 = var2.attackedAtYaw;
            GlStateManager.rotate(-var4, 0.0F, 1.0F, 0.0F);
            GlStateManager.rotate(-var3 * 14.0F, 0.0F, 0.0F, 1.0F);
            GlStateManager.rotate(var4, 0.0F, 1.0F, 0.0F);
         }

      }
   }

   private boolean isDrawBlockOutline() {
      if (!this.drawBlockOutline) {
         return false;
      } else {
         Entity var1 = this.mc.getRenderViewEntity();
         boolean var2 = var1 instanceof EntityPlayer && !this.mc.gameSettings.hideGUI;
         if (var2 && !((EntityPlayer)var1).capabilities.allowEdit) {
            ItemStack var3 = ((EntityPlayer)var1).getCurrentEquippedItem();
            if (this.mc.objectMouseOver != null && this.mc.objectMouseOver.typeOfHit == MovingObjectPosition.MovingObjectType.BLOCK) {
               BlockPos var4 = this.mc.objectMouseOver.getBlockPos();
               IBlockState var5 = Minecraft.theWorld.getBlockState(var4);
               Block var6 = var5.getBlock();
               if (Minecraft.playerController.getCurrentGameType() == WorldSettings.GameType.SPECTATOR) {
                  var2 = ReflectorForge.blockHasTileEntity(var5) && Minecraft.theWorld.getTileEntity(var4) instanceof IInventory;
               } else {
                  var2 = var3 != null && (var3.canDestroy(var6) || var3.canPlaceOn(var6));
               }
            }
         }

         return var2;
      }
   }

   public void setupCameraTransform(float var1, int var2) {
      this.farPlaneDistance = (float)(this.mc.gameSettings.renderDistanceChunks * 16);
      if (Config.isFogFancy()) {
         this.farPlaneDistance *= 0.95F;
      }

      if (Config.isFogFast()) {
         this.farPlaneDistance *= 0.83F;
      }

      GlStateManager.matrixMode(5889);
      GlStateManager.loadIdentity();
      float var3 = 0.07F;
      if (this.mc.gameSettings.anaglyph) {
         GlStateManager.translate((float)(-(var2 * 2 - 1)) * var3, 0.0F, 0.0F);
      }

      this.clipDistance = this.farPlaneDistance * 2.0F;
      if (this.clipDistance < 173.0F) {
         this.clipDistance = 173.0F;
      }

      if (Minecraft.theWorld.provider.getDimensionId() == 1) {
         this.clipDistance = 256.0F;
      }

      if (this.cameraZoom != 1.0D) {
         GlStateManager.translate((float)this.cameraYaw, (float)(-this.cameraPitch), 0.0F);
         GlStateManager.scale(this.cameraZoom, this.cameraZoom, 1.0D);
      }

      Project.gluPerspective(this.getFOVModifier(var1, true), (float)this.mc.displayWidth / (float)this.mc.displayHeight, 0.05F, this.clipDistance);
      GlStateManager.matrixMode(5888);
      GlStateManager.loadIdentity();
      if (this.mc.gameSettings.anaglyph) {
         GlStateManager.translate((float)(var2 * 2 - 1) * 0.1F, 0.0F, 0.0F);
      }

      this.hurtCameraEffect(var1);
      if (this.mc.gameSettings.viewBobbing) {
         this.setupViewBobbing(var1);
      }

      float var4 = Minecraft.thePlayer.prevTimeInPortal + (Minecraft.thePlayer.timeInPortal - Minecraft.thePlayer.prevTimeInPortal) * var1;
      if (var4 > 0.0F) {
         byte var5 = 20;
         if (Minecraft.thePlayer.isPotionActive(Potion.confusion)) {
            var5 = 7;
         }

         float var6 = 5.0F / (var4 * var4 + 5.0F) - var4 * 0.04F;
         var6 *= var6;
         GlStateManager.rotate(((float)this.rendererUpdateCount + var1) * (float)var5, 0.0F, 1.0F, 1.0F);
         GlStateManager.scale(1.0F / var6, 1.0F, 1.0F);
         GlStateManager.rotate(-((float)this.rendererUpdateCount + var1) * (float)var5, 0.0F, 1.0F, 1.0F);
      }

      this.orientCamera(var1);
      if (this.debugView) {
         switch(this.debugViewDirection) {
         case 0:
            GlStateManager.rotate(90.0F, 0.0F, 1.0F, 0.0F);
            break;
         case 1:
            GlStateManager.rotate(180.0F, 0.0F, 1.0F, 0.0F);
            break;
         case 2:
            GlStateManager.rotate(-90.0F, 0.0F, 1.0F, 0.0F);
            break;
         case 3:
            GlStateManager.rotate(90.0F, 1.0F, 0.0F, 0.0F);
            break;
         case 4:
            GlStateManager.rotate(-90.0F, 1.0F, 0.0F, 0.0F);
         }
      }

   }

   public EntityRenderer(Minecraft var1, IResourceManager var2) {
      this.shaderIndex = shaderCount;
      this.useShader = false;
      this.frameCount = 0;
      this.mc = var1;
      this.resourceManager = var2;
      this.itemRenderer = var1.getItemRenderer();
      this.theMapItemRenderer = new MapItemRenderer(var1.getTextureManager());
      this.lightmapTexture = new DynamicTexture(16, 16);
      this.locationLightMap = var1.getTextureManager().getDynamicTextureLocation("lightMap", this.lightmapTexture);
      this.lightmapColors = this.lightmapTexture.getTextureData();
      this.theShaderGroup = null;

      for(int var3 = 0; var3 < 32; ++var3) {
         for(int var4 = 0; var4 < 32; ++var4) {
            float var5 = (float)(var4 - 16);
            float var6 = (float)(var3 - 16);
            float var7 = MathHelper.sqrt_float(var5 * var5 + var6 * var6);
            this.rainXCoords[var3 << 5 | var4] = -var6 / var7;
            this.rainYCoords[var3 << 5 | var4] = var5 / var7;
         }
      }

   }

   public ShaderGroup getShaderGroup() {
      return this.theShaderGroup;
   }

   private void frameFinish() {
      if (Minecraft.theWorld != null) {
         long var1 = System.currentTimeMillis();
         if (var1 > this.lastErrorCheckTimeMs + 10000L) {
            this.lastErrorCheckTimeMs = var1;
            int var3 = GL11.glGetError();
            if (var3 != 0) {
               String var4 = GLU.gluErrorString(var3);
               ChatComponentText var5 = new ChatComponentText(I18n.format("of.message.openglError", var3, var4));
               this.mc.ingameGUI.getChatGUI().printChatMessage(var5);
            }
         }
      }

   }

   public void updateShaderGroupSize(int var1, int var2) {
      if (OpenGlHelper.shadersSupported) {
         if (this.theShaderGroup != null) {
            this.theShaderGroup.createBindFramebuffers(var1, var2);
         }

         this.mc.renderGlobal.createBindEntityOutlineFbs(var1, var2);
      }

   }

   private void renderWorldPass(ScaledResolution var1, int var2, float var3, long var4) {
      boolean var6 = Config.isShaders();
      if (var6) {
         Shaders.beginRenderPass(var2, var3, var4);
      }

      RenderGlobal var7 = this.mc.renderGlobal;
      EffectRenderer var8 = this.mc.effectRenderer;
      boolean var9 = this.isDrawBlockOutline();
      GlStateManager.enableCull();
      this.mc.mcProfiler.endStartSection("clear");
      if (var6) {
         Shaders.setViewport(0, 0, this.mc.displayWidth, this.mc.displayHeight);
      } else {
         GlStateManager.viewport(0, 0, this.mc.displayWidth, this.mc.displayHeight);
      }

      this.updateFogColor(var3);
      GlStateManager.clear(16640);
      if (var6) {
         Shaders.clearRenderBuffer();
      }

      this.mc.mcProfiler.endStartSection("camera");
      this.setupCameraTransform(var3, var2);
      if (var6) {
         Shaders.setCamera(var3);
      }

      ActiveRenderInfo.updateRenderInfo(Minecraft.thePlayer, this.mc.gameSettings.thirdPersonView == 2);
      this.mc.mcProfiler.endStartSection("frustum");
      ClippingHelperImpl.getInstance();
      this.mc.mcProfiler.endStartSection("culling");
      Frustum var10 = new Frustum();
      Entity var11 = this.mc.getRenderViewEntity();
      double var12 = var11.lastTickPosX + (var11.posX - var11.lastTickPosX) * (double)var3;
      double var14 = var11.lastTickPosY + (var11.posY - var11.lastTickPosY) * (double)var3;
      double var16 = var11.lastTickPosZ + (var11.posZ - var11.lastTickPosZ) * (double)var3;
      if (var6) {
         ShadersRender.setFrustrumPosition(var10, var12, var14, var16);
      } else {
         var10.setPosition(var12, var14, var16);
      }

      if ((Config.isSkyEnabled() || Config.isSunMoonEnabled() || Config.isStarsEnabled()) && !Shaders.isShadowPass) {
         this.setupFog(-1, var3);
         this.mc.mcProfiler.endStartSection("sky");
         GlStateManager.matrixMode(5889);
         GlStateManager.loadIdentity();
         Project.gluPerspective(this.getFOVModifier(var3, true), (float)this.mc.displayWidth / (float)this.mc.displayHeight, 0.05F, this.clipDistance);
         GlStateManager.matrixMode(5888);
         if (var6) {
            Shaders.beginSky();
         }

         var7.renderSky(var3, var2);
         if (var6) {
            Shaders.endSky();
         }

         GlStateManager.matrixMode(5889);
         GlStateManager.loadIdentity();
         Project.gluPerspective(this.getFOVModifier(var3, true), (float)this.mc.displayWidth / (float)this.mc.displayHeight, 0.05F, this.clipDistance);
         GlStateManager.matrixMode(5888);
      } else {
         GlStateManager.disableBlend();
      }

      this.setupFog(0, var3);
      GlStateManager.shadeModel(7425);
      if (var11.posY + (double)var11.getEyeHeight() < 128.0D + (double)(this.mc.gameSettings.ofCloudsHeight * 128.0F)) {
         this.renderCloudsCheck(var7, var3, var2);
      }

      this.mc.mcProfiler.endStartSection("prepareterrain");
      this.setupFog(0, var3);
      this.mc.getTextureManager().bindTexture(TextureMap.locationBlocksTexture);
      RenderHelper.disableStandardItemLighting();
      this.mc.mcProfiler.endStartSection("terrain_setup");
      if (var6) {
         ShadersRender.setupTerrain(var7, var11, (double)var3, var10, this.frameCount++, Minecraft.thePlayer.isSpectator());
      } else {
         var7.setupTerrain(var11, (double)var3, var10, this.frameCount++, Minecraft.thePlayer.isSpectator());
      }

      if (var2 == 0 || var2 == 2) {
         this.mc.mcProfiler.endStartSection("updatechunks");
         Lagometer.timerChunkUpload.start();
         this.mc.renderGlobal.updateChunks(var4);
         Lagometer.timerChunkUpload.end();
      }

      this.mc.mcProfiler.endStartSection("terrain");
      Lagometer.timerTerrain.start();
      if (this.mc.gameSettings.ofSmoothFps && var2 > 0) {
         this.mc.mcProfiler.endStartSection("finish");
         GL11.glFinish();
         this.mc.mcProfiler.endStartSection("terrain");
      }

      GlStateManager.matrixMode(5888);
      GlStateManager.pushMatrix();
      GlStateManager.disableAlpha();
      if (var6) {
         ShadersRender.beginTerrainSolid();
      }

      var7.renderBlockLayer(EnumWorldBlockLayer.SOLID, (double)var3, var2, var11);
      GlStateManager.enableAlpha();
      if (var6) {
         ShadersRender.beginTerrainCutoutMipped();
      }

      var7.renderBlockLayer(EnumWorldBlockLayer.CUTOUT_MIPPED, (double)var3, var2, var11);
      this.mc.getTextureManager().getTexture(TextureMap.locationBlocksTexture).setBlurMipmap(false, false);
      if (var6) {
         ShadersRender.beginTerrainCutout();
      }

      var7.renderBlockLayer(EnumWorldBlockLayer.CUTOUT, (double)var3, var2, var11);
      this.mc.getTextureManager().getTexture(TextureMap.locationBlocksTexture).restoreLastBlurMipmap();
      if (var6) {
         ShadersRender.endTerrain();
      }

      Lagometer.timerTerrain.end();
      GlStateManager.shadeModel(7424);
      GlStateManager.alphaFunc(516, 0.1F);
      EntityPlayer var18;
      if (!this.debugView) {
         GlStateManager.matrixMode(5888);
         GlStateManager.popMatrix();
         GlStateManager.pushMatrix();
         RenderHelper.enableStandardItemLighting();
         this.mc.mcProfiler.endStartSection("entities");
         if (Reflector.ForgeHooksClient_setRenderPass.exists()) {
            Reflector.callVoid(Reflector.ForgeHooksClient_setRenderPass, 0);
         }

         var7.renderEntities(var11, var10, var3);
         if (Reflector.ForgeHooksClient_setRenderPass.exists()) {
            Reflector.callVoid(Reflector.ForgeHooksClient_setRenderPass, -1);
         }

         RenderHelper.disableStandardItemLighting();
         this.disableLightmap();
         GlStateManager.matrixMode(5888);
         GlStateManager.popMatrix();
         GlStateManager.pushMatrix();
         if (this.mc.objectMouseOver != null && var11.isInsideOfMaterial(Material.water) && var9) {
            var18 = (EntityPlayer)var11;
            GlStateManager.disableAlpha();
            this.mc.mcProfiler.endStartSection("outline");
            if ((!Reflector.ForgeHooksClient_onDrawBlockHighlight.exists() || !Reflector.callBoolean(Reflector.ForgeHooksClient_onDrawBlockHighlight, var7, var18, this.mc.objectMouseOver, 0, var18.getHeldItem(), var3)) && !this.mc.gameSettings.hideGUI) {
               var7.drawSelectionBox(var18, this.mc.objectMouseOver, 0, var3);
            }

            GlStateManager.enableAlpha();
         }
      }

      GlStateManager.matrixMode(5888);
      GlStateManager.popMatrix();
      if (var9 && this.mc.objectMouseOver != null && !var11.isInsideOfMaterial(Material.water)) {
         var18 = (EntityPlayer)var11;
         GlStateManager.disableAlpha();
         this.mc.mcProfiler.endStartSection("outline");
         if ((!Reflector.ForgeHooksClient_onDrawBlockHighlight.exists() || !Reflector.callBoolean(Reflector.ForgeHooksClient_onDrawBlockHighlight, var7, var18, this.mc.objectMouseOver, 0, var18.getHeldItem(), var3)) && !this.mc.gameSettings.hideGUI) {
            var7.drawSelectionBox(var18, this.mc.objectMouseOver, 0, var3);
         }

         GlStateManager.enableAlpha();
      }

      if (!var7.damagedBlocks.isEmpty()) {
         this.mc.mcProfiler.endStartSection("destroyProgress");
         GlStateManager.enableBlend();
         GlStateManager.tryBlendFuncSeparate(770, 1, 1, 0);
         this.mc.getTextureManager().getTexture(TextureMap.locationBlocksTexture).setBlurMipmap(false, false);
         var7.drawBlockDamageTexture(Tessellator.getInstance(), Tessellator.getInstance().getWorldRenderer(), var11, var3);
         this.mc.getTextureManager().getTexture(TextureMap.locationBlocksTexture).restoreLastBlurMipmap();
         GlStateManager.disableBlend();
      }

      GlStateManager.tryBlendFuncSeparate(770, 771, 1, 0);
      GlStateManager.disableBlend();
      if (!this.debugView) {
         this.enableLightmap();
         this.mc.mcProfiler.endStartSection("litParticles");
         if (var6) {
            Shaders.beginLitParticles();
         }

         var8.renderLitParticles(var11, var3);
         RenderHelper.disableStandardItemLighting();
         this.setupFog(0, var3);
         this.mc.mcProfiler.endStartSection("particles");
         if (var6) {
            Shaders.beginParticles();
         }

         var8.renderParticles(var11, var3);
         if (var6) {
            Shaders.endParticles();
         }

         this.disableLightmap();
      }

      GlStateManager.depthMask(false);
      GlStateManager.enableCull();
      this.mc.mcProfiler.endStartSection("weather");
      if (var6) {
         Shaders.beginWeather();
      }

      this.renderRainSnow(var3);
      if (var6) {
         Shaders.endWeather();
      }

      GlStateManager.depthMask(true);
      var7.renderWorldBorder(var11, var3);
      if (var6) {
         ShadersRender.renderHand0(this, var3, var2);
         Shaders.preWater();
      }

      GlStateManager.disableBlend();
      GlStateManager.enableCull();
      GlStateManager.tryBlendFuncSeparate(770, 771, 1, 0);
      GlStateManager.alphaFunc(516, 0.1F);
      this.setupFog(0, var3);
      GlStateManager.enableBlend();
      GlStateManager.depthMask(false);
      this.mc.getTextureManager().bindTexture(TextureMap.locationBlocksTexture);
      GlStateManager.shadeModel(7425);
      this.mc.mcProfiler.endStartSection("translucent");
      if (var6) {
         Shaders.beginWater();
      }

      var7.renderBlockLayer(EnumWorldBlockLayer.TRANSLUCENT, (double)var3, var2, var11);
      if (var6) {
         Shaders.endWater();
      }

      if (Reflector.ForgeHooksClient_setRenderPass.exists() && !this.debugView) {
         RenderHelper.enableStandardItemLighting();
         this.mc.mcProfiler.endStartSection("entities");
         Reflector.callVoid(Reflector.ForgeHooksClient_setRenderPass, 1);
         this.mc.renderGlobal.renderEntities(var11, var10, var3);
         GlStateManager.tryBlendFuncSeparate(770, 771, 1, 0);
         Reflector.callVoid(Reflector.ForgeHooksClient_setRenderPass, -1);
         RenderHelper.disableStandardItemLighting();
      }

      GlStateManager.shadeModel(7424);
      GlStateManager.depthMask(true);
      GlStateManager.enableCull();
      GlStateManager.disableBlend();
      GlStateManager.disableFog();
      if (var11.posY + (double)var11.getEyeHeight() >= 128.0D + (double)(this.mc.gameSettings.ofCloudsHeight * 128.0F)) {
         this.mc.mcProfiler.endStartSection("aboveClouds");
         this.renderCloudsCheck(var7, var3, var2);
      }

      if (Reflector.ForgeHooksClient_dispatchRenderLast.exists()) {
         this.mc.mcProfiler.endStartSection("forge_render_last");
         Reflector.callVoid(Reflector.ForgeHooksClient_dispatchRenderLast, var7, var3);
      }

      Render3DEvent var20 = new Render3DEvent(var1, var3);
      var20.call();
      this.mc.mcProfiler.endStartSection("hand");
      boolean var19 = ReflectorForge.renderFirstPersonHand(this.mc.renderGlobal, var3, var2);
      if (!var19 && this.renderHand && !Shaders.isShadowPass) {
         if (var6) {
            ShadersRender.renderHand1(this, var3, var2);
            Shaders.renderCompositeFinal();
         }

         GlStateManager.clear(256);
         if (var6) {
            ShadersRender.renderFPOverlay(this, var3, var2);
         } else {
            this.renderHand(var3, var2);
         }

         this.renderWorldDirections(var3);
      }

      if (var6) {
         Shaders.endRender();
      }

   }

   public void enableLightmap() {
      GlStateManager.setActiveTexture(OpenGlHelper.lightmapTexUnit);
      GlStateManager.matrixMode(5890);
      GlStateManager.loadIdentity();
      float var1 = 0.00390625F;
      GlStateManager.scale(var1, var1, var1);
      GlStateManager.translate(8.0F, 8.0F, 8.0F);
      GlStateManager.matrixMode(5888);
      this.mc.getTextureManager().bindTexture(this.locationLightMap);
      GL11.glTexParameteri(3553, 10241, 9729);
      GL11.glTexParameteri(3553, 10240, 9729);
      GL11.glTexParameteri(3553, 10242, 10496);
      GL11.glTexParameteri(3553, 10243, 10496);
      GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
      GlStateManager.enableTexture2D();
      GlStateManager.setActiveTexture(OpenGlHelper.defaultTexUnit);
      if (Config.isShaders()) {
         Shaders.enableLightmap();
      }

   }

   public void loadShader(ResourceLocation var1) {
      if (OpenGlHelper.isFramebufferEnabled()) {
         try {
            this.theShaderGroup = new ShaderGroup(this.mc.getTextureManager(), this.resourceManager, this.mc.getFramebuffer(), var1);
            this.theShaderGroup.createBindFramebuffers(this.mc.displayWidth, this.mc.displayHeight);
            this.useShader = true;
         } catch (IOException var3) {
            logger.warn((String)("Failed to load shader: " + var1), (Throwable)var3);
            this.shaderIndex = shaderCount;
            this.useShader = false;
         } catch (JsonSyntaxException var4) {
            logger.warn((String)("Failed to load shader: " + var1), (Throwable)var4);
            this.shaderIndex = shaderCount;
            this.useShader = false;
         }
      }

   }

   static {
      shaderCount = shaderResourceLocations.length;
   }

   private void waitForServerThread() {
      this.serverWaitTimeCurrent = 0;
      if (Config.isSmoothWorld() && Config.isSingleProcessor()) {
         if (this.mc.isIntegratedServerRunning()) {
            IntegratedServer var1 = this.mc.getIntegratedServer();
            if (var1 != null) {
               boolean var2 = this.mc.isGamePaused();
               if (!var2 && !(this.mc.currentScreen instanceof GuiDownloadTerrain)) {
                  if (this.serverWaitTime > 0) {
                     Lagometer.timerServer.start();
                     Config.sleep((long)this.serverWaitTime);
                     Lagometer.timerServer.end();
                     this.serverWaitTimeCurrent = this.serverWaitTime;
                  }

                  long var3 = System.nanoTime() / 1000000L;
                  if (this.lastServerTime != 0L && this.lastServerTicks != 0) {
                     long var5 = var3 - this.lastServerTime;
                     if (var5 < 0L) {
                        this.lastServerTime = var3;
                        var5 = 0L;
                     }

                     if (var5 >= 50L) {
                        this.lastServerTime = var3;
                        int var7 = var1.getTickCounter();
                        int var8 = var7 - this.lastServerTicks;
                        if (var8 < 0) {
                           this.lastServerTicks = var7;
                           var8 = 0;
                        }

                        if (var8 < 1 && this.serverWaitTime < 100) {
                           this.serverWaitTime += 2;
                        }

                        if (var8 > 1 && this.serverWaitTime > 0) {
                           --this.serverWaitTime;
                        }

                        this.lastServerTicks = var7;
                     }
                  } else {
                     this.lastServerTime = var3;
                     this.lastServerTicks = var1.getTickCounter();
                     this.avgServerTickDiff = 1.0F;
                     this.avgServerTimeDiff = 50.0F;
                  }
               } else {
                  if (this.mc.currentScreen instanceof GuiDownloadTerrain) {
                     Config.sleep(20L);
                  }

                  this.lastServerTime = 0L;
                  this.lastServerTicks = 0;
               }
            }
         }
      } else {
         this.lastServerTime = 0L;
         this.lastServerTicks = 0;
      }

   }

   private void renderHand(float var1, int var2) {
      this.renderHand(var1, var2, true, true, false);
   }

   public void updateRenderer() {
      if (OpenGlHelper.shadersSupported && ShaderLinkHelper.getStaticShaderLinkHelper() == null) {
         ShaderLinkHelper.setNewStaticShaderLinkHelper();
      }

      this.updateFovModifierHand();
      this.updateTorchFlicker();
      this.fogColor2 = this.fogColor1;
      this.thirdPersonDistanceTemp = this.thirdPersonDistance;
      if (this.mc.gameSettings.smoothCamera) {
         float var1 = this.mc.gameSettings.mouseSensitivity * 0.6F + 0.2F;
         float var2 = var1 * var1 * var1 * 8.0F;
         this.smoothCamFilterX = this.mouseFilterXAxis.smooth(this.smoothCamYaw, 0.05F * var2);
         this.smoothCamFilterY = this.mouseFilterYAxis.smooth(this.smoothCamPitch, 0.05F * var2);
         this.smoothCamPartialTicks = 0.0F;
         this.smoothCamYaw = 0.0F;
         this.smoothCamPitch = 0.0F;
      } else {
         this.smoothCamFilterX = 0.0F;
         this.smoothCamFilterY = 0.0F;
         this.mouseFilterXAxis.reset();
         this.mouseFilterYAxis.reset();
      }

      if (this.mc.getRenderViewEntity() == null) {
         this.mc.setRenderViewEntity(Minecraft.thePlayer);
      }

      Entity var11 = this.mc.getRenderViewEntity();
      double var12 = var11.posX;
      double var4 = var11.posY + (double)var11.getEyeHeight();
      double var6 = var11.posZ;
      float var8 = Minecraft.theWorld.getLightBrightness(new BlockPos(var12, var4, var6));
      float var9 = (float)this.mc.gameSettings.renderDistanceChunks / 16.0F;
      var9 = MathHelper.clamp_float(var9, 0.0F, 1.0F);
      float var10 = var8 * (1.0F - var9) + var9;
      this.fogColor1 += (var10 - this.fogColor1) * 0.1F;
      ++this.rendererUpdateCount;
      this.itemRenderer.updateEquippedItem();
      this.addRainParticles();
      this.bossColorModifierPrev = this.bossColorModifier;
      if (BossStatus.hasColorModifier) {
         this.bossColorModifier += 0.05F;
         if (this.bossColorModifier > 1.0F) {
            this.bossColorModifier = 1.0F;
         }

         BossStatus.hasColorModifier = false;
      } else if (this.bossColorModifier > 0.0F) {
         this.bossColorModifier -= 0.0125F;
      }

   }

   public void setupOverlayRendering() {
      ScaledResolution var1 = new ScaledResolution(this.mc);
      GlStateManager.clear(256);
      GlStateManager.matrixMode(5889);
      GlStateManager.loadIdentity();
      GlStateManager.ortho(0.0D, var1.getScaledWidth_double(), var1.getScaledHeight_double(), 0.0D, 1000.0D, 3000.0D);
      GlStateManager.matrixMode(5888);
      GlStateManager.loadIdentity();
      GlStateManager.translate(0.0F, 0.0F, -2000.0F);
   }

   private void renderWorldDirections(float var1) {
      if (this.mc.gameSettings.showDebugInfo && !this.mc.gameSettings.hideGUI && !Minecraft.thePlayer.hasReducedDebug() && !this.mc.gameSettings.reducedDebugInfo) {
         Entity var2 = this.mc.getRenderViewEntity();
         GlStateManager.enableBlend();
         GlStateManager.tryBlendFuncSeparate(770, 771, 1, 0);
         GL11.glLineWidth(1.0F);
         GlStateManager.disableTexture2D();
         GlStateManager.depthMask(false);
         GlStateManager.pushMatrix();
         GlStateManager.matrixMode(5888);
         GlStateManager.loadIdentity();
         this.orientCamera(var1);
         GlStateManager.translate(0.0F, var2.getEyeHeight(), 0.0F);
         RenderGlobal.func_181563_a(new AxisAlignedBB(0.0D, 0.0D, 0.0D, 0.005D, 1.0E-4D, 1.0E-4D), 255, 0, 0, 255);
         RenderGlobal.func_181563_a(new AxisAlignedBB(0.0D, 0.0D, 0.0D, 1.0E-4D, 1.0E-4D, 0.005D), 0, 0, 255, 255);
         RenderGlobal.func_181563_a(new AxisAlignedBB(0.0D, 0.0D, 0.0D, 1.0E-4D, 0.0033D, 1.0E-4D), 0, 255, 0, 255);
         GlStateManager.popMatrix();
         GlStateManager.depthMask(true);
         GlStateManager.enableTexture2D();
         GlStateManager.disableBlend();
      }

   }

   public void loadEntityShader(Entity var1) {
      if (OpenGlHelper.shadersSupported) {
         if (this.theShaderGroup != null) {
            this.theShaderGroup.deleteShaderGroup();
         }

         this.theShaderGroup = null;
         if (var1 instanceof EntityCreeper) {
            this.loadShader(new ResourceLocation("shaders/post/creeper.json"));
         } else if (var1 instanceof EntitySpider) {
            this.loadShader(new ResourceLocation("shaders/post/spider.json"));
         } else if (var1 instanceof EntityEnderman) {
            this.loadShader(new ResourceLocation("shaders/post/invert.json"));
         } else if (Reflector.ForgeHooksClient_loadEntityShader.exists()) {
            Reflector.call(Reflector.ForgeHooksClient_loadEntityShader, var1, this);
         }
      }

   }

   private void setupFog(int var1, float var2) {
      Entity var3 = this.mc.getRenderViewEntity();
      boolean var4 = false;
      this.fogStandard = false;
      if (var3 instanceof EntityPlayer) {
         var4 = ((EntityPlayer)var3).capabilities.isCreativeMode;
      }

      GL11.glFog(2918, (FloatBuffer)this.setFogColorBuffer(this.fogColorRed, this.fogColorGreen, this.fogColorBlue, 1.0F));
      GL11.glNormal3f(0.0F, -1.0F, 0.0F);
      GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
      Block var5 = ActiveRenderInfo.getBlockAtEntityViewpoint(Minecraft.theWorld, var3, var2);
      float var6 = -1.0F;
      if (Reflector.ForgeHooksClient_getFogDensity.exists()) {
         var6 = Reflector.callFloat(Reflector.ForgeHooksClient_getFogDensity, this, var3, var5, var2, 0.1F);
      }

      if (var6 >= 0.0F) {
         GlStateManager.setFogDensity(var6);
      } else {
         float var7;
         if (var3 instanceof EntityLivingBase && ((EntityLivingBase)var3).isPotionActive(Potion.blindness)) {
            var7 = 5.0F;
            int var8 = ((EntityLivingBase)var3).getActivePotionEffect(Potion.blindness).getDuration();
            if (var8 < 20) {
               var7 = 5.0F + (this.farPlaneDistance - 5.0F) * (1.0F - (float)var8 / 20.0F);
            }

            if (Config.isShaders()) {
               Shaders.setFog(9729);
            } else {
               GlStateManager.setFog(9729);
            }

            if (var1 == -1) {
               GlStateManager.setFogStart(0.0F);
               GlStateManager.setFogEnd(var7 * 0.8F);
            } else {
               GlStateManager.setFogStart(var7 * 0.25F);
               GlStateManager.setFogEnd(var7);
            }

            if (GLContext.getCapabilities().GL_NV_fog_distance && Config.isFogFancy()) {
               GL11.glFogi(34138, 34139);
            }
         } else if (this.cloudFog) {
            if (Config.isShaders()) {
               Shaders.setFog(2048);
            } else {
               GlStateManager.setFog(2048);
            }

            GlStateManager.setFogDensity(0.1F);
         } else if (var5.getMaterial() == Material.water) {
            if (Config.isShaders()) {
               Shaders.setFog(2048);
            } else {
               GlStateManager.setFog(2048);
            }

            if (var3 instanceof EntityLivingBase && ((EntityLivingBase)var3).isPotionActive(Potion.waterBreathing)) {
               GlStateManager.setFogDensity(0.01F);
            } else {
               GlStateManager.setFogDensity(0.1F - (float)EnchantmentHelper.getRespiration(var3) * 0.03F);
            }

            if (Config.isClearWater()) {
               GlStateManager.setFogDensity(0.02F);
            }
         } else if (var5.getMaterial() == Material.lava) {
            if (Config.isShaders()) {
               Shaders.setFog(2048);
            } else {
               GlStateManager.setFog(2048);
            }

            GlStateManager.setFogDensity(2.0F);
         } else {
            var7 = this.farPlaneDistance;
            this.fogStandard = true;
            if (Config.isShaders()) {
               Shaders.setFog(9729);
            } else {
               GlStateManager.setFog(9729);
            }

            if (var1 == -1) {
               GlStateManager.setFogStart(0.0F);
               GlStateManager.setFogEnd(var7);
            } else {
               GlStateManager.setFogStart(var7 * Config.getFogStart());
               GlStateManager.setFogEnd(var7);
            }

            if (GLContext.getCapabilities().GL_NV_fog_distance) {
               if (Config.isFogFancy()) {
                  GL11.glFogi(34138, 34139);
               }

               if (Config.isFogFast()) {
                  GL11.glFogi(34138, 34140);
               }
            }

            if (Minecraft.theWorld.provider.doesXZShowFog((int)var3.posX, (int)var3.posZ)) {
               GlStateManager.setFogStart(var7 * 0.05F);
               GlStateManager.setFogEnd(var7);
            }

            if (Reflector.ForgeHooksClient_onFogRender.exists()) {
               Reflector.callVoid(Reflector.ForgeHooksClient_onFogRender, this, var3, var5, var2, var1, var7);
            }
         }
      }

      GlStateManager.enableColorMaterial();
      GlStateManager.enableFog();
      GlStateManager.colorMaterial(1028, 4608);
   }
}
