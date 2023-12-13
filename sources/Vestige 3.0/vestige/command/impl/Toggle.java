package vestige.command.impl;

import vestige.Vestige;
import vestige.command.Command;
import vestige.module.Module;
import vestige.util.misc.LogUtil;

public class Toggle extends Command {

    public Toggle() {
        super("Toggle", "Turns on or off the specified module.", "t");
    }

    @Override
    public void onCommand(String[] args) {
        if(args.length >= 2) {
            Module module = Vestige.instance.getModuleManager().getModuleByNameNoSpace(args[1]);

            if(module != null) {
                module.toggle();

                LogUtil.addChatMessage((module.isEnabled() ? "Enabled " : "Disabled ") + module.getName());
            }
        } else {
            LogUtil.addChatMessage("Usage : .t/toggle modulename");
        }
    }
}