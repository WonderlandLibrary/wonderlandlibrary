/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraftforge.fml.relauncher.Side
 *  net.minecraftforge.fml.relauncher.SideOnly
 */
package net.ccbluex.liquidbounce.utils.render;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;
import javax.imageio.ImageIO;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(value=Side.CLIENT)
public final class IconUtils {
    public static ByteBuffer[] getFavicon() {
        try {
            return new ByteBuffer[]{IconUtils.readImageToBuffer(IconUtils.class.getResourceAsStream("/assets/minecraft/liquidbounce/icon_16x16.png")), IconUtils.readImageToBuffer(IconUtils.class.getResourceAsStream("/assets/minecraft/liquidbounce/icon_32x32.png"))};
        }
        catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    private static ByteBuffer readImageToBuffer(InputStream imageStream) throws IOException {
        if (imageStream == null) {
            return null;
        }
        BufferedImage bufferedImage = ImageIO.read(imageStream);
        int[] rgb = bufferedImage.getRGB(0, 0, bufferedImage.getWidth(), bufferedImage.getHeight(), null, 0, bufferedImage.getWidth());
        ByteBuffer byteBuffer = ByteBuffer.allocate(4 * rgb.length);
        for (int i : rgb) {
            byteBuffer.putInt(i << 8 | i >> 24 & 0xFF);
        }
        byteBuffer.flip();
        return byteBuffer;
    }
}

