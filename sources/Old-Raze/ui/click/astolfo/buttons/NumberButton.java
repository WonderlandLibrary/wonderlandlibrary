package markgg.ui.click.astolfo.buttons;

import markgg.settings.NumberSetting;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.util.MathHelper;

import java.awt.*;

public class NumberButton extends Button{
    public NumberSetting setting;
    public Color color;

    public boolean dragged;

    public NumberButton(float x, float y, float width, float height, NumberSetting setting, Color color) {
        super(x, y, width, height);
        this.setting = setting;
        this.color = color;
    }

    @Override
    public void draw(int x, int y) {
        double diff = setting.maximum - setting.minimum;

        double widthPrec = (setting.getValue() - setting.minimum) / (setting.getMaximum() - setting.getMinimum());

        int handleWidth = 3;

        if(dragged) {
            double value = setting.getMinimum() + MathHelper.clamp_double((double)(x - this.x - handleWidth / 2) / (width - handleWidth), 0, 1) * diff;
            setting.setValue(Math.round(value * 100D) / 100D);
        }

        Gui.drawRect(this.x, this.y, this.x + width, this.y + height, 0xff181A17);
        Gui.drawRect(this.x + 1, this.y, this.x + (int)(widthPrec * (width - 2)) + 1, this.y + height, color.getRGB());

        
        Minecraft.getMinecraft().fontRendererObj.drawHeightCenteredString(setting.name + ": " + Math.round(setting.getValue() * 100D) / 100D, this.x + 4, this.y + height / 2, -1);
    }


    @Override
    public void action(int x, int y, boolean click, int button) {
        if(isHovered(x, y)){
            dragged = true;
        }

        if(!click)
            dragged = false;
    }

    @Override
    public void key(char typedChar, int key) {

    }
}
