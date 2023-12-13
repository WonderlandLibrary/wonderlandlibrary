package net.minecraft.network.rcon;

import net.minecraft.command.CommandResultStats;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.Entity;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.BlockPos;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.IChatComponent;
import net.minecraft.util.Vec3;
import net.minecraft.world.World;

public class RConConsoleSource implements ICommandSender {
   private StringBuffer buffer = new StringBuffer();
   private static final RConConsoleSource instance = new RConConsoleSource();

   public IChatComponent getDisplayName() {
      return new ChatComponentText(this.getName());
   }

   public void setCommandStat(CommandResultStats.Type var1, int var2) {
   }

   public boolean canCommandSenderUseCommand(int var1, String var2) {
      return true;
   }

   public Vec3 getPositionVector() {
      return new Vec3(0.0D, 0.0D, 0.0D);
   }

   public String getName() {
      return "Rcon";
   }

   public Entity getCommandSenderEntity() {
      return null;
   }

   public World getEntityWorld() {
      return MinecraftServer.getServer().getEntityWorld();
   }

   public void addChatMessage(IChatComponent var1) {
      this.buffer.append(var1.getUnformattedText());
   }

   public BlockPos getPosition() {
      return new BlockPos(0, 0, 0);
   }

   public boolean sendCommandFeedback() {
      return true;
   }
}
