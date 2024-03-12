// 
// Decompiled by Procyon v0.5.36
// 

package dev.tenacity.module.impl.render;

import dev.tenacity.utils.render.ESPUtil;
import dev.tenacity.utils.tuples.Pair;
import net.minecraft.entity.EntityLivingBase;
import dev.tenacity.utils.render.ColorUtil;
import net.minecraft.tileentity.TileEntityChest;
import net.minecraft.entity.passive.EntityAnimal;
import net.minecraft.entity.monster.EntityMob;
import net.minecraft.entity.player.EntityPlayer;
import java.nio.FloatBuffer;
import org.lwjgl.opengl.GL20;
import dev.tenacity.utils.misc.MathUtils;
import org.lwjgl.BufferUtils;
import java.util.Iterator;
import org.lwjgl.opengl.GL13;
import org.lwjgl.opengl.GL11;
import dev.tenacity.utils.render.GLUtil;
import net.minecraft.client.gui.ScaledResolution;
import dev.tenacity.event.impl.render.Render2DEvent;
import net.minecraft.client.renderer.GlStateManager;
import dev.tenacity.event.impl.render.RenderModelEvent;
import dev.tenacity.event.impl.render.RenderChestEvent;
import dev.tenacity.utils.render.RenderUtil;
import dev.tenacity.utils.animations.impl.DecelerateAnimation;
import dev.tenacity.event.impl.game.WorldEvent;
import dev.tenacity.module.settings.Setting;
import dev.tenacity.module.settings.ParentAttribute;
import java.util.HashMap;
import java.util.ArrayList;
import dev.tenacity.Tenacity;
import dev.tenacity.module.Category;
import dev.tenacity.utils.animations.Animation;
import java.awt.Color;
import java.util.Map;
import net.minecraft.entity.Entity;
import java.util.List;
import net.minecraft.client.shader.Framebuffer;
import dev.tenacity.utils.render.ShaderUtil;
import dev.tenacity.module.settings.impl.NumberSetting;
import dev.tenacity.module.settings.impl.ColorSetting;
import dev.tenacity.module.settings.impl.MultipleBoolSetting;
import dev.tenacity.module.settings.impl.ModeSetting;
import dev.tenacity.module.settings.impl.BooleanSetting;
import dev.tenacity.module.Module;

public class GlowESP extends Module
{
    private final BooleanSetting kawaseGlow;
    private final ModeSetting colorMode;
    private final MultipleBoolSetting validEntities;
    private final ColorSetting playerColor;
    private final ColorSetting animalColor;
    private final ColorSetting mobColor;
    private final ColorSetting chestColor;
    private final ColorSetting hurtTimeColor;
    private final NumberSetting radius;
    private final NumberSetting iterationsSetting;
    private final NumberSetting offsetSetting;
    private final NumberSetting exposure;
    private final BooleanSetting seperate;
    public static boolean renderNameTags;
    private final ShaderUtil chamsShader;
    private final ShaderUtil outlineShader;
    private final ShaderUtil glowShader;
    private final ShaderUtil kawaseGlowShader;
    private final ShaderUtil kawaseGlowShader2;
    public Framebuffer framebuffer;
    public Framebuffer outlineFrameBuffer;
    public Framebuffer glowFrameBuffer;
    public static boolean renderGlint;
    private final List<Entity> entities;
    private final Map<Object, Color> entityColorMap;
    public static Animation fadeIn;
    private static int currentIterations;
    private static final List<Framebuffer> framebufferList;
    
    public GlowESP() {
        super("GlowESP", "Glow ESP", Category.RENDER, "ESP that glows on players");
        this.kawaseGlow = new BooleanSetting("Kawase Glow", false);
        this.colorMode = new ModeSetting("Color Mode", "Sync", new String[] { "Sync", "Random", "Custom" });
        this.validEntities = new MultipleBoolSetting("Entities", new BooleanSetting[] { new BooleanSetting("Players", true), new BooleanSetting("Animals", true), new BooleanSetting("Mobs", true), new BooleanSetting("Chests", true) });
        this.playerColor = new ColorSetting("Player Color", Tenacity.INSTANCE.getClientColor());
        this.animalColor = new ColorSetting("Animal Color", Tenacity.INSTANCE.getAlternateClientColor());
        this.mobColor = new ColorSetting("Mob Color", Color.RED);
        this.chestColor = new ColorSetting("Chest Color", Color.GREEN);
        this.hurtTimeColor = new ColorSetting("Hurt Time Color", Color.RED);
        this.radius = new NumberSetting("Radius", 4.0, 20.0, 2.0, 2.0);
        this.iterationsSetting = new NumberSetting("Iterations", 4.0, 10.0, 2.0, 1.0);
        this.offsetSetting = new NumberSetting("Offset", 4.0, 10.0, 2.0, 1.0);
        this.exposure = new NumberSetting("Exposure", 2.2, 3.5, 0.5, 0.1);
        this.seperate = new BooleanSetting("Seperate Texture", false);
        this.chamsShader = new ShaderUtil("chams");
        this.outlineShader = new ShaderUtil("Tenacity/Shaders/outline.frag");
        this.glowShader = new ShaderUtil("glow");
        this.kawaseGlowShader = new ShaderUtil("kawaseDownBloom");
        this.kawaseGlowShader2 = new ShaderUtil("kawaseUpGlow");
        this.entities = new ArrayList<Entity>();
        this.entityColorMap = new HashMap<Object, Color>();
        this.playerColor.addParent(this.colorMode, modeSetting -> modeSetting.is("Custom") && this.validEntities.getSetting("Players").isEnabled());
        this.animalColor.addParent(this.colorMode, modeSetting -> modeSetting.is("Custom") && this.validEntities.getSetting("Animals").isEnabled());
        this.mobColor.addParent(this.colorMode, modeSetting -> modeSetting.is("Custom") && this.validEntities.getSetting("Mobs").isEnabled());
        this.chestColor.addParent(this.colorMode, modeSetting -> modeSetting.is("Custom") && this.validEntities.getSetting("Chests").isEnabled());
        this.radius.addParent(this.kawaseGlow, ParentAttribute.BOOLEAN_CONDITION.negate());
        this.iterationsSetting.addParent(this.kawaseGlow, ParentAttribute.BOOLEAN_CONDITION);
        this.offsetSetting.addParent(this.kawaseGlow, ParentAttribute.BOOLEAN_CONDITION);
        this.addSettings(this.kawaseGlow, this.colorMode, this.validEntities, this.playerColor, this.animalColor, this.mobColor, this.chestColor, this.hurtTimeColor, this.iterationsSetting, this.offsetSetting, this.radius, this.exposure, this.seperate);
    }
    
    @Override
    public void onWorldEvent(final WorldEvent event) {
        this.entityColorMap.clear();
    }
    
    @Override
    public void onEnable() {
        super.onEnable();
        this.entityColorMap.clear();
        GlowESP.fadeIn = new DecelerateAnimation(250, 1.0);
    }
    
    public void createFrameBuffers() {
        this.framebuffer = RenderUtil.createFrameBuffer(this.framebuffer);
        this.outlineFrameBuffer = RenderUtil.createFrameBuffer(this.outlineFrameBuffer);
    }
    
    @Override
    public void onRenderChestEvent(final RenderChestEvent e) {
        if (this.validEntities.getSetting("Chests").isEnabled() && this.framebuffer != null) {
            this.framebuffer.bindFramebuffer(false);
            this.chamsShader.init();
            this.chamsShader.setUniformi("textureIn", 0);
            final Color color = this.getColor(e.getEntity());
            RenderUtil.resetColor();
            this.chamsShader.setUniformf("color", color.getRed() / 255.0f, color.getGreen() / 255.0f, color.getBlue() / 255.0f, 1.0f);
            e.drawChest();
            this.chamsShader.unload();
            GlowESP.mc.getFramebuffer().bindFramebuffer(false);
        }
    }
    
    @Override
    public void onRenderModelEvent(final RenderModelEvent e) {
        if (e.isPost() && this.framebuffer != null) {
            if (!this.entities.contains(e.getEntity())) {
                return;
            }
            this.framebuffer.bindFramebuffer(false);
            this.chamsShader.init();
            this.chamsShader.setUniformi("textureIn", 0);
            final Color color = this.getColor(e.getEntity());
            this.chamsShader.setUniformf("color", color.getRed() / 255.0f, color.getGreen() / 255.0f, color.getBlue() / 255.0f, 1.0f);
            RenderUtil.resetColor();
            GlStateManager.enableCull();
            GlowESP.renderGlint = false;
            e.drawModel();
            e.drawLayers();
            GlowESP.renderGlint = true;
            GlStateManager.disableCull();
            this.chamsShader.unload();
            GlowESP.mc.getFramebuffer().bindFramebuffer(false);
        }
    }
    
    @Override
    public void onRender2DEvent(final Render2DEvent e) {
        this.createFrameBuffers();
        this.collectEntities();
        final ScaledResolution sr = new ScaledResolution(GlowESP.mc);
        if (this.framebuffer != null && this.outlineFrameBuffer != null && (this.validEntities.getSetting("Chests").isEnabled() || this.entities.size() > 0)) {
            RenderUtil.setAlphaLimit(0.0f);
            GLUtil.startBlend();
            this.outlineFrameBuffer.framebufferClear();
            this.outlineFrameBuffer.bindFramebuffer(false);
            this.outlineShader.init();
            this.setupOutlineUniforms(0.0f, 1.0f);
            RenderUtil.bindTexture(this.framebuffer.framebufferTexture);
            ShaderUtil.drawQuads();
            this.outlineShader.init();
            this.setupOutlineUniforms(1.0f, 0.0f);
            RenderUtil.bindTexture(this.framebuffer.framebufferTexture);
            ShaderUtil.drawQuads();
            this.outlineShader.unload();
            this.outlineFrameBuffer.unbindFramebuffer();
            if (this.kawaseGlow.isEnabled()) {
                final int offset = this.offsetSetting.getValue().intValue();
                final int iterations = 3;
                if (GlowESP.framebufferList.isEmpty() || GlowESP.currentIterations != iterations || this.framebuffer.framebufferWidth != GlowESP.mc.displayWidth || this.framebuffer.framebufferHeight != GlowESP.mc.displayHeight) {
                    this.initFramebuffers((float)iterations);
                    GlowESP.currentIterations = iterations;
                }
                RenderUtil.setAlphaLimit(0.0f);
                GL11.glBlendFunc(1, 1);
                GL11.glClearColor(0.0f, 0.0f, 0.0f, 0.0f);
                renderFBO(GlowESP.framebufferList.get(1), this.outlineFrameBuffer.framebufferTexture, this.kawaseGlowShader, (float)offset);
                for (int i = 1; i < iterations; ++i) {
                    renderFBO(GlowESP.framebufferList.get(i + 1), GlowESP.framebufferList.get(i).framebufferTexture, this.kawaseGlowShader, (float)offset);
                }
                for (int i = iterations; i > 1; --i) {
                    renderFBO(GlowESP.framebufferList.get(i - 1), GlowESP.framebufferList.get(i).framebufferTexture, this.kawaseGlowShader2, (float)offset);
                }
                final Framebuffer lastBuffer = GlowESP.framebufferList.get(0);
                lastBuffer.framebufferClear();
                lastBuffer.bindFramebuffer(false);
                this.kawaseGlowShader2.init();
                this.kawaseGlowShader2.setUniformf("offset", (float)offset, (float)offset);
                this.kawaseGlowShader2.setUniformi("inTexture", 0);
                this.kawaseGlowShader2.setUniformi("check", this.seperate.isEnabled() ? 1 : 0);
                this.kawaseGlowShader2.setUniformf("lastPass", 1.0f);
                this.kawaseGlowShader2.setUniformf("exposure", this.exposure.getValue().floatValue() * GlowESP.fadeIn.getOutput().floatValue());
                this.kawaseGlowShader2.setUniformi("textureToCheck", 16);
                this.kawaseGlowShader2.setUniformf("halfpixel", 1.0f / lastBuffer.framebufferWidth, 1.0f / lastBuffer.framebufferHeight);
                this.kawaseGlowShader2.setUniformf("iResolution", (float)lastBuffer.framebufferWidth, (float)lastBuffer.framebufferHeight);
                GL13.glActiveTexture(34000);
                RenderUtil.bindTexture(this.framebuffer.framebufferTexture);
                GL13.glActiveTexture(33984);
                RenderUtil.bindTexture(GlowESP.framebufferList.get(1).framebufferTexture);
                ShaderUtil.drawQuads();
                this.kawaseGlowShader2.unload();
                GL11.glClearColor(0.0f, 0.0f, 0.0f, 0.0f);
                GL11.glBlendFunc(770, 771);
                this.framebuffer.framebufferClear();
                RenderUtil.resetColor();
                GlowESP.mc.getFramebuffer().bindFramebuffer(true);
                RenderUtil.bindTexture(GlowESP.framebufferList.get(0).framebufferTexture);
                ShaderUtil.drawQuads();
                RenderUtil.setAlphaLimit(0.0f);
                GlStateManager.bindTexture(0);
            }
            else {
                if (!GlowESP.framebufferList.isEmpty()) {
                    for (final Framebuffer framebuffer : GlowESP.framebufferList) {
                        framebuffer.deleteFramebuffer();
                    }
                    this.glowFrameBuffer = null;
                    GlowESP.framebufferList.clear();
                }
                this.glowFrameBuffer = RenderUtil.createFrameBuffer(this.glowFrameBuffer);
                GL11.glClearColor(0.0f, 0.0f, 0.0f, 0.0f);
                this.glowFrameBuffer.framebufferClear();
                this.glowFrameBuffer.bindFramebuffer(false);
                this.glowShader.init();
                this.setupGlowUniforms(1.0f, 0.0f);
                RenderUtil.bindTexture(this.outlineFrameBuffer.framebufferTexture);
                GL11.glBlendFunc(1, 1);
                ShaderUtil.drawQuads();
                this.glowShader.unload();
                GlowESP.mc.getFramebuffer().bindFramebuffer(false);
                GL11.glClearColor(0.0f, 0.0f, 0.0f, 0.0f);
                this.glowShader.init();
                this.setupGlowUniforms(0.0f, 1.0f);
                if (this.seperate.isEnabled()) {
                    GL13.glActiveTexture(34000);
                    RenderUtil.bindTexture(this.framebuffer.framebufferTexture);
                }
                GL13.glActiveTexture(33984);
                RenderUtil.bindTexture(this.glowFrameBuffer.framebufferTexture);
                GL11.glBlendFunc(770, 771);
                ShaderUtil.drawQuads();
                this.glowShader.unload();
                this.framebuffer.framebufferClear();
                GlowESP.mc.getFramebuffer().bindFramebuffer(false);
            }
        }
    }
    
    private void initFramebuffers(final float iterations) {
        for (final Framebuffer framebuffer : GlowESP.framebufferList) {
            framebuffer.deleteFramebuffer();
        }
        GlowESP.framebufferList.clear();
        GlowESP.framebufferList.add(this.glowFrameBuffer = RenderUtil.createFrameBuffer(null));
        for (int i = 1; i <= iterations; ++i) {
            final Framebuffer currentBuffer = new Framebuffer((int)(GlowESP.mc.displayWidth / Math.pow(2.0, i)), (int)(GlowESP.mc.displayHeight / Math.pow(2.0, i)), true);
            currentBuffer.setFramebufferFilter(9729);
            GlStateManager.bindTexture(currentBuffer.framebufferTexture);
            GL11.glTexParameteri(3553, 10242, 33648);
            GL11.glTexParameteri(3553, 10243, 33648);
            GlStateManager.bindTexture(0);
            GlowESP.framebufferList.add(currentBuffer);
        }
    }
    
    private static void renderFBO(final Framebuffer framebuffer, final int framebufferTexture, final ShaderUtil shader, final float offset) {
        framebuffer.framebufferClear();
        framebuffer.bindFramebuffer(false);
        shader.init();
        RenderUtil.bindTexture(framebufferTexture);
        shader.setUniformf("offset", offset, offset);
        shader.setUniformi("inTexture", 0);
        shader.setUniformi("check", 0);
        shader.setUniformf("lastPass", 0.0f);
        shader.setUniformf("halfpixel", 1.0f / framebuffer.framebufferWidth, 1.0f / framebuffer.framebufferHeight);
        shader.setUniformf("iResolution", (float)framebuffer.framebufferWidth, (float)framebuffer.framebufferHeight);
        ShaderUtil.drawQuads();
        shader.unload();
    }
    
    public void setupGlowUniforms(final float dir1, final float dir2) {
        this.glowShader.setUniformi("texture", 0);
        if (this.seperate.isEnabled()) {
            this.glowShader.setUniformi("textureToCheck", 16);
        }
        this.glowShader.setUniformf("radius", this.radius.getValue().floatValue());
        this.glowShader.setUniformf("texelSize", 1.0f / GlowESP.mc.displayWidth, 1.0f / GlowESP.mc.displayHeight);
        this.glowShader.setUniformf("direction", dir1, dir2);
        this.glowShader.setUniformf("exposure", this.exposure.getValue().floatValue() * GlowESP.fadeIn.getOutput().floatValue());
        this.glowShader.setUniformi("avoidTexture", this.seperate.isEnabled() ? 1 : 0);
        final FloatBuffer buffer = BufferUtils.createFloatBuffer(256);
        for (int i = 1; i <= this.radius.getValue().floatValue(); ++i) {
            buffer.put(MathUtils.calculateGaussianValue((float)i, this.radius.getValue().floatValue() / 2.0f));
        }
        buffer.rewind();
        GL20.glUniform1(this.glowShader.getUniform("weights"), buffer);
    }
    
    public void setupOutlineUniforms(final float dir1, final float dir2) {
        this.outlineShader.setUniformi("textureIn", 0);
        final float iterations = this.kawaseGlow.isEnabled() ? (this.iterationsSetting.getValue().floatValue() * 2.0f) : (this.radius.getValue().floatValue() / 1.5f);
        this.outlineShader.setUniformf("radius", iterations);
        this.outlineShader.setUniformf("texelSize", 1.0f / GlowESP.mc.displayWidth, 1.0f / GlowESP.mc.displayHeight);
        this.outlineShader.setUniformf("direction", dir1, dir2);
    }
    
    private Color getColor(final Object entity) {
        Color color = Color.WHITE;
        final String mode = this.colorMode.getMode();
        switch (mode) {
            case "Custom": {
                if (entity instanceof EntityPlayer) {
                    color = this.playerColor.getColor();
                }
                if (entity instanceof EntityMob) {
                    color = this.mobColor.getColor();
                }
                if (entity instanceof EntityAnimal) {
                    color = this.animalColor.getColor();
                }
                if (entity instanceof TileEntityChest) {
                    color = this.chestColor.getColor();
                    break;
                }
                break;
            }
            case "Sync": {
                final Pair<Color, Color> colors = HUDMod.getClientColors();
                if (HUDMod.isRainbowTheme()) {
                    color = colors.getFirst();
                    break;
                }
                color = ColorUtil.interpolateColorsBackAndForth(15, 0, colors.getFirst(), colors.getSecond(), false);
                break;
            }
            case "Random": {
                if (this.entityColorMap.containsKey(entity)) {
                    color = this.entityColorMap.get(entity);
                    break;
                }
                color = ColorUtil.getRandomColor();
                this.entityColorMap.put(entity, color);
                break;
            }
        }
        if (entity instanceof EntityLivingBase) {
            final EntityLivingBase entityLivingBase = (EntityLivingBase)entity;
            if (entityLivingBase.hurtTime > 0) {
                color = ColorUtil.interpolateColorC(color, this.hurtTimeColor.getColor(), (float)Math.sin(entityLivingBase.hurtTime * 0.3141592653589793));
            }
        }
        return color;
    }
    
    public void collectEntities() {
        this.entities.clear();
        for (final Entity entity : GlowESP.mc.theWorld.getLoadedEntityList()) {
            if (!ESPUtil.isInView(entity)) {
                continue;
            }
            if (entity == GlowESP.mc.thePlayer && GlowESP.mc.gameSettings.thirdPersonView == 0) {
                continue;
            }
            if (entity instanceof EntityAnimal && this.validEntities.getSetting("animals").isEnabled()) {
                this.entities.add(entity);
            }
            if (entity instanceof EntityPlayer && this.validEntities.getSetting("players").isEnabled()) {
                this.entities.add(entity);
            }
            if (!(entity instanceof EntityMob) || !this.validEntities.getSetting("mobs").isEnabled()) {
                continue;
            }
            this.entities.add(entity);
        }
    }
    
    static {
        GlowESP.renderNameTags = true;
        GlowESP.renderGlint = true;
        framebufferList = new ArrayList<Framebuffer>();
    }
}
