// 
// Decompiled by Procyon v0.5.36
// 

package net.minecraft.command;

import java.util.Collections;
import java.util.List;
import javax.annotation.Nullable;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraft.server.MinecraftServer;

public class CommandLocate extends CommandBase
{
    @Override
    public String getName() {
        return "locate";
    }
    
    @Override
    public int getRequiredPermissionLevel() {
        return 2;
    }
    
    @Override
    public String getUsage(final ICommandSender sender) {
        return "commands.locate.usage";
    }
    
    @Override
    public void execute(final MinecraftServer server, final ICommandSender sender, final String[] args) throws CommandException {
        if (args.length != 1) {
            throw new WrongUsageException("commands.locate.usage", new Object[0]);
        }
        final String s = args[0];
        final BlockPos blockpos = sender.getEntityWorld().findNearestStructure(s, sender.getPosition(), false);
        if (blockpos != null) {
            sender.sendMessage(new TextComponentTranslation("commands.locate.success", new Object[] { s, blockpos.getX(), blockpos.getZ() }));
            return;
        }
        throw new CommandException("commands.locate.failure", new Object[] { s });
    }
    
    @Override
    public List<String> getTabCompletions(final MinecraftServer server, final ICommandSender sender, final String[] args, @Nullable final BlockPos targetPos) {
        return (args.length == 1) ? CommandBase.getListOfStringsMatchingLastWord(args, "Stronghold", "Monument", "Village", "Mansion", "EndCity", "Fortress", "Temple", "Mineshaft") : Collections.emptyList();
    }
}
