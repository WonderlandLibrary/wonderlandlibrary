// 
// Decompiled by Procyon v0.5.30
// 

package net.minecraft.command;

import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayerMP;

public class CommandKill extends CommandBase
{
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
    public String getCommandUsage(final ICommandSender sender) {
        return "commands.kill.usage";
    }
    
    @Override
    public void processCommand(final ICommandSender sender, final String[] args) throws CommandException {
        if (args.length == 0) {
            final EntityPlayerMP var4 = CommandBase.getCommandSenderAsPlayer(sender);
            var4.func_174812_G();
            CommandBase.notifyOperators(sender, this, "commands.kill.successful", var4.getDisplayName());
        }
        else {
            final Entity var5 = CommandBase.func_175768_b(sender, args[0]);
            var5.func_174812_G();
            CommandBase.notifyOperators(sender, this, "commands.kill.successful", var5.getDisplayName());
        }
    }
    
    @Override
    public boolean isUsernameIndex(final String[] args, final int index) {
        return index == 0;
    }
}
