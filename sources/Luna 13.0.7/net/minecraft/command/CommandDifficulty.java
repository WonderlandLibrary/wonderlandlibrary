package net.minecraft.command;

import java.util.List;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.BlockPos;
import net.minecraft.util.ChatComponentTranslation;
import net.minecraft.world.EnumDifficulty;

public class CommandDifficulty
  extends CommandBase
{
  private static final String __OBFID = "CL_00000422";
  
  public CommandDifficulty() {}
  
  public String getCommandName()
  {
    return "difficulty";
  }
  
  public int getRequiredPermissionLevel()
  {
    return 2;
  }
  
  public String getCommandUsage(ICommandSender sender)
  {
    return "orders.difficulty.usage";
  }
  
  public void processCommand(ICommandSender sender, String[] args)
    throws CommandException
  {
    if (args.length <= 0) {
      throw new WrongUsageException("orders.difficulty.usage", new Object[0]);
    }
    EnumDifficulty var3 = func_180531_e(args[0]);
    MinecraftServer.getServer().setDifficultyForAllWorlds(var3);
    notifyOperators(sender, this, "orders.difficulty.success", new Object[] { new ChatComponentTranslation(var3.getDifficultyResourceKey(), new Object[0]) });
  }
  
  protected EnumDifficulty func_180531_e(String p_180531_1_)
    throws CommandException
  {
    return (!p_180531_1_.equalsIgnoreCase("peaceful")) && (!p_180531_1_.equalsIgnoreCase("p")) ? EnumDifficulty.EASY : (!p_180531_1_.equalsIgnoreCase("easy")) && (!p_180531_1_.equalsIgnoreCase("e")) ? EnumDifficulty.NORMAL : (!p_180531_1_.equalsIgnoreCase("normal")) && (!p_180531_1_.equalsIgnoreCase("n")) ? EnumDifficulty.HARD : (!p_180531_1_.equalsIgnoreCase("hard")) && (!p_180531_1_.equalsIgnoreCase("h")) ? EnumDifficulty.getDifficultyEnum(parseInt(p_180531_1_, 0, 3)) : EnumDifficulty.PEACEFUL;
  }
  
  public List addTabCompletionOptions(ICommandSender sender, String[] args, BlockPos pos)
  {
    return args.length == 1 ? getListOfStringsMatchingLastWord(args, new String[] { "peaceful", "easy", "normal", "hard" }) : null;
  }
}
