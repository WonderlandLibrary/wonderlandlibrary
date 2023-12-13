package summer.base.utilities;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.ScaledResolution;
import org.lwjgl.input.Mouse;

public class MouseUtil {

    private static ScaledResolution sr() {
        return new ScaledResolution(Minecraft.getMinecraft());
    }

    public static int getMouseX() {
        return Mouse.getX() * sr().getScaledWidth() / Minecraft.getMinecraft().displayWidth;
    }

    public static int getMouseY() {
        return sr().getScaledHeight() - Mouse.getY() * sr().getScaledHeight() / Minecraft.getMinecraft().displayHeight - 1;
    }

    public static boolean isHovered(float x, float y, float width, float height, int mouseX, int mouseY){
        return mouseX > x && mouseX < width && mouseY > y && mouseY < height;
    }

    public static boolean isHovered(float x, float y, float width, float height) {
        return getMouseX() > x && getMouseX() < width && getMouseY() > y && getMouseY() < height;
    }

    public static boolean isHoveredWH(float x, float y, float width, float height) {
        return getMouseX() > x && getMouseX() < x + width && getMouseY() > y && getMouseY() < y + height;
    }

    public boolean isHoveredCircle(final int x, final int y, int x1, int y1, int radius, int thickness) {
        return x >= x1 - radius - thickness && y >= y1 - radius - thickness && x <= x1 + radius + thickness && y <= y1 + radius + thickness;
    }

}
