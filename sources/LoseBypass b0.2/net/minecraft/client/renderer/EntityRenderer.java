/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  org.apache.logging.log4j.LogManager
 *  org.apache.logging.log4j.Logger
 *  org.lwjgl.input.Mouse
 *  org.lwjgl.opengl.Display
 *  org.lwjgl.opengl.GL11
 *  org.lwjgl.opengl.GLContext
 *  org.lwjgl.util.glu.Project
 */
package net.minecraft.client.renderer;

import com.google.common.base.Predicate;
import com.google.common.base.Predicates;
import com.google.gson.JsonSyntaxException;
import com.wallhacks.losebypass.LoseBypass;
import com.wallhacks.losebypass.event.events.Render3DEvent;
import com.wallhacks.losebypass.systems.module.modules.combat.Reach;
import com.wallhacks.losebypass.systems.module.modules.misc.FreeLook;
import com.wallhacks.losebypass.systems.module.modules.misc.Freecam;
import com.wallhacks.losebypass.systems.module.modules.misc.Zoom;
import com.wallhacks.losebypass.systems.module.modules.render.FullBright;
import java.io.IOException;
import java.nio.FloatBuffer;
import java.util.List;
import java.util.Random;
import java.util.concurrent.Callable;
import net.minecraft.block.Block;
import net.minecraft.block.BlockBed;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.AbstractClientPlayer;
import net.minecraft.client.gui.MapItemRenderer;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.multiplayer.WorldClient;
import net.minecraft.client.particle.EffectRenderer;
import net.minecraft.client.renderer.ActiveRenderInfo;
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
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EntitySelectors;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.EnumWorldBlockLayer;
import net.minecraft.util.MathHelper;
import net.minecraft.util.MouseFilter;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.util.ReportedException;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.Vec3;
import net.minecraft.world.WorldSettings;
import net.minecraft.world.biome.BiomeGenBase;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GLContext;
import org.lwjgl.util.glu.Project;

public class EntityRenderer
implements IResourceManagerReloadListener {
    private static final Logger logger = LogManager.getLogger();
    private static final ResourceLocation locationRainPng = new ResourceLocation("textures/environment/rain.png");
    private static final ResourceLocation locationSnowPng = new ResourceLocation("textures/environment/snow.png");
    private static final ResourceLocation[] shaderResourceLocations = new ResourceLocation[]{new ResourceLocation("shaders/post/notch.json"), new ResourceLocation("shaders/post/fxaa.json"), new ResourceLocation("shaders/post/art.json"), new ResourceLocation("shaders/post/bumpy.json"), new ResourceLocation("shaders/post/blobs2.json"), new ResourceLocation("shaders/post/pencil.json"), new ResourceLocation("shaders/post/color_convolve.json"), new ResourceLocation("shaders/post/deconverge.json"), new ResourceLocation("shaders/post/flip.json"), new ResourceLocation("shaders/post/invert.json"), new ResourceLocation("shaders/post/ntsc.json"), new ResourceLocation("shaders/post/outline.json"), new ResourceLocation("shaders/post/phosphor.json"), new ResourceLocation("shaders/post/scan_pincushion.json"), new ResourceLocation("shaders/post/sobel.json"), new ResourceLocation("shaders/post/bits.json"), new ResourceLocation("shaders/post/desaturate.json"), new ResourceLocation("shaders/post/green.json"), new ResourceLocation("shaders/post/blur.json"), new ResourceLocation("shaders/post/wobble.json"), new ResourceLocation("shaders/post/blobs.json"), new ResourceLocation("shaders/post/antialias.json"), new ResourceLocation("shaders/post/creeper.json"), new ResourceLocation("shaders/post/spider.json")};
    public static final int shaderCount = shaderResourceLocations.length;
    public static boolean anaglyphEnable;
    public static int anaglyphField;
    public final ItemRenderer itemRenderer;
    private final IResourceManager resourceManager;
    private final MapItemRenderer theMapItemRenderer;
    private final DynamicTexture lightmapTexture;
    private final int[] lightmapColors;
    private final ResourceLocation locationLightMap;
    private Minecraft mc;
    private Random random = new Random();
    private float farPlaneDistance;
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
    private boolean lightmapUpdateNeeded;
    private float torchFlickerX;
    private float torchFlickerDX;
    private int rainSoundCounter;
    private float[] rainXCoords = new float[1024];
    private float[] rainYCoords = new float[1024];
    private FloatBuffer fogColorBuffer = GLAllocation.createDirectFloatBuffer(16);
    private float fogColorRed;
    private float fogColorGreen;
    private float fogColorBlue;
    private float fogColor2;
    private float fogColor1;
    private int debugViewDirection = 0;
    private boolean debugView = false;
    private double cameraZoom = 1.0;
    private double cameraYaw;
    private double cameraPitch;
    private ShaderGroup theShaderGroup;
    private int shaderIndex = shaderCount;
    private boolean useShader = false;
    private int frameCount = 0;

    public EntityRenderer(Minecraft mcIn, IResourceManager resourceManagerIn) {
        this.mc = mcIn;
        this.resourceManager = resourceManagerIn;
        this.itemRenderer = mcIn.getItemRenderer();
        this.theMapItemRenderer = new MapItemRenderer(mcIn.getTextureManager());
        this.lightmapTexture = new DynamicTexture(16, 16);
        this.locationLightMap = mcIn.getTextureManager().getDynamicTextureLocation("lightMap", this.lightmapTexture);
        this.lightmapColors = this.lightmapTexture.getTextureData();
        this.theShaderGroup = null;
        int i = 0;
        while (i < 32) {
            for (int j = 0; j < 32; ++j) {
                float f = j - 16;
                float f1 = i - 16;
                float f2 = MathHelper.sqrt_float(f * f + f1 * f1);
                this.rainXCoords[i << 5 | j] = -f1 / f2;
                this.rainYCoords[i << 5 | j] = f / f2;
            }
            ++i;
        }
    }

    public boolean isShaderActive() {
        if (!OpenGlHelper.shadersSupported) return false;
        if (this.theShaderGroup == null) return false;
        return true;
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
        if (!OpenGlHelper.shadersSupported) return;
        if (this.theShaderGroup != null) {
            this.theShaderGroup.deleteShaderGroup();
        }
        this.theShaderGroup = null;
        if (entityIn instanceof EntityCreeper) {
            this.loadShader(new ResourceLocation("shaders/post/creeper.json"));
            return;
        }
        if (entityIn instanceof EntitySpider) {
            this.loadShader(new ResourceLocation("shaders/post/spider.json"));
            return;
        }
        if (!(entityIn instanceof EntityEnderman)) return;
        this.loadShader(new ResourceLocation("shaders/post/invert.json"));
    }

    public void activateNextShader() {
        if (!OpenGlHelper.shadersSupported) return;
        if (!(this.mc.getRenderViewEntity() instanceof EntityPlayer)) return;
        if (this.theShaderGroup != null) {
            this.theShaderGroup.deleteShaderGroup();
        }
        this.shaderIndex = (this.shaderIndex + 1) % (shaderResourceLocations.length + 1);
        if (this.shaderIndex != shaderCount) {
            this.loadShader(shaderResourceLocations[this.shaderIndex]);
            return;
        }
        this.theShaderGroup = null;
    }

    private void loadShader(ResourceLocation resourceLocationIn) {
        try {
            this.theShaderGroup = new ShaderGroup(this.mc.getTextureManager(), this.resourceManager, this.mc.getFramebuffer(), resourceLocationIn);
            this.theShaderGroup.createBindFramebuffers(this.mc.displayWidth, this.mc.displayHeight);
            this.useShader = true;
            return;
        }
        catch (IOException ioexception) {
            logger.warn("Failed to load shader: " + resourceLocationIn, (Throwable)ioexception);
            this.shaderIndex = shaderCount;
            this.useShader = false;
            return;
        }
        catch (JsonSyntaxException jsonsyntaxexception) {
            logger.warn("Failed to load shader: " + resourceLocationIn, (Throwable)jsonsyntaxexception);
            this.shaderIndex = shaderCount;
            this.useShader = false;
        }
    }

    @Override
    public void onResourceManagerReload(IResourceManager resourceManager) {
        if (this.theShaderGroup != null) {
            this.theShaderGroup.deleteShaderGroup();
        }
        this.theShaderGroup = null;
        if (this.shaderIndex != shaderCount) {
            this.loadShader(shaderResourceLocations[this.shaderIndex]);
            return;
        }
        this.loadEntityShader(this.mc.getRenderViewEntity());
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
        if (!BossStatus.hasColorModifier) {
            if (!(this.bossColorModifier > 0.0f)) return;
            this.bossColorModifier -= 0.0125f;
            return;
        }
        this.bossColorModifier += 0.05f;
        if (this.bossColorModifier > 1.0f) {
            this.bossColorModifier = 1.0f;
        }
        BossStatus.hasColorModifier = false;
    }

    public ShaderGroup getShaderGroup() {
        return this.theShaderGroup;
    }

    public void updateShaderGroupSize(int width, int height) {
        if (!OpenGlHelper.shadersSupported) return;
        if (this.theShaderGroup != null) {
            this.theShaderGroup.createBindFramebuffers(width, height);
        }
        this.mc.renderGlobal.createBindEntityOutlineFbs(width, height);
    }

    public void getMouseOver(float partialTicks) {
        Entity entity = this.mc.getRenderViewEntity();
        if (entity == null) return;
        if (this.mc.theWorld == null) return;
        this.mc.mcProfiler.startSection("pick");
        this.mc.pointedEntity = null;
        double d0 = this.mc.playerController.getBlockReachDistance();
        this.mc.objectMouseOver = entity.rayTrace(d0, partialTicks);
        double d1 = d0;
        Vec3 vec3 = entity.getPositionEyes(partialTicks);
        boolean flag = false;
        int i = 3;
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
        float f = 1.0f;
        List<Entity> list = this.mc.theWorld.getEntitiesInAABBexcluding(entity, entity.getEntityBoundingBox().addCoord(vec31.xCoord * d0, vec31.yCoord * d0, vec31.zCoord * d0).expand(f, f, f), Predicates.and(EntitySelectors.NOT_SPECTATING, new Predicate<Entity>(){

            @Override
            public boolean apply(Entity p_apply_1_) {
                return p_apply_1_.canBeCollidedWith();
            }
        }));
        double d2 = d1;
        for (int j = 0; j < list.size(); ++j) {
            double d3;
            Entity entity1 = list.get(j);
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
            if (entity1 == entity.ridingEntity) {
                if (d2 != 0.0) continue;
                this.pointedEntity = entity1;
                vec33 = movingobjectposition.hitVec;
                continue;
            }
            this.pointedEntity = entity1;
            vec33 = movingobjectposition.hitVec;
            d2 = d3;
        }
        if (this.pointedEntity != null && flag && vec3.distanceTo(vec33) > (double)Reach.getReach()) {
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
        if (!(this.fovModifierHand < 0.1f)) return;
        this.fovModifierHand = 0.1f;
    }

    private float getFOVModifier(float partialTicks, boolean p_78481_2_) {
        Block block;
        if (this.debugView) {
            return 90.0f;
        }
        Entity entity = this.mc.getRenderViewEntity();
        float f = 70.0f;
        if (p_78481_2_) {
            f = Zoom.getFov();
            f *= this.fovModifierHandPrev + (this.fovModifierHand - this.fovModifierHandPrev) * partialTicks;
        }
        if (entity instanceof EntityLivingBase && ((EntityLivingBase)entity).getHealth() <= 0.0f) {
            float f1 = (float)((EntityLivingBase)entity).deathTime + partialTicks;
            f /= (1.0f - 500.0f / (f1 + 500.0f)) * 2.0f + 1.0f;
        }
        if ((block = ActiveRenderInfo.getBlockAtEntityViewpoint(this.mc.theWorld, entity, partialTicks)).getMaterial() != Material.water) return f;
        return f * 60.0f / 70.0f;
    }

    private void hurtCameraEffect(float partialTicks) {
        if (!(this.mc.getRenderViewEntity() instanceof EntityLivingBase)) return;
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

    private void setupViewBobbing(float partialTicks) {
        if (!(this.mc.getRenderViewEntity() instanceof EntityPlayer)) return;
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

    private void orientCamera(float partialTicks) {
        Entity entity = this.mc.getRenderViewEntity();
        float f = entity.getEyeHeight();
        double d0 = entity.prevPosX + (entity.posX - entity.prevPosX) * (double)partialTicks;
        double d1 = entity.prevPosY + (entity.posY - entity.prevPosY) * (double)partialTicks + (double)f;
        double d2 = entity.prevPosZ + (entity.posZ - entity.prevPosZ) * (double)partialTicks;
        int perspective = FreeLook.getPerspective();
        if (entity instanceof EntityLivingBase && ((EntityLivingBase)entity).isPlayerSleeping()) {
            f = (float)((double)f + 1.0);
            GlStateManager.translate(0.0f, 0.3f, 0.0f);
            if (!this.mc.gameSettings.debugCamEnable) {
                BlockPos blockpos = new BlockPos(entity);
                IBlockState iblockstate = this.mc.theWorld.getBlockState(blockpos);
                Block block = iblockstate.getBlock();
                if (block == Blocks.bed) {
                    int j = iblockstate.getValue(BlockBed.FACING).getHorizontalIndex();
                    GlStateManager.rotate(j * 90, 0.0f, 1.0f, 0.0f);
                }
                GlStateManager.rotate(entity.prevRotationYaw + (entity.rotationYaw - entity.prevRotationYaw) * partialTicks + 180.0f, 0.0f, -1.0f, 0.0f);
                GlStateManager.rotate(entity.prevRotationPitch + (entity.rotationPitch - entity.prevRotationPitch) * partialTicks, -1.0f, 0.0f, 0.0f);
            }
        } else if (perspective > 0) {
            double d3 = this.thirdPersonDistanceTemp + (this.thirdPersonDistance - this.thirdPersonDistanceTemp) * partialTicks;
            if (this.mc.gameSettings.debugCamEnable) {
                GlStateManager.translate(0.0f, 0.0f, (float)(-d3));
            } else {
                float yaw = FreeLook.getYaw();
                float pitch = FreeLook.getPitch();
                if (perspective == 2) {
                    pitch += 180.0f;
                }
                double d4 = (double)(-MathHelper.sin(yaw / 180.0f * (float)Math.PI) * MathHelper.cos(pitch / 180.0f * (float)Math.PI)) * d3;
                double d5 = (double)(MathHelper.cos(yaw / 180.0f * (float)Math.PI) * MathHelper.cos(pitch / 180.0f * (float)Math.PI)) * d3;
                double d6 = (double)(-MathHelper.sin(pitch / 180.0f * (float)Math.PI)) * d3;
                for (int i = 0; i < 8; ++i) {
                    double d7;
                    MovingObjectPosition movingobjectposition;
                    float f3 = (i & 1) * 2 - 1;
                    float f4 = (i >> 1 & 1) * 2 - 1;
                    float f5 = (i >> 2 & 1) * 2 - 1;
                    if ((movingobjectposition = this.mc.theWorld.rayTraceBlocks(new Vec3(d0 + (double)(f3 *= 0.1f), d1 + (double)(f4 *= 0.1f), d2 + (double)(f5 *= 0.1f)), new Vec3(d0 - d4 + (double)f3 + (double)f5, d1 - d6 + (double)f4, d2 - d5 + (double)f5))) == null || !((d7 = movingobjectposition.hitVec.distanceTo(new Vec3(d0, d1, d2))) < d3)) continue;
                    d3 = d7;
                }
                if (perspective == 2) {
                    GlStateManager.rotate(180.0f, 0.0f, 1.0f, 0.0f);
                }
                GlStateManager.rotate(FreeLook.getPitch() - pitch, 1.0f, 0.0f, 0.0f);
                GlStateManager.rotate(FreeLook.getYaw() - yaw, 0.0f, 1.0f, 0.0f);
                GlStateManager.translate(0.0f, 0.0f, (float)(-d3));
                GlStateManager.rotate(yaw - FreeLook.getYaw(), 0.0f, 1.0f, 0.0f);
                GlStateManager.rotate(pitch - FreeLook.getPitch(), 1.0f, 0.0f, 0.0f);
            }
        } else {
            GlStateManager.translate(0.0f, 0.0f, -0.1f);
        }
        if (!this.mc.gameSettings.debugCamEnable) {
            GlStateManager.rotate(FreeLook.getPrevPitch() + (FreeLook.getPitch() - FreeLook.getPrevPitch()) * partialTicks, 1.0f, 0.0f, 0.0f);
            if (entity instanceof EntityAnimal) {
                EntityAnimal entityanimal = (EntityAnimal)entity;
                GlStateManager.rotate(entityanimal.prevRotationYaw + (entityanimal.rotationYaw - entityanimal.prevRotationYaw) * partialTicks + 180.0f, 0.0f, 1.0f, 0.0f);
            } else {
                GlStateManager.rotate(FreeLook.getPrevYaw() + (FreeLook.getYaw() - FreeLook.getPrevYaw()) * partialTicks + 180.0f, 0.0f, 1.0f, 0.0f);
            }
        }
        GlStateManager.translate(0.0f, -f, 0.0f);
        d0 = entity.prevPosX + (entity.posX - entity.prevPosX) * (double)partialTicks;
        d1 = entity.prevPosY + (entity.posY - entity.prevPosY) * (double)partialTicks + (double)f;
        d2 = entity.prevPosZ + (entity.posZ - entity.prevPosZ) * (double)partialTicks;
        this.cloudFog = this.mc.renderGlobal.hasCloudFog(d0, d1, d2, partialTicks);
    }

    private void setupCameraTransform(float partialTicks, int pass) {
        float f1;
        this.farPlaneDistance = this.mc.gameSettings.renderDistanceChunks * 16;
        GlStateManager.matrixMode(5889);
        GlStateManager.loadIdentity();
        float f = 0.07f;
        if (this.mc.gameSettings.anaglyph) {
            GlStateManager.translate((float)(-(pass * 2 - 1)) * f, 0.0f, 0.0f);
        }
        if (this.cameraZoom != 1.0) {
            GlStateManager.translate((float)this.cameraYaw, (float)(-this.cameraPitch), 0.0f);
            GlStateManager.scale(this.cameraZoom, this.cameraZoom, 1.0);
        }
        Project.gluPerspective((float)this.getFOVModifier(partialTicks, true), (float)((float)this.mc.displayWidth / (float)this.mc.displayHeight), (float)0.05f, (float)(this.farPlaneDistance * MathHelper.SQRT_2));
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
            int i = 20;
            if (this.mc.thePlayer.isPotionActive(Potion.confusion)) {
                i = 7;
            }
            float f2 = 5.0f / (f1 * f1 + 5.0f) - f1 * 0.04f;
            f2 *= f2;
            GlStateManager.rotate(((float)this.rendererUpdateCount + partialTicks) * (float)i, 0.0f, 1.0f, 1.0f);
            GlStateManager.scale(1.0f / f2, 1.0f, 1.0f);
            GlStateManager.rotate(-((float)this.rendererUpdateCount + partialTicks) * (float)i, 0.0f, 1.0f, 1.0f);
        }
        this.orientCamera(partialTicks);
        if (!this.debugView) return;
        switch (this.debugViewDirection) {
            case 0: {
                GlStateManager.rotate(90.0f, 0.0f, 1.0f, 0.0f);
                return;
            }
            case 1: {
                GlStateManager.rotate(180.0f, 0.0f, 1.0f, 0.0f);
                return;
            }
            case 2: {
                GlStateManager.rotate(-90.0f, 0.0f, 1.0f, 0.0f);
                return;
            }
            case 3: {
                GlStateManager.rotate(90.0f, 1.0f, 0.0f, 0.0f);
                return;
            }
            case 4: {
                GlStateManager.rotate(-90.0f, 1.0f, 0.0f, 0.0f);
                return;
            }
        }
    }

    private void renderHand(float partialTicks, int xOffset) {
        boolean flag;
        if (this.debugView) return;
        GlStateManager.matrixMode(5889);
        GlStateManager.loadIdentity();
        float f = 0.07f;
        if (this.mc.gameSettings.anaglyph) {
            GlStateManager.translate((float)(-(xOffset * 2 - 1)) * f, 0.0f, 0.0f);
        }
        Project.gluPerspective((float)this.getFOVModifier(partialTicks, false), (float)((float)this.mc.displayWidth / (float)this.mc.displayHeight), (float)0.05f, (float)(this.farPlaneDistance * 2.0f));
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
        if (!(FreeLook.getPerspective() != 0 || flag || this.mc.gameSettings.hideGUI || this.mc.playerController.isSpectator())) {
            this.enableLightmap();
            this.itemRenderer.renderItemInFirstPerson(partialTicks);
            this.disableLightmap();
        }
        GlStateManager.popMatrix();
        if (FreeLook.getPerspective() == 0 && !flag) {
            this.itemRenderer.renderOverlays(partialTicks);
            this.hurtCameraEffect(partialTicks);
        }
        if (!this.mc.gameSettings.viewBobbing) return;
        this.setupViewBobbing(partialTicks);
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
        float f = 0.00390625f;
        GlStateManager.scale(f, f, f);
        GlStateManager.translate(8.0f, 8.0f, 8.0f);
        GlStateManager.matrixMode(5888);
        this.mc.getTextureManager().bindTexture(this.locationLightMap);
        GL11.glTexParameteri((int)3553, (int)10241, (int)9729);
        GL11.glTexParameteri((int)3553, (int)10240, (int)9729);
        GL11.glTexParameteri((int)3553, (int)10242, (int)10496);
        GL11.glTexParameteri((int)3553, (int)10243, (int)10496);
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
        if (!this.lightmapUpdateNeeded) return;
        this.mc.mcProfiler.startSection("lightTex");
        WorldClient world = this.mc.theWorld;
        if (world == null) return;
        float f = world.getSunBrightness(1.0f);
        float f1 = f * 0.95f + 0.05f;
        int i = 0;
        while (true) {
            if (i >= 256) {
                this.lightmapTexture.updateDynamicTexture();
                this.lightmapUpdateNeeded = false;
                this.mc.mcProfiler.endSection();
                return;
            }
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
            if (world.provider.getDimensionId() == 1) {
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
            if (FullBright.enabled()) {
                f16 = 100.0f;
            }
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
            this.lightmapColors[i] = j << 24 | k << 16 | l << 8 | i1;
            ++i;
        }
    }

    private float getNightVisionBrightness(EntityLivingBase entitylivingbaseIn, float partialTicks) {
        int i = entitylivingbaseIn.getActivePotionEffect(Potion.nightVision).getDuration();
        if (i > 200) {
            return 1.0f;
        }
        float f = 0.7f + MathHelper.sin(((float)i - partialTicks) * (float)Math.PI * 0.2f) * 0.3f;
        return f;
    }

    public void func_181560_a(float p_181560_1_, long p_181560_2_) {
        boolean flag = Display.isActive();
        if (!(flag || !this.mc.gameSettings.pauseOnLostFocus || this.mc.gameSettings.touchscreen && Mouse.isButtonDown((int)1))) {
            if (Minecraft.getSystemTime() - this.prevFrameTime > 500L) {
                this.mc.displayInGameMenu();
            }
        } else {
            this.prevFrameTime = Minecraft.getSystemTime();
        }
        this.mc.mcProfiler.startSection("mouse");
        if (flag && Minecraft.isRunningOnMac && this.mc.inGameHasFocus && !Mouse.isInsideWindow()) {
            Mouse.setGrabbed((boolean)false);
            Mouse.setCursorPosition((int)(Display.getWidth() / 2), (int)(Display.getHeight() / 2));
            Mouse.setGrabbed((boolean)true);
        }
        if (this.mc.inGameHasFocus && flag) {
            this.mc.mouseHelper.mouseXYChange();
            float f = (this.mc.gameSettings.mouseSensitivity * 0.6f + 0.2f) * Zoom.getSensitivity();
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
                float f4 = p_181560_1_ - this.smoothCamPartialTicks;
                this.smoothCamPartialTicks = p_181560_1_;
                f2 = this.smoothCamFilterX * f4;
                f3 = this.smoothCamFilterY * f4;
                if (FreeLook.enabled()) {
                    FreeLook.updateAngle(f2, f3 * (float)i);
                } else {
                    this.mc.getRenderViewEntity().setAngles(f2, f3 * (float)i);
                }
            } else {
                this.smoothCamYaw = 0.0f;
                this.smoothCamPitch = 0.0f;
                if (FreeLook.enabled()) {
                    FreeLook.updateAngle(f2, f3 * (float)i);
                } else {
                    this.mc.getRenderViewEntity().setAngles(f2, f3 * (float)i);
                }
            }
        }
        this.mc.mcProfiler.endSection();
        if (this.mc.skipRenderWorld) return;
        anaglyphEnable = this.mc.gameSettings.anaglyph;
        final ScaledResolution scaledresolution = new ScaledResolution(this.mc);
        int i1 = scaledresolution.getScaledWidth();
        int j1 = scaledresolution.getScaledHeight();
        final int k1 = Mouse.getX() * i1 / this.mc.displayWidth;
        final int l1 = j1 - Mouse.getY() * j1 / this.mc.displayHeight - 1;
        int i2 = this.mc.gameSettings.limitFramerate;
        if (this.mc.theWorld != null) {
            this.mc.mcProfiler.startSection("level");
            int j = Math.min(Minecraft.getDebugFPS(), i2);
            j = Math.max(j, 60);
            long k = System.nanoTime() - p_181560_2_;
            long l = Math.max((long)(1000000000 / j / 4) - k, 0L);
            this.renderWorld(p_181560_1_, System.nanoTime() + l);
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
        if (this.mc.currentScreen == null) return;
        GlStateManager.clear(256);
        try {
            this.mc.currentScreen.drawScreen(k1, l1, p_181560_1_);
            return;
        }
        catch (Throwable throwable) {
            CrashReport crashreport = CrashReport.makeCrashReport(throwable, "Rendering screen");
            CrashReportCategory crashreportcategory = crashreport.makeCategory("Screen render details");
            crashreportcategory.addCrashSectionCallable("Screen name", new Callable<String>(){

                @Override
                public String call() throws Exception {
                    return ((EntityRenderer)EntityRenderer.this).mc.currentScreen.getClass().getCanonicalName();
                }
            });
            crashreportcategory.addCrashSectionCallable("Mouse location", new Callable<String>(){

                @Override
                public String call() throws Exception {
                    return String.format("Scaled: (%d, %d). Absolute: (%d, %d)", k1, l1, Mouse.getX(), Mouse.getY());
                }
            });
            crashreportcategory.addCrashSectionCallable("Screen size", new Callable<String>(){

                @Override
                public String call() throws Exception {
                    return String.format("Scaled: (%d, %d). Absolute: (%d, %d). Scale factor of %d", scaledresolution.getScaledWidth(), scaledresolution.getScaledHeight(), ((EntityRenderer)EntityRenderer.this).mc.displayWidth, ((EntityRenderer)EntityRenderer.this).mc.displayHeight, scaledresolution.getScaleFactor());
                }
            });
            throw new ReportedException(crashreport);
        }
    }

    public void renderStreamIndicator(float partialTicks) {
        this.setupOverlayRendering();
        this.mc.ingameGUI.renderStreamIndicator(new ScaledResolution(this.mc));
    }

    private boolean isDrawBlockOutline() {
        if (!this.drawBlockOutline) {
            return false;
        }
        Entity entity = this.mc.getRenderViewEntity();
        boolean flag = entity instanceof EntityPlayer && !this.mc.gameSettings.hideGUI;
        if (!flag) return flag;
        if (((EntityPlayer)entity).capabilities.allowEdit) return flag;
        ItemStack itemstack = ((EntityPlayer)entity).getCurrentEquippedItem();
        if (this.mc.objectMouseOver == null) return flag;
        if (this.mc.objectMouseOver.typeOfHit != MovingObjectPosition.MovingObjectType.BLOCK) return flag;
        BlockPos blockpos = this.mc.objectMouseOver.getBlockPos();
        Block block = this.mc.theWorld.getBlockState(blockpos).getBlock();
        if (this.mc.playerController.getCurrentGameType() == WorldSettings.GameType.SPECTATOR) {
            if (!block.hasTileEntity()) return false;
            if (!(this.mc.theWorld.getTileEntity(blockpos) instanceof IInventory)) return false;
            return true;
        }
        if (itemstack == null) return false;
        if (itemstack.canDestroy(block)) return true;
        if (!itemstack.canPlaceOn(block)) return false;
        return true;
    }

    private void renderWorldDirections(float partialTicks) {
        if (!this.mc.gameSettings.showDebugInfo) return;
        if (this.mc.gameSettings.hideGUI) return;
        if (this.mc.thePlayer.hasReducedDebug()) return;
        if (this.mc.gameSettings.reducedDebugInfo) return;
        Entity entity = this.mc.getRenderViewEntity();
        GlStateManager.enableBlend();
        GlStateManager.tryBlendFuncSeparate(770, 771, 1, 0);
        GL11.glLineWidth((float)1.0f);
        GlStateManager.disableTexture2D();
        GlStateManager.depthMask(false);
        GlStateManager.pushMatrix();
        GlStateManager.matrixMode(5888);
        GlStateManager.loadIdentity();
        this.orientCamera(partialTicks);
        GlStateManager.translate(0.0f, entity.getEyeHeight(), 0.0f);
        RenderGlobal.func_181563_a(new AxisAlignedBB(0.0, 0.0, 0.0, 0.005, 1.0E-4, 1.0E-4), 255, 0, 0, 255);
        RenderGlobal.func_181563_a(new AxisAlignedBB(0.0, 0.0, 0.0, 1.0E-4, 1.0E-4, 0.005), 0, 0, 255, 255);
        RenderGlobal.func_181563_a(new AxisAlignedBB(0.0, 0.0, 0.0, 1.0E-4, 0.0033, 1.0E-4), 0, 255, 0, 255);
        GlStateManager.popMatrix();
        GlStateManager.depthMask(true);
        GlStateManager.enableTexture2D();
        GlStateManager.disableBlend();
    }

    public void renderWorld(float partialTicks, long finishTimeNano) {
        this.updateLightmap(partialTicks);
        if (this.mc.getRenderViewEntity() == null) {
            this.mc.setRenderViewEntity(this.mc.thePlayer);
        }
        this.getMouseOver(partialTicks);
        GlStateManager.enableDepth();
        GlStateManager.enableAlpha();
        GlStateManager.alphaFunc(516, 0.5f);
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
        ActiveRenderInfo.updateRenderInfo(this.mc.thePlayer, FreeLook.getPerspective() == 2);
        this.mc.mcProfiler.endStartSection("frustum");
        ClippingHelperImpl.getInstance();
        this.mc.mcProfiler.endStartSection("culling");
        Frustum icamera = new Frustum();
        Entity entity = this.mc.getRenderViewEntity();
        double d0 = entity.lastTickPosX + (entity.posX - entity.lastTickPosX) * (double)partialTicks;
        double d1 = entity.lastTickPosY + (entity.posY - entity.lastTickPosY) * (double)partialTicks;
        double d2 = entity.lastTickPosZ + (entity.posZ - entity.lastTickPosZ) * (double)partialTicks;
        icamera.setPosition(d0, d1, d2);
        if (this.mc.gameSettings.renderDistanceChunks >= 4) {
            this.setupFog(-1, partialTicks);
            this.mc.mcProfiler.endStartSection("sky");
            GlStateManager.matrixMode(5889);
            GlStateManager.loadIdentity();
            Project.gluPerspective((float)this.getFOVModifier(partialTicks, true), (float)((float)this.mc.displayWidth / (float)this.mc.displayHeight), (float)0.05f, (float)(this.farPlaneDistance * 2.0f));
            GlStateManager.matrixMode(5888);
            renderglobal.renderSky(partialTicks, pass);
            GlStateManager.matrixMode(5889);
            GlStateManager.loadIdentity();
            Project.gluPerspective((float)this.getFOVModifier(partialTicks, true), (float)((float)this.mc.displayWidth / (float)this.mc.displayHeight), (float)0.05f, (float)(this.farPlaneDistance * MathHelper.SQRT_2));
            GlStateManager.matrixMode(5888);
        }
        this.setupFog(0, partialTicks);
        GlStateManager.shadeModel(7425);
        if (entity.posY + (double)entity.getEyeHeight() < 128.0) {
            this.renderCloudsCheck(renderglobal, partialTicks, pass);
        }
        this.mc.mcProfiler.endStartSection("prepareterrain");
        this.setupFog(0, partialTicks);
        this.mc.getTextureManager().bindTexture(TextureMap.locationBlocksTexture);
        RenderHelper.disableStandardItemLighting();
        this.mc.mcProfiler.endStartSection("terrain_setup");
        renderglobal.setupTerrain(entity, partialTicks, icamera, this.frameCount++, this.mc.thePlayer.isSpectator() || Freecam.fakePlayer != null);
        if (pass == 0 || pass == 2) {
            this.mc.mcProfiler.endStartSection("updatechunks");
            this.mc.renderGlobal.updateChunks(finishTimeNano);
        }
        this.mc.mcProfiler.endStartSection("terrain");
        GlStateManager.matrixMode(5888);
        GlStateManager.pushMatrix();
        GlStateManager.disableAlpha();
        renderglobal.renderBlockLayer(EnumWorldBlockLayer.SOLID, partialTicks, pass, entity);
        GlStateManager.enableAlpha();
        renderglobal.renderBlockLayer(EnumWorldBlockLayer.CUTOUT_MIPPED, partialTicks, pass, entity);
        this.mc.getTextureManager().getTexture(TextureMap.locationBlocksTexture).setBlurMipmap(false, false);
        renderglobal.renderBlockLayer(EnumWorldBlockLayer.CUTOUT, partialTicks, pass, entity);
        this.mc.getTextureManager().getTexture(TextureMap.locationBlocksTexture).restoreLastBlurMipmap();
        GlStateManager.shadeModel(7424);
        GlStateManager.alphaFunc(516, 0.1f);
        if (!this.debugView) {
            GlStateManager.matrixMode(5888);
            GlStateManager.popMatrix();
            GlStateManager.pushMatrix();
            RenderHelper.enableStandardItemLighting();
            this.mc.mcProfiler.endStartSection("entities");
            renderglobal.renderEntities(entity, icamera, partialTicks);
            RenderHelper.disableStandardItemLighting();
            this.disableLightmap();
            GlStateManager.matrixMode(5888);
            GlStateManager.popMatrix();
            GlStateManager.pushMatrix();
            if (this.mc.objectMouseOver != null && entity.isInsideOfMaterial(Material.water) && flag) {
                EntityPlayer entityplayer = (EntityPlayer)entity;
                GlStateManager.disableAlpha();
                this.mc.mcProfiler.endStartSection("outline");
                renderglobal.drawSelectionBox(entityplayer, this.mc.objectMouseOver, 0, partialTicks);
                GlStateManager.enableAlpha();
            }
        }
        GlStateManager.matrixMode(5888);
        GlStateManager.popMatrix();
        if (flag && this.mc.objectMouseOver != null && !entity.isInsideOfMaterial(Material.water)) {
            EntityPlayer entityplayer1 = (EntityPlayer)entity;
            GlStateManager.disableAlpha();
            this.mc.mcProfiler.endStartSection("outline");
            renderglobal.drawSelectionBox(entityplayer1, this.mc.objectMouseOver, 0, partialTicks);
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
        GlStateManager.shadeModel(7424);
        GlStateManager.depthMask(true);
        GlStateManager.enableCull();
        GlStateManager.disableBlend();
        GlStateManager.disableFog();
        if (entity.posY + (double)entity.getEyeHeight() >= 128.0) {
            this.mc.mcProfiler.endStartSection("aboveClouds");
            this.renderCloudsCheck(renderglobal, partialTicks, pass);
        }
        LoseBypass.eventBus.post(new Render3DEvent(partialTicks));
        this.mc.mcProfiler.endStartSection("hand");
        if (!this.renderHand) return;
        GlStateManager.clear(256);
        this.renderHand(partialTicks, pass);
        this.renderWorldDirections(partialTicks);
    }

    private void renderCloudsCheck(RenderGlobal renderGlobalIn, float partialTicks, int pass) {
        if (this.mc.gameSettings.func_181147_e() == 0) return;
        this.mc.mcProfiler.endStartSection("clouds");
        GlStateManager.matrixMode(5889);
        GlStateManager.loadIdentity();
        Project.gluPerspective((float)this.getFOVModifier(partialTicks, true), (float)((float)this.mc.displayWidth / (float)this.mc.displayHeight), (float)0.05f, (float)(this.farPlaneDistance * 4.0f));
        GlStateManager.matrixMode(5888);
        GlStateManager.pushMatrix();
        this.setupFog(0, partialTicks);
        renderGlobalIn.renderClouds(partialTicks, pass);
        GlStateManager.disableFog();
        GlStateManager.popMatrix();
        GlStateManager.matrixMode(5889);
        GlStateManager.loadIdentity();
        Project.gluPerspective((float)this.getFOVModifier(partialTicks, true), (float)((float)this.mc.displayWidth / (float)this.mc.displayHeight), (float)0.05f, (float)(this.farPlaneDistance * MathHelper.SQRT_2));
        GlStateManager.matrixMode(5888);
    }

    private void addRainParticles() {
        float f = this.mc.theWorld.getRainStrength(1.0f);
        if (!this.mc.gameSettings.fancyGraphics) {
            f /= 2.0f;
        }
        if (f == 0.0f) return;
        this.random.setSeed((long)this.rendererUpdateCount * 312987231L);
        Entity entity = this.mc.getRenderViewEntity();
        WorldClient world = this.mc.theWorld;
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
            BlockPos blockpos1 = world.getPrecipitationHeight(blockpos.add(this.random.nextInt(i) - this.random.nextInt(i), 0, this.random.nextInt(i) - this.random.nextInt(i)));
            BiomeGenBase biomegenbase = world.getBiomeGenForCoords(blockpos1);
            BlockPos blockpos2 = blockpos1.down();
            Block block = world.getBlockState(blockpos2).getBlock();
            if (blockpos1.getY() > blockpos.getY() + i || blockpos1.getY() < blockpos.getY() - i || !biomegenbase.canSpawnLightningBolt() || !(biomegenbase.getFloatTemperature(blockpos1) >= 0.15f)) continue;
            double d3 = this.random.nextDouble();
            double d4 = this.random.nextDouble();
            if (block.getMaterial() == Material.lava) {
                this.mc.theWorld.spawnParticle(EnumParticleTypes.SMOKE_NORMAL, (double)blockpos1.getX() + d3, (double)((float)blockpos1.getY() + 0.1f) - block.getBlockBoundsMinY(), (double)blockpos1.getZ() + d4, 0.0, 0.0, 0.0, new int[0]);
                continue;
            }
            if (block.getMaterial() == Material.air) continue;
            block.setBlockBoundsBasedOnState(world, blockpos2);
            if (this.random.nextInt(++j) == 0) {
                d0 = (double)blockpos2.getX() + d3;
                d1 = (double)((float)blockpos2.getY() + 0.1f) + block.getBlockBoundsMaxY() - 1.0;
                d2 = (double)blockpos2.getZ() + d4;
            }
            this.mc.theWorld.spawnParticle(EnumParticleTypes.WATER_DROP, (double)blockpos2.getX() + d3, (double)((float)blockpos2.getY() + 0.1f) + block.getBlockBoundsMaxY(), (double)blockpos2.getZ() + d4, 0.0, 0.0, 0.0, new int[0]);
        }
        if (j <= 0) return;
        if (this.random.nextInt(3) >= this.rainSoundCounter++) return;
        this.rainSoundCounter = 0;
        if (d1 > (double)(blockpos.getY() + 1) && world.getPrecipitationHeight(blockpos).getY() > MathHelper.floor_float(blockpos.getY())) {
            this.mc.theWorld.playSound(d0, d1, d2, "ambient.weather.rain", 0.1f, 0.5f, false);
            return;
        }
        this.mc.theWorld.playSound(d0, d1, d2, "ambient.weather.rain", 0.2f, 1.0f, false);
    }

    protected void renderRainSnow(float partialTicks) {
        float f = this.mc.theWorld.getRainStrength(partialTicks);
        if (!(f > 0.0f)) return;
        this.enableLightmap();
        Entity entity = this.mc.getRenderViewEntity();
        WorldClient world = this.mc.theWorld;
        int i = MathHelper.floor_double(entity.posX);
        int j = MathHelper.floor_double(entity.posY);
        int k = MathHelper.floor_double(entity.posZ);
        Tessellator tessellator = Tessellator.getInstance();
        WorldRenderer worldrenderer = tessellator.getWorldRenderer();
        GlStateManager.disableCull();
        GL11.glNormal3f((float)0.0f, (float)1.0f, (float)0.0f);
        GlStateManager.enableBlend();
        GlStateManager.tryBlendFuncSeparate(770, 771, 1, 0);
        GlStateManager.alphaFunc(516, 0.1f);
        double d0 = entity.lastTickPosX + (entity.posX - entity.lastTickPosX) * (double)partialTicks;
        double d1 = entity.lastTickPosY + (entity.posY - entity.lastTickPosY) * (double)partialTicks;
        double d2 = entity.lastTickPosZ + (entity.posZ - entity.lastTickPosZ) * (double)partialTicks;
        int l = MathHelper.floor_double(d1);
        int i1 = 5;
        if (this.mc.gameSettings.fancyGraphics) {
            i1 = 10;
        }
        int j1 = -1;
        float f1 = (float)this.rendererUpdateCount + partialTicks;
        worldrenderer.setTranslation(-d0, -d1, -d2);
        GlStateManager.color(1.0f, 1.0f, 1.0f, 1.0f);
        BlockPos.MutableBlockPos blockpos$mutableblockpos = new BlockPos.MutableBlockPos();
        for (int k1 = k - i1; k1 <= k + i1; ++k1) {
            for (int l1 = i - i1; l1 <= i + i1; ++l1) {
                int i2 = (k1 - k + 16) * 32 + l1 - i + 16;
                double d3 = (double)this.rainXCoords[i2] * 0.5;
                double d4 = (double)this.rainYCoords[i2] * 0.5;
                blockpos$mutableblockpos.func_181079_c(l1, 0, k1);
                BiomeGenBase biomegenbase = world.getBiomeGenForCoords(blockpos$mutableblockpos);
                if (!biomegenbase.canSpawnLightningBolt() && !biomegenbase.getEnableSnow()) continue;
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
                blockpos$mutableblockpos.func_181079_c(l1, k2, k1);
                float f2 = biomegenbase.getFloatTemperature(blockpos$mutableblockpos);
                if (world.getWorldChunkManager().getTemperatureAtHeight(f2, j2) >= 0.15f) {
                    if (j1 != 0) {
                        if (j1 >= 0) {
                            tessellator.draw();
                        }
                        j1 = 0;
                        this.mc.getTextureManager().bindTexture(locationRainPng);
                        worldrenderer.begin(7, DefaultVertexFormats.PARTICLE_POSITION_TEX_COLOR_LMAP);
                    }
                    double d5 = ((double)(this.rendererUpdateCount + l1 * l1 * 3121 + l1 * 45238971 + k1 * k1 * 418711 + k1 * 13761 & 0x1F) + (double)partialTicks) / 32.0 * (3.0 + this.random.nextDouble());
                    double d6 = (double)((float)l1 + 0.5f) - entity.posX;
                    double d7 = (double)((float)k1 + 0.5f) - entity.posZ;
                    float f3 = MathHelper.sqrt_double(d6 * d6 + d7 * d7) / (float)i1;
                    float f4 = ((1.0f - f3 * f3) * 0.5f + 0.5f) * f;
                    blockpos$mutableblockpos.func_181079_c(l1, i3, k1);
                    int j3 = world.getCombinedLight(blockpos$mutableblockpos, 0);
                    int k3 = j3 >> 16 & 0xFFFF;
                    int l3 = j3 & 0xFFFF;
                    worldrenderer.pos((double)l1 - d3 + 0.5, k2, (double)k1 - d4 + 0.5).tex(0.0, (double)k2 * 0.25 + d5).color(1.0f, 1.0f, 1.0f, f4).lightmap(k3, l3).endVertex();
                    worldrenderer.pos((double)l1 + d3 + 0.5, k2, (double)k1 + d4 + 0.5).tex(1.0, (double)k2 * 0.25 + d5).color(1.0f, 1.0f, 1.0f, f4).lightmap(k3, l3).endVertex();
                    worldrenderer.pos((double)l1 + d3 + 0.5, l2, (double)k1 + d4 + 0.5).tex(1.0, (double)l2 * 0.25 + d5).color(1.0f, 1.0f, 1.0f, f4).lightmap(k3, l3).endVertex();
                    worldrenderer.pos((double)l1 - d3 + 0.5, l2, (double)k1 - d4 + 0.5).tex(0.0, (double)l2 * 0.25 + d5).color(1.0f, 1.0f, 1.0f, f4).lightmap(k3, l3).endVertex();
                    continue;
                }
                if (j1 != 1) {
                    if (j1 >= 0) {
                        tessellator.draw();
                    }
                    j1 = 1;
                    this.mc.getTextureManager().bindTexture(locationSnowPng);
                    worldrenderer.begin(7, DefaultVertexFormats.PARTICLE_POSITION_TEX_COLOR_LMAP);
                }
                double d8 = ((float)(this.rendererUpdateCount & 0x1FF) + partialTicks) / 512.0f;
                double d9 = this.random.nextDouble() + (double)f1 * 0.01 * (double)((float)this.random.nextGaussian());
                double d10 = this.random.nextDouble() + (double)(f1 * (float)this.random.nextGaussian()) * 0.001;
                double d11 = (double)((float)l1 + 0.5f) - entity.posX;
                double d12 = (double)((float)k1 + 0.5f) - entity.posZ;
                float f6 = MathHelper.sqrt_double(d11 * d11 + d12 * d12) / (float)i1;
                float f5 = ((1.0f - f6 * f6) * 0.3f + 0.5f) * f;
                blockpos$mutableblockpos.func_181079_c(l1, i3, k1);
                int i4 = (world.getCombinedLight(blockpos$mutableblockpos, 0) * 3 + 0xF000F0) / 4;
                int j4 = i4 >> 16 & 0xFFFF;
                int k4 = i4 & 0xFFFF;
                worldrenderer.pos((double)l1 - d3 + 0.5, k2, (double)k1 - d4 + 0.5).tex(0.0 + d9, (double)k2 * 0.25 + d8 + d10).color(1.0f, 1.0f, 1.0f, f5).lightmap(j4, k4).endVertex();
                worldrenderer.pos((double)l1 + d3 + 0.5, k2, (double)k1 + d4 + 0.5).tex(1.0 + d9, (double)k2 * 0.25 + d8 + d10).color(1.0f, 1.0f, 1.0f, f5).lightmap(j4, k4).endVertex();
                worldrenderer.pos((double)l1 + d3 + 0.5, l2, (double)k1 + d4 + 0.5).tex(1.0 + d9, (double)l2 * 0.25 + d8 + d10).color(1.0f, 1.0f, 1.0f, f5).lightmap(j4, k4).endVertex();
                worldrenderer.pos((double)l1 - d3 + 0.5, l2, (double)k1 - d4 + 0.5).tex(0.0 + d9, (double)l2 * 0.25 + d8 + d10).color(1.0f, 1.0f, 1.0f, f5).lightmap(j4, k4).endVertex();
            }
        }
        if (j1 >= 0) {
            tessellator.draw();
        }
        worldrenderer.setTranslation(0.0, 0.0, 0.0);
        GlStateManager.enableCull();
        GlStateManager.disableBlend();
        GlStateManager.alphaFunc(516, 0.1f);
        this.disableLightmap();
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
        float f9;
        WorldClient world = this.mc.theWorld;
        Entity entity = this.mc.getRenderViewEntity();
        float f = 0.25f + 0.75f * (float)this.mc.gameSettings.renderDistanceChunks / 32.0f;
        f = 1.0f - (float)Math.pow(f, 0.25);
        Vec3 vec3 = world.getSkyColor(this.mc.getRenderViewEntity(), partialTicks);
        float f1 = (float)vec3.xCoord;
        float f2 = (float)vec3.yCoord;
        float f3 = (float)vec3.zCoord;
        Vec3 vec31 = world.getFogColor(partialTicks);
        this.fogColorRed = (float)vec31.xCoord;
        this.fogColorGreen = (float)vec31.yCoord;
        this.fogColorBlue = (float)vec31.zCoord;
        if (this.mc.gameSettings.renderDistanceChunks >= 4) {
            float[] afloat;
            double d0 = -1.0;
            Vec3 vec32 = MathHelper.sin(world.getCelestialAngleRadians(partialTicks)) > 0.0f ? new Vec3(d0, 0.0, 0.0) : new Vec3(1.0, 0.0, 0.0);
            float f5 = (float)entity.getLook(partialTicks).dotProduct(vec32);
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
        Block block = ActiveRenderInfo.getBlockAtEntityViewpoint(this.mc.theWorld, entity, partialTicks);
        if (this.cloudFog) {
            Vec3 vec33 = world.getCloudColour(partialTicks);
            this.fogColorRed = (float)vec33.xCoord;
            this.fogColorGreen = (float)vec33.yCoord;
            this.fogColorBlue = (float)vec33.zCoord;
        } else if (block.getMaterial() == Material.water) {
            float f12 = (float)EnchantmentHelper.getRespiration(entity) * 0.2f;
            if (entity instanceof EntityLivingBase && ((EntityLivingBase)entity).isPotionActive(Potion.waterBreathing)) {
                f12 = f12 * 0.3f + 0.6f;
            }
            this.fogColorRed = 0.02f + f12;
            this.fogColorGreen = 0.02f + f12;
            this.fogColorBlue = 0.2f + f12;
        } else if (block.getMaterial() == Material.lava) {
            this.fogColorRed = 0.6f;
            this.fogColorGreen = 0.1f;
            this.fogColorBlue = 0.0f;
        }
        float f13 = this.fogColor2 + (this.fogColor1 - this.fogColor2) * partialTicks;
        this.fogColorRed *= f13;
        this.fogColorGreen *= f13;
        this.fogColorBlue *= f13;
        double d1 = (entity.lastTickPosY + (entity.posY - entity.lastTickPosY) * (double)partialTicks) * world.provider.getVoidFogYFactor();
        if (entity instanceof EntityLivingBase && ((EntityLivingBase)entity).isPotionActive(Potion.blindness)) {
            int i = ((EntityLivingBase)entity).getActivePotionEffect(Potion.blindness).getDuration();
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
        GlStateManager.clearColor(this.fogColorRed, this.fogColorGreen, this.fogColorBlue, 0.0f);
    }

    private void setupFog(int p_78468_1_, float partialTicks) {
        Entity entity = this.mc.getRenderViewEntity();
        boolean flag = false;
        if (entity instanceof EntityPlayer) {
            flag = ((EntityPlayer)entity).capabilities.isCreativeMode;
        }
        GL11.glFog((int)2918, (FloatBuffer)this.setFogColorBuffer(this.fogColorRed, this.fogColorGreen, this.fogColorBlue, 1.0f));
        GL11.glNormal3f((float)0.0f, (float)-1.0f, (float)0.0f);
        GlStateManager.color(1.0f, 1.0f, 1.0f, 1.0f);
        Block block = ActiveRenderInfo.getBlockAtEntityViewpoint(this.mc.theWorld, entity, partialTicks);
        if (entity instanceof EntityLivingBase && ((EntityLivingBase)entity).isPotionActive(Potion.blindness)) {
            float f1 = 5.0f;
            int i = ((EntityLivingBase)entity).getActivePotionEffect(Potion.blindness).getDuration();
            if (i < 20) {
                f1 = 5.0f + (this.farPlaneDistance - 5.0f) * (1.0f - (float)i / 20.0f);
            }
            GlStateManager.setFog(9729);
            if (p_78468_1_ == -1) {
                GlStateManager.setFogStart(0.0f);
                GlStateManager.setFogEnd(f1 * 0.8f);
            } else {
                GlStateManager.setFogStart(f1 * 0.25f);
                GlStateManager.setFogEnd(f1);
            }
            if (GLContext.getCapabilities().GL_NV_fog_distance) {
                GL11.glFogi((int)34138, (int)34139);
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
        } else if (block.getMaterial() == Material.lava) {
            GlStateManager.setFog(2048);
            GlStateManager.setFogDensity(2.0f);
        } else {
            float f = this.farPlaneDistance;
            GlStateManager.setFog(9729);
            if (p_78468_1_ == -1) {
                GlStateManager.setFogStart(0.0f);
                GlStateManager.setFogEnd(f);
            } else {
                GlStateManager.setFogStart(f * 0.75f);
                GlStateManager.setFogEnd(f);
            }
            if (GLContext.getCapabilities().GL_NV_fog_distance) {
                GL11.glFogi((int)34138, (int)34139);
            }
            if (this.mc.theWorld.provider.doesXZShowFog((int)entity.posX, (int)entity.posZ)) {
                GlStateManager.setFogStart(f * 0.05f);
                GlStateManager.setFogEnd(Math.min(f, 192.0f) * 0.5f);
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
}

