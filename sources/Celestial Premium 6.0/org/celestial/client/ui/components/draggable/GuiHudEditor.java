/*
 * Decompiled with CFR 0.150.
 */
package org.celestial.client.ui.components.draggable;

import net.minecraft.client.gui.GuiScreen;
import org.celestial.client.Celestial;
import org.celestial.client.ui.components.draggable.DraggableComponent;
import org.celestial.client.ui.components.draggable.DraggableModule;

public class GuiHudEditor
extends GuiScreen {
    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        this.drawWorldBackground(0);
        for (DraggableModule mod : Celestial.instance.draggableManager.getMods()) {
            mod.render(mouseX, mouseY);
            if (!mod.drag.isDragging()) continue;
            DraggableComponent.draggableModules.add(mod);
        }
        super.drawScreen(mouseX, mouseY, partialTicks);
    }
}

