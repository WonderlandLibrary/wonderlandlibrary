package intent.AquaDev.aqua.utils.shader;

import events.listeners.EventGlowArray;
import intent.AquaDev.aqua.Aqua;
import intent.AquaDev.aqua.utils.shader.ShaderProgram;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.shader.Framebuffer;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL20;

public class Array {
    public static Array INSTANCE;
    static Minecraft MC;
    public static Framebuffer pass;
    public static Framebuffer output;
    public static Framebuffer input;
    public static ShaderProgram blurProgram;

    public static void checkSetup() {
        input.checkSetup(Array.MC.displayWidth, Array.MC.displayHeight);
        pass.checkSetup(Array.MC.displayWidth, Array.MC.displayHeight);
        output.checkSetup(Array.MC.displayWidth, Array.MC.displayHeight);
    }

    public static void doBlurPass(int pass, int texture, Framebuffer out, int width, int height) {
        out.framebufferClear();
        out.bindFramebuffer(false);
        GL20.glUniform2f((int)blurProgram.uniform("direction"), (float)(1 - pass), (float)pass);
        GL11.glBindTexture((int)3553, (int)texture);
        blurProgram.doRenderPass((float)width, (float)height);
    }

    public static void setupBlurUniforms() {
        GL20.glUniform2f((int)blurProgram.uniform("texelSize"), (float)(1.0f / (float)Array.MC.displayWidth), (float)(1.0f / (float)Array.MC.displayHeight));
        GL20.glUniform1i((int)blurProgram.uniform("texture"), (int)0);
        float sigma1 = (float)Aqua.setmgr.getSetting("ArraylistSigma").getCurrentNumber();
        float strength = (float)Aqua.setmgr.getSetting("ArraylistMultiplier").getCurrentNumber();
        GL20.glUniform1f((int)blurProgram.uniform("sigmaArray"), (float)sigma1);
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

    public static void onArray(EventGlowArray event) {
        event.setCancelled(true);
        input.bindFramebuffer(false);
        event.getRunnable().run();
        MC.getFramebuffer().bindFramebuffer(false);
    }

    static {
        MC = Minecraft.getMinecraft();
        pass = new Framebuffer(Array.MC.displayWidth, Array.MC.displayHeight, false);
        output = new Framebuffer(Array.MC.displayWidth, Array.MC.displayHeight, false);
        input = new Framebuffer(Array.MC.displayWidth, Array.MC.displayHeight, true);
        blurProgram = new ShaderProgram("vertex.vert", "alphaBlurArray.glsl");
    }
}
