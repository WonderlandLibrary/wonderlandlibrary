package club.async.clickgui.flat.components;

import club.async.clickgui.flat.ClickGUI;
import club.async.module.setting.Setting;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.ScaledResolution;
import java.io.IOException;

public class Component {

    public Setting sParent;
    protected ClickGUI parent;
    protected double width, offset;

    public void drawScreen(int mouseX, int mouseY, float partialTicks) {

    }

    public void keyTyped(char typedChar, int keyCode) throws IOException {

    }

    public void mouseClicked(int mouseX, int mouseY, int mouseButton) throws IOException {

    }

    public void mouseReleased(int mouseX, int mouseY, int state) {

    }

    public void updateScreen(int mouseX, int mouseY) {
        if (parent.width != 420)
            parent.width = 420;
    }

    public final ScaledResolution getScaledResolution() {
        return new ScaledResolution(Minecraft.getMinecraft());
    }

    public final boolean isInside(int mouseX, int mouseY, double x, double y, double width, double height) {
        return (mouseX > x && mouseX < x + width) && (mouseY > y && mouseY < y + height);
    }

}
