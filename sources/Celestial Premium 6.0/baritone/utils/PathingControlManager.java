/*
 * Decompiled with CFR 0.150.
 */
package baritone.utils;

import baritone.Baritone;
import baritone.api.event.events.TickEvent;
import baritone.api.event.listener.AbstractGameEventListener;
import baritone.api.pathing.calc.IPathingControlManager;
import baritone.api.pathing.goals.Goal;
import baritone.api.process.IBaritoneProcess;
import baritone.api.process.PathingCommand;
import baritone.api.process.PathingCommandType;
import baritone.api.utils.BetterBlockPos;
import baritone.behavior.PathingBehavior;
import baritone.pathing.path.PathExecutor;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

public class PathingControlManager
implements IPathingControlManager {
    private final Baritone baritone;
    private final HashSet<IBaritoneProcess> processes;
    private final List<IBaritoneProcess> active;
    private IBaritoneProcess inControlLastTick;
    private IBaritoneProcess inControlThisTick;
    private PathingCommand command;

    public PathingControlManager(Baritone baritone) {
        this.baritone = baritone;
        this.processes = new HashSet();
        this.active = new ArrayList<IBaritoneProcess>();
        baritone.getGameEventHandler().registerEventListener(new AbstractGameEventListener(){

            @Override
            public void onTick(TickEvent event) {
                if (event.getType() == TickEvent.Type.IN) {
                    PathingControlManager.this.postTick();
                }
            }
        });
    }

    @Override
    public void registerProcess(IBaritoneProcess process) {
        process.onLostControl();
        this.processes.add(process);
    }

    public void cancelEverything() {
        this.inControlLastTick = null;
        this.inControlThisTick = null;
        this.command = null;
        this.active.clear();
        for (IBaritoneProcess proc : this.processes) {
            proc.onLostControl();
            if (!proc.isActive() || proc.isTemporary()) continue;
            throw new IllegalStateException(proc.displayName());
        }
    }

    @Override
    public Optional<IBaritoneProcess> mostRecentInControl() {
        return Optional.ofNullable(this.inControlThisTick);
    }

    @Override
    public Optional<PathingCommand> mostRecentCommand() {
        return Optional.ofNullable(this.command);
    }

    public void preTick() {
        this.inControlLastTick = this.inControlThisTick;
        this.inControlThisTick = null;
        PathingBehavior p = this.baritone.getPathingBehavior();
        this.command = this.executeProcesses();
        if (this.command == null) {
            p.cancelSegmentIfSafe();
            p.secretInternalSetGoal(null);
            return;
        }
        if (!Objects.equals(this.inControlThisTick, this.inControlLastTick) && this.command.commandType != PathingCommandType.REQUEST_PAUSE && this.inControlLastTick != null && !this.inControlLastTick.isTemporary()) {
            p.cancelSegmentIfSafe();
        }
        switch (this.command.commandType) {
            case REQUEST_PAUSE: {
                p.requestPause();
                break;
            }
            case CANCEL_AND_SET_GOAL: {
                p.secretInternalSetGoal(this.command.goal);
                p.cancelSegmentIfSafe();
                break;
            }
            case FORCE_REVALIDATE_GOAL_AND_PATH: {
                if (p.isPathing() || p.getInProgress().isPresent()) break;
                p.secretInternalSetGoalAndPath(this.command);
                break;
            }
            case REVALIDATE_GOAL_AND_PATH: {
                if (p.isPathing() || p.getInProgress().isPresent()) break;
                p.secretInternalSetGoalAndPath(this.command);
                break;
            }
            case SET_GOAL_AND_PATH: {
                if (this.command.goal == null) break;
                this.baritone.getPathingBehavior().secretInternalSetGoalAndPath(this.command);
                break;
            }
            default: {
                throw new IllegalStateException();
            }
        }
    }

    private void postTick() {
        if (this.command == null) {
            return;
        }
        PathingBehavior p = this.baritone.getPathingBehavior();
        switch (this.command.commandType) {
            case FORCE_REVALIDATE_GOAL_AND_PATH: {
                if (this.command.goal == null || this.forceRevalidate(this.command.goal) || this.revalidateGoal(this.command.goal)) {
                    p.softCancelIfSafe();
                }
                p.secretInternalSetGoalAndPath(this.command);
                break;
            }
            case REVALIDATE_GOAL_AND_PATH: {
                if (((Boolean)Baritone.settings().cancelOnGoalInvalidation.value).booleanValue() && (this.command.goal == null || this.revalidateGoal(this.command.goal))) {
                    p.softCancelIfSafe();
                }
                p.secretInternalSetGoalAndPath(this.command);
                break;
            }
        }
    }

    public boolean forceRevalidate(Goal newGoal) {
        PathExecutor current = this.baritone.getPathingBehavior().getCurrent();
        if (current != null) {
            if (newGoal.isInGoal(current.getPath().getDest())) {
                return false;
            }
            return !newGoal.toString().equals(current.getPath().getGoal().toString());
        }
        return false;
    }

    public boolean revalidateGoal(Goal newGoal) {
        BetterBlockPos end;
        Goal intended;
        PathExecutor current = this.baritone.getPathingBehavior().getCurrent();
        return current != null && (intended = current.getPath().getGoal()).isInGoal(end = current.getPath().getDest()) && !newGoal.isInGoal(end);
    }

    public PathingCommand executeProcesses() {
        for (IBaritoneProcess process : this.processes) {
            if (process.isActive()) {
                if (this.active.contains(process)) continue;
                this.active.add(0, process);
                continue;
            }
            this.active.remove(process);
        }
        this.active.sort(Comparator.comparingDouble(IBaritoneProcess::priority).reversed());
        Iterator<IBaritoneProcess> iterator = this.active.iterator();
        while (iterator.hasNext()) {
            IBaritoneProcess proc;
            PathingCommand exec = proc.onTick(Objects.equals(proc = iterator.next(), this.inControlLastTick) && this.baritone.getPathingBehavior().calcFailedLastTick(), this.baritone.getPathingBehavior().isSafeToCancel());
            if (exec == null) {
                if (!proc.isActive()) continue;
                throw new IllegalStateException(proc.displayName() + " actively returned null PathingCommand");
            }
            if (exec.commandType == PathingCommandType.DEFER) continue;
            this.inControlThisTick = proc;
            if (!proc.isTemporary()) {
                iterator.forEachRemaining(IBaritoneProcess::onLostControl);
            }
            return exec;
        }
        return null;
    }
}

