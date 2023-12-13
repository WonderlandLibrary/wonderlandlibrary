package me.aidanmees.trivia.client.alts;

import java.net.Proxy;

import com.mojang.authlib.Agent;
import com.mojang.authlib.UserAuthentication;
import com.mojang.authlib.exceptions.AuthenticationException;
import com.mojang.authlib.yggdrasil.YggdrasilAuthenticationService;

import me.aidanmees.trivia.client.main.trivia;
import me.aidanmees.trivia.gui.GuitriviaAltLogin;
import me.aidanmees.trivia.gui.GuitriviaAltManager;
import net.minecraft.client.Minecraft;
import net.minecraft.util.Session;

public class Login {

	public static boolean login(String email, String password) throws AuthenticationException {
		login(email, password, Proxy.NO_PROXY);
		trivia.loggedInName = null;
		return true;
	}

	public static void login(String email, String password, Proxy proxy) throws AuthenticationException {
		YggdrasilAuthenticationService authService = new YggdrasilAuthenticationService(proxy, "");
		UserAuthentication auth = authService.createUserAuthentication(Agent.MINECRAFT);

		auth.setUsername(email);
		auth.setPassword(password);
		auth.logIn();
		Minecraft.getMinecraft().session = new Session(auth.getSelectedProfile().getName(),
				auth.getSelectedProfile().getId().toString(), auth.getAuthenticatedToken(), "mojang");
	}

	public static void changeName(String newName) {
		if (newName.equals("NoHaxJusttrivia") || newName.equals("Robin146") || newName.equals("triviaClient")
				|| newName.equals("triviaDev")) {
			newName = "succ";
		}
		Minecraft.getMinecraft().session = new Session(newName, "", "", "mojang");
		trivia.loggedInName = null;
	}

}
