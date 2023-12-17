// 
// Decompiled by Procyon v0.5.36
// 

package ru.tuskevich.util.math;

public class HoveringUtil
{
    public static boolean isHovering(final float x, final float y, final float width, final float height, final int mouseX, final int mouseY) {
        return mouseX >= x && mouseY >= y && mouseX < x + width && mouseY < y + height;
    }
}
