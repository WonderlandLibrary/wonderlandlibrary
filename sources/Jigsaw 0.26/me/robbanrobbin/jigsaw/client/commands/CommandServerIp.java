package me.robbanrobbin.jigsaw.client.commands;

import java.net.InetAddress;
import java.net.UnknownHostException;

import me.robbanrobbin.jigsaw.client.main.Jigsaw;
import net.minecraft.client.multiplayer.ServerAddress;

public class CommandServerIp extends Command {

	@Override
	public void run(String[] commands) {
		if (mc.getCurrentServerData() == null) {
			Jigsaw.chatMessage("§cYou are not on a server!");
			return;
		}
		try {
			Jigsaw.chatMessage(InetAddress
					.getByName(ServerAddress.func_78860_a(mc.getCurrentServerData().serverIP).getIP()).getHostAddress()
					+ " : " + ServerAddress.func_78860_a(mc.getCurrentServerData().serverIP).getPort());
		} catch (UnknownHostException e) {
			Jigsaw.chatMessage("§cError...");
		}
	}

	@Override
	public String getActivator() {
		return ".serverip";
	}

	@Override
	public String getSyntax() {
		return ".serverip";
	}

	@Override
	public String getDesc() {
		return "Prints the server ip in IPv4 format";
	}
}
