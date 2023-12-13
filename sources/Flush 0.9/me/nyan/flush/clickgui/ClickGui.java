package me.nyan.flush.clickgui;

import me.nyan.flush.Flush;
import me.nyan.flush.module.ModuleManager;
import me.nyan.flush.ui.elements.Button;
import net.minecraft.client.gui.GuiScreen;

public class ClickGui extends GuiScreen {
    protected final Flush flush = Flush.getInstance();
    protected final ModuleManager moduleManager = Flush.getInstance().getModuleManager();
    private Button button;

    @Override
    public void initGui() {
        super.initGui();
        button = new Button("Custom HUD", width - 104, height - 24, 100, 20);
    }

    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        super.drawScreen(mouseX, mouseY, partialTicks);
        button.drawButton(mouseX, mouseY);
    }

    @Override
    public void mouseClicked(int mouseX, int mouseY, int mouseButton) {
        if (button.mouseClicked(mouseX, mouseY, mouseButton)) {
            mc.displayGuiScreen(flush.getHudCustomizer());
        }
    }

    @Override
    public boolean doesGuiPauseGame() {
        return false;
    }
}
