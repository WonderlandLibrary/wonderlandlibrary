package club.async.clickgui.dropdown;

import club.async.Async;
import club.async.module.Category;
import club.async.module.Module;
import club.async.util.ColorUtil;
import net.minecraft.client.gui.Gui;

import java.awt.*;
import java.io.IOException;

public class Panel {

    public Category category;
    public double x, y, width, height, dragX, dragY;
    public ClickGUI parent;
    public boolean dragging, expand;
    private final MButton mButton = new MButton(this);

    public Panel(Category category, double x, ClickGUI parent) {
        this.category = category;
        this.x = x;
        this.parent = parent;
        this.y = 30;
        this.width = 115;
        this.height = 200;
        expand = true;
        correctHeight();
    }

    public void init() {
        dragging = false;
    }

    public void drawScreen(int mouseX, int mouseY) {
        if (dragging) {
            x = mouseX - dragX;
            y = mouseY - dragY;
        }

        /*
        Rendering the dragging box and background
         */
        if (expand)
            Gui.drawRect(x, y, x + width, y + height, new Color(35, 35, 35));
            Gui.drawRect(x, y, x + width, y + (Async.INSTANCE.getModuleManager().getModules(category).get(0).isEnabled() ? 25 : 23), ColorUtil.getMainColor());
            Gui.drawRect(x, y, x + 23, y + 23, 0x30000000);
            Async.INSTANCE.getFontManager().getFont("Arial 28").drawString(category.getName(), x + 30, y + 4, -1);
            Async.INSTANCE.getFontManager().getFont("Arial 28").drawCenteredString(String.valueOf(Async.INSTANCE.getModuleManager().getModules(category).size()), x + 23D / 2, y + 4, new Color(255,255,255,180));

        if (!expand)
            return;

        mButton.drawScreen(mouseX, mouseY);

        if (!Async.INSTANCE.getModuleManager().getModules(category).isEmpty()) {
            Gui.drawGradientRect(x, y + 23, x + width, y + 38, new Color(5, 5, 5, 150), new Color(30, 30, 30, 0));
            Gui.drawGradientRect(x, y + height - 13, x + width, y + height, new Color(30, 30, 30, 0), new Color(5, 5, 5, 150));
        }
    }

    /*
    Handling dragging, expand and Module Button
     */
    public void mouseClicked(int mouseX, int mouseY, int mouseButton) {
        if (parent.isInside(mouseX, mouseY, x, y, width, 15)) {
            if (mouseButton == 0) {
                dragging = true;
                dragX = mouseX - x;
                dragY = mouseY - y;
            }
            if (mouseButton == 1) {
                expand = !expand;
            }
        }
        if (!expand)
            return;
        mButton.mouseClicked(mouseX,mouseY,mouseButton);
    }

    public void mouseReleased(int mouseX, int mouseY) {
        dragging = false;
    }

    public void keyTyped(char typedChar, int keyCode) throws IOException {
        mButton.keyTyped(typedChar, keyCode);
    }

    private void correctHeight() {
        double offset = 25;
        for (Module module : Async.INSTANCE.getModuleManager().getModules(category))
        {
            offset += 21;
        }
        height = offset;
    }

}
