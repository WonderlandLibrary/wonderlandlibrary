package cc.novoline.commands.impl;

import cc.novoline.Novoline;
import cc.novoline.commands.NovoCommand;
import cc.novoline.utils.java.Checks;
import io.netty.buffer.Unpooled;

import java.util.Locale;
import java.util.function.Consumer;
import net.minecraft.client.Minecraft;
import net.minecraft.client.network.NetHandlerPlayClient;
import net.minecraft.command.CommandException;
import net.minecraft.network.NetworkManager;
import static net.minecraft.network.NetworkManager.ViaNetworkManager;
import net.minecraft.network.PacketBuffer;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Range;
import viaversion.viaversion.api.data.UserConnection;

public class TestCommand extends NovoCommand {

	public TestCommand(@NotNull Novoline novoline) {
		super(novoline, "test");
	}

	@Override
	public void process(String[] args) throws CommandException {
		Minecraft mc = novoline.getMinecraft();

		if(mc.player != null) {
			NetHandlerPlayClient sendQueue = mc.player.connection;
			NetworkManager networkManager = sendQueue.getNetworkManager();

			//noinspection InstanceofConcreteClass
			if(networkManager instanceof ViaNetworkManager) {
				ViaNetworkManager viaNetworkManager = (ViaNetworkManager) networkManager;
				UserConnection userConnection = viaNetworkManager.getUserConnection();

				if(args.length < 1) {
					send("No args");
					return;
				}

				String s = args[0].toLowerCase(Locale.ROOT);
				PacketBuffer packetBuffer;

				switch(s) {
					case "a":
						packetBuffer = constructPacket(10, 0x27, buf -> buf.writeEnumValue(EnumHand.OFF_HAND));
						break;
					case "b":
						packetBuffer = constructPacket(10, 0x2A, buf -> buf.writeEnumValue(EnumHand.OFF_HAND));
						break;
					default:
						send("Unexpected value: " + s);
						return;
				}

				userConnection.sendRawPacketToServerClientSide(packetBuffer, false);
			} else {
				send("No via");
			}
		}
	}

	public static void sendPacketWithoutConversion(@NotNull PacketBuffer packetBuffer) {
		Checks.notNull(packetBuffer, "Packet buffer");

		Minecraft mc = Minecraft.getInstance();
		if(mc.player == null) throw new IllegalStateException("mc.player is null");

		NetHandlerPlayClient sendQueue = mc.player.connection;
		NetworkManager networkManager = sendQueue.getNetworkManager();
		//noinspection InstanceofConcreteClass
		if(!(networkManager instanceof ViaNetworkManager)) throw new IllegalStateException("No via");

		ViaNetworkManager viaNetworkManager = (ViaNetworkManager) networkManager;
		UserConnection userConnection = viaNetworkManager.getUserConnection();

		/*try {
			ChannelHandlerContext ctx = userConnection.getChannel().pipeline().context(Via.getManager().getInjector().getEncoderName());
			ctx.writeAndFlush(packetBuffer);
		} catch(Throwable e) {
			e.printStackTrace();
			packet.release(); // Couldn't schedule
		}*/

		userConnection.sendRawPacketToServerClientSide(packetBuffer, false);
	}

	@Contract("_, _, _ -> new")
	public static @NotNull PacketBuffer constructPacket(@Range(from = 0, to = Integer.MAX_VALUE) int bufferCapacity,
														@Range(from = 0, to = Integer.MAX_VALUE) int packetId,
														@NotNull Consumer<? super PacketBuffer> transformations) {
		PacketBuffer packetBuffer = new PacketBuffer(Unpooled.buffer(bufferCapacity));
		transformations.accept(packetBuffer);
		return packetBuffer;
	}

	public enum EnumHand {

		MAIN_HAND,
		OFF_HAND
    }
}
