package today.flux.utility.tweaker;

import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL12;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.client.shader.Framebuffer;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.IChatComponent;

import static net.minecraft.util.EnumChatFormatting.*;

import java.io.File;
import java.nio.IntBuffer;

public class ScreenShotHook {

    private static IntBuffer pixelBuffer;
    private static int[] pixelValues;

    public static IChatComponent handleScreenshot(File gameDirectory, String screenshotName, int width, int height, Framebuffer buffer) {
        File screenshotDirectory  = new File(Minecraft.getMinecraft().mcDataDir, "screenshots");

        if (!screenshotDirectory.exists()) {
            screenshotDirectory.mkdir();
        }

        if (OpenGlHelper.isFramebufferEnabled()) {
            width = buffer.framebufferTextureWidth;
            height = buffer.framebufferTextureHeight;
        }

        final int imageScale = width * height;
        if (pixelBuffer == null || pixelBuffer.capacity() < imageScale) {
            pixelBuffer = BufferUtils.createIntBuffer(imageScale);
            pixelValues = new int[imageScale];
        }

        GL11.glPixelStorei(GL11.GL_PACK_ALIGNMENT, 1);
        GL11.glPixelStorei(GL11.GL_UNPACK_ALIGNMENT, 1);
        pixelBuffer.clear();

        if (OpenGlHelper.isFramebufferEnabled()) {
            GlStateManager.bindTexture(buffer.framebufferTexture);
            GL11.glGetTexImage(GL11.GL_TEXTURE_2D, 0, GL12.GL_BGRA, GL12.GL_UNSIGNED_INT_8_8_8_8_REV, pixelBuffer);
        } else {
            GL11.glReadPixels(0, 0, width, height, GL12.GL_BGRA, GL12.GL_UNSIGNED_INT_8_8_8_8_REV, pixelBuffer);
        }

        pixelBuffer.get(pixelValues);

        new Thread(new ASyncScreenShot(width, height, pixelValues, Minecraft.getMinecraft().getFramebuffer(), screenshotDirectory)).start();
        return new ChatComponentText(BLUE + "(" + GRAY + BOLD + "Tweaker" + BLUE + ") " + GRAY + "Capturing screenshot.");
    }
}
