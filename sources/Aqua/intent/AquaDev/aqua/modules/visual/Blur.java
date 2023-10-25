package intent.AquaDev.aqua.modules.visual;

import de.Hero.settings.Setting;
import events.Event;
import events.listeners.EventBlur;
import events.listeners.EventPostRender2D;
import intent.AquaDev.aqua.Aqua;
import intent.AquaDev.aqua.modules.Category;
import intent.AquaDev.aqua.modules.Module;
import intent.AquaDev.aqua.utils.shader.ShaderProgram;
import intent.AquaDev.aqua.utils.shader.ShaderStencilUtil;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.shader.Framebuffer;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL20;

public class Blur
extends Module {
    Minecraft MC = Minecraft.getMinecraft();
    private final Framebuffer fboA;
    private final Framebuffer fboB;
    private final Framebuffer maskBuffer;
    private ShaderProgram blurProgram;

    public Blur() {
        super("Blur", "Blur", 0, Category.Visual);
        this.fboA = new Framebuffer(this.MC.displayWidth, this.MC.displayHeight, false);
        this.fboB = new Framebuffer(this.MC.displayWidth, this.MC.displayHeight, false);
        this.maskBuffer = new Framebuffer(this.MC.displayWidth, this.MC.displayHeight, true);
        this.blurProgram = new ShaderProgram("vertex.vert", "blur.glsl");
        Aqua.setmgr.register(new Setting("Sigma", (Module)this, 6.0, 4.0, 20.0, false));
    }

    public static void drawBlurred(Runnable runnable, boolean renderTwice) {
        EventBlur event = new EventBlur(runnable);
        Aqua.INSTANCE.onEvent((Event)event);
        if ((!event.isCancelled() || renderTwice) && Display.isActive()) {
            runnable.run();
        }
    }

    public void onEnable() {
        this.blurProgram = new ShaderProgram("vertex.vert", "blur.glsl");
        super.onEnable();
    }

    public void onDisable() {
        super.onDisable();
    }

    public void onEvent(Event event) {
        if (event instanceof EventPostRender2D) {
            this.onRender2D();
        } else if (event instanceof EventBlur) {
            this.onBlurredEvent((EventBlur)event);
        }
    }

    public void onBlurredEvent(EventBlur event) {
        event.setCancelled(true);
        this.maskBuffer.bindFramebuffer(false);
        event.getBlurredFunction().run();
        mc.getFramebuffer().bindFramebuffer(false);
    }

    public void checkSetup() {
        this.fboA.checkSetup(Blur.mc.displayWidth, Blur.mc.displayHeight);
        this.fboB.checkSetup(Blur.mc.displayWidth, Blur.mc.displayHeight);
        this.maskBuffer.checkSetup(Blur.mc.displayWidth, Blur.mc.displayHeight);
    }

    public void onRender2D() {
        this.checkSetup();
        GlStateManager.pushMatrix();
        GlStateManager.translate((float)0.0f, (float)0.0f, (float)-1.0f);
        GlStateManager.enableAlpha();
        GlStateManager.alphaFunc((int)516, (float)0.0f);
        GlStateManager.enableDepth();
        GlStateManager.enableTexture2D();
        GlStateManager.disableLighting();
        ScaledResolution sr = new ScaledResolution(mc);
        float width = (float)sr.getScaledWidth_double();
        float height = (float)sr.getScaledHeight_double();
        this.blurProgram.init();
        this.setupBlurUniforms();
        this.doBlurPass(0, Blur.mc.getFramebuffer().framebufferTexture, this.fboA, (int)width, (int)height);
        this.doBlurPass(1, this.fboA.framebufferTexture, this.fboB, (int)width, (int)height);
        this.blurProgram.uninit();
        ShaderStencilUtil.initStencil();
        ShaderStencilUtil.bindWriteStencilBuffer();
        this.drawTexturedQuad(this.maskBuffer.framebufferTexture, width, height);
        ShaderStencilUtil.bindReadStencilBuffer((int)1);
        this.drawTexturedQuad(this.fboB.framebufferTexture, width, height);
        ShaderStencilUtil.uninitStencilBuffer();
        mc.getFramebuffer().bindFramebuffer(false);
        GlStateManager.bindTexture((int)0);
        GlStateManager.alphaFunc((int)516, (float)0.2f);
        GlStateManager.disableAlpha();
        GlStateManager.popMatrix();
        GlStateManager.disableBlend();
        this.maskBuffer.framebufferClear();
        mc.getFramebuffer().bindFramebuffer(false);
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
        float sigma = (float)Aqua.setmgr.getSetting("BlurSigma").getCurrentNumber();
        GL20.glUniform1f((int)this.blurProgram.uniform("ratio"), (float)0.0f);
        GL20.glUniform1f((int)this.blurProgram.uniform("sigma"), (float)sigma);
    }

    private void drawTexturedQuad(int texture, double width, double height) {
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
