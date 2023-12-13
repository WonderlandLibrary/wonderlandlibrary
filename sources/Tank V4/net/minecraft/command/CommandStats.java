package net.minecraft.command;

import com.google.common.collect.Lists;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import net.minecraft.entity.Entity;
import net.minecraft.scoreboard.ScoreObjective;
import net.minecraft.server.MinecraftServer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityCommandBlock;
import net.minecraft.tileentity.TileEntitySign;
import net.minecraft.util.BlockPos;
import net.minecraft.world.World;

public class CommandStats extends CommandBase {
   public void processCommand(ICommandSender var1, String[] var2) throws CommandException {
      if (var2.length < 1) {
         throw new WrongUsageException("commands.stats.usage", new Object[0]);
      } else {
         boolean var3;
         if (var2[0].equals("entity")) {
            var3 = false;
         } else {
            if (!var2[0].equals("block")) {
               throw new WrongUsageException("commands.stats.usage", new Object[0]);
            }

            var3 = true;
         }

         byte var4;
         if (var3) {
            if (var2.length < 5) {
               throw new WrongUsageException("commands.stats.block.usage", new Object[0]);
            }

            var4 = 4;
         } else {
            if (var2.length < 3) {
               throw new WrongUsageException("commands.stats.entity.usage", new Object[0]);
            }

            var4 = 2;
         }

         int var11 = var4 + 1;
         String var5 = var2[var4];
         if ("set".equals(var5)) {
            if (var2.length < var11 + 3) {
               if (var11 == 5) {
                  throw new WrongUsageException("commands.stats.block.set.usage", new Object[0]);
               }

               throw new WrongUsageException("commands.stats.entity.set.usage", new Object[0]);
            }
         } else {
            if (!"clear".equals(var5)) {
               throw new WrongUsageException("commands.stats.usage", new Object[0]);
            }

            if (var2.length < var11 + 1) {
               if (var11 == 5) {
                  throw new WrongUsageException("commands.stats.block.clear.usage", new Object[0]);
               }

               throw new WrongUsageException("commands.stats.entity.clear.usage", new Object[0]);
            }
         }

         CommandResultStats.Type var6 = CommandResultStats.Type.getTypeByName(var2[var11++]);
         if (var6 == null) {
            throw new CommandException("commands.stats.failed", new Object[0]);
         } else {
            World var7 = var1.getEntityWorld();
            CommandResultStats var8;
            BlockPos var9;
            TileEntity var10;
            if (var3) {
               var9 = parseBlockPos(var1, var2, 1, false);
               var10 = var7.getTileEntity(var9);
               if (var10 == null) {
                  throw new CommandException("commands.stats.noCompatibleBlock", new Object[]{var9.getX(), var9.getY(), var9.getZ()});
               }

               if (var10 instanceof TileEntityCommandBlock) {
                  var8 = ((TileEntityCommandBlock)var10).getCommandResultStats();
               } else {
                  if (!(var10 instanceof TileEntitySign)) {
                     throw new CommandException("commands.stats.noCompatibleBlock", new Object[]{var9.getX(), var9.getY(), var9.getZ()});
                  }

                  var8 = ((TileEntitySign)var10).getStats();
               }
            } else {
               Entity var12 = func_175768_b(var1, var2[1]);
               var8 = var12.getCommandStats();
            }

            if ("set".equals(var5)) {
               String var13 = var2[var11++];
               String var14 = var2[var11];
               if (var13.length() == 0 || var14.length() == 0) {
                  throw new CommandException("commands.stats.failed", new Object[0]);
               }

               CommandResultStats.func_179667_a(var8, var6, var13, var14);
               notifyOperators(var1, this, "commands.stats.success", new Object[]{var6.getTypeName(), var14, var13});
            } else if ("clear".equals(var5)) {
               CommandResultStats.func_179667_a(var8, var6, (String)null, (String)null);
               notifyOperators(var1, this, "commands.stats.cleared", new Object[]{var6.getTypeName()});
            }

            if (var3) {
               var9 = parseBlockPos(var1, var2, 1, false);
               var10 = var7.getTileEntity(var9);
               var10.markDirty();
            }

         }
      }
   }

   public int getRequiredPermissionLevel() {
      return 2;
   }

   public boolean isUsernameIndex(String[] var1, int var2) {
      return var1.length > 0 && var1[0].equals("entity") && var2 == 1;
   }

   protected List func_175777_e() {
      Collection var1 = MinecraftServer.getServer().worldServerForDimension(0).getScoreboard().getScoreObjectives();
      ArrayList var2 = Lists.newArrayList();
      Iterator var4 = var1.iterator();

      while(var4.hasNext()) {
         ScoreObjective var3 = (ScoreObjective)var4.next();
         if (!var3.getCriteria().isReadOnly()) {
            var2.add(var3.getName());
         }
      }

      return var2;
   }

   public String getCommandName() {
      return "stats";
   }

   public String getCommandUsage(ICommandSender var1) {
      return "commands.stats.usage";
   }

   protected String[] func_175776_d() {
      return MinecraftServer.getServer().getAllUsernames();
   }

   public List addTabCompletionOptions(ICommandSender var1, String[] var2, BlockPos var3) {
      return var2.length == 1 ? getListOfStringsMatchingLastWord(var2, new String[]{"entity", "block"}) : (var2.length == 2 && var2[0].equals("entity") ? getListOfStringsMatchingLastWord(var2, this.func_175776_d()) : (var2.length >= 2 && var2.length <= 4 && var2[0].equals("block") ? func_175771_a(var2, 1, var3) : (var2.length == 3 && var2[0].equals("entity") || var2.length == 5 && var2[0].equals("block") ? getListOfStringsMatchingLastWord(var2, new String[]{"set", "clear"}) : (var2.length == 4 && var2[0].equals("entity") || var2.length == 6 && var2[0].equals("block") ? getListOfStringsMatchingLastWord(var2, CommandResultStats.Type.getTypeNames()) : (var2.length == 6 && var2[0].equals("entity") || var2.length == 8 && var2[0].equals("block") ? getListOfStringsMatchingLastWord(var2, this.func_175777_e()) : null)))));
   }
}
