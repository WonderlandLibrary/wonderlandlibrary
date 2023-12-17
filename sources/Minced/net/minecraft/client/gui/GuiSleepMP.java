// 
// Decompiled by Procyon v0.5.36
// 

package net.minecraft.client.gui;

import net.minecraft.client.network.NetHandlerPlayClient;
import net.minecraft.network.Packet;
import net.minecraft.entity.Entity;
import net.minecraft.network.play.client.CPacketEntityAction;
import java.io.IOException;
import net.minecraft.client.Minecraft;
import net.minecraft.client.resources.I18n;

public class GuiSleepMP extends GuiChat
{
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
            final String s = this.inputField.getText().trim();
            if (!s.isEmpty()) {
                final Minecraft mc = GuiSleepMP.mc;
                Minecraft.player.sendChatMessage(s);
            }
            this.inputField.setText("");
            GuiSleepMP.mc.ingameGUI.getChatGUI().resetScroll();
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
        final Minecraft mc = GuiSleepMP.mc;
        final NetHandlerPlayClient connection;
        final NetHandlerPlayClient nethandlerplayclient = connection = Minecraft.player.connection;
        final Minecraft mc2 = GuiSleepMP.mc;
        connection.sendPacket(new CPacketEntityAction(Minecraft.player, CPacketEntityAction.Action.STOP_SLEEPING));
    }
}
