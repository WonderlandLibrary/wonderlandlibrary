/*
 * Decompiled with CFR 0.150.
 */
package net.minecraft.command;

import java.util.UUID;
import net.minecraft.command.CommandBase;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommand;
import net.minecraft.command.ICommandSender;
import net.minecraft.command.WrongUsageException;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.JsonToNBT;
import net.minecraft.nbt.NBTException;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.server.MinecraftServer;

public class CommandEntityData
extends CommandBase {
    @Override
    public String getCommandName() {
        return "entitydata";
    }

    @Override
    public int getRequiredPermissionLevel() {
        return 2;
    }

    @Override
    public String getCommandUsage(ICommandSender sender) {
        return "commands.entitydata.usage";
    }

    @Override
    public void execute(MinecraftServer server, ICommandSender sender, String[] args) throws CommandException {
        NBTTagCompound nbttagcompound2;
        if (args.length < 2) {
            throw new WrongUsageException("commands.entitydata.usage", new Object[0]);
        }
        Entity entity = CommandEntityData.getEntity(server, sender, args[0]);
        if (entity instanceof EntityPlayer) {
            throw new CommandException("commands.entitydata.noPlayers", entity.getDisplayName());
        }
        NBTTagCompound nbttagcompound = CommandEntityData.entityToNBT(entity);
        NBTTagCompound nbttagcompound1 = nbttagcompound.copy();
        try {
            nbttagcompound2 = JsonToNBT.getTagFromJson(CommandEntityData.buildString(args, 1));
        }
        catch (NBTException nbtexception) {
            throw new CommandException("commands.entitydata.tagError", nbtexception.getMessage());
        }
        UUID uuid = entity.getUniqueID();
        nbttagcompound.merge(nbttagcompound2);
        entity.setUniqueId(uuid);
        if (nbttagcompound.equals(nbttagcompound1)) {
            throw new CommandException("commands.entitydata.failed", nbttagcompound.toString());
        }
        entity.readFromNBT(nbttagcompound);
        CommandEntityData.notifyCommandListener(sender, (ICommand)this, "commands.entitydata.success", nbttagcompound.toString());
    }

    @Override
    public boolean isUsernameIndex(String[] args, int index) {
        return index == 0;
    }
}

