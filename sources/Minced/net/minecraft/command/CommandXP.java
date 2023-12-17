// 
// Decompiled by Procyon v0.5.36
// 

package net.minecraft.command;

import java.util.Collections;
import java.util.List;
import javax.annotation.Nullable;
import net.minecraft.util.math.BlockPos;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.server.MinecraftServer;

public class CommandXP extends CommandBase
{
    @Override
    public String getName() {
        return "xp";
    }
    
    @Override
    public int getRequiredPermissionLevel() {
        return 2;
    }
    
    @Override
    public String getUsage(final ICommandSender sender) {
        return "commands.xp.usage";
    }
    
    @Override
    public void execute(final MinecraftServer server, final ICommandSender sender, final String[] args) throws CommandException {
        if (args.length <= 0) {
            throw new WrongUsageException("commands.xp.usage", new Object[0]);
        }
        String s = args[0];
        final boolean flag = s.endsWith("l") || s.endsWith("L");
        if (flag && s.length() > 1) {
            s = s.substring(0, s.length() - 1);
        }
        int i = CommandBase.parseInt(s);
        final boolean flag2 = i < 0;
        if (flag2) {
            i *= -1;
        }
        final EntityPlayer entityplayer = (args.length > 1) ? CommandBase.getPlayer(server, sender, args[1]) : CommandBase.getCommandSenderAsPlayer(sender);
        if (flag) {
            sender.setCommandStat(CommandResultStats.Type.QUERY_RESULT, entityplayer.experienceLevel);
            if (flag2) {
                entityplayer.addExperienceLevel(-i);
                CommandBase.notifyCommandListener(sender, this, "commands.xp.success.negative.levels", i, entityplayer.getName());
            }
            else {
                entityplayer.addExperienceLevel(i);
                CommandBase.notifyCommandListener(sender, this, "commands.xp.success.levels", i, entityplayer.getName());
            }
        }
        else {
            sender.setCommandStat(CommandResultStats.Type.QUERY_RESULT, entityplayer.experienceTotal);
            if (flag2) {
                throw new CommandException("commands.xp.failure.widthdrawXp", new Object[0]);
            }
            entityplayer.addExperience(i);
            CommandBase.notifyCommandListener(sender, this, "commands.xp.success", i, entityplayer.getName());
        }
    }
    
    @Override
    public List<String> getTabCompletions(final MinecraftServer server, final ICommandSender sender, final String[] args, @Nullable final BlockPos targetPos) {
        return (args.length == 2) ? CommandBase.getListOfStringsMatchingLastWord(args, server.getOnlinePlayerNames()) : Collections.emptyList();
    }
    
    @Override
    public boolean isUsernameIndex(final String[] args, final int index) {
        return index == 1;
    }
}
