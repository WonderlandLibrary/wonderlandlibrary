/*
 * Decompiled with CFR 0.150.
 */
package baritone.command.defaults;

import baritone.api.IBaritone;
import baritone.api.command.Command;
import baritone.api.command.argument.IArgConsumer;
import baritone.api.command.datatypes.RelativeCoordinate;
import baritone.api.command.datatypes.RelativeGoal;
import baritone.api.command.exception.CommandException;
import baritone.api.command.helpers.TabCompleteHelper;
import baritone.api.pathing.goals.Goal;
import baritone.api.process.ICustomGoalProcess;
import baritone.api.utils.BetterBlockPos;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Stream;

public class GoalCommand
extends Command {
    public GoalCommand(IBaritone baritone) {
        super(baritone, "goal");
    }

    /*
     * Enabled aggressive block sorting
     */
    @Override
    public void execute(String label, IArgConsumer args) throws CommandException {
        ICustomGoalProcess goalProcess = this.baritone.getCustomGoalProcess();
        if (args.hasAny()) {
            if (Arrays.asList("reset", "clear", "none").contains(args.peekString())) {
                args.requireMax(1);
                if (goalProcess.getGoal() != null) {
                    goalProcess.setGoal(null);
                    this.logDirect("Cleared goal");
                    return;
                }
                this.logDirect("There was no goal to clear");
                return;
            }
        }
        args.requireMax(3);
        BetterBlockPos origin = this.baritone.getPlayerContext().playerFeet();
        Goal goal = (Goal)args.getDatatypePost(RelativeGoal.INSTANCE, origin);
        goalProcess.setGoal(goal);
        this.logDirect(String.format("Goal: %s", goal.toString()));
    }

    @Override
    public Stream<String> tabComplete(String label, IArgConsumer args) throws CommandException {
        TabCompleteHelper helper = new TabCompleteHelper();
        if (args.hasExactlyOne()) {
            helper.append("reset", "clear", "none", "~");
        } else if (args.hasAtMost(3)) {
            while (args.has(2) && args.peekDatatypeOrNull(RelativeCoordinate.INSTANCE) != null) {
                args.get();
                if (args.has(2)) continue;
                helper.append("~");
            }
        }
        return helper.filterPrefix(args.getString()).stream();
    }

    @Override
    public String getShortDesc() {
        return "Set or clear the goal";
    }

    @Override
    public List<String> getLongDesc() {
        return Arrays.asList("The goal command allows you to set or clear Baritone's goal.", "", "Wherever a coordinate is expected, you can use ~ just like in regular Minecraft commands. Or, you can just use regular numbers.", "", "Usage:", "> goal - Set the goal to your current position", "> goal <reset/clear/none> - Erase the goal", "> goal <y> - Set the goal to a Y level", "> goal <x> <z> - Set the goal to an X,Z position", "> goal <x> <y> <z> - Set the goal to an X,Y,Z position");
    }
}

