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
    public boolean isUsernameIndex(String[] stringArray, int n) {
        return n == 0;
    }

    @Override
    public List<String> addTabCompletionOptions(ICommandSender iCommandSender, String[] stringArray, BlockPos blockPos) {
        return stringArray.length == 1 ? CommandEnchant.getListOfStringsMatchingLastWord(stringArray, this.getListOfPlayers()) : (stringArray.length == 2 ? CommandEnchant.getListOfStringsMatchingLastWord(stringArray, Enchantment.func_181077_c()) : null);
    }

    @Override
    public String getCommandUsage(ICommandSender iCommandSender) {
        return "commands.enchant.usage";
    }

    @Override
    public int getRequiredPermissionLevel() {
        return 2;
    }

    @Override
    public void processCommand(ICommandSender iCommandSender, String[] stringArray) throws CommandException {
        NBTTagList nBTTagList;
        Object object;
        int n;
        if (stringArray.length < 2) {
            throw new WrongUsageException("commands.enchant.usage", new Object[0]);
        }
        EntityPlayerMP entityPlayerMP = CommandEnchant.getPlayer(iCommandSender, stringArray[0]);
        iCommandSender.setCommandStat(CommandResultStats.Type.AFFECTED_ITEMS, 0);
        try {
            n = CommandEnchant.parseInt(stringArray[1], 0);
        }
        catch (NumberInvalidException numberInvalidException) {
            object = Enchantment.getEnchantmentByLocation(stringArray[1]);
            if (object == null) {
                throw numberInvalidException;
            }
            n = ((Enchantment)object).effectId;
        }
        int n2 = 1;
        object = entityPlayerMP.getCurrentEquippedItem();
        if (object == null) {
            throw new CommandException("commands.enchant.noItem", new Object[0]);
        }
        Enchantment enchantment = Enchantment.getEnchantmentById(n);
        if (enchantment == null) {
            throw new NumberInvalidException("commands.enchant.notFound", n);
        }
        if (!enchantment.canApply((ItemStack)object)) {
            throw new CommandException("commands.enchant.cantEnchant", new Object[0]);
        }
        if (stringArray.length >= 3) {
            n2 = CommandEnchant.parseInt(stringArray[2], enchantment.getMinLevel(), enchantment.getMaxLevel());
        }
        if (((ItemStack)object).hasTagCompound() && (nBTTagList = ((ItemStack)object).getEnchantmentTagList()) != null) {
            int n3 = 0;
            while (n3 < nBTTagList.tagCount()) {
                Enchantment enchantment2;
                short s = nBTTagList.getCompoundTagAt(n3).getShort("id");
                if (Enchantment.getEnchantmentById(s) != null && !(enchantment2 = Enchantment.getEnchantmentById(s)).canApplyTogether(enchantment)) {
                    throw new CommandException("commands.enchant.cantCombine", enchantment.getTranslatedName(n2), enchantment2.getTranslatedName(nBTTagList.getCompoundTagAt(n3).getShort("lvl")));
                }
                ++n3;
            }
        }
        ((ItemStack)object).addEnchantment(enchantment, n2);
        CommandEnchant.notifyOperators(iCommandSender, (ICommand)this, "commands.enchant.success", new Object[0]);
        iCommandSender.setCommandStat(CommandResultStats.Type.AFFECTED_ITEMS, 1);
    }

    @Override
    public String getCommandName() {
        return "enchant";
    }

    protected String[] getListOfPlayers() {
        return MinecraftServer.getServer().getAllUsernames();
    }
}

