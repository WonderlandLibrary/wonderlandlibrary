package net.minecraft.command;

import com.google.common.collect.Maps;
import java.util.List;
import java.util.Map;
import net.minecraft.block.Block;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.JsonToNBT;
import net.minecraft.nbt.NBTException;
import net.minecraft.server.MinecraftServer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.BlockPos;
import net.minecraft.world.World;

public class CommandReplaceItem extends CommandBase {
   private static final Map SHORTCUTS = Maps.newHashMap();

   public void processCommand(ICommandSender var1, String[] var2) throws CommandException {
      if (var2.length < 1) {
         throw new WrongUsageException("commands.replaceitem.usage", new Object[0]);
      } else {
         boolean var3;
         if (var2[0].equals("entity")) {
            var3 = false;
         } else {
            if (!var2[0].equals("block")) {
               throw new WrongUsageException("commands.replaceitem.usage", new Object[0]);
            }

            var3 = true;
         }

         byte var4;
         if (var3) {
            if (var2.length < 6) {
               throw new WrongUsageException("commands.replaceitem.block.usage", new Object[0]);
            }

            var4 = 4;
         } else {
            if (var2.length < 4) {
               throw new WrongUsageException("commands.replaceitem.entity.usage", new Object[0]);
            }

            var4 = 2;
         }

         int var16 = var4 + 1;
         int var5 = this.getSlotForShortcut(var2[var4]);

         Item var6;
         try {
            var6 = getItemByText(var1, var2[var16]);
         } catch (NumberInvalidException var15) {
            if (Block.getBlockFromName(var2[var16]) != Blocks.air) {
               throw var15;
            }

            var6 = null;
         }

         ++var16;
         int var7 = var2.length > var16 ? parseInt(var2[var16++], 1, 64) : 1;
         int var8 = var2.length > var16 ? parseInt(var2[var16++]) : 0;
         ItemStack var9 = new ItemStack(var6, var7, var8);
         if (var2.length > var16) {
            String var10 = getChatComponentFromNthArg(var1, var2, var16).getUnformattedText();

            try {
               var9.setTagCompound(JsonToNBT.getTagFromJson(var10));
            } catch (NBTException var14) {
               throw new CommandException("commands.replaceitem.tagError", new Object[]{var14.getMessage()});
            }
         }

         if (var9.getItem() == null) {
            var9 = null;
         }

         if (var3) {
            var1.setCommandStat(CommandResultStats.Type.AFFECTED_ITEMS, 0);
            BlockPos var17 = parseBlockPos(var1, var2, 1, false);
            World var11 = var1.getEntityWorld();
            TileEntity var12 = var11.getTileEntity(var17);
            if (var12 == null || !(var12 instanceof IInventory)) {
               throw new CommandException("commands.replaceitem.noContainer", new Object[]{var17.getX(), var17.getY(), var17.getZ()});
            }

            IInventory var13 = (IInventory)var12;
            if (var5 >= 0 && var5 < var13.getSizeInventory()) {
               var13.setInventorySlotContents(var5, var9);
            }
         } else {
            Entity var18 = func_175768_b(var1, var2[1]);
            var1.setCommandStat(CommandResultStats.Type.AFFECTED_ITEMS, 0);
            if (var18 instanceof EntityPlayer) {
               ((EntityPlayer)var18).inventoryContainer.detectAndSendChanges();
            }

            if (!var18.replaceItemInInventory(var5, var9)) {
               throw new CommandException("commands.replaceitem.failed", new Object[]{var5, var7, var9 == null ? "Air" : var9.getChatComponent()});
            }

            if (var18 instanceof EntityPlayer) {
               ((EntityPlayer)var18).inventoryContainer.detectAndSendChanges();
            }
         }

         var1.setCommandStat(CommandResultStats.Type.AFFECTED_ITEMS, var7);
         notifyOperators(var1, this, "commands.replaceitem.success", new Object[]{var5, var7, var9 == null ? "Air" : var9.getChatComponent()});
      }
   }

   private int getSlotForShortcut(String var1) throws CommandException {
      if (!SHORTCUTS.containsKey(var1)) {
         throw new CommandException("commands.generic.parameter.invalid", new Object[]{var1});
      } else {
         return (Integer)SHORTCUTS.get(var1);
      }
   }

   public String getCommandUsage(ICommandSender var1) {
      return "commands.replaceitem.usage";
   }

   protected String[] getUsernames() {
      return MinecraftServer.getServer().getAllUsernames();
   }

   static {
      int var0;
      for(var0 = 0; var0 < 54; ++var0) {
         SHORTCUTS.put("slot.container." + var0, var0);
      }

      for(var0 = 0; var0 < 9; ++var0) {
         SHORTCUTS.put("slot.hotbar." + var0, var0);
      }

      for(var0 = 0; var0 < 27; ++var0) {
         SHORTCUTS.put("slot.inventory." + var0, 9 + var0);
      }

      for(var0 = 0; var0 < 27; ++var0) {
         SHORTCUTS.put("slot.enderchest." + var0, 200 + var0);
      }

      for(var0 = 0; var0 < 8; ++var0) {
         SHORTCUTS.put("slot.villager." + var0, 300 + var0);
      }

      for(var0 = 0; var0 < 15; ++var0) {
         SHORTCUTS.put("slot.horse." + var0, 500 + var0);
      }

      SHORTCUTS.put("slot.weapon", 99);
      SHORTCUTS.put("slot.armor.head", 103);
      SHORTCUTS.put("slot.armor.chest", 102);
      SHORTCUTS.put("slot.armor.legs", 101);
      SHORTCUTS.put("slot.armor.feet", 100);
      SHORTCUTS.put("slot.horse.saddle", 400);
      SHORTCUTS.put("slot.horse.armor", 401);
      SHORTCUTS.put("slot.horse.chest", 499);
   }

   public String getCommandName() {
      return "replaceitem";
   }

   public boolean isUsernameIndex(String[] var1, int var2) {
      return var1.length > 0 && var1[0].equals("entity") && var2 == 1;
   }

   public int getRequiredPermissionLevel() {
      return 2;
   }

   public List addTabCompletionOptions(ICommandSender var1, String[] var2, BlockPos var3) {
      return var2.length == 1 ? getListOfStringsMatchingLastWord(var2, new String[]{"entity", "block"}) : (var2.length == 2 && var2[0].equals("entity") ? getListOfStringsMatchingLastWord(var2, this.getUsernames()) : (var2.length >= 2 && var2.length <= 4 && var2[0].equals("block") ? func_175771_a(var2, 1, var3) : (var2.length == 3 && var2[0].equals("entity") || var2.length == 5 && var2[0].equals("block") ? getListOfStringsMatchingLastWord(var2, SHORTCUTS.keySet()) : (var2.length == 4 && var2[0].equals("entity") || var2.length == 6 && var2[0].equals("block") ? getListOfStringsMatchingLastWord(var2, Item.itemRegistry.getKeys()) : null))));
   }
}
