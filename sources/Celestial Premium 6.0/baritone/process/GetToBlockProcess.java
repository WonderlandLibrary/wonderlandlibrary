/*
 * Decompiled with CFR 0.150.
 */
package baritone.process;

import baritone.Baritone;
import baritone.api.pathing.goals.Goal;
import baritone.api.pathing.goals.GoalBlock;
import baritone.api.pathing.goals.GoalComposite;
import baritone.api.pathing.goals.GoalGetToBlock;
import baritone.api.pathing.goals.GoalRunAway;
import baritone.api.pathing.goals.GoalTwoBlocks;
import baritone.api.process.IGetToBlockProcess;
import baritone.api.process.PathingCommand;
import baritone.api.process.PathingCommandType;
import baritone.api.utils.BlockOptionalMeta;
import baritone.api.utils.BlockOptionalMetaLookup;
import baritone.api.utils.Rotation;
import baritone.api.utils.RotationUtils;
import baritone.api.utils.input.Input;
import baritone.pathing.movement.CalculationContext;
import baritone.process.MineProcess;
import baritone.utils.BaritoneProcessHelper;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.inventory.ContainerPlayer;
import net.minecraft.util.math.BlockPos;

public final class GetToBlockProcess
extends BaritoneProcessHelper
implements IGetToBlockProcess {
    private BlockOptionalMeta gettingTo;
    private List<BlockPos> knownLocations;
    private List<BlockPos> blacklist;
    private BlockPos start;
    private int tickCount = 0;
    private int arrivalTickCount = 0;

    public GetToBlockProcess(Baritone baritone) {
        super(baritone);
    }

    @Override
    public void getToBlock(BlockOptionalMeta block) {
        this.onLostControl();
        this.gettingTo = block;
        this.start = this.ctx.playerFeet();
        this.blacklist = new ArrayList<BlockPos>();
        this.arrivalTickCount = 0;
        this.rescan(new ArrayList<BlockPos>(), new GetToBlockCalculationContext(false));
    }

    @Override
    public boolean isActive() {
        return this.gettingTo != null;
    }

    @Override
    public synchronized PathingCommand onTick(boolean calcFailed, boolean isSafeToCancel) {
        if (this.knownLocations == null) {
            this.rescan(new ArrayList<BlockPos>(), new GetToBlockCalculationContext(false));
        }
        if (this.knownLocations.isEmpty()) {
            if (((Boolean)Baritone.settings().exploreForBlocks.value).booleanValue() && !calcFailed) {
                return new PathingCommand(new GoalRunAway(1.0, new BlockPos[]{this.start}){

                    @Override
                    public boolean isInGoal(int x, int y, int z) {
                        return false;
                    }

                    @Override
                    public double heuristic() {
                        return Double.NEGATIVE_INFINITY;
                    }
                }, PathingCommandType.FORCE_REVALIDATE_GOAL_AND_PATH);
            }
            this.logDirect("No known locations of " + this.gettingTo + ", canceling GetToBlock");
            if (isSafeToCancel) {
                this.onLostControl();
            }
            return new PathingCommand(null, PathingCommandType.CANCEL_AND_SET_GOAL);
        }
        GoalComposite goal = new GoalComposite((Goal[])this.knownLocations.stream().map(this::createGoal).toArray(Goal[]::new));
        if (calcFailed) {
            if (((Boolean)Baritone.settings().blacklistClosestOnFailure.value).booleanValue()) {
                this.logDirect("Unable to find any path to " + this.gettingTo + ", blacklisting presumably unreachable closest instances...");
                this.blacklistClosest();
                return this.onTick(false, isSafeToCancel);
            }
            this.logDirect("Unable to find any path to " + this.gettingTo + ", canceling GetToBlock");
            if (isSafeToCancel) {
                this.onLostControl();
            }
            return new PathingCommand(goal, PathingCommandType.CANCEL_AND_SET_GOAL);
        }
        int mineGoalUpdateInterval = (Integer)Baritone.settings().mineGoalUpdateInterval.value;
        if (mineGoalUpdateInterval != 0 && this.tickCount++ % mineGoalUpdateInterval == 0) {
            ArrayList<BlockPos> current = new ArrayList<BlockPos>(this.knownLocations);
            GetToBlockCalculationContext context = new GetToBlockCalculationContext(true);
            Baritone.getExecutor().execute(() -> this.rescan(current, context));
        }
        if (goal.isInGoal(this.ctx.playerFeet()) && goal.isInGoal(this.baritone.getPathingBehavior().pathStart()) && isSafeToCancel) {
            if (this.rightClickOnArrival(this.gettingTo.getBlock())) {
                if (this.rightClick()) {
                    this.onLostControl();
                    return new PathingCommand(null, PathingCommandType.CANCEL_AND_SET_GOAL);
                }
            } else {
                this.onLostControl();
                return new PathingCommand(null, PathingCommandType.CANCEL_AND_SET_GOAL);
            }
        }
        return new PathingCommand(goal, PathingCommandType.REVALIDATE_GOAL_AND_PATH);
    }

    @Override
    public synchronized boolean blacklistClosest() {
        ArrayList<BlockPos> newBlacklist = new ArrayList<BlockPos>();
        this.knownLocations.stream().min(Comparator.comparingDouble(this.ctx.player()::getDistanceSq)).ifPresent(newBlacklist::add);
        block2: while (true) {
            block3: for (BlockPos known : this.knownLocations) {
                for (BlockPos blacklist : newBlacklist) {
                    if (!this.areAdjacent(known, blacklist)) continue;
                    newBlacklist.add(known);
                    this.knownLocations.remove(known);
                    continue block2;
                    continue block3;
                }
            }
            break;
        }
        switch (newBlacklist.size()) {
            default: 
        }
        this.logDebug("Blacklisting unreachable locations " + newBlacklist);
        this.blacklist.addAll(newBlacklist);
        return !newBlacklist.isEmpty();
    }

    private boolean areAdjacent(BlockPos posA, BlockPos posB) {
        int diffZ;
        int diffY;
        int diffX = Math.abs(posA.getX() - posB.getX());
        return diffX + (diffY = Math.abs(posA.getY() - posB.getY())) + (diffZ = Math.abs(posA.getZ() - posB.getZ())) == 1;
    }

    @Override
    public synchronized void onLostControl() {
        this.gettingTo = null;
        this.knownLocations = null;
        this.start = null;
        this.blacklist = null;
        this.baritone.getInputOverrideHandler().clearAllKeys();
    }

    @Override
    public String displayName0() {
        if (this.knownLocations.isEmpty()) {
            return "Exploring randomly to find " + this.gettingTo + ", no known locations";
        }
        return "Get To " + this.gettingTo + ", " + this.knownLocations.size() + " known locations";
    }

    private synchronized void rescan(List<BlockPos> known, CalculationContext context) {
        List<BlockPos> positions = MineProcess.searchWorld(context, new BlockOptionalMetaLookup(this.gettingTo), 64, known, this.blacklist, Collections.emptyList());
        positions.removeIf(this.blacklist::contains);
        this.knownLocations = positions;
    }

    private Goal createGoal(BlockPos pos) {
        if (this.walkIntoInsteadOfAdjacent(this.gettingTo.getBlock())) {
            return new GoalTwoBlocks(pos);
        }
        if (this.blockOnTopMustBeRemoved(this.gettingTo.getBlock()) && this.baritone.bsi.get0(pos.up()).isBlockNormalCube()) {
            return new GoalBlock(pos.up());
        }
        return new GoalGetToBlock(pos);
    }

    private boolean rightClick() {
        for (BlockPos pos : this.knownLocations) {
            Optional<Rotation> reachable = RotationUtils.reachable(this.ctx.player(), pos, this.ctx.playerController().getBlockReachDistance());
            if (!reachable.isPresent()) continue;
            this.baritone.getLookBehavior().updateTarget(reachable.get(), true);
            if (this.knownLocations.contains(this.ctx.getSelectedBlock().orElse(null))) {
                this.baritone.getInputOverrideHandler().setInputForceState(Input.CLICK_RIGHT, true);
                System.out.println(this.ctx.player().openContainer);
                if (!(this.ctx.player().openContainer instanceof ContainerPlayer)) {
                    return true;
                }
            }
            if (this.arrivalTickCount++ > 20) {
                this.logDirect("Right click timed out");
                return true;
            }
            return false;
        }
        this.logDirect("Arrived but failed to right click open");
        return true;
    }

    private boolean walkIntoInsteadOfAdjacent(Block block) {
        if (!((Boolean)Baritone.settings().enterPortal.value).booleanValue()) {
            return false;
        }
        return block == Blocks.PORTAL;
    }

    private boolean rightClickOnArrival(Block block) {
        if (!((Boolean)Baritone.settings().rightClickContainerOnArrival.value).booleanValue()) {
            return false;
        }
        return block == Blocks.CRAFTING_TABLE || block == Blocks.FURNACE || block == Blocks.ENDER_CHEST || block == Blocks.CHEST || block == Blocks.TRAPPED_CHEST;
    }

    private boolean blockOnTopMustBeRemoved(Block block) {
        if (!this.rightClickOnArrival(block)) {
            return false;
        }
        return block == Blocks.ENDER_CHEST || block == Blocks.CHEST || block == Blocks.TRAPPED_CHEST;
    }

    public class GetToBlockCalculationContext
    extends CalculationContext {
        public GetToBlockCalculationContext(boolean forUseOnAnotherThread) {
            super(GetToBlockProcess.this.baritone, forUseOnAnotherThread);
        }

        @Override
        public double breakCostMultiplierAt(int x, int y, int z, IBlockState current) {
            return 1.0;
        }
    }
}

