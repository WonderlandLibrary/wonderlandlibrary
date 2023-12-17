// 
// Decompiled by Procyon v0.5.36
// 

package net.minecraft.command;

import net.minecraft.server.MinecraftServer;
import net.minecraft.world.World;
import net.minecraft.util.text.ITextComponent;
import java.util.Objects;
import net.minecraft.entity.Entity;
import net.minecraft.util.math.BlockPos;
import javax.annotation.Nullable;
import net.minecraft.util.math.Vec3d;

public class CommandSenderWrapper implements ICommandSender
{
    private final ICommandSender delegate;
    @Nullable
    private final Vec3d positionVector;
    @Nullable
    private final BlockPos position;
    @Nullable
    private final Integer permissionLevel;
    @Nullable
    private final Entity entity;
    @Nullable
    private final Boolean sendCommandFeedback;
    
    public CommandSenderWrapper(final ICommandSender delegateIn, @Nullable final Vec3d positionVectorIn, @Nullable final BlockPos positionIn, @Nullable final Integer permissionLevelIn, @Nullable final Entity entityIn, @Nullable final Boolean sendCommandFeedbackIn) {
        this.delegate = delegateIn;
        this.positionVector = positionVectorIn;
        this.position = positionIn;
        this.permissionLevel = permissionLevelIn;
        this.entity = entityIn;
        this.sendCommandFeedback = sendCommandFeedbackIn;
    }
    
    public static CommandSenderWrapper create(final ICommandSender sender) {
        return (CommandSenderWrapper)((sender instanceof CommandSenderWrapper) ? sender : new CommandSenderWrapper(sender, null, null, null, null, null));
    }
    
    public CommandSenderWrapper withEntity(final Entity entityIn, final Vec3d p_193997_2_) {
        return (this.entity == entityIn && Objects.equals(this.positionVector, p_193997_2_)) ? this : new CommandSenderWrapper(this.delegate, p_193997_2_, new BlockPos(p_193997_2_), this.permissionLevel, entityIn, this.sendCommandFeedback);
    }
    
    public CommandSenderWrapper withPermissionLevel(final int level) {
        return (this.permissionLevel != null && this.permissionLevel <= level) ? this : new CommandSenderWrapper(this.delegate, this.positionVector, this.position, level, this.entity, this.sendCommandFeedback);
    }
    
    public CommandSenderWrapper withSendCommandFeedback(final boolean sendCommandFeedbackIn) {
        return (this.sendCommandFeedback == null || (this.sendCommandFeedback && !sendCommandFeedbackIn)) ? new CommandSenderWrapper(this.delegate, this.positionVector, this.position, this.permissionLevel, this.entity, sendCommandFeedbackIn) : this;
    }
    
    public CommandSenderWrapper computePositionVector() {
        return (this.positionVector != null) ? this : new CommandSenderWrapper(this.delegate, this.getPositionVector(), this.getPosition(), this.permissionLevel, this.entity, this.sendCommandFeedback);
    }
    
    @Override
    public String getName() {
        return (this.entity != null) ? this.entity.getName() : this.delegate.getName();
    }
    
    @Override
    public ITextComponent getDisplayName() {
        return (this.entity != null) ? this.entity.getDisplayName() : this.delegate.getDisplayName();
    }
    
    @Override
    public void sendMessage(final ITextComponent component) {
        if (this.sendCommandFeedback == null || this.sendCommandFeedback) {
            this.delegate.sendMessage(component);
        }
    }
    
    @Override
    public boolean canUseCommand(final int permLevel, final String commandName) {
        return (this.permissionLevel == null || this.permissionLevel >= permLevel) && this.delegate.canUseCommand(permLevel, commandName);
    }
    
    @Override
    public BlockPos getPosition() {
        if (this.position != null) {
            return this.position;
        }
        return (this.entity != null) ? this.entity.getPosition() : this.delegate.getPosition();
    }
    
    @Override
    public Vec3d getPositionVector() {
        if (this.positionVector != null) {
            return this.positionVector;
        }
        return (this.entity != null) ? this.entity.getPositionVector() : this.delegate.getPositionVector();
    }
    
    @Override
    public World getEntityWorld() {
        return (this.entity != null) ? this.entity.getEntityWorld() : this.delegate.getEntityWorld();
    }
    
    @Nullable
    @Override
    public Entity getCommandSenderEntity() {
        return (this.entity != null) ? this.entity.getCommandSenderEntity() : this.delegate.getCommandSenderEntity();
    }
    
    @Override
    public boolean sendCommandFeedback() {
        return (this.sendCommandFeedback != null) ? this.sendCommandFeedback : this.delegate.sendCommandFeedback();
    }
    
    @Override
    public void setCommandStat(final CommandResultStats.Type type, final int amount) {
        if (this.entity != null) {
            this.entity.setCommandStat(type, amount);
        }
        else {
            this.delegate.setCommandStat(type, amount);
        }
    }
    
    @Nullable
    @Override
    public MinecraftServer getServer() {
        return this.delegate.getServer();
    }
}
