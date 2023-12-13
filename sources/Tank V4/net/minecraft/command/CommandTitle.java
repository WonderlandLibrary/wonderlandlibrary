package net.minecraft.command;

import com.google.gson.JsonParseException;
import java.util.List;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.network.play.server.S45PacketTitle;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.BlockPos;
import net.minecraft.util.ChatComponentProcessor;
import net.minecraft.util.IChatComponent;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class CommandTitle extends CommandBase {
   private static final Logger LOGGER = LogManager.getLogger();

   public String getCommandUsage(ICommandSender var1) {
      return "commands.title.usage";
   }

   public boolean isUsernameIndex(String[] var1, int var2) {
      return var2 == 0;
   }

   public void processCommand(ICommandSender var1, String[] var2) throws CommandException {
      if (var2.length < 2) {
         throw new WrongUsageException("commands.title.usage", new Object[0]);
      } else {
         if (var2.length < 3) {
            if ("title".equals(var2[1]) || "subtitle".equals(var2[1])) {
               throw new WrongUsageException("commands.title.usage.title", new Object[0]);
            }

            if ("times".equals(var2[1])) {
               throw new WrongUsageException("commands.title.usage.times", new Object[0]);
            }
         }

         EntityPlayerMP var3 = getPlayer(var1, var2[0]);
         S45PacketTitle.Type var4 = S45PacketTitle.Type.byName(var2[1]);
         if (var4 != S45PacketTitle.Type.CLEAR && var4 != S45PacketTitle.Type.RESET) {
            if (var4 == S45PacketTitle.Type.TIMES) {
               if (var2.length != 5) {
                  throw new WrongUsageException("commands.title.usage", new Object[0]);
               }

               int var10 = parseInt(var2[2]);
               int var6 = parseInt(var2[3]);
               int var7 = parseInt(var2[4]);
               S45PacketTitle var8 = new S45PacketTitle(var10, var6, var7);
               var3.playerNetServerHandler.sendPacket(var8);
               notifyOperators(var1, this, "commands.title.success", new Object[0]);
            } else {
               if (var2.length < 3) {
                  throw new WrongUsageException("commands.title.usage", new Object[0]);
               }

               String var11 = buildString(var2, 2);

               IChatComponent var12;
               try {
                  var12 = IChatComponent.Serializer.jsonToComponent(var11);
               } catch (JsonParseException var9) {
                  Throwable var14 = ExceptionUtils.getRootCause(var9);
                  throw new SyntaxErrorException("commands.tellraw.jsonException", new Object[]{var14 == null ? "" : var14.getMessage()});
               }

               S45PacketTitle var13 = new S45PacketTitle(var4, ChatComponentProcessor.processComponent(var1, var12, var3));
               var3.playerNetServerHandler.sendPacket(var13);
               notifyOperators(var1, this, "commands.title.success", new Object[0]);
            }
         } else {
            if (var2.length != 2) {
               throw new WrongUsageException("commands.title.usage", new Object[0]);
            }

            S45PacketTitle var5 = new S45PacketTitle(var4, (IChatComponent)null);
            var3.playerNetServerHandler.sendPacket(var5);
            notifyOperators(var1, this, "commands.title.success", new Object[0]);
         }

      }
   }

   public String getCommandName() {
      return "title";
   }

   public int getRequiredPermissionLevel() {
      return 2;
   }

   public List addTabCompletionOptions(ICommandSender var1, String[] var2, BlockPos var3) {
      return var2.length == 1 ? getListOfStringsMatchingLastWord(var2, MinecraftServer.getServer().getAllUsernames()) : (var2.length == 2 ? getListOfStringsMatchingLastWord(var2, S45PacketTitle.Type.getNames()) : null);
   }
}
