package wtf.dawn.command.impl;

import net.minecraft.world.gen.structure.StructureNetherBridgePieces;
import org.lwjgl.input.Keyboard;
import wtf.dawn.Dawn;
import wtf.dawn.command.Command;
import wtf.dawn.module.Module;

import java.security.Key;

public class Bind extends Command {

    public Bind() {
        super("Bind", "Binds a module", "bind <name> <key> | clear", "b");
    }

    @Override
    public void onCommand(String[] args, String command) {
        if (args.length == 2) {
            String moduleName = args[0];
            String keyName = args[1];

            boolean foundModule = false;

            for(Module module : Dawn.getInstance().getModuleManager().getModules()) {
                if(module.getName().equalsIgnoreCase(moduleName)) {
                    module.setKeyCode(Keyboard.getKeyIndex(keyName.toUpperCase()));
                    Dawn.addChatMessage(String.format("Bound %s to %s", module.getName(), Keyboard.getKeyName(module.getKeyCode())));
                    foundModule = true;

                    break;
                }
            }
            if(!foundModule) {
                Dawn.addChatMessage("Couldn't find module!");
            }

        }

        if (args.length == 1) {
            if(args[0].equalsIgnoreCase("clear")) {
                for(Module module : Dawn.getInstance().getModuleManager().getModules()) {
                    module.setKeyCode(Keyboard.KEY_NONE);
                }
            }
            Dawn.addChatMessage("Cleared all binds!");

        }

    }
}
