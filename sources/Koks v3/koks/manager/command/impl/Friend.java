package koks.manager.command.impl;

import koks.Koks;
import koks.manager.command.Command;
import koks.manager.command.CommandInfo;
import koks.manager.friends.FriendManager;

/**
 * @author kroko
 * @created on 14.10.2020 : 18:02
 */

@CommandInfo(name = "friend", alias = "f")
public class Friend extends Command {

    @Override
    public void execute(String[] args) {
        FriendManager fm = Koks.getKoks().friendManager;
        if(args.length == 0) {
            sendError("Usage", ".friend add <Name> <Alias>");
            sendError("Usage", ".friend remove <Name>");
        }else if(args.length == 2) {
            if(args[0].equalsIgnoreCase("remove") || args[0].equalsIgnoreCase("r")) {
                if(fm.isFriend(args[1])) {
                    fm.removeFriend(args[1]);
                    sendmsg("§cremoved §e" + args[1] + " §cfrom the friend list", true);
                }else{
                    sendError("NOT FOUND", args[1] + " is not a friend!");
                }
            }
        }else if(args.length == 3) {
            if(args[0].equalsIgnoreCase("add")) {
                if(!fm.isFriend(args[1])) {
                    fm.addFriend(args[1], args[2]);
                    sendmsg("§aadded §e" + args[1] + " §ato the friend list", true);
                }else{
                    sendError("Exist", args[1] + " is already your friend");
                }
            }
        }
    }
}
