package net.minecraft.command;

import java.util.Iterator;
import java.util.List;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.network.play.server.S19PacketEntityStatus;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.BlockPos;
import net.minecraft.util.ChatComponentText;
import net.minecraft.world.GameRules;

public class CommandGameRule extends CommandBase {
   public int getRequiredPermissionLevel() {
      return 2;
   }

   public List addTabCompletionOptions(ICommandSender var1, String[] var2, BlockPos var3) {
      if (var2.length == 1) {
         return getListOfStringsMatchingLastWord(var2, this.getGameRules().getRules());
      } else {
         if (var2.length == 2) {
            GameRules var4 = this.getGameRules();
            if (var4.areSameType(var2[0], GameRules.ValueType.BOOLEAN_VALUE)) {
               return getListOfStringsMatchingLastWord(var2, new String[]{"true", "false"});
            }
         }

         return null;
      }
   }

   public void processCommand(ICommandSender var1, String[] var2) throws CommandException {
      GameRules var3 = this.getGameRules();
      String var4 = var2.length > 0 ? var2[0] : "";
      String var5 = var2.length > 1 ? buildString(var2, 1) : "";
      switch(var2.length) {
      case 0:
         var1.addChatMessage(new ChatComponentText(joinNiceString(var3.getRules())));
         break;
      case 1:
         if (!var3.hasRule(var4)) {
            throw new CommandException("commands.gamerule.norule", new Object[]{var4});
         }

         String var6 = var3.getString(var4);
         var1.addChatMessage((new ChatComponentText(var4)).appendText(" = ").appendText(var6));
         var1.setCommandStat(CommandResultStats.Type.QUERY_RESULT, var3.getInt(var4));
         break;
      default:
         if (var3.areSameType(var4, GameRules.ValueType.BOOLEAN_VALUE) && !"true".equals(var5) && !"false".equals(var5)) {
            throw new CommandException("commands.generic.boolean.invalid", new Object[]{var5});
         }

         var3.setOrCreateGameRule(var4, var5);
         func_175773_a(var3, var4);
         notifyOperators(var1, this, "commands.gamerule.success", new Object[0]);
      }

   }

   public static void func_175773_a(GameRules var0, String var1) {
      if ("reducedDebugInfo".equals(var1)) {
         byte var2 = (byte)(var0.getBoolean(var1) ? 22 : 23);
         Iterator var4 = MinecraftServer.getServer().getConfigurationManager().func_181057_v().iterator();

         while(var4.hasNext()) {
            EntityPlayerMP var3 = (EntityPlayerMP)var4.next();
            var3.playerNetServerHandler.sendPacket(new S19PacketEntityStatus(var3, var2));
         }
      }

   }

   public String getCommandUsage(ICommandSender var1) {
      return "commands.gamerule.usage";
   }

   public String getCommandName() {
      return "gamerule";
   }

   private GameRules getGameRules() {
      return MinecraftServer.getServer().worldServerForDimension(0).getGameRules();
   }
}
