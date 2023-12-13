// 
// Decompiled by Procyon v0.5.30
// 

package net.minecraft.command;

import java.util.List;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.block.state.IBlockState;
import net.minecraft.world.World;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.init.Blocks;
import net.minecraft.util.BlockPos;
import net.minecraft.util.Vec3i;
import net.minecraft.world.gen.structure.StructureBoundingBox;

public class CommandCompare extends CommandBase
{
    private static final String __OBFID = "CL_00002346";
    
    @Override
    public String getCommandName() {
        return "testforblocks";
    }
    
    @Override
    public int getRequiredPermissionLevel() {
        return 2;
    }
    
    @Override
    public String getCommandUsage(final ICommandSender sender) {
        return "commands.compare.usage";
    }
    
    @Override
    public void processCommand(final ICommandSender sender, final String[] args) throws CommandException {
        if (args.length < 9) {
            throw new WrongUsageException("commands.compare.usage", new Object[0]);
        }
        sender.func_174794_a(CommandResultStats.Type.AFFECTED_BLOCKS, 0);
        final BlockPos var3 = CommandBase.func_175757_a(sender, args, 0, false);
        final BlockPos var4 = CommandBase.func_175757_a(sender, args, 3, false);
        final BlockPos var5 = CommandBase.func_175757_a(sender, args, 6, false);
        final StructureBoundingBox var6 = new StructureBoundingBox(var3, var4);
        final StructureBoundingBox var7 = new StructureBoundingBox(var5, var5.add(var6.func_175896_b()));
        int var8 = var6.getXSize() * var6.getYSize() * var6.getZSize();
        if (var8 > 524288) {
            throw new CommandException("commands.compare.tooManyBlocks", new Object[] { var8, 524288 });
        }
        if (var6.minY < 0 || var6.maxY >= 256 || var7.minY < 0 || var7.maxY >= 256) {
            throw new CommandException("commands.compare.outOfWorld", new Object[0]);
        }
        final World var9 = sender.getEntityWorld();
        if (var9.isAreaLoaded(var6) && var9.isAreaLoaded(var7)) {
            boolean var10 = false;
            if (args.length > 9 && args[9].equals("masked")) {
                var10 = true;
            }
            var8 = 0;
            final BlockPos var11 = new BlockPos(var7.minX - var6.minX, var7.minY - var6.minY, var7.minZ - var6.minZ);
            for (int var12 = var6.minZ; var12 <= var6.maxZ; ++var12) {
                for (int var13 = var6.minY; var13 <= var6.maxY; ++var13) {
                    for (int var14 = var6.minX; var14 <= var6.maxX; ++var14) {
                        final BlockPos var15 = new BlockPos(var14, var13, var12);
                        final BlockPos var16 = var15.add(var11);
                        boolean var17 = false;
                        final IBlockState var18 = var9.getBlockState(var15);
                        if (!var10 || var18.getBlock() != Blocks.air) {
                            if (var18 == var9.getBlockState(var16)) {
                                final TileEntity var19 = var9.getTileEntity(var15);
                                final TileEntity var20 = var9.getTileEntity(var16);
                                if (var19 != null && var20 != null) {
                                    final NBTTagCompound var21 = new NBTTagCompound();
                                    var19.writeToNBT(var21);
                                    var21.removeTag("x");
                                    var21.removeTag("y");
                                    var21.removeTag("z");
                                    final NBTTagCompound var22 = new NBTTagCompound();
                                    var20.writeToNBT(var22);
                                    var22.removeTag("x");
                                    var22.removeTag("y");
                                    var22.removeTag("z");
                                    if (!var21.equals(var22)) {
                                        var17 = true;
                                    }
                                }
                                else if (var19 != null) {
                                    var17 = true;
                                }
                            }
                            else {
                                var17 = true;
                            }
                            ++var8;
                            if (var17) {
                                throw new CommandException("commands.compare.failed", new Object[0]);
                            }
                        }
                    }
                }
            }
            sender.func_174794_a(CommandResultStats.Type.AFFECTED_BLOCKS, var8);
            CommandBase.notifyOperators(sender, this, "commands.compare.success", var8);
            return;
        }
        throw new CommandException("commands.compare.outOfWorld", new Object[0]);
    }
    
    @Override
    public List addTabCompletionOptions(final ICommandSender sender, final String[] args, final BlockPos pos) {
        return (args.length > 0 && args.length <= 3) ? CommandBase.func_175771_a(args, 0, pos) : ((args.length > 3 && args.length <= 6) ? CommandBase.func_175771_a(args, 3, pos) : ((args.length > 6 && args.length <= 9) ? CommandBase.func_175771_a(args, 6, pos) : ((args.length == 10) ? CommandBase.getListOfStringsMatchingLastWord(args, "masked", "all") : null)));
    }
}
