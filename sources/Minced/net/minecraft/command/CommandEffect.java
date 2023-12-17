// 
// Decompiled by Procyon v0.5.36
// 

package net.minecraft.command;

import java.util.Collections;
import java.util.Collection;
import java.util.List;
import javax.annotation.Nullable;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraft.potion.PotionEffect;
import net.minecraft.potion.Potion;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.server.MinecraftServer;

public class CommandEffect extends CommandBase
{
    @Override
    public String getName() {
        return "effect";
    }
    
    @Override
    public int getRequiredPermissionLevel() {
        return 2;
    }
    
    @Override
    public String getUsage(final ICommandSender sender) {
        return "commands.effect.usage";
    }
    
    @Override
    public void execute(final MinecraftServer server, final ICommandSender sender, final String[] args) throws CommandException {
        if (args.length < 2) {
            throw new WrongUsageException("commands.effect.usage", new Object[0]);
        }
        final EntityLivingBase entitylivingbase = CommandBase.getEntity(server, sender, args[0], (Class<? extends EntityLivingBase>)EntityLivingBase.class);
        if ("clear".equals(args[1])) {
            if (entitylivingbase.getActivePotionEffects().isEmpty()) {
                throw new CommandException("commands.effect.failure.notActive.all", new Object[] { entitylivingbase.getName() });
            }
            entitylivingbase.clearActivePotions();
            CommandBase.notifyCommandListener(sender, this, "commands.effect.success.removed.all", entitylivingbase.getName());
        }
        else {
            Potion potion;
            try {
                potion = Potion.getPotionById(CommandBase.parseInt(args[1], 1));
            }
            catch (NumberInvalidException var11) {
                potion = Potion.getPotionFromResourceLocation(args[1]);
            }
            if (potion == null) {
                throw new NumberInvalidException("commands.effect.notFound", new Object[] { args[1] });
            }
            int i = 600;
            int j = 30;
            int k = 0;
            if (args.length >= 3) {
                j = CommandBase.parseInt(args[2], 0, 1000000);
                if (potion.isInstant()) {
                    i = j;
                }
                else {
                    i = j * 20;
                }
            }
            else if (potion.isInstant()) {
                i = 1;
            }
            if (args.length >= 4) {
                k = CommandBase.parseInt(args[3], 0, 255);
            }
            boolean flag = true;
            if (args.length >= 5 && "true".equalsIgnoreCase(args[4])) {
                flag = false;
            }
            if (j > 0) {
                final PotionEffect potioneffect = new PotionEffect(potion, i, k, false, flag);
                entitylivingbase.addPotionEffect(potioneffect);
                CommandBase.notifyCommandListener(sender, this, "commands.effect.success", new TextComponentTranslation(potioneffect.getEffectName(), new Object[0]), Potion.getIdFromPotion(potion), k, entitylivingbase.getName(), j);
            }
            else {
                if (!entitylivingbase.isPotionActive(potion)) {
                    throw new CommandException("commands.effect.failure.notActive", new Object[] { new TextComponentTranslation(potion.getName(), new Object[0]), entitylivingbase.getName() });
                }
                entitylivingbase.removePotionEffect(potion);
                CommandBase.notifyCommandListener(sender, this, "commands.effect.success.removed", new TextComponentTranslation(potion.getName(), new Object[0]), entitylivingbase.getName());
            }
        }
    }
    
    @Override
    public List<String> getTabCompletions(final MinecraftServer server, final ICommandSender sender, final String[] args, @Nullable final BlockPos targetPos) {
        if (args.length == 1) {
            return CommandBase.getListOfStringsMatchingLastWord(args, server.getOnlinePlayerNames());
        }
        if (args.length == 2) {
            return CommandBase.getListOfStringsMatchingLastWord(args, Potion.REGISTRY.getKeys());
        }
        return (args.length == 5) ? CommandBase.getListOfStringsMatchingLastWord(args, "true", "false") : Collections.emptyList();
    }
    
    @Override
    public boolean isUsernameIndex(final String[] args, final int index) {
        return index == 0;
    }
}
