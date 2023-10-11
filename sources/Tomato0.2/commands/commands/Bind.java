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
import cow.milkgod.cheese.module.Module;
import cow.milkgod.cheese.module.ModuleManager;
import cow.milkgod.cheese.utils.Logger;
import org.lwjgl.input.Keyboard;

public class Bind
extends Command {
    public Bind() {
        super("Bind", "U fucked up!", new String[]{"b"}, "<mod> <key>", "Binds a mod to a key.");
    }

    @EventTarget
    public void onChatSend(EventTick event) {
        if (this.getState()) {
            try {
                String modd = EventChatSend.getMessage().split(" ")[1];
                String key = EventChatSend.getMessage().split(" ")[2];
                Cheese.getInstance();
                Module mod = Cheese.moduleManager.getModbyName(modd);
                Cheese.getInstance();
                Module alias = Cheese.moduleManager.getModByAlias(modd);
                if (key == "" || modd == "") {
                    Logger.logChat("U fucked up! bind <mod> <key>");
                }
                if (mod == null && alias == null) {
                    Logger.logChat("That module does not exist!");
                } else if (modd != "" && key != "") {
                    if (mod != null && alias == null) {
                        mod.setKeybind(Keyboard.getKeyIndex(key.toUpperCase()));
                        Logger.logChat("\u00a7e" + mod.getName() + "'s" + "\u00a77 bind set to key:" + "\u00a7a " + Keyboard.getKeyName(mod.getKeybind()));
                    } else if (alias != null && mod == null) {
                        alias.setKeybind(Keyboard.getKeyIndex(key.toUpperCase()));
                        Logger.logChat("\u00a7e" + alias.getName() + "'s" + "\u00a77 bind set to key:" + "\u00a7a " + Keyboard.getKeyName(alias.getKeybind()));
                    }
                }
            }
            catch (Exception e) {
                Logger.logChat("U fucked up! Bind <mod> <key>");
            }
            Cheese.getInstance();
            Cheese.fileManager.saveFiles();
            this.Toggle();
        }
    }
}

