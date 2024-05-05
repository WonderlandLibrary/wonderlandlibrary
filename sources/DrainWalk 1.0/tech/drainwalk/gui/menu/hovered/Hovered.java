package tech.drainwalk.gui.menu.hovered;

public class Hovered {
    public static boolean isHovered(int mX, int mY, float x, float y, float width, float height) {
        return (mX > x && mX < (x + width)) && (mY > y && mY < (y + height));
    }
}
