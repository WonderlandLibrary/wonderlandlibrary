// 
// Decompiled by Procyon v0.5.36
// 

package net.minecraft.command;

import java.util.Collections;
import java.util.List;
import javax.annotation.Nullable;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.block.state.IBlockState;
import net.minecraft.world.World;
import net.minecraft.util.math.BlockPos;
import net.minecraft.nbt.NBTException;
import net.minecraft.nbt.JsonToNBT;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.server.MinecraftServer;

public class CommandBlockData extends CommandBase
{
    @Override
    public String getName() {
        return "blockdata";
    }
    
    @Override
    public int getRequiredPermissionLevel() {
        return 2;
    }
    
    @Override
    public String getUsage(final ICommandSender sender) {
        return "commands.blockdata.usage";
    }
    
    @Override
    public void execute(final MinecraftServer server, final ICommandSender sender, final String[] args) throws CommandException {
        if (args.length < 4) {
            throw new WrongUsageException("commands.blockdata.usage", new Object[0]);
        }
        sender.setCommandStat(CommandResultStats.Type.AFFECTED_BLOCKS, 0);
        final BlockPos blockpos = CommandBase.parseBlockPos(sender, args, 0, false);
        final World world = sender.getEntityWorld();
        if (!world.isBlockLoaded(blockpos)) {
            throw new CommandException("commands.blockdata.outOfWorld", new Object[0]);
        }
        final IBlockState iblockstate = world.getBlockState(blockpos);
        final TileEntity tileentity = world.getTileEntity(blockpos);
        if (tileentity == null) {
            throw new CommandException("commands.blockdata.notValid", new Object[0]);
        }
        final NBTTagCompound nbttagcompound = tileentity.writeToNBT(new NBTTagCompound());
        final NBTTagCompound nbttagcompound2 = nbttagcompound.copy();
        NBTTagCompound nbttagcompound3;
        try {
            nbttagcompound3 = JsonToNBT.getTagFromJson(CommandBase.buildString(args, 3));
        }
        catch (NBTException nbtexception) {
            throw new CommandException("commands.blockdata.tagError", new Object[] { nbtexception.getMessage() });
        }
        nbttagcompound.merge(nbttagcompound3);
        nbttagcompound.setInteger("x", blockpos.getX());
        nbttagcompound.setInteger("y", blockpos.getY());
        nbttagcompound.setInteger("z", blockpos.getZ());
        if (nbttagcompound.equals(nbttagcompound2)) {
            throw new CommandException("commands.blockdata.failed", new Object[] { nbttagcompound.toString() });
        }
        tileentity.readFromNBT(nbttagcompound);
        tileentity.markDirty();
        world.notifyBlockUpdate(blockpos, iblockstate, iblockstate, 3);
        sender.setCommandStat(CommandResultStats.Type.AFFECTED_BLOCKS, 1);
        CommandBase.notifyCommandListener(sender, this, "commands.blockdata.success", nbttagcompound.toString());
    }
    
    @Override
    public List<String> getTabCompletions(final MinecraftServer server, final ICommandSender sender, final String[] args, @Nullable final BlockPos targetPos) {
        return (args.length > 0 && args.length <= 3) ? CommandBase.getTabCompletionCoordinate(args, 0, targetPos) : Collections.emptyList();
    }
}
