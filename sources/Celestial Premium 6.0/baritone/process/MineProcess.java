/*
 * Decompiled with CFR 0.150.
 */
package baritone.process;

import baritone.Baritone;
import baritone.api.pathing.goals.Goal;
import baritone.api.pathing.goals.GoalBlock;
import baritone.api.pathing.goals.GoalComposite;
import baritone.api.pathing.goals.GoalRunAway;
import baritone.api.pathing.goals.GoalTwoBlocks;
import baritone.api.process.IMineProcess;
import baritone.api.process.PathingCommand;
import baritone.api.process.PathingCommandType;
import baritone.api.utils.BetterBlockPos;
import baritone.api.utils.BlockOptionalMeta;
import baritone.api.utils.BlockOptionalMetaLookup;
import baritone.api.utils.BlockUtils;
import baritone.api.utils.Rotation;
import baritone.api.utils.RotationUtils;
import baritone.api.utils.input.Input;
import baritone.cache.CachedChunk;
import baritone.cache.WorldScanner;
import baritone.pathing.movement.CalculationContext;
import baritone.pathing.movement.MovementHelper;
import baritone.utils.BaritoneProcessHelper;
import baritone.utils.BlockStateInterface;
import baritone.utils.NotificationHelper;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;
import net.minecraft.block.Block;
import net.minecraft.block.BlockAir;
import net.minecraft.block.BlockFalling;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3i;
import org.celestial.client.Celestial;
import org.celestial.client.feature.impl.player.AutoEat;

public final class MineProcess
extends BaritoneProcessHelper
implements IMineProcess {
    private static final int ORE_LOCATIONS_COUNT = 64;
    private BlockOptionalMetaLookup filter;
    private List<BlockPos> knownOreLocations;
    private List<BlockPos> blacklist;
    private Map<BlockPos, Long> anticipatedDrops;
    private BlockPos branchPoint;
    private GoalRunAway branchPointRunaway;
    private int desiredQuantity;
    private int tickCount;

    public MineProcess(Baritone baritone) {
        super(baritone);
    }

    @Override
    public boolean isActive() {
        return this.filter != null;
    }

    @Override
    public PathingCommand onTick(boolean calcFailed, boolean isSafeToCancel) {
        PathingCommand command;
        if (Celestial.instance.featureManager.getFeatureByClass(AutoEat.class).getState() && AutoEat.isEating) {
            return null;
        }
        if (this.desiredQuantity > 0) {
            int curr = this.ctx.player().inventory.mainInventory.stream().filter(stack -> this.filter.has((ItemStack)stack)).mapToInt(ItemStack::getCount).sum();
            System.out.println("Currently have " + curr + " valid items");
            if (curr >= this.desiredQuantity) {
                this.logDirect("Have " + curr + " valid items");
                this.cancel();
                return null;
            }
        }
        if (calcFailed) {
            if (!this.knownOreLocations.isEmpty() && ((Boolean)Baritone.settings().blacklistClosestOnFailure.value).booleanValue()) {
                this.logDirect("Unable to find any path to " + this.filter + ", blacklisting presumably unreachable closest instance...");
                if (((Boolean)Baritone.settings().desktopNotifications.value).booleanValue() && ((Boolean)Baritone.settings().notificationOnMineFail.value).booleanValue()) {
                    NotificationHelper.notify("Unable to find any path to " + this.filter + ", blacklisting presumably unreachable closest instance...", true);
                }
                this.knownOreLocations.stream().min(Comparator.comparingDouble(this.ctx.player()::getDistanceSq)).ifPresent(this.blacklist::add);
                this.knownOreLocations.removeIf(this.blacklist::contains);
            } else {
                this.logDirect("Unable to find any path to " + this.filter + ", canceling mine");
                if (((Boolean)Baritone.settings().desktopNotifications.value).booleanValue() && ((Boolean)Baritone.settings().notificationOnMineFail.value).booleanValue()) {
                    NotificationHelper.notify("Unable to find any path to " + this.filter + ", canceling mine", true);
                }
                this.cancel();
                return null;
            }
        }
        if (!((Boolean)Baritone.settings().allowBreak.value).booleanValue()) {
            this.logDirect("Unable to mine when allowBreak is false!");
            this.cancel();
            return null;
        }
        this.updateLoucaSystem();
        int mineGoalUpdateInterval = (Integer)Baritone.settings().mineGoalUpdateInterval.value;
        ArrayList<BlockPos> curr = new ArrayList<BlockPos>(this.knownOreLocations);
        if (mineGoalUpdateInterval != 0 && this.tickCount++ % mineGoalUpdateInterval == 0) {
            CalculationContext context = new CalculationContext(this.baritone, true);
            Baritone.getExecutor().execute(() -> this.rescan(curr, context));
        }
        if (((Boolean)Baritone.settings().legitMine.value).booleanValue()) {
            this.addNearby();
        }
        Optional<BlockPos> shaft = curr.stream().filter(pos -> pos.getX() == this.ctx.playerFeet().getX() && pos.getZ() == this.ctx.playerFeet().getZ()).filter(pos -> pos.getY() >= this.ctx.playerFeet().getY()).filter(pos -> !(BlockStateInterface.get(this.ctx, pos).getBlock() instanceof BlockAir)).min(Comparator.comparingDouble(this.ctx.player()::getDistanceSq));
        this.baritone.getInputOverrideHandler().clearAllKeys();
        if (shaft.isPresent() && this.ctx.player().onGround) {
            Optional<Rotation> rot;
            BlockPos pos2 = shaft.get();
            IBlockState state = this.baritone.bsi.get0(pos2);
            if (!MovementHelper.avoidBreaking(this.baritone.bsi, pos2.getX(), pos2.getY(), pos2.getZ(), state) && (rot = RotationUtils.reachable(this.ctx, pos2)).isPresent() && isSafeToCancel) {
                this.baritone.getLookBehavior().updateTarget(rot.get(), true);
                MovementHelper.switchToBestToolFor(this.ctx, this.ctx.world().getBlockState(pos2));
                if (this.ctx.isLookingAt(pos2) || this.ctx.playerRotations().isReallyCloseTo(rot.get())) {
                    this.baritone.getInputOverrideHandler().setInputForceState(Input.CLICK_LEFT, true);
                }
                return new PathingCommand(null, PathingCommandType.REQUEST_PAUSE);
            }
        }
        if ((command = this.updateGoal()) == null) {
            this.cancel();
            return null;
        }
        return command;
    }

    private void updateLoucaSystem() {
        HashMap<BlockPos, Long> copy = new HashMap<BlockPos, Long>(this.anticipatedDrops);
        this.ctx.getSelectedBlock().ifPresent(pos -> {
            if (this.knownOreLocations.contains(pos)) {
                copy.put((BlockPos)pos, System.currentTimeMillis() + (Long)Baritone.settings().mineDropLoiterDurationMSThanksLouca.value);
            }
        });
        for (BlockPos pos2 : this.anticipatedDrops.keySet()) {
            if ((Long)copy.get(pos2) >= System.currentTimeMillis()) continue;
            copy.remove(pos2);
        }
        this.anticipatedDrops = copy;
    }

    @Override
    public void onLostControl() {
        this.mine(0, (BlockOptionalMetaLookup)null);
    }

    @Override
    public String displayName0() {
        return "Mine " + this.filter;
    }

    private PathingCommand updateGoal() {
        boolean legit = (Boolean)Baritone.settings().legitMine.value;
        List<BlockPos> locs = this.knownOreLocations;
        if (!locs.isEmpty()) {
            CalculationContext context = new CalculationContext(this.baritone);
            List<BlockPos> locs2 = MineProcess.prune(context, new ArrayList<BlockPos>(locs), this.filter, 64, this.blacklist, this.droppedItemsScan());
            GoalComposite goal = new GoalComposite((Goal[])locs2.stream().map(loc -> this.coalesce((BlockPos)loc, locs2, context)).toArray(Goal[]::new));
            this.knownOreLocations = locs2;
            return new PathingCommand(goal, legit ? PathingCommandType.FORCE_REVALIDATE_GOAL_AND_PATH : PathingCommandType.REVALIDATE_GOAL_AND_PATH);
        }
        if (!legit) {
            return null;
        }
        int y = (Integer)Baritone.settings().legitMineYLevel.value;
        if (this.branchPoint == null) {
            this.branchPoint = this.ctx.playerFeet();
        }
        if (this.branchPointRunaway == null) {
            this.branchPointRunaway = new GoalRunAway(1.0, y, new BlockPos[]{this.branchPoint}){

                @Override
                public boolean isInGoal(int x, int y, int z) {
                    return false;
                }

                @Override
                public double heuristic() {
                    return Double.NEGATIVE_INFINITY;
                }
            };
        }
        return new PathingCommand(this.branchPointRunaway, PathingCommandType.REVALIDATE_GOAL_AND_PATH);
    }

    private void rescan(List<BlockPos> already, CalculationContext context) {
        if (this.filter == null) {
            return;
        }
        if (((Boolean)Baritone.settings().legitMine.value).booleanValue()) {
            return;
        }
        List<BlockPos> dropped = this.droppedItemsScan();
        List<BlockPos> locs = MineProcess.searchWorld(context, this.filter, 64, already, this.blacklist, dropped);
        locs.addAll(dropped);
        if (locs.isEmpty()) {
            this.logDirect("No locations for " + this.filter + " known, cancelling");
            if (((Boolean)Baritone.settings().desktopNotifications.value).booleanValue() && ((Boolean)Baritone.settings().notificationOnMineFail.value).booleanValue()) {
                NotificationHelper.notify("No locations for " + this.filter + " known, cancelling", true);
            }
            this.cancel();
            return;
        }
        this.knownOreLocations = locs;
    }

    private boolean internalMiningGoal(BlockPos pos, CalculationContext context, List<BlockPos> locs) {
        if (locs.contains(pos)) {
            return true;
        }
        IBlockState state = context.bsi.get0(pos);
        if (((Boolean)Baritone.settings().internalMiningAirException.value).booleanValue() && state.getBlock() instanceof BlockAir) {
            return true;
        }
        return this.filter.has(state) && MineProcess.plausibleToBreak(context, pos);
    }

    private Goal coalesce(BlockPos loc, List<BlockPos> locs, CalculationContext context) {
        boolean assumeVerticalShaftMine;
        boolean bl = assumeVerticalShaftMine = !(this.baritone.bsi.get0(loc.up()).getBlock() instanceof BlockFalling);
        if (!((Boolean)Baritone.settings().forceInternalMining.value).booleanValue()) {
            if (assumeVerticalShaftMine) {
                return new GoalThreeBlocks(loc);
            }
            return new GoalTwoBlocks(loc);
        }
        boolean upwardGoal = this.internalMiningGoal(loc.up(), context, locs);
        boolean downwardGoal = this.internalMiningGoal(loc.down(), context, locs);
        boolean doubleDownwardGoal = this.internalMiningGoal(loc.down(2), context, locs);
        if (upwardGoal == downwardGoal) {
            if (doubleDownwardGoal && assumeVerticalShaftMine) {
                return new GoalThreeBlocks(loc);
            }
            return new GoalTwoBlocks(loc);
        }
        if (upwardGoal) {
            return new GoalBlock(loc);
        }
        if (doubleDownwardGoal && assumeVerticalShaftMine) {
            return new GoalTwoBlocks(loc.down());
        }
        return new GoalBlock(loc.down());
    }

    public List<BlockPos> droppedItemsScan() {
        if (org.celestial.client.feature.impl.misc.Baritone.noBack.getCurrentValue()) {
            return Collections.emptyList();
        }
        if (!((Boolean)Baritone.settings().mineScanDroppedItems.value).booleanValue()) {
            return Collections.emptyList();
        }
        ArrayList<BlockPos> ret = new ArrayList<BlockPos>();
        for (Entity entity : this.ctx.world().loadedEntityList) {
            EntityItem ei;
            if (!(entity instanceof EntityItem) || !this.filter.has((ei = (EntityItem)entity).getItem())) continue;
            ret.add(new BlockPos(entity));
        }
        ret.addAll(this.anticipatedDrops.keySet());
        return ret;
    }

    public static List<BlockPos> searchWorld(CalculationContext ctx, BlockOptionalMetaLookup filter, int max, List<BlockPos> alreadyKnown, List<BlockPos> blacklist, List<BlockPos> dropped) {
        List<BlockPos> locs = new ArrayList<BlockPos>();
        ArrayList<Block> untracked = new ArrayList<Block>();
        for (BlockOptionalMeta bom : filter.blocks()) {
            Block block = bom.getBlock();
            if (CachedChunk.BLOCKS_TO_KEEP_TRACK_OF.contains(block)) {
                BetterBlockPos pf = ctx.baritone.getPlayerContext().playerFeet();
                locs.addAll(ctx.worldData.getCachedWorld().getLocationsOf(BlockUtils.blockToString(block), (Integer)Baritone.settings().maxCachedWorldScanCount.value, pf.x, pf.z, 2));
                continue;
            }
            untracked.add(block);
        }
        locs = MineProcess.prune(ctx, locs, filter, max, blacklist, dropped);
        if (!untracked.isEmpty() || ((Boolean)Baritone.settings().extendCacheOnThreshold.value).booleanValue() && locs.size() < max) {
            locs.addAll(WorldScanner.INSTANCE.scanChunkRadius(ctx.getBaritone().getPlayerContext(), filter, max, 10, 32));
        }
        locs.addAll(alreadyKnown);
        return MineProcess.prune(ctx, locs, filter, max, blacklist, dropped);
    }

    private void addNearby() {
        List<BlockPos> dropped = this.droppedItemsScan();
        this.knownOreLocations.addAll(dropped);
        BetterBlockPos playerFeet = this.ctx.playerFeet();
        BlockStateInterface bsi = new BlockStateInterface(this.ctx);
        int searchDist = 10;
        double fakedBlockReachDistance = 20.0;
        for (int x = playerFeet.getX() - searchDist; x <= playerFeet.getX() + searchDist; ++x) {
            for (int y = playerFeet.getY() - searchDist; y <= playerFeet.getY() + searchDist; ++y) {
                for (int z = playerFeet.getZ() - searchDist; z <= playerFeet.getZ() + searchDist; ++z) {
                    if (!this.filter.has(bsi.get0(x, y, z))) continue;
                    BlockPos pos = new BlockPos(x, y, z);
                    if ((!((Boolean)Baritone.settings().legitMineIncludeDiagonals.value).booleanValue() || !this.knownOreLocations.stream().anyMatch(ore -> ore.distanceSq(pos) <= 2.0)) && !RotationUtils.reachable(this.ctx.player(), pos, fakedBlockReachDistance).isPresent()) continue;
                    this.knownOreLocations.add(pos);
                }
            }
        }
        this.knownOreLocations = MineProcess.prune(new CalculationContext(this.baritone), this.knownOreLocations, this.filter, 64, this.blacklist, dropped);
    }

    private static List<BlockPos> prune(CalculationContext ctx, List<BlockPos> locs2, BlockOptionalMetaLookup filter, int max, List<BlockPos> blacklist, List<BlockPos> dropped) {
        dropped.removeIf(drop -> {
            for (BlockPos pos : locs2) {
                if (!(pos.distanceSq((Vec3i)drop) <= 9.0) || !filter.has(ctx.get(pos.getX(), pos.getY(), pos.getZ())) || !MineProcess.plausibleToBreak(ctx, pos)) continue;
                return true;
            }
            return false;
        });
        List<BlockPos> locs = locs2.stream().distinct().filter(pos -> !ctx.bsi.worldContainsLoadedChunk(pos.getX(), pos.getZ()) || filter.has(ctx.get(pos.getX(), pos.getY(), pos.getZ())) || dropped.contains(pos)).filter(pos -> MineProcess.plausibleToBreak(ctx, pos)).filter(pos -> {
            if (((Boolean)Baritone.settings().allowOnlyExposedOres.value).booleanValue()) {
                return MineProcess.isNextToAir(ctx, pos);
            }
            return true;
        }).filter(pos -> pos.getY() >= (Integer)Baritone.settings().minYLevelWhileMining.value).filter(pos -> !blacklist.contains(pos)).sorted(Comparator.comparingDouble(ctx.getBaritone().getPlayerContext().player()::getDistanceSq)).collect(Collectors.toList());
        if (locs.size() > max) {
            return locs.subList(0, max);
        }
        return locs;
    }

    public static boolean isNextToAir(CalculationContext ctx, BlockPos pos) {
        int radius = (Integer)Baritone.settings().allowOnlyExposedOresDistance.value;
        for (int dx = -radius; dx <= radius; ++dx) {
            for (int dy = -radius; dy <= radius; ++dy) {
                for (int dz = -radius; dz <= radius; ++dz) {
                    if (Math.abs(dx) + Math.abs(dy) + Math.abs(dz) > radius || !MovementHelper.isTransparent(ctx.getBlock(pos.getX() + dx, pos.getY() + dy, pos.getZ() + dz))) continue;
                    return true;
                }
            }
        }
        return false;
    }

    public static boolean plausibleToBreak(CalculationContext ctx, BlockPos pos) {
        if (MovementHelper.getMiningDurationTicks(ctx, pos.getX(), pos.getY(), pos.getZ(), ctx.bsi.get0(pos), true) >= 1000000.0) {
            return false;
        }
        return ctx.bsi.get0(pos.up()).getBlock() != Blocks.BEDROCK || ctx.bsi.get0(pos.down()).getBlock() != Blocks.BEDROCK;
    }

    @Override
    public void mineByName(int quantity, String ... blocks) {
        this.mine(quantity, new BlockOptionalMetaLookup(blocks));
    }

    @Override
    public void mine(int quantity, BlockOptionalMetaLookup filter) {
        this.filter = filter;
        if (filter != null && !((Boolean)Baritone.settings().allowBreak.value).booleanValue()) {
            this.logDirect("Unable to mine when allowBreak is false!");
            this.mine(quantity, (BlockOptionalMetaLookup)null);
            return;
        }
        this.desiredQuantity = quantity;
        this.knownOreLocations = new ArrayList<BlockPos>();
        this.blacklist = new ArrayList<BlockPos>();
        this.branchPoint = null;
        this.branchPointRunaway = null;
        this.anticipatedDrops = new HashMap<BlockPos, Long>();
        if (filter != null) {
            this.rescan(new ArrayList<BlockPos>(), new CalculationContext(this.baritone));
        }
    }

    private static class GoalThreeBlocks
    extends GoalTwoBlocks {
        public GoalThreeBlocks(BlockPos pos) {
            super(pos);
        }

        @Override
        public boolean isInGoal(int x, int y, int z) {
            return x == this.x && (y == this.y || y == this.y - 1 || y == this.y - 2) && z == this.z;
        }

        @Override
        public double heuristic(int x, int y, int z) {
            int xDiff = x - this.x;
            int yDiff = y - this.y;
            int zDiff = z - this.z;
            return GoalBlock.calculate(xDiff, yDiff < -1 ? yDiff + 2 : (yDiff == -1 ? 0 : yDiff), zDiff);
        }
    }
}

