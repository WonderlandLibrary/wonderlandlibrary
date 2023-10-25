/*
 * Decompiled with CFR 0.150.
 */
package baritone.behavior;

import baritone.Baritone;
import baritone.api.behavior.IPathingBehavior;
import baritone.api.event.events.PathEvent;
import baritone.api.event.events.PlayerUpdateEvent;
import baritone.api.event.events.RenderEvent;
import baritone.api.event.events.SprintStateEvent;
import baritone.api.event.events.TickEvent;
import baritone.api.pathing.calc.IPath;
import baritone.api.pathing.goals.Goal;
import baritone.api.pathing.goals.GoalXZ;
import baritone.api.process.PathingCommand;
import baritone.api.utils.BetterBlockPos;
import baritone.api.utils.Helper;
import baritone.api.utils.PathCalculationResult;
import baritone.api.utils.interfaces.IGoalRenderPos;
import baritone.behavior.Behavior;
import baritone.pathing.calc.AStarPathFinder;
import baritone.pathing.calc.AbstractNodeCostSearch;
import baritone.pathing.movement.CalculationContext;
import baritone.pathing.movement.MovementHelper;
import baritone.pathing.path.PathExecutor;
import baritone.utils.PathRenderer;
import baritone.utils.PathingCommandContext;
import baritone.utils.pathing.Favoring;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Objects;
import java.util.Optional;
import java.util.concurrent.LinkedBlockingQueue;
import net.minecraft.util.math.BlockPos;

public final class PathingBehavior
extends Behavior
implements IPathingBehavior,
Helper {
    private PathExecutor current;
    private PathExecutor next;
    private Goal goal;
    private CalculationContext context;
    private int ticksElapsedSoFar;
    private BetterBlockPos startPosition;
    private boolean safeToCancel;
    private boolean pauseRequestedLastTick;
    private boolean unpausedLastTick;
    private boolean pausedThisTick;
    private boolean cancelRequested;
    private boolean calcFailedLastTick;
    private volatile AbstractNodeCostSearch inProgress;
    private final Object pathCalcLock = new Object();
    private final Object pathPlanLock = new Object();
    private boolean lastAutoJump;
    private BetterBlockPos expectedSegmentStart;
    private final LinkedBlockingQueue<PathEvent> toDispatch = new LinkedBlockingQueue();

    public PathingBehavior(Baritone baritone) {
        super(baritone);
    }

    private void queuePathEvent(PathEvent event) {
        this.toDispatch.add(event);
    }

    private void dispatchEvents() {
        ArrayList curr = new ArrayList();
        this.toDispatch.drainTo(curr);
        this.calcFailedLastTick = curr.contains((Object)PathEvent.CALC_FAILED);
        for (PathEvent event : curr) {
            this.baritone.getGameEventHandler().onPathEvent(event);
        }
    }

    @Override
    public void onTick(TickEvent event) {
        this.dispatchEvents();
        if (event.getType() == TickEvent.Type.OUT) {
            this.secretInternalSegmentCancel();
            this.baritone.getPathingControlManager().cancelEverything();
            return;
        }
        this.expectedSegmentStart = this.pathStart();
        this.baritone.getPathingControlManager().preTick();
        this.tickPath();
        ++this.ticksElapsedSoFar;
        this.dispatchEvents();
    }

    @Override
    public void onPlayerSprintState(SprintStateEvent event) {
        if (this.isPathing()) {
            event.setState(this.current.isSprinting());
        }
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    private void tickPath() {
        this.pausedThisTick = false;
        if (this.pauseRequestedLastTick && this.safeToCancel) {
            this.pauseRequestedLastTick = false;
            if (this.unpausedLastTick) {
                this.baritone.getInputOverrideHandler().clearAllKeys();
                this.baritone.getInputOverrideHandler().getBlockBreakHelper().stopBreakingBlock();
            }
            this.unpausedLastTick = false;
            this.pausedThisTick = true;
            return;
        }
        this.unpausedLastTick = true;
        if (this.cancelRequested) {
            this.cancelRequested = false;
            this.baritone.getInputOverrideHandler().clearAllKeys();
        }
        Object object = this.pathPlanLock;
        synchronized (object) {
            Object object2 = this.pathCalcLock;
            synchronized (object2) {
                if (this.inProgress != null) {
                    BetterBlockPos calcFrom = this.inProgress.getStart();
                    Optional<IPath> currentBest = this.inProgress.bestPathSoFar();
                    if (!(this.current != null && this.current.getPath().getDest().equals(calcFrom) || calcFrom.equals(this.ctx.playerFeet()) || calcFrom.equals(this.expectedSegmentStart) || currentBest.isPresent() && (currentBest.get().positions().contains(this.ctx.playerFeet()) || currentBest.get().positions().contains(this.expectedSegmentStart)))) {
                        this.inProgress.cancel();
                    }
                }
            }
            if (this.current == null) {
                return;
            }
            this.safeToCancel = this.current.onTick();
            if (this.current.failed() || this.current.finished()) {
                this.current = null;
                if (this.goal == null || this.goal.isInGoal(this.ctx.playerFeet())) {
                    this.logDebug("All done. At " + this.goal);
                    this.queuePathEvent(PathEvent.AT_GOAL);
                    this.next = null;
                    if (((Boolean)Baritone.settings().disconnectOnArrival.value).booleanValue()) {
                        this.ctx.world().sendQuittingDisconnectingPacket();
                    }
                    return;
                }
                if (this.next != null && !this.next.getPath().positions().contains(this.ctx.playerFeet()) && !this.next.getPath().positions().contains(this.expectedSegmentStart)) {
                    this.logDebug("Discarding next path as it does not contain current position");
                    this.queuePathEvent(PathEvent.DISCARD_NEXT);
                    this.next = null;
                }
                if (this.next != null) {
                    this.logDebug("Continuing on to planned next path");
                    this.queuePathEvent(PathEvent.CONTINUING_ONTO_PLANNED_NEXT);
                    this.current = this.next;
                    this.next = null;
                    this.current.onTick();
                    return;
                }
                object2 = this.pathCalcLock;
                synchronized (object2) {
                    if (this.inProgress != null) {
                        this.queuePathEvent(PathEvent.PATH_FINISHED_NEXT_STILL_CALCULATING);
                        return;
                    }
                    this.queuePathEvent(PathEvent.CALC_STARTED);
                    this.findPathInNewThread(this.expectedSegmentStart, true, this.context);
                }
                return;
            }
            if (this.safeToCancel && this.next != null && this.next.snipsnapifpossible()) {
                this.logDebug("Splicing into planned next path early...");
                this.queuePathEvent(PathEvent.SPLICING_ONTO_NEXT_EARLY);
                this.current = this.next;
                this.next = null;
                this.current.onTick();
                return;
            }
            if (((Boolean)Baritone.settings().splicePath.value).booleanValue()) {
                this.current = this.current.trySplice(this.next);
            }
            if (this.next != null && this.current.getPath().getDest().equals(this.next.getPath().getDest())) {
                this.next = null;
            }
            object2 = this.pathCalcLock;
            synchronized (object2) {
                if (this.inProgress != null) {
                    return;
                }
                if (this.next != null) {
                    return;
                }
                if (this.goal == null || this.goal.isInGoal(this.current.getPath().getDest())) {
                    return;
                }
                if (this.ticksRemainingInSegment(false).get() < (double)((Integer)Baritone.settings().planningTickLookahead.value).intValue()) {
                    this.logDebug("Path almost over. Planning ahead...");
                    this.queuePathEvent(PathEvent.NEXT_SEGMENT_CALC_STARTED);
                    this.findPathInNewThread(this.current.getPath().getDest(), false, this.context);
                }
            }
        }
    }

    @Override
    public void onPlayerUpdate(PlayerUpdateEvent event) {
        if (this.current != null) {
            switch (event.getState()) {
                case PRE: {
                    this.lastAutoJump = PathingBehavior.mc.gameSettings.autoJump;
                    PathingBehavior.mc.gameSettings.autoJump = false;
                    break;
                }
                case POST: {
                    PathingBehavior.mc.gameSettings.autoJump = this.lastAutoJump;
                    break;
                }
            }
        }
    }

    public void secretInternalSetGoal(Goal goal) {
        this.goal = goal;
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    public boolean secretInternalSetGoalAndPath(PathingCommand command) {
        this.secretInternalSetGoal(command.goal);
        this.context = command instanceof PathingCommandContext ? ((PathingCommandContext)command).desiredCalcContext : new CalculationContext(this.baritone, true);
        if (this.goal == null) {
            return false;
        }
        if (this.goal.isInGoal(this.ctx.playerFeet()) || this.goal.isInGoal(this.expectedSegmentStart)) {
            return false;
        }
        Object object = this.pathPlanLock;
        synchronized (object) {
            if (this.current != null) {
                return false;
            }
            Object object2 = this.pathCalcLock;
            synchronized (object2) {
                if (this.inProgress != null) {
                    return false;
                }
                this.queuePathEvent(PathEvent.CALC_STARTED);
                this.findPathInNewThread(this.expectedSegmentStart, true, this.context);
                return true;
            }
        }
    }

    @Override
    public Goal getGoal() {
        return this.goal;
    }

    @Override
    public boolean isPathing() {
        return this.hasPath() && !this.pausedThisTick;
    }

    @Override
    public PathExecutor getCurrent() {
        return this.current;
    }

    @Override
    public PathExecutor getNext() {
        return this.next;
    }

    public Optional<AbstractNodeCostSearch> getInProgress() {
        return Optional.ofNullable(this.inProgress);
    }

    public boolean isSafeToCancel() {
        return this.current == null || this.safeToCancel;
    }

    public void requestPause() {
        this.pauseRequestedLastTick = true;
    }

    public boolean cancelSegmentIfSafe() {
        if (this.isSafeToCancel()) {
            this.secretInternalSegmentCancel();
            return true;
        }
        return false;
    }

    @Override
    public boolean cancelEverything() {
        boolean doIt = this.isSafeToCancel();
        if (doIt) {
            this.secretInternalSegmentCancel();
        }
        this.baritone.getPathingControlManager().cancelEverything();
        return doIt;
    }

    public boolean calcFailedLastTick() {
        return this.calcFailedLastTick;
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    public void softCancelIfSafe() {
        Object object = this.pathPlanLock;
        synchronized (object) {
            this.getInProgress().ifPresent(AbstractNodeCostSearch::cancel);
            if (!this.isSafeToCancel()) {
                return;
            }
            this.current = null;
            this.next = null;
        }
        this.cancelRequested = true;
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    private void secretInternalSegmentCancel() {
        this.queuePathEvent(PathEvent.CANCELED);
        Object object = this.pathPlanLock;
        synchronized (object) {
            this.getInProgress().ifPresent(AbstractNodeCostSearch::cancel);
            if (this.current != null) {
                this.current = null;
                this.next = null;
                this.baritone.getInputOverrideHandler().clearAllKeys();
                this.baritone.getInputOverrideHandler().getBlockBreakHelper().stopBreakingBlock();
            }
        }
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    @Override
    public void forceCancel() {
        this.cancelEverything();
        this.secretInternalSegmentCancel();
        Object object = this.pathCalcLock;
        synchronized (object) {
            this.inProgress = null;
        }
    }

    public CalculationContext secretInternalGetCalculationContext() {
        return this.context;
    }

    @Override
    public Optional<Double> estimatedTicksToGoal() {
        double start;
        BetterBlockPos currentPos = this.ctx.playerFeet();
        if (this.goal == null || currentPos == null || this.startPosition == null) {
            return Optional.empty();
        }
        if (this.goal.isInGoal(this.ctx.playerFeet())) {
            this.resetEstimatedTicksToGoal();
            return Optional.of(0.0);
        }
        if (this.ticksElapsedSoFar == 0) {
            return Optional.empty();
        }
        double current = this.goal.heuristic(currentPos.x, currentPos.y, currentPos.z);
        if (current == (start = this.goal.heuristic(this.startPosition.x, this.startPosition.y, this.startPosition.z))) {
            return Optional.empty();
        }
        double eta = Math.abs(current - this.goal.heuristic()) * (double)this.ticksElapsedSoFar / Math.abs(start - current);
        return Optional.of(eta);
    }

    private void resetEstimatedTicksToGoal() {
        this.resetEstimatedTicksToGoal(this.expectedSegmentStart);
    }

    private void resetEstimatedTicksToGoal(BlockPos start) {
        this.resetEstimatedTicksToGoal(new BetterBlockPos(start));
    }

    private void resetEstimatedTicksToGoal(BetterBlockPos start) {
        this.ticksElapsedSoFar = 0;
        this.startPosition = start;
    }

    public BetterBlockPos pathStart() {
        BetterBlockPos feet = this.ctx.playerFeet();
        if (!MovementHelper.canWalkOn(this.ctx, feet.down())) {
            if (this.ctx.player().onGround) {
                double playerX = this.ctx.player().posX;
                double playerZ = this.ctx.player().posZ;
                ArrayList<BetterBlockPos> closest = new ArrayList<BetterBlockPos>();
                for (int dx = -1; dx <= 1; ++dx) {
                    for (int dz = -1; dz <= 1; ++dz) {
                        closest.add(new BetterBlockPos(feet.x + dx, feet.y, feet.z + dz));
                    }
                }
                closest.sort(Comparator.comparingDouble(pos -> ((double)pos.x + 0.5 - playerX) * ((double)pos.x + 0.5 - playerX) + ((double)pos.z + 0.5 - playerZ) * ((double)pos.z + 0.5 - playerZ)));
                for (int i = 0; i < 4; ++i) {
                    BetterBlockPos possibleSupport = (BetterBlockPos)closest.get(i);
                    double xDist = Math.abs((double)possibleSupport.x + 0.5 - playerX);
                    double zDist = Math.abs((double)possibleSupport.z + 0.5 - playerZ);
                    if (xDist > 0.8 && zDist > 0.8 || !MovementHelper.canWalkOn(this.ctx, possibleSupport.down()) || !MovementHelper.canWalkThrough(this.ctx, possibleSupport) || !MovementHelper.canWalkThrough(this.ctx, possibleSupport.up())) continue;
                    return possibleSupport;
                }
            } else if (MovementHelper.canWalkOn(this.ctx, feet.down().down())) {
                return feet.down();
            }
        }
        return feet;
    }

    private void findPathInNewThread(BlockPos start, boolean talkAboutIt, CalculationContext context) {
        long failureTimeout;
        long primaryTimeout;
        if (!Thread.holdsLock(this.pathCalcLock)) {
            throw new IllegalStateException("Must be called with synchronization on pathCalcLock");
        }
        if (this.inProgress != null) {
            throw new IllegalStateException("Already doing it");
        }
        if (!context.safeForThreadedUse) {
            throw new IllegalStateException("Improper context thread safety level");
        }
        Goal goal = this.goal;
        if (goal == null) {
            this.logDebug("no goal");
            return;
        }
        if (this.current == null) {
            primaryTimeout = (Long)Baritone.settings().primaryTimeoutMS.value;
            failureTimeout = (Long)Baritone.settings().failureTimeoutMS.value;
        } else {
            primaryTimeout = (Long)Baritone.settings().planAheadPrimaryTimeoutMS.value;
            failureTimeout = (Long)Baritone.settings().planAheadFailureTimeoutMS.value;
        }
        AbstractNodeCostSearch pathfinder = PathingBehavior.createPathfinder(start, goal, this.current == null ? null : this.current.getPath(), context);
        if (!Objects.equals(pathfinder.getGoal(), goal)) {
            this.logDebug("Simplifying " + goal.getClass() + " to GoalXZ due to distance");
        }
        this.inProgress = pathfinder;
        Baritone.getExecutor().execute(() -> {
            if (talkAboutIt) {
                this.logDebug("Starting to search for path from " + start + " to " + goal);
            }
            PathCalculationResult calcResult = pathfinder.calculate(primaryTimeout, failureTimeout);
            Object object = this.pathPlanLock;
            synchronized (object) {
                Optional<PathExecutor> executor = calcResult.getPath().map(p -> new PathExecutor(this, (IPath)p));
                if (this.current == null) {
                    if (executor.isPresent()) {
                        if (executor.get().getPath().positions().contains(this.expectedSegmentStart)) {
                            this.queuePathEvent(PathEvent.CALC_FINISHED_NOW_EXECUTING);
                            this.current = executor.get();
                            this.resetEstimatedTicksToGoal(start);
                        } else {
                            this.logDebug("Warning: discarding orphan path segment with incorrect start");
                        }
                    } else if (calcResult.getType() != PathCalculationResult.Type.CANCELLATION && calcResult.getType() != PathCalculationResult.Type.EXCEPTION) {
                        this.queuePathEvent(PathEvent.CALC_FAILED);
                    }
                } else if (this.next == null) {
                    if (executor.isPresent()) {
                        if (executor.get().getPath().getSrc().equals(this.current.getPath().getDest())) {
                            this.queuePathEvent(PathEvent.NEXT_SEGMENT_CALC_FINISHED);
                            this.next = executor.get();
                        } else {
                            this.logDebug("Warning: discarding orphan next segment with incorrect start");
                        }
                    } else {
                        this.queuePathEvent(PathEvent.NEXT_CALC_FAILED);
                    }
                } else {
                    this.logDirect("Warning: PathingBehaivor illegal state! Discarding invalid path!");
                }
                if (talkAboutIt && this.current != null && this.current.getPath() != null) {
                    if (goal.isInGoal(this.current.getPath().getDest())) {
                        this.logDebug("Finished finding a path from " + start + " to " + goal + ". " + this.current.getPath().getNumNodesConsidered() + " nodes considered");
                    } else {
                        this.logDebug("Found path segment from " + start + " towards " + goal + ". " + this.current.getPath().getNumNodesConsidered() + " nodes considered");
                    }
                }
                Object object2 = this.pathCalcLock;
                synchronized (object2) {
                    this.inProgress = null;
                }
            }
        });
    }

    private static AbstractNodeCostSearch createPathfinder(BlockPos start, Goal goal, IPath previous, CalculationContext context) {
        BlockPos pos;
        Goal transformed = goal;
        if (((Boolean)Baritone.settings().simplifyUnloadedYCoord.value).booleanValue() && goal instanceof IGoalRenderPos && !context.bsi.worldContainsLoadedChunk((pos = ((IGoalRenderPos)((Object)goal)).getGoalPos()).getX(), pos.getZ())) {
            transformed = new GoalXZ(pos.getX(), pos.getZ());
        }
        Favoring favoring = new Favoring(context.getBaritone().getPlayerContext(), previous, context);
        return new AStarPathFinder(start.getX(), start.getY(), start.getZ(), transformed, favoring, context);
    }

    @Override
    public void onRenderPass(RenderEvent event) {
        PathRenderer.render(event, this);
    }
}

