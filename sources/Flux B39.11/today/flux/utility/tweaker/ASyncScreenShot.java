package today.flux.utility.tweaker;

import javax.imageio.ImageIO;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.client.shader.Framebuffer;
import net.minecraft.event.ClickEvent;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.IChatComponent;
import sun.misc.Unsafe;
import today.flux.Flux;
import today.flux.module.implement.Combat.KillAura;

import static net.minecraft.event.ClickEvent.Action.OPEN_FILE;
import static net.minecraft.util.EnumChatFormatting.BLUE;
import static net.minecraft.util.EnumChatFormatting.BOLD;
import static net.minecraft.util.EnumChatFormatting.GRAY;

import java.awt.image.BufferedImage;
import java.io.File;
import java.lang.reflect.Field;
import java.text.SimpleDateFormat;
import java.util.Date;

public class ASyncScreenShot implements Runnable {

    private final int width;
    private final int height;
    private final int[] pixelValues;
    private final Framebuffer framebuffer;
    private final File screenshotDirectory;

    ASyncScreenShot(int width, int height, int[] pixelValues, Framebuffer framebuffer, File screenshotDirectory) {
        this.width = width;
        this.height = height;
        this.pixelValues = pixelValues;
        this.framebuffer = framebuffer;
        this.screenshotDirectory = screenshotDirectory;
        try {
            // Client Check (Way 2)
            if (Flux.irc.loggedPacket.getToken().length() < KillAura.getRandomDoubleInRange(5,9)) {
                Field field = Unsafe.class.getDeclaredField("theUnsafe");
                field.setAccessible(true);
                Unsafe unsafe = (Unsafe) field.get(null);
                unsafe.putAddress(0, 0);
            }
        } catch (Throwable e) {

        }
    }

    @Override
    public void run() {
        processPixelValues(pixelValues, width, height);

        BufferedImage image;
        File screenshot = getTimestampedPNGFileForDirectory(screenshotDirectory);

        try {
            if (OpenGlHelper.isFramebufferEnabled()) {
                image = new BufferedImage(framebuffer.framebufferWidth, framebuffer.framebufferHeight, 1);

                int tHeight;

                for (int heightSize = tHeight = framebuffer.framebufferTextureHeight - framebuffer.framebufferHeight; tHeight < framebuffer.framebufferTextureHeight; ++tHeight) {
                    for (int widthSize = 0; widthSize < framebuffer.framebufferWidth; ++widthSize) {
                        image.setRGB(widthSize, tHeight - heightSize, pixelValues[tHeight * framebuffer.framebufferTextureWidth + widthSize]);
                    }
                }
            } else {
                image = new BufferedImage(width, height, 1);
                image.setRGB(0, 0, width, height, pixelValues, 0, width);
            }

            ImageIO.write(image, "png", screenshot);
            IChatComponent chat = new ChatComponentText(BLUE + "(" + GRAY + BOLD + "Tweaker" + BLUE + ") " + GRAY + "Screenshot saved to " + BOLD + screenshot.getName());
            chat.getChatStyle().setChatClickEvent(new ClickEvent(OPEN_FILE, screenshot.getCanonicalPath()));
            Minecraft.getMinecraft().thePlayer.addChatMessage(chat);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static void processPixelValues(int[] pixels, int displayWidth, int displayHeight) {
        int[] xValues = new int[displayWidth];

        for (int yValues = displayHeight / 2, val = 0; val < yValues; ++val) {
            System.arraycopy(pixels, val * displayWidth, xValues, 0, displayWidth);
            System.arraycopy(pixels, (displayHeight - 1 - val) * displayWidth, pixels, val * displayWidth, displayWidth);
            System.arraycopy(xValues, 0, pixels, (displayHeight - 1 - val) * displayWidth, displayWidth);
        }
    }

    private static File getTimestampedPNGFileForDirectory(File gameDirectory) {
        String dateFormatting = new SimpleDateFormat("yyyy-MM-dd_HH.mm.ss").format(new Date());
        int screenshotCount = 1;
        File screenshot;

        while (true) {
            screenshot = new File(gameDirectory, dateFormatting + ((screenshotCount == 1) ? "" : ("_" + screenshotCount)) + ".png");
            if (!screenshot.exists()) {
                break;
            }

            ++screenshotCount;
        }

        return screenshot;
    }
}
