package markgg.ui.click.astolfo.buttons;

import markgg.settings.BooleanSetting;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;

import java.awt.*;

public class BooleanButton extends Button{
    public BooleanSetting setting;
    public Color color;

    public BooleanButton(float x, float y, float width, float height, BooleanSetting setting, Color color) {
        super(x, y, width, height);
        this.setting = setting;
        this.color = color;
    }

    @Override
    public void draw(int mx, int my) {
        Gui.drawRect(x, y, x + width, y + height, 0xff181A17);
        if(setting.getValue()) {
            Gui.drawRect(x + 3, y, x + width - 3, y + height, color.getRGB());
        }
        Minecraft.getMinecraft().fontRendererObj.drawHeightCenteredString(setting.name, x + 4, y + height / 2, -1);
    }

    @Override
    public void action(int x, int y, boolean click, int button) {
        if(isHovered(x, y) && click) {
            setting.toggle();
        }
    }

    @Override
    public void key(char typedChar, int key) {

    }
}
