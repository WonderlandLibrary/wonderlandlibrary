// 
// Decompiled by Procyon v0.5.30
// 

package net.minecraft.client.gui;

import net.minecraft.client.network.NetHandlerPlayClient;
import net.minecraft.network.Packet;
import net.minecraft.entity.Entity;
import net.minecraft.network.play.client.C0BPacketEntityAction;
import java.io.IOException;
import net.minecraft.client.resources.I18n;

public class GuiSleepMP extends GuiChat
{
    private static final String __OBFID = "CL_00000697";
    
    @Override
    public void initGui() {
        super.initGui();
        this.buttonList.add(new GuiButton(1, this.width / 2 - 100, this.height - 40, I18n.format("multiplayer.stopSleeping", new Object[0])));
    }
    
    @Override
    protected void keyTyped(final char typedChar, final int keyCode) throws IOException {
        if (keyCode == 1) {
            this.wakeFromSleep();
        }
        else if (keyCode != 28 && keyCode != 156) {
            super.keyTyped(typedChar, keyCode);
        }
        else {
            final String var3 = this.inputField.getText().trim();
            if (!var3.isEmpty()) {
                this.mc.thePlayer.sendChatMessage(var3);
            }
            this.inputField.setText("");
            this.mc.ingameGUI.getChatGUI().resetScroll();
        }
    }
    
    @Override
    protected void actionPerformed(final GuiButton button) throws IOException {
        if (button.id == 1) {
            this.wakeFromSleep();
        }
        else {
            super.actionPerformed(button);
        }
    }
    
    private void wakeFromSleep() {
        final NetHandlerPlayClient var1 = this.mc.thePlayer.sendQueue;
        var1.addToSendQueue(new C0BPacketEntityAction(this.mc.thePlayer, C0BPacketEntityAction.Action.STOP_SLEEPING));
    }
}
