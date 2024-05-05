package net.minecraft.command;

import net.minecraft.command.CommandResultStats;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.Entity;
import net.minecraft.util.BlockPos;
import net.minecraft.util.IChatComponent;
import net.minecraft.util.Vec3;
import net.minecraft.world.World;

class CommandResultStats.1
implements ICommandSender {
    final /* synthetic */ ICommandSender val$sender;

    CommandResultStats.1(ICommandSender iCommandSender) {
        this.val$sender = iCommandSender;
    }

    public String getName() {
        return this.val$sender.getName();
    }

    public IChatComponent getDisplayName() {
        return this.val$sender.getDisplayName();
    }

    public void addChatMessage(IChatComponent component) {
        this.val$sender.addChatMessage(component);
    }

    public boolean canCommandSenderUseCommand(int permLevel, String commandName) {
        return true;
    }

    public BlockPos getPosition() {
        return this.val$sender.getPosition();
    }

    public Vec3 getPositionVector() {
        return this.val$sender.getPositionVector();
    }

    public World getEntityWorld() {
        return this.val$sender.getEntityWorld();
    }

    public Entity getCommandSenderEntity() {
        return this.val$sender.getCommandSenderEntity();
    }

    public boolean sendCommandFeedback() {
        return this.val$sender.sendCommandFeedback();
    }

    public void setCommandStat(CommandResultStats.Type type, int amount) {
        this.val$sender.setCommandStat(type, amount);
    }
}
