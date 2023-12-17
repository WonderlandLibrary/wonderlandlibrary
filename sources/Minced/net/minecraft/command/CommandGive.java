// 
// Decompiled by Procyon v0.5.36
// 

package net.minecraft.command;

import java.util.Collections;
import java.util.Collection;
import java.util.List;
import javax.annotation.Nullable;
import net.minecraft.util.math.BlockPos;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.item.Item;
import net.minecraft.util.SoundCategory;
import net.minecraft.init.SoundEvents;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTException;
import net.minecraft.nbt.JsonToNBT;
import net.minecraft.item.ItemStack;
import net.minecraft.server.MinecraftServer;

public class CommandGive extends CommandBase
{
    @Override
    public String getName() {
        return "give";
    }
    
    @Override
    public int getRequiredPermissionLevel() {
        return 2;
    }
    
    @Override
    public String getUsage(final ICommandSender sender) {
        return "commands.give.usage";
    }
    
    @Override
    public void execute(final MinecraftServer server, final ICommandSender sender, final String[] args) throws CommandException {
        if (args.length < 2) {
            throw new WrongUsageException("commands.give.usage", new Object[0]);
        }
        final EntityPlayer entityplayer = CommandBase.getPlayer(server, sender, args[0]);
        final Item item = CommandBase.getItemByText(sender, args[1]);
        final int i = (args.length >= 3) ? CommandBase.parseInt(args[2], 1, item.getItemStackLimit()) : 1;
        final int j = (args.length >= 4) ? CommandBase.parseInt(args[3]) : 0;
        final ItemStack itemstack = new ItemStack(item, i, j);
        if (args.length >= 5) {
            final String s = CommandBase.buildString(args, 4);
            try {
                itemstack.setTagCompound(JsonToNBT.getTagFromJson(s));
            }
            catch (NBTException nbtexception) {
                throw new CommandException("commands.give.tagError", new Object[] { nbtexception.getMessage() });
            }
        }
        final boolean flag = entityplayer.inventory.addItemStackToInventory(itemstack);
        if (flag) {
            entityplayer.world.playSound(null, entityplayer.posX, entityplayer.posY, entityplayer.posZ, SoundEvents.ENTITY_ITEM_PICKUP, SoundCategory.PLAYERS, 0.2f, ((entityplayer.getRNG().nextFloat() - entityplayer.getRNG().nextFloat()) * 0.7f + 1.0f) * 2.0f);
            entityplayer.inventoryContainer.detectAndSendChanges();
        }
        if (flag && itemstack.isEmpty()) {
            itemstack.setCount(1);
            sender.setCommandStat(CommandResultStats.Type.AFFECTED_ITEMS, i);
            final EntityItem entityitem1 = entityplayer.dropItem(itemstack, false);
            if (entityitem1 != null) {
                entityitem1.makeFakeItem();
            }
        }
        else {
            sender.setCommandStat(CommandResultStats.Type.AFFECTED_ITEMS, i - itemstack.getCount());
            final EntityItem entityitem2 = entityplayer.dropItem(itemstack, false);
            if (entityitem2 != null) {
                entityitem2.setNoPickupDelay();
                entityitem2.setOwner(entityplayer.getName());
            }
        }
        CommandBase.notifyCommandListener(sender, this, "commands.give.success", itemstack.getTextComponent(), i, entityplayer.getName());
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
