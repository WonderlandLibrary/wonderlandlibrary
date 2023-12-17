// 
// Decompiled by Procyon v0.5.36
// 

package net.minecraft.command;

import java.util.Set;
import javax.annotation.Nullable;
import net.minecraft.util.math.BlockPos;
import java.util.Collections;
import java.util.Map;
import net.minecraft.util.text.event.ClickEvent;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.tileentity.CommandBlockBaseLogic;
import net.minecraft.server.MinecraftServer;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

public class CommandHelp extends CommandBase
{
    private static final String[] SEARGE_SAYS;
    private final Random rand;
    
    public CommandHelp() {
        this.rand = new Random();
    }
    
    @Override
    public String getName() {
        return "help";
    }
    
    @Override
    public int getRequiredPermissionLevel() {
        return 0;
    }
    
    @Override
    public String getUsage(final ICommandSender sender) {
        return "commands.help.usage";
    }
    
    @Override
    public List<String> getAliases() {
        return Arrays.asList("?");
    }
    
    @Override
    public void execute(final MinecraftServer server, final ICommandSender sender, final String[] args) throws CommandException {
        if (sender instanceof CommandBlockBaseLogic) {
            sender.sendMessage(new TextComponentString("Searge says: ").appendText(CommandHelp.SEARGE_SAYS[this.rand.nextInt(CommandHelp.SEARGE_SAYS.length) % CommandHelp.SEARGE_SAYS.length]));
        }
        else {
            final List<ICommand> list = this.getSortedPossibleCommands(sender, server);
            final int i = 7;
            final int j = (list.size() - 1) / 7;
            int k = 0;
            try {
                k = ((args.length == 0) ? 0 : (CommandBase.parseInt(args[0], 1, j + 1) - 1));
            }
            catch (NumberInvalidException numberinvalidexception) {
                final Map<String, ICommand> map = this.getCommandMap(server);
                final ICommand icommand = map.get(args[0]);
                if (icommand != null) {
                    throw new WrongUsageException(icommand.getUsage(sender), new Object[0]);
                }
                if (MathHelper.getInt(args[0], -1) == -1 && MathHelper.getInt(args[0], -2) == -2) {
                    throw new CommandNotFoundException();
                }
                throw numberinvalidexception;
            }
            final int l = Math.min((k + 1) * 7, list.size());
            final TextComponentTranslation textcomponenttranslation1 = new TextComponentTranslation("commands.help.header", new Object[] { k + 1, j + 1 });
            textcomponenttranslation1.getStyle().setColor(TextFormatting.DARK_GREEN);
            sender.sendMessage(textcomponenttranslation1);
            for (int i2 = k * 7; i2 < l; ++i2) {
                final ICommand icommand2 = list.get(i2);
                final TextComponentTranslation textcomponenttranslation2 = new TextComponentTranslation(icommand2.getUsage(sender), new Object[0]);
                textcomponenttranslation2.getStyle().setClickEvent(new ClickEvent(ClickEvent.Action.SUGGEST_COMMAND, "/" + icommand2.getName() + " "));
                sender.sendMessage(textcomponenttranslation2);
            }
            if (k == 0) {
                final TextComponentTranslation textcomponenttranslation3 = new TextComponentTranslation("commands.help.footer", new Object[0]);
                textcomponenttranslation3.getStyle().setColor(TextFormatting.GREEN);
                sender.sendMessage(textcomponenttranslation3);
            }
        }
    }
    
    protected List<ICommand> getSortedPossibleCommands(final ICommandSender sender, final MinecraftServer server) {
        final List<ICommand> list = server.getCommandManager().getPossibleCommands(sender);
        Collections.sort(list);
        return list;
    }
    
    protected Map<String, ICommand> getCommandMap(final MinecraftServer server) {
        return server.getCommandManager().getCommands();
    }
    
    @Override
    public List<String> getTabCompletions(final MinecraftServer server, final ICommandSender sender, final String[] args, @Nullable final BlockPos targetPos) {
        if (args.length == 1) {
            final Set<String> set = this.getCommandMap(server).keySet();
            return CommandBase.getListOfStringsMatchingLastWord(args, (String[])set.toArray(new String[set.size()]));
        }
        return Collections.emptyList();
    }
    
    static {
        SEARGE_SAYS = new String[] { "Yolo", "Ask for help on twitter", "/deop @p", "Scoreboard deleted, commands blocked", "Contact helpdesk for help", "/testfornoob @p", "/trigger warning", "Oh my god, it's full of stats", "/kill @p[name=!Searge]", "Have you tried turning it off and on again?", "Sorry, no help today" };
    }
}
