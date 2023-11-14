package net.ccbluex.LiquidBase.clickgui;

import net.ccbluex.LiquidBase.module.ModuleCategory;
import net.ccbluex.LiquidBase.module.ModuleManager;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.GuiScreen;

import java.awt.*;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Copyright © 2015 - 2017 | CCBlueX | All rights reserved.
 * <p>
 * LiquidBase - By CCBlueX(Marco)
 */
public class ClickGUI extends GuiScreen {

    private List<Panel> panels = new ArrayList<>();

    public ClickGUI() {
        int x = 202;

        for(final ModuleCategory moduleCategory : ModuleCategory.values()) {
            final Panel panel = new Panel(moduleCategory.getName(), x, 50, 100);

            ModuleManager.getModules().stream().filter(module -> module.getModuleCategory() == moduleCategory).forEach(module -> panel.addButton(new Button(panel, module)));

            panels.add(panel);
        }
    }

    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        for(final Panel panel : panels) {
            // Draw panel
            Gui.drawRect(panel.getX(), panel.getY(), panel.getX() + panel.getWidth(), panel.getY() + 20, new Color(66, 134, 244).hashCode());
            fontRendererObj.drawString(panel.getPanelName(), panel.getX() + 5, panel.getY() + 5, 0xffffff);

            for(int i = 0; i < panel.getButtons().size(); i++) {
                final Button button = panel.getButtons().get(i);

                Gui.drawRect(panel.getX(), panel.getY() + 20 + (20 * i), panel.getX() + panel.getWidth(), panel.getY() + (20 * i) + 40, Integer.MIN_VALUE);
                fontRendererObj.drawString((button.getModule().getState() ? "§a" : "§c") + button.getModule().getModuleName(), panel.getX() + 2, panel.getY() + 20 + (20 * i) + 7, 0xffffff);
            }

            // Drag panel
            if(panel.isDrag()) {
                panel.setX(mouseX + panel.getDragX());
                panel.setY(mouseY + panel.getDragY());
            }
        }
        super.drawScreen(mouseX, mouseY, partialTicks);
    }

    @Override
    protected void mouseClicked(int mouseX, int mouseY, int mouseButton) throws IOException {
        if(mouseButton != 0)
            return;

        for(int index = panels.size() - 1; index >= 0; index--) {
            final Panel panel = panels.get(index);

            // Drag panel
            if(panel.isHoverHead(mouseX, mouseY)) {
                panel.setDrag(true);
                panel.setDragX(panel.getX() - mouseX);
                panel.setDragY(panel.getY() - mouseY);
                panels.remove(panel);
                panels.add(panel);
                break;
            }

            //
            for(int buttonIndex = 0; buttonIndex < panel.getButtons().size(); buttonIndex++) {
                final Button button = panel.getButtons().get(buttonIndex);

                if(button.isHover(panel.getX(),panel.getY() + 20 + (20 * buttonIndex), panel.getWidth(), 20, mouseX, mouseY)) {
                    button.getModule().setState(!button.getModule().getState());
                }
            }
        }
        super.mouseClicked(mouseX, mouseY, mouseButton);
    }

    @Override
    protected void mouseReleased(int mouseX, int mouseY, int state) {
        for(final Panel panel : panels)
            panel.setDrag(false);
        super.mouseReleased(mouseX, mouseY, state);
    }
}