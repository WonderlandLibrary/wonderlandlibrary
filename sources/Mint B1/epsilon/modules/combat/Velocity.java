package epsilon.modules.combat;


import epsilon.Epsilon;
import epsilon.events.Event;
import epsilon.events.listeners.EventMotion;
import epsilon.events.listeners.EventUpdate;
import epsilon.events.listeners.packet.EventReceivePacket;
import epsilon.events.listeners.packet.EventSendPacket;
import epsilon.settings.setting.ModeSetting;
import epsilon.settings.setting.NumberSetting;
import epsilon.util.MoveUtil;
import net.minecraft.network.Packet;
import net.minecraft.network.play.client.C00PacketKeepAlive;
import net.minecraft.network.play.client.C03PacketPlayer;
import net.minecraft.network.play.client.C03PacketPlayer.C04PacketPlayerPosition;
import net.minecraft.network.play.client.C03PacketPlayer.C06PacketPlayerPosLook;
import net.minecraft.network.play.client.C0CPacketInput;
import net.minecraft.network.play.client.C0FPacketConfirmTransaction;
import net.minecraft.network.play.server.S12PacketEntityVelocity;
import net.minecraft.network.play.server.S27PacketExplosion;
import org.lwjgl.input.Keyboard;
import epsilon.util.MoveUtil;

import epsilon.modules.Module;
import epsilon.modules.Module.Category;


public class Velocity extends Module {
	
	
	public MoveUtil move = new MoveUtil();
	
	
	public ModeSetting mode = new ModeSetting("Mode", "Cancel", "Cancel", "Motion","Reverse","VerusNew", "Matrix", "Vulcan");
	
	
	public NumberSetting horiz = new NumberSetting ("Horiz", 0, 0, 200, 10);

	public NumberSetting y = new NumberSetting ("Vert", 0, 0, 200, 10);
	
    public Velocity(){
    	super("Velocity", Keyboard.KEY_NONE, Category.COMBAT, "Denies knockback");
    	this.addSettings(mode, horiz, y);
    }
    public void onEnable() {
    }
    public void onDisable() {
    }

	
   
    
   
    public void onEvent(Event e) {
    	
    	
    	if(mc.thePlayer!= null && mc.theWorld!=null) {
    		
    		if(e instanceof EventUpdate) {
    			this.displayInfo = mode.getMode();
    			if(mode.getMode()=="Motion") {
    				this.displayInfo = mode.getMode() + " | " + horiz.getValue() + "xz | " + y.getValue() + "y";
    			}
    		}
    	
	    	if(e instanceof EventReceivePacket) {
	    		
	    		Packet p = e.getPacket(); 
	    		
	    		if(p instanceof S12PacketEntityVelocity) {
	    			S12PacketEntityVelocity packet = (S12PacketEntityVelocity) p;
	    			
	    			if(packet.id != mc.thePlayer.getEntityId()) {
		    			switch(mode.getMode()) {
		    			case "Vulcan":
		    			case "Matrix":
		    			case "Cancel":
		    				
		        			e.setCancelled();
		    				
		    				break;
		    				
		    			case "Motion":
		    				
		    				if(horiz.getValue()==0 && y.getValue() == 0) {
		    					e.setCancelled();
		    					return;
		    				}	
		    				
		    				packet.motionX *= horiz.getValue() / 100.0;
		                    packet.motionY *= y.getValue() / 100.0;
		                    packet.motionZ *= horiz.getValue() / 100.0;
		    				
		    				
		    				
		    				break;
		    				
		    			case "Reverse":
		    				
		    				packet.motionX = -packet.motionX;
		    				packet.motionZ = -packet.motionZ;
		    				
		    				break;
		    				
		    				
		    			}
	    			}
	    			
	    			
	    			
	    		}	
	    		
	    		
	    		
	    	}
	    	
	    	if(e instanceof EventSendPacket && mc.thePlayer.hurtTime>0) {

    			final Packet p = e.getPacket();
    			
	    		switch(mode.getMode()) {
	    		case "Matrix":

	    			
	    			if(p instanceof C0FPacketConfirmTransaction) {
	    				final C0FPacketConfirmTransaction packet = (C0FPacketConfirmTransaction) p;
	    				packet.uid*=-1;
	    				e.setCancelled();
	    				mc.getNetHandler().sendPacketNoEventDelayed(packet, 1000);
	    			}
	    			
	    			
	    			break;
	    		case "Vulcan":
	    			
	    			
	    			if(p instanceof C0FPacketConfirmTransaction) 
	    				e.setCancelled();
	    				
	    			
	    			break;
	    		
	    		
	    		}
	    		
	    	}
    	
    	
    	}
    }
}