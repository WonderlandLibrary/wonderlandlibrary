/*
 * Decompiled with CFR 0.152.
 */
package net.minecraft.command;

import java.util.List;
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
import net.minecraft.util.BlockPos;
import net.minecraft.util.ChatComponentTranslation;

public class CommandClearInventory
extends CommandBase {
    @Override
    public String getCommandName() {
        return "clear";
    }

    protected String[] func_147209_d() {
        return MinecraftServer.getServer().getAllUsernames();
    }

    @Override
    public void processCommand(ICommandSender iCommandSender, String[] stringArray) throws CommandException {
        EntityPlayerMP entityPlayerMP = stringArray.length == 0 ? CommandClearInventory.getCommandSenderAsPlayer(iCommandSender) : CommandClearInventory.getPlayer(iCommandSender, stringArray[0]);
        Item item = stringArray.length >= 2 ? CommandClearInventory.getItemByText(iCommandSender, stringArray[1]) : null;
        int n = stringArray.length >= 3 ? CommandClearInventory.parseInt(stringArray[2], -1) : -1;
        int n2 = stringArray.length >= 4 ? CommandClearInventory.parseInt(stringArray[3], -1) : -1;
        NBTTagCompound nBTTagCompound = null;
        if (stringArray.length >= 5) {
            try {
                nBTTagCompound = JsonToNBT.getTagFromJson(CommandClearInventory.buildString(stringArray, 4));
            }
            catch (NBTException nBTException) {
                throw new CommandException("commands.clear.tagError", nBTException.getMessage());
            }
        }
        if (stringArray.length >= 2 && item == null) {
            throw new CommandException("commands.clear.failure", entityPlayerMP.getName());
        }
        int n3 = entityPlayerMP.inventory.clearMatchingItems(item, n, n2, nBTTagCompound);
        entityPlayerMP.inventoryContainer.detectAndSendChanges();
        if (!entityPlayerMP.capabilities.isCreativeMode) {
            entityPlayerMP.updateHeldItem();
        }
        iCommandSender.setCommandStat(CommandResultStats.Type.AFFECTED_ITEMS, n3);
        if (n3 == 0) {
            throw new CommandException("commands.clear.failure", entityPlayerMP.getName());
        }
        if (n2 == 0) {
            iCommandSender.addChatMessage(new ChatComponentTranslation("commands.clear.testing", entityPlayerMP.getName(), n3));
        } else {
            CommandClearInventory.notifyOperators(iCommandSender, (ICommand)this, "commands.clear.success", entityPlayerMP.getName(), n3);
        }
    }

    @Override
    public int getRequiredPermissionLevel() {
        return 2;
    }

    @Override
    public List<String> addTabCompletionOptions(ICommandSender iCommandSender, String[] stringArray, BlockPos blockPos) {
        return stringArray.length == 1 ? CommandClearInventory.getListOfStringsMatchingLastWord(stringArray, this.func_147209_d()) : (stringArray.length == 2 ? CommandClearInventory.getListOfStringsMatchingLastWord(stringArray, Item.itemRegistry.getKeys()) : null);
    }

    @Override
    public String getCommandUsage(ICommandSender iCommandSender) {
        return "commands.clear.usage";
    }

    @Override
    public boolean isUsernameIndex(String[] stringArray, int n) {
        return n == 0;
    }
}

