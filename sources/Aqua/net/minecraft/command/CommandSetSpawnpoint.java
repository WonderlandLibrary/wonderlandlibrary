package net.minecraft.command;

import java.util.List;
import net.minecraft.command.CommandBase;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommand;
import net.minecraft.command.ICommandSender;
import net.minecraft.command.WrongUsageException;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.BlockPos;

public class CommandSetSpawnpoint
extends CommandBase {
    public String getCommandName() {
        return "spawnpoint";
    }

    public int getRequiredPermissionLevel() {
        return 2;
    }

    public String getCommandUsage(ICommandSender sender) {
        return "commands.spawnpoint.usage";
    }

    public void processCommand(ICommandSender sender, String[] args) throws CommandException {
        BlockPos blockpos;
        if (args.length > 1 && args.length < 4) {
            throw new WrongUsageException("commands.spawnpoint.usage", new Object[0]);
        }
        EntityPlayerMP entityplayermp = args.length > 0 ? CommandSetSpawnpoint.getPlayer((ICommandSender)sender, (String)args[0]) : CommandSetSpawnpoint.getCommandSenderAsPlayer((ICommandSender)sender);
        BlockPos blockPos = blockpos = args.length > 3 ? CommandSetSpawnpoint.parseBlockPos((ICommandSender)sender, (String[])args, (int)1, (boolean)true) : entityplayermp.getPosition();
        if (entityplayermp.worldObj != null) {
            entityplayermp.setSpawnPoint(blockpos, true);
            CommandSetSpawnpoint.notifyOperators((ICommandSender)sender, (ICommand)this, (String)"commands.spawnpoint.success", (Object[])new Object[]{entityplayermp.getName(), blockpos.getX(), blockpos.getY(), blockpos.getZ()});
        }
    }

    public List<String> addTabCompletionOptions(ICommandSender sender, String[] args, BlockPos pos) {
        return args.length == 1 ? CommandSetSpawnpoint.getListOfStringsMatchingLastWord((String[])args, (String[])MinecraftServer.getServer().getAllUsernames()) : (args.length > 1 && args.length <= 4 ? CommandSetSpawnpoint.func_175771_a((String[])args, (int)1, (BlockPos)pos) : null);
    }

    public boolean isUsernameIndex(String[] args, int index) {
        return index == 0;
    }
}
