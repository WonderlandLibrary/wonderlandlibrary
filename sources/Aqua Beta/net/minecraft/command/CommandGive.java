package net.minecraft.command;

import java.util.Collection;
import java.util.List;
import net.minecraft.command.CommandBase;
import net.minecraft.command.CommandException;
import net.minecraft.command.CommandResultStats;
import net.minecraft.command.ICommand;
import net.minecraft.command.ICommandSender;
import net.minecraft.command.WrongUsageException;
import net.minecraft.entity.Entity;
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
    public String getCommandName() {
        return "give";
    }

    public int getRequiredPermissionLevel() {
        return 2;
    }

    public String getCommandUsage(ICommandSender sender) {
        return "commands.give.usage";
    }

    public void processCommand(ICommandSender sender, String[] args) throws CommandException {
        boolean flag;
        if (args.length < 2) {
            throw new WrongUsageException("commands.give.usage", new Object[0]);
        }
        EntityPlayerMP entityplayer = CommandGive.getPlayer((ICommandSender)sender, (String)args[0]);
        Item item = CommandGive.getItemByText((ICommandSender)sender, (String)args[1]);
        int i = args.length >= 3 ? CommandGive.parseInt((String)args[2], (int)1, (int)64) : 1;
        int j = args.length >= 4 ? CommandGive.parseInt((String)args[3]) : 0;
        ItemStack itemstack = new ItemStack(item, i, j);
        if (args.length >= 5) {
            String s = CommandGive.getChatComponentFromNthArg((ICommandSender)sender, (String[])args, (int)4).getUnformattedText();
            try {
                itemstack.setTagCompound(JsonToNBT.getTagFromJson((String)s));
            }
            catch (NBTException nbtexception) {
                throw new CommandException("commands.give.tagError", new Object[]{nbtexception.getMessage()});
            }
        }
        if (flag = entityplayer.inventory.addItemStackToInventory(itemstack)) {
            entityplayer.worldObj.playSoundAtEntity((Entity)entityplayer, "random.pop", 0.2f, ((entityplayer.getRNG().nextFloat() - entityplayer.getRNG().nextFloat()) * 0.7f + 1.0f) * 2.0f);
            entityplayer.inventoryContainer.detectAndSendChanges();
        }
        if (flag && itemstack.stackSize <= 0) {
            itemstack.stackSize = 1;
            sender.setCommandStat(CommandResultStats.Type.AFFECTED_ITEMS, i);
            EntityItem entityitem1 = entityplayer.dropPlayerItemWithRandomChoice(itemstack, false);
            if (entityitem1 != null) {
                entityitem1.func_174870_v();
            }
        } else {
            sender.setCommandStat(CommandResultStats.Type.AFFECTED_ITEMS, i - itemstack.stackSize);
            EntityItem entityitem = entityplayer.dropPlayerItemWithRandomChoice(itemstack, false);
            if (entityitem != null) {
                entityitem.setNoPickupDelay();
                entityitem.setOwner(entityplayer.getName());
            }
        }
        CommandGive.notifyOperators((ICommandSender)sender, (ICommand)this, (String)"commands.give.success", (Object[])new Object[]{itemstack.getChatComponent(), i, entityplayer.getName()});
    }

    public List<String> addTabCompletionOptions(ICommandSender sender, String[] args, BlockPos pos) {
        return args.length == 1 ? CommandGive.getListOfStringsMatchingLastWord((String[])args, (String[])this.getPlayers()) : (args.length == 2 ? CommandGive.getListOfStringsMatchingLastWord((String[])args, (Collection)Item.itemRegistry.getKeys()) : null);
    }

    protected String[] getPlayers() {
        return MinecraftServer.getServer().getAllUsernames();
    }

    public boolean isUsernameIndex(String[] args, int index) {
        return index == 0;
    }
}
