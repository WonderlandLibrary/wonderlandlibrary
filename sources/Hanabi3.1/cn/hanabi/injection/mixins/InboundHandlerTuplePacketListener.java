package cn.hanabi.injection.mixins;

import io.netty.util.concurrent.GenericFutureListener;
import net.minecraft.network.Packet;

class InboundHandlerTuplePacketListener {
   @SafeVarargs
   public InboundHandlerTuplePacketListener(Packet inPacket, GenericFutureListener... inFutureListeners) {
   }
}
