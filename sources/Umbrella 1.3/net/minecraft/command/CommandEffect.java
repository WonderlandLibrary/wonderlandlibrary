/*
 * Decompiled with CFR 0.150.
 */
package net.minecraft.command;

import java.util.List;
import net.minecraft.command.CommandBase;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommand;
import net.minecraft.command.ICommandSender;
import net.minecraft.command.NumberInvalidException;
import net.minecraft.command.WrongUsageException;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.BlockPos;
import net.minecraft.util.ChatComponentTranslation;

public class CommandEffect
extends CommandBase {
    private static final String __OBFID = "CL_00000323";

    @Override
    public String getCommandName() {
        return "effect";
    }

    @Override
    public int getRequiredPermissionLevel() {
        return 2;
    }

    @Override
    public String getCommandUsage(ICommandSender sender) {
        return "commands.effect.usage";
    }

    /*
     * Enabled force condition propagation
     * Lifted jumps to return sites
     */
    @Override
    public void processCommand(ICommandSender sender, String[] args) throws CommandException {
        int var4;
        if (args.length < 2) {
            throw new WrongUsageException("commands.effect.usage", new Object[0]);
        }
        EntityLivingBase var3 = (EntityLivingBase)CommandEffect.func_175759_a(sender, args[0], EntityLivingBase.class);
        if (args[1].equals("clear")) {
            if (var3.getActivePotionEffects().isEmpty()) {
                throw new CommandException("commands.effect.failure.notActive.all", var3.getName());
            }
            var3.clearActivePotions();
            CommandEffect.notifyOperators(sender, (ICommand)this, "commands.effect.success.removed.all", var3.getName());
            return;
        }
        try {
            var4 = CommandEffect.parseInt(args[1], 1);
        }
        catch (NumberInvalidException var11) {
            Potion var6 = Potion.func_180142_b(args[1]);
            if (var6 == null) {
                throw var11;
            }
            var4 = var6.id;
        }
        int var5 = 600;
        int var12 = 30;
        int var7 = 0;
        if (var4 < 0 || var4 >= Potion.potionTypes.length || Potion.potionTypes[var4] == null) throw new NumberInvalidException("commands.effect.notFound", var4);
        Potion var8 = Potion.potionTypes[var4];
        if (args.length >= 3) {
            var12 = CommandEffect.parseInt(args[2], 0, 1000000);
            var5 = var8.isInstant() ? var12 : var12 * 20;
        } else if (var8.isInstant()) {
            var5 = 1;
        }
        if (args.length >= 4) {
            var7 = CommandEffect.parseInt(args[3], 0, 255);
        }
        boolean var9 = true;
        if (args.length >= 5 && "true".equalsIgnoreCase(args[4])) {
            var9 = false;
        }
        if (var12 > 0) {
            PotionEffect var10 = new PotionEffect(var4, var5, var7, false, var9);
            var3.addPotionEffect(var10);
            CommandEffect.notifyOperators(sender, (ICommand)this, "commands.effect.success", new ChatComponentTranslation(var10.getEffectName(), new Object[0]), var4, var7, var3.getName(), var12);
            return;
        } else {
            if (!var3.isPotionActive(var4)) throw new CommandException("commands.effect.failure.notActive", new ChatComponentTranslation(var8.getName(), new Object[0]), var3.getName());
            var3.removePotionEffect(var4);
            CommandEffect.notifyOperators(sender, (ICommand)this, "commands.effect.success.removed", new ChatComponentTranslation(var8.getName(), new Object[0]), var3.getName());
        }
    }

    @Override
    public List addTabCompletionOptions(ICommandSender sender, String[] args, BlockPos pos) {
        return args.length == 1 ? CommandEffect.getListOfStringsMatchingLastWord(args, this.getAllUsernames()) : (args.length == 2 ? CommandEffect.getListOfStringsMatchingLastWord(args, Potion.func_180141_c()) : (args.length == 5 ? CommandEffect.getListOfStringsMatchingLastWord(args, "true", "false") : null));
    }

    protected String[] getAllUsernames() {
        return MinecraftServer.getServer().getAllUsernames();
    }

    @Override
    public boolean isUsernameIndex(String[] args, int index) {
        return index == 0;
    }
}

