/*
 * Decompiled with CFR 0.150.
 */
package baritone.pathing.movement.movements;

import baritone.Baritone;
import baritone.api.IBaritone;
import baritone.api.pathing.movement.MovementStatus;
import baritone.api.utils.BetterBlockPos;
import baritone.api.utils.Rotation;
import baritone.api.utils.RotationUtils;
import baritone.api.utils.VecUtils;
import baritone.api.utils.input.Input;
import baritone.pathing.movement.CalculationContext;
import baritone.pathing.movement.Movement;
import baritone.pathing.movement.MovementHelper;
import baritone.pathing.movement.MovementState;
import baritone.pathing.movement.movements.MovementPillar;
import baritone.utils.BlockStateInterface;
import com.google.common.collect.ImmutableSet;
import java.util.Optional;
import java.util.Set;
import net.minecraft.block.Block;
import net.minecraft.block.BlockAir;
import net.minecraft.block.BlockDoor;
import net.minecraft.block.BlockFenceGate;
import net.minecraft.block.BlockLadder;
import net.minecraft.block.BlockSlab;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.math.Vec3i;

public class MovementTraverse
extends Movement {
    private boolean wasTheBridgeBlockAlwaysThere = true;

    public MovementTraverse(IBaritone baritone, BetterBlockPos from, BetterBlockPos to) {
        super(baritone, from, to, new BetterBlockPos[]{to.up(), to}, to.down());
    }

    @Override
    public void reset() {
        super.reset();
        this.wasTheBridgeBlockAlwaysThere = true;
    }

    @Override
    public double calculateCost(CalculationContext context) {
        return MovementTraverse.cost(context, this.src.x, this.src.y, this.src.z, this.dest.x, this.dest.z);
    }

    @Override
    protected Set<BetterBlockPos> calculateValidPositions() {
        return ImmutableSet.of(this.src, this.dest);
    }

    public static double cost(CalculationContext context, int x, int y, int z, int destX, int destZ) {
        IBlockState pb0 = context.get(destX, y + 1, destZ);
        IBlockState pb1 = context.get(destX, y, destZ);
        IBlockState destOn = context.get(destX, y - 1, destZ);
        Block srcDown = context.getBlock(x, y - 1, z);
        if (MovementHelper.canWalkOn(context.bsi, destX, y - 1, destZ, destOn)) {
            double WC = 4.63284688441047;
            boolean water = false;
            if (MovementHelper.isWater(pb0.getBlock()) || MovementHelper.isWater(pb1.getBlock())) {
                WC = context.waterWalkSpeed;
                water = true;
            } else {
                if (destOn.getBlock() == Blocks.SOUL_SAND) {
                    WC += 2.316423442205235;
                } else if (destOn.getBlock() == Blocks.WATER) {
                    WC += context.walkOnWaterOnePenalty;
                }
                if (srcDown == Blocks.SOUL_SAND) {
                    WC += 2.316423442205235;
                }
            }
            double hardness1 = MovementHelper.getMiningDurationTicks(context, destX, y, destZ, pb1, false);
            if (hardness1 >= 1000000.0) {
                return 1000000.0;
            }
            double hardness2 = MovementHelper.getMiningDurationTicks(context, destX, y + 1, destZ, pb0, true);
            if (hardness1 == 0.0 && hardness2 == 0.0) {
                if (!water && context.canSprint) {
                    WC *= 0.7692444761225944;
                }
                return WC;
            }
            if (srcDown == Blocks.LADDER || srcDown == Blocks.VINE) {
                hardness1 *= 5.0;
                hardness2 *= 5.0;
            }
            return WC + hardness1 + hardness2;
        }
        if (srcDown == Blocks.LADDER || srcDown == Blocks.VINE) {
            return 1000000.0;
        }
        if (MovementHelper.isReplaceable(destX, y - 1, destZ, destOn, context.bsi)) {
            boolean throughWater;
            boolean bl = throughWater = MovementHelper.isWater(pb0.getBlock()) || MovementHelper.isWater(pb1.getBlock());
            if (MovementHelper.isWater(destOn.getBlock()) && throughWater) {
                return 1000000.0;
            }
            double placeCost = context.costOfPlacingAt(destX, y - 1, destZ, destOn);
            if (placeCost >= 1000000.0) {
                return 1000000.0;
            }
            double hardness1 = MovementHelper.getMiningDurationTicks(context, destX, y, destZ, pb1, false);
            if (hardness1 >= 1000000.0) {
                return 1000000.0;
            }
            double hardness2 = MovementHelper.getMiningDurationTicks(context, destX, y + 1, destZ, pb0, true);
            double WC = throughWater ? context.waterWalkSpeed : 4.63284688441047;
            for (int i = 0; i < 5; ++i) {
                int againstX = destX + HORIZONTALS_BUT_ALSO_DOWN_____SO_EVERY_DIRECTION_EXCEPT_UP[i].getXOffset();
                int againstY = y - 1 + HORIZONTALS_BUT_ALSO_DOWN_____SO_EVERY_DIRECTION_EXCEPT_UP[i].getYOffset();
                int againstZ = destZ + HORIZONTALS_BUT_ALSO_DOWN_____SO_EVERY_DIRECTION_EXCEPT_UP[i].getZOffset();
                if (againstX == x && againstZ == z || !MovementHelper.canPlaceAgainst(context.bsi, againstX, againstY, againstZ)) continue;
                return WC + placeCost + hardness1 + hardness2;
            }
            if (srcDown == Blocks.SOUL_SAND || srcDown instanceof BlockSlab && !((BlockSlab)srcDown).isDouble()) {
                return 1000000.0;
            }
            if (srcDown == Blocks.FLOWING_WATER || srcDown == Blocks.WATER) {
                return 1000000.0;
            }
            return (WC *= 3.3207692307692307) + placeCost + hardness1 + hardness2;
        }
        return 1000000.0;
    }

    @Override
    public MovementState updateState(MovementState state) {
        double dist;
        boolean ladder;
        super.updateState(state);
        IBlockState pb0 = BlockStateInterface.get(this.ctx, this.positionsToBreak[0]);
        IBlockState pb1 = BlockStateInterface.get(this.ctx, this.positionsToBreak[1]);
        if (state.getStatus() != MovementStatus.RUNNING) {
            if (!((Boolean)Baritone.settings().walkWhileBreaking.value).booleanValue()) {
                return state;
            }
            if (state.getStatus() != MovementStatus.PREPPING) {
                return state;
            }
            if (MovementHelper.avoidWalkingInto(pb0.getBlock())) {
                return state;
            }
            if (MovementHelper.avoidWalkingInto(pb1.getBlock())) {
                return state;
            }
            double dist2 = Math.max(Math.abs(this.ctx.player().posX - ((double)this.dest.getX() + 0.5)), Math.abs(this.ctx.player().posZ - ((double)this.dest.getZ() + 0.5)));
            if (dist2 < 0.83) {
                return state;
            }
            if (!state.getTarget().getRotation().isPresent()) {
                return state;
            }
            float yawToDest = RotationUtils.calcRotationFromVec3d(this.ctx.playerHead(), VecUtils.calculateBlockCenter(this.ctx.world(), this.dest), this.ctx.playerRotations()).getYaw();
            float pitchToBreak = state.getTarget().getRotation().get().getPitch();
            if (pb0.isFullCube() || pb0.getBlock() instanceof BlockAir && (pb1.isFullCube() || pb1.getBlock() instanceof BlockAir)) {
                pitchToBreak = 26.0f;
            }
            return state.setTarget(new MovementState.MovementTarget(new Rotation(yawToDest, pitchToBreak), true)).setInput(Input.MOVE_FORWARD, true).setInput(Input.SPRINT, true);
        }
        state.setInput(Input.SNEAK, false);
        Block fd = BlockStateInterface.get(this.ctx, this.src.down()).getBlock();
        boolean bl = ladder = fd == Blocks.LADDER || fd == Blocks.VINE;
        if (pb0.getBlock() instanceof BlockDoor || pb1.getBlock() instanceof BlockDoor) {
            boolean canOpen;
            boolean notPassable = pb0.getBlock() instanceof BlockDoor && !MovementHelper.isDoorPassable(this.ctx, this.src, this.dest) || pb1.getBlock() instanceof BlockDoor && !MovementHelper.isDoorPassable(this.ctx, this.dest, this.src);
            boolean bl2 = canOpen = !Blocks.IRON_DOOR.equals(pb0.getBlock()) && !Blocks.IRON_DOOR.equals(pb1.getBlock());
            if (notPassable && canOpen) {
                return state.setTarget(new MovementState.MovementTarget(RotationUtils.calcRotationFromVec3d(this.ctx.playerHead(), VecUtils.calculateBlockCenter(this.ctx.world(), this.positionsToBreak[0]), this.ctx.playerRotations()), true)).setInput(Input.CLICK_RIGHT, true);
            }
        }
        if (pb0.getBlock() instanceof BlockFenceGate || pb1.getBlock() instanceof BlockFenceGate) {
            Optional<Rotation> rotation;
            BetterBlockPos blocked;
            BetterBlockPos betterBlockPos = !MovementHelper.isGatePassable(this.ctx, this.positionsToBreak[0], this.src.up()) ? this.positionsToBreak[0] : (blocked = !MovementHelper.isGatePassable(this.ctx, this.positionsToBreak[1], this.src) ? this.positionsToBreak[1] : null);
            if (blocked != null && (rotation = RotationUtils.reachable(this.ctx, blocked)).isPresent()) {
                return state.setTarget(new MovementState.MovementTarget(rotation.get(), true)).setInput(Input.CLICK_RIGHT, true);
            }
        }
        boolean isTheBridgeBlockThere = MovementHelper.canWalkOn(this.ctx, this.positionToPlace) || ladder;
        BetterBlockPos feet = this.ctx.playerFeet();
        if (feet.getY() != this.dest.getY() && !ladder) {
            this.logDebug("Wrong Y coordinate");
            if (feet.getY() < this.dest.getY()) {
                return state.setInput(Input.JUMP, true);
            }
            return state;
        }
        if (isTheBridgeBlockThere) {
            if (((Vec3i)feet).equals(this.dest)) {
                return state.setStatus(MovementStatus.SUCCESS);
            }
            if (((Boolean)Baritone.settings().overshootTraverse.value).booleanValue() && (((Vec3i)feet).equals(this.dest.add(this.getDirection())) || ((Vec3i)feet).equals(this.dest.add(this.getDirection()).add(this.getDirection())))) {
                return state.setStatus(MovementStatus.SUCCESS);
            }
            Block low = BlockStateInterface.get(this.ctx, this.src).getBlock();
            Block high = BlockStateInterface.get(this.ctx, this.src.up()).getBlock();
            if (this.ctx.player().posY > (double)this.src.y + 0.1 && !this.ctx.player().onGround && (low == Blocks.VINE || low == Blocks.LADDER || high == Blocks.VINE || high == Blocks.LADDER)) {
                return state;
            }
            BlockPos into = this.dest.subtract(this.src).add(this.dest);
            Block intoBelow = BlockStateInterface.get(this.ctx, into).getBlock();
            Block intoAbove = BlockStateInterface.get(this.ctx, into.up()).getBlock();
            if (!(!this.wasTheBridgeBlockAlwaysThere || MovementHelper.isLiquid(this.ctx, feet) && !((Boolean)Baritone.settings().sprintInWater.value).booleanValue() || MovementHelper.avoidWalkingInto(intoBelow) && !MovementHelper.isWater(intoBelow) || MovementHelper.avoidWalkingInto(intoAbove))) {
                state.setInput(Input.SPRINT, true);
            }
            IBlockState destDown = BlockStateInterface.get(this.ctx, this.dest.down());
            BetterBlockPos against = this.positionsToBreak[0];
            if (feet.getY() != this.dest.getY() && ladder && (destDown.getBlock() == Blocks.VINE || destDown.getBlock() == Blocks.LADDER)) {
                BlockPos blockPos = against = destDown.getBlock() == Blocks.VINE ? MovementPillar.getAgainst(new CalculationContext(this.baritone), this.dest.down()) : this.dest.offset(destDown.getValue(BlockLadder.FACING).getOpposite());
                if (against == null) {
                    this.logDirect("Unable to climb vines. Consider disabling allowVines.");
                    return state.setStatus(MovementStatus.UNREACHABLE);
                }
            }
            MovementHelper.moveTowards(this.ctx, state, against);
            return state;
        }
        this.wasTheBridgeBlockAlwaysThere = false;
        Block standingOn = BlockStateInterface.get(this.ctx, ((BlockPos)feet).down()).getBlock();
        if ((standingOn.equals(Blocks.SOUL_SAND) || standingOn instanceof BlockSlab) && (dist = Math.max(Math.abs((double)this.dest.getX() + 0.5 - this.ctx.player().posX), Math.abs((double)this.dest.getZ() + 0.5 - this.ctx.player().posZ))) < 0.85) {
            MovementHelper.moveTowards(this.ctx, state, this.dest);
            return state.setInput(Input.MOVE_FORWARD, false).setInput(Input.MOVE_BACK, true);
        }
        double dist1 = Math.max(Math.abs(this.ctx.player().posX - ((double)this.dest.getX() + 0.5)), Math.abs(this.ctx.player().posZ - ((double)this.dest.getZ() + 0.5)));
        MovementHelper.PlaceResult p = MovementHelper.attemptToPlaceABlock(state, this.baritone, this.dest.down(), false, true);
        if ((p == MovementHelper.PlaceResult.READY_TO_PLACE || dist1 < 0.6) && !((Boolean)Baritone.settings().assumeSafeWalk.value).booleanValue()) {
            state.setInput(Input.SNEAK, true);
        }
        switch (p) {
            case READY_TO_PLACE: {
                if (this.ctx.player().isSneaking() || ((Boolean)Baritone.settings().assumeSafeWalk.value).booleanValue()) {
                    state.setInput(Input.CLICK_RIGHT, true);
                }
                return state;
            }
            case ATTEMPTING: {
                if (dist1 > 0.83) {
                    float yaw = RotationUtils.calcRotationFromVec3d(this.ctx.playerHead(), VecUtils.getBlockPosCenter(this.dest), this.ctx.playerRotations()).getYaw();
                    if ((double)Math.abs(state.getTarget().rotation.getYaw() - yaw) < 0.1) {
                        return state.setInput(Input.MOVE_FORWARD, true);
                    }
                } else if (this.ctx.playerRotations().isReallyCloseTo(state.getTarget().rotation)) {
                    return state.setInput(Input.CLICK_LEFT, true);
                }
                return state;
            }
        }
        if (((Vec3i)feet).equals(this.dest)) {
            double faceX = ((double)(this.dest.getX() + this.src.getX()) + 1.0) * 0.5;
            double faceY = ((double)(this.dest.getY() + this.src.getY()) - 1.0) * 0.5;
            double faceZ = ((double)(this.dest.getZ() + this.src.getZ()) + 1.0) * 0.5;
            BetterBlockPos goalLook = this.src.down();
            Rotation backToFace = RotationUtils.calcRotationFromVec3d(this.ctx.playerHead(), new Vec3d(faceX, faceY, faceZ), this.ctx.playerRotations());
            float pitch = backToFace.getPitch();
            double dist2 = Math.max(Math.abs(this.ctx.player().posX - faceX), Math.abs(this.ctx.player().posZ - faceZ));
            if (dist2 < 0.29) {
                float yaw = RotationUtils.calcRotationFromVec3d(VecUtils.getBlockPosCenter(this.dest), this.ctx.playerHead(), this.ctx.playerRotations()).getYaw();
                state.setTarget(new MovementState.MovementTarget(new Rotation(yaw, pitch), true));
                state.setInput(Input.MOVE_BACK, true);
            } else {
                state.setTarget(new MovementState.MovementTarget(backToFace, true));
            }
            if (this.ctx.isLookingAt(goalLook)) {
                return state.setInput(Input.CLICK_RIGHT, true);
            }
            if (this.ctx.playerRotations().isReallyCloseTo(state.getTarget().rotation)) {
                state.setInput(Input.CLICK_LEFT, true);
            }
            return state;
        }
        MovementHelper.moveTowards(this.ctx, state, this.positionsToBreak[0]);
        return state;
    }

    @Override
    public boolean safeToCancel(MovementState state) {
        return state.getStatus() != MovementStatus.RUNNING || MovementHelper.canWalkOn(this.ctx, this.dest.down());
    }

    @Override
    protected boolean prepared(MovementState state) {
        Block block;
        if ((this.ctx.playerFeet().equals(this.src) || this.ctx.playerFeet().equals(this.src.down())) && ((block = BlockStateInterface.getBlock(this.ctx, this.src.down())) == Blocks.LADDER || block == Blocks.VINE)) {
            state.setInput(Input.SNEAK, true);
        }
        return super.prepared(state);
    }
}

