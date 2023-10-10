package my.NewSnake.Tank.command.commands;

import my.NewSnake.Tank.command.Com;
import my.NewSnake.Tank.command.Command;
import my.NewSnake.utils.ClientUtils;
import net.minecraft.network.play.client.C01PacketChatMessage;

@Com(
   names = {"ncp", "testncp"}
)
public class Ncp extends Command {
   public String getHelp() {
      return "Ncp - ncp <testncp> (name) - Sets a player as a testncp target.";
   }

   public void runCommand(String[] var1) {
      if (var1.length > 1) {
         ClientUtils.packet(new C01PacketChatMessage("/testncp input"));
         ClientUtils.packet(new C01PacketChatMessage("/testncp input " + var1[1]));
      } else {
         ClientUtils.sendMessage(this.getHelp());
      }

   }
}
