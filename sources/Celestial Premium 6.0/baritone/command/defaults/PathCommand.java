/*
 * Decompiled with CFR 0.150.
 */
package baritone.command.defaults;

import baritone.api.IBaritone;
import baritone.api.command.Command;
import baritone.api.command.argument.IArgConsumer;
import baritone.api.command.exception.CommandException;
import baritone.api.process.ICustomGoalProcess;
import baritone.cache.WorldScanner;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Stream;

public class PathCommand
extends Command {
    public PathCommand(IBaritone baritone) {
        super(baritone, "path");
    }

    @Override
    public void execute(String label, IArgConsumer args) throws CommandException {
        ICustomGoalProcess customGoalProcess = this.baritone.getCustomGoalProcess();
        args.requireMax(0);
        WorldScanner.INSTANCE.repack(this.ctx);
        customGoalProcess.path();
        this.logDirect("Now pathing");
    }

    @Override
    public Stream<String> tabComplete(String label, IArgConsumer args) throws CommandException {
        return Stream.empty();
    }

    @Override
    public String getShortDesc() {
        return "Start heading towards the goal";
    }

    @Override
    public List<String> getLongDesc() {
        return Arrays.asList("The path command tells Baritone to head towards the current goal.", "", "Usage:", "> path - Start the pathing.");
    }
}

