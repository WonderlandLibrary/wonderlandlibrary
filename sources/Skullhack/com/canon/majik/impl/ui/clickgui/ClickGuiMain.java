package com.canon.majik.impl.ui.clickgui;

import com.canon.majik.impl.modules.api.Category;
import com.canon.majik.impl.modules.impl.client.HudEditor;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiScreen;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class ClickGuiMain extends GuiScreen {
    public List<CategoryPanel> panels = new ArrayList<>();

    @Override
    public void initGui() {
        if (this.panels.isEmpty()) {
            int x = 10;
            for (Category category : Category.values()) {
                if(!(HudEditor.instance.isEnabled()) && category != Category.HUD) {
                    panels.add(new CategoryPanel(category, x, 10, 130, 15, Minecraft.getMinecraft()));
                    x += 140;
                } else if (HudEditor.instance.isEnabled()){
                    panels.add(new CategoryPanel(Category.HUD,100, 10, 100, 15, Minecraft.getMinecraft()));
                }
            }
        }
    }

    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        this.panels.forEach(panel -> panel.drawScreen(mouseX, mouseY, partialTicks));
    }

    @Override
    public void mouseClicked(int mouseX, int mouseY, int mouseButton) {
        this.panels.forEach(panel -> panel.mouseClicked(mouseX, mouseY, mouseButton));
    }

    @Override
    public void mouseReleased(int mouseX, int mouseY, int state) {
        this.panels.forEach(panel -> panel.mouseReleased(mouseX, mouseY, state));
    }

    @Override
    public void keyTyped(char typedChar, int keyCode) {
        this.panels.forEach(panel -> panel.keyTyped(typedChar, keyCode));
        try { super.keyTyped(typedChar, keyCode); } catch (IOException ignored) { }
    }
}
