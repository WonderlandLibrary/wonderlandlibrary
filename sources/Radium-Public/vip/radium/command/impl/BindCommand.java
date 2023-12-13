package vip.radium.command.impl;

import net.minecraft.util.EnumChatFormatting;
import org.lwjgl.input.Keyboard;
import vip.radium.RadiumClient;
import vip.radium.command.Command;
import vip.radium.command.CommandExecutionException;
import vip.radium.module.Module;
import vip.radium.utils.Wrapper;

public class BindCommand implements Command {

    @Override
    public String[] getAliases() {
        return new String[]{"bind", "b", "keybind"};
    }

    @Override
    public void execute(String[] arguments) throws CommandExecutionException {
        if(arguments.length != 3)
            throw new CommandExecutionException(getUsage());

        Module module = RadiumClient.getInstance().getModuleManager().getModule(arguments[1]);
        int key = Keyboard.getKeyIndex(arguments[2].toUpperCase());

        if(module == null) {
            Wrapper.addChatMessage(EnumChatFormatting.RED + arguments[1] + " \u00A77is an invalid module");
            return;
        }

        module.setKey(key);
        Wrapper.addChatMessage(String.format("Bound \u00A76%s \u00A77to \u00A76%s", module.getLabel(), key == 0 ? "none" : arguments[2].toUpperCase()));
    }

    @Override
    public String getUsage() {
        return "bind/b/keybind <module> <key>";
    }
}
