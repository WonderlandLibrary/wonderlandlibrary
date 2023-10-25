package net.minecraft.client.gui;

import java.io.IOException;
import net.minecraft.client.network.NetHandlerPlayClient;
import net.minecraft.client.resources.I18n;
import net.minecraft.entity.Entity;
import net.minecraft.network.Packet;
import net.minecraft.network.play.client.C0BPacketEntityAction;

public class GuiSleepMP extends GuiChat {
  public void initGui() {
    super.initGui();
    this.buttonList.add(new GuiButton(1, this.width / 2 - 100, this.height - 40, I18n.format("multiplayer.stopSleeping", new Object[0])));
  }
  
  protected void keyTyped(char typedChar, int keyCode) throws IOException {
    if (keyCode == 1) {
      wakeFromSleep();
    } else if (keyCode != 28 && keyCode != 156) {
      super.keyTyped(typedChar, keyCode);
    } else {
      String s = this.inputField.getText().trim();
      if (!s.isEmpty())
        this.mc.thePlayer.sendChatMessage(s); 
      this.inputField.setText("");
      this.mc.ingameGUI.getChatGUI().resetScroll();
    } 
  }
  
  protected void actionPerformed(GuiButton button) throws IOException {
    if (button.id == 1) {
      wakeFromSleep();
    } else {
      super.actionPerformed(button);
    } 
  }
  
  private void wakeFromSleep() {
    NetHandlerPlayClient nethandlerplayclient = this.mc.thePlayer.sendQueue;
    nethandlerplayclient.addToSendQueue((Packet)new C0BPacketEntityAction((Entity)this.mc.thePlayer, C0BPacketEntityAction.Action.STOP_SLEEPING));
  }
}


/* Location:              C:\Users\Joona\Downloads\Cupid.jar!\net\minecraft\client\gui\GuiSleepMP.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */