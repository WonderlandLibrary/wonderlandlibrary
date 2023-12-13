package net.minecraft.command;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.ChatComponentTranslation;
import net.minecraft.world.World;

public class CommandShowSeed extends CommandBase {
   public int getRequiredPermissionLevel() {
      return 2;
   }

   public String getCommandName() {
      return "seed";
   }

   public boolean canCommandSenderUseCommand(ICommandSender var1) {
      return MinecraftServer.getServer().isSinglePlayer() || super.canCommandSenderUseCommand(var1);
   }

   public String getCommandUsage(ICommandSender var1) {
      return "commands.seed.usage";
   }

   public void processCommand(ICommandSender var1, String[] var2) throws CommandException {
      Object var3 = var1 instanceof EntityPlayer ? ((EntityPlayer)var1).worldObj : MinecraftServer.getServer().worldServerForDimension(0);
      var1.addChatMessage(new ChatComponentTranslation("commands.seed.success", new Object[]{((World)var3).getSeed()}));
   }
}
