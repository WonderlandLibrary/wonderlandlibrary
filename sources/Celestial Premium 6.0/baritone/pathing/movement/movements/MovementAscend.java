/*
 * Decompiled with CFR 0.150.
 */
package baritone.pathing.movement.movements;

import baritone.Baritone;
import baritone.api.IBaritone;
import baritone.api.pathing.movement.MovementStatus;
import baritone.api.utils.BetterBlockPos;
import baritone.api.utils.input.Input;
import baritone.pathing.movement.CalculationContext;
import baritone.pathing.movement.Movement;
import baritone.pathing.movement.MovementHelper;
import baritone.pathing.movement.MovementState;
import baritone.utils.BlockStateInterface;
import com.google.common.collect.ImmutableSet;
import java.util.Set;
import net.minecraft.block.BlockFalling;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.util.EnumFacing;

public class MovementAscend
extends Movement {
    private int ticksWithoutPlacement = 0;

    public MovementAscend(IBaritone baritone, BetterBlockPos src, BetterBlockPos dest) {
        super(baritone, src, dest, new BetterBlockPos[]{dest, src.up(2), dest.up()}, dest.down());
    }

    @Override
    public void reset() {
        super.reset();
        this.ticksWithoutPlacement = 0;
    }

    @Override
    public double calculateCost(CalculationContext context) {
        return MovementAscend.cost(context, this.src.x, this.src.y, this.src.z, this.dest.x, this.dest.z);
    }

    @Override
    protected Set<BetterBlockPos> calculateValidPositions() {
        BetterBlockPos prior = new BetterBlockPos(this.src.subtract(this.getDirection()).up());
        return ImmutableSet.of(this.src, this.src.up(), this.dest, prior, prior.up());
    }

    public static double cost(CalculationContext context, int x, int y, int z, int destX, int destZ) {
        double walk;
        IBlockState toPlace = context.get(destX, y, destZ);
        double additionalPlacementCost = 0.0;
        if (!MovementHelper.canWalkOn(context.bsi, destX, y, destZ, toPlace)) {
            additionalPlacementCost = context.costOfPlacingAt(destX, y, destZ, toPlace);
            if (additionalPlacementCost >= 1000000.0) {
                return 1000000.0;
            }
            if (!MovementHelper.isReplaceable(destX, y, destZ, toPlace, context.bsi)) {
                return 1000000.0;
            }
            boolean foundPlaceOption = false;
            for (int i = 0; i < 5; ++i) {
                int againstX = destX + HORIZONTALS_BUT_ALSO_DOWN_____SO_EVERY_DIRECTION_EXCEPT_UP[i].getXOffset();
                int againstY = y + HORIZONTALS_BUT_ALSO_DOWN_____SO_EVERY_DIRECTION_EXCEPT_UP[i].getYOffset();
                int againstZ = destZ + HORIZONTALS_BUT_ALSO_DOWN_____SO_EVERY_DIRECTION_EXCEPT_UP[i].getZOffset();
                if (againstX == x && againstZ == z || !MovementHelper.canPlaceAgainst(context.bsi, againstX, againstY, againstZ)) continue;
                foundPlaceOption = true;
                break;
            }
            if (!foundPlaceOption) {
                return 1000000.0;
            }
        }
        IBlockState srcUp2 = context.get(x, y + 2, z);
        if (context.get(x, y + 3, z).getBlock() instanceof BlockFalling && (MovementHelper.canWalkThrough(context.bsi, x, y + 1, z) || !(srcUp2.getBlock() instanceof BlockFalling))) {
            return 1000000.0;
        }
        IBlockState srcDown = context.get(x, y - 1, z);
        if (srcDown.getBlock() == Blocks.LADDER || srcDown.getBlock() == Blocks.VINE) {
            return 1000000.0;
        }
        boolean jumpingFromBottomSlab = MovementHelper.isBottomSlab(srcDown);
        boolean jumpingToBottomSlab = MovementHelper.isBottomSlab(toPlace);
        if (jumpingFromBottomSlab && !jumpingToBottomSlab) {
            return 1000000.0;
        }
        if (jumpingToBottomSlab) {
            if (jumpingFromBottomSlab) {
                walk = Math.max(JUMP_ONE_BLOCK_COST, 4.63284688441047);
                walk += context.jumpPenalty;
            } else {
                walk = 4.63284688441047;
            }
        } else {
            walk = toPlace.getBlock() == Blocks.SOUL_SAND ? 9.26569376882094 : Math.max(JUMP_ONE_BLOCK_COST, 4.63284688441047);
            walk += context.jumpPenalty;
        }
        double totalCost = walk + additionalPlacementCost;
        totalCost += MovementHelper.getMiningDurationTicks(context, x, y + 2, z, srcUp2, false);
        if (totalCost >= 1000000.0) {
            return 1000000.0;
        }
        if ((totalCost += MovementHelper.getMiningDurationTicks(context, destX, y + 1, destZ, false)) >= 1000000.0) {
            return 1000000.0;
        }
        return totalCost += MovementHelper.getMiningDurationTicks(context, destX, y + 2, destZ, true);
    }

    @Override
    public MovementState updateState(MovementState state) {
        if (this.ctx.playerFeet().y < this.src.y) {
            return state.setStatus(MovementStatus.UNREACHABLE);
        }
        super.updateState(state);
        if (state.getStatus() != MovementStatus.RUNNING) {
            return state;
        }
        if (this.ctx.playerFeet().equals(this.dest) || this.ctx.playerFeet().equals(this.dest.add(this.getDirection().down()))) {
            return state.setStatus(MovementStatus.SUCCESS);
        }
        IBlockState jumpingOnto = BlockStateInterface.get(this.ctx, this.positionToPlace);
        if (!MovementHelper.canWalkOn(this.ctx, this.positionToPlace, jumpingOnto)) {
            ++this.ticksWithoutPlacement;
            if (MovementHelper.attemptToPlaceABlock(state, this.baritone, this.dest.down(), false, true) == MovementHelper.PlaceResult.READY_TO_PLACE) {
                state.setInput(Input.SNEAK, true);
                if (this.ctx.player().isSneaking()) {
                    state.setInput(Input.CLICK_RIGHT, true);
                }
            }
            if (this.ticksWithoutPlacement > 10) {
                state.setInput(Input.MOVE_BACK, true);
            }
            return state;
        }
        MovementHelper.moveTowards(this.ctx, state, this.dest);
        if (MovementHelper.isBottomSlab(jumpingOnto) && !MovementHelper.isBottomSlab(BlockStateInterface.get(this.ctx, this.src.down()))) {
            return state;
        }
        if (((Boolean)Baritone.settings().assumeStep.value).booleanValue() || this.ctx.playerFeet().equals(this.src.up())) {
            return state;
        }
        int xAxis = Math.abs(this.src.getX() - this.dest.getX());
        int zAxis = Math.abs(this.src.getZ() - this.dest.getZ());
        double flatDistToNext = (double)xAxis * Math.abs((double)this.dest.getX() + 0.5 - this.ctx.player().posX) + (double)zAxis * Math.abs((double)this.dest.getZ() + 0.5 - this.ctx.player().posZ);
        double sideDist = (double)zAxis * Math.abs((double)this.dest.getX() + 0.5 - this.ctx.player().posX) + (double)xAxis * Math.abs((double)this.dest.getZ() + 0.5 - this.ctx.player().posZ);
        double lateralMotion = (double)xAxis * this.ctx.player().motionZ + (double)zAxis * this.ctx.player().motionX;
        if (Math.abs(lateralMotion) > 0.1) {
            return state;
        }
        if (this.headBonkClear()) {
            return state.setInput(Input.JUMP, true);
        }
        if (flatDistToNext > 1.2 || sideDist > 0.2) {
            return state;
        }
        return state.setInput(Input.JUMP, true);
    }

    public boolean headBonkClear() {
        BetterBlockPos startUp = this.src.up(2);
        for (int i = 0; i < 4; ++i) {
            BetterBlockPos check = startUp.offset(EnumFacing.byHorizontalIndex(i));
            if (MovementHelper.canWalkThrough(this.ctx, check)) continue;
            return false;
        }
        return true;
    }

    @Override
    public boolean safeToCancel(MovementState state) {
        return state.getStatus() != MovementStatus.RUNNING || this.ticksWithoutPlacement == 0;
    }
}

