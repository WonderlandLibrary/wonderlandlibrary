package net.minecraft.command;

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

public class CommandEntityData
extends CommandBase {
    public String getCommandName() {
        return "entitydata";
    }

    public int getRequiredPermissionLevel() {
        return 2;
    }

    public String getCommandUsage(ICommandSender sender) {
        return "commands.entitydata.usage";
    }

    public void processCommand(ICommandSender sender, String[] args) throws CommandException {
        NBTTagCompound nbttagcompound2;
        if (args.length < 2) {
            throw new WrongUsageException("commands.entitydata.usage", new Object[0]);
        }
        Entity entity = CommandEntityData.getEntity((ICommandSender)sender, (String)args[0]);
        if (entity instanceof EntityPlayer) {
            throw new CommandException("commands.entitydata.noPlayers", new Object[]{entity.getDisplayName()});
        }
        NBTTagCompound nbttagcompound = new NBTTagCompound();
        entity.writeToNBT(nbttagcompound);
        NBTTagCompound nbttagcompound1 = (NBTTagCompound)nbttagcompound.copy();
        try {
            nbttagcompound2 = JsonToNBT.getTagFromJson((String)CommandEntityData.getChatComponentFromNthArg((ICommandSender)sender, (String[])args, (int)1).getUnformattedText());
        }
        catch (NBTException nbtexception) {
            throw new CommandException("commands.entitydata.tagError", new Object[]{nbtexception.getMessage()});
        }
        nbttagcompound2.removeTag("UUIDMost");
        nbttagcompound2.removeTag("UUIDLeast");
        nbttagcompound.merge(nbttagcompound2);
        if (nbttagcompound.equals((Object)nbttagcompound1)) {
            throw new CommandException("commands.entitydata.failed", new Object[]{nbttagcompound.toString()});
        }
        entity.readFromNBT(nbttagcompound);
        CommandEntityData.notifyOperators((ICommandSender)sender, (ICommand)this, (String)"commands.entitydata.success", (Object[])new Object[]{nbttagcompound.toString()});
    }

    public boolean isUsernameIndex(String[] args, int index) {
        return index == 0;
    }
}
