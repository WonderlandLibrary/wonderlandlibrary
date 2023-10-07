package net.minecraft.command;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import net.minecraft.entity.Entity;
import net.minecraft.util.BlockPos;
import net.minecraft.util.ChatComponentTranslation;
import static net.minecraft.util.EnumChatFormatting.RED;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.jetbrains.annotations.NotNull;

public class CommandHandler implements ICommandManager {

    private static final Logger LOGGER = LogManager.getLogger();

    protected final Map<String, ICommand> commandMap = Maps.newHashMap();
    private final Set<ICommand> commandSet = Sets.newHashSet();
    private final String prefix;

    public CommandHandler(@NotNull String prefix) {
        this.prefix = prefix;
    }

    public int executeCommand(ICommandSender sender, String rawCommand) {
        rawCommand = rawCommand.trim();

        if (rawCommand.startsWith(this.prefix)) {
            rawCommand = rawCommand.substring(1);
        }

        String[] astring = rawCommand.split(" ");
        final String s = astring[0];
        astring = dropFirstString(astring);
        final ICommand icommand = this.commandMap.get(s);
        final int i = this.getUsernameIndex(icommand, astring);
        int j = 0;

        if (icommand == null) {
            final ChatComponentTranslation chatcomponenttranslation = new ChatComponentTranslation("commands.generic.notFound");
            chatcomponenttranslation.getChatStyle().setColor(RED);
            sender.addChatMessage(chatcomponenttranslation);
        } else if (icommand.canCommandSenderUseCommand(sender)) {
            if (i > -1) {
                final List<Entity> list = PlayerSelector.matchEntities(sender, astring[i], Entity.class);
                final String s1 = astring[i];
                sender.setCommandStat(CommandResultStats.Type.AFFECTED_ENTITIES, list.size());

                for (Entity entity : list) {
                    astring[i] = entity.getUniqueID().toString();

                    if (this.tryExecute(sender, astring, icommand, rawCommand)) {
                        ++j;
                    }
                }

                astring[i] = s1;
            } else {
                sender.setCommandStat(CommandResultStats.Type.AFFECTED_ENTITIES, 1);

                if (this.tryExecute(sender, astring, icommand, rawCommand)) {
                    ++j;
                }
            }
        } else {
            final ChatComponentTranslation chatcomponenttranslation1 = new ChatComponentTranslation("commands.generic.permission");
            chatcomponenttranslation1.getChatStyle().setColor(RED);
            sender.addChatMessage(chatcomponenttranslation1);
        }

        sender.setCommandStat(CommandResultStats.Type.SUCCESS_COUNT, j);
        return j;
    }

    protected boolean tryExecute(ICommandSender sender, String[] args, ICommand command, String input) {
        try {
            command.processCommand(sender, args);
            return true;
        } catch (WrongUsageException e) {
            final ChatComponentTranslation component = new ChatComponentTranslation("commands.generic.usage", new ChatComponentTranslation(e.getMessage(), e.getErrorObjects()));
            component.getChatStyle().setColor(RED);

            sender.addChatMessage(component);
        } catch (CommandException e) {
            final ChatComponentTranslation component = new ChatComponentTranslation(e.getMessage(), e.getErrorObjects());
            component.getChatStyle().setColor(RED);

            sender.addChatMessage(component);
        } catch (Throwable t) {
            LOGGER.warn("Unexpected error occurred while processing command: \"" + input + "\"");

            final ChatComponentTranslation component = new ChatComponentTranslation("commands.generic.exception");
            component.getChatStyle().setColor(RED);

            sender.addChatMessage(component);
        }

        return false;
    }

    /**
     * adds the command and any aliases it has to the internal map of available commands
     */
    public void registerCommand(ICommand command) {
        this.commandMap.put(command.getCommandName(), command);
        this.commandSet.add(command);

        for (String s : command.getCommandAliases()) {
            final ICommand icommand = this.commandMap.get(s);

            if (icommand == null || !icommand.getCommandName().equals(s)) {
                this.commandMap.put(s, command);
            }
        }
    }

    /**
     * creates a new array and sets elements 0..n-2 to be 0..n-1 of the input (n elements)
     */
    protected static @NotNull String[] dropFirstString(@NotNull String[] input) {
        final String[] args = new String[input.length - 1];

        System.arraycopy(input, 1, args, 0, input.length - 1);
        return args;
    }

    public List<String> getTabCompletionOptions(ICommandSender sender, @NotNull String input, BlockPos pos) {
        final String[] args = input.split(" ", -1);
        final String s = args[0];

        if (args.length == 1) {
            final List<String> list = Lists.newArrayList();

            for (Entry<String, ICommand> entry : this.commandMap.entrySet()) {
                if (CommandBase.doesStringStartWith(s, entry.getKey()) && entry.getValue().canCommandSenderUseCommand(sender)) {
                    list.add(entry.getKey());
                }
            }

            return list;
        } else {
            final ICommand command = this.commandMap.get(s);

            if (command != null && command.canCommandSenderUseCommand(sender)) {
                return command.addTabCompletionOptions(sender, dropFirstString(args), pos);
            }

            return null;
        }
    }

    public List<ICommand> getPossibleCommands(ICommandSender sender) {
        final List<ICommand> list = Lists.newArrayList();

        for (ICommand icommand : this.commandSet) {
            if (icommand.canCommandSenderUseCommand(sender)) {
                list.add(icommand);
            }
        }

        return list;
    }

    public Map<String, ICommand> getCommands() {
        return this.commandMap;
    }

    /**
     * Return a command's first parameter index containing a valid username.
     */
    private int getUsernameIndex(ICommand command, String[] args) {
        if (command != null) {
            for (int i = 0; i < args.length; ++i) {
                if (command.isUsernameIndex(args, i) && PlayerSelector.matchesMultiplePlayers(args[i])) {
                    return i;
                }
            }
        }

        return -1;
    }

}
