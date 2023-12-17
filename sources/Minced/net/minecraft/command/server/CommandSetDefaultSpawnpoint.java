// 
// Decompiled by Procyon v0.5.36
// 

package net.minecraft.command.server;

import java.util.Collections;
import java.util.List;
import javax.annotation.Nullable;
import net.minecraft.command.CommandException;
import net.minecraft.util.math.BlockPos;
import net.minecraft.command.ICommand;
import net.minecraft.network.Packet;
import net.minecraft.network.play.server.SPacketSpawnPosition;
import net.minecraft.command.WrongUsageException;
import net.minecraft.server.MinecraftServer;
import net.minecraft.command.ICommandSender;
import net.minecraft.command.CommandBase;

public class CommandSetDefaultSpawnpoint extends CommandBase
{
    @Override
    public String getName() {
        return "setworldspawn";
    }
    
    @Override
    public int getRequiredPermissionLevel() {
        return 2;
    }
    
    @Override
    public String getUsage(final ICommandSender sender) {
        return "commands.setworldspawn.usage";
    }
    
    @Override
    public void execute(final MinecraftServer server, final ICommandSender sender, final String[] args) throws CommandException {
        BlockPos blockpos;
        if (args.length == 0) {
            blockpos = CommandBase.getCommandSenderAsPlayer(sender).getPosition();
        }
        else {
            if (args.length != 3 || sender.getEntityWorld() == null) {
                throw new WrongUsageException("commands.setworldspawn.usage", new Object[0]);
            }
            blockpos = CommandBase.parseBlockPos(sender, args, 0, true);
        }
        sender.getEntityWorld().setSpawnPoint(blockpos);
        server.getPlayerList().sendPacketToAllPlayers(new SPacketSpawnPosition(blockpos));
        CommandBase.notifyCommandListener(sender, this, "commands.setworldspawn.success", blockpos.getX(), blockpos.getY(), blockpos.getZ());
    }
    
    @Override
    public List<String> getTabCompletions(final MinecraftServer server, final ICommandSender sender, final String[] args, @Nullable final BlockPos targetPos) {
        return (args.length > 0 && args.length <= 3) ? CommandBase.getTabCompletionCoordinate(args, 0, targetPos) : Collections.emptyList();
    }
}
