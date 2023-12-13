package cn.hanabi.command.commands;

import cn.hanabi.command.Command;
import cn.hanabi.modules.Mod;
import cn.hanabi.modules.ModManager;
import cn.hanabi.utils.PlayerUtil;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import org.jetbrains.annotations.NotNull;
import org.lwjgl.input.Keyboard;

public class BindsCommand extends Command {
   public BindsCommand() {
      super("binds");
   }

   public void run(String alias, @NotNull String[] args) {
      try {
         Iterator var3 = ModManager.modules.iterator();

         while(var3.hasNext()) {
            Mod mod = (Mod)var3.next();
            if (mod.getKeybind() != 0) {
               PlayerUtil.tellPlayer("§b[Hanabi]§a" + mod.getName() + " - " + Keyboard.getKeyName(mod.getKeybind()));
            }
         }
      } catch (Exception var5) {
      }

   }

   public List autocomplete(int arg, String[] args) {
      return new ArrayList();
   }
}
