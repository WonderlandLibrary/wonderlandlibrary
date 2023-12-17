// 
// Decompiled by Procyon v0.5.36
// 

package net.minecraft.command;

import java.util.Collections;
import java.util.Collection;
import java.util.List;
import javax.annotation.Nullable;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.ResourceLocation;
import net.minecraft.server.MinecraftServer;

public class CommandFunction extends CommandBase
{
    @Override
    public String getName() {
        return "function";
    }
    
    @Override
    public int getRequiredPermissionLevel() {
        return 2;
    }
    
    @Override
    public String getUsage(final ICommandSender sender) {
        return "commands.function.usage";
    }
    
    @Override
    public void execute(final MinecraftServer server, final ICommandSender sender, final String[] args) throws CommandException {
        if (args.length != 1 && args.length != 3) {
            throw new WrongUsageException("commands.function.usage", new Object[0]);
        }
        final ResourceLocation resourcelocation = new ResourceLocation(args[0]);
        final FunctionObject functionobject = server.getFunctionManager().getFunction(resourcelocation);
        if (functionobject == null) {
            throw new CommandException("commands.function.unknown", new Object[] { resourcelocation });
        }
        if (args.length == 3) {
            final String s = args[1];
            boolean flag;
            if ("if".equals(s)) {
                flag = true;
            }
            else {
                if (!"unless".equals(s)) {
                    throw new WrongUsageException("commands.function.usage", new Object[0]);
                }
                flag = false;
            }
            boolean flag2 = false;
            try {
                flag2 = !CommandBase.getEntityList(server, sender, args[2]).isEmpty();
            }
            catch (EntityNotFoundException ex) {}
            if (flag != flag2) {
                throw new CommandException("commands.function.skipped", new Object[] { resourcelocation });
            }
        }
        final int i = server.getFunctionManager().execute(functionobject, CommandSenderWrapper.create(sender).computePositionVector().withPermissionLevel(2).withSendCommandFeedback(false));
        CommandBase.notifyCommandListener(sender, this, "commands.function.success", resourcelocation, i);
    }
    
    @Override
    public List<String> getTabCompletions(final MinecraftServer server, final ICommandSender sender, final String[] args, @Nullable final BlockPos targetPos) {
        if (args.length == 1) {
            return CommandBase.getListOfStringsMatchingLastWord(args, server.getFunctionManager().getFunctions().keySet());
        }
        if (args.length == 2) {
            return CommandBase.getListOfStringsMatchingLastWord(args, "if", "unless");
        }
        return (args.length == 3) ? CommandBase.getListOfStringsMatchingLastWord(args, server.getOnlinePlayerNames()) : Collections.emptyList();
    }
}
