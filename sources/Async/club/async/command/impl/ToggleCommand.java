package club.async.command.impl;

import club.async.Async;
import club.async.command.Command;
import club.async.module.Module;
import club.async.util.ChatUtil;

@Command.Info(name = "Toggle", description = "Toggle Modules", usage = ".toggle <module>", aliases = {"toggle", "t"})
public class ToggleCommand extends Command {

    @Override
    public void execute(String[] args) {
        Module module = Async.INSTANCE.getModuleManager().getModule(args[0]);
        if(module != null) {
            module.toggle();
            ChatUtil.addChatMessage("Toggled module " + module.getName());
        } else {
            ChatUtil.addChatMessage("Invalid Module");
        }
    }
}
