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

public class Visible
extends Command {
    public Visible() {
        super("Visible", "Invalid Module/Arguments! ", new String[]{"vis"}, "<module>", "Makes a module show on the arraylist.");
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
                mod.setVisible(!mod.isVisible());
                String state = mod.isVisible() ? "\u00a7a now" : "\u00a7c no longer";
                Logger.logChat("\u00a7e" + mod.getName() + " \u00a77is" + state + " \u00a77visible" + "\u00a77.");
            } else {
                Logger.logChat(String.valueOf(String.valueOf(this.getErrorMessage())) + this.getArguments());
            }
        }
        catch (Exception e) {
            Logger.logChat(String.valueOf(String.valueOf(this.getErrorMessage())) + this.getArguments());
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

