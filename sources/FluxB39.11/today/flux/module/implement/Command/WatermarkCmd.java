package today.flux.module.implement.Command;

import net.minecraft.util.EnumChatFormatting;
import today.flux.module.Command;
import today.flux.module.ModuleManager;
import today.flux.module.implement.Render.Hud;
import today.flux.module.implement.Render.NameProtect;
import today.flux.utility.ChatUtils;

/**
 * Created by Admin on 2017/03/10.
 */
@Command.Info(name = "watermark", syntax = {"<name> | <player> <name> "}, help = "Set Watermark name in HUD.")
public class WatermarkCmd extends Command {
    @Override
    public void execute(String[] args) throws Error {
        if (args.length == 1) {
            Hud.name = args[0].replace("&", "§").replace("\\_", "/*/<>/*/").replace("_", " ").replace("/*/<>/*/", "_");
                ChatUtils.sendMessageToPlayer("Watermark name changed to " + EnumChatFormatting.GOLD + Hud.name);
            }
        }
}
