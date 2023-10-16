package com.enjoytheban.module.modules.combat;

import java.awt.Color;

import com.enjoytheban.Client;
import com.enjoytheban.api.EventHandler;
import com.enjoytheban.api.events.world.EventPacketSend;
import com.enjoytheban.api.events.world.EventPreUpdate;
import com.enjoytheban.api.value.Mode;
import com.enjoytheban.command.commands.Help;
import com.enjoytheban.module.Module;
import com.enjoytheban.module.ModuleType;
import com.enjoytheban.module.modules.movement.Speed;
import com.enjoytheban.utils.Helper;
import com.enjoytheban.utils.TimerUtil;

import net.minecraft.network.play.client.C02PacketUseEntity;
import net.minecraft.network.play.client.C03PacketPlayer;

/**
 * Basic Minijump criticals
 * @author Purity
 */

public class Criticals extends Module {

	private Mode mode = new Mode("Mode", "mode", CritMode.values(), CritMode.Packet);

	private TimerUtil timer = new TimerUtil();

	//Constructor
	public Criticals() {
		super("Criticals", new String[] {"crits", "crit"}, ModuleType.Combat);
		setColor(new Color(235,194,138).getRGB());
		addValues(mode);
	}
	
	@EventHandler
	private void onUpdate(EventPreUpdate e) {
		setSuffix(mode.getValue());
	}
	
	//A simple check for checking if the player can crit or not
	private boolean canCrit() {
		return mc.thePlayer.onGround && !mc.thePlayer.isInWater() && !Client.instance.getModuleManager().getModuleByClass(Speed.class).isEnabled();
	}
	
	//Crit method for minijumps
	@EventHandler
	private void onPacket(EventPacketSend e) {
		if(e.getPacket() instanceof C02PacketUseEntity && canCrit() && mode.getValue() == CritMode.Minijumps) {
			mc.thePlayer.motionY = 0.2;
		}
	}

	//do not call this method as an event, call it in aura
	void packetCrit() {
		//if the timer  has reached 250 and the mode is packet + player is onground
		if (timer.hasReached(Helper.onServer("hypixel") ? 500 : 10) && mode.getValue() == CritMode.Packet && canCrit()) {
			//the offsets we will be using
			double[] offsets = {0.0625, 0.0, 1.0E-4, 0.0};
			//for every offset send a movement packet to crit
			for (int i = 0; i < offsets.length; ++i) {
				mc.thePlayer.sendQueue.addToSendQueue(new C03PacketPlayer.C04PacketPlayerPosition(mc.thePlayer.posX, mc.thePlayer.posY + offsets[i], mc.thePlayer.posZ, false));
			}
			//reset the timer
			timer.reset();
		}
	}

	// critting without a timer for tickaura
	void offsetCrit() {
		if (canCrit() && !mc.getCurrentServerData().serverIP.contains("hypixel")) {
			double[] offsets= {0.0624, 0.0, 1.0E-4, 0.0};
			for(int i = 0; i < offsets.length; i++ ) {
				mc.thePlayer.sendQueue.addToSendQueue(new C03PacketPlayer.C04PacketPlayerPosition(mc.thePlayer.posX, mc.thePlayer.posY + offsets[i], mc.thePlayer.posZ, false));
			}
		}
	}
	
	public void hypixelCrit() {
		if(mode.getValue() == CritMode.Hypixel) {
	        if (canCrit()) {
	            for (double offset : new double[]{0.06143F, 0.0, 0.012511F, 0.0})
	                mc.thePlayer.sendQueue.addToSendQueue(new C03PacketPlayer.C04PacketPlayerPosition(mc.thePlayer.posX, mc.thePlayer.posY + offset, mc.thePlayer.posZ, true));
	        }
		}
	}
	
	//modes for crits
	enum CritMode {
		Packet, Hypixel, Minijumps
	}
}
