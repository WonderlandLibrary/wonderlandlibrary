/*
 * Decompiled with CFR 0.150.
 */
package net.minecraft.block.state;

import java.util.List;
import javax.annotation.Nullable;
import net.minecraft.block.material.EnumPushReaction;
import net.minecraft.block.material.MapColor;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.BlockFaceShape;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.EnumBlockRenderType;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.Mirror;
import net.minecraft.util.Rotation;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

public interface IBlockProperties {
    public Material getMaterial();

    public boolean isFullBlock();

    public boolean canEntitySpawn(Entity var1);

    public int getLightOpacity();

    public int getLightValue();

    public boolean isTranslucent();

    public boolean useNeighborBrightness();

    public MapColor getMapColor(IBlockAccess var1, BlockPos var2);

    public IBlockState withRotation(Rotation var1);

    public IBlockState withMirror(Mirror var1);

    public boolean isFullCube();

    public boolean func_191057_i();

    public EnumBlockRenderType getRenderType();

    public int getPackedLightmapCoords(IBlockAccess var1, BlockPos var2);

    public float getAmbientOcclusionLightValue();

    public boolean isBlockNormalCube();

    public boolean isNormalCube();

    public boolean canProvidePower();

    public int getWeakPower(IBlockAccess var1, BlockPos var2, EnumFacing var3);

    public boolean hasComparatorInputOverride();

    public int getComparatorInputOverride(World var1, BlockPos var2);

    public float getBlockHardness(World var1, BlockPos var2);

    public float getPlayerRelativeBlockHardness(EntityPlayer var1, World var2, BlockPos var3);

    public int getStrongPower(IBlockAccess var1, BlockPos var2, EnumFacing var3);

    public EnumPushReaction getMobilityFlag();

    public IBlockState getActualState(IBlockAccess var1, BlockPos var2);

    public AxisAlignedBB getSelectedBoundingBox(World var1, BlockPos var2);

    public boolean shouldSideBeRendered(IBlockAccess var1, BlockPos var2, EnumFacing var3);

    public boolean isOpaqueCube();

    @Nullable
    public AxisAlignedBB getCollisionBoundingBox(IBlockAccess var1, BlockPos var2);

    public void addCollisionBoxToList(World var1, BlockPos var2, AxisAlignedBB var3, List<AxisAlignedBB> var4, Entity var5, boolean var6);

    public AxisAlignedBB getBoundingBox(IBlockAccess var1, BlockPos var2);

    public RayTraceResult collisionRayTrace(World var1, BlockPos var2, Vec3d var3, Vec3d var4);

    public boolean isFullyOpaque();

    public Vec3d func_191059_e(IBlockAccess var1, BlockPos var2);

    public boolean func_191058_s();

    public BlockFaceShape func_193401_d(IBlockAccess var1, BlockPos var2, EnumFacing var3);
}

