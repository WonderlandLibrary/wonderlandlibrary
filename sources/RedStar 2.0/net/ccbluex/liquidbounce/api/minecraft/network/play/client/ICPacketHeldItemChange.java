package net.ccbluex.liquidbounce.api.minecraft.network.play.client;

import kotlin.Metadata;
import net.ccbluex.liquidbounce.api.minecraft.network.IPacket;

@Metadata(mv={1, 1, 16}, bv={1, 0, 3}, k=1, d1={"\u0000\n\n\n\u0000\n\b\n\b\bf\u000020R0X¦¢\b¨"}, d2={"Lnet/ccbluex/liquidbounce/api/minecraft/network/play/client/ICPacketHeldItemChange;", "Lnet/ccbluex/liquidbounce/api/minecraft/network/IPacket;", "slotId", "", "getSlotId", "()I", "Pride"})
public interface ICPacketHeldItemChange
extends IPacket {
    public int getSlotId();
}
