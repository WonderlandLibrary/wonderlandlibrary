/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  kotlin.jvm.internal.Intrinsics
 *  net.minecraft.block.state.IBlockState
 *  net.minecraft.client.multiplayer.WorldClient
 *  net.minecraft.util.math.BlockPos
 *  net.minecraft.world.World
 *  org.jetbrains.annotations.Nullable
 */
package net.ccbluex.liquidbounce.injection.backend;

import java.util.Collection;
import kotlin.jvm.internal.Intrinsics;
import net.ccbluex.liquidbounce.api.minecraft.client.entity.IEntity;
import net.ccbluex.liquidbounce.api.minecraft.client.entity.player.IEntityPlayer;
import net.ccbluex.liquidbounce.api.minecraft.client.multiplayer.IWorldClient;
import net.ccbluex.liquidbounce.api.minecraft.tileentity.ITileEntity;
import net.ccbluex.liquidbounce.api.minecraft.util.WBlockPos;
import net.ccbluex.liquidbounce.api.util.WrappedCollection;
import net.ccbluex.liquidbounce.injection.backend.EntityImpl;
import net.ccbluex.liquidbounce.injection.backend.WorldClientImpl;
import net.ccbluex.liquidbounce.injection.backend.WorldImpl;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.multiplayer.WorldClient;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

public final class WorldClientImpl
extends WorldImpl<WorldClient>
implements IWorldClient {
    @Override
    public Collection<IEntityPlayer> getPlayerEntities() {
        return new WrappedCollection(((WorldClient)this.getWrapped()).field_73010_i, playerEntities.1.INSTANCE, playerEntities.2.INSTANCE);
    }

    @Override
    public Collection<IEntity> getLoadedEntityList() {
        return new WrappedCollection(((WorldClient)this.getWrapped()).field_72996_f, loadedEntityList.1.INSTANCE, loadedEntityList.2.INSTANCE);
    }

    @Override
    public Collection<ITileEntity> getLoadedTileEntityList() {
        return new WrappedCollection(((WorldClient)this.getWrapped()).field_147482_g, loadedTileEntityList.1.INSTANCE, loadedTileEntityList.2.INSTANCE);
    }

    @Override
    public void sendQuittingDisconnectingPacket() {
        ((WorldClient)this.getWrapped()).func_72882_A();
    }

    /*
     * WARNING - void declaration
     */
    @Override
    public void sendBlockBreakProgress(int entityId, WBlockPos blockPos, int damage) {
        void $this$unwrap$iv;
        WBlockPos wBlockPos = blockPos;
        int n = entityId;
        WorldClient worldClient = (WorldClient)this.getWrapped();
        boolean $i$f$unwrap = false;
        BlockPos blockPos2 = new BlockPos($this$unwrap$iv.getX(), $this$unwrap$iv.getY(), $this$unwrap$iv.getZ());
        worldClient.func_175715_c(n, blockPos2, damage);
    }

    /*
     * WARNING - void declaration
     */
    @Override
    public void addEntityToWorld(int entityId, IEntity fakePlayer) {
        void $this$unwrap$iv;
        IEntity iEntity = fakePlayer;
        int n = entityId;
        WorldClient worldClient = (WorldClient)this.getWrapped();
        boolean $i$f$unwrap = false;
        Object t = ((EntityImpl)$this$unwrap$iv).getWrapped();
        worldClient.func_73027_a(n, t);
    }

    @Override
    public void removeEntityFromWorld(int entityId) {
        ((WorldClient)this.getWrapped()).func_73028_b(entityId);
    }

    @Override
    public boolean setBlockState(@Nullable WBlockPos blockPos, @Nullable IBlockState blockstate, int size) {
        WorldClient worldClient = (WorldClient)this.getWrapped();
        WBlockPos wBlockPos = blockPos;
        if (wBlockPos == null) {
            Intrinsics.throwNpe();
        }
        return worldClient.func_180501_a(new BlockPos(wBlockPos.getX(), blockPos.getY(), blockPos.getZ()), blockstate, size);
    }

    public WorldClientImpl(WorldClient wrapped) {
        super((World)wrapped);
    }
}

