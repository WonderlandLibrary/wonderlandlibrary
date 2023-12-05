package rip.helium.cheat.commands.cmds;

import rip.helium.ChatUtil;
import rip.helium.cheat.FriendManager;
import rip.helium.cheat.commands.Command;

public class Friend extends Command {

    public Friend() {
        super("friend", "af");
    }

    @Override
    public void run(String[] args) {
        try {
            if (!FriendManager.isFriend(args[1])) {
                if (args.length >= 3) {
                    FriendManager.friends.add(args[1]);
                    ChatUtil.chat("Friend " + args[1] + " added as " + args[2] + ".");
                } else {
                    FriendManager.friends.add(args[1]);
                    ChatUtil.chat("Friend " + args[1] + " added.");
                }
            } else {
                FriendManager.friends.remove(args[1]);
                ChatUtil.chat("Friend " + args[1] + " removed.");
            }
        } catch (Exception e) {
            ChatUtil.chat("Error (Invalid arguments?)");
        }
    }

}
