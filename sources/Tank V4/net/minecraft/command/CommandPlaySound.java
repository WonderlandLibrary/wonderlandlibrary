package net.minecraft.command;

import java.util.List;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.network.play.server.S29PacketSoundEffect;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.BlockPos;
import net.minecraft.util.Vec3;

public class CommandPlaySound extends CommandBase {
   public String getCommandName() {
      return "playsound";
   }

   public boolean isUsernameIndex(String[] var1, int var2) {
      return var2 == 1;
   }

   public int getRequiredPermissionLevel() {
      return 2;
   }

   public List addTabCompletionOptions(ICommandSender var1, String[] var2, BlockPos var3) {
      return var2.length == 2 ? getListOfStringsMatchingLastWord(var2, MinecraftServer.getServer().getAllUsernames()) : (var2.length > 2 && var2.length <= 5 ? func_175771_a(var2, 2, var3) : null);
   }

   public void processCommand(ICommandSender var1, String[] var2) throws CommandException {
      if (var2.length < 2) {
         throw new WrongUsageException(this.getCommandUsage(var1), new Object[0]);
      } else {
         byte var3 = 0;
         int var31 = var3 + 1;
         String var4 = var2[var3];
         EntityPlayerMP var5 = getPlayer(var1, var2[var31++]);
         Vec3 var6 = var1.getPositionVector();
         double var7 = var6.xCoord;
         if (var2.length > var31) {
            var7 = parseDouble(var7, var2[var31++], true);
         }

         double var9 = var6.yCoord;
         if (var2.length > var31) {
            var9 = parseDouble(var9, var2[var31++], 0, 0, false);
         }

         double var11 = var6.zCoord;
         if (var2.length > var31) {
            var11 = parseDouble(var11, var2[var31++], true);
         }

         double var13 = 1.0D;
         if (var2.length > var31) {
            var13 = parseDouble(var2[var31++], 0.0D, 3.4028234663852886E38D);
         }

         double var15 = 1.0D;
         if (var2.length > var31) {
            var15 = parseDouble(var2[var31++], 0.0D, 2.0D);
         }

         double var17 = 0.0D;
         if (var2.length > var31) {
            var17 = parseDouble(var2[var31], 0.0D, 1.0D);
         }

         double var19 = var13 > 1.0D ? var13 * 16.0D : 16.0D;
         double var21 = var5.getDistance(var7, var9, var11);
         if (var21 > var19) {
            if (var17 <= 0.0D) {
               throw new CommandException("commands.playsound.playerTooFar", new Object[]{var5.getName()});
            }

            double var23 = var7 - var5.posX;
            double var25 = var9 - var5.posY;
            double var27 = var11 - var5.posZ;
            double var29 = Math.sqrt(var23 * var23 + var25 * var25 + var27 * var27);
            if (var29 > 0.0D) {
               var7 = var5.posX + var23 / var29 * 2.0D;
               var9 = var5.posY + var25 / var29 * 2.0D;
               var11 = var5.posZ + var27 / var29 * 2.0D;
            }

            var13 = var17;
         }

         var5.playerNetServerHandler.sendPacket(new S29PacketSoundEffect(var4, var7, var9, var11, (float)var13, (float)var15));
         notifyOperators(var1, this, "commands.playsound.success", new Object[]{var4, var5.getName()});
      }
   }

   public String getCommandUsage(ICommandSender var1) {
      return "commands.playsound.usage";
   }
}
