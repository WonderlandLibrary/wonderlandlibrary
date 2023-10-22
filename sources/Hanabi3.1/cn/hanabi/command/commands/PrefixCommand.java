package cn.hanabi.command.commands;

import cn.hanabi.command.Command;
import cn.hanabi.gui.notifications.Notification;
import cn.hanabi.modules.modules.player.Spammer;
import cn.hanabi.utils.ClientUtil;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;
import net.minecraft.client.Minecraft;
import org.jetbrains.annotations.NotNull;

public class PrefixCommand extends Command {
   private static final String fileDir;

   public PrefixCommand() {
      super("prefix");
   }

   public static void saveText() {
      File f = new File(fileDir + "/prefix.txt");

      try {
         if (!f.exists()) {
            f.createNewFile();
         }

         PrintWriter pw = new PrintWriter(f);
         pw.print(Spammer.prefix);
         pw.close();
      } catch (Exception var2) {
         var2.printStackTrace();
      }

   }

   public static void loadText() throws IOException {
      File f = new File(fileDir + "/prefix.txt");
      if (!f.exists()) {
         f.createNewFile();
      } else {
         BufferedReader br = new BufferedReader(new FileReader(f));

         String line;
         while((line = br.readLine()) != null) {
            try {
               Spammer.prefix = line;
            } catch (Exception var4) {
            }
         }
      }

   }

   public void run(String alias, @NotNull String[] args) {
      if (args.length != 1) {
         ClientUtil.sendClientMessage("Failed", Notification.Type.WARNING);
      } else {
         Spammer.prefix = args[0];
         saveText();
         ClientUtil.sendClientMessage("Changed to " + args[0], Notification.Type.SUCCESS);
      }

   }

   public @NotNull List autocomplete(int arg, String[] args) {
      return new ArrayList();
   }

   static {
      fileDir = Minecraft.getMinecraft().mcDataDir.getAbsolutePath() + "/Hanabi";
   }
}
