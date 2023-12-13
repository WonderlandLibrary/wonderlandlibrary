// 
// Decompiled by Procyon v0.5.36
// 

package Lavish.cmd.impl;

import Lavish.cmd.Command;
import Lavish.utils.misc.Console;
import Lavish.cmd.CommandManager;
import java.util.List;
import net.minecraft.client.entity.EntityPlayerSP;
import Lavish.cmd.CommandExecutor;

public class Help implements CommandExecutor
{
    @Override
    public void execute(final EntityPlayerSP sender, final List<String> args) {
        CommandManager.getCommands().forEach(command -> Console.sendChatToPlayerWithPrefix(String.valueOf(command.getName().toUpperCase()) + " - " + command.getDesc()));
    }
}
