package net.minecraft.command;

import java.util.List;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.Vec3;
import net.minecraft.world.World;
import net.minecraft.world.WorldServer;

public class CommandParticle extends CommandBase {
   public String getCommandName() {
      return "particle";
   }

   public List addTabCompletionOptions(ICommandSender var1, String[] var2, BlockPos var3) {
      return var2.length == 1 ? getListOfStringsMatchingLastWord(var2, EnumParticleTypes.getParticleNames()) : (var2.length > 1 && var2.length <= 4 ? func_175771_a(var2, 1, var3) : (var2.length == 10 ? getListOfStringsMatchingLastWord(var2, new String[]{"normal", "force"}) : null));
   }

   public void processCommand(ICommandSender var1, String[] var2) throws CommandException {
      if (var2.length < 8) {
         throw new WrongUsageException("commands.particle.usage", new Object[0]);
      } else {
         boolean var3 = false;
         EnumParticleTypes var4 = null;
         EnumParticleTypes[] var8;
         int var7 = (var8 = EnumParticleTypes.values()).length;

         for(int var6 = 0; var6 < var7; ++var6) {
            EnumParticleTypes var5 = var8[var6];
            if (var5.hasArguments()) {
               if (var2[0].startsWith(var5.getParticleName())) {
                  var3 = true;
                  var4 = var5;
                  break;
               }
            } else if (var2[0].equals(var5.getParticleName())) {
               var3 = true;
               var4 = var5;
               break;
            }
         }

         if (!var3) {
            throw new CommandException("commands.particle.notFound", new Object[]{var2[0]});
         } else {
            String var30 = var2[0];
            Vec3 var31 = var1.getPositionVector();
            double var32 = (double)((float)parseDouble(var31.xCoord, var2[1], true));
            double var9 = (double)((float)parseDouble(var31.yCoord, var2[2], true));
            double var11 = (double)((float)parseDouble(var31.zCoord, var2[3], true));
            double var13 = (double)((float)parseDouble(var2[4]));
            double var15 = (double)((float)parseDouble(var2[5]));
            double var17 = (double)((float)parseDouble(var2[6]));
            double var19 = (double)((float)parseDouble(var2[7]));
            int var21 = 0;
            if (var2.length > 8) {
               var21 = parseInt(var2[8], 0);
            }

            boolean var22 = false;
            if (var2.length > 9 && "force".equals(var2[9])) {
               var22 = true;
            }

            World var23 = var1.getEntityWorld();
            if (var23 instanceof WorldServer) {
               WorldServer var24 = (WorldServer)var23;
               int[] var25 = new int[var4.getArgumentCount()];
               if (var4.hasArguments()) {
                  String[] var26 = var2[0].split("_", 3);

                  for(int var27 = 1; var27 < var26.length; ++var27) {
                     try {
                        var25[var27 - 1] = Integer.parseInt(var26[var27]);
                     } catch (NumberFormatException var29) {
                        throw new CommandException("commands.particle.notFound", new Object[]{var2[0]});
                     }
                  }
               }

               var24.spawnParticle(var4, var22, var32, var9, var11, var21, var13, var15, var17, var19, var25);
               notifyOperators(var1, this, "commands.particle.success", new Object[]{var30, Math.max(var21, 1)});
            }

         }
      }
   }

   public int getRequiredPermissionLevel() {
      return 2;
   }

   public String getCommandUsage(ICommandSender var1) {
      return "commands.particle.usage";
   }
}
