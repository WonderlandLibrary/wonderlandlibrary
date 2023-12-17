// 
// Decompiled by Procyon v0.5.36
// 

package net.minecraft.command;

import java.util.Collections;
import java.util.Collection;
import java.util.List;
import javax.annotation.Nullable;
import net.minecraft.util.math.BlockPos;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.item.Item;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraft.nbt.NBTException;
import net.minecraft.nbt.JsonToNBT;
import net.minecraft.server.MinecraftServer;

public class CommandClearInventory extends CommandBase
{
    @Override
    public String getName() {
        return "clear";
    }
    
    @Override
    public String getUsage(final ICommandSender sender) {
        return "commands.clear.usage";
    }
    
    @Override
    public int getRequiredPermissionLevel() {
        return 2;
    }
    
    @Override
    public void execute(final MinecraftServer server, final ICommandSender sender, final String[] args) throws CommandException {
        final EntityPlayerMP entityplayermp = (args.length == 0) ? CommandBase.getCommandSenderAsPlayer(sender) : CommandBase.getPlayer(server, sender, args[0]);
        final Item item = (args.length >= 2) ? CommandBase.getItemByText(sender, args[1]) : null;
        final int i = (args.length >= 3) ? CommandBase.parseInt(args[2], -1) : -1;
        final int j = (args.length >= 4) ? CommandBase.parseInt(args[3], -1) : -1;
        NBTTagCompound nbttagcompound = null;
        if (args.length >= 5) {
            try {
                nbttagcompound = JsonToNBT.getTagFromJson(CommandBase.buildString(args, 4));
            }
            catch (NBTException nbtexception) {
                throw new CommandException("commands.clear.tagError", new Object[] { nbtexception.getMessage() });
            }
        }
        if (args.length >= 2 && item == null) {
            throw new CommandException("commands.clear.failure", new Object[] { entityplayermp.getName() });
        }
        final int k = entityplayermp.inventory.clearMatchingItems(item, i, j, nbttagcompound);
        entityplayermp.inventoryContainer.detectAndSendChanges();
        if (!entityplayermp.capabilities.isCreativeMode) {
            entityplayermp.updateHeldItem();
        }
        sender.setCommandStat(CommandResultStats.Type.AFFECTED_ITEMS, k);
        if (k == 0) {
            throw new CommandException("commands.clear.failure", new Object[] { entityplayermp.getName() });
        }
        if (j == 0) {
            sender.sendMessage(new TextComponentTranslation("commands.clear.testing", new Object[] { entityplayermp.getName(), k }));
        }
        else {
            CommandBase.notifyCommandListener(sender, this, "commands.clear.success", entityplayermp.getName(), k);
        }
    }
    
    @Override
    public List<String> getTabCompletions(final MinecraftServer server, final ICommandSender sender, final String[] args, @Nullable final BlockPos targetPos) {
        if (args.length == 1) {
            return CommandBase.getListOfStringsMatchingLastWord(args, server.getOnlinePlayerNames());
        }
        return (args.length == 2) ? CommandBase.getListOfStringsMatchingLastWord(args, Item.REGISTRY.getKeys()) : Collections.emptyList();
    }
    
    @Override
    public boolean isUsernameIndex(final String[] args, final int index) {
        return index == 0;
    }
}
