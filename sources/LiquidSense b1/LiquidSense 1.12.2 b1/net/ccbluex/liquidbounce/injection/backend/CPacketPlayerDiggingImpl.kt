package net.ccbluex.liquidbounce.injection.backend

import net.ccbluex.liquidbounce.api.minecraft.network.play.client.ICPacketPlayerDigging
import net.ccbluex.liquidbounce.api.minecraft.util.IEnumFacing
import net.ccbluex.liquidbounce.api.minecraft.util.WBlockPos
import net.ccbluex.liquidbounce.injection.backend.utils.wrap
import net.minecraft.network.play.client.CPacketPlayerDigging

 class CPacketPlayerDiggingImpl<T : CPacketPlayerDigging>(wrapped: T) : PacketImpl<T>(wrapped), ICPacketPlayerDigging {

     override val position: WBlockPos
         get() = WBlockPos(wrapped.position.x,wrapped.position.y, wrapped.position.z)

     override val facing : IEnumFacing
         get() = wrapped.facing.wrap()

     override val action: ICPacketPlayerDigging.WAction
         get() =  wrapped.action.wrap()

}

inline fun ICPacketPlayerDigging.unwrap(): CPacketPlayerDigging = (this as CPacketPlayerDiggingImpl<*>).wrapped
inline fun CPacketPlayerDigging.wrap(): ICPacketPlayerDigging = CPacketPlayerDiggingImpl(this)