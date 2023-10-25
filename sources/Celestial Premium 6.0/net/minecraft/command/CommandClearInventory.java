/*
 * Decompiled with CFR 0.150.
 */
package net.minecraft.command;

import java.util.Collections;
import java.util.List;
import javax.annotation.Nullable;
import net.minecraft.command.CommandBase;
import net.minecraft.command.CommandException;
import net.minecraft.command.CommandResultStats;
import net.minecraft.command.ICommand;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.Item;
import net.minecraft.nbt.JsonToNBT;
import net.minecraft.nbt.NBTException;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextComponentTranslation;

public class CommandClearInventory
extends CommandBase {
    @Override
    public String getCommandName() {
        return "clear";
    }

    @Override
    public String getCommandUsage(ICommandSender sender) {
        return "commands.clear.usage";
    }

    @Override
    public int getRequiredPermissionLevel() {
        return 2;
    }

    @Override
    public void execute(MinecraftServer server, ICommandSender sender, String[] args) throws CommandException {
        EntityPlayerMP entityplayermp = args.length == 0 ? CommandClearInventory.getCommandSenderAsPlayer(sender) : CommandClearInventory.getPlayer(server, sender, args[0]);
        Item item = args.length >= 2 ? CommandClearInventory.getItemByText(sender, args[1]) : null;
        int i = args.length >= 3 ? CommandClearInventory.parseInt(args[2], -1) : -1;
        int j = args.length >= 4 ? CommandClearInventory.parseInt(args[3], -1) : -1;
        NBTTagCompound nbttagcompound = null;
        if (args.length >= 5) {
            try {
                nbttagcompound = JsonToNBT.getTagFromJson(CommandClearInventory.buildString(args, 4));
            }
            catch (NBTException nbtexception) {
                throw new CommandException("commands.clear.tagError", nbtexception.getMessage());
            }
        }
        if (args.length >= 2 && item == null) {
            throw new CommandException("commands.clear.failure", entityplayermp.getName());
        }
        int k = entityplayermp.inventory.clearMatchingItems(item, i, j, nbttagcompound);
        entityplayermp.inventoryContainer.detectAndSendChanges();
        if (!entityplayermp.capabilities.isCreativeMode) {
            entityplayermp.updateHeldItem();
        }
        sender.setCommandStat(CommandResultStats.Type.AFFECTED_ITEMS, k);
        if (k == 0) {
            throw new CommandException("commands.clear.failure", entityplayermp.getName());
        }
        if (j == 0) {
            sender.addChatMessage(new TextComponentTranslation("commands.clear.testing", entityplayermp.getName(), k));
        } else {
            CommandClearInventory.notifyCommandListener(sender, (ICommand)this, "commands.clear.success", entityplayermp.getName(), k);
        }
    }

    @Override
    public List<String> getTabCompletionOptions(MinecraftServer server, ICommandSender sender, String[] args, @Nullable BlockPos pos) {
        if (args.length == 1) {
            return CommandClearInventory.getListOfStringsMatchingLastWord(args, server.getAllUsernames());
        }
        return args.length == 2 ? CommandClearInventory.getListOfStringsMatchingLastWord(args, Item.REGISTRY.getKeys()) : Collections.emptyList();
    }

    @Override
    public boolean isUsernameIndex(String[] args, int index) {
        return index == 0;
    }
}

