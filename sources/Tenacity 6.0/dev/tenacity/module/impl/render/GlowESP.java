//package dev.tenacity.module.impl.render;
//
//import dev.tenacity.Tenacity;
//import dev.tenacity.module.ModuleCategory;
//import dev.tenacity.module.Module;
//import dev.tenacity.util.render.*;
//import dev.tenacity.event.IEventListener;
//
//import dev.tenacity.event.impl.render.Render2DEvent;
//import dev.tenacity.setting.impl.*;
//import dev.tenacity.util.animations.Animation;
//import dev.tenacity.util.animations.impl.DecelerateAnimation;
//import dev.tenacity.util.misc.MathUtils;
//import net.minecraft.client.gui.ScaledResolution;
//import net.minecraft.client.renderer.GlStateManager;
//import net.minecraft.client.renderer.OpenGlHelper;
//import net.minecraft.client.renderer.culling.Frustum;
//import net.minecraft.client.shader.Framebuffer;
//import net.minecraft.entity.Entity;
//import net.minecraft.entity.monster.EntityMob;
//import net.minecraft.entity.monster.EntitySkeleton;
//import net.minecraft.entity.passive.EntityAnimal;
//import net.minecraft.entity.passive.EntitySheep;
//import net.minecraft.entity.player.EntityPlayer;
//import org.lwjgl.BufferUtils;
//import org.lwjgl.opengl.GL11;
//import org.lwjgl.opengl.GL13;
//
//import java.awt.*;
//import java.nio.FloatBuffer;
//import java.util.ArrayList;
//import java.util.List;
//
//import static org.lwjgl.opengl.GL20.glUniform1;
//
//public class GlowESP extends Module {
//
//    private final ModeSetting colorMode = new ModeSetting("Color Mode", "Sync", "Sync", "Custom");
//    private final ColorSetting glowColor = new ColorSetting("Glow Color", Tenacity.INSTANCE.getClientColor());
//    private final NumberSetting radius = new NumberSetting("Radius", 4, 30, 2, 2);
//    private final NumberSetting exposure = new NumberSetting("Exposure", 2.2, 3.5, .5, .1);
//    private final BooleanSetting seperate = new BooleanSetting("Seperate Texture", false);
//    private final MultipleBoolSetting validEntities = new MultipleBoolSetting("Entities",
//            new BooleanSetting("Players", true),
//            new BooleanSetting("Animals", true),
//            new BooleanSetting("Mobs", true));
//
//    public GlowESP() {
//        super("GlowESP", "ESP that glows on players", ModuleCategory.RENDER);
//        glowColor.addParent(colorMode, modeSetting -> modeSetting.isMode("Custom"));
//        initializeSettings(colorMode, glowColor, radius, exposure, seperate, validEntities);
//    }
//
//    public static boolean renderNameTags = true;
//    private final ShaderUtil outlineShader = new ShaderUtil("Tenacity/Shaders/outline.frag");
//    private final ShaderUtil glowShader = new ShaderUtil("Tenacity/Shaders/glow.frag");
//
//    public Framebuffer framebuffer;
//    public Framebuffer outlineFrameBuffer;
//    public Framebuffer glowFrameBuffer;
//    private final Frustum frustum = new Frustum();
//
//    private final List<Entity> entities = new ArrayList<>();
//
//    public static Animation fadeIn;
//
//    @Override
//    public void onEnable() {
//        super.onEnable();
//        fadeIn = new DecelerateAnimation(250, 1);
//    }
//
//    public void createFrameBuffers() {
//        framebuffer = RenderUtil.createFrameBuffer(framebuffer);
//        outlineFrameBuffer = RenderUtil.createFrameBuffer(outlineFrameBuffer);
//        glowFrameBuffer = RenderUtil.createFrameBuffer(glowFrameBuffer);
//    }
//
//
//    private final IEventListener<Render3DEvent> event3D = e -> {
//        createFrameBuffers();
//        collectEntities();
//        framebuffer.framebufferClear();
//        framebuffer.bindFramebuffer(true);
//     //   renderEntities(tick.getTicks());
//        framebuffer.unbindFramebuffer();
//        mc.getFramebuffer().bindFramebuffer(true);
//        GlStateManager.disableLighting();
//    };
//
//    private final IEventListener<Render2DEvent> event2D = e -> {
//
//        ScaledResolution sr = new ScaledResolution(mc);
//        if (framebuffer != null && outlineFrameBuffer != null && entities.size() > 0) {
//            GlStateManager.enableAlpha();
//            GlStateManager.alphaFunc(516, 0.0f);
//            GlStateManager.enableBlend();
//            OpenGlHelper.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA, GL11.GL_ONE, GL11.GL_ZERO);
//
//            outlineFrameBuffer.framebufferClear();
//            outlineFrameBuffer.bindFramebuffer(true);
//            outlineShader.init();
//            setupOutlineUniforms(0, 1);
//            RenderUtil.bindTexture(framebuffer.framebufferTexture);
//            ShaderUtil.drawQuads();
//            outlineShader.init();
//            setupOutlineUniforms(1, 0);
//            RenderUtil.bindTexture(framebuffer.framebufferTexture);
//            ShaderUtil.drawQuads();
//            outlineShader.unload();
//            outlineFrameBuffer.unbindFramebuffer();
//
//            GlStateManager.color(1, 1, 1, 1);
//            glowFrameBuffer.framebufferClear();
//            glowFrameBuffer.bindFramebuffer(true);
//            glowShader.init();
//            setupGlowUniforms(1, 0);
//            RenderUtil.bindTexture(outlineFrameBuffer.framebufferTexture);
//            ShaderUtil.drawQuads();
//            glowShader.unload();
//            glowFrameBuffer.unbindFramebuffer();
//
//            mc.getFramebuffer().bindFramebuffer(true);
//            glowShader.init();
//            setupGlowUniforms(0, 1);
//            if (seperate.isEnabled()) {
//                GL13.glActiveTexture(GL13.GL_TEXTURE16);
//                RenderUtil.bindTexture(framebuffer.framebufferTexture);
//            }
//            GL13.glActiveTexture(GL13.GL_TEXTURE0);
//            RenderUtil.bindTexture(glowFrameBuffer.framebufferTexture);
//            ShaderUtil.drawQuads();
//            glowShader.unload();
//
//        }
//
//    };
//
//
//    public void setupGlowUniforms(float dir1, float dir2) {
//        glowShader.setUniformi("texture", 0);
//        if (seperate.isEnabled()) {
//            glowShader.setUniformi("textureToCheck", 16);
//        }
//        glowShader.setUniformf("radius", (float) radius.getCurrentValue());
//        glowShader.setUniformf("texelSize", 1.0f / mc.displayWidth, 1.0f / mc.displayHeight);
//        glowShader.setUniformf("direction", dir1, dir2);
//        glowShader.setUniformf("color", 255,255, 255);
//        glowShader.setUniformf("exposure", (float) (exposure.getCurrentValue() * fadeIn.getOutput()));
//        glowShader.setUniformi("avoidTexture", seperate.isEnabled() ? 1 : 0);
//
//        final FloatBuffer buffer = BufferUtils.createFloatBuffer(256);
//        for (int i = 1; i <= (float) radius.getCurrentValue(); i++) {
//            buffer.put(MathUtils.calculateGaussianValue(i, (float) (radius.getCurrentValue() / 2)));
//        }
//        buffer.rewind();
//
//        glUniform1(glowShader.getUniform("weights"), buffer);
//    }
//
//
//    public void setupOutlineUniforms(float dir1, float dir2) {
//        outlineShader.setUniformi("texture", 0);
//        outlineShader.setUniformf("radius", (float) (radius.getCurrentValue() / 1.5f));
//        outlineShader.setUniformf("texelSize", 1.0f / mc.displayWidth, 1.0f / mc.displayHeight);
//        outlineShader.setUniformf("direction", dir1, dir2);
//        outlineShader.setUniformf("color", 255, 255,255);
//    }
//
//    public void renderEntities(float ticks) {
//        entities.forEach(entity -> {
//            renderNameTags = true;
//        });
//    }
//
//    public void collectEntities() {
//        entities.clear();
//        for (Entity entity : mc.theWorld.getLoadedEntityList()) {
//            if (!ESPUtil.isInView(entity)) continue;
//            if (entity == mc.thePlayer && mc.gameSettings.thirdPersonView == 0) continue;
//            if (entity instanceof EntityAnimal && validEntities.getSetting("animals").isEnabled()) {
//                entities.add(entity);
//            }
//
//            if (entity instanceof EntityPlayer && validEntities.getSetting("players").isEnabled()) {
//                entities.add(entity);
//            }
//
//            if (entity instanceof EntityMob && validEntities.getSetting("mobs").isEnabled()) {
//                entities.add(entity);
//            }
//        }
//    }
//
//
//}
