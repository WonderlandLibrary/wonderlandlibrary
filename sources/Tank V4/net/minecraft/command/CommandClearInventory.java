package net.minecraft.command;

import java.util.List;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.Item;
import net.minecraft.nbt.JsonToNBT;
import net.minecraft.nbt.NBTException;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.BlockPos;
import net.minecraft.util.ChatComponentTranslation;

public class CommandClearInventory extends CommandBase {
   protected String[] func_147209_d() {
      return MinecraftServer.getServer().getAllUsernames();
   }

   public boolean isUsernameIndex(String[] var1, int var2) {
      return var2 == 0;
   }

   public String getCommandUsage(ICommandSender var1) {
      return "commands.clear.usage";
   }

   public List addTabCompletionOptions(ICommandSender var1, String[] var2, BlockPos var3) {
      return var2.length == 1 ? getListOfStringsMatchingLastWord(var2, this.func_147209_d()) : (var2.length == 2 ? getListOfStringsMatchingLastWord(var2, Item.itemRegistry.getKeys()) : null);
   }

   public void processCommand(ICommandSender var1, String[] var2) throws CommandException {
      EntityPlayerMP var3 = var2.length == 0 ? getCommandSenderAsPlayer(var1) : getPlayer(var1, var2[0]);
      Item var4 = var2.length >= 2 ? getItemByText(var1, var2[1]) : null;
      int var5 = var2.length >= 3 ? parseInt(var2[2], -1) : -1;
      int var6 = var2.length >= 4 ? parseInt(var2[3], -1) : -1;
      NBTTagCompound var7 = null;
      if (var2.length >= 5) {
         try {
            var7 = JsonToNBT.getTagFromJson(buildString(var2, 4));
         } catch (NBTException var9) {
            throw new CommandException("commands.clear.tagError", new Object[]{var9.getMessage()});
         }
      }

      if (var2.length >= 2 && var4 == null) {
         throw new CommandException("commands.clear.failure", new Object[]{var3.getName()});
      } else {
         int var8 = var3.inventory.clearMatchingItems(var4, var5, var6, var7);
         var3.inventoryContainer.detectAndSendChanges();
         if (!var3.capabilities.isCreativeMode) {
            var3.updateHeldItem();
         }

         var1.setCommandStat(CommandResultStats.Type.AFFECTED_ITEMS, var8);
         if (var8 == 0) {
            throw new CommandException("commands.clear.failure", new Object[]{var3.getName()});
         } else {
            if (var6 == 0) {
               var1.addChatMessage(new ChatComponentTranslation("commands.clear.testing", new Object[]{var3.getName(), var8}));
            } else {
               notifyOperators(var1, this, "commands.clear.success", new Object[]{var3.getName(), var8});
            }

         }
      }
   }

   public String getCommandName() {
      return "clear";
   }

   public int getRequiredPermissionLevel() {
      return 2;
   }
}
