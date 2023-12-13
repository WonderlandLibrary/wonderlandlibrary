package dev.rise.module.impl.render;

import dev.rise.event.impl.render.FadingOutlineEvent;
import dev.rise.event.impl.render.Render2DEvent;
import dev.rise.event.impl.render.Shader3DEvent;
import dev.rise.module.Module;
import dev.rise.module.api.ModuleInfo;
import dev.rise.module.enums.Category;
import dev.rise.util.render.RenderUtil;
import dev.rise.util.render.ShaderUtil;
import dev.rise.util.render.theme.ThemeType;
import dev.rise.util.render.theme.ThemeUtil;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.renderer.entity.Render;
import net.minecraft.client.renderer.entity.RendererLivingEntity;
import net.minecraft.client.shader.Framebuffer;
import net.minecraft.entity.player.EntityPlayer;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL20;
import store.intent.intentguard.annotation.Exclude;
import store.intent.intentguard.annotation.Strategy;

import java.awt.*;

@Exclude({Strategy.NUMBER_OBFUSCATION, Strategy.FLOW_OBFUSCATION})
@ModuleInfo(name = "ShaderESP", description = "Renders a glow around players through walls", category = Category.RENDER)
public class ShaderESP extends Module {

    private Framebuffer dataFBO = new Framebuffer(mc.displayWidth, mc.displayHeight, true);
    private final ShaderUtil shaderProgram = new ShaderUtil("rise/shader/fade_outline.glsl");

    public static boolean runningShader;

    @Override
    protected void onDisable() {
        runningShader = false;
    }

    @Override
    public void onShader3DEvent(final Shader3DEvent event) {
        runningShader = true;
        setupBuffers();

        dataFBO.bindFramebuffer(true);

        RendererLivingEntity.NAME_TAG_RANGE = 0;
        RendererLivingEntity.NAME_TAG_RANGE_SNEAK = 0;

        final float partialTicks = event.getPartialTicks();

        int count = 0;
        for (final EntityPlayer player : mc.theWorld.playerEntities) {
            final Render<EntityPlayer> render = mc.getRenderManager().getEntityRenderObject(player);

            if (mc.getRenderManager() == null || render == null || player == null || player == mc.thePlayer || player.isDead || player.bot || !RenderUtil.isInViewFrustrum(player))
                continue;

            final double x = player.prevPosX + (player.posX - player.prevPosX) * partialTicks;
            final double y = player.prevPosY + (player.posY - player.prevPosY) * partialTicks;
            final double z = player.prevPosZ + (player.posZ - player.prevPosZ) * partialTicks;
            final float yaw = player.prevRotationYaw + (player.rotationYaw - player.prevRotationYaw) * partialTicks;

            final Color color = ThemeUtil.getThemeColor(count * 5, ThemeType.GENERAL, 0.5f);

            RendererLivingEntity.setShaderBrightness(player, player.hurtTime > 0 ? Color.RED : color);
            render.doRender(player, x - mc.getRenderManager().renderPosX, y - mc.getRenderManager().renderPosY, z - mc.getRenderManager().renderPosZ, yaw, partialTicks);
            RendererLivingEntity.unsetShaderBrightness();

            count++;
        }

        RendererLivingEntity.NAME_TAG_RANGE = 64;
        RendererLivingEntity.NAME_TAG_RANGE_SNEAK = 32;

        RenderHelper.disableStandardItemLighting();
        mc.entityRenderer.disableLightmap();
        mc.getFramebuffer().bindFramebuffer(true);
        runningShader = false;
    }

    @Override
    public void onRender2DEvent(final Render2DEvent event) {
        if (mc.gameSettings.showDebugInfo)
            return;

        final ScaledResolution scaledResolution = event.getScaledResolution();
        final int programID = shaderProgram.getProgramID();

        // TODO: you can create values for those variables passed to the shader
        final Color color = ThemeUtil.getThemeColor(0, ThemeType.GENERAL, 0.5f);
        final int radius = 7;
        final int fading = 300;

        dataFBO.bindFramebuffer(true);
        final FadingOutlineEvent fadingOutlineEvent = new FadingOutlineEvent();
        fadingOutlineEvent.call();
        mc.getFramebuffer().bindFramebuffer(true);

        shaderProgram.init();
        passUniforms(programID, scaledResolution, color, radius, fading);

        GlStateManager.alphaFunc(GL11.GL_GREATER, 0.0F);
        GlStateManager.enableBlend();
        dataFBO.bindFramebufferTexture();
        ShaderUtil.drawQuads(scaledResolution);
        shaderProgram.unload();
        GlStateManager.disableBlend();
    }

    private void passUniforms(final int programID, final ScaledResolution scaledResolution, final Color color, final int radius, final int fading) {
        GL20.glUniform1i(GL20.glGetUniformLocation(programID, "u_diffuse_sampler"), 0);
        GL20.glUniform2f(GL20.glGetUniformLocation(programID, "u_texel_size"), 1.0F / scaledResolution.getScaledWidth(), 1.0F / scaledResolution.getScaledHeight());
        GL20.glUniform1i(GL20.glGetUniformLocation(programID, "u_radius"), radius);
        GL20.glUniform1i(GL20.glGetUniformLocation(programID, "u_fading"), fading);
    }

    private void setupBuffers() {
        try {
            dataFBO.framebufferClear();

            if (mc.displayWidth != dataFBO.framebufferWidth || mc.displayHeight != dataFBO.framebufferHeight) {
                dataFBO.deleteFramebuffer();
                dataFBO = new Framebuffer(mc.displayWidth, mc.displayHeight, true);
            }
        } catch (final Exception exception) {
        }
    }
}