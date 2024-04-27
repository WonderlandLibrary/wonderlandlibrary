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
    private static ByteBuffer readImageToBuffer(InputStream inputStream) throws IOException {
        if (inputStream == null) {
            return null;
        }
        BufferedImage bufferedImage = ImageIO.read(inputStream);
        int[] nArray = bufferedImage.getRGB(0, 0, bufferedImage.getWidth(), bufferedImage.getHeight(), null, 0, bufferedImage.getWidth());
        ByteBuffer byteBuffer = ByteBuffer.allocate(4 * nArray.length);
        for (int n : nArray) {
            byteBuffer.putInt(n << 8 | n >> 24 & 0xFF);
        }
        byteBuffer.flip();
        return byteBuffer;
    }

    public static ByteBuffer[] getFavicon() {
        try {
            return new ByteBuffer[]{IconUtils.readImageToBuffer(IconUtils.class.getResourceAsStream("/assets/minecraft/more/icon_16x16.png")), IconUtils.readImageToBuffer(IconUtils.class.getResourceAsStream("/assets/minecraft/more/icon_32x32.png"))};
        }
        catch (IOException iOException) {
            iOException.printStackTrace();
            return null;
        }
    }
}

