package net.optifine;

import java.awt.*;
import java.awt.image.BufferedImage;

/**
 * @author prplz
 */
public class ResourcePackImageScaler {

    public static final int SIZE = 64;

    private ResourcePackImageScaler() {
        throw new java.lang.UnsupportedOperationException("This is a utility class and cannot be instantiated");
    }

    public static BufferedImage scalePackImage(final BufferedImage image) {
        if (image == null) {
            return null;
        }

        // System.out.println("Scaling resource pack icon from " + image.getWidth() + " to " + 64);

        final BufferedImage smallImage = new BufferedImage(64, 64, 2);
        final Graphics graphics = smallImage.getGraphics();
        graphics.drawImage(image, 0, 0, 64, 64, null);
        graphics.dispose();

        return smallImage;
    }

}
