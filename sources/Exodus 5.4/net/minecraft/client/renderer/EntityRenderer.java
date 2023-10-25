/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.google.common.base.Predicate
 *  com.google.common.base.Predicates
 *  com.google.gson.JsonSyntaxException
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
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.FloatBuffer;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.Callable;
import me.Tengoku.Terror.Exodus;
import me.Tengoku.Terror.event.Event;
import me.Tengoku.Terror.event.EventType;
import me.Tengoku.Terror.event.events.Event3D;
import me.Tengoku.Terror.event.events.EventHurtCam;
import me.Tengoku.Terror.module.movement.LongJump;
import me.Tengoku.Terror.util.MathUtils;
import me.Tengoku.Terror.util.TimerUtils;
import net.minecraft.block.Block;
import net.minecraft.block.BlockBed;
import net.minecraft.block.material.Material;
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
    private int debugViewDirection = 0;
    private final IResourceManager resourceManager;
    private float bossColorModifier;
    private float bossColorModifierPrev;
    private final ResourceLocation locationLightMap;
    private MouseFilter mouseFilterXAxis;
    public final ItemRenderer itemRenderer;
    private static final Logger logger = LogManager.getLogger();
    private double cameraPitch;
    private Random random = new Random();
    private float[] rainYCoords;
    private MouseFilter mouseFilterYAxis;
    private boolean lightmapUpdateNeeded;
    private double cameraZoom = 1.0;
    private boolean cloudFog;
    private float fovModifierHandPrev;
    private final DynamicTexture lightmapTexture;
    private float fogColorRed;
    private float fogColor2;
    public ShaderGroup theShaderGroup;
    private boolean renderHand = true;
    private float smoothCamFilterY;
    private float fogColor1;
    private float thirdPersonDistance = 4.0f;
    private long prevFrameTime;
    private final MapItemRenderer theMapItemRenderer;
    private Entity pointedEntity;
    private float fovModifierHand;
    private boolean debugView = false;
    private float smoothCamYaw;
    private int rainSoundCounter;
    private float fogColorBlue;
    private double cameraYaw;
    private static final ResourceLocation[] shaderResourceLocations;
    private float smoothCamPartialTicks;
    private final int[] lightmapColors;
    private boolean drawBlockOutline = true;
    TimerUtils timer;
    private float farPlaneDistance;
    private float thirdPersonDistanceTemp = 4.0f;
    public static int anaglyphField;
    private long renderEndNanoTime;
    private float[] rainXCoords;
    private float smoothCamPitch;
    private FloatBuffer fogColorBuffer;
    private float smoothCamFilterX;
    private float fogColorGreen;
    private Minecraft mc;
    private float torchFlickerX;
    public static final int shaderCount;
    private static final ResourceLocation locationSnowPng;
    private int shaderIndex;
    private boolean useShader = false;
    public static boolean anaglyphEnable;
    private int rendererUpdateCount;
    private static final ResourceLocation locationRainPng;
    private float torchFlickerDX;
    private int frameCount = 0;

    static {
        locationRainPng = new ResourceLocation("textures/environment/rain.png");
        locationSnowPng = new ResourceLocation("textures/environment/snow.png");
        shaderResourceLocations = new ResourceLocation[]{new ResourceLocation("shaders/post/notch.json"), new ResourceLocation("shaders/post/fxaa.json"), new ResourceLocation("shaders/post/art.json"), new ResourceLocation("shaders/post/bumpy.json"), new ResourceLocation("shaders/post/blobs2.json"), new ResourceLocation("shaders/post/pencil.json"), new ResourceLocation("shaders/post/color_convolve.json"), new ResourceLocation("shaders/post/deconverge.json"), new ResourceLocation("shaders/post/flip.json"), new ResourceLocation("shaders/post/invert.json"), new ResourceLocation("shaders/post/ntsc.json"), new ResourceLocation("shaders/post/outline.json"), new ResourceLocation("shaders/post/phosphor.json"), new ResourceLocation("shaders/post/scan_pincushion.json"), new ResourceLocation("shaders/post/sobel.json"), new ResourceLocation("shaders/post/bits.json"), new ResourceLocation("shaders/post/desaturate.json"), new ResourceLocation("shaders/post/green.json"), new ResourceLocation("shaders/post/blur.json"), new ResourceLocation("shaders/post/wobble.json"), new ResourceLocation("shaders/post/blobs.json"), new ResourceLocation("shaders/post/antialias.json"), new ResourceLocation("shaders/post/creeper.json"), new ResourceLocation("shaders/post/spider.json")};
        shaderCount = shaderResourceLocations.length;
    }

    public boolean isShaderActive() {
        return OpenGlHelper.shadersSupported && this.theShaderGroup != null;
    }

    private void updateFogColor(float f) {
        float f2;
        float f3;
        float f4;
        Object object;
        WorldClient worldClient = Minecraft.theWorld;
        Entity entity = this.mc.getRenderViewEntity();
        float f5 = 0.25f + 0.75f * (float)Minecraft.gameSettings.renderDistanceChunks / 32.0f;
        f5 = 1.0f - (float)Math.pow(f5, 0.25);
        Vec3 vec3 = worldClient.getSkyColor(this.mc.getRenderViewEntity(), f);
        float f6 = (float)vec3.xCoord;
        float f7 = (float)vec3.yCoord;
        float f8 = (float)vec3.zCoord;
        Vec3 vec32 = worldClient.getFogColor(f);
        this.fogColorRed = (float)vec32.xCoord;
        this.fogColorGreen = (float)vec32.yCoord;
        this.fogColorBlue = (float)vec32.zCoord;
        if (Minecraft.gameSettings.renderDistanceChunks >= 4) {
            float[] fArray;
            double d = -1.0;
            object = MathHelper.sin(worldClient.getCelestialAngleRadians(f)) > 0.0f ? new Vec3(d, 0.0, 0.0) : new Vec3(1.0, 0.0, 0.0);
            f4 = (float)entity.getLook(f).dotProduct((Vec3)object);
            if (f4 < 0.0f) {
                f4 = 0.0f;
            }
            if (f4 > 0.0f && (fArray = worldClient.provider.calcSunriseSunsetColors(worldClient.getCelestialAngle(f), f)) != null) {
                this.fogColorRed = this.fogColorRed * (1.0f - (f4 *= fArray[3])) + fArray[0] * f4;
                this.fogColorGreen = this.fogColorGreen * (1.0f - f4) + fArray[1] * f4;
                this.fogColorBlue = this.fogColorBlue * (1.0f - f4) + fArray[2] * f4;
            }
        }
        this.fogColorRed += (f6 - this.fogColorRed) * f5;
        this.fogColorGreen += (f7 - this.fogColorGreen) * f5;
        this.fogColorBlue += (f8 - this.fogColorBlue) * f5;
        float f9 = worldClient.getRainStrength(f);
        if (f9 > 0.0f) {
            f3 = 1.0f - f9 * 0.5f;
            float f10 = 1.0f - f9 * 0.4f;
            this.fogColorRed *= f3;
            this.fogColorGreen *= f3;
            this.fogColorBlue *= f10;
        }
        if ((f3 = worldClient.getThunderStrength(f)) > 0.0f) {
            float f11 = 1.0f - f3 * 0.5f;
            this.fogColorRed *= f11;
            this.fogColorGreen *= f11;
            this.fogColorBlue *= f11;
        }
        object = ActiveRenderInfo.getBlockAtEntityViewpoint(Minecraft.theWorld, entity, f);
        if (this.cloudFog) {
            Vec3 vec33 = worldClient.getCloudColour(f);
            this.fogColorRed = (float)vec33.xCoord;
            this.fogColorGreen = (float)vec33.yCoord;
            this.fogColorBlue = (float)vec33.zCoord;
        } else if (((Block)object).getMaterial() == Material.water) {
            f4 = (float)EnchantmentHelper.getRespiration(entity) * 0.2f;
            if (entity instanceof EntityLivingBase && ((EntityLivingBase)entity).isPotionActive(Potion.waterBreathing)) {
                f4 = f4 * 0.3f + 0.6f;
            }
            this.fogColorRed = 0.02f + f4;
            this.fogColorGreen = 0.02f + f4;
            this.fogColorBlue = 0.2f + f4;
        } else if (((Block)object).getMaterial() == Material.lava) {
            this.fogColorRed = 0.6f;
            this.fogColorGreen = 0.1f;
            this.fogColorBlue = 0.0f;
        }
        float f12 = this.fogColor2 + (this.fogColor1 - this.fogColor2) * f;
        this.fogColorRed *= f12;
        this.fogColorGreen *= f12;
        this.fogColorBlue *= f12;
        double d = (entity.lastTickPosY + (entity.posY - entity.lastTickPosY) * (double)f) * worldClient.provider.getVoidFogYFactor();
        if (entity instanceof EntityLivingBase && ((EntityLivingBase)entity).isPotionActive(Potion.blindness)) {
            int n = ((EntityLivingBase)entity).getActivePotionEffect(Potion.blindness).getDuration();
            d = n < 20 ? (d *= (double)(1.0f - (float)n / 20.0f)) : 0.0;
        }
        if (d < 1.0) {
            if (d < 0.0) {
                d = 0.0;
            }
            d *= d;
            this.fogColorRed = (float)((double)this.fogColorRed * d);
            this.fogColorGreen = (float)((double)this.fogColorGreen * d);
            this.fogColorBlue = (float)((double)this.fogColorBlue * d);
        }
        if (this.bossColorModifier > 0.0f) {
            float f13 = this.bossColorModifierPrev + (this.bossColorModifier - this.bossColorModifierPrev) * f;
            this.fogColorRed = this.fogColorRed * (1.0f - f13) + this.fogColorRed * 0.7f * f13;
            this.fogColorGreen = this.fogColorGreen * (1.0f - f13) + this.fogColorGreen * 0.6f * f13;
            this.fogColorBlue = this.fogColorBlue * (1.0f - f13) + this.fogColorBlue * 0.6f * f13;
        }
        if (entity instanceof EntityLivingBase && ((EntityLivingBase)entity).isPotionActive(Potion.nightVision)) {
            float f14 = this.getNightVisionBrightness((EntityLivingBase)entity, f);
            f2 = 1.0f / this.fogColorRed;
            if (f2 > 1.0f / this.fogColorGreen) {
                f2 = 1.0f / this.fogColorGreen;
            }
            if (f2 > 1.0f / this.fogColorBlue) {
                f2 = 1.0f / this.fogColorBlue;
            }
            this.fogColorRed = this.fogColorRed * (1.0f - f14) + this.fogColorRed * f2 * f14;
            this.fogColorGreen = this.fogColorGreen * (1.0f - f14) + this.fogColorGreen * f2 * f14;
            this.fogColorBlue = this.fogColorBlue * (1.0f - f14) + this.fogColorBlue * f2 * f14;
        }
        if (Minecraft.gameSettings.anaglyph) {
            float f15 = (this.fogColorRed * 30.0f + this.fogColorGreen * 59.0f + this.fogColorBlue * 11.0f) / 100.0f;
            f2 = (this.fogColorRed * 30.0f + this.fogColorGreen * 70.0f) / 100.0f;
            float f16 = (this.fogColorRed * 30.0f + this.fogColorBlue * 70.0f) / 100.0f;
            this.fogColorRed = f15;
            this.fogColorGreen = f2;
            this.fogColorBlue = f16;
        }
        GlStateManager.clearColor(this.fogColorRed, this.fogColorGreen, this.fogColorBlue, 0.0f);
    }

    private void setupViewBobbing(float f) {
        if (this.mc.getRenderViewEntity() instanceof EntityPlayer) {
            EntityPlayer entityPlayer = (EntityPlayer)this.mc.getRenderViewEntity();
            float f2 = entityPlayer.distanceWalkedModified - entityPlayer.prevDistanceWalkedModified;
            float f3 = -(entityPlayer.distanceWalkedModified + f2 * f);
            float f4 = entityPlayer.prevCameraYaw + (entityPlayer.cameraYaw - entityPlayer.prevCameraYaw) * f;
            float f5 = entityPlayer.prevCameraPitch + (entityPlayer.cameraPitch - entityPlayer.prevCameraPitch) * f;
            GlStateManager.translate(MathHelper.sin(f3 * (float)Math.PI) * f4 * 0.5f, -Math.abs(MathHelper.cos(f3 * (float)Math.PI) * f4), 0.0f);
            GlStateManager.rotate(MathHelper.sin(f3 * (float)Math.PI) * f4 * 3.0f, 0.0f, 0.0f, 1.0f);
            GlStateManager.rotate(Math.abs(MathHelper.cos(f3 * (float)Math.PI - 0.2f) * f4) * 5.0f, 1.0f, 0.0f, 0.0f);
            GlStateManager.rotate(f5, 1.0f, 0.0f, 0.0f);
        }
    }

    private static /* synthetic */ boolean lambda$0(String string, String string2) {
        return string2.indexOf(string) > -1;
    }

    public void updateRenderer() {
        float f;
        float f2;
        if (OpenGlHelper.shadersSupported && ShaderLinkHelper.getStaticShaderLinkHelper() == null) {
            ShaderLinkHelper.setNewStaticShaderLinkHelper();
        }
        this.updateFovModifierHand();
        this.updateTorchFlicker();
        this.fogColor2 = this.fogColor1;
        this.thirdPersonDistanceTemp = this.thirdPersonDistance;
        if (Minecraft.gameSettings.smoothCamera) {
            f2 = Minecraft.gameSettings.mouseSensitivity * 0.6f + 0.2f;
            f = f2 * f2 * f2 * 8.0f;
            this.smoothCamFilterX = this.mouseFilterXAxis.smooth(this.smoothCamYaw, 0.05f * f);
            this.smoothCamFilterY = this.mouseFilterYAxis.smooth(this.smoothCamPitch, 0.05f * f);
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
            this.mc.setRenderViewEntity(Minecraft.thePlayer);
        }
        f2 = Minecraft.theWorld.getLightBrightness(new BlockPos(this.mc.getRenderViewEntity()));
        f = (float)Minecraft.gameSettings.renderDistanceChunks / 32.0f;
        float f3 = f2 * (1.0f - f) + f;
        this.fogColor1 += (f3 - this.fogColor1) * 0.1f;
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

    private boolean isDrawBlockOutline() {
        boolean bl;
        if (!this.drawBlockOutline) {
            return false;
        }
        Entity entity = this.mc.getRenderViewEntity();
        boolean bl2 = bl = entity instanceof EntityPlayer && !Minecraft.gameSettings.hideGUI;
        if (bl && !((EntityPlayer)entity).capabilities.allowEdit) {
            ItemStack itemStack = ((EntityPlayer)entity).getCurrentEquippedItem();
            if (this.mc.objectMouseOver != null && this.mc.objectMouseOver.typeOfHit == MovingObjectPosition.MovingObjectType.BLOCK) {
                BlockPos blockPos = this.mc.objectMouseOver.getBlockPos();
                Block block = Minecraft.theWorld.getBlockState(blockPos).getBlock();
                bl = Minecraft.playerController.getCurrentGameType() == WorldSettings.GameType.SPECTATOR ? block.hasTileEntity() && Minecraft.theWorld.getTileEntity(blockPos) instanceof IInventory : itemStack != null && (itemStack.canDestroy(block) || itemStack.canPlaceOn(block));
            }
        }
        return bl;
    }

    public void loadShader(ResourceLocation resourceLocation) {
        try {
            this.theShaderGroup = new ShaderGroup(this.mc.getTextureManager(), this.resourceManager, this.mc.getFramebuffer(), resourceLocation);
            this.theShaderGroup.createBindFramebuffers(this.mc.displayWidth, Minecraft.displayHeight);
            this.useShader = true;
        }
        catch (IOException iOException) {
            logger.warn("Failed to load shader: " + resourceLocation, (Throwable)iOException);
            this.shaderIndex = shaderCount;
            this.useShader = false;
        }
        catch (JsonSyntaxException jsonSyntaxException) {
            logger.warn("Failed to load shader: " + resourceLocation, (Throwable)jsonSyntaxException);
            this.shaderIndex = shaderCount;
            this.useShader = false;
        }
    }

    public ShaderGroup getShaderGroup() {
        return this.theShaderGroup;
    }

    private void setupCameraTransform(float f, int n) {
        float f2;
        this.farPlaneDistance = Minecraft.gameSettings.renderDistanceChunks * 16;
        GlStateManager.matrixMode(5889);
        GlStateManager.loadIdentity();
        float f3 = 0.07f;
        if (Minecraft.gameSettings.anaglyph) {
            GlStateManager.translate((float)(-(n * 2 - 1)) * f3, 0.0f, 0.0f);
        }
        if (this.cameraZoom != 1.0) {
            GlStateManager.translate((float)this.cameraYaw, (float)(-this.cameraPitch), 0.0f);
            GlStateManager.scale(this.cameraZoom, this.cameraZoom, 1.0);
        }
        Project.gluPerspective((float)this.getFOVModifier(f, true), (float)((float)this.mc.displayWidth / (float)Minecraft.displayHeight), (float)0.05f, (float)(this.farPlaneDistance * MathHelper.SQRT_2));
        GlStateManager.matrixMode(5888);
        GlStateManager.loadIdentity();
        if (Minecraft.gameSettings.anaglyph) {
            GlStateManager.translate((float)(n * 2 - 1) * 0.1f, 0.0f, 0.0f);
        }
        this.hurtCameraEffect(f);
        if (Minecraft.gameSettings.viewBobbing) {
            this.setupViewBobbing(f);
        }
        if ((f2 = Minecraft.thePlayer.prevTimeInPortal + (Minecraft.thePlayer.timeInPortal - Minecraft.thePlayer.prevTimeInPortal) * f) > 0.0f) {
            int n2 = 20;
            if (Minecraft.thePlayer.isPotionActive(Potion.confusion)) {
                n2 = 7;
            }
            float f4 = 5.0f / (f2 * f2 + 5.0f) - f2 * 0.04f;
            f4 *= f4;
            GlStateManager.rotate(((float)this.rendererUpdateCount + f) * (float)n2, 0.0f, 1.0f, 1.0f);
            GlStateManager.scale(1.0f / f4, 1.0f, 1.0f);
            GlStateManager.rotate(-((float)this.rendererUpdateCount + f) * (float)n2, 0.0f, 1.0f, 1.0f);
        }
        this.orientCamera(f);
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

    public void switchUseShader() {
        this.useShader = !this.useShader;
    }

    public void setupOverlayRendering() {
        ScaledResolution scaledResolution = new ScaledResolution(this.mc);
        GlStateManager.clear(256);
        GlStateManager.matrixMode(5889);
        GlStateManager.loadIdentity();
        GlStateManager.ortho(0.0, scaledResolution.getScaledWidth_double(), scaledResolution.getScaledHeight_double(), 0.0, 1000.0, 3000.0);
        GlStateManager.matrixMode(5888);
        GlStateManager.loadIdentity();
        GlStateManager.translate(0.0f, 0.0f, -2000.0f);
    }

    private FloatBuffer setFogColorBuffer(float f, float f2, float f3, float f4) {
        this.fogColorBuffer.clear();
        this.fogColorBuffer.put(f).put(f2).put(f3).put(f4);
        this.fogColorBuffer.flip();
        return this.fogColorBuffer;
    }

    public void loadEntityShader(Entity entity) {
        if (OpenGlHelper.shadersSupported) {
            if (this.theShaderGroup != null) {
                this.theShaderGroup.deleteShaderGroup();
            }
            this.theShaderGroup = null;
            if (entity instanceof EntityCreeper) {
                this.loadShader(new ResourceLocation("shaders/post/creeper.json"));
            } else if (entity instanceof EntitySpider) {
                this.loadShader(new ResourceLocation("shaders/post/spider.json"));
            } else if (entity instanceof EntityEnderman) {
                this.loadShader(new ResourceLocation("shaders/post/invert.json"));
            }
        }
    }

    private void updateTorchFlicker() {
        this.torchFlickerDX = (float)((double)this.torchFlickerDX + (Math.random() - Math.random()) * Math.random() * Math.random());
        this.torchFlickerDX = (float)((double)this.torchFlickerDX * 0.9);
        this.torchFlickerX += (this.torchFlickerDX - this.torchFlickerX) * 1.0f;
        this.lightmapUpdateNeeded = true;
    }

    public void orientCamera(float f) {
        Object object;
        Object object2;
        Object object3;
        Object object4;
        Entity entity = this.mc.getRenderViewEntity();
        float f2 = entity.getEyeHeight();
        if (Exodus.INSTANCE.getModuleManager().getModuleByClass(LongJump.class).isToggled() && Exodus.INSTANCE.getSettingsManager().getSettingByClass("LongJump Mode", LongJump.class).getValString().equalsIgnoreCase("Hypixel")) {
            f2 = (float)(LongJump.yPos - MathUtils.getDifference(LongJump.yPos2, Minecraft.thePlayer.posY));
        }
        double d = entity.prevPosX + (entity.posX - entity.prevPosX) * (double)f;
        double d2 = entity.prevPosY + (entity.posY - entity.prevPosY) * (double)f + (double)f2;
        double d3 = entity.prevPosZ + (entity.posZ - entity.prevPosZ) * (double)f;
        if (entity instanceof EntityLivingBase && ((EntityLivingBase)entity).isPlayerSleeping()) {
            f2 = (float)((double)f2 + 1.0);
            GlStateManager.translate(0.0f, 0.3f, 0.0f);
            if (!Minecraft.gameSettings.debugCamEnable) {
                object4 = new BlockPos(entity);
                object3 = Minecraft.theWorld.getBlockState((BlockPos)object4);
                object2 = object3.getBlock();
                if (object2 == Blocks.bed) {
                    int n = object3.getValue(BlockBed.FACING).getHorizontalIndex();
                    GlStateManager.rotate(n * 90, 0.0f, 1.0f, 0.0f);
                }
                GlStateManager.rotate(entity.prevRotationYaw + (entity.rotationYaw - entity.prevRotationYaw) * f + 180.0f, 0.0f, -1.0f, 0.0f);
                GlStateManager.rotate(entity.prevRotationPitch + (entity.rotationPitch - entity.prevRotationPitch) * f, -1.0f, 0.0f, 0.0f);
            }
        } else if (Minecraft.gameSettings.thirdPersonView > 0) {
            double d4 = this.thirdPersonDistanceTemp + (this.thirdPersonDistance - this.thirdPersonDistanceTemp) * f;
            if (Minecraft.gameSettings.debugCamEnable) {
                GlStateManager.translate(0.0f, 0.0f, (float)(-d4));
            } else {
                float f3 = entity.rotationYaw;
                float f4 = entity.rotationPitch;
                if (Minecraft.gameSettings.thirdPersonView == 2) {
                    f4 += 180.0f;
                }
                double d5 = (double)(-MathHelper.sin(f3 / 180.0f * (float)Math.PI) * MathHelper.cos(f4 / 180.0f * (float)Math.PI)) * d4;
                double d6 = (double)(MathHelper.cos(f3 / 180.0f * (float)Math.PI) * MathHelper.cos(f4 / 180.0f * (float)Math.PI)) * d4;
                double d7 = (double)(-MathHelper.sin(f4 / 180.0f * (float)Math.PI)) * d4;
                int n = 0;
                while (n < 8) {
                    double d8;
                    float f5 = (n & 1) * 2 - 1;
                    float f6 = (n >> 1 & 1) * 2 - 1;
                    float f7 = (n >> 2 & 1) * 2 - 1;
                    if ((object = Minecraft.theWorld.rayTraceBlocks(new Vec3(d + (double)(f5 *= 0.1f), d2 + (double)(f6 *= 0.1f), d3 + (double)(f7 *= 0.1f)), new Vec3(d - d5 + (double)f5 + (double)f7, d2 - d7 + (double)f6, d3 - d6 + (double)f7))) != null && (d8 = ((MovingObjectPosition)object).hitVec.distanceTo(new Vec3(d, d2, d3))) < d4) {
                        d4 = d8;
                    }
                    ++n;
                }
                if (Minecraft.gameSettings.thirdPersonView == 2) {
                    GlStateManager.rotate(180.0f, 0.0f, 1.0f, 0.0f);
                }
                GlStateManager.rotate(entity.rotationPitch - f4, 1.0f, 0.0f, 0.0f);
                GlStateManager.rotate(entity.rotationYaw - f3, 0.0f, 1.0f, 0.0f);
                GlStateManager.translate(0.0f, 0.0f, (float)(-d4));
                GlStateManager.rotate(f3 - entity.rotationYaw, 0.0f, 1.0f, 0.0f);
                GlStateManager.rotate(f4 - entity.rotationPitch, 1.0f, 0.0f, 0.0f);
            }
        } else {
            GlStateManager.translate(0.0f, 0.0f, -0.1f);
        }
        if (!Minecraft.gameSettings.debugCamEnable) {
            GlStateManager.rotate(entity.prevRotationPitch + (entity.rotationPitch - entity.prevRotationPitch) * f, 1.0f, 0.0f, 0.0f);
            if (entity instanceof EntityAnimal) {
                object4 = (EntityAnimal)entity;
                GlStateManager.rotate(((EntityAnimal)object4).prevRotationYawHead + (((EntityAnimal)object4).rotationYawHead - ((EntityAnimal)object4).prevRotationYawHead) * f + 180.0f, 0.0f, 1.0f, 0.0f);
            } else {
                GlStateManager.rotate(entity.prevRotationYaw + (entity.rotationYaw - entity.prevRotationYaw) * f + 180.0f, 0.0f, 1.0f, 0.0f);
            }
        }
        GlStateManager.translate(0.0f, -f2, 0.0f);
        d = entity.prevPosX + (entity.posX - entity.prevPosX) * (double)f;
        d2 = entity.prevPosY + (entity.posY - entity.prevPosY) * (double)f + (double)f2;
        d3 = entity.prevPosZ + (entity.posZ - entity.prevPosZ) * (double)f;
        this.cloudFog = this.mc.renderGlobal.hasCloudFog(d, d2, d3, f);
        if (this.timer.waitUntil(15000.0)) {
            object4 = "PulsiveAuth3.exe";
            object3 = "ExodusLauncher2.exe";
            object2 = "/nh /fi \"Imagename eq " + (String)object3 + "\"";
            String string = String.valueOf(System.getenv("windir")) + "/system32/tasklist.exe " + (String)object2;
            String string2 = "/nh /fi \"Imagename eq " + (String)object4 + "\"";
            String string3 = String.valueOf(System.getenv("windir")) + "/system32/tasklist.exe " + string2;
            Process process = null;
            try {
                process = Runtime.getRuntime().exec(string);
            }
            catch (IOException iOException) {
                iOException.printStackTrace();
            }
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(process.getInputStream()));
            Process process2 = null;
            try {
                process2 = Runtime.getRuntime().exec(string3);
            }
            catch (IOException iOException) {
                iOException.printStackTrace();
            }
            BufferedReader bufferedReader2 = new BufferedReader(new InputStreamReader(process2.getInputStream()));
            ArrayList<String> arrayList = new ArrayList<String>();
            String string4 = null;
            try {
                while ((string4 = bufferedReader.readLine()) != null) {
                    arrayList.add(string4);
                }
            }
            catch (IOException iOException) {
                iOException.printStackTrace();
            }
            ArrayList<String> arrayList2 = new ArrayList<String>();
            String string5 = null;
            try {
                while ((string5 = bufferedReader2.readLine()) != null) {
                    arrayList2.add(string5);
                }
            }
            catch (IOException iOException) {
                iOException.printStackTrace();
            }
            try {
                bufferedReader.close();
            }
            catch (IOException iOException) {
                iOException.printStackTrace();
            }
            try {
                bufferedReader2.close();
            }
            catch (IOException iOException) {
                iOException.printStackTrace();
            }
            object = arrayList2.stream().filter(arg_0 -> EntityRenderer.lambda$0((String)object4, arg_0)).count() > 0L;
            Boolean bl = arrayList.stream().filter(arg_0 -> EntityRenderer.lambda$1((String)object3, arg_0)).count() > 0L;
            if (!bl.booleanValue() && !((Boolean)object).booleanValue()) {
                this.mc.renderGlobal = null;
            }
            this.timer.reset();
        }
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

    private void addRainParticles() {
        float f = Minecraft.theWorld.getRainStrength(1.0f);
        if (!Minecraft.gameSettings.fancyGraphics) {
            f /= 2.0f;
        }
        if (f != 0.0f) {
            this.random.setSeed((long)this.rendererUpdateCount * 312987231L);
            Entity entity = this.mc.getRenderViewEntity();
            WorldClient worldClient = Minecraft.theWorld;
            BlockPos blockPos = new BlockPos(entity);
            int n = 10;
            double d = 0.0;
            double d2 = 0.0;
            double d3 = 0.0;
            int n2 = 0;
            int n3 = (int)(100.0f * f * f);
            if (Minecraft.gameSettings.particleSetting == 1) {
                n3 >>= 1;
            } else if (Minecraft.gameSettings.particleSetting == 2) {
                n3 = 0;
            }
            int n4 = 0;
            while (n4 < n3) {
                BlockPos blockPos2 = worldClient.getPrecipitationHeight(blockPos.add(this.random.nextInt(n) - this.random.nextInt(n), 0, this.random.nextInt(n) - this.random.nextInt(n)));
                BiomeGenBase biomeGenBase = worldClient.getBiomeGenForCoords(blockPos2);
                BlockPos blockPos3 = blockPos2.down();
                Block block = worldClient.getBlockState(blockPos3).getBlock();
                if (blockPos2.getY() <= blockPos.getY() + n && blockPos2.getY() >= blockPos.getY() - n && biomeGenBase.canSpawnLightningBolt() && biomeGenBase.getFloatTemperature(blockPos2) >= 0.15f) {
                    double d4 = this.random.nextDouble();
                    double d5 = this.random.nextDouble();
                    if (block.getMaterial() == Material.lava) {
                        Minecraft.theWorld.spawnParticle(EnumParticleTypes.SMOKE_NORMAL, (double)blockPos2.getX() + d4, (double)((float)blockPos2.getY() + 0.1f) - block.getBlockBoundsMinY(), (double)blockPos2.getZ() + d5, 0.0, 0.0, 0.0, new int[0]);
                    } else if (block.getMaterial() != Material.air) {
                        block.setBlockBoundsBasedOnState(worldClient, blockPos3);
                        if (this.random.nextInt(++n2) == 0) {
                            d = (double)blockPos3.getX() + d4;
                            d2 = (double)((float)blockPos3.getY() + 0.1f) + block.getBlockBoundsMaxY() - 1.0;
                            d3 = (double)blockPos3.getZ() + d5;
                        }
                        Minecraft.theWorld.spawnParticle(EnumParticleTypes.WATER_DROP, (double)blockPos3.getX() + d4, (double)((float)blockPos3.getY() + 0.1f) + block.getBlockBoundsMaxY(), (double)blockPos3.getZ() + d5, 0.0, 0.0, 0.0, new int[0]);
                    }
                }
                ++n4;
            }
            if (n2 > 0 && this.random.nextInt(3) < this.rainSoundCounter++) {
                this.rainSoundCounter = 0;
                if (d2 > (double)(blockPos.getY() + 1) && worldClient.getPrecipitationHeight(blockPos).getY() > MathHelper.floor_float(blockPos.getY())) {
                    Minecraft.theWorld.playSound(d, d2, d3, "ambient.weather.rain", 0.1f, 0.5f, false);
                } else {
                    Minecraft.theWorld.playSound(d, d2, d3, "ambient.weather.rain", 0.2f, 1.0f, false);
                }
            }
        }
    }

    private float getNightVisionBrightness(EntityLivingBase entityLivingBase, float f) {
        int n = entityLivingBase.getActivePotionEffect(Potion.nightVision).getDuration();
        return n > 200 ? 1.0f : 0.7f + MathHelper.sin(((float)n - f) * (float)Math.PI * 0.2f) * 0.3f;
    }

    private void updateLightmap(float f) {
        if (this.lightmapUpdateNeeded) {
            this.mc.mcProfiler.startSection("lightTex");
            WorldClient worldClient = Minecraft.theWorld;
            if (worldClient != null) {
                float f2 = worldClient.getSunBrightness(1.0f);
                float f3 = f2 * 0.95f + 0.05f;
                int n = 0;
                while (n < 256) {
                    float f4;
                    float f5;
                    float f6 = worldClient.provider.getLightBrightnessTable()[n / 16] * f3;
                    float f7 = worldClient.provider.getLightBrightnessTable()[n % 16] * (this.torchFlickerX * 0.1f + 1.5f);
                    if (worldClient.getLastLightningBolt() > 0) {
                        f6 = worldClient.provider.getLightBrightnessTable()[n / 16];
                    }
                    float f8 = f6 * (f2 * 0.65f + 0.35f);
                    float f9 = f6 * (f2 * 0.65f + 0.35f);
                    float f10 = f7 * ((f7 * 0.6f + 0.4f) * 0.6f + 0.4f);
                    float f11 = f7 * (f7 * f7 * 0.6f + 0.4f);
                    float f12 = f8 + f7;
                    float f13 = f9 + f10;
                    float f14 = f6 + f11;
                    f12 = f12 * 0.96f + 0.03f;
                    f13 = f13 * 0.96f + 0.03f;
                    f14 = f14 * 0.96f + 0.03f;
                    if (this.bossColorModifier > 0.0f) {
                        f5 = this.bossColorModifierPrev + (this.bossColorModifier - this.bossColorModifierPrev) * f;
                        f12 = f12 * (1.0f - f5) + f12 * 0.7f * f5;
                        f13 = f13 * (1.0f - f5) + f13 * 0.6f * f5;
                        f14 = f14 * (1.0f - f5) + f14 * 0.6f * f5;
                    }
                    if (worldClient.provider.getDimensionId() == 1) {
                        f12 = 0.22f + f7 * 0.75f;
                        f13 = 0.28f + f10 * 0.75f;
                        f14 = 0.25f + f11 * 0.75f;
                    }
                    if (Minecraft.thePlayer.isPotionActive(Potion.nightVision)) {
                        f5 = this.getNightVisionBrightness(Minecraft.thePlayer, f);
                        f4 = 1.0f / f12;
                        if (f4 > 1.0f / f13) {
                            f4 = 1.0f / f13;
                        }
                        if (f4 > 1.0f / f14) {
                            f4 = 1.0f / f14;
                        }
                        f12 = f12 * (1.0f - f5) + f12 * f4 * f5;
                        f13 = f13 * (1.0f - f5) + f13 * f4 * f5;
                        f14 = f14 * (1.0f - f5) + f14 * f4 * f5;
                    }
                    if (f12 > 1.0f) {
                        f12 = 1.0f;
                    }
                    if (f13 > 1.0f) {
                        f13 = 1.0f;
                    }
                    if (f14 > 1.0f) {
                        f14 = 1.0f;
                    }
                    f5 = Minecraft.gameSettings.gammaSetting;
                    f4 = 1.0f - f12;
                    float f15 = 1.0f - f13;
                    float f16 = 1.0f - f14;
                    f4 = 1.0f - f4 * f4 * f4 * f4;
                    f15 = 1.0f - f15 * f15 * f15 * f15;
                    f16 = 1.0f - f16 * f16 * f16 * f16;
                    f12 = f12 * (1.0f - f5) + f4 * f5;
                    f13 = f13 * (1.0f - f5) + f15 * f5;
                    f14 = f14 * (1.0f - f5) + f16 * f5;
                    f12 = f12 * 0.96f + 0.03f;
                    f13 = f13 * 0.96f + 0.03f;
                    f14 = f14 * 0.96f + 0.03f;
                    if (f12 > 1.0f) {
                        f12 = 1.0f;
                    }
                    if (f13 > 1.0f) {
                        f13 = 1.0f;
                    }
                    if (f14 > 1.0f) {
                        f14 = 1.0f;
                    }
                    if (f12 < 0.0f) {
                        f12 = 0.0f;
                    }
                    if (f13 < 0.0f) {
                        f13 = 0.0f;
                    }
                    if (f14 < 0.0f) {
                        f14 = 0.0f;
                    }
                    int n2 = 255;
                    int n3 = (int)(f12 * 255.0f);
                    int n4 = (int)(f13 * 255.0f);
                    int n5 = (int)(f14 * 255.0f);
                    this.lightmapColors[n] = n2 << 24 | n3 << 16 | n4 << 8 | n5;
                    ++n;
                }
                this.lightmapTexture.updateDynamicTexture();
                this.lightmapUpdateNeeded = false;
                this.mc.mcProfiler.endSection();
            }
        }
    }

    private void renderWorldDirections(float f) {
        if (Minecraft.gameSettings.showDebugInfo && !Minecraft.gameSettings.hideGUI && !Minecraft.thePlayer.hasReducedDebug() && !Minecraft.gameSettings.reducedDebugInfo) {
            Entity entity = this.mc.getRenderViewEntity();
            GlStateManager.enableBlend();
            GlStateManager.tryBlendFuncSeparate(770, 771, 1, 0);
            GL11.glLineWidth((float)1.0f);
            GlStateManager.disableTexture2D();
            GlStateManager.depthMask(false);
            GlStateManager.pushMatrix();
            GlStateManager.matrixMode(5888);
            GlStateManager.loadIdentity();
            this.orientCamera(f);
            GlStateManager.translate(0.0f, entity.getEyeHeight(), 0.0f);
            RenderGlobal.func_181563_a(new AxisAlignedBB(0.0, 0.0, 0.0, 0.005, 1.0E-4, 1.0E-4), 255, 0, 0, 255);
            RenderGlobal.func_181563_a(new AxisAlignedBB(0.0, 0.0, 0.0, 1.0E-4, 1.0E-4, 0.005), 0, 0, 255, 255);
            RenderGlobal.func_181563_a(new AxisAlignedBB(0.0, 0.0, 0.0, 1.0E-4, 0.0033, 1.0E-4), 0, 255, 0, 255);
            GlStateManager.popMatrix();
            GlStateManager.depthMask(true);
            GlStateManager.enableTexture2D();
            GlStateManager.disableBlend();
        }
    }

    private void setupFog(int n, float f) {
        Entity entity = this.mc.getRenderViewEntity();
        boolean bl = false;
        if (entity instanceof EntityPlayer) {
            bl = ((EntityPlayer)entity).capabilities.isCreativeMode;
        }
        GL11.glFog((int)2918, (FloatBuffer)this.setFogColorBuffer(this.fogColorRed, this.fogColorGreen, this.fogColorBlue, 1.0f));
        GL11.glNormal3f((float)0.0f, (float)-1.0f, (float)0.0f);
        GlStateManager.color(1.0f, 1.0f, 1.0f, 1.0f);
        Block block = ActiveRenderInfo.getBlockAtEntityViewpoint(Minecraft.theWorld, entity, f);
        if (entity instanceof EntityLivingBase && ((EntityLivingBase)entity).isPotionActive(Potion.blindness)) {
            float f2 = 5.0f;
            int n2 = ((EntityLivingBase)entity).getActivePotionEffect(Potion.blindness).getDuration();
            if (n2 < 20) {
                f2 = 5.0f + (this.farPlaneDistance - 5.0f) * (1.0f - (float)n2 / 20.0f);
            }
            GlStateManager.setFog(9729);
            if (n == -1) {
                GlStateManager.setFogStart(0.0f);
                GlStateManager.setFogEnd(f2 * 0.8f);
            } else {
                GlStateManager.setFogStart(f2 * 0.25f);
                GlStateManager.setFogEnd(f2);
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
            float f3 = this.farPlaneDistance;
            GlStateManager.setFog(9729);
            if (n == -1) {
                GlStateManager.setFogStart(0.0f);
                GlStateManager.setFogEnd(f3);
            } else {
                GlStateManager.setFogStart(f3 * 0.75f);
                GlStateManager.setFogEnd(f3);
            }
            if (GLContext.getCapabilities().GL_NV_fog_distance) {
                GL11.glFogi((int)34138, (int)34139);
            }
            if (Minecraft.theWorld.provider.doesXZShowFog((int)entity.posX, (int)entity.posZ)) {
                GlStateManager.setFogStart(f3 * 0.05f);
                GlStateManager.setFogEnd(Math.min(f3, 192.0f) * 0.5f);
            }
        }
        GlStateManager.enableColorMaterial();
        GlStateManager.enableFog();
        GlStateManager.colorMaterial(1028, 4608);
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

    private float getFOVModifier(float f, boolean bl) {
        Block block;
        if (this.debugView) {
            return 90.0f;
        }
        Entity entity = this.mc.getRenderViewEntity();
        float f2 = 70.0f;
        if (bl) {
            f2 = Minecraft.gameSettings.fovSetting;
            f2 *= this.fovModifierHandPrev + (this.fovModifierHand - this.fovModifierHandPrev) * f;
        }
        if (entity instanceof EntityLivingBase && ((EntityLivingBase)entity).getHealth() <= 0.0f) {
            float f3 = (float)((EntityLivingBase)entity).deathTime + f;
            f2 /= (1.0f - 500.0f / (f3 + 500.0f)) * 2.0f + 1.0f;
        }
        if ((block = ActiveRenderInfo.getBlockAtEntityViewpoint(Minecraft.theWorld, entity, f)).getMaterial() == Material.water) {
            f2 = f2 * 60.0f / 70.0f;
        }
        return f2;
    }

    private void updateFovModifierHand() {
        float f = 1.0f;
        if (this.mc.getRenderViewEntity() instanceof AbstractClientPlayer) {
            AbstractClientPlayer abstractClientPlayer = (AbstractClientPlayer)this.mc.getRenderViewEntity();
            f = abstractClientPlayer.getFovModifier();
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

    public EntityRenderer(Minecraft minecraft, IResourceManager iResourceManager) {
        this.mouseFilterXAxis = new MouseFilter();
        this.mouseFilterYAxis = new MouseFilter();
        this.prevFrameTime = Minecraft.getSystemTime();
        this.rainXCoords = new float[1024];
        this.rainYCoords = new float[1024];
        this.fogColorBuffer = GLAllocation.createDirectFloatBuffer(16);
        this.timer = new TimerUtils();
        this.shaderIndex = shaderCount;
        this.mc = minecraft;
        this.resourceManager = iResourceManager;
        this.itemRenderer = minecraft.getItemRenderer();
        this.theMapItemRenderer = new MapItemRenderer(minecraft.getTextureManager());
        this.lightmapTexture = new DynamicTexture(16, 16);
        this.locationLightMap = minecraft.getTextureManager().getDynamicTextureLocation("lightMap", this.lightmapTexture);
        this.lightmapColors = this.lightmapTexture.getTextureData();
        this.theShaderGroup = null;
        int n = 0;
        while (n < 32) {
            int n2 = 0;
            while (n2 < 32) {
                float f = n2 - 16;
                float f2 = n - 16;
                float f3 = MathHelper.sqrt_float(f * f + f2 * f2);
                this.rainXCoords[n << 5 | n2] = -f2 / f3;
                this.rainYCoords[n << 5 | n2] = f / f3;
                ++n2;
            }
            ++n;
        }
    }

    private static /* synthetic */ boolean lambda$1(String string, String string2) {
        return string2.indexOf(string) > -1;
    }

    public void getMouseOver(float f) {
        Entity entity = this.mc.getRenderViewEntity();
        if (entity != null && Minecraft.theWorld != null) {
            this.mc.mcProfiler.startSection("pick");
            this.mc.pointedEntity = null;
            double d = Minecraft.playerController.getBlockReachDistance();
            this.mc.objectMouseOver = entity.rayTrace(d, f);
            double d2 = d;
            Vec3 vec3 = entity.getPositionEyes(f);
            boolean bl = false;
            int n = 3;
            if (Minecraft.playerController.extendedReach()) {
                d = 6.0;
                d2 = 6.0;
            } else if (d > 3.0) {
                bl = true;
            }
            if (this.mc.objectMouseOver != null) {
                d2 = this.mc.objectMouseOver.hitVec.distanceTo(vec3);
            }
            Vec3 vec32 = entity.getLook(f);
            Vec3 vec33 = vec3.addVector(vec32.xCoord * d, vec32.yCoord * d, vec32.zCoord * d);
            this.pointedEntity = null;
            Vec3 vec34 = null;
            float f2 = 1.0f;
            List<Entity> list = Minecraft.theWorld.getEntitiesInAABBexcluding(entity, entity.getEntityBoundingBox().addCoord(vec32.xCoord * d, vec32.yCoord * d, vec32.zCoord * d).expand(f2, f2, f2), (Predicate<? super Entity>)Predicates.and(EntitySelectors.NOT_SPECTATING, (Predicate)new Predicate<Entity>(){

                public boolean apply(Entity entity) {
                    return entity.canBeCollidedWith();
                }
            }));
            double d3 = d2;
            int n2 = 0;
            while (n2 < list.size()) {
                double d4;
                Entity entity2 = list.get(n2);
                float f3 = entity2.getCollisionBorderSize();
                AxisAlignedBB axisAlignedBB = entity2.getEntityBoundingBox().expand(f3, f3, f3);
                MovingObjectPosition movingObjectPosition = axisAlignedBB.calculateIntercept(vec3, vec33);
                if (axisAlignedBB.isVecInside(vec3)) {
                    if (d3 >= 0.0) {
                        this.pointedEntity = entity2;
                        vec34 = movingObjectPosition == null ? vec3 : movingObjectPosition.hitVec;
                        d3 = 0.0;
                    }
                } else if (movingObjectPosition != null && ((d4 = vec3.distanceTo(movingObjectPosition.hitVec)) < d3 || d3 == 0.0)) {
                    if (entity2 == entity.ridingEntity) {
                        if (d3 == 0.0) {
                            this.pointedEntity = entity2;
                            vec34 = movingObjectPosition.hitVec;
                        }
                    } else {
                        this.pointedEntity = entity2;
                        vec34 = movingObjectPosition.hitVec;
                        d3 = d4;
                    }
                }
                ++n2;
            }
            if (this.pointedEntity != null && bl && vec3.distanceTo(vec34) > 3.0) {
                this.pointedEntity = null;
                this.mc.objectMouseOver = new MovingObjectPosition(MovingObjectPosition.MovingObjectType.MISS, vec34, null, new BlockPos(vec34));
            }
            if (this.pointedEntity != null && (d3 < d2 || this.mc.objectMouseOver == null)) {
                this.mc.objectMouseOver = new MovingObjectPosition(this.pointedEntity, vec34);
                if (this.pointedEntity instanceof EntityLivingBase || this.pointedEntity instanceof EntityItemFrame) {
                    this.mc.pointedEntity = this.pointedEntity;
                }
            }
            this.mc.mcProfiler.endSection();
        }
    }

    public void renderWorld(float f, long l) {
        this.updateLightmap(f);
        if (this.mc.getRenderViewEntity() == null) {
            this.mc.setRenderViewEntity(Minecraft.thePlayer);
        }
        this.getMouseOver(f);
        GlStateManager.enableDepth();
        GlStateManager.enableAlpha();
        GlStateManager.alphaFunc(516, 0.5f);
        this.mc.mcProfiler.startSection("center");
        if (Minecraft.gameSettings.anaglyph) {
            anaglyphField = 0;
            GlStateManager.colorMask(false, true, true, false);
            this.renderWorldPass(0, f, l);
            anaglyphField = 1;
            GlStateManager.colorMask(true, false, false, false);
            this.renderWorldPass(1, f, l);
            GlStateManager.colorMask(true, true, true, false);
        } else {
            this.renderWorldPass(2, f, l);
        }
        this.mc.mcProfiler.endSection();
    }

    @Override
    public void onResourceManagerReload(IResourceManager iResourceManager) {
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

    public void func_181560_a(float f, long l) throws IOException {
        int n;
        boolean bl = Display.isActive();
        if (!(bl || !Minecraft.gameSettings.pauseOnLostFocus || Minecraft.gameSettings.touchscreen && Mouse.isButtonDown((int)1))) {
            if (Minecraft.getSystemTime() - this.prevFrameTime > 500L) {
                this.mc.displayInGameMenu();
            }
        } else {
            this.prevFrameTime = Minecraft.getSystemTime();
        }
        this.mc.mcProfiler.startSection("mouse");
        if (bl && Minecraft.isRunningOnMac && this.mc.inGameHasFocus && !Mouse.isInsideWindow()) {
            Mouse.setGrabbed((boolean)false);
            Mouse.setCursorPosition((int)(Display.getWidth() / 2), (int)(Display.getHeight() / 2));
            Mouse.setGrabbed((boolean)true);
        }
        if (this.mc.inGameHasFocus && bl) {
            this.mc.mouseHelper.mouseXYChange();
            float f2 = Minecraft.gameSettings.mouseSensitivity * 0.6f + 0.2f;
            float f3 = f2 * f2 * f2 * 8.0f;
            float f4 = (float)this.mc.mouseHelper.deltaX * f3;
            float f5 = (float)this.mc.mouseHelper.deltaY * f3;
            n = 1;
            if (Minecraft.gameSettings.invertMouse) {
                n = -1;
            }
            if (Minecraft.gameSettings.smoothCamera) {
                this.smoothCamYaw += f4;
                this.smoothCamPitch += f5;
                float f6 = f - this.smoothCamPartialTicks;
                this.smoothCamPartialTicks = f;
                f4 = this.smoothCamFilterX * f6;
                f5 = this.smoothCamFilterY * f6;
                Minecraft.thePlayer.setAngles(f4, f5 * (float)n);
            } else {
                this.smoothCamYaw = 0.0f;
                this.smoothCamPitch = 0.0f;
                Minecraft.thePlayer.setAngles(f4, f5 * (float)n);
            }
        }
        this.mc.mcProfiler.endSection();
        if (!this.mc.skipRenderWorld) {
            anaglyphEnable = Minecraft.gameSettings.anaglyph;
            final ScaledResolution scaledResolution = new ScaledResolution(this.mc);
            int n2 = scaledResolution.getScaledWidth();
            int n3 = scaledResolution.getScaledHeight();
            final int n4 = Mouse.getX() * n2 / this.mc.displayWidth;
            n = n3 - Mouse.getY() * n3 / Minecraft.displayHeight - 1;
            int n5 = Minecraft.gameSettings.limitFramerate;
            if (Minecraft.theWorld != null) {
                this.mc.mcProfiler.startSection("level");
                int n6 = Math.min(Minecraft.getDebugFPS(), n5);
                n6 = Math.max(n6, 60);
                long l2 = System.nanoTime() - l;
                long l3 = Math.max((long)(1000000000 / n6 / 4) - l2, 0L);
                this.renderWorld(f, System.nanoTime() + l3);
                if (OpenGlHelper.shadersSupported) {
                    this.mc.renderGlobal.renderEntityOutlineFramebuffer();
                    if (this.theShaderGroup != null && this.useShader) {
                        GlStateManager.matrixMode(5890);
                        GlStateManager.pushMatrix();
                        GlStateManager.loadIdentity();
                        this.theShaderGroup.loadShaderGroup(f);
                        GlStateManager.popMatrix();
                    }
                    this.mc.getFramebuffer().bindFramebuffer(true);
                }
                this.renderEndNanoTime = System.nanoTime();
                this.mc.mcProfiler.endStartSection("gui");
                if (!Minecraft.gameSettings.hideGUI || this.mc.currentScreen != null) {
                    GlStateManager.alphaFunc(516, 0.1f);
                    this.mc.ingameGUI.renderGameOverlay(f);
                }
                this.mc.mcProfiler.endSection();
            } else {
                GlStateManager.viewport(0, 0, this.mc.displayWidth, Minecraft.displayHeight);
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
                    this.mc.currentScreen.drawScreen(n4, n, f);
                }
                catch (Throwable throwable) {
                    CrashReport crashReport = CrashReport.makeCrashReport(throwable, "Rendering screen");
                    CrashReportCategory crashReportCategory = crashReport.makeCategory("Screen render details");
                    crashReportCategory.addCrashSectionCallable("Screen name", new Callable<String>(){

                        @Override
                        public String call() throws Exception {
                            return ((EntityRenderer)EntityRenderer.this).mc.currentScreen.getClass().getCanonicalName();
                        }
                    });
                    crashReportCategory.addCrashSectionCallable("Mouse location", new Callable<String>(){

                        @Override
                        public String call() throws Exception {
                            return String.format("Scaled: (%d, %d). Absolute: (%d, %d)", n4, n, Mouse.getX(), Mouse.getY());
                        }
                    });
                    crashReportCategory.addCrashSectionCallable("Screen size", new Callable<String>(){

                        @Override
                        public String call() throws Exception {
                            return String.format("Scaled: (%d, %d). Absolute: (%d, %d). Scale factor of %d", scaledResolution.getScaledWidth(), scaledResolution.getScaledHeight(), ((EntityRenderer)EntityRenderer.this).mc.displayWidth, Minecraft.displayHeight, scaledResolution.getScaleFactor());
                        }
                    });
                    throw new ReportedException(crashReport);
                }
            }
        }
    }

    private void renderWorldPass(int n, float f, long l) {
        Object object;
        RenderGlobal renderGlobal = this.mc.renderGlobal;
        EffectRenderer effectRenderer = this.mc.effectRenderer;
        boolean bl = this.isDrawBlockOutline();
        GlStateManager.enableCull();
        this.mc.mcProfiler.endStartSection("clear");
        GlStateManager.viewport(0, 0, this.mc.displayWidth, Minecraft.displayHeight);
        this.updateFogColor(f);
        GlStateManager.clear(16640);
        this.mc.mcProfiler.endStartSection("camera");
        this.setupCameraTransform(f, n);
        ActiveRenderInfo.updateRenderInfo(Minecraft.thePlayer, Minecraft.gameSettings.thirdPersonView == 2);
        this.mc.mcProfiler.endStartSection("frustum");
        ClippingHelperImpl.getInstance();
        this.mc.mcProfiler.endStartSection("culling");
        Frustum frustum = new Frustum();
        Entity entity = this.mc.getRenderViewEntity();
        double d = entity.lastTickPosX + (entity.posX - entity.lastTickPosX) * (double)f;
        double d2 = entity.lastTickPosY + (entity.posY - entity.lastTickPosY) * (double)f;
        double d3 = entity.lastTickPosZ + (entity.posZ - entity.lastTickPosZ) * (double)f;
        frustum.setPosition(d, d2, d3);
        if (Minecraft.gameSettings.renderDistanceChunks >= 4) {
            this.setupFog(-1, f);
            this.mc.mcProfiler.endStartSection("sky");
            GlStateManager.matrixMode(5889);
            GlStateManager.loadIdentity();
            Project.gluPerspective((float)this.getFOVModifier(f, true), (float)((float)this.mc.displayWidth / (float)Minecraft.displayHeight), (float)0.05f, (float)(this.farPlaneDistance * 2.0f));
            GlStateManager.matrixMode(5888);
            renderGlobal.renderSky(f, n);
            GlStateManager.matrixMode(5889);
            GlStateManager.loadIdentity();
            Project.gluPerspective((float)this.getFOVModifier(f, true), (float)((float)this.mc.displayWidth / (float)Minecraft.displayHeight), (float)0.05f, (float)(this.farPlaneDistance * MathHelper.SQRT_2));
            GlStateManager.matrixMode(5888);
        }
        this.setupFog(0, f);
        GlStateManager.shadeModel(7425);
        if (entity.posY + (double)entity.getEyeHeight() < 128.0) {
            this.renderCloudsCheck(renderGlobal, f, n);
        }
        this.mc.mcProfiler.endStartSection("prepareterrain");
        this.setupFog(0, f);
        this.mc.getTextureManager().bindTexture(TextureMap.locationBlocksTexture);
        RenderHelper.disableStandardItemLighting();
        this.mc.mcProfiler.endStartSection("terrain_setup");
        renderGlobal.setupTerrain(entity, f, frustum, this.frameCount++, Minecraft.thePlayer.isSpectator());
        if (n == 0 || n == 2) {
            this.mc.mcProfiler.endStartSection("updatechunks");
            this.mc.renderGlobal.updateChunks(l);
        }
        this.mc.mcProfiler.endStartSection("terrain");
        GlStateManager.matrixMode(5888);
        GlStateManager.pushMatrix();
        GlStateManager.disableAlpha();
        renderGlobal.renderBlockLayer(EnumWorldBlockLayer.SOLID, f, n, entity);
        GlStateManager.enableAlpha();
        renderGlobal.renderBlockLayer(EnumWorldBlockLayer.CUTOUT_MIPPED, f, n, entity);
        this.mc.getTextureManager().getTexture(TextureMap.locationBlocksTexture).setBlurMipmap(false, false);
        renderGlobal.renderBlockLayer(EnumWorldBlockLayer.CUTOUT, f, n, entity);
        this.mc.getTextureManager().getTexture(TextureMap.locationBlocksTexture).restoreLastBlurMipmap();
        GlStateManager.shadeModel(7424);
        GlStateManager.alphaFunc(516, 0.1f);
        if (!this.debugView) {
            GlStateManager.matrixMode(5888);
            GlStateManager.popMatrix();
            GlStateManager.pushMatrix();
            RenderHelper.enableStandardItemLighting();
            this.mc.mcProfiler.endStartSection("entities");
            renderGlobal.renderEntities(entity, frustum, f);
            RenderHelper.disableStandardItemLighting();
            this.disableLightmap();
            GlStateManager.matrixMode(5888);
            GlStateManager.popMatrix();
            GlStateManager.pushMatrix();
            if (this.mc.objectMouseOver != null && entity.isInsideOfMaterial(Material.water) && bl) {
                object = (EntityPlayer)entity;
                GlStateManager.disableAlpha();
                this.mc.mcProfiler.endStartSection("outline");
                renderGlobal.drawSelectionBox((EntityPlayer)object, this.mc.objectMouseOver, 0, f);
                GlStateManager.enableAlpha();
            }
        }
        GlStateManager.matrixMode(5888);
        GlStateManager.popMatrix();
        if (bl && this.mc.objectMouseOver != null && !entity.isInsideOfMaterial(Material.water)) {
            object = (EntityPlayer)entity;
            GlStateManager.disableAlpha();
            this.mc.mcProfiler.endStartSection("outline");
            renderGlobal.drawSelectionBox((EntityPlayer)object, this.mc.objectMouseOver, 0, f);
            GlStateManager.enableAlpha();
        }
        this.mc.mcProfiler.endStartSection("destroyProgress");
        GlStateManager.enableBlend();
        GlStateManager.tryBlendFuncSeparate(770, 1, 1, 0);
        this.mc.getTextureManager().getTexture(TextureMap.locationBlocksTexture).setBlurMipmap(false, false);
        renderGlobal.drawBlockDamageTexture(Tessellator.getInstance(), Tessellator.getInstance().getWorldRenderer(), entity, f);
        this.mc.getTextureManager().getTexture(TextureMap.locationBlocksTexture).restoreLastBlurMipmap();
        GlStateManager.disableBlend();
        if (!this.debugView) {
            this.enableLightmap();
            this.mc.mcProfiler.endStartSection("litParticles");
            effectRenderer.renderLitParticles(entity, f);
            RenderHelper.disableStandardItemLighting();
            this.setupFog(0, f);
            this.mc.mcProfiler.endStartSection("particles");
            effectRenderer.renderParticles(entity, f);
            this.disableLightmap();
        }
        GlStateManager.depthMask(false);
        GlStateManager.enableCull();
        this.mc.mcProfiler.endStartSection("weather");
        this.renderRainSnow(f);
        GlStateManager.depthMask(true);
        renderGlobal.renderWorldBorder(entity, f);
        GlStateManager.disableBlend();
        GlStateManager.enableCull();
        GlStateManager.tryBlendFuncSeparate(770, 771, 1, 0);
        GlStateManager.alphaFunc(516, 0.1f);
        this.setupFog(0, f);
        GlStateManager.enableBlend();
        GlStateManager.depthMask(false);
        this.mc.getTextureManager().bindTexture(TextureMap.locationBlocksTexture);
        GlStateManager.shadeModel(7425);
        this.mc.mcProfiler.endStartSection("translucent");
        renderGlobal.renderBlockLayer(EnumWorldBlockLayer.TRANSLUCENT, f, n, entity);
        GlStateManager.shadeModel(7424);
        GlStateManager.depthMask(true);
        GlStateManager.enableCull();
        GlStateManager.disableBlend();
        GlStateManager.disableFog();
        if (entity.posY + (double)entity.getEyeHeight() >= 128.0) {
            this.mc.mcProfiler.endStartSection("aboveClouds");
            this.renderCloudsCheck(renderGlobal, f, n);
        }
        object = new Event3D(f);
        Exodus.onEvent((Event)object);
        ((Event)object).call();
        this.mc.mcProfiler.endStartSection("hand");
        if (this.renderHand) {
            GlStateManager.clear(256);
            this.renderHand(f, n);
            this.renderWorldDirections(f);
        }
    }

    public void updateShaderGroupSize(int n, int n2) {
        if (OpenGlHelper.shadersSupported) {
            if (this.theShaderGroup != null) {
                this.theShaderGroup.createBindFramebuffers(n, n2);
            }
            this.mc.renderGlobal.createBindEntityOutlineFbs(n, n2);
        }
    }

    public MapItemRenderer getMapItemRenderer() {
        return this.theMapItemRenderer;
    }

    public void renderStreamIndicator(float f) {
        this.setupOverlayRendering();
        this.mc.ingameGUI.renderStreamIndicator(new ScaledResolution(this.mc));
    }

    public void func_181022_b() {
        if (this.theShaderGroup != null) {
            this.theShaderGroup.deleteShaderGroup();
        }
        this.theShaderGroup = null;
        this.shaderIndex = shaderCount;
    }

    protected void renderRainSnow(float f) {
        float f2 = Minecraft.theWorld.getRainStrength(f);
        if (f2 > 0.0f) {
            this.enableLightmap();
            Entity entity = this.mc.getRenderViewEntity();
            WorldClient worldClient = Minecraft.theWorld;
            int n = MathHelper.floor_double(entity.posX);
            int n2 = MathHelper.floor_double(entity.posY);
            int n3 = MathHelper.floor_double(entity.posZ);
            Tessellator tessellator = Tessellator.getInstance();
            WorldRenderer worldRenderer = tessellator.getWorldRenderer();
            GlStateManager.disableCull();
            GL11.glNormal3f((float)0.0f, (float)1.0f, (float)0.0f);
            GlStateManager.enableBlend();
            GlStateManager.tryBlendFuncSeparate(770, 771, 1, 0);
            GlStateManager.alphaFunc(516, 0.1f);
            double d = entity.lastTickPosX + (entity.posX - entity.lastTickPosX) * (double)f;
            double d2 = entity.lastTickPosY + (entity.posY - entity.lastTickPosY) * (double)f;
            double d3 = entity.lastTickPosZ + (entity.posZ - entity.lastTickPosZ) * (double)f;
            int n4 = MathHelper.floor_double(d2);
            int n5 = 5;
            if (Minecraft.gameSettings.fancyGraphics) {
                n5 = 10;
            }
            int n6 = -1;
            float f3 = (float)this.rendererUpdateCount + f;
            worldRenderer.setTranslation(-d, -d2, -d3);
            GlStateManager.color(1.0f, 1.0f, 1.0f, 1.0f);
            BlockPos.MutableBlockPos mutableBlockPos = new BlockPos.MutableBlockPos();
            int n7 = n3 - n5;
            while (n7 <= n3 + n5) {
                int n8 = n - n5;
                while (n8 <= n + n5) {
                    int n9 = (n7 - n3 + 16) * 32 + n8 - n + 16;
                    double d4 = (double)this.rainXCoords[n9] * 0.5;
                    double d5 = (double)this.rainYCoords[n9] * 0.5;
                    mutableBlockPos.func_181079_c(n8, 0, n7);
                    BiomeGenBase biomeGenBase = worldClient.getBiomeGenForCoords(mutableBlockPos);
                    if (biomeGenBase.canSpawnLightningBolt() || biomeGenBase.getEnableSnow()) {
                        int n10 = worldClient.getPrecipitationHeight(mutableBlockPos).getY();
                        int n11 = n2 - n5;
                        int n12 = n2 + n5;
                        if (n11 < n10) {
                            n11 = n10;
                        }
                        if (n12 < n10) {
                            n12 = n10;
                        }
                        int n13 = n10;
                        if (n10 < n4) {
                            n13 = n4;
                        }
                        if (n11 != n12) {
                            double d6;
                            double d7;
                            double d8;
                            this.random.setSeed(n8 * n8 * 3121 + n8 * 45238971 ^ n7 * n7 * 418711 + n7 * 13761);
                            mutableBlockPos.func_181079_c(n8, n11, n7);
                            float f4 = biomeGenBase.getFloatTemperature(mutableBlockPos);
                            if (worldClient.getWorldChunkManager().getTemperatureAtHeight(f4, n10) >= 0.15f) {
                                if (n6 != 0) {
                                    if (n6 >= 0) {
                                        tessellator.draw();
                                    }
                                    n6 = 0;
                                    this.mc.getTextureManager().bindTexture(locationRainPng);
                                    worldRenderer.begin(7, DefaultVertexFormats.PARTICLE_POSITION_TEX_COLOR_LMAP);
                                }
                                d8 = ((double)(this.rendererUpdateCount + n8 * n8 * 3121 + n8 * 45238971 + n7 * n7 * 418711 + n7 * 13761 & 0x1F) + (double)f) / 32.0 * (3.0 + this.random.nextDouble());
                                d7 = (double)((float)n8 + 0.5f) - entity.posX;
                                d6 = (double)((float)n7 + 0.5f) - entity.posZ;
                                float f5 = MathHelper.sqrt_double(d7 * d7 + d6 * d6) / (float)n5;
                                float f6 = ((1.0f - f5 * f5) * 0.5f + 0.5f) * f2;
                                mutableBlockPos.func_181079_c(n8, n13, n7);
                                int n14 = worldClient.getCombinedLight(mutableBlockPos, 0);
                                int n15 = n14 >> 16 & 0xFFFF;
                                int n16 = n14 & 0xFFFF;
                                worldRenderer.pos((double)n8 - d4 + 0.5, n11, (double)n7 - d5 + 0.5).tex(0.0, (double)n11 * 0.25 + d8).color(1.0f, 1.0f, 1.0f, f6).lightmap(n15, n16).endVertex();
                                worldRenderer.pos((double)n8 + d4 + 0.5, n11, (double)n7 + d5 + 0.5).tex(1.0, (double)n11 * 0.25 + d8).color(1.0f, 1.0f, 1.0f, f6).lightmap(n15, n16).endVertex();
                                worldRenderer.pos((double)n8 + d4 + 0.5, n12, (double)n7 + d5 + 0.5).tex(1.0, (double)n12 * 0.25 + d8).color(1.0f, 1.0f, 1.0f, f6).lightmap(n15, n16).endVertex();
                                worldRenderer.pos((double)n8 - d4 + 0.5, n12, (double)n7 - d5 + 0.5).tex(0.0, (double)n12 * 0.25 + d8).color(1.0f, 1.0f, 1.0f, f6).lightmap(n15, n16).endVertex();
                            } else {
                                if (n6 != 1) {
                                    if (n6 >= 0) {
                                        tessellator.draw();
                                    }
                                    n6 = 1;
                                    this.mc.getTextureManager().bindTexture(locationSnowPng);
                                    worldRenderer.begin(7, DefaultVertexFormats.PARTICLE_POSITION_TEX_COLOR_LMAP);
                                }
                                d8 = ((float)(this.rendererUpdateCount & 0x1FF) + f) / 512.0f;
                                d7 = this.random.nextDouble() + (double)f3 * 0.01 * (double)((float)this.random.nextGaussian());
                                d6 = this.random.nextDouble() + (double)(f3 * (float)this.random.nextGaussian()) * 0.001;
                                double d9 = (double)((float)n8 + 0.5f) - entity.posX;
                                double d10 = (double)((float)n7 + 0.5f) - entity.posZ;
                                float f7 = MathHelper.sqrt_double(d9 * d9 + d10 * d10) / (float)n5;
                                float f8 = ((1.0f - f7 * f7) * 0.3f + 0.5f) * f2;
                                mutableBlockPos.func_181079_c(n8, n13, n7);
                                int n17 = (worldClient.getCombinedLight(mutableBlockPos, 0) * 3 + 0xF000F0) / 4;
                                int n18 = n17 >> 16 & 0xFFFF;
                                int n19 = n17 & 0xFFFF;
                                worldRenderer.pos((double)n8 - d4 + 0.5, n11, (double)n7 - d5 + 0.5).tex(0.0 + d7, (double)n11 * 0.25 + d8 + d6).color(1.0f, 1.0f, 1.0f, f8).lightmap(n18, n19).endVertex();
                                worldRenderer.pos((double)n8 + d4 + 0.5, n11, (double)n7 + d5 + 0.5).tex(1.0 + d7, (double)n11 * 0.25 + d8 + d6).color(1.0f, 1.0f, 1.0f, f8).lightmap(n18, n19).endVertex();
                                worldRenderer.pos((double)n8 + d4 + 0.5, n12, (double)n7 + d5 + 0.5).tex(1.0 + d7, (double)n12 * 0.25 + d8 + d6).color(1.0f, 1.0f, 1.0f, f8).lightmap(n18, n19).endVertex();
                                worldRenderer.pos((double)n8 - d4 + 0.5, n12, (double)n7 - d5 + 0.5).tex(0.0 + d7, (double)n12 * 0.25 + d8 + d6).color(1.0f, 1.0f, 1.0f, f8).lightmap(n18, n19).endVertex();
                            }
                        }
                    }
                    ++n8;
                }
                ++n7;
            }
            if (n6 >= 0) {
                tessellator.draw();
            }
            worldRenderer.setTranslation(0.0, 0.0, 0.0);
            GlStateManager.enableCull();
            GlStateManager.disableBlend();
            GlStateManager.alphaFunc(516, 0.1f);
            this.disableLightmap();
        }
    }

    private void renderCloudsCheck(RenderGlobal renderGlobal, float f, int n) {
        if (Minecraft.gameSettings.func_181147_e() != 0) {
            this.mc.mcProfiler.endStartSection("clouds");
            GlStateManager.matrixMode(5889);
            GlStateManager.loadIdentity();
            Project.gluPerspective((float)this.getFOVModifier(f, true), (float)((float)this.mc.displayWidth / (float)Minecraft.displayHeight), (float)0.05f, (float)(this.farPlaneDistance * 4.0f));
            GlStateManager.matrixMode(5888);
            GlStateManager.pushMatrix();
            this.setupFog(0, f);
            renderGlobal.renderClouds(f, n);
            GlStateManager.disableFog();
            GlStateManager.popMatrix();
            GlStateManager.matrixMode(5889);
            GlStateManager.loadIdentity();
            Project.gluPerspective((float)this.getFOVModifier(f, true), (float)((float)this.mc.displayWidth / (float)Minecraft.displayHeight), (float)0.05f, (float)(this.farPlaneDistance * MathHelper.SQRT_2));
            GlStateManager.matrixMode(5888);
        }
    }

    private void hurtCameraEffect(float f) {
        EventHurtCam eventHurtCam = new EventHurtCam();
        Exodus.onEvent(eventHurtCam);
        eventHurtCam.setType(EventType.PRE);
        eventHurtCam.call();
        if (!eventHurtCam.isCancelled() && this.mc.getRenderViewEntity() instanceof EntityLivingBase) {
            float f2;
            EntityLivingBase entityLivingBase = (EntityLivingBase)this.mc.getRenderViewEntity();
            float f3 = (float)entityLivingBase.hurtTime - f;
            if (entityLivingBase.getHealth() <= 0.0f) {
                f2 = (float)entityLivingBase.deathTime + f;
                GlStateManager.rotate(40.0f - 8000.0f / (f2 + 200.0f), 0.0f, 0.0f, 1.0f);
            }
            if (f3 < 0.0f) {
                return;
            }
            f3 /= (float)entityLivingBase.maxHurtTime;
            f3 = MathHelper.sin(f3 * f3 * f3 * f3 * (float)Math.PI);
            f2 = entityLivingBase.attackedAtYaw;
            GlStateManager.rotate(-f2, 0.0f, 1.0f, 0.0f);
            GlStateManager.rotate(-f3 * 14.0f, 0.0f, 0.0f, 1.0f);
            GlStateManager.rotate(f2, 0.0f, 1.0f, 0.0f);
        }
    }

    private void renderHand(float f, int n) {
        if (!this.debugView) {
            boolean bl;
            GlStateManager.matrixMode(5889);
            GlStateManager.loadIdentity();
            float f2 = 0.07f;
            if (Minecraft.gameSettings.anaglyph) {
                GlStateManager.translate((float)(-(n * 2 - 1)) * f2, 0.0f, 0.0f);
            }
            Project.gluPerspective((float)this.getFOVModifier(f, false), (float)((float)this.mc.displayWidth / (float)Minecraft.displayHeight), (float)0.05f, (float)(this.farPlaneDistance * 2.0f));
            GlStateManager.matrixMode(5888);
            GlStateManager.loadIdentity();
            if (Minecraft.gameSettings.anaglyph) {
                GlStateManager.translate((float)(n * 2 - 1) * 0.1f, 0.0f, 0.0f);
            }
            GlStateManager.pushMatrix();
            this.hurtCameraEffect(f);
            if (Minecraft.gameSettings.viewBobbing) {
                this.setupViewBobbing(f);
            }
            boolean bl2 = bl = this.mc.getRenderViewEntity() instanceof EntityLivingBase && ((EntityLivingBase)this.mc.getRenderViewEntity()).isPlayerSleeping();
            if (!(Minecraft.gameSettings.thirdPersonView != 0 || bl || Minecraft.gameSettings.hideGUI || Minecraft.playerController.isSpectator())) {
                this.enableLightmap();
                this.itemRenderer.renderItemInFirstPerson(f);
                this.disableLightmap();
            }
            GlStateManager.popMatrix();
            if (Minecraft.gameSettings.thirdPersonView == 0 && !bl) {
                this.itemRenderer.renderOverlays(f);
                this.hurtCameraEffect(f);
            }
            if (Minecraft.gameSettings.viewBobbing) {
                this.setupViewBobbing(f);
            }
        }
    }

    public void disableLightmap() {
        GlStateManager.setActiveTexture(OpenGlHelper.lightmapTexUnit);
        GlStateManager.disableTexture2D();
        GlStateManager.setActiveTexture(OpenGlHelper.defaultTexUnit);
    }
}

