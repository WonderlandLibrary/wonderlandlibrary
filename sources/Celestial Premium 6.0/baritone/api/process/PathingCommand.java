/*
 * Decompiled with CFR 0.150.
 */
package baritone.api.process;

import baritone.api.pathing.goals.Goal;
import baritone.api.process.PathingCommandType;
import java.util.Objects;

public class PathingCommand {
    public final Goal goal;
    public final PathingCommandType commandType;

    public PathingCommand(Goal goal, PathingCommandType commandType) {
        Objects.requireNonNull(commandType);
        this.goal = goal;
        this.commandType = commandType;
    }

    public String toString() {
        return (Object)((Object)this.commandType) + " " + this.goal;
    }
}

