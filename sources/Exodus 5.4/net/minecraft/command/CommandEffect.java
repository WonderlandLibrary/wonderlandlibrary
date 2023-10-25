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
    protected String[] getAllUsernames() {
        return MinecraftServer.getServer().getAllUsernames();
    }

    @Override
    public int getRequiredPermissionLevel() {
        return 2;
    }

    @Override
    public String getCommandName() {
        return "effect";
    }

    @Override
    public boolean isUsernameIndex(String[] stringArray, int n) {
        return n == 0;
    }

    @Override
    public List<String> addTabCompletionOptions(ICommandSender iCommandSender, String[] stringArray, BlockPos blockPos) {
        return stringArray.length == 1 ? CommandEffect.getListOfStringsMatchingLastWord(stringArray, this.getAllUsernames()) : (stringArray.length == 2 ? CommandEffect.getListOfStringsMatchingLastWord(stringArray, Potion.func_181168_c()) : (stringArray.length == 5 ? CommandEffect.getListOfStringsMatchingLastWord(stringArray, "true", "false") : null));
    }

    @Override
    public String getCommandUsage(ICommandSender iCommandSender) {
        return "commands.effect.usage";
    }

    /*
     * Enabled force condition propagation
     * Lifted jumps to return sites
     */
    @Override
    public void processCommand(ICommandSender iCommandSender, String[] stringArray) throws CommandException {
        int n;
        if (stringArray.length < 2) {
            throw new WrongUsageException("commands.effect.usage", new Object[0]);
        }
        EntityLivingBase entityLivingBase = CommandEffect.getEntity(iCommandSender, stringArray[0], EntityLivingBase.class);
        if (stringArray[1].equals("clear")) {
            if (entityLivingBase.getActivePotionEffects().isEmpty()) {
                throw new CommandException("commands.effect.failure.notActive.all", entityLivingBase.getName());
            }
            entityLivingBase.clearActivePotions();
            CommandEffect.notifyOperators(iCommandSender, (ICommand)this, "commands.effect.success.removed.all", entityLivingBase.getName());
            return;
        }
        try {
            n = CommandEffect.parseInt(stringArray[1], 1);
        }
        catch (NumberInvalidException numberInvalidException) {
            Potion potion = Potion.getPotionFromResourceLocation(stringArray[1]);
            if (potion == null) {
                throw numberInvalidException;
            }
            n = potion.id;
        }
        int n2 = 600;
        int n3 = 30;
        int n4 = 0;
        if (n < 0 || n >= Potion.potionTypes.length || Potion.potionTypes[n] == null) throw new NumberInvalidException("commands.effect.notFound", n);
        Potion potion = Potion.potionTypes[n];
        if (stringArray.length >= 3) {
            n3 = CommandEffect.parseInt(stringArray[2], 0, 1000000);
            n2 = potion.isInstant() ? n3 : n3 * 20;
        } else if (potion.isInstant()) {
            n2 = 1;
        }
        if (stringArray.length >= 4) {
            n4 = CommandEffect.parseInt(stringArray[3], 0, 255);
        }
        boolean bl = true;
        if (stringArray.length >= 5 && "true".equalsIgnoreCase(stringArray[4])) {
            bl = false;
        }
        if (n3 > 0) {
            PotionEffect potionEffect = new PotionEffect(n, n2, n4, false, bl);
            entityLivingBase.addPotionEffect(potionEffect);
            CommandEffect.notifyOperators(iCommandSender, (ICommand)this, "commands.effect.success", new ChatComponentTranslation(potionEffect.getEffectName(), new Object[0]), n, n4, entityLivingBase.getName(), n3);
            return;
        } else {
            if (!entityLivingBase.isPotionActive(n)) throw new CommandException("commands.effect.failure.notActive", new ChatComponentTranslation(potion.getName(), new Object[0]), entityLivingBase.getName());
            entityLivingBase.removePotionEffect(n);
            CommandEffect.notifyOperators(iCommandSender, (ICommand)this, "commands.effect.success.removed", new ChatComponentTranslation(potion.getName(), new Object[0]), entityLivingBase.getName());
        }
    }
}

