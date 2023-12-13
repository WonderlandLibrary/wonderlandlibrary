package net.minecraft.command;

import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.JsonToNBT;
import net.minecraft.nbt.NBTException;
import net.minecraft.nbt.NBTTagCompound;

public class CommandEntityData extends CommandBase {
   public String getCommandName() {
      return "entitydata";
   }

   public void processCommand(ICommandSender var1, String[] var2) throws CommandException {
      if (var2.length < 2) {
         throw new WrongUsageException("commands.entitydata.usage", new Object[0]);
      } else {
         Entity var3 = func_175768_b(var1, var2[0]);
         if (var3 instanceof EntityPlayer) {
            throw new CommandException("commands.entitydata.noPlayers", new Object[]{var3.getDisplayName()});
         } else {
            NBTTagCompound var4 = new NBTTagCompound();
            var3.writeToNBT(var4);
            NBTTagCompound var5 = (NBTTagCompound)var4.copy();

            NBTTagCompound var6;
            try {
               var6 = JsonToNBT.getTagFromJson(getChatComponentFromNthArg(var1, var2, 1).getUnformattedText());
            } catch (NBTException var9) {
               throw new CommandException("commands.entitydata.tagError", new Object[]{var9.getMessage()});
            }

            var6.removeTag("UUIDMost");
            var6.removeTag("UUIDLeast");
            var4.merge(var6);
            if (var4.equals(var5)) {
               throw new CommandException("commands.entitydata.failed", new Object[]{var4.toString()});
            } else {
               var3.readFromNBT(var4);
               notifyOperators(var1, this, "commands.entitydata.success", new Object[]{var4.toString()});
            }
         }
      }
   }

   public String getCommandUsage(ICommandSender var1) {
      return "commands.entitydata.usage";
   }

   public int getRequiredPermissionLevel() {
      return 2;
   }

   public boolean isUsernameIndex(String[] var1, int var2) {
      return var2 == 0;
   }
}
