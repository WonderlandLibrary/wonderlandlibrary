// 
// Decompiled by Procyon v0.5.36
// 

package net.minecraft.command;

import net.minecraft.inventory.EntityEquipmentSlot;
import com.google.common.collect.Maps;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import javax.annotation.Nullable;
import net.minecraft.entity.Entity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;
import net.minecraft.util.math.BlockPos;
import net.minecraft.item.Item;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.nbt.NBTException;
import net.minecraft.nbt.JsonToNBT;
import net.minecraft.item.ItemStack;
import net.minecraft.init.Blocks;
import net.minecraft.block.Block;
import net.minecraft.server.MinecraftServer;
import java.util.Map;

public class CommandReplaceItem extends CommandBase
{
    private static final Map<String, Integer> SHORTCUTS;
    
    @Override
    public String getName() {
        return "replaceitem";
    }
    
    @Override
    public int getRequiredPermissionLevel() {
        return 2;
    }
    
    @Override
    public String getUsage(final ICommandSender sender) {
        return "commands.replaceitem.usage";
    }
    
    @Override
    public void execute(final MinecraftServer server, final ICommandSender sender, final String[] args) throws CommandException {
        if (args.length < 1) {
            throw new WrongUsageException("commands.replaceitem.usage", new Object[0]);
        }
        boolean flag;
        if ("entity".equals(args[0])) {
            flag = false;
        }
        else {
            if (!"block".equals(args[0])) {
                throw new WrongUsageException("commands.replaceitem.usage", new Object[0]);
            }
            flag = true;
        }
        int i;
        if (flag) {
            if (args.length < 6) {
                throw new WrongUsageException("commands.replaceitem.block.usage", new Object[0]);
            }
            i = 4;
        }
        else {
            if (args.length < 4) {
                throw new WrongUsageException("commands.replaceitem.entity.usage", new Object[0]);
            }
            i = 2;
        }
        final String s = args[i];
        final int j = this.getSlotForShortcut(args[i++]);
        Item item;
        try {
            item = CommandBase.getItemByText(sender, args[i]);
        }
        catch (NumberInvalidException numberinvalidexception) {
            if (Block.getBlockFromName(args[i]) != Blocks.AIR) {
                throw numberinvalidexception;
            }
            item = null;
        }
        ++i;
        final int k = (args.length > i) ? CommandBase.parseInt(args[i++], 1, item.getItemStackLimit()) : 1;
        final int l = (args.length > i) ? CommandBase.parseInt(args[i++]) : 0;
        final ItemStack itemstack = new ItemStack(item, k, l);
        if (args.length > i) {
            final String s2 = CommandBase.buildString(args, i);
            try {
                itemstack.setTagCompound(JsonToNBT.getTagFromJson(s2));
            }
            catch (NBTException nbtexception) {
                throw new CommandException("commands.replaceitem.tagError", new Object[] { nbtexception.getMessage() });
            }
        }
        if (flag) {
            sender.setCommandStat(CommandResultStats.Type.AFFECTED_ITEMS, 0);
            final BlockPos blockpos = CommandBase.parseBlockPos(sender, args, 1, false);
            final World world = sender.getEntityWorld();
            final TileEntity tileentity = world.getTileEntity(blockpos);
            if (tileentity == null || !(tileentity instanceof IInventory)) {
                throw new CommandException("commands.replaceitem.noContainer", new Object[] { blockpos.getX(), blockpos.getY(), blockpos.getZ() });
            }
            final IInventory iinventory = (IInventory)tileentity;
            if (j >= 0 && j < iinventory.getSizeInventory()) {
                iinventory.setInventorySlotContents(j, itemstack);
            }
        }
        else {
            final Entity entity = CommandBase.getEntity(server, sender, args[1]);
            sender.setCommandStat(CommandResultStats.Type.AFFECTED_ITEMS, 0);
            if (entity instanceof EntityPlayer) {
                ((EntityPlayer)entity).inventoryContainer.detectAndSendChanges();
            }
            if (!entity.replaceItemInInventory(j, itemstack)) {
                throw new CommandException("commands.replaceitem.failed", new Object[] { s, k, itemstack.isEmpty() ? "Air" : itemstack.getTextComponent() });
            }
            if (entity instanceof EntityPlayer) {
                ((EntityPlayer)entity).inventoryContainer.detectAndSendChanges();
            }
        }
        sender.setCommandStat(CommandResultStats.Type.AFFECTED_ITEMS, k);
        CommandBase.notifyCommandListener(sender, this, "commands.replaceitem.success", s, k, itemstack.isEmpty() ? "Air" : itemstack.getTextComponent());
    }
    
    private int getSlotForShortcut(final String shortcut) throws CommandException {
        if (!CommandReplaceItem.SHORTCUTS.containsKey(shortcut)) {
            throw new CommandException("commands.generic.parameter.invalid", new Object[] { shortcut });
        }
        return CommandReplaceItem.SHORTCUTS.get(shortcut);
    }
    
    @Override
    public List<String> getTabCompletions(final MinecraftServer server, final ICommandSender sender, final String[] args, @Nullable final BlockPos targetPos) {
        if (args.length == 1) {
            return CommandBase.getListOfStringsMatchingLastWord(args, "entity", "block");
        }
        if (args.length == 2 && "entity".equals(args[0])) {
            return CommandBase.getListOfStringsMatchingLastWord(args, server.getOnlinePlayerNames());
        }
        if (args.length >= 2 && args.length <= 4 && "block".equals(args[0])) {
            return CommandBase.getTabCompletionCoordinate(args, 1, targetPos);
        }
        if ((args.length != 3 || !"entity".equals(args[0])) && (args.length != 5 || !"block".equals(args[0]))) {
            return ((args.length != 4 || !"entity".equals(args[0])) && (args.length != 6 || !"block".equals(args[0]))) ? Collections.emptyList() : CommandBase.getListOfStringsMatchingLastWord(args, Item.REGISTRY.getKeys());
        }
        return CommandBase.getListOfStringsMatchingLastWord(args, CommandReplaceItem.SHORTCUTS.keySet());
    }
    
    @Override
    public boolean isUsernameIndex(final String[] args, final int index) {
        return args.length > 0 && "entity".equals(args[0]) && index == 1;
    }
    
    static {
        SHORTCUTS = Maps.newHashMap();
        for (int i = 0; i < 54; ++i) {
            CommandReplaceItem.SHORTCUTS.put("slot.container." + i, i);
        }
        for (int j = 0; j < 9; ++j) {
            CommandReplaceItem.SHORTCUTS.put("slot.hotbar." + j, j);
        }
        for (int k = 0; k < 27; ++k) {
            CommandReplaceItem.SHORTCUTS.put("slot.inventory." + k, 9 + k);
        }
        for (int l = 0; l < 27; ++l) {
            CommandReplaceItem.SHORTCUTS.put("slot.enderchest." + l, 200 + l);
        }
        for (int i2 = 0; i2 < 8; ++i2) {
            CommandReplaceItem.SHORTCUTS.put("slot.villager." + i2, 300 + i2);
        }
        for (int j2 = 0; j2 < 15; ++j2) {
            CommandReplaceItem.SHORTCUTS.put("slot.horse." + j2, 500 + j2);
        }
        CommandReplaceItem.SHORTCUTS.put("slot.weapon", 98);
        CommandReplaceItem.SHORTCUTS.put("slot.weapon.mainhand", 98);
        CommandReplaceItem.SHORTCUTS.put("slot.weapon.offhand", 99);
        CommandReplaceItem.SHORTCUTS.put("slot.armor.head", 100 + EntityEquipmentSlot.HEAD.getIndex());
        CommandReplaceItem.SHORTCUTS.put("slot.armor.chest", 100 + EntityEquipmentSlot.CHEST.getIndex());
        CommandReplaceItem.SHORTCUTS.put("slot.armor.legs", 100 + EntityEquipmentSlot.LEGS.getIndex());
        CommandReplaceItem.SHORTCUTS.put("slot.armor.feet", 100 + EntityEquipmentSlot.FEET.getIndex());
        CommandReplaceItem.SHORTCUTS.put("slot.horse.saddle", 400);
        CommandReplaceItem.SHORTCUTS.put("slot.horse.armor", 401);
        CommandReplaceItem.SHORTCUTS.put("slot.horse.chest", 499);
    }
}
