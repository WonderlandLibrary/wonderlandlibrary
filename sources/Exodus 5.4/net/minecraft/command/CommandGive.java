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
import net.minecraft.command.WrongUsageException;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.JsonToNBT;
import net.minecraft.nbt.NBTException;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.BlockPos;

public class CommandGive
extends CommandBase {
    @Override
    public boolean isUsernameIndex(String[] stringArray, int n) {
        return n == 0;
    }

    @Override
    public void processCommand(ICommandSender iCommandSender, String[] stringArray) throws CommandException {
        boolean bl;
        if (stringArray.length < 2) {
            throw new WrongUsageException("commands.give.usage", new Object[0]);
        }
        EntityPlayerMP entityPlayerMP = CommandGive.getPlayer(iCommandSender, stringArray[0]);
        Item item = CommandGive.getItemByText(iCommandSender, stringArray[1]);
        int n = stringArray.length >= 3 ? CommandGive.parseInt(stringArray[2], 1, 64) : 1;
        int n2 = stringArray.length >= 4 ? CommandGive.parseInt(stringArray[3]) : 0;
        ItemStack itemStack = new ItemStack(item, n, n2);
        if (stringArray.length >= 5) {
            String string = CommandGive.getChatComponentFromNthArg(iCommandSender, stringArray, 4).getUnformattedText();
            try {
                itemStack.setTagCompound(JsonToNBT.getTagFromJson(string));
            }
            catch (NBTException nBTException) {
                throw new CommandException("commands.give.tagError", nBTException.getMessage());
            }
        }
        if (bl = entityPlayerMP.inventory.addItemStackToInventory(itemStack)) {
            entityPlayerMP.worldObj.playSoundAtEntity(entityPlayerMP, "random.pop", 0.2f, ((entityPlayerMP.getRNG().nextFloat() - entityPlayerMP.getRNG().nextFloat()) * 0.7f + 1.0f) * 2.0f);
            entityPlayerMP.inventoryContainer.detectAndSendChanges();
        }
        if (bl && itemStack.stackSize <= 0) {
            itemStack.stackSize = 1;
            iCommandSender.setCommandStat(CommandResultStats.Type.AFFECTED_ITEMS, n);
            EntityItem entityItem = entityPlayerMP.dropPlayerItemWithRandomChoice(itemStack, false);
            if (entityItem != null) {
                entityItem.func_174870_v();
            }
        } else {
            iCommandSender.setCommandStat(CommandResultStats.Type.AFFECTED_ITEMS, n - itemStack.stackSize);
            EntityItem entityItem = entityPlayerMP.dropPlayerItemWithRandomChoice(itemStack, false);
            if (entityItem != null) {
                entityItem.setNoPickupDelay();
                entityItem.setOwner(entityPlayerMP.getName());
            }
        }
        CommandGive.notifyOperators(iCommandSender, (ICommand)this, "commands.give.success", itemStack.getChatComponent(), n, entityPlayerMP.getName());
    }

    @Override
    public int getRequiredPermissionLevel() {
        return 2;
    }

    @Override
    public String getCommandName() {
        return "give";
    }

    @Override
    public String getCommandUsage(ICommandSender iCommandSender) {
        return "commands.give.usage";
    }

    @Override
    public List<String> addTabCompletionOptions(ICommandSender iCommandSender, String[] stringArray, BlockPos blockPos) {
        return stringArray.length == 1 ? CommandGive.getListOfStringsMatchingLastWord(stringArray, this.getPlayers()) : (stringArray.length == 2 ? CommandGive.getListOfStringsMatchingLastWord(stringArray, Item.itemRegistry.getKeys()) : null);
    }

    protected String[] getPlayers() {
        return MinecraftServer.getServer().getAllUsernames();
    }
}

