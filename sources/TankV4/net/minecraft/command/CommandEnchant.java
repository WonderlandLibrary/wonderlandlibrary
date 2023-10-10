package net.minecraft.command;

import java.util.List;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.BlockPos;

public class CommandEnchant extends CommandBase {
   protected String[] getListOfPlayers() {
      return MinecraftServer.getServer().getAllUsernames();
   }

   public void processCommand(ICommandSender var1, String[] var2) throws CommandException {
      if (var2.length < 2) {
         throw new WrongUsageException("commands.enchant.usage", new Object[0]);
      } else {
         EntityPlayerMP var3 = getPlayer(var1, var2[0]);
         var1.setCommandStat(CommandResultStats.Type.AFFECTED_ITEMS, 0);

         int var4;
         try {
            var4 = parseInt(var2[1], 0);
         } catch (NumberInvalidException var12) {
            Enchantment var6 = Enchantment.getEnchantmentByLocation(var2[1]);
            if (var6 == null) {
               throw var12;
            }

            var4 = var6.effectId;
         }

         int var5 = 1;
         ItemStack var13 = var3.getCurrentEquippedItem();
         if (var13 == null) {
            throw new CommandException("commands.enchant.noItem", new Object[0]);
         } else {
            Enchantment var7 = Enchantment.getEnchantmentById(var4);
            if (var7 == null) {
               throw new NumberInvalidException("commands.enchant.notFound", new Object[]{var4});
            } else if (!var7.canApply(var13)) {
               throw new CommandException("commands.enchant.cantEnchant", new Object[0]);
            } else {
               if (var2.length >= 3) {
                  var5 = parseInt(var2[2], var7.getMinLevel(), var7.getMaxLevel());
               }

               if (var13.hasTagCompound()) {
                  NBTTagList var8 = var13.getEnchantmentTagList();
                  if (var8 != null) {
                     for(int var9 = 0; var9 < var8.tagCount(); ++var9) {
                        short var10 = var8.getCompoundTagAt(var9).getShort("id");
                        if (Enchantment.getEnchantmentById(var10) != null) {
                           Enchantment var11 = Enchantment.getEnchantmentById(var10);
                           if (!var11.canApplyTogether(var7)) {
                              throw new CommandException("commands.enchant.cantCombine", new Object[]{var7.getTranslatedName(var5), var11.getTranslatedName(var8.getCompoundTagAt(var9).getShort("lvl"))});
                           }
                        }
                     }
                  }
               }

               var13.addEnchantment(var7, var5);
               notifyOperators(var1, this, "commands.enchant.success", new Object[0]);
               var1.setCommandStat(CommandResultStats.Type.AFFECTED_ITEMS, 1);
            }
         }
      }
   }

   public String getCommandName() {
      return "enchant";
   }

   public boolean isUsernameIndex(String[] var1, int var2) {
      return var2 == 0;
   }

   public List addTabCompletionOptions(ICommandSender var1, String[] var2, BlockPos var3) {
      return var2.length == 1 ? getListOfStringsMatchingLastWord(var2, this.getListOfPlayers()) : (var2.length == 2 ? getListOfStringsMatchingLastWord(var2, Enchantment.func_181077_c()) : null);
   }

   public int getRequiredPermissionLevel() {
      return 2;
   }

   public String getCommandUsage(ICommandSender var1) {
      return "commands.enchant.usage";
   }
}
