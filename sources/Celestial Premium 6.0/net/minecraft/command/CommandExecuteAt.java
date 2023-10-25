/*
 * Decompiled with CFR 0.150.
 */
package net.minecraft.command;

import java.util.Collections;
import java.util.List;
import javax.annotation.Nullable;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.command.CommandBase;
import net.minecraft.command.CommandException;
import net.minecraft.command.CommandSenderWrapper;
import net.minecraft.command.ICommandManager;
import net.minecraft.command.ICommandSender;
import net.minecraft.command.WrongUsageException;
import net.minecraft.entity.Entity;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;

public class CommandExecuteAt
extends CommandBase {
    @Override
    public String getCommandName() {
        return "execute";
    }

    @Override
    public int getRequiredPermissionLevel() {
        return 2;
    }

    @Override
    public String getCommandUsage(ICommandSender sender) {
        return "commands.execute.usage";
    }

    @Override
    public void execute(MinecraftServer server, ICommandSender sender, String[] args) throws CommandException {
        if (args.length < 5) {
            throw new WrongUsageException("commands.execute.usage", new Object[0]);
        }
        Entity entity = CommandExecuteAt.getEntity(server, sender, args[0], Entity.class);
        double d0 = CommandExecuteAt.parseDouble(entity.posX, args[1], false);
        double d1 = CommandExecuteAt.parseDouble(entity.posY, args[2], false);
        double d2 = CommandExecuteAt.parseDouble(entity.posZ, args[3], false);
        new BlockPos(d0, d1, d2);
        int i = 4;
        if ("detect".equals(args[4]) && args.length > 10) {
            World world = entity.getEntityWorld();
            double d3 = CommandExecuteAt.parseDouble(d0, args[5], false);
            double d4 = CommandExecuteAt.parseDouble(d1, args[6], false);
            double d5 = CommandExecuteAt.parseDouble(d2, args[7], false);
            Block block = CommandExecuteAt.getBlockByText(sender, args[8]);
            BlockPos blockpos = new BlockPos(d3, d4, d5);
            if (!world.isBlockLoaded(blockpos)) {
                throw new CommandException("commands.execute.failed", "detect", entity.getName());
            }
            IBlockState iblockstate = world.getBlockState(blockpos);
            if (iblockstate.getBlock() != block) {
                throw new CommandException("commands.execute.failed", "detect", entity.getName());
            }
            if (!CommandBase.func_190791_b(block, args[9]).apply(iblockstate)) {
                throw new CommandException("commands.execute.failed", "detect", entity.getName());
            }
            i = 10;
        }
        String s = CommandExecuteAt.buildString(args, i);
        CommandSenderWrapper icommandsender = CommandSenderWrapper.func_193998_a(sender).func_193997_a(entity, new Vec3d(d0, d1, d2)).func_194001_a(server.worldServers[0].getGameRules().getBoolean("commandBlockOutput"));
        ICommandManager icommandmanager = server.getCommandManager();
        try {
            int j = icommandmanager.executeCommand(icommandsender, s);
            if (j < 1) {
                throw new CommandException("commands.execute.allInvocationsFailed", s);
            }
        }
        catch (Throwable var23) {
            throw new CommandException("commands.execute.failed", s, entity.getName());
        }
    }

    @Override
    public List<String> getTabCompletionOptions(MinecraftServer server, ICommandSender sender, String[] args, @Nullable BlockPos pos) {
        if (args.length == 1) {
            return CommandExecuteAt.getListOfStringsMatchingLastWord(args, server.getAllUsernames());
        }
        if (args.length > 1 && args.length <= 4) {
            return CommandExecuteAt.getTabCompletionCoordinate(args, 1, pos);
        }
        if (args.length > 5 && args.length <= 8 && "detect".equals(args[4])) {
            return CommandExecuteAt.getTabCompletionCoordinate(args, 5, pos);
        }
        return args.length == 9 && "detect".equals(args[4]) ? CommandExecuteAt.getListOfStringsMatchingLastWord(args, Block.REGISTRY.getKeys()) : Collections.emptyList();
    }

    @Override
    public boolean isUsernameIndex(String[] args, int index) {
        return index == 0;
    }
}

