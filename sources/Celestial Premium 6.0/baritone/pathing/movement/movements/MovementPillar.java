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
import baritone.utils.BlockStateInterface;
import com.google.common.collect.ImmutableSet;
import java.util.Objects;
import java.util.Set;
import net.minecraft.block.Block;
import net.minecraft.block.BlockAir;
import net.minecraft.block.BlockFalling;
import net.minecraft.block.BlockFenceGate;
import net.minecraft.block.BlockLadder;
import net.minecraft.block.BlockLiquid;
import net.minecraft.block.BlockSlab;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;

public class MovementPillar
extends Movement {
    public MovementPillar(IBaritone baritone, BetterBlockPos start, BetterBlockPos end) {
        super(baritone, start, end, new BetterBlockPos[]{start.up(2)}, start);
    }

    @Override
    public double calculateCost(CalculationContext context) {
        return MovementPillar.cost(context, this.src.x, this.src.y, this.src.z);
    }

    @Override
    protected Set<BetterBlockPos> calculateValidPositions() {
        return ImmutableSet.of(this.src, this.dest);
    }

    public static double cost(CalculationContext context, int x, int y, int z) {
        IBlockState fromState = context.get(x, y, z);
        Block from = fromState.getBlock();
        boolean ladder = from == Blocks.LADDER || from == Blocks.VINE;
        IBlockState fromDown = context.get(x, y - 1, z);
        if (!ladder) {
            if (fromDown.getBlock() == Blocks.LADDER || fromDown.getBlock() == Blocks.VINE) {
                return 1000000.0;
            }
            if (fromDown.getBlock() instanceof BlockSlab && !((BlockSlab)fromDown.getBlock()).isDouble() && fromDown.getValue(BlockSlab.HALF) == BlockSlab.EnumBlockHalf.BOTTOM) {
                return 1000000.0;
            }
        }
        if (from == Blocks.VINE && !MovementPillar.hasAgainst(context, x, y, z)) {
            return 1000000.0;
        }
        IBlockState toBreak = context.get(x, y + 2, z);
        Block toBreakBlock = toBreak.getBlock();
        if (toBreakBlock instanceof BlockFenceGate) {
            return 1000000.0;
        }
        Block srcUp = null;
        if (MovementHelper.isWater(toBreakBlock) && MovementHelper.isWater(from) && MovementHelper.isWater(srcUp = context.get(x, y + 1, z).getBlock())) {
            return 8.51063829787234;
        }
        double placeCost = 0.0;
        if (!ladder) {
            placeCost = context.costOfPlacingAt(x, y, z, fromState);
            if (placeCost >= 1000000.0) {
                return 1000000.0;
            }
            if (fromDown.getBlock() == Blocks.AIR) {
                placeCost += 0.1;
            }
        }
        if (from instanceof BlockLiquid || fromDown.getBlock() instanceof BlockLiquid && context.assumeWalkOnWater) {
            return 1000000.0;
        }
        double hardness = MovementHelper.getMiningDurationTicks(context, x, y + 2, z, toBreak, true);
        if (hardness >= 1000000.0) {
            return 1000000.0;
        }
        if (hardness != 0.0) {
            if (toBreakBlock == Blocks.LADDER || toBreakBlock == Blocks.VINE) {
                hardness = 0.0;
            } else {
                IBlockState check = context.get(x, y + 3, z);
                if (check.getBlock() instanceof BlockFalling) {
                    if (srcUp == null) {
                        srcUp = context.get(x, y + 1, z).getBlock();
                    }
                    if (!(toBreakBlock instanceof BlockFalling) || !(srcUp instanceof BlockFalling)) {
                        return 1000000.0;
                    }
                }
            }
        }
        if (ladder) {
            return 8.51063829787234 + hardness * 5.0;
        }
        return JUMP_ONE_BLOCK_COST + placeCost + context.jumpPenalty + hardness;
    }

    public static boolean hasAgainst(CalculationContext context, int x, int y, int z) {
        return context.get(x + 1, y, z).isBlockNormalCube() || context.get(x - 1, y, z).isBlockNormalCube() || context.get(x, y, z + 1).isBlockNormalCube() || context.get(x, y, z - 1).isBlockNormalCube();
    }

    public static BlockPos getAgainst(CalculationContext context, BetterBlockPos vine) {
        if (context.get(vine.north()).isBlockNormalCube()) {
            return vine.north();
        }
        if (context.get(vine.south()).isBlockNormalCube()) {
            return vine.south();
        }
        if (context.get(vine.east()).isBlockNormalCube()) {
            return vine.east();
        }
        if (context.get(vine.west()).isBlockNormalCube()) {
            return vine.west();
        }
        return null;
    }

    @Override
    public MovementState updateState(MovementState state) {
        boolean blockIsThere;
        super.updateState(state);
        if (state.getStatus() != MovementStatus.RUNNING) {
            return state;
        }
        if (this.ctx.playerFeet().y < this.src.y) {
            return state.setStatus(MovementStatus.UNREACHABLE);
        }
        IBlockState fromDown = BlockStateInterface.get(this.ctx, this.src);
        if (MovementHelper.isWater(fromDown.getBlock()) && MovementHelper.isWater(this.ctx, this.dest)) {
            state.setTarget(new MovementState.MovementTarget(RotationUtils.calcRotationFromVec3d(this.ctx.playerHead(), VecUtils.getBlockPosCenter(this.dest), this.ctx.playerRotations()), false));
            Vec3d destCenter = VecUtils.getBlockPosCenter(this.dest);
            if (Math.abs(this.ctx.player().posX - destCenter.x) > 0.2 || Math.abs(this.ctx.player().posZ - destCenter.z) > 0.2) {
                state.setInput(Input.MOVE_FORWARD, true);
            }
            if (this.ctx.playerFeet().equals(this.dest)) {
                return state.setStatus(MovementStatus.SUCCESS);
            }
            return state;
        }
        boolean ladder = fromDown.getBlock() == Blocks.LADDER || fromDown.getBlock() == Blocks.VINE;
        boolean vine = fromDown.getBlock() == Blocks.VINE;
        Rotation rotation = RotationUtils.calcRotationFromVec3d(this.ctx.playerHead(), VecUtils.getBlockPosCenter(this.positionToPlace), new Rotation(this.ctx.player().rotationYaw, this.ctx.player().rotationPitch));
        if (!ladder) {
            state.setTarget(new MovementState.MovementTarget(new Rotation(this.ctx.player().rotationYaw, rotation.getPitch()), true));
        }
        boolean bl = blockIsThere = MovementHelper.canWalkOn(this.ctx, this.src) || ladder;
        if (ladder) {
            BlockPos against;
            BlockPos blockPos = against = vine ? MovementPillar.getAgainst(new CalculationContext(this.baritone), this.src) : this.src.offset(fromDown.getValue(BlockLadder.FACING).getOpposite());
            if (against == null) {
                this.logDirect("Unable to climb vines. Consider disabling allowVines.");
                return state.setStatus(MovementStatus.UNREACHABLE);
            }
            if (this.ctx.playerFeet().equals(against.up()) || this.ctx.playerFeet().equals(this.dest)) {
                return state.setStatus(MovementStatus.SUCCESS);
            }
            if (MovementHelper.isBottomSlab(BlockStateInterface.get(this.ctx, this.src.down()))) {
                state.setInput(Input.JUMP, true);
            }
            MovementHelper.moveTowards(this.ctx, state, against);
            return state;
        }
        if (!((Baritone)this.baritone).getInventoryBehavior().selectThrowawayForLocation(true, this.src.x, this.src.y, this.src.z)) {
            return state.setStatus(MovementStatus.UNREACHABLE);
        }
        state.setInput(Input.SNEAK, this.ctx.player().posY > (double)this.dest.getY() || this.ctx.player().posY < (double)this.src.getY() + 0.2);
        double diffX = this.ctx.player().posX - ((double)this.dest.getX() + 0.5);
        double diffZ = this.ctx.player().posZ - ((double)this.dest.getZ() + 0.5);
        double dist = Math.sqrt(diffX * diffX + diffZ * diffZ);
        double flatMotion = Math.sqrt(this.ctx.player().motionX * this.ctx.player().motionX + this.ctx.player().motionZ * this.ctx.player().motionZ);
        if (dist > 0.17) {
            state.setInput(Input.MOVE_FORWARD, true);
            state.setTarget(new MovementState.MovementTarget(rotation, true));
        } else if (flatMotion < 0.05) {
            state.setInput(Input.JUMP, this.ctx.player().posY < (double)this.dest.getY());
        }
        if (!blockIsThere) {
            IBlockState frState = BlockStateInterface.get(this.ctx, this.src);
            Block fr = frState.getBlock();
            if (!(fr instanceof BlockAir) && !frState.getMaterial().isReplaceable()) {
                RotationUtils.reachable(this.ctx.player(), (BlockPos)this.src, this.ctx.playerController().getBlockReachDistance()).map(rot -> new MovementState.MovementTarget((Rotation)rot, true)).ifPresent(state::setTarget);
                state.setInput(Input.JUMP, false);
                state.setInput(Input.CLICK_LEFT, true);
                blockIsThere = false;
            } else if (this.ctx.player().isSneaking() && (Objects.equals(this.src.down(), this.ctx.objectMouseOver().getBlockPos()) || Objects.equals(this.src, this.ctx.objectMouseOver().getBlockPos())) && this.ctx.player().posY > (double)this.dest.getY() + 0.1) {
                state.setInput(Input.CLICK_RIGHT, true);
            }
        }
        if (this.ctx.playerFeet().equals(this.dest) && blockIsThere) {
            return state.setStatus(MovementStatus.SUCCESS);
        }
        return state;
    }

    @Override
    protected boolean prepared(MovementState state) {
        Block block;
        if ((this.ctx.playerFeet().equals(this.src) || this.ctx.playerFeet().equals(this.src.down())) && ((block = BlockStateInterface.getBlock(this.ctx, this.src.down())) == Blocks.LADDER || block == Blocks.VINE)) {
            state.setInput(Input.SNEAK, true);
        }
        if (MovementHelper.isWater(this.ctx, this.dest.up())) {
            return true;
        }
        return super.prepared(state);
    }
}

