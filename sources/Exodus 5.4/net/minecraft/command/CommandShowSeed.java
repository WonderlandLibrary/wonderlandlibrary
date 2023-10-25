/*
 * Decompiled with CFR 0.152.
 */
package net.minecraft.command;

import net.minecraft.command.CommandBase;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.ChatComponentTranslation;
import net.minecraft.world.World;

public class CommandShowSeed
extends CommandBase {
    @Override
    public boolean canCommandSenderUseCommand(ICommandSender iCommandSender) {
        return MinecraftServer.getServer().isSinglePlayer() || super.canCommandSenderUseCommand(iCommandSender);
    }

    @Override
    public String getCommandName() {
        return "seed";
    }

    @Override
    public void processCommand(ICommandSender iCommandSender, String[] stringArray) throws CommandException {
        World world = iCommandSender instanceof EntityPlayer ? ((EntityPlayer)iCommandSender).worldObj : MinecraftServer.getServer().worldServerForDimension(0);
        iCommandSender.addChatMessage(new ChatComponentTranslation("commands.seed.success", world.getSeed()));
    }

    @Override
    public String getCommandUsage(ICommandSender iCommandSender) {
        return "commands.seed.usage";
    }

    @Override
    public int getRequiredPermissionLevel() {
        return 2;
    }
}

