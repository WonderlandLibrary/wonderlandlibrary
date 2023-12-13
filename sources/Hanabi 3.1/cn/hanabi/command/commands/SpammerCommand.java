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

public class SpammerCommand extends Command {
   private static final String fileDir;

   public SpammerCommand() {
      super("spammer");
   }

   public static void saveText() {
      File f = new File(fileDir + "/spammer.txt");

      try {
         if (!f.exists()) {
            f.createNewFile();
         }

         PrintWriter pw = new PrintWriter(f);
         pw.print(Spammer.text);
         pw.close();
      } catch (Exception var2) {
         var2.printStackTrace();
      }

   }

   public static void loadText() throws IOException {
      File f = new File(fileDir + "/spammer.txt");
      if (!f.exists()) {
         f.createNewFile();
      } else {
         BufferedReader br = new BufferedReader(new FileReader(f));

         String line;
         while((line = br.readLine()) != null) {
            try {
               Spammer.text = line;
            } catch (Exception var4) {
            }
         }
      }

   }

   public void run(String alias, String[] args) {
      if (args.length == 0) {
         ClientUtil.sendClientMessage("Failed", Notification.Type.WARNING);
      } else {
         StringBuilder msg = new StringBuilder();

         for(int i = 0; i < args.length; ++i) {
            msg.append(args[i]).append(" ");
         }

         Spammer.text = msg.toString();
         saveText();
         ClientUtil.sendClientMessage("Changed to " + msg, Notification.Type.SUCCESS);
      }

   }

   public List autocomplete(int arg, String[] args) {
      return new ArrayList();
   }

   static {
      fileDir = Minecraft.getMinecraft().mcDataDir.getAbsolutePath() + "/Hanabi";
   }
}
