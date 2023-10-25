/*
 * Decompiled with CFR 0.150.
 */
package baritone.command.defaults;

import baritone.api.IBaritone;
import baritone.api.command.Command;
import baritone.api.command.IBaritoneChatControl;
import baritone.api.command.ICommand;
import baritone.api.command.argument.IArgConsumer;
import baritone.api.command.exception.CommandException;
import baritone.api.command.exception.CommandNotFoundException;
import baritone.api.command.helpers.Paginator;
import baritone.api.command.helpers.TabCompleteHelper;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.util.text.event.ClickEvent;
import net.minecraft.util.text.event.HoverEvent;

public class HelpCommand
extends Command {
    public HelpCommand(IBaritone baritone) {
        super(baritone, "help", "?");
    }

    @Override
    public void execute(String label, IArgConsumer args) throws CommandException {
        args.requireMax(1);
        if (!args.hasAny() || args.is(Integer.class)) {
            Paginator.paginate(args, new Paginator(this.baritone.getCommandManager().getRegistry().descendingStream().filter(command -> !command.hiddenFromHelp()).collect(Collectors.toList())), () -> this.logDirect("All Baritone commands (clickable):"), command -> {
                String names = String.join((CharSequence)"/", command.getNames());
                String name = command.getNames().get(0);
                TextComponentString shortDescComponent = new TextComponentString(" - " + command.getShortDesc());
                shortDescComponent.getStyle().setColor(TextFormatting.DARK_GRAY);
                TextComponentString namesComponent = new TextComponentString(names);
                namesComponent.getStyle().setColor(TextFormatting.WHITE);
                TextComponentString hoverComponent = new TextComponentString("");
                hoverComponent.getStyle().setColor(TextFormatting.GRAY);
                hoverComponent.appendSibling(namesComponent);
                hoverComponent.appendText("\n" + command.getShortDesc());
                hoverComponent.appendText("\n\nClick to view full help");
                String clickCommand = IBaritoneChatControl.FORCE_COMMAND_PREFIX + String.format("%s %s", label, command.getNames().get(0));
                TextComponentString component = new TextComponentString(name);
                component.getStyle().setColor(TextFormatting.GRAY);
                component.appendSibling(shortDescComponent);
                component.getStyle().setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, hoverComponent)).setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, clickCommand));
                return component;
            }, IBaritoneChatControl.FORCE_COMMAND_PREFIX + label);
        } else {
            String commandName = args.getString().toLowerCase();
            ICommand command2 = this.baritone.getCommandManager().getCommand(commandName);
            if (command2 == null) {
                throw new CommandNotFoundException(commandName);
            }
            this.logDirect(String.format("%s - %s", String.join((CharSequence)" / ", command2.getNames()), command2.getShortDesc()));
            this.logDirect("");
            command2.getLongDesc().forEach(this::logDirect);
            this.logDirect("");
            TextComponentString returnComponent = new TextComponentString("Click to return to the help menu");
            returnComponent.getStyle().setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, IBaritoneChatControl.FORCE_COMMAND_PREFIX + label));
            this.logDirect(returnComponent);
        }
    }

    @Override
    public Stream<String> tabComplete(String label, IArgConsumer args) throws CommandException {
        if (args.hasExactlyOne()) {
            return new TabCompleteHelper().addCommands(this.baritone.getCommandManager()).filterPrefix(args.getString()).stream();
        }
        return Stream.empty();
    }

    @Override
    public String getShortDesc() {
        return "View all commands or help on specific ones";
    }

    @Override
    public List<String> getLongDesc() {
        return Arrays.asList("Using this command, you can view detailed help information on how to use certain commands of Baritone.", "", "Usage:", "> help - Lists all commands and their short descriptions.", "> help <command> - Displays help information on a specific command.");
    }
}

