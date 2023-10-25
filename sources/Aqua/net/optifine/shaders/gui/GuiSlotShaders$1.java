package net.optifine.shaders.gui;

import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.GuiYesNoCallback;
import net.optifine.shaders.gui.GuiSlotShaders;

/*
 * Exception performing whole class analysis ignored.
 */
class GuiSlotShaders.1
implements GuiYesNoCallback {
    final /* synthetic */ int val$index;

    GuiSlotShaders.1(int n) {
        this.val$index = n;
    }

    public void confirmClicked(boolean result, int id) {
        if (result) {
            GuiSlotShaders.access$000((GuiSlotShaders)GuiSlotShaders.this, (int)this.val$index);
        }
        GuiSlotShaders.access$100((GuiSlotShaders)GuiSlotShaders.this).displayGuiScreen((GuiScreen)GuiSlotShaders.this.shadersGui);
    }
}
