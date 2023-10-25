/*
 * Decompiled with CFR 0.152.
 */
package net.minecraft.command.server;

import java.util.List;
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
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.BlockPos;
import net.minecraft.world.World;

public class CommandTestForBlock
extends CommandBase {
    @Override
    public void processCommand(ICommandSender iCommandSender, String[] stringArray) throws CommandException {
        int n;
        Block block;
        Object object;
        World world;
        if (stringArray.length < 4) {
            throw new WrongUsageException("commands.testforblock.usage", new Object[0]);
        }
        iCommandSender.setCommandStat(CommandResultStats.Type.AFFECTED_BLOCKS, 0);
        BlockPos blockPos = CommandTestForBlock.parseBlockPos(iCommandSender, stringArray, 0, false);
        Block block2 = Block.getBlockFromName(stringArray[3]);
        if (block2 == null) {
            throw new NumberInvalidException("commands.setblock.notFound", stringArray[3]);
        }
        int n2 = -1;
        if (stringArray.length >= 5) {
            n2 = CommandTestForBlock.parseInt(stringArray[4], -1, 15);
        }
        if (!(world = iCommandSender.getEntityWorld()).isBlockLoaded(blockPos)) {
            throw new CommandException("commands.testforblock.outOfWorld", new Object[0]);
        }
        NBTTagCompound nBTTagCompound = new NBTTagCompound();
        boolean bl = false;
        if (stringArray.length >= 6 && block2.hasTileEntity()) {
            object = CommandTestForBlock.getChatComponentFromNthArg(iCommandSender, stringArray, 5).getUnformattedText();
            try {
                nBTTagCompound = JsonToNBT.getTagFromJson((String)object);
                bl = true;
            }
            catch (NBTException nBTException) {
                throw new CommandException("commands.setblock.tagError", nBTException.getMessage());
            }
        }
        if ((block = (object = world.getBlockState(blockPos)).getBlock()) != block2) {
            throw new CommandException("commands.testforblock.failed.tile", blockPos.getX(), blockPos.getY(), blockPos.getZ(), block.getLocalizedName(), block2.getLocalizedName());
        }
        if (n2 > -1 && (n = object.getBlock().getMetaFromState((IBlockState)object)) != n2) {
            throw new CommandException("commands.testforblock.failed.data", blockPos.getX(), blockPos.getY(), blockPos.getZ(), n, n2);
        }
        if (bl) {
            TileEntity tileEntity = world.getTileEntity(blockPos);
            if (tileEntity == null) {
                throw new CommandException("commands.testforblock.failed.tileEntity", blockPos.getX(), blockPos.getY(), blockPos.getZ());
            }
            NBTTagCompound nBTTagCompound2 = new NBTTagCompound();
            tileEntity.writeToNBT(nBTTagCompound2);
            if (!NBTUtil.func_181123_a(nBTTagCompound, nBTTagCompound2, true)) {
                throw new CommandException("commands.testforblock.failed.nbt", blockPos.getX(), blockPos.getY(), blockPos.getZ());
            }
        }
        iCommandSender.setCommandStat(CommandResultStats.Type.AFFECTED_BLOCKS, 1);
        CommandTestForBlock.notifyOperators(iCommandSender, (ICommand)this, "commands.testforblock.success", blockPos.getX(), blockPos.getY(), blockPos.getZ());
    }

    @Override
    public String getCommandName() {
        return "testforblock";
    }

    @Override
    public int getRequiredPermissionLevel() {
        return 2;
    }

    @Override
    public String getCommandUsage(ICommandSender iCommandSender) {
        return "commands.testforblock.usage";
    }

    @Override
    public List<String> addTabCompletionOptions(ICommandSender iCommandSender, String[] stringArray, BlockPos blockPos) {
        return stringArray.length > 0 && stringArray.length <= 3 ? CommandTestForBlock.func_175771_a(stringArray, 0, blockPos) : (stringArray.length == 4 ? CommandTestForBlock.getListOfStringsMatchingLastWord(stringArray, Block.blockRegistry.getKeys()) : null);
    }
}

