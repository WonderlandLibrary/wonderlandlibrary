package net.minecraft.command;

import java.util.List;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.BlockPos;

public class CommandEnchant
  extends CommandBase
{
  private static final String __OBFID = "CL_00000377";
  
  public CommandEnchant() {}
  
  public String getCommandName()
  {
    return "enchant";
  }
  
  public int getRequiredPermissionLevel()
  {
    return 2;
  }
  
  public String getCommandUsage(ICommandSender sender)
  {
    return "orders.enchant.usage";
  }
  
  public void processCommand(ICommandSender sender, String[] args)
    throws CommandException
  {
    if (args.length < 2) {
      throw new WrongUsageException("orders.enchant.usage", new Object[0]);
    }
    EntityPlayerMP var3 = getPlayer(sender, args[0]);
    sender.func_174794_a(CommandResultStats.Type.AFFECTED_ITEMS, 0);
    int var4;
    try
    {
      var4 = parseInt(args[1], 0);
    }
    catch (NumberInvalidException var12)
    {
      int var4;
      Enchantment var6 = Enchantment.func_180305_b(args[1]);
      if (var6 == null) {
        throw var12;
      }
      var4 = var6.effectId;
    }
    int var5 = 1;
    ItemStack var13 = var3.getCurrentEquippedItem();
    if (var13 == null) {
      throw new CommandException("orders.enchant.noItem", new Object[0]);
    }
    Enchantment var7 = Enchantment.func_180306_c(var4);
    if (var7 == null) {
      throw new NumberInvalidException("orders.enchant.notFound", new Object[] { Integer.valueOf(var4) });
    }
    if (!var7.canApply(var13)) {
      throw new CommandException("orders.enchant.cantEnchant", new Object[0]);
    }
    if (args.length >= 3) {
      var5 = parseInt(args[2], var7.getMinLevel(), var7.getMaxLevel());
    }
    if (var13.hasTagCompound())
    {
      NBTTagList var8 = var13.getEnchantmentTagList();
      if (var8 != null) {
        for (int var9 = 0; var9 < var8.tagCount(); var9++)
        {
          short var10 = var8.getCompoundTagAt(var9).getShort("id");
          if (Enchantment.func_180306_c(var10) != null)
          {
            Enchantment var11 = Enchantment.func_180306_c(var10);
            if (!var11.canApplyTogether(var7)) {
              throw new CommandException("orders.enchant.cantCombine", new Object[] { var7.getTranslatedName(var5), var11.getTranslatedName(var8.getCompoundTagAt(var9).getShort("lvl")) });
            }
          }
        }
      }
    }
    var13.addEnchantment(var7, var5);
    notifyOperators(sender, this, "orders.enchant.success", new Object[0]);
    sender.func_174794_a(CommandResultStats.Type.AFFECTED_ITEMS, 1);
  }
  
  public List addTabCompletionOptions(ICommandSender sender, String[] args, BlockPos pos)
  {
    return args.length == 2 ? getListOfStringsMatchingLastWord(args, Enchantment.func_180304_c()) : args.length == 1 ? getListOfStringsMatchingLastWord(args, getListOfPlayers()) : null;
  }
  
  protected String[] getListOfPlayers()
  {
    return MinecraftServer.getServer().getAllUsernames();
  }
  
  public boolean isUsernameIndex(String[] args, int index)
  {
    return index == 0;
  }
}
