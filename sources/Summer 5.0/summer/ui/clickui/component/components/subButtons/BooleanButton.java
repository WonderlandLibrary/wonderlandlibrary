package summer.ui.clickui.component.components.subButtons;

import java.awt.Color;

import summer.Summer;
import summer.cheat.cheats.render.ClickGUI;
import summer.cheat.guiutil.Setting;
import summer.ui.clickui.component.Parentable;
import summer.ui.clickui.panel.panels.CheatTypePanel;
import summer.ui.clickuiutils.AnimationUtil;
import summer.ui.clickuiutils.ColorUtil;
import summer.ui.clickuiutils.RenderUtil;

/**
 * @author: AmirCC
 * 10:41 am, 11/10/2020, Wednesday
 **/
public class BooleanButton extends Parentable<Setting, CheatTypePanel> {

    //animation values
    private float textHoverAnimate = 0f;
    private float leftRectAnimation = 0f;
    private float rightRectAnimation = 0f;

    public BooleanButton(Setting object, CheatTypePanel parent, float staticX, float staticY, float width, float height, float offsetX, float offsetY) {
        super(object, parent, staticX, staticY, width, height, offsetX, offsetY);
    }

    @Override
    public void updatePosition(float x, float y) {
        setX(x + getOffsetX());
        setY(y + getOffsetY());
    }

    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        boolean hover = isHovered(getX(), getY(), getX() + getWidth(), getY() + getHeight());
        textHoverAnimate = AnimationUtil.moveUD(textHoverAnimate, hover ? 4 : 2, 0.000542f);
        leftRectAnimation = AnimationUtil.moveUD(leftRectAnimation, getObject().getValBoolean() ? 10 : 17, 0.000542f);
        rightRectAnimation = AnimationUtil.moveUD(rightRectAnimation, (getObject().getValBoolean() ? 3 : 10), 0.000542f);
        RenderUtil.INSTANCE.drawRect(getX() + getWidth() - 18, getY() + 2, getX() + getWidth() - 2, getY() + getHeight() - 2, new Color(14, 14, 14).getRGB());
        Color c = Color.getHSBColor(ClickGUI.guiHue.getValFloat(), 1.0F, 0.65F);
        if (ClickGUI.rainbowGui.getValBoolean()) {
            RenderUtil.INSTANCE.drawRect(getX() + getWidth() - leftRectAnimation, getY() + 3, getX() + getWidth() - rightRectAnimation, getY() + getHeight() - 3, getObject().getValBoolean() ? /*new Color(145,145,145).getRGB()*/ ColorUtil.getRainbow(17000, -15 * (int) getY()) : new Color(50, 50, 50).getRGB());
        } else RenderUtil.INSTANCE.drawRect(getX() + getWidth() - leftRectAnimation, getY() + 3, getX() + getWidth() - rightRectAnimation, getY() + getHeight() - 3, getObject().getValBoolean() ? /*new Color(145,145,145).getRGB()*/ c.getRGB() : new Color(50, 50, 50).getRGB());
        Summer.INSTANCE.fontManager.getFont("ROBO 16").drawStringWithShadow(getObject().getName(), getX() + textHoverAnimate, getY() + 1, -1);

        super.drawScreen(mouseX, mouseY, partialTicks);
    }

    @Override
    public void mouseClicked(int mouseX, int mouseY, int mouseButton) {
        if (isHovered(getX(), getY(), getX() + getWidth(), getY() + getHeight())) {
            if (mouseButton == 0) {
                getObject().setValBoolean(!getObject().getValBoolean());
            }
        }
        super.mouseClicked(mouseX, mouseY, mouseButton);
    }

    @Override
    public boolean isVisible() {
        return getObject().isVisible();
    }
}
