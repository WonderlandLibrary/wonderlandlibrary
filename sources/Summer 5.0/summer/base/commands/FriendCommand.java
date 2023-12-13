package summer.base.commands;

import com.mojang.realmsclient.gui.ChatFormatting;
import summer.Summer;
import summer.base.manager.Command;
import summer.base.utilities.ChatUtils;

public class FriendCommand implements Command {

    private boolean handleFriend(String command, String name) {
        return handleFriend(command, name, name);
    }

    private boolean handleFriend(String command, String name, String nickname) {
        switch (command.toLowerCase()) {
            case "add": {
                Summer.INSTANCE.friendManager.addFriend(name, nickname);
                ChatUtils.sendMessage("Added \"" + name + "\".");
                return true;
            }
            case "del": {
                Summer.INSTANCE.friendManager.deleteFriend(name);
                ChatUtils.sendMessage("Deleted \"" + name + "\".");
                return true;
            }
            default: {
                return false;
            }
        }
    }

    @Override
    public boolean run(String[] args) {
        if (args.length == 3) {
            String command = args[1];
            String name = args[2];
            return handleFriend(command, name);
        } else if (args.length == 4) {
            String command = args[1];
            String name = args[2];
            String nickname = args[3];
            return handleFriend(command, name, nickname);
        }
        return false;
    }

    @Override
    public String usage() {
        return ChatFormatting.WHITE + "f | friend <add/del> <name>";
    }
}