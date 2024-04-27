/*
 * Decompiled with CFR 0.152.
 */
package net.ccbluex.liquidbounce.utils.render;

import java.awt.image.BufferedImage;
import java.nio.ByteBuffer;

public class ImageUtils {
    public static ByteBuffer readImageToBuffer(BufferedImage bufferedImage) {
        int[] nArray = bufferedImage.getRGB(0, 0, bufferedImage.getWidth(), bufferedImage.getHeight(), null, 0, bufferedImage.getWidth());
        ByteBuffer byteBuffer = ByteBuffer.allocateDirect(4 * nArray.length);
        for (int n : nArray) {
            byteBuffer.putInt(n << 8 | n >> 24 & 0xFF);
        }
        byteBuffer.flip();
        return byteBuffer;
    }

    public static BufferedImage resizeImage(BufferedImage bufferedImage, int n, int n2) {
        BufferedImage bufferedImage2 = new BufferedImage(n, n2, 6);
        bufferedImage2.getGraphics().drawImage(bufferedImage.getScaledInstance(n, n2, 4), 0, 0, null);
        return bufferedImage2;
    }
}

