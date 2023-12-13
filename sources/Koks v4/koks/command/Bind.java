package koks.command;

import koks.api.registry.command.Command;
import koks.api.registry.file.FileRegistry;
import koks.api.registry.file.Files;
import koks.api.registry.module.Module;
import koks.api.registry.module.ModuleRegistry;
import koks.file.BindFile;
import org.lwjgl.input.Keyboard;

/**
 * Copyright 2020, Koks Team
 * Please don't use the code
 */

@Command.Info(name = "bind", description = "bind modules to a key")
public class Bind extends Command {

    @Override
    public boolean execute(String[] args) {
        if(args.length == 2) {
            final Module module = ModuleRegistry.getModule(args[0]);
            if(module != null) {
                final int key = Keyboard.getKeyIndex(args[1].toUpperCase());
                module.setKey(key);
                final Files files = FileRegistry.getFile(BindFile.class);
                FileRegistry.writeFile(files);
                sendMessage("Key set to §e§l" + args[1].toUpperCase());
            }else{
                sendError("NOT EXIST", "§aModule §l" + args[0] + " §anot found!");
            }
        } else if (args.length == 0) {
            sendHelp(this, "[Module] [Key]");
        } else {
            return false;
        }
        return true;
    }
}
