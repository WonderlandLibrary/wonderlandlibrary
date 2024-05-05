package net.minecraft.client.resources;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.GuiScreenWorking;

class ResourcePackRepository.2
implements Runnable {
    final /* synthetic */ Minecraft val$minecraft;
    final /* synthetic */ GuiScreenWorking val$guiscreenworking;

    ResourcePackRepository.2(Minecraft minecraft, GuiScreenWorking guiScreenWorking) {
        this.val$minecraft = minecraft;
        this.val$guiscreenworking = guiScreenWorking;
    }

    public void run() {
        this.val$minecraft.displayGuiScreen((GuiScreen)this.val$guiscreenworking);
    }
}
