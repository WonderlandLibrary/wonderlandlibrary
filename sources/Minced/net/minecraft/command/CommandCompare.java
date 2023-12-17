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
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.init.Blocks;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3i;
import net.minecraft.world.gen.structure.StructureBoundingBox;
import net.minecraft.server.MinecraftServer;

public class CommandCompare extends CommandBase
{
    @Override
    public String getName() {
        return "testforblocks";
    }
    
    @Override
    public int getRequiredPermissionLevel() {
        return 2;
    }
    
    @Override
    public String getUsage(final ICommandSender sender) {
        return "commands.compare.usage";
    }
    
    @Override
    public void execute(final MinecraftServer server, final ICommandSender sender, final String[] args) throws CommandException {
        if (args.length < 9) {
            throw new WrongUsageException("commands.compare.usage", new Object[0]);
        }
        sender.setCommandStat(CommandResultStats.Type.AFFECTED_BLOCKS, 0);
        final BlockPos blockpos = CommandBase.parseBlockPos(sender, args, 0, false);
        final BlockPos blockpos2 = CommandBase.parseBlockPos(sender, args, 3, false);
        final BlockPos blockpos3 = CommandBase.parseBlockPos(sender, args, 6, false);
        final StructureBoundingBox structureboundingbox = new StructureBoundingBox(blockpos, blockpos2);
        final StructureBoundingBox structureboundingbox2 = new StructureBoundingBox(blockpos3, blockpos3.add(structureboundingbox.getLength()));
        int i = structureboundingbox.getXSize() * structureboundingbox.getYSize() * structureboundingbox.getZSize();
        if (i > 524288) {
            throw new CommandException("commands.compare.tooManyBlocks", new Object[] { i, 524288 });
        }
        if (structureboundingbox.minY < 0 || structureboundingbox.maxY >= 256 || structureboundingbox2.minY < 0 || structureboundingbox2.maxY >= 256) {
            throw new CommandException("commands.compare.outOfWorld", new Object[0]);
        }
        final World world = sender.getEntityWorld();
        if (world.isAreaLoaded(structureboundingbox) && world.isAreaLoaded(structureboundingbox2)) {
            boolean flag = false;
            if (args.length > 9 && "masked".equals(args[9])) {
                flag = true;
            }
            i = 0;
            final BlockPos blockpos4 = new BlockPos(structureboundingbox2.minX - structureboundingbox.minX, structureboundingbox2.minY - structureboundingbox.minY, structureboundingbox2.minZ - structureboundingbox.minZ);
            final BlockPos.MutableBlockPos blockpos$mutableblockpos = new BlockPos.MutableBlockPos();
            final BlockPos.MutableBlockPos blockpos$mutableblockpos2 = new BlockPos.MutableBlockPos();
            for (int j = structureboundingbox.minZ; j <= structureboundingbox.maxZ; ++j) {
                for (int k = structureboundingbox.minY; k <= structureboundingbox.maxY; ++k) {
                    for (int l = structureboundingbox.minX; l <= structureboundingbox.maxX; ++l) {
                        blockpos$mutableblockpos.setPos(l, k, j);
                        blockpos$mutableblockpos2.setPos(l + blockpos4.getX(), k + blockpos4.getY(), j + blockpos4.getZ());
                        boolean flag2 = false;
                        final IBlockState iblockstate = world.getBlockState(blockpos$mutableblockpos);
                        if (!flag || iblockstate.getBlock() != Blocks.AIR) {
                            if (iblockstate == world.getBlockState(blockpos$mutableblockpos2)) {
                                final TileEntity tileentity = world.getTileEntity(blockpos$mutableblockpos);
                                final TileEntity tileentity2 = world.getTileEntity(blockpos$mutableblockpos2);
                                if (tileentity != null && tileentity2 != null) {
                                    final NBTTagCompound nbttagcompound = tileentity.writeToNBT(new NBTTagCompound());
                                    nbttagcompound.removeTag("x");
                                    nbttagcompound.removeTag("y");
                                    nbttagcompound.removeTag("z");
                                    final NBTTagCompound nbttagcompound2 = tileentity2.writeToNBT(new NBTTagCompound());
                                    nbttagcompound2.removeTag("x");
                                    nbttagcompound2.removeTag("y");
                                    nbttagcompound2.removeTag("z");
                                    if (!nbttagcompound.equals(nbttagcompound2)) {
                                        flag2 = true;
                                    }
                                }
                                else if (tileentity != null) {
                                    flag2 = true;
                                }
                            }
                            else {
                                flag2 = true;
                            }
                            ++i;
                            if (flag2) {
                                throw new CommandException("commands.compare.failed", new Object[0]);
                            }
                        }
                    }
                }
            }
            sender.setCommandStat(CommandResultStats.Type.AFFECTED_BLOCKS, i);
            CommandBase.notifyCommandListener(sender, this, "commands.compare.success", i);
            return;
        }
        throw new CommandException("commands.compare.outOfWorld", new Object[0]);
    }
    
    @Override
    public List<String> getTabCompletions(final MinecraftServer server, final ICommandSender sender, final String[] args, @Nullable final BlockPos targetPos) {
        if (args.length > 0 && args.length <= 3) {
            return CommandBase.getTabCompletionCoordinate(args, 0, targetPos);
        }
        if (args.length > 3 && args.length <= 6) {
            return CommandBase.getTabCompletionCoordinate(args, 3, targetPos);
        }
        if (args.length > 6 && args.length <= 9) {
            return CommandBase.getTabCompletionCoordinate(args, 6, targetPos);
        }
        return (args.length == 10) ? CommandBase.getListOfStringsMatchingLastWord(args, "masked", "all") : Collections.emptyList();
    }
}
