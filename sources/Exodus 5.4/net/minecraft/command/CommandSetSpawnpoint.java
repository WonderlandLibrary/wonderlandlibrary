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
    public String getCommandUsage(ICommandSender iCommandSender) {
        return "commands.spawnpoint.usage";
    }

    @Override
    public boolean isUsernameIndex(String[] stringArray, int n) {
        return n == 0;
    }

    @Override
    public String getCommandName() {
        return "spawnpoint";
    }

    @Override
    public int getRequiredPermissionLevel() {
        return 2;
    }

    @Override
    public void processCommand(ICommandSender iCommandSender, String[] stringArray) throws CommandException {
        BlockPos blockPos;
        if (stringArray.length > 1 && stringArray.length < 4) {
            throw new WrongUsageException("commands.spawnpoint.usage", new Object[0]);
        }
        EntityPlayerMP entityPlayerMP = stringArray.length > 0 ? CommandSetSpawnpoint.getPlayer(iCommandSender, stringArray[0]) : CommandSetSpawnpoint.getCommandSenderAsPlayer(iCommandSender);
        BlockPos blockPos2 = blockPos = stringArray.length > 3 ? CommandSetSpawnpoint.parseBlockPos(iCommandSender, stringArray, 1, true) : entityPlayerMP.getPosition();
        if (entityPlayerMP.worldObj != null) {
            entityPlayerMP.setSpawnPoint(blockPos, true);
            CommandSetSpawnpoint.notifyOperators(iCommandSender, (ICommand)this, "commands.spawnpoint.success", entityPlayerMP.getName(), blockPos.getX(), blockPos.getY(), blockPos.getZ());
        }
    }

    @Override
    public List<String> addTabCompletionOptions(ICommandSender iCommandSender, String[] stringArray, BlockPos blockPos) {
        return stringArray.length == 1 ? CommandSetSpawnpoint.getListOfStringsMatchingLastWord(stringArray, MinecraftServer.getServer().getAllUsernames()) : (stringArray.length > 1 && stringArray.length <= 4 ? CommandSetSpawnpoint.func_175771_a(stringArray, 1, blockPos) : null);
    }
}

