/*
 * Decompiled with CFR 0.152.
 */
package net.minecraft.command.server;

import java.util.List;
import net.minecraft.command.CommandBase;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommand;
import net.minecraft.command.ICommandSender;
import net.minecraft.command.WrongUsageException;
import net.minecraft.network.play.server.S05PacketSpawnPosition;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.BlockPos;

public class CommandSetDefaultSpawnpoint
extends CommandBase {
    @Override
    public List<String> addTabCompletionOptions(ICommandSender iCommandSender, String[] stringArray, BlockPos blockPos) {
        return stringArray.length > 0 && stringArray.length <= 3 ? CommandSetDefaultSpawnpoint.func_175771_a(stringArray, 0, blockPos) : null;
    }

    @Override
    public String getCommandName() {
        return "setworldspawn";
    }

    @Override
    public int getRequiredPermissionLevel() {
        return 2;
    }

    @Override
    public void processCommand(ICommandSender iCommandSender, String[] stringArray) throws CommandException {
        BlockPos blockPos;
        if (stringArray.length == 0) {
            blockPos = CommandSetDefaultSpawnpoint.getCommandSenderAsPlayer(iCommandSender).getPosition();
        } else {
            if (stringArray.length != 3 || iCommandSender.getEntityWorld() == null) {
                throw new WrongUsageException("commands.setworldspawn.usage", new Object[0]);
            }
            blockPos = CommandSetDefaultSpawnpoint.parseBlockPos(iCommandSender, stringArray, 0, true);
        }
        iCommandSender.getEntityWorld().setSpawnPoint(blockPos);
        MinecraftServer.getServer().getConfigurationManager().sendPacketToAllPlayers(new S05PacketSpawnPosition(blockPos));
        CommandSetDefaultSpawnpoint.notifyOperators(iCommandSender, (ICommand)this, "commands.setworldspawn.success", blockPos.getX(), blockPos.getY(), blockPos.getZ());
    }

    @Override
    public String getCommandUsage(ICommandSender iCommandSender) {
        return "commands.setworldspawn.usage";
    }
}

