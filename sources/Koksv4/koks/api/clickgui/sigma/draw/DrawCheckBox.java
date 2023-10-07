package koks.api.clickgui.sigma.draw;

import koks.api.clickgui.Element;
import koks.api.font.Fonts;
import koks.api.manager.value.Value;
import koks.api.manager.value.ValueManager;
import koks.api.registry.module.ModuleRegistry;
import koks.api.utils.Animation;
import koks.api.utils.RenderUtil;
import koks.module.gui.ClickGUI;
import net.minecraft.util.MathHelper;

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
        animation.setX(x + width + (value.castBoolean() ? 13 : 0));
    }

    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        height = 20;
        final String text = value.getDisplayName() != null ? value.getDisplayName() : value.getName();
        final RenderUtil renderUtil = RenderUtil.getInstance();
        final Color offBackground = new Color(105,105,105,255);
        final ClickGUI clickGUI = ModuleRegistry.getModule(ClickGUI.class);
        final Color customColor = new Color(ValueManager.getInstance().getValue("Color", clickGUI).castInteger());
        final Color off = new Color(150,150,150,255);
        final Color onBackground = customColor.darker();

        renderUtil.drawRect(x + width,y - 5, x + width + 13, y+ 5, !value.castBoolean() ? offBackground.getRGB() : onBackground.getRGB());
        renderUtil.drawCircle(x + width + 13, y, 5D, !value.castBoolean() ? offBackground.getRGB() : onBackground.getRGB());
        renderUtil.drawCircle(x + width, y, 5D, !value.castBoolean() ? offBackground.getRGB() : onBackground.getRGB());

        animation.setGoalX(x + width + (value.castBoolean() ? 13 : 0));
        animation.setSpeed(55F);

        renderUtil.drawCircle(MathHelper.clamp_double(animation.getAnimationX(), x + width, x + width + 13), y, 6D, value.castBoolean() ? customColor.getRGB() : off.getRGB());
        fr.drawString(text, x + 40, y - fr.getStringHeight(value.getDisplayName() != null ? value.getDisplayName() : value.getName()) / 2 + 1, Color.white, true);
    }

    @Override
    public void keyTyped(char typedChar, int keyCode) {

    }

    @Override
    public void mouseClicked(int mouseX, int mouseY, int mouseButton) {
        if(value == null || fr == null) return;
        if((mouseX >= x + width - 5 && mouseX <= x + width + 18 && mouseY >= y - 5 && mouseY <= y + 5) || (mouseX >= x + 40 && mouseX <= x + 40 + fr.getStringWidth(value.getDisplayName() != null ? value.getDisplayName() : value.getName()) && mouseY >= y - 5 && mouseY <= y + 5)) {
            value.castIfPossible(!value.castBoolean() + "");
        }
    }

    @Override
    public void mouseReleased(int mouseX, int mouseY, int state) {

    }
}
