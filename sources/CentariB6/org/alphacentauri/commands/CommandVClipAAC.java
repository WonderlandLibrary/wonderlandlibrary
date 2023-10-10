package org.alphacentauri.commands;

import java.util.ArrayList;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.network.play.client.C03PacketPlayer.C04PacketPlayerPosition;
import org.alphacentauri.AC;
import org.alphacentauri.management.commands.Command;
import org.alphacentauri.management.commands.ICommandHandler;

public class CommandVClipAAC implements ICommandHandler {
   public String getName() {
      return "VClipAAC";
   }

   public boolean execute(Command cmd) {
      String[] args = cmd.getArgs();
      if(args.length != 1) {
         return false;
      } else {
         try {
            double v = Double.parseDouble(args[0]);
            if(v > 0.0D) {
               AC.addChat(this.getName(), "Only works for down VClip");
               return true;
            } else {
               EntityPlayerSP player = AC.getMC().getPlayer();
               (new Thread(() -> {
                  for(int i = 0; i < 2; ++i) {
                     player.sendQueue.addToSendQueue(new C04PacketPlayerPosition(player.posX, player.posY + v, player.posZ, false));
                     player.sendQueue.addToSendQueue(new C04PacketPlayerPosition(player.posX + 99.0D, player.posY, player.posZ + 99.0D, false));

                     try {
                        Thread.sleep(50L);
                     } catch (InterruptedException var5) {
                        var5.printStackTrace();
                     }
                  }

               })).start();
               return true;
            }
         } catch (Exception var6) {
            return false;
         }
      }
   }

   public String[] getAliases() {
      return new String[]{"vclipaac"};
   }

   public String getDesc() {
      return "Vertical Phase AAC Edition";
   }

   public ArrayList autocomplete(Command cmd) {
      return new ArrayList();
   }
}
