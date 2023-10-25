package club.async.command.impl;

import club.async.Async;
import club.async.command.Command;
import club.async.module.Module;
import club.async.util.ChatUtil;
import org.lwjgl.input.Keyboard;

@Command.Info(name = "Bind", description = "Bind Modules", usage = ".bind <module> <key>", aliases = {"bind", "b"})
public class BindCommand extends Command {

    @Override
    public void execute(String[] args) {
        Module module = Async.INSTANCE.getModuleManager().getModule(args[0]);
        if(module != null) {
            module.setKey(Keyboard.getKeyIndex(args[1].toUpperCase()));
        } else {
            ChatUtil.addChatMessage("Invalid Module");
        }
    }
}
