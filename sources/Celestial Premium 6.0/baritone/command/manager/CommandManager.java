/*
 * Decompiled with CFR 0.150.
 */
package baritone.command.manager;

import baritone.Baritone;
import baritone.api.IBaritone;
import baritone.api.command.ICommand;
import baritone.api.command.argument.ICommandArgument;
import baritone.api.command.exception.CommandUnhandledException;
import baritone.api.command.exception.ICommandException;
import baritone.api.command.helpers.TabCompleteHelper;
import baritone.api.command.manager.ICommandManager;
import baritone.api.command.registry.Registry;
import baritone.command.argument.ArgConsumer;
import baritone.command.argument.CommandArguments;
import baritone.command.defaults.DefaultCommands;
import java.util.List;
import java.util.Locale;
import java.util.stream.Stream;
import net.minecraft.util.Tuple;

public class CommandManager
implements ICommandManager {
    private final Registry<ICommand> registry = new Registry();
    private final Baritone baritone;

    public CommandManager(Baritone baritone) {
        this.baritone = baritone;
        DefaultCommands.createAll(baritone).forEach(this.registry::register);
    }

    @Override
    public IBaritone getBaritone() {
        return this.baritone;
    }

    @Override
    public Registry<ICommand> getRegistry() {
        return this.registry;
    }

    @Override
    public ICommand getCommand(String name) {
        for (ICommand command : this.registry.entries) {
            if (!command.getNames().contains(name.toLowerCase(Locale.US))) continue;
            return command;
        }
        return null;
    }

    @Override
    public boolean execute(String string) {
        return this.execute(CommandManager.expand(string));
    }

    @Override
    public boolean execute(Tuple<String, List<ICommandArgument>> expanded) {
        ExecutionWrapper execution = this.from(expanded);
        if (execution != null) {
            execution.execute();
        }
        return execution != null;
    }

    @Override
    public Stream<String> tabComplete(Tuple<String, List<ICommandArgument>> expanded) {
        ExecutionWrapper execution = this.from(expanded);
        return execution == null ? Stream.empty() : execution.tabComplete();
    }

    @Override
    public Stream<String> tabComplete(String prefix) {
        Tuple<String, List<ICommandArgument>> pair = CommandManager.expand(prefix, true);
        String label = pair.getFirst();
        List<ICommandArgument> args = pair.getSecond();
        if (args.isEmpty()) {
            return new TabCompleteHelper().addCommands(this.baritone.getCommandManager()).filterPrefix(label).stream();
        }
        return this.tabComplete(pair);
    }

    private ExecutionWrapper from(Tuple<String, List<ICommandArgument>> expanded) {
        String label = expanded.getFirst();
        ArgConsumer args = new ArgConsumer(this, expanded.getSecond());
        ICommand command = this.getCommand(label);
        return command == null ? null : new ExecutionWrapper(command, label, args);
    }

    private static Tuple<String, List<ICommandArgument>> expand(String string, boolean preserveEmptyLast) {
        String label = string.split("\\s", 2)[0];
        List<ICommandArgument> args = CommandArguments.from(string.substring(label.length()), preserveEmptyLast);
        return new Tuple<String, List<ICommandArgument>>(label, args);
    }

    public static Tuple<String, List<ICommandArgument>> expand(String string) {
        return CommandManager.expand(string, false);
    }

    private static final class ExecutionWrapper {
        private ICommand command;
        private String label;
        private ArgConsumer args;

        private ExecutionWrapper(ICommand command, String label, ArgConsumer args) {
            this.command = command;
            this.label = label;
            this.args = args;
        }

        private void execute() {
            try {
                this.command.execute(this.label, this.args);
            }
            catch (Throwable t) {
                ICommandException exception = t instanceof ICommandException ? (ICommandException)((Object)t) : new CommandUnhandledException(t);
                exception.handle(this.command, this.args.getArgs());
            }
        }

        private Stream<String> tabComplete() {
            try {
                return this.command.tabComplete(this.label, this.args);
            }
            catch (Throwable t) {
                return Stream.empty();
            }
        }
    }
}

