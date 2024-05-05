package epsilon.modules.combat;

import org.lwjgl.input.Keyboard;

import epsilon.Epsilon;
import epsilon.events.Event;
import epsilon.events.listeners.EventMotion;
import epsilon.events.listeners.packet.EventSendPacket;
import epsilon.modules.Module;
import epsilon.settings.setting.BooleanSetting;
import epsilon.settings.setting.ModeSetting;
import epsilon.settings.setting.NumberSetting;
import epsilon.util.MoveUtil;
import epsilon.util.Timer;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.network.Packet;
import net.minecraft.network.play.client.C02PacketUseEntity;
import net.minecraft.network.play.client.C03PacketPlayer.C04PacketPlayerPosition;
import net.minecraft.util.BlockPos;

public class Criticals extends Module {

	public ModeSetting mode = new ModeSetting ("Mode", "Vanilla", "Vanilla", "Watchdog", "NCP", "Bypass","Verus", "Vulcan", "Zonecraft", "Matrix", "Minelatino", "Horizon",  "NoGround","NanoPacket");
	
	
	public NumberSetting onlywhen = new NumberSetting ("Delay", 25.0, 0, 1000.0, 25.0);

	public BooleanSetting onlyfans = new BooleanSetting ("Only when not falling", true);
	
	private Timer timer = new Timer();
	
	private boolean att = false;
	
	public Criticals() {
		super("Criticals", Keyboard.KEY_NONE, Category.COMBAT, "Always performs critical hits");
		this.addSettings(mode, onlywhen, onlyfans);
	}
	
	public void onEnable() {
		if(mode.getMode()=="Matrix" || mode.getMode()=="Zonecraft") 
			Epsilon.addChatMessage("this crit mode must have atleast 200 delay.");
		att = false;
	}
	
	public void onEvent(final Event e) {

		this.displayInfo = mode.getMode();
		if(mode.getMode()=="Matrix") {
			if(onlywhen.getValue()<200) {
				onlywhen.setValue(200);
				Epsilon.addChatMessage("Matrix criticals must atleast have 200 delay, else timer flags");
			}
		}
		
		final MoveUtil move = new MoveUtil();
		
		if(e instanceof EventMotion && e.isPre()) {
			
			final EventMotion event = (EventMotion) e;

        	if(onlyfans.isEnabled() && mc.thePlayer.fallDistance>0)
        		return;
        	
        	if(att) {
        	
	        	switch(mode.getMode()) {
	        	
	        	case "Watchdog":
	        		if(mc.thePlayer.onGround) {
	        			
	        		}
	        		
	        		break;
	        	
	        	}
        	}
		}
		
		if(e instanceof EventSendPacket) {
    		final Packet p = e.getPacket(); 
    		
			if(p instanceof C02PacketUseEntity && (timer.hasTimeElapsed((long) onlywhen.getValue(), true) || onlywhen.getValue()==0)) {

                C02PacketUseEntity packet = (C02PacketUseEntity)e.getPacket();
                
                
                if(packet.getAction() == C02PacketUseEntity.Action.ATTACK) {

                    att = true;
                	if(onlyfans.isEnabled() && mc.thePlayer.fallDistance>0)
                		return;
                	
                	switch(mode.getMode()) {
                	
                	case "Watchdog":
                		if(mc.thePlayer.onGround) {

                    		mc.getNetHandler().sendPacketNoEvent(new C04PacketPlayerPosition(mc.thePlayer.posX, mc.thePlayer.posY + 0.0625, mc.thePlayer.posZ, false));
                    		mc.getNetHandler().sendPacketNoEvent(new C04PacketPlayerPosition(mc.thePlayer.posX, mc.thePlayer.posY, mc.thePlayer.posZ, false));
                		}else {

                    		mc.getNetHandler().sendPacketNoEvent(new C04PacketPlayerPosition(mc.thePlayer.posX, mc.thePlayer.posY + 1E-10, mc.thePlayer.posZ, false));
                    		mc.getNetHandler().sendPacketNoEvent(new C04PacketPlayerPosition(mc.thePlayer.posX, mc.thePlayer.posY, mc.thePlayer.posZ, false));
                		}
                		
                		break;
                	
                	case "Zonecraft":
                		
                		if(mc.thePlayer.onGround)
                    		mc.getNetHandler().sendPacketNoEvent(new C04PacketPlayerPosition(mc.thePlayer.posX, mc.thePlayer.posY - 0.01, mc.thePlayer.posZ, false));
                		else if(mc.thePlayer.fallDistance<=0){
                			mc.getNetHandler().sendPacketNoEvent(new C04PacketPlayerPosition(mc.thePlayer.posX, mc.thePlayer.posY + (mc.thePlayer.motionY), mc.thePlayer.posZ, false));
                				//When ur not onground we have to do motion cuz zonecraft fly checks dont work as long as your y motion is less then it was last tick
                			mc.getNetHandler().sendPacketNoEvent(new C04PacketPlayerPosition(mc.thePlayer.posX, mc.thePlayer.posY, mc.thePlayer.posZ, false));
                		}
                		
                		break;
                	case "Minelatino":
                	case "Vanilla":
                		
                		mc.getNetHandler().sendPacketNoEvent(new C04PacketPlayerPosition(mc.thePlayer.posX, mc.thePlayer.posY - 0.01, mc.thePlayer.posZ, false));
                		break;
                		
                		
                	case "NCP":

                		 mc.getNetHandler().addToSendQueue(new C04PacketPlayerPosition(mc.thePlayer.posX, mc.thePlayer.posY + 0.11, mc.thePlayer.posZ, true));
                         mc.getNetHandler().addToSendQueue(new C04PacketPlayerPosition(mc.thePlayer.posX, mc.thePlayer.posY + 0.0000013579, mc.thePlayer.posZ, false));
                		break;
                		
                	case "Verus":
                		
                		final double y = Math.round((mc.thePlayer.posY+0.5)*2)/2.0;
                		move.place(new BlockPos(mc.thePlayer.posX, mc.thePlayer.posY - 1, mc.thePlayer.posZ), 1,
								new ItemStack(Blocks.stone.getItem
									(mc.theWorld, new BlockPos(0, 0, 0))), 0.5f, 0.5f, 0.5f);
                		mc.getNetHandler().addToSendQueue(new C04PacketPlayerPosition(mc.thePlayer.posX, y+0.5, mc.thePlayer.posZ, false));

                		move.place(new BlockPos(mc.thePlayer.posX, mc.thePlayer.posY - 1, mc.thePlayer.posZ), 1,
								new ItemStack(Blocks.stone.getItem
									(mc.theWorld, new BlockPos(0, 0, 0))), 0.5f, 0.5f, 0.5f);
                		mc.getNetHandler().addToSendQueue(new C04PacketPlayerPosition(mc.thePlayer.posX, y, mc.thePlayer.posZ, false));
                		
                		break;
                		
                		
                	case "Vulcan":

               		 	mc.getNetHandler().addToSendQueue(new C04PacketPlayerPosition(mc.thePlayer.posX, mc.thePlayer.posY + 0.11, mc.thePlayer.posZ, true));

               		 	mc.getNetHandler().addToSendQueue(new C04PacketPlayerPosition(mc.thePlayer.posX, mc.thePlayer.posY, mc.thePlayer.posZ, false));
                        
                        
                		break;
                		
                	case "Matrix":
                		mc.getNetHandler().sendPacketNoEvent(new C04PacketPlayerPosition(mc.thePlayer.posX, mc.thePlayer.posY - 0.00000000001, mc.thePlayer.posZ, false));
                		
                		break;
                		
                	case "Horizon":
                		mc.getNetHandler().sendPacketNoEvent(new C04PacketPlayerPosition(mc.thePlayer.posX, mc.thePlayer.posY + 0.00000000001, mc.thePlayer.posZ, true));
                		mc.getNetHandler().sendPacketNoEvent(new C04PacketPlayerPosition(mc.thePlayer.posX, mc.thePlayer.posY - 0.0000000001, mc.thePlayer.posZ, false));
                		
                		break;
                		
                	case "Bypass":

                		mc.getNetHandler().sendPacketNoEvent(new C04PacketPlayerPosition(mc.thePlayer.posX, mc.thePlayer.posY + 0.00000000001, mc.thePlayer.posZ, true));
                		mc.getNetHandler().sendPacketNoEvent(new C04PacketPlayerPosition(mc.thePlayer.posX, mc.thePlayer.posY, mc.thePlayer.posZ, false));
                		
                		break;
                		
                	case "NanoPacket":
                		
                		mc.getNetHandler().sendPacketNoEvent(new C04PacketPlayerPosition(mc.thePlayer.posX, mc.thePlayer.posY - 1e-20, mc.thePlayer.posZ, false));
                		
                		break;
                	
                	}
                }
			}
		}
	}

}
