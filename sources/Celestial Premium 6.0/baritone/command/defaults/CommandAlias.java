/*
 * Decompiled with CFR 0.150.
 */
package baritone.command.defaults;

import baritone.api.IBaritone;
import baritone.api.command.Command;
import baritone.api.command.argument.IArgConsumer;
import java.util.Collections;
import java.util.List;
import java.util.stream.Stream;

public class CommandAlias
extends Command {
    private final String shortDesc;
    public final String target;

    public CommandAlias(IBaritone baritone, List<String> names, String shortDesc, String target) {
        super(baritone, names.toArray(new String[0]));
        this.shortDesc = shortDesc;
        this.target = target;
    }

    public CommandAlias(IBaritone baritone, String name, String shortDesc, String target) {
        super(baritone, name);
        this.shortDesc = shortDesc;
        this.target = target;
    }

    @Override
    public void execute(String label, IArgConsumer args) {
        this.baritone.getCommandManager().execute(String.format("%s %s", this.target, args.rawRest()));
    }

    @Override
    public Stream<String> tabComplete(String label, IArgConsumer args) {
        return this.baritone.getCommandManager().tabComplete(String.format("%s %s", this.target, args.rawRest()));
    }

    @Override
    public String getShortDesc() {
        return this.shortDesc;
    }

    @Override
    public List<String> getLongDesc() {
        return Collections.singletonList(String.format("This command is an alias, for: %s ...", this.target));
    }
}

