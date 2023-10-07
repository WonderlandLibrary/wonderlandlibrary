package vestige.impl.module.player;

import java.util.ArrayList;

import net.minecraft.network.Packet;
import net.minecraft.network.play.client.C03PacketPlayer;
import vestige.api.event.Listener;
import vestige.api.event.impl.MotionEvent;
import vestige.api.event.impl.PacketSendEvent;
import vestige.api.module.Category;
import vestige.api.module.Module;
import vestige.api.module.ModuleInfo;
import vestige.api.setting.impl.ModeSetting;
import vestige.util.network.PacketUtil;

@ModuleInfo(name = "NoFall", category = Category.PLAYER)
public class Nofall extends Module {

    public ModeSetting mode = new ModeSetting("Mode", this, "Spoof", "Spoof", "Redesky", "NoGround", "Packet", "Bukkit MoveEvent");
    private double fallDist;
    
    private int timesSpoofedGround;
    
    private final ArrayList<Packet> packets = new ArrayList<>();
    
    public Nofall() {
        this.registerSettings(mode);
    }
    
    public void onDisable() {
    	timesSpoofedGround = 0;
    }
    
    private void sendPackets() {
		if(!packets.isEmpty()) {
			for(Packet p : packets) {
				PacketUtil.sendPacketNoEvent(p);
			}
			packets.clear();
		}
	}

    @Listener
    public void onMotion(MotionEvent e) {
        setSuffix(mode.getMode());
        
        if(mc.thePlayer.onGround) {
        	timesSpoofedGround = 0;
        }
        
        if(mc.thePlayer.motionY < 0) {
            fallDist += mc.thePlayer.fallDistance - fallDist;
        }
        
        switch (mode.getMode()) {
            case "Packet":
                if(fallDist >= 3) {
                    PacketUtil.sendPacketNoEvent(new C03PacketPlayer(true));
                    fallDist = 0;
                }
                break;
            case "Bukkit MoveEvent":
            	/*
            	 * Bukkit move event is only called when you move (and not every tick), 
            	 * so c03s can be used for nofall. However, the ground state is delayed by one tick because the bukkit player onground
            	 * statement gets updated after the move event gets called, if it does. So that's why another c03 with false onground value is sent.
            	 * */
            	if(fallDist >= 2) {
            		PacketUtil.sendPacketNoEvent(new C03PacketPlayer(true));
                    PacketUtil.sendPacketNoEvent(new C03PacketPlayer(false));
                    fallDist = 0;
                }
            	break;
            case "Spoof":
                if(fallDist >= 3) {
                    e.setOnGround(true);
                    fallDist = 0;
                }
                break;
            case "Redesky":
            case "Hypixel":
            	if(fallDist >= 3 && timesSpoofedGround <= 6) {
            		e.setOnGround(true);
            		fallDist = 0;
            		timesSpoofedGround++;
            	}
            	break;
            case "NoGround":
            	e.setOnGround(false);
            	break;
        }
    }
    
    @Listener
    public void onSend(PacketSendEvent e) {
    	if(e.getPacket() instanceof C03PacketPlayer) {
    		C03PacketPlayer packet = (C03PacketPlayer) e.getPacket();
    		
    		if(mode.is("Hypixel") && ((fallDist < 1 && fallDist > 0) || (fallDist > 2 && timesSpoofedGround <= 6))) {
    			e.setCancelled(true);
    			packets.add(e.getPacket());
    		} else {
    			this.sendPackets();
    		}
    		
    		if(mode.is("NoGround")) {
    			packet.onGround = false;
    		}
    	}
    }

}
