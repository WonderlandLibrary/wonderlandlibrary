/*
 * Decompiled with CFR 0.150.
 */
package baritone.process;

import baritone.Baritone;
import baritone.api.process.PathingCommand;
import baritone.api.process.PathingCommandType;
import baritone.api.utils.input.Input;
import baritone.pathing.movement.Movement;
import baritone.pathing.movement.MovementHelper;
import baritone.pathing.movement.MovementState;
import baritone.pathing.path.PathExecutor;
import baritone.utils.BaritoneProcessHelper;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.chunk.EmptyChunk;
import org.celestial.client.Celestial;
import org.celestial.client.feature.impl.player.AutoEat;

public final class BackfillProcess
extends BaritoneProcessHelper {
    public HashMap<BlockPos, IBlockState> blocksToReplace = new HashMap();

    public BackfillProcess(Baritone baritone) {
        super(baritone);
    }

    @Override
    public boolean isActive() {
        if (this.ctx.player() == null || this.ctx.world() == null) {
            return false;
        }
        if (!((Boolean)Baritone.settings().backfill.value).booleanValue()) {
            return false;
        }
        if (((Boolean)Baritone.settings().allowParkour.value).booleanValue()) {
            this.logDirect("Backfill cannot be used with allowParkour true");
            Baritone.settings().backfill.value = false;
            return false;
        }
        this.amIBreakingABlockHMMMMMMM();
        for (BlockPos pos : new ArrayList<BlockPos>(this.blocksToReplace.keySet())) {
            if (!(this.ctx.world().getChunk(pos) instanceof EmptyChunk)) continue;
            this.blocksToReplace.remove(pos);
        }
        this.baritone.getInputOverrideHandler().clearAllKeys();
        return !this.toFillIn().isEmpty();
    }

    @Override
    public PathingCommand onTick(boolean calcFailed, boolean isSafeToCancel) {
        if (Celestial.instance.featureManager.getFeatureByClass(AutoEat.class).getState() && AutoEat.isEating) {
            return null;
        }
        if (!isSafeToCancel) {
            return new PathingCommand(null, PathingCommandType.REQUEST_PAUSE);
        }
        this.baritone.getInputOverrideHandler().clearAllKeys();
        block5: for (BlockPos toPlace : this.toFillIn()) {
            MovementState fake = new MovementState();
            switch (MovementHelper.attemptToPlaceABlock(fake, this.baritone, toPlace, false, false)) {
                case NO_OPTION: {
                    continue block5;
                }
                case READY_TO_PLACE: {
                    this.baritone.getInputOverrideHandler().setInputForceState(Input.CLICK_RIGHT, true);
                    return new PathingCommand(null, PathingCommandType.REQUEST_PAUSE);
                }
                case ATTEMPTING: {
                    this.baritone.getLookBehavior().updateTarget(fake.getTarget().getRotation().get(), true);
                    return new PathingCommand(null, PathingCommandType.REQUEST_PAUSE);
                }
            }
            throw new IllegalStateException();
        }
        return new PathingCommand(null, PathingCommandType.DEFER);
    }

    private void amIBreakingABlockHMMMMMMM() {
        if (!this.ctx.getSelectedBlock().isPresent()) {
            return;
        }
        this.blocksToReplace.put(this.ctx.getSelectedBlock().get(), this.ctx.world().getBlockState(this.ctx.getSelectedBlock().get()));
    }

    public List<BlockPos> toFillIn() {
        return this.blocksToReplace.keySet().stream().filter(pos -> this.ctx.world().getBlockState((BlockPos)pos).getBlock() == Blocks.AIR).filter(pos -> this.ctx.world().mayPlace(Blocks.DIRT, (BlockPos)pos, false, EnumFacing.UP, null)).filter(pos -> !this.partOfCurrentMovement((BlockPos)pos)).sorted(Comparator.comparingDouble(this.ctx.player()::getDistanceSq).reversed()).collect(Collectors.toList());
    }

    private boolean partOfCurrentMovement(BlockPos pos) {
        PathExecutor exec = this.baritone.getPathingBehavior().getCurrent();
        if (exec == null || exec.finished() || exec.failed()) {
            return false;
        }
        Movement movement = (Movement)exec.getPath().movements().get(exec.getPosition());
        return Arrays.asList(movement.toBreakAll()).contains(pos);
    }

    @Override
    public void onLostControl() {
        if (this.blocksToReplace != null && !this.blocksToReplace.isEmpty()) {
            this.blocksToReplace.clear();
        }
    }

    @Override
    public String displayName0() {
        return "Backfill";
    }

    @Override
    public boolean isTemporary() {
        return true;
    }

    @Override
    public double priority() {
        return 5.0;
    }
}

