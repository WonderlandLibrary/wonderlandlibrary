package net.minecraft.command;

import com.google.common.collect.Lists;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import net.minecraft.entity.Entity;
import net.minecraft.scoreboard.IScoreObjectiveCriteria;
import net.minecraft.scoreboard.ScoreObjective;
import net.minecraft.scoreboard.Scoreboard;
import net.minecraft.server.MinecraftServer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityCommandBlock;
import net.minecraft.tileentity.TileEntitySign;
import net.minecraft.util.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.WorldServer;

public class CommandStats
  extends CommandBase
{
  private static final String __OBFID = "CL_00002339";
  
  public CommandStats() {}
  
  public String getCommandName()
  {
    return "stats";
  }
  
  public int getRequiredPermissionLevel()
  {
    return 2;
  }
  
  public String getCommandUsage(ICommandSender sender)
  {
    return "orders.stats.usage";
  }
  
  public void processCommand(ICommandSender sender, String[] args)
    throws CommandException
  {
    if (args.length < 1) {
      throw new WrongUsageException("orders.stats.usage", new Object[0]);
    }
    boolean var3;
    boolean var3;
    if (args[0].equals("entity"))
    {
      var3 = false;
    }
    else
    {
      if (!args[0].equals("block")) {
        throw new WrongUsageException("orders.stats.usage", new Object[0]);
      }
      var3 = true;
    }
    byte var4;
    byte var4;
    if (var3)
    {
      if (args.length < 5) {
        throw new WrongUsageException("orders.stats.block.usage", new Object[0]);
      }
      var4 = 4;
    }
    else
    {
      if (args.length < 3) {
        throw new WrongUsageException("orders.stats.entity.usage", new Object[0]);
      }
      var4 = 2;
    }
    int var11 = var4 + 1;
    String var5 = args[var4];
    if ("set".equals(var5))
    {
      if (args.length < var11 + 3)
      {
        if (var11 == 5) {
          throw new WrongUsageException("orders.stats.block.set.usage", new Object[0]);
        }
        throw new WrongUsageException("orders.stats.entity.set.usage", new Object[0]);
      }
    }
    else
    {
      if (!"clear".equals(var5)) {
        throw new WrongUsageException("orders.stats.usage", new Object[0]);
      }
      if (args.length < var11 + 1)
      {
        if (var11 == 5) {
          throw new WrongUsageException("orders.stats.block.clear.usage", new Object[0]);
        }
        throw new WrongUsageException("orders.stats.entity.clear.usage", new Object[0]);
      }
    }
    CommandResultStats.Type var6 = CommandResultStats.Type.func_179635_a(args[(var11++)]);
    if (var6 == null) {
      throw new CommandException("orders.stats.failed", new Object[0]);
    }
    World var7 = sender.getEntityWorld();
    CommandResultStats var8;
    CommandResultStats var8;
    if (var3)
    {
      BlockPos var9 = func_175757_a(sender, args, 1, false);
      TileEntity var10 = var7.getTileEntity(var9);
      if (var10 == null) {
        throw new CommandException("orders.stats.noCompatibleBlock", new Object[] { Integer.valueOf(var9.getX()), Integer.valueOf(var9.getY()), Integer.valueOf(var9.getZ()) });
      }
      CommandResultStats var8;
      if ((var10 instanceof TileEntityCommandBlock))
      {
        var8 = ((TileEntityCommandBlock)var10).func_175124_c();
      }
      else
      {
        if (!(var10 instanceof TileEntitySign)) {
          throw new CommandException("orders.stats.noCompatibleBlock", new Object[] { Integer.valueOf(var9.getX()), Integer.valueOf(var9.getY()), Integer.valueOf(var9.getZ()) });
        }
        var8 = ((TileEntitySign)var10).func_174880_d();
      }
    }
    else
    {
      Entity var12 = func_175768_b(sender, args[1]);
      var8 = var12.func_174807_aT();
    }
    if ("set".equals(var5))
    {
      String var13 = args[(var11++)];
      String var14 = args[var11];
      if ((var13.length() == 0) || (var14.length() == 0)) {
        throw new CommandException("orders.stats.failed", new Object[0]);
      }
      CommandResultStats.func_179667_a(var8, var6, var13, var14);
      notifyOperators(sender, this, "orders.stats.success", new Object[] { var6.func_179637_b(), var14, var13 });
    }
    else if ("clear".equals(var5))
    {
      CommandResultStats.func_179667_a(var8, var6, null, null);
      notifyOperators(sender, this, "orders.stats.cleared", new Object[] { var6.func_179637_b() });
    }
    if (var3)
    {
      BlockPos var9 = func_175757_a(sender, args, 1, false);
      TileEntity var10 = var7.getTileEntity(var9);
      var10.markDirty();
    }
  }
  
  public List addTabCompletionOptions(ICommandSender sender, String[] args, BlockPos pos)
  {
    return ((args.length != 3) || (!args[0].equals("entity"))) && ((args.length != 5) || (!args[0].equals("block"))) ? getListOfStringsMatchingLastWord(args, CommandResultStats.Type.func_179634_c()) : ((args.length != 4) || (!args[0].equals("entity"))) && ((args.length != 6) || (!args[0].equals("block"))) ? func_175762_a(args, func_175777_e()) : ((args.length != 6) || (!args[0].equals("entity"))) && ((args.length != 8) || (!args[0].equals("block"))) ? null : (args.length == 2) && (args[0].equals("entity")) ? getListOfStringsMatchingLastWord(args, func_175776_d()) : args.length == 1 ? getListOfStringsMatchingLastWord(args, new String[] { "entity", "block" }) : getListOfStringsMatchingLastWord(args, new String[] { "set", "clear" });
  }
  
  protected String[] func_175776_d()
  {
    return MinecraftServer.getServer().getAllUsernames();
  }
  
  protected List func_175777_e()
  {
    Collection var1 = MinecraftServer.getServer().worldServerForDimension(0).getScoreboard().getScoreObjectives();
    ArrayList var2 = Lists.newArrayList();
    Iterator var3 = var1.iterator();
    while (var3.hasNext())
    {
      ScoreObjective var4 = (ScoreObjective)var3.next();
      if (!var4.getCriteria().isReadOnly()) {
        var2.add(var4.getName());
      }
    }
    return var2;
  }
  
  public boolean isUsernameIndex(String[] args, int index)
  {
    return (args.length > 0) && (args[0].equals("entity")) && (index == 1);
  }
}
