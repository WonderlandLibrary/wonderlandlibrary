// 
// Decompiled by Procyon v0.5.36
// 

package net.minecraft.command;

import net.minecraft.server.MinecraftServer;
import javax.annotation.Nullable;
import net.minecraft.entity.Entity;
import net.minecraft.world.World;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.util.text.ITextComponent;

public interface ICommandSender
{
    String getName();
    
    default ITextComponent getDisplayName() {
        return new TextComponentString(this.getName());
    }
    
    default void sendMessage(final ITextComponent component) {
    }
    
    boolean canUseCommand(final int p0, final String p1);
    
    default BlockPos getPosition() {
        return BlockPos.ORIGIN;
    }
    
    default Vec3d getPositionVector() {
        return Vec3d.ZERO;
    }
    
    World getEntityWorld();
    
    @Nullable
    default Entity getCommandSenderEntity() {
        return null;
    }
    
    default boolean sendCommandFeedback() {
        return false;
    }
    
    default void setCommandStat(final CommandResultStats.Type type, final int amount) {
    }
    
    @Nullable
    MinecraftServer getServer();
}
