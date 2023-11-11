package me.nyan.flush.utils.render;

import net.minecraft.client.renderer.texture.TextureUtil;
import net.minecraft.util.EnumChatFormatting;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL12;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.nio.IntBuffer;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

public class ExportingHelper {
    private static final Logger logger = LogManager.getLogger();
    private static final DateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy_HH.mm.ss");

    private static IntBuffer pixelBuffer;

    private static int[] pixelValues;

    public static String saveScreenshot(File directory, int x, int y, int width, int height) {
        return saveScreenshot(directory, null, Format.PNG, x, y, width, height);
    }

    public static String saveScreenshot(File directory, Format format, int x, int y, int width, int height) {
        return saveScreenshot(directory, null, format, x, y, width, height);
    }

    public static String saveScreenshot(File directory, String name, Format format, int x, int y, int width, int height) {
        if (format == null) {
            format = Format.PNG;
        }

        try {
            directory.mkdir();

            int i = width * height;

            if (pixelBuffer == null || pixelBuffer.capacity() < i) {
                pixelBuffer = BufferUtils.createIntBuffer(i);
                pixelValues = new int[i];
            }

            GL11.glPixelStorei(GL11.GL_PACK_ALIGNMENT, 1);
            GL11.glPixelStorei(GL11.GL_UNPACK_ALIGNMENT, 1);
            pixelBuffer.clear();

            GL11.glReadPixels(x, y, width, height, GL12.GL_BGRA, GL12.GL_UNSIGNED_INT_8_8_8_8_REV, pixelBuffer);

            pixelBuffer.get(pixelValues);
            TextureUtil.processPixelValues(pixelValues, width, height);
            BufferedImage bufferedimage = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
            bufferedimage.setRGB(0, 0, width, height, pixelValues, 0, width);

            File saveLocation;
            if (name == null) {
                saveLocation = new File(directory, getFileNameForDirectory(directory));
            } else {
                saveLocation = new File(directory, name);
            }
            ImageIO.write(bufferedimage, format.name, saveLocation);
            return EnumChatFormatting.GREEN + "Exported image: " + saveLocation.getCanonicalPath();
        } catch (Exception exception) {
            logger.warn("Couldn't save image", exception);
            return EnumChatFormatting.RED + "Failed to export image: " + exception;
        }
    }

    public static String getFileNameForDirectory(File directory) {
        String s = dateFormat.format(new Date());
        int i = 1;
        while (true) {
            File file = new File(directory, s + (i == 1 ? "" : "_" + i));
            if (!file.exists()) {
                return file.getName();
            }
            ++i;
        }
    }

    public enum Format {
        PNG("png"),
        JPEG("jpeg"),
        GIF("gif"),
        BMP("bmp");

        public final String name;

        Format(String name) {
            this.name = name;
        }
    }
}