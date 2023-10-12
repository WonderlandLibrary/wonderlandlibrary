package wtf.diablo.gui.clickGui.impl.configs;

import net.minecraft.client.gui.Gui;
import wtf.diablo.Diablo;
import wtf.diablo.config.Config;
import wtf.diablo.gui.clickGuiAlternate.impl.configs.ConfigTab;
import wtf.diablo.utils.font.Fonts;
import wtf.diablo.utils.render.RenderUtil;

import java.awt.*;

public class ConfigSelect {

    public Config config;
    public ConfigTab parent;
    public int index, height;
    public double y;
    public boolean selected = false;

    public ConfigSelect(ConfigTab parent, Config config, int index) {
        this.parent = parent;
        this.config = config;
        this.index = index;
        this.height = 14;
    }

    public double drawScreen(int mouseX, int mouseY, float partialTicks, double offset) {

        boolean hovered = false;
        y = parent.y + parent.barHeight + offset;

        Color bgColor = new Color(0xFF1a1a1a);

        if (RenderUtil.isHovered(mouseX, mouseY, parent.x, y, parent.x + parent.width, y + height))
            hovered = true;
           // bgColor = new Color(0xFF1c1c1c);

        Gui.drawRect(parent.x, y, parent.x + parent.width, y + height, bgColor.getRGB());
        Fonts.SFReg18.drawStringWithShadow(config.getName(), parent.x + (parent.width / 2f) - Fonts.SFReg18.getStringWidth(config.getName()) /2f, y + ((height - Fonts.SFReg18.getHeight()) / 2f) + 0.5,selected ? 0xffe2b0ff : hovered ? -1 : 0x80FFFFFF);
        return height;
    }

    public void keyTyped(char typedChar, int keyCode) {

    }

    public void mouseClicked(int mouseX, int mouseY, int mouseButton) {
        if (RenderUtil.isHovered(mouseX, mouseY, parent.x, y, parent.x + parent.width, y + height)) {
            switch (mouseButton) {
                case 0:
                    if(selected)
                        Diablo.configManager.loadConfig(config.getName(),false);

                    selected = true;

                    break;
            }
        }else {
            selected = false;
        }
    }

    public void mouseReleased(int mouseX, int mouseY, int state) {

    }

    public double getHeight() {
        return height;
    }

    public void onOpenGUI() {
    }

}
