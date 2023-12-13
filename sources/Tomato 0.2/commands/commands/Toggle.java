/*
 * Decompiled with CFR 0_115.
 */
package cow.milkgod.cheese.commands.commands;

import com.darkmagician6.eventapi.EventTarget;
import cow.milkgod.cheese.Cheese;
import cow.milkgod.cheese.commands.Command;
import cow.milkgod.cheese.events.EventChatSend;
import cow.milkgod.cheese.events.EventTick;
import cow.milkgod.cheese.files.FileManager;
import cow.milkgod.cheese.files.files.modulefiles.Modules;
import cow.milkgod.cheese.module.Module;
import cow.milkgod.cheese.module.ModuleManager;
import cow.milkgod.cheese.utils.Logger;
import java.io.IOException;

public class Toggle
extends Command {
    public Toggle() {
        super("Toggle", null, new String[]{"t"}, "<module>", "Toggles a module.");
    }

    @EventTarget
    public void onEvent(EventTick tick) {
        try {
            String module = EventChatSend.getMessage().split(" ")[1];
            Cheese.getInstance();
            Module name = Cheese.moduleManager.getModbyName(module);
            Cheese.getInstance();
            Module alias = Cheese.moduleManager.getModByAlias(module);
            Module mod = null;
            if (name == null && alias != null) {
                mod = alias;
            } else if (name != null && alias == null) {
                mod = name;
            }
            if (mod != null) {
                mod.toggleModule();
                String state = mod.getState() ? "\u00a7a enabled" : "\u00a7c disabled";
                Logger.logChat("\u00a7e" + mod.getName() + "\u00a77 has been" + state + "\u00a77.");
            } else {
                Logger.logChat("That module does not exist!");
            }
        }
        catch (Exception e) {
            Logger.logChat("Unknown module!");
        }
        try {
            Cheese.getInstance();
            Cheese.fileManager.getFile(Modules.class).saveFile();
        }
        catch (IOException e) {
            e.printStackTrace();
        }
        this.Toggle();
    }
}

