package net.minecraft.client.network;

import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.GuiYesNo;
import net.minecraft.client.gui.GuiYesNoCallback;
import net.minecraft.client.network.NetHandlerPlayClient;
import net.minecraft.client.resources.I18n;

/*
 * Exception performing whole class analysis ignored.
 */
class NetHandlerPlayClient.3
implements Runnable {
    final /* synthetic */ String val$s1;
    final /* synthetic */ String val$s;

    NetHandlerPlayClient.3(String string, String string2) {
        this.val$s1 = string;
        this.val$s = string2;
    }

    public void run() {
        NetHandlerPlayClient.access$100((NetHandlerPlayClient)NetHandlerPlayClient.this).displayGuiScreen((GuiScreen)new GuiYesNo((GuiYesNoCallback)new /* Unavailable Anonymous Inner Class!! */, I18n.format((String)"multiplayer.texturePrompt.line1", (Object[])new Object[0]), I18n.format((String)"multiplayer.texturePrompt.line2", (Object[])new Object[0]), 0));
    }
}
