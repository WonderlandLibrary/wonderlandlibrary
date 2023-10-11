/*
 * Decompiled with CFR 0_115.
 */
package cow.milkgod.cheese.commands.commands;

import com.darkmagician6.eventapi.EventTarget;
import cow.milkgod.cheese.commands.Command;
import cow.milkgod.cheese.events.EventChatSend;
import cow.milkgod.cheese.events.EventTick;
import cow.milkgod.cheese.people.EnemyManager;
import cow.milkgod.cheese.utils.Logger;
import java.util.ArrayList;

public class Enemy
extends Command {
    private boolean realnameBoolean;

    public Enemy() {
        super("Enemy", null, new String[]{"enemies", "enem"}, "<add | remove | clear>", "Adds or deletes Enemies. Also clears them.");
    }

    @EventTarget
    public void onTick(EventTick tick) {
        block15 : {
            try {
                String cmd2 = EventChatSend.getMessage().split(" ")[1];
                if (cmd2.equalsIgnoreCase("add")) {
                    try {
                        String name = EventChatSend.getMessage().split(" ")[2];
                        if (!cmd2.equalsIgnoreCase("add") || name.equalsIgnoreCase(" ")) break block15;
                        if (!EnemyManager.isEnemy(name)) {
                            EnemyManager.addEnemy(name);
                            Logger.logChat("You have added \u00a7e" + name + " \u00a77to your Enemy list.");
                            break block15;
                        }
                        Logger.logChat("That player is already a Enemy or isn't online!");
                    }
                    catch (Exception e) {
                        Logger.logChat("U fucked up! Enemy add <name>");
                    }
                    break block15;
                }
                if (cmd2.equalsIgnoreCase("remove")) {
                    try {
                        String name = EventChatSend.getMessage().split(" ")[2];
                        if (!cmd2.equalsIgnoreCase("remove")) break block15;
                        if (EnemyManager.isEnemy(name)) {
                            EnemyManager.deleteEnemy(name);
                            Logger.logChat("You have removed \u00a7e" + name + "\u00a77 from your Enemies list.");
                            break block15;
                        }
                        Logger.logChat(String.valueOf(String.valueOf(name)) + " is not a Enemy!");
                    }
                    catch (Exception e) {
                        Logger.logChat("U fucked up! Enemy remove <name>");
                    }
                    break block15;
                }
                if (cmd2.equalsIgnoreCase("clear")) {
                    try {
                        Logger.logChat("Cleared Enemies!");
                        EnemyManager.Enemies.clear();
                    }
                    catch (Exception e) {}
                } else if (!(cmd2.equalsIgnoreCase("clear") && cmd2.equalsIgnoreCase("add") && cmd2.equalsIgnoreCase("remove"))) {
                    Logger.logChat("Invalid option! Enemy <add | remove | clear>");
                }
            }
            catch (Exception e2) {
                Logger.logChat("Invalid option! Enemy <add | remove | clear>");
            }
        }
        this.Toggle();
    }
}

