package summer.ui.clickuiutils;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.ScaledResolution;
import org.lwjgl.input.Mouse;

/**
 * @author: AmirCC
 * 03:43 pm, 10/10/2020, Tuesday
 **/
public enum MouseUtil {
    INSTANCE;

    private ScaledResolution sr() {
        return new ScaledResolution(Minecraft.getMinecraft());
    }

    public int getMouseX() {
        return Mouse.getX() * sr().getScaledWidth() / Minecraft.getMinecraft().displayWidth;
    }

    public int getMouseY() {
        return sr().getScaledHeight() - Mouse.getY() * sr().getScaledHeight() / Minecraft.getMinecraft().displayHeight - 1;
    }

    public boolean isHovered(float x, float y, float width, float height) {
        return getMouseX() > x && getMouseX() < width && getMouseY() > y && getMouseY() < height;
    }

}
