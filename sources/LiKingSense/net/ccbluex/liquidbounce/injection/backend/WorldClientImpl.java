/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  kotlin.Metadata
 *  kotlin.jvm.internal.Intrinsics
 *  net.minecraft.block.state.IBlockState
 *  net.minecraft.client.multiplayer.WorldClient
 *  net.minecraft.util.math.BlockPos
 *  net.minecraft.world.World
 *  org.jetbrains.annotations.NotNull
 *  org.jetbrains.annotations.Nullable
 */
package net.ccbluex.liquidbounce.injection.backend;

import java.util.Collection;
import kotlin.Metadata;
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
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

@Metadata(mv={1, 1, 16}, bv={1, 0, 3}, k=1, d1={"\u0000T\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0010\u001e\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\u0002\n\u0000\n\u0002\u0010\b\n\u0002\b\u0004\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0010\u000b\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\u0018\u00002\b\u0012\u0004\u0012\u00020\u00020\u00012\u00020\u0003B\r\u0012\u0006\u0010\u0004\u001a\u00020\u0002\u00a2\u0006\u0002\u0010\u0005J\u0018\u0010\u0011\u001a\u00020\u00122\u0006\u0010\u0013\u001a\u00020\u00142\u0006\u0010\u0015\u001a\u00020\bH\u0016J\u0010\u0010\u0016\u001a\u00020\u00122\u0006\u0010\u0013\u001a\u00020\u0014H\u0016J \u0010\u0017\u001a\u00020\u00122\u0006\u0010\u0013\u001a\u00020\u00142\u0006\u0010\u0018\u001a\u00020\u00192\u0006\u0010\u001a\u001a\u00020\u0014H\u0016J\b\u0010\u001b\u001a\u00020\u0012H\u0016J$\u0010\u001c\u001a\u00020\u001d2\b\u0010\u0018\u001a\u0004\u0018\u00010\u00192\b\u0010\u001e\u001a\u0004\u0018\u00010\u001f2\u0006\u0010 \u001a\u00020\u0014H\u0016R\u001a\u0010\u0006\u001a\b\u0012\u0004\u0012\u00020\b0\u00078VX\u0096\u0004\u00a2\u0006\u0006\u001a\u0004\b\t\u0010\nR\u001a\u0010\u000b\u001a\b\u0012\u0004\u0012\u00020\f0\u00078VX\u0096\u0004\u00a2\u0006\u0006\u001a\u0004\b\r\u0010\nR\u001a\u0010\u000e\u001a\b\u0012\u0004\u0012\u00020\u000f0\u00078VX\u0096\u0004\u00a2\u0006\u0006\u001a\u0004\b\u0010\u0010\n\u00a8\u0006!"}, d2={"Lnet/ccbluex/liquidbounce/injection/backend/WorldClientImpl;", "Lnet/ccbluex/liquidbounce/injection/backend/WorldImpl;", "Lnet/minecraft/client/multiplayer/WorldClient;", "Lnet/ccbluex/liquidbounce/api/minecraft/client/multiplayer/IWorldClient;", "wrapped", "(Lnet/minecraft/client/multiplayer/WorldClient;)V", "loadedEntityList", "", "Lnet/ccbluex/liquidbounce/api/minecraft/client/entity/IEntity;", "getLoadedEntityList", "()Ljava/util/Collection;", "loadedTileEntityList", "Lnet/ccbluex/liquidbounce/api/minecraft/tileentity/ITileEntity;", "getLoadedTileEntityList", "playerEntities", "Lnet/ccbluex/liquidbounce/api/minecraft/client/entity/player/IEntityPlayer;", "getPlayerEntities", "addEntityToWorld", "", "entityId", "", "fakePlayer", "removeEntityFromWorld", "sendBlockBreakProgress", "blockPos", "Lnet/ccbluex/liquidbounce/api/minecraft/util/WBlockPos;", "damage", "sendQuittingDisconnectingPacket", "setBlockState", "", "blockstate", "Lnet/minecraft/block/state/IBlockState;", "size", "LiKingSense"})
public final class WorldClientImpl
extends WorldImpl<WorldClient>
implements IWorldClient {
    @Override
    @NotNull
    public Collection<IEntityPlayer> getPlayerEntities() {
        return new WrappedCollection(((WorldClient)this.getWrapped()).field_73010_i, playerEntities.1.INSTANCE, playerEntities.2.INSTANCE);
    }

    @Override
    @NotNull
    public Collection<IEntity> getLoadedEntityList() {
        return new WrappedCollection(((WorldClient)this.getWrapped()).field_72996_f, loadedEntityList.1.INSTANCE, loadedEntityList.2.INSTANCE);
    }

    @Override
    @NotNull
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
    public void sendBlockBreakProgress(int entityId, @NotNull WBlockPos blockPos, int damage) {
        void $this$unwrap$iv;
        Intrinsics.checkParameterIsNotNull((Object)blockPos, (String)"blockPos");
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
    public void addEntityToWorld(int entityId, @NotNull IEntity fakePlayer) {
        void $this$unwrap$iv;
        Intrinsics.checkParameterIsNotNull((Object)fakePlayer, (String)"fakePlayer");
        IEntity iEntity = fakePlayer;
        int n = entityId;
        WorldClient worldClient = (WorldClient)this.getWrapped();
        boolean $i$f$unwrap = false;
        Object t2 = ((EntityImpl)$this$unwrap$iv).getWrapped();
        worldClient.func_73027_a(n, t2);
    }

    @Override
    public void removeEntityFromWorld(int entityId) {
        ((WorldClient)this.getWrapped()).func_73028_b(entityId);
    }

    @Override
    public boolean setBlockState(@Nullable WBlockPos blockPos, @Nullable IBlockState blockstate, int size) {
        return ((WorldClient)this.getWrapped()).func_180501_a(new BlockPos(blockPos.getX(), blockPos.getY(), blockPos.getZ()), blockstate, size);
    }

    public WorldClientImpl(@NotNull WorldClient wrapped) {
        Intrinsics.checkParameterIsNotNull((Object)wrapped, (String)"wrapped");
        super((World)wrapped);
    }
}

