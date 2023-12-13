package slavikcodd3r.rainbow.command.commands;

import org.lwjgl.input.Keyboard;

import slavikcodd3r.rainbow.command.Com;
import slavikcodd3r.rainbow.command.Command;
import slavikcodd3r.rainbow.module.Module;
import slavikcodd3r.rainbow.module.ModuleManager;
import slavikcodd3r.rainbow.utils.ClientUtils;

@Com(names = { "bind", "b" })
public class Bind extends Command
{
    @Override
    public void runCommand(final String[] args) {
        String modName = "";
        String keyName = "";
        if (args.length > 1) {
            modName = args[1];
            if (args.length > 2) {
                keyName = args[2];
            }
        }
        final Module module = ModuleManager.getModule(modName);
        if (module.getId().equalsIgnoreCase("null")) {
            ClientUtils.sendMessage("Unknown Module");
            return;
        }
        if (keyName == "") {
            ClientUtils.sendMessage(String.valueOf(module.getDisplayName()) + "'s bind cleared");
            module.setKeybind(0);
            ModuleManager.save();
            return;
        }
        module.setKeybind(Keyboard.getKeyIndex(keyName.toUpperCase()));
        ModuleManager.save();
        if (Keyboard.getKeyIndex(keyName.toUpperCase()) == 0) {
            ClientUtils.sendMessage("Bind cleared");
        }
        else {
            ClientUtils.sendMessage(String.valueOf(module.getDisplayName()) + " bind to " + keyName);
        }
    }
    
    @Override
    public String getHelp() {
        return "bind";
    }
}
