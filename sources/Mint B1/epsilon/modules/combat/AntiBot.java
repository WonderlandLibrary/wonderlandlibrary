package epsilon.modules.combat;

import java.util.ArrayList;
import java.util.List;

import org.lwjgl.input.Keyboard;

import epsilon.Epsilon;
import epsilon.events.Event;
import epsilon.events.listeners.EventUpdate;
import epsilon.events.listeners.packet.EventReceivePacket;
import epsilon.modules.Module.Category;
import epsilon.modules.Module;
import epsilon.settings.setting.BooleanSetting;
import epsilon.settings.setting.ModeSetting;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.network.Packet;
import net.minecraft.network.play.server.S0CPacketSpawnPlayer;
public class AntiBot extends Module {

	public static List<Entity> bots = new ArrayList<>();
	
	public ModeSetting mode = new ModeSetting ("Mode", "Hypixel", "Hypixel", "Matrix");
	
	public AntiBot(){
		super("AntiBot", Keyboard.KEY_NONE, Category.COMBAT, "Prevents targeting invisible entities");
		this.addSettings(mode);
	}
	
	
	public void onEnable(){
		bots.clear();
	}
	
	
	
	
	public void onEvent(Event e){
		
		if(e instanceof EventReceivePacket && mc.thePlayer!=null) {
		
    		/*Packet p = e.getPacket();
    		if(mc.thePlayer.ticksExisted>55) {
				if(p instanceof S0CPacketSpawnPlayer) {

					S0CPacketSpawnPlayer entity = (S0CPacketSpawnPlayer)p;
	
					EntityPlayer en = entity.ent;
					
					if(en!=null) {
					
						if(mc.thePlayer.getDistanceToEntity(en) < 6 && !(bots.contains(en))) {
							bots.add(en);
							Epsilon.addChatMessage("Detected a bot");
							en.bot = true;
						}
						else
							en.bot = false;
					}
					
				}
    		}*/
				
		}
		
		if(e instanceof EventUpdate){
			
			
		}
	}	
	
}
