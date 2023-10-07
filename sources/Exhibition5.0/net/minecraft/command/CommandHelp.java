// 
// Decompiled by Procyon v0.5.30
// 

package net.minecraft.command;

import java.util.Set;
import net.minecraft.util.BlockPos;
import java.util.Collections;
import net.minecraft.server.MinecraftServer;
import java.util.Map;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.event.ClickEvent;
import net.minecraft.util.IChatComponent;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.ChatComponentTranslation;
import net.minecraft.util.MathHelper;
import java.util.Arrays;
import java.util.List;

public class CommandHelp extends CommandBase
{
    private static final String __OBFID = "CL_00000529";
    
    @Override
    public String getCommandName() {
        return "help";
    }
    
    @Override
    public int getRequiredPermissionLevel() {
        return 0;
    }
    
    @Override
    public String getCommandUsage(final ICommandSender sender) {
        return "commands.help.usage";
    }
    
    @Override
    public List getCommandAliases() {
        return Arrays.asList("?");
    }
    
    @Override
    public void processCommand(final ICommandSender sender, final String[] args) throws CommandException {
        final List var3 = this.getSortedPossibleCommands(sender);
        final boolean var4 = true;
        final int var5 = (var3.size() - 1) / 7;
        final boolean var6 = false;
        int var7;
        try {
            var7 = ((args.length == 0) ? 0 : (CommandBase.parseInt(args[0], 1, var5 + 1) - 1));
        }
        catch (NumberInvalidException var10) {
            final Map var8 = this.getCommands();
            final ICommand var9 = var8.get(args[0]);
            if (var9 != null) {
                throw new WrongUsageException(var9.getCommandUsage(sender), new Object[0]);
            }
            if (MathHelper.parseIntWithDefault(args[0], -1) != -1) {
                throw var10;
            }
            throw new CommandNotFoundException();
        }
        final int var11 = Math.min((var7 + 1) * 7, var3.size());
        final ChatComponentTranslation var12 = new ChatComponentTranslation("commands.help.header", new Object[] { var7 + 1, var5 + 1 });
        var12.getChatStyle().setColor(EnumChatFormatting.DARK_GREEN);
        sender.addChatMessage(var12);
        for (int var13 = var7 * 7; var13 < var11; ++var13) {
            final ICommand var14 = var3.get(var13);
            final ChatComponentTranslation var15 = new ChatComponentTranslation(var14.getCommandUsage(sender), new Object[0]);
            var15.getChatStyle().setChatClickEvent(new ClickEvent(ClickEvent.Action.SUGGEST_COMMAND, "/" + var14.getCommandName() + " "));
            sender.addChatMessage(var15);
        }
        if (var7 == 0 && sender instanceof EntityPlayer) {
            final ChatComponentTranslation var16 = new ChatComponentTranslation("commands.help.footer", new Object[0]);
            var16.getChatStyle().setColor(EnumChatFormatting.GREEN);
            sender.addChatMessage(var16);
        }
    }
    
    protected List getSortedPossibleCommands(final ICommandSender p_71534_1_) {
        final List var2 = MinecraftServer.getServer().getCommandManager().getPossibleCommands(p_71534_1_);
        Collections.sort((List<Comparable>)var2);
        return var2;
    }
    
    protected Map getCommands() {
        return MinecraftServer.getServer().getCommandManager().getCommands();
    }
    
    @Override
    public List addTabCompletionOptions(final ICommandSender sender, final String[] args, final BlockPos pos) {
        if (args.length == 1) {
            final Set var4 = this.getCommands().keySet();
            return CommandBase.getListOfStringsMatchingLastWord(args, (String[])var4.toArray(new String[var4.size()]));
        }
        return null;
    }
}
