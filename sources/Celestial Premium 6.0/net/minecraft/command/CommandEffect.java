/*
 * Decompiled with CFR 0.150.
 */
package net.minecraft.command;

import java.util.Collections;
import java.util.List;
import javax.annotation.Nullable;
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
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextComponentTranslation;

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

    @Override
    public void execute(MinecraftServer server, ICommandSender sender, String[] args) throws CommandException {
        if (args.length < 2) {
            throw new WrongUsageException("commands.effect.usage", new Object[0]);
        }
        EntityLivingBase entitylivingbase = CommandEffect.getEntity(server, sender, args[0], EntityLivingBase.class);
        if ("clear".equals(args[1])) {
            if (entitylivingbase.getActivePotionEffects().isEmpty()) {
                throw new CommandException("commands.effect.failure.notActive.all", entitylivingbase.getName());
            }
            entitylivingbase.clearActivePotions();
            CommandEffect.notifyCommandListener(sender, (ICommand)this, "commands.effect.success.removed.all", entitylivingbase.getName());
        } else {
            Potion potion;
            try {
                potion = Potion.getPotionById(CommandEffect.parseInt(args[1], 1));
            }
            catch (NumberInvalidException var11) {
                potion = Potion.getPotionFromResourceLocation(args[1]);
            }
            if (potion == null) {
                throw new NumberInvalidException("commands.effect.notFound", args[1]);
            }
            int i = 600;
            int j = 30;
            int k = 0;
            if (args.length >= 3) {
                j = CommandEffect.parseInt(args[2], 0, 1000000);
                i = potion.isInstant() ? j : j * 20;
            } else if (potion.isInstant()) {
                i = 1;
            }
            if (args.length >= 4) {
                k = CommandEffect.parseInt(args[3], 0, 255);
            }
            boolean flag = true;
            if (args.length >= 5 && "true".equalsIgnoreCase(args[4])) {
                flag = false;
            }
            if (j > 0) {
                PotionEffect potioneffect = new PotionEffect(potion, i, k, false, flag);
                entitylivingbase.addPotionEffect(potioneffect);
                CommandEffect.notifyCommandListener(sender, (ICommand)this, "commands.effect.success", new TextComponentTranslation(potioneffect.getEffectName(), new Object[0]), Potion.getIdFromPotion(potion), k, entitylivingbase.getName(), j);
            } else if (entitylivingbase.isPotionActive(potion)) {
                entitylivingbase.removePotionEffect(potion);
                CommandEffect.notifyCommandListener(sender, (ICommand)this, "commands.effect.success.removed", new TextComponentTranslation(potion.getName(), new Object[0]), entitylivingbase.getName());
            } else {
                throw new CommandException("commands.effect.failure.notActive", new TextComponentTranslation(potion.getName(), new Object[0]), entitylivingbase.getName());
            }
        }
    }

    @Override
    public List<String> getTabCompletionOptions(MinecraftServer server, ICommandSender sender, String[] args, @Nullable BlockPos pos) {
        if (args.length == 1) {
            return CommandEffect.getListOfStringsMatchingLastWord(args, server.getAllUsernames());
        }
        if (args.length == 2) {
            return CommandEffect.getListOfStringsMatchingLastWord(args, Potion.REGISTRY.getKeys());
        }
        return args.length == 5 ? CommandEffect.getListOfStringsMatchingLastWord(args, "true", "false") : Collections.emptyList();
    }

    @Override
    public boolean isUsernameIndex(String[] args, int index) {
        return index == 0;
    }
}

