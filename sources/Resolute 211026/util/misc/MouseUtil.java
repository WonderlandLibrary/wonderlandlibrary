// 
// Decompiled by Procyon v0.5.36
// 

package vip.Resolute.util.misc;

public class MouseUtil
{
    public static boolean mouseWithinBounds(final int mouseX, final int mouseY, final double x, final double y, final double width, final double height) {
        return mouseX >= x && mouseX <= x + width && mouseY >= y && mouseY <= y + height;
    }
}
