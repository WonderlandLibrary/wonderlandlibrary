package net.minecraft.command;

import net.minecraft.entity.Entity;
import net.minecraft.util.BlockPos;
import net.minecraft.util.IChatComponent;
import net.minecraft.util.Vec3;
import net.minecraft.world.World;

public interface ICommandSender {
  String getName();
  
  IChatComponent getDisplayName();
  
  void addChatMessage(IChatComponent paramIChatComponent);
  
  boolean canCommandSenderUseCommand(int paramInt, String paramString);
  
  BlockPos getPosition();
  
  Vec3 getPositionVector();
  
  World getEntityWorld();
  
  Entity getCommandSenderEntity();
  
  boolean sendCommandFeedback();
  
  void setCommandStat(CommandResultStats.Type paramType, int paramInt);
}


/* Location:              C:\Users\Joona\Downloads\Cupid.jar!\net\minecraft\command\ICommandSender.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */