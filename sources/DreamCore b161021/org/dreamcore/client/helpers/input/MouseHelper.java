package org.dreamcore.client.helpers.input;

import org.dreamcore.client.helpers.Helper;

public class MouseHelper implements Helper {

    public static boolean isHovered(double x, double y, double mouseX, double mouseY, int width, int height) {
        return width > x && height > y && width < mouseX && height < mouseY;
    }
}
