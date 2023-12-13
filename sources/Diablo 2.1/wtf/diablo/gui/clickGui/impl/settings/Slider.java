package wtf.diablo.gui.clickGui.impl.settings;

import net.minecraft.client.gui.Gui;
import wtf.diablo.gui.clickGui.impl.Button;
import wtf.diablo.settings.Setting;
import wtf.diablo.settings.impl.ModeSetting;
import wtf.diablo.settings.impl.NumberSetting;
import wtf.diablo.utils.font.Fonts;
import wtf.diablo.utils.render.ColorUtil;
import wtf.diablo.utils.render.RenderUtil;

public class Slider extends SetBase {

    public Button parent;
    public double y;

    public Slider(Setting set, Button parent) {
        this.setting = set;
        this.parent = parent;
    }
    double x;
    double width;
    NumberSetting mode;
    boolean sliding;
    int height;

    public double drawScreen(int mouseX, int mouseY, float partialTicks, double settingHeight) {
        //if(sliding && !RenderUtil.isHovered(mouseX, mouseY, x, y, x + width, y + height))
        //    sliding = false;

        height = 12;
        mode = (NumberSetting) setting;
        x = parent.parent.x;
        width = parent.parent.width;
        y = settingHeight + parent.y + parent.height;

        if(sliding){
            double getValue = mouseX - (x);
            if(mode.getMin() + ((mode.getMax() - mode.getMin()) / width) * getValue < mode.getMax() &&
                    mode.getMin() + ((mode.getMax() - mode.getMin()) / width) * getValue > mode.getMin())
            mode.setValue(mode.getMin() + ((mode.getMax() - mode.getMin()) / width) * getValue);
        }

        Gui.drawRect(x, y, x + width, y + height, 0xFF252525);
        RenderUtil.drawRect(x, y, x + (((mode.getValue() - mode.getMin()) / (mode.getMax() - mode.getMin())) * width), y + height, this.parent.parent.color.darker().getRGB());
        Fonts.SFReg18.drawStringWithShadow(mode.getName(), x + 3, y + ((height - Fonts.SFReg18.getHeight()) / 2) + 0.5, -1);
        Fonts.SFReg18.drawStringWithShadow(Math.floor(mode.getValue() * 1000) / 1000 + "", x + width - 3 - Fonts.SFReg18.getStringWidth(String.valueOf(Math.floor(mode.getValue() * 1000) / 1000)), y + ((height - Fonts.SFReg18.getHeight()) / 2) + 0.5, -1);
        return height;
    }

    public double getHeight(){
        return height;
    }

    public void mouseClicked(int mouseX, int mouseY, int mouseButton) {
        if(RenderUtil.isHovered(mouseX, mouseY, x, y, x + width, y + height)) {
            sliding = true;
        }
    }

    @Override
    public void mouseReleased(int mouseX, int mouseY, int state) {
        sliding = false;
    }

    public boolean isHidden() {
        return setting.isHidden();
    }
}
