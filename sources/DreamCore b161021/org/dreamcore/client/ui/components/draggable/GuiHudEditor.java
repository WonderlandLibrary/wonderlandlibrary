package org.dreamcore.client.ui.components.draggable;

import net.minecraft.client.gui.GuiScreen;
import org.dreamcore.client.dreamcore;

public class GuiHudEditor extends GuiScreen {

    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        this.drawWorldBackground(0);
        for (DraggableModule mod : dreamcore.instance.draggableManager.getMods()) {
            mod.render(mouseX, mouseY);
        }
        super.drawScreen(mouseX, mouseY, partialTicks);
    }
}
