/*
 * Decompiled with CFR 0.150.
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
    private static final String __OBFID = "CL_00000218";

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
    public void processCommand(ICommandSender sender, String[] args) throws CommandException {
        EntityPlayerMP var3 = args.length == 0 ? CommandClearInventory.getCommandSenderAsPlayer(sender) : CommandClearInventory.getPlayer(sender, args[0]);
        Item var4 = args.length >= 2 ? CommandClearInventory.getItemByText(sender, args[1]) : null;
        int var5 = args.length >= 3 ? CommandClearInventory.parseInt(args[2], -1) : -1;
        int var6 = args.length >= 4 ? CommandClearInventory.parseInt(args[3], -1) : -1;
        NBTTagCompound var7 = null;
        if (args.length >= 5) {
            try {
                var7 = JsonToNBT.func_180713_a(CommandClearInventory.func_180529_a(args, 4));
            }
            catch (NBTException var9) {
                throw new CommandException("commands.clear.tagError", var9.getMessage());
            }
        }
        if (args.length >= 2 && var4 == null) {
            throw new CommandException("commands.clear.failure", var3.getName());
        }
        int var8 = var3.inventory.func_174925_a(var4, var5, var6, var7);
        var3.inventoryContainer.detectAndSendChanges();
        if (!var3.capabilities.isCreativeMode) {
            var3.updateHeldItem();
        }
        sender.func_174794_a(CommandResultStats.Type.AFFECTED_ITEMS, var8);
        if (var8 == 0) {
            throw new CommandException("commands.clear.failure", var3.getName());
        }
        if (var6 == 0) {
            sender.addChatMessage(new ChatComponentTranslation("commands.clear.testing", var3.getName(), var8));
        } else {
            CommandClearInventory.notifyOperators(sender, (ICommand)this, "commands.clear.success", var3.getName(), var8);
        }
    }

    @Override
    public List addTabCompletionOptions(ICommandSender sender, String[] args, BlockPos pos) {
        return args.length == 1 ? CommandClearInventory.getListOfStringsMatchingLastWord(args, this.func_147209_d()) : (args.length == 2 ? CommandClearInventory.func_175762_a(args, Item.itemRegistry.getKeys()) : null);
    }

    protected String[] func_147209_d() {
        return MinecraftServer.getServer().getAllUsernames();
    }

    @Override
    public boolean isUsernameIndex(String[] args, int index) {
        return index == 0;
    }
}

