// 
// Decompiled by Procyon v0.5.30
// 

package net.minecraft.command;

import net.minecraft.world.storage.WorldInfo;
import net.minecraft.server.MinecraftServer;

public class CommandToggleDownfall extends CommandBase
{
    private static final String __OBFID = "CL_00001184";
    
    @Override
    public String getCommandName() {
        return "toggledownfall";
    }
    
    @Override
    public int getRequiredPermissionLevel() {
        return 2;
    }
    
    @Override
    public String getCommandUsage(final ICommandSender sender) {
        return "commands.downfall.usage";
    }
    
    @Override
    public void processCommand(final ICommandSender sender, final String[] args) throws CommandException {
        this.toggleDownfall();
        CommandBase.notifyOperators(sender, this, "commands.downfall.success", new Object[0]);
    }
    
    protected void toggleDownfall() {
        final WorldInfo var1 = MinecraftServer.getServer().worldServers[0].getWorldInfo();
        var1.setRaining(!var1.isRaining());
    }
}
