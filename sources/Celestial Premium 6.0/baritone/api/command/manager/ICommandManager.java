/*
 * Decompiled with CFR 0.150.
 */
package baritone.api.command.manager;

import baritone.api.IBaritone;
import baritone.api.command.ICommand;
import baritone.api.command.argument.ICommandArgument;
import baritone.api.command.registry.Registry;
import java.util.List;
import java.util.stream.Stream;
import net.minecraft.util.Tuple;

public interface ICommandManager {
    public IBaritone getBaritone();

    public Registry<ICommand> getRegistry();

    public ICommand getCommand(String var1);

    public boolean execute(String var1);

    public boolean execute(Tuple<String, List<ICommandArgument>> var1);

    public Stream<String> tabComplete(Tuple<String, List<ICommandArgument>> var1);

    public Stream<String> tabComplete(String var1);
}

