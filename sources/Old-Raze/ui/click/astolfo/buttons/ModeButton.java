package markgg.ui.click.astolfo.buttons;

import markgg.settings.ModeSetting;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;

import java.awt.*;

public class ModeButton extends Button{
    public ModeSetting setting;
    public Color color;

    public ModeButton(float x, float y, float width, float height, ModeSetting setting, Color color) {
        super(x, y, width, height);
        this.setting = setting;
        this.color = color;
    }

    @Override
    public void draw(int mx, int my) {
        Gui.drawRect(x, y, x + width, y +height, 0xff181A17);
        Minecraft.getMinecraft().fontRendererObj.drawHeightCenteredString(setting.name, x + 4, y +height / 2, -1);
        Minecraft.getMinecraft().fontRendererObj.drawHeightCenteredString(setting.getMode(), x + 4 + 93 - Minecraft.getMinecraft().fontRendererObj.getStringWidth(setting.getMode()), y +height / 2, -1);
    }

    @Override
    public void action(int x, int y, boolean click, int button) {
        if(isHovered(x, y) && click) {
            if(button == 0)
                setting.cycle();
            else if(button == 1)
                setting.invertCycle();
        }
    }

    @Override
    public void key(char typedChar, int key) { }
}
