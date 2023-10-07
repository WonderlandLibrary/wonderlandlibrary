// 
// Decompiled by Procyon v0.5.30
// 

package net.minecraft.command;

import net.minecraft.server.MinecraftServer;
import java.util.List;
import net.minecraft.util.BlockPos;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.item.ItemStack;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.enchantment.Enchantment;

public class CommandEnchant extends CommandBase
{
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
    public String getCommandUsage(final ICommandSender sender) {
        return "commands.enchant.usage";
    }
    
    @Override
    public void processCommand(final ICommandSender sender, final String[] args) throws CommandException {
        if (args.length < 2) {
            throw new WrongUsageException("commands.enchant.usage", new Object[0]);
        }
        final EntityPlayerMP var3 = CommandBase.getPlayer(sender, args[0]);
        sender.func_174794_a(CommandResultStats.Type.AFFECTED_ITEMS, 0);
        int var4;
        try {
            var4 = CommandBase.parseInt(args[1], 0);
        }
        catch (NumberInvalidException var6) {
            final Enchantment var5 = Enchantment.func_180305_b(args[1]);
            if (var5 == null) {
                throw var6;
            }
            var4 = var5.effectId;
        }
        int var7 = 1;
        final ItemStack var8 = var3.getCurrentEquippedItem();
        if (var8 == null) {
            throw new CommandException("commands.enchant.noItem", new Object[0]);
        }
        final Enchantment var9 = Enchantment.func_180306_c(var4);
        if (var9 == null) {
            throw new NumberInvalidException("commands.enchant.notFound", new Object[] { var4 });
        }
        if (!var9.canApply(var8)) {
            throw new CommandException("commands.enchant.cantEnchant", new Object[0]);
        }
        if (args.length >= 3) {
            var7 = CommandBase.parseInt(args[2], var9.getMinLevel(), var9.getMaxLevel());
        }
        if (var8.hasTagCompound()) {
            final NBTTagList var10 = var8.getEnchantmentTagList();
            if (var10 != null) {
                for (int var11 = 0; var11 < var10.tagCount(); ++var11) {
                    final short var12 = var10.getCompoundTagAt(var11).getShort("id");
                    if (Enchantment.func_180306_c(var12) != null) {
                        final Enchantment var13 = Enchantment.func_180306_c(var12);
                        if (!var13.canApplyTogether(var9)) {
                            throw new CommandException("commands.enchant.cantCombine", new Object[] { var9.getTranslatedName(var7), var13.getTranslatedName(var10.getCompoundTagAt(var11).getShort("lvl")) });
                        }
                    }
                }
            }
        }
        var8.addEnchantment(var9, var7);
        CommandBase.notifyOperators(sender, this, "commands.enchant.success", new Object[0]);
        sender.func_174794_a(CommandResultStats.Type.AFFECTED_ITEMS, 1);
    }
    
    @Override
    public List addTabCompletionOptions(final ICommandSender sender, final String[] args, final BlockPos pos) {
        return (args.length == 1) ? CommandBase.getListOfStringsMatchingLastWord(args, this.getListOfPlayers()) : ((args.length == 2) ? CommandBase.getListOfStringsMatchingLastWord(args, Enchantment.func_180304_c()) : null);
    }
    
    protected String[] getListOfPlayers() {
        return MinecraftServer.getServer().getAllUsernames();
    }
    
    @Override
    public boolean isUsernameIndex(final String[] args, final int index) {
        return index == 0;
    }
}
