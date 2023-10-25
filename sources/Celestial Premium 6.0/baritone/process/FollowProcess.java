/*
 * Decompiled with CFR 0.150.
 */
package baritone.process;

import baritone.Baritone;
import baritone.api.pathing.goals.Goal;
import baritone.api.pathing.goals.GoalComposite;
import baritone.api.pathing.goals.GoalNear;
import baritone.api.pathing.goals.GoalXZ;
import baritone.api.process.IFollowProcess;
import baritone.api.process.PathingCommand;
import baritone.api.process.PathingCommandType;
import baritone.utils.BaritoneProcessHelper;
import java.util.Collection;
import java.util.List;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import net.minecraft.entity.Entity;
import net.minecraft.util.math.BlockPos;

public final class FollowProcess
extends BaritoneProcessHelper
implements IFollowProcess {
    private Predicate<Entity> filter;
    private List<Entity> cache;

    public FollowProcess(Baritone baritone) {
        super(baritone);
    }

    @Override
    public PathingCommand onTick(boolean calcFailed, boolean isSafeToCancel) {
        this.scanWorld();
        GoalComposite goal = new GoalComposite((Goal[])this.cache.stream().map(this::towards).toArray(Goal[]::new));
        return new PathingCommand(goal, PathingCommandType.REVALIDATE_GOAL_AND_PATH);
    }

    private Goal towards(Entity following) {
        BlockPos pos;
        if ((Double)Baritone.settings().followOffsetDistance.value == 0.0) {
            pos = new BlockPos(following);
        } else {
            GoalXZ g = GoalXZ.fromDirection(following.getPositionVector(), ((Float)Baritone.settings().followOffsetDirection.value).floatValue(), (Double)Baritone.settings().followOffsetDistance.value);
            pos = new BlockPos((double)g.getX(), following.posY, (double)g.getZ());
        }
        return new GoalNear(pos, (Integer)Baritone.settings().followRadius.value);
    }

    private boolean followable(Entity entity) {
        if (entity == null) {
            return false;
        }
        if (entity.isDead) {
            return false;
        }
        if (entity.equals(this.ctx.player())) {
            return false;
        }
        return this.ctx.world().loadedEntityList.contains(entity);
    }

    private void scanWorld() {
        this.cache = Stream.of(this.ctx.world().loadedEntityList, this.ctx.world().playerEntities).flatMap(Collection::stream).filter(this::followable).filter(this.filter).distinct().collect(Collectors.toList());
    }

    @Override
    public boolean isActive() {
        if (this.filter == null) {
            return false;
        }
        this.scanWorld();
        return !this.cache.isEmpty();
    }

    @Override
    public void onLostControl() {
        this.filter = null;
        this.cache = null;
    }

    @Override
    public String displayName0() {
        return "Following " + this.cache;
    }

    @Override
    public void follow(Predicate<Entity> filter) {
        this.filter = filter;
    }

    @Override
    public List<Entity> following() {
        return this.cache;
    }

    @Override
    public Predicate<Entity> currentFilter() {
        return this.filter;
    }
}

