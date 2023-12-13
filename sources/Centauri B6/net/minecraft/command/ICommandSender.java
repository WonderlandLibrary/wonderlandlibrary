package net.minecraft.command;

import net.minecraft.command.CommandResultStats;
import net.minecraft.entity.Entity;
import net.minecraft.util.BlockPos;
import net.minecraft.util.IChatComponent;
import net.minecraft.util.Vec3;
import net.minecraft.world.World;

public interface ICommandSender {
   String getName();

   IChatComponent getDisplayName();

   BlockPos getPosition();

   boolean sendCommandFeedback();

   void addChatMessage(IChatComponent var1);

   World getEntityWorld();

   void setCommandStat(CommandResultStats.Type var1, int var2);

   Vec3 getPositionVector();

   Entity getCommandSenderEntity();

   boolean canCommandSenderUseCommand(int var1, String var2);
}
