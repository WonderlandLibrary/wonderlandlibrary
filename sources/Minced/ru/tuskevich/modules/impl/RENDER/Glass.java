// 
// Decompiled by Procyon v0.5.36
// 

package ru.tuskevich.modules.impl.RENDER;

import java.awt.Color;
import ru.tuskevich.util.shader.BlurUtility;
import net.minecraft.client.renderer.OpenGlHelper;
import ru.tuskevich.Minced;
import ru.tuskevich.event.events.impl.EventDisplay;
import ru.tuskevich.event.EventTarget;
import net.minecraft.client.renderer.entity.Render;
import java.util.Iterator;
import org.lwjgl.opengl.GL11;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.client.renderer.entity.RenderLivingBase;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.Entity;
import ru.tuskevich.event.events.impl.EventRender;
import java.nio.FloatBuffer;
import org.lwjgl.opengl.GL20;
import org.lwjgl.BufferUtils;
import ru.tuskevich.ui.dropui.setting.Setting;
import ru.tuskevich.ui.dropui.setting.imp.MultiBoxSetting;
import ru.tuskevich.util.shader.ShaderUtility;
import net.minecraft.client.shader.Framebuffer;
import ru.tuskevich.modules.Type;
import ru.tuskevich.modules.ModuleAnnotation;
import ru.tuskevich.modules.Module;

@ModuleAnnotation(name = "Glass", type = Type.RENDER)
public class Glass extends Module
{
    public Framebuffer framebuffer;
    public static ShaderUtility blurShader;
    public MultiBoxSetting mode;
    
    public Glass() {
        this.mode = new MultiBoxSetting("Mode", new String[] { "Blur", "Shadow", "White-Black", "Pixel", "Distortion", "Player" });
        this.add(this.mode);
    }
    
    public static void setupUniforms(final float dir1, final float dir2, final float radius) {
        Glass.blurShader.setUniformi("textureIn", 0);
        Glass.blurShader.setUniformf("texelSize", 1.0f / Glass.mc.displayWidth, 1.0f / Glass.mc.displayHeight);
        Glass.blurShader.setUniformf("direction", dir1, dir2);
        Glass.blurShader.setUniformf("radius", radius);
        final FloatBuffer weightBuffer = BufferUtils.createFloatBuffer(256);
        for (int i = 0; i <= radius; ++i) {
            weightBuffer.put(ShaderUtility.calculateGaussianValue((float)i, radius / 2.0f));
        }
        weightBuffer.rewind();
        GL20.glUniform1(Glass.blurShader.getUniform("weights"), weightBuffer);
    }
    
    @EventTarget
    public void onTarget(final EventRender e) {
        (this.framebuffer = ShaderUtility.createFrameBuffer(this.framebuffer)).framebufferClear();
        this.framebuffer.bindFramebuffer(true);
        for (final Entity entity2 : Glass.mc.world.loadedEntityList) {
            final Entity entity = entity2;
            final Minecraft mc = Glass.mc;
            if (entity2 != Minecraft.player && entity != null && entity instanceof EntityPlayer) {
                GlStateManager.pushMatrix();
                final float x = (float)(entity.lastTickPosX + (entity.posX - entity.lastTickPosX) * e.pt - Glass.mc.getRenderManager().renderPosX);
                final float y = (float)(entity.lastTickPosY + (entity.posY - entity.lastTickPosY) * e.pt - Glass.mc.getRenderManager().renderPosY);
                final float z = (float)(entity.lastTickPosZ + (entity.posZ - entity.lastTickPosZ) * e.pt - Glass.mc.getRenderManager().renderPosZ);
                Render<Entity> render = null;
                try {
                    render = Glass.mc.getRenderManager().getEntityRenderObject(entity);
                }
                catch (Exception e2) {
                    e2.printStackTrace();
                }
                if (render != null) {
                    RenderLivingBase.class.cast(render).doRender2((EntityLivingBase)entity, x, y, z, entity.rotationYaw, Glass.mc.getRenderPartialTicks());
                }
                GL11.glPopMatrix();
            }
        }
        this.framebuffer.unbindFramebuffer();
        Glass.mc.getFramebuffer().bindFramebuffer(true);
    }
    
    @EventTarget(0)
    public void onDisplay(final EventDisplay e) {
        Minced.getInstance().scaleMath.pushScale();
        if (this.framebuffer != null) {
            GL11.glPushMatrix();
            GlStateManager.enableAlpha();
            GlStateManager.alphaFunc(516, 0.0f);
            GlStateManager.enableBlend();
            OpenGlHelper.glBlendFunc(770, 771, 1, 0);
            if (this.mode.get(4)) {
                Glass.mc.getFramebuffer().bindFramebuffer(true);
                ShaderUtility.bindTexture(this.framebuffer.framebufferTexture);
                BlurUtility.drawDistortion(ShaderUtility::drawQuads);
                Glass.mc.getFramebuffer().bindFramebuffer(false);
            }
            if (this.mode.get(3)) {
                Glass.mc.getFramebuffer().bindFramebuffer(true);
                ShaderUtility.bindTexture(this.framebuffer.framebufferTexture);
                BlurUtility.drawPixel(ShaderUtility::drawQuads);
                Glass.mc.getFramebuffer().bindFramebuffer(false);
            }
            if (this.mode.get(2)) {
                Glass.mc.getFramebuffer().bindFramebuffer(true);
                ShaderUtility.bindTexture(this.framebuffer.framebufferTexture);
                BlurUtility.drawWhiteBlack(ShaderUtility::drawQuads);
                Glass.mc.getFramebuffer().bindFramebuffer(false);
            }
            if (this.mode.get(0)) {
                Glass.mc.getFramebuffer().bindFramebuffer(true);
                ShaderUtility.bindTexture(this.framebuffer.framebufferTexture);
                BlurUtility.drawBlur(20.0f, ShaderUtility::drawQuads);
                Glass.mc.getFramebuffer().bindFramebuffer(false);
            }
            if (this.mode.get(1)) {
                Glass.mc.getFramebuffer().bindFramebuffer(true);
                ShaderUtility.bindTexture(this.framebuffer.framebufferTexture);
                BlurUtility.drawShadow(10.0f, 2.0f, ShaderUtility::drawQuads, Color.BLACK, false);
                Glass.mc.getFramebuffer().bindFramebuffer(false);
            }
            if (this.mode.get(5)) {
                Glass.mc.getFramebuffer().bindFramebuffer(true);
                ShaderUtility.bindTexture(this.framebuffer.framebufferTexture);
                GL11.glColor4f(1.0f, 1.0f, 1.0f, 0.1f);
                ShaderUtility.drawQuads();
                Glass.mc.getFramebuffer().bindFramebuffer(false);
            }
            GlStateManager.disableAlpha();
            GL11.glPopMatrix();
        }
        Minced.getInstance().scaleMath.popScale();
    }
    
    static {
        Glass.blurShader = new ShaderUtility("client/shaders/gaussian.frag");
    }
}
