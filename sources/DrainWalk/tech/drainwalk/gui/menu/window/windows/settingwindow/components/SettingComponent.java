package tech.drainwalk.gui.menu.window.windows.settingwindow.components;

import lombok.Getter;
import lombok.Setter;
import tech.drainwalk.animation.Animation;
import tech.drainwalk.client.option.Option;
import tech.drainwalk.gui.menu.window.windows.settingwindow.SettingWindow;
import tech.drainwalk.client.module.Module;

public class SettingComponent {
    @Setter
    @Getter
    protected float x, y;
    protected SettingWindow parent;
    @Getter
    protected Module module;
    @Getter
    protected float offsetSize = 14;
    @Getter
    protected final Animation hoveredAnimation = new Animation();
    @Getter
    protected final Animation visibleAnimation = new Animation();
    public SettingComponent(float x, float y, Module module, SettingWindow parent) {
        this.x = x;
        this.y = y;
        this.parent = parent;
        this.module = module;
    }

    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
    }

    public void mouseClicked(int mouseX, int mouseY, int mouseButton) {
    }

    public void mouseReleased(int mouseX, int mouseY, int state) {

    }
    public void updateScreen(int mouseX, int mouseY) {
    }
}
