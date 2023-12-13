// 
// Decompiled by Procyon v0.5.36
// 

package Lavish.cmd.impl;

import Lavish.utils.misc.Console;
import Lavish.Client;
import java.util.List;
import net.minecraft.client.entity.EntityPlayerSP;
import Lavish.cmd.CommandExecutor;

public class ClientName implements CommandExecutor
{
    @Override
    public void execute(final EntityPlayerSP sender, final List<String> args) {
        if (args.size() == 1) {
            Client.instance.HUDName = args.get(0);
            Console.sendChatToPlayerWithPrefix("Client name set to " + args.get(0));
        }
        else {
            Console.sendChatToPlayerWithPrefix("Please enter a client name.");
        }
    }
}
