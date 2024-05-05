package epsilon.modules.dev;

import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedDeque;

import org.lwjgl.input.Keyboard;

import epsilon.Epsilon;
import epsilon.events.Event;
import epsilon.events.listeners.EventUpdate;
import epsilon.events.listeners.packet.EventReceivePacket;
import epsilon.modules.Module;
import epsilon.settings.setting.BooleanSetting;
import epsilon.settings.setting.NumberSetting;
import epsilon.util.NewMath;
import epsilon.util.Timer;
import net.minecraft.network.INetHandler;
import net.minecraft.network.Packet;
import net.minecraft.network.play.server.S08PacketPlayerPosLook;
import net.minecraft.network.play.server.S0EPacketSpawnObject;
import net.minecraft.network.play.server.S21PacketChunkData;
import net.minecraft.network.play.server.S22PacketMultiBlockChange;
import net.minecraft.network.play.server.S23PacketBlockChange;
import net.minecraft.network.play.server.S27PacketExplosion;
import net.minecraft.network.play.server.S2APacketParticles;
import net.minecraft.network.play.server.S38PacketPlayerListItem;
import net.minecraft.network.play.server.S3CPacketUpdateScore;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.IChatComponent;
import net.minecraft.network.play.INetHandlerPlayClient;

public class AntiLag extends Module {

	public NumberSetting arrowlimit = new NumberSetting ("Arrow Limit", 20, 1, 100, 1);
	public NumberSetting entityupdatelimit = new NumberSetting ("Entity Update Limit", 100, 1, 1000, 1);
	
	public BooleanSetting arrow = new BooleanSetting ("Arrow Limit", true);
	public BooleanSetting update = new BooleanSetting ("Entity Update", true);
	public BooleanSetting nostill = new BooleanSetting ("Dont update still entities", true);
	public BooleanSetting hycrash = new BooleanSetting ("Anti Hycrash", true);
	
	private int bMultiUpdateLimit, bUpdateLimit, ticks, particleLimit, explosionLimit, S3CLimit, S38Limit, s12Limit;
	
	private final Timer timerM = new Timer();

	private final Timer timerS = new Timer();
	private final Queue<S22PacketMultiBlockChange> blockQueue = new ConcurrentLinkedDeque<>();

	private final Queue<S23PacketBlockChange> blockQueueSingular = new ConcurrentLinkedDeque<>();
	
	public AntiLag() {
		super("AntiLag", Keyboard.KEY_NONE, Category.DEV, "Uses various methods to reduce lag on large servers like hypixel.");
		this.addSettings(hycrash, arrow, arrowlimit, update, entityupdatelimit, nostill);
	}

	public void onEnable() {
		bUpdateLimit = 500;
		bMultiUpdateLimit = 50;
		ticks = particleLimit = explosionLimit = S3CLimit = S38Limit = s12Limit = 0;
	}
	
	
	public void onEvent(Event e) {
		if(mc.getNetHandler()!=null) {
			if(e instanceof EventReceivePacket) {
				if(hycrash.isEnabled()) {
					boolean crashDetected = false;
					
					final Packet p = e.getPacket();
		    		
		    		if(p instanceof S2APacketParticles) {
		    			particleLimit++;
		    			if(particleLimit>5 && explosionLimit>2) {
		    				e.setCancelled();
		    				Epsilon.addChatMessage("Crash attempt detected");
		    				crashDetected = true;
		    				final IChatComponent message = new ChatComponentText("Disconnected to prevent contagious faggotism");
		    				mc.getNetHandler().onDisconnect(message);
		    				
		    			}
		    		}
		    		if(p instanceof S27PacketExplosion) {
		    			explosionLimit++;
		    			if(explosionLimit>3)
		    				e.setCancelled();
		    		}
		    		if (p instanceof S38PacketPlayerListItem) {
		    			S38Limit++;
		    			if(S38Limit>1) {
		    				e.setCancelled();
		    			}
		    		}
		    		if (p instanceof S3CPacketUpdateScore) {
		    			S3CLimit++;
		    			if(S3CLimit>20) {
		    				e.setCancelled();
		    			}
		    		}
		    		if (p instanceof S21PacketChunkData && mc.getNetHandler().doneLoadingTerrain && mc.thePlayer.ticksExisted>15) {
		    			s12Limit++;
		    			if(s12Limit>21) {
		    				e.setCancelled();
		    			}
		    			
		    		}
		    		
		    		//if(crashDetected) e.setCancelled();
		    		
				}
			}
	    		
		}
		
		if(e instanceof EventUpdate) {
			ticks++;
			if(ticks>20) {
				ticks = particleLimit = explosionLimit = S3CLimit = S38Limit = s12Limit = 0;
			}
		}
		
		
	}
	
}
