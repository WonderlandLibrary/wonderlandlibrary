package today.flux.module.implement.Command;

import net.minecraft.util.EnumChatFormatting;
import today.flux.module.Command;
import today.flux.module.ModuleManager;
import today.flux.module.implement.Render.NameProtect;
import today.flux.utility.ChatUtils;

/**
 * Created by Admin on 2017/03/10.
 */
@Command.Info(name = "rename", syntax = {"<name> | <player> <name> "}, help = "Set display name used by NameProtect")
public class NamePCmd extends Command {
    @Override
    public void execute(String[] args) throws Error {
        if (args.length == 1) {
            NameProtect.name = args[0].replace("&", "§").replace("\\_", "/*/<>/*/").replace("_", " ").replace("/*/<>/*/", "_");

            if (ModuleManager.nameProtectMod.isEnabled()) {
                ChatUtils.sendMessageToPlayer("Display name changed to " + EnumChatFormatting.GOLD + NameProtect.name);
            } else {
                ChatUtils.sendMessageToPlayer(EnumChatFormatting.RED + "You need to enable NameProtect to use the command");
            }
        } else if (args.length == 2) {
            if (ModuleManager.nameProtectMod.isEnabled()) {
                if (!NameProtect.Player_IGN.containsKey(args[0])) {
                    NameProtect.Player_IGN.put(args[0], args[1].replace("\\_", "/*/<>/*/").replace("_", " ").replace("/*/<>/*/", "_"));
                }

                ChatUtils.sendMessageToPlayer("Display name changed to " + EnumChatFormatting.GOLD + args[1]);
            } else {
                ChatUtils.sendMessageToPlayer(EnumChatFormatting.RED + "You need to enable NameProtect to use the command");
            }
        } else {
            this.syntaxError();
        }
    }
}
