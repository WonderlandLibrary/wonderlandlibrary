package net.minecraft.command;

import net.minecraft.entity.Entity;
import net.minecraft.util.BlockPos;
import net.minecraft.util.IChatComponent;
import net.minecraft.util.Vec3;
import net.minecraft.world.World;

public abstract interface ICommandSender
{
  public abstract String getName();
  
  public abstract IChatComponent getDisplayName();
  
  public abstract void addChatMessage(IChatComponent paramIChatComponent);
  
  public abstract boolean canCommandSenderUseCommand(int paramInt, String paramString);
  
  public abstract BlockPos getPosition();
  
  public abstract Vec3 getPositionVector();
  
  public abstract World getEntityWorld();
  
  public abstract Entity getCommandSenderEntity();
  
  public abstract boolean sendCommandFeedback();
  
  public abstract void func_174794_a(CommandResultStats.Type paramType, int paramInt);
}
