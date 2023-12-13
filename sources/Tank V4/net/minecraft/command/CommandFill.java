package net.minecraft.command;

import com.google.common.collect.Lists;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.inventory.IInventory;
import net.minecraft.nbt.JsonToNBT;
import net.minecraft.nbt.NBTException;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.BlockPos;
import net.minecraft.world.World;

public class CommandFill extends CommandBase {
   public int getRequiredPermissionLevel() {
      return 2;
   }

   public String getCommandUsage(ICommandSender var1) {
      return "commands.fill.usage";
   }

   public void processCommand(ICommandSender var1, String[] var2) throws CommandException {
      if (var2.length < 7) {
         throw new WrongUsageException("commands.fill.usage", new Object[0]);
      } else {
         var1.setCommandStat(CommandResultStats.Type.AFFECTED_BLOCKS, 0);
         BlockPos var3 = parseBlockPos(var1, var2, 0, false);
         BlockPos var4 = parseBlockPos(var1, var2, 3, false);
         Block var5 = CommandBase.getBlockByText(var1, var2[6]);
         int var6 = 0;
         if (var2.length >= 8) {
            var6 = parseInt(var2[7], 0, 15);
         }

         BlockPos var7 = new BlockPos(Math.min(var3.getX(), var4.getX()), Math.min(var3.getY(), var4.getY()), Math.min(var3.getZ(), var4.getZ()));
         BlockPos var8 = new BlockPos(Math.max(var3.getX(), var4.getX()), Math.max(var3.getY(), var4.getY()), Math.max(var3.getZ(), var4.getZ()));
         int var9 = (var8.getX() - var7.getX() + 1) * (var8.getY() - var7.getY() + 1) * (var8.getZ() - var7.getZ() + 1);
         if (var9 > 32768) {
            throw new CommandException("commands.fill.tooManyBlocks", new Object[]{var9, 32768});
         } else if (var7.getY() >= 0 && var8.getY() < 256) {
            World var10 = var1.getEntityWorld();

            for(int var11 = var7.getZ(); var11 < var8.getZ() + 16; var11 += 16) {
               for(int var12 = var7.getX(); var12 < var8.getX() + 16; var12 += 16) {
                  if (!var10.isBlockLoaded(new BlockPos(var12, var8.getY() - var7.getY(), var11))) {
                     throw new CommandException("commands.fill.outOfWorld", new Object[0]);
                  }
               }
            }

            NBTTagCompound var23 = new NBTTagCompound();
            boolean var24 = false;
            if (var2.length >= 10 && var5.hasTileEntity()) {
               String var13 = getChatComponentFromNthArg(var1, var2, 9).getUnformattedText();

               try {
                  var23 = JsonToNBT.getTagFromJson(var13);
                  var24 = true;
               } catch (NBTException var22) {
                  throw new CommandException("commands.fill.tagError", new Object[]{var22.getMessage()});
               }
            }

            ArrayList var25 = Lists.newArrayList();
            var9 = 0;

            for(int var14 = var7.getZ(); var14 <= var8.getZ(); ++var14) {
               for(int var15 = var7.getY(); var15 <= var8.getY(); ++var15) {
                  for(int var16 = var7.getX(); var16 <= var8.getX(); ++var16) {
                     BlockPos var17 = new BlockPos(var16, var15, var14);
                     IBlockState var19;
                     if (var2.length >= 9) {
                        if (!var2[8].equals("outline") && !var2[8].equals("hollow")) {
                           if (var2[8].equals("destroy")) {
                              var10.destroyBlock(var17, true);
                           } else if (var2[8].equals("keep")) {
                              if (!var10.isAirBlock(var17)) {
                                 continue;
                              }
                           } else if (var2[8].equals("replace") && !var5.hasTileEntity()) {
                              if (var2.length > 9) {
                                 Block var18 = CommandBase.getBlockByText(var1, var2[9]);
                                 if (var10.getBlockState(var17).getBlock() != var18) {
                                    continue;
                                 }
                              }

                              if (var2.length > 10) {
                                 int var29 = CommandBase.parseInt(var2[10]);
                                 var19 = var10.getBlockState(var17);
                                 if (var19.getBlock().getMetaFromState(var19) != var29) {
                                    continue;
                                 }
                              }
                           }
                        } else if (var16 != var7.getX() && var16 != var8.getX() && var15 != var7.getY() && var15 != var8.getY() && var14 != var7.getZ() && var14 != var8.getZ()) {
                           if (var2[8].equals("hollow")) {
                              var10.setBlockState(var17, Blocks.air.getDefaultState(), 2);
                              var25.add(var17);
                           }
                           continue;
                        }
                     }

                     TileEntity var30 = var10.getTileEntity(var17);
                     if (var30 != null) {
                        if (var30 instanceof IInventory) {
                           ((IInventory)var30).clear();
                        }

                        var10.setBlockState(var17, Blocks.barrier.getDefaultState(), var5 == Blocks.barrier ? 2 : 4);
                     }

                     var19 = var5.getStateFromMeta(var6);
                     if (var10.setBlockState(var17, var19, 2)) {
                        var25.add(var17);
                        ++var9;
                        if (var24) {
                           TileEntity var20 = var10.getTileEntity(var17);
                           if (var20 != null) {
                              var23.setInteger("x", var17.getX());
                              var23.setInteger("y", var17.getY());
                              var23.setInteger("z", var17.getZ());
                              var20.readFromNBT(var23);
                           }
                        }
                     }
                  }
               }
            }

            Iterator var27 = var25.iterator();

            while(var27.hasNext()) {
               BlockPos var26 = (BlockPos)var27.next();
               Block var28 = var10.getBlockState(var26).getBlock();
               var10.notifyNeighborsRespectDebug(var26, var28);
            }

            if (var9 <= 0) {
               throw new CommandException("commands.fill.failed", new Object[0]);
            } else {
               var1.setCommandStat(CommandResultStats.Type.AFFECTED_BLOCKS, var9);
               notifyOperators(var1, this, "commands.fill.success", new Object[]{var9});
            }
         } else {
            throw new CommandException("commands.fill.outOfWorld", new Object[0]);
         }
      }
   }

   public List addTabCompletionOptions(ICommandSender var1, String[] var2, BlockPos var3) {
      return var2.length > 0 && var2.length <= 3 ? func_175771_a(var2, 0, var3) : (var2.length > 3 && var2.length <= 6 ? func_175771_a(var2, 3, var3) : (var2.length == 7 ? getListOfStringsMatchingLastWord(var2, Block.blockRegistry.getKeys()) : (var2.length == 9 ? getListOfStringsMatchingLastWord(var2, new String[]{"replace", "destroy", "keep", "hollow", "outline"}) : (var2.length == 10 && "replace".equals(var2[8]) ? getListOfStringsMatchingLastWord(var2, Block.blockRegistry.getKeys()) : null))));
   }

   public String getCommandName() {
      return "fill";
   }
}
