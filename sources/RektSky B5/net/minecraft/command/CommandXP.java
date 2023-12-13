/*
 * Decompiled with CFR 0.152.
 */
package net.minecraft.command;

import java.util.List;
import net.minecraft.command.CommandBase;
import net.minecraft.command.CommandException;
import net.minecraft.command.CommandResultStats;
import net.minecraft.command.ICommand;
import net.minecraft.command.ICommandSender;
import net.minecraft.command.WrongUsageException;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.BlockPos;

public class CommandXP
extends CommandBase {
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
        EntityPlayerMP entityplayer;
        int i2;
        boolean flag1;
        boolean flag;
        if (args.length <= 0) {
            throw new WrongUsageException("commands.xp.usage", new Object[0]);
        }
        String s2 = args[0];
        boolean bl = flag = s2.endsWith("l") || s2.endsWith("L");
        if (flag && s2.length() > 1) {
            s2 = s2.substring(0, s2.length() - 1);
        }
        boolean bl2 = flag1 = (i2 = CommandXP.parseInt(s2)) < 0;
        if (flag1) {
            i2 *= -1;
        }
        EntityPlayerMP entityPlayerMP = entityplayer = args.length > 1 ? CommandXP.getPlayer(sender, args[1]) : CommandXP.getCommandSenderAsPlayer(sender);
        if (flag) {
            sender.setCommandStat(CommandResultStats.Type.QUERY_RESULT, entityplayer.experienceLevel);
            if (flag1) {
                ((EntityPlayer)entityplayer).addExperienceLevel(-i2);
                CommandXP.notifyOperators(sender, (ICommand)this, "commands.xp.success.negative.levels", i2, entityplayer.getName());
            } else {
                ((EntityPlayer)entityplayer).addExperienceLevel(i2);
                CommandXP.notifyOperators(sender, (ICommand)this, "commands.xp.success.levels", i2, entityplayer.getName());
            }
        } else {
            sender.setCommandStat(CommandResultStats.Type.QUERY_RESULT, entityplayer.experienceTotal);
            if (flag1) {
                throw new CommandException("commands.xp.failure.widthdrawXp", new Object[0]);
            }
            entityplayer.addExperience(i2);
            CommandXP.notifyOperators(sender, (ICommand)this, "commands.xp.success", i2, entityplayer.getName());
        }
    }

    @Override
    public List<String> addTabCompletionOptions(ICommandSender sender, String[] args, BlockPos pos) {
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

