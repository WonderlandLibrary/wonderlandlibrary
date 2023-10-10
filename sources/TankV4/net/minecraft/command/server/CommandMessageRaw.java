package net.minecraft.command.server;

import com.google.gson.JsonParseException;
import java.util.List;
import net.minecraft.command.CommandBase;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.command.SyntaxErrorException;
import net.minecraft.command.WrongUsageException;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.BlockPos;
import net.minecraft.util.ChatComponentProcessor;
import net.minecraft.util.IChatComponent;
import org.apache.commons.lang3.exception.ExceptionUtils;

public class CommandMessageRaw extends CommandBase {
   public boolean isUsernameIndex(String[] var1, int var2) {
      return var2 == 0;
   }

   public String getCommandName() {
      return "tellraw";
   }

   public List addTabCompletionOptions(ICommandSender var1, String[] var2, BlockPos var3) {
      return var2.length == 1 ? getListOfStringsMatchingLastWord(var2, MinecraftServer.getServer().getAllUsernames()) : null;
   }

   public void processCommand(ICommandSender var1, String[] var2) throws CommandException {
      if (var2.length < 2) {
         throw new WrongUsageException("commands.tellraw.usage", new Object[0]);
      } else {
         EntityPlayerMP var3 = getPlayer(var1, var2[0]);
         String var4 = buildString(var2, 1);

         try {
            IChatComponent var5 = IChatComponent.Serializer.jsonToComponent(var4);
            var3.addChatMessage(ChatComponentProcessor.processComponent(var1, var5, var3));
         } catch (JsonParseException var7) {
            Throwable var6 = ExceptionUtils.getRootCause(var7);
            throw new SyntaxErrorException("commands.tellraw.jsonException", new Object[]{var6 == null ? "" : var6.getMessage()});
         }
      }
   }

   public String getCommandUsage(ICommandSender var1) {
      return "commands.tellraw.usage";
   }

   public int getRequiredPermissionLevel() {
      return 2;
   }
}
