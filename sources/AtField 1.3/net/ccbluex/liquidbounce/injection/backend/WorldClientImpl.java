/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  kotlin.jvm.internal.Intrinsics
 *  net.minecraft.block.state.IBlockState
 *  net.minecraft.client.multiplayer.WorldClient
 *  net.minecraft.entity.Entity
 *  net.minecraft.util.math.BlockPos
 *  net.minecraft.world.World
 *  org.jetbrains.annotations.Nullable
 */
package net.ccbluex.liquidbounce.injection.backend;

import java.util.Collection;
import kotlin.jvm.internal.Intrinsics;
import net.ccbluex.liquidbounce.api.minecraft.client.entity.IEntity;
import net.ccbluex.liquidbounce.api.minecraft.client.multiplayer.IWorldClient;
import net.ccbluex.liquidbounce.api.minecraft.util.WBlockPos;
import net.ccbluex.liquidbounce.api.util.WrappedCollection;
import net.ccbluex.liquidbounce.injection.backend.EntityImpl;
import net.ccbluex.liquidbounce.injection.backend.WorldClientImpl;
import net.ccbluex.liquidbounce.injection.backend.WorldImpl;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.multiplayer.WorldClient;
import net.minecraft.entity.Entity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

public final class WorldClientImpl
extends WorldImpl
implements IWorldClient {
    public WorldClientImpl(WorldClient worldClient) {
        super((World)worldClient);
    }

    @Override
    public void sendBlockBreakProgress(int n, WBlockPos wBlockPos, int n2) {
        WBlockPos wBlockPos2 = wBlockPos;
        int n3 = n;
        WorldClient worldClient = (WorldClient)this.getWrapped();
        boolean bl = false;
        BlockPos blockPos = new BlockPos(wBlockPos2.getX(), wBlockPos2.getY(), wBlockPos2.getZ());
        worldClient.func_175715_c(n3, blockPos, n2);
    }

    @Override
    public void sendQuittingDisconnectingPacket() {
        ((WorldClient)this.getWrapped()).func_72882_A();
    }

    @Override
    public Collection getLoadedTileEntityList() {
        return new WrappedCollection(((WorldClient)this.getWrapped()).field_147482_g, loadedTileEntityList.1.INSTANCE, loadedTileEntityList.2.INSTANCE);
    }

    @Override
    public boolean setBlockState(@Nullable WBlockPos wBlockPos, @Nullable IBlockState iBlockState, int n) {
        WorldClient worldClient = (WorldClient)this.getWrapped();
        WBlockPos wBlockPos2 = wBlockPos;
        if (wBlockPos2 == null) {
            Intrinsics.throwNpe();
        }
        return worldClient.func_180501_a(new BlockPos(wBlockPos2.getX(), wBlockPos.getY(), wBlockPos.getZ()), iBlockState, n);
    }

    @Override
    public Collection getLoadedEntityList() {
        return new WrappedCollection(((WorldClient)this.getWrapped()).field_72996_f, loadedEntityList.1.INSTANCE, loadedEntityList.2.INSTANCE);
    }

    @Override
    public void addEntityToWorld(int n, IEntity iEntity) {
        IEntity iEntity2 = iEntity;
        int n2 = n;
        WorldClient worldClient = (WorldClient)this.getWrapped();
        boolean bl = false;
        Entity entity = ((EntityImpl)iEntity2).getWrapped();
        worldClient.func_73027_a(n2, entity);
    }

    @Override
    public Collection getPlayerEntities() {
        return new WrappedCollection(((WorldClient)this.getWrapped()).field_73010_i, playerEntities.1.INSTANCE, playerEntities.2.INSTANCE);
    }

    @Override
    public void removeEntityFromWorld(int n) {
        ((WorldClient)this.getWrapped()).func_73028_b(n);
    }
}

