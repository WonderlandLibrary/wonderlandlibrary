/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  org.lwjgl.BufferUtils
 *  org.lwjgl.opengl.GL13
 *  org.lwjgl.opengl.GL20
 */
package com.wallhacks.losebypass.systems.module.modules.render;

import com.wallhacks.losebypass.LoseBypass;
import com.wallhacks.losebypass.event.eventbus.SubscribeEvent;
import com.wallhacks.losebypass.event.events.Render2DEvent;
import com.wallhacks.losebypass.event.events.Render3DEvent;
import com.wallhacks.losebypass.systems.module.Module;
import com.wallhacks.losebypass.systems.module.modules.misc.AntiBot;
import com.wallhacks.losebypass.systems.module.modules.misc.FreeLook;
import com.wallhacks.losebypass.systems.setting.settings.BooleanSetting;
import com.wallhacks.losebypass.systems.setting.settings.ColorSetting;
import com.wallhacks.losebypass.systems.setting.settings.DoubleSetting;
import com.wallhacks.losebypass.systems.setting.settings.ModeSetting;
import com.wallhacks.losebypass.utils.MC;
import com.wallhacks.losebypass.utils.MathUtil;
import com.wallhacks.losebypass.utils.RenderUtil;
import com.wallhacks.losebypass.utils.ShaderUtil;
import java.awt.Color;
import java.nio.FloatBuffer;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.stream.Collectors;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.client.shader.Framebuffer;
import net.minecraft.entity.Entity;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.AxisAlignedBB;
import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.GL13;
import org.lwjgl.opengl.GL20;

@Module.Registration(name="ESP", description="Renders on entities", category=Module.Category.RENDER)
public class ESP
extends Module {
    public static boolean rendering = false;
    private final ModeSetting mode = this.modeSetting("Mode", "Shader", Arrays.asList("Shader", "Full", "Outline", "Fill"));
    private final BooleanSetting showInvisible = this.booleanSetting("ShowInvisible", true).visibility(v -> {
        if (this.mode.is("Shader")) return false;
        return true;
    });
    private final DoubleSetting lineWidth = this.doubleSetting("LineWidth", 4.0, 1.0, 10.0).visibility(v -> {
        if (this.mode.is("fill")) return false;
        return true;
    });
    private final DoubleSetting alpha = this.doubleSetting("Alpha", 1.0, 0.5, 15.0).visibility(v -> this.mode.is("Shader"));
    private final BooleanSetting renderPlayer = this.booleanSetting("Players", true).visibility(v -> this.mode.is("Shader"));
    private final ColorSetting color = this.colorSetting("Color", Color.WHITE);
    private final BooleanSetting renderFriends = this.booleanSetting("Friends", true).visibility(v -> this.mode.is("Shader"));
    private final ColorSetting friendColor = this.colorSetting("FriendColor", new Color(1245089954, true));
    private final BooleanSetting renderDrops = this.booleanSetting("Drops", true).visibility(v -> this.mode.is("Shader")).visibility(v -> this.mode.is("Shader"));
    private final ColorSetting dropsColor = this.colorSetting("Drops Color", new Color(1245089954, true)).visibility(v -> this.mode.is("Shader"));
    private final ShaderUtil outlineShader = new ShaderUtil("textures/shaders/outline.frag");
    private final ShaderUtil glowShader = new ShaderUtil("textures/shaders/glow.frag");
    private final ESPBufferGroup enemies = new ESPBufferGroup(){

        @Override
        public void entities() {
            this.entities = MC.mc.theWorld.playerEntities.stream().filter(e -> {
                if (!AntiBot.allowRender(e)) return false;
                if (LoseBypass.socialManager.isFriend((EntityPlayer)e)) return false;
                return true;
            }).collect(Collectors.toList());
        }
    };
    private final ESPBufferGroup friends = new ESPBufferGroup(){

        @Override
        public void entities() {
            this.entities = MC.mc.theWorld.playerEntities.stream().filter(e -> {
                if (MC.mc.thePlayer == e) return false;
                if (!LoseBypass.socialManager.isFriend((EntityPlayer)e)) return false;
                return true;
            }).collect(Collectors.toList());
        }
    };
    private final ESPBufferGroup drops = new ESPBufferGroup(){

        @Override
        public void entities() {
            this.entities = MC.mc.theWorld.loadedEntityList.stream().filter(e -> e instanceof EntityItem).collect(Collectors.toList());
        }
    };

    @Override
    public void onDisable() {
        this.enemies.clear();
        this.friends.clear();
        this.drops.clear();
        rendering = false;
    }

    @SubscribeEvent
    public void onRenderWorldLast(Render3DEvent event) {
        Entity entity = mc.getRenderViewEntity();
        if (this.mode.is("Shader")) {
            float partialTicks = event.partialTicks;
            this.enemies.complete((Boolean)this.renderPlayer.getValue(), this.color.getColor(), partialTicks);
            this.friends.complete((Boolean)this.renderFriends.getValue(), this.friendColor.getColor(), partialTicks);
            this.drops.complete((Boolean)this.renderDrops.getValue(), this.dropsColor.getColor(), partialTicks);
            mc.getFramebuffer().bindFramebuffer(true);
            GlStateManager.disableLighting();
            return;
        }
        Iterator iterator = ESP.mc.theWorld.playerEntities.stream().filter(AntiBot::allowRender).collect(Collectors.toList()).iterator();
        block8: while (iterator.hasNext()) {
            EntityPlayer entityPlayer = (EntityPlayer)iterator.next();
            if (entityPlayer.isInvisible() && !((Boolean)this.showInvisible.getValue()).booleanValue()) continue;
            Color color = this.playerColor(entityPlayer);
            AxisAlignedBB bb = RenderUtil.getRenderBB(entityPlayer);
            double lineWidth = (Double)this.lineWidth.getValue();
            switch ((String)this.mode.getValue()) {
                case "Fill": {
                    this.fill(bb, color);
                    continue block8;
                }
                case "Outline": {
                    this.outline(bb, color, lineWidth);
                    continue block8;
                }
            }
            this.fill(bb, color);
            this.outline(bb, color, lineWidth);
        }
    }

    @SubscribeEvent
    public void onRenderHud(Render2DEvent event) {
        if (!this.mode.is("Shader")) return;
        if (FreeLook.getPerspective() != 0) return;
        GlStateManager.enableAlpha();
        GlStateManager.enableBlend();
        if (this.enemies.bound && this.enemies.shouldRender) {
            this.shader(this.enemies);
        }
        if (this.friends.bound && this.friends.shouldRender) {
            this.shader(this.friends);
        }
        if (this.drops.bound && this.drops.shouldRender) {
            this.shader(this.drops);
        }
        GlStateManager.disableBlend();
    }

    public void shader(ESPBufferGroup group) {
        if (group.glowFrameBuffer == null) return;
        if (group.outlineFrameBuffer == null) return;
        if (group.framebuffer == null) {
            return;
        }
        GlStateManager.alphaFunc(516, 0.0f);
        OpenGlHelper.glBlendFunc(770, 771, 1, 0);
        group.outlineFrameBuffer.framebufferClear();
        group.outlineFrameBuffer.bindFramebuffer(true);
        this.outlineShader.init();
        this.setupOutlineUniforms(0.0f, 1.0f, group.color);
        RenderUtil.bindTexture(((ESPBufferGroup)group).framebuffer.framebufferTexture);
        ShaderUtil.drawQuads();
        this.outlineShader.init();
        this.setupOutlineUniforms(1.0f, 0.0f, group.color);
        RenderUtil.bindTexture(((ESPBufferGroup)group).framebuffer.framebufferTexture);
        ShaderUtil.drawQuads();
        this.outlineShader.unload();
        group.outlineFrameBuffer.unbindFramebuffer();
        GlStateManager.color(1.0f, 1.0f, 1.0f, 1.0f);
        group.glowFrameBuffer.framebufferClear();
        group.glowFrameBuffer.bindFramebuffer(true);
        this.glowShader.init();
        this.setupGlowUniforms(1.0f, 0.0f, group.color);
        RenderUtil.bindTexture(((ESPBufferGroup)group).outlineFrameBuffer.framebufferTexture);
        ShaderUtil.drawQuads();
        this.glowShader.unload();
        group.glowFrameBuffer.unbindFramebuffer();
        mc.getFramebuffer().bindFramebuffer(true);
        this.glowShader.init();
        this.setupGlowUniforms(0.0f, 1.0f, group.color);
        GL13.glActiveTexture((int)34000);
        RenderUtil.bindTexture(((ESPBufferGroup)group).framebuffer.framebufferTexture);
        GL13.glActiveTexture((int)33984);
        RenderUtil.bindTexture(((ESPBufferGroup)group).glowFrameBuffer.framebufferTexture);
        ShaderUtil.drawQuads();
        this.glowShader.unload();
        group.bound = false;
    }

    public void setupGlowUniforms(float direction1, float direction2, Color color) {
        this.glowShader.setUniformi("texture", 0);
        this.glowShader.setUniformi("textureToCheck", 16);
        this.glowShader.setUniformf("radius", ((Double)this.lineWidth.getValue()).floatValue());
        this.glowShader.setUniformf("texelSize", 1.0f / (float)ESP.mc.displayWidth, 1.0f / (float)ESP.mc.displayHeight);
        this.glowShader.setUniformf("direction", direction1, direction2);
        this.glowShader.setUniformf("color", (float)color.getRed() / 255.0f, (float)color.getGreen() / 255.0f, (float)color.getBlue() / 255.0f);
        this.glowShader.setUniformf("exposure", ((Double)this.alpha.getValue()).floatValue());
        this.glowShader.setUniformi("avoidTexture", 1);
        FloatBuffer buffer = BufferUtils.createFloatBuffer((int)256);
        int i = 1;
        while (true) {
            if (!((float)i <= ((Double)this.lineWidth.getValue()).floatValue())) {
                buffer.rewind();
                GL20.glUniform1((int)this.glowShader.getUniform("weights"), (FloatBuffer)buffer);
                return;
            }
            buffer.put(MathUtil.calculateGaussianValue(i, ((Double)this.lineWidth.getValue()).floatValue() / 2.0f));
            ++i;
        }
    }

    public void setupOutlineUniforms(float dir1, float dir2, Color color) {
        this.outlineShader.setUniformi("texture", 0);
        this.outlineShader.setUniformf("radius", ((Double)this.lineWidth.getValue()).floatValue());
        this.outlineShader.setUniformf("texelSize", 1.0f / (float)ESP.mc.displayWidth, 1.0f / (float)ESP.mc.displayHeight);
        this.outlineShader.setUniformf("direction", dir1, dir2);
        this.outlineShader.setUniformf("color", (float)color.getRed() / 255.0f, (float)color.getGreen() / 255.0f, (float)color.getBlue() / 255.0f);
    }

    public void renderEntities(List<Entity> renderEntities, float ticks) {
        boolean entityShadows = ESP.mc.gameSettings.field_181151_V;
        ESP.mc.gameSettings.field_181151_V = false;
        rendering = true;
        renderEntities.forEach(entity -> mc.getRenderManager().renderEntityStatic((Entity)entity, ticks, false));
        rendering = false;
        ESP.mc.gameSettings.field_181151_V = entityShadows;
    }

    private void fill(AxisAlignedBB bb, Color color) {
        RenderUtil.boundingESPBoxFilled(bb, color);
    }

    private void outline(AxisAlignedBB bb, Color color, double lineWidth) {
        RenderUtil.boundingESPBox(bb, color, (float)lineWidth);
    }

    private Color playerColor(EntityPlayer entityPlayer) {
        Color color;
        if (LoseBypass.socialManager.isFriend(entityPlayer.getName())) {
            color = this.friendColor.getColor();
            return color;
        }
        color = this.color.getColor();
        return color;
    }

    public class ESPBufferGroup {
        private Framebuffer framebuffer;
        private Framebuffer outlineFrameBuffer;
        private Framebuffer glowFrameBuffer;
        private boolean shouldRender;
        private boolean cleared;
        public List<Entity> entities;
        public boolean bound;
        private Color color;

        public void setup(Color color) {
            this.color = color;
            this.framebuffer = RenderUtil.createFrameBuffer(this.framebuffer);
            this.outlineFrameBuffer = RenderUtil.createFrameBuffer(this.outlineFrameBuffer);
            this.glowFrameBuffer = RenderUtil.createFrameBuffer(this.glowFrameBuffer);
            this.framebuffer.framebufferClear();
            this.framebuffer.bindFramebuffer(true);
        }

        public void render(Color color, List<Entity> entities, float partialTicks) {
            this.setup(color);
            ESP.this.renderEntities(entities, partialTicks);
            this.framebuffer.unbindFramebuffer();
            this.cleared = false;
        }

        public void clear() {
            this.framebuffer = null;
            this.outlineFrameBuffer = null;
            this.glowFrameBuffer = null;
            this.cleared = true;
        }

        public void update(boolean setting, Color color, float partialTicks) {
            boolean flag;
            boolean bl = flag = !setting || this.entities.isEmpty();
            if (flag) {
                this.shouldRender = false;
                if (this.cleared) return;
                this.clear();
                return;
            }
            this.render(color, this.entities, partialTicks);
            this.shouldRender = true;
            this.bound = true;
        }

        public void complete(boolean setting, Color color, float partialTicks) {
            this.entities();
            this.update(setting, color, partialTicks);
        }

        public void entities() {
        }
    }
}

