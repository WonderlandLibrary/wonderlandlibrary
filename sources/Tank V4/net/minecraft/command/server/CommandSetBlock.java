package net.minecraft.command.server;

import java.util.List;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.command.CommandBase;
import net.minecraft.command.CommandException;
import net.minecraft.command.CommandResultStats;
import net.minecraft.command.ICommandSender;
import net.minecraft.command.WrongUsageException;
import net.minecraft.init.Blocks;
import net.minecraft.inventory.IInventory;
import net.minecraft.nbt.JsonToNBT;
import net.minecraft.nbt.NBTException;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.BlockPos;
import net.minecraft.world.World;

public class CommandSetBlock extends CommandBase {
   public List addTabCompletionOptions(ICommandSender var1, String[] var2, BlockPos var3) {
      return var2.length > 0 && var2.length <= 3 ? func_175771_a(var2, 0, var3) : (var2.length == 4 ? getListOfStringsMatchingLastWord(var2, Block.blockRegistry.getKeys()) : (var2.length == 6 ? getListOfStringsMatchingLastWord(var2, new String[]{"replace", "destroy", "keep"}) : null));
   }

   public int getRequiredPermissionLevel() {
      return 2;
   }

   public void processCommand(ICommandSender var1, String[] var2) throws CommandException {
      if (var2.length < 4) {
         throw new WrongUsageException("commands.setblock.usage", new Object[0]);
      } else {
         var1.setCommandStat(CommandResultStats.Type.AFFECTED_BLOCKS, 0);
         BlockPos var3 = parseBlockPos(var1, var2, 0, false);
         Block var4 = CommandBase.getBlockByText(var1, var2[3]);
         int var5 = 0;
         if (var2.length >= 5) {
            var5 = parseInt(var2[4], 0, 15);
         }

         World var6 = var1.getEntityWorld();
         if (!var6.isBlockLoaded(var3)) {
            throw new CommandException("commands.setblock.outOfWorld", new Object[0]);
         } else {
            NBTTagCompound var7 = new NBTTagCompound();
            boolean var8 = false;
            if (var2.length >= 7 && var4.hasTileEntity()) {
               String var9 = getChatComponentFromNthArg(var1, var2, 6).getUnformattedText();

               try {
                  var7 = JsonToNBT.getTagFromJson(var9);
                  var8 = true;
               } catch (NBTException var13) {
                  throw new CommandException("commands.setblock.tagError", new Object[]{var13.getMessage()});
               }
            }

            if (var2.length >= 6) {
               if (var2[5].equals("destroy")) {
                  var6.destroyBlock(var3, true);
                  if (var4 == Blocks.air) {
                     notifyOperators(var1, this, "commands.setblock.success", new Object[0]);
                     return;
                  }
               } else if (var2[5].equals("keep") && !var6.isAirBlock(var3)) {
                  throw new CommandException("commands.setblock.noChange", new Object[0]);
               }
            }

            TileEntity var14 = var6.getTileEntity(var3);
            if (var14 != null) {
               if (var14 instanceof IInventory) {
                  ((IInventory)var14).clear();
               }

               var6.setBlockState(var3, Blocks.air.getDefaultState(), var4 == Blocks.air ? 2 : 4);
            }

            IBlockState var10 = var4.getStateFromMeta(var5);
            if (!var6.setBlockState(var3, var10, 2)) {
               throw new CommandException("commands.setblock.noChange", new Object[0]);
            } else {
               if (var8) {
                  TileEntity var11 = var6.getTileEntity(var3);
                  if (var11 != null) {
                     var7.setInteger("x", var3.getX());
                     var7.setInteger("y", var3.getY());
                     var7.setInteger("z", var3.getZ());
                     var11.readFromNBT(var7);
                  }
               }

               var6.notifyNeighborsRespectDebug(var3, var10.getBlock());
               var1.setCommandStat(CommandResultStats.Type.AFFECTED_BLOCKS, 1);
               notifyOperators(var1, this, "commands.setblock.success", new Object[0]);
            }
         }
      }
   }

   public String getCommandName() {
      return "setblock";
   }

   public String getCommandUsage(ICommandSender var1) {
      return "commands.setblock.usage";
   }
}
