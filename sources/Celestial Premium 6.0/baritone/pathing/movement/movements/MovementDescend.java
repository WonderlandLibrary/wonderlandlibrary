/*
 * Decompiled with CFR 0.150.
 */
package baritone.pathing.movement.movements;

import baritone.api.IBaritone;
import baritone.api.pathing.movement.MovementStatus;
import baritone.api.utils.BetterBlockPos;
import baritone.api.utils.Rotation;
import baritone.api.utils.RotationUtils;
import baritone.api.utils.input.Input;
import baritone.pathing.movement.CalculationContext;
import baritone.pathing.movement.Movement;
import baritone.pathing.movement.MovementHelper;
import baritone.pathing.movement.MovementState;
import baritone.utils.BlockStateInterface;
import baritone.utils.pathing.MutableMoveResult;
import com.google.common.collect.ImmutableSet;
import java.util.Set;
import net.minecraft.block.Block;
import net.minecraft.block.BlockFalling;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.init.Blocks;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.math.Vec3i;

public class MovementDescend
extends Movement {
    private int numTicks = 0;

    public MovementDescend(IBaritone baritone, BetterBlockPos start, BetterBlockPos end) {
        super(baritone, start, end, new BetterBlockPos[]{end.up(2), end.up(), end}, end.down());
    }

    @Override
    public void reset() {
        super.reset();
        this.numTicks = 0;
    }

    @Override
    public double calculateCost(CalculationContext context) {
        MutableMoveResult result = new MutableMoveResult();
        MovementDescend.cost(context, this.src.x, this.src.y, this.src.z, this.dest.x, this.dest.z, result);
        if (result.y != this.dest.y) {
            return 1000000.0;
        }
        return result.cost;
    }

    @Override
    protected Set<BetterBlockPos> calculateValidPositions() {
        return ImmutableSet.of(this.src, this.dest.up(), this.dest);
    }

    public static void cost(CalculationContext context, int x, int y, int z, int destX, int destZ, MutableMoveResult res) {
        double totalCost = 0.0;
        IBlockState destDown = context.get(destX, y - 1, destZ);
        if ((totalCost += MovementHelper.getMiningDurationTicks(context, destX, y - 1, destZ, destDown, false)) >= 1000000.0) {
            return;
        }
        if ((totalCost += MovementHelper.getMiningDurationTicks(context, destX, y, destZ, false)) >= 1000000.0) {
            return;
        }
        if ((totalCost += MovementHelper.getMiningDurationTicks(context, destX, y + 1, destZ, true)) >= 1000000.0) {
            return;
        }
        Block fromDown = context.get(x, y - 1, z).getBlock();
        if (fromDown == Blocks.LADDER || fromDown == Blocks.VINE) {
            return;
        }
        IBlockState below = context.get(destX, y - 2, destZ);
        if (!MovementHelper.canWalkOn(context.bsi, destX, y - 2, destZ, below)) {
            MovementDescend.dynamicFallCost(context, x, y, z, destX, destZ, totalCost, below, res);
            return;
        }
        if (destDown.getBlock() == Blocks.LADDER || destDown.getBlock() == Blocks.VINE) {
            return;
        }
        double walk = 3.7062775075283763;
        if (fromDown == Blocks.SOUL_SAND) {
            walk *= 2.0;
        }
        res.x = destX;
        res.y = y - 1;
        res.z = destZ;
        res.cost = totalCost += walk + Math.max(FALL_N_BLOCKS_COST[1], 0.9265693768820937);
    }

    public static boolean dynamicFallCost(CalculationContext context, int x, int y, int z, int destX, int destZ, double frontBreak, IBlockState below, MutableMoveResult res) {
        if (frontBreak != 0.0 && context.get(destX, y + 2, destZ).getBlock() instanceof BlockFalling) {
            return false;
        }
        if (!MovementHelper.canWalkThrough(context.bsi, destX, y - 2, destZ, below)) {
            return false;
        }
        double costSoFar = 0.0;
        int effectiveStartHeight = y;
        int fallHeight = 3;
        int newY;
        while ((newY = y - fallHeight) >= 0) {
            IBlockState ontoBlock = context.get(destX, newY, destZ);
            int unprotectedFallHeight = fallHeight - (y - effectiveStartHeight);
            double tentativeCost = 3.7062775075283763 + FALL_N_BLOCKS_COST[unprotectedFallHeight] + frontBreak + costSoFar;
            if (MovementHelper.isWater(ontoBlock.getBlock())) {
                if (!MovementHelper.canWalkThrough(context.bsi, destX, newY, destZ, ontoBlock)) {
                    return false;
                }
                if (context.assumeWalkOnWater) {
                    return false;
                }
                if (MovementHelper.isFlowing(destX, newY, destZ, ontoBlock, context.bsi)) {
                    return false;
                }
                if (!MovementHelper.canWalkOn(context.bsi, destX, newY - 1, destZ)) {
                    return false;
                }
                res.x = destX;
                res.y = newY;
                res.z = destZ;
                res.cost = tentativeCost;
                return false;
            }
            if (unprotectedFallHeight <= 11 && (ontoBlock.getBlock() == Blocks.VINE || ontoBlock.getBlock() == Blocks.LADDER)) {
                costSoFar += FALL_N_BLOCKS_COST[unprotectedFallHeight - 1];
                costSoFar += 6.666666666666667;
                effectiveStartHeight = newY;
            } else if (!MovementHelper.canWalkThrough(context.bsi, destX, newY, destZ, ontoBlock)) {
                if (!MovementHelper.canWalkOn(context.bsi, destX, newY, destZ, ontoBlock)) {
                    return false;
                }
                if (MovementHelper.isBottomSlab(ontoBlock)) {
                    return false;
                }
                if (unprotectedFallHeight <= context.maxFallHeightNoWater + 1) {
                    res.x = destX;
                    res.y = newY + 1;
                    res.z = destZ;
                    res.cost = tentativeCost;
                    return false;
                }
                if (context.hasWaterBucket && unprotectedFallHeight <= context.maxFallHeightBucket + 1) {
                    res.x = destX;
                    res.y = newY + 1;
                    res.z = destZ;
                    res.cost = tentativeCost + context.placeBucketCost();
                    return true;
                }
                return false;
            }
            ++fallHeight;
        }
        return false;
    }

    @Override
    public MovementState updateState(MovementState state) {
        super.updateState(state);
        if (state.getStatus() != MovementStatus.RUNNING) {
            return state;
        }
        BetterBlockPos playerFeet = this.ctx.playerFeet();
        BlockPos fakeDest = new BlockPos(this.dest.getX() * 2 - this.src.getX(), this.dest.getY(), this.dest.getZ() * 2 - this.src.getZ());
        if ((((Vec3i)playerFeet).equals(this.dest) || ((Vec3i)playerFeet).equals(fakeDest)) && (MovementHelper.isLiquid(this.ctx, this.dest) || this.ctx.player().posY - (double)this.dest.getY() < 0.5)) {
            return state.setStatus(MovementStatus.SUCCESS);
        }
        if (this.safeMode()) {
            double destX = ((double)this.src.getX() + 0.5) * 0.17 + ((double)this.dest.getX() + 0.5) * 0.83;
            double destZ = ((double)this.src.getZ() + 0.5) * 0.17 + ((double)this.dest.getZ() + 0.5) * 0.83;
            EntityPlayerSP player = this.ctx.player();
            state.setTarget(new MovementState.MovementTarget(new Rotation(RotationUtils.calcRotationFromVec3d(this.ctx.playerHead(), new Vec3d(destX, this.dest.getY(), destZ), new Rotation(player.rotationYaw, player.rotationPitch)).getYaw(), player.rotationPitch), false)).setInput(Input.MOVE_FORWARD, true);
            return state;
        }
        double diffX = this.ctx.player().posX - ((double)this.dest.getX() + 0.5);
        double diffZ = this.ctx.player().posZ - ((double)this.dest.getZ() + 0.5);
        double ab = Math.sqrt(diffX * diffX + diffZ * diffZ);
        double x = this.ctx.player().posX - ((double)this.src.getX() + 0.5);
        double z = this.ctx.player().posZ - ((double)this.src.getZ() + 0.5);
        double fromStart = Math.sqrt(x * x + z * z);
        if (!((Vec3i)playerFeet).equals(this.dest) || ab > 0.25) {
            if (this.numTicks++ < 20 && fromStart < 1.25) {
                MovementHelper.moveTowards(this.ctx, state, fakeDest);
            } else {
                MovementHelper.moveTowards(this.ctx, state, this.dest);
            }
        }
        return state;
    }

    public boolean safeMode() {
        BlockPos into = this.dest.subtract(this.src.down()).add(this.dest);
        if (this.skipToAscend()) {
            return true;
        }
        for (int y = 0; y <= 2; ++y) {
            if (!MovementHelper.avoidWalkingInto(BlockStateInterface.getBlock(this.ctx, into.up(y)))) continue;
            return true;
        }
        return false;
    }

    public boolean skipToAscend() {
        BlockPos into = this.dest.subtract(this.src.down()).add(this.dest);
        return !MovementHelper.canWalkThrough(this.ctx, new BetterBlockPos(into)) && MovementHelper.canWalkThrough(this.ctx, new BetterBlockPos(into).up()) && MovementHelper.canWalkThrough(this.ctx, new BetterBlockPos(into).up(2));
    }
}

