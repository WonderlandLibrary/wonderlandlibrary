// 
// Decompiled by Procyon v0.5.36
// 

package net.minecraft.block.state;

import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.Vec3d;
import java.util.List;
import javax.annotation.Nullable;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.block.material.EnumPushReaction;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.world.World;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumBlockRenderType;
import net.minecraft.util.Mirror;
import net.minecraft.util.Rotation;
import net.minecraft.block.material.MapColor;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.entity.Entity;
import net.minecraft.block.material.Material;

public interface IBlockProperties
{
    Material getMaterial();
    
    boolean isFullBlock();
    
    boolean canEntitySpawn(final Entity p0);
    
    int getLightOpacity();
    
    int getLightValue();
    
    boolean isTranslucent();
    
    boolean useNeighborBrightness();
    
    MapColor getMapColor(final IBlockAccess p0, final BlockPos p1);
    
    IBlockState withRotation(final Rotation p0);
    
    IBlockState withMirror(final Mirror p0);
    
    boolean isFullCube();
    
    boolean hasCustomBreakingProgress();
    
    EnumBlockRenderType getRenderType();
    
    int getPackedLightmapCoords(final IBlockAccess p0, final BlockPos p1);
    
    float getAmbientOcclusionLightValue();
    
    boolean isBlockNormalCube();
    
    boolean isNormalCube();
    
    boolean canProvidePower();
    
    int getWeakPower(final IBlockAccess p0, final BlockPos p1, final EnumFacing p2);
    
    boolean hasComparatorInputOverride();
    
    int getComparatorInputOverride(final World p0, final BlockPos p1);
    
    float getBlockHardness(final World p0, final BlockPos p1);
    
    float getPlayerRelativeBlockHardness(final EntityPlayer p0, final World p1, final BlockPos p2);
    
    int getStrongPower(final IBlockAccess p0, final BlockPos p1, final EnumFacing p2);
    
    EnumPushReaction getPushReaction();
    
    IBlockState getActualState(final IBlockAccess p0, final BlockPos p1);
    
    AxisAlignedBB getSelectedBoundingBox(final World p0, final BlockPos p1);
    
    boolean shouldSideBeRendered(final IBlockAccess p0, final BlockPos p1, final EnumFacing p2);
    
    boolean isOpaqueCube();
    
    @Nullable
    AxisAlignedBB getCollisionBoundingBox(final IBlockAccess p0, final BlockPos p1);
    
    void addCollisionBoxToList(final World p0, final BlockPos p1, final AxisAlignedBB p2, final List<AxisAlignedBB> p3, final Entity p4, final boolean p5);
    
    AxisAlignedBB getBoundingBox(final IBlockAccess p0, final BlockPos p1);
    
    RayTraceResult collisionRayTrace(final World p0, final BlockPos p1, final Vec3d p2, final Vec3d p3);
    
    boolean isTopSolid();
    
    Vec3d getOffset(final IBlockAccess p0, final BlockPos p1);
    
    boolean causesSuffocation();
    
    BlockFaceShape getBlockFaceShape(final IBlockAccess p0, final BlockPos p1, final EnumFacing p2);
}
