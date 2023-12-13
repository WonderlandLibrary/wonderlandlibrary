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
    private static final String __OBFID = "CL_00000377";

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
        NBTTagList var8;
        int var4;
        if (args.length < 2) {
            throw new WrongUsageException("commands.enchant.usage", new Object[0]);
        }
        EntityPlayerMP var3 = CommandEnchant.getPlayer(sender, args[0]);
        sender.func_174794_a(CommandResultStats.Type.AFFECTED_ITEMS, 0);
        try {
            var4 = CommandEnchant.parseInt(args[1], 0);
        }
        catch (NumberInvalidException var12) {
            Enchantment var6 = Enchantment.func_180305_b(args[1]);
            if (var6 == null) {
                throw var12;
            }
            var4 = var6.effectId;
        }
        int var5 = 1;
        ItemStack var13 = var3.getCurrentEquippedItem();
        if (var13 == null) {
            throw new CommandException("commands.enchant.noItem", new Object[0]);
        }
        Enchantment var7 = Enchantment.func_180306_c(var4);
        if (var7 == null) {
            throw new NumberInvalidException("commands.enchant.notFound", var4);
        }
        if (!var7.canApply(var13)) {
            throw new CommandException("commands.enchant.cantEnchant", new Object[0]);
        }
        if (args.length >= 3) {
            var5 = CommandEnchant.parseInt(args[2], var7.getMinLevel(), var7.getMaxLevel());
        }
        if (var13.hasTagCompound() && (var8 = var13.getEnchantmentTagList()) != null) {
            for (int var9 = 0; var9 < var8.tagCount(); ++var9) {
                Enchantment var11;
                short var10 = var8.getCompoundTagAt(var9).getShort("id");
                if (Enchantment.func_180306_c(var10) == null || (var11 = Enchantment.func_180306_c(var10)).canApplyTogether(var7)) continue;
                throw new CommandException("commands.enchant.cantCombine", var7.getTranslatedName(var5), var11.getTranslatedName(var8.getCompoundTagAt(var9).getShort("lvl")));
            }
        }
        var13.addEnchantment(var7, var5);
        CommandEnchant.notifyOperators(sender, (ICommand)this, "commands.enchant.success", new Object[0]);
        sender.func_174794_a(CommandResultStats.Type.AFFECTED_ITEMS, 1);
    }

    @Override
    public List addTabCompletionOptions(ICommandSender sender, String[] args, BlockPos pos) {
        return args.length == 1 ? CommandEnchant.getListOfStringsMatchingLastWord(args, this.getListOfPlayers()) : (args.length == 2 ? CommandEnchant.getListOfStringsMatchingLastWord(args, Enchantment.func_180304_c()) : null);
    }

    protected String[] getListOfPlayers() {
        return MinecraftServer.getServer().getAllUsernames();
    }

    @Override
    public boolean isUsernameIndex(String[] args, int index) {
        return index == 0;
    }
}

