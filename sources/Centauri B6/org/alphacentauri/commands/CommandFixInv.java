package org.alphacentauri.commands;

import java.util.ArrayList;
import net.minecraft.client.network.NetHandlerPlayClient;
import net.minecraft.network.play.client.C0DPacketCloseWindow;
import org.alphacentauri.AC;
import org.alphacentauri.management.commands.Command;
import org.alphacentauri.management.commands.ICommandHandler;

public class CommandFixInv implements ICommandHandler {
   public String getName() {
      return "FixInv";
   }

   public boolean execute(Command cmd) {
      NetHandlerPlayClient sendQueue = AC.getMC().getPlayer().sendQueue;

      for(int i = 0; i < 36; ++i) {
         AC.getMC().playerController.windowClick(0, i, 0, 0, AC.getMC().getPlayer());
      }

      sendQueue.addToSendQueue(new C0DPacketCloseWindow(0));
      AC.getMC().playerController.updateController();
      return true;
   }

   public String[] getAliases() {
      return new String[]{"fixinv"};
   }

   public String getDesc() {
      return "Fixes inventory bugs.";
   }

   public ArrayList autocomplete(Command cmd) {
      return new ArrayList();
   }
}
