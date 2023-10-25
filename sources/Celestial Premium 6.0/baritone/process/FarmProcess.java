/*
 * Decompiled with CFR 0.150.
 */
package baritone.process;

import baritone.Baritone;
import baritone.api.pathing.goals.Goal;
import baritone.api.pathing.goals.GoalBlock;
import baritone.api.pathing.goals.GoalComposite;
import baritone.api.process.IFarmProcess;
import baritone.api.process.PathingCommand;
import baritone.api.process.PathingCommandType;
import baritone.api.utils.RayTraceUtils;
import baritone.api.utils.Rotation;
import baritone.api.utils.RotationUtils;
import baritone.api.utils.input.Input;
import baritone.cache.WorldScanner;
import baritone.pathing.movement.MovementHelper;
import baritone.process.BuilderProcess;
import baritone.utils.BaritoneProcessHelper;
import baritone.utils.NotificationHelper;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.function.Predicate;
import net.minecraft.block.Block;
import net.minecraft.block.BlockAir;
import net.minecraft.block.BlockCactus;
import net.minecraft.block.BlockCrops;
import net.minecraft.block.BlockNetherWart;
import net.minecraft.block.BlockReed;
import net.minecraft.block.IGrowable;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.EnumDyeColor;
import net.minecraft.item.Item;
import net.minecraft.item.ItemDye;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import org.celestial.client.Celestial;
import org.celestial.client.feature.impl.player.AutoEat;

public final class FarmProcess
extends BaritoneProcessHelper
implements IFarmProcess {
    private boolean active;
    private List<BlockPos> locations;
    private int tickCount;
    private int range;
    private BlockPos center;
    private static final List<Item> FARMLAND_PLANTABLE = Arrays.asList(Items.BEETROOT_SEEDS, Items.MELON_SEEDS, Items.WHEAT_SEEDS, Items.PUMPKIN_SEEDS, Items.POTATO, Items.CARROT);
    private static final List<Item> PICKUP_DROPPED = Arrays.asList(Items.BEETROOT_SEEDS, Items.BEETROOT, Items.MELON_SEEDS, Items.MELON, Item.getItemFromBlock(Blocks.MELON_BLOCK), Items.WHEAT_SEEDS, Items.WHEAT, Items.PUMPKIN_SEEDS, Item.getItemFromBlock(Blocks.PUMPKIN), Items.POTATO, Items.CARROT, Items.NETHER_WART, Items.REEDS, Item.getItemFromBlock(Blocks.CACTUS));

    public FarmProcess(Baritone baritone) {
        super(baritone);
    }

    @Override
    public boolean isActive() {
        return this.active;
    }

    @Override
    public void farm(int range, BlockPos pos) {
        this.center = pos == null ? this.baritone.getPlayerContext().playerFeet() : pos;
        this.range = range;
        this.active = true;
        this.locations = null;
    }

    private boolean readyForHarvest(World world, BlockPos pos, IBlockState state) {
        for (Harvest harvest : Harvest.values()) {
            if (harvest.block != state.getBlock()) continue;
            return harvest.readyToHarvest(world, pos, state);
        }
        return false;
    }

    private boolean isPlantable(ItemStack stack) {
        return FARMLAND_PLANTABLE.contains(stack.getItem());
    }

    private boolean isBoneMeal(ItemStack stack) {
        return !stack.isEmpty() && stack.getItem() instanceof ItemDye && EnumDyeColor.byDyeDamage(stack.getMetadata()) == EnumDyeColor.WHITE;
    }

    private boolean isNetherWart(ItemStack stack) {
        return !stack.isEmpty() && stack.getItem().equals(Items.NETHER_WART);
    }

    @Override
    public PathingCommand onTick(boolean calcFailed, boolean isSafeToCancel) {
        if (Celestial.instance.featureManager.getFeatureByClass(AutoEat.class).getState() && AutoEat.isEating) {
            return null;
        }
        ArrayList<Block> scan = new ArrayList<Block>();
        for (Harvest harvest : Harvest.values()) {
            scan.add(harvest.block);
        }
        if (((Boolean)Baritone.settings().replantCrops.value).booleanValue()) {
            scan.add(Blocks.FARMLAND);
            if (((Boolean)Baritone.settings().replantNetherWart.value).booleanValue()) {
                scan.add(Blocks.SOUL_SAND);
            }
        }
        if ((Integer)Baritone.settings().mineGoalUpdateInterval.value != 0 && this.tickCount++ % (Integer)Baritone.settings().mineGoalUpdateInterval.value == 0) {
            Baritone.getExecutor().execute(() -> {
                this.locations = WorldScanner.INSTANCE.scanChunkRadius(this.ctx, scan, 256, 10, 10);
            });
        }
        if (this.locations == null) {
            return new PathingCommand(null, PathingCommandType.REQUEST_PAUSE);
        }
        ArrayList<BlockPos> toBreak = new ArrayList<BlockPos>();
        ArrayList<BlockPos> openFarmland = new ArrayList<BlockPos>();
        ArrayList<BlockPos> bonemealable = new ArrayList<BlockPos>();
        ArrayList<BlockPos> openSoulsand = new ArrayList<BlockPos>();
        for (BlockPos blockPos : this.locations) {
            IGrowable ig;
            if (this.range != 0 && blockPos.getDistance(this.center.getX(), this.center.getY(), this.center.getZ()) > (double)this.range) continue;
            IBlockState iBlockState = this.ctx.world().getBlockState(blockPos);
            boolean bl = this.ctx.world().getBlockState(blockPos.up()).getBlock() instanceof BlockAir;
            if (iBlockState.getBlock() == Blocks.FARMLAND) {
                if (!bl) continue;
                openFarmland.add(blockPos);
                continue;
            }
            if (iBlockState.getBlock() == Blocks.SOUL_SAND) {
                if (!bl) continue;
                openSoulsand.add(blockPos);
                continue;
            }
            if (this.readyForHarvest(this.ctx.world(), blockPos, iBlockState)) {
                toBreak.add(blockPos);
                continue;
            }
            if (!(iBlockState.getBlock() instanceof IGrowable) || !(ig = (IGrowable)((Object)iBlockState.getBlock())).canGrow(this.ctx.world(), blockPos, iBlockState, true) || !ig.canUseBonemeal(this.ctx.world(), this.ctx.world().rand, blockPos, iBlockState)) continue;
            bonemealable.add(blockPos);
        }
        this.baritone.getInputOverrideHandler().clearAllKeys();
        for (BlockPos blockPos : toBreak) {
            Optional<Rotation> optional = RotationUtils.reachable(this.ctx, blockPos);
            if (!optional.isPresent() || !isSafeToCancel) continue;
            this.baritone.getLookBehavior().updateTarget(optional.get(), true);
            MovementHelper.switchToBestToolFor(this.ctx, this.ctx.world().getBlockState(blockPos));
            if (this.ctx.isLookingAt(blockPos)) {
                this.baritone.getInputOverrideHandler().setInputForceState(Input.CLICK_LEFT, true);
            }
            return new PathingCommand(null, PathingCommandType.REQUEST_PAUSE);
        }
        ArrayList<BlockPos> both = new ArrayList<BlockPos>(openFarmland);
        both.addAll(openSoulsand);
        for (BlockPos blockPos : both) {
            boolean bl = openSoulsand.contains(blockPos);
            Optional<Rotation> rot = RotationUtils.reachableOffset(this.ctx.player(), blockPos, new Vec3d((double)blockPos.getX() + 0.5, blockPos.getY() + 1, (double)blockPos.getZ() + 0.5), this.ctx.playerController().getBlockReachDistance(), false);
            if (!rot.isPresent() || !isSafeToCancel || !this.baritone.getInventoryBehavior().throwaway(true, bl ? this::isNetherWart : this::isPlantable)) continue;
            RayTraceResult result = RayTraceUtils.rayTraceTowards(this.ctx.player(), rot.get(), this.ctx.playerController().getBlockReachDistance());
            if (result.typeOfHit != RayTraceResult.Type.BLOCK || result.sideHit != EnumFacing.UP) continue;
            this.baritone.getLookBehavior().updateTarget(rot.get(), true);
            if (this.ctx.isLookingAt(blockPos)) {
                this.baritone.getInputOverrideHandler().setInputForceState(Input.CLICK_RIGHT, true);
            }
            return new PathingCommand(null, PathingCommandType.REQUEST_PAUSE);
        }
        for (BlockPos blockPos : bonemealable) {
            Optional<Rotation> optional = RotationUtils.reachable(this.ctx, blockPos);
            if (!optional.isPresent() || !isSafeToCancel || !this.baritone.getInventoryBehavior().throwaway(true, this::isBoneMeal)) continue;
            this.baritone.getLookBehavior().updateTarget(optional.get(), true);
            if (this.ctx.isLookingAt(blockPos)) {
                this.baritone.getInputOverrideHandler().setInputForceState(Input.CLICK_RIGHT, true);
            }
            return new PathingCommand(null, PathingCommandType.REQUEST_PAUSE);
        }
        if (calcFailed) {
            this.logDirect("Farm failed");
            if (((Boolean)Baritone.settings().desktopNotifications.value).booleanValue() && ((Boolean)Baritone.settings().notificationOnFarmFail.value).booleanValue()) {
                NotificationHelper.notify("Farm failed", true);
            }
            this.onLostControl();
            return new PathingCommand(null, PathingCommandType.REQUEST_PAUSE);
        }
        ArrayList<Goal> arrayList = new ArrayList<Goal>();
        for (BlockPos blockPos : toBreak) {
            arrayList.add(new BuilderProcess.GoalBreak(blockPos));
        }
        if (this.baritone.getInventoryBehavior().throwaway(false, this::isPlantable)) {
            for (BlockPos blockPos : openFarmland) {
                arrayList.add(new GoalBlock(blockPos.up()));
            }
        }
        if (this.baritone.getInventoryBehavior().throwaway(false, this::isNetherWart)) {
            for (BlockPos blockPos : openSoulsand) {
                arrayList.add(new GoalBlock(blockPos.up()));
            }
        }
        if (this.baritone.getInventoryBehavior().throwaway(false, this::isBoneMeal)) {
            for (BlockPos blockPos : bonemealable) {
                arrayList.add(new GoalBlock(blockPos));
            }
        }
        for (Entity entity : this.ctx.world().loadedEntityList) {
            EntityItem ei;
            if (!(entity instanceof EntityItem) || !entity.onGround || !PICKUP_DROPPED.contains((ei = (EntityItem)entity).getItem().getItem())) continue;
            arrayList.add(new GoalBlock(new BlockPos(entity.posX, entity.posY + 0.1, entity.posZ)));
        }
        return new PathingCommand(new GoalComposite(arrayList.toArray(new Goal[0])), PathingCommandType.SET_GOAL_AND_PATH);
    }

    @Override
    public void onLostControl() {
        this.active = false;
    }

    @Override
    public String displayName0() {
        return "Farming";
    }

    private static enum Harvest {
        WHEAT((BlockCrops)Blocks.WHEAT),
        CARROTS((BlockCrops)Blocks.CARROTS),
        POTATOES((BlockCrops)Blocks.POTATOES),
        BEETROOT((BlockCrops)Blocks.BEETROOTS),
        PUMPKIN(Blocks.PUMPKIN, state -> true),
        MELON(Blocks.MELON_BLOCK, state -> true),
        NETHERWART(Blocks.NETHER_WART, state -> state.getValue(BlockNetherWart.AGE) >= 3),
        SUGARCANE((Block)Blocks.REEDS, null){

            @Override
            public boolean readyToHarvest(World world, BlockPos pos, IBlockState state) {
                if (((Boolean)Baritone.settings().replantCrops.value).booleanValue()) {
                    return world.getBlockState(pos.down()).getBlock() instanceof BlockReed;
                }
                return true;
            }
        }
        ,
        CACTUS((Block)Blocks.CACTUS, null){

            @Override
            public boolean readyToHarvest(World world, BlockPos pos, IBlockState state) {
                if (((Boolean)Baritone.settings().replantCrops.value).booleanValue()) {
                    return world.getBlockState(pos.down()).getBlock() instanceof BlockCactus;
                }
                return true;
            }
        };

        public final Block block;
        public final Predicate<IBlockState> readyToHarvest;

        private Harvest(BlockCrops blockCrops) {
            this(blockCrops, blockCrops::isMaxAge);
        }

        private Harvest(Block block, Predicate<IBlockState> readyToHarvest) {
            this.block = block;
            this.readyToHarvest = readyToHarvest;
        }

        public boolean readyToHarvest(World world, BlockPos pos, IBlockState state) {
            return this.readyToHarvest.test(state);
        }
    }
}

