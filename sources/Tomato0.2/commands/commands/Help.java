/*
 * Decompiled with CFR 0_115.
 */
package cow.milkgod.cheese.commands.commands;

import com.darkmagician6.eventapi.EventTarget;
import cow.milkgod.cheese.Cheese;
import cow.milkgod.cheese.commands.Command;
import cow.milkgod.cheese.commands.CommandManager;
import cow.milkgod.cheese.events.EventChatSend;
import cow.milkgod.cheese.events.EventTick;
import cow.milkgod.cheese.module.Category;
import cow.milkgod.cheese.module.Module;
import cow.milkgod.cheese.module.ModuleManager;
import cow.milkgod.cheese.utils.Logger;
import org.lwjgl.input.Keyboard;

public class Help
extends Command {
    public Help() {
        super("Help", null, null, "<module | command> <mod | cmd>", "Gives you some help. [duh]");
    }

    @EventTarget
    public void onChatSend(EventTick event) {
        if (this.getState()) {
            block19 : {
                try {
                    String modorcmd = EventChatSend.getMessage().split(" ")[1];
                    if (!modorcmd.equalsIgnoreCase("module") && !modorcmd.equalsIgnoreCase("mod")) {
                        if (!modorcmd.equalsIgnoreCase("command") && !modorcmd.equalsIgnoreCase("cmd")) {
                            Logger.logChat("U fucked up! Help <Module | Command>");
                            break block19;
                        }
                        try {
                            Cheese.getInstance();
                            Command cmd2 = Cheese.commandManager.getCommandbyName(EventChatSend.getMessage().split(" ")[2]);
                            if (cmd2 == null) {
                                Cheese.getInstance();
                                cmd2 = Cheese.commandManager.getCommandbyAlias(EventChatSend.getMessage().split(" ")[2]);
                            }
                            if (cmd2 != null) {
                                Logger.logChat("Command: \u00a7e" + cmd2.getName());
                                if (cmd2.getAlias() != null && cmd2.getAlias().length != 0 && !cmd2.getAlias()[0].equalsIgnoreCase("")) {
                                    String[] alias2 = cmd2.getAlias();
                                    if (alias2.length != 0) {
                                        String alias = alias2[0];
                                        StringBuilder list = new StringBuilder("\u00a7eAliases: \u00a77" + alias);
                                        int loop = 1;
                                        while (loop < cmd2.getAlias().length) {
                                            list.append("\u00a78 - ").append("\u00a77" + cmd2.getAlias()[loop]);
                                            ++loop;
                                        }
                                        Logger.logChat(list.toString());
                                    }
                                } else {
                                    Logger.logChat("No aliases.");
                                }
                                Logger.logChat("Arguments: \u00a7e" + (cmd2.getArguments() == null || cmd2.getArguments().equalsIgnoreCase("") ? "none" : new StringBuilder(String.valueOf(String.valueOf(cmd2.getName()))).append(" ").append(cmd2.getArguments()).toString()));
                                Logger.logChat("Description: \u00a7e" + cmd2.getDescription());
                                break block19;
                            }
                            Logger.logChat("Invalid Command! Do -list commands for commands!");
                        }
                        catch (Exception e) {
                            Logger.logChat("U fucked up! Help command <command>");
                        }
                        break block19;
                    }
                    try {
                        Cheese.getInstance();
                        Module mod = Cheese.moduleManager.getModbyName(EventChatSend.getMessage().split(" ")[2]);
                        if (mod == null) {
                            Cheese.getInstance();
                            mod = Cheese.moduleManager.getModByAlias(EventChatSend.getMessage().split(" ")[2]);
                        }
                        if (mod != null) {
                            Logger.logChat("Mod: \u00a7e" + mod.getName());
                            Logger.logChat("KeyBind: \u00a7e" + Keyboard.getKeyName(mod.getKeybind()));
                            Logger.logChat("Category: \u00a7e" + (Object)((Object)mod.getCategory()));
                            String[] alias3 = mod.getAlias();
                            if (alias3.length != 0) {
                                String alias = alias3[0];
                                StringBuilder list = new StringBuilder("\u00a7eAliases: \u00a77" + alias);
                                int loop = 1;
                                while (loop < mod.getAlias().length) {
                                    list.append("\u00a78 - ").append("\u00a77" + mod.getAlias()[loop]);
                                    ++loop;
                                }
                                Logger.logChat(list.toString());
                            }
                            Logger.logChat("Description: \u00a7e" + mod.getDescription());
                            break block19;
                        }
                        Logger.logChat("Invalid Module! Do -list mods for modules!");
                    }
                    catch (Exception e) {
                        Logger.logChat("U fucked up! Help module <module>");
                    }
                }
                catch (Exception e2) {
                    Logger.logChat("U fucked up! Do help <module | command> <mod | cmd>");
                }
            }
            this.Toggle();
        }
    }
}

