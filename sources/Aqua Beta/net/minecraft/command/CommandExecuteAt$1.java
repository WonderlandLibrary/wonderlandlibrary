package net.minecraft.command;

import net.minecraft.command.CommandResultStats;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.Entity;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.BlockPos;
import net.minecraft.util.IChatComponent;
import net.minecraft.util.Vec3;
import net.minecraft.world.World;

class CommandExecuteAt.1
implements ICommandSender {
    final /* synthetic */ Entity val$entity;
    final /* synthetic */ ICommandSender val$sender;
    final /* synthetic */ BlockPos val$blockpos;
    final /* synthetic */ double val$d0;
    final /* synthetic */ double val$d1;
    final /* synthetic */ double val$d2;

    CommandExecuteAt.1(Entity entity, ICommandSender iCommandSender, BlockPos blockPos, double d, double d2, double d3) {
        this.val$entity = entity;
        this.val$sender = iCommandSender;
        this.val$blockpos = blockPos;
        this.val$d0 = d;
        this.val$d1 = d2;
        this.val$d2 = d3;
    }

    public String getName() {
        return this.val$entity.getName();
    }

    public IChatComponent getDisplayName() {
        return this.val$entity.getDisplayName();
    }

    public void addChatMessage(IChatComponent component) {
        this.val$sender.addChatMessage(component);
    }

    public boolean canCommandSenderUseCommand(int permLevel, String commandName) {
        return this.val$sender.canCommandSenderUseCommand(permLevel, commandName);
    }

    public BlockPos getPosition() {
        return this.val$blockpos;
    }

    public Vec3 getPositionVector() {
        return new Vec3(this.val$d0, this.val$d1, this.val$d2);
    }

    public World getEntityWorld() {
        return this.val$entity.worldObj;
    }

    public Entity getCommandSenderEntity() {
        return this.val$entity;
    }

    public boolean sendCommandFeedback() {
        MinecraftServer minecraftserver = MinecraftServer.getServer();
        return minecraftserver == null || minecraftserver.worldServers[0].getGameRules().getBoolean("commandBlockOutput");
    }

    public void setCommandStat(CommandResultStats.Type type, int amount) {
        this.val$entity.setCommandStat(type, amount);
    }
}
