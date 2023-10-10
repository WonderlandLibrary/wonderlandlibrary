package net.minecraft.command.server;

import com.google.common.collect.Lists;
import com.mojang.authlib.GameProfile;
import java.util.ArrayList;
import java.util.List;
import net.minecraft.command.CommandBase;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.command.WrongUsageException;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.BlockPos;

public class CommandOp extends CommandBase {
   public List addTabCompletionOptions(ICommandSender var1, String[] var2, BlockPos var3) {
      if (var2.length == 1) {
         String var4 = var2[var2.length - 1];
         ArrayList var5 = Lists.newArrayList();
         GameProfile[] var9;
         int var8 = (var9 = MinecraftServer.getServer().getGameProfiles()).length;

         for(int var7 = 0; var7 < var8; ++var7) {
            GameProfile var6 = var9[var7];
            if (!MinecraftServer.getServer().getConfigurationManager().canSendCommands(var6) && doesStringStartWith(var4, var6.getName())) {
               var5.add(var6.getName());
            }
         }

         return var5;
      } else {
         return null;
      }
   }

   public String getCommandUsage(ICommandSender var1) {
      return "commands.op.usage";
   }

   public void processCommand(ICommandSender var1, String[] var2) throws CommandException {
      if (var2.length == 1 && var2[0].length() > 0) {
         MinecraftServer var3 = MinecraftServer.getServer();
         GameProfile var4 = var3.getPlayerProfileCache().getGameProfileForUsername(var2[0]);
         if (var4 == null) {
            throw new CommandException("commands.op.failed", new Object[]{var2[0]});
         } else {
            var3.getConfigurationManager().addOp(var4);
            notifyOperators(var1, this, "commands.op.success", new Object[]{var2[0]});
         }
      } else {
         throw new WrongUsageException("commands.op.usage", new Object[0]);
      }
   }

   public String getCommandName() {
      return "op";
   }

   public int getRequiredPermissionLevel() {
      return 3;
   }
}
