package net.minecraft.command.server;

import java.util.List;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.command.CommandBase;
import net.minecraft.command.CommandException;
import net.minecraft.command.CommandResultStats;
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

public class CommandTestForBlock extends CommandBase {
   public List addTabCompletionOptions(ICommandSender var1, String[] var2, BlockPos var3) {
      return var2.length > 0 && var2.length <= 3 ? func_175771_a(var2, 0, var3) : (var2.length == 4 ? getListOfStringsMatchingLastWord(var2, Block.blockRegistry.getKeys()) : null);
   }

   public void processCommand(ICommandSender var1, String[] var2) throws CommandException {
      if (var2.length < 4) {
         throw new WrongUsageException("commands.testforblock.usage", new Object[0]);
      } else {
         var1.setCommandStat(CommandResultStats.Type.AFFECTED_BLOCKS, 0);
         BlockPos var3 = parseBlockPos(var1, var2, 0, false);
         Block var4 = Block.getBlockFromName(var2[3]);
         if (var4 == null) {
            throw new NumberInvalidException("commands.setblock.notFound", new Object[]{var2[3]});
         } else {
            int var5 = -1;
            if (var2.length >= 5) {
               var5 = parseInt(var2[4], -1, 15);
            }

            World var6 = var1.getEntityWorld();
            if (!var6.isBlockLoaded(var3)) {
               throw new CommandException("commands.testforblock.outOfWorld", new Object[0]);
            } else {
               NBTTagCompound var7 = new NBTTagCompound();
               boolean var8 = false;
               if (var2.length >= 6 && var4.hasTileEntity()) {
                  String var9 = getChatComponentFromNthArg(var1, var2, 5).getUnformattedText();

                  try {
                     var7 = JsonToNBT.getTagFromJson(var9);
                     var8 = true;
                  } catch (NBTException var14) {
                     throw new CommandException("commands.setblock.tagError", new Object[]{var14.getMessage()});
                  }
               }

               IBlockState var15 = var6.getBlockState(var3);
               Block var10 = var15.getBlock();
               if (var10 != var4) {
                  throw new CommandException("commands.testforblock.failed.tile", new Object[]{var3.getX(), var3.getY(), var3.getZ(), var10.getLocalizedName(), var4.getLocalizedName()});
               } else {
                  if (var5 > -1) {
                     int var11 = var15.getBlock().getMetaFromState(var15);
                     if (var11 != var5) {
                        throw new CommandException("commands.testforblock.failed.data", new Object[]{var3.getX(), var3.getY(), var3.getZ(), var11, var5});
                     }
                  }

                  if (var8) {
                     TileEntity var16 = var6.getTileEntity(var3);
                     if (var16 == null) {
                        throw new CommandException("commands.testforblock.failed.tileEntity", new Object[]{var3.getX(), var3.getY(), var3.getZ()});
                     }

                     NBTTagCompound var12 = new NBTTagCompound();
                     var16.writeToNBT(var12);
                     if (!NBTUtil.func_181123_a(var7, var12, true)) {
                        throw new CommandException("commands.testforblock.failed.nbt", new Object[]{var3.getX(), var3.getY(), var3.getZ()});
                     }
                  }

                  var1.setCommandStat(CommandResultStats.Type.AFFECTED_BLOCKS, 1);
                  notifyOperators(var1, this, "commands.testforblock.success", new Object[]{var3.getX(), var3.getY(), var3.getZ()});
               }
            }
         }
      }
   }

   public String getCommandUsage(ICommandSender var1) {
      return "commands.testforblock.usage";
   }

   public String getCommandName() {
      return "testforblock";
   }

   public int getRequiredPermissionLevel() {
      return 2;
   }
}
