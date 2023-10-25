package wtf.evolution.clickgui;

import net.minecraft.client.gui.GuiScreen;
import wtf.evolution.clickgui.panels.Panel;
import wtf.evolution.helpers.StencilUtil;
import wtf.evolution.helpers.render.RenderUtil;
import wtf.evolution.helpers.render.RoundedUtil;
import wtf.evolution.module.Category;

import java.awt.*;
import java.io.IOException;
import java.util.ArrayList;

public class ClickScreen extends GuiScreen {

    public ArrayList<Panel> panels = new ArrayList<>();

    public ClickScreen() {
        int y = 5;
        int x = 5;
        for (Category category : Category.values()) {
            panels.add(new Panel(category, x, y));
            x += 130;
        }
    }

    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        super.drawScreen(mouseX, mouseY, partialTicks);
        panels.forEach(panel -> {
            panel.render(mouseX, mouseY);
        });
    }

    @Override
    protected void mouseClicked(int mouseX, int mouseY, int mouseButton) throws IOException {
        super.mouseClicked(mouseX, mouseY, mouseButton);
        panels.forEach(panel -> {
            if (RenderUtil.isHovered(mouseX, mouseY, panel.x, panel.y, 125, panel.getHeight())) {
                panel.click(mouseX, mouseY, mouseButton);
            }
        });
    }
}
