package viaversion.viafabric.handler.clientside;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToMessageEncoder;
import java.util.List;
import viaversion.viaversion.api.data.UserConnection;
import viaversion.viaversion.exception.CancelCodecException;
import viaversion.viaversion.exception.CancelEncoderException;

public class VREncodeHandler extends MessageToMessageEncoder<ByteBuf> {

	private final UserConnection info;

	public VREncodeHandler(UserConnection info) {
		this.info = info;
	}

	@Override
	protected void encode(ChannelHandlerContext ctx, ByteBuf byteBuf, List<Object> out) throws Exception {
		if(!info.checkIncomingPacket()) throw CancelEncoderException.generate(null);
		if(!info.shouldTransformPacket()) {
			out.add(byteBuf.retain());
			return;
		}

		/*ByteBuf copy = byteBuf.copy();
		PacketBuffer packetBuffer = new PacketBuffer(copy);
		int packetId = packetBuffer.readVarIntFromBuffer();*/

		ByteBuf transformedBuf = ctx.alloc().buffer().writeBytes(byteBuf);
		try {
			info.transformIncoming(transformedBuf, CancelEncoderException::generate);
			out.add(transformedBuf.retain());
		} finally {
			transformedBuf.release();
		}
	}

	@Override
	public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
		if(cause instanceof CancelCodecException) return;
		super.exceptionCaught(ctx, cause);
	}
}
