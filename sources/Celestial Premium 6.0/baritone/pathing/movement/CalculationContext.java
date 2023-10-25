/*
 * Decompiled with CFR 0.150.
 */
package baritone.pathing.movement;

import baritone.Baritone;
import baritone.api.IBaritone;
import baritone.cache.WorldData;
import baritone.utils.BlockStateInterface;
import baritone.utils.ToolSet;
import baritone.utils.pathing.BetterWorldBorder;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class CalculationContext {
    private static final ItemStack STACK_BUCKET_WATER = new ItemStack(Items.WATER_BUCKET);
    public final boolean safeForThreadedUse;
    public final IBaritone baritone;
    public final World world;
    public final WorldData worldData;
    public final BlockStateInterface bsi;
    public final ToolSet toolSet;
    public final boolean hasWaterBucket;
    public final boolean hasThrowaway;
    public final boolean canSprint;
    protected final double placeBlockCost;
    public final boolean allowBreak;
    public final boolean allowParkour;
    public final boolean allowParkourPlace;
    public final boolean allowJumpAt256;
    public final boolean allowParkourAscend;
    public final boolean assumeWalkOnWater;
    public final boolean allowDiagonalDescend;
    public final boolean allowDiagonalAscend;
    public final boolean allowDownward;
    public final int maxFallHeightNoWater;
    public final int maxFallHeightBucket;
    public final double waterWalkSpeed;
    public final double breakBlockAdditionalCost;
    public double backtrackCostFavoringCoefficient;
    public double jumpPenalty;
    public final double walkOnWaterOnePenalty;
    public final BetterWorldBorder worldBorder;

    public CalculationContext(IBaritone baritone) {
        this(baritone, false);
    }

    public CalculationContext(IBaritone baritone, boolean forUseOnAnotherThread) {
        this.safeForThreadedUse = forUseOnAnotherThread;
        this.baritone = baritone;
        EntityPlayerSP player = baritone.getPlayerContext().player();
        this.world = baritone.getPlayerContext().world();
        this.worldData = (WorldData)baritone.getWorldProvider().getCurrentWorld();
        this.bsi = new BlockStateInterface(this.world, this.worldData, forUseOnAnotherThread);
        this.toolSet = new ToolSet(player);
        this.hasThrowaway = (Boolean)Baritone.settings().allowPlace.value != false && ((Baritone)baritone).getInventoryBehavior().hasGenericThrowaway();
        this.hasWaterBucket = (Boolean)Baritone.settings().allowWaterBucketFall.value != false && InventoryPlayer.isHotbar(player.inventory.getSlotFor(STACK_BUCKET_WATER)) && !this.world.provider.isNether();
        this.canSprint = (Boolean)Baritone.settings().allowSprint.value != false && player.getFoodStats().getFoodLevel() > 6;
        this.placeBlockCost = (Double)Baritone.settings().blockPlacementPenalty.value;
        this.allowBreak = (Boolean)Baritone.settings().allowBreak.value;
        this.allowParkour = (Boolean)Baritone.settings().allowParkour.value;
        this.allowParkourPlace = (Boolean)Baritone.settings().allowParkourPlace.value;
        this.allowJumpAt256 = (Boolean)Baritone.settings().allowJumpAt256.value;
        this.allowParkourAscend = (Boolean)Baritone.settings().allowParkourAscend.value;
        this.assumeWalkOnWater = (Boolean)Baritone.settings().assumeWalkOnWater.value;
        this.allowDiagonalDescend = (Boolean)Baritone.settings().allowDiagonalDescend.value;
        this.allowDiagonalAscend = (Boolean)Baritone.settings().allowDiagonalAscend.value;
        this.allowDownward = (Boolean)Baritone.settings().allowDownward.value;
        this.maxFallHeightNoWater = (Integer)Baritone.settings().maxFallHeightNoWater.value;
        this.maxFallHeightBucket = (Integer)Baritone.settings().maxFallHeightBucket.value;
        int depth = EnchantmentHelper.getDepthStriderModifier(player);
        if (depth > 3) {
            depth = 3;
        }
        float mult = (float)depth / 3.0f;
        this.waterWalkSpeed = 9.09090909090909 * (double)(1.0f - mult) + 4.63284688441047 * (double)mult;
        this.breakBlockAdditionalCost = (Double)Baritone.settings().blockBreakAdditionalPenalty.value;
        this.backtrackCostFavoringCoefficient = (Double)Baritone.settings().backtrackCostFavoringCoefficient.value;
        this.jumpPenalty = (Double)Baritone.settings().jumpPenalty.value;
        this.walkOnWaterOnePenalty = (Double)Baritone.settings().walkOnWaterOnePenalty.value;
        this.worldBorder = new BetterWorldBorder(this.world.getWorldBorder());
    }

    public final IBaritone getBaritone() {
        return this.baritone;
    }

    public IBlockState get(int x, int y, int z) {
        return this.bsi.get0(x, y, z);
    }

    public boolean isLoaded(int x, int z) {
        return this.bsi.isLoaded(x, z);
    }

    public IBlockState get(BlockPos pos) {
        return this.get(pos.getX(), pos.getY(), pos.getZ());
    }

    public Block getBlock(int x, int y, int z) {
        return this.get(x, y, z).getBlock();
    }

    public double costOfPlacingAt(int x, int y, int z, IBlockState current) {
        if (!this.hasThrowaway) {
            return 1000000.0;
        }
        if (this.isPossiblyProtected(x, y, z)) {
            return 1000000.0;
        }
        if (!this.worldBorder.canPlaceAt(x, z)) {
            return 1000000.0;
        }
        return this.placeBlockCost;
    }

    public double breakCostMultiplierAt(int x, int y, int z, IBlockState current) {
        if (!this.allowBreak) {
            return 1000000.0;
        }
        if (this.isPossiblyProtected(x, y, z)) {
            return 1000000.0;
        }
        return 1.0;
    }

    public double placeBucketCost() {
        return this.placeBlockCost;
    }

    public boolean isPossiblyProtected(int x, int y, int z) {
        return false;
    }
}

