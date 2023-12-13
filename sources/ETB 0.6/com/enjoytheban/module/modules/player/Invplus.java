package com.enjoytheban.module.modules.player;

import java.awt.Color;

import org.lwjgl.input.Keyboard;

import com.enjoytheban.api.EventHandler;
import com.enjoytheban.api.events.misc.EventInventory;
import com.enjoytheban.api.events.rendering.EventRender2D;
import com.enjoytheban.api.events.world.EventPacketSend;
import com.enjoytheban.api.value.Option;
import com.enjoytheban.module.Module;
import com.enjoytheban.module.ModuleType;
import com.enjoytheban.utils.math.RotationUtil;

import net.minecraft.client.gui.GuiChat;
import net.minecraft.network.play.client.C0DPacketCloseWindow;

/**
 * Basic inventorymove with arrowkey movement in inventory
 * @called MovementInputFromOptions updatePlayerMoveState()
 * @author Purity
 */

public class Invplus extends Module {

	public Option<Boolean> sw = new Option("ScreenWalk", "screenwalk", true);
	private Option<Boolean> xc = new Option("MoreInventory", "MoreInventory", false);

	
	public Invplus() {
		super("Inventory+", new String[] {"inventorywalk", "invwalk", "inventorymove", "inv+"}, ModuleType.Player);
		setColor(new Color(174,174,227).getRGB());
		addValues(sw, xc);
	}

	@EventHandler
    public void onEvent(EventPacketSend event) {
        if (event.getPacket() instanceof C0DPacketCloseWindow) {
   		 if(xc.getValue())
            event.setCancelled(true);
        }
    }
	
	 @EventHandler
     public void onInv(EventInventory event) {
		 if(xc.getValue())
         event.setCancelled(true);
     } 
	
	//The code for head movement with arrow keys in inventory
	@EventHandler
	private void onRender(EventRender2D e) {
		//if isnt in chat and a screen is open
		if(mc.currentScreen != null && !(mc.currentScreen instanceof GuiChat) && sw.getValue()) {
			//if the respective key is down change the pitch/yaw of the players head
			if (Keyboard.isKeyDown(200)) {
                RotationUtil.pitch(RotationUtil.pitch() - 2.0f);
            }
			
            if (Keyboard.isKeyDown(208)) {
            	RotationUtil.pitch(RotationUtil.pitch() + 2.0f);
            }
            
            if (Keyboard.isKeyDown(203)) {
            	RotationUtil.yaw(RotationUtil.yaw() - 2.0f);
            }
            
            if (Keyboard.isKeyDown(205)) {
            	RotationUtil.yaw(RotationUtil.yaw() + 2.0f);
            }
		}
	}
}