// 
// Decompiled by Procyon v0.5.30
// 

package net.minecraft.command.server;

import java.util.List;
import net.minecraft.command.CommandException;
import net.minecraft.util.BlockPos;
import net.minecraft.command.ICommand;
import net.minecraft.network.Packet;
import net.minecraft.network.play.server.S05PacketSpawnPosition;
import net.minecraft.server.MinecraftServer;
import net.minecraft.command.WrongUsageException;
import net.minecraft.command.ICommandSender;
import net.minecraft.command.CommandBase;

public class CommandSetDefaultSpawnpoint extends CommandBase
{
    private static final String __OBFID = "CL_00000973";
    
    @Override
    public String getCommandName() {
        return "setworldspawn";
    }
    
    @Override
    public int getRequiredPermissionLevel() {
        return 2;
    }
    
    @Override
    public String getCommandUsage(final ICommandSender sender) {
        return "commands.setworldspawn.usage";
    }
    
    @Override
    public void processCommand(final ICommandSender sender, final String[] args) throws CommandException {
        BlockPos var3;
        if (args.length == 0) {
            var3 = CommandBase.getCommandSenderAsPlayer(sender).getPosition();
        }
        else {
            if (args.length != 3 || sender.getEntityWorld() == null) {
                throw new WrongUsageException("commands.setworldspawn.usage", new Object[0]);
            }
            var3 = CommandBase.func_175757_a(sender, args, 0, true);
        }
        sender.getEntityWorld().setSpawnLocation(var3);
        MinecraftServer.getServer().getConfigurationManager().sendPacketToAllPlayers(new S05PacketSpawnPosition(var3));
        CommandBase.notifyOperators(sender, this, "commands.setworldspawn.success", var3.getX(), var3.getY(), var3.getZ());
    }
    
    @Override
    public List addTabCompletionOptions(final ICommandSender sender, final String[] args, final BlockPos pos) {
        return (args.length > 0 && args.length <= 3) ? CommandBase.func_175771_a(args, 0, pos) : null;
    }
}
