// 
// Decompiled by Procyon v0.5.30
// 

package net.minecraft.command;

import net.minecraft.server.MinecraftServer;
import java.util.List;
import net.minecraft.util.BlockPos;
import net.minecraft.util.ChatComponentTranslation;
import net.minecraft.potion.PotionEffect;
import net.minecraft.potion.Potion;
import net.minecraft.entity.EntityLivingBase;

public class CommandEffect extends CommandBase
{
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
    public String getCommandUsage(final ICommandSender sender) {
        return "commands.effect.usage";
    }
    
    @Override
    public void processCommand(final ICommandSender sender, final String[] args) throws CommandException {
        if (args.length < 2) {
            throw new WrongUsageException("commands.effect.usage", new Object[0]);
        }
        final EntityLivingBase var3 = (EntityLivingBase)CommandBase.func_175759_a(sender, args[0], EntityLivingBase.class);
        if (args[1].equals("clear")) {
            if (var3.getActivePotionEffects().isEmpty()) {
                throw new CommandException("commands.effect.failure.notActive.all", new Object[] { var3.getName() });
            }
            var3.clearActivePotions();
            CommandBase.notifyOperators(sender, this, "commands.effect.success.removed.all", var3.getName());
        }
        else {
            int var4;
            try {
                var4 = CommandBase.parseInt(args[1], 1);
            }
            catch (NumberInvalidException var6) {
                final Potion var5 = Potion.func_180142_b(args[1]);
                if (var5 == null) {
                    throw var6;
                }
                var4 = var5.id;
            }
            int var7 = 600;
            int var8 = 30;
            int var9 = 0;
            if (var4 < 0 || var4 >= Potion.potionTypes.length || Potion.potionTypes[var4] == null) {
                throw new NumberInvalidException("commands.effect.notFound", new Object[] { var4 });
            }
            final Potion var10 = Potion.potionTypes[var4];
            if (args.length >= 3) {
                var8 = CommandBase.parseInt(args[2], 0, 1000000);
                if (var10.isInstant()) {
                    var7 = var8;
                }
                else {
                    var7 = var8 * 20;
                }
            }
            else if (var10.isInstant()) {
                var7 = 1;
            }
            if (args.length >= 4) {
                var9 = CommandBase.parseInt(args[3], 0, 255);
            }
            boolean var11 = true;
            if (args.length >= 5 && "true".equalsIgnoreCase(args[4])) {
                var11 = false;
            }
            if (var8 > 0) {
                final PotionEffect var12 = new PotionEffect(var4, var7, var9, false, var11);
                var3.addPotionEffect(var12);
                CommandBase.notifyOperators(sender, this, "commands.effect.success", new ChatComponentTranslation(var12.getEffectName(), new Object[0]), var4, var9, var3.getName(), var8);
            }
            else {
                if (!var3.isPotionActive(var4)) {
                    throw new CommandException("commands.effect.failure.notActive", new Object[] { new ChatComponentTranslation(var10.getName(), new Object[0]), var3.getName() });
                }
                var3.removePotionEffect(var4);
                CommandBase.notifyOperators(sender, this, "commands.effect.success.removed", new ChatComponentTranslation(var10.getName(), new Object[0]), var3.getName());
            }
        }
    }
    
    @Override
    public List addTabCompletionOptions(final ICommandSender sender, final String[] args, final BlockPos pos) {
        return (args.length == 1) ? CommandBase.getListOfStringsMatchingLastWord(args, this.getAllUsernames()) : ((args.length == 2) ? CommandBase.getListOfStringsMatchingLastWord(args, Potion.func_180141_c()) : ((args.length == 5) ? CommandBase.getListOfStringsMatchingLastWord(args, "true", "false") : null));
    }
    
    protected String[] getAllUsernames() {
        return MinecraftServer.getServer().getAllUsernames();
    }
    
    @Override
    public boolean isUsernameIndex(final String[] args, final int index) {
        return index == 0;
    }
}
