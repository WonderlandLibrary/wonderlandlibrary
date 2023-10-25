/*
 * Decompiled with CFR 0.150.
 */
package net.minecraft.block.state;

import baritone.utils.accessor.IBlockStateContainer;
import com.google.common.base.Function;
import com.google.common.base.MoreObjects;
import com.google.common.collect.HashBasedTable;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.ImmutableSortedMap;
import com.google.common.collect.ImmutableTable;
import com.google.common.collect.Iterables;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.regex.Pattern;
import javax.annotation.Nullable;
import net.minecraft.block.Block;
import net.minecraft.block.BlockFlower;
import net.minecraft.block.material.EnumPushReaction;
import net.minecraft.block.material.MapColor;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.state.BlockFaceShape;
import net.minecraft.block.state.BlockStateBase;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.BitArray;
import net.minecraft.util.EnumBlockRenderType;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.MapPopulator;
import net.minecraft.util.Mirror;
import net.minecraft.util.Rotation;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Cartesian;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraft.world.chunk.IBlockStatePalette;
import net.minecraftforge.common.property.IUnlistedProperty;
import optifine.BlockModelUtils;
import optifine.Reflector;

public class BlockStateContainer
implements IBlockStateContainer {
    private static final Pattern NAME_PATTERN = Pattern.compile("^[a-z0-9_]+$");
    private static final Function<IProperty<?>, String> GET_NAME_FUNC = new Function<IProperty<?>, String>(){

        @Override
        @Nullable
        public String apply(@Nullable IProperty<?> p_apply_1_) {
            return p_apply_1_ == null ? "<NULL>" : p_apply_1_.getName();
        }
    };
    private final Block block;
    private final ImmutableSortedMap<String, IProperty<?>> properties;
    private final ImmutableList<IBlockState> validStates;
    protected BitArray storage;
    protected IBlockStatePalette palette;

    public BlockStateContainer(Block blockIn, IProperty<?> ... properties) {
        this(blockIn, properties, (ImmutableMap)null);
    }

    protected StateImplementation createState(Block p_createState_1_, ImmutableMap<IProperty<?>, Comparable<?>> p_createState_2_, @Nullable ImmutableMap<IUnlistedProperty<?>, Optional<?>> p_createState_3_) {
        return new StateImplementation(p_createState_1_, (ImmutableMap)p_createState_2_);
    }

    protected BlockStateContainer(Block p_i9_1_, IProperty<?>[] p_i9_2_, ImmutableMap<IUnlistedProperty<?>, Optional<?>> p_i9_3_) {
        this.block = p_i9_1_;
        HashMap<String, IProperty<?>> map = Maps.newHashMap();
        for (IProperty<?> iproperty : p_i9_2_) {
            BlockStateContainer.validateProperty(p_i9_1_, iproperty);
            map.put(iproperty.getName(), iproperty);
        }
        this.properties = ImmutableSortedMap.copyOf(map);
        LinkedHashMap<Map<IProperty<?>, Comparable<?>>, StateImplementation> map2 = Maps.newLinkedHashMap();
        ArrayList<StateImplementation> list = Lists.newArrayList();
        for (List<Comparable<?>> list1 : Cartesian.cartesianProduct(this.getAllowedValues())) {
            Map map1 = MapPopulator.createMap(this.properties.values(), list1);
            StateImplementation blockstatecontainer$stateimplementation = this.createState(p_i9_1_, ImmutableMap.copyOf(map1), p_i9_3_);
            map2.put(map1, blockstatecontainer$stateimplementation);
            list.add(blockstatecontainer$stateimplementation);
        }
        for (StateImplementation blockstatecontainer$stateimplementation1 : list) {
            blockstatecontainer$stateimplementation1.buildPropertyValueTable(map2);
        }
        this.validStates = ImmutableList.copyOf(list);
    }

    public static <T extends Comparable<T>> String validateProperty(Block block, IProperty<T> property) {
        String s = property.getName();
        if (!NAME_PATTERN.matcher(s).matches()) {
            throw new IllegalArgumentException("Block: " + block.getClass() + " has invalidly named property: " + s);
        }
        for (Comparable t : property.getAllowedValues()) {
            String s1 = property.getName(t);
            if (NAME_PATTERN.matcher(s1).matches()) continue;
            throw new IllegalArgumentException("Block: " + block.getClass() + " has property: " + s + " with invalidly named value: " + s1);
        }
        return s;
    }

    public ImmutableList<IBlockState> getValidStates() {
        return this.validStates;
    }

    private List<Iterable<Comparable<?>>> getAllowedValues() {
        ArrayList<Iterable<Comparable<?>>> list = Lists.newArrayList();
        Collection immutablecollection = this.properties.values();
        for (IProperty iproperty : immutablecollection) {
            list.add(iproperty.getAllowedValues());
        }
        return list;
    }

    public IBlockState getBaseState() {
        return (IBlockState)this.validStates.get(0);
    }

    public Block getBlock() {
        return this.block;
    }

    public Collection<IProperty<?>> getProperties() {
        return this.properties.values();
    }

    public String toString() {
        return MoreObjects.toStringHelper(this).add("block", Block.REGISTRY.getNameForObject(this.block)).add("properties", Iterables.transform(this.properties.values(), GET_NAME_FUNC)).toString();
    }

    @Nullable
    public IProperty<?> getProperty(String propertyName) {
        return this.properties.get(propertyName);
    }

    @Override
    public IBlockState getAtPalette(int index) {
        return this.palette.getBlockState(index);
    }

    @Override
    public int[] storageArray() {
        return this.storage.toArray();
    }

    static class StateImplementation
    extends BlockStateBase {
        private final Block block;
        private final ImmutableMap<IProperty<?>, Comparable<?>> properties;
        private ImmutableTable<IProperty<?>, Comparable<?>, IBlockState> propertyValueTable;

        private StateImplementation(Block blockIn, ImmutableMap<IProperty<?>, Comparable<?>> propertiesIn) {
            this.block = blockIn;
            this.properties = propertiesIn;
        }

        protected StateImplementation(Block p_i8_1_, ImmutableMap<IProperty<?>, Comparable<?>> p_i8_2_, ImmutableTable<IProperty<?>, Comparable<?>, IBlockState> p_i8_3_) {
            this.block = p_i8_1_;
            this.properties = p_i8_2_;
            this.propertyValueTable = p_i8_3_;
        }

        @Override
        public Collection<IProperty<?>> getPropertyKeys() {
            return Collections.unmodifiableCollection(this.properties.keySet());
        }

        @Override
        public <T extends Comparable<T>> T getValue(IProperty<T> property) {
            Comparable<?> comparable = this.properties.get(property);
            if (comparable == null) {
                throw new IllegalArgumentException("Cannot get property " + property + " as it does not exist in " + this.block.getBlockState());
            }
            return (T)((Comparable)property.getValueClass().cast(comparable));
        }

        @Override
        public <T extends Comparable<T>, V extends T> IBlockState withProperty(IProperty<T> property, V value) {
            Comparable<?> comparable = this.properties.get(property);
            if (comparable == null) {
                throw new IllegalArgumentException("Cannot set property " + property + " as it does not exist in " + this.block.getBlockState());
            }
            if (comparable == value) {
                return this;
            }
            IBlockState iblockstate = (IBlockState)this.propertyValueTable.get(property, value);
            if (iblockstate == null) {
                throw new IllegalArgumentException("Cannot set property " + property + " to " + value + " on block " + Block.REGISTRY.getNameForObject(this.block) + ", it is not an allowed value");
            }
            return iblockstate;
        }

        @Override
        public ImmutableMap<IProperty<?>, Comparable<?>> getProperties() {
            return this.properties;
        }

        @Override
        public Block getBlock() {
            return this.block;
        }

        public boolean equals(Object p_equals_1_) {
            return this == p_equals_1_;
        }

        public int hashCode() {
            return this.properties.hashCode();
        }

        public void buildPropertyValueTable(Map<Map<IProperty<?>, Comparable<?>>, StateImplementation> map) {
            if (this.propertyValueTable != null) {
                throw new IllegalStateException();
            }
            HashBasedTable<IProperty, Comparable, StateImplementation> table = HashBasedTable.create();
            for (Map.Entry entry : this.properties.entrySet()) {
                IProperty iproperty = (IProperty)entry.getKey();
                for (Comparable comparable : iproperty.getAllowedValues()) {
                    if (comparable == entry.getValue()) continue;
                    table.put(iproperty, comparable, map.get(this.getPropertiesWithValue(iproperty, comparable)));
                }
            }
            this.propertyValueTable = ImmutableTable.copyOf(table);
        }

        private Map<IProperty<?>, Comparable<?>> getPropertiesWithValue(IProperty<?> property, Comparable<?> value) {
            HashMap<IProperty<?>, Comparable<?>> map = Maps.newHashMap(this.properties);
            map.put(property, value);
            return map;
        }

        @Override
        public Material getMaterial() {
            return this.block.getMaterial(this);
        }

        @Override
        public boolean isFullBlock() {
            return this.block.isFullBlock(this);
        }

        @Override
        public boolean canEntitySpawn(Entity entityIn) {
            return this.block.canEntitySpawn(this, entityIn);
        }

        @Override
        public int getLightOpacity() {
            return this.block.getLightOpacity(this);
        }

        @Override
        public int getLightValue() {
            return this.block.getLightValue(this);
        }

        @Override
        public boolean isTranslucent() {
            return this.block.isTranslucent(this);
        }

        @Override
        public boolean useNeighborBrightness() {
            return this.block.getUseNeighborBrightness(this);
        }

        @Override
        public MapColor getMapColor(IBlockAccess p_185909_1_, BlockPos p_185909_2_) {
            return this.block.getMapColor(this, p_185909_1_, p_185909_2_);
        }

        @Override
        public IBlockState withRotation(Rotation rot) {
            return this.block.withRotation(this, rot);
        }

        @Override
        public IBlockState withMirror(Mirror mirrorIn) {
            return this.block.withMirror(this, mirrorIn);
        }

        @Override
        public boolean isFullCube() {
            return this.block.isFullCube(this);
        }

        @Override
        public boolean func_191057_i() {
            return this.block.func_190946_v(this);
        }

        @Override
        public EnumBlockRenderType getRenderType() {
            return this.block.getRenderType(this);
        }

        @Override
        public int getPackedLightmapCoords(IBlockAccess source, BlockPos pos) {
            return this.block.getPackedLightmapCoords(this, source, pos);
        }

        @Override
        public float getAmbientOcclusionLightValue() {
            return this.block.getAmbientOcclusionLightValue(this);
        }

        @Override
        public boolean isBlockNormalCube() {
            return this.block.isBlockNormalCube(this);
        }

        @Override
        public boolean isNormalCube() {
            return this.block.isNormalCube(this);
        }

        @Override
        public boolean canProvidePower() {
            return this.block.canProvidePower(this);
        }

        @Override
        public int getWeakPower(IBlockAccess blockAccess, BlockPos pos, EnumFacing side) {
            return this.block.getWeakPower(this, blockAccess, pos, side);
        }

        @Override
        public boolean hasComparatorInputOverride() {
            return this.block.hasComparatorInputOverride(this);
        }

        @Override
        public int getComparatorInputOverride(World worldIn, BlockPos pos) {
            return this.block.getComparatorInputOverride(this, worldIn, pos);
        }

        @Override
        public float getBlockHardness(World worldIn, BlockPos pos) {
            return this.block.getBlockHardness(this, worldIn, pos);
        }

        @Override
        public float getPlayerRelativeBlockHardness(EntityPlayer player, World worldIn, BlockPos pos) {
            return this.block.getPlayerRelativeBlockHardness(this, player, worldIn, pos);
        }

        @Override
        public int getStrongPower(IBlockAccess blockAccess, BlockPos pos, EnumFacing side) {
            return this.block.getStrongPower(this, blockAccess, pos, side);
        }

        @Override
        public EnumPushReaction getMobilityFlag() {
            return this.block.getMobilityFlag(this);
        }

        @Override
        public IBlockState getActualState(IBlockAccess blockAccess, BlockPos pos) {
            return this.block.getActualState(this, blockAccess, pos);
        }

        @Override
        public AxisAlignedBB getSelectedBoundingBox(World worldIn, BlockPos pos) {
            return this.block.getSelectedBoundingBox(this, worldIn, pos);
        }

        @Override
        public boolean shouldSideBeRendered(IBlockAccess blockAccess, BlockPos pos, EnumFacing facing) {
            return this.block.shouldSideBeRendered(this, blockAccess, pos, facing);
        }

        @Override
        public boolean isOpaqueCube() {
            return this.block.isOpaqueCube(this);
        }

        @Override
        @Nullable
        public AxisAlignedBB getCollisionBoundingBox(IBlockAccess worldIn, BlockPos pos) {
            return this.block.getCollisionBoundingBox(this, worldIn, pos);
        }

        @Override
        public void addCollisionBoxToList(World worldIn, BlockPos pos, AxisAlignedBB p_185908_3_, List<AxisAlignedBB> p_185908_4_, @Nullable Entity p_185908_5_, boolean p_185908_6_) {
            this.block.addCollisionBoxToList(this, worldIn, pos, p_185908_3_, p_185908_4_, p_185908_5_, p_185908_6_);
        }

        @Override
        public AxisAlignedBB getBoundingBox(IBlockAccess blockAccess, BlockPos pos) {
            Block.EnumOffsetType block$enumoffsettype = this.block.getOffsetType();
            if (block$enumoffsettype != Block.EnumOffsetType.NONE && !(this.block instanceof BlockFlower)) {
                AxisAlignedBB axisalignedbb = this.block.getBoundingBox(this, blockAccess, pos);
                axisalignedbb = BlockModelUtils.getOffsetBoundingBox(axisalignedbb, block$enumoffsettype, pos);
                return axisalignedbb;
            }
            return this.block.getBoundingBox(this, blockAccess, pos);
        }

        @Override
        public RayTraceResult collisionRayTrace(World worldIn, BlockPos pos, Vec3d start, Vec3d end) {
            return this.block.collisionRayTrace(this, worldIn, pos, start, end);
        }

        @Override
        public boolean isFullyOpaque() {
            return this.block.isFullyOpaque(this);
        }

        @Override
        public Vec3d func_191059_e(IBlockAccess p_191059_1_, BlockPos p_191059_2_) {
            return this.block.func_190949_e(this, p_191059_1_, p_191059_2_);
        }

        @Override
        public boolean onBlockEventReceived(World worldIn, BlockPos pos, int id, int param) {
            return this.block.eventReceived(this, worldIn, pos, id, param);
        }

        @Override
        public void neighborChanged(World worldIn, BlockPos pos, Block blockIn, BlockPos p_189546_4_) {
            this.block.neighborChanged(this, worldIn, pos, blockIn, p_189546_4_);
        }

        @Override
        public boolean func_191058_s() {
            return this.block.causesSuffocation(this);
        }

        @Override
        public ImmutableTable<IProperty<?>, Comparable<?>, IBlockState> getPropertyValueTable() {
            return this.propertyValueTable;
        }

        public int getLightOpacity(IBlockAccess p_getLightOpacity_1_, BlockPos p_getLightOpacity_2_) {
            return Reflector.callInt(this.block, Reflector.ForgeBlock_getLightOpacity, this, p_getLightOpacity_1_, p_getLightOpacity_2_);
        }

        public int getLightValue(IBlockAccess p_getLightValue_1_, BlockPos p_getLightValue_2_) {
            return Reflector.callInt(this.block, Reflector.ForgeBlock_getLightValue, this, p_getLightValue_1_, p_getLightValue_2_);
        }

        public boolean isSideSolid(IBlockAccess p_isSideSolid_1_, BlockPos p_isSideSolid_2_, EnumFacing p_isSideSolid_3_) {
            return Reflector.callBoolean(this.block, Reflector.ForgeBlock_isSideSolid, this, p_isSideSolid_1_, p_isSideSolid_2_, p_isSideSolid_3_);
        }

        public boolean doesSideBlockRendering(IBlockAccess p_doesSideBlockRendering_1_, BlockPos p_doesSideBlockRendering_2_, EnumFacing p_doesSideBlockRendering_3_) {
            return Reflector.callBoolean(this.block, Reflector.ForgeBlock_doesSideBlockRendering, this, p_doesSideBlockRendering_1_, p_doesSideBlockRendering_2_, p_doesSideBlockRendering_3_);
        }

        @Override
        public BlockFaceShape func_193401_d(IBlockAccess p_193401_1_, BlockPos p_193401_2_, EnumFacing p_193401_3_) {
            return this.block.func_193383_a(p_193401_1_, this, p_193401_2_, p_193401_3_);
        }
    }

    public static class Builder {
        private final Block block;
        private final List<IProperty<?>> listed = Lists.newArrayList();
        private final List<IUnlistedProperty<?>> unlisted = Lists.newArrayList();

        public Builder(Block p_i11_1_) {
            this.block = p_i11_1_;
        }

        public Builder add(IProperty<?> ... p_add_1_) {
            for (IProperty<?> iproperty : p_add_1_) {
                this.listed.add(iproperty);
            }
            return this;
        }

        public Builder add(IUnlistedProperty<?> ... p_add_1_) {
            for (IUnlistedProperty<?> iunlistedproperty : p_add_1_) {
                this.unlisted.add(iunlistedproperty);
            }
            return this;
        }

        public BlockStateContainer build() {
            IProperty[] iproperty = new IProperty[this.listed.size()];
            iproperty = this.listed.toArray(iproperty);
            if (this.unlisted.size() == 0) {
                return new BlockStateContainer(this.block, iproperty);
            }
            IUnlistedProperty[] iunlistedproperty = new IUnlistedProperty[this.unlisted.size()];
            iunlistedproperty = this.unlisted.toArray(iunlistedproperty);
            return (BlockStateContainer)Reflector.newInstance(Reflector.ExtendedBlockState_Constructor, this.block, iproperty, iunlistedproperty);
        }
    }
}

