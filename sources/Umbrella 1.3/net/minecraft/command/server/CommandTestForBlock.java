/*
 * Decompiled with CFR 0.150.
 */
package net.minecraft.command.server;

import java.util.Iterator;
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
import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTException;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.BlockPos;
import net.minecraft.world.World;

public class CommandTestForBlock
extends CommandBase {
    private static final String __OBFID = "CL_00001181";

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
    public void processCommand(ICommandSender sender, String[] args) throws CommandException {
        int var11;
        IBlockState var14;
        Block var10;
        World var6;
        if (args.length < 4) {
            throw new WrongUsageException("commands.testforblock.usage", new Object[0]);
        }
        sender.func_174794_a(CommandResultStats.Type.AFFECTED_BLOCKS, 0);
        BlockPos var3 = CommandTestForBlock.func_175757_a(sender, args, 0, false);
        Block var4 = Block.getBlockFromName(args[3]);
        if (var4 == null) {
            throw new NumberInvalidException("commands.setblock.notFound", args[3]);
        }
        int var5 = -1;
        if (args.length >= 5) {
            var5 = CommandTestForBlock.parseInt(args[4], -1, 15);
        }
        if (!(var6 = sender.getEntityWorld()).isBlockLoaded(var3)) {
            throw new CommandException("commands.testforblock.outOfWorld", new Object[0]);
        }
        NBTTagCompound var7 = new NBTTagCompound();
        boolean var8 = false;
        if (args.length >= 6 && var4.hasTileEntity()) {
            String var9 = CommandTestForBlock.getChatComponentFromNthArg(sender, args, 5).getUnformattedText();
            try {
                var7 = JsonToNBT.func_180713_a(var9);
                var8 = true;
            }
            catch (NBTException var13) {
                throw new CommandException("commands.setblock.tagError", var13.getMessage());
            }
        }
        if ((var10 = (var14 = var6.getBlockState(var3)).getBlock()) != var4) {
            throw new CommandException("commands.testforblock.failed.tile", var3.getX(), var3.getY(), var3.getZ(), var10.getLocalizedName(), var4.getLocalizedName());
        }
        if (var5 > -1 && (var11 = var14.getBlock().getMetaFromState(var14)) != var5) {
            throw new CommandException("commands.testforblock.failed.data", var3.getX(), var3.getY(), var3.getZ(), var11, var5);
        }
        if (var8) {
            TileEntity var15 = var6.getTileEntity(var3);
            if (var15 == null) {
                throw new CommandException("commands.testforblock.failed.tileEntity", var3.getX(), var3.getY(), var3.getZ());
            }
            NBTTagCompound var12 = new NBTTagCompound();
            var15.writeToNBT(var12);
            if (!CommandTestForBlock.func_175775_a(var7, var12, true)) {
                throw new CommandException("commands.testforblock.failed.nbt", var3.getX(), var3.getY(), var3.getZ());
            }
        }
        sender.func_174794_a(CommandResultStats.Type.AFFECTED_BLOCKS, 1);
        CommandTestForBlock.notifyOperators(sender, (ICommand)this, "commands.testforblock.success", var3.getX(), var3.getY(), var3.getZ());
    }

    public static boolean func_175775_a(NBTBase p_175775_0_, NBTBase p_175775_1_, boolean p_175775_2_) {
        if (p_175775_0_ == p_175775_1_) {
            return true;
        }
        if (p_175775_0_ == null) {
            return true;
        }
        if (p_175775_1_ == null) {
            return false;
        }
        if (!p_175775_0_.getClass().equals(p_175775_1_.getClass())) {
            return false;
        }
        if (p_175775_0_ instanceof NBTTagCompound) {
            String var12;
            NBTBase var13;
            NBTTagCompound var9 = (NBTTagCompound)p_175775_0_;
            NBTTagCompound var10 = (NBTTagCompound)p_175775_1_;
            Iterator var11 = var9.getKeySet().iterator();
            do {
                if (var11.hasNext()) continue;
                return true;
            } while (CommandTestForBlock.func_175775_a(var13 = var9.getTag(var12 = (String)var11.next()), var10.getTag(var12), p_175775_2_));
            return false;
        }
        if (p_175775_0_ instanceof NBTTagList && p_175775_2_) {
            NBTTagList var3 = (NBTTagList)p_175775_0_;
            NBTTagList var4 = (NBTTagList)p_175775_1_;
            if (var3.tagCount() == 0) {
                return var4.tagCount() == 0;
            }
            for (int var5 = 0; var5 < var3.tagCount(); ++var5) {
                NBTBase var6 = var3.get(var5);
                boolean var7 = false;
                for (int var8 = 0; var8 < var4.tagCount(); ++var8) {
                    if (!CommandTestForBlock.func_175775_a(var6, var4.get(var8), p_175775_2_)) {
                        continue;
                    }
                    var7 = true;
                    break;
                }
                if (var7) continue;
                return false;
            }
            return true;
        }
        return p_175775_0_.equals(p_175775_1_);
    }

    @Override
    public List addTabCompletionOptions(ICommandSender sender, String[] args, BlockPos pos) {
        return args.length > 0 && args.length <= 3 ? CommandTestForBlock.func_175771_a(args, 0, pos) : (args.length == 4 ? CommandTestForBlock.func_175762_a(args, Block.blockRegistry.getKeys()) : null);
    }
}

