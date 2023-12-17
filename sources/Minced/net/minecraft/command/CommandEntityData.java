// 
// Decompiled by Procyon v0.5.36
// 

package net.minecraft.command;

import java.util.UUID;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.entity.Entity;
import net.minecraft.nbt.NBTException;
import net.minecraft.nbt.JsonToNBT;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.server.MinecraftServer;

public class CommandEntityData extends CommandBase
{
    @Override
    public String getName() {
        return "entitydata";
    }
    
    @Override
    public int getRequiredPermissionLevel() {
        return 2;
    }
    
    @Override
    public String getUsage(final ICommandSender sender) {
        return "commands.entitydata.usage";
    }
    
    @Override
    public void execute(final MinecraftServer server, final ICommandSender sender, final String[] args) throws CommandException {
        if (args.length < 2) {
            throw new WrongUsageException("commands.entitydata.usage", new Object[0]);
        }
        final Entity entity = CommandBase.getEntity(server, sender, args[0]);
        if (entity instanceof EntityPlayer) {
            throw new CommandException("commands.entitydata.noPlayers", new Object[] { entity.getDisplayName() });
        }
        final NBTTagCompound nbttagcompound = CommandBase.entityToNBT(entity);
        final NBTTagCompound nbttagcompound2 = nbttagcompound.copy();
        NBTTagCompound nbttagcompound3;
        try {
            nbttagcompound3 = JsonToNBT.getTagFromJson(CommandBase.buildString(args, 1));
        }
        catch (NBTException nbtexception) {
            throw new CommandException("commands.entitydata.tagError", new Object[] { nbtexception.getMessage() });
        }
        final UUID uuid = entity.getUniqueID();
        nbttagcompound.merge(nbttagcompound3);
        entity.setUniqueId(uuid);
        if (nbttagcompound.equals(nbttagcompound2)) {
            throw new CommandException("commands.entitydata.failed", new Object[] { nbttagcompound.toString() });
        }
        entity.readFromNBT(nbttagcompound);
        CommandBase.notifyCommandListener(sender, this, "commands.entitydata.success", nbttagcompound.toString());
    }
    
    @Override
    public boolean isUsernameIndex(final String[] args, final int index) {
        return index == 0;
    }
}
