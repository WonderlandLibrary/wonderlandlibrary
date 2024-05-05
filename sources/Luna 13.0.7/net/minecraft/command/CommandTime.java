package net.minecraft.command;

import java.util.List;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.WorldServer;

public class CommandTime
  extends CommandBase
{
  private static final String __OBFID = "CL_00001183";
  
  public CommandTime() {}
  
  public String getCommandName()
  {
    return "time";
  }
  
  public int getRequiredPermissionLevel()
  {
    return 2;
  }
  
  public String getCommandUsage(ICommandSender sender)
  {
    return "orders.time.usage";
  }
  
  public void processCommand(ICommandSender sender, String[] args)
    throws CommandException
  {
    if (args.length > 1)
    {
      if (args[0].equals("set"))
      {
        int var3;
        int var3;
        if (args[1].equals("day"))
        {
          var3 = 1000;
        }
        else
        {
          int var3;
          if (args[1].equals("night")) {
            var3 = 13000;
          } else {
            var3 = parseInt(args[1], 0);
          }
        }
        setTime(sender, var3);
        notifyOperators(sender, this, "orders.time.set", new Object[] { Integer.valueOf(var3) });
        return;
      }
      if (args[0].equals("add"))
      {
        int var3 = parseInt(args[1], 0);
        addTime(sender, var3);
        notifyOperators(sender, this, "orders.time.added", new Object[] { Integer.valueOf(var3) });
        return;
      }
      if (args[0].equals("query"))
      {
        if (args[1].equals("daytime"))
        {
          int var3 = (int)(sender.getEntityWorld().getWorldTime() % 2147483647L);
          sender.func_174794_a(CommandResultStats.Type.QUERY_RESULT, var3);
          notifyOperators(sender, this, "orders.time.query", new Object[] { Integer.valueOf(var3) });
          return;
        }
        if (args[1].equals("gametime"))
        {
          int var3 = (int)(sender.getEntityWorld().getTotalWorldTime() % 2147483647L);
          sender.func_174794_a(CommandResultStats.Type.QUERY_RESULT, var3);
          notifyOperators(sender, this, "orders.time.query", new Object[] { Integer.valueOf(var3) });
          return;
        }
      }
    }
    throw new WrongUsageException("orders.time.usage", new Object[0]);
  }
  
  public List addTabCompletionOptions(ICommandSender sender, String[] args, BlockPos pos)
  {
    return (args.length == 2) && (args[0].equals("query")) ? getListOfStringsMatchingLastWord(args, new String[] { "daytime", "gametime" }) : (args.length == 2) && (args[0].equals("set")) ? getListOfStringsMatchingLastWord(args, new String[] { "day", "night" }) : args.length == 1 ? getListOfStringsMatchingLastWord(args, new String[] { "set", "add", "query" }) : null;
  }
  
  protected void setTime(ICommandSender p_71552_1_, int p_71552_2_)
  {
    for (int var3 = 0; var3 < MinecraftServer.getServer().worldServers.length; var3++) {
      MinecraftServer.getServer().worldServers[var3].setWorldTime(p_71552_2_);
    }
  }
  
  protected void addTime(ICommandSender p_71553_1_, int p_71553_2_)
  {
    for (int var3 = 0; var3 < MinecraftServer.getServer().worldServers.length; var3++)
    {
      WorldServer var4 = MinecraftServer.getServer().worldServers[var3];
      var4.setWorldTime(var4.getWorldTime() + p_71553_2_);
    }
  }
}
