package net.ccbluex.liquidbounce.api.minecraft.network.play.server;

import kotlin.Metadata;
import net.ccbluex.liquidbounce.api.minecraft.client.entity.IEntity;
import net.ccbluex.liquidbounce.api.minecraft.network.IPacket;
import net.ccbluex.liquidbounce.api.minecraft.world.IWorld;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

@Metadata(mv={1, 1, 16}, bv={1, 0, 3}, k=1, d1={"\u0000\n\n\n\u0000\n\n\b\n\n\u0000\n\n\u0000\bf\u000020J02\b0\tH&R0X¦¢\b¨\n"}, d2={"Lnet/ccbluex/liquidbounce/api/minecraft/network/play/server/ISPacketEntity;", "Lnet/ccbluex/liquidbounce/api/minecraft/network/IPacket;", "onGround", "", "getOnGround", "()Z", "getEntity", "Lnet/ccbluex/liquidbounce/api/minecraft/client/entity/IEntity;", "world", "Lnet/ccbluex/liquidbounce/api/minecraft/world/IWorld;", "Pride"})
public interface ISPacketEntity
extends IPacket {
    public boolean getOnGround();

    @Nullable
    public IEntity getEntity(@NotNull IWorld var1);
}
