/*
 * Decompiled with CFR 0.152.
 */
package ad;

public class HoveringUtil {
    public static boolean isHovering(float x, float y, float width, float height, int mouseX, int mouseY) {
        return (float)mouseX >= x && (float)mouseY >= y && (float)mouseX < x + width && (float)mouseY < y + height;
    }
}

