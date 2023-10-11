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
import cow.milkgod.cheese.module.Module;
import cow.milkgod.cheese.module.ModuleManager;
import cow.milkgod.cheese.utils.ComparatorAlphabeticalOrder;
import cow.milkgod.cheese.utils.Logger;
import java.text.Collator;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Locale;

public class ListStuff
extends Command {
    private ArrayList<Module> mods;
    private ArrayList<Command> cmds;
    private Collator collator = Collator.getInstance(Locale.US);

    public ListStuff() {
        super("List", null, new String[]{"ls"}, "<Mods | Commands>", "Displays the avaliable mods or commands.");
    }

    @EventTarget
    public void onChatSend(EventTick event) {
        if (this.getState()) {
            try {
                StringBuilder list;
                this.mods = new ArrayList();
                this.cmds = new ArrayList();
                Cheese.getInstance();
                ModuleManager moduleManager = Cheese.moduleManager;
                for (Module m2 : ModuleManager.activeModules) {
                    if (this.mods.contains(m2)) continue;
                    this.mods.add(m2);
                }
                Cheese.getInstance();
                CommandManager commandManager = Cheese.commandManager;
                for (Command cmd3 : CommandManager.activeCommands) {
                    if (this.cmds.contains(cmd3)) continue;
                    this.cmds.add(cmd3);
                }
                String bol2 = EventChatSend.getMessage().split(" ")[1];
                if (!bol2.equalsIgnoreCase("modules") && !bol2.equalsIgnoreCase("mods")) {
                    if (!bol2.equalsIgnoreCase("commands") && !bol2.equalsIgnoreCase("cmds")) {
                        Logger.logChat("Invalid Option! List <Mods | Commands>");
                    } else {
                        try {
                            this.cmds.sort((cmd1, cmd2) -> this.collator.compare(cmd1.getName(), cmd2.getName()));
                            list = new StringBuilder("\u00a7eCommands (" + this.cmds.size() + "): ");
                            for (Command cmd4 : this.cmds) {
                                list.append("\u00a77").append(cmd4.getName()).append("\u00a78 - ");
                            }
                            Logger.logChat(list.toString().substring(0, list.toString().length() - 2));
                        }
                        catch (Exception e) {
                            Logger.logChat("U fucked up! List <Mods | Commands>");
                        }
                    }
                } else {
                    try {
                        this.mods.sort(new ComparatorAlphabeticalOrder());
                        list = new StringBuilder("\u00a7eMods (" + this.mods.size() + "): ");
                        for (Module mod : this.mods) {
                            list.append(mod.getState() ? "\u00a7e" : "\u00a77").append(mod.getName()).append("\u00a78 - ");
                        }
                        Logger.logChat(list.toString().substring(0, list.toString().length() - 2));
                    }
                    catch (Exception e) {
                        Logger.logChat("U fucked up! List <Mods | Commands>");
                    }
                }
            }
            catch (Exception e2) {
                Logger.logChat("U fucked up! List <Mods | Commands>");
            }
            this.Toggle();
        }
    }
}

