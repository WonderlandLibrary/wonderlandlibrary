/*
 * Decompiled with CFR 0.150.
 */
package net.minecraft.command;

import java.util.Collections;
import java.util.List;
import javax.annotation.Nullable;
import net.minecraft.command.CommandBase;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommand;
import net.minecraft.command.ICommandSender;
import net.minecraft.command.WrongUsageException;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.math.BlockPos;

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
    public void execute(MinecraftServer server, ICommandSender sender, String[] args) throws CommandException {
        BlockPos blockpos;
        if (args.length > 1 && args.length < 4) {
            throw new WrongUsageException("commands.spawnpoint.usage", new Object[0]);
        }
        EntityPlayerMP entityplayermp = args.length > 0 ? CommandSetSpawnpoint.getPlayer(server, sender, args[0]) : CommandSetSpawnpoint.getCommandSenderAsPlayer(sender);
        BlockPos blockPos = blockpos = args.length > 3 ? CommandSetSpawnpoint.parseBlockPos(sender, args, 1, true) : entityplayermp.getPosition();
        if (entityplayermp.world != null) {
            entityplayermp.setSpawnPoint(blockpos, true);
            CommandSetSpawnpoint.notifyCommandListener(sender, (ICommand)this, "commands.spawnpoint.success", entityplayermp.getName(), blockpos.getX(), blockpos.getY(), blockpos.getZ());
        }
    }

    @Override
    public List<String> getTabCompletionOptions(MinecraftServer server, ICommandSender sender, String[] args, @Nullable BlockPos pos) {
        if (args.length == 1) {
            return CommandSetSpawnpoint.getListOfStringsMatchingLastWord(args, server.getAllUsernames());
        }
        return args.length > 1 && args.length <= 4 ? CommandSetSpawnpoint.getTabCompletionCoordinate(args, 1, pos) : Collections.emptyList();
    }

    @Override
    public boolean isUsernameIndex(String[] args, int index) {
        return index == 0;
    }
}

