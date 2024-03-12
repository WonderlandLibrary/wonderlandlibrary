// 
// Decompiled by Procyon v0.5.36
// 

package net.minecraft.client.renderer;

import org.apache.logging.log4j.LogManager;
import net.minecraft.client.renderer.chunk.RenderChunk;
import org.lwjgl.input.Keyboard;
import net.optifine.reflect.Reflector;
import java.util.Date;
import java.util.Calendar;
import net.optifine.util.TimedEvent;
import net.minecraft.client.gui.GuiScreen;
import net.optifine.gui.GuiChatOF;
import net.minecraft.client.gui.GuiChat;
import net.optifine.RandomEntities;
import net.minecraft.client.gui.GuiMainMenu;
import net.minecraft.util.IChatComponent;
import net.minecraft.event.ClickEvent;
import net.minecraft.util.ChatStyle;
import net.minecraft.util.ChatComponentText;
import net.minecraft.client.resources.I18n;
import net.optifine.util.TextureUtils;
import net.optifine.reflect.ReflectorResolver;
import net.optifine.GlErrors;
import net.minecraft.server.integrated.IntegratedServer;
import net.minecraft.client.gui.GuiDownloadTerrain;
import org.lwjgl.opengl.GLContext;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.world.biome.BiomeGenBase;
import net.minecraft.world.IBlockAccess;
import net.minecraft.util.EnumParticleTypes;
import dev.tenacity.module.impl.render.Ambience;
import net.minecraft.client.renderer.culling.ICamera;
import net.minecraft.client.renderer.culling.ClippingHelper;
import net.minecraft.client.particle.EffectRenderer;
import dev.tenacity.event.impl.render.Render3DEvent;
import dev.tenacity.utils.render.EntityCulling;
import net.minecraft.util.EnumWorldBlockLayer;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.client.renderer.culling.Frustum;
import net.minecraft.client.renderer.culling.ClippingHelperImpl;
import net.minecraft.item.ItemStack;
import net.minecraft.inventory.IInventory;
import net.minecraft.world.WorldSettings;
import net.minecraft.crash.CrashReportCategory;
import net.optifine.util.MemoryMonitor;
import net.minecraft.util.ReportedException;
import net.minecraft.crash.CrashReport;
import net.minecraft.client.renderer.tileentity.TileEntityRendererDispatcher;
import net.optifine.Lagometer;
import net.minecraft.client.gui.ScaledResolution;
import dev.tenacity.module.impl.player.ChestStealer;
import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.Display;
import net.optifine.CustomColors;
import org.lwjgl.opengl.GL11;
import net.optifine.shaders.ShadersRender;
import net.optifine.reflect.ReflectorForge;
import net.optifine.shaders.Shaders;
import net.minecraft.potion.Potion;
import org.lwjgl.util.glu.Project;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.passive.EntityAnimal;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.BlockBed;
import dev.tenacity.event.Event;
import dev.tenacity.Tenacity;
import dev.tenacity.event.impl.render.HurtCamEvent;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.client.settings.GameSettings;
import net.minecraft.src.Config;
import net.minecraft.client.entity.AbstractClientPlayer;
import net.minecraft.util.AxisAlignedBB;
import java.util.Iterator;
import java.util.List;
import net.minecraft.util.Vec3;
import net.minecraft.entity.item.EntityItemFrame;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.MovingObjectPosition;
import com.google.common.base.Predicate;
import com.google.common.base.Predicates;
import net.minecraft.util.EntitySelectors;
import net.minecraft.entity.boss.BossStatus;
import net.minecraft.util.BlockPos;
import net.minecraft.client.shader.ShaderLinkHelper;
import com.google.gson.JsonSyntaxException;
import java.io.IOException;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.monster.EntityEnderman;
import net.minecraft.entity.monster.EntitySpider;
import net.minecraft.entity.monster.EntityCreeper;
import net.minecraft.util.MathHelper;
import net.minecraft.world.World;
import net.minecraft.client.shader.ShaderGroup;
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
    private static final Logger logger;
    private static final ResourceLocation locationRainPng;
    private static final ResourceLocation locationSnowPng;
    public static boolean anaglyphEnable;
    public static int anaglyphField;
    private final Minecraft mc;
    private final IResourceManager resourceManager;
    private final Random random;
    private float farPlaneDistance;
    public ItemRenderer itemRenderer;
    private final MapItemRenderer theMapItemRenderer;
    private int rendererUpdateCount;
    private Entity pointedEntity;
    private MouseFilter mouseFilterXAxis;
    private MouseFilter mouseFilterYAxis;
    private final float thirdPersonDistance = 4.0f;
    private float thirdPersonDistanceTemp;
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
    private final boolean renderHand = true;
    private final boolean drawBlockOutline = true;
    private long prevFrameTime;
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
    private final boolean debugView = false;
    private ShaderGroup theShaderGroup;
    private static final ResourceLocation[] shaderResourceLocations;
    public static final int shaderCount;
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
    private final ShaderGroup[] fxaaShaders;
    private boolean loadVisibleChunks;
    
    public EntityRenderer(final Minecraft mcIn, final IResourceManager resourceManagerIn) {
        this.random = new Random();
        this.mouseFilterXAxis = new MouseFilter();
        this.mouseFilterYAxis = new MouseFilter();
        this.thirdPersonDistanceTemp = 4.0f;
        this.prevFrameTime = Minecraft.getSystemTime();
        this.rainXCoords = new float[1024];
        this.rainYCoords = new float[1024];
        this.fogColorBuffer = GLAllocation.createDirectFloatBuffer(16);
        this.initialized = false;
        this.updatedWorld = null;
        this.fogStandard = false;
        this.clipDistance = 128.0f;
        this.lastServerTime = 0L;
        this.lastServerTicks = 0;
        this.serverWaitTime = 0;
        this.fxaaShaders = new ShaderGroup[10];
        this.loadVisibleChunks = false;
        this.shaderIndex = EntityRenderer.shaderCount;
        this.useShader = false;
        this.frameCount = 0;
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
                final float f = (float)(j - 16);
                final float f2 = (float)(i - 16);
                final float f3 = MathHelper.sqrt_float(f * f + f2 * f2);
                this.rainXCoords[i << 5 | j] = -f2 / f3;
                this.rainYCoords[i << 5 | j] = f / f3;
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
        this.shaderIndex = EntityRenderer.shaderCount;
    }
    
    public void switchUseShader() {
        this.useShader = !this.useShader;
    }
    
    public void loadEntityShader(final Entity entityIn) {
        if (OpenGlHelper.shadersSupported) {
            if (this.theShaderGroup != null) {
                this.theShaderGroup.deleteShaderGroup();
            }
            this.theShaderGroup = null;
            if (entityIn instanceof EntityCreeper) {
                this.loadShader(new ResourceLocation("shaders/post/creeper.json"));
            }
            else if (entityIn instanceof EntitySpider) {
                this.loadShader(new ResourceLocation("shaders/post/spider.json"));
            }
            else if (entityIn instanceof EntityEnderman) {
                this.loadShader(new ResourceLocation("shaders/post/invert.json"));
            }
        }
    }
    
    public void activateNextShader() {
        if (OpenGlHelper.shadersSupported && this.mc.getRenderViewEntity() instanceof EntityPlayer) {
            if (this.theShaderGroup != null) {
                this.theShaderGroup.deleteShaderGroup();
            }
            this.shaderIndex = (this.shaderIndex + 1) % (EntityRenderer.shaderResourceLocations.length + 1);
            if (this.shaderIndex != EntityRenderer.shaderCount) {
                this.loadShader(EntityRenderer.shaderResourceLocations[this.shaderIndex]);
            }
            else {
                this.theShaderGroup = null;
            }
        }
    }
    
    private void loadShader(final ResourceLocation resourceLocationIn) {
        if (OpenGlHelper.isFramebufferEnabled()) {
            try {
                (this.theShaderGroup = new ShaderGroup(this.mc.getTextureManager(), this.resourceManager, this.mc.getFramebuffer(), resourceLocationIn)).createBindFramebuffers(this.mc.displayWidth, this.mc.displayHeight);
                this.useShader = true;
            }
            catch (IOException | JsonSyntaxException ex2) {
                final Exception ex;
                final Exception ioexception = ex;
                EntityRenderer.logger.warn("Failed to load shader: " + resourceLocationIn, (Throwable)ioexception);
                this.shaderIndex = EntityRenderer.shaderCount;
                this.useShader = false;
            }
        }
    }
    
    @Override
    public void onResourceManagerReload(final IResourceManager resourceManager) {
        if (this.theShaderGroup != null) {
            this.theShaderGroup.deleteShaderGroup();
        }
        this.theShaderGroup = null;
        if (this.shaderIndex != EntityRenderer.shaderCount) {
            this.loadShader(EntityRenderer.shaderResourceLocations[this.shaderIndex]);
        }
        else {
            this.loadEntityShader(this.mc.getRenderViewEntity());
        }
    }
    
    public void updateRenderer() {
        if (OpenGlHelper.shadersSupported && ShaderLinkHelper.getStaticShaderLinkHelper() == null) {
            ShaderLinkHelper.setNewStaticShaderLinkHelper();
        }
        this.updateFovModifierHand();
        this.updateTorchFlicker();
        this.fogColor2 = this.fogColor1;
        this.getClass();
        this.thirdPersonDistanceTemp = 4.0f;
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
            this.mc.setRenderViewEntity(this.mc.thePlayer);
        }
        final Entity entity = this.mc.getRenderViewEntity();
        final double d2 = entity.posX;
        final double d3 = entity.posY + entity.getEyeHeight();
        final double d4 = entity.posZ;
        final float f3 = this.mc.theWorld.getLightBrightness(new BlockPos(d2, d3, d4));
        float f4 = this.mc.gameSettings.renderDistanceChunks / 16.0f;
        f4 = MathHelper.clamp_float(f4, 0.0f, 1.0f);
        final float f5 = f3 * (1.0f - f4) + f4;
        this.fogColor1 += (f5 - this.fogColor1) * 0.1f;
        ++this.rendererUpdateCount;
        this.itemRenderer.updateEquippedItem();
        this.addRainParticles();
        this.bossColorModifierPrev = this.bossColorModifier;
        if (BossStatus.hasColorModifier) {
            this.bossColorModifier += 0.05f;
            if (this.bossColorModifier > 1.0f) {
                this.bossColorModifier = 1.0f;
            }
            BossStatus.hasColorModifier = false;
        }
        else if (this.bossColorModifier > 0.0f) {
            this.bossColorModifier -= 0.0125f;
        }
    }
    
    public ShaderGroup getShaderGroup() {
        return this.theShaderGroup;
    }
    
    public void updateShaderGroupSize(final int width, final int height) {
        if (OpenGlHelper.shadersSupported) {
            if (this.theShaderGroup != null) {
                this.theShaderGroup.createBindFramebuffers(width, height);
            }
            this.mc.renderGlobal.createBindEntityOutlineFbs(width, height);
        }
    }
    
    public void getMouseOver(final float partialTicks) {
        final Entity entity = this.mc.getRenderViewEntity();
        if (entity != null && this.mc.theWorld != null) {
            this.mc.mcProfiler.startSection("pick");
            this.mc.pointedEntity = null;
            final double d0 = this.mc.playerController.getBlockReachDistance();
            final double reachDistance = 3.0;
            this.mc.objectMouseOver = entity.rayTrace(d0, partialTicks);
            double d2 = d0;
            final Vec3 vec3 = entity.getPositionEyes(partialTicks);
            boolean flag = false;
            if (this.mc.playerController.extendedReach()) {
                d2 = 6.0;
            }
            else if (d0 > reachDistance) {
                flag = true;
            }
            if (this.mc.objectMouseOver != null) {
                d2 = this.mc.objectMouseOver.hitVec.distanceTo(vec3);
            }
            final Vec3 vec4 = entity.getLook(partialTicks);
            final Vec3 vec5 = vec3.addVector(vec4.xCoord * d0, vec4.yCoord * d0, vec4.zCoord * d0);
            this.pointedEntity = null;
            Vec3 vec6 = null;
            final float f = 1.0f;
            final List<Entity> list = this.mc.theWorld.getEntitiesInAABBexcluding(entity, entity.getEntityBoundingBox().addCoord(vec4.xCoord * d0, vec4.yCoord * d0, vec4.zCoord * d0).expand(f, f, f), (Predicate<? super Entity>)Predicates.and((Predicate)EntitySelectors.NOT_SPECTATING, Entity::canBeCollidedWith));
            double d3 = d2;
            for (final Entity entity2 : list) {
                final float f2 = entity2.getCollisionBorderSize();
                final AxisAlignedBB axisalignedbb = entity2.getEntityBoundingBox().expand(f2, f2, f2);
                final MovingObjectPosition movingobjectposition = axisalignedbb.calculateIntercept(vec3, vec5);
                if (axisalignedbb.isVecInside(vec3)) {
                    if (d3 < 0.0) {
                        continue;
                    }
                    this.pointedEntity = entity2;
                    vec6 = ((movingobjectposition == null) ? vec3 : movingobjectposition.hitVec);
                    d3 = 0.0;
                }
                else {
                    if (movingobjectposition == null) {
                        continue;
                    }
                    final double d4 = vec3.distanceTo(movingobjectposition.hitVec);
                    if (d4 >= d3 && d3 != 0.0) {
                        continue;
                    }
                    final boolean flag2 = false;
                    if (!flag2 && entity2 == entity.ridingEntity) {
                        if (d3 != 0.0) {
                            continue;
                        }
                        this.pointedEntity = entity2;
                        vec6 = movingobjectposition.hitVec;
                    }
                    else {
                        this.pointedEntity = entity2;
                        vec6 = movingobjectposition.hitVec;
                        d3 = d4;
                    }
                }
            }
            if (this.pointedEntity != null && flag && vec3.distanceTo(vec6) > reachDistance) {
                this.pointedEntity = null;
                this.mc.objectMouseOver = new MovingObjectPosition(MovingObjectPosition.MovingObjectType.MISS, vec6, null, new BlockPos(vec6));
            }
            if (this.pointedEntity != null && (d3 < d2 || this.mc.objectMouseOver == null)) {
                this.mc.objectMouseOver = new MovingObjectPosition(this.pointedEntity, vec6);
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
        this.getClass();
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
            if (!Config.zoomMode) {
                Config.zoomMode = true;
                Config.zoomSmoothCamera = this.mc.gameSettings.smoothCamera;
                this.mc.gameSettings.smoothCamera = true;
                this.mc.renderGlobal.displayListEntitiesDirty = true;
            }
            f /= 4.0f;
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
        final Block block = ActiveRenderInfo.getBlockAtEntityViewpoint(this.mc.theWorld, entity, partialTicks);
        if (block.getMaterial() == Material.water) {
            f = f * 60.0f / 70.0f;
        }
        return f;
    }
    
    private void hurtCameraEffect(final float partialTicks) {
        if (this.mc.getRenderViewEntity() instanceof EntityLivingBase) {
            final HurtCamEvent hurtCamEvent = new HurtCamEvent();
            Tenacity.INSTANCE.getEventProtocol().handleEvent(hurtCamEvent);
            if (!hurtCamEvent.isCancelled()) {
                final EntityLivingBase entitylivingbase = (EntityLivingBase)this.mc.getRenderViewEntity();
                float f = entitylivingbase.hurtTime - partialTicks;
                if (entitylivingbase.getHealth() <= 0.0f) {
                    final float f2 = entitylivingbase.deathTime + partialTicks;
                    GlStateManager.rotate(40.0f - 8000.0f / (f2 + 200.0f), 0.0f, 0.0f, 1.0f);
                }
                if (f < 0.0f) {
                    return;
                }
                f /= entitylivingbase.maxHurtTime;
                f = MathHelper.sin(f * f * f * f * 3.1415927f);
                final float f3 = entitylivingbase.attackedAtYaw;
                GlStateManager.rotate(-f3, 0.0f, 1.0f, 0.0f);
                GlStateManager.rotate(-f * 14.0f, 0.0f, 0.0f, 1.0f);
                GlStateManager.rotate(f3, 0.0f, 1.0f, 0.0f);
            }
        }
    }
    
    private void setupViewBobbing(final float partialTicks) {
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
                final IBlockState iblockstate = this.mc.theWorld.getBlockState(blockpos);
                final int j = iblockstate.getValue((IProperty<EnumFacing>)BlockBed.FACING).getHorizontalIndex();
                GlStateManager.rotate((float)(j * 90), 0.0f, 1.0f, 0.0f);
                GlStateManager.rotate(entity.prevRotationYaw + (entity.rotationYaw - entity.prevRotationYaw) * partialTicks + 180.0f, 0.0f, -1.0f, 0.0f);
                GlStateManager.rotate(entity.prevRotationPitch + (entity.rotationPitch - entity.prevRotationPitch) * partialTicks, -1.0f, 0.0f, 0.0f);
            }
        }
        else if (this.mc.gameSettings.thirdPersonView > 0) {
            final float thirdPersonDistanceTemp = this.thirdPersonDistanceTemp;
            this.getClass();
            double d4 = thirdPersonDistanceTemp + (4.0f - this.thirdPersonDistanceTemp) * partialTicks;
            if (this.mc.gameSettings.debugCamEnable) {
                GlStateManager.translate(0.0f, 0.0f, (float)(-d4));
            }
            else {
                final float f2 = entity.rotationYaw;
                float f3 = entity.rotationPitch;
                if (this.mc.gameSettings.thirdPersonView == 2) {
                    f3 += 180.0f;
                }
                final double d5 = -MathHelper.sin(f2 / 180.0f * 3.1415927f) * MathHelper.cos(f3 / 180.0f * 3.1415927f) * d4;
                final double d6 = MathHelper.cos(f2 / 180.0f * 3.1415927f) * MathHelper.cos(f3 / 180.0f * 3.1415927f) * d4;
                final double d7 = -MathHelper.sin(f3 / 180.0f * 3.1415927f) * d4;
                for (int i = 0; i < 8; ++i) {
                    float f4 = (float)((i & 0x1) * 2 - 1);
                    float f5 = (float)((i >> 1 & 0x1) * 2 - 1);
                    float f6 = (float)((i >> 2 & 0x1) * 2 - 1);
                    f4 *= 0.1f;
                    f5 *= 0.1f;
                    f6 *= 0.1f;
                    final MovingObjectPosition movingobjectposition = this.mc.theWorld.rayTraceBlocks(new Vec3(d0 + f4, d2 + f5, d3 + f6), new Vec3(d0 - d5 + f4 + f6, d2 - d7 + f5, d3 - d6 + f6));
                    if (movingobjectposition != null) {
                        final double d8 = movingobjectposition.hitVec.distanceTo(new Vec3(d0, d2, d3));
                        if (d8 < d4) {
                            d4 = d8;
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
            GlStateManager.translate(0.0f, 0.0f, -0.1f);
        }
        GlStateManager.rotate(entity.prevRotationPitch + (entity.rotationPitch - entity.prevRotationPitch) * partialTicks, 1.0f, 0.0f, 0.0f);
        if (entity instanceof EntityAnimal) {
            final EntityAnimal entityanimal = (EntityAnimal)entity;
            GlStateManager.rotate(entityanimal.prevRotationYawHead + (entityanimal.rotationYawHead - entityanimal.prevRotationYawHead) * partialTicks + 180.0f, 0.0f, 1.0f, 0.0f);
        }
        else {
            GlStateManager.rotate(entity.prevRotationYaw + (entity.rotationYaw - entity.prevRotationYaw) * partialTicks + 180.0f, 0.0f, 1.0f, 0.0f);
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
            GlStateManager.translate(-(pass * 2 - 1) * f, 0.0f, 0.0f);
        }
        this.clipDistance = this.farPlaneDistance * 2.0f;
        if (this.clipDistance < 173.0f) {
            this.clipDistance = 173.0f;
        }
        Project.gluPerspective(this.getFOVModifier(partialTicks, true), this.mc.displayWidth / (float)this.mc.displayHeight, 0.05f, this.clipDistance);
        GlStateManager.matrixMode(5888);
        GlStateManager.loadIdentity();
        if (this.mc.gameSettings.anaglyph) {
            GlStateManager.translate((pass * 2 - 1) * 0.1f, 0.0f, 0.0f);
        }
        this.hurtCameraEffect(partialTicks);
        if (this.mc.gameSettings.viewBobbing) {
            this.setupViewBobbing(partialTicks);
        }
        final float f2 = this.mc.thePlayer.prevTimeInPortal + (this.mc.thePlayer.timeInPortal - this.mc.thePlayer.prevTimeInPortal) * partialTicks;
        if (f2 > 0.0f) {
            int i = 20;
            if (this.mc.thePlayer.isPotionActive(Potion.confusion)) {
                i = 7;
            }
            float f3 = 5.0f / (f2 * f2 + 5.0f) - f2 * 0.04f;
            f3 *= f3;
            GlStateManager.rotate((this.rendererUpdateCount + partialTicks) * i, 0.0f, 1.0f, 1.0f);
            GlStateManager.scale(1.0f / f3, 1.0f, 1.0f);
            GlStateManager.rotate(-(this.rendererUpdateCount + partialTicks) * i, 0.0f, 1.0f, 1.0f);
        }
        this.orientCamera(partialTicks);
        this.getClass();
    }
    
    private void renderHand(final float partialTicks, final int xOffset) {
        this.renderHand(partialTicks, xOffset, true, true, false);
    }
    
    public void renderHand(final float p_renderHand_1_, final int p_renderHand_2_, final boolean p_renderHand_3_, final boolean p_renderHand_4_, final boolean p_renderHand_5_) {
        this.getClass();
        GlStateManager.matrixMode(5889);
        GlStateManager.loadIdentity();
        final float f = 0.07f;
        if (this.mc.gameSettings.anaglyph) {
            GlStateManager.translate(-(p_renderHand_2_ * 2 - 1) * f, 0.0f, 0.0f);
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
            this.hurtCameraEffect(p_renderHand_1_);
            if (this.mc.gameSettings.viewBobbing) {
                this.setupViewBobbing(p_renderHand_1_);
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
            this.hurtCameraEffect(p_renderHand_1_);
        }
        if (this.mc.gameSettings.viewBobbing) {
            this.setupViewBobbing(p_renderHand_1_);
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
        GlStateManager.scale(f, f, f);
        GlStateManager.translate(8.0f, 8.0f, 8.0f);
        GlStateManager.matrixMode(5888);
        this.mc.getTextureManager().bindTexture(this.locationLightMap);
        GL11.glTexParameteri(3553, 10241, 9729);
        GL11.glTexParameteri(3553, 10240, 9729);
        GL11.glTexParameteri(3553, 10242, 33071);
        GL11.glTexParameteri(3553, 10243, 33071);
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
            this.mc.mcProfiler.startSection("lightTex");
            final World world = this.mc.theWorld;
            if (world != null) {
                if (Config.isCustomColors() && CustomColors.updateLightmap(world, this.torchFlickerX, this.lightmapColors, this.mc.thePlayer.isPotionActive(Potion.nightVision), partialTicks)) {
                    this.lightmapTexture.updateDynamicTexture();
                    this.lightmapUpdateNeeded = false;
                    this.mc.mcProfiler.endSection();
                    return;
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
                    if (world.provider.getDimensionId() == 1) {
                        f9 = 0.22f + f4 * 0.75f;
                        f10 = 0.28f + f7 * 0.75f;
                        f11 = 0.25f + f8 * 0.75f;
                    }
                    if (this.mc.thePlayer.isPotionActive(Potion.nightVision)) {
                        final float f13 = this.getNightVisionBrightness(this.mc.thePlayer, partialTicks);
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
                    this.lightmapColors[i] = (j << 24 | k << 16 | l << 8 | i2);
                }
                this.lightmapTexture.updateDynamicTexture();
                this.lightmapUpdateNeeded = false;
                this.mc.mcProfiler.endSection();
            }
        }
    }
    
    public float getNightVisionBrightness(final EntityLivingBase entitylivingbaseIn, final float partialTicks) {
        final int i = entitylivingbaseIn.getActivePotionEffect(Potion.nightVision).getDuration();
        return (i > 200) ? 1.0f : (0.7f + MathHelper.sin((i - partialTicks) * 3.1415927f * 0.2f) * 0.3f);
    }
    
    public void updateCameraAndRender(final float partialTicks, final long nanoTime) {
        Config.renderPartialTicks = partialTicks;
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
        this.mc.mcProfiler.startSection("mouse");
        if (flag && Minecraft.isRunningOnMac && this.mc.inGameHasFocus && !Mouse.isInsideWindow()) {
            Mouse.setGrabbed(false);
            Mouse.setCursorPosition(Display.getWidth() / 2, Display.getHeight() / 2);
            Mouse.setGrabbed(true);
        }
        if (this.mc.inGameHasFocus && flag) {
            this.mc.mouseHelper.mouseXYChange();
            final float f = this.mc.gameSettings.mouseSensitivity * 0.6f + 0.2f;
            final float f2 = f * f * f * (ChestStealer.canSteal() ? 10.0f : 8.0f);
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
                this.mc.thePlayer.setAngles(f3, f4 * i);
            }
            else {
                this.smoothCamYaw = 0.0f;
                this.smoothCamPitch = 0.0f;
                this.mc.thePlayer.setAngles(f3, f4 * i);
            }
        }
        this.mc.mcProfiler.endSection();
        if (!this.mc.skipRenderWorld) {
            EntityRenderer.anaglyphEnable = this.mc.gameSettings.anaglyph;
            final ScaledResolution scaledresolution = new ScaledResolution(this.mc);
            final int i2 = scaledresolution.getScaledWidth();
            final int j1 = scaledresolution.getScaledHeight();
            final int k1 = Mouse.getX() * i2 / this.mc.displayWidth;
            final int l1 = j1 - Mouse.getY() * j1 / this.mc.displayHeight - 1;
            final int i3 = this.mc.gameSettings.limitFramerate;
            if (this.mc.theWorld != null) {
                this.mc.mcProfiler.startSection("level");
                int m = Math.min(Minecraft.getDebugFPS(), i3);
                m = Math.max(m, 60);
                final long k2 = System.nanoTime() - nanoTime;
                final long l2 = Math.max(1000000000 / m / 4 - k2, 0L);
                this.renderWorld(partialTicks, System.nanoTime() + l2);
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
                this.mc.mcProfiler.endStartSection("gui");
                if (!this.mc.gameSettings.hideGUI || this.mc.currentScreen != null) {
                    if (this.mc.currentScreen != null) {
                        this.mc.currentScreen.onDrag(k1, l1);
                    }
                    GlStateManager.alphaFunc(516, 0.1f);
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
            else {
                GlStateManager.viewport(0, 0, this.mc.displayWidth, this.mc.displayHeight);
                GlStateManager.matrixMode(5889);
                GlStateManager.loadIdentity();
                GlStateManager.matrixMode(5888);
                GlStateManager.loadIdentity();
                this.setupOverlayRendering();
                TileEntityRendererDispatcher.instance.renderEngine = this.mc.getTextureManager();
                TileEntityRendererDispatcher.instance.fontRenderer = this.mc.fontRendererObj;
            }
            if (this.mc.currentScreen != null) {
                GlStateManager.clear(256);
                try {
                    this.mc.currentScreen.drawScreen(k1, l1, partialTicks);
                }
                catch (Throwable throwable) {
                    final CrashReport crashreport = CrashReport.makeCrashReport(throwable, "Rendering screen");
                    final CrashReportCategory crashreportcategory = crashreport.makeCategory("Screen render details");
                    crashreportcategory.addCrashSectionCallable("Screen name", () -> this.mc.currentScreen.getClass().getCanonicalName());
                    crashreportcategory.addCrashSectionCallable("Mouse location", () -> String.format("Scaled: (%d, %d). Absolute: (%d, %d)", k1, l1, Mouse.getX(), Mouse.getY()));
                    final ScaledResolution scaledResolution;
                    crashreportcategory.addCrashSectionCallable("Screen size", () -> String.format("Scaled: (%d, %d). Absolute: (%d, %d). Scale factor of %d", scaledResolution.getScaledWidth(), scaledResolution.getScaledHeight(), this.mc.displayWidth, this.mc.displayHeight, scaledResolution.getScaleFactor()));
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
    
    private boolean isDrawBlockOutline() {
        this.getClass();
        final Entity entity = this.mc.getRenderViewEntity();
        boolean flag = entity instanceof EntityPlayer && !this.mc.gameSettings.hideGUI;
        if (flag && !((EntityPlayer)entity).capabilities.allowEdit) {
            final ItemStack itemstack = ((EntityPlayer)entity).getCurrentEquippedItem();
            if (this.mc.objectMouseOver != null && this.mc.objectMouseOver.typeOfHit == MovingObjectPosition.MovingObjectType.BLOCK) {
                final BlockPos blockpos = this.mc.objectMouseOver.getBlockPos();
                final IBlockState iblockstate = this.mc.theWorld.getBlockState(blockpos);
                final Block block = iblockstate.getBlock();
                if (this.mc.playerController.getCurrentGameType() == WorldSettings.GameType.SPECTATOR) {
                    flag = (ReflectorForge.blockHasTileEntity(iblockstate) && this.mc.theWorld.getTileEntity(blockpos) instanceof IInventory);
                }
                else {
                    flag = (itemstack != null && (itemstack.canDestroy(block) || itemstack.canPlaceOn(block)));
                }
            }
        }
        return flag;
    }
    
    private void renderWorldDirections(final float partialTicks) {
        if (this.mc.gameSettings.showDebugInfo && !this.mc.gameSettings.hideGUI && !this.mc.thePlayer.hasReducedDebug() && !this.mc.gameSettings.reducedDebugInfo) {
            final Entity entity = this.mc.getRenderViewEntity();
            GlStateManager.enableBlend();
            GlStateManager.tryBlendFuncSeparate(770, 771, 1, 0);
            GL11.glLineWidth(1.0f);
            GlStateManager.disableTexture2D();
            GlStateManager.depthMask(false);
            GlStateManager.pushMatrix();
            GlStateManager.matrixMode(5888);
            GlStateManager.loadIdentity();
            this.orientCamera(partialTicks);
            GlStateManager.translate(0.0f, entity.getEyeHeight(), 0.0f);
            RenderGlobal.drawOutlinedBoundingBox(new AxisAlignedBB(0.0, 0.0, 0.0, 0.005, 1.0E-4, 1.0E-4), 255, 0, 0, 255);
            RenderGlobal.drawOutlinedBoundingBox(new AxisAlignedBB(0.0, 0.0, 0.0, 1.0E-4, 1.0E-4, 0.005), 0, 0, 255, 255);
            RenderGlobal.drawOutlinedBoundingBox(new AxisAlignedBB(0.0, 0.0, 0.0, 1.0E-4, 0.0033, 1.0E-4), 0, 255, 0, 255);
            GlStateManager.popMatrix();
            GlStateManager.depthMask(true);
            GlStateManager.enableTexture2D();
            GlStateManager.disableBlend();
        }
    }
    
    public void renderWorld(final float partialTicks, final long finishTimeNano) {
        this.updateLightmap(partialTicks);
        if (this.mc.getRenderViewEntity() == null) {
            this.mc.setRenderViewEntity(this.mc.thePlayer);
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
        this.mc.mcProfiler.endSection();
    }
    
    private void renderWorldPass(final int pass, final float partialTicks, final long finishTimeNano) {
        final boolean flag = Config.isShaders();
        if (flag) {
            Shaders.beginRenderPass(pass, partialTicks, finishTimeNano);
        }
        final RenderGlobal renderglobal = this.mc.renderGlobal;
        final EffectRenderer effectrenderer = this.mc.effectRenderer;
        final boolean flag2 = this.isDrawBlockOutline();
        GlStateManager.enableCull();
        this.mc.mcProfiler.endStartSection("clear");
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
        this.mc.mcProfiler.endStartSection("camera");
        this.setupCameraTransform(partialTicks, pass);
        if (flag) {
            Shaders.setCamera(partialTicks);
        }
        ActiveRenderInfo.updateRenderInfo(this.mc.thePlayer, this.mc.gameSettings.thirdPersonView == 2);
        this.mc.mcProfiler.endStartSection("frustum");
        final ClippingHelper clippinghelper = ClippingHelperImpl.getInstance();
        this.mc.mcProfiler.endStartSection("culling");
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
            this.mc.mcProfiler.endStartSection("sky");
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
            this.renderCloudsCheck(renderglobal, partialTicks, pass);
        }
        this.mc.mcProfiler.endStartSection("prepareterrain");
        this.setupFog(0, partialTicks);
        this.mc.getTextureManager().bindTexture(TextureMap.locationBlocksTexture);
        RenderHelper.disableStandardItemLighting();
        this.mc.mcProfiler.endStartSection("terrain_setup");
        this.checkLoadVisibleChunks(entity, partialTicks, icamera, this.mc.thePlayer.isSpectator());
        if (flag) {
            ShadersRender.setupTerrain(renderglobal, entity, partialTicks, icamera, this.frameCount++, this.mc.thePlayer.isSpectator());
        }
        else {
            renderglobal.setupTerrain(entity, partialTicks, icamera, this.frameCount++, this.mc.thePlayer.isSpectator());
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
        renderglobal.renderBlockLayer(EnumWorldBlockLayer.SOLID, partialTicks, pass, entity);
        GlStateManager.enableAlpha();
        if (flag) {
            ShadersRender.beginTerrainCutoutMipped();
        }
        this.mc.getTextureManager().getTexture(TextureMap.locationBlocksTexture).setBlurMipmap(false, this.mc.gameSettings.mipmapLevels > 0);
        renderglobal.renderBlockLayer(EnumWorldBlockLayer.CUTOUT_MIPPED, partialTicks, pass, entity);
        this.mc.getTextureManager().getTexture(TextureMap.locationBlocksTexture).restoreLastBlurMipmap();
        this.mc.getTextureManager().getTexture(TextureMap.locationBlocksTexture).setBlurMipmap(false, false);
        if (flag) {
            ShadersRender.beginTerrainCutout();
        }
        renderglobal.renderBlockLayer(EnumWorldBlockLayer.CUTOUT, partialTicks, pass, entity);
        this.mc.getTextureManager().getTexture(TextureMap.locationBlocksTexture).restoreLastBlurMipmap();
        if (flag) {
            ShadersRender.endTerrain();
        }
        Lagometer.timerTerrain.end();
        GlStateManager.shadeModel(7424);
        GlStateManager.alphaFunc(516, 0.1f);
        this.getClass();
        GlStateManager.matrixMode(5888);
        GlStateManager.popMatrix();
        GlStateManager.pushMatrix();
        RenderHelper.enableStandardItemLighting();
        this.mc.mcProfiler.endStartSection("entities");
        EntityCulling.shouldPerformCulling = true;
        renderglobal.renderEntities(entity, icamera, partialTicks);
        EntityCulling.shouldPerformCulling = false;
        RenderHelper.disableStandardItemLighting();
        this.disableLightmap();
        GlStateManager.matrixMode(5888);
        GlStateManager.popMatrix();
        GlStateManager.pushMatrix();
        if (this.mc.objectMouseOver != null && entity.isInsideOfMaterial(Material.water) && flag2) {
            final EntityPlayer entityplayer = (EntityPlayer)entity;
            GlStateManager.disableAlpha();
            this.mc.mcProfiler.endStartSection("outline");
            renderglobal.drawSelectionBox(entityplayer, this.mc.objectMouseOver, 0, partialTicks);
            GlStateManager.enableAlpha();
        }
        GlStateManager.matrixMode(5888);
        GlStateManager.popMatrix();
        if (flag2 && this.mc.objectMouseOver != null && !entity.isInsideOfMaterial(Material.water)) {
            final EntityPlayer entityplayer2 = (EntityPlayer)entity;
            GlStateManager.disableAlpha();
            this.mc.mcProfiler.endStartSection("outline");
            if (!this.mc.gameSettings.hideGUI) {
                renderglobal.drawSelectionBox(entityplayer2, this.mc.objectMouseOver, 0, partialTicks);
            }
            GlStateManager.enableAlpha();
        }
        if (!renderglobal.damagedBlocks.isEmpty()) {
            this.mc.mcProfiler.endStartSection("destroyProgress");
            GlStateManager.enableBlend();
            GlStateManager.tryBlendFuncSeparate(770, 1, 1, 0);
            this.mc.getTextureManager().getTexture(TextureMap.locationBlocksTexture).setBlurMipmap(false, false);
            renderglobal.drawBlockDamageTexture(Tessellator.getInstance(), Tessellator.getInstance().getWorldRenderer(), entity, partialTicks);
            this.mc.getTextureManager().getTexture(TextureMap.locationBlocksTexture).restoreLastBlurMipmap();
            GlStateManager.disableBlend();
        }
        GlStateManager.tryBlendFuncSeparate(770, 771, 1, 0);
        GlStateManager.disableBlend();
        this.getClass();
        this.enableLightmap();
        this.mc.mcProfiler.endStartSection("litParticles");
        if (flag) {
            Shaders.beginLitParticles();
        }
        effectrenderer.renderLitParticles(entity, partialTicks);
        RenderHelper.disableStandardItemLighting();
        this.setupFog(0, partialTicks);
        this.mc.mcProfiler.endStartSection("particles");
        if (flag) {
            Shaders.beginParticles();
        }
        effectrenderer.renderParticles(entity, partialTicks);
        if (flag) {
            Shaders.endParticles();
        }
        this.disableLightmap();
        GlStateManager.depthMask(false);
        if (Config.isShaders()) {
            GlStateManager.depthMask(Shaders.isRainDepth());
        }
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
        GlStateManager.tryBlendFuncSeparate(770, 771, 1, 0);
        GlStateManager.alphaFunc(516, 0.1f);
        this.setupFog(0, partialTicks);
        GlStateManager.enableBlend();
        GlStateManager.depthMask(false);
        this.mc.getTextureManager().bindTexture(TextureMap.locationBlocksTexture);
        GlStateManager.shadeModel(7425);
        this.mc.mcProfiler.endStartSection("translucent");
        if (flag) {
            Shaders.beginWater();
        }
        renderglobal.renderBlockLayer(EnumWorldBlockLayer.TRANSLUCENT, partialTicks, pass, entity);
        if (flag) {
            Shaders.endWater();
        }
        GlStateManager.shadeModel(7424);
        GlStateManager.depthMask(true);
        GlStateManager.enableCull();
        GlStateManager.disableBlend();
        GlStateManager.disableFog();
        if (entity.posY + entity.getEyeHeight() >= 128.0 + this.mc.gameSettings.ofCloudsHeight * 128.0f) {
            this.mc.mcProfiler.endStartSection("aboveClouds");
            this.renderCloudsCheck(renderglobal, partialTicks, pass);
        }
        Tenacity.INSTANCE.getEventProtocol().handleEvent(new Render3DEvent(partialTicks));
        this.mc.mcProfiler.endStartSection("hand");
        this.getClass();
        if (!Shaders.isShadowPass) {
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
            this.renderWorldDirections(partialTicks);
        }
        if (flag) {
            Shaders.endRender();
        }
    }
    
    private void renderCloudsCheck(final RenderGlobal renderGlobalIn, final float partialTicks, final int pass) {
        if (this.mc.gameSettings.renderDistanceChunks >= 4 && !Config.isCloudsOff() && Shaders.shouldRenderClouds(this.mc.gameSettings)) {
            this.mc.mcProfiler.endStartSection("clouds");
            GlStateManager.matrixMode(5889);
            GlStateManager.loadIdentity();
            Project.gluPerspective(this.getFOVModifier(partialTicks, true), this.mc.displayWidth / (float)this.mc.displayHeight, 0.05f, this.clipDistance * 4.0f);
            GlStateManager.matrixMode(5888);
            GlStateManager.pushMatrix();
            this.setupFog(0, partialTicks);
            renderGlobalIn.renderClouds(partialTicks, pass);
            GlStateManager.disableFog();
            GlStateManager.popMatrix();
            GlStateManager.matrixMode(5889);
            GlStateManager.loadIdentity();
            Project.gluPerspective(this.getFOVModifier(partialTicks, true), this.mc.displayWidth / (float)this.mc.displayHeight, 0.05f, this.clipDistance);
            GlStateManager.matrixMode(5888);
        }
    }
    
    private void addRainParticles() {
        float f = this.mc.theWorld.getRainStrength(1.0f);
        if (!Config.isRainFancy()) {
            f /= 2.0f;
        }
        if (f != 0.0f && Config.isRainSplash() && !Ambience.overrideSnow) {
            this.random.setSeed(this.rendererUpdateCount * 312987231L);
            final Entity entity = this.mc.getRenderViewEntity();
            final World world = this.mc.theWorld;
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
                final BlockPos blockpos2 = world.getPrecipitationHeight(blockpos.add(this.random.nextInt(i) - this.random.nextInt(i), 0, this.random.nextInt(i) - this.random.nextInt(i)));
                final BiomeGenBase biomegenbase = world.getBiomeGenForCoords(blockpos2);
                final BlockPos blockpos3 = blockpos2.down();
                final Block block = world.getBlockState(blockpos3).getBlock();
                if (blockpos2.getY() <= blockpos.getY() + i && blockpos2.getY() >= blockpos.getY() - i && biomegenbase.canRain() && biomegenbase.getFloatTemperature(blockpos2) >= 0.15f) {
                    final double d4 = this.random.nextDouble();
                    final double d5 = this.random.nextDouble();
                    if (block.getMaterial() == Material.lava) {
                        this.mc.theWorld.spawnParticle(EnumParticleTypes.SMOKE_NORMAL, blockpos2.getX() + d4, blockpos2.getY() + 0.1f - block.getBlockBoundsMinY(), blockpos2.getZ() + d5, 0.0, 0.0, 0.0, new int[0]);
                    }
                    else if (block.getMaterial() != Material.air) {
                        block.setBlockBoundsBasedOnState(world, blockpos3);
                        ++j;
                        if (this.random.nextInt(j) == 0) {
                            d0 = blockpos3.getX() + d4;
                            d2 = blockpos3.getY() + 0.1f + block.getBlockBoundsMaxY() - 1.0;
                            d3 = blockpos3.getZ() + d5;
                        }
                        this.mc.theWorld.spawnParticle(EnumParticleTypes.WATER_DROP, blockpos3.getX() + d4, blockpos3.getY() + 0.1f + block.getBlockBoundsMaxY(), blockpos3.getZ() + d5, 0.0, 0.0, 0.0, new int[0]);
                    }
                }
            }
            if (j > 0 && this.random.nextInt(3) < this.rainSoundCounter++) {
                this.rainSoundCounter = 0;
                if (d2 > blockpos.getY() + 1 && world.getPrecipitationHeight(blockpos).getY() > MathHelper.floor_float((float)blockpos.getY())) {
                    this.mc.theWorld.playSound(d0, d2, d3, "ambient.weather.rain", 0.1f, 0.5f, false);
                }
                else {
                    this.mc.theWorld.playSound(d0, d2, d3, "ambient.weather.rain", 0.2f, 1.0f, false);
                }
            }
        }
    }
    
    protected void renderRainSnow(final float partialTicks) {
        final float f5 = this.mc.theWorld.getRainStrength(partialTicks);
        if (f5 > 0.0f) {
            if (Config.isRainOff()) {
                return;
            }
            this.enableLightmap();
            final Entity entity = this.mc.getRenderViewEntity();
            final World world = this.mc.theWorld;
            final int i = MathHelper.floor_double(entity.posX);
            final int j = MathHelper.floor_double(entity.posY);
            final int k = MathHelper.floor_double(entity.posZ);
            final Tessellator tessellator = Tessellator.getInstance();
            final WorldRenderer worldrenderer = tessellator.getWorldRenderer();
            GlStateManager.disableCull();
            GL11.glNormal3f(0.0f, 1.0f, 0.0f);
            GlStateManager.enableBlend();
            GlStateManager.tryBlendFuncSeparate(770, 771, 1, 0);
            GlStateManager.alphaFunc(516, 0.1f);
            final double d0 = entity.lastTickPosX + (entity.posX - entity.lastTickPosX) * partialTicks;
            final double d2 = entity.lastTickPosY + (entity.posY - entity.lastTickPosY) * partialTicks;
            final double d3 = entity.lastTickPosZ + (entity.posZ - entity.lastTickPosZ) * partialTicks;
            final int l = MathHelper.floor_double(d2);
            int i2 = 5;
            if (Config.isRainFancy()) {
                i2 = 10;
            }
            int j2 = -1;
            final float f6 = this.rendererUpdateCount + partialTicks;
            worldrenderer.setTranslation(-d0, -d2, -d3);
            GlStateManager.color(1.0f, 1.0f, 1.0f, 1.0f);
            final BlockPos.MutableBlockPos blockpos$mutableblockpos = new BlockPos.MutableBlockPos();
            for (int k2 = k - i2; k2 <= k + i2; ++k2) {
                for (int l2 = i - i2; l2 <= i + i2; ++l2) {
                    final int i3 = (k2 - k + 16) * 32 + l2 - i + 16;
                    final double d4 = this.rainXCoords[i3] * 0.5;
                    final double d5 = this.rainYCoords[i3] * 0.5;
                    blockpos$mutableblockpos.set(l2, 0, k2);
                    final BiomeGenBase biomegenbase = world.getBiomeGenForCoords(blockpos$mutableblockpos);
                    if (biomegenbase.canRain() || biomegenbase.getEnableSnow()) {
                        final int j3 = world.getPrecipitationHeight(blockpos$mutableblockpos).getY();
                        int k3 = j - i2;
                        int l3 = j + i2;
                        if (k3 < j3) {
                            k3 = j3;
                        }
                        if (l3 < j3) {
                            l3 = j3;
                        }
                        final int i4 = Math.max(j3, l);
                        if (k3 != l3) {
                            this.random.setSeed(l2 * (long)l2 * 3121L + l2 * 45238971L ^ k2 * (long)k2 * 418711L + k2 * 13761L);
                            blockpos$mutableblockpos.set(l2, k3, k2);
                            final float f7 = biomegenbase.getFloatTemperature(blockpos$mutableblockpos);
                            if (world.getWorldChunkManager().getTemperatureAtHeight(f7, j3) >= 0.15f && !Ambience.overrideSnow) {
                                if (j2 != 0) {
                                    if (j2 >= 0) {
                                        tessellator.draw();
                                    }
                                    j2 = 0;
                                    this.mc.getTextureManager().bindTexture(EntityRenderer.locationRainPng);
                                    worldrenderer.begin(7, DefaultVertexFormats.PARTICLE_POSITION_TEX_COLOR_LMAP);
                                }
                                final double d6 = ((this.rendererUpdateCount + l2 * l2 * 3121 + l2 * 45238971 + k2 * k2 * 418711 + k2 * 13761 & 0x1F) + (double)partialTicks) / 32.0 * (3.0 + this.random.nextDouble());
                                final double d7 = l2 + 0.5f - entity.posX;
                                final double d8 = k2 + 0.5f - entity.posZ;
                                final float f8 = MathHelper.sqrt_double(d7 * d7 + d8 * d8) / i2;
                                final float f9 = ((1.0f - f8 * f8) * 0.5f + 0.5f) * f5;
                                blockpos$mutableblockpos.set(l2, i4, k2);
                                final int j4 = world.getCombinedLight(blockpos$mutableblockpos, 0);
                                final int k4 = j4 >> 16 & 0xFFFF;
                                final int l4 = j4 & 0xFFFF;
                                worldrenderer.pos(l2 - d4 + 0.5, k3, k2 - d5 + 0.5).tex(0.0, k3 * 0.25 + d6).color(1.0f, 1.0f, 1.0f, f9).lightmap(k4, l4).endVertex();
                                worldrenderer.pos(l2 + d4 + 0.5, k3, k2 + d5 + 0.5).tex(1.0, k3 * 0.25 + d6).color(1.0f, 1.0f, 1.0f, f9).lightmap(k4, l4).endVertex();
                                worldrenderer.pos(l2 + d4 + 0.5, l3, k2 + d5 + 0.5).tex(1.0, l3 * 0.25 + d6).color(1.0f, 1.0f, 1.0f, f9).lightmap(k4, l4).endVertex();
                                worldrenderer.pos(l2 - d4 + 0.5, l3, k2 - d5 + 0.5).tex(0.0, l3 * 0.25 + d6).color(1.0f, 1.0f, 1.0f, f9).lightmap(k4, l4).endVertex();
                            }
                            else {
                                if (j2 != 1) {
                                    if (j2 >= 0) {
                                        tessellator.draw();
                                    }
                                    j2 = 1;
                                    this.mc.getTextureManager().bindTexture(EntityRenderer.locationSnowPng);
                                    worldrenderer.begin(7, DefaultVertexFormats.PARTICLE_POSITION_TEX_COLOR_LMAP);
                                }
                                final double d9 = ((this.rendererUpdateCount & 0x1FF) + partialTicks) / 512.0f;
                                final double d10 = this.random.nextDouble() + f6 * 0.01 * (float)this.random.nextGaussian();
                                final double d11 = this.random.nextDouble() + f6 * (float)this.random.nextGaussian() * 0.001;
                                final double d12 = l2 + 0.5f - entity.posX;
                                final double d13 = k2 + 0.5f - entity.posZ;
                                final float f10 = MathHelper.sqrt_double(d12 * d12 + d13 * d13) / i2;
                                final float f11 = ((1.0f - f10 * f10) * 0.3f + 0.5f) * f5;
                                blockpos$mutableblockpos.set(l2, i4, k2);
                                final int i5 = (world.getCombinedLight(blockpos$mutableblockpos, 0) * 3 + 15728880) / 4;
                                final int j5 = i5 >> 16 & 0xFFFF;
                                final int k5 = i5 & 0xFFFF;
                                worldrenderer.pos(l2 - d4 + 0.5, k3, k2 - d5 + 0.5).tex(0.0 + d10, k3 * 0.25 + d9 + d11).color(1.0f, 1.0f, 1.0f, f11).lightmap(j5, k5).endVertex();
                                worldrenderer.pos(l2 + d4 + 0.5, k3, k2 + d5 + 0.5).tex(1.0 + d10, k3 * 0.25 + d9 + d11).color(1.0f, 1.0f, 1.0f, f11).lightmap(j5, k5).endVertex();
                                worldrenderer.pos(l2 + d4 + 0.5, l3, k2 + d5 + 0.5).tex(1.0 + d10, l3 * 0.25 + d9 + d11).color(1.0f, 1.0f, 1.0f, f11).lightmap(j5, k5).endVertex();
                                worldrenderer.pos(l2 - d4 + 0.5, l3, k2 - d5 + 0.5).tex(0.0 + d10, l3 * 0.25 + d9 + d11).color(1.0f, 1.0f, 1.0f, f11).lightmap(j5, k5).endVertex();
                            }
                        }
                    }
                }
            }
            if (j2 >= 0) {
                tessellator.draw();
            }
            worldrenderer.setTranslation(0.0, 0.0, 0.0);
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
        final World world = this.mc.theWorld;
        final Entity entity = this.mc.getRenderViewEntity();
        float f = 0.25f + 0.75f * this.mc.gameSettings.renderDistanceChunks / 32.0f;
        f = 1.0f - (float)Math.pow(f, 0.25);
        Vec3 vec3 = world.getSkyColor(this.mc.getRenderViewEntity(), partialTicks);
        vec3 = CustomColors.getWorldSkyColor(vec3, world, this.mc.getRenderViewEntity(), partialTicks);
        final float f2 = (float)vec3.xCoord;
        final float f3 = (float)vec3.yCoord;
        final float f4 = (float)vec3.zCoord;
        Vec3 vec4 = world.getFogColor(partialTicks);
        vec4 = CustomColors.getWorldFogColor(vec4, world, this.mc.getRenderViewEntity(), partialTicks);
        this.fogColorRed = (float)vec4.xCoord;
        this.fogColorGreen = (float)vec4.yCoord;
        this.fogColorBlue = (float)vec4.zCoord;
        if (this.mc.gameSettings.renderDistanceChunks >= 4) {
            final double d0 = -1.0;
            final Vec3 vec5 = (MathHelper.sin(world.getCelestialAngleRadians(partialTicks)) > 0.0f) ? new Vec3(d0, 0.0, 0.0) : new Vec3(1.0, 0.0, 0.0);
            float f5 = (float)entity.getLook(partialTicks).dotProduct(vec5);
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
        final Block block = ActiveRenderInfo.getBlockAtEntityViewpoint(this.mc.theWorld, entity, partialTicks);
        if (this.cloudFog) {
            final Vec3 vec6 = world.getCloudColour(partialTicks);
            this.fogColorRed = (float)vec6.xCoord;
            this.fogColorGreen = (float)vec6.yCoord;
            this.fogColorBlue = (float)vec6.zCoord;
        }
        else if (block.getMaterial() == Material.water) {
            float f11 = EnchantmentHelper.getRespiration(entity) * 0.2f;
            f11 = Config.limit(f11, 0.0f, 0.6f);
            if (entity instanceof EntityLivingBase && ((EntityLivingBase)entity).isPotionActive(Potion.waterBreathing)) {
                f11 = f11 * 0.3f + 0.6f;
            }
            this.fogColorRed = 0.02f + f11;
            this.fogColorGreen = 0.02f + f11;
            this.fogColorBlue = 0.2f + f11;
            final Vec3 vec7 = CustomColors.getUnderwaterColor(this.mc.theWorld, this.mc.getRenderViewEntity().posX, this.mc.getRenderViewEntity().posY + 1.0, this.mc.getRenderViewEntity().posZ);
            if (vec7 != null) {
                this.fogColorRed = (float)vec7.xCoord;
                this.fogColorGreen = (float)vec7.yCoord;
                this.fogColorBlue = (float)vec7.zCoord;
            }
        }
        else if (block.getMaterial() == Material.lava) {
            this.fogColorRed = 0.6f;
            this.fogColorGreen = 0.1f;
            this.fogColorBlue = 0.0f;
            final Vec3 vec8 = CustomColors.getUnderlavaColor(this.mc.theWorld, this.mc.getRenderViewEntity().posX, this.mc.getRenderViewEntity().posY + 1.0, this.mc.getRenderViewEntity().posZ);
            if (vec8 != null) {
                this.fogColorRed = (float)vec8.xCoord;
                this.fogColorGreen = (float)vec8.yCoord;
                this.fogColorBlue = (float)vec8.zCoord;
            }
        }
        final float f12 = this.fogColor2 + (this.fogColor1 - this.fogColor2) * partialTicks;
        this.fogColorRed *= f12;
        this.fogColorGreen *= f12;
        this.fogColorBlue *= f12;
        double d2 = (entity.lastTickPosY + (entity.posY - entity.lastTickPosY) * partialTicks) * world.provider.getVoidFogYFactor();
        if (entity instanceof EntityLivingBase && ((EntityLivingBase)entity).isPotionActive(Potion.blindness)) {
            final int i = ((EntityLivingBase)entity).getActivePotionEffect(Potion.blindness).getDuration();
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
        if (entity instanceof EntityLivingBase && ((EntityLivingBase)entity).isPotionActive(Potion.nightVision)) {
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
        Shaders.setClearColor(this.fogColorRed, this.fogColorGreen, this.fogColorBlue, 0.0f);
    }
    
    private void setupFog(final int startCoords, final float partialTicks) {
        this.fogStandard = false;
        final Entity entity = this.mc.getRenderViewEntity();
        GL11.glFog(2918, this.setFogColorBuffer(this.fogColorRed, this.fogColorGreen, this.fogColorBlue));
        GL11.glNormal3f(0.0f, -1.0f, 0.0f);
        GlStateManager.color(1.0f, 1.0f, 1.0f, 1.0f);
        final Block block = ActiveRenderInfo.getBlockAtEntityViewpoint(this.mc.theWorld, entity, partialTicks);
        if (entity instanceof EntityLivingBase && ((EntityLivingBase)entity).isPotionActive(Potion.blindness)) {
            float f4 = 5.0f;
            final int i = ((EntityLivingBase)entity).getActivePotionEffect(Potion.blindness).getDuration();
            if (i < 20) {
                f4 = 5.0f + (this.farPlaneDistance - 5.0f) * (1.0f - i / 20.0f);
            }
            GlStateManager.setFog(9729);
            if (startCoords == -1) {
                GlStateManager.setFogStart(0.0f);
                GlStateManager.setFogEnd(f4 * 0.8f);
            }
            else {
                GlStateManager.setFogStart(f4 * 0.25f);
                GlStateManager.setFogEnd(f4);
            }
            if (GLContext.getCapabilities().GL_NV_fog_distance && Config.isFogFancy()) {
                GL11.glFogi(34138, 34139);
            }
        }
        else if (this.cloudFog) {
            GlStateManager.setFog(2048);
            GlStateManager.setFogDensity(0.1f);
        }
        else if (block.getMaterial() == Material.water) {
            GlStateManager.setFog(2048);
            final float f5 = Config.isClearWater() ? 0.02f : 0.1f;
            if (entity instanceof EntityLivingBase && ((EntityLivingBase)entity).isPotionActive(Potion.waterBreathing)) {
                GlStateManager.setFogDensity(0.01f);
            }
            else {
                final float f6 = 0.1f - EnchantmentHelper.getRespiration(entity) * 0.03f;
                GlStateManager.setFogDensity(Config.limit(f6, 0.0f, f5));
            }
        }
        else if (block.getMaterial() == Material.lava) {
            GlStateManager.setFog(2048);
            GlStateManager.setFogDensity(2.0f);
        }
        else {
            final float f7 = this.farPlaneDistance;
            this.fogStandard = true;
            GlStateManager.setFog(9729);
            if (startCoords == -1) {
                GlStateManager.setFogStart(0.0f);
                GlStateManager.setFogEnd(f7);
            }
            else {
                GlStateManager.setFogStart(f7 * Config.getFogStart());
                GlStateManager.setFogEnd(f7);
            }
            if (GLContext.getCapabilities().GL_NV_fog_distance) {
                if (Config.isFogFancy()) {
                    GL11.glFogi(34138, 34139);
                }
                if (Config.isFogFast()) {
                    GL11.glFogi(34138, 34140);
                }
            }
            if (this.mc.theWorld.provider.doesXZShowFog((int)entity.posX, (int)entity.posZ)) {
                GlStateManager.setFogStart(f7 * 0.05f);
                GlStateManager.setFogEnd(f7);
            }
        }
        GlStateManager.enableColorMaterial();
        GlStateManager.enableFog();
        GlStateManager.colorMaterial(1028, 4608);
    }
    
    private FloatBuffer setFogColorBuffer(final float red, final float green, final float blue) {
        if (Config.isShaders()) {
            Shaders.setFogColor(red, green, blue);
        }
        this.fogColorBuffer.clear();
        this.fogColorBuffer.put(red).put(green).put(blue).put(1.0f);
        this.fogColorBuffer.flip();
        return this.fogColorBuffer;
    }
    
    public MapItemRenderer getMapItemRenderer() {
        return this.theMapItemRenderer;
    }
    
    private void waitForServerThread() {
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
        final World world = this.mc.theWorld;
        if (world != null) {
            if (Config.getNewRelease() != null) {
                final String s = "HD_U".replace("HD_U", "HD Ultra").replace("L", "Light");
                final String s2 = s + " " + Config.getNewRelease();
                final ChatComponentText chatcomponenttext = new ChatComponentText(I18n.format("of.message.newVersion", "§n" + s2 + "§r"));
                chatcomponenttext.setChatStyle(new ChatStyle().setChatClickEvent(new ClickEvent(ClickEvent.Action.OPEN_URL, "https://optifine.net/downloads")));
                this.mc.ingameGUI.getChatGUI().printChatMessage(chatcomponenttext);
                Config.setNewRelease(null);
            }
            if (Config.isNotify64BitJava()) {
                Config.setNotify64BitJava(false);
                final ChatComponentText chatcomponenttext2 = new ChatComponentText(I18n.format("of.message.java64Bit", new Object[0]));
                this.mc.ingameGUI.getChatGUI().printChatMessage(chatcomponenttext2);
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
        if (this.mc.theWorld != null && Config.isShowGlErrors() && TimedEvent.isActive("CheckGlErrorFrameFinish", 10000L)) {
            final int i = GlStateManager.glGetError();
            if (i != 0 && GlErrors.isEnabled(i)) {
                final String s = Config.getGlErrorString(i);
                final ChatComponentText chatcomponenttext = new ChatComponentText(I18n.format("of.message.openglError", i, s));
                this.mc.ingameGUI.getChatGUI().printChatMessage(chatcomponenttext);
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
        if (this.theShaderGroup != null && this.theShaderGroup != this.fxaaShaders[2] && this.theShaderGroup != this.fxaaShaders[4]) {
            return true;
        }
        if (p_setFxaaShader_1_ != 2 && p_setFxaaShader_1_ != 4) {
            if (this.theShaderGroup != null) {
                this.theShaderGroup.deleteShaderGroup();
                this.theShaderGroup = null;
            }
            return true;
        }
        if (this.theShaderGroup != null && this.theShaderGroup == this.fxaaShaders[p_setFxaaShader_1_]) {
            return true;
        }
        if (this.mc.theWorld == null) {
            return true;
        }
        this.loadShader(new ResourceLocation("shaders/post/fxaa_of_" + p_setFxaaShader_1_ + "x.json"));
        this.fxaaShaders[p_setFxaaShader_1_] = this.theShaderGroup;
        return this.useShader;
    }
    
    private void checkLoadVisibleChunks(final Entity p_checkLoadVisibleChunks_1_, final float p_checkLoadVisibleChunks_2_, final ICamera p_checkLoadVisibleChunks_3_, final boolean p_checkLoadVisibleChunks_4_) {
        final int i = 201435902;
        if (this.loadVisibleChunks) {
            this.loadVisibleChunks = false;
            this.loadAllVisibleChunks(p_checkLoadVisibleChunks_1_, p_checkLoadVisibleChunks_2_, p_checkLoadVisibleChunks_3_, p_checkLoadVisibleChunks_4_);
            this.mc.ingameGUI.getChatGUI().deleteChatLine(i);
        }
        if (Keyboard.isKeyDown(61) && Keyboard.isKeyDown(38)) {
            if (this.mc.currentScreen != null) {
                return;
            }
            this.loadVisibleChunks = true;
            final ChatComponentText chatcomponenttext = new ChatComponentText(I18n.format("of.message.loadingVisibleChunks", new Object[0]));
            this.mc.ingameGUI.getChatGUI().printChatMessageWithOptionalDeletion(chatcomponenttext, i);
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
            Config.dbg("Loading visible chunks");
            long l = System.currentTimeMillis() + 5000L;
            int i2 = 0;
            boolean flag2;
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
    
    static {
        logger = LogManager.getLogger();
        locationRainPng = new ResourceLocation("textures/environment/rain.png");
        locationSnowPng = new ResourceLocation("textures/environment/snow.png");
        shaderResourceLocations = new ResourceLocation[] { new ResourceLocation("shaders/post/notch.json"), new ResourceLocation("shaders/post/fxaa.json"), new ResourceLocation("shaders/post/art.json"), new ResourceLocation("shaders/post/bumpy.json"), new ResourceLocation("shaders/post/blobs2.json"), new ResourceLocation("shaders/post/pencil.json"), new ResourceLocation("shaders/post/color_convolve.json"), new ResourceLocation("shaders/post/deconverge.json"), new ResourceLocation("shaders/post/flip.json"), new ResourceLocation("shaders/post/invert.json"), new ResourceLocation("shaders/post/ntsc.json"), new ResourceLocation("shaders/post/outline.json"), new ResourceLocation("shaders/post/phosphor.json"), new ResourceLocation("shaders/post/scan_pincushion.json"), new ResourceLocation("shaders/post/sobel.json"), new ResourceLocation("shaders/post/bits.json"), new ResourceLocation("shaders/post/desaturate.json"), new ResourceLocation("shaders/post/green.json"), new ResourceLocation("shaders/post/blur.json"), new ResourceLocation("shaders/post/wobble.json"), new ResourceLocation("shaders/post/blobs.json"), new ResourceLocation("shaders/post/antialias.json"), new ResourceLocation("shaders/post/creeper.json"), new ResourceLocation("shaders/post/spider.json") };
        shaderCount = EntityRenderer.shaderResourceLocations.length;
    }
}
