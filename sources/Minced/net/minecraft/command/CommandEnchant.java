// 
// Decompiled by Procyon v0.5.36
// 

package net.minecraft.command;

import java.util.Collections;
import java.util.Collection;
import java.util.List;
import javax.annotation.Nullable;
import net.minecraft.util.math.BlockPos;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.item.ItemStack;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.server.MinecraftServer;

public class CommandEnchant extends CommandBase
{
    @Override
    public String getName() {
        return "enchant";
    }
    
    @Override
    public int getRequiredPermissionLevel() {
        return 2;
    }
    
    @Override
    public String getUsage(final ICommandSender sender) {
        return "commands.enchant.usage";
    }
    
    @Override
    public void execute(final MinecraftServer server, final ICommandSender sender, final String[] args) throws CommandException {
        if (args.length < 2) {
            throw new WrongUsageException("commands.enchant.usage", new Object[0]);
        }
        final EntityLivingBase entitylivingbase = CommandBase.getEntity(server, sender, args[0], (Class<? extends EntityLivingBase>)EntityLivingBase.class);
        sender.setCommandStat(CommandResultStats.Type.AFFECTED_ITEMS, 0);
        Enchantment enchantment;
        try {
            enchantment = Enchantment.getEnchantmentByID(CommandBase.parseInt(args[1], 0));
        }
        catch (NumberInvalidException var12) {
            enchantment = Enchantment.getEnchantmentByLocation(args[1]);
        }
        if (enchantment == null) {
            throw new NumberInvalidException("commands.enchant.notFound", new Object[] { args[1] });
        }
        int i = 1;
        final ItemStack itemstack = entitylivingbase.getHeldItemMainhand();
        if (itemstack.isEmpty()) {
            throw new CommandException("commands.enchant.noItem", new Object[0]);
        }
        if (!enchantment.canApply(itemstack)) {
            throw new CommandException("commands.enchant.cantEnchant", new Object[0]);
        }
        if (args.length >= 3) {
            i = CommandBase.parseInt(args[2], enchantment.getMinLevel(), enchantment.getMaxLevel());
        }
        if (itemstack.hasTagCompound()) {
            final NBTTagList nbttaglist = itemstack.getEnchantmentTagList();
            for (int j = 0; j < nbttaglist.tagCount(); ++j) {
                final int k = nbttaglist.getCompoundTagAt(j).getShort("id");
                if (Enchantment.getEnchantmentByID(k) != null) {
                    final Enchantment enchantment2 = Enchantment.getEnchantmentByID(k);
                    if (!enchantment.isCompatibleWith(enchantment2)) {
                        throw new CommandException("commands.enchant.cantCombine", new Object[] { enchantment.getTranslatedName(i), enchantment2.getTranslatedName(nbttaglist.getCompoundTagAt(j).getShort("lvl")) });
                    }
                }
            }
        }
        itemstack.addEnchantment(enchantment, i);
        CommandBase.notifyCommandListener(sender, this, "commands.enchant.success", new Object[0]);
        sender.setCommandStat(CommandResultStats.Type.AFFECTED_ITEMS, 1);
    }
    
    @Override
    public List<String> getTabCompletions(final MinecraftServer server, final ICommandSender sender, final String[] args, @Nullable final BlockPos targetPos) {
        if (args.length == 1) {
            return CommandBase.getListOfStringsMatchingLastWord(args, server.getOnlinePlayerNames());
        }
        return (args.length == 2) ? CommandBase.getListOfStringsMatchingLastWord(args, Enchantment.REGISTRY.getKeys()) : Collections.emptyList();
    }
    
    @Override
    public boolean isUsernameIndex(final String[] args, final int index) {
        return index == 0;
    }
}
