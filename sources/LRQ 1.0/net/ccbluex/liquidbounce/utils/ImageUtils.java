/*
 * Decompiled with CFR 0.152.
 */
package net.ccbluex.liquidbounce.utils;

import java.awt.image.BufferedImage;
import java.nio.ByteBuffer;

public class ImageUtils {
    public static ByteBuffer readImageToBuffer(BufferedImage bufferedImage) {
        int[] rgbArray = bufferedImage.getRGB(0, 0, bufferedImage.getWidth(), bufferedImage.getHeight(), null, 0, bufferedImage.getWidth());
        ByteBuffer byteBuffer = ByteBuffer.allocateDirect(4 * rgbArray.length);
        for (int rgb : rgbArray) {
            byteBuffer.putInt(rgb << 8 | rgb >> 24 & 0xFF);
        }
        byteBuffer.flip();
        return byteBuffer;
    }

    public static BufferedImage resizeImage(BufferedImage image2, int width, int height) {
        BufferedImage buffImg = new BufferedImage(width, height, 6);
        buffImg.getGraphics().drawImage(image2.getScaledInstance(width, height, 4), 0, 0, null);
        return buffImg;
    }
}

