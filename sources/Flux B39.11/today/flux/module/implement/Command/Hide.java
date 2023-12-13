package today.flux.module.implement.Command;

import today.flux.Flux;
import today.flux.module.Command;
import today.flux.module.Module;
import today.flux.module.ModuleManager;
import today.flux.utility.ChatUtils;

/**
 * Created by John on 2017/06/01.
 */
@Command.Info(name = "hide", syntax = {"<module> | clear"}, help = "Hide the specified module from Arraylist")
public class Hide extends Command {
    @Override
    public void execute(String[] args) throws Error {
        if (args.length >= 1) {
            if (args[0].equalsIgnoreCase("clear")) {
                for (Module module : ModuleManager.getModList()) {
                    module.setHide(false);
                }
                ChatUtils.sendMessageToPlayer("All modules are shown");
            }
            Module module = Flux.INSTANCE.getModuleManager().getModuleByName(args[0]);
            if (module != null) {

                if (module.isHide()) {
                    module.setHide(false);
                    ChatUtils.sendMessageToPlayer("The module is shown");
                } else {
                    module.setHide(true);
                    ChatUtils.sendMessageToPlayer("The module is hidden");
                }
            } else {
                ChatUtils.sendMessageToPlayer("Unknown module (" + args[0] + ")");
            }
        } else {
            this.syntaxError();
        }
    }
}