// 
// Decompiled by Procyon v0.5.30
// 

package net.minecraft.command;

import net.minecraft.server.MinecraftServer;
import java.util.List;
import net.minecraft.util.BlockPos;
import net.minecraft.entity.Entity;
import net.minecraft.nbt.NBTException;
import net.minecraft.nbt.JsonToNBT;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.entity.player.EntityPlayer;

public class CommandEntityData extends CommandBase
{
    private static final String __OBFID = "CL_00002345";
    
    @Override
    public String getCommandName() {
        return "entitydata";
    }
    
    @Override
    public int getRequiredPermissionLevel() {
        return 2;
    }
    
    @Override
    public String getCommandUsage(final ICommandSender sender) {
        return "commands.entitydata.usage";
    }
    
    @Override
    public void processCommand(final ICommandSender sender, final String[] args) throws CommandException {
        if (args.length < 2) {
            throw new WrongUsageException("commands.entitydata.usage", new Object[0]);
        }
        final Entity var3 = CommandBase.func_175768_b(sender, args[0]);
        if (var3 instanceof EntityPlayer) {
            throw new CommandException("commands.entitydata.noPlayers", new Object[] { var3.getDisplayName() });
        }
        final NBTTagCompound var4 = new NBTTagCompound();
        var3.writeToNBT(var4);
        final NBTTagCompound var5 = (NBTTagCompound)var4.copy();
        NBTTagCompound var6;
        try {
            var6 = JsonToNBT.func_180713_a(CommandBase.getChatComponentFromNthArg(sender, args, 1).getUnformattedText());
        }
        catch (NBTException var7) {
            throw new CommandException("commands.entitydata.tagError", new Object[] { var7.getMessage() });
        }
        var6.removeTag("UUIDMost");
        var6.removeTag("UUIDLeast");
        var4.merge(var6);
        if (var4.equals(var5)) {
            throw new CommandException("commands.entitydata.failed", new Object[] { var4.toString() });
        }
        var3.readFromNBT(var4);
        CommandBase.notifyOperators(sender, this, "commands.entitydata.success", var4.toString());
    }
    
    @Override
    public List addTabCompletionOptions(final ICommandSender sender, final String[] args, final BlockPos pos) {
        return (args.length == 1) ? CommandBase.getListOfStringsMatchingLastWord(args, MinecraftServer.getServer().getAllUsernames()) : null;
    }
    
    @Override
    public boolean isUsernameIndex(final String[] args, final int index) {
        return index == 0;
    }
}
