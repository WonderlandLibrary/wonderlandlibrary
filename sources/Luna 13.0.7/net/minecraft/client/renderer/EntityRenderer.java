package net.minecraft.client.renderer;

import com.google.gson.JsonSyntaxException;
import java.io.IOException;
import java.lang.reflect.Field;
import java.nio.FloatBuffer;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.concurrent.Callable;
import net.minecraft.block.Block;
import net.minecraft.block.BlockBed;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.AbstractClientPlayer;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.client.gui.GuiDownloadTerrain;
import net.minecraft.client.gui.GuiIngame;
import net.minecraft.client.gui.GuiMainMenu;
import net.minecraft.client.gui.GuiNewChat;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.MapItemRenderer;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.multiplayer.PlayerControllerMP;
import net.minecraft.client.multiplayer.WorldClient;
import net.minecraft.client.particle.EffectRenderer;
import net.minecraft.client.renderer.culling.ClippingHelperImpl;
import net.minecraft.client.renderer.culling.Frustrum;
import net.minecraft.client.renderer.texture.DynamicTexture;
import net.minecraft.client.renderer.texture.ITextureObject;
import net.minecraft.client.renderer.texture.TextureManager;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.client.resources.I18n;
import net.minecraft.client.resources.IResourceManager;
import net.minecraft.client.resources.IResourceManagerReloadListener;
import net.minecraft.client.settings.GameSettings;
import net.minecraft.client.shader.Framebuffer;
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
import net.minecraft.entity.player.PlayerCapabilities;
import net.minecraft.init.Blocks;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import net.minecraft.profiler.Profiler;
import net.minecraft.server.integrated.IntegratedServer;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.BlockPos;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.EnumWorldBlockLayer;
import net.minecraft.util.MathHelper;
import net.minecraft.util.MouseFilter;
import net.minecraft.util.MouseHelper;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.util.MovingObjectPosition.MovingObjectType;
import net.minecraft.util.ReportedException;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.Vec3;
import net.minecraft.world.World;
import net.minecraft.world.WorldProvider;
import net.minecraft.world.WorldSettings.GameType;
import net.minecraft.world.biome.BiomeGenBase;
import net.minecraft.world.biome.WorldChunkManager;
import optifine.Config;
import optifine.CustomColors;
import optifine.Lagometer;
import optifine.Lagometer.TimerNano;
import optifine.RandomMobs;
import optifine.Reflector;
import optifine.ReflectorConstructor;
import optifine.ReflectorForge;
import optifine.ReflectorMethod;
import optifine.TextureUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.ContextCapabilities;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GLContext;
import org.lwjgl.util.glu.GLU;
import org.lwjgl.util.glu.Project;
import shadersmod.client.Shaders;
import shadersmod.client.ShadersRender;
import space.lunaclient.luna.impl.events.EventRender3D;
import space.lunaclient.luna.impl.events.EventSafeWalk;

public class EntityRenderer
  implements IResourceManagerReloadListener
{
  private static final Logger logger = ;
  private static final ResourceLocation locationRainPng = new ResourceLocation("textures/environment/rain.png");
  private static final ResourceLocation locationSnowPng = new ResourceLocation("textures/environment/snow.png");
  public static boolean anaglyphEnable;
  public static int anaglyphField;
  private Minecraft mc;
  private final IResourceManager resourceManager;
  private Random random = new Random();
  private float farPlaneDistance;
  public ItemRenderer itemRenderer;
  private final MapItemRenderer theMapItemRenderer;
  private int rendererUpdateCount;
  private Entity pointedEntity;
  private MouseFilter mouseFilterXAxis = new MouseFilter();
  private MouseFilter mouseFilterYAxis = new MouseFilter();
  private float thirdPersonDistance = 4.0F;
  private float thirdPersonDistanceTemp = 4.0F;
  private float smoothCamYaw;
  private float smoothCamPitch;
  private float smoothCamFilterX;
  private float smoothCamFilterY;
  private float smoothCamPartialTicks;
  private float fovModifierHand;
  private float fovModifierHandPrev;
  private float bossColorModifier;
  private float bossColorModifierPrev;
  private boolean cloudFog;
  private boolean field_175074_C = true;
  private boolean field_175073_D = true;
  private long prevFrameTime = Minecraft.getSystemTime();
  private long renderEndNanoTime;
  private final DynamicTexture lightmapTexture;
  private final int[] lightmapColors;
  private final ResourceLocation locationLightMap;
  private boolean lightmapUpdateNeeded;
  private float torchFlickerX;
  private float field_175075_L;
  private int rainSoundCounter;
  private float[] field_175076_N = new float['Ѐ'];
  private float[] field_175077_O = new float['Ѐ'];
  private FloatBuffer fogColorBuffer = GLAllocation.createDirectFloatBuffer(16);
  public float field_175080_Q;
  public float field_175082_R;
  public float field_175081_S;
  private float fogColor2;
  private float fogColor1;
  private int field_175079_V = 0;
  private boolean field_175078_W = false;
  private double cameraZoom = 1.0D;
  private double cameraYaw;
  private double cameraPitch;
  private ShaderGroup theShaderGroup;
  private static final ResourceLocation[] shaderResourceLocations = { new ResourceLocation("shaders/post/notch.json"), new ResourceLocation("shaders/post/fxaa.json"), new ResourceLocation("shaders/post/art.json"), new ResourceLocation("shaders/post/bumpy.json"), new ResourceLocation("shaders/post/blobs2.json"), new ResourceLocation("shaders/post/pencil.json"), new ResourceLocation("shaders/post/color_convolve.json"), new ResourceLocation("shaders/post/deconverge.json"), new ResourceLocation("shaders/post/flip.json"), new ResourceLocation("shaders/post/invert.json"), new ResourceLocation("shaders/post/ntsc.json"), new ResourceLocation("shaders/post/outline.json"), new ResourceLocation("shaders/post/phosphor.json"), new ResourceLocation("shaders/post/scan_pincushion.json"), new ResourceLocation("shaders/post/sobel.json"), new ResourceLocation("shaders/post/bits.json"), new ResourceLocation("shaders/post/desaturate.json"), new ResourceLocation("shaders/post/green.json"), new ResourceLocation("shaders/post/blur.json"), new ResourceLocation("shaders/post/wobble.json"), new ResourceLocation("shaders/post/blobs.json"), new ResourceLocation("shaders/post/antialias.json"), new ResourceLocation("shaders/post/creeper.json"), new ResourceLocation("shaders/post/spider.json") };
  public static final int shaderCount = shaderResourceLocations.length;
  private int shaderIndex;
  private boolean field_175083_ad;
  public int field_175084_ae;
  private static final String __OBFID = "CL_00000947";
  private boolean initialized = false;
  private World updatedWorld = null;
  private boolean showDebugInfo = false;
  public boolean fogStandard = false;
  private float clipDistance = 128.0F;
  private long lastServerTime = 0L;
  private int lastServerTicks = 0;
  private int serverWaitTime = 0;
  private int serverWaitTimeCurrent = 0;
  private float avgServerTimeDiff = 0.0F;
  private float avgServerTickDiff = 0.0F;
  private long lastErrorCheckTimeMs = 0L;
  private ShaderGroup[] fxaaShaders = new ShaderGroup[10];
  
  public EntityRenderer(Minecraft mcIn, IResourceManager p_i45076_2_)
  {
    this.shaderIndex = shaderCount;
    this.field_175083_ad = false;
    this.field_175084_ae = 0;
    this.mc = mcIn;
    this.resourceManager = p_i45076_2_;
    this.itemRenderer = mcIn.getItemRenderer();
    this.theMapItemRenderer = new MapItemRenderer(mcIn.getTextureManager());
    this.lightmapTexture = new DynamicTexture(16, 16);
    this.locationLightMap = mcIn.getTextureManager().getDynamicTextureLocation("lightMap", this.lightmapTexture);
    this.lightmapColors = this.lightmapTexture.getTextureData();
    this.theShaderGroup = null;
    for (int var3 = 0; var3 < 32; var3++) {
      for (int var4 = 0; var4 < 32; var4++)
      {
        float var5 = var4 - 16;
        float var6 = var3 - 16;
        float var7 = MathHelper.sqrt_float(var5 * var5 + var6 * var6);
        this.field_175076_N[(var3 << 5 | var4)] = (-var6 / var7);
        this.field_175077_O[(var3 << 5 | var4)] = (var5 / var7);
      }
    }
  }
  
  public boolean isShaderActive()
  {
    return (OpenGlHelper.shadersSupported) && (this.theShaderGroup != null);
  }
  
  public void stopUseShader()
  {
    if (this.theShaderGroup != null) {
      this.theShaderGroup.deleteShaderGroup();
    }
    this.theShaderGroup = null;
    this.shaderIndex = shaderCount;
  }
  
  public void func_175071_c()
  {
    this.field_175083_ad = (!this.field_175083_ad);
  }
  
  public void func_175066_a(Entity p_175066_1_)
  {
    if (OpenGlHelper.shadersSupported)
    {
      if (this.theShaderGroup != null) {
        this.theShaderGroup.deleteShaderGroup();
      }
      this.theShaderGroup = null;
      if ((p_175066_1_ instanceof EntityCreeper)) {
        func_175069_a(new ResourceLocation("shaders/post/creeper.json"));
      } else if ((p_175066_1_ instanceof EntitySpider)) {
        func_175069_a(new ResourceLocation("shaders/post/spider.json"));
      } else if ((p_175066_1_ instanceof EntityEnderman)) {
        func_175069_a(new ResourceLocation("shaders/post/invert.json"));
      } else if (Reflector.ForgeHooksClient_loadEntityShader.exists()) {
        Reflector.call(Reflector.ForgeHooksClient_loadEntityShader, new Object[] { p_175066_1_, this });
      }
    }
  }
  
  public void activateNextShader()
  {
    if ((OpenGlHelper.shadersSupported) && ((this.mc.func_175606_aa() instanceof EntityPlayer)))
    {
      if (this.theShaderGroup != null) {
        this.theShaderGroup.deleteShaderGroup();
      }
      this.shaderIndex = ((this.shaderIndex + 1) % (shaderResourceLocations.length + 1));
      if (this.shaderIndex != shaderCount) {
        func_175069_a(shaderResourceLocations[this.shaderIndex]);
      } else {
        this.theShaderGroup = null;
      }
    }
  }
  
  private void func_175069_a(ResourceLocation p_175069_1_)
  {
    if (OpenGlHelper.isFramebufferEnabled()) {
      try
      {
        this.theShaderGroup = new ShaderGroup(this.mc.getTextureManager(), this.resourceManager, this.mc.getFramebuffer(), p_175069_1_);
        this.theShaderGroup.createBindFramebuffers(this.mc.displayWidth, this.mc.displayHeight);
        this.field_175083_ad = true;
      }
      catch (IOException var3)
      {
        logger.warn("Failed to load shader: " + p_175069_1_, var3);
        this.shaderIndex = shaderCount;
        this.field_175083_ad = false;
      }
      catch (JsonSyntaxException var4)
      {
        logger.warn("Failed to load shader: " + p_175069_1_, var4);
        this.shaderIndex = shaderCount;
        this.field_175083_ad = false;
      }
    }
  }
  
  public void onResourceManagerReload(IResourceManager resourceManager)
  {
    if (this.theShaderGroup != null) {
      this.theShaderGroup.deleteShaderGroup();
    }
    this.theShaderGroup = null;
    if (this.shaderIndex != shaderCount) {
      func_175069_a(shaderResourceLocations[this.shaderIndex]);
    } else {
      func_175066_a(this.mc.func_175606_aa());
    }
  }
  
  public void updateRenderer()
  {
    if ((OpenGlHelper.shadersSupported) && (ShaderLinkHelper.getStaticShaderLinkHelper() == null)) {
      ShaderLinkHelper.setNewStaticShaderLinkHelper();
    }
    updateFovModifierHand();
    updateTorchFlicker();
    this.fogColor2 = this.fogColor1;
    this.thirdPersonDistanceTemp = this.thirdPersonDistance;
    if (this.mc.gameSettings.smoothCamera)
    {
      float var1 = this.mc.gameSettings.mouseSensitivity * 0.6F + 0.2F;
      float var2 = var1 * var1 * var1 * 8.0F;
      this.smoothCamFilterX = this.mouseFilterXAxis.smooth(this.smoothCamYaw, 0.05F * var2);
      this.smoothCamFilterY = this.mouseFilterYAxis.smooth(this.smoothCamPitch, 0.05F * var2);
      this.smoothCamPartialTicks = 0.0F;
      this.smoothCamYaw = 0.0F;
      this.smoothCamPitch = 0.0F;
    }
    else
    {
      this.smoothCamFilterX = 0.0F;
      this.smoothCamFilterY = 0.0F;
      this.mouseFilterXAxis.func_180179_a();
      this.mouseFilterYAxis.func_180179_a();
    }
    if (this.mc.func_175606_aa() == null) {
      this.mc.func_175607_a(Minecraft.thePlayer);
    }
    Entity viewEntity = this.mc.func_175606_aa();
    double vx = viewEntity.posX;
    double vy = viewEntity.posY + viewEntity.getEyeHeight();
    double vz = viewEntity.posZ;
    float var1 = Minecraft.theWorld.getLightBrightness(new BlockPos(vx, vy, vz));
    float var2 = this.mc.gameSettings.renderDistanceChunks / 16.0F;
    var2 = MathHelper.clamp_float(var2, 0.0F, 1.0F);
    float var3 = var1 * (1.0F - var2) + var2;
    this.fogColor1 += (var3 - this.fogColor1) * 0.1F;
    this.rendererUpdateCount += 1;
    this.itemRenderer.updateEquippedItem();
    addRainParticles();
    this.bossColorModifierPrev = this.bossColorModifier;
    if (BossStatus.hasColorModifier)
    {
      this.bossColorModifier += 0.05F;
      if (this.bossColorModifier > 1.0F) {
        this.bossColorModifier = 1.0F;
      }
      BossStatus.hasColorModifier = false;
    }
    else if (this.bossColorModifier > 0.0F)
    {
      this.bossColorModifier -= 0.0125F;
    }
  }
  
  public ShaderGroup getShaderGroup()
  {
    return this.theShaderGroup;
  }
  
  public void updateShaderGroupSize(int p_147704_1_, int p_147704_2_)
  {
    if (OpenGlHelper.shadersSupported)
    {
      if (this.theShaderGroup != null) {
        this.theShaderGroup.createBindFramebuffers(p_147704_1_, p_147704_2_);
      }
      this.mc.renderGlobal.checkOcclusionQueryResult(p_147704_1_, p_147704_2_);
    }
  }
  
  public void getMouseOver(float p_78473_1_)
  {
    Entity var2 = this.mc.func_175606_aa();
    if ((var2 != null) && (Minecraft.theWorld != null))
    {
      this.mc.mcProfiler.startSection("pick");
      this.mc.pointedEntity = null;
      double var3 = Minecraft.playerController.getBlockReachDistance();
      this.mc.objectMouseOver = var2.func_174822_a(var3, p_78473_1_);
      double var5 = var3;
      Vec3 var7 = var2.func_174824_e(p_78473_1_);
      if (Minecraft.playerController.extendedReach())
      {
        var3 = 6.0D;
        var5 = 6.0D;
      }
      else
      {
        if (var3 > 3.0D) {
          var5 = 3.0D;
        }
        var3 = var5;
      }
      if (this.mc.objectMouseOver != null) {
        var5 = this.mc.objectMouseOver.hitVec.distanceTo(var7);
      }
      Vec3 var8 = var2.getLook(p_78473_1_);
      Vec3 var9 = var7.addVector(var8.xCoord * var3, var8.yCoord * var3, var8.zCoord * var3);
      this.pointedEntity = null;
      Vec3 var10 = null;
      float var11 = 1.0F;
      List var12 = Minecraft.theWorld.getEntitiesWithinAABBExcludingEntity(var2, var2.getEntityBoundingBox().addCoord(var8.xCoord * var3, var8.yCoord * var3, var8.zCoord * var3).expand(var11, var11, var11));
      double var13 = var5;
      for (int var15 = 0; var15 < var12.size(); var15++)
      {
        Entity var16 = (Entity)var12.get(var15);
        if (var16.canBeCollidedWith())
        {
          float var17 = var16.getCollisionBorderSize();
          AxisAlignedBB var18 = var16.getEntityBoundingBox().expand(var17, var17, var17);
          MovingObjectPosition var19 = var18.calculateIntercept(var7, var9);
          if (var18.isVecInside(var7))
          {
            if ((0.0D < var13) || (var13 == 0.0D))
            {
              this.pointedEntity = var16;
              var10 = var19 == null ? var7 : var19.hitVec;
              var13 = 0.0D;
            }
          }
          else if (var19 != null)
          {
            double var20 = var7.distanceTo(var19.hitVec);
            if ((var20 < var13) || (var13 == 0.0D))
            {
              boolean canRiderInteract = false;
              if (Reflector.ForgeEntity_canRiderInteract.exists()) {
                canRiderInteract = Reflector.callBoolean(var16, Reflector.ForgeEntity_canRiderInteract, new Object[0]);
              }
              if ((var16 == var2.ridingEntity) && (!canRiderInteract))
              {
                if (var13 == 0.0D)
                {
                  this.pointedEntity = var16;
                  var10 = var19.hitVec;
                }
              }
              else
              {
                this.pointedEntity = var16;
                var10 = var19.hitVec;
                var13 = var20;
              }
            }
          }
        }
      }
      if ((this.pointedEntity != null) && ((var13 < var5) || (this.mc.objectMouseOver == null)))
      {
        this.mc.objectMouseOver = new MovingObjectPosition(this.pointedEntity, var10);
        if (((this.pointedEntity instanceof EntityLivingBase)) || ((this.pointedEntity instanceof EntityItemFrame))) {
          this.mc.pointedEntity = this.pointedEntity;
        }
      }
      this.mc.mcProfiler.endSection();
    }
  }
  
  private void updateFovModifierHand()
  {
    float var1 = 1.0F;
    if ((this.mc.func_175606_aa() instanceof AbstractClientPlayer))
    {
      AbstractClientPlayer var2 = (AbstractClientPlayer)this.mc.func_175606_aa();
      var1 = var2.func_175156_o();
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
  
  private float getFOVModifier(float partialTicks, boolean p_78481_2_)
  {
    if (this.field_175078_W) {
      return 90.0F;
    }
    Entity var3 = this.mc.func_175606_aa();
    float var4 = 70.0F;
    if (p_78481_2_)
    {
      var4 = this.mc.gameSettings.fovSetting;
      if (Config.isDynamicFov()) {
        var4 *= (this.fovModifierHandPrev + (this.fovModifierHand - this.fovModifierHandPrev) * partialTicks);
      }
    }
    boolean zoomActive = false;
    if (this.mc.currentScreen == null)
    {
      GameSettings var10000 = this.mc.gameSettings;
      zoomActive = GameSettings.isKeyDown(this.mc.gameSettings.ofKeyBindZoom);
    }
    if (zoomActive)
    {
      if (!Config.zoomMode)
      {
        Config.zoomMode = true;
        this.mc.gameSettings.smoothCamera = true;
      }
      if (Config.zoomMode) {
        var4 /= 4.0F;
      }
    }
    else if (Config.zoomMode)
    {
      Config.zoomMode = false;
      this.mc.gameSettings.smoothCamera = false;
      this.mouseFilterXAxis = new MouseFilter();
      this.mouseFilterYAxis = new MouseFilter();
      this.mc.renderGlobal.displayListEntitiesDirty = true;
    }
    if (((var3 instanceof EntityLivingBase)) && (((EntityLivingBase)var3).getHealth() <= 0.0F))
    {
      float var6 = ((EntityLivingBase)var3).deathTime + partialTicks;
      var4 /= ((1.0F - 500.0F / (var6 + 500.0F)) * 2.0F + 1.0F);
    }
    Block var61 = ActiveRenderInfo.func_180786_a(Minecraft.theWorld, var3, partialTicks);
    if (var61.getMaterial() == Material.water) {
      var4 = var4 * 60.0F / 70.0F;
    }
    return var4;
  }
  
  private void hurtCameraEffect(float p_78482_1_)
  {
    if ((this.mc.func_175606_aa() instanceof EntityLivingBase))
    {
      EventSafeWalk e = new EventSafeWalk();
      e.call();
      EntityLivingBase var2 = (EntityLivingBase)this.mc.func_175606_aa();
      float var3 = var2.hurtTime - p_78482_1_;
      if (var2.getHealth() <= 0.0F)
      {
        float var4 = var2.deathTime + p_78482_1_;
        GlStateManager.rotate(40.0F - 8000.0F / (var4 + 200.0F), 0.0F, 0.0F, 1.0F);
      }
      if (var3 < 0.0F) {
        return;
      }
      var3 /= var2.maxHurtTime;
      var3 = MathHelper.sin(var3 * var3 * var3 * var3 * 3.1415927F);
      float var4 = var2.attackedAtYaw;
      GlStateManager.rotate(-var4, 0.0F, 1.0F, 0.0F);
      GlStateManager.rotate(-var3 * 14.0F, 0.0F, 0.0F, 1.0F);
      GlStateManager.rotate(var4, 0.0F, 1.0F, 0.0F);
    }
  }
  
  private void setupViewBobbing(float p_78475_1_)
  {
    if ((this.mc.func_175606_aa() instanceof EntityPlayer))
    {
      EntityPlayer var2 = (EntityPlayer)this.mc.func_175606_aa();
      float var3 = var2.distanceWalkedModified - var2.prevDistanceWalkedModified;
      float var4 = -(var2.distanceWalkedModified + var3 * p_78475_1_);
      float var5 = var2.prevCameraYaw + (var2.cameraYaw - var2.prevCameraYaw) * p_78475_1_;
      float var6 = var2.prevCameraPitch + (var2.cameraPitch - var2.prevCameraPitch) * p_78475_1_;
      GlStateManager.translate(MathHelper.sin(var4 * 3.1415927F) * var5 * 0.5F, -Math.abs(MathHelper.cos(var4 * 3.1415927F) * var5), 0.0F);
      GlStateManager.rotate(MathHelper.sin(var4 * 3.1415927F) * var5 * 3.0F, 0.0F, 0.0F, 1.0F);
      GlStateManager.rotate(Math.abs(MathHelper.cos(var4 * 3.1415927F - 0.2F) * var5) * 5.0F, 1.0F, 0.0F, 0.0F);
      GlStateManager.rotate(var6, 1.0F, 0.0F, 0.0F);
    }
  }
  
  public void orientCamera(float p_78467_1_)
  {
    Entity var2 = this.mc.func_175606_aa();
    float var3 = var2.getEyeHeight();
    double var4 = var2.prevPosX + (var2.posX - var2.prevPosX) * p_78467_1_;
    double var6 = var2.prevPosY + (var2.posY - var2.prevPosY) * p_78467_1_ + var3;
    double var8 = var2.prevPosZ + (var2.posZ - var2.prevPosZ) * p_78467_1_;
    if (((var2 instanceof EntityLivingBase)) && (((EntityLivingBase)var2).isPlayerSleeping()))
    {
      var3 = (float)(var3 + 1.0D);
      GlStateManager.translate(0.0F, 0.3F, 0.0F);
      if (!this.mc.gameSettings.debugCamEnable)
      {
        BlockPos var27 = new BlockPos(var2);
        IBlockState partialTicks = Minecraft.theWorld.getBlockState(var27);
        Block var29 = partialTicks.getBlock();
        if (Reflector.ForgeHooksClient_orientBedCamera.exists())
        {
          Reflector.callVoid(Reflector.ForgeHooksClient_orientBedCamera, new Object[] { Minecraft.theWorld, var27, partialTicks, var2 });
        }
        else if (var29 == Blocks.bed)
        {
          int var30 = ((EnumFacing)partialTicks.getValue(BlockBed.AGE)).getHorizontalIndex();
          GlStateManager.rotate(var30 * 90, 0.0F, 1.0F, 0.0F);
        }
        GlStateManager.rotate(var2.prevRotationYaw + (var2.rotationYaw - var2.prevRotationYaw) * p_78467_1_ + 180.0F, 0.0F, -1.0F, 0.0F);
        GlStateManager.rotate(var2.prevRotationPitch + (var2.rotationPitch - var2.prevRotationPitch) * p_78467_1_, -1.0F, 0.0F, 0.0F);
      }
    }
    else if (this.mc.gameSettings.thirdPersonView > 0)
    {
      double var28 = this.thirdPersonDistanceTemp + (this.thirdPersonDistance - this.thirdPersonDistanceTemp) * p_78467_1_;
      if (this.mc.gameSettings.debugCamEnable)
      {
        GlStateManager.translate(0.0F, 0.0F, (float)-var28);
      }
      else
      {
        float yaw = var2.rotationYaw;
        float pitch = var2.rotationPitch;
        if (this.mc.gameSettings.thirdPersonView == 2) {
          pitch += 180.0F;
        }
        double roll = -MathHelper.sin(yaw / 180.0F * 3.1415927F) * MathHelper.cos(pitch / 180.0F * 3.1415927F) * var28;
        double event = MathHelper.cos(yaw / 180.0F * 3.1415927F) * MathHelper.cos(pitch / 180.0F * 3.1415927F) * var28;
        double var18 = -MathHelper.sin(pitch / 180.0F * 3.1415927F) * var28;
        for (int var20 = 0; var20 < 8; var20++)
        {
          float var21 = (var20 & 0x1) * 2 - 1;
          float var22 = (var20 >> 1 & 0x1) * 2 - 1;
          float var23 = (var20 >> 2 & 0x1) * 2 - 1;
          var21 *= 0.1F;
          var22 *= 0.1F;
          var23 *= 0.1F;
          MovingObjectPosition var24 = Minecraft.theWorld.rayTraceBlocks(new Vec3(var4 + var21, var6 + var22, var8 + var23), new Vec3(var4 - roll + var21 + var23, var6 - var18 + var22, var8 - event + var23));
          if (var24 != null)
          {
            double var25 = var24.hitVec.distanceTo(new Vec3(var4, var6, var8));
            if (var25 < var28) {
              var28 = var25;
            }
          }
        }
        if (this.mc.gameSettings.thirdPersonView == 2) {
          GlStateManager.rotate(180.0F, 0.0F, 1.0F, 0.0F);
        }
        GlStateManager.rotate(var2.rotationPitch - pitch, 1.0F, 0.0F, 0.0F);
        GlStateManager.rotate(var2.rotationYaw - yaw, 0.0F, 1.0F, 0.0F);
        GlStateManager.translate(0.0F, 0.0F, (float)-var28);
        GlStateManager.rotate(yaw - var2.rotationYaw, 0.0F, 1.0F, 0.0F);
        GlStateManager.rotate(pitch - var2.rotationPitch, 1.0F, 0.0F, 0.0F);
      }
    }
    else
    {
      GlStateManager.translate(0.0F, 0.0F, -0.1F);
    }
    if (Reflector.EntityViewRenderEvent_CameraSetup_Constructor.exists())
    {
      if (!this.mc.gameSettings.debugCamEnable)
      {
        float yaw = var2.prevRotationYaw + (var2.rotationYaw - var2.prevRotationYaw) * p_78467_1_ + 180.0F;
        float pitch = var2.prevRotationPitch + (var2.rotationPitch - var2.prevRotationPitch) * p_78467_1_;
        float var31 = 0.0F;
        if ((var2 instanceof EntityAnimal))
        {
          EntityAnimal block = (EntityAnimal)var2;
          yaw = block.prevRotationYawHead + (block.rotationYawHead - block.prevRotationYawHead) * p_78467_1_ + 180.0F;
        }
        Block var32 = ActiveRenderInfo.func_180786_a(Minecraft.theWorld, var2, p_78467_1_);
        Object var33 = Reflector.newInstance(Reflector.EntityViewRenderEvent_CameraSetup_Constructor, new Object[] { this, var2, var32, Float.valueOf(p_78467_1_), Float.valueOf(yaw), Float.valueOf(pitch), Float.valueOf(var31) });
        Reflector.postForgeBusEvent(var33);
        var31 = Reflector.getFieldValueFloat(var33, Reflector.EntityViewRenderEvent_CameraSetup_roll, var31);
        pitch = Reflector.getFieldValueFloat(var33, Reflector.EntityViewRenderEvent_CameraSetup_pitch, pitch);
        yaw = Reflector.getFieldValueFloat(var33, Reflector.EntityViewRenderEvent_CameraSetup_yaw, yaw);
        GlStateManager.rotate(var31, 0.0F, 0.0F, 1.0F);
        GlStateManager.rotate(pitch, 1.0F, 0.0F, 0.0F);
        GlStateManager.rotate(yaw, 0.0F, 1.0F, 0.0F);
      }
    }
    else if (!this.mc.gameSettings.debugCamEnable)
    {
      GlStateManager.rotate(var2.prevRotationPitch + (var2.rotationPitch - var2.prevRotationPitch) * p_78467_1_, 1.0F, 0.0F, 0.0F);
      if ((var2 instanceof EntityAnimal))
      {
        EntityAnimal var281 = (EntityAnimal)var2;
        GlStateManager.rotate(var281.prevRotationYawHead + (var281.rotationYawHead - var281.prevRotationYawHead) * p_78467_1_ + 180.0F, 0.0F, 1.0F, 0.0F);
      }
      else
      {
        GlStateManager.rotate(var2.prevRotationYaw + (var2.rotationYaw - var2.prevRotationYaw) * p_78467_1_ + 180.0F, 0.0F, 1.0F, 0.0F);
      }
    }
    GlStateManager.translate(0.0F, -var3, 0.0F);
    var4 = var2.prevPosX + (var2.posX - var2.prevPosX) * p_78467_1_;
    var6 = var2.prevPosY + (var2.posY - var2.prevPosY) * p_78467_1_ + var3;
    var8 = var2.prevPosZ + (var2.posZ - var2.prevPosZ) * p_78467_1_;
    this.cloudFog = this.mc.renderGlobal.hasCloudFog(var4, var6, var8, p_78467_1_);
  }
  
  public void setupCameraTransform(float partialTicks, int pass)
  {
    this.farPlaneDistance = (this.mc.gameSettings.renderDistanceChunks * 16);
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
      GlStateManager.translate(-(pass * 2 - 1) * var3, 0.0F, 0.0F);
    }
    this.clipDistance = (this.farPlaneDistance * 2.0F);
    if (this.clipDistance < 173.0F) {
      this.clipDistance = 173.0F;
    }
    if (Minecraft.theWorld.provider.getDimensionId() == 1) {
      this.clipDistance = 256.0F;
    }
    if (this.cameraZoom != 1.0D)
    {
      GlStateManager.translate((float)this.cameraYaw, (float)-this.cameraPitch, 0.0F);
      GlStateManager.scale(this.cameraZoom, this.cameraZoom, 1.0D);
    }
    Project.gluPerspective(getFOVModifier(partialTicks, true), this.mc.displayWidth / this.mc.displayHeight, 0.05F, this.clipDistance);
    GlStateManager.matrixMode(5888);
    GlStateManager.loadIdentity();
    if (this.mc.gameSettings.anaglyph) {
      GlStateManager.translate((pass * 2 - 1) * 0.1F, 0.0F, 0.0F);
    }
    hurtCameraEffect(partialTicks);
    if (this.mc.gameSettings.viewBobbing) {
      setupViewBobbing(partialTicks);
    }
    float var4 = Minecraft.thePlayer.prevTimeInPortal + (Minecraft.thePlayer.timeInPortal - Minecraft.thePlayer.prevTimeInPortal) * partialTicks;
    if (var4 > 0.0F)
    {
      byte var5 = 20;
      if (Minecraft.thePlayer.isPotionActive(Potion.confusion)) {
        var5 = 7;
      }
      float var6 = 5.0F / (var4 * var4 + 5.0F) - var4 * 0.04F;
      var6 *= var6;
      GlStateManager.rotate((this.rendererUpdateCount + partialTicks) * var5, 0.0F, 1.0F, 1.0F);
      GlStateManager.scale(1.0F / var6, 1.0F, 1.0F);
      GlStateManager.rotate(-(this.rendererUpdateCount + partialTicks) * var5, 0.0F, 1.0F, 1.0F);
    }
    orientCamera(partialTicks);
    if (this.field_175078_W) {
      switch (this.field_175079_V)
      {
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
  
  public void renderHand(float p_78476_1_, int p_78476_2_)
  {
    if (!this.field_175078_W)
    {
      GlStateManager.matrixMode(5889);
      GlStateManager.loadIdentity();
      float var3 = 0.07F;
      if (this.mc.gameSettings.anaglyph) {
        GlStateManager.translate(-(p_78476_2_ * 2 - 1) * var3, 0.0F, 0.0F);
      }
      if (Config.isShaders()) {
        Shaders.applyHandDepth();
      }
      Project.gluPerspective(getFOVModifier(p_78476_1_, false), this.mc.displayWidth / this.mc.displayHeight, 0.05F, this.farPlaneDistance * 2.0F);
      GlStateManager.matrixMode(5888);
      GlStateManager.loadIdentity();
      if (this.mc.gameSettings.anaglyph) {
        GlStateManager.translate((p_78476_2_ * 2 - 1) * 0.1F, 0.0F, 0.0F);
      }
      boolean var4 = false;
      if ((!Config.isShaders()) || (!Shaders.isHandRendered))
      {
        GlStateManager.pushMatrix();
        hurtCameraEffect(p_78476_1_);
        if (this.mc.gameSettings.viewBobbing) {
          setupViewBobbing(p_78476_1_);
        }
        var4 = ((this.mc.func_175606_aa() instanceof EntityLivingBase)) && (((EntityLivingBase)this.mc.func_175606_aa()).isPlayerSleeping());
        if ((this.mc.gameSettings.thirdPersonView == 0) && (!var4) && (!this.mc.gameSettings.hideGUI) && (!Minecraft.playerController.enableEverythingIsScrewedUpMode()))
        {
          func_180436_i();
          if (Config.isShaders()) {
            ShadersRender.renderItemFP(this.itemRenderer, p_78476_1_);
          } else {
            this.itemRenderer.renderItemInFirstPerson(p_78476_1_);
          }
          func_175072_h();
        }
        GlStateManager.popMatrix();
      }
      if ((Config.isShaders()) && (!Shaders.isCompositeRendered)) {
        return;
      }
      func_175072_h();
      if ((this.mc.gameSettings.thirdPersonView == 0) && (!var4))
      {
        this.itemRenderer.renderOverlays(p_78476_1_);
        hurtCameraEffect(p_78476_1_);
      }
      if (this.mc.gameSettings.viewBobbing) {
        setupViewBobbing(p_78476_1_);
      }
    }
  }
  
  public void func_175072_h()
  {
    GlStateManager.setActiveTexture(OpenGlHelper.lightmapTexUnit);
    GlStateManager.func_179090_x();
    GlStateManager.setActiveTexture(OpenGlHelper.defaultTexUnit);
    if (Config.isShaders()) {
      Shaders.disableLightmap();
    }
  }
  
  public void func_180436_i()
  {
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
    GlStateManager.func_179098_w();
    GlStateManager.setActiveTexture(OpenGlHelper.defaultTexUnit);
    if (Config.isShaders()) {
      Shaders.enableLightmap();
    }
  }
  
  private void updateTorchFlicker()
  {
    this.field_175075_L = ((float)(this.field_175075_L + (Math.random() - Math.random()) * Math.random() * Math.random()));
    this.field_175075_L = ((float)(this.field_175075_L * 0.9D));
    this.torchFlickerX += (this.field_175075_L - this.torchFlickerX) * 1.0F;
    this.lightmapUpdateNeeded = true;
  }
  
  private void updateLightmap(float partialTicks)
  {
    if (this.lightmapUpdateNeeded)
    {
      this.mc.mcProfiler.startSection("lightTex");
      WorldClient var2 = Minecraft.theWorld;
      if (var2 != null)
      {
        if ((Config.isCustomColors()) && (CustomColors.updateLightmap(var2, this.torchFlickerX, this.lightmapColors, Minecraft.thePlayer.isPotionActive(Potion.nightVision))))
        {
          this.lightmapTexture.updateDynamicTexture();
          this.lightmapUpdateNeeded = false;
          this.mc.mcProfiler.endSection();
          return;
        }
        for (int var3 = 0; var3 < 256; var3++)
        {
          float var4 = var2.getSunBrightness(1.0F) * 0.95F + 0.05F;
          float var5 = var2.provider.getLightBrightnessTable()[(var3 / 16)] * var4;
          float var6 = var2.provider.getLightBrightnessTable()[(var3 % 16)] * (this.torchFlickerX * 0.1F + 1.5F);
          if (var2.func_175658_ac() > 0) {
            var5 = var2.provider.getLightBrightnessTable()[(var3 / 16)];
          }
          float var7 = var5 * (var2.getSunBrightness(1.0F) * 0.65F + 0.35F);
          float var8 = var5 * (var2.getSunBrightness(1.0F) * 0.65F + 0.35F);
          float var11 = var6 * ((var6 * 0.6F + 0.4F) * 0.6F + 0.4F);
          float var12 = var6 * (var6 * var6 * 0.6F + 0.4F);
          float var13 = var7 + var6;
          float var14 = var8 + var11;
          float var15 = var5 + var12;
          var13 = var13 * 0.96F + 0.03F;
          var14 = var14 * 0.96F + 0.03F;
          var15 = var15 * 0.96F + 0.03F;
          if (this.bossColorModifier > 0.0F)
          {
            float var16 = this.bossColorModifierPrev + (this.bossColorModifier - this.bossColorModifierPrev) * partialTicks;
            var13 = var13 * (1.0F - var16) + var13 * 0.7F * var16;
            var14 = var14 * (1.0F - var16) + var14 * 0.6F * var16;
            var15 = var15 * (1.0F - var16) + var15 * 0.6F * var16;
          }
          if (var2.provider.getDimensionId() == 1)
          {
            var13 = 0.22F + var6 * 0.75F;
            var14 = 0.28F + var11 * 0.75F;
            var15 = 0.25F + var12 * 0.75F;
          }
          if (Minecraft.thePlayer.isPotionActive(Potion.nightVision))
          {
            float var16 = func_180438_a(Minecraft.thePlayer, partialTicks);
            float var17 = 1.0F / var13;
            if (var17 > 1.0F / var14) {
              var17 = 1.0F / var14;
            }
            if (var17 > 1.0F / var15) {
              var17 = 1.0F / var15;
            }
            var13 = var13 * (1.0F - var16) + var13 * var17 * var16;
            var14 = var14 * (1.0F - var16) + var14 * var17 * var16;
            var15 = var15 * (1.0F - var16) + var15 * var17 * var16;
          }
          if (var13 > 1.0F) {
            var13 = 1.0F;
          }
          if (var14 > 1.0F) {
            var14 = 1.0F;
          }
          if (var15 > 1.0F) {
            var15 = 1.0F;
          }
          float var16 = this.mc.gameSettings.gammaSetting;
          float var17 = 1.0F - var13;
          float var18 = 1.0F - var14;
          float var19 = 1.0F - var15;
          var17 = 1.0F - var17 * var17 * var17 * var17;
          var18 = 1.0F - var18 * var18 * var18 * var18;
          var19 = 1.0F - var19 * var19 * var19 * var19;
          var13 = var13 * (1.0F - var16) + var17 * var16;
          var14 = var14 * (1.0F - var16) + var18 * var16;
          var15 = var15 * (1.0F - var16) + var19 * var16;
          var13 = var13 * 0.96F + 0.03F;
          var14 = var14 * 0.96F + 0.03F;
          var15 = var15 * 0.96F + 0.03F;
          if (var13 > 1.0F) {
            var13 = 1.0F;
          }
          if (var14 > 1.0F) {
            var14 = 1.0F;
          }
          if (var15 > 1.0F) {
            var15 = 1.0F;
          }
          if (var13 < 0.0F) {
            var13 = 0.0F;
          }
          if (var14 < 0.0F) {
            var14 = 0.0F;
          }
          if (var15 < 0.0F) {
            var15 = 0.0F;
          }
          short var20 = 255;
          int var21 = (int)(var13 * 255.0F);
          int var22 = (int)(var14 * 255.0F);
          int var23 = (int)(var15 * 255.0F);
          this.lightmapColors[var3] = (var20 << 24 | var21 << 16 | var22 << 8 | var23);
        }
        this.lightmapTexture.updateDynamicTexture();
        this.lightmapUpdateNeeded = false;
        this.mc.mcProfiler.endSection();
      }
    }
  }
  
  private float func_180438_a(EntityLivingBase p_180438_1_, float partialTicks)
  {
    int var3 = p_180438_1_.getActivePotionEffect(Potion.nightVision).getDuration();
    return var3 > 200 ? 1.0F : 0.7F + MathHelper.sin((var3 - partialTicks) * 3.1415927F * 0.2F) * 0.3F;
  }
  
  public void updateCameraAndRender(float partialTicks)
  {
    frameInit();
    boolean var2 = Display.isActive();
    if ((!var2) && (this.mc.gameSettings.pauseOnLostFocus) && ((!this.mc.gameSettings.touchscreen) || (!Mouse.isButtonDown(1))))
    {
      if (Minecraft.getSystemTime() - this.prevFrameTime > 500L) {
        this.mc.displayInGameMenu();
      }
    }
    else {
      this.prevFrameTime = Minecraft.getSystemTime();
    }
    this.mc.mcProfiler.startSection("mouse");
    if ((var2) && (Minecraft.isRunningOnMac) && (this.mc.inGameHasFocus) && (!Mouse.isInsideWindow()))
    {
      Mouse.setGrabbed(false);
      Mouse.setCursorPosition(Display.getWidth() / 2, Display.getHeight() / 2);
      Mouse.setGrabbed(true);
    }
    if ((this.mc.inGameHasFocus) && (var2))
    {
      this.mc.mouseHelper.mouseXYChange();
      float var13 = this.mc.gameSettings.mouseSensitivity * 0.6F + 0.2F;
      float var14 = var13 * var13 * var13 * 8.0F;
      float var15 = this.mc.mouseHelper.deltaX * var14;
      float var16 = this.mc.mouseHelper.deltaY * var14;
      byte var17 = 1;
      if (this.mc.gameSettings.invertMouse) {
        var17 = -1;
      }
      if (this.mc.gameSettings.smoothCamera)
      {
        this.smoothCamYaw += var15;
        this.smoothCamPitch += var16;
        float var18 = partialTicks - this.smoothCamPartialTicks;
        this.smoothCamPartialTicks = partialTicks;
        var15 = this.smoothCamFilterX * var18;
        var16 = this.smoothCamFilterY * var18;
        Minecraft.thePlayer.setAngles(var15, var16 * var17);
      }
      else
      {
        this.smoothCamYaw = 0.0F;
        this.smoothCamPitch = 0.0F;
        Minecraft.thePlayer.setAngles(var15, var16 * var17);
      }
    }
    this.mc.mcProfiler.endSection();
    if (!this.mc.skipRenderWorld)
    {
      anaglyphEnable = this.mc.gameSettings.anaglyph;
      final ScaledResolution var131 = new ScaledResolution(this.mc, this.mc.displayWidth, this.mc.displayHeight);
      int var141 = var131.getScaledWidth();
      int var151 = var131.getScaledHeight();
      final int var161 = Mouse.getX() * var141 / this.mc.displayWidth;
      final int var171 = var151 - Mouse.getY() * var151 / this.mc.displayHeight - 1;
      int var181 = this.mc.gameSettings.limitFramerate;
      if (Minecraft.theWorld != null)
      {
        this.mc.mcProfiler.startSection("level");
        int var12 = Math.max(Minecraft.func_175610_ah(), 30);
        renderWorld(partialTicks, this.renderEndNanoTime + 1000000000 / var12);
        if (OpenGlHelper.shadersSupported)
        {
          this.mc.renderGlobal.func_174975_c();
          if ((this.theShaderGroup != null) && (this.field_175083_ad))
          {
            GlStateManager.matrixMode(5890);
            GlStateManager.pushMatrix();
            GlStateManager.loadIdentity();
            this.theShaderGroup.loadShaderGroup(partialTicks);
            GlStateManager.popMatrix();
          }
          this.mc.getFramebuffer().bindFramebuffer(true);
        }
        this.renderEndNanoTime = System.nanoTime();
        this.mc.mcProfiler.endStartSection("gui");
        if ((!this.mc.gameSettings.hideGUI) || (this.mc.currentScreen != null))
        {
          GlStateManager.alphaFunc(516, 0.1F);
          this.mc.ingameGUI.func_175180_a(partialTicks);
          if ((this.mc.gameSettings.ofShowFps) && (!this.mc.gameSettings.showDebugInfo)) {
            Config.drawFps();
          }
          if (this.mc.gameSettings.showDebugInfo) {
            Lagometer.showLagometer(var131);
          }
        }
        this.mc.mcProfiler.endSection();
      }
      else
      {
        GlStateManager.viewport(0, 0, this.mc.displayWidth, this.mc.displayHeight);
        GlStateManager.matrixMode(5889);
        GlStateManager.loadIdentity();
        GlStateManager.matrixMode(5888);
        GlStateManager.loadIdentity();
        setupOverlayRendering();
        this.renderEndNanoTime = System.nanoTime();
      }
      if (this.mc.currentScreen != null)
      {
        GlStateManager.clear(256);
        try
        {
          if (Reflector.ForgeHooksClient_drawScreen.exists()) {
            Reflector.callVoid(Reflector.ForgeHooksClient_drawScreen, new Object[] { this.mc.currentScreen, Integer.valueOf(var161), Integer.valueOf(var171), Float.valueOf(partialTicks) });
          } else {
            this.mc.currentScreen.drawScreen(var161, var171, partialTicks);
          }
        }
        catch (Throwable var121)
        {
          CrashReport var10 = CrashReport.makeCrashReport(var121, "Rendering screen");
          CrashReportCategory var11 = var10.makeCategory("Screen render details");
          var11.addCrashSectionCallable("Screen name", new Callable()
          {
            private static final String __OBFID = "CL_00000948";
            
            public String call()
            {
              return EntityRenderer.this.mc.currentScreen.getClass().getCanonicalName();
            }
          });
          var11.addCrashSectionCallable("Mouse location", new Callable()
          {
            private static final String __OBFID = "CL_00000950";
            
            public String call()
            {
              return String.format("Scaled: (%d, %d). Absolute: (%d, %d)", new Object[] { Integer.valueOf(var161), Integer.valueOf(var171), Integer.valueOf(Mouse.getX()), Integer.valueOf(Mouse.getY()) });
            }
          });
          var11.addCrashSectionCallable("Screen size", new Callable()
          {
            private static final String __OBFID = "CL_00000951";
            
            public String call()
            {
              return String.format("Scaled: (%d, %d). Absolute: (%d, %d). Scale factor of %d", new Object[] { Integer.valueOf(var131.getScaledWidth()), Integer.valueOf(var131.getScaledHeight()), Integer.valueOf(EntityRenderer.this.mc.displayWidth), Integer.valueOf(EntityRenderer.this.mc.displayHeight), Integer.valueOf(var131.getScaleFactor()) });
            }
          });
          throw new ReportedException(var10);
        }
      }
    }
    frameFinish();
    waitForServerThread();
    Lagometer.updateLagometer();
    if (this.mc.gameSettings.ofProfiler) {
      this.mc.gameSettings.showDebugProfilerChart = true;
    }
  }
  
  public void func_152430_c(float p_152430_1_)
  {
    setupOverlayRendering();
    this.mc.ingameGUI.func_180478_c(new ScaledResolution(this.mc, this.mc.displayWidth, this.mc.displayHeight));
  }
  
  private boolean func_175070_n()
  {
    if (!this.field_175073_D) {
      return false;
    }
    Entity var1 = this.mc.func_175606_aa();
    boolean var2 = ((var1 instanceof EntityPlayer)) && (!this.mc.gameSettings.hideGUI);
    if ((var2) && (!((EntityPlayer)var1).capabilities.allowEdit))
    {
      ItemStack var3 = ((EntityPlayer)var1).getCurrentEquippedItem();
      if ((this.mc.objectMouseOver != null) && (this.mc.objectMouseOver.typeOfHit == MovingObjectPosition.MovingObjectType.BLOCK))
      {
        BlockPos var4 = this.mc.objectMouseOver.func_178782_a();
        IBlockState state = Minecraft.theWorld.getBlockState(var4);
        Block var5 = state.getBlock();
        if (Minecraft.playerController.func_178889_l() == WorldSettings.GameType.SPECTATOR) {
          var2 = (ReflectorForge.blockHasTileEntity(state)) && ((Minecraft.theWorld.getTileEntity(var4) instanceof IInventory));
        } else {
          var2 = (var3 != null) && ((var3.canDestroy(var5)) || (var3.canPlaceOn(var5)));
        }
      }
    }
    return var2;
  }
  
  private void func_175067_i(float p_175067_1_)
  {
    if ((this.mc.gameSettings.showDebugInfo) && (!this.mc.gameSettings.hideGUI) && (!Minecraft.thePlayer.func_175140_cp()) && (!this.mc.gameSettings.field_178879_v))
    {
      Entity var2 = this.mc.func_175606_aa();
      GlStateManager.enableBlend();
      GlStateManager.tryBlendFuncSeparate(770, 771, 1, 0);
      GL11.glLineWidth(1.0F);
      GlStateManager.func_179090_x();
      GlStateManager.depthMask(false);
      GlStateManager.pushMatrix();
      GlStateManager.matrixMode(5888);
      GlStateManager.loadIdentity();
      orientCamera(p_175067_1_);
      GlStateManager.translate(0.0F, var2.getEyeHeight(), 0.0F);
      RenderGlobal.drawOutlinedBoundingBox(new AxisAlignedBB(0.0D, 0.0D, 0.0D, 0.005D, 1.0E-4D, 1.0E-4D), -65536);
      RenderGlobal.drawOutlinedBoundingBox(new AxisAlignedBB(0.0D, 0.0D, 0.0D, 1.0E-4D, 1.0E-4D, 0.005D), -16776961);
      RenderGlobal.drawOutlinedBoundingBox(new AxisAlignedBB(0.0D, 0.0D, 0.0D, 1.0E-4D, 0.0033D, 1.0E-4D), -16711936);
      GlStateManager.popMatrix();
      GlStateManager.depthMask(true);
      GlStateManager.func_179098_w();
      GlStateManager.disableBlend();
    }
  }
  
  public void renderWorld(float partialTicks, long finishTimeNano)
  {
    updateLightmap(partialTicks);
    if (this.mc.func_175606_aa() == null) {
      this.mc.func_175607_a(Minecraft.thePlayer);
    }
    getMouseOver(partialTicks);
    if (Config.isShaders()) {
      Shaders.beginRender(this.mc, partialTicks, finishTimeNano);
    }
    GlStateManager.enableDepth();
    GlStateManager.enableAlpha();
    GlStateManager.alphaFunc(516, 0.1F);
    this.mc.mcProfiler.startSection("center");
    if (this.mc.gameSettings.anaglyph)
    {
      anaglyphField = 0;
      GlStateManager.colorMask(false, true, true, false);
      func_175068_a(0, partialTicks, finishTimeNano);
      anaglyphField = 1;
      GlStateManager.colorMask(true, false, false, false);
      func_175068_a(1, partialTicks, finishTimeNano);
      GlStateManager.colorMask(true, true, true, false);
    }
    else
    {
      func_175068_a(2, partialTicks, finishTimeNano);
    }
    this.mc.mcProfiler.endSection();
  }
  
  private void func_175068_a(int pass, float partialTicks, long finishTimeNano)
  {
    boolean isShaders = Config.isShaders();
    if (isShaders) {
      Shaders.beginRenderPass(pass, partialTicks, finishTimeNano);
    }
    RenderGlobal var5 = this.mc.renderGlobal;
    EffectRenderer var6 = this.mc.effectRenderer;
    boolean var7 = func_175070_n();
    GlStateManager.enableCull();
    this.mc.mcProfiler.endStartSection("clear");
    if (isShaders) {
      Shaders.setViewport(0, 0, this.mc.displayWidth, this.mc.displayHeight);
    } else {
      GlStateManager.viewport(0, 0, this.mc.displayWidth, this.mc.displayHeight);
    }
    updateFogColor(partialTicks);
    GlStateManager.clear(16640);
    if (isShaders) {
      Shaders.clearRenderBuffer();
    }
    this.mc.mcProfiler.endStartSection("camera");
    setupCameraTransform(partialTicks, pass);
    if (isShaders) {
      Shaders.setCamera(partialTicks);
    }
    ActiveRenderInfo.updateRenderInfo(Minecraft.thePlayer, this.mc.gameSettings.thirdPersonView == 2);
    this.mc.mcProfiler.endStartSection("frustum");
    ClippingHelperImpl.getInstance();
    this.mc.mcProfiler.endStartSection("culling");
    Frustrum var8 = new Frustrum();
    Entity var9 = this.mc.func_175606_aa();
    double var10 = var9.lastTickPosX + (var9.posX - var9.lastTickPosX) * partialTicks;
    double var12 = var9.lastTickPosY + (var9.posY - var9.lastTickPosY) * partialTicks;
    double var14 = var9.lastTickPosZ + (var9.posZ - var9.lastTickPosZ) * partialTicks;
    if (isShaders) {
      ShadersRender.setFrustrumPosition(var8, var10, var12, var14);
    } else {
      var8.setPosition(var10, var12, var14);
    }
    if (((Config.isSkyEnabled()) || (Config.isSunMoonEnabled()) || (Config.isStarsEnabled())) && (!Shaders.isShadowPass))
    {
      setupFog(-1, partialTicks);
      this.mc.mcProfiler.endStartSection("sky");
      GlStateManager.matrixMode(5889);
      GlStateManager.loadIdentity();
      Project.gluPerspective(getFOVModifier(partialTicks, true), this.mc.displayWidth / this.mc.displayHeight, 0.05F, this.clipDistance);
      GlStateManager.matrixMode(5888);
      if (isShaders) {
        Shaders.beginSky();
      }
      var5.func_174976_a(partialTicks, pass);
      if (isShaders) {
        Shaders.endSky();
      }
      GlStateManager.matrixMode(5889);
      GlStateManager.loadIdentity();
      Project.gluPerspective(getFOVModifier(partialTicks, true), this.mc.displayWidth / this.mc.displayHeight, 0.05F, this.clipDistance);
      GlStateManager.matrixMode(5888);
    }
    else
    {
      GlStateManager.disableBlend();
    }
    setupFog(0, partialTicks);
    GlStateManager.shadeModel(7425);
    if (var9.posY + var9.getEyeHeight() < 128.0D + this.mc.gameSettings.ofCloudsHeight * 128.0F) {
      func_180437_a(var5, partialTicks, pass);
    }
    this.mc.mcProfiler.endStartSection("prepareterrain");
    setupFog(0, partialTicks);
    this.mc.getTextureManager().bindTexture(TextureMap.locationBlocksTexture);
    RenderHelper.disableStandardItemLighting();
    this.mc.mcProfiler.endStartSection("terrain_setup");
    if (isShaders) {
      ShadersRender.setupTerrain(var5, var9, partialTicks, var8, this.field_175084_ae++, Minecraft.thePlayer.func_175149_v());
    } else {
      var5.func_174970_a(var9, partialTicks, var8, this.field_175084_ae++, Minecraft.thePlayer.func_175149_v());
    }
    if ((pass == 0) || (pass == 2))
    {
      this.mc.mcProfiler.endStartSection("updatechunks");
      Lagometer.timerChunkUpload.start();
      if (isShaders) {
        ShadersRender.updateChunks(var5, finishTimeNano);
      } else {
        this.mc.renderGlobal.func_174967_a(finishTimeNano);
      }
      Lagometer.timerChunkUpload.end();
    }
    this.mc.mcProfiler.endStartSection("terrain");
    Lagometer.timerTerrain.start();
    if ((this.mc.gameSettings.ofSmoothFps) && (pass > 0))
    {
      this.mc.mcProfiler.endStartSection("finish");
      GL11.glFinish();
      this.mc.mcProfiler.endStartSection("terrain");
    }
    GlStateManager.matrixMode(5888);
    GlStateManager.pushMatrix();
    GlStateManager.disableAlpha();
    if (isShaders) {
      ShadersRender.beginTerrainSolid();
    }
    var5.func_174977_a(EnumWorldBlockLayer.SOLID, partialTicks, pass, var9);
    GlStateManager.enableAlpha();
    if (isShaders) {
      ShadersRender.beginTerrainCutoutMipped();
    }
    var5.func_174977_a(EnumWorldBlockLayer.CUTOUT_MIPPED, partialTicks, pass, var9);
    this.mc.getTextureManager().getTexture(TextureMap.locationBlocksTexture).func_174936_b(false, false);
    if (isShaders) {
      ShadersRender.beginTerrainCutout();
    }
    var5.func_174977_a(EnumWorldBlockLayer.CUTOUT, partialTicks, pass, var9);
    this.mc.getTextureManager().getTexture(TextureMap.locationBlocksTexture).func_174935_a();
    if (isShaders) {
      ShadersRender.endTerrain();
    }
    Lagometer.timerTerrain.end();
    GlStateManager.shadeModel(7424);
    GlStateManager.alphaFunc(516, 0.1F);
    if (!this.field_175078_W)
    {
      GlStateManager.matrixMode(5888);
      GlStateManager.popMatrix();
      GlStateManager.pushMatrix();
      RenderHelper.enableStandardItemLighting();
      this.mc.mcProfiler.endStartSection("entities");
      if (Reflector.ForgeHooksClient_setRenderPass.exists()) {
        Reflector.callVoid(Reflector.ForgeHooksClient_setRenderPass, new Object[] { Integer.valueOf(0) });
      }
      var5.func_180446_a(var9, var8, partialTicks);
      if (Reflector.ForgeHooksClient_setRenderPass.exists()) {
        Reflector.callVoid(Reflector.ForgeHooksClient_setRenderPass, new Object[] { Integer.valueOf(-1) });
      }
      RenderHelper.disableStandardItemLighting();
      func_175072_h();
      GlStateManager.matrixMode(5888);
      GlStateManager.popMatrix();
      GlStateManager.pushMatrix();
      if ((this.mc.objectMouseOver != null) && (var9.isInsideOfMaterial(Material.water)) && (var7))
      {
        EntityPlayer var16 = (EntityPlayer)var9;
        GlStateManager.disableAlpha();
        this.mc.mcProfiler.endStartSection("outline");
        if (Reflector.ForgeHooksClient_onDrawBlockHighlight.exists())
        {
          if (Reflector.callBoolean(Reflector.ForgeHooksClient_onDrawBlockHighlight, new Object[] { var5, var16, this.mc.objectMouseOver, Integer.valueOf(0), var16.getHeldItem(), Float.valueOf(partialTicks) })) {}
        }
        else if (!this.mc.gameSettings.hideGUI) {
          var5.drawSelectionBox(var16, this.mc.objectMouseOver, 0, partialTicks);
        }
        GlStateManager.enableAlpha();
      }
    }
    GlStateManager.matrixMode(5888);
    GlStateManager.popMatrix();
    if ((var7) && (this.mc.objectMouseOver != null) && (!var9.isInsideOfMaterial(Material.water)))
    {
      EntityPlayer var16 = (EntityPlayer)var9;
      GlStateManager.disableAlpha();
      this.mc.mcProfiler.endStartSection("outline");
      if (Reflector.ForgeHooksClient_onDrawBlockHighlight.exists())
      {
        if (Reflector.callBoolean(Reflector.ForgeHooksClient_onDrawBlockHighlight, new Object[] { var5, var16, this.mc.objectMouseOver, Integer.valueOf(0), var16.getHeldItem(), Float.valueOf(partialTicks) })) {}
      }
      else if (!this.mc.gameSettings.hideGUI) {
        var5.drawSelectionBox(var16, this.mc.objectMouseOver, 0, partialTicks);
      }
      GlStateManager.enableAlpha();
    }
    if (!var5.damagedBlocks.isEmpty())
    {
      this.mc.mcProfiler.endStartSection("destroyProgress");
      GlStateManager.enableBlend();
      GlStateManager.tryBlendFuncSeparate(770, 1, 1, 0);
      var5.func_174981_a(Tessellator.getInstance(), Tessellator.getInstance().getWorldRenderer(), var9, partialTicks);
      GlStateManager.disableBlend();
    }
    GlStateManager.tryBlendFuncSeparate(770, 771, 1, 0);
    GlStateManager.disableBlend();
    if (!this.field_175078_W)
    {
      func_180436_i();
      this.mc.mcProfiler.endStartSection("litParticles");
      if (isShaders) {
        Shaders.beginLitParticles();
      }
      var6.renderLitParticles(var9, partialTicks);
      RenderHelper.disableStandardItemLighting();
      setupFog(0, partialTicks);
      this.mc.mcProfiler.endStartSection("particles");
      if (isShaders) {
        Shaders.beginParticles();
      }
      var6.renderParticles(var9, partialTicks);
      if (isShaders) {
        Shaders.endParticles();
      }
      func_175072_h();
    }
    GlStateManager.depthMask(false);
    GlStateManager.enableCull();
    this.mc.mcProfiler.endStartSection("weather");
    if (isShaders) {
      Shaders.beginWeather();
    }
    renderRainSnow(partialTicks);
    if (isShaders) {
      Shaders.endWeather();
    }
    GlStateManager.depthMask(true);
    var5.func_180449_a(var9, partialTicks);
    if (isShaders)
    {
      ShadersRender.renderHand0(this, partialTicks, pass);
      Shaders.preWater();
    }
    GlStateManager.disableBlend();
    GlStateManager.enableCull();
    GlStateManager.tryBlendFuncSeparate(770, 771, 1, 0);
    GlStateManager.alphaFunc(516, 0.1F);
    setupFog(0, partialTicks);
    GlStateManager.enableBlend();
    GlStateManager.depthMask(false);
    this.mc.getTextureManager().bindTexture(TextureMap.locationBlocksTexture);
    GlStateManager.shadeModel(7425);
    if (Config.isTranslucentBlocksFancy())
    {
      this.mc.mcProfiler.endStartSection("translucent");
      GlStateManager.enableBlend();
      GlStateManager.tryBlendFuncSeparate(770, 771, 1, 0);
      if (isShaders) {
        Shaders.beginWater();
      }
      var5.func_174977_a(EnumWorldBlockLayer.TRANSLUCENT, partialTicks, pass, var9);
      if (isShaders) {
        Shaders.endWater();
      }
      GlStateManager.disableBlend();
    }
    else
    {
      this.mc.mcProfiler.endStartSection("translucent");
      if (isShaders) {
        Shaders.beginWater();
      }
      var5.func_174977_a(EnumWorldBlockLayer.TRANSLUCENT, partialTicks, pass, var9);
      if (isShaders) {
        Shaders.endWater();
      }
    }
    if ((Reflector.ForgeHooksClient_setRenderPass.exists()) && (!this.field_175078_W))
    {
      RenderHelper.enableStandardItemLighting();
      this.mc.mcProfiler.endStartSection("entities");
      Reflector.callVoid(Reflector.ForgeHooksClient_setRenderPass, new Object[] { Integer.valueOf(1) });
      this.mc.renderGlobal.func_180446_a(var9, var8, partialTicks);
      GlStateManager.tryBlendFuncSeparate(770, 771, 1, 0);
      Reflector.callVoid(Reflector.ForgeHooksClient_setRenderPass, new Object[] { Integer.valueOf(-1) });
      RenderHelper.disableStandardItemLighting();
    }
    GlStateManager.shadeModel(7424);
    GlStateManager.depthMask(true);
    GlStateManager.enableCull();
    GlStateManager.disableBlend();
    GlStateManager.disableFog();
    
    new EventRender3D(partialTicks).call();
    if (var9.posY + var9.getEyeHeight() >= 128.0D + this.mc.gameSettings.ofCloudsHeight * 128.0F)
    {
      this.mc.mcProfiler.endStartSection("aboveClouds");
      func_180437_a(var5, partialTicks, pass);
    }
    if (Reflector.ForgeHooksClient_dispatchRenderLast.exists())
    {
      this.mc.mcProfiler.endStartSection("forge_render_last");
      Reflector.callVoid(Reflector.ForgeHooksClient_dispatchRenderLast, new Object[] { var5, Float.valueOf(partialTicks) });
    }
    this.mc.mcProfiler.endStartSection("hand");
    boolean handRendered = Reflector.callBoolean(Reflector.ForgeHooksClient_renderFirstPersonHand, new Object[] { this.mc.renderGlobal, Float.valueOf(partialTicks), Integer.valueOf(pass) });
    if ((!handRendered) && (this.field_175074_C) && (!Shaders.isShadowPass))
    {
      if (isShaders)
      {
        ShadersRender.renderHand1(this, partialTicks, pass);
        Shaders.renderCompositeFinal();
      }
      GlStateManager.clear(256);
      if (isShaders) {
        ShadersRender.renderFPOverlay(this, partialTicks, pass);
      } else {
        renderHand(partialTicks, pass);
      }
      func_175067_i(partialTicks);
    }
    if (isShaders) {
      Shaders.endRender();
    }
  }
  
  private void func_180437_a(RenderGlobal p_180437_1_, float partialTicks, int pass)
  {
    if ((this.mc.gameSettings.renderDistanceChunks >= 4) && (!Config.isCloudsOff()) && (Shaders.shouldRenderClouds(this.mc.gameSettings)))
    {
      this.mc.mcProfiler.endStartSection("clouds");
      GlStateManager.matrixMode(5889);
      GlStateManager.loadIdentity();
      Project.gluPerspective(getFOVModifier(partialTicks, true), this.mc.displayWidth / this.mc.displayHeight, 0.05F, this.clipDistance * 4.0F);
      GlStateManager.matrixMode(5888);
      GlStateManager.pushMatrix();
      setupFog(0, partialTicks);
      p_180437_1_.func_180447_b(partialTicks, pass);
      GlStateManager.disableFog();
      GlStateManager.popMatrix();
      GlStateManager.matrixMode(5889);
      GlStateManager.loadIdentity();
      Project.gluPerspective(getFOVModifier(partialTicks, true), this.mc.displayWidth / this.mc.displayHeight, 0.05F, this.clipDistance);
      GlStateManager.matrixMode(5888);
    }
  }
  
  private void addRainParticles()
  {
    float var1 = Minecraft.theWorld.getRainStrength(1.0F);
    if (!Config.isRainFancy()) {
      var1 /= 2.0F;
    }
    if ((var1 != 0.0F) && (Config.isRainSplash()))
    {
      this.random.setSeed(this.rendererUpdateCount * 312987231L);
      Entity var2 = this.mc.func_175606_aa();
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
      for (int var14 = 0; var14 < var13; var14++)
      {
        BlockPos var15 = var3.func_175725_q(var4.add(this.random.nextInt(var5) - this.random.nextInt(var5), 0, this.random.nextInt(var5) - this.random.nextInt(var5)));
        BiomeGenBase var16 = var3.getBiomeGenForCoords(var15);
        BlockPos var17 = var15.offsetDown();
        Block var18 = var3.getBlockState(var17).getBlock();
        if ((var15.getY() <= var4.getY() + var5) && (var15.getY() >= var4.getY() - var5) && (var16.canSpawnLightningBolt()) && (var16.func_180626_a(var15) >= 0.15F))
        {
          float var19 = this.random.nextFloat();
          float var20 = this.random.nextFloat();
          if (var18.getMaterial() == Material.lava)
          {
            Minecraft.theWorld.spawnParticle(EnumParticleTypes.SMOKE_NORMAL, var15.getX() + var19, var15.getY() + 0.1F - var18.getBlockBoundsMinY(), var15.getZ() + var20, 0.0D, 0.0D, 0.0D, new int[0]);
          }
          else if (var18.getMaterial() != Material.air)
          {
            var18.setBlockBoundsBasedOnState(var3, var17);
            var12++;
            if (this.random.nextInt(var12) == 0)
            {
              var6 = var17.getX() + var19;
              var8 = var17.getY() + 0.1F + var18.getBlockBoundsMaxY() - 1.0D;
              var10 = var17.getZ() + var20;
            }
            Minecraft.theWorld.spawnParticle(EnumParticleTypes.WATER_DROP, var17.getX() + var19, var17.getY() + 0.1F + var18.getBlockBoundsMaxY(), var17.getZ() + var20, 0.0D, 0.0D, 0.0D, new int[0]);
          }
        }
      }
      if ((var12 > 0) && (this.random.nextInt(3) < this.rainSoundCounter++))
      {
        this.rainSoundCounter = 0;
        if ((var8 > var4.getY() + 1) && (var3.func_175725_q(var4).getY() > MathHelper.floor_float(var4.getY()))) {
          Minecraft.theWorld.playSound(var6, var8, var10, "ambient.weather.rain", 0.1F, 0.5F, false);
        } else {
          Minecraft.theWorld.playSound(var6, var8, var10, "ambient.weather.rain", 0.2F, 1.0F, false);
        }
      }
    }
  }
  
  protected void renderRainSnow(float partialTicks)
  {
    if (Reflector.ForgeWorldProvider_getWeatherRenderer.exists())
    {
      WorldProvider var2 = Minecraft.theWorld.provider;
      Object var3 = Reflector.call(var2, Reflector.ForgeWorldProvider_getWeatherRenderer, new Object[0]);
      if (var3 != null)
      {
        Reflector.callVoid(var3, Reflector.IRenderHandler_render, new Object[] { Float.valueOf(partialTicks), Minecraft.theWorld, this.mc });
        return;
      }
    }
    float var421 = Minecraft.theWorld.getRainStrength(partialTicks);
    if (var421 > 0.0F)
    {
      if (Config.isRainOff()) {
        return;
      }
      func_180436_i();
      Entity var431 = this.mc.func_175606_aa();
      WorldClient var4 = Minecraft.theWorld;
      int var5 = MathHelper.floor_double(var431.posX);
      int var6 = MathHelper.floor_double(var431.posY);
      int var7 = MathHelper.floor_double(var431.posZ);
      Tessellator var8 = Tessellator.getInstance();
      WorldRenderer var9 = var8.getWorldRenderer();
      GlStateManager.disableCull();
      GL11.glNormal3f(0.0F, 1.0F, 0.0F);
      GlStateManager.enableBlend();
      GlStateManager.tryBlendFuncSeparate(770, 771, 1, 0);
      GlStateManager.alphaFunc(516, 0.1F);
      double var10 = var431.lastTickPosX + (var431.posX - var431.lastTickPosX) * partialTicks;
      double var12 = var431.lastTickPosY + (var431.posY - var431.lastTickPosY) * partialTicks;
      double var14 = var431.lastTickPosZ + (var431.posZ - var431.lastTickPosZ) * partialTicks;
      int var16 = MathHelper.floor_double(var12);
      byte var17 = 5;
      if (Config.isRainFancy()) {
        var17 = 10;
      }
      byte var18 = -1;
      float var19 = this.rendererUpdateCount + partialTicks;
      if (Config.isRainFancy()) {
        var17 = 10;
      }
      GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
      for (int var20 = var7 - var17; var20 <= var7 + var17; var20++) {
        for (int var21 = var5 - var17; var21 <= var5 + var17; var21++)
        {
          int var22 = (var20 - var7 + 16) * 32 + var21 - var5 + 16;
          float var23 = this.field_175076_N[var22] * 0.5F;
          float var24 = this.field_175077_O[var22] * 0.5F;
          BlockPos var25 = new BlockPos(var21, 0, var20);
          BiomeGenBase var26 = var4.getBiomeGenForCoords(var25);
          if ((var26.canSpawnLightningBolt()) || (var26.getEnableSnow()))
          {
            int var27 = var4.func_175725_q(var25).getY();
            int var28 = var6 - var17;
            int var29 = var6 + var17;
            if (var28 < var27) {
              var28 = var27;
            }
            if (var29 < var27) {
              var29 = var27;
            }
            float var30 = 1.0F;
            int var31 = var27;
            if (var27 < var16) {
              var31 = var16;
            }
            if (var28 != var29)
            {
              this.random.setSeed(var21 * var21 * 3121 + var21 * 45238971 ^ var20 * var20 * 418711 + var20 * 13761);
              float var32 = var26.func_180626_a(new BlockPos(var21, var28, var20));
              if (var4.getWorldChunkManager().getTemperatureAtHeight(var32, var27) >= 0.15F)
              {
                if (var18 != 0)
                {
                  if (var18 >= 0) {
                    var8.draw();
                  }
                  var18 = 0;
                  this.mc.getTextureManager().bindTexture(locationRainPng);
                  var9.startDrawingQuads();
                }
                float var33 = ((this.rendererUpdateCount + var21 * var21 * 3121 + var21 * 45238971 + var20 * var20 * 418711 + var20 * 13761 & 0x1F) + partialTicks) / 32.0F * (3.0F + this.random.nextFloat());
                double var42 = var21 + 0.5F - var431.posX;
                double var36 = var20 + 0.5F - var431.posZ;
                float var43 = MathHelper.sqrt_double(var42 * var42 + var36 * var36) / var17;
                float var39 = 1.0F;
                var9.func_178963_b(var4.getCombinedLight(new BlockPos(var21, var31, var20), 0));
                var9.func_178960_a(var39, var39, var39, ((1.0F - var43 * var43) * 0.5F + 0.5F) * var421);
                var9.setTranslation(-var10 * 1.0D, -var12 * 1.0D, -var14 * 1.0D);
                var9.addVertexWithUV(var21 - var23 + 0.5D, var28, var20 - var24 + 0.5D, 0.0F * var30, var28 * var30 / 4.0F + var33 * var30);
                var9.addVertexWithUV(var21 + var23 + 0.5D, var28, var20 + var24 + 0.5D, 1.0F * var30, var28 * var30 / 4.0F + var33 * var30);
                var9.addVertexWithUV(var21 + var23 + 0.5D, var29, var20 + var24 + 0.5D, 1.0F * var30, var29 * var30 / 4.0F + var33 * var30);
                var9.addVertexWithUV(var21 - var23 + 0.5D, var29, var20 - var24 + 0.5D, 0.0F * var30, var29 * var30 / 4.0F + var33 * var30);
                var9.setTranslation(0.0D, 0.0D, 0.0D);
              }
              else
              {
                if (var18 != 1)
                {
                  if (var18 >= 0) {
                    var8.draw();
                  }
                  var18 = 1;
                  this.mc.getTextureManager().bindTexture(locationSnowPng);
                  var9.startDrawingQuads();
                }
                float var33 = ((this.rendererUpdateCount & 0x1FF) + partialTicks) / 512.0F;
                float var44 = this.random.nextFloat() + var19 * 0.01F * (float)this.random.nextGaussian();
                float var35 = this.random.nextFloat() + var19 * (float)this.random.nextGaussian() * 0.001F;
                double var36 = var21 + 0.5F - var431.posX;
                double var45 = var20 + 0.5F - var431.posZ;
                float var40 = MathHelper.sqrt_double(var36 * var36 + var45 * var45) / var17;
                float var41 = 1.0F;
                var9.func_178963_b((var4.getCombinedLight(new BlockPos(var21, var31, var20), 0) * 3 + 15728880) / 4);
                var9.func_178960_a(var41, var41, var41, ((1.0F - var40 * var40) * 0.3F + 0.5F) * var421);
                var9.setTranslation(-var10 * 1.0D, -var12 * 1.0D, -var14 * 1.0D);
                var9.addVertexWithUV(var21 - var23 + 0.5D, var28, var20 - var24 + 0.5D, 0.0F * var30 + var44, var28 * var30 / 4.0F + var33 * var30 + var35);
                var9.addVertexWithUV(var21 + var23 + 0.5D, var28, var20 + var24 + 0.5D, 1.0F * var30 + var44, var28 * var30 / 4.0F + var33 * var30 + var35);
                var9.addVertexWithUV(var21 + var23 + 0.5D, var29, var20 + var24 + 0.5D, 1.0F * var30 + var44, var29 * var30 / 4.0F + var33 * var30 + var35);
                var9.addVertexWithUV(var21 - var23 + 0.5D, var29, var20 - var24 + 0.5D, 0.0F * var30 + var44, var29 * var30 / 4.0F + var33 * var30 + var35);
                var9.setTranslation(0.0D, 0.0D, 0.0D);
              }
            }
          }
        }
      }
      if (var18 >= 0) {
        var8.draw();
      }
      GlStateManager.enableCull();
      GlStateManager.disableBlend();
      GlStateManager.alphaFunc(516, 0.1F);
      func_175072_h();
    }
  }
  
  public void setupOverlayRendering()
  {
    ScaledResolution var1 = new ScaledResolution(this.mc, this.mc.displayWidth, this.mc.displayHeight);
    GlStateManager.clear(256);
    GlStateManager.matrixMode(5889);
    GlStateManager.loadIdentity();
    GlStateManager.ortho(0.0D, var1.getScaledWidth_double(), var1.getScaledHeight_double(), 0.0D, 1000.0D, 3000.0D);
    GlStateManager.matrixMode(5888);
    GlStateManager.loadIdentity();
    GlStateManager.translate(0.0F, 0.0F, -2000.0F);
  }
  
  private void updateFogColor(float partialTicks)
  {
    WorldClient var2 = Minecraft.theWorld;
    Entity var3 = this.mc.func_175606_aa();
    float var4 = 0.25F + 0.75F * this.mc.gameSettings.renderDistanceChunks / 32.0F;
    var4 = 1.0F - (float)Math.pow(var4, 0.25D);
    Vec3 var5 = var2.getSkyColor(this.mc.func_175606_aa(), partialTicks);
    var5 = CustomColors.getWorldSkyColor(var5, var2, this.mc.func_175606_aa(), partialTicks);
    float var6 = (float)var5.xCoord;
    float var7 = (float)var5.yCoord;
    float var8 = (float)var5.zCoord;
    Vec3 var9 = var2.getFogColor(partialTicks);
    var9 = CustomColors.getWorldFogColor(var9, var2, this.mc.func_175606_aa(), partialTicks);
    this.field_175080_Q = ((float)var9.xCoord);
    this.field_175082_R = ((float)var9.yCoord);
    this.field_175081_S = ((float)var9.zCoord);
    if (this.mc.gameSettings.renderDistanceChunks >= 4)
    {
      double var19 = -1.0D;
      Vec3 var20 = MathHelper.sin(var2.getCelestialAngleRadians(partialTicks)) > 0.0F ? new Vec3(var19, 0.0D, 0.0D) : new Vec3(1.0D, 0.0D, 0.0D);
      float var13 = (float)var3.getLook(partialTicks).dotProduct(var20);
      if (var13 < 0.0F) {
        var13 = 0.0F;
      }
      if (var13 > 0.0F)
      {
        float[] var21 = var2.provider.calcSunriseSunsetColors(var2.getCelestialAngle(partialTicks), partialTicks);
        if (var21 != null)
        {
          var13 *= var21[3];
          this.field_175080_Q = (this.field_175080_Q * (1.0F - var13) + var21[0] * var13);
          this.field_175082_R = (this.field_175082_R * (1.0F - var13) + var21[1] * var13);
          this.field_175081_S = (this.field_175081_S * (1.0F - var13) + var21[2] * var13);
        }
      }
    }
    this.field_175080_Q += (var6 - this.field_175080_Q) * var4;
    this.field_175082_R += (var7 - this.field_175082_R) * var4;
    this.field_175081_S += (var8 - this.field_175081_S) * var4;
    float var191 = var2.getRainStrength(partialTicks);
    if (var191 > 0.0F)
    {
      float var11 = 1.0F - var191 * 0.5F;
      float var201 = 1.0F - var191 * 0.4F;
      this.field_175080_Q *= var11;
      this.field_175082_R *= var11;
      this.field_175081_S *= var201;
    }
    float var11 = var2.getWeightedThunderStrength(partialTicks);
    if (var11 > 0.0F)
    {
      float var201 = 1.0F - var11 * 0.5F;
      this.field_175080_Q *= var201;
      this.field_175082_R *= var201;
      this.field_175081_S *= var201;
    }
    Block var211 = ActiveRenderInfo.func_180786_a(Minecraft.theWorld, var3, partialTicks);
    if (this.cloudFog)
    {
      Vec3 fogYFactor = var2.getCloudColour(partialTicks);
      this.field_175080_Q = ((float)fogYFactor.xCoord);
      this.field_175082_R = ((float)fogYFactor.yCoord);
      this.field_175081_S = ((float)fogYFactor.zCoord);
    }
    else if (var211.getMaterial() == Material.water)
    {
      float var13 = EnchantmentHelper.func_180319_a(var3) * 0.2F;
      if (((var3 instanceof EntityLivingBase)) && (((EntityLivingBase)var3).isPotionActive(Potion.waterBreathing))) {
        var13 = var13 * 0.3F + 0.6F;
      }
      this.field_175080_Q = (0.02F + var13);
      this.field_175082_R = (0.02F + var13);
      this.field_175081_S = (0.2F + var13);
      Vec3 fogYFactor = CustomColors.getUnderwaterColor(Minecraft.theWorld, this.mc.func_175606_aa().posX, this.mc.func_175606_aa().posY + 1.0D, this.mc.func_175606_aa().posZ);
      if (fogYFactor != null)
      {
        this.field_175080_Q = ((float)fogYFactor.xCoord);
        this.field_175082_R = ((float)fogYFactor.yCoord);
        this.field_175081_S = ((float)fogYFactor.zCoord);
      }
    }
    else if (var211.getMaterial() == Material.lava)
    {
      this.field_175080_Q = 0.6F;
      this.field_175082_R = 0.1F;
      this.field_175081_S = 0.0F;
    }
    float var13 = this.fogColor2 + (this.fogColor1 - this.fogColor2) * partialTicks;
    this.field_175080_Q *= var13;
    this.field_175082_R *= var13;
    this.field_175081_S *= var13;
    double fogYFactor1 = var2.provider.getVoidFogYFactor();
    double var23 = (var3.lastTickPosY + (var3.posY - var3.lastTickPosY) * partialTicks) * fogYFactor1;
    if (((var3 instanceof EntityLivingBase)) && (((EntityLivingBase)var3).isPotionActive(Potion.blindness)))
    {
      int var24 = ((EntityLivingBase)var3).getActivePotionEffect(Potion.blindness).getDuration();
      if (var24 < 20) {
        var23 *= (1.0F - var24 / 20.0F);
      } else {
        var23 = 0.0D;
      }
    }
    if (var23 < 1.0D)
    {
      if (var23 < 0.0D) {
        var23 = 0.0D;
      }
      var23 *= var23;
      this.field_175080_Q = ((float)(this.field_175080_Q * var23));
      this.field_175082_R = ((float)(this.field_175082_R * var23));
      this.field_175081_S = ((float)(this.field_175081_S * var23));
    }
    if (this.bossColorModifier > 0.0F)
    {
      float var241 = this.bossColorModifierPrev + (this.bossColorModifier - this.bossColorModifierPrev) * partialTicks;
      this.field_175080_Q = (this.field_175080_Q * (1.0F - var241) + this.field_175080_Q * 0.7F * var241);
      this.field_175082_R = (this.field_175082_R * (1.0F - var241) + this.field_175082_R * 0.6F * var241);
      this.field_175081_S = (this.field_175081_S * (1.0F - var241) + this.field_175081_S * 0.6F * var241);
    }
    if (((var3 instanceof EntityLivingBase)) && (((EntityLivingBase)var3).isPotionActive(Potion.nightVision)))
    {
      float var241 = func_180438_a((EntityLivingBase)var3, partialTicks);
      float var17 = 1.0F / this.field_175080_Q;
      if (var17 > 1.0F / this.field_175082_R) {
        var17 = 1.0F / this.field_175082_R;
      }
      if (var17 > 1.0F / this.field_175081_S) {
        var17 = 1.0F / this.field_175081_S;
      }
      this.field_175080_Q = (this.field_175080_Q * (1.0F - var241) + this.field_175080_Q * var17 * var241);
      this.field_175082_R = (this.field_175082_R * (1.0F - var241) + this.field_175082_R * var17 * var241);
      this.field_175081_S = (this.field_175081_S * (1.0F - var241) + this.field_175081_S * var17 * var241);
    }
    if (this.mc.gameSettings.anaglyph)
    {
      float var241 = (this.field_175080_Q * 30.0F + this.field_175082_R * 59.0F + this.field_175081_S * 11.0F) / 100.0F;
      float var17 = (this.field_175080_Q * 30.0F + this.field_175082_R * 70.0F) / 100.0F;
      float event = (this.field_175080_Q * 30.0F + this.field_175081_S * 70.0F) / 100.0F;
      this.field_175080_Q = var241;
      this.field_175082_R = var17;
      this.field_175081_S = event;
    }
    if (Reflector.EntityViewRenderEvent_FogColors_Constructor.exists())
    {
      Object event1 = Reflector.newInstance(Reflector.EntityViewRenderEvent_FogColors_Constructor, new Object[] { this, var3, var211, Float.valueOf(partialTicks), Float.valueOf(this.field_175080_Q), Float.valueOf(this.field_175082_R), Float.valueOf(this.field_175081_S) });
      Reflector.postForgeBusEvent(event1);
      this.field_175080_Q = Reflector.getFieldValueFloat(event1, Reflector.EntityViewRenderEvent_FogColors_red, this.field_175080_Q);
      this.field_175082_R = Reflector.getFieldValueFloat(event1, Reflector.EntityViewRenderEvent_FogColors_green, this.field_175082_R);
      this.field_175081_S = Reflector.getFieldValueFloat(event1, Reflector.EntityViewRenderEvent_FogColors_blue, this.field_175081_S);
    }
    Shaders.setClearColor(this.field_175080_Q, this.field_175082_R, this.field_175081_S, 0.0F);
  }
  
  private void setupFog(int p_78468_1_, float partialTicks)
  {
    Entity var3 = this.mc.func_175606_aa();
    boolean var4 = false;
    this.fogStandard = false;
    if ((var3 instanceof EntityPlayer)) {
      var4 = ((EntityPlayer)var3).capabilities.isCreativeMode;
    }
    GL11.glFog(2918, setFogColorBuffer(this.field_175080_Q, this.field_175082_R, this.field_175081_S, 1.0F));
    GL11.glNormal3f(0.0F, -1.0F, 0.0F);
    GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
    Block var5 = ActiveRenderInfo.func_180786_a(Minecraft.theWorld, var3, partialTicks);
    float forgeFogDensity = -1.0F;
    if (Reflector.ForgeHooksClient_getFogDensity.exists()) {
      forgeFogDensity = Reflector.callFloat(Reflector.ForgeHooksClient_getFogDensity, new Object[] { this, var3, var5, Float.valueOf(partialTicks), Float.valueOf(0.1F) });
    }
    if (forgeFogDensity >= 0.0F)
    {
      GlStateManager.setFogDensity(forgeFogDensity);
    }
    else if (((var3 instanceof EntityLivingBase)) && (((EntityLivingBase)var3).isPotionActive(Potion.blindness)))
    {
      float var6 = 5.0F;
      int var7 = ((EntityLivingBase)var3).getActivePotionEffect(Potion.blindness).getDuration();
      if (var7 < 20) {
        var6 = 5.0F + (this.farPlaneDistance - 5.0F) * (1.0F - var7 / 20.0F);
      }
      if (Config.isShaders()) {
        Shaders.setFog(9729);
      } else {
        GlStateManager.setFog(9729);
      }
      if (p_78468_1_ == -1)
      {
        GlStateManager.setFogStart(0.0F);
        GlStateManager.setFogEnd(var6 * 0.8F);
      }
      else
      {
        GlStateManager.setFogStart(var6 * 0.25F);
        GlStateManager.setFogEnd(var6);
      }
      if ((GLContext.getCapabilities().GL_NV_fog_distance) && (Config.isFogFancy())) {
        GL11.glFogi(34138, 34139);
      }
    }
    else if (this.cloudFog)
    {
      if (Config.isShaders()) {
        Shaders.setFog(2048);
      } else {
        GlStateManager.setFog(2048);
      }
      GlStateManager.setFogDensity(0.1F);
    }
    else if (var5.getMaterial() == Material.water)
    {
      if (Config.isShaders()) {
        Shaders.setFog(2048);
      } else {
        GlStateManager.setFog(2048);
      }
      if (((var3 instanceof EntityLivingBase)) && (((EntityLivingBase)var3).isPotionActive(Potion.waterBreathing))) {
        GlStateManager.setFogDensity(0.01F);
      } else {
        GlStateManager.setFogDensity(0.1F - EnchantmentHelper.func_180319_a(var3) * 0.03F);
      }
      if (Config.isClearWater()) {
        GlStateManager.setFogDensity(0.02F);
      }
    }
    else if (var5.getMaterial() == Material.lava)
    {
      if (Config.isShaders()) {
        Shaders.setFog(2048);
      } else {
        GlStateManager.setFog(2048);
      }
      GlStateManager.setFogDensity(2.0F);
    }
    else
    {
      float var6 = this.farPlaneDistance;
      this.fogStandard = true;
      if (Config.isShaders()) {
        Shaders.setFog(9729);
      } else {
        GlStateManager.setFog(9729);
      }
      if (p_78468_1_ == -1)
      {
        GlStateManager.setFogStart(0.0F);
        GlStateManager.setFogEnd(var6);
      }
      else
      {
        GlStateManager.setFogStart(var6 * Config.getFogStart());
        GlStateManager.setFogEnd(var6);
      }
      if (GLContext.getCapabilities().GL_NV_fog_distance)
      {
        if (Config.isFogFancy()) {
          GL11.glFogi(34138, 34139);
        }
        if (Config.isFogFast()) {
          GL11.glFogi(34138, 34140);
        }
      }
      if (Minecraft.theWorld.provider.doesXZShowFog((int)var3.posX, (int)var3.posZ))
      {
        GlStateManager.setFogStart(var6 * 0.05F);
        GlStateManager.setFogEnd(var6);
      }
      if (Reflector.ForgeHooksClient_onFogRender.exists()) {
        Reflector.callVoid(Reflector.ForgeHooksClient_onFogRender, new Object[] { this, var3, var5, Float.valueOf(partialTicks), Integer.valueOf(p_78468_1_), Float.valueOf(var6) });
      }
    }
    GlStateManager.enableColorMaterial();
    GlStateManager.enableFog();
    GlStateManager.colorMaterial(1028, 4608);
  }
  
  private FloatBuffer setFogColorBuffer(float p_78469_1_, float p_78469_2_, float p_78469_3_, float p_78469_4_)
  {
    if (Config.isShaders()) {
      Shaders.setFogColor(p_78469_1_, p_78469_2_, p_78469_3_);
    }
    this.fogColorBuffer.clear();
    this.fogColorBuffer.put(p_78469_1_).put(p_78469_2_).put(p_78469_3_).put(p_78469_4_);
    this.fogColorBuffer.flip();
    return this.fogColorBuffer;
  }
  
  public MapItemRenderer getMapItemRenderer()
  {
    return this.theMapItemRenderer;
  }
  
  private void waitForServerThread()
  {
    this.serverWaitTimeCurrent = 0;
    if ((Config.isSmoothWorld()) && (Config.isSingleProcessor()))
    {
      if (this.mc.isIntegratedServerRunning())
      {
        IntegratedServer srv = this.mc.getIntegratedServer();
        if (srv != null)
        {
          boolean paused = this.mc.isGamePaused();
          if ((!paused) && (!(this.mc.currentScreen instanceof GuiDownloadTerrain)))
          {
            if (this.serverWaitTime > 0)
            {
              Lagometer.timerServer.start();
              Config.sleep(this.serverWaitTime);
              Lagometer.timerServer.end();
              this.serverWaitTimeCurrent = this.serverWaitTime;
            }
            long timeNow = System.nanoTime() / 1000000L;
            if ((this.lastServerTime != 0L) && (this.lastServerTicks != 0))
            {
              long timeDiff = timeNow - this.lastServerTime;
              if (timeDiff < 0L)
              {
                this.lastServerTime = timeNow;
                timeDiff = 0L;
              }
              if (timeDiff >= 50L)
              {
                this.lastServerTime = timeNow;
                int ticks = srv.getTickCounter();
                int tickDiff = ticks - this.lastServerTicks;
                if (tickDiff < 0)
                {
                  this.lastServerTicks = ticks;
                  tickDiff = 0;
                }
                if ((tickDiff < 1) && (this.serverWaitTime < 100)) {
                  this.serverWaitTime += 2;
                }
                if ((tickDiff > 1) && (this.serverWaitTime > 0)) {
                  this.serverWaitTime -= 1;
                }
                this.lastServerTicks = ticks;
              }
            }
            else
            {
              this.lastServerTime = timeNow;
              this.lastServerTicks = srv.getTickCounter();
              this.avgServerTickDiff = 1.0F;
              this.avgServerTimeDiff = 50.0F;
            }
          }
          else
          {
            if ((this.mc.currentScreen instanceof GuiDownloadTerrain)) {
              Config.sleep(20L);
            }
            this.lastServerTime = 0L;
            this.lastServerTicks = 0;
          }
        }
      }
    }
    else
    {
      this.lastServerTime = 0L;
      this.lastServerTicks = 0;
    }
  }
  
  private void frameInit()
  {
    if (!this.initialized)
    {
      TextureUtils.registerResourceListener();
      if ((Config.getBitsOs() == 64) && (Config.getBitsJre() == 32)) {
        Config.setNotify64BitJava(true);
      }
      this.initialized = true;
    }
    Config.checkDisplayMode();
    WorldClient world = Minecraft.theWorld;
    if (world != null)
    {
      if (Config.getNewRelease() != null)
      {
        String msg = "HD_U".replace("HD_U", "HD Ultra").replace("L", "Light");
        String fullNewVer = msg + " " + Config.getNewRelease();
        ChatComponentText msg1 = new ChatComponentText(I18n.format("of.message.newVersion", new Object[] { fullNewVer }));
        this.mc.ingameGUI.getChatGUI().printChatMessage(msg1);
        Config.setNewRelease(null);
      }
      if (Config.isNotify64BitJava())
      {
        Config.setNotify64BitJava(false);
        ChatComponentText msg2 = new ChatComponentText(I18n.format("of.message.java64Bit", new Object[0]));
        this.mc.ingameGUI.getChatGUI().printChatMessage(msg2);
      }
    }
    if ((this.mc.currentScreen instanceof GuiMainMenu)) {
      updateMainMenu((GuiMainMenu)this.mc.currentScreen);
    }
    if (this.updatedWorld != world)
    {
      RandomMobs.worldChanged(this.updatedWorld, world);
      Config.updateThreadPriorities();
      this.lastServerTime = 0L;
      this.lastServerTicks = 0;
      this.updatedWorld = world;
    }
    if (!setFxaaShader(Shaders.configAntialiasingLevel)) {
      Shaders.configAntialiasingLevel = 0;
    }
  }
  
  private void frameFinish()
  {
    if (Minecraft.theWorld != null)
    {
      long now = System.currentTimeMillis();
      if (now > this.lastErrorCheckTimeMs + 10000L)
      {
        this.lastErrorCheckTimeMs = now;
        int err = GL11.glGetError();
        if (err != 0)
        {
          String text = GLU.gluErrorString(err);
          ChatComponentText msg = new ChatComponentText(I18n.format("of.message.openglError", new Object[] { Integer.valueOf(err), text }));
          this.mc.ingameGUI.getChatGUI().printChatMessage(msg);
        }
      }
    }
  }
  
  private void updateMainMenu(GuiMainMenu mainGui)
  {
    try
    {
      String e = null;
      Calendar calendar = Calendar.getInstance();
      calendar.setTime(new Date());
      int day = calendar.get(5);
      int month = calendar.get(2) + 1;
      if ((day == 8) && (month == 4)) {
        e = "Happy birthday, OptiFine!";
      }
      if ((day == 14) && (month == 8)) {
        e = "Happy birthday, sp614x!";
      }
      if (e == null) {
        return;
      }
      Field[] fs = GuiMainMenu.class.getDeclaredFields();
      for (int i = 0; i < fs.length; i++) {
        if (fs[i].getType() == String.class)
        {
          fs[i].setAccessible(true);
          fs[i].set(mainGui, e);
          break;
        }
      }
    }
    catch (Throwable localThrowable) {}
  }
  
  public boolean setFxaaShader(int fxaaLevel)
  {
    if (!OpenGlHelper.isFramebufferEnabled()) {
      return false;
    }
    if ((this.theShaderGroup != null) && (this.theShaderGroup != this.fxaaShaders[2]) && (this.theShaderGroup != this.fxaaShaders[4])) {
      return true;
    }
    if ((fxaaLevel != 2) && (fxaaLevel != 4))
    {
      if (this.theShaderGroup == null) {
        return true;
      }
      this.theShaderGroup.deleteShaderGroup();
      this.theShaderGroup = null;
      return true;
    }
    if ((this.theShaderGroup != null) && (this.theShaderGroup == this.fxaaShaders[fxaaLevel])) {
      return true;
    }
    if (Minecraft.theWorld == null) {
      return true;
    }
    func_175069_a(new ResourceLocation("shaders/post/fxaa_of_" + fxaaLevel + "x.json"));
    this.fxaaShaders[fxaaLevel] = this.theShaderGroup;
    return this.field_175083_ad;
  }
}
