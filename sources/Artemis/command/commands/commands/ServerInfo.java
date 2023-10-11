package artemis.command.commands;

import artemis.Artemis;
import artemis.module;
import artemis.command.Command;
import artemis.util.MovementUtils;
import net.minecraft.client.Minecraft;

public class ServerInfo extends Command{

	@Override
	public String getAlias() {
		// TODO Auto-generated method stub
		return "serverinfo";
	}

	@Override
	public String getDescription() {
		// TODO Auto-generated method stub
		return "gets info for current server";
	}

	@Override
	public String getSyntax() {
		// TODO Auto-generated method stub
		return ".serverinfo";
	}


		

		
	@Override
	public void onCommand(String command, String[] args) throws Exception {
	long ping = Minecraft.getMinecraft().currentServerData.pingToServer;
	String name = Minecraft.getMinecraft().currentServerData.serverName;
	int version = Minecraft.getMinecraft().currentServerData.version;
	String MO = Minecraft.getMinecraft().currentServerData.serverMOTD;
	String ver = Minecraft.getMinecraft().currentServerData.gameVersion;
	String ip = Minecraft.getMinecraft().currentServerData.serverIP;
	String players = Minecraft.getMinecraft().currentServerData.populationInfo;
	
Artemis.addChatMessage("Info: ");		
Artemis.addChatMessage("Ping: " + ping);
Artemis.addChatMessage("Name: " + name);
Artemis.addChatMessage("ProtocolVersion: " + version);
Artemis.addChatMessage("IP: " + ip);
Artemis.addChatMessage("Players: " + players);
Artemis.addChatMessage("ServerMOTD: " + MO);
Artemis.addChatMessage("Version: " + ver);
		
	}
	}


