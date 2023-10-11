package artemis.util;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ServerAddress;
import net.minecraft.client.multiplayer.ServerData;
import net.minecraft.network.EnumConnectionState;
import net.minecraft.network.INetHandler;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.handshake.client.C00Handshake;
import net.minecraft.network.status.INetHandlerStatusClient;
import net.minecraft.network.status.client.C00PacketServerQuery;
import net.minecraft.network.status.client.C01PacketPing;
import net.minecraft.network.status.server.S00PacketServerInfo;
import net.minecraft.network.status.server.S01PacketPong;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.IChatComponent;

public class PingUtils {

	private static long nextUpdate = 0;
	private static int delay = 5000;

	public static int getDelay() {
		return delay;
	}

	public static void setDelay(int delay) {
		PingUtils.delay = delay;
	}

	public static int serverPing = 0;

	/**
	 * Method used to determine ping to current server. Base minecraft code from OldServerPinger.java
	 * */
	public static void ping() {
		if (System.currentTimeMillis() < nextUpdate) {
			return;
		}
		try {
			ServerData server = Minecraft.getMinecraft().getCurrentServerData();
			if (server == null) {
				serverPing = 0;
				return;
			}
			ServerAddress address = ServerAddress.func_78860_a(server.serverIP);

			final NetworkManager network = NetworkManager.provideLanClient(InetAddress.getByName(address.getIP()),
					address.getPort());
			network.setNetHandler(new INetHandlerStatusClient() {
				long start;

				@Override
				public void onDisconnect(IChatComponent var1) {

				}

				@Override
				public void handleServerInfo(S00PacketServerInfo var1) {
					start = Minecraft.getSystemTime();
					network.sendPacket(new C01PacketPing(start));
				}

				@Override
				public void handlePong(S01PacketPong var1) {
					long time = Minecraft.getSystemTime();
					network.closeChannel(new ChatComponentText("Finished"));
					PingUtils.serverPing = (int) (time - start);
				}
			});
			network.sendPacket(new C00Handshake(47, address.getIP(), address.getPort(), EnumConnectionState.STATUS));
			network.sendPacket(new C00PacketServerQuery());
			nextUpdate = System.currentTimeMillis() + delay;
		} catch (UnknownHostException e) {
			e.printStackTrace();
		}
		return;
	}
}
