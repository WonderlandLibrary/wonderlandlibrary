/*
 * Decompiled with CFR 0.152.
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
        int i2;
        if (args.length < 2) {
            throw new WrongUsageException("commands.effect.usage", new Object[0]);
        }
        EntityLivingBase entitylivingbase = CommandEffect.getEntity(sender, args[0], EntityLivingBase.class);
        if (args[1].equals("clear")) {
            if (entitylivingbase.getActivePotionEffects().isEmpty()) {
                throw new CommandException("commands.effect.failure.notActive.all", entitylivingbase.getName());
            }
            entitylivingbase.clearActivePotions();
            CommandEffect.notifyOperators(sender, (ICommand)this, "commands.effect.success.removed.all", entitylivingbase.getName());
            return;
        }
        try {
            i2 = CommandEffect.parseInt(args[1], 1);
        }
        catch (NumberInvalidException numberinvalidexception) {
            Potion potion = Potion.getPotionFromResourceLocation(args[1]);
            if (potion == null) {
                throw numberinvalidexception;
            }
            i2 = potion.id;
        }
        int j2 = 600;
        int l2 = 30;
        int k2 = 0;
        if (i2 < 0 || i2 >= Potion.potionTypes.length || Potion.potionTypes[i2] == null) throw new NumberInvalidException("commands.effect.notFound", i2);
        Potion potion1 = Potion.potionTypes[i2];
        if (args.length >= 3) {
            l2 = CommandEffect.parseInt(args[2], 0, 1000000);
            j2 = potion1.isInstant() ? l2 : l2 * 20;
        } else if (potion1.isInstant()) {
            j2 = 1;
        }
        if (args.length >= 4) {
            k2 = CommandEffect.parseInt(args[3], 0, 255);
        }
        boolean flag = true;
        if (args.length >= 5 && "true".equalsIgnoreCase(args[4])) {
            flag = false;
        }
        if (l2 > 0) {
            PotionEffect potioneffect = new PotionEffect(i2, j2, k2, false, flag);
            entitylivingbase.addPotionEffect(potioneffect);
            CommandEffect.notifyOperators(sender, (ICommand)this, "commands.effect.success", new ChatComponentTranslation(potioneffect.getEffectName(), new Object[0]), i2, k2, entitylivingbase.getName(), l2);
            return;
        } else {
            if (!entitylivingbase.isPotionActive(i2)) throw new CommandException("commands.effect.failure.notActive", new ChatComponentTranslation(potion1.getName(), new Object[0]), entitylivingbase.getName());
            entitylivingbase.removePotionEffect(i2);
            CommandEffect.notifyOperators(sender, (ICommand)this, "commands.effect.success.removed", new ChatComponentTranslation(potion1.getName(), new Object[0]), entitylivingbase.getName());
        }
    }

    @Override
    public List<String> addTabCompletionOptions(ICommandSender sender, String[] args, BlockPos pos) {
        return args.length == 1 ? CommandEffect.getListOfStringsMatchingLastWord(args, this.getAllUsernames()) : (args.length == 2 ? CommandEffect.getListOfStringsMatchingLastWord(args, Potion.func_181168_c()) : (args.length == 5 ? CommandEffect.getListOfStringsMatchingLastWord(args, "true", "false") : null));
    }

    protected String[] getAllUsernames() {
        return MinecraftServer.getServer().getAllUsernames();
    }

    @Override
    public boolean isUsernameIndex(String[] args, int index) {
        return index == 0;
    }
}

