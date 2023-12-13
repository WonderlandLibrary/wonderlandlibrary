// 
// Decompiled by Procyon v0.5.36
// 

package com.elementars.eclient.command.commands;

import com.elementars.eclient.friend.Nicknames;
import com.elementars.eclient.command.Command;

public class NicknameCommand extends Command
{
    public NicknameCommand() {
        super("nickname", "adds or deletes friends", new String[] { "set", "remove" });
    }
    
    @Override
    public void execute(final String[] args) {
        if (args.length < 2) {
            Command.sendChatMessage("Usage: .nickname set/remove (name) (nickname)");
            return;
        }
        if (args[1].equalsIgnoreCase("help")) {
            this.showSyntax(args[0]);
        }
        if (args.length < 3) {
            Command.sendChatMessage("Specify a username");
            return;
        }
        if (args.length < 4 && !args[1].equalsIgnoreCase("remove")) {
            Command.sendChatMessage("Specify a nickname");
            return;
        }
        if (args[1].equalsIgnoreCase("set")) {
            Nicknames.addNickname(args[2], args[3]);
            Command.sendChatMessage("Set nickname for &b" + args[2]);
        }
        else if (args[1].equalsIgnoreCase("remove")) {
            if (Nicknames.hasNickname(args[2])) {
                Nicknames.removeNickname(args[2]);
                Command.sendChatMessage("Nickname has been removed for &b" + args[2]);
            }
            else {
                Command.sendChatMessage("&b" + args[2] + "&f doesn't have a nickname");
            }
        }
        else {
            Command.sendChatMessage("Unknown attribute '" + args[1] + "'");
        }
    }
}
