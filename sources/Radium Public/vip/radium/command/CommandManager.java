package vip.radium.command;

import io.github.nevalackin.homoBus.annotations.EventLink;
import io.github.nevalackin.homoBus.Listener;
import vip.radium.RadiumClient;
import vip.radium.command.impl.*;
import vip.radium.event.impl.player.SendMessageEvent;
import vip.radium.utils.Wrapper;
import vip.radium.utils.handler.Manager;

import java.util.Arrays;

public final class CommandManager extends Manager<Command> {

    private static final String PREFIX = ".";
    private static final String HELP_MESSAGE = "Try '" + PREFIX + "help'";
    @EventLink
    public final Listener<SendMessageEvent> onSendMessageEvent = event -> {
        String message;
        if ((message = event.getMessage()).startsWith(PREFIX)) {
            event.setCancelled();
            String removedPrefix = message.substring(1);
            String[] arguments = removedPrefix.split(" ");
            if (!removedPrefix.isEmpty() && arguments.length > 0) {
                for (Command command : getElements()) {
                    for (String alias : command.getAliases()) {
                        if (alias.equalsIgnoreCase(arguments[0])) {
                            try {
                                command.execute(arguments);
                            } catch (CommandExecutionException e) {
                                Wrapper.addChatMessage("Invalid command syntax. Hint: " + e.getMessage());
                            }
                            return;
                        }
                    }
                }
                Wrapper.addChatMessage("'" + arguments[0] + "' is not a command. " + HELP_MESSAGE);
            } else
                Wrapper.addChatMessage("No arguments were supplied. " + HELP_MESSAGE);
        }
    };

    public CommandManager() {
        super(Arrays.asList(
                new HelpCommand(),
                new ToggleCommand(),
                new ConfigCommand(),
                new ClientNameCommand(),
                new VisibleCommand(),
                new NameCommand(),
                new ModuleCommand(),
                new FriendCommand(),
                new EnemyCommand(),
                new BindCommand())
        );
        RadiumClient.getInstance().getEventBus().subscribe(this);
    }
}
