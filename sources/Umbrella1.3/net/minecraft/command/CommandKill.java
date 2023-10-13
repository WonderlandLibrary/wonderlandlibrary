/*
 * Decompiled with CFR 0.150.
 */
package net.minecraft.command;

import net.minecraft.command.CommandBase;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommand;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayerMP;

public class CommandKill
extends CommandBase {
    private static final String __OBFID = "CL_00000570";

    @Override
    public String getCommandName() {
        return "kill";
    }

    @Override
    public int getRequiredPermissionLevel() {
        return 2;
    }

    @Override
    public String getCommandUsage(ICommandSender sender) {
        return "commands.kill.usage";
    }

    @Override
    public void processCommand(ICommandSender sender, String[] args) throws CommandException {
        if (args.length == 0) {
            EntityPlayerMP var4 = CommandKill.getCommandSenderAsPlayer(sender);
            var4.func_174812_G();
            CommandKill.notifyOperators(sender, (ICommand)this, "commands.kill.successful", var4.getDisplayName());
        } else {
            Entity var3 = CommandKill.func_175768_b(sender, args[0]);
            var3.func_174812_G();
            CommandKill.notifyOperators(sender, (ICommand)this, "commands.kill.successful", var3.getDisplayName());
        }
    }

    @Override
    public boolean isUsernameIndex(String[] args, int index) {
        return index == 0;
    }
}

