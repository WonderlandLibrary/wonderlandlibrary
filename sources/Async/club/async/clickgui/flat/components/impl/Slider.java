package club.async.clickgui.flat.components.impl;

import club.async.Async;
import club.async.clickgui.flat.ClickGUI;
import club.async.clickgui.flat.components.Component;
import club.async.module.setting.impl.NumberSetting;
import club.async.util.ColorUtil;
import net.minecraft.client.gui.Gui;

import java.awt.*;
import java.io.IOException;
import java.math.BigDecimal;
import java.math.RoundingMode;

public class Slider extends Component {

    private boolean dragging = false;

    private double renderWidth;
    private double renderWidth2;

    NumberSetting sParent;

    public Slider(NumberSetting sParent, ClickGUI parent, float width, double offset) {
        this.sParent = sParent;
        this.parent = parent;
        this.width = width;
        this.offset = offset;
    }

    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        super.drawScreen(mouseX, mouseY, partialTicks);
        Gui.drawRect(parent.x + 285, parent.y + offset - 6 - parent.scrollOffset2, parent.x + 285 + renderWidth2,parent.y + offset - parent.scrollOffset2, new Color(28,28,28).getRGB());
        Gui.drawRect(parent.x + 285, parent.y + offset - 6 - parent.scrollOffset2, parent.x + 285 + renderWidth,parent.y + offset - parent.scrollOffset2, ColorUtil.getMainColor().getRGB());
        Gui.drawRect(parent.x + 285, parent.y + offset - 8 - parent.scrollOffset2 - 13, parent.x + 285 + Async.INSTANCE.getFontManager().getFont("Arial 20").getWidth(sParent.getName() + ": " + sParent.getDouble()) + 2,parent.y + offset - 8 - parent.scrollOffset2 - 2, new Color(28,28,28).getRGB());
        Async.INSTANCE.getFontManager().getFont("Arial 20").drawString(sParent.getName() + ": " + sParent.getDouble(), (float)parent.x + 287,(float) parent.y + (float) offset - parent.scrollOffset2 - 20, new Color(140,140,140).getRGB());
    }

    @Override
    public void keyTyped(char typedChar, int keyCode) throws IOException {
        super.keyTyped(typedChar, keyCode);
    }

    @Override
    public void mouseClicked(int mouseX, int mouseY, int mouseButton) throws IOException {
        super.mouseClicked(mouseX, mouseY, mouseButton);
        if (isInside(mouseX, mouseY, parent.x + 285, parent.y + offset - 6 - parent.scrollOffset2, width,6) && mouseButton == 0) {
            dragging = true;
        }
    }

    @Override
    public void mouseReleased(int mouseX, int mouseY, int state) {
        super.mouseReleased(mouseX, mouseY, state);
        dragging = false;
    }

    @Override
    public void updateScreen(int mouseX, int mouseY) {
        super.updateScreen(mouseX, mouseY);
        double min = sParent.getMin();
        double max = sParent.getMax();

        renderWidth = (130) * (sParent.getDouble() - min) / (max - min);
        renderWidth2 = (130) * (sParent.getMax() - min) / (max - min);

        double diff = Math.min(130, Math.max(0, mouseX - (parent.x + 285)));
        if (dragging) {
            if (diff == 0) {
                sParent.setCurrent(sParent.getMin());
            }
            else {
                double newValue = roundToPlace(((diff / 130) * (max - min) + min));
                sParent.setCurrent(newValue);
            }
        }
    }

    private double roundToPlace(double value) {
        BigDecimal bd = new BigDecimal(value);
        bd = bd.setScale(1, RoundingMode.HALF_UP);
        return bd.doubleValue();
    }

}
