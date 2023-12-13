package me.robbanrobbin.jigsaw.client.commands;

import java.net.InetAddress;
import java.net.UnknownHostException;

import me.robbanrobbin.jigsaw.client.commands.option.CommandOption;
import net.minecraft.client.multiplayer.ServerAddress;

public class CommandServerIp extends Command {

	@Override
	public void run(String[] commands) {
		if (mc.getCurrentServerData() == null) {
			addResult("§cYou are not on a server!");
			return;
		}
		try {
			addResult(InetAddress
					.getByName(ServerAddress.fromString(mc.getCurrentServerData().serverIP).getIP()).getHostAddress()
					+ " : " + ServerAddress.fromString(mc.getCurrentServerData().serverIP).getPort());
		} catch (UnknownHostException e) {
			addResult("§cError...");
		}
	}

	@Override
	public String getActivator() {
		return "serverip";
	}

	@Override
	public String getSyntax() {
		return "serverip";
	}

	@Override
	public String getDesc() {
		return "Prints the server ip in IPv4 format";
	}

	@Override
	public CommandOption[] setOptions() {
		// TODO Auto-generated method stub
		return null;
	}
}
