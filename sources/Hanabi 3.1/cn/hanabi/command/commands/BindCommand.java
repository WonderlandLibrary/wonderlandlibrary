package cn.hanabi.command.commands;

import cn.hanabi.command.Command;
import cn.hanabi.command.CommandException;
import cn.hanabi.events.EventKey;
import cn.hanabi.modules.Mod;
import cn.hanabi.modules.ModManager;
import cn.hanabi.utils.ChatUtils;
import com.darkmagician6.eventapi.EventManager;
import com.darkmagician6.eventapi.EventTarget;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.lwjgl.input.Keyboard;

public class BindCommand extends Command {
   private boolean active = false;
   private @Nullable Mod currentModule = null;

   public BindCommand() {
      super("bind");
      EventManager.register(this);
   }

   public void run(String alias, @NotNull String[] args) {
      if (args.length == 0) {
         throw new CommandException("Usage: ." + alias + " <module> [<none/show>]");
      } else {
         Mod mod = ModManager.getModule(args[0], false);
         if (mod == null) {
            throw new CommandException("The module '" + args[0] + "' does not exist");
         } else if (args.length > 1) {
            if (args[1].equalsIgnoreCase("none")) {
               mod.setKeybind(0);
               ChatUtils.success("§1" + mod.getName() + "§7" + " was bound to " + "§1" + "NONE");
            } else if (args[1].equalsIgnoreCase("show")) {
               ChatUtils.success("§1" + mod.getName() + "§7" + " is bound to " + "§1" + Keyboard.getKeyName(mod.getKeybind()));
            } else {
               int key = Keyboard.getKeyIndex(args[1].toUpperCase());
               mod.setKeybind(key);
               ChatUtils.success("§1" + mod.getName() + "§7" + " was bound to " + "§1" + Keyboard.getKeyName(key));
            }

         } else {
            this.active = true;
            this.currentModule = mod;
            ChatUtils.info("Please press a key");
         }
      }
   }

   public List autocomplete(int arg, String[] args) {
      String prefix = "";
      boolean flag = false;
      if (arg != 0 && args.length != 0) {
         if (arg == 1) {
            flag = true;
            prefix = args[0];
         }
      } else {
         flag = true;
      }

      if (flag) {
         return (List)ModManager.getModules().stream().map(Mod::getName).filter((name) -> {
            return name.toLowerCase().startsWith(prefix);
         }).collect(Collectors.toList());
      } else if (arg == 2) {
         ArrayList arrayList = new ArrayList();
         arrayList.add("none");
         arrayList.add("show");
         return arrayList;
      } else {
         return new ArrayList();
      }
   }

   @EventTarget
   public void onKey(@NotNull EventKey event) {
      if (this.active) {
         this.currentModule.setKeybind(event.getKey());
         ChatUtils.success("§1" + this.currentModule.getName() + "§7" + " was bound to " + "§1" + Keyboard.getKeyName(event.getKey()));
         this.active = false;
         this.currentModule = null;
      }

   }
}
