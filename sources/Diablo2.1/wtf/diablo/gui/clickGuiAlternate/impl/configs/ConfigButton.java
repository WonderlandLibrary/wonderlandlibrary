package wtf.diablo.gui.clickGuiAlternate.impl.configs;

import net.minecraft.client.gui.Gui;
import wtf.diablo.utils.font.Fonts;
import wtf.diablo.utils.render.RenderUtil;

import java.awt.*;

public class ConfigButton {

    public String displayName;
    public ConfigTab parent;
    public int index, height;
    public double y;
    public int num;

    public ConfigButton(String displayName,int num, ConfigTab parent, int index) {
        this.displayName = displayName;
        this.parent = parent;
        this.num = num;
        this.index = index;
        this.height = 14;
    }

    public double drawScreen(int mouseX, int mouseY, float partialTicks, double offset) {

        y = parent.y + parent.barHeight + offset;
        boolean hover = false;
        Color bgColor = new Color(0xFF1a1a1a);

        if (RenderUtil.isHovered(mouseX, mouseY, parent.x, y, parent.x + parent.width, y + height))
            hover = true;
            //bgColor = new Color(0xFF1c1c1c);

        Gui.drawRect(parent.x, y, parent.x + parent.width, y + height, bgColor.getRGB());
        Fonts.SFReg18.drawStringWithShadow(displayName, parent.x + 3, y + ((height - Fonts.SFReg18.getHeight()) / 2f) + 0.5, hover ? -1 : 0x80FFFFFF);
        return height;
    }

    public void keyTyped(char typedChar, int keyCode) {

    }

    public boolean mouseClicked(int mouseX, int mouseY, int mouseButton) {
        if (RenderUtil.isHovered(mouseX, mouseY, parent.x, y, parent.x + parent.width, y + height))
            return mouseButton == 0;
        return false;
    }

    public void mouseReleased(int mouseX, int mouseY, int state) {

    }

    public double getHeight() {
        return height;
    }

    public void onOpenGUI() {
    }

}
