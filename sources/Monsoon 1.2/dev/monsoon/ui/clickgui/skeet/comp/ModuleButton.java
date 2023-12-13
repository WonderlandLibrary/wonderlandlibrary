package dev.monsoon.ui.clickgui.skeet.comp;

import dev.monsoon.Monsoon;
import dev.monsoon.module.base.Module;
import dev.monsoon.ui.clickgui.skeet.SkeetGUI;
import dev.monsoon.util.render.DrawUtil;
import dev.monsoon.util.render.RenderUtil;
import net.minecraft.client.gui.Gui;

import java.awt.*;

public class ModuleButton {

    public int x,y,w,h;
    public Module m;

    public ModuleButton(int x, int y, Module m) {
        this.x = x;
        this.y = y;
        this.w = Monsoon.getFont().getStringWidth(m.getName()) + 1;
        this.h = Monsoon.getFont().getHeight() + 1;
        this.m = m;

        drawButton();
    }

    public void drawButton() {
        Monsoon.getFont().drawStringWithShadow(m.getName(), x, y + 1, -1);
        Gui.drawRect(x + w + 2, y + 1,  x + w + 11, y + h, m.isEnabled() ? new Color(0, 140, 255).getRGB() : new Color(39,39,39).getRGB());
        DrawUtil.drawHollowRect(x + w + 1, y,  10, h, new Color(50,50,50).getRGB());
    }

    public void onClick(int mouseX, int mouseY, int button) {
        if (this.isHovered(mouseX, mouseY) && Monsoon.getSkeetGui().viewing == m.category) {
            if (button == 0) m.setEnabled(!m.isEnabled());
            if (button == 1) Monsoon.getSkeetGui().setConfiguring(m);
        }
    }

    protected boolean isHovered(int mouseX, int mouseY) {
        return RenderUtil.isHovered(x + w + 2, y + 1, x + w + 11, y + h, mouseX, mouseY);
    }
}
