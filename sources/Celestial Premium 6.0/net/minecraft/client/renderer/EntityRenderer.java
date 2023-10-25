/*
 * Decompiled with CFR 0.150.
 * 
 * Could not load the following classes:
 *  javafx.animation.Interpolator
 */
package net.minecraft.client.renderer;

import baritone.events.events.render.EventPostRender3D;
import com.google.common.base.Predicate;
import com.google.common.base.Predicates;
import com.google.gson.JsonSyntaxException;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.awt.image.RenderedImage;
import java.io.IOException;
import java.nio.FloatBuffer;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Random;
import javafx.animation.Interpolator;
import javax.annotation.Nullable;
import javax.imageio.ImageIO;
import net.minecraft.block.Block;
import net.minecraft.block.BlockBed;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.AbstractClientPlayer;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.GuiDownloadTerrain;
import net.minecraft.client.gui.GuiMainMenu;
import net.minecraft.client.gui.MapItemRenderer;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.gui.advancements.GuiScreenAdvancements;
import net.minecraft.client.multiplayer.WorldClient;
import net.minecraft.client.particle.ParticleManager;
import net.minecraft.client.renderer.ActiveRenderInfo;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.GLAllocation;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.ItemRenderer;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.client.renderer.RenderGlobal;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.block.model.ItemCameraTransforms;
import net.minecraft.client.renderer.chunk.RenderChunk;
import net.minecraft.client.renderer.culling.ClippingHelper;
import net.minecraft.client.renderer.culling.ClippingHelperImpl;
import net.minecraft.client.renderer.culling.Frustum;
import net.minecraft.client.renderer.culling.ICamera;
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
import net.minecraft.crash.ICrashReportDetail;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.item.EntityArmorStand;
import net.minecraft.entity.item.EntityItemFrame;
import net.minecraft.entity.monster.EntityCreeper;
import net.minecraft.entity.monster.EntityEnderman;
import net.minecraft.entity.monster.EntitySpider;
import net.minecraft.entity.passive.EntityAnimal;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.MobEffects;
import net.minecraft.init.SoundEvents;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.server.integrated.IntegratedServer;
import net.minecraft.util.BlockRenderLayer;
import net.minecraft.util.EntitySelectors;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.MouseFilter;
import net.minecraft.util.ReportedException;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.ScreenShotHelper;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.world.GameType;
import net.minecraft.world.World;
import net.minecraft.world.WorldProvider;
import net.minecraft.world.biome.Biome;
import optifine.Config;
import optifine.CustomColors;
import optifine.Lagometer;
import optifine.RandomMobs;
import optifine.Reflector;
import optifine.ReflectorForge;
import optifine.TextureUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.celestial.client.Celestial;
import org.celestial.client.event.EventManager;
import org.celestial.client.event.events.impl.render.EventFogColor;
import org.celestial.client.event.events.impl.render.EventRender3D;
import org.celestial.client.feature.impl.combat.Reach;
import org.celestial.client.feature.impl.player.NoInteract;
import org.celestial.client.feature.impl.visuals.FogColor;
import org.celestial.client.feature.impl.visuals.NightMode;
import org.celestial.client.feature.impl.visuals.NoRender;
import org.celestial.client.feature.impl.visuals.PersonViewer;
import org.celestial.client.feature.impl.visuals.Weather;
import org.celestial.client.feature.impl.visuals.WorldColor;
import org.celestial.client.helpers.misc.ClientHelper;
import org.celestial.client.helpers.palette.PaletteHelper;
import org.celestial.client.ui.GuiConfig;
import org.celestial.client.ui.clickgui.ClickGuiScreen;
import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GLContext;
import org.lwjgl.util.glu.GLU;
import org.lwjgl.util.glu.Project;
import shadersmod.client.Shaders;
import shadersmod.client.ShadersRender;

public class EntityRenderer
implements IResourceManagerReloadListener {
    private static final Logger LOGGER = LogManager.getLogger();
    private static final ResourceLocation RAIN_TEXTURES = new ResourceLocation("textures/environment/rain.png");
    private static final ResourceLocation SNOW_TEXTURES = new ResourceLocation("textures/environment/snow.png");
    public static boolean anaglyphEnable;
    public static int anaglyphField;
    private final Minecraft mc;
    private final IResourceManager resourceManager;
    private final Random random = new Random();
    private float farPlaneDistance;
    public ItemRenderer itemRenderer;
    private final MapItemRenderer theMapItemRenderer;
    private int rendererUpdateCount;
    private Entity pointedEntity;
    private MouseFilter mouseFilterXAxis = new MouseFilter();
    private MouseFilter mouseFilterYAxis = new MouseFilter();
    private final float thirdPersonDistance = 4.0f;
    private float thirdPersonDistancePrev = 4.0f;
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
    private boolean renderHand = true;
    private boolean drawBlockOutline = true;
    private long timeWorldIcon;
    private long prevFrameTime = Minecraft.getSystemTime();
    private long renderEndNanoTime;
    private final DynamicTexture lightmapTexture;
    private final int[] lightmapColors;
    private final ResourceLocation locationLightMap;
    private boolean lightmapUpdateNeeded;
    private float torchFlickerX;
    private float torchFlickerDX;
    private int rainSoundCounter;
    private final float[] rainXCoords = new float[1024];
    private final float[] rainYCoords = new float[1024];
    private final FloatBuffer fogColorBuffer = GLAllocation.createDirectFloatBuffer(16);
    public float fogColorRed;
    public float fogColorGreen;
    public float fogColorBlue;
    private float fogColor2;
    private float fogColor1;
    private int debugViewDirection;
    private boolean debugView;
    private double cameraZoom = 1.0;
    private double cameraYaw;
    private double cameraPitch;
    private ItemStack field_190566_ab;
    private int field_190567_ac;
    private float field_190568_ad;
    private float field_190569_ae;
    public ShaderGroup theShaderGroup;
    private static final ResourceLocation[] SHADERS_TEXTURES;
    public static final int SHADER_COUNT;
    private int shaderIndex;
    private boolean useShader;
    public int frameCount;
    private boolean initialized = false;
    private World updatedWorld = null;
    public boolean fogStandard = false;
    private float clipDistance = 128.0f;
    private long lastServerTime = 0L;
    private int lastServerTicks = 0;
    private int serverWaitTime = 0;
    private int serverWaitTimeCurrent = 0;
    private float avgServerTimeDiff = 0.0f;
    private float avgServerTickDiff = 0.0f;
    private long lastErrorCheckTimeMs = 0L;
    private ShaderGroup[] fxaaShaders = new ShaderGroup[10];
    private boolean loadVisibleChunks = false;
    private float zoomSpeed = 1.0f;
    float d3 = 0.0f;

    public EntityRenderer(Minecraft mcIn, IResourceManager resourceManagerIn) {
        this.shaderIndex = SHADER_COUNT;
        this.mc = mcIn;
        this.resourceManager = resourceManagerIn;
        this.itemRenderer = mcIn.getItemRenderer();
        this.theMapItemRenderer = new MapItemRenderer(mcIn.getTextureManager());
        this.lightmapTexture = new DynamicTexture(16, 16);
        this.locationLightMap = mcIn.getTextureManager().getDynamicTextureLocation("lightMap", this.lightmapTexture);
        this.lightmapColors = this.lightmapTexture.getTextureData();
        this.theShaderGroup = null;
        for (int i = 0; i < 32; ++i) {
            for (int j = 0; j < 32; ++j) {
                float f = j - 16;
                float f1 = i - 16;
                float f2 = MathHelper.sqrt(f * f + f1 * f1);
                this.rainXCoords[i << 5 | j] = -f1 / f2;
                this.rainYCoords[i << 5 | j] = f / f2;
            }
        }
    }

    public boolean isShaderActive() {
        return OpenGlHelper.shadersSupported && this.theShaderGroup != null;
    }

    public void stopUseShader() {
        if (this.theShaderGroup != null) {
            this.theShaderGroup.deleteShaderGroup();
        }
        this.theShaderGroup = null;
        this.shaderIndex = SHADER_COUNT;
    }

    public void switchUseShader() {
        this.useShader = !this.useShader;
    }

    public void loadEntityShader(@Nullable Entity entityIn) {
        if (OpenGlHelper.shadersSupported) {
            if (this.theShaderGroup != null) {
                this.theShaderGroup.deleteShaderGroup();
            }
            this.theShaderGroup = null;
            if (entityIn instanceof EntityCreeper) {
                this.loadShader(new ResourceLocation("shaders/post/creeper.json"));
            } else if (entityIn instanceof EntitySpider) {
                this.loadShader(new ResourceLocation("shaders/post/spider.json"));
            } else if (entityIn instanceof EntityEnderman) {
                this.loadShader(new ResourceLocation("shaders/post/invert.json"));
            } else if (Reflector.ForgeHooksClient_loadEntityShader.exists()) {
                Reflector.call(Reflector.ForgeHooksClient_loadEntityShader, entityIn, this);
            }
        }
    }

    public void loadShader(ResourceLocation resourceLocationIn) {
        if (OpenGlHelper.isFramebufferEnabled()) {
            try {
                this.theShaderGroup = new ShaderGroup(this.mc.getTextureManager(), this.resourceManager, this.mc.getFramebuffer(), resourceLocationIn);
                this.theShaderGroup.createBindFramebuffers(this.mc.displayWidth, this.mc.displayHeight);
                this.useShader = true;
            }
            catch (IOException ioexception) {
                LOGGER.warn("Failed to load shader: {}", resourceLocationIn, ioexception);
                this.shaderIndex = SHADER_COUNT;
                this.useShader = false;
            }
            catch (JsonSyntaxException jsonsyntaxexception) {
                LOGGER.warn("Failed to load shader: {}", resourceLocationIn, jsonsyntaxexception);
                this.shaderIndex = SHADER_COUNT;
                this.useShader = false;
            }
        }
    }

    @Override
    public void onResourceManagerReload(IResourceManager resourceManager) {
        if (this.theShaderGroup != null) {
            this.theShaderGroup.deleteShaderGroup();
        }
        this.theShaderGroup = null;
        if (this.shaderIndex == SHADER_COUNT) {
            this.loadEntityShader(this.mc.getRenderViewEntity());
        } else {
            this.loadShader(SHADERS_TEXTURES[this.shaderIndex]);
        }
    }

    public void updateRenderer() {
        if (OpenGlHelper.shadersSupported && ShaderLinkHelper.getStaticShaderLinkHelper() == null) {
            ShaderLinkHelper.setNewStaticShaderLinkHelper();
        }
        this.updateFovModifierHand();
        this.updateTorchFlicker();
        this.fogColor2 = this.fogColor1;
        this.thirdPersonDistancePrev = 4.0f;
        if (this.mc.gameSettings.smoothCamera) {
            float f = this.mc.gameSettings.mouseSensitivity * 0.6f + 0.2f;
            float f1 = f * f * f * 8.0f;
            this.smoothCamFilterX = this.mouseFilterXAxis.smooth(this.smoothCamYaw, 0.05f * f1);
            this.smoothCamFilterY = this.mouseFilterYAxis.smooth(this.smoothCamPitch, 0.05f * f1);
            this.smoothCamPartialTicks = 0.0f;
            this.smoothCamYaw = 0.0f;
            this.smoothCamPitch = 0.0f;
        } else {
            this.smoothCamFilterX = 0.0f;
            this.smoothCamFilterY = 0.0f;
            this.mouseFilterXAxis.reset();
            this.mouseFilterYAxis.reset();
        }
        if (this.mc.getRenderViewEntity() == null) {
            this.mc.setRenderViewEntity(this.mc.player);
        }
        Entity entity = this.mc.getRenderViewEntity();
        double d2 = entity.posX;
        double d0 = entity.posY + (double)entity.getEyeHeight();
        double d1 = entity.posZ;
        float f2 = this.mc.world.getLightBrightness(new BlockPos(d2, d0, d1));
        float f3 = (float)this.mc.gameSettings.renderDistanceChunks / 16.0f;
        f3 = MathHelper.clamp(f3, 0.0f, 1.0f);
        float f4 = f2 * (1.0f - f3) + f3;
        this.fogColor1 += (f4 - this.fogColor1) * 0.1f;
        ++this.rendererUpdateCount;
        this.itemRenderer.updateEquippedItem();
        this.addRainParticles();
        this.bossColorModifierPrev = this.bossColorModifier;
        if (this.mc.ingameGUI.getBossOverlay().shouldDarkenSky()) {
            this.bossColorModifier += 0.05f;
            if (this.bossColorModifier > 1.0f) {
                this.bossColorModifier = 1.0f;
            }
        } else if (this.bossColorModifier > 0.0f) {
            this.bossColorModifier -= 0.0125f;
        }
        if (this.field_190567_ac > 0) {
            --this.field_190567_ac;
            if (this.field_190567_ac == 0) {
                this.field_190566_ab = null;
            }
        }
    }

    public ShaderGroup getShaderGroup() {
        return this.theShaderGroup;
    }

    public void updateShaderGroupSize(int width, int height) {
        if (OpenGlHelper.shadersSupported) {
            if (this.theShaderGroup != null) {
                this.theShaderGroup.createBindFramebuffers(width, height);
            }
            this.mc.renderGlobal.createBindEntityOutlineFbs(width, height);
        }
    }

    public void getMouseOver(float partialTicks) {
        Entity entity = this.mc.getRenderViewEntity();
        if (entity != null && this.mc.world != null) {
            double reachValue;
            this.mc.mcProfiler.startSection("pick");
            this.mc.pointedEntity = null;
            double d0 = this.mc.playerController.getBlockReachDistance();
            this.mc.objectMouseOver = entity.rayTrace(d0, partialTicks);
            Vec3d vec3d = entity.getPositionEyes(partialTicks);
            boolean flag = false;
            double d1 = d0;
            double d = reachValue = Celestial.instance.featureManager.getFeatureByClass(Reach.class).getState() ? (double)Reach.reachValue.getCurrentValue() : 3.0;
            if (this.mc.playerController.extendedReach()) {
                d0 = d1 = 6.0;
            } else if (d0 > reachValue) {
                flag = true;
            }
            if (this.mc.objectMouseOver != null) {
                d1 = this.mc.objectMouseOver.hitVec.distanceTo(vec3d);
            }
            Vec3d vec3d1 = entity.getLook(1.0f);
            Vec3d vec3d2 = vec3d.add(vec3d1.x * d0, vec3d1.y * d0, vec3d1.z * d0);
            this.pointedEntity = null;
            Vec3d vec3d3 = null;
            List<Entity> list = this.mc.world.getEntitiesInAABBexcluding(entity, entity.getEntityBoundingBox().addCoord(vec3d1.x * d0, vec3d1.y * d0, vec3d1.z * d0).expand(1.0, 1.0, 1.0), Predicates.and(EntitySelectors.NOT_SPECTATING, new Predicate<Entity>(){

                @Override
                public boolean apply(@Nullable Entity p_apply_1_) {
                    return p_apply_1_ != null && p_apply_1_.canBeCollidedWith();
                }
            }));
            double d2 = d1;
            for (Entity entity1 : list) {
                double d3;
                if (entity1 instanceof EntityArmorStand && Celestial.instance.featureManager.getFeatureByClass(NoInteract.class).getState() && NoInteract.armorStands.getCurrentValue()) {
                    return;
                }
                AxisAlignedBB axisalignedbb = entity1.getEntityBoundingBox().expandXyz(entity1.getCollisionBorderSize());
                RayTraceResult raytraceresult = axisalignedbb.calculateIntercept(vec3d, vec3d2);
                if (axisalignedbb.isVecInside(vec3d)) {
                    if (!(d2 >= 0.0)) continue;
                    this.pointedEntity = entity1;
                    vec3d3 = raytraceresult == null ? vec3d : raytraceresult.hitVec;
                    d2 = 0.0;
                    continue;
                }
                if (raytraceresult == null || !((d3 = vec3d.distanceTo(raytraceresult.hitVec)) < d2) && d2 != 0.0) continue;
                boolean flag1 = false;
                if (Reflector.ForgeEntity_canRiderInteract.exists()) {
                    flag1 = Reflector.callBoolean(entity1, Reflector.ForgeEntity_canRiderInteract, new Object[0]);
                }
                if (!flag1 && entity1.getLowestRidingEntity() == entity.getLowestRidingEntity()) {
                    if (d2 != 0.0) continue;
                    this.pointedEntity = entity1;
                    vec3d3 = raytraceresult.hitVec;
                    continue;
                }
                this.pointedEntity = entity1;
                vec3d3 = raytraceresult.hitVec;
                d2 = d3;
            }
            if (this.pointedEntity != null && flag && vec3d.distanceTo(vec3d3) > reachValue) {
                this.pointedEntity = null;
                this.mc.objectMouseOver = new RayTraceResult(RayTraceResult.Type.MISS, vec3d3, null, new BlockPos(vec3d3));
            }
            if (this.pointedEntity != null && (d2 < d1 || this.mc.objectMouseOver == null)) {
                this.mc.objectMouseOver = new RayTraceResult(this.pointedEntity, vec3d3);
                if (this.pointedEntity instanceof EntityLivingBase || this.pointedEntity instanceof EntityItemFrame) {
                    this.mc.pointedEntity = this.pointedEntity;
                }
            }
            this.mc.mcProfiler.endSection();
        }
    }

    private void updateFovModifierHand() {
        float f = 1.0f;
        if (this.mc.getRenderViewEntity() instanceof AbstractClientPlayer) {
            AbstractClientPlayer abstractclientplayer = (AbstractClientPlayer)this.mc.getRenderViewEntity();
            f = abstractclientplayer.getFovModifier();
        }
        this.fovModifierHandPrev = this.fovModifierHand;
        this.fovModifierHand += (f - this.fovModifierHand) * 0.5f;
        if (this.fovModifierHand > 1.5f) {
            this.fovModifierHand = 1.5f;
        }
        if (this.fovModifierHand < 0.1f) {
            this.fovModifierHand = 0.1f;
        }
    }

    private float getFOVModifier(float partialTicks, boolean useFOVSetting) {
        IBlockState iblockstate;
        if (this.debugView) {
            return 90.0f;
        }
        Entity entity = this.mc.getRenderViewEntity();
        float f = 70.0f;
        if (useFOVSetting) {
            f = this.mc.gameSettings.fovSetting;
            if (Config.isDynamicFov()) {
                f *= this.fovModifierHandPrev + (this.fovModifierHand - this.fovModifierHandPrev) * partialTicks;
            }
        }
        boolean flag = false;
        if (this.mc.currentScreen == null) {
            flag = GameSettings.isKeyDown(this.mc.gameSettings.ofKeyBindZoom);
        }
        double zoomSpeedPress = 0.05;
        double zoomSpeedUNPress = 0.03;
        if (flag) {
            if (!Config.zoomMode) {
                Config.zoomMode = true;
                this.mc.gameSettings.smoothCamera = true;
                this.mc.renderGlobal.displayListEntitiesDirty = true;
            }
            if (this.zoomSpeed < 5.0f) {
                this.zoomSpeed = (float)((double)this.zoomSpeed + zoomSpeedPress * (Minecraft.frameTime * 0.1));
            }
        } else {
            if (this.zoomSpeed > 1.0f) {
                this.zoomSpeed = (float)((double)this.zoomSpeed - zoomSpeedUNPress * (Minecraft.frameTime * 0.1));
            }
            if (Config.zoomMode && this.zoomSpeed < 1.0f) {
                Config.zoomMode = false;
                this.mc.gameSettings.smoothCamera = false;
                this.mouseFilterXAxis = new MouseFilter();
                this.mouseFilterYAxis = new MouseFilter();
                this.mc.renderGlobal.displayListEntitiesDirty = true;
            }
        }
        f /= this.zoomSpeed;
        if (entity instanceof EntityLivingBase && ((EntityLivingBase)entity).getHealth() <= 0.0f) {
            float f1 = (float)((EntityLivingBase)entity).deathTime + partialTicks;
            f /= (1.0f - 500.0f / (f1 + 500.0f)) * 2.0f + 1.0f;
        }
        if ((iblockstate = ActiveRenderInfo.getBlockStateAtEntityViewpoint(this.mc.world, entity, partialTicks)).getMaterial() == Material.WATER) {
            f = f * 60.0f / 70.0f;
        }
        return Reflector.ForgeHooksClient_getFOVModifier.exists() ? Reflector.callFloat(Reflector.ForgeHooksClient_getFOVModifier, this, entity, iblockstate, Float.valueOf(partialTicks), Float.valueOf(f)) : f;
    }

    private void hurtCameraEffect(float partialTicks) {
        if (Celestial.instance.featureManager.getFeatureByClass(NoRender.class).getState() && NoRender.hurt.getCurrentValue()) {
            return;
        }
        if (this.mc.getRenderViewEntity() instanceof EntityLivingBase) {
            EntityLivingBase entitylivingbase = (EntityLivingBase)this.mc.getRenderViewEntity();
            float f = (float)entitylivingbase.hurtTime - partialTicks;
            if (entitylivingbase.getHealth() <= 0.0f) {
                float f1 = (float)entitylivingbase.deathTime + partialTicks;
                GlStateManager.rotate(40.0f - 8000.0f / (f1 + 200.0f), 0.0f, 0.0f, 1.0f);
            }
            if (f < 0.0f) {
                return;
            }
            f /= (float)entitylivingbase.maxHurtTime;
            f = MathHelper.sin(f * f * f * f * (float)Math.PI);
            float f2 = entitylivingbase.attackedAtYaw;
            GlStateManager.rotate(-f2, 0.0f, 1.0f, 0.0f);
            GlStateManager.rotate(-f * 14.0f, 0.0f, 0.0f, 1.0f);
            GlStateManager.rotate(f2, 0.0f, 1.0f, 0.0f);
        }
    }

    private void setupViewBobbing(float partialTicks) {
        if (this.mc.getRenderViewEntity() instanceof EntityPlayer) {
            EntityPlayer entityplayer = (EntityPlayer)this.mc.getRenderViewEntity();
            float f = entityplayer.distanceWalkedModified - entityplayer.prevDistanceWalkedModified;
            float f1 = -(entityplayer.distanceWalkedModified + f * partialTicks);
            float f2 = entityplayer.prevCameraYaw + (entityplayer.cameraYaw - entityplayer.prevCameraYaw) * partialTicks;
            float f3 = entityplayer.prevCameraPitch + (entityplayer.cameraPitch - entityplayer.prevCameraPitch) * partialTicks;
            GlStateManager.translate(MathHelper.sin(f1 * (float)Math.PI) * f2 * 0.5f, -Math.abs(MathHelper.cos(f1 * (float)Math.PI) * f2), 0.0f);
            GlStateManager.rotate(MathHelper.sin(f1 * (float)Math.PI) * f2 * 3.0f, 0.0f, 0.0f, 1.0f);
            GlStateManager.rotate(Math.abs(MathHelper.cos(f1 * (float)Math.PI - 0.2f) * f2) * 5.0f, 1.0f, 0.0f, 0.0f);
            GlStateManager.rotate(f3, 1.0f, 0.0f, 0.0f);
        }
    }

    private void orientCamera(float partialTicks) {
        Entity entity = this.mc.getRenderViewEntity();
        float f = entity.getEyeHeight();
        double d0 = entity.prevPosX + (entity.posX - entity.prevPosX) * (double)partialTicks;
        double d1 = entity.prevPosY + (entity.posY - entity.prevPosY) * (double)partialTicks + (double)f;
        double d2 = entity.prevPosZ + (entity.posZ - entity.prevPosZ) * (double)partialTicks;
        if (entity instanceof EntityLivingBase && ((EntityLivingBase)entity).isPlayerSleeping()) {
            f = (float)((double)f + 1.0);
            GlStateManager.translate(0.0f, 0.3f, 0.0f);
            if (!this.mc.gameSettings.debugCamEnable) {
                BlockPos blockpos = new BlockPos(entity);
                IBlockState iblockstate = this.mc.world.getBlockState(blockpos);
                Block block = iblockstate.getBlock();
                if (Reflector.ForgeHooksClient_orientBedCamera.exists()) {
                    Reflector.callVoid(Reflector.ForgeHooksClient_orientBedCamera, this.mc.world, blockpos, iblockstate, entity);
                } else if (block == Blocks.BED) {
                    int j = iblockstate.getValue(BlockBed.FACING).getHorizontalIndex();
                    GlStateManager.rotate(j * 90, 0.0f, 1.0f, 0.0f);
                }
                GlStateManager.rotate(entity.prevRotationYaw + (entity.rotationYaw - entity.prevRotationYaw) * partialTicks + 180.0f, 0.0f, -1.0f, 0.0f);
                GlStateManager.rotate(entity.prevRotationPitch + (entity.rotationPitch - entity.prevRotationPitch) * partialTicks, -1.0f, 0.0f, 0.0f);
            }
        } else if (this.mc.gameSettings.thirdPersonView > 0) {
            float f2 = this.d3 = Celestial.instance.featureManager.getFeatureByClass(PersonViewer.class).getState() ? (float)Interpolator.LINEAR.interpolate((double)this.d3, (double)PersonViewer.fovModifier.getCurrentValue(), 5.0 / (double)Minecraft.getDebugFPS()) : (float)Interpolator.LINEAR.interpolate((double)this.d3, 4.0, 5.0 / (double)Minecraft.getDebugFPS());
            if (Double.isNaN(this.d3)) {
                this.d3 = 0.01f;
            }
            this.d3 = (float)MathHelper.clamp((double)this.d3, 0.01, Celestial.instance.featureManager.getFeatureByClass(PersonViewer.class).getState() ? (double)PersonViewer.fovModifier.getCurrentValue() : 4.0);
            if (this.mc.gameSettings.debugCamEnable) {
                GlStateManager.translate(0.0f, 0.0f, -this.d3);
            } else {
                float f22;
                float f1;
                if (Celestial.instance.featureManager.getFeatureByClass(PersonViewer.class).getState()) {
                    f1 = entity.rotationYaw + PersonViewer.viewerYaw.getCurrentValue();
                    f22 = entity.rotationPitch + PersonViewer.viewerPitch.getCurrentValue();
                } else {
                    f1 = entity.rotationYaw;
                    f22 = entity.rotationPitch;
                }
                if (this.mc.gameSettings.thirdPersonView == 2) {
                    f22 += 180.0f;
                }
                double d4 = (double)(-MathHelper.sin(f1 * ((float)Math.PI / 180)) * MathHelper.cos(f22 * ((float)Math.PI / 180))) * (double)this.d3;
                double d5 = (double)(MathHelper.cos(f1 * ((float)Math.PI / 180)) * MathHelper.cos(f22 * ((float)Math.PI / 180))) * (double)this.d3;
                double d6 = (double)(-MathHelper.sin(f22 * ((float)Math.PI / 180))) * (double)this.d3;
                for (int i = 0; i < 8; ++i) {
                    double d7;
                    RayTraceResult raytraceresult;
                    float f3 = (i & 1) * 2 - 1;
                    float f4 = (i >> 1 & 1) * 2 - 1;
                    float f5 = (i >> 2 & 1) * 2 - 1;
                    if (Celestial.instance.featureManager.getFeatureByClass(NoRender.class).getState() && NoRender.cameraBounds.getCurrentValue() || (raytraceresult = this.mc.world.rayTraceBlocks(new Vec3d(d0 + (double)(f3 *= 0.1f), d1 + (double)(f4 *= 0.1f), d2 + (double)(f5 *= 0.1f)), new Vec3d(d0 - d4 + (double)f3 + (double)f5, d1 - d6 + (double)f4, d2 - d5 + (double)f5))) == null || !((d7 = raytraceresult.hitVec.distanceTo(new Vec3d(d0, d1, d2))) < (double)this.d3)) continue;
                    this.d3 = (float)d7;
                }
                if (this.mc.gameSettings.thirdPersonView == 2) {
                    GlStateManager.rotate(180.0f, 0.0f, 1.0f, 0.0f);
                }
                GlStateManager.rotate(entity.rotationPitch - f22, 1.0f, 0.0f, 0.0f);
                GlStateManager.rotate(entity.rotationYaw - f1, 0.0f, 1.0f, 0.0f);
                GlStateManager.translate(0.0f, 0.0f, -this.d3);
                GlStateManager.rotate(f1 - entity.rotationYaw, 0.0f, 1.0f, 0.0f);
                GlStateManager.rotate(f22 - entity.rotationPitch, 1.0f, 0.0f, 0.0f);
            }
        } else {
            GlStateManager.translate(0.0f, 0.0f, 0.05f);
            this.d3 = 0.01f;
        }
        if (Reflector.EntityViewRenderEvent_CameraSetup_Constructor.exists()) {
            if (!this.mc.gameSettings.debugCamEnable) {
                float f6 = entity.prevRotationYaw + (entity.rotationYaw - entity.prevRotationYaw) * partialTicks + 180.0f;
                float f7 = entity.prevRotationPitch + (entity.rotationPitch - entity.prevRotationPitch) * partialTicks;
                float f8 = 0.0f;
                if (entity instanceof EntityAnimal) {
                    EntityAnimal entityanimal1 = (EntityAnimal)entity;
                    f6 = entityanimal1.prevRotationYawHead + (entityanimal1.rotationYawHead - entityanimal1.prevRotationYawHead) * partialTicks + 180.0f;
                }
                IBlockState iblockstate1 = ActiveRenderInfo.getBlockStateAtEntityViewpoint(this.mc.world, entity, partialTicks);
                Object object = Reflector.newInstance(Reflector.EntityViewRenderEvent_CameraSetup_Constructor, this, entity, iblockstate1, Float.valueOf(partialTicks), Float.valueOf(f6), Float.valueOf(f7), Float.valueOf(f8));
                Reflector.postForgeBusEvent(object);
                f8 = Reflector.callFloat(object, Reflector.EntityViewRenderEvent_CameraSetup_getRoll, new Object[0]);
                f7 = Reflector.callFloat(object, Reflector.EntityViewRenderEvent_CameraSetup_getPitch, new Object[0]);
                f6 = Reflector.callFloat(object, Reflector.EntityViewRenderEvent_CameraSetup_getYaw, new Object[0]);
                GlStateManager.rotate(f8, 0.0f, 0.0f, 1.0f);
                GlStateManager.rotate(f7, 1.0f, 0.0f, 0.0f);
                GlStateManager.rotate(f6, 0.0f, 1.0f, 0.0f);
            }
        } else if (!this.mc.gameSettings.debugCamEnable) {
            GlStateManager.rotate(entity.prevRotationPitch + (entity.rotationPitch - entity.prevRotationPitch) * partialTicks, 1.0f, 0.0f, 0.0f);
            if (entity instanceof EntityAnimal) {
                EntityAnimal entityanimal = (EntityAnimal)entity;
                GlStateManager.rotate(entityanimal.prevRotationYawHead + (entityanimal.rotationYawHead - entityanimal.prevRotationYawHead) * partialTicks + 180.0f, 0.0f, 1.0f, 0.0f);
            } else {
                GlStateManager.rotate(entity.prevRotationYaw + (entity.rotationYaw - entity.prevRotationYaw) * partialTicks + 180.0f, 0.0f, 1.0f, 0.0f);
            }
        }
        GlStateManager.translate(0.0f, -f, 0.0f);
        d0 = entity.prevPosX + (entity.posX - entity.prevPosX) * (double)partialTicks;
        d1 = entity.prevPosY + (entity.posY - entity.prevPosY) * (double)partialTicks + (double)f;
        d2 = entity.prevPosZ + (entity.posZ - entity.prevPosZ) * (double)partialTicks;
        this.cloudFog = this.mc.renderGlobal.hasCloudFog(d0, d1, d2, partialTicks);
    }

    public void setupCameraTransform(float partialTicks, int pass) {
        float f1;
        this.farPlaneDistance = this.mc.gameSettings.renderDistanceChunks * 16;
        if (Config.isFogFancy()) {
            this.farPlaneDistance *= 0.95f;
        }
        if (Config.isFogFast()) {
            this.farPlaneDistance *= 0.83f;
        }
        GlStateManager.matrixMode(5889);
        GlStateManager.loadIdentity();
        if (this.mc.gameSettings.anaglyph) {
            GlStateManager.translate((float)(-(pass * 2 - 1)) * 0.07f, 0.0f, 0.0f);
        }
        this.clipDistance = this.farPlaneDistance * 2.0f;
        if (this.clipDistance < 173.0f) {
            this.clipDistance = 173.0f;
        }
        if (this.cameraZoom != 1.0) {
            GlStateManager.translate((float)this.cameraYaw, (float)(-this.cameraPitch), 0.0f);
            GlStateManager.scale(this.cameraZoom, this.cameraZoom, 1.0);
        }
        Project.gluPerspective(this.getFOVModifier(partialTicks, true), (float)this.mc.displayWidth / (float)this.mc.displayHeight, 0.05f, this.clipDistance);
        GlStateManager.matrixMode(5888);
        GlStateManager.loadIdentity();
        if (this.mc.gameSettings.anaglyph) {
            GlStateManager.translate((float)(pass * 2 - 1) * 0.1f, 0.0f, 0.0f);
        }
        this.hurtCameraEffect(partialTicks);
        if (this.mc.gameSettings.viewBobbing) {
            this.setupViewBobbing(partialTicks);
        }
        if ((f1 = this.mc.player.prevTimeInPortal + (this.mc.player.timeInPortal - this.mc.player.prevTimeInPortal) * partialTicks) > 0.0f) {
            int i = 20;
            if (this.mc.player.isPotionActive(MobEffects.NAUSEA)) {
                i = 7;
            }
            float f2 = 5.0f / (f1 * f1 + 5.0f) - f1 * 0.04f;
            f2 *= f2;
            GlStateManager.rotate(((float)this.rendererUpdateCount + partialTicks) * (float)i, 0.0f, 1.0f, 1.0f);
            GlStateManager.scale(1.0f / f2, 1.0f, 1.0f);
            GlStateManager.rotate(-((float)this.rendererUpdateCount + partialTicks) * (float)i, 0.0f, 1.0f, 1.0f);
        }
        this.orientCamera(partialTicks);
        if (this.debugView) {
            switch (this.debugViewDirection) {
                case 0: {
                    GlStateManager.rotate(90.0f, 0.0f, 1.0f, 0.0f);
                    break;
                }
                case 1: {
                    GlStateManager.rotate(180.0f, 0.0f, 1.0f, 0.0f);
                    break;
                }
                case 2: {
                    GlStateManager.rotate(-90.0f, 0.0f, 1.0f, 0.0f);
                    break;
                }
                case 3: {
                    GlStateManager.rotate(90.0f, 1.0f, 0.0f, 0.0f);
                    break;
                }
                case 4: {
                    GlStateManager.rotate(-90.0f, 1.0f, 0.0f, 0.0f);
                }
            }
        }
    }

    private void renderHand(float partialTicks, int pass) {
        this.renderHand(partialTicks, pass, true, true, false);
    }

    public void renderHand(float p_renderHand_1_, int p_renderHand_2_, boolean p_renderHand_3_, boolean p_renderHand_4_, boolean p_renderHand_5_) {
        if (!this.debugView) {
            GlStateManager.matrixMode(5889);
            GlStateManager.loadIdentity();
            float f = 0.07f;
            if (this.mc.gameSettings.anaglyph) {
                GlStateManager.translate((float)(-(p_renderHand_2_ * 2 - 1)) * 0.07f, 0.0f, 0.0f);
            }
            if (Config.isShaders()) {
                Shaders.applyHandDepth();
            }
            Project.gluPerspective(this.getFOVModifier(p_renderHand_1_, false), (float)this.mc.displayWidth / (float)this.mc.displayHeight, 0.05f, this.farPlaneDistance * 2.0f);
            GlStateManager.matrixMode(5888);
            GlStateManager.loadIdentity();
            if (this.mc.gameSettings.anaglyph) {
                GlStateManager.translate((float)(p_renderHand_2_ * 2 - 1) * 0.1f, 0.0f, 0.0f);
            }
            boolean flag = false;
            if (p_renderHand_3_) {
                boolean flag1;
                GlStateManager.pushMatrix();
                this.hurtCameraEffect(p_renderHand_1_);
                if (this.mc.gameSettings.viewBobbing) {
                    this.setupViewBobbing(p_renderHand_1_);
                }
                flag = this.mc.getRenderViewEntity() instanceof EntityLivingBase && ((EntityLivingBase)this.mc.getRenderViewEntity()).isPlayerSleeping();
                boolean bl = flag1 = !ReflectorForge.renderFirstPersonHand(this.mc.renderGlobal, p_renderHand_1_, p_renderHand_2_);
                if (flag1 && this.mc.gameSettings.thirdPersonView == 0 && !flag && !this.mc.gameSettings.hideGUI && !this.mc.playerController.isSpectator()) {
                    this.enableLightmap();
                    if (Config.isShaders()) {
                        ShadersRender.renderItemFP(this.itemRenderer, p_renderHand_1_, p_renderHand_5_);
                    } else {
                        this.itemRenderer.renderItemInFirstPerson(p_renderHand_1_);
                    }
                    this.disableLightmap();
                }
                GlStateManager.popMatrix();
            }
            if (!p_renderHand_4_) {
                return;
            }
            this.disableLightmap();
            if (this.mc.gameSettings.thirdPersonView == 0 && !flag) {
                this.itemRenderer.renderOverlays(p_renderHand_1_);
                this.hurtCameraEffect(p_renderHand_1_);
            }
            if (this.mc.gameSettings.viewBobbing) {
                this.setupViewBobbing(p_renderHand_1_);
            }
        }
    }

    public void disableLightmap() {
        GlStateManager.setActiveTexture(OpenGlHelper.lightmapTexUnit);
        GlStateManager.disableTexture2D();
        GlStateManager.setActiveTexture(OpenGlHelper.defaultTexUnit);
        if (Config.isShaders()) {
            Shaders.disableLightmap();
        }
    }

    public void enableLightmap() {
        GlStateManager.setActiveTexture(OpenGlHelper.lightmapTexUnit);
        GlStateManager.matrixMode(5890);
        GlStateManager.loadIdentity();
        float f = 0.00390625f;
        GlStateManager.scale(0.00390625f, 0.00390625f, 0.00390625f);
        GlStateManager.translate(8.0f, 8.0f, 8.0f);
        GlStateManager.matrixMode(5888);
        this.mc.getTextureManager().bindTexture(this.locationLightMap);
        GlStateManager.glTexParameteri(3553, 10241, 9729);
        GlStateManager.glTexParameteri(3553, 10240, 9729);
        GlStateManager.glTexParameteri(3553, 10242, 10496);
        GlStateManager.glTexParameteri(3553, 10243, 10496);
        GlStateManager.color(1.0f, 1.0f, 1.0f, 1.0f);
        GlStateManager.enableTexture2D();
        GlStateManager.setActiveTexture(OpenGlHelper.defaultTexUnit);
        if (Config.isShaders()) {
            Shaders.enableLightmap();
        }
    }

    private void updateTorchFlicker() {
        this.torchFlickerDX = (float)((double)this.torchFlickerDX + (Math.random() - Math.random()) * Math.random() * Math.random());
        this.torchFlickerDX = (float)((double)this.torchFlickerDX * 0.9);
        this.torchFlickerX += this.torchFlickerDX - this.torchFlickerX;
        this.lightmapUpdateNeeded = true;
    }

    private void updateLightmap(float partialTicks) {
        if (this.lightmapUpdateNeeded) {
            this.mc.mcProfiler.startSection("lightTex");
            WorldClient world = this.mc.world;
            if (world != null) {
                if (Config.isCustomColors() && CustomColors.updateLightmap(world, this.torchFlickerX, this.lightmapColors, this.mc.player.isPotionActive(MobEffects.NIGHT_VISION))) {
                    this.lightmapTexture.updateDynamicTexture();
                    this.lightmapUpdateNeeded = false;
                    this.mc.mcProfiler.endSection();
                    return;
                }
                float f = world.getSunBrightness(1.0f);
                float f1 = f * 0.95f + 0.05f - (Celestial.instance.featureManager.getFeatureByClass(NightMode.class).getState() ? NightMode.darkModifier.getCurrentValue() : 0.0f);
                if (Celestial.instance.featureManager.getFeatureByClass(WorldColor.class).getState()) {
                    int oneColor = WorldColor.lightMapColor.getColor();
                    int color = 0;
                    switch (WorldColor.worldColorMode.currentMode) {
                        case "Client": {
                            color = ClientHelper.getClientColor().getRGB();
                            break;
                        }
                        case "Custom": {
                            color = oneColor;
                            break;
                        }
                        case "Astolfo": {
                            color = PaletteHelper.astolfo(5000.0f, 1).getRGB();
                            break;
                        }
                        case "Rainbow": {
                            color = PaletteHelper.rainbow(300, 1.0f, 1.0f).getRGB();
                        }
                    }
                    int color2 = color;
                    int r = color2 >> 16 & 0xFF;
                    int g = color2 >> 8 & 0xFF;
                    int b = color2 & 0xFF;
                    int a = color2 >> 24 & 0xFF;
                    for (int i = 0; i < 256; ++i) {
                        this.lightmapColors[i] = a << 24 | r << 16 | g << 8 | b;
                    }
                } else {
                    for (int i = 0; i < 256; ++i) {
                        float f2 = world.provider.getLightBrightnessTable()[i / 16] * f1;
                        float f3 = world.provider.getLightBrightnessTable()[i % 16] * (this.torchFlickerX * 0.1f + 1.5f);
                        if (world.getLastLightningBolt() > 0) {
                            f2 = world.provider.getLightBrightnessTable()[i / 16];
                        }
                        float f4 = f2 * (f * 0.65f + 0.35f);
                        float f5 = f2 * (f * 0.65f + 0.35f);
                        float f6 = f3 * ((f3 * 0.6f + 0.4f) * 0.6f + 0.4f);
                        float f7 = f3 * (f3 * f3 * 0.6f + 0.4f);
                        float f8 = f4 + f3;
                        float f9 = f5 + f6;
                        float f10 = f2 + f7;
                        f8 = f8 * 0.96f + 0.03f;
                        f9 = f9 * 0.96f + 0.03f;
                        f10 = f10 * 0.96f + 0.03f;
                        if (this.bossColorModifier > 0.0f) {
                            float f11 = this.bossColorModifierPrev + (this.bossColorModifier - this.bossColorModifierPrev) * partialTicks;
                            f8 = f8 * (1.0f - f11) + f8 * 0.7f * f11;
                            f9 = f9 * (1.0f - f11) + f9 * 0.6f * f11;
                            f10 = f10 * (1.0f - f11) + f10 * 0.6f * f11;
                        }
                        if (world.provider.getDimensionType().getId() == 1) {
                            f8 = 0.22f + f3 * 0.75f;
                            f9 = 0.28f + f6 * 0.75f;
                            f10 = 0.25f + f7 * 0.75f;
                        }
                        if (Reflector.ForgeWorldProvider_getLightmapColors.exists()) {
                            float[] afloat = new float[]{f8, f9, f10};
                            Reflector.call(world.provider, Reflector.ForgeWorldProvider_getLightmapColors, Float.valueOf(partialTicks), Float.valueOf(f), Float.valueOf(f2), Float.valueOf(f3), afloat);
                            f8 = afloat[0];
                            f9 = afloat[1];
                            f10 = afloat[2];
                        }
                        f8 = MathHelper.clamp(f8, 0.0f, 1.0f);
                        f9 = MathHelper.clamp(f9, 0.0f, 1.0f);
                        f10 = MathHelper.clamp(f10, 0.0f, 1.0f);
                        if (this.mc.player.isPotionActive(MobEffects.NIGHT_VISION)) {
                            float f15 = this.getNightVisionBrightness(this.mc.player, partialTicks);
                            float f12 = 1.0f / f8;
                            if (f12 > 1.0f / f9) {
                                f12 = 1.0f / f9;
                            }
                            if (f12 > 1.0f / f10) {
                                f12 = 1.0f / f10;
                            }
                            f8 = f8 * (1.0f - f15) + f8 * f12 * f15;
                            f9 = f9 * (1.0f - f15) + f9 * f12 * f15;
                            f10 = f10 * (1.0f - f15) + f10 * f12 * f15;
                        }
                        if (f8 > 1.0f) {
                            f8 = 1.0f;
                        }
                        if (f9 > 1.0f) {
                            f9 = 1.0f;
                        }
                        if (f10 > 1.0f) {
                            f10 = 1.0f;
                        }
                        float f16 = this.mc.gameSettings.gammaSetting;
                        float f17 = 1.0f - f8;
                        float f13 = 1.0f - f9;
                        float f14 = 1.0f - f10;
                        f17 = 1.0f - f17 * f17 * f17 * f17;
                        f13 = 1.0f - f13 * f13 * f13 * f13;
                        f14 = 1.0f - f14 * f14 * f14 * f14;
                        f8 = f8 * (1.0f - f16) + f17 * f16;
                        f9 = f9 * (1.0f - f16) + f13 * f16;
                        f10 = f10 * (1.0f - f16) + f14 * f16;
                        f8 = f8 * 0.96f + 0.03f;
                        f9 = f9 * 0.96f + 0.03f;
                        f10 = f10 * 0.96f + 0.03f;
                        if (f8 > 1.0f) {
                            f8 = 1.0f;
                        }
                        if (f9 > 1.0f) {
                            f9 = 1.0f;
                        }
                        if (f10 > 1.0f) {
                            f10 = 1.0f;
                        }
                        if (f8 < 0.0f) {
                            f8 = 0.0f;
                        }
                        if (f9 < 0.0f) {
                            f9 = 0.0f;
                        }
                        if (f10 < 0.0f) {
                            f10 = 0.0f;
                        }
                        int j = 255;
                        int k = (int)(f8 * 255.0f);
                        int l = (int)(f9 * 255.0f);
                        int i1 = (int)(f10 * 255.0f);
                        this.lightmapColors[i] = 0xFF000000 | k << 16 | l << 8 | i1;
                    }
                }
                this.lightmapTexture.updateDynamicTexture();
                this.lightmapUpdateNeeded = false;
                this.mc.mcProfiler.endSection();
            }
        }
    }

    public float getNightVisionBrightness(EntityLivingBase entitylivingbaseIn, float partialTicks) {
        int i = entitylivingbaseIn.getActivePotionEffect(MobEffects.NIGHT_VISION).getDuration();
        return i > 200 ? 1.0f : 0.7f + MathHelper.sin(((float)i - partialTicks) * (float)Math.PI * 0.2f) * 0.3f;
    }

    public void updateCameraAndRender(float partialTicks, long nanoTime) {
        this.frameInit();
        boolean flag = Display.isActive();
        if (!(flag || !this.mc.gameSettings.pauseOnLostFocus || this.mc.gameSettings.touchscreen && Mouse.isButtonDown(1))) {
            if (Minecraft.getSystemTime() - this.prevFrameTime > 500L) {
                this.mc.displayInGameMenu();
            }
        } else {
            this.prevFrameTime = Minecraft.getSystemTime();
        }
        this.mc.mcProfiler.startSection("mouse");
        if (flag && Minecraft.IS_RUNNING_ON_MAC && this.mc.inGameHasFocus && !Mouse.isInsideWindow()) {
            Mouse.setGrabbed(false);
            Mouse.setCursorPosition(Display.getWidth() / 2, Display.getHeight() / 2 - 20);
            Mouse.setGrabbed(true);
        }
        if (this.mc.inGameHasFocus && flag) {
            this.mc.mouseHelper.mouseXYChange();
            this.mc.func_193032_ao().func_193299_a(this.mc.mouseHelper);
            float f = this.mc.gameSettings.mouseSensitivity * 0.6f + 0.2f;
            float f1 = f * f * f * 8.0f;
            float f2 = (float)this.mc.mouseHelper.deltaX * f1;
            float f3 = (float)this.mc.mouseHelper.deltaY * f1;
            int i = 1;
            if (this.mc.gameSettings.invertMouse) {
                i = -1;
            }
            if (this.mc.gameSettings.smoothCamera) {
                this.smoothCamYaw += f2;
                this.smoothCamPitch += f3;
                float f4 = partialTicks - this.smoothCamPartialTicks;
                this.smoothCamPartialTicks = partialTicks;
                f2 = this.smoothCamFilterX * f4;
                f3 = this.smoothCamFilterY * f4;
                this.mc.player.setAngles(f2, f3 * (float)i);
            } else {
                this.smoothCamYaw = 0.0f;
                this.smoothCamPitch = 0.0f;
                this.mc.player.setAngles(f2, f3 * (float)i);
            }
        }
        this.mc.mcProfiler.endSection();
        if (!this.mc.skipRenderWorld) {
            anaglyphEnable = this.mc.gameSettings.anaglyph;
            final ScaledResolution scaledresolution = new ScaledResolution(this.mc);
            int i1 = scaledresolution.getScaledWidth();
            int j1 = scaledresolution.getScaledHeight();
            final int k1 = Mouse.getX() * i1 / this.mc.displayWidth;
            final int l1 = j1 - Mouse.getY() * j1 / this.mc.displayHeight - 1;
            int i2 = this.mc.gameSettings.limitFramerate;
            if (this.mc.world == null) {
                GlStateManager.viewport(0, 0, this.mc.displayWidth, this.mc.displayHeight);
                GlStateManager.matrixMode(5889);
                GlStateManager.loadIdentity();
                GlStateManager.matrixMode(5888);
                GlStateManager.loadIdentity();
                this.setupOverlayRendering();
                this.renderEndNanoTime = System.nanoTime();
                TileEntityRendererDispatcher.instance.renderEngine = this.mc.getTextureManager();
                TileEntityRendererDispatcher.instance.fontRenderer = this.mc.fontRendererObj;
            } else {
                this.mc.mcProfiler.startSection("level");
                int j = Math.min(Minecraft.getDebugFPS(), i2);
                j = Math.max(j, 60);
                long k = System.nanoTime() - nanoTime;
                long l = Math.max((long)(1000000000 / j / 4) - k, 0L);
                this.renderWorld(partialTicks, System.nanoTime() + l);
                if (this.mc.isSingleplayer() && this.timeWorldIcon < Minecraft.getSystemTime() - 1000L) {
                    this.timeWorldIcon = Minecraft.getSystemTime();
                    if (!this.mc.getIntegratedServer().isWorldIconSet()) {
                        this.createWorldIcon();
                    }
                }
                if (OpenGlHelper.shadersSupported) {
                    this.mc.renderGlobal.renderEntityOutlineFramebuffer();
                    if (this.theShaderGroup != null && this.useShader) {
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
                if (!this.mc.gameSettings.hideGUI || this.mc.currentScreen != null) {
                    GlStateManager.alphaFunc(516, 0.1f);
                    this.setupOverlayRendering();
                    this.func_190563_a(i1, j1, partialTicks);
                    if (this.mc.currentScreen instanceof ClickGuiScreen || this.mc.currentScreen instanceof GuiConfig) {
                        ClickGuiScreen.callback();
                    }
                    this.mc.ingameGUI.renderGameOverlay(partialTicks);
                    if (this.mc.gameSettings.ofShowFps && !this.mc.gameSettings.showDebugInfo) {
                        Config.drawFps();
                    }
                    if (this.mc.gameSettings.showDebugInfo) {
                        Lagometer.showLagometer(scaledresolution);
                    }
                }
                this.mc.mcProfiler.endSection();
            }
            if (this.mc.currentScreen != null) {
                GlStateManager.clear(256);
                try {
                    if (Reflector.ForgeHooksClient_drawScreen.exists()) {
                        Reflector.callVoid(Reflector.ForgeHooksClient_drawScreen, this.mc.currentScreen, k1, l1, Float.valueOf(this.mc.func_193989_ak()));
                    } else {
                        this.mc.currentScreen.drawScreen(k1, l1, this.mc.func_193989_ak());
                    }
                }
                catch (Throwable throwable1) {
                    CrashReport crashreport = CrashReport.makeCrashReport(throwable1, "Rendering screen");
                    CrashReportCategory crashreportcategory = crashreport.makeCategory("Screen render details");
                    crashreportcategory.setDetail("Screen name", new ICrashReportDetail<String>(){

                        @Override
                        public String call() throws Exception {
                            return ((EntityRenderer)EntityRenderer.this).mc.currentScreen.getClass().getCanonicalName();
                        }
                    });
                    crashreportcategory.setDetail("Mouse location", new ICrashReportDetail<String>(){

                        @Override
                        public String call() throws Exception {
                            return String.format("Scaled: (%d, %d). Absolute: (%d, %d)", k1, l1, Mouse.getX(), Mouse.getY());
                        }
                    });
                    crashreportcategory.setDetail("Screen size", new ICrashReportDetail<String>(){

                        @Override
                        public String call() throws Exception {
                            return String.format("Scaled: (%d, %d). Absolute: (%d, %d). Scale factor of %d", scaledresolution.getScaledWidth(), scaledresolution.getScaledHeight(), ((EntityRenderer)EntityRenderer.this).mc.displayWidth, ((EntityRenderer)EntityRenderer.this).mc.displayHeight, scaledresolution.getScaleFactor());
                        }
                    });
                    throw new ReportedException(crashreport);
                }
            }
            if (!(this.mc.currentScreen instanceof Gui)) {
                ClickGuiScreen.callback();
            }
        }
        this.frameFinish();
        this.waitForServerThread();
        Lagometer.updateLagometer();
        if (this.mc.gameSettings.ofProfiler) {
            this.mc.gameSettings.showDebugProfilerChart = true;
        }
    }

    private void createWorldIcon() {
        if (this.mc.renderGlobal.getRenderedChunks() > 10 && this.mc.renderGlobal.hasNoChunkUpdates() && !this.mc.getIntegratedServer().isWorldIconSet()) {
            BufferedImage bufferedimage = ScreenShotHelper.createScreenshot(this.mc.displayWidth, this.mc.displayHeight, this.mc.getFramebuffer());
            int i = bufferedimage.getWidth();
            int j = bufferedimage.getHeight();
            int k = 0;
            int l = 0;
            if (i > j) {
                k = (i - j) / 2;
                i = j;
            } else {
                l = (j - i) / 2;
            }
            try {
                BufferedImage bufferedimage1 = new BufferedImage(64, 64, 1);
                Graphics2D graphics = bufferedimage1.createGraphics();
                graphics.drawImage(bufferedimage, 0, 0, 64, 64, k, l, k + i, l + i, null);
                graphics.dispose();
                ImageIO.write((RenderedImage)bufferedimage1, "png", this.mc.getIntegratedServer().getWorldIconFile());
            }
            catch (IOException ioexception1) {
                LOGGER.warn("Couldn't save auto screenshot", (Throwable)ioexception1);
            }
        }
    }

    public void renderStreamIndicator(float partialTicks) {
        this.setupOverlayRendering();
    }

    private boolean isDrawBlockOutline() {
        boolean flag;
        if (!this.drawBlockOutline) {
            return false;
        }
        Entity entity = this.mc.getRenderViewEntity();
        boolean bl = flag = entity instanceof EntityPlayer && !this.mc.gameSettings.hideGUI;
        if (flag && !((EntityPlayer)entity).capabilities.allowEdit) {
            ItemStack itemstack = ((EntityPlayer)entity).getHeldItemMainhand();
            if (this.mc.objectMouseOver != null && this.mc.objectMouseOver.typeOfHit == RayTraceResult.Type.BLOCK) {
                BlockPos blockpos = this.mc.objectMouseOver.getBlockPos();
                IBlockState iblockstate = this.mc.world.getBlockState(blockpos);
                Block block = iblockstate.getBlock();
                flag = this.mc.playerController.getCurrentGameType() == GameType.SPECTATOR ? ReflectorForge.blockHasTileEntity(iblockstate) && this.mc.world.getTileEntity(blockpos) instanceof IInventory : !itemstack.isEmpty() && (itemstack.canDestroy(block) || itemstack.canPlaceOn(block));
            }
        }
        return flag;
    }

    public void renderWorld(float partialTicks, long finishTimeNano) {
        this.updateLightmap(partialTicks);
        if (this.mc.getRenderViewEntity() == null) {
            this.mc.setRenderViewEntity(this.mc.player);
        }
        this.getMouseOver(partialTicks);
        if (Config.isShaders()) {
            Shaders.beginRender(this.mc, partialTicks, finishTimeNano);
        }
        GlStateManager.enableDepth();
        GlStateManager.enableAlpha();
        GlStateManager.alphaFunc(516, 0.1f);
        this.mc.mcProfiler.startSection("center");
        if (this.mc.gameSettings.anaglyph) {
            anaglyphField = 0;
            GlStateManager.colorMask(false, true, true, false);
            this.renderWorldPass(0, partialTicks, finishTimeNano);
            anaglyphField = 1;
            GlStateManager.colorMask(true, false, false, false);
            this.renderWorldPass(1, partialTicks, finishTimeNano);
            GlStateManager.colorMask(true, true, true, false);
        } else {
            this.renderWorldPass(2, partialTicks, finishTimeNano);
        }
        this.mc.mcProfiler.endSection();
    }

    private void renderWorldPass(int pass, float partialTicks, long finishTimeNano) {
        double d2;
        double d1;
        double d0;
        Entity entity;
        Frustum icamera;
        ParticleManager particlemanager;
        RenderGlobal renderglobal;
        boolean flag;
        block45: {
            block47: {
                EntityPlayer entityplayer;
                block46: {
                    flag = Config.isShaders();
                    if (flag) {
                        Shaders.beginRenderPass(pass, partialTicks, finishTimeNano);
                    }
                    renderglobal = this.mc.renderGlobal;
                    particlemanager = this.mc.effectRenderer;
                    boolean flag1 = this.isDrawBlockOutline();
                    GlStateManager.enableCull();
                    this.mc.mcProfiler.endStartSection("clear");
                    if (flag) {
                        Shaders.setViewport(0, 0, this.mc.displayWidth, this.mc.displayHeight);
                    } else {
                        GlStateManager.viewport(0, 0, this.mc.displayWidth, this.mc.displayHeight);
                    }
                    this.updateFogColor(partialTicks);
                    GlStateManager.clear(16640);
                    if (flag) {
                        Shaders.clearRenderBuffer();
                    }
                    this.mc.mcProfiler.endStartSection("camera");
                    this.setupCameraTransform(partialTicks, pass);
                    if (flag) {
                        Shaders.setCamera(partialTicks);
                    }
                    ActiveRenderInfo.updateRenderInfo(this.mc.player, this.mc.gameSettings.thirdPersonView == 2);
                    this.mc.mcProfiler.endStartSection("frustum");
                    ClippingHelper clippinghelper = ClippingHelperImpl.getInstance();
                    this.mc.mcProfiler.endStartSection("culling");
                    icamera = new Frustum(clippinghelper);
                    entity = this.mc.getRenderViewEntity();
                    d0 = entity.lastTickPosX + (entity.posX - entity.lastTickPosX) * (double)partialTicks;
                    d1 = entity.lastTickPosY + (entity.posY - entity.lastTickPosY) * (double)partialTicks;
                    d2 = entity.lastTickPosZ + (entity.posZ - entity.lastTickPosZ) * (double)partialTicks;
                    if (flag) {
                        ShadersRender.setFrustrumPosition(icamera, d0, d1, d2);
                    } else {
                        icamera.setPosition(d0, d1, d2);
                    }
                    if ((Config.isSkyEnabled() || Config.isSunMoonEnabled() || Config.isStarsEnabled()) && !Shaders.isShadowPass) {
                        this.setupFog(-1, partialTicks);
                        this.mc.mcProfiler.endStartSection("sky");
                        GlStateManager.matrixMode(5889);
                        GlStateManager.loadIdentity();
                        Project.gluPerspective(this.getFOVModifier(partialTicks, true), (float)this.mc.displayWidth / (float)this.mc.displayHeight, 0.05f, this.clipDistance);
                        GlStateManager.matrixMode(5888);
                        if (flag) {
                            Shaders.beginSky();
                        }
                        renderglobal.renderSky(partialTicks, pass);
                        if (flag) {
                            Shaders.endSky();
                        }
                        GlStateManager.matrixMode(5889);
                        GlStateManager.loadIdentity();
                        Project.gluPerspective(this.getFOVModifier(partialTicks, true), (float)this.mc.displayWidth / (float)this.mc.displayHeight, 0.05f, this.clipDistance);
                        GlStateManager.matrixMode(5888);
                    } else {
                        GlStateManager.disableBlend();
                    }
                    this.setupFog(0, partialTicks);
                    GlStateManager.shadeModel(7425);
                    if (entity.posY + (double)entity.getEyeHeight() < 128.0 + (double)(this.mc.gameSettings.ofCloudsHeight * 128.0f)) {
                        this.renderCloudsCheck(renderglobal, partialTicks, pass, d0, d1, d2);
                    }
                    this.mc.mcProfiler.endStartSection("prepareterrain");
                    this.setupFog(0, partialTicks);
                    this.mc.getTextureManager().bindTexture(TextureMap.LOCATION_BLOCKS_TEXTURE);
                    RenderHelper.disableStandardItemLighting();
                    this.mc.mcProfiler.endStartSection("terrain_setup");
                    this.checkLoadVisibleChunks(entity, partialTicks, icamera, this.mc.player.isSpectator());
                    if (flag) {
                        ShadersRender.setupTerrain(renderglobal, entity, partialTicks, icamera, this.frameCount++, this.mc.player.isSpectator());
                    } else {
                        renderglobal.setupTerrain(entity, partialTicks, icamera, this.frameCount++, this.mc.player.isSpectator());
                    }
                    if (pass == 0 || pass == 2) {
                        this.mc.mcProfiler.endStartSection("updatechunks");
                        Lagometer.timerChunkUpload.start();
                        this.mc.renderGlobal.updateChunks(finishTimeNano);
                        Lagometer.timerChunkUpload.end();
                    }
                    this.mc.mcProfiler.endStartSection("terrain");
                    Lagometer.timerTerrain.start();
                    if (this.mc.gameSettings.ofSmoothFps && pass > 0) {
                        this.mc.mcProfiler.endStartSection("finish");
                        GL11.glFinish();
                        this.mc.mcProfiler.endStartSection("terrain");
                    }
                    GlStateManager.matrixMode(5888);
                    GlStateManager.pushMatrix();
                    GlStateManager.disableAlpha();
                    if (flag) {
                        ShadersRender.beginTerrainSolid();
                    }
                    renderglobal.renderBlockLayer(BlockRenderLayer.SOLID, partialTicks, pass, entity);
                    GlStateManager.enableAlpha();
                    if (flag) {
                        ShadersRender.beginTerrainCutoutMipped();
                    }
                    renderglobal.renderBlockLayer(BlockRenderLayer.CUTOUT_MIPPED, partialTicks, pass, entity);
                    this.mc.getTextureManager().getTexture(TextureMap.LOCATION_BLOCKS_TEXTURE).setBlurMipmap(false, false);
                    if (flag) {
                        ShadersRender.beginTerrainCutout();
                    }
                    renderglobal.renderBlockLayer(BlockRenderLayer.CUTOUT, partialTicks, pass, entity);
                    this.mc.getTextureManager().getTexture(TextureMap.LOCATION_BLOCKS_TEXTURE).restoreLastBlurMipmap();
                    if (flag) {
                        ShadersRender.endTerrain();
                    }
                    Lagometer.timerTerrain.end();
                    GlStateManager.shadeModel(7424);
                    GlStateManager.alphaFunc(516, 0.1f);
                    if (!this.debugView) {
                        GlStateManager.matrixMode(5888);
                        GlStateManager.popMatrix();
                        GlStateManager.pushMatrix();
                        RenderHelper.enableStandardItemLighting();
                        this.mc.mcProfiler.endStartSection("entities");
                        if (Reflector.ForgeHooksClient_setRenderPass.exists()) {
                            Reflector.callVoid(Reflector.ForgeHooksClient_setRenderPass, 0);
                        }
                        renderglobal.renderEntities(entity, icamera, partialTicks);
                        if (Reflector.ForgeHooksClient_setRenderPass.exists()) {
                            Reflector.callVoid(Reflector.ForgeHooksClient_setRenderPass, -1);
                        }
                        RenderHelper.disableStandardItemLighting();
                        this.disableLightmap();
                    }
                    GlStateManager.matrixMode(5888);
                    GlStateManager.popMatrix();
                    if (!flag1 || this.mc.objectMouseOver == null || entity.isInsideOfMaterial(Material.WATER)) break block45;
                    entityplayer = (EntityPlayer)entity;
                    GlStateManager.disableAlpha();
                    this.mc.mcProfiler.endStartSection("outline");
                    if (!Reflector.ForgeHooksClient_onDrawBlockHighlight.exists()) break block46;
                    if (Reflector.callBoolean(Reflector.ForgeHooksClient_onDrawBlockHighlight, renderglobal, entityplayer, this.mc.objectMouseOver, 0, Float.valueOf(partialTicks))) break block47;
                }
                renderglobal.drawSelectionBox(entityplayer, this.mc.objectMouseOver, 0, partialTicks);
            }
            GlStateManager.enableAlpha();
        }
        if (this.mc.debugRenderer.shouldRender()) {
            boolean flag2 = GlStateManager.isFogEnabled();
            GlStateManager.disableFog();
            this.mc.debugRenderer.renderDebug(partialTicks, finishTimeNano);
            GlStateManager.setFogEnabled(flag2);
        }
        if (!renderglobal.damagedBlocks.isEmpty()) {
            this.mc.mcProfiler.endStartSection("destroyProgress");
            GlStateManager.enableBlend();
            GlStateManager.tryBlendFuncSeparate(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE, GlStateManager.SourceFactor.ONE, GlStateManager.DestFactor.ZERO);
            this.mc.getTextureManager().getTexture(TextureMap.LOCATION_BLOCKS_TEXTURE).setBlurMipmap(false, false);
            renderglobal.drawBlockDamageTexture(Tessellator.getInstance(), Tessellator.getInstance().getBuffer(), entity, partialTicks);
            this.mc.getTextureManager().getTexture(TextureMap.LOCATION_BLOCKS_TEXTURE).restoreLastBlurMipmap();
            GlStateManager.disableBlend();
        }
        GlStateManager.tryBlendFuncSeparate(770, 771, 1, 0);
        GlStateManager.disableBlend();
        if (!this.debugView) {
            this.enableLightmap();
            this.mc.mcProfiler.endStartSection("litParticles");
            if (flag) {
                Shaders.beginLitParticles();
            }
            particlemanager.renderLitParticles(entity, partialTicks);
            RenderHelper.disableStandardItemLighting();
            this.setupFog(0, partialTicks);
            this.mc.mcProfiler.endStartSection("particles");
            if (flag) {
                Shaders.beginParticles();
            }
            particlemanager.renderParticles(entity, partialTicks);
            if (flag) {
                Shaders.endParticles();
            }
            this.disableLightmap();
        }
        GlStateManager.depthMask(false);
        GlStateManager.enableCull();
        this.mc.mcProfiler.endStartSection("weather");
        if (flag) {
            Shaders.beginWeather();
        }
        this.renderRainSnow(partialTicks);
        if (flag) {
            Shaders.endWeather();
        }
        GlStateManager.depthMask(true);
        renderglobal.renderWorldBorder(entity, partialTicks);
        if (flag) {
            ShadersRender.renderHand0(this, partialTicks, pass);
            Shaders.preWater();
        }
        GlStateManager.disableBlend();
        GlStateManager.enableCull();
        GlStateManager.tryBlendFuncSeparate(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA, GlStateManager.SourceFactor.ONE, GlStateManager.DestFactor.ZERO);
        GlStateManager.alphaFunc(516, 0.1f);
        this.setupFog(0, partialTicks);
        GlStateManager.enableBlend();
        GlStateManager.depthMask(false);
        this.mc.getTextureManager().bindTexture(TextureMap.LOCATION_BLOCKS_TEXTURE);
        GlStateManager.shadeModel(7425);
        this.mc.mcProfiler.endStartSection("translucent");
        if (flag) {
            Shaders.beginWater();
        }
        renderglobal.renderBlockLayer(BlockRenderLayer.TRANSLUCENT, partialTicks, pass, entity);
        if (flag) {
            Shaders.endWater();
        }
        if (Reflector.ForgeHooksClient_setRenderPass.exists() && !this.debugView) {
            RenderHelper.enableStandardItemLighting();
            this.mc.mcProfiler.endStartSection("entities");
            Reflector.callVoid(Reflector.ForgeHooksClient_setRenderPass, 1);
            this.mc.renderGlobal.renderEntities(entity, icamera, partialTicks);
            GlStateManager.tryBlendFuncSeparate(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA, GlStateManager.SourceFactor.ONE, GlStateManager.DestFactor.ZERO);
            Reflector.callVoid(Reflector.ForgeHooksClient_setRenderPass, -1);
            RenderHelper.disableStandardItemLighting();
        }
        GlStateManager.shadeModel(7424);
        GlStateManager.depthMask(true);
        GlStateManager.enableCull();
        GlStateManager.disableBlend();
        GlStateManager.disableFog();
        if (entity.posY + (double)entity.getEyeHeight() >= 128.0 + (double)(this.mc.gameSettings.ofCloudsHeight * 128.0f)) {
            this.mc.mcProfiler.endStartSection("aboveClouds");
            this.renderCloudsCheck(renderglobal, partialTicks, pass, d0, d1, d2);
        }
        if (Reflector.ForgeHooksClient_dispatchRenderLast.exists()) {
            this.mc.mcProfiler.endStartSection("forge_render_last");
            Reflector.callVoid(Reflector.ForgeHooksClient_dispatchRenderLast, renderglobal, Float.valueOf(partialTicks));
        }
        EventRender3D event = new EventRender3D(partialTicks);
        EventManager.call(event);
        EventManager.call(new EventPostRender3D());
        this.mc.mcProfiler.endStartSection("hand");
        if (this.renderHand && !Shaders.isShadowPass) {
            if (flag) {
                ShadersRender.renderHand1(this, partialTicks, pass);
                Shaders.renderCompositeFinal();
            }
            GlStateManager.clear(256);
            if (flag) {
                ShadersRender.renderFPOverlay(this, partialTicks, pass);
            } else {
                this.renderHand(partialTicks, pass);
            }
        }
        if (flag) {
            Shaders.endRender();
        }
    }

    private void renderCloudsCheck(RenderGlobal renderGlobalIn, float partialTicks, int pass, double p_180437_4_, double p_180437_6_, double p_180437_8_) {
        if (this.mc.gameSettings.renderDistanceChunks >= 4 && !Config.isCloudsOff() && Shaders.shouldRenderClouds(this.mc.gameSettings)) {
            this.mc.mcProfiler.endStartSection("clouds");
            GlStateManager.matrixMode(5889);
            GlStateManager.loadIdentity();
            Project.gluPerspective(this.getFOVModifier(partialTicks, true), (float)this.mc.displayWidth / (float)this.mc.displayHeight, 0.05f, this.clipDistance * 4.0f);
            GlStateManager.matrixMode(5888);
            GlStateManager.pushMatrix();
            this.setupFog(0, partialTicks);
            renderGlobalIn.renderClouds(partialTicks, pass, p_180437_4_, p_180437_6_, p_180437_8_);
            GlStateManager.disableFog();
            GlStateManager.popMatrix();
            GlStateManager.matrixMode(5889);
            GlStateManager.loadIdentity();
            Project.gluPerspective(this.getFOVModifier(partialTicks, true), (float)this.mc.displayWidth / (float)this.mc.displayHeight, 0.05f, this.clipDistance);
            GlStateManager.matrixMode(5888);
        }
    }

    private void addRainParticles() {
        float f = this.mc.world.getRainStrength(1.0f);
        if (!Config.isRainFancy()) {
            f /= 2.0f;
        }
        if (f != 0.0f && Config.isRainSplash()) {
            this.random.setSeed((long)this.rendererUpdateCount * 312987231L);
            Entity entity = this.mc.getRenderViewEntity();
            WorldClient world = this.mc.world;
            BlockPos blockpos = new BlockPos(entity);
            int i = 10;
            double d0 = 0.0;
            double d1 = 0.0;
            double d2 = 0.0;
            int j = 0;
            int k = (int)(100.0f * f * f);
            if (this.mc.gameSettings.particleSetting == 1) {
                k >>= 1;
            } else if (this.mc.gameSettings.particleSetting == 2) {
                k = 0;
            }
            for (int l = 0; l < k; ++l) {
                BlockPos blockpos1 = world.getPrecipitationHeight(blockpos.add(this.random.nextInt(10) - this.random.nextInt(10), 0, this.random.nextInt(10) - this.random.nextInt(10)));
                Biome biome = world.getBiome(blockpos1);
                BlockPos blockpos2 = blockpos1.down();
                IBlockState iblockstate = world.getBlockState(blockpos2);
                if (blockpos1.getY() > blockpos.getY() + 10 || blockpos1.getY() < blockpos.getY() - 10 || !biome.canRain() || !(biome.getFloatTemperature(blockpos1) >= 0.15f)) continue;
                double d3 = this.random.nextDouble();
                double d4 = this.random.nextDouble();
                AxisAlignedBB axisalignedbb = iblockstate.getBoundingBox(world, blockpos2);
                if (iblockstate.getMaterial() != Material.LAVA && iblockstate.getBlock() != Blocks.MAGMA) {
                    if (iblockstate.getMaterial() == Material.AIR) continue;
                    if (this.random.nextInt(++j) == 0) {
                        d0 = (double)blockpos2.getX() + d3;
                        d1 = (double)((float)blockpos2.getY() + 0.1f) + axisalignedbb.maxY - 1.0;
                        d2 = (double)blockpos2.getZ() + d4;
                    }
                    this.mc.world.spawnParticle(EnumParticleTypes.WATER_DROP, (double)blockpos2.getX() + d3, (double)((float)blockpos2.getY() + 0.1f) + axisalignedbb.maxY, (double)blockpos2.getZ() + d4, 0.0, 0.0, 0.0, new int[0]);
                    continue;
                }
                this.mc.world.spawnParticle(EnumParticleTypes.SMOKE_NORMAL, (double)blockpos1.getX() + d3, (double)((float)blockpos1.getY() + 0.1f) - axisalignedbb.minY, (double)blockpos1.getZ() + d4, 0.0, 0.0, 0.0, new int[0]);
            }
            if (j > 0 && this.random.nextInt(3) < this.rainSoundCounter++) {
                this.rainSoundCounter = 0;
                if (d1 > (double)(blockpos.getY() + 1) && world.getPrecipitationHeight(blockpos).getY() > MathHelper.floor(blockpos.getY())) {
                    this.mc.world.playSound(d0, d1, d2, SoundEvents.WEATHER_RAIN_ABOVE, SoundCategory.WEATHER, 0.1f, 0.5f, false);
                } else {
                    this.mc.world.playSound(d0, d1, d2, SoundEvents.WEATHER_RAIN, SoundCategory.WEATHER, 0.2f, 1.0f, false);
                }
            }
        }
    }

    protected void renderRainSnow(float partialTicks) {
        WorldProvider worldprovider;
        Object object;
        if (Reflector.ForgeWorldProvider_getWeatherRenderer.exists() && (object = Reflector.call(worldprovider = this.mc.world.provider, Reflector.ForgeWorldProvider_getWeatherRenderer, new Object[0])) != null) {
            Reflector.callVoid(object, Reflector.IRenderHandler_render, Float.valueOf(partialTicks), this.mc.world, this.mc);
            return;
        }
        if (Celestial.instance.featureManager.getFeatureByClass(Weather.class).getState()) {
            int hexColor = Weather.snowColor.getColor();
            float red = (float)(hexColor >> 16 & 0xFF) / 255.0f;
            float green = (float)(hexColor >> 8 & 0xFF) / 255.0f;
            float blue = (float)(hexColor & 0xFF) / 255.0f;
            float alpha = (float)(hexColor >> 24 & 0xFF) / 255.0f;
            float f = alpha * 10.0f;
            if (f > 0.0f) {
                this.enableLightmap();
                Entity entity = this.mc.getRenderViewEntity();
                WorldClient world = this.mc.world;
                int i = MathHelper.floor(entity.posX);
                int j = MathHelper.floor(entity.posY);
                int k = MathHelper.floor(entity.posZ);
                Tessellator tessellator = Tessellator.getInstance();
                BufferBuilder bufferbuilder = tessellator.getBuffer();
                GlStateManager.disableCull();
                GlStateManager.glNormal3f(0.0f, 1.0f, 0.0f);
                GlStateManager.enableBlend();
                GlStateManager.tryBlendFuncSeparate(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA, GlStateManager.SourceFactor.ONE, GlStateManager.DestFactor.ZERO);
                GlStateManager.alphaFunc(516, 0.1f);
                double d0 = entity.lastTickPosX + (entity.posX - entity.lastTickPosX) * (double)partialTicks;
                double d1 = entity.lastTickPosY + (entity.posY - entity.lastTickPosY) * (double)partialTicks;
                double d2 = entity.lastTickPosZ + (entity.posZ - entity.lastTickPosZ) * (double)partialTicks;
                int l = MathHelper.floor(d1);
                int i1 = 5;
                if (this.mc.gameSettings.fancyGraphics) {
                    i1 = 10;
                }
                int j1 = -1;
                float f1 = (float)this.rendererUpdateCount + partialTicks;
                bufferbuilder.setTranslation(-d0, -d1, -d2);
                GlStateManager.color(1.0f, 1.0f, 1.0f, 1.0f);
                BlockPos.MutableBlockPos blockpos$mutableblockpos = new BlockPos.MutableBlockPos();
                for (int k1 = k - i1; k1 <= k + i1; ++k1) {
                    for (int l1 = i - i1; l1 <= i + i1; ++l1) {
                        boolean check;
                        int i2 = (k1 - k + 16) * 32 + l1 - i + 16;
                        double d3 = (double)this.rainXCoords[i2] * 0.5;
                        double d4 = (double)this.rainYCoords[i2] * 0.5;
                        blockpos$mutableblockpos.setPos(l1, 0, k1);
                        Biome biome = world.getBiome(blockpos$mutableblockpos);
                        boolean bl = Celestial.instance.featureManager.getFeatureByClass(Weather.class).getState() ? true : (check = biome.canRain() || biome.getEnableSnow());
                        if (!check) continue;
                        int j2 = world.getPrecipitationHeight(blockpos$mutableblockpos).getY();
                        int k2 = j - i1;
                        int l2 = j + i1;
                        if (k2 < j2) {
                            k2 = j2;
                        }
                        if (l2 < j2) {
                            l2 = j2;
                        }
                        int i3 = j2;
                        if (j2 < l) {
                            i3 = l;
                        }
                        if (k2 == l2) continue;
                        this.random.setSeed(l1 * l1 * 3121 + l1 * 45238971 ^ k1 * k1 * 418711 + k1 * 13761);
                        blockpos$mutableblockpos.setPos(l1, k2, k1);
                        float f2 = biome.getFloatTemperature(blockpos$mutableblockpos);
                        if (j1 != 1) {
                            if (j1 >= 0) {
                                tessellator.draw();
                            }
                            j1 = 1;
                            this.mc.getTextureManager().bindTexture(SNOW_TEXTURES);
                            bufferbuilder.begin(7, DefaultVertexFormats.PARTICLE_POSITION_TEX_COLOR_LMAP);
                        }
                        double d8 = -((float)(this.rendererUpdateCount & 0x1FF) + partialTicks) / 512.0f;
                        double d9 = this.random.nextDouble() + (double)f1 * 0.01 * (double)((float)this.random.nextGaussian());
                        double d10 = this.random.nextDouble() + (double)(f1 * (float)this.random.nextGaussian()) * 0.001;
                        double d11 = (double)((float)l1 + 0.5f) - entity.posX;
                        double d12 = (double)((float)k1 + 0.5f) - entity.posZ;
                        float f6 = MathHelper.sqrt(d11 * d11 + d12 * d12) / (float)i1;
                        float f5 = ((1.0f - f6 * f6) * 0.3f + 0.5f) * f;
                        blockpos$mutableblockpos.setPos(l1, i3, k1);
                        int i4 = 0xF000F0;
                        int j4 = i4 >> 16 & 0xFFFF;
                        int k4 = i4 & 0xFFFF;
                        bufferbuilder.pos((double)l1 - d3 + 0.5, l2, (double)k1 - d4 + 0.5).tex(0.0 + d9, (double)k2 * 0.25 + d8 + d10).color(red, green, blue, f5).lightmap(j4, k4).endVertex();
                        bufferbuilder.pos((double)l1 + d3 + 0.5, l2, (double)k1 + d4 + 0.5).tex(1.0 + d9, (double)k2 * 0.25 + d8 + d10).color(red, green, blue, f5).lightmap(j4, k4).endVertex();
                        bufferbuilder.pos((double)l1 + d3 + 0.5, k2, (double)k1 + d4 + 0.5).tex(1.0 + d9, (double)l2 * 0.25 + d8 + d10).color(red, green, blue, f5).lightmap(j4, k4).endVertex();
                        bufferbuilder.pos((double)l1 - d3 + 0.5, k2, (double)k1 - d4 + 0.5).tex(0.0 + d9, (double)l2 * 0.25 + d8 + d10).color(red, green, blue, f5).lightmap(j4, k4).endVertex();
                    }
                }
                if (j1 >= 0) {
                    tessellator.draw();
                }
                bufferbuilder.setTranslation(0.0, 0.0, 0.0);
                GlStateManager.enableCull();
                GlStateManager.disableBlend();
                GlStateManager.alphaFunc(516, 0.1f);
                this.disableLightmap();
            }
        } else {
            float f5 = this.mc.world.getRainStrength(partialTicks);
            if (f5 > 0.0f) {
                if (Config.isRainOff()) {
                    return;
                }
                this.enableLightmap();
                Entity entity = this.mc.getRenderViewEntity();
                WorldClient world = this.mc.world;
                int i = MathHelper.floor(entity.posX);
                int j = MathHelper.floor(entity.posY);
                int k = MathHelper.floor(entity.posZ);
                Tessellator tessellator = Tessellator.getInstance();
                BufferBuilder bufferbuilder = tessellator.getBuffer();
                GlStateManager.disableCull();
                GlStateManager.glNormal3f(0.0f, 1.0f, 0.0f);
                GlStateManager.enableBlend();
                GlStateManager.tryBlendFuncSeparate(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA, GlStateManager.SourceFactor.ONE, GlStateManager.DestFactor.ZERO);
                GlStateManager.alphaFunc(516, 0.1f);
                double d0 = entity.lastTickPosX + (entity.posX - entity.lastTickPosX) * (double)partialTicks;
                double d1 = entity.lastTickPosY + (entity.posY - entity.lastTickPosY) * (double)partialTicks;
                double d2 = entity.lastTickPosZ + (entity.posZ - entity.lastTickPosZ) * (double)partialTicks;
                int l = MathHelper.floor(d1);
                int i1 = 5;
                if (Config.isRainFancy()) {
                    i1 = 10;
                }
                int j1 = -1;
                float f = (float)this.rendererUpdateCount + partialTicks;
                bufferbuilder.setTranslation(-d0, -d1, -d2);
                GlStateManager.color(1.0f, 1.0f, 1.0f, 1.0f);
                BlockPos.MutableBlockPos blockpos$mutableblockpos = new BlockPos.MutableBlockPos();
                for (int k1 = k - i1; k1 <= k + i1; ++k1) {
                    for (int l1 = i - i1; l1 <= i + i1; ++l1) {
                        int i2 = (k1 - k + 16) * 32 + l1 - i + 16;
                        double d3 = (double)this.rainXCoords[i2] * 0.5;
                        double d4 = (double)this.rainYCoords[i2] * 0.5;
                        blockpos$mutableblockpos.setPos(l1, 0, k1);
                        Biome biome = world.getBiome(blockpos$mutableblockpos);
                        if (!biome.canRain() && !biome.getEnableSnow()) continue;
                        int j2 = world.getPrecipitationHeight(blockpos$mutableblockpos).getY();
                        int k2 = j - i1;
                        int l2 = j + i1;
                        if (k2 < j2) {
                            k2 = j2;
                        }
                        if (l2 < j2) {
                            l2 = j2;
                        }
                        int i3 = j2;
                        if (j2 < l) {
                            i3 = l;
                        }
                        if (k2 == l2) continue;
                        this.random.setSeed(l1 * l1 * 3121 + l1 * 45238971 ^ k1 * k1 * 418711 + k1 * 13761);
                        blockpos$mutableblockpos.setPos(l1, k2, k1);
                        float f1 = biome.getFloatTemperature(blockpos$mutableblockpos);
                        if (world.getBiomeProvider().getTemperatureAtHeight(f1, j2) >= 0.15f) {
                            if (j1 != 0) {
                                if (j1 >= 0) {
                                    tessellator.draw();
                                }
                                j1 = 0;
                                this.mc.getTextureManager().bindTexture(RAIN_TEXTURES);
                                bufferbuilder.begin(7, DefaultVertexFormats.PARTICLE_POSITION_TEX_COLOR_LMAP);
                            }
                            double d5 = -((double)(this.rendererUpdateCount + l1 * l1 * 3121 + l1 * 45238971 + k1 * k1 * 418711 + k1 * 13761 & 0x1F) + (double)partialTicks) / 32.0 * (3.0 + this.random.nextDouble());
                            double d6 = (double)((float)l1 + 0.5f) - entity.posX;
                            double d7 = (double)((float)k1 + 0.5f) - entity.posZ;
                            float f2 = MathHelper.sqrt(d6 * d6 + d7 * d7) / (float)i1;
                            float f3 = ((1.0f - f2 * f2) * 0.5f + 0.5f) * f5;
                            blockpos$mutableblockpos.setPos(l1, i3, k1);
                            int j3 = ((World)world).getCombinedLight(blockpos$mutableblockpos, 0);
                            int k3 = j3 >> 16 & 0xFFFF;
                            int l3 = j3 & 0xFFFF;
                            bufferbuilder.pos((double)l1 - d3 + 0.5, l2, (double)k1 - d4 + 0.5).tex(0.0, (double)k2 * 0.25 + d5).color(1.0f, 1.0f, 1.0f, f3).lightmap(k3, l3).endVertex();
                            bufferbuilder.pos((double)l1 + d3 + 0.5, l2, (double)k1 + d4 + 0.5).tex(1.0, (double)k2 * 0.25 + d5).color(1.0f, 1.0f, 1.0f, f3).lightmap(k3, l3).endVertex();
                            bufferbuilder.pos((double)l1 + d3 + 0.5, k2, (double)k1 + d4 + 0.5).tex(1.0, (double)l2 * 0.25 + d5).color(1.0f, 1.0f, 1.0f, f3).lightmap(k3, l3).endVertex();
                            bufferbuilder.pos((double)l1 - d3 + 0.5, k2, (double)k1 - d4 + 0.5).tex(0.0, (double)l2 * 0.25 + d5).color(1.0f, 1.0f, 1.0f, f3).lightmap(k3, l3).endVertex();
                            continue;
                        }
                        if (j1 != 1) {
                            if (j1 >= 0) {
                                tessellator.draw();
                            }
                            j1 = 1;
                            this.mc.getTextureManager().bindTexture(SNOW_TEXTURES);
                            bufferbuilder.begin(7, DefaultVertexFormats.PARTICLE_POSITION_TEX_COLOR_LMAP);
                        }
                        double d8 = -((float)(this.rendererUpdateCount & 0x1FF) + partialTicks) / 512.0f;
                        double d9 = this.random.nextDouble() + (double)f * 0.01 * (double)((float)this.random.nextGaussian());
                        double d10 = this.random.nextDouble() + (double)(f * (float)this.random.nextGaussian()) * 0.001;
                        double d11 = (double)((float)l1 + 0.5f) - entity.posX;
                        double d12 = (double)((float)k1 + 0.5f) - entity.posZ;
                        float f6 = MathHelper.sqrt(d11 * d11 + d12 * d12) / (float)i1;
                        float f4 = ((1.0f - f6 * f6) * 0.3f + 0.5f) * f5;
                        blockpos$mutableblockpos.setPos(l1, i3, k1);
                        int i4 = (((World)world).getCombinedLight(blockpos$mutableblockpos, 0) * 3 + 0xF000F0) / 4;
                        int j4 = i4 >> 16 & 0xFFFF;
                        int k4 = i4 & 0xFFFF;
                        bufferbuilder.pos((double)l1 - d3 + 0.5, l2, (double)k1 - d4 + 0.5).tex(0.0 + d9, (double)k2 * 0.25 + d8 + d10).color(1.0f, 1.0f, 1.0f, f4).lightmap(j4, k4).endVertex();
                        bufferbuilder.pos((double)l1 + d3 + 0.5, l2, (double)k1 + d4 + 0.5).tex(1.0 + d9, (double)k2 * 0.25 + d8 + d10).color(1.0f, 1.0f, 1.0f, f4).lightmap(j4, k4).endVertex();
                        bufferbuilder.pos((double)l1 + d3 + 0.5, k2, (double)k1 + d4 + 0.5).tex(1.0 + d9, (double)l2 * 0.25 + d8 + d10).color(1.0f, 1.0f, 1.0f, f4).lightmap(j4, k4).endVertex();
                        bufferbuilder.pos((double)l1 - d3 + 0.5, k2, (double)k1 - d4 + 0.5).tex(0.0 + d9, (double)l2 * 0.25 + d8 + d10).color(1.0f, 1.0f, 1.0f, f4).lightmap(j4, k4).endVertex();
                    }
                }
                if (j1 >= 0) {
                    tessellator.draw();
                }
                bufferbuilder.setTranslation(0.0, 0.0, 0.0);
                GlStateManager.enableCull();
                GlStateManager.disableBlend();
                GlStateManager.alphaFunc(516, 0.1f);
                this.disableLightmap();
            }
        }
    }

    public void setupOverlayRendering() {
        ScaledResolution scaledresolution = new ScaledResolution(this.mc);
        GlStateManager.clear(256);
        GlStateManager.matrixMode(5889);
        GlStateManager.loadIdentity();
        GlStateManager.ortho(0.0, scaledresolution.getScaledWidth(), scaledresolution.getScaledHeight(), 0.0, 1000.0, 3000.0);
        GlStateManager.matrixMode(5888);
        GlStateManager.loadIdentity();
        GlStateManager.translate(0.0f, 0.0f, -2000.0f);
    }

    private void updateFogColor(float partialTicks) {
        float f9;
        WorldClient world = this.mc.world;
        Entity entity = this.mc.getRenderViewEntity();
        float f = Celestial.instance.featureManager.getFeatureByClass(FogColor.class).getState() ? FogColor.distance.getCurrentValue() * 10.0f : 0.25f + 0.75f * (float)this.mc.gameSettings.renderDistanceChunks / 32.0f;
        f = 1.0f - (float)Math.pow(f, 0.25);
        Vec3d vec3d = world.getSkyColor(this.mc.getRenderViewEntity(), partialTicks);
        vec3d = CustomColors.getWorldSkyColor(vec3d, world, this.mc.getRenderViewEntity(), partialTicks);
        float f1 = (float)vec3d.x;
        float f2 = (float)vec3d.y;
        float f3 = (float)vec3d.z;
        Vec3d vec3d1 = world.getFogColor(partialTicks);
        vec3d1 = CustomColors.getWorldFogColor(vec3d1, world, this.mc.getRenderViewEntity(), partialTicks);
        this.fogColorRed = (float)vec3d1.x;
        this.fogColorGreen = (float)vec3d1.y;
        this.fogColorBlue = (float)vec3d1.z;
        if (this.mc.gameSettings.renderDistanceChunks >= 4) {
            float[] afloat;
            double d0 = MathHelper.sin(world.getCelestialAngleRadians(partialTicks)) > 0.0f ? -1.0 : 1.0;
            Vec3d vec3d2 = new Vec3d(d0, 0.0, 0.0);
            float f5 = (float)entity.getLook(partialTicks).dotProduct(vec3d2);
            if (f5 < 0.0f) {
                f5 = 0.0f;
            }
            if (f5 > 0.0f && (afloat = world.provider.calcSunriseSunsetColors(world.getCelestialAngle(partialTicks), partialTicks)) != null) {
                this.fogColorRed = this.fogColorRed * (1.0f - (f5 *= afloat[3])) + afloat[0] * f5;
                this.fogColorGreen = this.fogColorGreen * (1.0f - f5) + afloat[1] * f5;
                this.fogColorBlue = this.fogColorBlue * (1.0f - f5) + afloat[2] * f5;
            }
        }
        this.fogColorRed += (f1 - this.fogColorRed) * f;
        this.fogColorGreen += (f2 - this.fogColorGreen) * f;
        this.fogColorBlue += (f3 - this.fogColorBlue) * f;
        float f8 = world.getRainStrength(partialTicks);
        if (f8 > 0.0f) {
            float f4 = 1.0f - f8 * 0.5f;
            float f10 = 1.0f - f8 * 0.4f;
            this.fogColorRed *= f4;
            this.fogColorGreen *= f4;
            this.fogColorBlue *= f10;
        }
        if ((f9 = world.getThunderStrength(partialTicks)) > 0.0f) {
            float f11 = 1.0f - f9 * 0.5f;
            this.fogColorRed *= f11;
            this.fogColorGreen *= f11;
            this.fogColorBlue *= f11;
        }
        IBlockState iblockstate1 = ActiveRenderInfo.getBlockStateAtEntityViewpoint(this.mc.world, entity, partialTicks);
        if (this.cloudFog) {
            Vec3d vec3d4 = world.getCloudColour(partialTicks);
            this.fogColorRed = (float)vec3d4.x;
            this.fogColorGreen = (float)vec3d4.y;
            this.fogColorBlue = (float)vec3d4.z;
        } else if (Reflector.ForgeBlock_getFogColor.exists()) {
            Vec3d vec3d5 = ActiveRenderInfo.projectViewFromEntity(entity, partialTicks);
            BlockPos blockpos = new BlockPos(vec3d5);
            IBlockState iblockstate = this.mc.world.getBlockState(blockpos);
            Vec3d vec3d3 = (Vec3d)Reflector.call(iblockstate.getBlock(), Reflector.ForgeBlock_getFogColor, this.mc.world, blockpos, iblockstate, entity, new Vec3d(this.fogColorRed, this.fogColorGreen, this.fogColorBlue), Float.valueOf(partialTicks));
            this.fogColorRed = (float)vec3d3.x;
            this.fogColorGreen = (float)vec3d3.y;
            this.fogColorBlue = (float)vec3d3.z;
        } else if (iblockstate1.getMaterial() == Material.WATER) {
            float f12 = 0.0f;
            if (entity instanceof EntityLivingBase) {
                f12 = (float)EnchantmentHelper.getRespirationModifier((EntityLivingBase)entity) * 0.2f;
                if (((EntityLivingBase)entity).isPotionActive(MobEffects.WATER_BREATHING)) {
                    f12 = f12 * 0.3f + 0.6f;
                }
            }
            this.fogColorRed = 0.02f + f12;
            this.fogColorGreen = 0.02f + f12;
            this.fogColorBlue = 0.2f + f12;
            Vec3d vec3d7 = CustomColors.getUnderwaterColor(this.mc.world, this.mc.getRenderViewEntity().posX, this.mc.getRenderViewEntity().posY + 1.0, this.mc.getRenderViewEntity().posZ);
            if (vec3d7 != null) {
                this.fogColorRed = (float)vec3d7.x;
                this.fogColorGreen = (float)vec3d7.y;
                this.fogColorBlue = (float)vec3d7.z;
            }
        } else if (iblockstate1.getMaterial() == Material.LAVA) {
            this.fogColorRed = 0.6f;
            this.fogColorGreen = 0.1f;
            this.fogColorBlue = 0.0f;
            Vec3d vec3d6 = CustomColors.getUnderlavaColor(this.mc.world, this.mc.getRenderViewEntity().posX, this.mc.getRenderViewEntity().posY + 1.0, this.mc.getRenderViewEntity().posZ);
            if (vec3d6 != null) {
                this.fogColorRed = (float)vec3d6.x;
                this.fogColorGreen = (float)vec3d6.y;
                this.fogColorBlue = (float)vec3d6.z;
            }
        }
        float f13 = this.fogColor2 + (this.fogColor1 - this.fogColor2) * partialTicks;
        this.fogColorRed *= f13;
        this.fogColorGreen *= f13;
        this.fogColorBlue *= f13;
        double d1 = (entity.lastTickPosY + (entity.posY - entity.lastTickPosY) * (double)partialTicks) * world.provider.getVoidFogYFactor();
        if (entity instanceof EntityLivingBase && ((EntityLivingBase)entity).isPotionActive(MobEffects.BLINDNESS)) {
            int i = ((EntityLivingBase)entity).getActivePotionEffect(MobEffects.BLINDNESS).getDuration();
            d1 = i < 20 ? (d1 *= (double)(1.0f - (float)i / 20.0f)) : 0.0;
        }
        if (d1 < 1.0) {
            if (d1 < 0.0) {
                d1 = 0.0;
            }
            d1 *= d1;
            this.fogColorRed = (float)((double)this.fogColorRed * d1);
            this.fogColorGreen = (float)((double)this.fogColorGreen * d1);
            this.fogColorBlue = (float)((double)this.fogColorBlue * d1);
        }
        if (this.bossColorModifier > 0.0f) {
            float f14 = this.bossColorModifierPrev + (this.bossColorModifier - this.bossColorModifierPrev) * partialTicks;
            this.fogColorRed = this.fogColorRed * (1.0f - f14) + this.fogColorRed * 0.7f * f14;
            this.fogColorGreen = this.fogColorGreen * (1.0f - f14) + this.fogColorGreen * 0.6f * f14;
            this.fogColorBlue = this.fogColorBlue * (1.0f - f14) + this.fogColorBlue * 0.6f * f14;
        }
        if (entity instanceof EntityLivingBase && ((EntityLivingBase)entity).isPotionActive(MobEffects.NIGHT_VISION)) {
            float f15 = this.getNightVisionBrightness((EntityLivingBase)entity, partialTicks);
            float f6 = 1.0f / this.fogColorRed;
            if (f6 > 1.0f / this.fogColorGreen) {
                f6 = 1.0f / this.fogColorGreen;
            }
            if (f6 > 1.0f / this.fogColorBlue) {
                f6 = 1.0f / this.fogColorBlue;
            }
            this.fogColorRed = this.fogColorRed * (1.0f - f15) + this.fogColorRed * f6 * f15;
            this.fogColorGreen = this.fogColorGreen * (1.0f - f15) + this.fogColorGreen * f6 * f15;
            this.fogColorBlue = this.fogColorBlue * (1.0f - f15) + this.fogColorBlue * f6 * f15;
        }
        if (this.mc.gameSettings.anaglyph) {
            float f16 = (this.fogColorRed * 30.0f + this.fogColorGreen * 59.0f + this.fogColorBlue * 11.0f) / 100.0f;
            float f17 = (this.fogColorRed * 30.0f + this.fogColorGreen * 70.0f) / 100.0f;
            float f7 = (this.fogColorRed * 30.0f + this.fogColorBlue * 70.0f) / 100.0f;
            this.fogColorRed = f16;
            this.fogColorGreen = f17;
            this.fogColorBlue = f7;
        }
        if (Reflector.EntityViewRenderEvent_FogColors_Constructor.exists()) {
            Object object = Reflector.newInstance(Reflector.EntityViewRenderEvent_FogColors_Constructor, this, entity, iblockstate1, Float.valueOf(partialTicks), Float.valueOf(this.fogColorRed), Float.valueOf(this.fogColorGreen), Float.valueOf(this.fogColorBlue));
            Reflector.postForgeBusEvent(object);
            this.fogColorRed = Reflector.callFloat(object, Reflector.EntityViewRenderEvent_FogColors_getRed, new Object[0]);
            this.fogColorGreen = Reflector.callFloat(object, Reflector.EntityViewRenderEvent_FogColors_getGreen, new Object[0]);
            this.fogColorBlue = Reflector.callFloat(object, Reflector.EntityViewRenderEvent_FogColors_getBlue, new Object[0]);
        }
        Shaders.setClearColor(this.fogColorRed, this.fogColorGreen, this.fogColorBlue, 0.0f);
    }

    private void setupFog(int startCoords, float partialTicks) {
        if (Celestial.instance.featureManager.getFeatureByClass(NoRender.class).getState() && NoRender.fog.getCurrentValue()) {
            return;
        }
        this.fogStandard = false;
        Entity entity = this.mc.getRenderViewEntity();
        this.setupFogColor(false);
        GlStateManager.glNormal3f(0.0f, -1.0f, 0.0f);
        GlStateManager.color(1.0f, 1.0f, 1.0f, 1.0f);
        IBlockState iblockstate = ActiveRenderInfo.getBlockStateAtEntityViewpoint(this.mc.world, entity, partialTicks);
        float f = -1.0f;
        if (Reflector.ForgeHooksClient_getFogDensity.exists()) {
            f = Reflector.callFloat(Reflector.ForgeHooksClient_getFogDensity, this, entity, iblockstate, Float.valueOf(partialTicks), Float.valueOf(0.1f));
        }
        if (f >= 0.0f) {
            GlStateManager.setFogDensity(f);
        } else if (entity instanceof EntityLivingBase && ((EntityLivingBase)entity).isPotionActive(MobEffects.BLINDNESS)) {
            float f2 = 5.0f;
            int i = ((EntityLivingBase)entity).getActivePotionEffect(MobEffects.BLINDNESS).getDuration();
            if (i < 20) {
                f2 = 5.0f + (this.farPlaneDistance - 5.0f) * (1.0f - (float)i / 20.0f);
            }
            if (Config.isShaders()) {
                Shaders.setFog(GlStateManager.FogMode.LINEAR);
            } else {
                GlStateManager.setFog(GlStateManager.FogMode.LINEAR);
            }
            if (startCoords == -1) {
                GlStateManager.setFogStart(0.0f);
                GlStateManager.setFogEnd(f2 * 0.8f);
            } else {
                GlStateManager.setFogStart(f2 * 0.25f);
                GlStateManager.setFogEnd(f2);
            }
            if (GLContext.getCapabilities().GL_NV_fog_distance && Config.isFogFancy()) {
                GlStateManager.glFogi(34138, 34139);
            }
        } else if (this.cloudFog) {
            if (Config.isShaders()) {
                Shaders.setFog(GlStateManager.FogMode.EXP);
            } else {
                GlStateManager.setFog(GlStateManager.FogMode.EXP);
            }
            GlStateManager.setFogDensity(0.1f);
        } else if (iblockstate.getMaterial() == Material.WATER) {
            if (Config.isShaders()) {
                Shaders.setFog(GlStateManager.FogMode.EXP);
            } else {
                GlStateManager.setFog(GlStateManager.FogMode.EXP);
            }
            if (entity instanceof EntityLivingBase) {
                if (((EntityLivingBase)entity).isPotionActive(MobEffects.WATER_BREATHING)) {
                    GlStateManager.setFogDensity(0.01f);
                } else {
                    GlStateManager.setFogDensity(0.1f - (float)EnchantmentHelper.getRespirationModifier((EntityLivingBase)entity) * 0.03f);
                }
            } else {
                GlStateManager.setFogDensity(0.1f);
            }
            if (Config.isClearWater()) {
                GlStateManager.setFogDensity(0.02f);
            }
        } else if (iblockstate.getMaterial() == Material.LAVA) {
            if (Config.isShaders()) {
                Shaders.setFog(GlStateManager.FogMode.EXP);
            } else {
                GlStateManager.setFog(GlStateManager.FogMode.EXP);
            }
            GlStateManager.setFogDensity(2.0f);
        } else {
            float value = Celestial.instance.featureManager.getFeatureByClass(FogColor.class).getState() ? FogColor.distance.getCurrentValue() * 50.0f : 0.0f;
            float f1 = this.farPlaneDistance - value;
            this.fogStandard = true;
            if (Config.isShaders()) {
                Shaders.setFog(GlStateManager.FogMode.LINEAR);
            } else {
                GlStateManager.setFog(GlStateManager.FogMode.LINEAR);
            }
            if (startCoords == -1) {
                GlStateManager.setFogStart(0.0f);
                GlStateManager.setFogEnd(f1);
            } else {
                GlStateManager.setFogStart(f1 * Config.getFogStart());
                GlStateManager.setFogEnd(f1);
            }
            if (GLContext.getCapabilities().GL_NV_fog_distance) {
                if (Config.isFogFancy()) {
                    GlStateManager.glFogi(34138, 34139);
                }
                if (Config.isFogFast()) {
                    GlStateManager.glFogi(34138, 34140);
                }
            }
            if (this.mc.world.provider.doesXZShowFog((int)entity.posX, (int)entity.posZ) || this.mc.ingameGUI.getBossOverlay().shouldCreateFog()) {
                GlStateManager.setFogStart(f1 * 0.05f);
                GlStateManager.setFogEnd(f1);
            }
            if (Reflector.ForgeHooksClient_onFogRender.exists()) {
                Reflector.callVoid(Reflector.ForgeHooksClient_onFogRender, this, entity, iblockstate, Float.valueOf(partialTicks), startCoords, Float.valueOf(f1));
            }
        }
        EventFogColor event = new EventFogColor(this.fogColorRed, this.fogColorGreen, this.fogColorBlue, 0);
        EventManager.call(event);
        this.fogColorRed = event.red / 255.0f;
        this.fogColorGreen = event.green / 255.0f;
        this.fogColorBlue = event.blue / 255.0f;
        GlStateManager.enableColorMaterial();
        GlStateManager.enableFog();
        GlStateManager.colorMaterial(1028, 4608);
    }

    public void setupFogColor(boolean p_191514_1_) {
        if (p_191514_1_) {
            GlStateManager.glFog(2918, this.setFogColorBuffer(0.0f, 0.0f, 0.0f, 1.0f));
        } else {
            GlStateManager.glFog(2918, this.setFogColorBuffer(this.fogColorRed, this.fogColorGreen, this.fogColorBlue, 1.0f));
        }
    }

    private FloatBuffer setFogColorBuffer(float red, float green, float blue, float alpha) {
        if (Config.isShaders()) {
            Shaders.setFogColor(red, green, blue);
        }
        this.fogColorBuffer.clear();
        this.fogColorBuffer.put(red).put(green).put(blue).put(alpha);
        this.fogColorBuffer.flip();
        return this.fogColorBuffer;
    }

    public void func_190564_k() {
        this.field_190566_ab = null;
        this.theMapItemRenderer.clearLoadedMaps();
    }

    public MapItemRenderer getMapItemRenderer() {
        return this.theMapItemRenderer;
    }

    private void waitForServerThread() {
        this.serverWaitTimeCurrent = 0;
        if (Config.isSmoothWorld() && Config.isSingleProcessor()) {
            IntegratedServer integratedserver;
            if (this.mc.isIntegratedServerRunning() && (integratedserver = this.mc.getIntegratedServer()) != null) {
                boolean flag = this.mc.isGamePaused();
                if (!flag && !(this.mc.currentScreen instanceof GuiDownloadTerrain)) {
                    if (this.serverWaitTime > 0) {
                        Lagometer.timerServer.start();
                        Config.sleep(this.serverWaitTime);
                        Lagometer.timerServer.end();
                        this.serverWaitTimeCurrent = this.serverWaitTime;
                    }
                    long i = System.nanoTime() / 1000000L;
                    if (this.lastServerTime != 0L && this.lastServerTicks != 0) {
                        long j = i - this.lastServerTime;
                        if (j < 0L) {
                            this.lastServerTime = i;
                            j = 0L;
                        }
                        if (j >= 50L) {
                            this.lastServerTime = i;
                            int k = integratedserver.getTickCounter();
                            int l = k - this.lastServerTicks;
                            if (l < 0) {
                                this.lastServerTicks = k;
                                l = 0;
                            }
                            if (l < 1 && this.serverWaitTime < 100) {
                                this.serverWaitTime += 2;
                            }
                            if (l > 1 && this.serverWaitTime > 0) {
                                --this.serverWaitTime;
                            }
                            this.lastServerTicks = k;
                        }
                    } else {
                        this.lastServerTime = i;
                        this.lastServerTicks = integratedserver.getTickCounter();
                        this.avgServerTickDiff = 1.0f;
                        this.avgServerTimeDiff = 50.0f;
                    }
                } else {
                    if (this.mc.currentScreen instanceof GuiDownloadTerrain) {
                        Config.sleep(20L);
                    }
                    this.lastServerTime = 0L;
                    this.lastServerTicks = 0;
                }
            }
        } else {
            this.lastServerTime = 0L;
            this.lastServerTicks = 0;
        }
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
        WorldClient world = this.mc.world;
        if (this.mc.currentScreen instanceof GuiMainMenu) {
            this.updateMainMenu((GuiMainMenu)this.mc.currentScreen);
        }
        if (this.updatedWorld != world) {
            RandomMobs.worldChanged(this.updatedWorld, world);
            Config.updateThreadPriorities();
            this.lastServerTime = 0L;
            this.lastServerTicks = 0;
            this.updatedWorld = world;
        }
        if (!this.setFxaaShader(Shaders.configAntialiasingLevel)) {
            Shaders.configAntialiasingLevel = 0;
        }
    }

    private void frameFinish() {
        long i;
        if (this.mc.world != null && (i = System.currentTimeMillis()) > this.lastErrorCheckTimeMs + 10000L) {
            this.lastErrorCheckTimeMs = i;
            int j = GlStateManager.glGetError();
            if (j != 0) {
                String s = GLU.gluErrorString(j);
                TextComponentString textcomponentstring = new TextComponentString(I18n.format("of.message.openglError", j, s));
                this.mc.ingameGUI.getChatGUI().printChatMessage(textcomponentstring);
            }
        }
    }

    private void updateMainMenu(GuiMainMenu p_updateMainMenu_1_) {
        try {
            String s = null;
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(new Date());
            int i = calendar.get(5);
            int j = calendar.get(2) + 1;
            if (i == 8 && j == 4) {
                s = "Happy birthday, OptiFine!";
            }
            if (i == 14 && j == 8) {
                s = "Happy birthday, sp614x!";
            }
            if (s == null) {
                return;
            }
            Reflector.setFieldValue(p_updateMainMenu_1_, Reflector.GuiMainMenu_splashText, s);
        }
        catch (Throwable throwable) {
            // empty catch block
        }
    }

    public boolean setFxaaShader(int p_setFxaaShader_1_) {
        if (!OpenGlHelper.isFramebufferEnabled()) {
            return false;
        }
        if (this.theShaderGroup != null && this.theShaderGroup != this.fxaaShaders[2] && this.theShaderGroup != this.fxaaShaders[4]) {
            return true;
        }
        if (p_setFxaaShader_1_ != 2 && p_setFxaaShader_1_ != 4) {
            if (this.theShaderGroup == null) {
                return true;
            }
            this.theShaderGroup.deleteShaderGroup();
            this.theShaderGroup = null;
            return true;
        }
        if (this.theShaderGroup != null && this.theShaderGroup == this.fxaaShaders[p_setFxaaShader_1_]) {
            return true;
        }
        if (this.mc.world == null) {
            return true;
        }
        this.loadShader(new ResourceLocation("shaders/post/fxaa_of_" + p_setFxaaShader_1_ + "x.json"));
        this.fxaaShaders[p_setFxaaShader_1_] = this.theShaderGroup;
        return this.useShader;
    }

    private void checkLoadVisibleChunks(Entity p_checkLoadVisibleChunks_1_, float p_checkLoadVisibleChunks_2_, ICamera p_checkLoadVisibleChunks_3_, boolean p_checkLoadVisibleChunks_4_) {
        int i = 201435902;
        if (this.loadVisibleChunks) {
            this.loadVisibleChunks = false;
            this.loadAllVisibleChunks(p_checkLoadVisibleChunks_1_, p_checkLoadVisibleChunks_2_, p_checkLoadVisibleChunks_3_, p_checkLoadVisibleChunks_4_);
            this.mc.ingameGUI.getChatGUI().deleteChatLine(i);
        }
        if (Keyboard.isKeyDown(61) && Keyboard.isKeyDown(38)) {
            if (this.mc.gameSettings.field_194146_ao.getKeyCode() == 38) {
                if (this.mc.currentScreen instanceof GuiScreenAdvancements) {
                    this.mc.displayGuiScreen(null);
                }
                while (Keyboard.next()) {
                }
            }
            if (this.mc.currentScreen != null) {
                return;
            }
            this.loadVisibleChunks = true;
            TextComponentString textcomponentstring = new TextComponentString(I18n.format("of.message.loadingVisibleChunks", new Object[0]));
            this.mc.ingameGUI.getChatGUI().printChatMessageWithOptionalDeletion(textcomponentstring, i);
            Reflector.Minecraft_actionKeyF3.setValue(this.mc, Boolean.TRUE);
        }
    }

    private void loadAllVisibleChunks(Entity p_loadAllVisibleChunks_1_, double p_loadAllVisibleChunks_2_, ICamera p_loadAllVisibleChunks_4_, boolean p_loadAllVisibleChunks_5_) {
        RenderGlobal renderglobal = Config.getRenderGlobal();
        int i = renderglobal.getCountLoadedChunks();
        long j = System.currentTimeMillis();
        Config.dbg("Loading visible chunks");
        long k = System.currentTimeMillis() + 5000L;
        int l = 0;
        boolean flag = false;
        do {
            flag = false;
            for (int i1 = 0; i1 < 100; ++i1) {
                renderglobal.displayListEntitiesDirty = true;
                renderglobal.setupTerrain(p_loadAllVisibleChunks_1_, p_loadAllVisibleChunks_2_, p_loadAllVisibleChunks_4_, this.frameCount++, p_loadAllVisibleChunks_5_);
                if (!renderglobal.hasNoChunkUpdates()) {
                    flag = true;
                }
                l += renderglobal.getCountChunksToUpdate();
                renderglobal.updateChunks(System.nanoTime() + 1000000000L);
                l -= renderglobal.getCountChunksToUpdate();
            }
            if (renderglobal.getCountLoadedChunks() != i) {
                flag = true;
                i = renderglobal.getCountLoadedChunks();
            }
            if (System.currentTimeMillis() <= k) continue;
            Config.log("Chunks loaded: " + l);
            k = System.currentTimeMillis() + 5000L;
        } while (flag);
        Config.log("Chunks loaded: " + l);
        Config.log("Finished loading visible chunks");
        RenderChunk.renderChunksUpdated = 0;
    }

    public static void drawNameplate(FontRenderer fontRendererIn, String str, float x, float y, float z, int verticalShift, float viewerYaw, float viewerPitch, boolean isThirdPersonFrontal, boolean isSneaking) {
        GlStateManager.pushMatrix();
        GlStateManager.translate(x, y, z);
        GlStateManager.glNormal3f(0.0f, 1.0f, 0.0f);
        GlStateManager.rotate(-viewerYaw, 0.0f, 1.0f, 0.0f);
        GlStateManager.rotate((float)(isThirdPersonFrontal ? -1 : 1) * viewerPitch, 1.0f, 0.0f, 0.0f);
        GlStateManager.scale(-0.025f, -0.025f, 0.025f);
        GlStateManager.disableLighting();
        GlStateManager.depthMask(false);
        if (!isSneaking) {
            GlStateManager.disableDepth();
        }
        GlStateManager.enableBlend();
        GlStateManager.tryBlendFuncSeparate(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA, GlStateManager.SourceFactor.ONE, GlStateManager.DestFactor.ZERO);
        int i = fontRendererIn.getStringWidth(str) / 2;
        GlStateManager.disableTexture2D();
        Tessellator tessellator = Tessellator.getInstance();
        BufferBuilder bufferbuilder = tessellator.getBuffer();
        bufferbuilder.begin(7, DefaultVertexFormats.POSITION_COLOR);
        bufferbuilder.pos(-i - 1, -1 + verticalShift, 0.0).color(0.0f, 0.0f, 0.0f, 0.25f).endVertex();
        bufferbuilder.pos(-i - 1, 8 + verticalShift, 0.0).color(0.0f, 0.0f, 0.0f, 0.25f).endVertex();
        bufferbuilder.pos(i + 1, 8 + verticalShift, 0.0).color(0.0f, 0.0f, 0.0f, 0.25f).endVertex();
        bufferbuilder.pos(i + 1, -1 + verticalShift, 0.0).color(0.0f, 0.0f, 0.0f, 0.25f).endVertex();
        tessellator.draw();
        GlStateManager.enableTexture2D();
        if (!isSneaking) {
            fontRendererIn.drawString(str, -fontRendererIn.getStringWidth(str) / 2, verticalShift, 0x20FFFFFF);
            GlStateManager.enableDepth();
        }
        GlStateManager.depthMask(true);
        fontRendererIn.drawString(str, -fontRendererIn.getStringWidth(str) / 2, verticalShift, isSneaking ? 0x20FFFFFF : -1);
        GlStateManager.enableLighting();
        GlStateManager.disableBlend();
        GlStateManager.color(1.0f, 1.0f, 1.0f, 1.0f);
        GlStateManager.popMatrix();
    }

    public void func_190565_a(ItemStack p_190565_1_) {
        if (Celestial.instance.featureManager.getFeatureByClass(NoRender.class).getState() && NoRender.totem.getCurrentValue()) {
            return;
        }
        this.field_190566_ab = p_190565_1_;
        this.field_190567_ac = 40;
        this.field_190568_ad = this.random.nextFloat() * 2.0f - 1.0f;
        this.field_190569_ae = this.random.nextFloat() * 2.0f - 1.0f;
    }

    private void func_190563_a(int p_190563_1_, int p_190563_2_, float p_190563_3_) {
        if (this.field_190566_ab != null && this.field_190567_ac > 0) {
            int i = 40 - this.field_190567_ac;
            float f = ((float)i + p_190563_3_) / 40.0f;
            float f1 = f * f;
            float f2 = f * f1;
            float f3 = 10.25f * f2 * f1 + -24.95f * f1 * f1 + 25.5f * f2 + -13.8f * f1 + 4.0f * f;
            float f4 = f3 * (float)Math.PI;
            float f5 = this.field_190568_ad * (float)(p_190563_1_ / 4);
            float f6 = this.field_190569_ae * (float)(p_190563_2_ / 4);
            GlStateManager.enableAlpha();
            GlStateManager.pushMatrix();
            GlStateManager.pushAttrib();
            GlStateManager.enableDepth();
            GlStateManager.disableCull();
            RenderHelper.enableStandardItemLighting();
            GlStateManager.translate((float)(p_190563_1_ / 2) + f5 * MathHelper.abs(MathHelper.sin(f4 * 2.0f)), (float)(p_190563_2_ / 2) + f6 * MathHelper.abs(MathHelper.sin(f4 * 2.0f)), -50.0f);
            float f7 = 50.0f + 175.0f * MathHelper.sin(f4);
            GlStateManager.scale(f7, -f7, f7);
            GlStateManager.rotate(900.0f * MathHelper.abs(MathHelper.sin(f4)), 0.0f, 1.0f, 0.0f);
            GlStateManager.rotate(6.0f * MathHelper.cos(f * 8.0f), 1.0f, 0.0f, 0.0f);
            GlStateManager.rotate(6.0f * MathHelper.cos(f * 8.0f), 0.0f, 0.0f, 1.0f);
            this.mc.getRenderItem().renderItem(this.field_190566_ab, ItemCameraTransforms.TransformType.FIXED);
            GlStateManager.popAttrib();
            GlStateManager.popMatrix();
            RenderHelper.disableStandardItemLighting();
            GlStateManager.enableCull();
            GlStateManager.disableDepth();
        }
    }

    static {
        SHADERS_TEXTURES = new ResourceLocation[]{new ResourceLocation("shaders/post/notch.json"), new ResourceLocation("shaders/post/fxaa.json"), new ResourceLocation("shaders/post/art.json"), new ResourceLocation("shaders/post/bumpy.json"), new ResourceLocation("shaders/post/blobs2.json"), new ResourceLocation("shaders/post/pencil.json"), new ResourceLocation("shaders/post/color_convolve.json"), new ResourceLocation("shaders/post/deconverge.json"), new ResourceLocation("shaders/post/flip.json"), new ResourceLocation("shaders/post/invert.json"), new ResourceLocation("shaders/post/ntsc.json"), new ResourceLocation("shaders/post/outline.json"), new ResourceLocation("shaders/post/phosphor.json"), new ResourceLocation("shaders/post/scan_pincushion.json"), new ResourceLocation("shaders/post/sobel.json"), new ResourceLocation("shaders/post/bits.json"), new ResourceLocation("shaders/post/desaturate.json"), new ResourceLocation("shaders/post/green.json"), new ResourceLocation("shaders/post/blur.json"), new ResourceLocation("shaders/post/wobble.json"), new ResourceLocation("shaders/post/blobs.json"), new ResourceLocation("shaders/post/antialias.json"), new ResourceLocation("shaders/post/creeper.json"), new ResourceLocation("shaders/post/spider.json")};
        SHADER_COUNT = SHADERS_TEXTURES.length;
    }
}

