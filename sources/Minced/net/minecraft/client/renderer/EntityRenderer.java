// 
// Decompiled by Procyon v0.5.36
// 

package net.minecraft.client.renderer;

import org.apache.logging.log4j.LogManager;
import net.minecraft.client.renderer.block.model.ItemCameraTransforms;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.renderer.chunk.RenderChunk;
import net.minecraft.client.gui.advancements.GuiScreenAdvancements;
import org.lwjgl.input.Keyboard;
import java.util.Date;
import java.util.Calendar;
import net.optifine.util.TimedEvent;
import net.minecraft.client.gui.GuiScreen;
import net.optifine.gui.GuiChatOF;
import net.minecraft.client.gui.GuiChat;
import net.optifine.RandomEntities;
import net.minecraft.client.gui.GuiMainMenu;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.event.ClickEvent;
import net.minecraft.util.text.Style;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.client.resources.I18n;
import net.optifine.util.TextureUtils;
import net.optifine.reflect.ReflectorResolver;
import net.optifine.GlErrors;
import net.minecraft.server.integrated.IntegratedServer;
import net.minecraft.client.gui.GuiDownloadTerrain;
import org.lwjgl.opengl.GLContext;
import ru.tuskevich.event.events.impl.EventOverlay;
import ru.tuskevich.modules.impl.HUD.NoOverlay;
import java.awt.Color;
import net.minecraft.enchantment.EnchantmentHelper;
import ru.tuskevich.modules.impl.RENDER.CustomFog;
import net.minecraft.world.WorldProvider;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.world.biome.Biome;
import net.minecraft.util.SoundCategory;
import net.minecraft.init.SoundEvents;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.world.IBlockAccess;
import net.minecraft.client.renderer.culling.ICamera;
import net.minecraft.client.renderer.culling.ClippingHelper;
import net.minecraft.client.particle.ParticleManager;
import ru.tuskevich.event.events.Event;
import ru.tuskevich.event.EventManager;
import ru.tuskevich.event.events.impl.EventRender;
import net.minecraft.util.BlockRenderLayer;
import org.lwjgl.opengl.GL11;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.client.renderer.culling.Frustum;
import net.minecraft.client.renderer.culling.ClippingHelperImpl;
import net.minecraft.inventory.IInventory;
import net.minecraft.world.GameType;
import java.awt.Graphics;
import java.awt.image.RenderedImage;
import javax.imageio.ImageIO;
import java.awt.Image;
import java.awt.image.ImageObserver;
import java.awt.image.BufferedImage;
import net.minecraft.util.ScreenShotHelper;
import net.minecraft.crash.CrashReportCategory;
import net.optifine.util.MemoryMonitor;
import net.minecraft.util.ReportedException;
import net.minecraft.crash.ICrashReportDetail;
import net.minecraft.crash.CrashReport;
import net.optifine.Lagometer;
import net.minecraft.client.renderer.tileentity.TileEntityRendererDispatcher;
import net.minecraft.client.gui.ScaledResolution;
import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.Display;
import ru.tuskevich.modules.impl.RENDER.WorldColor;
import net.optifine.CustomColors;
import net.optifine.shaders.ShadersRender;
import net.optifine.reflect.ReflectorForge;
import net.optifine.shaders.Shaders;
import net.minecraft.init.MobEffects;
import org.lwjgl.util.glu.Project;
import net.minecraft.block.Block;
import net.minecraft.entity.passive.EntityAnimal;
import ru.tuskevich.modules.Module;
import ru.tuskevich.modules.impl.PLAYER.CameraClip;
import ru.tuskevich.Minced;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.BlockBed;
import net.minecraft.init.Blocks;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.block.material.Material;
import ru.tuskevich.util.math.MathUtility;
import ru.tuskevich.util.animations.AnimationMath;
import net.minecraft.client.settings.GameSettings;
import net.minecraft.src.Config;
import net.minecraft.client.entity.AbstractClientPlayer;
import net.minecraft.util.math.AxisAlignedBB;
import java.util.List;
import net.minecraft.util.math.Vec3d;
import net.minecraft.entity.item.EntityItemFrame;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.RayTraceResult;
import com.google.common.base.Predicates;
import com.google.common.base.Predicate;
import net.minecraft.util.EntitySelectors;
import net.minecraft.util.math.BlockPos;
import net.minecraft.client.shader.ShaderLinkHelper;
import com.google.gson.JsonSyntaxException;
import java.io.IOException;
import net.optifine.reflect.Reflector;
import net.minecraft.entity.monster.EntityEnderman;
import net.minecraft.entity.monster.EntitySpider;
import net.minecraft.entity.monster.EntityCreeper;
import javax.annotation.Nullable;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;
import net.minecraft.client.shader.ShaderGroup;
import net.minecraft.item.ItemStack;
import java.nio.FloatBuffer;
import net.minecraft.client.renderer.texture.DynamicTexture;
import net.minecraft.util.MouseFilter;
import net.minecraft.entity.Entity;
import net.minecraft.client.gui.MapItemRenderer;
import java.util.Random;
import net.minecraft.client.resources.IResourceManager;
import net.minecraft.client.Minecraft;
import net.minecraft.util.ResourceLocation;
import org.apache.logging.log4j.Logger;
import net.minecraft.client.resources.IResourceManagerReloadListener;

public class EntityRenderer implements IResourceManagerReloadListener
{
    private static final Logger LOGGER;
    private static final ResourceLocation RAIN_TEXTURES;
    private static final ResourceLocation SNOW_TEXTURES;
    public static boolean anaglyphEnable;
    public static int anaglyphField;
    private final Minecraft mc;
    private final IResourceManager resourceManager;
    private final Random random;
    private float farPlaneDistance;
    public ItemRenderer itemRenderer;
    private final MapItemRenderer mapItemRenderer;
    private int rendererUpdateCount;
    private Entity pointedEntity;
    private MouseFilter mouseFilterXAxis;
    private MouseFilter mouseFilterYAxis;
    private float thirdPersonDistance;
    private float thirdPersonDistancePrev;
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
    private boolean renderHand;
    private boolean drawBlockOutline;
    private long timeWorldIcon;
    private long prevFrameTime;
    private long renderEndNanoTime;
    private final DynamicTexture lightmapTexture;
    private final int[] lightmapColors;
    private final ResourceLocation locationLightMap;
    private boolean lightmapUpdateNeeded;
    private float torchFlickerX;
    private float torchFlickerDX;
    private int rainSoundCounter;
    private final float[] rainXCoords;
    private final float[] rainYCoords;
    private final FloatBuffer fogColorBuffer;
    public float fogColorRed;
    public float fogColorGreen;
    public float fogColorBlue;
    private float fogColor2;
    private float fogColor1;
    private int debugViewDirection;
    private boolean debugView;
    private double cameraZoom;
    private double cameraYaw;
    private double cameraPitch;
    private ItemStack itemActivationItem;
    private int itemActivationTicks;
    private float itemActivationOffX;
    private float itemActivationOffY;
    private ShaderGroup shaderGroup;
    private static final ResourceLocation[] SHADERS_TEXTURES;
    public static final int SHADER_COUNT;
    private int shaderIndex;
    private boolean useShader;
    public int frameCount;
    private boolean initialized;
    private World updatedWorld;
    public boolean fogStandard;
    private float clipDistance;
    private long lastServerTime;
    private int lastServerTicks;
    private int serverWaitTime;
    private int serverWaitTimeCurrent;
    private float avgServerTimeDiff;
    private float avgServerTickDiff;
    private ShaderGroup[] fxaaShaders;
    private boolean loadVisibleChunks;
    private float a;
    public double dLerp;
    public double scrool;
    
    public EntityRenderer(final Minecraft mcIn, final IResourceManager resourceManagerIn) {
        this.random = new Random();
        this.mouseFilterXAxis = new MouseFilter();
        this.mouseFilterYAxis = new MouseFilter();
        this.thirdPersonDistance = 4.0f;
        this.thirdPersonDistancePrev = 4.0f;
        this.renderHand = true;
        this.drawBlockOutline = true;
        this.prevFrameTime = Minecraft.getSystemTime();
        this.rainXCoords = new float[1024];
        this.rainYCoords = new float[1024];
        this.fogColorBuffer = GLAllocation.createDirectFloatBuffer(16);
        this.cameraZoom = 1.0;
        this.initialized = false;
        this.updatedWorld = null;
        this.fogStandard = false;
        this.clipDistance = 128.0f;
        this.lastServerTime = 0L;
        this.lastServerTicks = 0;
        this.serverWaitTime = 0;
        this.serverWaitTimeCurrent = 0;
        this.avgServerTimeDiff = 0.0f;
        this.avgServerTickDiff = 0.0f;
        this.fxaaShaders = new ShaderGroup[10];
        this.loadVisibleChunks = false;
        this.dLerp = 0.0;
        this.scrool = 0.0;
        this.shaderIndex = EntityRenderer.SHADER_COUNT;
        this.mc = mcIn;
        this.resourceManager = resourceManagerIn;
        this.itemRenderer = mcIn.getItemRenderer();
        this.mapItemRenderer = new MapItemRenderer(mcIn.getTextureManager());
        this.lightmapTexture = new DynamicTexture(16, 16);
        this.locationLightMap = mcIn.getTextureManager().getDynamicTextureLocation("lightMap", this.lightmapTexture);
        this.lightmapColors = this.lightmapTexture.getTextureData();
        this.shaderGroup = null;
        for (int i = 0; i < 32; ++i) {
            for (int j = 0; j < 32; ++j) {
                final float f = (float)(j - 16);
                final float f2 = (float)(i - 16);
                final float f3 = MathHelper.sqrt(f * f + f2 * f2);
                this.rainXCoords[i << 5 | j] = -f2 / f3;
                this.rainYCoords[i << 5 | j] = f / f3;
            }
        }
    }
    
    public boolean isShaderActive() {
        return OpenGlHelper.shadersSupported && this.shaderGroup != null;
    }
    
    public void stopUseShader() {
        if (this.shaderGroup != null) {
            this.shaderGroup.deleteShaderGroup();
        }
        this.shaderGroup = null;
        this.shaderIndex = EntityRenderer.SHADER_COUNT;
    }
    
    public void switchUseShader() {
        this.useShader = !this.useShader;
    }
    
    public void loadEntityShader(@Nullable final Entity entityIn) {
        if (OpenGlHelper.shadersSupported) {
            if (this.shaderGroup != null) {
                this.shaderGroup.deleteShaderGroup();
            }
            this.shaderGroup = null;
            if (entityIn instanceof EntityCreeper) {
                this.loadShader(new ResourceLocation("shaders/post/creeper.json"));
            }
            else if (entityIn instanceof EntitySpider) {
                this.loadShader(new ResourceLocation("shaders/post/spider.json"));
            }
            else if (entityIn instanceof EntityEnderman) {
                this.loadShader(new ResourceLocation("shaders/post/invert.json"));
            }
            else if (Reflector.ForgeHooksClient_loadEntityShader.exists()) {
                Reflector.call(Reflector.ForgeHooksClient_loadEntityShader, entityIn, this);
            }
        }
    }
    
    private void loadShader(final ResourceLocation resourceLocationIn) {
        if (OpenGlHelper.isFramebufferEnabled()) {
            try {
                (this.shaderGroup = new ShaderGroup(this.mc.getTextureManager(), this.resourceManager, this.mc.getFramebuffer(), resourceLocationIn)).createBindFramebuffers(this.mc.displayWidth, this.mc.displayHeight);
                this.useShader = true;
            }
            catch (IOException ioexception) {
                EntityRenderer.LOGGER.warn("Failed to load shader: {}", (Object)resourceLocationIn, (Object)ioexception);
                this.shaderIndex = EntityRenderer.SHADER_COUNT;
                this.useShader = false;
            }
            catch (JsonSyntaxException jsonsyntaxexception) {
                EntityRenderer.LOGGER.warn("Failed to load shader: {}", (Object)resourceLocationIn, (Object)jsonsyntaxexception);
                this.shaderIndex = EntityRenderer.SHADER_COUNT;
                this.useShader = false;
            }
        }
    }
    
    @Override
    public void onResourceManagerReload(final IResourceManager resourceManager) {
        if (this.shaderGroup != null) {
            this.shaderGroup.deleteShaderGroup();
        }
        this.shaderGroup = null;
        if (this.shaderIndex == EntityRenderer.SHADER_COUNT) {
            this.loadEntityShader(this.mc.getRenderViewEntity());
        }
        else {
            this.loadShader(EntityRenderer.SHADERS_TEXTURES[this.shaderIndex]);
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
            final float f = this.mc.gameSettings.mouseSensitivity * 0.6f + 0.2f;
            final float f2 = f * f * f * 8.0f;
            this.smoothCamFilterX = this.mouseFilterXAxis.smooth(this.smoothCamYaw, 0.05f * f2);
            this.smoothCamFilterY = this.mouseFilterYAxis.smooth(this.smoothCamPitch, 0.05f * f2);
            this.smoothCamPartialTicks = 0.0f;
            this.smoothCamYaw = 0.0f;
            this.smoothCamPitch = 0.0f;
        }
        else {
            this.smoothCamFilterX = 0.0f;
            this.smoothCamFilterY = 0.0f;
            this.mouseFilterXAxis.reset();
            this.mouseFilterYAxis.reset();
        }
        if (this.mc.getRenderViewEntity() == null) {
            final Minecraft mc = this.mc;
            final Minecraft mc2 = this.mc;
            mc.setRenderViewEntity(Minecraft.player);
        }
        final Entity entity = this.mc.getRenderViewEntity();
        final double d2 = entity.posX;
        final double d3 = entity.posY + entity.getEyeHeight();
        final double d4 = entity.posZ;
        final float f3 = this.mc.world.getLightBrightness(new BlockPos(d2, d3, d4));
        float f4 = this.mc.gameSettings.renderDistanceChunks / 16.0f;
        f4 = MathHelper.clamp(f4, 0.0f, 1.0f);
        final float f5 = f3 * (1.0f - f4) + f4;
        this.fogColor1 += (f5 - this.fogColor1) * 0.1f;
        ++this.rendererUpdateCount;
        this.itemRenderer.updateEquippedItem();
        this.addRainParticles();
        this.bossColorModifierPrev = this.bossColorModifier;
        if (this.mc.ingameGUI.getBossOverlay().shouldDarkenSky()) {
            this.bossColorModifier += 0.05f;
            if (this.bossColorModifier > 1.0f) {
                this.bossColorModifier = 1.0f;
            }
        }
        else if (this.bossColorModifier > 0.0f) {
            this.bossColorModifier -= 0.0125f;
        }
        if (this.itemActivationTicks > 0) {
            --this.itemActivationTicks;
            if (this.itemActivationTicks == 0) {
                this.itemActivationItem = null;
            }
        }
    }
    
    public ShaderGroup getShaderGroup() {
        return this.shaderGroup;
    }
    
    public void updateShaderGroupSize(final int width, final int height) {
        if (OpenGlHelper.shadersSupported) {
            if (this.shaderGroup != null) {
                this.shaderGroup.createBindFramebuffers(width, height);
            }
            this.mc.renderGlobal.createBindEntityOutlineFbs(width, height);
        }
    }
    
    public void getMouseOver(final float partialTicks) {
        final Entity entity = this.mc.getRenderViewEntity();
        if (entity != null && this.mc.world != null) {
            this.mc.profiler.startSection("pick");
            this.mc.pointedEntity = null;
            double d0 = this.mc.playerController.getBlockReachDistance();
            this.mc.objectMouseOver = entity.rayTrace(d0, partialTicks);
            final Vec3d vec3d = entity.getPositionEyes(partialTicks);
            boolean flag = false;
            final int i = 3;
            double d2 = d0;
            if (this.mc.playerController.extendedReach()) {
                d2 = (d0 = 6.0);
            }
            else if (d0 > 3.0) {
                flag = true;
            }
            if (this.mc.objectMouseOver != null) {
                d2 = this.mc.objectMouseOver.hitVec.distanceTo(vec3d);
            }
            final Vec3d vec3d2 = entity.getLook(1.0f);
            final Vec3d vec3d3 = vec3d.add(vec3d2.x * d0, vec3d2.y * d0, vec3d2.z * d0);
            this.pointedEntity = null;
            Vec3d vec3d4 = null;
            final float f = 1.0f;
            final List<Entity> list = this.mc.world.getEntitiesInAABBexcluding(entity, entity.getEntityBoundingBox().expand(vec3d2.x * d0, vec3d2.y * d0, vec3d2.z * d0).grow(1.0, 1.0, 1.0), (Predicate<? super Entity>)Predicates.and((Predicate)EntitySelectors.NOT_SPECTATING, (Predicate)new Predicate<Entity>() {
                public boolean apply(@Nullable final Entity p_apply_1_) {
                    return p_apply_1_ != null && p_apply_1_.canBeCollidedWith();
                }
            }));
            double d3 = d2;
            for (int j = 0; j < list.size(); ++j) {
                final Entity entity2 = list.get(j);
                final AxisAlignedBB axisalignedbb = entity2.getEntityBoundingBox().grow(entity2.getCollisionBorderSize());
                final RayTraceResult raytraceresult = axisalignedbb.calculateIntercept(vec3d, vec3d3);
                if (axisalignedbb.contains(vec3d)) {
                    if (d3 >= 0.0) {
                        this.pointedEntity = entity2;
                        vec3d4 = ((raytraceresult == null) ? vec3d : raytraceresult.hitVec);
                        d3 = 0.0;
                    }
                }
                else if (raytraceresult != null) {
                    final double d4 = vec3d.distanceTo(raytraceresult.hitVec);
                    if (d4 < d3 || d3 == 0.0) {
                        boolean flag2 = false;
                        if (Reflector.ForgeEntity_canRiderInteract.exists()) {
                            flag2 = Reflector.callBoolean(entity2, Reflector.ForgeEntity_canRiderInteract, new Object[0]);
                        }
                        if (!flag2 && entity2.getLowestRidingEntity() == entity.getLowestRidingEntity()) {
                            if (d3 == 0.0) {
                                this.pointedEntity = entity2;
                                vec3d4 = raytraceresult.hitVec;
                            }
                        }
                        else {
                            this.pointedEntity = entity2;
                            vec3d4 = raytraceresult.hitVec;
                            d3 = d4;
                        }
                    }
                }
            }
            if (this.pointedEntity != null && flag && vec3d.distanceTo(vec3d4) > 3.0) {
                this.pointedEntity = null;
                this.mc.objectMouseOver = new RayTraceResult(RayTraceResult.Type.MISS, vec3d4, null, new BlockPos(vec3d4));
            }
            if (this.pointedEntity != null && (d3 < d2 || this.mc.objectMouseOver == null)) {
                this.mc.objectMouseOver = new RayTraceResult(this.pointedEntity, vec3d4);
                if (this.pointedEntity instanceof EntityLivingBase || this.pointedEntity instanceof EntityItemFrame) {
                    this.mc.pointedEntity = this.pointedEntity;
                }
            }
            this.mc.profiler.endSection();
        }
    }
    
    private void updateFovModifierHand() {
        float f = 1.0f;
        if (this.mc.getRenderViewEntity() instanceof AbstractClientPlayer) {
            final AbstractClientPlayer abstractclientplayer = (AbstractClientPlayer)this.mc.getRenderViewEntity();
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
    
    private float getFOVModifier(final float partialTicks, final boolean useFOVSetting) {
        if (this.debugView) {
            return 90.0f;
        }
        final Entity entity = this.mc.getRenderViewEntity();
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
        if (flag) {
            this.a = MathUtility.lerp(this.a, 6.0f, (float)(AnimationMath.deltaTime() * 3.0));
        }
        else {
            this.a = MathUtility.lerp(this.a, 1.0f, (float)(AnimationMath.deltaTime() * 3.0));
        }
        f /= this.a;
        if (flag) {
            if (!Config.zoomMode) {
                Config.zoomMode = true;
                Config.zoomSmoothCamera = this.mc.gameSettings.smoothCamera;
                this.mc.gameSettings.smoothCamera = true;
                this.mc.renderGlobal.displayListEntitiesDirty = true;
            }
        }
        else if (Config.zoomMode) {
            Config.zoomMode = false;
            this.mc.gameSettings.smoothCamera = Config.zoomSmoothCamera;
            this.mouseFilterXAxis = new MouseFilter();
            this.mouseFilterYAxis = new MouseFilter();
            this.mc.renderGlobal.displayListEntitiesDirty = true;
        }
        if (entity instanceof EntityLivingBase && ((EntityLivingBase)entity).getHealth() <= 0.0f) {
            final float f2 = ((EntityLivingBase)entity).deathTime + partialTicks;
            f /= (1.0f - 500.0f / (f2 + 500.0f)) * 2.0f + 1.0f;
        }
        final IBlockState iblockstate = ActiveRenderInfo.getBlockStateAtEntityViewpoint(this.mc.world, entity, partialTicks);
        if (iblockstate.getMaterial() == Material.WATER) {
            f = f * 60.0f / 70.0f;
        }
        return Reflector.ForgeHooksClient_getFOVModifier.exists() ? Reflector.callFloat(Reflector.ForgeHooksClient_getFOVModifier, this, entity, iblockstate, partialTicks, f) : f;
    }
    
    private void applyBobbing(final float partialTicks) {
        if (this.mc.getRenderViewEntity() instanceof EntityPlayer) {
            final EntityPlayer entityplayer = (EntityPlayer)this.mc.getRenderViewEntity();
            final float f = entityplayer.distanceWalkedModified - entityplayer.prevDistanceWalkedModified;
            final float f2 = -(entityplayer.distanceWalkedModified + f * partialTicks);
            final float f3 = entityplayer.prevCameraYaw + (entityplayer.cameraYaw - entityplayer.prevCameraYaw) * partialTicks;
            final float f4 = entityplayer.prevCameraPitch + (entityplayer.cameraPitch - entityplayer.prevCameraPitch) * partialTicks;
            GlStateManager.translate(MathHelper.sin(f2 * 3.1415927f) * f3 * 0.5f, -Math.abs(MathHelper.cos(f2 * 3.1415927f) * f3), 0.0f);
            GlStateManager.rotate(MathHelper.sin(f2 * 3.1415927f) * f3 * 3.0f, 0.0f, 0.0f, 1.0f);
            GlStateManager.rotate(Math.abs(MathHelper.cos(f2 * 3.1415927f - 0.2f) * f3) * 5.0f, 1.0f, 0.0f, 0.0f);
            GlStateManager.rotate(f4, 1.0f, 0.0f, 0.0f);
        }
    }
    
    public void orientCamera(final float partialTicks) {
        final Entity entity = this.mc.getRenderViewEntity();
        float f = entity.getEyeHeight();
        double d0 = entity.prevPosX + (entity.posX - entity.prevPosX) * partialTicks;
        double d2 = entity.prevPosY + (entity.posY - entity.prevPosY) * partialTicks + f;
        double d3 = entity.prevPosZ + (entity.posZ - entity.prevPosZ) * partialTicks;
        if (entity instanceof EntityLivingBase && ((EntityLivingBase)entity).isPlayerSleeping()) {
            ++f;
            GlStateManager.translate(0.0f, 0.3f, 0.0f);
            if (!this.mc.gameSettings.debugCamEnable) {
                final BlockPos blockpos = new BlockPos(entity);
                final IBlockState iblockstate = this.mc.world.getBlockState(blockpos);
                final Block block = iblockstate.getBlock();
                if (Reflector.ForgeHooksClient_orientBedCamera.exists()) {
                    Reflector.callVoid(Reflector.ForgeHooksClient_orientBedCamera, this.mc.world, blockpos, iblockstate, entity);
                }
                else if (block == Blocks.BED) {
                    final int j = iblockstate.getValue((IProperty<EnumFacing>)BlockBed.FACING).getHorizontalIndex();
                    GlStateManager.rotate((float)(j * 90), 0.0f, 1.0f, 0.0f);
                }
                GlStateManager.rotate(entity.prevRotationYaw + (entity.rotationYaw - entity.prevRotationYaw) * partialTicks + 180.0f, 0.0f, -1.0f, 0.0f);
                GlStateManager.rotate(entity.prevRotationPitch + (entity.rotationPitch - entity.prevRotationPitch) * partialTicks, -1.0f, 0.0f, 0.0f);
            }
        }
        else if (this.mc.gameSettings.thirdPersonView > 0) {
            double d4 = this.thirdPersonDistancePrev - 0.5;
            if (this.mc.gameSettings.debugCamEnable) {
                GlStateManager.translate(0.0f, 0.0f, (float)(-d4));
            }
            else {
                final float f2 = entity.rotationYaw;
                float f3 = entity.rotationPitch;
                if (this.mc.gameSettings.thirdPersonView == 2) {
                    f3 += 180.0f;
                }
                final double d5 = -MathHelper.sin(f2 * 0.017453292f) * MathHelper.cos(f3 * 0.017453292f) * d4;
                final double d6 = MathHelper.cos(f2 * 0.017453292f) * MathHelper.cos(f3 * 0.017453292f) * d4;
                final double d7 = -MathHelper.sin(f3 * 0.017453292f) * d4;
                for (int i = 0; i < 8; ++i) {
                    float f4 = (float)((i & 0x1) * 2 - 1);
                    float f5 = (float)((i >> 1 & 0x1) * 2 - 1);
                    float f6 = (float)((i >> 2 & 0x1) * 2 - 1);
                    f4 *= 0.1f;
                    f5 *= 0.1f;
                    f6 *= 0.1f;
                    final RayTraceResult raytraceresult = this.mc.world.rayTraceBlocks(new Vec3d(d0 + f4, d2 + f5, d3 + f6), new Vec3d(d0 - d5 + f4 + f6, d2 - d7 + f5, d3 - d6 + f6));
                    if (raytraceresult != null) {
                        final double d8 = raytraceresult.hitVec.distanceTo(new Vec3d(d0, d2, d3));
                        if (!Minced.getInstance().manager.getModule(CameraClip.class).state) {
                            if (d8 < d4) {
                                d4 = d8;
                            }
                        }
                    }
                }
                if (this.mc.gameSettings.thirdPersonView == 2) {
                    GlStateManager.rotate(180.0f, 0.0f, 1.0f, 0.0f);
                }
                GlStateManager.rotate(entity.rotationPitch - f3, 1.0f, 0.0f, 0.0f);
                GlStateManager.rotate(entity.rotationYaw - f2, 0.0f, 1.0f, 0.0f);
                GlStateManager.translate(0.0f, 0.0f, (float)(-d4));
                GlStateManager.rotate(f2 - entity.rotationYaw, 0.0f, 1.0f, 0.0f);
                GlStateManager.rotate(f3 - entity.rotationPitch, 1.0f, 0.0f, 0.0f);
            }
        }
        else {
            GlStateManager.translate(0.0f, 0.0f, 0.05f);
        }
        if (Reflector.EntityViewRenderEvent_CameraSetup_Constructor.exists()) {
            if (!this.mc.gameSettings.debugCamEnable) {
                float f7 = entity.prevRotationYaw + (entity.rotationYaw - entity.prevRotationYaw) * partialTicks + 180.0f;
                float f8 = entity.prevRotationPitch + (entity.rotationPitch - entity.prevRotationPitch) * partialTicks;
                float f9 = 0.0f;
                if (entity instanceof EntityAnimal) {
                    final EntityAnimal entityanimal1 = (EntityAnimal)entity;
                    f7 = entityanimal1.prevRotationYawHead + (entityanimal1.rotationYawHead - entityanimal1.prevRotationYawHead) * partialTicks + 180.0f;
                }
                final IBlockState iblockstate2 = ActiveRenderInfo.getBlockStateAtEntityViewpoint(this.mc.world, entity, partialTicks);
                final Object object = Reflector.newInstance(Reflector.EntityViewRenderEvent_CameraSetup_Constructor, this, entity, iblockstate2, partialTicks, f7, f8, f9);
                Reflector.postForgeBusEvent(object);
                f9 = Reflector.callFloat(object, Reflector.EntityViewRenderEvent_CameraSetup_getRoll, new Object[0]);
                f8 = Reflector.callFloat(object, Reflector.EntityViewRenderEvent_CameraSetup_getPitch, new Object[0]);
                f7 = Reflector.callFloat(object, Reflector.EntityViewRenderEvent_CameraSetup_getYaw, new Object[0]);
                GlStateManager.rotate(f9, 0.0f, 0.0f, 1.0f);
                GlStateManager.rotate(f8, 1.0f, 0.0f, 0.0f);
                GlStateManager.rotate(f7, 0.0f, 1.0f, 0.0f);
            }
        }
        else if (!this.mc.gameSettings.debugCamEnable) {
            GlStateManager.rotate(entity.prevRotationPitch + (entity.rotationPitch - entity.prevRotationPitch) * partialTicks, 1.0f, 0.0f, 0.0f);
            if (entity instanceof EntityAnimal) {
                final EntityAnimal entityanimal2 = (EntityAnimal)entity;
                GlStateManager.rotate(entityanimal2.prevRotationYawHead + (entityanimal2.rotationYawHead - entityanimal2.prevRotationYawHead) * partialTicks + 180.0f, 0.0f, 1.0f, 0.0f);
            }
            else {
                GlStateManager.rotate(entity.prevRotationYaw + (entity.rotationYaw - entity.prevRotationYaw) * partialTicks + 180.0f, 0.0f, 1.0f, 0.0f);
            }
        }
        GlStateManager.translate(0.0f, -f, 0.0f);
        d0 = entity.prevPosX + (entity.posX - entity.prevPosX) * partialTicks;
        d2 = entity.prevPosY + (entity.posY - entity.prevPosY) * partialTicks + f;
        d3 = entity.prevPosZ + (entity.posZ - entity.prevPosZ) * partialTicks;
        this.cloudFog = this.mc.renderGlobal.hasCloudFog(d0, d2, d3, partialTicks);
    }
    
    public void setupCameraTransform(final float partialTicks, final int pass) {
        this.farPlaneDistance = (float)(this.mc.gameSettings.renderDistanceChunks * 16);
        if (Config.isFogFancy()) {
            this.farPlaneDistance *= 0.95f;
        }
        if (Config.isFogFast()) {
            this.farPlaneDistance *= 0.83f;
        }
        GlStateManager.matrixMode(5889);
        GlStateManager.loadIdentity();
        final float f = 0.07f;
        if (this.mc.gameSettings.anaglyph) {
            GlStateManager.translate(-(pass * 2 - 1) * 0.07f, 0.0f, 0.0f);
        }
        this.clipDistance = this.farPlaneDistance * 2.0f;
        if (this.clipDistance < 173.0f) {
            this.clipDistance = 173.0f;
        }
        if (this.cameraZoom != 1.0) {
            GlStateManager.translate((float)this.cameraYaw, (float)(-this.cameraPitch), 0.0f);
            GlStateManager.scale(this.cameraZoom, this.cameraZoom, 1.0);
        }
        Project.gluPerspective(this.getFOVModifier(partialTicks, true), this.mc.displayWidth / (float)this.mc.displayHeight, 0.05f, this.clipDistance);
        GlStateManager.matrixMode(5888);
        GlStateManager.loadIdentity();
        if (this.mc.gameSettings.anaglyph) {
            GlStateManager.translate((pass * 2 - 1) * 0.1f, 0.0f, 0.0f);
        }
        if (this.mc.gameSettings.viewBobbing) {
            this.applyBobbing(partialTicks);
        }
        final Minecraft mc = this.mc;
        final float prevTimeInPortal = Minecraft.player.prevTimeInPortal;
        final Minecraft mc2 = this.mc;
        final float timeInPortal = Minecraft.player.timeInPortal;
        final Minecraft mc3 = this.mc;
        final float f2 = prevTimeInPortal + (timeInPortal - Minecraft.player.prevTimeInPortal) * partialTicks;
        if (f2 > 0.0f) {
            int i = 20;
            final Minecraft mc4 = this.mc;
            if (Minecraft.player.isPotionActive(MobEffects.NAUSEA)) {
                i = 7;
            }
            float f3 = 5.0f / (f2 * f2 + 5.0f) - f2 * 0.04f;
            f3 *= f3;
            GlStateManager.rotate((this.rendererUpdateCount + partialTicks) * i, 0.0f, 1.0f, 1.0f);
            GlStateManager.scale(1.0f / f3, 1.0f, 1.0f);
            GlStateManager.rotate(-(this.rendererUpdateCount + partialTicks) * i, 0.0f, 1.0f, 1.0f);
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
                    break;
                }
            }
        }
    }
    
    private void renderHand(final float partialTicks, final int pass) {
        this.renderHand(partialTicks, pass, true, true, false);
    }
    
    public void renderHand(final float p_renderHand_1_, final int p_renderHand_2_, final boolean p_renderHand_3_, final boolean p_renderHand_4_, final boolean p_renderHand_5_) {
        if (!this.debugView) {
            GlStateManager.matrixMode(5889);
            GlStateManager.loadIdentity();
            final float f = 0.07f;
            if (this.mc.gameSettings.anaglyph) {
                GlStateManager.translate(-(p_renderHand_2_ * 2 - 1) * 0.07f, 0.0f, 0.0f);
            }
            if (Config.isShaders()) {
                Shaders.applyHandDepth();
            }
            Project.gluPerspective(this.getFOVModifier(p_renderHand_1_, false), this.mc.displayWidth / (float)this.mc.displayHeight, 0.05f, this.farPlaneDistance * 2.0f);
            GlStateManager.matrixMode(5888);
            GlStateManager.loadIdentity();
            if (this.mc.gameSettings.anaglyph) {
                GlStateManager.translate((p_renderHand_2_ * 2 - 1) * 0.1f, 0.0f, 0.0f);
            }
            boolean flag = false;
            if (p_renderHand_3_) {
                GlStateManager.pushMatrix();
                if (this.mc.gameSettings.viewBobbing) {
                    this.applyBobbing(p_renderHand_1_);
                }
                flag = (this.mc.getRenderViewEntity() instanceof EntityLivingBase && ((EntityLivingBase)this.mc.getRenderViewEntity()).isPlayerSleeping());
                final boolean flag2 = !ReflectorForge.renderFirstPersonHand(this.mc.renderGlobal, p_renderHand_1_, p_renderHand_2_);
                if (flag2 && this.mc.gameSettings.thirdPersonView == 0 && !flag && !this.mc.gameSettings.hideGUI && !this.mc.playerController.isSpectator()) {
                    this.enableLightmap();
                    if (Config.isShaders()) {
                        ShadersRender.renderItemFP(this.itemRenderer, p_renderHand_1_, p_renderHand_5_);
                    }
                    else {
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
            }
            if (this.mc.gameSettings.viewBobbing) {
                this.applyBobbing(p_renderHand_1_);
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
        final float f = 0.00390625f;
        GlStateManager.scale(0.00390625f, 0.00390625f, 0.00390625f);
        GlStateManager.translate(8.0f, 8.0f, 8.0f);
        GlStateManager.matrixMode(5888);
        this.mc.getTextureManager().bindTexture(this.locationLightMap);
        GlStateManager.glTexParameteri(3553, 10241, 9729);
        GlStateManager.glTexParameteri(3553, 10240, 9729);
        GlStateManager.glTexParameteri(3553, 10242, 33071);
        GlStateManager.glTexParameteri(3553, 10243, 33071);
        GlStateManager.color(1.0f, 1.0f, 1.0f, 1.0f);
        GlStateManager.enableTexture2D();
        GlStateManager.setActiveTexture(OpenGlHelper.defaultTexUnit);
        if (Config.isShaders()) {
            Shaders.enableLightmap();
        }
    }
    
    private void updateTorchFlicker() {
        this.torchFlickerDX += (float)((Math.random() - Math.random()) * Math.random() * Math.random());
        this.torchFlickerDX *= (float)0.9;
        this.torchFlickerX += this.torchFlickerDX - this.torchFlickerX;
        this.lightmapUpdateNeeded = true;
    }
    
    private void updateLightmap(final float partialTicks) {
        if (this.lightmapUpdateNeeded) {
            this.mc.profiler.startSection("lightTex");
            final World world = this.mc.world;
            if (world != null) {
                if (Config.isCustomColors()) {
                    final World world2 = world;
                    final float torchFlickerX = this.torchFlickerX;
                    final int[] lightmapColors = this.lightmapColors;
                    final Minecraft mc = this.mc;
                    if (CustomColors.updateLightmap(world2, torchFlickerX, lightmapColors, Minecraft.player.isPotionActive(MobEffects.NIGHT_VISION), partialTicks)) {
                        this.lightmapTexture.updateDynamicTexture();
                        this.lightmapUpdateNeeded = false;
                        this.mc.profiler.endSection();
                        return;
                    }
                }
                final float f = world.getSunBrightness(1.0f);
                final float f2 = f * 0.95f + 0.05f;
                for (int i = 0; i < 256; ++i) {
                    float f3 = world.provider.getLightBrightnessTable()[i / 16] * f2;
                    final float f4 = world.provider.getLightBrightnessTable()[i % 16] * (this.torchFlickerX * 0.1f + 1.5f);
                    if (world.getLastLightningBolt() > 0) {
                        f3 = world.provider.getLightBrightnessTable()[i / 16];
                    }
                    final float f5 = f3 * (f * 0.65f + 0.35f);
                    final float f6 = f3 * (f * 0.65f + 0.35f);
                    final float f7 = f4 * ((f4 * 0.6f + 0.4f) * 0.6f + 0.4f);
                    final float f8 = f4 * (f4 * f4 * 0.6f + 0.4f);
                    float f9 = f5 + f4;
                    float f10 = f6 + f7;
                    float f11 = f3 + f8;
                    f9 = f9 * 0.96f + 0.03f;
                    f10 = f10 * 0.96f + 0.03f;
                    f11 = f11 * 0.96f + 0.03f;
                    if (this.bossColorModifier > 0.0f) {
                        final float f12 = this.bossColorModifierPrev + (this.bossColorModifier - this.bossColorModifierPrev) * partialTicks;
                        f9 = f9 * (1.0f - f12) + f9 * 0.7f * f12;
                        f10 = f10 * (1.0f - f12) + f10 * 0.6f * f12;
                        f11 = f11 * (1.0f - f12) + f11 * 0.6f * f12;
                    }
                    if (world.provider.getDimensionType().getId() == 1) {
                        f9 = 0.22f + f4 * 0.75f;
                        f10 = 0.28f + f7 * 0.75f;
                        f11 = 0.25f + f8 * 0.75f;
                    }
                    if (Reflector.ForgeWorldProvider_getLightmapColors.exists()) {
                        final float[] afloat = { f9, f10, f11 };
                        Reflector.call(world.provider, Reflector.ForgeWorldProvider_getLightmapColors, partialTicks, f, f3, f4, afloat);
                        f9 = afloat[0];
                        f10 = afloat[1];
                        f11 = afloat[2];
                    }
                    f9 = MathHelper.clamp(f9, 0.0f, 1.0f);
                    f10 = MathHelper.clamp(f10, 0.0f, 1.0f);
                    f11 = MathHelper.clamp(f11, 0.0f, 1.0f);
                    final Minecraft mc2 = this.mc;
                    if (Minecraft.player.isPotionActive(MobEffects.NIGHT_VISION)) {
                        final Minecraft mc3 = this.mc;
                        final float f13 = this.getNightVisionBrightness(Minecraft.player, partialTicks);
                        float f14 = 1.0f / f9;
                        if (f14 > 1.0f / f10) {
                            f14 = 1.0f / f10;
                        }
                        if (f14 > 1.0f / f11) {
                            f14 = 1.0f / f11;
                        }
                        f9 = f9 * (1.0f - f13) + f9 * f14 * f13;
                        f10 = f10 * (1.0f - f13) + f10 * f14 * f13;
                        f11 = f11 * (1.0f - f13) + f11 * f14 * f13;
                    }
                    if (f9 > 1.0f) {
                        f9 = 1.0f;
                    }
                    if (f10 > 1.0f) {
                        f10 = 1.0f;
                    }
                    if (f11 > 1.0f) {
                        f11 = 1.0f;
                    }
                    final float f15 = this.mc.gameSettings.gammaSetting;
                    float f16 = 1.0f - f9;
                    float f17 = 1.0f - f10;
                    float f18 = 1.0f - f11;
                    f16 = 1.0f - f16 * f16 * f16 * f16;
                    f17 = 1.0f - f17 * f17 * f17 * f17;
                    f18 = 1.0f - f18 * f18 * f18 * f18;
                    f9 = f9 * (1.0f - f15) + f16 * f15;
                    f10 = f10 * (1.0f - f15) + f17 * f15;
                    f11 = f11 * (1.0f - f15) + f18 * f15;
                    f9 = f9 * 0.96f + 0.03f;
                    f10 = f10 * 0.96f + 0.03f;
                    f11 = f11 * 0.96f + 0.03f;
                    if (f9 > 1.0f) {
                        f9 = 1.0f;
                    }
                    if (f10 > 1.0f) {
                        f10 = 1.0f;
                    }
                    if (f11 > 1.0f) {
                        f11 = 1.0f;
                    }
                    if (f9 < 0.0f) {
                        f9 = 0.0f;
                    }
                    if (f10 < 0.0f) {
                        f10 = 0.0f;
                    }
                    if (f11 < 0.0f) {
                        f11 = 0.0f;
                    }
                    final int j = 255;
                    final int k = (int)(f9 * 255.0f);
                    final int l = (int)(f10 * 255.0f);
                    final int i2 = (int)(f11 * 255.0f);
                    if (Minced.getInstance().manager.getModule(WorldColor.class).state) {
                        this.lightmapColors[i] = ((WorldColor)Minced.getInstance().manager.getModule(WorldColor.class)).color.getColorValue();
                    }
                    else {
                        this.lightmapColors[i] = (0xFF000000 | k << 16 | l << 8 | i2);
                    }
                }
                this.lightmapTexture.updateDynamicTexture();
                this.lightmapUpdateNeeded = false;
                this.mc.profiler.endSection();
            }
        }
    }
    
    public float getNightVisionBrightness(final EntityLivingBase entitylivingbaseIn, final float partialTicks) {
        final int i = entitylivingbaseIn.getActivePotionEffect(MobEffects.NIGHT_VISION).getDuration();
        return (i > 200) ? 1.0f : (0.7f + MathHelper.sin((i - partialTicks) * 3.1415927f * 0.2f) * 0.3f);
    }
    
    public void updateCameraAndRender(final float partialTicks, final long nanoTime) {
        this.frameInit();
        final boolean flag = Display.isActive();
        if (!flag && this.mc.gameSettings.pauseOnLostFocus && (!this.mc.gameSettings.touchscreen || !Mouse.isButtonDown(1))) {
            if (Minecraft.getSystemTime() - this.prevFrameTime > 500L) {
                this.mc.displayInGameMenu();
            }
        }
        else {
            this.prevFrameTime = Minecraft.getSystemTime();
        }
        this.mc.profiler.startSection("mouse");
        if (flag && Minecraft.IS_RUNNING_ON_MAC && this.mc.inGameHasFocus && !Mouse.isInsideWindow()) {
            Mouse.setGrabbed(false);
            Mouse.setCursorPosition(Display.getWidth() / 2, Display.getHeight() / 2 - 20);
            Mouse.setGrabbed(true);
        }
        if (this.mc.inGameHasFocus && flag) {
            this.mc.mouseHelper.mouseXYChange();
            this.mc.getTutorial().handleMouse(this.mc.mouseHelper);
            final float f = this.mc.gameSettings.mouseSensitivity * 0.6f + 0.2f;
            final float f2 = f * f * f * 8.0f;
            float f3 = this.mc.mouseHelper.deltaX * f2;
            float f4 = this.mc.mouseHelper.deltaY * f2;
            int i = 1;
            if (this.mc.gameSettings.invertMouse) {
                i = -1;
            }
            if (this.mc.gameSettings.smoothCamera) {
                this.smoothCamYaw += f3;
                this.smoothCamPitch += f4;
                final float f5 = partialTicks - this.smoothCamPartialTicks;
                this.smoothCamPartialTicks = partialTicks;
                f3 = this.smoothCamFilterX * f5;
                f4 = this.smoothCamFilterY * f5;
                final Minecraft mc = this.mc;
                Minecraft.player.turn(f3, f4 * i);
            }
            else {
                this.smoothCamYaw = 0.0f;
                this.smoothCamPitch = 0.0f;
                final Minecraft mc2 = this.mc;
                Minecraft.player.turn(f3, f4 * i);
            }
        }
        this.mc.profiler.endSection();
        if (!this.mc.skipRenderWorld) {
            EntityRenderer.anaglyphEnable = this.mc.gameSettings.anaglyph;
            final ScaledResolution scaledresolution = new ScaledResolution(this.mc);
            final int i2 = scaledresolution.getScaledWidth();
            final int j1 = scaledresolution.getScaledHeight();
            final int k1 = Mouse.getX() * i2 / this.mc.displayWidth;
            final int l1 = j1 - Mouse.getY() * j1 / this.mc.displayHeight - 1;
            final int i3 = this.mc.gameSettings.limitFramerate;
            if (this.mc.world == null) {
                GlStateManager.viewport(0, 0, this.mc.displayWidth, this.mc.displayHeight);
                GlStateManager.matrixMode(5889);
                GlStateManager.loadIdentity();
                GlStateManager.matrixMode(5888);
                GlStateManager.loadIdentity();
                this.setupOverlayRendering();
                this.renderEndNanoTime = System.nanoTime();
                TileEntityRendererDispatcher.instance.renderEngine = this.mc.getTextureManager();
                TileEntityRendererDispatcher.instance.fontRenderer = this.mc.fontRenderer;
            }
            else {
                this.mc.profiler.startSection("level");
                int m = Math.min(Minecraft.getDebugFPS(), i3);
                m = Math.max(m, 60);
                final long k2 = System.nanoTime() - nanoTime;
                final long l2 = Math.max(1000000000 / m / 4 - k2, 0L);
                this.renderWorld(partialTicks, System.nanoTime() + l2);
                if (this.mc.isSingleplayer() && this.timeWorldIcon < Minecraft.getSystemTime() - 1000L) {
                    this.timeWorldIcon = Minecraft.getSystemTime();
                    if (!this.mc.getIntegratedServer().isWorldIconSet()) {
                        this.createWorldIcon();
                    }
                }
                if (OpenGlHelper.shadersSupported) {
                    this.mc.renderGlobal.renderEntityOutlineFramebuffer();
                    if (this.shaderGroup != null && this.useShader) {
                        GlStateManager.matrixMode(5890);
                        GlStateManager.pushMatrix();
                        GlStateManager.loadIdentity();
                        this.shaderGroup.render(partialTicks);
                        GlStateManager.popMatrix();
                    }
                    this.mc.getFramebuffer().bindFramebuffer(true);
                }
                this.renderEndNanoTime = System.nanoTime();
                this.mc.profiler.endStartSection("gui");
                if (!this.mc.gameSettings.hideGUI || this.mc.currentScreen != null) {
                    GlStateManager.alphaFunc(516, 0.1f);
                    this.setupOverlayRendering();
                    this.renderItemActivation(i2, j1, partialTicks);
                    this.mc.ingameGUI.renderGameOverlay(partialTicks);
                    if (this.mc.gameSettings.ofShowFps && !this.mc.gameSettings.showDebugInfo) {
                        Config.drawFps();
                    }
                    if (this.mc.gameSettings.showDebugInfo) {
                        Lagometer.showLagometer(scaledresolution);
                    }
                }
                this.mc.profiler.endSection();
            }
            if (this.mc.currentScreen != null) {
                GlStateManager.clear(256);
                try {
                    if (Reflector.ForgeHooksClient_drawScreen.exists()) {
                        Reflector.callVoid(Reflector.ForgeHooksClient_drawScreen, this.mc.currentScreen, k1, l1, this.mc.getTickLength());
                    }
                    else {
                        this.mc.currentScreen.drawScreen(k1, l1, this.mc.getTickLength());
                    }
                }
                catch (Throwable throwable1) {
                    final CrashReport crashreport = CrashReport.makeCrashReport(throwable1, "Rendering screen");
                    final CrashReportCategory crashreportcategory = crashreport.makeCategory("Screen render details");
                    crashreportcategory.addDetail("Screen name", new ICrashReportDetail<String>() {
                        @Override
                        public String call() throws Exception {
                            return EntityRenderer.this.mc.currentScreen.getClass().getCanonicalName();
                        }
                    });
                    crashreportcategory.addDetail("Mouse location", new ICrashReportDetail<String>() {
                        @Override
                        public String call() throws Exception {
                            return String.format("Scaled: (%d, %d). Absolute: (%d, %d)", k1, l1, Mouse.getX(), Mouse.getY());
                        }
                    });
                    crashreportcategory.addDetail("Screen size", new ICrashReportDetail<String>() {
                        @Override
                        public String call() throws Exception {
                            final String format = "Scaled: (%d, %d). Absolute: (%d, %d). Scale factor of %d";
                            final Object[] args = { scaledresolution.getScaledWidth(), scaledresolution.getScaledHeight(), EntityRenderer.this.mc.displayWidth, EntityRenderer.this.mc.displayHeight, null };
                            final int n = 4;
                            final ScaledResolution val$scaledresolution = scaledresolution;
                            args[n] = ScaledResolution.getScaleFactor();
                            return String.format(format, args);
                        }
                    });
                    throw new ReportedException(crashreport);
                }
            }
        }
        this.frameFinish();
        this.waitForServerThread();
        MemoryMonitor.update();
        Lagometer.updateLagometer();
        if (this.mc.gameSettings.ofProfiler) {
            this.mc.gameSettings.showDebugProfilerChart = true;
        }
    }
    
    private void createWorldIcon() {
        if (this.mc.renderGlobal.getRenderedChunks() > 10 && this.mc.renderGlobal.hasNoChunkUpdates() && !this.mc.getIntegratedServer().isWorldIconSet()) {
            final BufferedImage bufferedimage = ScreenShotHelper.createScreenshot(this.mc.displayWidth, this.mc.displayHeight, this.mc.getFramebuffer());
            int i = bufferedimage.getWidth();
            final int j = bufferedimage.getHeight();
            int k = 0;
            int l = 0;
            if (i > j) {
                k = (i - j) / 2;
                i = j;
            }
            else {
                l = (j - i) / 2;
            }
            try {
                final BufferedImage bufferedimage2 = new BufferedImage(64, 64, 1);
                final Graphics graphics = bufferedimage2.createGraphics();
                graphics.drawImage(bufferedimage, 0, 0, 64, 64, k, l, k + i, l + i, null);
                graphics.dispose();
                ImageIO.write(bufferedimage2, "png", this.mc.getIntegratedServer().getWorldIconFile());
            }
            catch (IOException ioexception1) {
                EntityRenderer.LOGGER.warn("Couldn't save auto screenshot", (Throwable)ioexception1);
            }
        }
    }
    
    public void renderStreamIndicator(final float partialTicks) {
        this.setupOverlayRendering();
    }
    
    private boolean isDrawBlockOutline() {
        if (!this.drawBlockOutline) {
            return false;
        }
        final Entity entity = this.mc.getRenderViewEntity();
        boolean flag = entity instanceof EntityPlayer && !this.mc.gameSettings.hideGUI;
        if (flag && !((EntityPlayer)entity).capabilities.allowEdit) {
            final ItemStack itemstack = ((EntityPlayer)entity).getHeldItemMainhand();
            if (this.mc.objectMouseOver != null && this.mc.objectMouseOver.typeOfHit == RayTraceResult.Type.BLOCK) {
                final BlockPos blockpos = this.mc.objectMouseOver.getBlockPos();
                final IBlockState iblockstate = this.mc.world.getBlockState(blockpos);
                final Block block = iblockstate.getBlock();
                if (this.mc.playerController.getCurrentGameType() == GameType.SPECTATOR) {
                    flag = (ReflectorForge.blockHasTileEntity(iblockstate) && this.mc.world.getTileEntity(blockpos) instanceof IInventory);
                }
                else {
                    flag = (!itemstack.isEmpty() && (itemstack.canDestroy(block) || itemstack.canPlaceOn(block)));
                }
            }
        }
        return flag;
    }
    
    public void renderWorld(final float partialTicks, final long finishTimeNano) {
        this.updateLightmap(partialTicks);
        if (this.mc.getRenderViewEntity() == null) {
            final Minecraft mc = this.mc;
            final Minecraft mc2 = this.mc;
            mc.setRenderViewEntity(Minecraft.player);
        }
        this.getMouseOver(partialTicks);
        if (Config.isShaders()) {
            Shaders.beginRender(this.mc, partialTicks, finishTimeNano);
        }
        GlStateManager.enableDepth();
        GlStateManager.enableAlpha();
        GlStateManager.alphaFunc(516, 0.1f);
        this.mc.profiler.startSection("center");
        if (this.mc.gameSettings.anaglyph) {
            EntityRenderer.anaglyphField = 0;
            GlStateManager.colorMask(false, true, true, false);
            this.renderWorldPass(0, partialTicks, finishTimeNano);
            EntityRenderer.anaglyphField = 1;
            GlStateManager.colorMask(true, false, false, false);
            this.renderWorldPass(1, partialTicks, finishTimeNano);
            GlStateManager.colorMask(true, true, true, false);
        }
        else {
            this.renderWorldPass(2, partialTicks, finishTimeNano);
        }
        this.mc.profiler.endSection();
    }
    
    private void renderWorldPass(final int pass, final float partialTicks, final long finishTimeNano) {
        final boolean flag = Config.isShaders();
        if (flag) {
            Shaders.beginRenderPass(pass, partialTicks, finishTimeNano);
        }
        final RenderGlobal renderglobal = this.mc.renderGlobal;
        final ParticleManager particlemanager = this.mc.effectRenderer;
        final boolean flag2 = this.isDrawBlockOutline();
        GlStateManager.enableCull();
        this.mc.profiler.endStartSection("clear");
        if (flag) {
            Shaders.setViewport(0, 0, this.mc.displayWidth, this.mc.displayHeight);
        }
        else {
            GlStateManager.viewport(0, 0, this.mc.displayWidth, this.mc.displayHeight);
        }
        this.updateFogColor(partialTicks);
        GlStateManager.clear(16640);
        if (flag) {
            Shaders.clearRenderBuffer();
        }
        this.mc.profiler.endStartSection("camera");
        this.setupCameraTransform(partialTicks, pass);
        if (flag) {
            Shaders.setCamera(partialTicks);
        }
        if (Reflector.ActiveRenderInfo_updateRenderInfo2.exists()) {
            Reflector.call(Reflector.ActiveRenderInfo_updateRenderInfo2, this.mc.getRenderViewEntity(), this.mc.gameSettings.thirdPersonView == 2);
        }
        else {
            final Minecraft mc = this.mc;
            ActiveRenderInfo.updateRenderInfo(Minecraft.player, this.mc.gameSettings.thirdPersonView == 2);
        }
        this.mc.profiler.endStartSection("frustum");
        final ClippingHelper clippinghelper = ClippingHelperImpl.getInstance();
        this.mc.profiler.endStartSection("culling");
        clippinghelper.disabled = (Config.isShaders() && !Shaders.isFrustumCulling());
        final ICamera icamera = new Frustum(clippinghelper);
        final Entity entity = this.mc.getRenderViewEntity();
        final double d0 = entity.lastTickPosX + (entity.posX - entity.lastTickPosX) * partialTicks;
        final double d2 = entity.lastTickPosY + (entity.posY - entity.lastTickPosY) * partialTicks;
        final double d3 = entity.lastTickPosZ + (entity.posZ - entity.lastTickPosZ) * partialTicks;
        if (flag) {
            ShadersRender.setFrustrumPosition(icamera, d0, d2, d3);
        }
        else {
            icamera.setPosition(d0, d2, d3);
        }
        if ((Config.isSkyEnabled() || Config.isSunMoonEnabled() || Config.isStarsEnabled()) && !Shaders.isShadowPass) {
            this.setupFog(-1, partialTicks);
            this.mc.profiler.endStartSection("sky");
            GlStateManager.matrixMode(5889);
            GlStateManager.loadIdentity();
            Project.gluPerspective(this.getFOVModifier(partialTicks, true), this.mc.displayWidth / (float)this.mc.displayHeight, 0.05f, this.clipDistance);
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
            Project.gluPerspective(this.getFOVModifier(partialTicks, true), this.mc.displayWidth / (float)this.mc.displayHeight, 0.05f, this.clipDistance);
            GlStateManager.matrixMode(5888);
        }
        else {
            GlStateManager.disableBlend();
        }
        this.setupFog(0, partialTicks);
        GlStateManager.shadeModel(7425);
        if (entity.posY + entity.getEyeHeight() < 128.0 + this.mc.gameSettings.ofCloudsHeight * 128.0f) {
            this.renderCloudsCheck(renderglobal, partialTicks, pass, d0, d2, d3);
        }
        this.mc.profiler.endStartSection("prepareterrain");
        this.setupFog(0, partialTicks);
        this.mc.getTextureManager().bindTexture(TextureMap.LOCATION_BLOCKS_TEXTURE);
        RenderHelper.disableStandardItemLighting();
        this.mc.profiler.endStartSection("terrain_setup");
        final Entity p_checkLoadVisibleChunks_1_ = entity;
        final ICamera p_checkLoadVisibleChunks_3_ = icamera;
        final Minecraft mc2 = this.mc;
        this.checkLoadVisibleChunks(p_checkLoadVisibleChunks_1_, partialTicks, p_checkLoadVisibleChunks_3_, Minecraft.player.isSpectator());
        if (flag) {
            final RenderGlobal renderGlobal = renderglobal;
            final Entity viewEntity = entity;
            final double partialTicks2 = partialTicks;
            final ICamera camera = icamera;
            final int frameCount = this.frameCount++;
            final Minecraft mc3 = this.mc;
            ShadersRender.setupTerrain(renderGlobal, viewEntity, partialTicks2, camera, frameCount, Minecraft.player.isSpectator());
        }
        else {
            final RenderGlobal renderGlobal2 = renderglobal;
            final Entity viewEntity2 = entity;
            final double partialTicks3 = partialTicks;
            final ICamera camera2 = icamera;
            final int frameCount2 = this.frameCount++;
            final Minecraft mc4 = this.mc;
            renderGlobal2.setupTerrain(viewEntity2, partialTicks3, camera2, frameCount2, Minecraft.player.isSpectator());
        }
        if (pass == 0 || pass == 2) {
            this.mc.profiler.endStartSection("updatechunks");
            Lagometer.timerChunkUpload.start();
            this.mc.renderGlobal.updateChunks(finishTimeNano);
            Lagometer.timerChunkUpload.end();
        }
        this.mc.profiler.endStartSection("terrain");
        Lagometer.timerTerrain.start();
        if (this.mc.gameSettings.ofSmoothFps && pass > 0) {
            this.mc.profiler.endStartSection("finish");
            GL11.glFinish();
            this.mc.profiler.endStartSection("terrain");
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
        this.mc.getTextureManager().getTexture(TextureMap.LOCATION_BLOCKS_TEXTURE).setBlurMipmap(false, this.mc.gameSettings.mipmapLevels > 0);
        renderglobal.renderBlockLayer(BlockRenderLayer.CUTOUT_MIPPED, partialTicks, pass, entity);
        this.mc.getTextureManager().getTexture(TextureMap.LOCATION_BLOCKS_TEXTURE).restoreLastBlurMipmap();
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
            this.mc.profiler.endStartSection("entities");
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
        if (flag2 && this.mc.objectMouseOver != null && !entity.isInsideOfMaterial(Material.WATER)) {
            final EntityPlayer entityplayer = (EntityPlayer)entity;
            GlStateManager.disableAlpha();
            this.mc.profiler.endStartSection("outline");
            if (!Reflector.ForgeHooksClient_onDrawBlockHighlight.exists() || !Reflector.callBoolean(Reflector.ForgeHooksClient_onDrawBlockHighlight, renderglobal, entityplayer, this.mc.objectMouseOver, 0, partialTicks)) {
                renderglobal.drawSelectionBox(entityplayer, this.mc.objectMouseOver, 0, partialTicks);
            }
            GlStateManager.enableAlpha();
        }
        if (this.mc.debugRenderer.shouldRender()) {
            final boolean flag3 = GlStateManager.isFogEnabled();
            GlStateManager.disableFog();
            this.mc.debugRenderer.renderDebug(partialTicks, finishTimeNano);
            GlStateManager.setFogEnabled(flag3);
        }
        if (!renderglobal.damagedBlocks.isEmpty()) {
            this.mc.profiler.endStartSection("destroyProgress");
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
            this.mc.profiler.endStartSection("litParticles");
            if (flag) {
                Shaders.beginLitParticles();
            }
            particlemanager.renderLitParticles(entity, partialTicks);
            RenderHelper.disableStandardItemLighting();
            this.setupFog(0, partialTicks);
            this.mc.profiler.endStartSection("particles");
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
        if (Config.isShaders()) {
            GlStateManager.depthMask(Shaders.isRainDepth());
        }
        GlStateManager.enableCull();
        this.mc.profiler.endStartSection("weather");
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
        this.mc.profiler.endStartSection("translucent");
        if (flag) {
            Shaders.beginWater();
        }
        renderglobal.renderBlockLayer(BlockRenderLayer.TRANSLUCENT, partialTicks, pass, entity);
        if (flag) {
            Shaders.endWater();
        }
        if (Reflector.ForgeHooksClient_setRenderPass.exists() && !this.debugView) {
            RenderHelper.enableStandardItemLighting();
            this.mc.profiler.endStartSection("entities");
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
        if (entity.posY + entity.getEyeHeight() >= 128.0 + this.mc.gameSettings.ofCloudsHeight * 128.0f) {
            this.mc.profiler.endStartSection("aboveClouds");
            this.renderCloudsCheck(renderglobal, partialTicks, pass, d0, d2, d3);
        }
        if (Reflector.ForgeHooksClient_dispatchRenderLast.exists()) {
            this.mc.profiler.endStartSection("forge_render_last");
            Reflector.callVoid(Reflector.ForgeHooksClient_dispatchRenderLast, renderglobal, partialTicks);
        }
        EventManager.call(new EventRender(partialTicks));
        this.mc.profiler.endStartSection("hand");
        if (this.renderHand && !Shaders.isShadowPass) {
            if (flag) {
                ShadersRender.renderHand1(this, partialTicks, pass);
                Shaders.renderCompositeFinal();
            }
            GlStateManager.clear(256);
            if (flag) {
                ShadersRender.renderFPOverlay(this, partialTicks, pass);
            }
            else {
                this.renderHand(partialTicks, pass);
            }
        }
        if (flag) {
            Shaders.endRender();
        }
    }
    
    private void renderCloudsCheck(final RenderGlobal renderGlobalIn, final float partialTicks, final int pass, final double x, final double y, final double z) {
        if (this.mc.gameSettings.renderDistanceChunks >= 4 && !Config.isCloudsOff() && Shaders.shouldRenderClouds(this.mc.gameSettings)) {
            this.mc.profiler.endStartSection("clouds");
            GlStateManager.matrixMode(5889);
            GlStateManager.loadIdentity();
            Project.gluPerspective(this.getFOVModifier(partialTicks, true), this.mc.displayWidth / (float)this.mc.displayHeight, 0.05f, this.clipDistance * 4.0f);
            GlStateManager.matrixMode(5888);
            GlStateManager.pushMatrix();
            this.setupFog(0, partialTicks);
            renderGlobalIn.renderClouds(partialTicks, pass, x, y, z);
            GlStateManager.disableFog();
            GlStateManager.popMatrix();
            GlStateManager.matrixMode(5889);
            GlStateManager.loadIdentity();
            Project.gluPerspective(this.getFOVModifier(partialTicks, true), this.mc.displayWidth / (float)this.mc.displayHeight, 0.05f, this.clipDistance);
            GlStateManager.matrixMode(5888);
        }
    }
    
    private void addRainParticles() {
        float f = this.mc.world.getRainStrength(1.0f);
        if (!Config.isRainFancy()) {
            f /= 2.0f;
        }
        if (f != 0.0f && Config.isRainSplash()) {
            this.random.setSeed(this.rendererUpdateCount * 312987231L);
            final Entity entity = this.mc.getRenderViewEntity();
            final World world = this.mc.world;
            final BlockPos blockpos = new BlockPos(entity);
            final int i = 10;
            double d0 = 0.0;
            double d2 = 0.0;
            double d3 = 0.0;
            int j = 0;
            int k = (int)(100.0f * f * f);
            if (this.mc.gameSettings.particleSetting == 1) {
                k >>= 1;
            }
            else if (this.mc.gameSettings.particleSetting == 2) {
                k = 0;
            }
            for (int l = 0; l < k; ++l) {
                final BlockPos blockpos2 = world.getPrecipitationHeight(blockpos.add(this.random.nextInt(10) - this.random.nextInt(10), 0, this.random.nextInt(10) - this.random.nextInt(10)));
                final Biome biome = world.getBiome(blockpos2);
                final BlockPos blockpos3 = blockpos2.down();
                final IBlockState iblockstate = world.getBlockState(blockpos3);
                if (blockpos2.getY() <= blockpos.getY() + 10 && blockpos2.getY() >= blockpos.getY() - 10 && biome.canRain() && biome.getTemperature(blockpos2) >= 0.15f) {
                    final double d4 = this.random.nextDouble();
                    final double d5 = this.random.nextDouble();
                    final AxisAlignedBB axisalignedbb = iblockstate.getBoundingBox(world, blockpos3);
                    if (iblockstate.getMaterial() != Material.LAVA && iblockstate.getBlock() != Blocks.MAGMA) {
                        if (iblockstate.getMaterial() != Material.AIR) {
                            ++j;
                            if (this.random.nextInt(j) == 0) {
                                d0 = blockpos3.getX() + d4;
                                d2 = blockpos3.getY() + 0.1f + axisalignedbb.maxY - 1.0;
                                d3 = blockpos3.getZ() + d5;
                            }
                            this.mc.world.spawnParticle(EnumParticleTypes.WATER_DROP, blockpos3.getX() + d4, blockpos3.getY() + 0.1f + axisalignedbb.maxY, blockpos3.getZ() + d5, 0.0, 0.0, 0.0, new int[0]);
                        }
                    }
                    else {
                        this.mc.world.spawnParticle(EnumParticleTypes.SMOKE_NORMAL, blockpos2.getX() + d4, blockpos2.getY() + 0.1f - axisalignedbb.minY, blockpos2.getZ() + d5, 0.0, 0.0, 0.0, new int[0]);
                    }
                }
            }
            if (j > 0 && this.random.nextInt(3) < this.rainSoundCounter++) {
                this.rainSoundCounter = 0;
                if (d2 > blockpos.getY() + 1 && world.getPrecipitationHeight(blockpos).getY() > MathHelper.floor((float)blockpos.getY())) {
                    this.mc.world.playSound(d0, d2, d3, SoundEvents.WEATHER_RAIN_ABOVE, SoundCategory.WEATHER, 0.1f, 0.5f, false);
                }
                else {
                    this.mc.world.playSound(d0, d2, d3, SoundEvents.WEATHER_RAIN, SoundCategory.WEATHER, 0.2f, 1.0f, false);
                }
            }
        }
    }
    
    protected void renderRainSnow(final float partialTicks) {
        if (Reflector.ForgeWorldProvider_getWeatherRenderer.exists()) {
            final WorldProvider worldprovider = this.mc.world.provider;
            final Object object = Reflector.call(worldprovider, Reflector.ForgeWorldProvider_getWeatherRenderer, new Object[0]);
            if (object != null) {
                Reflector.callVoid(object, Reflector.IRenderHandler_render, partialTicks, this.mc.world, this.mc);
                return;
            }
        }
        final float f5 = this.mc.world.getRainStrength(partialTicks);
        if (f5 > 0.0f) {
            if (Config.isRainOff()) {
                return;
            }
            this.enableLightmap();
            final Entity entity = this.mc.getRenderViewEntity();
            final World world = this.mc.world;
            final int i = MathHelper.floor(entity.posX);
            final int j = MathHelper.floor(entity.posY);
            final int k = MathHelper.floor(entity.posZ);
            final Tessellator tessellator = Tessellator.getInstance();
            final BufferBuilder bufferbuilder = tessellator.getBuffer();
            GlStateManager.disableCull();
            GlStateManager.glNormal3f(0.0f, 1.0f, 0.0f);
            GlStateManager.enableBlend();
            GlStateManager.tryBlendFuncSeparate(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA, GlStateManager.SourceFactor.ONE, GlStateManager.DestFactor.ZERO);
            GlStateManager.alphaFunc(516, 0.1f);
            final double d0 = entity.lastTickPosX + (entity.posX - entity.lastTickPosX) * partialTicks;
            final double d2 = entity.lastTickPosY + (entity.posY - entity.lastTickPosY) * partialTicks;
            final double d3 = entity.lastTickPosZ + (entity.posZ - entity.lastTickPosZ) * partialTicks;
            final int l = MathHelper.floor(d2);
            int i2 = 5;
            if (Config.isRainFancy()) {
                i2 = 10;
            }
            int j2 = -1;
            final float f6 = this.rendererUpdateCount + partialTicks;
            bufferbuilder.setTranslation(-d0, -d2, -d3);
            GlStateManager.color(1.0f, 1.0f, 1.0f, 1.0f);
            final BlockPos.MutableBlockPos blockpos$mutableblockpos = new BlockPos.MutableBlockPos();
            for (int k2 = k - i2; k2 <= k + i2; ++k2) {
                for (int l2 = i - i2; l2 <= i + i2; ++l2) {
                    final int i3 = (k2 - k + 16) * 32 + l2 - i + 16;
                    final double d4 = this.rainXCoords[i3] * 0.5;
                    final double d5 = this.rainYCoords[i3] * 0.5;
                    blockpos$mutableblockpos.setPos(l2, 0, k2);
                    final Biome biome = world.getBiome(blockpos$mutableblockpos);
                    if (biome.canRain() || biome.getEnableSnow()) {
                        final int j3 = world.getPrecipitationHeight(blockpos$mutableblockpos).getY();
                        int k3 = j - i2;
                        int l3 = j + i2;
                        if (k3 < j3) {
                            k3 = j3;
                        }
                        if (l3 < j3) {
                            l3 = j3;
                        }
                        int i4;
                        if ((i4 = j3) < l) {
                            i4 = l;
                        }
                        if (k3 != l3) {
                            this.random.setSeed(l2 * l2 * 3121 + l2 * 45238971 ^ k2 * k2 * 418711 + k2 * 13761);
                            blockpos$mutableblockpos.setPos(l2, k3, k2);
                            final float f7 = biome.getTemperature(blockpos$mutableblockpos);
                            if (world.getBiomeProvider().getTemperatureAtHeight(f7, j3) >= 0.15f) {
                                if (j2 != 0) {
                                    if (j2 >= 0) {
                                        tessellator.draw();
                                    }
                                    j2 = 0;
                                    this.mc.getTextureManager().bindTexture(EntityRenderer.RAIN_TEXTURES);
                                    bufferbuilder.begin(7, DefaultVertexFormats.PARTICLE_POSITION_TEX_COLOR_LMAP);
                                }
                                final double d6 = -((this.rendererUpdateCount + l2 * l2 * 3121 + l2 * 45238971 + k2 * k2 * 418711 + k2 * 13761 & 0x1F) + (double)partialTicks) / 32.0 * (3.0 + this.random.nextDouble());
                                final double d7 = l2 + 0.5f - entity.posX;
                                final double d8 = k2 + 0.5f - entity.posZ;
                                final float f8 = MathHelper.sqrt(d7 * d7 + d8 * d8) / i2;
                                final float f9 = ((1.0f - f8 * f8) * 0.5f + 0.5f) * f5;
                                blockpos$mutableblockpos.setPos(l2, i4, k2);
                                final int j4 = world.getCombinedLight(blockpos$mutableblockpos, 0);
                                final int k4 = j4 >> 16 & 0xFFFF;
                                final int l4 = j4 & 0xFFFF;
                                bufferbuilder.pos(l2 - d4 + 0.5, l3, k2 - d5 + 0.5).tex(0.0, k3 * 0.25 + d6).color(1.0f, 1.0f, 1.0f, f9).lightmap(k4, l4).endVertex();
                                bufferbuilder.pos(l2 + d4 + 0.5, l3, k2 + d5 + 0.5).tex(1.0, k3 * 0.25 + d6).color(1.0f, 1.0f, 1.0f, f9).lightmap(k4, l4).endVertex();
                                bufferbuilder.pos(l2 + d4 + 0.5, k3, k2 + d5 + 0.5).tex(1.0, l3 * 0.25 + d6).color(1.0f, 1.0f, 1.0f, f9).lightmap(k4, l4).endVertex();
                                bufferbuilder.pos(l2 - d4 + 0.5, k3, k2 - d5 + 0.5).tex(0.0, l3 * 0.25 + d6).color(1.0f, 1.0f, 1.0f, f9).lightmap(k4, l4).endVertex();
                            }
                            else {
                                if (j2 != 1) {
                                    if (j2 >= 0) {
                                        tessellator.draw();
                                    }
                                    j2 = 1;
                                    this.mc.getTextureManager().bindTexture(EntityRenderer.SNOW_TEXTURES);
                                    bufferbuilder.begin(7, DefaultVertexFormats.PARTICLE_POSITION_TEX_COLOR_LMAP);
                                }
                                final double d9 = -((this.rendererUpdateCount & 0x1FF) + partialTicks) / 512.0f;
                                final double d10 = this.random.nextDouble() + f6 * 0.01 * (float)this.random.nextGaussian();
                                final double d11 = this.random.nextDouble() + f6 * (float)this.random.nextGaussian() * 0.001;
                                final double d12 = l2 + 0.5f - entity.posX;
                                final double d13 = k2 + 0.5f - entity.posZ;
                                final float f10 = MathHelper.sqrt(d12 * d12 + d13 * d13) / i2;
                                final float f11 = ((1.0f - f10 * f10) * 0.3f + 0.5f) * f5;
                                blockpos$mutableblockpos.setPos(l2, i4, k2);
                                final int i5 = (world.getCombinedLight(blockpos$mutableblockpos, 0) * 3 + 15728880) / 4;
                                final int j5 = i5 >> 16 & 0xFFFF;
                                final int k5 = i5 & 0xFFFF;
                                bufferbuilder.pos(l2 - d4 + 0.5, l3, k2 - d5 + 0.5).tex(0.0 + d10, k3 * 0.25 + d9 + d11).color(1.0f, 1.0f, 1.0f, f11).lightmap(j5, k5).endVertex();
                                bufferbuilder.pos(l2 + d4 + 0.5, l3, k2 + d5 + 0.5).tex(1.0 + d10, k3 * 0.25 + d9 + d11).color(1.0f, 1.0f, 1.0f, f11).lightmap(j5, k5).endVertex();
                                bufferbuilder.pos(l2 + d4 + 0.5, k3, k2 + d5 + 0.5).tex(1.0 + d10, l3 * 0.25 + d9 + d11).color(1.0f, 1.0f, 1.0f, f11).lightmap(j5, k5).endVertex();
                                bufferbuilder.pos(l2 - d4 + 0.5, k3, k2 - d5 + 0.5).tex(0.0 + d10, l3 * 0.25 + d9 + d11).color(1.0f, 1.0f, 1.0f, f11).lightmap(j5, k5).endVertex();
                            }
                        }
                    }
                }
            }
            if (j2 >= 0) {
                tessellator.draw();
            }
            bufferbuilder.setTranslation(0.0, 0.0, 0.0);
            GlStateManager.enableCull();
            GlStateManager.disableBlend();
            GlStateManager.alphaFunc(516, 0.1f);
            this.disableLightmap();
        }
    }
    
    public void setupOverlayRendering() {
        final ScaledResolution scaledresolution = new ScaledResolution(this.mc);
        GlStateManager.clear(256);
        GlStateManager.matrixMode(5889);
        GlStateManager.loadIdentity();
        GlStateManager.ortho(0.0, scaledresolution.getScaledWidth_double(), scaledresolution.getScaledHeight_double(), 0.0, 1000.0, 3000.0);
        GlStateManager.matrixMode(5888);
        GlStateManager.loadIdentity();
        GlStateManager.translate(0.0f, 0.0f, -2000.0f);
    }
    
    private void updateFogColor(final float partialTicks) {
        final World world = this.mc.world;
        final Entity entity = this.mc.getRenderViewEntity();
        float f = Minced.getInstance().manager.getModule(CustomFog.class).state ? (CustomFog.distance.getFloatValue() * 10.0f) : (0.25f + 0.75f * this.mc.gameSettings.renderDistanceChunks / 32.0f);
        f = 1.0f - (float)Math.pow(f, 0.25);
        Vec3d vec3d = world.getSkyColor(this.mc.getRenderViewEntity(), partialTicks);
        vec3d = CustomColors.getWorldSkyColor(vec3d, world, this.mc.getRenderViewEntity(), partialTicks);
        final float f2 = (float)vec3d.x;
        final float f3 = (float)vec3d.y;
        final float f4 = (float)vec3d.z;
        Vec3d vec3d2 = world.getFogColor(partialTicks);
        vec3d2 = CustomColors.getWorldFogColor(vec3d2, world, this.mc.getRenderViewEntity(), partialTicks);
        if (Minced.getInstance().manager.getModule(CustomFog.class).state) {
            final Color rgb = CustomFog.customColor.getColorValueColor();
            this.fogColorRed = rgb.getRed() / 255.0f;
            this.fogColorGreen = rgb.getGreen() / 255.0f;
            this.fogColorBlue = rgb.getBlue() / 255.0f;
        }
        else {
            this.fogColorRed = (float)vec3d2.x;
            this.fogColorGreen = (float)vec3d2.y;
            this.fogColorBlue = (float)vec3d2.z;
        }
        if (this.mc.gameSettings.renderDistanceChunks >= 4) {
            final double d0 = (MathHelper.sin(world.getCelestialAngleRadians(partialTicks)) > 0.0f) ? -1.0 : 1.0;
            final Vec3d vec3d3 = new Vec3d(d0, 0.0, 0.0);
            float f5 = (float)entity.getLook(partialTicks).dotProduct(vec3d3);
            if (f5 < 0.0f) {
                f5 = 0.0f;
            }
            if (f5 > 0.0f) {
                final float[] afloat = world.provider.calcSunriseSunsetColors(world.getCelestialAngle(partialTicks), partialTicks);
                if (afloat != null) {
                    f5 *= afloat[3];
                    this.fogColorRed = this.fogColorRed * (1.0f - f5) + afloat[0] * f5;
                    this.fogColorGreen = this.fogColorGreen * (1.0f - f5) + afloat[1] * f5;
                    this.fogColorBlue = this.fogColorBlue * (1.0f - f5) + afloat[2] * f5;
                }
            }
        }
        this.fogColorRed += (f2 - this.fogColorRed) * f;
        this.fogColorGreen += (f3 - this.fogColorGreen) * f;
        this.fogColorBlue += (f4 - this.fogColorBlue) * f;
        final float f6 = world.getRainStrength(partialTicks);
        if (f6 > 0.0f) {
            final float f7 = 1.0f - f6 * 0.5f;
            final float f8 = 1.0f - f6 * 0.4f;
            this.fogColorRed *= f7;
            this.fogColorGreen *= f7;
            this.fogColorBlue *= f8;
        }
        final float f9 = world.getThunderStrength(partialTicks);
        if (f9 > 0.0f) {
            final float f10 = 1.0f - f9 * 0.5f;
            this.fogColorRed *= f10;
            this.fogColorGreen *= f10;
            this.fogColorBlue *= f10;
        }
        final IBlockState iblockstate1 = ActiveRenderInfo.getBlockStateAtEntityViewpoint(this.mc.world, entity, partialTicks);
        if (this.cloudFog) {
            final Vec3d vec3d4 = world.getCloudColour(partialTicks);
            this.fogColorRed = (float)vec3d4.x;
            this.fogColorGreen = (float)vec3d4.y;
            this.fogColorBlue = (float)vec3d4.z;
        }
        else if (Reflector.ForgeBlock_getFogColor.exists()) {
            final Vec3d vec3d5 = ActiveRenderInfo.projectViewFromEntity(entity, partialTicks);
            final BlockPos blockpos = new BlockPos(vec3d5);
            final IBlockState iblockstate2 = this.mc.world.getBlockState(blockpos);
            final Vec3d vec3d6 = (Vec3d)Reflector.call(iblockstate2.getBlock(), Reflector.ForgeBlock_getFogColor, this.mc.world, blockpos, iblockstate2, entity, new Vec3d(this.fogColorRed, this.fogColorGreen, this.fogColorBlue), partialTicks);
            this.fogColorRed = (float)vec3d6.x;
            this.fogColorGreen = (float)vec3d6.y;
            this.fogColorBlue = (float)vec3d6.z;
        }
        else if (iblockstate1.getMaterial() == Material.WATER) {
            float f11 = 0.0f;
            if (entity instanceof EntityLivingBase) {
                f11 = EnchantmentHelper.getRespirationModifier((EntityLivingBase)entity) * 0.2f;
                f11 = Config.limit(f11, 0.0f, 0.6f);
                if (((EntityLivingBase)entity).isPotionActive(MobEffects.WATER_BREATHING)) {
                    f11 = f11 * 0.3f + 0.6f;
                }
            }
            this.fogColorRed = 0.02f + f11;
            this.fogColorGreen = 0.02f + f11;
            this.fogColorBlue = 0.2f + f11;
        }
        else if (iblockstate1.getMaterial() == Material.LAVA) {
            this.fogColorRed = 0.6f;
            this.fogColorGreen = 0.1f;
            this.fogColorBlue = 0.0f;
        }
        if (iblockstate1.getMaterial() == Material.WATER) {
            final Vec3d vec3d7 = CustomColors.getUnderwaterColor(this.mc.world, this.mc.getRenderViewEntity().posX, this.mc.getRenderViewEntity().posY + 1.0, this.mc.getRenderViewEntity().posZ);
            if (vec3d7 != null) {
                this.fogColorRed = (float)vec3d7.x;
                this.fogColorGreen = (float)vec3d7.y;
                this.fogColorBlue = (float)vec3d7.z;
            }
        }
        else if (iblockstate1.getMaterial() == Material.LAVA) {
            final Vec3d vec3d8 = CustomColors.getUnderlavaColor(this.mc.world, this.mc.getRenderViewEntity().posX, this.mc.getRenderViewEntity().posY + 1.0, this.mc.getRenderViewEntity().posZ);
            if (vec3d8 != null) {
                this.fogColorRed = (float)vec3d8.x;
                this.fogColorGreen = (float)vec3d8.y;
                this.fogColorBlue = (float)vec3d8.z;
            }
        }
        final float f12 = this.fogColor2 + (this.fogColor1 - this.fogColor2) * partialTicks;
        this.fogColorRed *= f12;
        this.fogColorGreen *= f12;
        this.fogColorBlue *= f12;
        double d2 = (entity.lastTickPosY + (entity.posY - entity.lastTickPosY) * partialTicks) * world.provider.getVoidFogYFactor();
        if (entity instanceof EntityLivingBase && ((EntityLivingBase)entity).isPotionActive(MobEffects.BLINDNESS)) {
            final int i = ((EntityLivingBase)entity).getActivePotionEffect(MobEffects.BLINDNESS).getDuration();
            if (i < 20) {
                d2 *= 1.0f - i / 20.0f;
            }
            else {
                d2 = 0.0;
            }
        }
        if (d2 < 1.0) {
            if (d2 < 0.0) {
                d2 = 0.0;
            }
            d2 *= d2;
            this.fogColorRed *= (float)d2;
            this.fogColorGreen *= (float)d2;
            this.fogColorBlue *= (float)d2;
        }
        if (this.bossColorModifier > 0.0f) {
            final float f13 = this.bossColorModifierPrev + (this.bossColorModifier - this.bossColorModifierPrev) * partialTicks;
            this.fogColorRed = this.fogColorRed * (1.0f - f13) + this.fogColorRed * 0.7f * f13;
            this.fogColorGreen = this.fogColorGreen * (1.0f - f13) + this.fogColorGreen * 0.6f * f13;
            this.fogColorBlue = this.fogColorBlue * (1.0f - f13) + this.fogColorBlue * 0.6f * f13;
        }
        if (entity instanceof EntityLivingBase && ((EntityLivingBase)entity).isPotionActive(MobEffects.NIGHT_VISION)) {
            final float f14 = this.getNightVisionBrightness((EntityLivingBase)entity, partialTicks);
            float f15 = 1.0f / this.fogColorRed;
            if (f15 > 1.0f / this.fogColorGreen) {
                f15 = 1.0f / this.fogColorGreen;
            }
            if (f15 > 1.0f / this.fogColorBlue) {
                f15 = 1.0f / this.fogColorBlue;
            }
            if (Float.isInfinite(f15)) {
                f15 = Math.nextAfter(f15, 0.0);
            }
            this.fogColorRed = this.fogColorRed * (1.0f - f14) + this.fogColorRed * f15 * f14;
            this.fogColorGreen = this.fogColorGreen * (1.0f - f14) + this.fogColorGreen * f15 * f14;
            this.fogColorBlue = this.fogColorBlue * (1.0f - f14) + this.fogColorBlue * f15 * f14;
        }
        if (this.mc.gameSettings.anaglyph) {
            final float f16 = (this.fogColorRed * 30.0f + this.fogColorGreen * 59.0f + this.fogColorBlue * 11.0f) / 100.0f;
            final float f17 = (this.fogColorRed * 30.0f + this.fogColorGreen * 70.0f) / 100.0f;
            final float f18 = (this.fogColorRed * 30.0f + this.fogColorBlue * 70.0f) / 100.0f;
            this.fogColorRed = f16;
            this.fogColorGreen = f17;
            this.fogColorBlue = f18;
        }
        if (Reflector.EntityViewRenderEvent_FogColors_Constructor.exists()) {
            final Object object = Reflector.newInstance(Reflector.EntityViewRenderEvent_FogColors_Constructor, this, entity, iblockstate1, partialTicks, this.fogColorRed, this.fogColorGreen, this.fogColorBlue);
            Reflector.postForgeBusEvent(object);
            this.fogColorRed = Reflector.callFloat(object, Reflector.EntityViewRenderEvent_FogColors_getRed, new Object[0]);
            this.fogColorGreen = Reflector.callFloat(object, Reflector.EntityViewRenderEvent_FogColors_getGreen, new Object[0]);
            this.fogColorBlue = Reflector.callFloat(object, Reflector.EntityViewRenderEvent_FogColors_getBlue, new Object[0]);
        }
        Shaders.setClearColor(this.fogColorRed, this.fogColorGreen, this.fogColorBlue, 0.0f);
    }
    
    private void setupFog(final int startCoords, final float partialTicks) {
        if (Minced.getInstance().manager.getModule(NoOverlay.class).state) {
            final EventOverlay event = new EventOverlay(EventOverlay.OverlayType.Fog);
            EventManager.call(event);
            if (event.isCanceled()) {
                return;
            }
        }
        this.fogStandard = false;
        final Entity entity = this.mc.getRenderViewEntity();
        this.setupFogColor(false);
        GlStateManager.glNormal3f(0.0f, -1.0f, 0.0f);
        GlStateManager.color(1.0f, 1.0f, 1.0f, 1.0f);
        final IBlockState iblockstate = ActiveRenderInfo.getBlockStateAtEntityViewpoint(this.mc.world, entity, partialTicks);
        float f = -1.0f;
        if (Reflector.ForgeHooksClient_getFogDensity.exists()) {
            f = Reflector.callFloat(Reflector.ForgeHooksClient_getFogDensity, this, entity, iblockstate, partialTicks, 0.1f);
        }
        if (f >= 0.0f) {
            GlStateManager.setFogDensity(f);
        }
        else if (entity instanceof EntityLivingBase && ((EntityLivingBase)entity).isPotionActive(MobEffects.BLINDNESS)) {
            float f2 = 5.0f;
            final int i = ((EntityLivingBase)entity).getActivePotionEffect(MobEffects.BLINDNESS).getDuration();
            if (i < 20) {
                f2 = 5.0f + (this.farPlaneDistance - 5.0f) * (1.0f - i / 20.0f);
            }
            GlStateManager.setFog(GlStateManager.FogMode.LINEAR);
            if (startCoords == -1) {
                GlStateManager.setFogStart(0.0f);
                GlStateManager.setFogEnd(f2 * 0.8f);
            }
            else {
                GlStateManager.setFogStart(f2 * 0.25f);
                GlStateManager.setFogEnd(f2);
            }
            if (GLContext.getCapabilities().GL_NV_fog_distance && Config.isFogFancy()) {
                GlStateManager.glFogi(34138, 34139);
            }
        }
        else if (this.cloudFog) {
            GlStateManager.setFog(GlStateManager.FogMode.EXP);
            GlStateManager.setFogDensity(0.1f);
        }
        else if (iblockstate.getMaterial() == Material.WATER) {
            GlStateManager.setFog(GlStateManager.FogMode.EXP);
            final float f3 = Config.isClearWater() ? 0.02f : 0.1f;
            if (entity instanceof EntityLivingBase) {
                if (((EntityLivingBase)entity).isPotionActive(MobEffects.WATER_BREATHING)) {
                    GlStateManager.setFogDensity(0.01f);
                }
                else {
                    final float f4 = 0.1f - EnchantmentHelper.getRespirationModifier((EntityLivingBase)entity) * 0.03f;
                    GlStateManager.setFogDensity(Config.limit(f4, 0.0f, f3));
                }
            }
            else {
                GlStateManager.setFogDensity(f3);
            }
        }
        else if (iblockstate.getMaterial() == Material.LAVA) {
            GlStateManager.setFog(GlStateManager.FogMode.EXP);
            GlStateManager.setFogDensity(2.0f);
        }
        else {
            final float f5 = this.farPlaneDistance;
            this.fogStandard = true;
            GlStateManager.setFog(GlStateManager.FogMode.LINEAR);
            if (startCoords == -1) {
                GlStateManager.setFogStart(0.0f);
                GlStateManager.setFogEnd(f5);
            }
            else {
                GlStateManager.setFogStart(f5 * (Minced.getInstance().manager.getModule(CustomFog.class).state ? CustomFog.distance.getFloatValue() : Config.getFogStart()));
                GlStateManager.setFogEnd(f5);
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
                GlStateManager.setFogStart(f5 * 0.05f);
                GlStateManager.setFogEnd(f5);
            }
            if (Reflector.ForgeHooksClient_onFogRender.exists()) {
                Reflector.callVoid(Reflector.ForgeHooksClient_onFogRender, this, entity, iblockstate, partialTicks, startCoords, f5);
            }
        }
        GlStateManager.enableColorMaterial();
        GlStateManager.enableFog();
        GlStateManager.colorMaterial(1028, 4608);
    }
    
    public void setupFogColor(final boolean black) {
        if (black) {
            GlStateManager.glFog(2918, this.setFogColorBuffer(0.0f, 0.0f, 0.0f, 1.0f));
        }
        else {
            GlStateManager.glFog(2918, this.setFogColorBuffer(this.fogColorRed, this.fogColorGreen, this.fogColorBlue, 1.0f));
        }
    }
    
    private FloatBuffer setFogColorBuffer(final float red, final float green, final float blue, final float alpha) {
        if (Config.isShaders()) {
            Shaders.setFogColor(red, green, blue);
        }
        this.fogColorBuffer.clear();
        this.fogColorBuffer.put(red).put(green).put(blue).put(alpha);
        this.fogColorBuffer.flip();
        return this.fogColorBuffer;
    }
    
    public void resetData() {
        this.itemActivationItem = null;
        this.mapItemRenderer.clearLoadedMaps();
    }
    
    public MapItemRenderer getMapItemRenderer() {
        return this.mapItemRenderer;
    }
    
    private void waitForServerThread() {
        this.serverWaitTimeCurrent = 0;
        if (Config.isSmoothWorld() && Config.isSingleProcessor()) {
            if (this.mc.isIntegratedServerRunning()) {
                final IntegratedServer integratedserver = this.mc.getIntegratedServer();
                if (integratedserver != null) {
                    final boolean flag = this.mc.isGamePaused();
                    if (!flag && !(this.mc.currentScreen instanceof GuiDownloadTerrain)) {
                        if (this.serverWaitTime > 0) {
                            Lagometer.timerServer.start();
                            Config.sleep(this.serverWaitTime);
                            Lagometer.timerServer.end();
                            this.serverWaitTimeCurrent = this.serverWaitTime;
                        }
                        final long i = System.nanoTime() / 1000000L;
                        if (this.lastServerTime != 0L && this.lastServerTicks != 0) {
                            long j = i - this.lastServerTime;
                            if (j < 0L) {
                                this.lastServerTime = i;
                                j = 0L;
                            }
                            if (j >= 50L) {
                                this.lastServerTime = i;
                                final int k = integratedserver.getTickCounter();
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
                        }
                        else {
                            this.lastServerTime = i;
                            this.lastServerTicks = integratedserver.getTickCounter();
                            this.avgServerTickDiff = 1.0f;
                            this.avgServerTimeDiff = 50.0f;
                        }
                    }
                    else {
                        if (this.mc.currentScreen instanceof GuiDownloadTerrain) {
                            Config.sleep(20L);
                        }
                        this.lastServerTime = 0L;
                        this.lastServerTicks = 0;
                    }
                }
            }
        }
        else {
            this.lastServerTime = 0L;
            this.lastServerTicks = 0;
        }
    }
    
    private void frameInit() {
        GlErrors.frameStart();
        if (!this.initialized) {
            ReflectorResolver.resolve();
            TextureUtils.registerResourceListener();
            if (Config.getBitsOs() == 64 && Config.getBitsJre() == 32) {
                Config.setNotify64BitJava(true);
            }
            this.initialized = true;
        }
        Config.checkDisplayMode();
        final World world = this.mc.world;
        if (world != null) {
            if (Config.getNewRelease() != null) {
                final String s = "HD_U".replace("HD_U", "HD Ultra").replace("L", "Light");
                final String s2 = s + " " + Config.getNewRelease();
                final TextComponentString textcomponentstring = new TextComponentString(I18n.format("of.message.newVersion", "§n" + s2 + "§r"));
                textcomponentstring.setStyle(new Style().setClickEvent(new ClickEvent(ClickEvent.Action.OPEN_URL, "https://optifine.net/downloads")));
                this.mc.ingameGUI.getChatGUI().printChatMessage(textcomponentstring);
                Config.setNewRelease(null);
            }
            if (Config.isNotify64BitJava()) {
                Config.setNotify64BitJava(false);
                final TextComponentString textcomponentstring2 = new TextComponentString(I18n.format("of.message.java64Bit", new Object[0]));
                this.mc.ingameGUI.getChatGUI().printChatMessage(textcomponentstring2);
            }
        }
        if (this.mc.currentScreen instanceof GuiMainMenu) {
            this.updateMainMenu((GuiMainMenu)this.mc.currentScreen);
        }
        if (this.updatedWorld != world) {
            RandomEntities.worldChanged(this.updatedWorld, world);
            Config.updateThreadPriorities();
            this.lastServerTime = 0L;
            this.lastServerTicks = 0;
            this.updatedWorld = world;
        }
        if (!this.setFxaaShader(Shaders.configAntialiasingLevel)) {
            Shaders.configAntialiasingLevel = 0;
        }
        if (this.mc.currentScreen != null && this.mc.currentScreen.getClass() == GuiChat.class) {
            this.mc.displayGuiScreen(new GuiChatOF((GuiChat)this.mc.currentScreen));
        }
    }
    
    private void frameFinish() {
        if (this.mc.world != null && Config.isShowGlErrors() && TimedEvent.isActive("CheckGlErrorFrameFinish", 10000L)) {
            final int i = GlStateManager.glGetError();
            if (i != 0 && GlErrors.isEnabled(i)) {
                final String s = Config.getGlErrorString(i);
                final TextComponentString textcomponentstring = new TextComponentString(I18n.format("of.message.openglError", i, s));
                this.mc.ingameGUI.getChatGUI().printChatMessage(textcomponentstring);
            }
        }
    }
    
    private void updateMainMenu(final GuiMainMenu p_updateMainMenu_1_) {
        try {
            String s = null;
            final Calendar calendar = Calendar.getInstance();
            calendar.setTime(new Date());
            final int i = calendar.get(5);
            final int j = calendar.get(2) + 1;
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
        catch (Throwable t) {}
    }
    
    public boolean setFxaaShader(final int p_setFxaaShader_1_) {
        if (!OpenGlHelper.isFramebufferEnabled()) {
            return false;
        }
        if (this.shaderGroup != null && this.shaderGroup != this.fxaaShaders[2] && this.shaderGroup != this.fxaaShaders[4]) {
            return true;
        }
        if (p_setFxaaShader_1_ != 2 && p_setFxaaShader_1_ != 4) {
            if (this.shaderGroup == null) {
                return true;
            }
            this.shaderGroup.deleteShaderGroup();
            this.shaderGroup = null;
            return true;
        }
        else {
            if (this.shaderGroup != null && this.shaderGroup == this.fxaaShaders[p_setFxaaShader_1_]) {
                return true;
            }
            if (this.mc.world == null) {
                return true;
            }
            this.loadShader(new ResourceLocation("shaders/post/fxaa_of_" + p_setFxaaShader_1_ + "x.json"));
            this.fxaaShaders[p_setFxaaShader_1_] = this.shaderGroup;
            return this.useShader;
        }
    }
    
    private void checkLoadVisibleChunks(final Entity p_checkLoadVisibleChunks_1_, final float p_checkLoadVisibleChunks_2_, final ICamera p_checkLoadVisibleChunks_3_, final boolean p_checkLoadVisibleChunks_4_) {
        final int i = 201435902;
        if (this.loadVisibleChunks) {
            this.loadVisibleChunks = false;
            this.loadAllVisibleChunks(p_checkLoadVisibleChunks_1_, p_checkLoadVisibleChunks_2_, p_checkLoadVisibleChunks_3_, p_checkLoadVisibleChunks_4_);
            this.mc.ingameGUI.getChatGUI().deleteChatLine(i);
        }
        if (Keyboard.isKeyDown(61) && Keyboard.isKeyDown(38)) {
            if (this.mc.gameSettings.keyBindAdvancements.getKeyCode() == 38) {
                if (this.mc.currentScreen instanceof GuiScreenAdvancements) {
                    this.mc.displayGuiScreen(null);
                }
                while (Keyboard.next()) {}
            }
            if (this.mc.currentScreen != null) {
                return;
            }
            this.loadVisibleChunks = true;
            final TextComponentString textcomponentstring = new TextComponentString(I18n.format("of.message.loadingVisibleChunks", new Object[0]));
            this.mc.ingameGUI.getChatGUI().printChatMessageWithOptionalDeletion(textcomponentstring, i);
            Reflector.Minecraft_actionKeyF3.setValue(this.mc, Boolean.TRUE);
        }
    }
    
    private void loadAllVisibleChunks(final Entity p_loadAllVisibleChunks_1_, final double p_loadAllVisibleChunks_2_, final ICamera p_loadAllVisibleChunks_4_, final boolean p_loadAllVisibleChunks_5_) {
        final int i = this.mc.gameSettings.ofChunkUpdates;
        final boolean flag = this.mc.gameSettings.ofLazyChunkLoading;
        try {
            this.mc.gameSettings.ofChunkUpdates = 1000;
            this.mc.gameSettings.ofLazyChunkLoading = false;
            final RenderGlobal renderglobal = Config.getRenderGlobal();
            int j = renderglobal.getCountLoadedChunks();
            final long k = System.currentTimeMillis();
            Config.dbg("Loading visible chunks");
            long l = System.currentTimeMillis() + 5000L;
            int i2 = 0;
            boolean flag2 = false;
            do {
                flag2 = false;
                for (int j2 = 0; j2 < 100; ++j2) {
                    renderglobal.displayListEntitiesDirty = true;
                    renderglobal.setupTerrain(p_loadAllVisibleChunks_1_, p_loadAllVisibleChunks_2_, p_loadAllVisibleChunks_4_, this.frameCount++, p_loadAllVisibleChunks_5_);
                    if (!renderglobal.hasNoChunkUpdates()) {
                        flag2 = true;
                    }
                    i2 += renderglobal.getCountChunksToUpdate();
                    while (!renderglobal.hasNoChunkUpdates()) {
                        renderglobal.updateChunks(System.nanoTime() + 1000000000L);
                    }
                    i2 -= renderglobal.getCountChunksToUpdate();
                    if (!flag2) {
                        break;
                    }
                }
                if (renderglobal.getCountLoadedChunks() != j) {
                    flag2 = true;
                    j = renderglobal.getCountLoadedChunks();
                }
                if (System.currentTimeMillis() > l) {
                    Config.log("Chunks loaded: " + i2);
                    l = System.currentTimeMillis() + 5000L;
                }
            } while (flag2);
            Config.log("Chunks loaded: " + i2);
            Config.log("Finished loading visible chunks");
            RenderChunk.renderChunksUpdated = 0;
        }
        finally {
            this.mc.gameSettings.ofChunkUpdates = i;
            this.mc.gameSettings.ofLazyChunkLoading = flag;
        }
    }
    
    public static void drawNameplate(final FontRenderer fontRendererIn, final String str, final float x, final float y, final float z, final int verticalShift, final float viewerYaw, final float viewerPitch, final boolean isThirdPersonFrontal, final boolean isSneaking) {
        GlStateManager.pushMatrix();
        GlStateManager.translate(x, y, z);
        GlStateManager.glNormal3f(0.0f, 1.0f, 0.0f);
        GlStateManager.rotate(-viewerYaw, 0.0f, 1.0f, 0.0f);
        GlStateManager.rotate((isThirdPersonFrontal ? -1 : 1) * viewerPitch, 1.0f, 0.0f, 0.0f);
        GlStateManager.scale(-0.025f, -0.025f, 0.025f);
        GlStateManager.disableLighting();
        GlStateManager.depthMask(false);
        if (!isSneaking) {
            GlStateManager.disableDepth();
        }
        GlStateManager.enableBlend();
        GlStateManager.tryBlendFuncSeparate(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA, GlStateManager.SourceFactor.ONE, GlStateManager.DestFactor.ZERO);
        final int i = fontRendererIn.getStringWidth(str) / 2;
        GlStateManager.disableTexture2D();
        final Tessellator tessellator = Tessellator.getInstance();
        final BufferBuilder bufferbuilder = tessellator.getBuffer();
        bufferbuilder.begin(7, DefaultVertexFormats.POSITION_COLOR);
        bufferbuilder.pos(-i - 1, -1 + verticalShift, 0.0).color(0.0f, 0.0f, 0.0f, 0.25f).endVertex();
        bufferbuilder.pos(-i - 1, 8 + verticalShift, 0.0).color(0.0f, 0.0f, 0.0f, 0.25f).endVertex();
        bufferbuilder.pos(i + 1, 8 + verticalShift, 0.0).color(0.0f, 0.0f, 0.0f, 0.25f).endVertex();
        bufferbuilder.pos(i + 1, -1 + verticalShift, 0.0).color(0.0f, 0.0f, 0.0f, 0.25f).endVertex();
        tessellator.draw();
        GlStateManager.enableTexture2D();
        if (!isSneaking) {
            fontRendererIn.drawString(str, -fontRendererIn.getStringWidth(str) / 2, verticalShift, 553648127);
            GlStateManager.enableDepth();
        }
        GlStateManager.depthMask(true);
        fontRendererIn.drawString(str, -fontRendererIn.getStringWidth(str) / 2, verticalShift, isSneaking ? 553648127 : -1);
        GlStateManager.enableLighting();
        GlStateManager.disableBlend();
        GlStateManager.color(1.0f, 1.0f, 1.0f, 1.0f);
        GlStateManager.popMatrix();
    }
    
    public void displayItemActivation(final ItemStack stack) {
        if (Minced.getInstance().manager.getModule(NoOverlay.class).state) {
            final EventOverlay event = new EventOverlay(EventOverlay.OverlayType.TotemAnimation);
            EventManager.call(event);
            if (event.isCanceled()) {
                return;
            }
        }
        this.itemActivationItem = stack;
        this.itemActivationTicks = 40;
        this.itemActivationOffX = this.random.nextFloat() * 2.0f - 1.0f;
        this.itemActivationOffY = this.random.nextFloat() * 2.0f - 1.0f;
    }
    
    private void renderItemActivation(final int p_190563_1_, final int p_190563_2_, final float p_190563_3_) {
        if (this.itemActivationItem != null && this.itemActivationTicks > 0) {
            final int i = 40 - this.itemActivationTicks;
            final float f = (i + p_190563_3_) / 40.0f;
            final float f2 = f * f;
            final float f3 = f * f2;
            final float f4 = 10.25f * f3 * f2 + -24.95f * f2 * f2 + 25.5f * f3 + -13.8f * f2 + 4.0f * f;
            final float f5 = f4 * 3.1415927f;
            final float f6 = this.itemActivationOffX * (p_190563_1_ / 4);
            final float f7 = this.itemActivationOffY * (p_190563_2_ / 4);
            GlStateManager.enableAlpha();
            GlStateManager.pushMatrix();
            GlStateManager.pushAttrib();
            GlStateManager.enableDepth();
            GlStateManager.disableCull();
            RenderHelper.enableStandardItemLighting();
            GlStateManager.translate(p_190563_1_ / 2 + f6 * MathHelper.abs(MathHelper.sin(f5 * 2.0f)), p_190563_2_ / 2 + f7 * MathHelper.abs(MathHelper.sin(f5 * 2.0f)), -50.0f);
            final float f8 = 50.0f + 175.0f * MathHelper.sin(f5);
            GlStateManager.scale(f8, -f8, f8);
            GlStateManager.rotate(900.0f * MathHelper.abs(MathHelper.sin(f5)), 0.0f, 1.0f, 0.0f);
            GlStateManager.rotate(6.0f * MathHelper.cos(f * 8.0f), 1.0f, 0.0f, 0.0f);
            GlStateManager.rotate(6.0f * MathHelper.cos(f * 8.0f), 0.0f, 0.0f, 1.0f);
            this.mc.getRenderItem().renderItem(this.itemActivationItem, ItemCameraTransforms.TransformType.FIXED);
            GlStateManager.popAttrib();
            GlStateManager.popMatrix();
            RenderHelper.disableStandardItemLighting();
            GlStateManager.enableCull();
            GlStateManager.disableDepth();
        }
    }
    
    static {
        LOGGER = LogManager.getLogger();
        RAIN_TEXTURES = new ResourceLocation("textures/environment/rain.png");
        SNOW_TEXTURES = new ResourceLocation("textures/environment/snow.png");
        SHADERS_TEXTURES = new ResourceLocation[] { new ResourceLocation("shaders/post/notch.json"), new ResourceLocation("shaders/post/fxaa.json"), new ResourceLocation("shaders/post/art.json"), new ResourceLocation("shaders/post/bumpy.json"), new ResourceLocation("shaders/post/blobs2.json"), new ResourceLocation("shaders/post/pencil.json"), new ResourceLocation("shaders/post/color_convolve.json"), new ResourceLocation("shaders/post/deconverge.json"), new ResourceLocation("shaders/post/flip.json"), new ResourceLocation("shaders/post/invert.json"), new ResourceLocation("shaders/post/ntsc.json"), new ResourceLocation("shaders/post/outline.json"), new ResourceLocation("shaders/post/phosphor.json"), new ResourceLocation("shaders/post/scan_pincushion.json"), new ResourceLocation("shaders/post/sobel.json"), new ResourceLocation("shaders/post/bits.json"), new ResourceLocation("shaders/post/desaturate.json"), new ResourceLocation("shaders/post/green.json"), new ResourceLocation("shaders/post/blur.json"), new ResourceLocation("shaders/post/wobble.json"), new ResourceLocation("shaders/post/blobs.json"), new ResourceLocation("shaders/post/antialias.json"), new ResourceLocation("shaders/post/creeper.json"), new ResourceLocation("shaders/post/spider.json") };
        SHADER_COUNT = EntityRenderer.SHADERS_TEXTURES.length;
    }
}
