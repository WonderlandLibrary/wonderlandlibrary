package org.dreamcore.client.ui.newclickgui.settings;

import org.dreamcore.client.helpers.Helper;
import org.dreamcore.client.settings.Setting;
import org.dreamcore.client.ui.newclickgui.FeaturePanel;

public class Component implements Helper {

    public FeaturePanel featurePanel;
    public Setting setting;
    public float x;
    public float y;
    public float width;
    public float height;

    public boolean extended;

    public void drawScreen(int mouseX, int mouseY) {

    }

    public void setInformations(float x, float y, float width, float height) {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
    }

    public void mouseClicked(int mouseX, int mouseY, int mouseButton) {
    }

    public void keyTyped(char chars, int key) {

    }

    public void mouseReleased(int mouseX, int mouseY, int state) {
    }

    public boolean isSettingVisible() {
        return setting.isVisible();
    }

    public boolean isHovered(int mouseX, int mouseY) {
        return mouseX >= x && mouseX <= x + width && mouseY >= y && mouseY <= y + height;
    }
}
