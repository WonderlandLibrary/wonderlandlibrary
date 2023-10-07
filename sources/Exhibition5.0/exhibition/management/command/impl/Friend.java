// 
// Decompiled by Procyon v0.5.30
// 

package exhibition.management.command.impl;

import net.minecraft.util.EnumChatFormatting;
import exhibition.util.misc.ChatUtil;
import exhibition.management.friend.FriendManager;
import exhibition.management.command.Command;

public class Friend extends Command
{
    public Friend(final String[] names, final String description) {
        super(names, description);
    }
    
    @Override
    public void fire(final String[] args) {
        if (args == null || args.length < 2) {
            this.printUsage();
            return;
        }
        try {
            if (args[0].equalsIgnoreCase("add") || args[0].equalsIgnoreCase("a")) {
                if (FriendManager.isFriend(args[1])) {
                    ChatUtil.printChat("§4[§cE§4]§8 " + String.valueOf(args[1]) + " is already your friend.");
                }
                FriendManager.removeFriend(args[1]);
                FriendManager.addFriend(args[1], (args.length == 3) ? args[2] : args[1]);
                ChatUtil.printChat("§4[§cE§4]§8 Added " + args[1]);
            }
            else if (args[0].equalsIgnoreCase("del") || args[0].equalsIgnoreCase("d")) {
                if (FriendManager.isFriend(args[1])) {
                    FriendManager.removeFriend(args[1]);
                    ChatUtil.printChat("§4[§cE§4]§8 Removed friend: " + args[1]);
                }
                else {
                    ChatUtil.printChat("§4[§cE§4]§8 " + String.valueOf(args[1]) + " is not your friend.");
                }
            }
        }
        catch (NullPointerException e) {
            this.printUsage();
        }
    }
    
    @Override
    public String getUsage() {
        return "friend <add/remove> " + EnumChatFormatting.RESET + "<name> " + EnumChatFormatting.RESET + "<alias>";
    }
}
