/*
 * Decompiled with CFR 0.150.
 */
package baritone.pathing.movement.movements;

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
import java.util.HashSet;
import java.util.Set;
import net.minecraft.block.Block;
import net.minecraft.block.BlockLiquid;
import net.minecraft.block.BlockStairs;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.util.EnumFacing;

public class MovementParkour
extends Movement {
    private static final BetterBlockPos[] EMPTY = new BetterBlockPos[0];
    private final EnumFacing direction;
    private final int dist;
    private final boolean ascend;

    private MovementParkour(IBaritone baritone, BetterBlockPos src, int dist, EnumFacing dir, boolean ascend) {
        super(baritone, src, src.offset(dir, dist).up(ascend ? 1 : 0), EMPTY, src.offset(dir, dist).down(ascend ? 0 : 1));
        this.direction = dir;
        this.dist = dist;
        this.ascend = ascend;
    }

    public static MovementParkour cost(CalculationContext context, BetterBlockPos src, EnumFacing direction) {
        MutableMoveResult res = new MutableMoveResult();
        MovementParkour.cost(context, src.x, src.y, src.z, direction, res);
        int dist = Math.abs(res.x - src.x) + Math.abs(res.z - src.z);
        return new MovementParkour(context.getBaritone(), src, dist, direction, res.y > src.y);
    }

    public static void cost(CalculationContext context, int x, int y, int z, EnumFacing dir, MutableMoveResult res) {
        int zDiff;
        if (!context.allowParkour) {
            return;
        }
        if (y == 256 && !context.allowJumpAt256) {
            return;
        }
        int xDiff = dir.getXOffset();
        if (!MovementHelper.fullyPassable(context, x + xDiff, y, z + (zDiff = dir.getZOffset()))) {
            return;
        }
        IBlockState adj = context.get(x + xDiff, y - 1, z + zDiff);
        if (MovementHelper.canWalkOn(context.bsi, x + xDiff, y - 1, z + zDiff, adj)) {
            return;
        }
        if (MovementHelper.avoidWalkingInto(adj.getBlock()) && adj.getBlock() != Blocks.WATER && adj.getBlock() != Blocks.FLOWING_WATER) {
            return;
        }
        if (!MovementHelper.fullyPassable(context, x + xDiff, y + 1, z + zDiff)) {
            return;
        }
        if (!MovementHelper.fullyPassable(context, x + xDiff, y + 2, z + zDiff)) {
            return;
        }
        if (!MovementHelper.fullyPassable(context, x, y + 2, z)) {
            return;
        }
        IBlockState standingOn = context.get(x, y - 1, z);
        if (standingOn.getBlock() == Blocks.VINE || standingOn.getBlock() == Blocks.LADDER || standingOn.getBlock() instanceof BlockStairs || MovementHelper.isBottomSlab(standingOn) || standingOn.getBlock() instanceof BlockLiquid) {
            return;
        }
        int maxJump = standingOn.getBlock() == Blocks.SOUL_SAND ? 2 : (context.canSprint ? 4 : 3);
        for (int i = 2; i <= maxJump; ++i) {
            int destX = x + xDiff * i;
            int destZ = z + zDiff * i;
            if (!MovementHelper.fullyPassable(context, destX, y + 1, destZ)) {
                return;
            }
            if (!MovementHelper.fullyPassable(context, destX, y + 2, destZ)) {
                return;
            }
            IBlockState destInto = context.bsi.get0(destX, y, destZ);
            if (!MovementHelper.fullyPassable(context.bsi.access, context.bsi.isPassableBlockPos.setPos(destX, y, destZ), destInto)) {
                if (i <= 3 && context.allowParkourAscend && context.canSprint && MovementHelper.canWalkOn(context.bsi, destX, y, destZ, destInto) && MovementParkour.checkOvershootSafety(context.bsi, destX + xDiff, y + 1, destZ + zDiff)) {
                    res.x = destX;
                    res.y = y + 1;
                    res.z = destZ;
                    res.cost = (double)i * 3.563791874554526 + context.jumpPenalty;
                }
                return;
            }
            IBlockState landingOn = context.bsi.get0(destX, y - 1, destZ);
            if (landingOn.getBlock() != Blocks.FARMLAND && MovementHelper.canWalkOn(context.bsi, destX, y - 1, destZ, landingOn)) {
                if (MovementParkour.checkOvershootSafety(context.bsi, destX + xDiff, y, destZ + zDiff)) {
                    res.x = destX;
                    res.y = y;
                    res.z = destZ;
                    res.cost = MovementParkour.costFromJumpDistance(i) + context.jumpPenalty;
                }
                return;
            }
            if (MovementHelper.fullyPassable(context, destX, y + 3, destZ)) continue;
            return;
        }
        if (maxJump != 4) {
            return;
        }
        if (!context.allowParkourPlace) {
            return;
        }
        int destX = x + 4 * xDiff;
        int destZ = z + 4 * zDiff;
        IBlockState toReplace = context.get(destX, y - 1, destZ);
        double placeCost = context.costOfPlacingAt(destX, y - 1, destZ, toReplace);
        if (placeCost >= 1000000.0) {
            return;
        }
        if (!MovementHelper.isReplaceable(destX, y - 1, destZ, toReplace, context.bsi)) {
            return;
        }
        if (!MovementParkour.checkOvershootSafety(context.bsi, destX + xDiff, y, destZ + zDiff)) {
            return;
        }
        for (int i = 0; i < 5; ++i) {
            int againstX = destX + HORIZONTALS_BUT_ALSO_DOWN_____SO_EVERY_DIRECTION_EXCEPT_UP[i].getXOffset();
            int againstY = y - 1 + HORIZONTALS_BUT_ALSO_DOWN_____SO_EVERY_DIRECTION_EXCEPT_UP[i].getYOffset();
            int againstZ = destZ + HORIZONTALS_BUT_ALSO_DOWN_____SO_EVERY_DIRECTION_EXCEPT_UP[i].getZOffset();
            if (againstX == x + xDiff * 3 && againstZ == z + zDiff * 3 || !MovementHelper.canPlaceAgainst(context.bsi, againstX, againstY, againstZ)) continue;
            res.x = destX;
            res.y = y;
            res.z = destZ;
            res.cost = MovementParkour.costFromJumpDistance(4) + placeCost + context.jumpPenalty;
            return;
        }
    }

    private static boolean checkOvershootSafety(BlockStateInterface bsi, int x, int y, int z) {
        return !MovementHelper.avoidWalkingInto(bsi.get0(x, y, z).getBlock()) && !MovementHelper.avoidWalkingInto(bsi.get0(x, y + 1, z).getBlock());
    }

    private static double costFromJumpDistance(int dist) {
        switch (dist) {
            case 2: {
                return 9.26569376882094;
            }
            case 3: {
                return 13.89854065323141;
            }
            case 4: {
                return 14.255167498218103;
            }
        }
        throw new IllegalStateException("LOL " + dist);
    }

    @Override
    public double calculateCost(CalculationContext context) {
        MutableMoveResult res = new MutableMoveResult();
        MovementParkour.cost(context, this.src.x, this.src.y, this.src.z, this.direction, res);
        if (res.x != this.dest.x || res.y != this.dest.y || res.z != this.dest.z) {
            return 1000000.0;
        }
        return res.cost;
    }

    @Override
    protected Set<BetterBlockPos> calculateValidPositions() {
        HashSet<BetterBlockPos> set = new HashSet<BetterBlockPos>();
        for (int i = 0; i <= this.dist; ++i) {
            for (int y = 0; y < 2; ++y) {
                set.add(this.src.offset(this.direction, i).up(y));
            }
        }
        return set;
    }

    @Override
    public boolean safeToCancel(MovementState state) {
        return state.getStatus() != MovementStatus.RUNNING;
    }

    @Override
    public MovementState updateState(MovementState state) {
        super.updateState(state);
        if (state.getStatus() != MovementStatus.RUNNING) {
            return state;
        }
        if (this.ctx.playerFeet().y < this.src.y) {
            this.logDebug("sorry");
            return state.setStatus(MovementStatus.UNREACHABLE);
        }
        if (this.dist >= 4 || this.ascend) {
            state.setInput(Input.SPRINT, true);
        }
        MovementHelper.moveTowards(this.ctx, state, this.dest);
        if (this.ctx.playerFeet().equals(this.dest)) {
            Block d = BlockStateInterface.getBlock(this.ctx, this.dest);
            if (d == Blocks.VINE || d == Blocks.LADDER) {
                return state.setStatus(MovementStatus.SUCCESS);
            }
            if (this.ctx.player().posY - (double)this.ctx.playerFeet().getY() < 0.094) {
                state.setStatus(MovementStatus.SUCCESS);
            }
        } else if (!this.ctx.playerFeet().equals(this.src)) {
            if (this.ctx.playerFeet().equals(this.src.offset(this.direction)) || this.ctx.player().posY - (double)this.src.y > 1.0E-4) {
                if (!MovementHelper.canWalkOn(this.ctx, this.dest.down()) && !this.ctx.player().onGround && MovementHelper.attemptToPlaceABlock(state, this.baritone, this.dest.down(), true, false) == MovementHelper.PlaceResult.READY_TO_PLACE) {
                    state.setInput(Input.CLICK_RIGHT, true);
                }
                if (this.dist == 3 && !this.ascend) {
                    double xDiff = (double)this.src.x + 0.5 - this.ctx.player().posX;
                    double zDiff = (double)this.src.z + 0.5 - this.ctx.player().posZ;
                    double distFromStart = Math.max(Math.abs(xDiff), Math.abs(zDiff));
                    if (distFromStart < 0.7) {
                        return state;
                    }
                }
                state.setInput(Input.JUMP, true);
            } else if (!this.ctx.playerFeet().equals(this.dest.offset(this.direction, -1))) {
                state.setInput(Input.SPRINT, false);
                if (this.ctx.playerFeet().equals(this.src.offset(this.direction, -1))) {
                    MovementHelper.moveTowards(this.ctx, state, this.src);
                } else {
                    MovementHelper.moveTowards(this.ctx, state, this.src.offset(this.direction, -1));
                }
            }
        }
        return state;
    }
}

