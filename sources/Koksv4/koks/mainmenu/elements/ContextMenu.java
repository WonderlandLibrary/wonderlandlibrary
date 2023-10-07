package koks.mainmenu.elements;

import koks.mainmenu.Windows;
import koks.mainmenu.interfaces.Element;
import lombok.AllArgsConstructor;
import net.minecraft.client.gui.Gui;

import java.awt.*;
import java.util.ArrayList;

public class ContextMenu implements Element {

    public boolean visible;
    public int x,y, width, height, calcWidth;
    private final ArrayList<Option> options = new ArrayList<>();

    public void init() {
        options.clear();
        options.add(new Option("Options", new Action() {
            @Override
            public void doAction() {
                Windows.getWindow(0).visible = true;
            }
        }));
        for (Option option : options) {
            if(optionFont.getStringWidth(option.name) > calcWidth)
                calcWidth = (int) optionFont.getStringWidth(option.name);
        }
    }

    public void draw(int mouseX, int mouseY) {
        if(!visible) return;
        if(width < 0) {
            width = Math.abs(width);
            x = x - width;
        }

        int offsetY = y;
        renderUtil.drawOutlineRect(x, offsetY, x + calcWidth, y + height, 1F, outlineColor.getRGB(), insideColor.getRGB());
        for(Option option : options) {
            double addY = infoFont.getStringHeight(option.name);
            if(mouseX > x && mouseX <= x + width && mouseY > offsetY && mouseY < offsetY + addY)
                Gui.drawRect(x, offsetY, x + calcWidth, (int) (offsetY + addY), insideColor.darker().getRGB());
            infoFont.drawString(option.name, x + width / 2F - infoFont.getStringWidth(option.name) / 2, (float) (offsetY), Color.white, true);
            offsetY += addY;
        }
        height = offsetY - y;
    }

    public void click(int mouseX, int mouseY, int button) {
        if(button == 0) {
            int offsetY = y;
            for (Option option : options) {
                double addY = infoFont.getStringHeight(option.name);
                if (mouseX > x && mouseX <= x + width && mouseY > offsetY && mouseY < offsetY + addY)
                    option.action.doAction();
                offsetY += addY;
            }
        }
    }

    public boolean isMouseOver(int mouseX, int mouseY) {
        return visible && mouseX >= x && mouseX <= x + width && mouseY >= y && mouseY <= y + height;
    }

    @AllArgsConstructor
    static class Option {
        public String name;
        public Action action;
    }
}
