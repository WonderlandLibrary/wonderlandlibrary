package koks.api.clickgui.periodic.draw;

import koks.api.clickgui.Element;
import koks.api.font.Fonts;
import koks.api.manager.value.Value;
import koks.api.utils.Animation;
import koks.api.utils.RenderUtil;
import koks.api.utils.TimeHelper;
import org.lwjgl.opengl.GL11;

import java.awt.*;

/**
 * @author kroko
 * @created on 13.02.2021 : 02:54
 */
public class DrawComboBox extends Element {

    public Animation animation = new Animation();

    public TimeHelper timeHelper = new TimeHelper();

    public DrawComboBox(Value<?> value) {
        super(Fonts.arial18);
        this.value = value;
    }

    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        final String text = (value.getDisplayName() != null ? value.getDisplayName() : value.getName()) + ": " + value.castString();
        height = (int) (fr.getStringHeight(text) + 10 + (extended ? (fr.getStringHeight(text) + 2) * value.getModes().size() : 0) + Math.abs(animation.getAnimationY() - y) / (y * 0.5));
        final RenderUtil renderUtil = RenderUtil.getInstance();
        if (!value.hasValidMode()) {
            value.setValidMode();
        }
        renderUtil.drawOutlineRect(x - 3, y, x - 3 + width, y + (fr.getStringHeight(text) + 2) * (extended ? value.getModes().size() + 1 : 1), 1, value.getModule().getCategory().getCategoryColor().getRGB(), new Color(16, 16, 16).getRGB());
        fr.drawString(text, x + width / 2F - fr.getStringWidth(text) / 2 - 3, y + 2, Color.white, true);
        GL11.glPushMatrix();
        fr.drawString(">", x - 3 + width - 7, y + fr.getStringHeight(text) / 2 - 3.5F, Color.white, true);
        fr.drawString("<", x, y + fr.getStringHeight(text) / 2 - 3.5F, Color.white, true);
        GL11.glPopMatrix();
    }

    @Override
    public void keyTyped(char typedChar, int keyCode) {

    }

    @Override
    public void mouseClicked(int mouseX, int mouseY, int mouseButton) {
        final String text = (value.getDisplayName() != null ? value.getDisplayName() : value.getName()) + ": " + value.castString();
        if (mouseButton == 0) {
            if (mouseX >= x + width / 2 && mouseX <= x + width && mouseY >= y + fr.getStringHeight(text) / 2 - 2 && mouseY <= y + fr.getStringHeight(text) / 2 - 2 + fr.getStringHeight(text)) {
                for (int i = 0; i < value.getModes().size(); i++) {
                    if (value.getModes().get(i).equalsIgnoreCase(value.castString())) {
                        int toSet = i + 1;
                        if(toSet == value.getModes().size())
                            toSet = 0;
                        value.castIfPossible(value.getModes().get(toSet));
                        break;
                    }
                }
            } else if (mouseX <= x + width / 2 && mouseX >= x && mouseY >= y + fr.getStringHeight(text) / 2 - 2 && mouseY <= y + fr.getStringHeight(text) / 2 - 2 + fr.getStringHeight(text)) {
                for (int i = 0; i < value.getModes().size(); i++) {
                    if (value.getModes().get(i).equalsIgnoreCase(value.castString())) {
                        int toSet = i;
                        if(toSet - 1 == -1)
                            toSet = value.getModes().size();
                        value.castIfPossible(value.getModes().get(toSet - 1));
                        break;
                    }
                }
            }
        }
    }

    @Override
    public void mouseReleased(int mouseX, int mouseY, int state) {

    }

}
