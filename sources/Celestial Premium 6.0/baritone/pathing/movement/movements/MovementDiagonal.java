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
import baritone.utils.pathing.MutableMoveResult;
import com.google.common.collect.ImmutableSet;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.init.Blocks;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;

public class MovementDiagonal
extends Movement {
    private static final double SQRT_2 = Math.sqrt(2.0);

    public MovementDiagonal(IBaritone baritone, BetterBlockPos start, EnumFacing dir1, EnumFacing dir2, int dy) {
        this(baritone, start, start.offset(dir1), start.offset(dir2), dir2, dy);
    }

    private MovementDiagonal(IBaritone baritone, BetterBlockPos start, BetterBlockPos dir1, BetterBlockPos dir2, EnumFacing drr2, int dy) {
        this(baritone, start, dir1.offset(drr2).up(dy), dir1, dir2);
    }

    private MovementDiagonal(IBaritone baritone, BetterBlockPos start, BetterBlockPos end, BetterBlockPos dir1, BetterBlockPos dir2) {
        super(baritone, start, end, new BetterBlockPos[]{dir1, dir1.up(), dir2, dir2.up(), end, end.up()});
    }

    @Override
    protected boolean safeToCancel(MovementState state) {
        EntityPlayerSP player = this.ctx.player();
        double offset = 0.25;
        double x = player.posX;
        double y = player.posY - 1.0;
        double z = player.posZ;
        if (this.ctx.playerFeet().equals(this.src)) {
            return true;
        }
        if (MovementHelper.canWalkOn(this.ctx, new BlockPos(this.src.x, this.src.y - 1, this.dest.z)) && MovementHelper.canWalkOn(this.ctx, new BlockPos(this.dest.x, this.src.y - 1, this.src.z))) {
            return true;
        }
        if (this.ctx.playerFeet().equals(new BetterBlockPos(this.src.x, this.src.y, this.dest.z)) || this.ctx.playerFeet().equals(new BetterBlockPos(this.dest.x, this.src.y, this.src.z))) {
            return MovementHelper.canWalkOn(this.ctx, new BetterBlockPos(x + offset, y, z + offset)) || MovementHelper.canWalkOn(this.ctx, new BetterBlockPos(x + offset, y, z - offset)) || MovementHelper.canWalkOn(this.ctx, new BetterBlockPos(x - offset, y, z + offset)) || MovementHelper.canWalkOn(this.ctx, new BetterBlockPos(x - offset, y, z - offset));
        }
        return true;
    }

    @Override
    public double calculateCost(CalculationContext context) {
        MutableMoveResult result = new MutableMoveResult();
        MovementDiagonal.cost(context, this.src.x, this.src.y, this.src.z, this.dest.x, this.dest.z, result);
        if (result.y != this.dest.y) {
            return 1000000.0;
        }
        return result.cost;
    }

    @Override
    protected Set<BetterBlockPos> calculateValidPositions() {
        BetterBlockPos diagA = new BetterBlockPos(this.src.x, this.src.y, this.dest.z);
        BetterBlockPos diagB = new BetterBlockPos(this.dest.x, this.src.y, this.src.z);
        if (this.dest.y < this.src.y) {
            return ImmutableSet.of(this.src, this.dest.up(), diagA, diagB, this.dest, diagA.down(), new BetterBlockPos[]{diagB.down()});
        }
        if (this.dest.y > this.src.y) {
            return ImmutableSet.of(this.src, this.src.up(), diagA, diagB, this.dest, diagA.up(), new BetterBlockPos[]{diagB.up()});
        }
        return ImmutableSet.of(this.src, this.dest, diagA, diagB);
    }

    public static void cost(CalculationContext context, int x, int y, int z, int destX, int destZ, MutableMoveResult res) {
        Block cuttingOver1;
        IBlockState destWalkOn;
        if (!MovementHelper.canWalkThrough(context.bsi, destX, y + 1, destZ)) {
            return;
        }
        IBlockState destInto = context.get(destX, y, destZ);
        boolean ascend = false;
        boolean descend = false;
        if (!MovementHelper.canWalkThrough(context.bsi, destX, y, destZ, destInto)) {
            ascend = true;
            if (!(context.allowDiagonalAscend && MovementHelper.canWalkThrough(context.bsi, x, y + 2, z) && MovementHelper.canWalkOn(context.bsi, destX, y, destZ, destInto) && MovementHelper.canWalkThrough(context.bsi, destX, y + 2, destZ))) {
                return;
            }
            destWalkOn = destInto;
        } else {
            destWalkOn = context.get(destX, y - 1, destZ);
            if (!MovementHelper.canWalkOn(context.bsi, destX, y - 1, destZ, destWalkOn)) {
                descend = true;
                if (!(context.allowDiagonalDescend && MovementHelper.canWalkOn(context.bsi, destX, y - 2, destZ) && MovementHelper.canWalkThrough(context.bsi, destX, y - 1, destZ, destWalkOn))) {
                    return;
                }
            }
        }
        double multiplier = 4.63284688441047;
        if (destWalkOn.getBlock() == Blocks.SOUL_SAND) {
            multiplier += 2.316423442205235;
        } else if (destWalkOn.getBlock() == Blocks.WATER) {
            multiplier += context.walkOnWaterOnePenalty * SQRT_2;
        }
        Block fromDown = context.get(x, y - 1, z).getBlock();
        if (fromDown == Blocks.LADDER || fromDown == Blocks.VINE) {
            return;
        }
        if (fromDown == Blocks.SOUL_SAND) {
            multiplier += 2.316423442205235;
        }
        if ((cuttingOver1 = context.get(x, y - 1, destZ).getBlock()) == Blocks.MAGMA || MovementHelper.isLava(cuttingOver1)) {
            return;
        }
        Block cuttingOver2 = context.get(destX, y - 1, z).getBlock();
        if (cuttingOver2 == Blocks.MAGMA || MovementHelper.isLava(cuttingOver2)) {
            return;
        }
        Block startIn = context.getBlock(x, y, z);
        boolean water = false;
        if (MovementHelper.isWater(startIn) || MovementHelper.isWater(destInto.getBlock())) {
            if (ascend) {
                return;
            }
            multiplier = context.waterWalkSpeed;
            water = true;
        }
        IBlockState pb0 = context.get(x, y, destZ);
        IBlockState pb2 = context.get(destX, y, z);
        if (ascend) {
            boolean ATop = MovementHelper.canWalkThrough(context.bsi, x, y + 2, destZ);
            boolean AMid = MovementHelper.canWalkThrough(context.bsi, x, y + 1, destZ);
            boolean ALow = MovementHelper.canWalkThrough(context.bsi, x, y, destZ, pb0);
            boolean BTop = MovementHelper.canWalkThrough(context.bsi, destX, y + 2, z);
            boolean BMid = MovementHelper.canWalkThrough(context.bsi, destX, y + 1, z);
            boolean BLow = MovementHelper.canWalkThrough(context.bsi, destX, y, z, pb2);
            if ((!ATop || !AMid || !ALow) && (!BTop || !BMid || !BLow) || MovementHelper.avoidWalkingInto(pb0.getBlock()) || MovementHelper.avoidWalkingInto(pb2.getBlock()) || ATop && AMid && MovementHelper.canWalkOn(context.bsi, x, y, destZ, pb0) || BTop && BMid && MovementHelper.canWalkOn(context.bsi, destX, y, z, pb2) || !ATop && AMid && ALow || !BTop && BMid && BLow) {
                return;
            }
            res.cost = multiplier * SQRT_2 + JUMP_ONE_BLOCK_COST;
            res.x = destX;
            res.z = destZ;
            res.y = y + 1;
            return;
        }
        double optionA = MovementHelper.getMiningDurationTicks(context, x, y, destZ, pb0, false);
        double optionB = MovementHelper.getMiningDurationTicks(context, destX, y, z, pb2, false);
        if (optionA != 0.0 && optionB != 0.0) {
            return;
        }
        IBlockState pb1 = context.get(x, y + 1, destZ);
        if ((optionA += MovementHelper.getMiningDurationTicks(context, x, y + 1, destZ, pb1, true)) != 0.0 && optionB != 0.0) {
            return;
        }
        IBlockState pb3 = context.get(destX, y + 1, z);
        if (optionA == 0.0 && (MovementHelper.avoidWalkingInto(pb2.getBlock()) && pb2.getBlock() != Blocks.WATER || MovementHelper.avoidWalkingInto(pb3.getBlock()))) {
            return;
        }
        if (optionA != 0.0 && (optionB += MovementHelper.getMiningDurationTicks(context, destX, y + 1, z, pb3, true)) != 0.0) {
            return;
        }
        if (optionB == 0.0 && (MovementHelper.avoidWalkingInto(pb0.getBlock()) && pb0.getBlock() != Blocks.WATER || MovementHelper.avoidWalkingInto(pb1.getBlock()))) {
            return;
        }
        if (optionA != 0.0 || optionB != 0.0) {
            multiplier *= SQRT_2 - 0.001;
            if (startIn == Blocks.LADDER || startIn == Blocks.VINE) {
                return;
            }
        } else if (context.canSprint && !water) {
            multiplier *= 0.7692444761225944;
        }
        res.cost = multiplier * SQRT_2;
        if (descend) {
            res.cost += Math.max(FALL_N_BLOCKS_COST[1], 0.9265693768820937);
            res.y = y - 1;
        } else {
            res.y = y;
        }
        res.x = destX;
        res.z = destZ;
    }

    @Override
    public MovementState updateState(MovementState state) {
        super.updateState(state);
        if (state.getStatus() != MovementStatus.RUNNING) {
            return state;
        }
        if (this.ctx.playerFeet().equals(this.dest)) {
            return state.setStatus(MovementStatus.SUCCESS);
        }
        if (!(this.playerInValidPosition() || MovementHelper.isLiquid(this.ctx, this.src) && this.getValidPositions().contains(this.ctx.playerFeet().up()))) {
            return state.setStatus(MovementStatus.UNREACHABLE);
        }
        if (this.dest.y > this.src.y && this.ctx.player().posY < (double)this.src.y + 0.1 && this.ctx.player().isCollidedHorizontally) {
            state.setInput(Input.JUMP, true);
        }
        if (this.sprint()) {
            state.setInput(Input.SPRINT, true);
        }
        MovementHelper.moveTowards(this.ctx, state, this.dest);
        return state;
    }

    private boolean sprint() {
        if (MovementHelper.isLiquid(this.ctx, this.ctx.playerFeet()) && !((Boolean)Baritone.settings().sprintInWater.value).booleanValue()) {
            return false;
        }
        for (int i = 0; i < 4; ++i) {
            if (MovementHelper.canWalkThrough(this.ctx, this.positionsToBreak[i])) continue;
            return false;
        }
        return true;
    }

    @Override
    protected boolean prepared(MovementState state) {
        return true;
    }

    @Override
    public List<BlockPos> toBreak(BlockStateInterface bsi) {
        if (this.toBreakCached != null) {
            return this.toBreakCached;
        }
        ArrayList<BlockPos> result = new ArrayList<BlockPos>();
        for (int i = 4; i < 6; ++i) {
            if (MovementHelper.canWalkThrough(bsi, this.positionsToBreak[i].x, this.positionsToBreak[i].y, this.positionsToBreak[i].z)) continue;
            result.add(this.positionsToBreak[i]);
        }
        this.toBreakCached = result;
        return result;
    }

    @Override
    public List<BlockPos> toWalkInto(BlockStateInterface bsi) {
        if (this.toWalkIntoCached == null) {
            this.toWalkIntoCached = new ArrayList();
        }
        ArrayList<BetterBlockPos> result = new ArrayList<BetterBlockPos>();
        for (int i = 0; i < 4; ++i) {
            if (MovementHelper.canWalkThrough(bsi, this.positionsToBreak[i].x, this.positionsToBreak[i].y, this.positionsToBreak[i].z)) continue;
            result.add(this.positionsToBreak[i]);
        }
        this.toWalkIntoCached = result;
        return this.toWalkIntoCached;
    }
}

