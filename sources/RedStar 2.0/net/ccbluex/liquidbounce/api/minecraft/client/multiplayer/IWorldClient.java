package net.ccbluex.liquidbounce.api.minecraft.client.multiplayer;

import java.util.Collection;
import kotlin.Metadata;
import net.ccbluex.liquidbounce.api.minecraft.client.entity.IEntity;
import net.ccbluex.liquidbounce.api.minecraft.client.entity.player.IEntityPlayer;
import net.ccbluex.liquidbounce.api.minecraft.tileentity.ITileEntity;
import net.ccbluex.liquidbounce.api.minecraft.util.WBlockPos;
import net.ccbluex.liquidbounce.api.minecraft.world.IWorld;
import net.minecraft.block.state.IBlockState;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

@Metadata(mv={1, 1, 16}, bv={1, 0, 3}, k=1, d1={"\u0000J\n\n\n\u0000\n\n\n\b\n\n\b\n\n\b\n\n\u0000\n\b\n\b\n\n\b\n\n\u0000\n\n\b\bf\u000020J\r02020H&J020H&J020H&J 0202020H&J\b0H&J$02\b02\b020H&R\b00X¦¢\bR\b0\b0X¦¢\b\tR\n\b00X¦¢\b\f¨"}, d2={"Lnet/ccbluex/liquidbounce/api/minecraft/client/multiplayer/IWorldClient;", "Lnet/ccbluex/liquidbounce/api/minecraft/world/IWorld;", "loadedEntityList", "", "Lnet/ccbluex/liquidbounce/api/minecraft/client/entity/IEntity;", "getLoadedEntityList", "()Ljava/util/Collection;", "loadedTileEntityList", "Lnet/ccbluex/liquidbounce/api/minecraft/tileentity/ITileEntity;", "getLoadedTileEntityList", "playerEntities", "Lnet/ccbluex/liquidbounce/api/minecraft/client/entity/player/IEntityPlayer;", "getPlayerEntities", "addEntityToWorld", "", "entityId", "", "fakePlayer", "removeEntity", "name", "removeEntityFromWorld", "sendBlockBreakProgress", "blockPos", "Lnet/ccbluex/liquidbounce/api/minecraft/util/WBlockPos;", "damage", "sendQuittingDisconnectingPacket", "setBlockState", "", "blockstate", "Lnet/minecraft/block/state/IBlockState;", "size", "Pride"})
public interface IWorldClient
extends IWorld {
    @NotNull
    public Collection<IEntityPlayer> getPlayerEntities();

    @NotNull
    public Collection<IEntity> getLoadedEntityList();

    @NotNull
    public Collection<ITileEntity> getLoadedTileEntityList();

    public void sendQuittingDisconnectingPacket();

    public void sendBlockBreakProgress(int var1, @NotNull WBlockPos var2, int var3);

    public void addEntityToWorld(int var1, @NotNull IEntity var2);

    public void removeEntityFromWorld(int var1);

    public void removeEntity(@NotNull IEntity var1);

    public boolean setBlockState(@Nullable WBlockPos var1, @Nullable IBlockState var2, int var3);
}
