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
import cow.milkgod.cheese.people.FriendManager;
import cow.milkgod.cheese.utils.Logger;
import java.util.ArrayList;

public class Friends
extends Command {
    private boolean realnameBoolean;

    public Friends() {
        super("friend", null, new String[]{"friends", "ally"}, "<add | remove | realname | clear>", "Adds or deletes friends. Also clears them.");
    }

    @EventTarget
    public void onTick(EventTick tick) {
        block21 : {
            try {
                String cmd2 = EventChatSend.getMessage().split(" ")[1];
                if (cmd2.equalsIgnoreCase("add")) {
                    try {
                        String name = EventChatSend.getMessage().split(" ")[2];
                        String alias = EventChatSend.getMessage().split(" ")[3];
                        if (alias == null) {
                            alias = name;
                        }
                        if (!cmd2.equalsIgnoreCase("add") || name.equalsIgnoreCase(" ") || alias.equalsIgnoreCase(" ")) break block21;
                        if (!FriendManager.isFriend(name)) {
                            FriendManager.addFriend(name, alias);
                            Logger.logChat("You have added \u00a7e" + name.replaceFirst(name.substring(1), new StringBuilder("\u00a7e").append(name.substring(1)).toString()) + " \u00a77to your friend list as \u00a7e" + alias);
                            break block21;
                        }
                        Logger.logChat("That player is already a friend or isn't online!");
                    }
                    catch (Exception e) {
                        Logger.logChat("U fucked up! friend add <name> <alias>");
                    }
                    break block21;
                }
                if (cmd2.equalsIgnoreCase("remove")) {
                    try {
                        String name = EventChatSend.getMessage().split(" ")[2];
                        if (!cmd2.equalsIgnoreCase("remove")) break block21;
                        if (FriendManager.isFriend(name)) {
                            FriendManager.deleteFriend(name);
                            Logger.logChat("You have removed \u00a7e" + name.replaceFirst(name.substring(1), new StringBuilder("\u00a7e").append(name.substring(1)).toString()) + "\u00a77 from your friends list.");
                            break block21;
                        }
                        Logger.logChat(String.valueOf(String.valueOf(name)) + " is not a friend!");
                    }
                    catch (Exception e) {
                        Logger.logChat("U fucked up! friend remove <name>");
                    }
                    break block21;
                }
                if (!cmd2.equalsIgnoreCase("realname") && !cmd2.equalsIgnoreCase("name")) {
                    if (cmd2.equalsIgnoreCase("clear")) {
                        try {
                            Logger.logChat("Cleared Friends!");
                            FriendManager.friends.clear();
                        }
                        catch (Exception e) {}
                    } else if (!(cmd2.equalsIgnoreCase("clear") && cmd2.equalsIgnoreCase("add") && cmd2.equalsIgnoreCase("remove"))) {
                        Logger.logChat("Invalid option! Options: " + this.getArguments());
                    }
                    break block21;
                }
                try {
                    String alias2 = EventChatSend.getMessage().split(" ")[2];
                    String name2 = "";
                    for (FriendManager.Friend theFriend : FriendManager.friends) {
                        if (theFriend.getAlias().equalsIgnoreCase(alias2)) {
                            this.realnameBoolean = true;
                            name2 = theFriend.getName();
                            Logger.logChat("\u00a7e" + alias2 + "\u00a7e's\u00a77 " + "realname is: \u00a7e" + name2.replaceFirst(name2.substring(1), new StringBuilder("\u00a7e").append(name2.substring(1)).toString()));
                            continue;
                        }
                        if (this.realnameBoolean) continue;
                        Logger.logChat(String.valueOf(String.valueOf(alias2)) + " is not a valid alias!");
                    }
                }
                catch (Exception e) {
                    Logger.logChat("U fucked up! friend realname <alias>");
                }
            }
            catch (Exception e2) {
                Logger.logChat("Invalid option! Options: " + this.getArguments());
            }
        }
        Cheese.getInstance();
        Cheese.fileManager.saveFiles();
        this.Toggle();
    }
}

