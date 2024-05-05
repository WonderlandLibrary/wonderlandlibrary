package net.minecraft.command;

import java.util.List;
import net.minecraft.util.BlockPos;
import net.minecraft.util.ChatComponentTranslation;
import net.minecraft.util.MathHelper;
import net.minecraft.world.WorldServer;
import net.minecraft.world.border.WorldBorder;

public class CommandWorldBorder
  extends CommandBase
{
  private static final String __OBFID = "CL_00002336";
  
  public CommandWorldBorder() {}
  
  public String getCommandName()
  {
    return "worldborder";
  }
  
  public int getRequiredPermissionLevel()
  {
    return 2;
  }
  
  public String getCommandUsage(ICommandSender sender)
  {
    return "orders.worldborder.usage";
  }
  
  public void processCommand(ICommandSender sender, String[] args)
    throws CommandException
  {
    if (args.length < 1) {
      throw new WrongUsageException("orders.worldborder.usage", new Object[0]);
    }
    WorldBorder var3 = getWorldBorder();
    if (args[0].equals("set"))
    {
      if ((args.length != 2) && (args.length != 3)) {
        throw new WrongUsageException("orders.worldborder.set.usage", new Object[0]);
      }
      double var4 = var3.getTargetSize();
      double var6 = parseDouble(args[1], 1.0D, 6.0E7D);
      long var8 = args.length > 2 ? parseLong(args[2], 0L, 9223372036854775L) * 1000L : 0L;
      if (var8 > 0L)
      {
        var3.setTransition(var4, var6, var8);
        if (var4 > var6) {
          notifyOperators(sender, this, "orders.worldborder.setSlowly.shrink.success", new Object[] { String.format("%.1f", new Object[] { Double.valueOf(var6) }), String.format("%.1f", new Object[] { Double.valueOf(var4) }), Long.toString(var8 / 1000L) });
        } else {
          notifyOperators(sender, this, "orders.worldborder.setSlowly.grow.success", new Object[] { String.format("%.1f", new Object[] { Double.valueOf(var6) }), String.format("%.1f", new Object[] { Double.valueOf(var4) }), Long.toString(var8 / 1000L) });
        }
      }
      else
      {
        var3.setTransition(var6);
        notifyOperators(sender, this, "orders.worldborder.set.success", new Object[] { String.format("%.1f", new Object[] { Double.valueOf(var6) }), String.format("%.1f", new Object[] { Double.valueOf(var4) }) });
      }
    }
    else if (args[0].equals("add"))
    {
      if ((args.length != 2) && (args.length != 3)) {
        throw new WrongUsageException("orders.worldborder.add.usage", new Object[0]);
      }
      double var4 = var3.getDiameter();
      double var6 = var4 + parseDouble(args[1], -var4, 6.0E7D - var4);
      long var8 = var3.getTimeUntilTarget() + (args.length > 2 ? parseLong(args[2], 0L, 9223372036854775L) * 1000L : 0L);
      if (var8 > 0L)
      {
        var3.setTransition(var4, var6, var8);
        if (var4 > var6) {
          notifyOperators(sender, this, "orders.worldborder.setSlowly.shrink.success", new Object[] { String.format("%.1f", new Object[] { Double.valueOf(var6) }), String.format("%.1f", new Object[] { Double.valueOf(var4) }), Long.toString(var8 / 1000L) });
        } else {
          notifyOperators(sender, this, "orders.worldborder.setSlowly.grow.success", new Object[] { String.format("%.1f", new Object[] { Double.valueOf(var6) }), String.format("%.1f", new Object[] { Double.valueOf(var4) }), Long.toString(var8 / 1000L) });
        }
      }
      else
      {
        var3.setTransition(var6);
        notifyOperators(sender, this, "orders.worldborder.set.success", new Object[] { String.format("%.1f", new Object[] { Double.valueOf(var6) }), String.format("%.1f", new Object[] { Double.valueOf(var4) }) });
      }
    }
    else if (args[0].equals("center"))
    {
      if (args.length != 3) {
        throw new WrongUsageException("orders.worldborder.center.usage", new Object[0]);
      }
      BlockPos var10 = sender.getPosition();
      double var5 = func_175761_b(var10.getX() + 0.5D, args[1], true);
      double var7 = func_175761_b(var10.getZ() + 0.5D, args[2], true);
      var3.setCenter(var5, var7);
      notifyOperators(sender, this, "orders.worldborder.center.success", new Object[] { Double.valueOf(var5), Double.valueOf(var7) });
    }
    else if (args[0].equals("damage"))
    {
      if (args.length < 2) {
        throw new WrongUsageException("orders.worldborder.damage.usage", new Object[0]);
      }
      if (args[1].equals("buffer"))
      {
        if (args.length != 3) {
          throw new WrongUsageException("orders.worldborder.damage.buffer.usage", new Object[0]);
        }
        double var4 = parseDouble(args[2], 0.0D);
        double var6 = var3.getDamageBuffer();
        var3.setDamageBuffer(var4);
        notifyOperators(sender, this, "orders.worldborder.damage.buffer.success", new Object[] { String.format("%.1f", new Object[] { Double.valueOf(var4) }), String.format("%.1f", new Object[] { Double.valueOf(var6) }) });
      }
      else if (args[1].equals("amount"))
      {
        if (args.length != 3) {
          throw new WrongUsageException("orders.worldborder.damage.amount.usage", new Object[0]);
        }
        double var4 = parseDouble(args[2], 0.0D);
        double var6 = var3.func_177727_n();
        var3.func_177744_c(var4);
        notifyOperators(sender, this, "orders.worldborder.damage.amount.success", new Object[] { String.format("%.2f", new Object[] { Double.valueOf(var4) }), String.format("%.2f", new Object[] { Double.valueOf(var6) }) });
      }
    }
    else if (args[0].equals("warning"))
    {
      if (args.length < 2) {
        throw new WrongUsageException("orders.worldborder.warning.usage", new Object[0]);
      }
      int var11 = parseInt(args[2], 0);
      if (args[1].equals("time"))
      {
        if (args.length != 3) {
          throw new WrongUsageException("orders.worldborder.warning.time.usage", new Object[0]);
        }
        int var12 = var3.getWarningTime();
        var3.setWarningTime(var11);
        notifyOperators(sender, this, "orders.worldborder.warning.time.success", new Object[] { Integer.valueOf(var11), Integer.valueOf(var12) });
      }
      else if (args[1].equals("distance"))
      {
        if (args.length != 3) {
          throw new WrongUsageException("orders.worldborder.warning.distance.usage", new Object[0]);
        }
        int var12 = var3.getWarningDistance();
        var3.setWarningDistance(var11);
        notifyOperators(sender, this, "orders.worldborder.warning.distance.success", new Object[] { Integer.valueOf(var11), Integer.valueOf(var12) });
      }
    }
    else if (args[0].equals("get"))
    {
      double var4 = var3.getDiameter();
      sender.func_174794_a(CommandResultStats.Type.QUERY_RESULT, MathHelper.floor_double(var4 + 0.5D));
      sender.addChatMessage(new ChatComponentTranslation("orders.worldborder.get.success", new Object[] { String.format("%.0f", new Object[] { Double.valueOf(var4) }) }));
    }
  }
  
  protected WorldBorder getWorldBorder()
  {
    return net.minecraft.server.MinecraftServer.getServer().worldServers[0].getWorldBorder();
  }
  
  public List addTabCompletionOptions(ICommandSender sender, String[] args, BlockPos pos)
  {
    return (args.length == 2) && (args[0].equals("warning")) ? getListOfStringsMatchingLastWord(args, new String[] { "time", "distance" }) : (args.length == 2) && (args[0].equals("damage")) ? getListOfStringsMatchingLastWord(args, new String[] { "buffer", "amount" }) : args.length == 1 ? getListOfStringsMatchingLastWord(args, new String[] { "set", "center", "damage", "warning", "add", "get" }) : null;
  }
}
