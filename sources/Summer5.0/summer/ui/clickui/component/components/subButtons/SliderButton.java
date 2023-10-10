package summer.ui.clickui.component.components.subButtons;

import java.awt.Color;
import java.math.BigDecimal;
import java.math.RoundingMode;

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
 * 01:49 pm, 11/10/2020, Wednesday
 **/

public class SliderButton extends Parentable<Setting, CheatTypePanel> {

    //animation values
    private float textHoverAnimate = 0f;
    private float currentValueAnimate = 0f;

    //others
    private boolean sliding;

    public SliderButton(Setting object, CheatTypePanel parent, float staticX, float staticY, float width, float height, float offsetX, float offsetY) {
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

        String currentValue = null;
        if (getObject().onlyInt()) {
            currentValue = String.valueOf(getObject().getValInt());
        } else {
            currentValue = String.format("%.2f", getObject().getValDouble());
        }
        final double amountWidth = ((getObject().getValDouble()) - getObject().getMin())
                / (getObject().getMax() - getObject().getMin());

        textHoverAnimate = AnimationUtil.moveUD(textHoverAnimate, hover || isSliding() ? 4 : 2, 0.000542f);
        currentValueAnimate = AnimationUtil.moveUD(currentValueAnimate, (float) amountWidth, 0.000542f);
        Color c = Color.getHSBColor(ClickGUI.guiHue.getValFloat(), 1.0F, 0.65F);
        if (ClickGUI.rainbowGui.getValBoolean()) {
            RenderUtil.INSTANCE.drawRect(getX(), getY(), getX() + (getWidth() * currentValueAnimate), getY() + getHeight(), /*new Color(145, 145, 145).getRGB()*/ ColorUtil.getRainbow(17000, -15 * (int) getY()));
        } else RenderUtil.INSTANCE.drawRect(getX(), getY(), getX() + (getWidth() * currentValueAnimate), getY() + getHeight(), /*new Color(145, 145, 145).getRGB()*/ c.getRGB());
        Summer.INSTANCE.fontManager.getFont("ROBO 16").drawStringWithShadow(getObject().getName(), getX() + textHoverAnimate, getY() + 1, -1);
       Summer.INSTANCE.fontManager.getFont("ROBO 15").drawStringWithShadow(currentValue, getX() + getWidth() - Summer.INSTANCE.fontManager.getFont("ROBO 15").getWidth(currentValue) - 2, getY() + 1, -1);

        //taken from lemon's gui, its public and free to use, line 57 to 67
        double diff = Math.min(getWidth(), Math.max(0, mouseX - getX()));
        double min = getObject().getMin();
        double max = getObject().getMax();
        if (isSliding()) {
            if (diff == 0) {
                getObject().setValDouble(getObject().getMin());
            } else {
                double newValue = roundToPlace(((diff / getWidth()) * (max - min) + min), 2);
                getObject().setValDouble(newValue);
            }
        }

        super.drawScreen(mouseX, mouseY, partialTicks);
    }

    @Override
    public void mouseClicked(int mouseX, int mouseY, int mouseButton) {
        if (isHovered(getX(), getY(), getX() + getWidth(), getY() + getHeight()) && mouseButton == 0) {
            setSliding(true);
        }
        super.mouseClicked(mouseX, mouseY, mouseButton);
    }

    @Override
    public void mouseReleased(int mouseX, int mouseY, int state) {
        if (isSliding() && state == 0)
            setSliding(false);
        super.mouseReleased(mouseX, mouseY, state);
    }

    @Override
    public boolean isVisible() {
        return getObject().isVisible();
    }

    public boolean isSliding() {
        return sliding;
    }

    public void setSliding(boolean sliding) {
        this.sliding = sliding;
    }

    private double roundToPlace(double value, int places) {
        if (places < 0) {
            throw new IllegalArgumentException();
        }
        BigDecimal bd = new BigDecimal(value);
        bd = bd.setScale(places, RoundingMode.HALF_UP);
        return bd.doubleValue();
    }

}
