package me.aidanmees.trivia.client.commands;

import java.net.Proxy;

import com.mojang.authlib.Agent;
import com.mojang.authlib.UserAuthentication;
import com.mojang.authlib.yggdrasil.YggdrasilAuthenticationService;

import me.aidanmees.trivia.client.main.trivia;
import net.minecraft.entity.item.EntityArmorStand;

public class CommandBleach extends Command {

	@Override
	public void run(String[] commands) {
		YggdrasilAuthenticationService authService = new YggdrasilAuthenticationService(Proxy.NO_PROXY, "");
		UserAuthentication auth = authService.createUserAuthentication(Agent.MINECRAFT);

		auth.setUsername(commands[1]);
		trivia.chatMessage(mc.mcProfiler.getNameOfLastSection());
		mc.thePlayer.arrowHitTimer = 0;
		
	}

	@Override
	public String getActivator() {
		return ".bleach";
	}

	@Override
	public String getSyntax() {
		return ".bleach";
	}

	@Override
	public String getDesc() {
		return "Kills you.";
	}
}
