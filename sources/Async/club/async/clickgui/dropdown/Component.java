package club.async.clickgui.dropdown;

import club.async.module.setting.Setting;
import club.async.util.RenderUtil;

public class Component {

    protected Setting setting;
    protected int offset;
    protected final double x = RenderUtil.getScaledResolution().getScaledWidth() / 2 - 150;
    protected final double x2 = RenderUtil.getScaledResolution().getScaledWidth() / 2 + 150;
    protected MButton parent;

    public Component(Setting setting, int offset, MButton parent) {
        this.setting = setting;
        this.offset = offset;
        this.parent = parent;
    }

    public void renderComponent(int mouseX, int mouseY) {
    }

    public void mouseClicked(int mouseX, int mouseY, int mouseButton) {
    }

    public void mouseReleased(int mouseX, int mouseY) {
    }

    protected boolean isInside(int mouseX, int mouseY, double x, double y, double width, double height) {
        return (mouseX >= x && mouseX <= x + width) && (mouseY >= y && mouseY <= y + height);
    }

}
