package intent.AquaDev.aqua.utils.shader;

import events.listeners.EventGlowESP;
import intent.AquaDev.aqua.Aqua;
import intent.AquaDev.aqua.utils.shader.ShaderProgram;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.shader.Framebuffer;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL20;

public class Glow {
    public static Glow INSTANCE;
    static Minecraft MC;
    public static Framebuffer pass;
    public static Framebuffer output;
    public static Framebuffer input;
    public static ShaderProgram blurProgram;

    public static void checkSetup() {
        input.checkSetup(Glow.MC.displayWidth, Glow.MC.displayHeight);
        pass.checkSetup(Glow.MC.displayWidth, Glow.MC.displayHeight);
        output.checkSetup(Glow.MC.displayWidth, Glow.MC.displayHeight);
    }

    public static void doBlurPass(int pass, int texture, Framebuffer out, int width, int height) {
        out.framebufferClear();
        out.bindFramebuffer(false);
        GL20.glUniform2f((int)blurProgram.uniform("direction"), (float)(1 - pass), (float)pass);
        GL11.glBindTexture((int)3553, (int)texture);
        blurProgram.doRenderPass((float)width, (float)height);
    }

    public static void setupBlurUniforms() {
        GL20.glUniform2f((int)blurProgram.uniform("texelSize"), (float)(1.0f / (float)Glow.MC.displayWidth), (float)(1.0f / (float)Glow.MC.displayHeight));
        GL20.glUniform1i((int)blurProgram.uniform("texture"), (int)0);
        float sigma1 = (float)Aqua.setmgr.getSetting("ESPSigma").getCurrentNumber();
        float strength = (float)Aqua.setmgr.getSetting("ESPMultiplier").getCurrentNumber();
        GL20.glUniform1f((int)blurProgram.uniform("sigmaESP"), (float)sigma1);
        GL20.glUniform1f((int)blurProgram.uniform("strength"), (float)strength);
    }

    public void drawTexturedQuad(int texture, double width, double height) {
        INSTANCE = this;
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

    public static void onGlowEvent(EventGlowESP event) {
        event.setCancelled(true);
        input.bindFramebuffer(false);
        event.getRunnable().run();
        MC.getFramebuffer().bindFramebuffer(false);
    }

    static {
        MC = Minecraft.getMinecraft();
        pass = new Framebuffer(Glow.MC.displayWidth, Glow.MC.displayHeight, false);
        output = new Framebuffer(Glow.MC.displayWidth, Glow.MC.displayHeight, false);
        input = new Framebuffer(Glow.MC.displayWidth, Glow.MC.displayHeight, true);
        blurProgram = new ShaderProgram("vertex.vert", "alphaBlurESP.glsl");
    }
}
