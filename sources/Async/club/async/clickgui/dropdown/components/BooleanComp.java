package club.async.clickgui.dropdown.components;

import club.async.Async;
import club.async.clickgui.dropdown.Component;
import club.async.clickgui.dropdown.MButton;
import club.async.module.setting.impl.BooleanSetting;
import club.async.util.ColorUtil;
import net.minecraft.client.gui.Gui;

import java.awt.*;

public class BooleanComp extends Component {
    BooleanSetting setting;
    public BooleanComp(BooleanSetting setting, int offset, MButton parent) {
        super(setting, offset, parent);
        this.setting = setting;
    }

    @Override
    public void renderComponent(int mouseX, int mouseY) {
        super.renderComponent(mouseX, mouseY);
        Gui.drawRect(x2 - 130, offset - parent.scrollOffset, x2 - 5, offset + 20 - parent.scrollOffset, setting.get() ? ColorUtil.getMainColor() : new Color(20,20,20));
        Async.INSTANCE.getFontManager().getFont("Arial 23").drawString(setting.getName(),x2 - 125, offset + 5 - parent.scrollOffset, new Color(255,255,255,180));
    }

    @Override
    public void mouseClicked(int mouseX, int mouseY, int mouseButton) {
        if (isInside(mouseX, mouseY, x2 - 130, offset - parent.scrollOffset,125, 20) && (mouseButton == 0 || mouseButton == 1))
            setting.toggle();

    }

}
