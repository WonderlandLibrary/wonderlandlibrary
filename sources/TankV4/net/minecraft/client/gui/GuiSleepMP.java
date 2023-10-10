package net.minecraft.client.gui;

import java.io.IOException;
import net.minecraft.client.Minecraft;
import net.minecraft.client.network.NetHandlerPlayClient;
import net.minecraft.client.resources.I18n;
import net.minecraft.network.play.client.C0BPacketEntityAction;

public class GuiSleepMP extends GuiChat {
   protected void actionPerformed(GuiButton var1) throws IOException {
      if (var1.id == 1) {
         this.wakeFromSleep();
      } else {
         super.actionPerformed(var1);
      }

   }

   public void initGui() {
      super.initGui();
      this.buttonList.add(new GuiButton(1, width / 2 - 100, height - 40, I18n.format("multiplayer.stopSleeping")));
   }

   private void wakeFromSleep() {
      NetHandlerPlayClient var1 = Minecraft.thePlayer.sendQueue;
      var1.addToSendQueue(new C0BPacketEntityAction(Minecraft.thePlayer, C0BPacketEntityAction.Action.STOP_SLEEPING));
   }

   protected void keyTyped(char var1, int var2) throws IOException {
      if (var2 == 1) {
         this.wakeFromSleep();
      } else if (var2 != 28 && var2 != 156) {
         super.keyTyped(var1, var2);
      } else {
         String var3 = this.inputField.getText().trim();
         if (!var3.isEmpty()) {
            Minecraft.thePlayer.sendChatMessage(var3);
         }

         this.inputField.setText("");
         this.mc.ingameGUI.getChatGUI().resetScroll();
      }

   }
}
