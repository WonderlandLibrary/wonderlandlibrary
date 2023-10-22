package cn.hanabi.command.commands;

import cn.hanabi.command.Command;
import cn.hanabi.utils.ChatUtils;
import java.util.ArrayList;
import java.util.List;
import me.yarukon.Yarukon;
import org.jetbrains.annotations.NotNull;

public class SkinChangeCommand extends Command {
   public static boolean slim = false;
   public static String targetSkin = "";

   public SkinChangeCommand() {
      super("changeskin");
   }

   public void run(String alias, @NotNull String[] args) {
      if (args.length == 1) {
         targetSkin = args[0];
         Yarukon.INSTANCE.loadSkinFromLocal(targetSkin);
         slim = !slim;
         ChatUtils.info("Current skin-type: " + (slim ? "Slim" : "Steve"));
      } else {
         targetSkin = "";
      }

   }

   public List autocomplete(int arg, String[] args) {
      return new ArrayList();
   }
}
