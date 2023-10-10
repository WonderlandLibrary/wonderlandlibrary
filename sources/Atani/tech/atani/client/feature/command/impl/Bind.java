package tech.atani.client.feature.command.impl;

import org.lwjgl.input.Keyboard;
import tech.atani.client.feature.command.data.CommandInfo;
import tech.atani.client.feature.module.Module;
import tech.atani.client.feature.module.storage.ModuleStorage;
import tech.atani.client.feature.command.Command;

@CommandInfo(name = "bind", description = "bind modules to a key")
public class Bind extends Command {

    @Override
    public boolean execute(String[] args) {
        if(args.length == 2) {
            final Module module = ModuleStorage.getInstance().getModule(args[0]);
            if(module != null) {
                final int key = Keyboard.getKeyIndex(args[1].toUpperCase());
                module.setKey(key);
                sendMessage("Key set to §e§l" + args[1].toUpperCase());
            }else{
                sendError("DOES NOT EXIST", "§aModule §l" + args[0] + " §anot found!");
            }
        } else if (args.length == 0) {
            sendHelp(this, "[Module] [Key]");
        } else {
            return false;
        }
        return true;
    }
}