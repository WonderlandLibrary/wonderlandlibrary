// 
// Decompiled by Procyon v0.5.36
// 

package net.minecraft.command.server;

import java.util.Collections;
import java.util.List;
import javax.annotation.Nullable;
import net.minecraft.util.math.BlockPos;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.entity.Entity;
import net.minecraft.command.ICommand;
import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTUtil;
import net.minecraft.nbt.NBTException;
import net.minecraft.command.CommandException;
import net.minecraft.nbt.JsonToNBT;
import net.minecraft.command.WrongUsageException;
import net.minecraft.server.MinecraftServer;
import net.minecraft.command.ICommandSender;
import net.minecraft.command.CommandBase;

public class CommandTestFor extends CommandBase
{
    @Override
    public String getName() {
        return "testfor";
    }
    
    @Override
    public int getRequiredPermissionLevel() {
        return 2;
    }
    
    @Override
    public String getUsage(final ICommandSender sender) {
        return "commands.testfor.usage";
    }
    
    @Override
    public void execute(final MinecraftServer server, final ICommandSender sender, final String[] args) throws CommandException {
        if (args.length < 1) {
            throw new WrongUsageException("commands.testfor.usage", new Object[0]);
        }
        final Entity entity = CommandBase.getEntity(server, sender, args[0]);
        NBTTagCompound nbttagcompound = null;
        if (args.length >= 2) {
            try {
                nbttagcompound = JsonToNBT.getTagFromJson(CommandBase.buildString(args, 1));
            }
            catch (NBTException nbtexception) {
                throw new CommandException("commands.testfor.tagError", new Object[] { nbtexception.getMessage() });
            }
        }
        if (nbttagcompound != null) {
            final NBTTagCompound nbttagcompound2 = CommandBase.entityToNBT(entity);
            if (!NBTUtil.areNBTEquals(nbttagcompound, nbttagcompound2, true)) {
                throw new CommandException("commands.testfor.failure", new Object[] { entity.getName() });
            }
        }
        CommandBase.notifyCommandListener(sender, this, "commands.testfor.success", entity.getName());
    }
    
    @Override
    public boolean isUsernameIndex(final String[] args, final int index) {
        return index == 0;
    }
    
    @Override
    public List<String> getTabCompletions(final MinecraftServer server, final ICommandSender sender, final String[] args, @Nullable final BlockPos targetPos) {
        return (args.length == 1) ? CommandBase.getListOfStringsMatchingLastWord(args, server.getOnlinePlayerNames()) : Collections.emptyList();
    }
}
