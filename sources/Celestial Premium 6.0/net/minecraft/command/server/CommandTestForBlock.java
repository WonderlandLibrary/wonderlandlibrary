/*
 * Decompiled with CFR 0.150.
 */
package net.minecraft.command.server;

import java.util.Collections;
import java.util.List;
import javax.annotation.Nullable;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.command.CommandBase;
import net.minecraft.command.CommandException;
import net.minecraft.command.CommandResultStats;
import net.minecraft.command.ICommand;
import net.minecraft.command.ICommandSender;
import net.minecraft.command.NumberInvalidException;
import net.minecraft.command.WrongUsageException;
import net.minecraft.nbt.JsonToNBT;
import net.minecraft.nbt.NBTException;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTUtil;
import net.minecraft.server.MinecraftServer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class CommandTestForBlock
extends CommandBase {
    @Override
    public String getCommandName() {
        return "testforblock";
    }

    @Override
    public int getRequiredPermissionLevel() {
        return 2;
    }

    @Override
    public String getCommandUsage(ICommandSender sender) {
        return "commands.testforblock.usage";
    }

    @Override
    public void execute(MinecraftServer server, ICommandSender sender, String[] args) throws CommandException {
        IBlockState iblockstate;
        Block block1;
        if (args.length < 4) {
            throw new WrongUsageException("commands.testforblock.usage", new Object[0]);
        }
        sender.setCommandStat(CommandResultStats.Type.AFFECTED_BLOCKS, 0);
        BlockPos blockpos = CommandTestForBlock.parseBlockPos(sender, args, 0, false);
        Block block = CommandTestForBlock.getBlockByText(sender, args[3]);
        if (block == null) {
            throw new NumberInvalidException("commands.setblock.notFound", args[3]);
        }
        World world = sender.getEntityWorld();
        if (!world.isBlockLoaded(blockpos)) {
            throw new CommandException("commands.testforblock.outOfWorld", new Object[0]);
        }
        NBTTagCompound nbttagcompound = new NBTTagCompound();
        boolean flag = false;
        if (args.length >= 6 && block.hasTileEntity()) {
            String s = CommandTestForBlock.buildString(args, 5);
            try {
                nbttagcompound = JsonToNBT.getTagFromJson(s);
                flag = true;
            }
            catch (NBTException nbtexception) {
                throw new CommandException("commands.setblock.tagError", nbtexception.getMessage());
            }
        }
        if ((block1 = (iblockstate = world.getBlockState(blockpos)).getBlock()) != block) {
            throw new CommandException("commands.testforblock.failed.tile", blockpos.getX(), blockpos.getY(), blockpos.getZ(), block1.getLocalizedName(), block.getLocalizedName());
        }
        if (args.length >= 5 && !CommandBase.func_190791_b(block, args[4]).apply(iblockstate)) {
            try {
                int i = iblockstate.getBlock().getMetaFromState(iblockstate);
                throw new CommandException("commands.testforblock.failed.data", blockpos.getX(), blockpos.getY(), blockpos.getZ(), i, Integer.parseInt(args[4]));
            }
            catch (NumberFormatException var13) {
                throw new CommandException("commands.testforblock.failed.data", blockpos.getX(), blockpos.getY(), blockpos.getZ(), iblockstate.toString(), args[4]);
            }
        }
        if (flag) {
            TileEntity tileentity = world.getTileEntity(blockpos);
            if (tileentity == null) {
                throw new CommandException("commands.testforblock.failed.tileEntity", blockpos.getX(), blockpos.getY(), blockpos.getZ());
            }
            NBTTagCompound nbttagcompound1 = tileentity.writeToNBT(new NBTTagCompound());
            if (!NBTUtil.areNBTEquals(nbttagcompound, nbttagcompound1, true)) {
                throw new CommandException("commands.testforblock.failed.nbt", blockpos.getX(), blockpos.getY(), blockpos.getZ());
            }
        }
        sender.setCommandStat(CommandResultStats.Type.AFFECTED_BLOCKS, 1);
        CommandTestForBlock.notifyCommandListener(sender, (ICommand)this, "commands.testforblock.success", blockpos.getX(), blockpos.getY(), blockpos.getZ());
    }

    @Override
    public List<String> getTabCompletionOptions(MinecraftServer server, ICommandSender sender, String[] args, @Nullable BlockPos pos) {
        if (args.length > 0 && args.length <= 3) {
            return CommandTestForBlock.getTabCompletionCoordinate(args, 0, pos);
        }
        return args.length == 4 ? CommandTestForBlock.getListOfStringsMatchingLastWord(args, Block.REGISTRY.getKeys()) : Collections.emptyList();
    }
}

