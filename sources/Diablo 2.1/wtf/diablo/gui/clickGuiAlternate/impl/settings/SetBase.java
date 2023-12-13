package wtf.diablo.gui.clickGuiAlternate.impl.settings;

import wtf.diablo.settings.Setting;

public class SetBase {
    public String name;
    public Setting setting;

    public double drawScreen(int mouseX, int mouseY, float partialTicks, double settingHeight) {
        return 0;
    }


    public void mouseClicked(int mouseX, int mouseY, int mouseButton) {
    }

    public void mouseReleased(int mouseX, int mouseY, int state) {
    }

    public void keyTyped(char typedChar, int keyCode) {
    }

    public boolean isHidden(){
        return true;
    }

    public double getHeight() {
        return 0;
    }

    public boolean canRender() {
        if(setting.hasParent()){
            if(setting.parentCheck()){
                return true;
            }else {
                return false;
            }
        }
        if(!setting.isHidden())
            return true;
        return false;
    }
}
