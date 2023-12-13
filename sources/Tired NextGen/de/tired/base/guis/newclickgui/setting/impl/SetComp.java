package de.tired.base.guis.newclickgui.setting.impl;

import de.tired.base.guis.newclickgui.setting.Setting;

public class SetComp {

    Setting setting;

    public int offset;

    public SetComp(Setting setting) {
        this.setting = setting;
    }

    public void renderComponent() {
//		offset = (int) RenderHelper.linearAnimation(offset, targetOffset, 2);
    }

    public void updateComponent(int mouseX, int mouseY) {

    }

    public void mouseClicked(int mouseX, int mouseY, int button) {

    }

    public void mouseReleased(int mouseX, int mouseY, int mouseButton) {
    }

    public int getParentHeight() {
        return 0;
    }

    public void keyTyped(char typedChar, int key) {

    }

    public void setOff(int newOff) {
        this.offset = newOff;
    }

    public int getHeight() {
        return 12;
    }

    public Setting getSetting() {
        return setting;
    }
}
