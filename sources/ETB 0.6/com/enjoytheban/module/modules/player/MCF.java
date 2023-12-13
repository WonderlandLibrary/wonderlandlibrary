package com.enjoytheban.module.modules.player;

import java.awt.Color;

import org.lwjgl.input.Mouse;

import com.enjoytheban.api.EventHandler;
import com.enjoytheban.api.events.world.EventPreUpdate;
import com.enjoytheban.management.FriendManager;
import com.enjoytheban.module.Module;
import com.enjoytheban.module.ModuleType;

import net.minecraft.entity.player.EntityPlayer;

/**
 * A module for easily adding friends by middle clicking
 * @author Purity
 */

public class MCF extends Module {

	//boolean that holds if the mouse is down
	private boolean down;
	
	public MCF() {
		super("MCF", new String[] {"middleclickfriends", "middleclick"}, ModuleType.Player);
		setColor(new Color(241,175,67).getRGB());
	}
	
	//now for the code
	@EventHandler
	private void onClick(EventPreUpdate e) {
		//if the mouse is down
		if ((Mouse.isButtonDown(2)) && (!down)) {
			
			if (mc.objectMouseOver.entityHit != null) {
	        
			EntityPlayer player = (EntityPlayer)mc.objectMouseOver.entityHit;
	        
			String playername = player.getName();
	        
			//send the command so its added as a friend
	        if (!FriendManager.isFriend(playername)) {
	        	mc.thePlayer.sendChatMessage(".f add " + playername);
	        
	        } else {
	        	mc.thePlayer.sendChatMessage(".f del " + playername);
	        }
	      }
	      down = true;
	    }
		//if the mouse isnt doown set down to false
	    if (!Mouse.isButtonDown(2)) {
	      down = false;
	    }
	}
}