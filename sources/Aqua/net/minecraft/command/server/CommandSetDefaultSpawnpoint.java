package net.minecraft.command.server;

import java.util.List;
import net.minecraft.command.CommandBase;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommand;
import net.minecraft.command.ICommandSender;
import net.minecraft.command.WrongUsageException;
import net.minecraft.network.Packet;
import net.minecraft.network.play.server.S05PacketSpawnPosition;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.BlockPos;

public class CommandSetDefaultSpawnpoint
extends CommandBase {
    public String getCommandName() {
        return "setworldspawn";
    }

    public int getRequiredPermissionLevel() {
        return 2;
    }

    public String getCommandUsage(ICommandSender sender) {
        return "commands.setworldspawn.usage";
    }

    public void processCommand(ICommandSender sender, String[] args) throws CommandException {
        BlockPos blockpos;
        if (args.length == 0) {
            blockpos = CommandSetDefaultSpawnpoint.getCommandSenderAsPlayer((ICommandSender)sender).getPosition();
        } else {
            if (args.length != 3 || sender.getEntityWorld() == null) {
                throw new WrongUsageException("commands.setworldspawn.usage", new Object[0]);
            }
            blockpos = CommandSetDefaultSpawnpoint.parseBlockPos((ICommandSender)sender, (String[])args, (int)0, (boolean)true);
        }
        sender.getEntityWorld().setSpawnPoint(blockpos);
        MinecraftServer.getServer().getConfigurationManager().sendPacketToAllPlayers((Packet)new S05PacketSpawnPosition(blockpos));
        CommandSetDefaultSpawnpoint.notifyOperators((ICommandSender)sender, (ICommand)this, (String)"commands.setworldspawn.success", (Object[])new Object[]{blockpos.getX(), blockpos.getY(), blockpos.getZ()});
    }

    public List<String> addTabCompletionOptions(ICommandSender sender, String[] args, BlockPos pos) {
        return args.length > 0 && args.length <= 3 ? CommandSetDefaultSpawnpoint.func_175771_a((String[])args, (int)0, (BlockPos)pos) : null;
    }
}
