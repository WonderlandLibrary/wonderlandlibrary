package club.async.clickgui.flat.components.impl;

import club.async.Async;
import club.async.clickgui.flat.ClickGUI;
import club.async.clickgui.flat.components.Component;
import club.async.module.setting.impl.ModeSetting;
import net.minecraft.client.gui.Gui;

import java.awt.*;
import java.io.IOException;

public class Combo extends Component {

    ModeSetting modeSetting;

    public Combo(ModeSetting sParent, ClickGUI parent, float width, double offset) {
        this.sParent = sParent;
        this.modeSetting = sParent;
        this.parent = parent;
        this.width = width;
        this.offset = offset;
    }

    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        super.drawScreen(mouseX, mouseY, partialTicks);
        Gui.drawRect(parent.x + 5, parent.y + offset - 8 - parent.scrollOffset2, parent.x + 5 + width,parent.y + offset + 20- parent.scrollOffset2, new Color(20, 20, 20).getRGB());
        Gui.drawRect(parent.x + 78, parent.y + offset - 8 - parent.scrollOffset2, parent.x + 5 + width,parent.y + offset + 20- parent.scrollOffset2, new Color(28, 28, 28).getRGB());
        Async.INSTANCE.getFontManager().getFont("Arial 22").drawString(sParent.getName() + ":",  (float)parent.x + 10 ,(float) + parent.y + (float) offset- parent.scrollOffset2, new Color(140,140,140).getRGB());
        Async.INSTANCE.getFontManager().getFont("Arial 22").drawCenteredString(modeSetting.getCurrMode(),  (float)parent.x + 108,(float) + parent.y + (float) offset- parent.scrollOffset2, new Color(140,140,140).getRGB());
    }

    @Override
    public void keyTyped(char typedChar, int keyCode) throws IOException {
        super.keyTyped(typedChar, keyCode);
    }

    @Override
    public void mouseClicked(int mouseX, int mouseY, int mouseButton) throws IOException {
        super.mouseClicked(mouseX, mouseY, mouseButton);
        if (parent.isInside(mouseX, mouseY,parent.x + 5, parent.y + offset - 8 - parent.scrollOffset2, width,28)) {
            if (mouseButton == 0) {
                int maxIndex = modeSetting.getModes().length;

                if(parent.modeIndex + 1 >= maxIndex)
                    parent.modeIndex = 0;
                else
                    parent.modeIndex++;

                modeSetting.setCurrMode(modeSetting.getModes()[parent.modeIndex]);
            }
            if (mouseButton == 1) {
                int maxIndex = modeSetting.getModes().length;

                if(parent.modeIndex == 0) {

                    parent.modeIndex = maxIndex - 1;
                } else {
                    parent.modeIndex--;
                }
                modeSetting.setCurrMode(modeSetting.getModes()[parent.modeIndex]);
            }
        }
    }

    @Override
    public void mouseReleased(int mouseX, int mouseY, int state) {
        super.mouseReleased(mouseX, mouseY, state);
    }

}
