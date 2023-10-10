package net.minecraft.command.server;

import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import net.minecraft.command.CommandBase;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.command.PlayerNotFoundException;
import net.minecraft.command.WrongUsageException;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.management.IPBanEntry;
import net.minecraft.util.BlockPos;
import net.minecraft.util.IChatComponent;

public class CommandBanIp extends CommandBase {
   public static final Pattern field_147211_a = Pattern.compile("^([01]?\\d\\d?|2[0-4]\\d|25[0-5])\\.([01]?\\d\\d?|2[0-4]\\d|25[0-5])\\.([01]?\\d\\d?|2[0-4]\\d|25[0-5])\\.([01]?\\d\\d?|2[0-4]\\d|25[0-5])$");

   public void processCommand(ICommandSender var1, String[] var2) throws CommandException {
      if (var2.length >= 1 && var2[0].length() > 1) {
         IChatComponent var3 = var2.length >= 2 ? getChatComponentFromNthArg(var1, var2, 1) : null;
         Matcher var4 = field_147211_a.matcher(var2[0]);
         if (var4.matches()) {
            this.func_147210_a(var1, var2[0], var3 == null ? null : var3.getUnformattedText());
         } else {
            EntityPlayerMP var5 = MinecraftServer.getServer().getConfigurationManager().getPlayerByUsername(var2[0]);
            if (var5 == null) {
               throw new PlayerNotFoundException("commands.banip.invalid", new Object[0]);
            }

            this.func_147210_a(var1, var5.getPlayerIP(), var3 == null ? null : var3.getUnformattedText());
         }

      } else {
         throw new WrongUsageException("commands.banip.usage", new Object[0]);
      }
   }

   public int getRequiredPermissionLevel() {
      return 3;
   }

   protected void func_147210_a(ICommandSender var1, String var2, String var3) {
      IPBanEntry var4 = new IPBanEntry(var2, (Date)null, var1.getName(), (Date)null, var3);
      MinecraftServer.getServer().getConfigurationManager().getBannedIPs().addEntry(var4);
      List var5 = MinecraftServer.getServer().getConfigurationManager().getPlayersMatchingAddress(var2);
      String[] var6 = new String[var5.size()];
      int var7 = 0;

      EntityPlayerMP var8;
      for(Iterator var9 = var5.iterator(); var9.hasNext(); var6[var7++] = var8.getName()) {
         var8 = (EntityPlayerMP)var9.next();
         var8.playerNetServerHandler.kickPlayerFromServer("You have been IP banned.");
      }

      if (var5.isEmpty()) {
         notifyOperators(var1, this, "commands.banip.success", new Object[]{var2});
      } else {
         notifyOperators(var1, this, "commands.banip.success.players", new Object[]{var2, joinNiceString(var6)});
      }

   }

   public String getCommandName() {
      return "ban-ip";
   }

   public List addTabCompletionOptions(ICommandSender var1, String[] var2, BlockPos var3) {
      return var2.length == 1 ? getListOfStringsMatchingLastWord(var2, MinecraftServer.getServer().getAllUsernames()) : null;
   }

   public boolean canCommandSenderUseCommand(ICommandSender var1) {
      return MinecraftServer.getServer().getConfigurationManager().getBannedIPs().isLanServer() && super.canCommandSenderUseCommand(var1);
   }

   public String getCommandUsage(ICommandSender var1) {
      return "commands.banip.usage";
   }
}
