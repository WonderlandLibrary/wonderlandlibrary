package net.minecraft.command;

import java.util.List;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.gen.structure.StructureBoundingBox;

public class CommandCompare extends CommandBase {
   public List addTabCompletionOptions(ICommandSender var1, String[] var2, BlockPos var3) {
      return var2.length > 0 && var2.length <= 3 ? func_175771_a(var2, 0, var3) : (var2.length > 3 && var2.length <= 6 ? func_175771_a(var2, 3, var3) : (var2.length > 6 && var2.length <= 9 ? func_175771_a(var2, 6, var3) : (var2.length == 10 ? getListOfStringsMatchingLastWord(var2, new String[]{"masked", "all"}) : null)));
   }

   public String getCommandName() {
      return "testforblocks";
   }

   public int getRequiredPermissionLevel() {
      return 2;
   }

   public void processCommand(ICommandSender var1, String[] var2) throws CommandException {
      if (var2.length < 9) {
         throw new WrongUsageException("commands.compare.usage", new Object[0]);
      } else {
         var1.setCommandStat(CommandResultStats.Type.AFFECTED_BLOCKS, 0);
         BlockPos var3 = parseBlockPos(var1, var2, 0, false);
         BlockPos var4 = parseBlockPos(var1, var2, 3, false);
         BlockPos var5 = parseBlockPos(var1, var2, 6, false);
         StructureBoundingBox var6 = new StructureBoundingBox(var3, var4);
         StructureBoundingBox var7 = new StructureBoundingBox(var5, var5.add(var6.func_175896_b()));
         int var8 = var6.getXSize() * var6.getYSize() * var6.getZSize();
         if (var8 > 524288) {
            throw new CommandException("commands.compare.tooManyBlocks", new Object[]{var8, 524288});
         } else if (var6.minY >= 0 && var6.maxY < 256 && var7.minY >= 0 && var7.maxY < 256) {
            World var9 = var1.getEntityWorld();
            if (var9.isAreaLoaded(var6) && var9.isAreaLoaded(var7)) {
               boolean var10 = false;
               if (var2.length > 9 && var2[9].equals("masked")) {
                  var10 = true;
               }

               var8 = 0;
               BlockPos var11 = new BlockPos(var7.minX - var6.minX, var7.minY - var6.minY, var7.minZ - var6.minZ);
               BlockPos.MutableBlockPos var12 = new BlockPos.MutableBlockPos();
               BlockPos.MutableBlockPos var13 = new BlockPos.MutableBlockPos();

               for(int var14 = var6.minZ; var14 <= var6.maxZ; ++var14) {
                  for(int var15 = var6.minY; var15 <= var6.maxY; ++var15) {
                     for(int var16 = var6.minX; var16 <= var6.maxX; ++var16) {
                        var12.func_181079_c(var16, var15, var14);
                        var13.func_181079_c(var16 + var11.getX(), var15 + var11.getY(), var14 + var11.getZ());
                        boolean var17 = false;
                        IBlockState var18 = var9.getBlockState(var12);
                        if (!var10 || var18.getBlock() != Blocks.air) {
                           if (var18 == var9.getBlockState(var13)) {
                              TileEntity var19 = var9.getTileEntity(var12);
                              TileEntity var20 = var9.getTileEntity(var13);
                              if (var19 != null && var20 != null) {
                                 NBTTagCompound var21 = new NBTTagCompound();
                                 var19.writeToNBT(var21);
                                 var21.removeTag("x");
                                 var21.removeTag("y");
                                 var21.removeTag("z");
                                 NBTTagCompound var22 = new NBTTagCompound();
                                 var20.writeToNBT(var22);
                                 var22.removeTag("x");
                                 var22.removeTag("y");
                                 var22.removeTag("z");
                                 if (!var21.equals(var22)) {
                                    var17 = true;
                                 }
                              } else if (var19 != null) {
                                 var17 = true;
                              }
                           } else {
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

               var1.setCommandStat(CommandResultStats.Type.AFFECTED_BLOCKS, var8);
               notifyOperators(var1, this, "commands.compare.success", new Object[]{var8});
            } else {
               throw new CommandException("commands.compare.outOfWorld", new Object[0]);
            }
         } else {
            throw new CommandException("commands.compare.outOfWorld", new Object[0]);
         }
      }
   }

   public String getCommandUsage(ICommandSender var1) {
      return "commands.compare.usage";
   }
}
