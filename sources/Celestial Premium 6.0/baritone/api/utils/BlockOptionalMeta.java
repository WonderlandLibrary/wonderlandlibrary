/*
 * Decompiled with CFR 0.150.
 */
package baritone.api.utils;

import baritone.api.utils.accessor.IItemStack;
import com.google.common.collect.ImmutableSet;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import java.util.Set;
import java.util.function.Consumer;
import java.util.regex.MatchResult;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import net.minecraft.block.Block;
import net.minecraft.block.BlockBanner;
import net.minecraft.block.BlockBed;
import net.minecraft.block.BlockBrewingStand;
import net.minecraft.block.BlockButton;
import net.minecraft.block.BlockChorusPlant;
import net.minecraft.block.BlockDirt;
import net.minecraft.block.BlockDoor;
import net.minecraft.block.BlockDoublePlant;
import net.minecraft.block.BlockFence;
import net.minecraft.block.BlockFire;
import net.minecraft.block.BlockGrass;
import net.minecraft.block.BlockLeaves;
import net.minecraft.block.BlockLever;
import net.minecraft.block.BlockLog;
import net.minecraft.block.BlockPane;
import net.minecraft.block.BlockQuartz;
import net.minecraft.block.BlockRailBase;
import net.minecraft.block.BlockRedstoneWire;
import net.minecraft.block.BlockSapling;
import net.minecraft.block.BlockSkull;
import net.minecraft.block.BlockSlab;
import net.minecraft.block.BlockStairs;
import net.minecraft.block.BlockStandingSign;
import net.minecraft.block.BlockStem;
import net.minecraft.block.BlockTrapDoor;
import net.minecraft.block.BlockTripWire;
import net.minecraft.block.BlockVine;
import net.minecraft.block.BlockWall;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.state.IBlockState;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ResourceLocation;

public final class BlockOptionalMeta {
    private final Block block;
    private final int meta;
    private final boolean noMeta;
    private final Set<IBlockState> blockstates;
    private final ImmutableSet<Integer> stateHashes;
    private final ImmutableSet<Integer> stackHashes;
    private static final Pattern pattern = Pattern.compile("^(.+?)(?::(\\d+))?$");
    private static final Map<Object, Object> normalizations;

    public BlockOptionalMeta(@Nonnull Block block, @Nullable Integer meta) {
        this.block = block;
        this.noMeta = meta == null;
        this.meta = this.noMeta ? 0 : meta;
        this.blockstates = BlockOptionalMeta.getStates(block, meta);
        this.stateHashes = BlockOptionalMeta.getStateHashes(this.blockstates);
        this.stackHashes = BlockOptionalMeta.getStackHashes(this.blockstates);
    }

    public BlockOptionalMeta(@Nonnull Block block) {
        this(block, null);
    }

    public BlockOptionalMeta(@Nonnull String selector) {
        Matcher matcher = pattern.matcher(selector);
        if (!matcher.find()) {
            throw new IllegalArgumentException("invalid block selector");
        }
        MatchResult matchResult = matcher.toMatchResult();
        this.noMeta = matchResult.group(2) == null;
        ResourceLocation id = new ResourceLocation(matchResult.group(1));
        if (!Block.REGISTRY.containsKey(id)) {
            throw new IllegalArgumentException("Invalid block ID");
        }
        this.block = Block.REGISTRY.getObject(id);
        this.meta = this.noMeta ? 0 : Integer.parseInt(matchResult.group(2));
        this.blockstates = BlockOptionalMeta.getStates(this.block, this.getMeta());
        this.stateHashes = BlockOptionalMeta.getStateHashes(this.blockstates);
        this.stackHashes = BlockOptionalMeta.getStackHashes(this.blockstates);
    }

    public static <C extends Comparable<C>, P extends IProperty<C>> P castToIProperty(Object value) {
        return (P)((IProperty)value);
    }

    public static <C extends Comparable<C>, P extends IProperty<C>> C castToIPropertyValue(P iproperty, Object value) {
        return (C)((Comparable)value);
    }

    public static IBlockState normalize(IBlockState state) {
        IBlockState newState = state;
        for (IProperty property : state.getProperties().keySet()) {
            Class valueClass = property.getValueClass();
            if (normalizations.containsKey(property)) {
                try {
                    newState = newState.withProperty(BlockOptionalMeta.castToIProperty(property), BlockOptionalMeta.castToIPropertyValue(property, normalizations.get(property)));
                }
                catch (IllegalArgumentException illegalArgumentException) {}
                continue;
            }
            if (normalizations.containsKey(state.getValue(property))) {
                try {
                    newState = newState.withProperty(BlockOptionalMeta.castToIProperty(property), BlockOptionalMeta.castToIPropertyValue(property, normalizations.get(state.getValue(property))));
                }
                catch (IllegalArgumentException illegalArgumentException) {}
                continue;
            }
            if (!normalizations.containsKey(valueClass)) continue;
            try {
                newState = newState.withProperty(BlockOptionalMeta.castToIProperty(property), BlockOptionalMeta.castToIPropertyValue(property, normalizations.get(valueClass)));
            }
            catch (IllegalArgumentException illegalArgumentException) {}
        }
        return newState;
    }

    public static int stateMeta(IBlockState state) {
        return state.getBlock().getMetaFromState(BlockOptionalMeta.normalize(state));
    }

    private static Set<IBlockState> getStates(@Nonnull Block block, @Nullable Integer meta) {
        return block.getBlockState().getValidStates().stream().filter(blockstate -> meta == null || BlockOptionalMeta.stateMeta(blockstate) == meta).collect(Collectors.toSet());
    }

    private static ImmutableSet<Integer> getStateHashes(Set<IBlockState> blockstates) {
        return ImmutableSet.copyOf(blockstates.stream().map(Object::hashCode).toArray(Integer[]::new));
    }

    private static ImmutableSet<Integer> getStackHashes(Set<IBlockState> blockstates) {
        return ImmutableSet.copyOf(blockstates.stream().map(state -> new ItemStack(state.getBlock().getItemDropped((IBlockState)state, new Random(), 0), state.getBlock().damageDropped((IBlockState)state))).map(stack -> ((IItemStack)stack).getBaritoneHash()).toArray(Integer[]::new));
    }

    public Block getBlock() {
        return this.block;
    }

    public Integer getMeta() {
        return this.noMeta ? null : Integer.valueOf(this.meta);
    }

    public boolean matches(@Nonnull Block block) {
        return block == this.block;
    }

    public boolean matches(@Nonnull IBlockState blockstate) {
        Block block = blockstate.getBlock();
        return block == this.block && this.stateHashes.contains(blockstate.hashCode());
    }

    public boolean matches(ItemStack stack) {
        int hash = ((IItemStack)stack).getBaritoneHash();
        if (this.noMeta) {
            hash -= stack.getItemDamage();
        }
        return this.stackHashes.contains(hash);
    }

    public String toString() {
        return String.format("BlockOptionalMeta{block=%s,meta=%s}", this.block, this.getMeta());
    }

    public static IBlockState blockStateFromStack(ItemStack stack) {
        return Block.getBlockFromItem(stack.getItem()).getStateFromMeta(stack.getMetadata());
    }

    public IBlockState getAnyBlockState() {
        if (this.blockstates.size() > 0) {
            return this.blockstates.iterator().next();
        }
        return null;
    }

    static {
        HashMap<Object, Object> _normalizations = new HashMap<Object, Object>();
        Consumer<Enum> put = instance -> _normalizations.put(instance.getClass(), instance);
        put.accept(EnumFacing.NORTH);
        put.accept(EnumFacing.Axis.Y);
        put.accept(BlockLog.EnumAxis.Y);
        put.accept(BlockStairs.EnumHalf.BOTTOM);
        put.accept(BlockStairs.EnumShape.STRAIGHT);
        put.accept(BlockLever.EnumOrientation.DOWN_X);
        put.accept(BlockDoublePlant.EnumBlockHalf.LOWER);
        put.accept(BlockSlab.EnumBlockHalf.BOTTOM);
        put.accept(BlockDoor.EnumDoorHalf.LOWER);
        put.accept(BlockDoor.EnumHingePosition.LEFT);
        put.accept(BlockBed.EnumPartType.HEAD);
        put.accept(BlockRailBase.EnumRailDirection.NORTH_SOUTH);
        put.accept(BlockTrapDoor.DoorHalf.BOTTOM);
        _normalizations.put(BlockBanner.ROTATION, 0);
        _normalizations.put(BlockBed.OCCUPIED, false);
        _normalizations.put(BlockBrewingStand.HAS_BOTTLE[0], false);
        _normalizations.put(BlockBrewingStand.HAS_BOTTLE[1], false);
        _normalizations.put(BlockBrewingStand.HAS_BOTTLE[2], false);
        _normalizations.put(BlockButton.POWERED, false);
        _normalizations.put(BlockChorusPlant.NORTH, false);
        _normalizations.put(BlockChorusPlant.EAST, false);
        _normalizations.put(BlockChorusPlant.SOUTH, false);
        _normalizations.put(BlockChorusPlant.WEST, false);
        _normalizations.put(BlockChorusPlant.UP, false);
        _normalizations.put(BlockChorusPlant.DOWN, false);
        _normalizations.put(BlockDirt.SNOWY, false);
        _normalizations.put(BlockDoor.OPEN, false);
        _normalizations.put(BlockDoor.POWERED, false);
        _normalizations.put(BlockFence.NORTH, false);
        _normalizations.put(BlockFence.EAST, false);
        _normalizations.put(BlockFence.WEST, false);
        _normalizations.put(BlockFence.SOUTH, false);
        _normalizations.put(BlockFire.AGE, 0);
        _normalizations.put(BlockFire.NORTH, false);
        _normalizations.put(BlockFire.EAST, false);
        _normalizations.put(BlockFire.SOUTH, false);
        _normalizations.put(BlockFire.WEST, false);
        _normalizations.put(BlockFire.UPPER, false);
        _normalizations.put(BlockGrass.SNOWY, false);
        _normalizations.put(BlockLeaves.CHECK_DECAY, false);
        _normalizations.put(BlockPane.NORTH, false);
        _normalizations.put(BlockPane.EAST, false);
        _normalizations.put(BlockPane.WEST, false);
        _normalizations.put(BlockPane.SOUTH, false);
        _normalizations.put(BlockQuartz.EnumType.LINES_X, BlockQuartz.EnumType.LINES_Y);
        _normalizations.put(BlockQuartz.EnumType.LINES_Z, BlockQuartz.EnumType.LINES_Y);
        _normalizations.put(BlockRedstoneWire.NORTH, false);
        _normalizations.put(BlockRedstoneWire.EAST, false);
        _normalizations.put(BlockRedstoneWire.SOUTH, false);
        _normalizations.put(BlockRedstoneWire.WEST, false);
        _normalizations.put(BlockSapling.STAGE, 0);
        _normalizations.put(BlockSkull.NODROP, false);
        _normalizations.put(BlockStandingSign.ROTATION, 0);
        _normalizations.put(BlockStem.AGE, 0);
        _normalizations.put(BlockTripWire.NORTH, false);
        _normalizations.put(BlockTripWire.EAST, false);
        _normalizations.put(BlockTripWire.WEST, false);
        _normalizations.put(BlockTripWire.SOUTH, false);
        _normalizations.put(BlockVine.NORTH, false);
        _normalizations.put(BlockVine.EAST, false);
        _normalizations.put(BlockVine.SOUTH, false);
        _normalizations.put(BlockVine.WEST, false);
        _normalizations.put(BlockVine.UP, false);
        _normalizations.put(BlockWall.UP, false);
        _normalizations.put(BlockWall.NORTH, false);
        _normalizations.put(BlockWall.EAST, false);
        _normalizations.put(BlockWall.WEST, false);
        _normalizations.put(BlockWall.SOUTH, false);
        normalizations = Collections.unmodifiableMap(_normalizations);
    }
}

