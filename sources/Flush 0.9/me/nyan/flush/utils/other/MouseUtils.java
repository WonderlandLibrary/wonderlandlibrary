package me.nyan.flush.utils.other;

public class MouseUtils {
    public static boolean hovered(int mouseX, int mouseY, double x, double y, double right, double bottom) {
        if (mouseX < 0 || mouseY < 0) {
            return false;
        }
        return mouseX >= x && mouseX <= right && mouseY >= y && mouseY <= bottom;
    }
}
