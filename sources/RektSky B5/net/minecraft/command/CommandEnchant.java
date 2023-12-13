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
import net.minecraft.command.NumberInvalidException;
import net.minecraft.command.WrongUsageException;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.BlockPos;

public class CommandEnchant
extends CommandBase {
    @Override
    public String getCommandName() {
        return "enchant";
    }

    @Override
    public int getRequiredPermissionLevel() {
        return 2;
    }

    @Override
    public String getCommandUsage(ICommandSender sender) {
        return "commands.enchant.usage";
    }

    @Override
    public void processCommand(ICommandSender sender, String[] args) throws CommandException {
        NBTTagList nbttaglist;
        int i2;
        if (args.length < 2) {
            throw new WrongUsageException("commands.enchant.usage", new Object[0]);
        }
        EntityPlayerMP entityplayer = CommandEnchant.getPlayer(sender, args[0]);
        sender.setCommandStat(CommandResultStats.Type.AFFECTED_ITEMS, 0);
        try {
            i2 = CommandEnchant.parseInt(args[1], 0);
        }
        catch (NumberInvalidException numberinvalidexception) {
            Enchantment enchantment = Enchantment.getEnchantmentByLocation(args[1]);
            if (enchantment == null) {
                throw numberinvalidexception;
            }
            i2 = enchantment.effectId;
        }
        int j2 = 1;
        ItemStack itemstack = entityplayer.getCurrentEquippedItem();
        if (itemstack == null) {
            throw new CommandException("commands.enchant.noItem", new Object[0]);
        }
        Enchantment enchantment1 = Enchantment.getEnchantmentById(i2);
        if (enchantment1 == null) {
            throw new NumberInvalidException("commands.enchant.notFound", i2);
        }
        if (!enchantment1.canApply(itemstack)) {
            throw new CommandException("commands.enchant.cantEnchant", new Object[0]);
        }
        if (args.length >= 3) {
            j2 = CommandEnchant.parseInt(args[2], enchantment1.getMinLevel(), enchantment1.getMaxLevel());
        }
        if (itemstack.hasTagCompound() && (nbttaglist = itemstack.getEnchantmentTagList()) != null) {
            for (int k2 = 0; k2 < nbttaglist.tagCount(); ++k2) {
                Enchantment enchantment2;
                short l2 = nbttaglist.getCompoundTagAt(k2).getShort("id");
                if (Enchantment.getEnchantmentById(l2) == null || (enchantment2 = Enchantment.getEnchantmentById(l2)).canApplyTogether(enchantment1)) continue;
                throw new CommandException("commands.enchant.cantCombine", enchantment1.getTranslatedName(j2), enchantment2.getTranslatedName(nbttaglist.getCompoundTagAt(k2).getShort("lvl")));
            }
        }
        itemstack.addEnchantment(enchantment1, j2);
        CommandEnchant.notifyOperators(sender, (ICommand)this, "commands.enchant.success", new Object[0]);
        sender.setCommandStat(CommandResultStats.Type.AFFECTED_ITEMS, 1);
    }

    @Override
    public List<String> addTabCompletionOptions(ICommandSender sender, String[] args, BlockPos pos) {
        return args.length == 1 ? CommandEnchant.getListOfStringsMatchingLastWord(args, this.getListOfPlayers()) : (args.length == 2 ? CommandEnchant.getListOfStringsMatchingLastWord(args, Enchantment.func_181077_c()) : null);
    }

    protected String[] getListOfPlayers() {
        return MinecraftServer.getServer().getAllUsernames();
    }

    @Override
    public boolean isUsernameIndex(String[] args, int index) {
        return index == 0;
    }
}

