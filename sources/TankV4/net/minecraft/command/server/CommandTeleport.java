package net.minecraft.command.server;

import java.util.EnumSet;
import java.util.List;
import net.minecraft.command.CommandBase;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.command.WrongUsageException;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.network.play.server.S08PacketPlayerPosLook;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.BlockPos;
import net.minecraft.util.MathHelper;

public class CommandTeleport extends CommandBase {
   public String getCommandUsage(ICommandSender var1) {
      return "commands.tp.usage";
   }

   public int getRequiredPermissionLevel() {
      return 2;
   }

   public void processCommand(ICommandSender var1, String[] var2) throws CommandException {
      if (var2.length < 1) {
         throw new WrongUsageException("commands.tp.usage", new Object[0]);
      } else {
         byte var3 = 0;
         Object var4;
         if (var2.length != 2 && var2.length != 4 && var2.length != 6) {
            var4 = getCommandSenderAsPlayer(var1);
         } else {
            var4 = func_175768_b(var1, var2[0]);
            var3 = 1;
         }

         if (var2.length != 1 && var2.length != 2) {
            if (var2.length < var3 + 3) {
               throw new WrongUsageException("commands.tp.usage", new Object[0]);
            }

            if (((Entity)var4).worldObj != null) {
               int var14 = var3 + 1;
               CommandBase.CoordinateArg var6 = parseCoordinate(((Entity)var4).posX, var2[var3], true);
               CommandBase.CoordinateArg var7 = parseCoordinate(((Entity)var4).posY, var2[var14++], 0, 0, false);
               CommandBase.CoordinateArg var8 = parseCoordinate(((Entity)var4).posZ, var2[var14++], true);
               CommandBase.CoordinateArg var9 = parseCoordinate((double)((Entity)var4).rotationYaw, var2.length > var14 ? var2[var14++] : "~", false);
               CommandBase.CoordinateArg var10 = parseCoordinate((double)((Entity)var4).rotationPitch, var2.length > var14 ? var2[var14] : "~", false);
               float var12;
               if (var4 instanceof EntityPlayerMP) {
                  EnumSet var15 = EnumSet.noneOf(S08PacketPlayerPosLook.EnumFlags.class);
                  if (var6.func_179630_c()) {
                     var15.add(S08PacketPlayerPosLook.EnumFlags.X);
                  }

                  if (var7.func_179630_c()) {
                     var15.add(S08PacketPlayerPosLook.EnumFlags.Y);
                  }

                  if (var8.func_179630_c()) {
                     var15.add(S08PacketPlayerPosLook.EnumFlags.Z);
                  }

                  if (var10.func_179630_c()) {
                     var15.add(S08PacketPlayerPosLook.EnumFlags.X_ROT);
                  }

                  if (var9.func_179630_c()) {
                     var15.add(S08PacketPlayerPosLook.EnumFlags.Y_ROT);
                  }

                  var12 = (float)var9.func_179629_b();
                  if (!var9.func_179630_c()) {
                     var12 = MathHelper.wrapAngleTo180_float(var12);
                  }

                  float var13 = (float)var10.func_179629_b();
                  if (!var10.func_179630_c()) {
                     var13 = MathHelper.wrapAngleTo180_float(var13);
                  }

                  if (var13 > 90.0F || var13 < -90.0F) {
                     var13 = MathHelper.wrapAngleTo180_float(180.0F - var13);
                     var12 = MathHelper.wrapAngleTo180_float(var12 + 180.0F);
                  }

                  ((Entity)var4).mountEntity((Entity)null);
                  ((EntityPlayerMP)var4).playerNetServerHandler.setPlayerLocation(var6.func_179629_b(), var7.func_179629_b(), var8.func_179629_b(), var12, var13, var15);
                  ((Entity)var4).setRotationYawHead(var12);
               } else {
                  float var11 = (float)MathHelper.wrapAngleTo180_double(var9.func_179628_a());
                  var12 = (float)MathHelper.wrapAngleTo180_double(var10.func_179628_a());
                  if (var12 > 90.0F || var12 < -90.0F) {
                     var12 = MathHelper.wrapAngleTo180_float(180.0F - var12);
                     var11 = MathHelper.wrapAngleTo180_float(var11 + 180.0F);
                  }

                  ((Entity)var4).setLocationAndAngles(var6.func_179628_a(), var7.func_179628_a(), var8.func_179628_a(), var11, var12);
                  ((Entity)var4).setRotationYawHead(var11);
               }

               notifyOperators(var1, this, "commands.tp.success.coordinates", new Object[]{((Entity)var4).getName(), var6.func_179628_a(), var7.func_179628_a(), var8.func_179628_a()});
            }
         } else {
            Entity var5 = func_175768_b(var1, var2[var2.length - 1]);
            if (var5.worldObj != ((Entity)var4).worldObj) {
               throw new CommandException("commands.tp.notSameDimension", new Object[0]);
            }

            ((Entity)var4).mountEntity((Entity)null);
            if (var4 instanceof EntityPlayerMP) {
               ((EntityPlayerMP)var4).playerNetServerHandler.setPlayerLocation(var5.posX, var5.posY, var5.posZ, var5.rotationYaw, var5.rotationPitch);
            } else {
               ((Entity)var4).setLocationAndAngles(var5.posX, var5.posY, var5.posZ, var5.rotationYaw, var5.rotationPitch);
            }

            notifyOperators(var1, this, "commands.tp.success", new Object[]{((Entity)var4).getName(), var5.getName()});
         }

      }
   }

   public boolean isUsernameIndex(String[] var1, int var2) {
      return var2 == 0;
   }

   public String getCommandName() {
      return "tp";
   }

   public List addTabCompletionOptions(ICommandSender var1, String[] var2, BlockPos var3) {
      return var2.length != 1 && var2.length != 2 ? null : getListOfStringsMatchingLastWord(var2, MinecraftServer.getServer().getAllUsernames());
   }
}
