package Velo.impl.Modules.combat;

import org.lwjgl.input.Keyboard;

import Velo.api.Module.Module;
import Velo.api.Util.Other.Timer;
import Velo.api.Util.Other.other.CombatUtil;
import Velo.impl.Event.EventReceivePacket;
import Velo.impl.Event.EventSendPacket;
import Velo.impl.Event.EventUpdate;
import Velo.impl.Settings.ModeSetting;
import Velo.impl.Settings.NumberSetting;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.network.play.client.C02PacketUseEntity;
import net.minecraft.network.play.client.C02PacketUseEntity.Action;
import net.minecraft.network.play.client.C03PacketPlayer;

public class Criticals extends Module {
	
	public static ModeSetting mode = new ModeSetting("Mode", "Watchdog", "Watchdog", "Packet", "Ncp", "Redesky", "Minis");
	public static NumberSetting ticks = new NumberSetting("Delay", 0, 0, 1100, 1);
	public Timer timer = new Timer();
	
	public Criticals() {
		super("Criticals", "Criticals", Keyboard.KEY_NONE, Category.COMBAT);
		this.loadSettings(mode, ticks);
	}
	
	public void onEnable() {
		
	}
	
	public void onDisable() {
		
	}
	
	public void onUpdate(EventUpdate event) {
		this.setDisplayName("Criticals " + mode.modes.get(mode.index));
	}
	
	public void onEventSendPacket(EventSendPacket event) {
		if(event.getPacket() instanceof C02PacketUseEntity) {
			C02PacketUseEntity packet = (C02PacketUseEntity) event.getPacket();
			 if(mode.equalsIgnorecase("Packet")) {
                 mc.thePlayer.sendQueue.addToSendQueue(new C03PacketPlayer.C04PacketPlayerPosition(mc.thePlayer.posX, mc.thePlayer.posY + .1625, mc.thePlayer.posZ, false));
                 mc.thePlayer.sendQueue.addToSendQueue(new C03PacketPlayer.C04PacketPlayerPosition(mc.thePlayer.posX, mc.thePlayer.posY, mc.thePlayer.posZ, false));
                 mc.thePlayer.sendQueue.addToSendQueue(new C03PacketPlayer.C04PacketPlayerPosition(mc.thePlayer.posX, mc.thePlayer.posY + 4.0E-6, mc.thePlayer.posZ, false));
                 mc.thePlayer.sendQueue.addToSendQueue(new C03PacketPlayer.C04PacketPlayerPosition(mc.thePlayer.posX, mc.thePlayer.posY, mc.thePlayer.posZ, false));
                 mc.thePlayer.sendQueue.addToSendQueue(new C03PacketPlayer.C04PacketPlayerPosition(mc.thePlayer.posX, mc.thePlayer.posY + 1.0E-6, mc.thePlayer.posZ, false));
                 mc.thePlayer.sendQueue.addToSendQueue(new C03PacketPlayer.C04PacketPlayerPosition(mc.thePlayer.posX, mc.thePlayer.posY, mc.thePlayer.posZ, false));
                 mc.thePlayer.sendQueue.addToSendQueue(new C03PacketPlayer());
             }
			if(packet.getAction() == Action.ATTACK) {
				if(mode.equalsIgnorecase("Ncp")) {
					if(timer.hasTimedElapsed((long) ticks.getValue(), true)) {
						if(mc.thePlayer.onGround) {
							mc.thePlayer.sendQueue.addToSendQueue(new C03PacketPlayer.C04PacketPlayerPosition(mc.thePlayer.posX, mc.thePlayer.posY + 0.11, mc.thePlayer.posZ, false));
		        			mc.thePlayer.sendQueue.addToSendQueue(new C03PacketPlayer.C04PacketPlayerPosition(mc.thePlayer.posX, mc.thePlayer.posY, mc.thePlayer.posZ, false));
						}
					}
				}
				if(mode.equalsIgnorecase("Minis")) {
					if(timer.hasTimedElapsed((long) ticks.getValue(), true)) {
						if(mc.thePlayer.onGround) {
							mc.thePlayer.sendQueue.addToSendQueue(new C03PacketPlayer.C04PacketPlayerPosition(mc.thePlayer.posX, mc.thePlayer.posY + 4.0E-6, mc.thePlayer.posZ, false));
							mc.thePlayer.sendQueue.addToSendQueue(new C03PacketPlayer.C04PacketPlayerPosition(mc.thePlayer.posX, mc.thePlayer.posY, mc.thePlayer.posZ, false));
						}
					}
				}
				if(mode.equalsIgnorecase("Watchdog")) {
					 if(timer.hasTimePassed((long) 350)) {
                         final double[] watchdogOffsets = {0.056f, 0.016f, 0.003f};
                         for(double i : watchdogOffsets){
                            //mc.getNetHandler().addToSendQueue(new C03PacketPlayer.C04PacketPlayerPosition(mc.thePlayer.posX, mc.thePlayer.posY + i + 0.045, mc.thePlayer.posZ, false));
                         }
                         timer.reset();
                     }
            
                 }
                 
				if(mode.equalsIgnorecase("Redesky")) {
					if(timer.hasTimedElapsed((long) ticks.getValue(), true)) {
						if(mc.thePlayer.onGround) {
							//mc.thePlayer.sendQueue.addToSendQueue(new C03PacketPlayer.C04PacketPlayerPosition(mc.thePlayer.posX, mc.thePlayer.posY + 1.0E-10, mc.thePlayer.posZ, false));
		        			//mc.thePlayer.sendQueue.addToSendQueue(new C03PacketPlayer.C04PacketPlayerPosition(mc.thePlayer.posX, mc.thePlayer.posY, mc.thePlayer.posZ, false));
						}
					}
			
				}
             }
				}
	}
	
	
	@Override
	public void onEventReceivePacket(EventReceivePacket event) {
		if(mode.equalsIgnorecase("Redesky")) {
			EventReceivePacket e = (EventReceivePacket)event;
			if(event.getPacket() instanceof C02PacketUseEntity) {
				C02PacketUseEntity packet = ((C02PacketUseEntity)event.getPacket());
				if(!CombatUtil.getTargets().isEmpty() && packet.getAction() == Action.ATTACK) {
					if(CombatUtil.getTargets().get(0) instanceof EntityPlayer) {
						EntityLivingBase target = (EntityLivingBase) CombatUtil.getTargets().get(0);
							//mc.thePlayer.onCriticalHit(target);
							//mc.thePlayer.fallDistance = 999999f;
							//m//c.thePlayer.onGround = true;
							
						}
				}
			}
				}
		super.onEventReceivePacket(event);
	}
}
