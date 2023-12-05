package koks.gui.clickgui.periodic.settings;

import koks.api.settings.Setting;
import koks.api.util.Animation;
import koks.gui.clickgui.Element;
import koks.api.interfaces.Wrapper;

import java.awt.*;

/**
 * @author kroko
 * @created on 12.11.2020 : 22:42
 */
public class DrawCheckBox extends Element implements Wrapper {

    Animation animation = new Animation();

    public DrawCheckBox(Setting setting) {
        this.setting = setting;
    }

    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        renderUtil.drawRect(x,y - 5, x + 13, y+ 5, !setting.isToggled() ? Color.darkGray.getRGB() : -1);
        renderUtil.drawCircle(x + 13, y, 5D, !setting.isToggled() ? Color.darkGray.getRGB() : -1);
        renderUtil.drawCircle(x, y, 5D, !setting.isToggled() ? Color.darkGray.getRGB() : -1);

        if(animation.getX() == 0)
            animation.setX(x + (setting.isToggled() ? 13 : 0));

        animation.setGoalX(x + (setting.isToggled() ? 13 : 0));
        animation.setSpeed(45F);

        renderUtil.drawCircle(animation.getAnimationX(), y, 5D, setting.isToggled() ? Color.green.getRGB() : Color.red.getRGB());
        fr.drawString(setting.getName(), x + 23, y - fr.FONT_HEIGHT / 2 + 1, -1);
    }

    @Override
    public void keyTyped(char typedChar, int keyCode) {

    }

    @Override
    public void mouseClicked(int mouseX, int mouseY, int mouseButton) {
        if(mouseX >= x && mouseX <= x + 13 && mouseY >= y - 5 && mouseY <= y + 5) {
            setting.setToggled(!setting.isToggled());
        }
    }

    @Override
    public void mouseReleased(int mouseX, int mouseY, int state) {

    }
}
