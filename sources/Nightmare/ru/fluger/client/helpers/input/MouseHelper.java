// 
// Decompiled by Procyon v0.5.36
// 

package ru.fluger.client.helpers.input;

import ru.fluger.client.helpers.Helper;

public class MouseHelper implements Helper
{
    public static boolean isHovered(final double x, final double y, final double mouseX, final double mouseY, final int width, final int height) {
        return width > x && height > y && width < mouseX && height < mouseY;
    }
}
