/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.google.common.collect.Maps
 */
package net.minecraft.command;

import com.google.common.collect.Maps;
import java.util.List;
import java.util.Map;
import net.minecraft.block.Block;
import net.minecraft.command.CommandBase;
import net.minecraft.command.CommandException;
import net.minecraft.command.CommandResultStats;
import net.minecraft.command.ICommand;
import net.minecraft.command.ICommandSender;
import net.minecraft.command.NumberInvalidException;
import net.minecraft.command.WrongUsageException;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.JsonToNBT;
import net.minecraft.nbt.NBTException;
import net.minecraft.server.MinecraftServer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.BlockPos;
import net.minecraft.util.Vec3i;
import net.minecraft.world.World;

public class CommandReplaceItem
extends CommandBase {
    private static final Map<String, Integer> SHORTCUTS = Maps.newHashMap();

    @Override
    public boolean isUsernameIndex(String[] stringArray, int n) {
        return stringArray.length > 0 && stringArray[0].equals("entity") && n == 1;
    }

    @Override
    public void processCommand(ICommandSender iCommandSender, String[] stringArray) throws CommandException {
        Object object;
        Item item;
        int n;
        boolean bl;
        if (stringArray.length < 1) {
            throw new WrongUsageException("commands.replaceitem.usage", new Object[0]);
        }
        if (stringArray[0].equals("entity")) {
            bl = false;
        } else {
            if (!stringArray[0].equals("block")) {
                throw new WrongUsageException("commands.replaceitem.usage", new Object[0]);
            }
            bl = true;
        }
        if (bl) {
            if (stringArray.length < 6) {
                throw new WrongUsageException("commands.replaceitem.block.usage", new Object[0]);
            }
            n = 4;
        } else {
            if (stringArray.length < 4) {
                throw new WrongUsageException("commands.replaceitem.entity.usage", new Object[0]);
            }
            n = 2;
        }
        int n2 = this.getSlotForShortcut(stringArray[n++]);
        try {
            item = CommandReplaceItem.getItemByText(iCommandSender, stringArray[n]);
        }
        catch (NumberInvalidException numberInvalidException) {
            if (Block.getBlockFromName(stringArray[n]) != Blocks.air) {
                throw numberInvalidException;
            }
            item = null;
        }
        int n3 = stringArray.length > ++n ? CommandReplaceItem.parseInt(stringArray[n++], 1, 64) : 1;
        int n4 = stringArray.length > n ? CommandReplaceItem.parseInt(stringArray[n++]) : 0;
        ItemStack itemStack = new ItemStack(item, n3, n4);
        if (stringArray.length > n) {
            object = CommandReplaceItem.getChatComponentFromNthArg(iCommandSender, stringArray, n).getUnformattedText();
            try {
                itemStack.setTagCompound(JsonToNBT.getTagFromJson((String)object));
            }
            catch (NBTException nBTException) {
                throw new CommandException("commands.replaceitem.tagError", nBTException.getMessage());
            }
        }
        if (itemStack.getItem() == null) {
            itemStack = null;
        }
        if (bl) {
            iCommandSender.setCommandStat(CommandResultStats.Type.AFFECTED_ITEMS, 0);
            object = CommandReplaceItem.parseBlockPos(iCommandSender, stringArray, 1, false);
            World world = iCommandSender.getEntityWorld();
            TileEntity tileEntity = world.getTileEntity((BlockPos)object);
            if (tileEntity == null || !(tileEntity instanceof IInventory)) {
                throw new CommandException("commands.replaceitem.noContainer", ((Vec3i)object).getX(), ((Vec3i)object).getY(), ((Vec3i)object).getZ());
            }
            IInventory iInventory = (IInventory)((Object)tileEntity);
            if (n2 >= 0 && n2 < iInventory.getSizeInventory()) {
                iInventory.setInventorySlotContents(n2, itemStack);
            }
        } else {
            object = CommandReplaceItem.func_175768_b(iCommandSender, stringArray[1]);
            iCommandSender.setCommandStat(CommandResultStats.Type.AFFECTED_ITEMS, 0);
            if (object instanceof EntityPlayer) {
                ((EntityPlayer)object).inventoryContainer.detectAndSendChanges();
            }
            if (!((Entity)object).replaceItemInInventory(n2, itemStack)) {
                throw new CommandException("commands.replaceitem.failed", n2, n3, itemStack == null ? "Air" : itemStack.getChatComponent());
            }
            if (object instanceof EntityPlayer) {
                ((EntityPlayer)object).inventoryContainer.detectAndSendChanges();
            }
        }
        iCommandSender.setCommandStat(CommandResultStats.Type.AFFECTED_ITEMS, n3);
        CommandReplaceItem.notifyOperators(iCommandSender, (ICommand)this, "commands.replaceitem.success", n2, n3, itemStack == null ? "Air" : itemStack.getChatComponent());
    }

    @Override
    public String getCommandUsage(ICommandSender iCommandSender) {
        return "commands.replaceitem.usage";
    }

    static {
        int n = 0;
        while (n < 54) {
            SHORTCUTS.put("slot.container." + n, n);
            ++n;
        }
        n = 0;
        while (n < 9) {
            SHORTCUTS.put("slot.hotbar." + n, n);
            ++n;
        }
        n = 0;
        while (n < 27) {
            SHORTCUTS.put("slot.inventory." + n, 9 + n);
            ++n;
        }
        n = 0;
        while (n < 27) {
            SHORTCUTS.put("slot.enderchest." + n, 200 + n);
            ++n;
        }
        n = 0;
        while (n < 8) {
            SHORTCUTS.put("slot.villager." + n, 300 + n);
            ++n;
        }
        n = 0;
        while (n < 15) {
            SHORTCUTS.put("slot.horse." + n, 500 + n);
            ++n;
        }
        SHORTCUTS.put("slot.weapon", 99);
        SHORTCUTS.put("slot.armor.head", 103);
        SHORTCUTS.put("slot.armor.chest", 102);
        SHORTCUTS.put("slot.armor.legs", 101);
        SHORTCUTS.put("slot.armor.feet", 100);
        SHORTCUTS.put("slot.horse.saddle", 400);
        SHORTCUTS.put("slot.horse.armor", 401);
        SHORTCUTS.put("slot.horse.chest", 499);
    }

    @Override
    public List<String> addTabCompletionOptions(ICommandSender iCommandSender, String[] stringArray, BlockPos blockPos) {
        return stringArray.length == 1 ? CommandReplaceItem.getListOfStringsMatchingLastWord(stringArray, "entity", "block") : (stringArray.length == 2 && stringArray[0].equals("entity") ? CommandReplaceItem.getListOfStringsMatchingLastWord(stringArray, this.getUsernames()) : (stringArray.length >= 2 && stringArray.length <= 4 && stringArray[0].equals("block") ? CommandReplaceItem.func_175771_a(stringArray, 1, blockPos) : (!(stringArray.length == 3 && stringArray[0].equals("entity") || stringArray.length == 5 && stringArray[0].equals("block")) ? (!(stringArray.length == 4 && stringArray[0].equals("entity") || stringArray.length == 6 && stringArray[0].equals("block")) ? null : CommandReplaceItem.getListOfStringsMatchingLastWord(stringArray, Item.itemRegistry.getKeys())) : CommandReplaceItem.getListOfStringsMatchingLastWord(stringArray, SHORTCUTS.keySet()))));
    }

    @Override
    public int getRequiredPermissionLevel() {
        return 2;
    }

    private int getSlotForShortcut(String string) throws CommandException {
        if (!SHORTCUTS.containsKey(string)) {
            throw new CommandException("commands.generic.parameter.invalid", string);
        }
        return SHORTCUTS.get(string);
    }

    @Override
    public String getCommandName() {
        return "replaceitem";
    }

    protected String[] getUsernames() {
        return MinecraftServer.getServer().getAllUsernames();
    }
}

