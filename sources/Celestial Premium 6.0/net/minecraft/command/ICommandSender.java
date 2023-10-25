/*
 * Decompiled with CFR 0.150.
 */
package net.minecraft.command;

import javax.annotation.Nullable;
import net.minecraft.command.CommandResultStats;
import net.minecraft.entity.Entity;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.world.World;

public interface ICommandSender {
    public String getName();

    default public ITextComponent getDisplayName() {
        return new TextComponentString(this.getName());
    }

    default public void addChatMessage(ITextComponent component) {
    }

    public boolean canCommandSenderUseCommand(int var1, String var2);

    default public BlockPos getPosition() {
        return BlockPos.ORIGIN;
    }

    default public Vec3d getPositionVector() {
        return Vec3d.ZERO;
    }

    public World getEntityWorld();

    @Nullable
    default public Entity getCommandSenderEntity() {
        return null;
    }

    default public boolean sendCommandFeedback() {
        return false;
    }

    default public void setCommandStat(CommandResultStats.Type type, int amount) {
    }

    @Nullable
    public MinecraftServer getServer();
}

