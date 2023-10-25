/*
 * Decompiled with CFR 0.150.
 */
package baritone.pathing.movement;

import baritone.Baritone;
import baritone.api.BaritoneAPI;
import baritone.api.IBaritone;
import baritone.api.pathing.movement.ActionCosts;
import baritone.api.pathing.movement.MovementStatus;
import baritone.api.utils.BetterBlockPos;
import baritone.api.utils.Helper;
import baritone.api.utils.IPlayerContext;
import baritone.api.utils.RayTraceUtils;
import baritone.api.utils.Rotation;
import baritone.api.utils.RotationUtils;
import baritone.api.utils.VecUtils;
import baritone.api.utils.input.Input;
import baritone.pathing.movement.CalculationContext;
import baritone.pathing.movement.Movement;
import baritone.pathing.movement.MovementState;
import baritone.utils.BlockStateInterface;
import baritone.utils.ToolSet;
import java.util.List;
import java.util.Optional;
import net.minecraft.block.Block;
import net.minecraft.block.BlockDoor;
import net.minecraft.block.BlockDoublePlant;
import net.minecraft.block.BlockEndPortal;
import net.minecraft.block.BlockFalling;
import net.minecraft.block.BlockFenceGate;
import net.minecraft.block.BlockHorizontal;
import net.minecraft.block.BlockLilyPad;
import net.minecraft.block.BlockLiquid;
import net.minecraft.block.BlockSilverfish;
import net.minecraft.block.BlockSkull;
import net.minecraft.block.BlockSlab;
import net.minecraft.block.BlockSnow;
import net.minecraft.block.BlockStairs;
import net.minecraft.block.BlockTrapDoor;
import net.minecraft.block.properties.PropertyBool;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.IBlockAccess;

public interface MovementHelper
extends ActionCosts,
Helper {
    public static boolean avoidBreaking(BlockStateInterface bsi, int x, int y, int z, IBlockState state) {
        Block b = state.getBlock();
        return b == Blocks.ICE || b instanceof BlockSilverfish || MovementHelper.avoidAdjacentBreaking(bsi, x, y + 1, z, true) || MovementHelper.avoidAdjacentBreaking(bsi, x + 1, y, z, false) || MovementHelper.avoidAdjacentBreaking(bsi, x - 1, y, z, false) || MovementHelper.avoidAdjacentBreaking(bsi, x, y, z + 1, false) || MovementHelper.avoidAdjacentBreaking(bsi, x, y, z - 1, false);
    }

    public static boolean avoidAdjacentBreaking(BlockStateInterface bsi, int x, int y, int z, boolean directlyAbove) {
        IBlockState state = bsi.get0(x, y, z);
        Block block = state.getBlock();
        if (!directlyAbove && block instanceof BlockFalling && ((Boolean)Baritone.settings().avoidUpdatingFallingBlocks.value).booleanValue() && BlockFalling.canFallThrough(bsi.get0(x, y - 1, z))) {
            return true;
        }
        return block instanceof BlockLiquid;
    }

    public static boolean canWalkThrough(IPlayerContext ctx, BetterBlockPos pos) {
        return MovementHelper.canWalkThrough(new BlockStateInterface(ctx), pos.x, pos.y, pos.z);
    }

    public static boolean canWalkThrough(BlockStateInterface bsi, int x, int y, int z) {
        return MovementHelper.canWalkThrough(bsi, x, y, z, bsi.get0(x, y, z));
    }

    public static boolean canWalkThrough(BlockStateInterface bsi, int x, int y, int z, IBlockState state) {
        Block block = state.getBlock();
        if (block == Blocks.AIR) {
            return true;
        }
        if (block == Blocks.FIRE || block == Blocks.TRIPWIRE || block == Blocks.WEB || block == Blocks.END_PORTAL || block == Blocks.COCOA || block instanceof BlockSkull || block instanceof BlockTrapDoor || block == Blocks.END_ROD) {
            return false;
        }
        if (((List)Baritone.settings().blocksToAvoid.value).contains(block)) {
            return false;
        }
        if (block instanceof BlockDoor || block instanceof BlockFenceGate) {
            return block != Blocks.IRON_DOOR;
        }
        if (block == Blocks.CARPET) {
            return MovementHelper.canWalkOn(bsi, x, y - 1, z);
        }
        if (block instanceof BlockSnow) {
            if (!bsi.worldContainsLoadedChunk(x, z)) {
                return true;
            }
            if (state.getValue(BlockSnow.LAYERS) >= 3) {
                return false;
            }
            return MovementHelper.canWalkOn(bsi, x, y - 1, z);
        }
        if (MovementHelper.isFlowing(x, y, z, state, bsi)) {
            return false;
        }
        if (block instanceof BlockLiquid) {
            if (((Boolean)Baritone.settings().assumeWalkOnWater.value).booleanValue()) {
                return false;
            }
            IBlockState up = bsi.get0(x, y + 1, z);
            if (up.getBlock() instanceof BlockLiquid || up.getBlock() instanceof BlockLilyPad) {
                return false;
            }
            return block == Blocks.WATER || block == Blocks.FLOWING_WATER;
        }
        return block.isPassable(bsi.access, bsi.isPassableBlockPos.setPos(x, y, z));
    }

    public static boolean fullyPassable(CalculationContext context, int x, int y, int z) {
        return MovementHelper.fullyPassable(context.bsi.access, context.bsi.isPassableBlockPos.setPos(x, y, z), context.bsi.get0(x, y, z));
    }

    public static boolean fullyPassable(IPlayerContext ctx, BlockPos pos) {
        return MovementHelper.fullyPassable(ctx.world(), pos, ctx.world().getBlockState(pos));
    }

    public static boolean fullyPassable(IBlockAccess access, BlockPos pos, IBlockState state) {
        Block block = state.getBlock();
        if (block == Blocks.AIR) {
            return true;
        }
        if (block == Blocks.FIRE || block == Blocks.TRIPWIRE || block == Blocks.WEB || block == Blocks.VINE || block == Blocks.LADDER || block == Blocks.COCOA || block instanceof BlockDoor || block instanceof BlockFenceGate || block instanceof BlockSnow || block instanceof BlockLiquid || block instanceof BlockTrapDoor || block instanceof BlockEndPortal || block instanceof BlockSkull) {
            return false;
        }
        return block.isPassable(access, pos);
    }

    public static boolean isReplaceable(int x, int y, int z, IBlockState state, BlockStateInterface bsi) {
        Block block = state.getBlock();
        if (block == Blocks.AIR || MovementHelper.isWater(block)) {
            return true;
        }
        if (block instanceof BlockSnow) {
            if (!bsi.worldContainsLoadedChunk(x, z)) {
                return true;
            }
            return state.getValue(BlockSnow.LAYERS) == 1;
        }
        if (block instanceof BlockDoublePlant) {
            BlockDoublePlant.EnumPlantType kek = state.getValue(BlockDoublePlant.VARIANT);
            return kek == BlockDoublePlant.EnumPlantType.FERN || kek == BlockDoublePlant.EnumPlantType.GRASS;
        }
        return state.getMaterial().isReplaceable();
    }

    @Deprecated
    public static boolean isReplacable(int x, int y, int z, IBlockState state, BlockStateInterface bsi) {
        return MovementHelper.isReplaceable(x, y, z, state, bsi);
    }

    public static boolean isDoorPassable(IPlayerContext ctx, BlockPos doorPos, BlockPos playerPos) {
        if (playerPos.equals(doorPos)) {
            return false;
        }
        IBlockState state = BlockStateInterface.get(ctx, doorPos);
        if (!(state.getBlock() instanceof BlockDoor)) {
            return true;
        }
        return MovementHelper.isHorizontalBlockPassable(doorPos, state, playerPos, BlockDoor.OPEN);
    }

    public static boolean isGatePassable(IPlayerContext ctx, BlockPos gatePos, BlockPos playerPos) {
        if (playerPos.equals(gatePos)) {
            return false;
        }
        IBlockState state = BlockStateInterface.get(ctx, gatePos);
        if (!(state.getBlock() instanceof BlockFenceGate)) {
            return true;
        }
        return state.getValue(BlockFenceGate.OPEN);
    }

    public static boolean isHorizontalBlockPassable(BlockPos blockPos, IBlockState blockState, BlockPos playerPos, PropertyBool propertyOpen) {
        EnumFacing.Axis playerFacing;
        if (playerPos.equals(blockPos)) {
            return false;
        }
        EnumFacing.Axis facing = blockState.getValue(BlockHorizontal.FACING).getAxis();
        boolean open = blockState.getValue(propertyOpen);
        if (playerPos.north().equals(blockPos) || playerPos.south().equals(blockPos)) {
            playerFacing = EnumFacing.Axis.Z;
        } else if (playerPos.east().equals(blockPos) || playerPos.west().equals(blockPos)) {
            playerFacing = EnumFacing.Axis.X;
        } else {
            return true;
        }
        return facing == playerFacing == open;
    }

    public static boolean avoidWalkingInto(Block block) {
        return block instanceof BlockLiquid || block == Blocks.MAGMA || block == Blocks.CACTUS || block == Blocks.FIRE || block == Blocks.END_PORTAL || block == Blocks.WEB;
    }

    public static boolean canWalkOn(BlockStateInterface bsi, int x, int y, int z, IBlockState state) {
        Block block = state.getBlock();
        if (block == Blocks.AIR || block == Blocks.MAGMA) {
            return false;
        }
        if (state.isBlockNormalCube()) {
            return true;
        }
        if (block == Blocks.LADDER || block == Blocks.VINE && ((Boolean)Baritone.settings().allowVines.value).booleanValue()) {
            return true;
        }
        if (block == Blocks.FARMLAND || block == Blocks.GRASS_PATH) {
            return true;
        }
        if (block == Blocks.ENDER_CHEST || block == Blocks.CHEST || block == Blocks.TRAPPED_CHEST) {
            return true;
        }
        if (MovementHelper.isWater(block)) {
            Block up = bsi.get0(x, y + 1, z).getBlock();
            if (up == Blocks.WATERLILY || up == Blocks.CARPET) {
                return true;
            }
            if (MovementHelper.isFlowing(x, y, z, state, bsi) || block == Blocks.FLOWING_WATER) {
                return MovementHelper.isWater(up) && (Boolean)Baritone.settings().assumeWalkOnWater.value == false;
            }
            return MovementHelper.isWater(up) ^ (Boolean)Baritone.settings().assumeWalkOnWater.value;
        }
        if (((Boolean)Baritone.settings().assumeWalkOnLava.value).booleanValue() && MovementHelper.isLava(block) && !MovementHelper.isFlowing(x, y, z, state, bsi)) {
            return true;
        }
        if (block == Blocks.GLASS || block == Blocks.STAINED_GLASS) {
            return true;
        }
        if (block instanceof BlockSlab) {
            if (!((Boolean)Baritone.settings().allowWalkOnBottomSlab.value).booleanValue()) {
                if (((BlockSlab)block).isDouble()) {
                    return true;
                }
                return state.getValue(BlockSlab.HALF) != BlockSlab.EnumBlockHalf.BOTTOM;
            }
            return true;
        }
        return block instanceof BlockStairs;
    }

    public static boolean canWalkOn(IPlayerContext ctx, BetterBlockPos pos, IBlockState state) {
        return MovementHelper.canWalkOn(new BlockStateInterface(ctx), pos.x, pos.y, pos.z, state);
    }

    public static boolean canWalkOn(IPlayerContext ctx, BlockPos pos) {
        return MovementHelper.canWalkOn(new BlockStateInterface(ctx), pos.getX(), pos.getY(), pos.getZ());
    }

    public static boolean canWalkOn(IPlayerContext ctx, BetterBlockPos pos) {
        return MovementHelper.canWalkOn(new BlockStateInterface(ctx), pos.x, pos.y, pos.z);
    }

    public static boolean canWalkOn(BlockStateInterface bsi, int x, int y, int z) {
        return MovementHelper.canWalkOn(bsi, x, y, z, bsi.get0(x, y, z));
    }

    public static boolean canPlaceAgainst(BlockStateInterface bsi, int x, int y, int z) {
        return MovementHelper.canPlaceAgainst(bsi, x, y, z, bsi.get0(x, y, z));
    }

    public static boolean canPlaceAgainst(BlockStateInterface bsi, BlockPos pos) {
        return MovementHelper.canPlaceAgainst(bsi, pos.getX(), pos.getY(), pos.getZ());
    }

    public static boolean canPlaceAgainst(IPlayerContext ctx, BlockPos pos) {
        return MovementHelper.canPlaceAgainst(new BlockStateInterface(ctx), pos);
    }

    public static boolean canPlaceAgainst(BlockStateInterface bsi, int x, int y, int z, IBlockState state) {
        return state.isBlockNormalCube() || state.isFullBlock() || state.getBlock() == Blocks.GLASS || state.getBlock() == Blocks.STAINED_GLASS;
    }

    public static double getMiningDurationTicks(CalculationContext context, int x, int y, int z, boolean includeFalling) {
        return MovementHelper.getMiningDurationTicks(context, x, y, z, context.get(x, y, z), includeFalling);
    }

    public static double getMiningDurationTicks(CalculationContext context, int x, int y, int z, IBlockState state, boolean includeFalling) {
        Block block = state.getBlock();
        if (!MovementHelper.canWalkThrough(context.bsi, x, y, z, state)) {
            IBlockState above;
            if (block instanceof BlockLiquid) {
                return 1000000.0;
            }
            double mult = context.breakCostMultiplierAt(x, y, z, state);
            if (mult >= 1000000.0) {
                return 1000000.0;
            }
            if (MovementHelper.avoidBreaking(context.bsi, x, y, z, state)) {
                return 1000000.0;
            }
            double strVsBlock = context.toolSet.getStrVsBlock(state);
            if (strVsBlock <= 0.0) {
                return 1000000.0;
            }
            double result = 1.0 / strVsBlock;
            result += context.breakBlockAdditionalCost;
            result *= mult;
            if (includeFalling && (above = context.get(x, y + 1, z)).getBlock() instanceof BlockFalling) {
                result += MovementHelper.getMiningDurationTicks(context, x, y + 1, z, above, true);
            }
            return result;
        }
        return 0.0;
    }

    public static boolean isBottomSlab(IBlockState state) {
        return state.getBlock() instanceof BlockSlab && !((BlockSlab)state.getBlock()).isDouble() && state.getValue(BlockSlab.HALF) == BlockSlab.EnumBlockHalf.BOTTOM;
    }

    public static void switchToBestToolFor(IPlayerContext ctx, IBlockState b) {
        MovementHelper.switchToBestToolFor(ctx, b, new ToolSet(ctx.player()), (Boolean)BaritoneAPI.getSettings().preferSilkTouch.value);
    }

    public static void switchToBestToolFor(IPlayerContext ctx, IBlockState b, ToolSet ts, boolean preferSilkTouch) {
        if (!((Boolean)Baritone.settings().disableAutoTool.value).booleanValue() && !((Boolean)Baritone.settings().assumeExternalAutoTool.value).booleanValue()) {
            ctx.player().inventory.currentItem = ts.getBestSlot(b.getBlock(), preferSilkTouch);
        }
    }

    public static void moveTowards(IPlayerContext ctx, MovementState state, BlockPos pos) {
        state.setTarget(new MovementState.MovementTarget(new Rotation(RotationUtils.calcRotationFromVec3d(ctx.playerHead(), VecUtils.getBlockPosCenter(pos), ctx.playerRotations()).getYaw(), ctx.player().rotationPitch), false)).setInput(Input.MOVE_FORWARD, true);
    }

    public static boolean isWater(Block b) {
        return b == Blocks.FLOWING_WATER || b == Blocks.WATER;
    }

    public static boolean isWater(IPlayerContext ctx, BlockPos bp) {
        return MovementHelper.isWater(BlockStateInterface.getBlock(ctx, bp));
    }

    public static boolean isLava(Block b) {
        return b == Blocks.FLOWING_LAVA || b == Blocks.LAVA;
    }

    public static boolean isLiquid(IPlayerContext ctx, BlockPos p) {
        return BlockStateInterface.getBlock(ctx, p) instanceof BlockLiquid;
    }

    public static boolean possiblyFlowing(IBlockState state) {
        return state.getBlock() instanceof BlockLiquid && state.getValue(BlockLiquid.LEVEL) != 0;
    }

    public static boolean isFlowing(int x, int y, int z, IBlockState state, BlockStateInterface bsi) {
        if (!(state.getBlock() instanceof BlockLiquid)) {
            return false;
        }
        if (state.getValue(BlockLiquid.LEVEL) != 0) {
            return true;
        }
        return MovementHelper.possiblyFlowing(bsi.get0(x + 1, y, z)) || MovementHelper.possiblyFlowing(bsi.get0(x - 1, y, z)) || MovementHelper.possiblyFlowing(bsi.get0(x, y, z + 1)) || MovementHelper.possiblyFlowing(bsi.get0(x, y, z - 1));
    }

    public static PlaceResult attemptToPlaceABlock(MovementState state, IBaritone baritone, BlockPos placeAt, boolean preferDown, boolean wouldSneak) {
        IPlayerContext ctx = baritone.getPlayerContext();
        Optional<Rotation> direct = RotationUtils.reachable(ctx, placeAt, wouldSneak);
        boolean found = false;
        if (direct.isPresent()) {
            state.setTarget(new MovementState.MovementTarget(direct.get(), true));
            found = true;
        }
        for (int i = 0; i < 5; ++i) {
            BlockPos against1 = placeAt.offset(Movement.HORIZONTALS_BUT_ALSO_DOWN_____SO_EVERY_DIRECTION_EXCEPT_UP[i]);
            if (!MovementHelper.canPlaceAgainst(ctx, against1)) continue;
            if (!((Baritone)baritone).getInventoryBehavior().selectThrowawayForLocation(false, placeAt.getX(), placeAt.getY(), placeAt.getZ())) {
                Helper.HELPER.logDebug("bb pls get me some blocks. dirt, netherrack, cobble");
                state.setStatus(MovementStatus.UNREACHABLE);
                return PlaceResult.NO_OPTION;
            }
            double faceX = ((double)(placeAt.getX() + against1.getX()) + 1.0) * 0.5;
            double faceY = ((double)(placeAt.getY() + against1.getY()) + 0.5) * 0.5;
            double faceZ = ((double)(placeAt.getZ() + against1.getZ()) + 1.0) * 0.5;
            Rotation place = RotationUtils.calcRotationFromVec3d(wouldSneak ? RayTraceUtils.inferSneakingEyePosition(ctx.player()) : ctx.playerHead(), new Vec3d(faceX, faceY, faceZ), ctx.playerRotations());
            RayTraceResult res = RayTraceUtils.rayTraceTowards(ctx.player(), place, ctx.playerController().getBlockReachDistance(), wouldSneak);
            if (res == null || res.typeOfHit != RayTraceResult.Type.BLOCK || !res.getBlockPos().equals(against1) || !res.getBlockPos().offset(res.sideHit).equals(placeAt)) continue;
            state.setTarget(new MovementState.MovementTarget(place, true));
            found = true;
            if (!preferDown) break;
        }
        if (ctx.getSelectedBlock().isPresent()) {
            BlockPos selectedBlock = ctx.getSelectedBlock().get();
            EnumFacing side = ctx.objectMouseOver().sideHit;
            if (selectedBlock.equals(placeAt) || MovementHelper.canPlaceAgainst(ctx, selectedBlock) && selectedBlock.offset(side).equals(placeAt)) {
                if (wouldSneak) {
                    state.setInput(Input.SNEAK, true);
                }
                ((Baritone)baritone).getInventoryBehavior().selectThrowawayForLocation(true, placeAt.getX(), placeAt.getY(), placeAt.getZ());
                return PlaceResult.READY_TO_PLACE;
            }
        }
        if (found) {
            if (wouldSneak) {
                state.setInput(Input.SNEAK, true);
            }
            ((Baritone)baritone).getInventoryBehavior().selectThrowawayForLocation(true, placeAt.getX(), placeAt.getY(), placeAt.getZ());
            return PlaceResult.ATTEMPTING;
        }
        return PlaceResult.NO_OPTION;
    }

    public static boolean isTransparent(Block b) {
        return b == Blocks.AIR || b == Blocks.FLOWING_LAVA || b == Blocks.FLOWING_WATER || b == Blocks.WATER;
    }

    public static enum PlaceResult {
        READY_TO_PLACE,
        ATTEMPTING,
        NO_OPTION;

    }
}

