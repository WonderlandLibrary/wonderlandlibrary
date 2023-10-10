package net.minecraft.command.server;

import java.util.List;
import net.minecraft.command.CommandBase;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.command.WrongUsageException;
import net.minecraft.entity.Entity;
import net.minecraft.nbt.JsonToNBT;
import net.minecraft.nbt.NBTException;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTUtil;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.BlockPos;

public class CommandTestFor extends CommandBase {
   public String getCommandUsage(ICommandSender var1) {
      return "commands.testfor.usage";
   }

   public String getCommandName() {
      return "testfor";
   }

   public List addTabCompletionOptions(ICommandSender var1, String[] var2, BlockPos var3) {
      return var2.length == 1 ? getListOfStringsMatchingLastWord(var2, MinecraftServer.getServer().getAllUsernames()) : null;
   }

   public void processCommand(ICommandSender var1, String[] var2) throws CommandException {
      if (var2.length < 1) {
         throw new WrongUsageException("commands.testfor.usage", new Object[0]);
      } else {
         Entity var3 = func_175768_b(var1, var2[0]);
         NBTTagCompound var4 = null;
         if (var2.length >= 2) {
            try {
               var4 = JsonToNBT.getTagFromJson(buildString(var2, 1));
            } catch (NBTException var7) {
               throw new CommandException("commands.testfor.tagError", new Object[]{var7.getMessage()});
            }
         }

         if (var4 != null) {
            NBTTagCompound var5 = new NBTTagCompound();
            var3.writeToNBT(var5);
            if (!NBTUtil.func_181123_a(var4, var5, true)) {
               throw new CommandException("commands.testfor.failure", new Object[]{var3.getName()});
            }
         }

         notifyOperators(var1, this, "commands.testfor.success", new Object[]{var3.getName()});
      }
   }

   public boolean isUsernameIndex(String[] var1, int var2) {
      return var2 == 0;
   }

   public int getRequiredPermissionLevel() {
      return 2;
   }
}
