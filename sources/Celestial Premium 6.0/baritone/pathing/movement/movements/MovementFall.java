/*
 * Decompiled with CFR 0.150.
 */
package baritone.pathing.movement.movements;

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
import baritone.pathing.movement.movements.MovementDescend;
import baritone.utils.pathing.MutableMoveResult;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;
import net.minecraft.block.Block;
import net.minecraft.block.BlockLadder;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.math.Vec3i;

public class MovementFall
extends Movement {
    private static final ItemStack STACK_BUCKET_WATER = new ItemStack(Items.WATER_BUCKET);
    private static final ItemStack STACK_BUCKET_EMPTY = new ItemStack(Items.BUCKET);

    public MovementFall(IBaritone baritone, BetterBlockPos src, BetterBlockPos dest) {
        super(baritone, src, dest, MovementFall.buildPositionsToBreak(src, dest));
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
        HashSet<BetterBlockPos> set = new HashSet<BetterBlockPos>();
        set.add(this.src);
        for (int y = this.src.y - this.dest.y; y >= 0; --y) {
            set.add(this.dest.up(y));
        }
        return set;
    }

    private boolean willPlaceBucket() {
        CalculationContext context = new CalculationContext(this.baritone);
        MutableMoveResult result = new MutableMoveResult();
        return MovementDescend.dynamicFallCost(context, this.src.x, this.src.y, this.src.z, this.dest.x, this.dest.z, 0.0, context.get(this.dest.x, this.src.y - 2, this.dest.z), result);
    }

    @Override
    public MovementState updateState(MovementState state) {
        Vec3i avoid;
        boolean isWater;
        super.updateState(state);
        if (state.getStatus() != MovementStatus.RUNNING) {
            return state;
        }
        BetterBlockPos playerFeet = this.ctx.playerFeet();
        Rotation toDest = RotationUtils.calcRotationFromVec3d(this.ctx.playerHead(), VecUtils.getBlockPosCenter(this.dest), this.ctx.playerRotations());
        Rotation targetRotation = null;
        Block destBlock = this.ctx.world().getBlockState(this.dest).getBlock();
        boolean bl = isWater = destBlock == Blocks.WATER || destBlock == Blocks.FLOWING_WATER;
        if (!isWater && this.willPlaceBucket() && !((Vec3i)playerFeet).equals(this.dest)) {
            if (!InventoryPlayer.isHotbar(this.ctx.player().inventory.getSlotFor(STACK_BUCKET_WATER)) || this.ctx.world().provider.isNether()) {
                return state.setStatus(MovementStatus.UNREACHABLE);
            }
            if (this.ctx.player().posY - (double)this.dest.getY() < this.ctx.playerController().getBlockReachDistance() && !this.ctx.player().onGround) {
                this.ctx.player().inventory.currentItem = this.ctx.player().inventory.getSlotFor(STACK_BUCKET_WATER);
                targetRotation = new Rotation(toDest.getYaw(), 90.0f);
                if (this.ctx.isLookingAt(this.dest) || this.ctx.isLookingAt(this.dest.down())) {
                    state.setInput(Input.CLICK_RIGHT, true);
                }
            }
        }
        if (targetRotation != null) {
            state.setTarget(new MovementState.MovementTarget(targetRotation, true));
        } else {
            state.setTarget(new MovementState.MovementTarget(toDest, false));
        }
        if (((Vec3i)playerFeet).equals(this.dest) && (this.ctx.player().posY - (double)playerFeet.getY() < 0.094 || isWater)) {
            if (isWater) {
                if (InventoryPlayer.isHotbar(this.ctx.player().inventory.getSlotFor(STACK_BUCKET_EMPTY))) {
                    this.ctx.player().inventory.currentItem = this.ctx.player().inventory.getSlotFor(STACK_BUCKET_EMPTY);
                    if (this.ctx.player().motionY >= 0.0) {
                        return state.setInput(Input.CLICK_RIGHT, true);
                    }
                    return state;
                }
                if (this.ctx.player().motionY >= 0.0) {
                    return state.setStatus(MovementStatus.SUCCESS);
                }
            } else {
                return state.setStatus(MovementStatus.SUCCESS);
            }
        }
        Vec3d destCenter = VecUtils.getBlockPosCenter(this.dest);
        if (Math.abs(this.ctx.player().posX + this.ctx.player().motionX - destCenter.x) > 0.1 || Math.abs(this.ctx.player().posZ + this.ctx.player().motionZ - destCenter.z) > 0.1) {
            if (!this.ctx.player().onGround && Math.abs(this.ctx.player().motionY) > 0.4) {
                state.setInput(Input.SNEAK, true);
            }
            state.setInput(Input.MOVE_FORWARD, true);
        }
        if ((avoid = (Vec3i)Optional.ofNullable(this.avoid()).map(EnumFacing::getDirectionVec).orElse(null)) == null) {
            avoid = this.src.subtract(this.dest);
        } else {
            double dist = Math.abs((double)avoid.getX() * (destCenter.x - (double)avoid.getX() / 2.0 - this.ctx.player().posX)) + Math.abs((double)avoid.getZ() * (destCenter.z - (double)avoid.getZ() / 2.0 - this.ctx.player().posZ));
            if (dist < 0.6) {
                state.setInput(Input.MOVE_FORWARD, true);
            } else if (!this.ctx.player().onGround) {
                state.setInput(Input.SNEAK, false);
            }
        }
        if (targetRotation == null) {
            Vec3d destCenterOffset = new Vec3d(destCenter.x + 0.125 * (double)avoid.getX(), destCenter.y, destCenter.z + 0.125 * (double)avoid.getZ());
            state.setTarget(new MovementState.MovementTarget(RotationUtils.calcRotationFromVec3d(this.ctx.playerHead(), destCenterOffset, this.ctx.playerRotations()), false));
        }
        return state;
    }

    private EnumFacing avoid() {
        for (int i = 0; i < 15; ++i) {
            IBlockState state = this.ctx.world().getBlockState(this.ctx.playerFeet().down(i));
            if (state.getBlock() != Blocks.LADDER) continue;
            return state.getValue(BlockLadder.FACING);
        }
        return null;
    }

    @Override
    public boolean safeToCancel(MovementState state) {
        return this.ctx.playerFeet().equals(this.src) || state.getStatus() != MovementStatus.RUNNING;
    }

    private static BetterBlockPos[] buildPositionsToBreak(BetterBlockPos src, BetterBlockPos dest) {
        int diffX = src.getX() - dest.getX();
        int diffZ = src.getZ() - dest.getZ();
        int diffY = src.getY() - dest.getY();
        BetterBlockPos[] toBreak = new BetterBlockPos[diffY + 2];
        for (int i = 0; i < toBreak.length; ++i) {
            toBreak[i] = new BetterBlockPos(src.getX() - diffX, src.getY() + 1 - i, src.getZ() - diffZ);
        }
        return toBreak;
    }

    @Override
    protected boolean prepared(MovementState state) {
        if (state.getStatus() == MovementStatus.WAITING) {
            return true;
        }
        for (int i = 0; i < 4 && i < this.positionsToBreak.length; ++i) {
            if (MovementHelper.canWalkThrough(this.ctx, this.positionsToBreak[i])) continue;
            return super.prepared(state);
        }
        return true;
    }
}

