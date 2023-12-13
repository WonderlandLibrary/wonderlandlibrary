package koks.api.clickgui.periodic.draw;

import koks.api.clickgui.Element;
import koks.api.font.Fonts;
import koks.api.manager.value.Value;
import koks.api.utils.Animation;
import koks.api.utils.RenderUtil;

import java.awt.*;

/**
 * @author kroko
 * @created on 13.02.2021 : 02:58
 */
public class DrawCheckBox extends Element {
    Animation animation = new Animation();

    public DrawCheckBox(Value<?> value) {
        super(Fonts.arial18);
        this.value = value;
    }

    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        final String name = value.getDisplayName() != null ? value.getDisplayName() : value.getName();
        height = (int) (fr.getStringHeight(name) + 4);
        final Color offColor = new Color(0xD0D0D0);
        final Color onColor = new Color(0x01CF0B);
        final RenderUtil renderUtil = RenderUtil.getInstance();
        renderUtil.drawRect(x,y - 5, x + 13, y+ 5, !value.castBoolean() ? offColor.getRGB() : onColor.getRGB());
        renderUtil.drawCircle(x + 13, y, 5D, !value.castBoolean() ? offColor.getRGB() : onColor.getRGB());
        renderUtil.drawCircle(x, y, 5D, !value.castBoolean() ? offColor.getRGB() : onColor.getRGB());

        if(animation.getX() == 0)
            animation.setX(x + (value.castBoolean() ? 13 : 0));

        animation.setGoalX(x + (value.castBoolean() ? 13 : 0));
        animation.setSpeed(45F);

        renderUtil.drawCircle(animation.getAnimationX(), y, 5D, Color.black.getRGB());
        renderUtil.drawCircle(animation.getAnimationX(), y, 4.5D, -1);
        fr.drawString(name, x + 23, y - fr.getStringHeight(name) / 2 + 1, value.castBoolean() ? Color.white : Color.white.darker(), true);
    }

    @Override
    public void keyTyped(char typedChar, int keyCode) {

    }

    @Override
    public void mouseClicked(int mouseX, int mouseY, int mouseButton) {
        if(mouseX >= x && mouseX <= x + 13 && mouseY >= y - 5 && mouseY <= y + 5) {
            value.castIfPossible(!value.castBoolean() + "");
        }
    }

    @Override
    public void mouseReleased(int mouseX, int mouseY, int state) {

    }
}
