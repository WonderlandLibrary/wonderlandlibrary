package club.async.clickgui.dropdown.components;

import club.async.Async;
import club.async.clickgui.dropdown.Component;
import club.async.clickgui.dropdown.MButton;
import club.async.module.setting.impl.NumberSetting;
import club.async.util.ColorUtil;
import club.async.util.RenderUtil;
import net.minecraft.client.gui.Gui;

import java.awt.*;
import java.math.BigDecimal;
import java.math.RoundingMode;

public class NumberComp extends Component {

    private double sliderWidth;
    private double sliderMaxWidth;
    private boolean dragging;
    NumberSetting setting;

    public NumberComp(NumberSetting setting, int offset, MButton parent) {
        super(setting, offset, parent);
        dragging = false;
        this.setting = setting;
    }

    @Override
    public void renderComponent(int mouseX, int mouseY) {
        super.renderComponent(mouseX, mouseY);
        handleSlider(mouseX, mouseY);
        Gui.drawRect(RenderUtil.getScaledResolution().getScaledWidth() / 2f - 146, offset - 2 + 13 - parent.scrollOffset, RenderUtil.getScaledResolution().getScaledWidth() / 2f - 144 + sliderMaxWidth + 2, offset + 22 + 13 - parent.scrollOffset, new Color(20,20,20));
        Gui.drawRect(RenderUtil.getScaledResolution().getScaledWidth() / 2f - 144, offset + 13 - parent.scrollOffset, RenderUtil.getScaledResolution().getScaledWidth() / 2f - 144 + sliderWidth, offset + 20 + 13 - parent.scrollOffset, ColorUtil.getMainColor());
        Gui.drawRect(RenderUtil.getScaledResolution().getScaledWidth() / 2f - 144, offset - 1 - parent.scrollOffset, RenderUtil.getScaledResolution().getScaledWidth() / 2f - 141 + Async.INSTANCE.getFontManager().getFont("Arial 20").getWidth(setting.getName() + ": " + setting.getDouble()), offset + 10 - parent.scrollOffset, new Color(10,10,10));
        Async.INSTANCE.getFontManager().getFont("Arial 20").drawString(setting.getName() + ": " + setting.getDouble(),RenderUtil.getScaledResolution().getScaledWidth() / 2f - 142,offset - parent.scrollOffset,new Color(255,255,255,180));

    }

    @Override
    public void mouseClicked(int mouseX, int mouseY, int mouseButton) {
        if (isInside(mouseX, mouseY, RenderUtil.getScaledResolution().getScaledWidth() / 2f - 144, offset + 13 - parent.scrollOffset, 125, 20) && (mouseButton == 0 | mouseButton == 1)) {
            dragging = true;
        }
    }

    @Override
    public void mouseReleased(int mouseX, int mouseY) {
        dragging = false;
    }

    private void handleSlider(int mouseX, int mouseY) {
        double min = setting.getMin();
        double max = setting.getMax();

        int l = 99;
        sliderWidth = (l) * (setting.getDouble() - min) / (max - min);
        sliderMaxWidth = (l) * (setting.getMax() - min) / (max - min);

        double diff = Math.min(l, Math.max(0, mouseX - (x + 1)));
        if (dragging) {
            if (diff == 0) {
                setting.setCurrent(setting.getMin());
            }
            else {
                double newValue = round(((diff / l) * (max - min) + min), 2);
                setting.setCurrent(newValue);
            }
        }
    }

    private double round(final double val, final int places) {
        final double v = Math.round(val / setting.getIncrement()) * setting.getIncrement();
        if (places < 0) {
            throw new IllegalArgumentException();
        }
        BigDecimal bd = new BigDecimal(v);
        bd = bd.setScale(places, RoundingMode.HALF_UP);
        return bd.doubleValue();
    }

}
