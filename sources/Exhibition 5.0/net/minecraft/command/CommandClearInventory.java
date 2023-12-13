// 
// Decompiled by Procyon v0.5.30
// 

package net.minecraft.command;

import net.minecraft.server.MinecraftServer;
import java.util.Collection;
import java.util.List;
import net.minecraft.util.BlockPos;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.item.Item;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.util.IChatComponent;
import net.minecraft.util.ChatComponentTranslation;
import net.minecraft.nbt.NBTException;
import net.minecraft.nbt.JsonToNBT;

public class CommandClearInventory extends CommandBase
{
    private static final String __OBFID = "CL_00000218";
    
    @Override
    public String getCommandName() {
        return "clear";
    }
    
    @Override
    public String getCommandUsage(final ICommandSender sender) {
        return "commands.clear.usage";
    }
    
    @Override
    public int getRequiredPermissionLevel() {
        return 2;
    }
    
    @Override
    public void processCommand(final ICommandSender sender, final String[] args) throws CommandException {
        final EntityPlayerMP var3 = (args.length == 0) ? CommandBase.getCommandSenderAsPlayer(sender) : CommandBase.getPlayer(sender, args[0]);
        final Item var4 = (args.length >= 2) ? CommandBase.getItemByText(sender, args[1]) : null;
        final int var5 = (args.length >= 3) ? CommandBase.parseInt(args[2], -1) : -1;
        final int var6 = (args.length >= 4) ? CommandBase.parseInt(args[3], -1) : -1;
        NBTTagCompound var7 = null;
        if (args.length >= 5) {
            try {
                var7 = JsonToNBT.func_180713_a(CommandBase.func_180529_a(args, 4));
            }
            catch (NBTException var8) {
                throw new CommandException("commands.clear.tagError", new Object[] { var8.getMessage() });
            }
        }
        if (args.length >= 2 && var4 == null) {
            throw new CommandException("commands.clear.failure", new Object[] { var3.getName() });
        }
        final int var9 = var3.inventory.func_174925_a(var4, var5, var6, var7);
        var3.inventoryContainer.detectAndSendChanges();
        if (!var3.capabilities.isCreativeMode) {
            var3.updateHeldItem();
        }
        sender.func_174794_a(CommandResultStats.Type.AFFECTED_ITEMS, var9);
        if (var9 == 0) {
            throw new CommandException("commands.clear.failure", new Object[] { var3.getName() });
        }
        if (var6 == 0) {
            sender.addChatMessage(new ChatComponentTranslation("commands.clear.testing", new Object[] { var3.getName(), var9 }));
        }
        else {
            CommandBase.notifyOperators(sender, this, "commands.clear.success", var3.getName(), var9);
        }
    }
    
    @Override
    public List addTabCompletionOptions(final ICommandSender sender, final String[] args, final BlockPos pos) {
        return (args.length == 1) ? CommandBase.getListOfStringsMatchingLastWord(args, this.func_147209_d()) : ((args.length == 2) ? CommandBase.func_175762_a(args, Item.itemRegistry.getKeys()) : null);
    }
    
    protected String[] func_147209_d() {
        return MinecraftServer.getServer().getAllUsernames();
    }
    
    @Override
    public boolean isUsernameIndex(final String[] args, final int index) {
        return index == 0;
    }
}
