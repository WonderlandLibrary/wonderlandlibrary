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
    public String getCommandName() {
        return "xp";
    }

    public int getRequiredPermissionLevel() {
        return 2;
    }

    public String getCommandUsage(ICommandSender sender) {
        return "commands.xp.usage";
    }

    public void processCommand(ICommandSender sender, String[] args) throws CommandException {
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
        boolean bl2 = flag1 = (i = CommandXP.parseInt((String)s)) < 0;
        if (flag1) {
            i *= -1;
        }
        EntityPlayerMP entityPlayerMP = entityplayer = args.length > 1 ? CommandXP.getPlayer((ICommandSender)sender, (String)args[1]) : CommandXP.getCommandSenderAsPlayer((ICommandSender)sender);
        if (flag) {
            sender.setCommandStat(CommandResultStats.Type.QUERY_RESULT, entityplayer.experienceLevel);
            if (flag1) {
                entityplayer.addExperienceLevel(-i);
                CommandXP.notifyOperators((ICommandSender)sender, (ICommand)this, (String)"commands.xp.success.negative.levels", (Object[])new Object[]{i, entityplayer.getName()});
            } else {
                entityplayer.addExperienceLevel(i);
                CommandXP.notifyOperators((ICommandSender)sender, (ICommand)this, (String)"commands.xp.success.levels", (Object[])new Object[]{i, entityplayer.getName()});
            }
        } else {
            sender.setCommandStat(CommandResultStats.Type.QUERY_RESULT, entityplayer.experienceTotal);
            if (flag1) {
                throw new CommandException("commands.xp.failure.widthdrawXp", new Object[0]);
            }
            entityplayer.addExperience(i);
            CommandXP.notifyOperators((ICommandSender)sender, (ICommand)this, (String)"commands.xp.success", (Object[])new Object[]{i, entityplayer.getName()});
        }
    }

    public List<String> addTabCompletionOptions(ICommandSender sender, String[] args, BlockPos pos) {
        return args.length == 2 ? CommandXP.getListOfStringsMatchingLastWord((String[])args, (String[])this.getAllUsernames()) : null;
    }

    protected String[] getAllUsernames() {
        return MinecraftServer.getServer().getAllUsernames();
    }

    public boolean isUsernameIndex(String[] args, int index) {
        return index == 1;
    }
}
