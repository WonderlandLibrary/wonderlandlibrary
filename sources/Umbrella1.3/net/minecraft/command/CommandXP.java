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
import net.minecraft.command.WrongUsageException;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.BlockPos;

public class CommandXP
extends CommandBase {
    private static final String __OBFID = "CL_00000398";

    @Override
    public String getCommandName() {
        return "xp";
    }

    @Override
    public int getRequiredPermissionLevel() {
        return 2;
    }

    @Override
    public String getCommandUsage(ICommandSender sender) {
        return "commands.xp.usage";
    }

    @Override
    public void processCommand(ICommandSender sender, String[] args) throws CommandException {
        EntityPlayerMP var7;
        int var5;
        boolean var6;
        boolean var4;
        if (args.length <= 0) {
            throw new WrongUsageException("commands.xp.usage", new Object[0]);
        }
        String var3 = args[0];
        boolean bl = var4 = var3.endsWith("l") || var3.endsWith("L");
        if (var4 && var3.length() > 1) {
            var3 = var3.substring(0, var3.length() - 1);
        }
        boolean bl2 = var6 = (var5 = CommandXP.parseInt(var3)) < 0;
        if (var6) {
            var5 *= -1;
        }
        EntityPlayerMP entityPlayerMP = var7 = args.length > 1 ? CommandXP.getPlayer(sender, args[1]) : CommandXP.getCommandSenderAsPlayer(sender);
        if (var4) {
            sender.func_174794_a(CommandResultStats.Type.QUERY_RESULT, var7.experienceLevel);
            if (var6) {
                var7.addExperienceLevel(-var5);
                CommandXP.notifyOperators(sender, (ICommand)this, "commands.xp.success.negative.levels", var5, var7.getName());
            } else {
                var7.addExperienceLevel(var5);
                CommandXP.notifyOperators(sender, (ICommand)this, "commands.xp.success.levels", var5, var7.getName());
            }
        } else {
            sender.func_174794_a(CommandResultStats.Type.QUERY_RESULT, var7.experienceTotal);
            if (var6) {
                throw new CommandException("commands.xp.failure.widthdrawXp", new Object[0]);
            }
            var7.addExperience(var5);
            CommandXP.notifyOperators(sender, (ICommand)this, "commands.xp.success", var5, var7.getName());
        }
    }

    @Override
    public List addTabCompletionOptions(ICommandSender sender, String[] args, BlockPos pos) {
        return args.length == 2 ? CommandXP.getListOfStringsMatchingLastWord(args, this.getAllUsernames()) : null;
    }

    protected String[] getAllUsernames() {
        return MinecraftServer.getServer().getAllUsernames();
    }

    @Override
    public boolean isUsernameIndex(String[] args, int index) {
        return index == 1;
    }
}

