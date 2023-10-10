package my.NewSnake.Tank.command.commands;

import my.NewSnake.Tank.command.Com;
import my.NewSnake.Tank.command.Command;
import my.NewSnake.utils.ClientUtils;
import net.minecraft.network.play.client.C03PacketPlayer;

@Com(
   names = {"kick", "k"}
)
public class Kick extends Command {
   public void runCommand(String[] var1) {
      ClientUtils.packet(new C03PacketPlayer.C05PacketPlayerLook(Float.NEGATIVE_INFINITY, Float.NEGATIVE_INFINITY, false));
   }

   public String getHelp() {
      return "Kick - kick <k> - Kicks you from most servers.";
   }
}
