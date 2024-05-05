package me.aidanmees.trivia.client.commands;

import java.net.Proxy;

import org.lwjgl.input.Keyboard;

import com.mojang.authlib.Agent;
import com.mojang.authlib.UserAuthentication;
import com.mojang.authlib.yggdrasil.YggdrasilAuthenticationService;

import me.aidanmees.trivia.client.main.trivia;
import me.aidanmees.trivia.module.Module;
import net.minecraft.entity.item.EntityArmorStand;

public class CommandBind extends Command {

	@Override
	public void run(String[] commands) {

       if (commands.length == 3) {
        int key = Keyboard.getKeyIndex((String)commands[2].toUpperCase());
        if (trivia.getModuleByName(commands[1]) != null) {
   
        	if (commands[2].toUpperCase().equals("NONE")) {
        		
        		 trivia.getModuleByName(commands[1]).setKeyCode(0);
        	}
        	else {
        	if (Keyboard.getKeyIndex(commands[2].toUpperCase()) == 0) {
                trivia.chatMessage("Could not find the key "+ commands[2]+".§f Please try .bind <module> <key>");
        	}
        	else {
            trivia.getModuleByName(commands[1]).setKeyCode(key);
            trivia.chatMessage(String.format(""+commands[1].toString() +"'s§f keybind as successfully been bound to "+commands[2].toString().toUpperCase()+"§f.", trivia.getModuleByName(commands[1]).getKeyboardKey(), commands[2].toUpperCase()));
        	}
        	}
        	} else {
            trivia.chatMessage("The module "+commands[1].toString()+"§f Does not exist! Please try .bind <module> <key>");
        }
       }
       else {
    	   trivia.chatMessage("Something went wrong! Please try .bind <module> <key>");
       }
    
		
	}

	@Override
	public String getActivator() {
		return ".bind";
	}

	@Override
	public String getSyntax() {
		return ".bind <module> <key>";
	}

	@Override
	public String getDesc() {
		return "how do u not fucking know what bind is retard.";
	}
}
