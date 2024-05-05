/*
 * Decompiled with CFR 0.152.
 */
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
    @Override
    public String getCommandName() {
        return "spawnpoint";
    }

    @Override
    public int getRequiredPermissionLevel() {
        return 2;
    }

    @Override
    public String getCommandUsage(ICommandSender sender) {
        return "commands.spawnpoint.usage";
    }

    @Override
    public void processCommand(ICommandSender sender, String[] args) throws CommandException {
        if (args.length > 1 && args.length < 4) {
            throw new WrongUsageException("commands.spawnpoint.usage", new Object[0]);
        }
        EntityPlayerMP entityplayermp = args.length > 0 ? CommandSetSpawnpoint.getPlayer(sender, args[0]) : CommandSetSpawnpoint.getCommandSenderAsPlayer(sender);
        BlockPos blockpos = args.length > 3 ? CommandSetSpawnpoint.parseBlockPos(sender, args, 1, true) : entityplayermp.getPosition();
        if (entityplayermp.worldObj == null) return;
        entityplayermp.setSpawnPoint(blockpos, true);
        CommandSetSpawnpoint.notifyOperators(sender, (ICommand)this, "commands.spawnpoint.success", entityplayermp.getName(), blockpos.getX(), blockpos.getY(), blockpos.getZ());
    }

    @Override
    public List<String> addTabCompletionOptions(ICommandSender sender, String[] args, BlockPos pos) {
        List<String> list;
        if (args.length == 1) {
            list = CommandSetSpawnpoint.getListOfStringsMatchingLastWord(args, MinecraftServer.getServer().getAllUsernames());
            return list;
        }
        if (args.length <= 1) return null;
        if (args.length > 4) return null;
        list = CommandSetSpawnpoint.func_175771_a(args, 1, pos);
        return list;
    }

    @Override
    public boolean isUsernameIndex(String[] args, int index) {
        if (index != 0) return false;
        return true;
    }
}

