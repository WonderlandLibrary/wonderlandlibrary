package my.NewSnake.Tank.module.modules.WORLD;

import my.NewSnake.Tank.command.Command;
import my.NewSnake.Tank.command.CommandManager;
import my.NewSnake.Tank.module.Module;
import my.NewSnake.event.EventTarget;
import my.NewSnake.event.events.PacketSendEvent;
import net.minecraft.network.play.client.C01PacketChatMessage;

@Module.Mod
public class Message extends Module {
   @EventTarget
   private void onPacketSend(PacketSendEvent var1) {
      if (var1.getPacket() instanceof C01PacketChatMessage) {
         C01PacketChatMessage var2 = (C01PacketChatMessage)var1.getPacket();
         String var3 = var2.getMessage();
         if (var3.startsWith(".")) {
            var1.setCancelled(true);
            var3 = var3.replace(".", "");
            Command var4 = CommandManager.getCommandFromMessage(var3);
            String[] var5 = var3.split(" ");
            var4.runCommand(var5);
         }
      }

   }
}
