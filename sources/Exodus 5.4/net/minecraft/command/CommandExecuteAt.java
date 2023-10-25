/*
 * Decompiled with CFR 0.152.
 */
package net.minecraft.command;

import java.util.List;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.command.CommandBase;
import net.minecraft.command.CommandException;
import net.minecraft.command.CommandResultStats;
import net.minecraft.command.ICommandManager;
import net.minecraft.command.ICommandSender;
import net.minecraft.command.WrongUsageException;
import net.minecraft.entity.Entity;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.BlockPos;
import net.minecraft.util.IChatComponent;
import net.minecraft.util.Vec3;
import net.minecraft.world.World;

public class CommandExecuteAt
extends CommandBase {
    @Override
    public void processCommand(final ICommandSender iCommandSender, String[] stringArray) throws CommandException {
        Object object;
        if (stringArray.length < 5) {
            throw new WrongUsageException("commands.execute.usage", new Object[0]);
        }
        final Entity entity = CommandExecuteAt.getEntity(iCommandSender, stringArray[0], Entity.class);
        final double d = CommandExecuteAt.parseDouble(entity.posX, stringArray[1], false);
        final double d2 = CommandExecuteAt.parseDouble(entity.posY, stringArray[2], false);
        final double d3 = CommandExecuteAt.parseDouble(entity.posZ, stringArray[3], false);
        final BlockPos blockPos = new BlockPos(d, d2, d3);
        int n = 4;
        if ("detect".equals(stringArray[4]) && stringArray.length > 10) {
            object = entity.getEntityWorld();
            double d4 = CommandExecuteAt.parseDouble(d, stringArray[5], false);
            double d5 = CommandExecuteAt.parseDouble(d2, stringArray[6], false);
            double d6 = CommandExecuteAt.parseDouble(d3, stringArray[7], false);
            Block block = CommandExecuteAt.getBlockByText(iCommandSender, stringArray[8]);
            int n2 = CommandExecuteAt.parseInt(stringArray[9], -1, 15);
            BlockPos blockPos2 = new BlockPos(d4, d5, d6);
            IBlockState iBlockState = ((World)object).getBlockState(blockPos2);
            if (iBlockState.getBlock() != block || n2 >= 0 && iBlockState.getBlock().getMetaFromState(iBlockState) != n2) {
                throw new CommandException("commands.execute.failed", "detect", entity.getName());
            }
            n = 10;
        }
        object = CommandExecuteAt.buildString(stringArray, n);
        ICommandSender iCommandSender2 = new ICommandSender(){

            @Override
            public Entity getCommandSenderEntity() {
                return entity;
            }

            @Override
            public boolean canCommandSenderUseCommand(int n, String string) {
                return iCommandSender.canCommandSenderUseCommand(n, string);
            }

            @Override
            public boolean sendCommandFeedback() {
                MinecraftServer minecraftServer = MinecraftServer.getServer();
                return minecraftServer == null || minecraftServer.worldServers[0].getGameRules().getBoolean("commandBlockOutput");
            }

            @Override
            public String getName() {
                return entity.getName();
            }

            @Override
            public IChatComponent getDisplayName() {
                return entity.getDisplayName();
            }

            @Override
            public void addChatMessage(IChatComponent iChatComponent) {
                iCommandSender.addChatMessage(iChatComponent);
            }

            @Override
            public BlockPos getPosition() {
                return blockPos;
            }

            @Override
            public void setCommandStat(CommandResultStats.Type type, int n) {
                entity.setCommandStat(type, n);
            }

            @Override
            public Vec3 getPositionVector() {
                return new Vec3(d, d2, d3);
            }

            @Override
            public World getEntityWorld() {
                return entity.worldObj;
            }
        };
        ICommandManager iCommandManager = MinecraftServer.getServer().getCommandManager();
        int n3 = iCommandManager.executeCommand(iCommandSender2, (String)object);
        if (n3 < 1) {
            throw new CommandException("commands.execute.allInvocationsFailed", object);
        }
    }

    @Override
    public String getCommandName() {
        return "execute";
    }

    @Override
    public List<String> addTabCompletionOptions(ICommandSender iCommandSender, String[] stringArray, BlockPos blockPos) {
        return stringArray.length == 1 ? CommandExecuteAt.getListOfStringsMatchingLastWord(stringArray, MinecraftServer.getServer().getAllUsernames()) : (stringArray.length > 1 && stringArray.length <= 4 ? CommandExecuteAt.func_175771_a(stringArray, 1, blockPos) : (stringArray.length > 5 && stringArray.length <= 8 && "detect".equals(stringArray[4]) ? CommandExecuteAt.func_175771_a(stringArray, 5, blockPos) : (stringArray.length == 9 && "detect".equals(stringArray[4]) ? CommandExecuteAt.getListOfStringsMatchingLastWord(stringArray, Block.blockRegistry.getKeys()) : null)));
    }

    @Override
    public int getRequiredPermissionLevel() {
        return 2;
    }

    @Override
    public boolean isUsernameIndex(String[] stringArray, int n) {
        return n == 0;
    }

    @Override
    public String getCommandUsage(ICommandSender iCommandSender) {
        return "commands.execute.usage";
    }
}

