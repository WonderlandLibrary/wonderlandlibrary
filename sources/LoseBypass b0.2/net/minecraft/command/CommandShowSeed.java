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
    public boolean canCommandSenderUseCommand(ICommandSender sender) {
        if (MinecraftServer.getServer().isSinglePlayer()) return true;
        if (super.canCommandSenderUseCommand(sender)) return true;
        return false;
    }

    @Override
    public String getCommandName() {
        return "seed";
    }

    @Override
    public int getRequiredPermissionLevel() {
        return 2;
    }

    @Override
    public String getCommandUsage(ICommandSender sender) {
        return "commands.seed.usage";
    }

    @Override
    public void processCommand(ICommandSender sender, String[] args) throws CommandException {
        World world = sender instanceof EntityPlayer ? ((EntityPlayer)sender).worldObj : MinecraftServer.getServer().worldServerForDimension(0);
        sender.addChatMessage(new ChatComponentTranslation("commands.seed.success", world.getSeed()));
    }
}

