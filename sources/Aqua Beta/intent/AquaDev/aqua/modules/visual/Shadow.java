package intent.AquaDev.aqua.modules.visual;

import de.Hero.settings.Setting;
import events.Event;
import events.listeners.EventGlow;
import events.listeners.EventPostRender2D;
import intent.AquaDev.aqua.Aqua;
import intent.AquaDev.aqua.modules.Category;
import intent.AquaDev.aqua.modules.Module;
import intent.AquaDev.aqua.utils.shader.ShaderProgram;
import intent.AquaDev.aqua.utils.shader.ShaderStencilUtil;
import java.awt.Color;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.shader.Framebuffer;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL20;

public class Shadow
extends Module {
    Minecraft MC = Minecraft.getMinecraft();
    private final Framebuffer pass;
    private final Framebuffer output;
    private final Framebuffer input;
    private ShaderProgram blurProgram;

    public Shadow() {
        super("Shadow", "Shadow", 0, Category.Visual);
        this.pass = new Framebuffer(this.MC.displayWidth, this.MC.displayHeight, false);
        this.output = new Framebuffer(this.MC.displayWidth, this.MC.displayHeight, false);
        this.input = new Framebuffer(this.MC.displayWidth, this.MC.displayHeight, true);
        this.blurProgram = new ShaderProgram("vertex.vert", "alphaBlur.glsl");
        Aqua.setmgr.register(new Setting("Sigma", (Module)this, 6.0, 6.0, 20.0, false));
    }

    public static void drawGlow(Runnable runnable, boolean renderTwice) {
        EventGlow event = new EventGlow(runnable);
        Aqua.INSTANCE.onEvent((Event)event);
        if (!event.isCancelled() || renderTwice) {
            runnable.run();
        }
    }

    public void onEvent(Event event) {
        if (event instanceof EventPostRender2D) {
            Shadow.drawGlow(() -> Gui.drawRect((int)-2001, (int)-2001, (int)-2000, (int)-2000, (int)Color.red.getRGB()), false);
            this.checkSetup();
            GlStateManager.color((float)1.0f, (float)1.0f, (float)1.0f, (float)1.0f);
            GlStateManager.pushMatrix();
            GlStateManager.enableAlpha();
            GlStateManager.alphaFunc((int)516, (float)0.0f);
            GlStateManager.blendFunc((int)770, (int)771);
            GlStateManager.enableDepth();
            GlStateManager.enableTexture2D();
            GlStateManager.disableLighting();
            ScaledResolution sr = new ScaledResolution(this.MC);
            double screenWidth = sr.getScaledWidth_double();
            double screenHeight = sr.getScaledHeight_double();
            this.blurProgram.init();
            this.setupBlurUniforms();
            this.doBlurPass(0, this.input.framebufferTexture, this.pass, (int)screenWidth, (int)screenHeight);
            this.doBlurPass(1, this.pass.framebufferTexture, this.output, (int)screenWidth, (int)screenHeight);
            this.blurProgram.uninit();
            ShaderStencilUtil.initStencil();
            ShaderStencilUtil.bindWriteStencilBuffer();
            this.drawTexturedQuad(this.input.framebufferTexture, screenWidth, screenHeight);
            ShaderStencilUtil.bindReadStencilBuffer((int)0);
            this.drawTexturedQuad(this.output.framebufferTexture, screenWidth, screenHeight);
            ShaderStencilUtil.uninitStencilBuffer();
            GlStateManager.bindTexture((int)0);
            GlStateManager.alphaFunc((int)516, (float)0.2f);
            GlStateManager.disableAlpha();
            GlStateManager.popMatrix();
            GlStateManager.disableBlend();
            this.input.framebufferClear();
            this.MC.getFramebuffer().bindFramebuffer(false);
        } else if (event instanceof EventGlow) {
            this.onGlowEvent((EventGlow)event);
        }
    }

    public void onGlowEvent(EventGlow event) {
        event.setCancelled(true);
        this.input.bindFramebuffer(false);
        event.getRunnable().run();
        this.MC.getFramebuffer().bindFramebuffer(false);
    }

    public void onEnable() {
        this.blurProgram = new ShaderProgram("vertex.vert", "alphaBlur.glsl");
        super.onEnable();
    }

    public void checkSetup() {
        this.input.checkSetup(this.MC.displayWidth, this.MC.displayHeight);
        this.pass.checkSetup(this.MC.displayWidth, this.MC.displayHeight);
        this.output.checkSetup(this.MC.displayWidth, this.MC.displayHeight);
    }

    public void doBlurPass(int pass, int texture, Framebuffer out, int width, int height) {
        out.framebufferClear();
        out.bindFramebuffer(false);
        GL20.glUniform2f((int)this.blurProgram.uniform("direction"), (float)(1 - pass), (float)pass);
        GL11.glBindTexture((int)3553, (int)texture);
        this.blurProgram.doRenderPass((float)width, (float)height);
    }

    public void setupBlurUniforms() {
        GL20.glUniform2f((int)this.blurProgram.uniform("texelSize"), (float)(1.0f / (float)this.MC.displayWidth), (float)(1.0f / (float)this.MC.displayHeight));
        GL20.glUniform1i((int)this.blurProgram.uniform("texture"), (int)0);
        float sigma = (float)Aqua.setmgr.getSetting("ShadowSigma").getCurrentNumber();
        float strength = 1.0f;
        GL20.glUniform1f((int)this.blurProgram.uniform("sigma"), (float)sigma);
        GL20.glUniform1f((int)this.blurProgram.uniform("strength"), (float)1.0f);
    }

    private void drawTexturedQuad(int texture, double width, double height) {
        GlStateManager.enableBlend();
        GL11.glBindTexture((int)3553, (int)texture);
        GL11.glBegin((int)7);
        GL11.glTexCoord2d((double)0.0, (double)1.0);
        GL11.glVertex2d((double)0.0, (double)0.0);
        GL11.glTexCoord2d((double)0.0, (double)0.0);
        GL11.glVertex2d((double)0.0, (double)height);
        GL11.glTexCoord2d((double)1.0, (double)0.0);
        GL11.glVertex2d((double)width, (double)height);
        GL11.glTexCoord2d((double)1.0, (double)1.0);
        GL11.glVertex2d((double)width, (double)0.0);
        GL11.glEnd();
    }
}
