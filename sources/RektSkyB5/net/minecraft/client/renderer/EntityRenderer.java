/*
 * Decompiled with CFR 0.152.
 */
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
import net.minecraft.client.renderer.ActiveRenderInfo;
import net.minecraft.client.renderer.EntityRenderer1;
import net.minecraft.client.renderer.EntityRenderer2;
import net.minecraft.client.renderer.GLAllocation;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.ItemRenderer;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.client.renderer.RenderGlobal;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.WorldRenderer;
import net.minecraft.client.renderer.culling.ClippingHelperImpl;
import net.minecraft.client.renderer.culling.Frustum;
import net.minecraft.client.renderer.texture.DynamicTexture;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
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
import optfine.Config;
import optfine.CustomColorizer;
import optfine.Lagometer;
import optfine.RandomMobs;
import optfine.Reflector;
import optfine.RenderPlayerOF;
import optfine.TextureUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GLContext;
import org.lwjgl.util.glu.GLU;
import org.lwjgl.util.glu.Project;
import tk.rektsky.event.EventManager;
import tk.rektsky.event.impl.EventRender3D;
import tk.rektsky.event.impl.RenderEvent;
import tk.rektsky.module.ModulesManager;
import tk.rektsky.module.impl.render.AmbianceControl;
import tk.rektsky.module.impl.render.NoClientRender;

public class EntityRenderer
implements IResourceManagerReloadListener {
    private static final Logger logger = LogManager.getLogger();
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
    private float thirdPersonDistance = 4.0f;
    private float thirdPersonDistanceTemp = 4.0f;
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
    private long prevFrameTime = Minecraft.getSystemTime();
    private long renderEndNanoTime;
    private final DynamicTexture lightmapTexture;
    private final int[] lightmapColors;
    private final ResourceLocation locationLightMap;
    private boolean lightmapUpdateNeeded;
    private float torchFlickerX;
    private float torchFlickerDX;
    private int rainSoundCounter;
    private float[] rainXCoords = new float[1024];
    private float[] rainYCoords = new float[1024];
    private FloatBuffer fogColorBuffer = GLAllocation.createDirectFloatBuffer(16);
    public float fogColorRed;
    public float fogColorGreen;
    public float fogColorBlue;
    private float fogColor2;
    private float fogColor1;
    private int debugViewDirection = 0;
    private boolean debugView = false;
    private double cameraZoom = 1.0;
    private double cameraYaw;
    private double cameraPitch;
    private ShaderGroup theShaderGroup;
    private static final ResourceLocation[] shaderResourceLocations;
    public static final int shaderCount;
    private int shaderIndex = shaderCount;
    private boolean useShader = false;
    private int frameCount = 0;
    private static final String __OBFID = "CL_00000947";
    private boolean initialized = false;
    private World updatedWorld = null;
    private boolean showDebugInfo = false;
    public boolean fogStandard = false;
    private float clipDistance = 128.0f;
    private long lastServerTime = 0L;
    private int lastServerTicks = 0;
    private int serverWaitTime = 0;
    private int serverWaitTimeCurrent = 0;
    private float avgServerTimeDiff = 0.0f;
    private float avgServerTickDiff = 0.0f;
    private long lastErrorCheckTimeMs = 0L;

    public EntityRenderer(Minecraft mcIn, IResourceManager resourceManagerIn) {
        this.mc = mcIn;
        this.resourceManager = resourceManagerIn;
        this.itemRenderer = mcIn.getItemRenderer();
        this.theMapItemRenderer = new MapItemRenderer(mcIn.getTextureManager());
        this.lightmapTexture = new DynamicTexture(16, 16);
        this.locationLightMap = mcIn.getTextureManager().getDynamicTextureLocation("lightMap", this.lightmapTexture);
        this.lightmapColors = this.lightmapTexture.getTextureData();
        this.theShaderGroup = null;
        for (int i2 = 0; i2 < 32; ++i2) {
            for (int j2 = 0; j2 < 32; ++j2) {
                float f2 = j2 - 16;
                float f1 = i2 - 16;
                float f22 = MathHelper.sqrt_float(f2 * f2 + f1 * f1);
                this.rainXCoords[i2 << 5 | j2] = -f1 / f22;
                this.rainYCoords[i2 << 5 | j2] = f2 / f22;
            }
        }
    }

    public boolean isShaderActive() {
        return OpenGlHelper.shadersSupported && this.theShaderGroup != null;
    }

    public void func_181022_b() {
        if (this.theShaderGroup != null) {
            this.theShaderGroup.deleteShaderGroup();
        }
        this.theShaderGroup = null;
        this.shaderIndex = shaderCount;
    }

    public void switchUseShader() {
        this.useShader = !this.useShader;
    }

    public void loadEntityShader(Entity entityIn) {
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
            }
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

    public void loadShader(ResourceLocation resourceLocationIn) {
        try {
            this.theShaderGroup = new ShaderGroup(this.mc.getTextureManager(), this.resourceManager, this.mc.getFramebuffer(), resourceLocationIn);
            this.theShaderGroup.createBindFramebuffers(this.mc.displayWidth, this.mc.displayHeight);
            this.useShader = true;
        }
        catch (IOException ioexception) {
            logger.warn("Failed to load shader: " + resourceLocationIn, (Throwable)ioexception);
            this.shaderIndex = shaderCount;
            this.useShader = false;
        }
        catch (JsonSyntaxException jsonsyntaxexception) {
            logger.warn("Failed to load shader: " + resourceLocationIn, (Throwable)jsonsyntaxexception);
            this.shaderIndex = shaderCount;
            this.useShader = false;
        }
    }

    public void stopUseShader() {
        this.shaderIndex = shaderCount;
        this.useShader = false;
    }

    @Override
    public void onResourceManagerReload(IResourceManager resourceManager) {
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

    public void updateRenderer() {
        if (OpenGlHelper.shadersSupported && ShaderLinkHelper.getStaticShaderLinkHelper() == null) {
            ShaderLinkHelper.setNewStaticShaderLinkHelper();
        }
        this.updateFovModifierHand();
        this.updateTorchFlicker();
        this.fogColor2 = this.fogColor1;
        this.thirdPersonDistanceTemp = this.thirdPersonDistance;
        if (this.mc.gameSettings.smoothCamera) {
            float f2 = this.mc.gameSettings.mouseSensitivity * 0.6f + 0.2f;
            float f1 = f2 * f2 * f2 * 8.0f;
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
            this.mc.setRenderViewEntity(this.mc.thePlayer);
        }
        float f3 = this.mc.theWorld.getLightBrightness(new BlockPos(this.mc.getRenderViewEntity()));
        float f4 = (float)this.mc.gameSettings.renderDistanceChunks / 32.0f;
        float f2 = f3 * (1.0f - f4) + f4;
        this.fogColor1 += (f2 - this.fogColor1) * 0.1f;
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
        } else if (this.bossColorModifier > 0.0f) {
            this.bossColorModifier -= 0.0125f;
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
        if (entity != null && this.mc.theWorld != null) {
            this.mc.mcProfiler.startSection("pick");
            this.mc.pointedEntity = null;
            double d0 = this.mc.playerController.getBlockReachDistance();
            this.mc.objectMouseOver = entity.rayTrace(d0, partialTicks);
            double d1 = d0;
            Vec3 vec3 = entity.getPositionEyes(partialTicks);
            boolean flag = false;
            boolean flag1 = true;
            if (this.mc.playerController.extendedReach()) {
                d0 = 6.0;
                d1 = 6.0;
            } else if (d0 > 3.0) {
                flag = true;
            }
            if (this.mc.objectMouseOver != null) {
                d1 = this.mc.objectMouseOver.hitVec.distanceTo(vec3);
            }
            Vec3 vec31 = entity.getLook(partialTicks);
            Vec3 vec32 = vec3.addVector(vec31.xCoord * d0, vec31.yCoord * d0, vec31.zCoord * d0);
            this.pointedEntity = null;
            Vec3 vec33 = null;
            float f2 = 1.0f;
            List<Entity> list = this.mc.theWorld.getEntitiesInAABBexcluding(entity, entity.getEntityBoundingBox().addCoord(vec31.xCoord * d0, vec31.yCoord * d0, vec31.zCoord * d0).expand(f2, f2, f2), Predicates.and(EntitySelectors.NOT_SPECTATING, new EntityRenderer1(this)));
            double d2 = d1;
            for (int i2 = 0; i2 < list.size(); ++i2) {
                double d3;
                Entity entity1 = list.get(i2);
                float f1 = entity1.getCollisionBorderSize();
                AxisAlignedBB axisalignedbb = entity1.getEntityBoundingBox().expand(f1, f1, f1);
                MovingObjectPosition movingobjectposition = axisalignedbb.calculateIntercept(vec3, vec32);
                if (axisalignedbb.isVecInside(vec3)) {
                    if (!(d2 >= 0.0)) continue;
                    this.pointedEntity = entity1;
                    vec33 = movingobjectposition == null ? vec3 : movingobjectposition.hitVec;
                    d2 = 0.0;
                    continue;
                }
                if (movingobjectposition == null || !((d3 = vec3.distanceTo(movingobjectposition.hitVec)) < d2) && d2 != 0.0) continue;
                boolean flag2 = false;
                if (Reflector.ForgeEntity_canRiderInteract.exists()) {
                    flag2 = Reflector.callBoolean(entity1, Reflector.ForgeEntity_canRiderInteract, new Object[0]);
                }
                if (entity1 == entity.ridingEntity && !flag2) {
                    if (d2 != 0.0) continue;
                    this.pointedEntity = entity1;
                    vec33 = movingobjectposition.hitVec;
                    continue;
                }
                this.pointedEntity = entity1;
                vec33 = movingobjectposition.hitVec;
                d2 = d3;
            }
            if (this.pointedEntity != null && flag && vec3.distanceTo(vec33) > 3.0) {
                this.pointedEntity = null;
                this.mc.objectMouseOver = new MovingObjectPosition(MovingObjectPosition.MovingObjectType.MISS, vec33, null, new BlockPos(vec33));
            }
            if (this.pointedEntity != null && (d2 < d1 || this.mc.objectMouseOver == null)) {
                this.mc.objectMouseOver = new MovingObjectPosition(this.pointedEntity, vec33);
                if (this.pointedEntity instanceof EntityLivingBase || this.pointedEntity instanceof EntityItemFrame) {
                    this.mc.pointedEntity = this.pointedEntity;
                }
            }
            this.mc.mcProfiler.endSection();
        }
    }

    private void updateFovModifierHand() {
        float f2 = 1.0f;
        if (this.mc.getRenderViewEntity() instanceof AbstractClientPlayer) {
            AbstractClientPlayer abstractclientplayer = (AbstractClientPlayer)this.mc.getRenderViewEntity();
            f2 = abstractclientplayer.getFovModifier();
        }
        this.fovModifierHandPrev = this.fovModifierHand;
        this.fovModifierHand += (f2 - this.fovModifierHand) * 0.5f;
        if (this.fovModifierHand > 1.5f) {
            this.fovModifierHand = 1.5f;
        }
        if (this.fovModifierHand < 0.1f) {
            this.fovModifierHand = 0.1f;
        }
    }

    private float getFOVModifier(float partialTicks, boolean p_78481_2_) {
        Block block;
        if (this.debugView) {
            return 90.0f;
        }
        Entity entity = this.mc.getRenderViewEntity();
        float f2 = 70.0f;
        if (p_78481_2_) {
            f2 = this.mc.gameSettings.fovSetting;
            f2 *= this.fovModifierHandPrev + (this.fovModifierHand - this.fovModifierHandPrev) * partialTicks;
        }
        boolean flag = false;
        if (this.mc.currentScreen == null) {
            GameSettings gamesettings = this.mc.gameSettings;
            flag = GameSettings.isKeyDown(this.mc.gameSettings.ofKeyBindZoom);
        }
        if (flag) {
            if (!Config.zoomMode) {
                Config.zoomMode = true;
                this.mc.gameSettings.smoothCamera = true;
            }
            if (Config.zoomMode) {
                f2 /= 4.0f;
            }
        } else if (Config.zoomMode) {
            Config.zoomMode = false;
            this.mc.gameSettings.smoothCamera = false;
            this.mouseFilterXAxis = new MouseFilter();
            this.mouseFilterYAxis = new MouseFilter();
            this.mc.renderGlobal.displayListEntitiesDirty = true;
        }
        if (entity instanceof EntityLivingBase && ((EntityLivingBase)entity).getHealth() <= 0.0f) {
            float f1 = (float)((EntityLivingBase)entity).deathTime + partialTicks;
            f2 /= (1.0f - 500.0f / (f1 + 500.0f)) * 2.0f + 1.0f;
        }
        if ((block = ActiveRenderInfo.getBlockAtEntityViewpoint(this.mc.theWorld, entity, partialTicks)).getMaterial() == Material.water) {
            f2 = f2 * 60.0f / 70.0f;
        }
        return f2;
    }

    private void hurtCameraEffect(float partialTicks) {
        if (this.mc.getRenderViewEntity() instanceof EntityLivingBase) {
            EntityLivingBase entitylivingbase = (EntityLivingBase)this.mc.getRenderViewEntity();
            float f2 = (float)entitylivingbase.hurtTime - partialTicks;
            if (entitylivingbase.getHealth() <= 0.0f) {
                float f1 = (float)entitylivingbase.deathTime + partialTicks;
                GlStateManager.rotate(40.0f - 8000.0f / (f1 + 200.0f), 0.0f, 0.0f, 1.0f);
            }
            if (f2 < 0.0f) {
                return;
            }
            f2 /= (float)entitylivingbase.maxHurtTime;
            f2 = MathHelper.sin(f2 * f2 * f2 * f2 * (float)Math.PI);
            float f22 = entitylivingbase.attackedAtYaw;
            GlStateManager.rotate(-f22, 0.0f, 1.0f, 0.0f);
            GlStateManager.rotate(-f2 * 14.0f, 0.0f, 0.0f, 1.0f);
            GlStateManager.rotate(f22, 0.0f, 1.0f, 0.0f);
        }
    }

    private void setupViewBobbing(float partialTicks) {
        if (this.mc.getRenderViewEntity() instanceof EntityPlayer) {
            EntityPlayer entityplayer = (EntityPlayer)this.mc.getRenderViewEntity();
            float f2 = entityplayer.distanceWalkedModified - entityplayer.prevDistanceWalkedModified;
            float f1 = -(entityplayer.distanceWalkedModified + f2 * partialTicks);
            float f22 = entityplayer.prevCameraYaw + (entityplayer.cameraYaw - entityplayer.prevCameraYaw) * partialTicks;
            float f3 = entityplayer.prevCameraPitch + (entityplayer.cameraPitch - entityplayer.prevCameraPitch) * partialTicks;
            GlStateManager.translate(MathHelper.sin(f1 * (float)Math.PI) * f22 * 0.5f, -Math.abs(MathHelper.cos(f1 * (float)Math.PI) * f22), 0.0f);
            GlStateManager.rotate(MathHelper.sin(f1 * (float)Math.PI) * f22 * 3.0f, 0.0f, 0.0f, 1.0f);
            GlStateManager.rotate(Math.abs(MathHelper.cos(f1 * (float)Math.PI - 0.2f) * f22) * 5.0f, 1.0f, 0.0f, 0.0f);
            GlStateManager.rotate(f3, 1.0f, 0.0f, 0.0f);
        }
    }

    private void orientCamera(float partialTicks) {
        Entity entity = this.mc.getRenderViewEntity();
        float f2 = entity.getEyeHeight();
        double d0 = entity.prevPosX + (entity.posX - entity.prevPosX) * (double)partialTicks;
        double d1 = entity.prevPosY + (entity.posY - entity.prevPosY) * (double)partialTicks + (double)f2;
        double d2 = entity.prevPosZ + (entity.posZ - entity.prevPosZ) * (double)partialTicks;
        if (entity instanceof EntityLivingBase && ((EntityLivingBase)entity).isPlayerSleeping()) {
            f2 = (float)((double)f2 + 1.0);
            GlStateManager.translate(0.0f, 0.3f, 0.0f);
            if (!this.mc.gameSettings.debugCamEnable) {
                BlockPos blockpos = new BlockPos(entity);
                IBlockState iblockstate = this.mc.theWorld.getBlockState(blockpos);
                Block block = iblockstate.getBlock();
                if (Reflector.ForgeHooksClient_orientBedCamera.exists()) {
                    Reflector.callVoid(Reflector.ForgeHooksClient_orientBedCamera, this.mc.theWorld, blockpos, iblockstate, entity);
                } else if (block == Blocks.bed) {
                    int j2 = iblockstate.getValue(BlockBed.FACING).getHorizontalIndex();
                    GlStateManager.rotate(j2 * 90, 0.0f, 1.0f, 0.0f);
                }
                GlStateManager.rotate(entity.prevRotationYaw + (entity.rotationYaw - entity.prevRotationYaw) * partialTicks + 180.0f, 0.0f, -1.0f, 0.0f);
                GlStateManager.rotate(entity.prevRotationPitch + (entity.rotationPitch - entity.prevRotationPitch) * partialTicks, -1.0f, 0.0f, 0.0f);
            }
        } else if (this.mc.gameSettings.thirdPersonView > 0) {
            double d3 = this.thirdPersonDistanceTemp + (this.thirdPersonDistance - this.thirdPersonDistanceTemp) * partialTicks;
            if (this.mc.gameSettings.debugCamEnable) {
                GlStateManager.translate(0.0f, 0.0f, (float)(-d3));
            } else {
                float f1 = entity.rotationYaw;
                float f22 = entity.rotationPitch;
                if (this.mc.gameSettings.thirdPersonView == 2) {
                    f22 += 180.0f;
                }
                double d4 = (double)(-MathHelper.sin(f1 / 180.0f * (float)Math.PI) * MathHelper.cos(f22 / 180.0f * (float)Math.PI)) * d3;
                double d5 = (double)(MathHelper.cos(f1 / 180.0f * (float)Math.PI) * MathHelper.cos(f22 / 180.0f * (float)Math.PI)) * d3;
                double d6 = (double)(-MathHelper.sin(f22 / 180.0f * (float)Math.PI)) * d3;
                for (int i2 = 0; i2 < 8; ++i2) {
                    double d7;
                    MovingObjectPosition movingobjectposition;
                    float f3 = (i2 & 1) * 2 - 1;
                    float f4 = (i2 >> 1 & 1) * 2 - 1;
                    float f5 = (i2 >> 2 & 1) * 2 - 1;
                    if ((movingobjectposition = this.mc.theWorld.rayTraceBlocks(new Vec3(d0 + (double)(f3 *= 0.1f), d1 + (double)(f4 *= 0.1f), d2 + (double)(f5 *= 0.1f)), new Vec3(d0 - d4 + (double)f3 + (double)f5, d1 - d6 + (double)f4, d2 - d5 + (double)f5))) == null || !((d7 = movingobjectposition.hitVec.distanceTo(new Vec3(d0, d1, d2))) < d3)) continue;
                    d3 = d7;
                }
                if (this.mc.gameSettings.thirdPersonView == 2) {
                    GlStateManager.rotate(180.0f, 0.0f, 1.0f, 0.0f);
                }
                GlStateManager.rotate(entity.rotationPitch - f22, 1.0f, 0.0f, 0.0f);
                GlStateManager.rotate(entity.rotationYaw - f1, 0.0f, 1.0f, 0.0f);
                GlStateManager.translate(0.0f, 0.0f, (float)(-d3));
                GlStateManager.rotate(f1 - entity.rotationYaw, 0.0f, 1.0f, 0.0f);
                GlStateManager.rotate(f22 - entity.rotationPitch, 1.0f, 0.0f, 0.0f);
            }
        } else {
            GlStateManager.translate(0.0f, 0.0f, -0.1f);
        }
        if (!this.mc.gameSettings.debugCamEnable) {
            GlStateManager.rotate(entity.prevRotationPitch + (entity.rotationPitch - entity.prevRotationPitch) * partialTicks, 1.0f, 0.0f, 0.0f);
            if (entity instanceof EntityAnimal) {
                EntityAnimal entityanimal = (EntityAnimal)entity;
                GlStateManager.rotate(entityanimal.prevRotationYawHead + (entityanimal.rotationYawHead - entityanimal.prevRotationYawHead) * partialTicks + 180.0f, 0.0f, 1.0f, 0.0f);
            } else {
                GlStateManager.rotate(entity.prevRotationYaw + (entity.rotationYaw - entity.prevRotationYaw) * partialTicks + 180.0f, 0.0f, 1.0f, 0.0f);
            }
        }
        GlStateManager.translate(0.0f, -f2, 0.0f);
        d0 = entity.prevPosX + (entity.posX - entity.prevPosX) * (double)partialTicks;
        d1 = entity.prevPosY + (entity.posY - entity.prevPosY) * (double)partialTicks + (double)f2;
        d2 = entity.prevPosZ + (entity.posZ - entity.prevPosZ) * (double)partialTicks;
        this.cloudFog = this.mc.renderGlobal.hasCloudFog(d0, d1, d2, partialTicks);
    }

    private void setupCameraTransform(float partialTicks, int pass) {
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
        float f2 = 0.07f;
        if (this.mc.gameSettings.anaglyph) {
            GlStateManager.translate((float)(-(pass * 2 - 1)) * f2, 0.0f, 0.0f);
        }
        this.clipDistance = this.farPlaneDistance * 2.0f;
        if (this.clipDistance < 173.0f) {
            this.clipDistance = 173.0f;
        }
        if (this.mc.theWorld.provider.getDimensionId() == 1) {
            this.clipDistance = 256.0f;
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
        if ((f1 = this.mc.thePlayer.prevTimeInPortal + (this.mc.thePlayer.timeInPortal - this.mc.thePlayer.prevTimeInPortal) * partialTicks) > 0.0f) {
            int b0 = 20;
            if (this.mc.thePlayer.isPotionActive(Potion.confusion)) {
                b0 = 7;
            }
            float f22 = 5.0f / (f1 * f1 + 5.0f) - f1 * 0.04f;
            f22 *= f22;
            GlStateManager.rotate(((float)this.rendererUpdateCount + partialTicks) * (float)b0, 0.0f, 1.0f, 1.0f);
            GlStateManager.scale(1.0f / f22, 1.0f, 1.0f);
            GlStateManager.rotate(-((float)this.rendererUpdateCount + partialTicks) * (float)b0, 0.0f, 1.0f, 1.0f);
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

    private void renderHand(float partialTicks, int xOffset) {
        if (!this.debugView) {
            boolean flag;
            GlStateManager.matrixMode(5889);
            GlStateManager.loadIdentity();
            float f2 = 0.07f;
            if (this.mc.gameSettings.anaglyph) {
                GlStateManager.translate((float)(-(xOffset * 2 - 1)) * f2, 0.0f, 0.0f);
            }
            Project.gluPerspective(this.getFOVModifier(partialTicks, false), (float)this.mc.displayWidth / (float)this.mc.displayHeight, 0.05f, this.farPlaneDistance * 2.0f);
            GlStateManager.matrixMode(5888);
            GlStateManager.loadIdentity();
            if (this.mc.gameSettings.anaglyph) {
                GlStateManager.translate((float)(xOffset * 2 - 1) * 0.1f, 0.0f, 0.0f);
            }
            GlStateManager.pushMatrix();
            this.hurtCameraEffect(partialTicks);
            if (this.mc.gameSettings.viewBobbing) {
                this.setupViewBobbing(partialTicks);
            }
            boolean bl = flag = this.mc.getRenderViewEntity() instanceof EntityLivingBase && ((EntityLivingBase)this.mc.getRenderViewEntity()).isPlayerSleeping();
            if (!(this.mc.gameSettings.thirdPersonView != 0 || flag || this.mc.gameSettings.hideGUI || this.mc.playerController.isSpectator())) {
                this.enableLightmap();
                this.itemRenderer.renderItemInFirstPerson(partialTicks);
                this.disableLightmap();
            }
            GlStateManager.popMatrix();
            if (this.mc.gameSettings.thirdPersonView == 0 && !flag) {
                this.itemRenderer.renderOverlays(partialTicks);
                this.hurtCameraEffect(partialTicks);
            }
            if (this.mc.gameSettings.viewBobbing) {
                this.setupViewBobbing(partialTicks);
            }
        }
    }

    public void disableLightmap() {
        GlStateManager.setActiveTexture(OpenGlHelper.lightmapTexUnit);
        GlStateManager.disableTexture2D();
        GlStateManager.setActiveTexture(OpenGlHelper.defaultTexUnit);
    }

    public void enableLightmap() {
        GlStateManager.setActiveTexture(OpenGlHelper.lightmapTexUnit);
        GlStateManager.matrixMode(5890);
        GlStateManager.loadIdentity();
        float f2 = 0.00390625f;
        GlStateManager.scale(f2, f2, f2);
        GlStateManager.translate(8.0f, 8.0f, 8.0f);
        GlStateManager.matrixMode(5888);
        this.mc.getTextureManager().bindTexture(this.locationLightMap);
        GL11.glTexParameteri(3553, 10241, 9729);
        GL11.glTexParameteri(3553, 10240, 9729);
        GL11.glTexParameteri(3553, 10242, 10496);
        GL11.glTexParameteri(3553, 10243, 10496);
        GlStateManager.color(1.0f, 1.0f, 1.0f, 1.0f);
        GlStateManager.enableTexture2D();
        GlStateManager.setActiveTexture(OpenGlHelper.defaultTexUnit);
    }

    private void updateTorchFlicker() {
        this.torchFlickerDX = (float)((double)this.torchFlickerDX + (Math.random() - Math.random()) * Math.random() * Math.random());
        this.torchFlickerDX = (float)((double)this.torchFlickerDX * 0.9);
        this.torchFlickerX += (this.torchFlickerDX - this.torchFlickerX) * 1.0f;
        this.lightmapUpdateNeeded = true;
    }

    private void updateLightmap(float partialTicks) {
        if (this.lightmapUpdateNeeded) {
            this.mc.mcProfiler.startSection("lightTex");
            WorldClient worldclient = this.mc.theWorld;
            if (worldclient != null) {
                if (CustomColorizer.updateLightmap(worldclient, this.torchFlickerX, this.lightmapColors, this.mc.thePlayer.isPotionActive(Potion.nightVision))) {
                    this.lightmapTexture.updateDynamicTexture();
                    this.lightmapUpdateNeeded = false;
                    this.mc.mcProfiler.endSection();
                    return;
                }
                float f2 = worldclient.getSunBrightness(1.0f);
                float f1 = f2 * 0.95f + 0.05f;
                for (int i2 = 0; i2 < 256; ++i2) {
                    float f22 = worldclient.provider.getLightBrightnessTable()[i2 / 16] * f1;
                    float f3 = worldclient.provider.getLightBrightnessTable()[i2 % 16] * (this.torchFlickerX * 0.1f + 1.5f);
                    if (worldclient.getLastLightningBolt() > 0) {
                        f22 = worldclient.provider.getLightBrightnessTable()[i2 / 16];
                    }
                    float f4 = f22 * (f2 * 0.65f + 0.35f);
                    float f5 = f22 * (f2 * 0.65f + 0.35f);
                    float f6 = f3 * ((f3 * 0.6f + 0.4f) * 0.6f + 0.4f);
                    float f7 = f3 * (f3 * f3 * 0.6f + 0.4f);
                    float f8 = f4 + f3;
                    float f9 = f5 + f6;
                    float f10 = f22 + f7;
                    f8 = f8 * 0.96f + 0.03f;
                    f9 = f9 * 0.96f + 0.03f;
                    f10 = f10 * 0.96f + 0.03f;
                    if (this.bossColorModifier > 0.0f) {
                        float f11 = this.bossColorModifierPrev + (this.bossColorModifier - this.bossColorModifierPrev) * partialTicks;
                        f8 = f8 * (1.0f - f11) + f8 * 0.7f * f11;
                        f9 = f9 * (1.0f - f11) + f9 * 0.6f * f11;
                        f10 = f10 * (1.0f - f11) + f10 * 0.6f * f11;
                    }
                    if (worldclient.provider.getDimensionId() == 1) {
                        f8 = 0.22f + f3 * 0.75f;
                        f9 = 0.28f + f6 * 0.75f;
                        f10 = 0.25f + f7 * 0.75f;
                    }
                    if (this.mc.thePlayer.isPotionActive(Potion.nightVision)) {
                        float f15 = this.getNightVisionBrightness(this.mc.thePlayer, partialTicks);
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
                    int short1 = 255;
                    int j2 = (int)(f8 * 255.0f);
                    int k2 = (int)(f9 * 255.0f);
                    int l2 = (int)(f10 * 255.0f);
                    this.lightmapColors[i2] = short1 << 24 | j2 << 16 | k2 << 8 | l2;
                }
                this.lightmapTexture.updateDynamicTexture();
                this.lightmapUpdateNeeded = false;
                this.mc.mcProfiler.endSection();
            }
        }
    }

    private float getNightVisionBrightness(EntityLivingBase entitylivingbaseIn, float partialTicks) {
        int i2 = entitylivingbaseIn.getActivePotionEffect(Potion.nightVision).getDuration();
        return i2 > 200 ? 1.0f : 0.7f + MathHelper.sin(((float)i2 - partialTicks) * (float)Math.PI * 0.2f) * 0.3f;
    }

    public void func_181560_a(float p_181560_1_, long p_181560_2_) {
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
        if (flag && Minecraft.isRunningOnMac && this.mc.inGameHasFocus && !Mouse.isInsideWindow()) {
            Mouse.setGrabbed(false);
            Mouse.setCursorPosition(Display.getWidth() / 2, Display.getHeight() / 2);
            Mouse.setGrabbed(true);
        }
        if (this.mc.inGameHasFocus && flag) {
            this.mc.mouseHelper.mouseXYChange();
            float f2 = this.mc.gameSettings.mouseSensitivity * 0.6f + 0.2f;
            float f1 = f2 * f2 * f2 * 8.0f;
            float f22 = (float)this.mc.mouseHelper.deltaX * f1;
            float f3 = (float)this.mc.mouseHelper.deltaY * f1;
            int b0 = 1;
            if (this.mc.gameSettings.invertMouse) {
                b0 = -1;
            }
            if (this.mc.gameSettings.smoothCamera) {
                this.smoothCamYaw += f22;
                this.smoothCamPitch += f3;
                float f4 = p_181560_1_ - this.smoothCamPartialTicks;
                this.smoothCamPartialTicks = p_181560_1_;
                f22 = this.smoothCamFilterX * f4;
                f3 = this.smoothCamFilterY * f4;
                this.mc.thePlayer.setAngles(f22, f3 * (float)b0);
            } else {
                this.smoothCamYaw = 0.0f;
                this.smoothCamPitch = 0.0f;
                this.mc.thePlayer.setAngles(f22, f3 * (float)b0);
            }
        }
        this.mc.mcProfiler.endSection();
        if (!this.mc.skipRenderWorld) {
            anaglyphEnable = this.mc.gameSettings.anaglyph;
            final ScaledResolution scaledresolution = new ScaledResolution(this.mc);
            int l2 = scaledresolution.getScaledWidth();
            int i1 = scaledresolution.getScaledHeight();
            final int j1 = Mouse.getX() * l2 / this.mc.displayWidth;
            final int k1 = i1 - Mouse.getY() * i1 / this.mc.displayHeight - 1;
            int l1 = this.mc.gameSettings.limitFramerate;
            if (this.mc.theWorld != null) {
                this.mc.mcProfiler.startSection("level");
                int i2 = Math.min(Minecraft.getDebugFPS(), l1);
                i2 = Math.max(i2, 60);
                long j2 = System.nanoTime() - p_181560_2_;
                long k2 = Math.max((long)(1000000000 / i2 / 4) - j2, 0L);
                this.renderWorld(p_181560_1_, System.nanoTime() + k2);
                if (OpenGlHelper.shadersSupported) {
                    this.mc.renderGlobal.renderEntityOutlineFramebuffer();
                    if (this.theShaderGroup != null && this.useShader) {
                        GlStateManager.matrixMode(5890);
                        GlStateManager.pushMatrix();
                        GlStateManager.loadIdentity();
                        this.theShaderGroup.loadShaderGroup(p_181560_1_);
                        GlStateManager.popMatrix();
                    }
                    this.mc.getFramebuffer().bindFramebuffer(true);
                }
                this.renderEndNanoTime = System.nanoTime();
                this.mc.mcProfiler.endStartSection("gui");
                if (!this.mc.gameSettings.hideGUI || this.mc.currentScreen != null) {
                    GlStateManager.alphaFunc(516, 0.1f);
                    this.mc.ingameGUI.renderGameOverlay(p_181560_1_);
                    if (this.mc.gameSettings.ofShowFps && !this.mc.gameSettings.showDebugInfo) {
                        Config.drawFps();
                    }
                    if (this.mc.gameSettings.showDebugInfo) {
                        Lagometer.showLagometer(scaledresolution);
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
            }
            if (this.mc.currentScreen != null) {
                GlStateManager.clear(256);
                try {
                    if (Reflector.ForgeHooksClient_drawScreen.exists()) {
                        Reflector.callVoid(Reflector.ForgeHooksClient_drawScreen, this.mc.currentScreen, j1, k1, Float.valueOf(p_181560_1_));
                    } else {
                        this.mc.currentScreen.drawScreen(j1, k1, p_181560_1_);
                    }
                }
                catch (Throwable throwable) {
                    CrashReport crashreport = CrashReport.makeCrashReport(throwable, "Rendering screen");
                    CrashReportCategory crashreportcategory = crashreport.makeCategory("Screen render details");
                    crashreportcategory.addCrashSectionCallable("Screen name", new EntityRenderer2(this));
                    crashreportcategory.addCrashSectionCallable("Mouse location", new Callable(){
                        private static final String __OBFID = "CL_00000950";

                        public String call() throws Exception {
                            return String.format("Scaled: (%d, %d). Absolute: (%d, %d)", j1, k1, Mouse.getX(), Mouse.getY());
                        }
                    });
                    crashreportcategory.addCrashSectionCallable("Screen size", new Callable(){
                        private static final String __OBFID = "CL_00000951";

                        public String call() throws Exception {
                            return String.format("Scaled: (%d, %d). Absolute: (%d, %d). Scale factor of %d", scaledresolution.getScaledWidth(), scaledresolution.getScaledHeight(), ((EntityRenderer)EntityRenderer.this).mc.displayWidth, ((EntityRenderer)EntityRenderer.this).mc.displayHeight, scaledresolution.getScaleFactor());
                        }
                    });
                    throw new ReportedException(crashreport);
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

    public void renderStreamIndicator(float partialTicks) {
        this.setupOverlayRendering();
        this.mc.ingameGUI.renderStreamIndicator(new ScaledResolution(this.mc));
    }

    private boolean isDrawBlockOutline() {
        boolean flag;
        if (!this.drawBlockOutline) {
            return false;
        }
        Entity entity = this.mc.getRenderViewEntity();
        boolean bl = flag = entity instanceof EntityPlayer && !this.mc.gameSettings.hideGUI;
        if (flag && !((EntityPlayer)entity).capabilities.allowEdit) {
            ItemStack itemstack = ((EntityPlayer)entity).getCurrentEquippedItem();
            if (this.mc.objectMouseOver != null && this.mc.objectMouseOver.typeOfHit == MovingObjectPosition.MovingObjectType.BLOCK) {
                BlockPos blockpos = this.mc.objectMouseOver.getBlockPos();
                Block block = this.mc.theWorld.getBlockState(blockpos).getBlock();
                if (this.mc.playerController.getCurrentGameType() == WorldSettings.GameType.SPECTATOR) {
                    boolean flag1;
                    if (Reflector.ForgeBlock_hasTileEntity.exists()) {
                        IBlockState iblockstate = this.mc.theWorld.getBlockState(blockpos);
                        flag1 = Reflector.callBoolean(block, Reflector.ForgeBlock_hasTileEntity, iblockstate);
                    } else {
                        flag1 = block.hasTileEntity();
                    }
                    flag = flag1 && this.mc.theWorld.getTileEntity(blockpos) instanceof IInventory;
                } else {
                    flag = itemstack != null && (itemstack.canDestroy(block) || itemstack.canPlaceOn(block));
                }
            }
        }
        return flag;
    }

    private void renderWorldDirections(float partialTicks) {
        if (this.mc.gameSettings.showDebugInfo && !this.mc.gameSettings.hideGUI && !this.mc.thePlayer.hasReducedDebug() && !this.mc.gameSettings.reducedDebugInfo) {
            Entity entity = this.mc.getRenderViewEntity();
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
            RenderGlobal.drawBoundingBoxWithColor(new AxisAlignedBB(0.0, 0.0, 0.0, 0.005, 1.0E-4, 1.0E-4), 255, 0, 0, 255);
            RenderGlobal.drawBoundingBoxWithColor(new AxisAlignedBB(0.0, 0.0, 0.0, 1.0E-4, 1.0E-4, 0.005), 0, 0, 255, 255);
            RenderGlobal.drawBoundingBoxWithColor(new AxisAlignedBB(0.0, 0.0, 0.0, 1.0E-4, 0.0033, 1.0E-4), 0, 255, 0, 255);
            GlStateManager.popMatrix();
            GlStateManager.depthMask(true);
            GlStateManager.enableTexture2D();
            GlStateManager.disableBlend();
        }
    }

    public void renderWorld(float partialTicks, long finishTimeNano) {
        AmbianceControl ambianceControl = ModulesManager.getModuleByClass(AmbianceControl.class);
        if (ambianceControl.isToggled()) {
            this.mc.theWorld.setWorldTime((int)ambianceControl.time.getValue().doubleValue());
        }
        this.updateLightmap(partialTicks);
        if (this.mc.getRenderViewEntity() == null) {
            this.mc.setRenderViewEntity(this.mc.thePlayer);
        }
        this.getMouseOver(partialTicks);
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
        RenderGlobal renderglobal = this.mc.renderGlobal;
        EffectRenderer effectrenderer = this.mc.effectRenderer;
        boolean flag = this.isDrawBlockOutline();
        GlStateManager.enableCull();
        this.mc.mcProfiler.endStartSection("clear");
        GlStateManager.viewport(0, 0, this.mc.displayWidth, this.mc.displayHeight);
        this.updateFogColor(partialTicks);
        GlStateManager.clear(16640);
        this.mc.mcProfiler.endStartSection("camera");
        this.setupCameraTransform(partialTicks, pass);
        ActiveRenderInfo.updateRenderInfo(this.mc.thePlayer, this.mc.gameSettings.thirdPersonView == 2);
        this.mc.mcProfiler.endStartSection("frustum");
        ClippingHelperImpl.getInstance();
        this.mc.mcProfiler.endStartSection("culling");
        Frustum frustum = new Frustum();
        Entity entity = this.mc.getRenderViewEntity();
        double d0 = entity.lastTickPosX + (entity.posX - entity.lastTickPosX) * (double)partialTicks;
        double d1 = entity.lastTickPosY + (entity.posY - entity.lastTickPosY) * (double)partialTicks;
        double d2 = entity.lastTickPosZ + (entity.posZ - entity.lastTickPosZ) * (double)partialTicks;
        frustum.setPosition(d0, d1, d2);
        if (!(Config.isSkyEnabled() || Config.isSunMoonEnabled() || Config.isStarsEnabled())) {
            GlStateManager.disableBlend();
        } else {
            this.setupFog(-1, partialTicks);
            this.mc.mcProfiler.endStartSection("sky");
            GlStateManager.matrixMode(5889);
            GlStateManager.loadIdentity();
            Project.gluPerspective(this.getFOVModifier(partialTicks, true), (float)this.mc.displayWidth / (float)this.mc.displayHeight, 0.05f, this.clipDistance);
            GlStateManager.matrixMode(5888);
            renderglobal.renderSky(partialTicks, pass);
            GlStateManager.matrixMode(5889);
            GlStateManager.loadIdentity();
            Project.gluPerspective(this.getFOVModifier(partialTicks, true), (float)this.mc.displayWidth / (float)this.mc.displayHeight, 0.05f, this.clipDistance);
            GlStateManager.matrixMode(5888);
        }
        this.setupFog(0, partialTicks);
        GlStateManager.shadeModel(7425);
        if (entity.posY + (double)entity.getEyeHeight() < 128.0 + (double)(this.mc.gameSettings.ofCloudsHeight * 128.0f)) {
            this.renderCloudsCheck(renderglobal, partialTicks, pass);
        }
        this.mc.mcProfiler.endStartSection("prepareterrain");
        this.setupFog(0, partialTicks);
        this.mc.getTextureManager().bindTexture(TextureMap.locationBlocksTexture);
        RenderHelper.disableStandardItemLighting();
        this.mc.mcProfiler.endStartSection("terrain_setup");
        renderglobal.setupTerrain(entity, partialTicks, frustum, this.frameCount++, this.mc.thePlayer.isSpectator());
        if (pass == 0 || pass == 2) {
            this.mc.mcProfiler.endStartSection("updatechunks");
            Lagometer.timerChunkUpload.start();
            this.mc.renderGlobal.updateChunks(finishTimeNano);
            Lagometer.timerChunkUpload.end();
        }
        this.mc.mcProfiler.endStartSection("terrain");
        Lagometer.timerTerrain.start();
        if (this.mc.gameSettings.ofSmoothFps && pass > 0) {
            GL11.glFinish();
        }
        GlStateManager.matrixMode(5888);
        GlStateManager.pushMatrix();
        GlStateManager.disableAlpha();
        renderglobal.renderBlockLayer(EnumWorldBlockLayer.SOLID, partialTicks, pass, entity);
        GlStateManager.enableAlpha();
        renderglobal.renderBlockLayer(EnumWorldBlockLayer.CUTOUT_MIPPED, partialTicks, pass, entity);
        this.mc.getTextureManager().getTexture(TextureMap.locationBlocksTexture).setBlurMipmap(false, false);
        renderglobal.renderBlockLayer(EnumWorldBlockLayer.CUTOUT, partialTicks, pass, entity);
        this.mc.getTextureManager().getTexture(TextureMap.locationBlocksTexture).restoreLastBlurMipmap();
        Lagometer.timerTerrain.end();
        GlStateManager.shadeModel(7424);
        GlStateManager.alphaFunc(516, 0.1f);
        if (!this.debugView) {
            GlStateManager.matrixMode(5888);
            GlStateManager.popMatrix();
            if (ModulesManager.getModuleByClass(NoClientRender.class).isToggled()) {
                // empty if block
            }
            EventManager.callEvent(new RenderEvent(partialTicks));
            GlStateManager.pushMatrix();
            RenderHelper.enableStandardItemLighting();
            this.mc.mcProfiler.endStartSection("entities");
            if (Reflector.ForgeHooksClient_setRenderPass.exists()) {
                Reflector.callVoid(Reflector.ForgeHooksClient_setRenderPass, 0);
            }
            renderglobal.renderEntities(entity, frustum, partialTicks);
            if (Reflector.ForgeHooksClient_setRenderPass.exists()) {
                Reflector.callVoid(Reflector.ForgeHooksClient_setRenderPass, -1);
            }
            RenderHelper.disableStandardItemLighting();
            this.disableLightmap();
            GlStateManager.matrixMode(5888);
            GlStateManager.popMatrix();
            GlStateManager.pushMatrix();
            if (this.mc.objectMouseOver != null && entity.isInsideOfMaterial(Material.water) && flag) {
                EntityPlayer entityplayer = (EntityPlayer)entity;
                GlStateManager.disableAlpha();
                this.mc.mcProfiler.endStartSection("outline");
                boolean flag1 = Reflector.ForgeHooksClient_onDrawBlockHighlight.exists();
                if (!(flag1 && Reflector.callBoolean(Reflector.ForgeHooksClient_onDrawBlockHighlight, renderglobal, entityplayer, this.mc.objectMouseOver, 0, entityplayer.getHeldItem(), Float.valueOf(partialTicks)) || this.mc.gameSettings.hideGUI)) {
                    renderglobal.drawSelectionBox(entityplayer, this.mc.objectMouseOver, 0, partialTicks);
                }
                GlStateManager.enableAlpha();
            }
        }
        GlStateManager.matrixMode(5888);
        GlStateManager.popMatrix();
        if (flag && this.mc.objectMouseOver != null && !entity.isInsideOfMaterial(Material.water)) {
            EntityPlayer entityplayer1 = (EntityPlayer)entity;
            GlStateManager.disableAlpha();
            this.mc.mcProfiler.endStartSection("outline");
            boolean flag2 = Reflector.ForgeHooksClient_onDrawBlockHighlight.exists();
            if (!(flag2 && Reflector.callBoolean(Reflector.ForgeHooksClient_onDrawBlockHighlight, renderglobal, entityplayer1, this.mc.objectMouseOver, 0, entityplayer1.getHeldItem(), Float.valueOf(partialTicks)) || this.mc.gameSettings.hideGUI)) {
                renderglobal.drawSelectionBox(entityplayer1, this.mc.objectMouseOver, 0, partialTicks);
            }
            GlStateManager.enableAlpha();
        }
        this.mc.mcProfiler.endStartSection("destroyProgress");
        GlStateManager.enableBlend();
        GlStateManager.tryBlendFuncSeparate(770, 1, 1, 0);
        this.mc.getTextureManager().getTexture(TextureMap.locationBlocksTexture).setBlurMipmap(false, false);
        renderglobal.drawBlockDamageTexture(Tessellator.getInstance(), Tessellator.getInstance().getWorldRenderer(), entity, partialTicks);
        this.mc.getTextureManager().getTexture(TextureMap.locationBlocksTexture).restoreLastBlurMipmap();
        GlStateManager.disableBlend();
        if (!this.debugView) {
            this.enableLightmap();
            this.mc.mcProfiler.endStartSection("litParticles");
            effectrenderer.renderLitParticles(entity, partialTicks);
            RenderHelper.disableStandardItemLighting();
            this.setupFog(0, partialTicks);
            this.mc.mcProfiler.endStartSection("particles");
            effectrenderer.renderParticles(entity, partialTicks);
            this.disableLightmap();
        }
        GlStateManager.depthMask(false);
        GlStateManager.enableCull();
        this.mc.mcProfiler.endStartSection("weather");
        this.renderRainSnow(partialTicks);
        GlStateManager.depthMask(true);
        renderglobal.renderWorldBorder(entity, partialTicks);
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
        renderglobal.renderBlockLayer(EnumWorldBlockLayer.TRANSLUCENT, partialTicks, pass, entity);
        if (Reflector.ForgeHooksClient_setRenderPass.exists() && !this.debugView) {
            RenderHelper.enableStandardItemLighting();
            this.mc.mcProfiler.endStartSection("entities");
            Reflector.callVoid(Reflector.ForgeHooksClient_setRenderPass, 1);
            this.mc.renderGlobal.renderEntities(entity, frustum, partialTicks);
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
            this.renderCloudsCheck(renderglobal, partialTicks, pass);
        }
        if (Reflector.ForgeHooksClient_dispatchRenderLast.exists()) {
            this.mc.mcProfiler.endStartSection("forge_render_last");
            Reflector.callVoid(Reflector.ForgeHooksClient_dispatchRenderLast, renderglobal, Float.valueOf(partialTicks));
        }
        this.mc.mcProfiler.endStartSection("hand");
        EventRender3D event = new EventRender3D();
        EventManager.callEvent(event);
        boolean flag3 = Reflector.callBoolean(Reflector.ForgeHooksClient_renderFirstPersonHand, this.mc.renderGlobal, Float.valueOf(partialTicks), pass);
        if (!flag3 && this.renderHand) {
            GlStateManager.clear(256);
            this.renderHand(partialTicks, pass);
            this.renderWorldDirections(partialTicks);
        }
    }

    private void renderCloudsCheck(RenderGlobal renderGlobalIn, float partialTicks, int pass) {
        if (this.mc.gameSettings.renderDistanceChunks >= 4 && !Config.isCloudsOff()) {
            this.mc.mcProfiler.endStartSection("clouds");
            GlStateManager.matrixMode(5889);
            GlStateManager.loadIdentity();
            Project.gluPerspective(this.getFOVModifier(partialTicks, true), (float)this.mc.displayWidth / (float)this.mc.displayHeight, 0.05f, this.clipDistance * 4.0f);
            GlStateManager.matrixMode(5888);
            GlStateManager.pushMatrix();
            this.setupFog(0, partialTicks);
            renderGlobalIn.renderClouds(partialTicks, pass);
            GlStateManager.disableFog();
            GlStateManager.popMatrix();
            GlStateManager.matrixMode(5889);
            GlStateManager.loadIdentity();
            Project.gluPerspective(this.getFOVModifier(partialTicks, true), (float)this.mc.displayWidth / (float)this.mc.displayHeight, 0.05f, this.clipDistance);
            GlStateManager.matrixMode(5888);
        }
    }

    private void addRainParticles() {
        float f2 = this.mc.theWorld.getRainStrength(1.0f);
        if (!Config.isRainFancy()) {
            f2 /= 2.0f;
        }
        if (f2 != 0.0f && Config.isRainSplash()) {
            this.random.setSeed((long)this.rendererUpdateCount * 312987231L);
            Entity entity = this.mc.getRenderViewEntity();
            WorldClient worldclient = this.mc.theWorld;
            BlockPos blockpos = new BlockPos(entity);
            int b0 = 10;
            double d0 = 0.0;
            double d1 = 0.0;
            double d2 = 0.0;
            int i2 = 0;
            int j2 = (int)(100.0f * f2 * f2);
            if (this.mc.gameSettings.particleSetting == 1) {
                j2 >>= 1;
            } else if (this.mc.gameSettings.particleSetting == 2) {
                j2 = 0;
            }
            for (int k2 = 0; k2 < j2; ++k2) {
                BlockPos blockpos1 = worldclient.getPrecipitationHeight(blockpos.add(this.random.nextInt(b0) - this.random.nextInt(b0), 0, this.random.nextInt(b0) - this.random.nextInt(b0)));
                BiomeGenBase biomegenbase = worldclient.getBiomeGenForCoords(blockpos1);
                BlockPos blockpos2 = blockpos1.down();
                Block block = worldclient.getBlockState(blockpos2).getBlock();
                if (blockpos1.getY() > blockpos.getY() + b0 || blockpos1.getY() < blockpos.getY() - b0 || !biomegenbase.canSpawnLightningBolt() || !(biomegenbase.getFloatTemperature(blockpos1) >= 0.15f)) continue;
                double d3 = this.random.nextDouble();
                double d4 = this.random.nextDouble();
                if (block.getMaterial() == Material.lava) {
                    this.mc.theWorld.spawnParticle(EnumParticleTypes.SMOKE_NORMAL, (double)blockpos1.getX() + d3, (double)((float)blockpos1.getY() + 0.1f) - block.getBlockBoundsMinY(), (double)blockpos1.getZ() + d4, 0.0, 0.0, 0.0, new int[0]);
                    continue;
                }
                if (block.getMaterial() == Material.air) continue;
                block.setBlockBoundsBasedOnState(worldclient, blockpos2);
                if (this.random.nextInt(++i2) == 0) {
                    d0 = (double)blockpos2.getX() + d3;
                    d1 = (double)((float)blockpos2.getY() + 0.1f) + block.getBlockBoundsMaxY() - 1.0;
                    d2 = (double)blockpos2.getZ() + d4;
                }
                this.mc.theWorld.spawnParticle(EnumParticleTypes.WATER_DROP, (double)blockpos2.getX() + d3, (double)((float)blockpos2.getY() + 0.1f) + block.getBlockBoundsMaxY(), (double)blockpos2.getZ() + d4, 0.0, 0.0, 0.0, new int[0]);
            }
            if (i2 > 0 && this.random.nextInt(3) < this.rainSoundCounter++) {
                this.rainSoundCounter = 0;
                if (d1 > (double)(blockpos.getY() + 1) && worldclient.getPrecipitationHeight(blockpos).getY() > MathHelper.floor_float(blockpos.getY())) {
                    this.mc.theWorld.playSound(d0, d1, d2, "ambient.weather.rain", 0.1f, 0.5f, false);
                } else {
                    this.mc.theWorld.playSound(d0, d1, d2, "ambient.weather.rain", 0.2f, 1.0f, false);
                }
            }
        }
    }

    protected void renderRainSnow(float partialTicks) {
        WorldProvider worldprovider;
        Object object;
        if (Reflector.ForgeWorldProvider_getWeatherRenderer.exists() && (object = Reflector.call(worldprovider = this.mc.theWorld.provider, Reflector.ForgeWorldProvider_getWeatherRenderer, new Object[0])) != null) {
            Reflector.callVoid(object, Reflector.IRenderHandler_render, Float.valueOf(partialTicks), this.mc.theWorld, this.mc);
            return;
        }
        float f5 = this.mc.theWorld.getRainStrength(partialTicks);
        if (f5 > 0.0f) {
            if (Config.isRainOff()) {
                return;
            }
            this.enableLightmap();
            Entity entity = this.mc.getRenderViewEntity();
            WorldClient worldclient = this.mc.theWorld;
            int i2 = MathHelper.floor_double(entity.posX);
            int j2 = MathHelper.floor_double(entity.posY);
            int k2 = MathHelper.floor_double(entity.posZ);
            Tessellator tessellator = Tessellator.getInstance();
            WorldRenderer worldrenderer = tessellator.getWorldRenderer();
            GlStateManager.disableCull();
            GL11.glNormal3f(0.0f, 1.0f, 0.0f);
            GlStateManager.enableBlend();
            GlStateManager.tryBlendFuncSeparate(770, 771, 1, 0);
            GlStateManager.alphaFunc(516, 0.1f);
            double d0 = entity.lastTickPosX + (entity.posX - entity.lastTickPosX) * (double)partialTicks;
            double d1 = entity.lastTickPosY + (entity.posY - entity.lastTickPosY) * (double)partialTicks;
            double d2 = entity.lastTickPosZ + (entity.posZ - entity.lastTickPosZ) * (double)partialTicks;
            int l2 = MathHelper.floor_double(d1);
            int b0 = 5;
            if (Config.isRainFancy()) {
                b0 = 10;
            }
            int b1 = -1;
            float f2 = (float)this.rendererUpdateCount + partialTicks;
            worldrenderer.setTranslation(-d0, -d1, -d2);
            if (Config.isRainFancy()) {
                b0 = 10;
            }
            GlStateManager.color(1.0f, 1.0f, 1.0f, 1.0f);
            BlockPos.MutableBlockPos blockpos$mutableblockpos = new BlockPos.MutableBlockPos();
            for (int i1 = k2 - b0; i1 <= k2 + b0; ++i1) {
                for (int j1 = i2 - b0; j1 <= i2 + b0; ++j1) {
                    int k1 = (i1 - k2 + 16) * 32 + j1 - i2 + 16;
                    double d3 = (double)this.rainXCoords[k1] * 0.5;
                    double d4 = (double)this.rainYCoords[k1] * 0.5;
                    blockpos$mutableblockpos.func_181079_c(j1, 0, i1);
                    BiomeGenBase biomegenbase = worldclient.getBiomeGenForCoords(blockpos$mutableblockpos);
                    if (!biomegenbase.canSpawnLightningBolt() && !biomegenbase.getEnableSnow()) continue;
                    int l1 = worldclient.getPrecipitationHeight(blockpos$mutableblockpos).getY();
                    int i22 = j2 - b0;
                    int j22 = j2 + b0;
                    if (i22 < l1) {
                        i22 = l1;
                    }
                    if (j22 < l1) {
                        j22 = l1;
                    }
                    int k22 = l1;
                    if (l1 < l2) {
                        k22 = l2;
                    }
                    if (i22 == j22) continue;
                    this.random.setSeed(j1 * j1 * 3121 + j1 * 45238971 ^ i1 * i1 * 418711 + i1 * 13761);
                    blockpos$mutableblockpos.func_181079_c(j1, i22, i1);
                    float f1 = biomegenbase.getFloatTemperature(blockpos$mutableblockpos);
                    if (worldclient.getWorldChunkManager().getTemperatureAtHeight(f1, l1) >= 0.15f) {
                        if (b1 != 0) {
                            if (b1 >= 0) {
                                tessellator.draw();
                            }
                            b1 = 0;
                            this.mc.getTextureManager().bindTexture(locationRainPng);
                            worldrenderer.begin(7, DefaultVertexFormats.PARTICLE_POSITION_TEX_COLOR_LMAP);
                        }
                        double d5 = ((double)(this.rendererUpdateCount + j1 * j1 * 3121 + j1 * 45238971 + i1 * i1 * 418711 + i1 * 13761 & 0x1F) + (double)partialTicks) / 32.0 * (3.0 + this.random.nextDouble());
                        double d6 = (double)((float)j1 + 0.5f) - entity.posX;
                        double d7 = (double)((float)i1 + 0.5f) - entity.posZ;
                        float f22 = MathHelper.sqrt_double(d6 * d6 + d7 * d7) / (float)b0;
                        float f3 = ((1.0f - f22 * f22) * 0.5f + 0.5f) * f5;
                        blockpos$mutableblockpos.func_181079_c(j1, k22, i1);
                        int l22 = worldclient.getCombinedLight(blockpos$mutableblockpos, 0);
                        int i3 = l22 >> 16 & 0xFFFF;
                        int j3 = l22 & 0xFFFF;
                        worldrenderer.pos((double)j1 - d3 + 0.5, i22, (double)i1 - d4 + 0.5).tex(0.0, (double)i22 * 0.25 + d5).color(1.0f, 1.0f, 1.0f, f3).lightmap(i3, j3).endVertex();
                        worldrenderer.pos((double)j1 + d3 + 0.5, i22, (double)i1 + d4 + 0.5).tex(1.0, (double)i22 * 0.25 + d5).color(1.0f, 1.0f, 1.0f, f3).lightmap(i3, j3).endVertex();
                        worldrenderer.pos((double)j1 + d3 + 0.5, j22, (double)i1 + d4 + 0.5).tex(1.0, (double)j22 * 0.25 + d5).color(1.0f, 1.0f, 1.0f, f3).lightmap(i3, j3).endVertex();
                        worldrenderer.pos((double)j1 - d3 + 0.5, j22, (double)i1 - d4 + 0.5).tex(0.0, (double)j22 * 0.25 + d5).color(1.0f, 1.0f, 1.0f, f3).lightmap(i3, j3).endVertex();
                        continue;
                    }
                    if (b1 != 1) {
                        if (b1 >= 0) {
                            tessellator.draw();
                        }
                        b1 = 1;
                        this.mc.getTextureManager().bindTexture(locationSnowPng);
                        worldrenderer.begin(7, DefaultVertexFormats.PARTICLE_POSITION_TEX_COLOR_LMAP);
                    }
                    double d8 = ((float)(this.rendererUpdateCount & 0x1FF) + partialTicks) / 512.0f;
                    double d9 = this.random.nextDouble() + (double)f2 * 0.01 * (double)((float)this.random.nextGaussian());
                    double d10 = this.random.nextDouble() + (double)(f2 * (float)this.random.nextGaussian()) * 0.001;
                    double d11 = (double)((float)j1 + 0.5f) - entity.posX;
                    double d12 = (double)((float)i1 + 0.5f) - entity.posZ;
                    float f6 = MathHelper.sqrt_double(d11 * d11 + d12 * d12) / (float)b0;
                    float f4 = ((1.0f - f6 * f6) * 0.3f + 0.5f) * f5;
                    blockpos$mutableblockpos.func_181079_c(j1, k22, i1);
                    int k3 = (worldclient.getCombinedLight(blockpos$mutableblockpos, 0) * 3 + 0xF000F0) / 4;
                    int l3 = k3 >> 16 & 0xFFFF;
                    int i4 = k3 & 0xFFFF;
                    worldrenderer.pos((double)j1 - d3 + 0.5, i22, (double)i1 - d4 + 0.5).tex(0.0 + d9, (double)i22 * 0.25 + d8 + d10).color(1.0f, 1.0f, 1.0f, f4).lightmap(l3, i4).endVertex();
                    worldrenderer.pos((double)j1 + d3 + 0.5, i22, (double)i1 + d4 + 0.5).tex(1.0 + d9, (double)i22 * 0.25 + d8 + d10).color(1.0f, 1.0f, 1.0f, f4).lightmap(l3, i4).endVertex();
                    worldrenderer.pos((double)j1 + d3 + 0.5, j22, (double)i1 + d4 + 0.5).tex(1.0 + d9, (double)j22 * 0.25 + d8 + d10).color(1.0f, 1.0f, 1.0f, f4).lightmap(l3, i4).endVertex();
                    worldrenderer.pos((double)j1 - d3 + 0.5, j22, (double)i1 - d4 + 0.5).tex(0.0 + d9, (double)j22 * 0.25 + d8 + d10).color(1.0f, 1.0f, 1.0f, f4).lightmap(l3, i4).endVertex();
                }
            }
            if (b1 >= 0) {
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
        ScaledResolution scaledresolution = new ScaledResolution(this.mc);
        GlStateManager.clear(256);
        GlStateManager.matrixMode(5889);
        GlStateManager.loadIdentity();
        GlStateManager.ortho(0.0, scaledresolution.getScaledWidth_double(), scaledresolution.getScaledHeight_double(), 0.0, 1000.0, 3000.0);
        GlStateManager.matrixMode(5888);
        GlStateManager.loadIdentity();
        GlStateManager.translate(0.0f, 0.0f, -2000.0f);
    }

    private void updateFogColor(float partialTicks) {
        float f11;
        WorldClient worldclient = this.mc.theWorld;
        Entity entity = this.mc.getRenderViewEntity();
        float f2 = 0.25f + 0.75f * (float)this.mc.gameSettings.renderDistanceChunks / 32.0f;
        f2 = 1.0f - (float)Math.pow(f2, 0.25);
        Vec3 vec3 = worldclient.getSkyColor(this.mc.getRenderViewEntity(), partialTicks);
        vec3 = CustomColorizer.getWorldSkyColor(vec3, worldclient, this.mc.getRenderViewEntity(), partialTicks);
        float f1 = (float)vec3.xCoord;
        float f22 = (float)vec3.yCoord;
        float f3 = (float)vec3.zCoord;
        Vec3 vec31 = worldclient.getFogColor(partialTicks);
        vec31 = CustomColorizer.getWorldFogColor(vec31, worldclient, this.mc.getRenderViewEntity(), partialTicks);
        this.fogColorRed = (float)vec31.xCoord;
        this.fogColorGreen = (float)vec31.yCoord;
        this.fogColorBlue = (float)vec31.zCoord;
        if (this.mc.gameSettings.renderDistanceChunks >= 4) {
            float[] afloat;
            double d0 = -1.0;
            Vec3 vec32 = MathHelper.sin(worldclient.getCelestialAngleRadians(partialTicks)) > 0.0f ? new Vec3(d0, 0.0, 0.0) : new Vec3(1.0, 0.0, 0.0);
            float f4 = (float)entity.getLook(partialTicks).dotProduct(vec32);
            if (f4 < 0.0f) {
                f4 = 0.0f;
            }
            if (f4 > 0.0f && (afloat = worldclient.provider.calcSunriseSunsetColors(worldclient.getCelestialAngle(partialTicks), partialTicks)) != null) {
                this.fogColorRed = this.fogColorRed * (1.0f - (f4 *= afloat[3])) + afloat[0] * f4;
                this.fogColorGreen = this.fogColorGreen * (1.0f - f4) + afloat[1] * f4;
                this.fogColorBlue = this.fogColorBlue * (1.0f - f4) + afloat[2] * f4;
            }
        }
        this.fogColorRed += (f1 - this.fogColorRed) * f2;
        this.fogColorGreen += (f22 - this.fogColorGreen) * f2;
        this.fogColorBlue += (f3 - this.fogColorBlue) * f2;
        float f10 = worldclient.getRainStrength(partialTicks);
        if (f10 > 0.0f) {
            float f5 = 1.0f - f10 * 0.5f;
            float f12 = 1.0f - f10 * 0.4f;
            this.fogColorRed *= f5;
            this.fogColorGreen *= f5;
            this.fogColorBlue *= f12;
        }
        if ((f11 = worldclient.getThunderStrength(partialTicks)) > 0.0f) {
            float f13 = 1.0f - f11 * 0.5f;
            this.fogColorRed *= f13;
            this.fogColorGreen *= f13;
            this.fogColorBlue *= f13;
        }
        Block block = ActiveRenderInfo.getBlockAtEntityViewpoint(this.mc.theWorld, entity, partialTicks);
        if (this.cloudFog) {
            Vec3 vec33 = worldclient.getCloudColour(partialTicks);
            this.fogColorRed = (float)vec33.xCoord;
            this.fogColorGreen = (float)vec33.yCoord;
            this.fogColorBlue = (float)vec33.zCoord;
        } else if (block.getMaterial() == Material.water) {
            float f8 = (float)EnchantmentHelper.getRespiration(entity) * 0.2f;
            if (entity instanceof EntityLivingBase && ((EntityLivingBase)entity).isPotionActive(Potion.waterBreathing)) {
                f8 = f8 * 0.3f + 0.6f;
            }
            this.fogColorRed = 0.02f + f8;
            this.fogColorGreen = 0.02f + f8;
            this.fogColorBlue = 0.2f + f8;
            Vec3 vec34 = CustomColorizer.getUnderwaterColor(this.mc.theWorld, this.mc.getRenderViewEntity().posX, this.mc.getRenderViewEntity().posY + 1.0, this.mc.getRenderViewEntity().posZ);
            if (vec34 != null) {
                this.fogColorRed = (float)vec34.xCoord;
                this.fogColorGreen = (float)vec34.yCoord;
                this.fogColorBlue = (float)vec34.zCoord;
            }
        } else if (block.getMaterial() == Material.lava) {
            this.fogColorRed = 0.6f;
            this.fogColorGreen = 0.1f;
            this.fogColorBlue = 0.0f;
        }
        float f9 = this.fogColor2 + (this.fogColor1 - this.fogColor2) * partialTicks;
        this.fogColorRed *= f9;
        this.fogColorGreen *= f9;
        this.fogColorBlue *= f9;
        double d2 = worldclient.provider.getVoidFogYFactor();
        double d1 = (entity.lastTickPosY + (entity.posY - entity.lastTickPosY) * (double)partialTicks) * d2;
        if (entity instanceof EntityLivingBase && ((EntityLivingBase)entity).isPotionActive(Potion.blindness)) {
            int i2 = ((EntityLivingBase)entity).getActivePotionEffect(Potion.blindness).getDuration();
            d1 = i2 < 20 ? (d1 *= (double)(1.0f - (float)i2 / 20.0f)) : 0.0;
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
        if (entity instanceof EntityLivingBase && ((EntityLivingBase)entity).isPotionActive(Potion.nightVision)) {
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
            Object object = Reflector.newInstance(Reflector.EntityViewRenderEvent_FogColors_Constructor, this, entity, block, Float.valueOf(partialTicks), Float.valueOf(this.fogColorRed), Float.valueOf(this.fogColorGreen), Float.valueOf(this.fogColorBlue));
            Reflector.postForgeBusEvent(object);
            this.fogColorRed = Reflector.getFieldValueFloat(object, Reflector.EntityViewRenderEvent_FogColors_red, this.fogColorRed);
            this.fogColorGreen = Reflector.getFieldValueFloat(object, Reflector.EntityViewRenderEvent_FogColors_green, this.fogColorGreen);
            this.fogColorBlue = Reflector.getFieldValueFloat(object, Reflector.EntityViewRenderEvent_FogColors_blue, this.fogColorBlue);
        }
        GlStateManager.clearColor(this.fogColorRed, this.fogColorGreen, this.fogColorBlue, 0.0f);
    }

    private void setupFog(int p_78468_1_, float partialTicks) {
        Entity entity = this.mc.getRenderViewEntity();
        boolean flag = false;
        this.fogStandard = false;
        if (entity instanceof EntityPlayer) {
            flag = ((EntityPlayer)entity).capabilities.isCreativeMode;
        }
        GL11.glFog(2918, this.setFogColorBuffer(this.fogColorRed, this.fogColorGreen, this.fogColorBlue, 1.0f));
        GL11.glNormal3f(0.0f, -1.0f, 0.0f);
        GlStateManager.color(1.0f, 1.0f, 1.0f, 1.0f);
        Block block = ActiveRenderInfo.getBlockAtEntityViewpoint(this.mc.theWorld, entity, partialTicks);
        Object object = Reflector.newInstance(Reflector.EntityViewRenderEvent_FogDensity_Constructor, this, entity, block, Float.valueOf(partialTicks), Float.valueOf(0.1f));
        if (Reflector.postForgeBusEvent(object)) {
            float f1 = Reflector.getFieldValueFloat(object, Reflector.EntityViewRenderEvent_FogDensity_density, 0.0f);
            GL11.glFogf(2914, f1);
        } else if (entity instanceof EntityLivingBase && ((EntityLivingBase)entity).isPotionActive(Potion.blindness)) {
            float f2 = 5.0f;
            int i2 = ((EntityLivingBase)entity).getActivePotionEffect(Potion.blindness).getDuration();
            if (i2 < 20) {
                f2 = 5.0f + (this.farPlaneDistance - 5.0f) * (1.0f - (float)i2 / 20.0f);
            }
            GlStateManager.setFog(9729);
            if (p_78468_1_ == -1) {
                GlStateManager.setFogStart(0.0f);
                GlStateManager.setFogEnd(f2 * 0.8f);
            } else {
                GlStateManager.setFogStart(f2 * 0.25f);
                GlStateManager.setFogEnd(f2);
            }
            if (GLContext.getCapabilities().GL_NV_fog_distance && Config.isFogFancy()) {
                GL11.glFogi(34138, 34139);
            }
        } else if (this.cloudFog) {
            GlStateManager.setFog(2048);
            GlStateManager.setFogDensity(0.1f);
        } else if (block.getMaterial() == Material.water) {
            GlStateManager.setFog(2048);
            if (entity instanceof EntityLivingBase && ((EntityLivingBase)entity).isPotionActive(Potion.waterBreathing)) {
                GlStateManager.setFogDensity(0.01f);
            } else {
                GlStateManager.setFogDensity(0.1f - (float)EnchantmentHelper.getRespiration(entity) * 0.03f);
            }
            if (Config.isClearWater()) {
                GL11.glFogf(2914, 0.02f);
            }
        } else if (block.getMaterial() == Material.lava) {
            GlStateManager.setFog(2048);
            GlStateManager.setFogDensity(2.0f);
        } else {
            float f2 = this.farPlaneDistance;
            this.fogStandard = true;
            GlStateManager.setFog(9729);
            if (p_78468_1_ == -1) {
                GlStateManager.setFogStart(0.0f);
                GlStateManager.setFogEnd(f2);
            } else {
                GlStateManager.setFogStart(f2 * Config.getFogStart());
                GlStateManager.setFogEnd(f2);
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
                GlStateManager.setFogStart(f2 * 0.05f);
                GlStateManager.setFogEnd(f2);
            }
            if (Reflector.ForgeHooksClient_onFogRender.exists()) {
                Reflector.callVoid(Reflector.ForgeHooksClient_onFogRender, this, entity, block, Float.valueOf(partialTicks), p_78468_1_, Float.valueOf(f2));
            }
        }
        GlStateManager.enableColorMaterial();
        GlStateManager.enableFog();
        GlStateManager.colorMaterial(1028, 4608);
    }

    private FloatBuffer setFogColorBuffer(float red, float green, float blue, float alpha) {
        this.fogColorBuffer.clear();
        this.fogColorBuffer.put(red).put(green).put(blue).put(alpha);
        this.fogColorBuffer.flip();
        return this.fogColorBuffer;
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
                    long i2 = System.nanoTime() / 1000000L;
                    if (this.lastServerTime != 0L && this.lastServerTicks != 0) {
                        long j2 = i2 - this.lastServerTime;
                        if (j2 < 0L) {
                            this.lastServerTime = i2;
                            j2 = 0L;
                        }
                        if (j2 >= 50L) {
                            this.lastServerTime = i2;
                            int k2 = integratedserver.getTickCounter();
                            int l2 = k2 - this.lastServerTicks;
                            if (l2 < 0) {
                                this.lastServerTicks = k2;
                                l2 = 0;
                            }
                            if (l2 < 1 && this.serverWaitTime < 100) {
                                this.serverWaitTime += 2;
                            }
                            if (l2 > 1 && this.serverWaitTime > 0) {
                                --this.serverWaitTime;
                            }
                            this.lastServerTicks = k2;
                        }
                    } else {
                        this.lastServerTime = i2;
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
            RenderPlayerOF.register();
            if (Config.getBitsOs() == 64 && Config.getBitsJre() == 32) {
                Config.setNotify64BitJava(true);
            }
            this.initialized = true;
        }
        Config.isActing();
        Config.checkDisplayMode();
        WorldClient world = this.mc.theWorld;
        if (world != null) {
            if (Config.getNewRelease() != null) {
                String s2 = "HD_U".replace("HD_U", "HD Ultra").replace("L", "Light");
                String s1 = s2 + " " + Config.getNewRelease();
                ChatComponentText chatcomponenttext = new ChatComponentText("A new \u00a7eOptiFine\u00a7f version is available: \u00a7e" + s1 + "\u00a7f");
                this.mc.ingameGUI.getChatGUI().printChatMessage(chatcomponenttext);
                Config.setNewRelease(null);
            }
            if (Config.isNotify64BitJava()) {
                Config.setNotify64BitJava(false);
                ChatComponentText chatcomponenttext1 = new ChatComponentText("You can install \u00a7e64-bit Java\u00a7f to increase performance");
                this.mc.ingameGUI.getChatGUI().printChatMessage(chatcomponenttext1);
            }
        }
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
    }

    private void frameFinish() {
        long i2;
        if (this.mc.theWorld != null && (i2 = System.currentTimeMillis()) > this.lastErrorCheckTimeMs + 10000L) {
            this.lastErrorCheckTimeMs = i2;
            int j2 = GL11.glGetError();
            if (j2 != 0) {
                String s2 = GLU.gluErrorString(j2);
                ChatComponentText chatcomponenttext = new ChatComponentText("\u00a7eOpenGL Error\u00a7f: " + j2 + " (" + s2 + ")");
                this.mc.ingameGUI.getChatGUI().printChatMessage(chatcomponenttext);
            }
        }
    }

    private void updateMainMenu(GuiMainMenu p_updateMainMenu_1_) {
        try {
            String s2 = null;
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(new Date());
            int i2 = calendar.get(5);
            int j2 = calendar.get(2) + 1;
            if (i2 == 8 && j2 == 4) {
                s2 = "Happy birthday, OptiFine!";
            }
            if (i2 == 14 && j2 == 8) {
                s2 = "Happy birthday, sp614x!";
            }
            if (s2 == null) {
                return;
            }
            Field[] afield = GuiMainMenu.class.getDeclaredFields();
            for (int k2 = 0; k2 < afield.length; ++k2) {
                if (afield[k2].getType() != String.class) continue;
                afield[k2].setAccessible(true);
                afield[k2].set(p_updateMainMenu_1_, s2);
                break;
            }
        }
        catch (Throwable throwable) {
            // empty catch block
        }
    }

    static {
        shaderResourceLocations = new ResourceLocation[]{new ResourceLocation("shaders/post/notch.json"), new ResourceLocation("shaders/post/fxaa.json"), new ResourceLocation("shaders/post/art.json"), new ResourceLocation("shaders/post/bumpy.json"), new ResourceLocation("shaders/post/blobs2.json"), new ResourceLocation("shaders/post/pencil.json"), new ResourceLocation("shaders/post/color_convolve.json"), new ResourceLocation("shaders/post/deconverge.json"), new ResourceLocation("shaders/post/flip.json"), new ResourceLocation("shaders/post/invert.json"), new ResourceLocation("shaders/post/ntsc.json"), new ResourceLocation("shaders/post/outline.json"), new ResourceLocation("shaders/post/phosphor.json"), new ResourceLocation("shaders/post/scan_pincushion.json"), new ResourceLocation("shaders/post/sobel.json"), new ResourceLocation("shaders/post/bits.json"), new ResourceLocation("shaders/post/desaturate.json"), new ResourceLocation("shaders/post/green.json"), new ResourceLocation("shaders/post/blur.json"), new ResourceLocation("shaders/post/wobble.json"), new ResourceLocation("shaders/post/blobs.json"), new ResourceLocation("shaders/post/antialias.json"), new ResourceLocation("shaders/post/creeper.json"), new ResourceLocation("shaders/post/spider.json")};
        shaderCount = shaderResourceLocations.length;
    }
}

