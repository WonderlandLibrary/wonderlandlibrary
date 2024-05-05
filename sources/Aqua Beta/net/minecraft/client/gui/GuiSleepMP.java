package net.minecraft.client.gui;

import java.io.IOException;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiChat;
import net.minecraft.client.network.NetHandlerPlayClient;
import net.minecraft.client.resources.I18n;
import net.minecraft.entity.Entity;
import net.minecraft.network.Packet;
import net.minecraft.network.play.client.C0BPacketEntityAction;

public class GuiSleepMP
extends GuiChat {
    public void initGui() {
        super.initGui();
        this.buttonList.add((Object)new GuiButton(1, width / 2 - 100, height - 40, I18n.format((String)"multiplayer.stopSleeping", (Object[])new Object[0])));
    }

    protected void keyTyped(char typedChar, int keyCode) throws IOException {
        if (keyCode == 1) {
            this.wakeFromSleep();
        } else if (keyCode != 28 && keyCode != 156) {
            super.keyTyped(typedChar, keyCode);
        } else {
            String s = this.inputField.getText().trim();
            if (!s.isEmpty()) {
                this.mc.thePlayer.sendChatMessage(s);
            }
            this.inputField.setText("");
            this.mc.ingameGUI.getChatGUI().resetScroll();
        }
    }

    protected void actionPerformed(GuiButton button) throws IOException {
        if (button.id == 1) {
            this.wakeFromSleep();
        } else {
            super.actionPerformed(button);
        }
    }

    private void wakeFromSleep() {
        NetHandlerPlayClient nethandlerplayclient = this.mc.thePlayer.sendQueue;
        nethandlerplayclient.addToSendQueue((Packet)new C0BPacketEntityAction((Entity)this.mc.thePlayer, C0BPacketEntityAction.Action.STOP_SLEEPING));
    }
}
