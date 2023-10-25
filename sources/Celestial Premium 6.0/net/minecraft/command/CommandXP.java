/*
 * Decompiled with CFR 0.150.
 */
package net.minecraft.command;

import java.util.Collections;
import java.util.List;
import javax.annotation.Nullable;
import net.minecraft.command.CommandBase;
import net.minecraft.command.CommandException;
import net.minecraft.command.CommandResultStats;
import net.minecraft.command.ICommand;
import net.minecraft.command.ICommandSender;
import net.minecraft.command.WrongUsageException;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.math.BlockPos;

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
    public void execute(MinecraftServer server, ICommandSender sender, String[] args) throws CommandException {
        EntityPlayerMP entityplayer;
        int i;
        boolean flag1;
        boolean flag;
        if (args.length <= 0) {
            throw new WrongUsageException("commands.xp.usage", new Object[0]);
        }
        String s = args[0];
        boolean bl = flag = s.endsWith("l") || s.endsWith("L");
        if (flag && s.length() > 1) {
            s = s.substring(0, s.length() - 1);
        }
        boolean bl2 = flag1 = (i = CommandXP.parseInt(s)) < 0;
        if (flag1) {
            i *= -1;
        }
        EntityPlayerMP entityPlayerMP = entityplayer = args.length > 1 ? CommandXP.getPlayer(server, sender, args[1]) : CommandXP.getCommandSenderAsPlayer(sender);
        if (flag) {
            sender.setCommandStat(CommandResultStats.Type.QUERY_RESULT, entityplayer.experienceLevel);
            if (flag1) {
                ((EntityPlayer)entityplayer).addExperienceLevel(-i);
                CommandXP.notifyCommandListener(sender, (ICommand)this, "commands.xp.success.negative.levels", i, entityplayer.getName());
            } else {
                ((EntityPlayer)entityplayer).addExperienceLevel(i);
                CommandXP.notifyCommandListener(sender, (ICommand)this, "commands.xp.success.levels", i, entityplayer.getName());
            }
        } else {
            sender.setCommandStat(CommandResultStats.Type.QUERY_RESULT, entityplayer.experienceTotal);
            if (flag1) {
                throw new CommandException("commands.xp.failure.widthdrawXp", new Object[0]);
            }
            entityplayer.addExperience(i);
            CommandXP.notifyCommandListener(sender, (ICommand)this, "commands.xp.success", i, entityplayer.getName());
        }
    }

    @Override
    public List<String> getTabCompletionOptions(MinecraftServer server, ICommandSender sender, String[] args, @Nullable BlockPos pos) {
        return args.length == 2 ? CommandXP.getListOfStringsMatchingLastWord(args, server.getAllUsernames()) : Collections.emptyList();
    }

    @Override
    public boolean isUsernameIndex(String[] args, int index) {
        return index == 1;
    }
}

