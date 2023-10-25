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
import net.minecraft.command.WrongUsageException;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.JsonToNBT;
import net.minecraft.nbt.NBTException;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.BlockPos;

public class CommandGive
extends CommandBase {
    @Override
    public String getCommandName() {
        return "give";
    }

    @Override
    public int getRequiredPermissionLevel() {
        return 2;
    }

    @Override
    public String getCommandUsage(ICommandSender sender) {
        return "commands.give.usage";
    }

    @Override
    public void execute(MinecraftServer server, ICommandSender sender, String[] args) throws CommandException {
        boolean flag;
        if (args.length < 2) {
            throw new WrongUsageException("commands.give.usage", new Object[0]);
        }
        EntityPlayerMP entityplayer = CommandGive.getPlayer(server, sender, args[0]);
        Item item = CommandGive.getItemByText(sender, args[1]);
        int i = args.length >= 3 ? CommandGive.parseInt(args[2], 1, item.getItemStackLimit()) : 1;
        int j = args.length >= 4 ? CommandGive.parseInt(args[3]) : 0;
        ItemStack itemstack = new ItemStack(item, i, j);
        if (args.length >= 5) {
            String s = CommandGive.buildString(args, 4);
            try {
                itemstack.setTagCompound(JsonToNBT.getTagFromJson(s));
            }
            catch (NBTException nbtexception) {
                throw new CommandException("commands.give.tagError", nbtexception.getMessage());
            }
        }
        if (flag = entityplayer.inventory.addItemStackToInventory(itemstack)) {
            entityplayer.world.playSound(null, entityplayer.posX, entityplayer.posY, entityplayer.posZ, SoundEvents.ENTITY_ITEM_PICKUP, SoundCategory.PLAYERS, 0.2f, ((entityplayer.getRNG().nextFloat() - entityplayer.getRNG().nextFloat()) * 0.7f + 1.0f) * 2.0f);
            entityplayer.inventoryContainer.detectAndSendChanges();
        }
        if (flag && itemstack.isEmpty()) {
            itemstack.func_190920_e(1);
            sender.setCommandStat(CommandResultStats.Type.AFFECTED_ITEMS, i);
            EntityItem entityitem1 = entityplayer.dropItem(itemstack, false);
            if (entityitem1 != null) {
                entityitem1.makeFakeItem();
            }
        } else {
            sender.setCommandStat(CommandResultStats.Type.AFFECTED_ITEMS, i - itemstack.getCount());
            EntityItem entityitem = entityplayer.dropItem(itemstack, false);
            if (entityitem != null) {
                entityitem.setNoPickupDelay();
                entityitem.setOwner(entityplayer.getName());
            }
        }
        CommandGive.notifyCommandListener(sender, (ICommand)this, "commands.give.success", itemstack.getTextComponent(), i, entityplayer.getName());
    }

    @Override
    public List<String> getTabCompletionOptions(MinecraftServer server, ICommandSender sender, String[] args, @Nullable BlockPos pos) {
        if (args.length == 1) {
            return CommandGive.getListOfStringsMatchingLastWord(args, server.getAllUsernames());
        }
        return args.length == 2 ? CommandGive.getListOfStringsMatchingLastWord(args, Item.REGISTRY.getKeys()) : Collections.emptyList();
    }

    @Override
    public boolean isUsernameIndex(String[] args, int index) {
        return index == 0;
    }
}

