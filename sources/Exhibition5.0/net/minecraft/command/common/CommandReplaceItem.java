// 
// Decompiled by Procyon v0.5.30
// 

package net.minecraft.command.common;

import com.google.common.collect.Maps;
import net.minecraft.server.MinecraftServer;
import java.util.Collection;
import java.util.List;
import net.minecraft.entity.Entity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;
import net.minecraft.util.BlockPos;
import net.minecraft.item.Item;
import net.minecraft.command.ICommand;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.command.CommandResultStats;
import net.minecraft.nbt.NBTException;
import net.minecraft.command.CommandException;
import net.minecraft.nbt.JsonToNBT;
import net.minecraft.item.ItemStack;
import net.minecraft.command.NumberInvalidException;
import net.minecraft.init.Blocks;
import net.minecraft.block.Block;
import net.minecraft.command.WrongUsageException;
import net.minecraft.command.ICommandSender;
import java.util.Map;
import net.minecraft.command.CommandBase;

public class CommandReplaceItem extends CommandBase
{
    private static final Map field_175785_a;
    private static final String __OBFID = "CL_00002340";
    
    @Override
    public String getCommandName() {
        return "replaceitem";
    }
    
    @Override
    public int getRequiredPermissionLevel() {
        return 2;
    }
    
    @Override
    public String getCommandUsage(final ICommandSender sender) {
        return "commands.replaceitem.usage";
    }
    
    @Override
    public void processCommand(final ICommandSender sender, final String[] args) throws CommandException {
        if (args.length < 1) {
            throw new WrongUsageException("commands.replaceitem.usage", new Object[0]);
        }
        boolean var3;
        if (args[0].equals("entity")) {
            var3 = false;
        }
        else {
            if (!args[0].equals("block")) {
                throw new WrongUsageException("commands.replaceitem.usage", new Object[0]);
            }
            var3 = true;
        }
        byte var4;
        if (var3) {
            if (args.length < 6) {
                throw new WrongUsageException("commands.replaceitem.block.usage", new Object[0]);
            }
            var4 = 4;
        }
        else {
            if (args.length < 4) {
                throw new WrongUsageException("commands.replaceitem.entity.usage", new Object[0]);
            }
            var4 = 2;
        }
        int var5 = var4 + 1;
        final int var6 = this.func_175783_e(args[var4]);
        Item var7;
        try {
            var7 = CommandBase.getItemByText(sender, args[var5]);
        }
        catch (NumberInvalidException var8) {
            if (Block.getBlockFromName(args[var5]) != Blocks.air) {
                throw var8;
            }
            var7 = null;
        }
        ++var5;
        final int var9 = (args.length > var5) ? CommandBase.parseInt(args[var5++], 1, 64) : 1;
        final int var10 = (args.length > var5) ? CommandBase.parseInt(args[var5++]) : 0;
        ItemStack var11 = new ItemStack(var7, var9, var10);
        if (args.length > var5) {
            final String var12 = CommandBase.getChatComponentFromNthArg(sender, args, var5).getUnformattedText();
            try {
                var11.setTagCompound(JsonToNBT.func_180713_a(var12));
            }
            catch (NBTException var13) {
                throw new CommandException("commands.replaceitem.tagError", new Object[] { var13.getMessage() });
            }
        }
        if (var11.getItem() == null) {
            var11 = null;
        }
        if (var3) {
            sender.func_174794_a(CommandResultStats.Type.AFFECTED_ITEMS, 0);
            final BlockPos var14 = CommandBase.func_175757_a(sender, args, 1, false);
            final World var15 = sender.getEntityWorld();
            final TileEntity var16 = var15.getTileEntity(var14);
            if (var16 == null || !(var16 instanceof IInventory)) {
                throw new CommandException("commands.replaceitem.noContainer", new Object[] { var14.getX(), var14.getY(), var14.getZ() });
            }
            final IInventory var17 = (IInventory)var16;
            if (var6 >= 0 && var6 < var17.getSizeInventory()) {
                var17.setInventorySlotContents(var6, var11);
            }
        }
        else {
            final Entity var18 = CommandBase.func_175768_b(sender, args[1]);
            sender.func_174794_a(CommandResultStats.Type.AFFECTED_ITEMS, 0);
            if (var18 instanceof EntityPlayer) {
                ((EntityPlayer)var18).inventoryContainer.detectAndSendChanges();
            }
            if (!var18.func_174820_d(var6, var11)) {
                throw new CommandException("commands.replaceitem.failed", new Object[] { var6, var9, (var11 == null) ? "Air" : var11.getChatComponent() });
            }
            if (var18 instanceof EntityPlayer) {
                ((EntityPlayer)var18).inventoryContainer.detectAndSendChanges();
            }
        }
        sender.func_174794_a(CommandResultStats.Type.AFFECTED_ITEMS, var9);
        CommandBase.notifyOperators(sender, this, "commands.replaceitem.success", var6, var9, (var11 == null) ? "Air" : var11.getChatComponent());
    }
    
    private int func_175783_e(final String p_175783_1_) throws CommandException {
        if (!CommandReplaceItem.field_175785_a.containsKey(p_175783_1_)) {
            throw new CommandException("commands.generic.parameter.invalid", new Object[] { p_175783_1_ });
        }
        return CommandReplaceItem.field_175785_a.get(p_175783_1_);
    }
    
    @Override
    public List addTabCompletionOptions(final ICommandSender sender, final String[] args, final BlockPos pos) {
        return (args.length == 1) ? CommandBase.getListOfStringsMatchingLastWord(args, "entity", "block") : ((args.length == 2 && args[0].equals("entity")) ? CommandBase.getListOfStringsMatchingLastWord(args, this.func_175784_d()) : (((args.length != 3 || !args[0].equals("entity")) && (args.length != 5 || !args[0].equals("block"))) ? (((args.length != 4 || !args[0].equals("entity")) && (args.length != 6 || !args[0].equals("block"))) ? null : CommandBase.func_175762_a(args, Item.itemRegistry.getKeys())) : CommandBase.func_175762_a(args, CommandReplaceItem.field_175785_a.keySet())));
    }
    
    protected String[] func_175784_d() {
        return MinecraftServer.getServer().getAllUsernames();
    }
    
    @Override
    public boolean isUsernameIndex(final String[] args, final int index) {
        return args.length > 0 && args[0].equals("entity") && index == 1;
    }
    
    static {
        field_175785_a = Maps.newHashMap();
        for (int var0 = 0; var0 < 54; ++var0) {
            CommandReplaceItem.field_175785_a.put("slot.container." + var0, var0);
        }
        for (int var0 = 0; var0 < 9; ++var0) {
            CommandReplaceItem.field_175785_a.put("slot.hotbar." + var0, var0);
        }
        for (int var0 = 0; var0 < 27; ++var0) {
            CommandReplaceItem.field_175785_a.put("slot.inventory." + var0, 9 + var0);
        }
        for (int var0 = 0; var0 < 27; ++var0) {
            CommandReplaceItem.field_175785_a.put("slot.enderchest." + var0, 200 + var0);
        }
        for (int var0 = 0; var0 < 8; ++var0) {
            CommandReplaceItem.field_175785_a.put("slot.villager." + var0, 300 + var0);
        }
        for (int var0 = 0; var0 < 15; ++var0) {
            CommandReplaceItem.field_175785_a.put("slot.horse." + var0, 500 + var0);
        }
        CommandReplaceItem.field_175785_a.put("slot.weapon", 99);
        CommandReplaceItem.field_175785_a.put("slot.armor.head", 103);
        CommandReplaceItem.field_175785_a.put("slot.armor.chest", 102);
        CommandReplaceItem.field_175785_a.put("slot.armor.legs", 101);
        CommandReplaceItem.field_175785_a.put("slot.armor.feet", 100);
        CommandReplaceItem.field_175785_a.put("slot.horse.saddle", 400);
        CommandReplaceItem.field_175785_a.put("slot.horse.armor", 401);
        CommandReplaceItem.field_175785_a.put("slot.horse.chest", 499);
    }
}
