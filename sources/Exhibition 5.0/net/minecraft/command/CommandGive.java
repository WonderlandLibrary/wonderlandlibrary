// 
// Decompiled by Procyon v0.5.30
// 

package net.minecraft.command;

import net.minecraft.server.MinecraftServer;
import java.util.Collection;
import java.util.List;
import net.minecraft.util.BlockPos;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.item.Item;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.entity.Entity;
import net.minecraft.nbt.NBTException;
import net.minecraft.nbt.JsonToNBT;
import net.minecraft.item.ItemStack;

public class CommandGive extends CommandBase
{
    private static final String __OBFID = "CL_00000502";
    
    @Override
    public String getCommandName() {
        return "give";
    }
    
    @Override
    public int getRequiredPermissionLevel() {
        return 2;
    }
    
    @Override
    public String getCommandUsage(final ICommandSender sender) {
        return "commands.give.usage";
    }
    
    @Override
    public void processCommand(final ICommandSender sender, final String[] args) throws CommandException {
        if (args.length < 2) {
            throw new WrongUsageException("commands.give.usage", new Object[0]);
        }
        final EntityPlayerMP var3 = CommandBase.getPlayer(sender, args[0]);
        final Item var4 = CommandBase.getItemByText(sender, args[1]);
        final int var5 = (args.length >= 3) ? CommandBase.parseInt(args[2], 1, 64) : 1;
        final int var6 = (args.length >= 4) ? CommandBase.parseInt(args[3]) : 0;
        final ItemStack var7 = new ItemStack(var4, var5, var6);
        if (args.length >= 5) {
            final String var8 = CommandBase.getChatComponentFromNthArg(sender, args, 4).getUnformattedText();
            try {
                var7.setTagCompound(JsonToNBT.func_180713_a(var8));
            }
            catch (NBTException var9) {
                throw new CommandException("commands.give.tagError", new Object[] { var9.getMessage() });
            }
        }
        final boolean var10 = var3.inventory.addItemStackToInventory(var7);
        if (var10) {
            var3.worldObj.playSoundAtEntity(var3, "random.pop", 0.2f, ((var3.getRNG().nextFloat() - var3.getRNG().nextFloat()) * 0.7f + 1.0f) * 2.0f);
            var3.inventoryContainer.detectAndSendChanges();
        }
        if (var10 && var7.stackSize <= 0) {
            var7.stackSize = 1;
            sender.func_174794_a(CommandResultStats.Type.AFFECTED_ITEMS, var5);
            final EntityItem var11 = var3.dropPlayerItemWithRandomChoice(var7, false);
            if (var11 != null) {
                var11.func_174870_v();
            }
        }
        else {
            sender.func_174794_a(CommandResultStats.Type.AFFECTED_ITEMS, var5 - var7.stackSize);
            final EntityItem var11 = var3.dropPlayerItemWithRandomChoice(var7, false);
            if (var11 != null) {
                var11.setNoPickupDelay();
                var11.setOwner(var3.getName());
            }
        }
        CommandBase.notifyOperators(sender, this, "commands.give.success", var7.getChatComponent(), var5, var3.getName());
    }
    
    @Override
    public List addTabCompletionOptions(final ICommandSender sender, final String[] args, final BlockPos pos) {
        return (args.length == 1) ? CommandBase.getListOfStringsMatchingLastWord(args, this.getPlayers()) : ((args.length == 2) ? CommandBase.func_175762_a(args, Item.itemRegistry.getKeys()) : null);
    }
    
    protected String[] getPlayers() {
        return MinecraftServer.getServer().getAllUsernames();
    }
    
    @Override
    public boolean isUsernameIndex(final String[] args, final int index) {
        return index == 0;
    }
}
