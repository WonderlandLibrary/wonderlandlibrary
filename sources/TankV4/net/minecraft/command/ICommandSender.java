package net.minecraft.command;

import net.minecraft.entity.Entity;
import net.minecraft.util.BlockPos;
import net.minecraft.util.IChatComponent;
import net.minecraft.util.Vec3;
import net.minecraft.world.World;

public interface ICommandSender {
   boolean canCommandSenderUseCommand(int var1, String var2);

   IChatComponent getDisplayName();

   boolean sendCommandFeedback();

   World getEntityWorld();

   Entity getCommandSenderEntity();

   String getName();

   void addChatMessage(IChatComponent var1);

   BlockPos getPosition();

   void setCommandStat(CommandResultStats.Type var1, int var2);

   Vec3 getPositionVector();
}
