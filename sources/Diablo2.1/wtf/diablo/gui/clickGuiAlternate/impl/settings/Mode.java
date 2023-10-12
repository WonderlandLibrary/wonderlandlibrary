package wtf.diablo.gui.clickGuiAlternate.impl.settings;

import net.minecraft.client.gui.Gui;
import wtf.diablo.gui.clickGuiAlternate.impl.Button;
import wtf.diablo.settings.Setting;
import wtf.diablo.settings.impl.ModeSetting;
import wtf.diablo.utils.font.Fonts;
import wtf.diablo.utils.render.RenderUtil;

public class Mode extends SetBase {
    public Button parent;
    public double y;

    public Mode(Setting set, Button parent) {
        this.setting = set;
        this.parent = parent;
    }
    double x;
    double width;
    public ModeSetting mode;
    int height;

    public double drawScreen(int mouseX, int mouseY, float partialTicks, double settingHeight) {
        height = 15;
        mode = (ModeSetting)setting;
        x = parent.parent.x;
        width = parent.parent.width;
        y = settingHeight + parent.y + parent.height;
        Gui.drawRect(x, y, x + width, y + height, 0x933F3F3F);
        Fonts.SFReg18.drawStringWithShadow(mode.getName(), x + 3, y + ((height - Fonts.SFReg18.getHeight()) / 2), -1);
        Fonts.SFReg18.drawStringWithShadow(mode.getMode(), x + width - 3 - Fonts.SFReg18.getStringWidth(mode.getMode()), y + ((height - Fonts.SFReg18.getHeight()) / 2), -1);
        return height;
    }
    public double getHeight(){
        return height;
    }
    public void mouseClicked(int mouseX, int mouseY, int mouseButton) {
        if(RenderUtil.isHovered(mouseX, mouseY, x, y, x + width, y + height))
            switch (mouseButton){
                case 0:
                    if(mode.getIndex() < mode.getModes().length - 1)
                        mode.setMode(mode.getIndex() + 1);
                    else
                        mode.setMode(0);
                    break;
                case 1:
                    if(mode.getIndex() > 0)
                        mode.setMode(mode.getIndex() - 1);
                    else
                        mode.setMode(mode.getModes().length - 1);

                    break;
            }
    }
    public boolean isHidden() {
        return setting.isHidden();
    }

}