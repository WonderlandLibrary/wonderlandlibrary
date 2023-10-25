/*
 * Decompiled with CFR 0.150.
 */
package baritone.api.command.datatypes;

import baritone.api.command.argument.IArgConsumer;
import baritone.api.command.datatypes.IDatatypeContext;
import baritone.api.command.datatypes.IDatatypePost;
import baritone.api.command.datatypes.RelativeCoordinate;
import baritone.api.command.datatypes.RelativeGoalBlock;
import baritone.api.command.datatypes.RelativeGoalXZ;
import baritone.api.command.datatypes.RelativeGoalYLevel;
import baritone.api.command.exception.CommandException;
import baritone.api.pathing.goals.Goal;
import baritone.api.pathing.goals.GoalBlock;
import baritone.api.pathing.goals.GoalXZ;
import baritone.api.pathing.goals.GoalYLevel;
import baritone.api.utils.BetterBlockPos;
import java.util.stream.Stream;

public enum RelativeGoal implements IDatatypePost<Goal, BetterBlockPos>
{
    INSTANCE;


    @Override
    public Goal apply(IDatatypeContext ctx, BetterBlockPos origin) throws CommandException {
        IArgConsumer consumer;
        GoalBlock goalBlock;
        if (origin == null) {
            origin = BetterBlockPos.ORIGIN;
        }
        if ((goalBlock = (GoalBlock)(consumer = ctx.getConsumer()).peekDatatypePostOrNull(RelativeGoalBlock.INSTANCE, origin)) != null) {
            return goalBlock;
        }
        GoalXZ goalXZ = (GoalXZ)consumer.peekDatatypePostOrNull(RelativeGoalXZ.INSTANCE, origin);
        if (goalXZ != null) {
            return goalXZ;
        }
        GoalYLevel goalYLevel = (GoalYLevel)consumer.peekDatatypePostOrNull(RelativeGoalYLevel.INSTANCE, origin);
        if (goalYLevel != null) {
            return goalYLevel;
        }
        return new GoalBlock(origin);
    }

    @Override
    public Stream<String> tabComplete(IDatatypeContext ctx) {
        return ctx.getConsumer().tabCompleteDatatype(RelativeCoordinate.INSTANCE);
    }
}

