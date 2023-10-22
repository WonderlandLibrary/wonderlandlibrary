package epsilon.modules.dev;

import org.lwjgl.input.Keyboard;

import epsilon.events.Event;
import epsilon.events.listeners.packet.EventReceivePacket;
import epsilon.events.listeners.packet.EventSendPacket;
import epsilon.modules.Module;
import epsilon.settings.setting.BooleanSetting;
import net.minecraft.network.Packet;
import net.minecraft.network.play.client.*;

public class PacketCanceller extends Module {

	
	//Client
	public BooleanSetting C00 = new BooleanSetting ("C00KeepAlive", false);
	public BooleanSetting C0F = new BooleanSetting ("C0FConfirmTransaction", false);
	public BooleanSetting C03 = new BooleanSetting ("C03PacketPlayer", false);
	public BooleanSetting C05 = new BooleanSetting ("C05Rotation", false);
	public BooleanSetting C07 = new BooleanSetting ("C07Digging", false);
	public BooleanSetting C08 = new BooleanSetting ("C08BlockPlacement", false);
	public BooleanSetting C09 = new BooleanSetting ("C09ItemChange", false);
	public BooleanSetting C0A = new BooleanSetting ("C0AAnimation", false);
	public BooleanSetting C0B = new BooleanSetting ("C0BEntityAction", false);
	public BooleanSetting C0C = new BooleanSetting ("C0CInput", false);
	public BooleanSetting C0D = new BooleanSetting ("C0DCloseWindow", false);
	public BooleanSetting C0E = new BooleanSetting ("C0EClickWindow", false);
	/*public BooleanSetting C10 = new BooleanSetting ("C10CreativeInvAction", false);
	public BooleanSetting C11 = new BooleanSetting ("C11Enchant", false);
	public BooleanSetting C12 = new BooleanSetting ("C12Sign", false); do we really need these*/ 
	public BooleanSetting C13 = new BooleanSetting ("C13Abilities", false);
	public BooleanSetting C15 = new BooleanSetting ("C15ClientSettings", false);
	public BooleanSetting C16 = new BooleanSetting ("C16Status", false);
	public BooleanSetting C18 = new BooleanSetting ("C18Spectate", false);
	public BooleanSetting C19 = new BooleanSetting ("C19ResourcePack", false);
	
	
	
	//Server
	public BooleanSetting S00 = new BooleanSetting ("S00KeepAlive", false);
	public BooleanSetting S08 = new BooleanSetting ("S08PositionAndRotation", false);
	public BooleanSetting S2F = new BooleanSetting ("S2FSetSlot", false);
	public BooleanSetting S2E = new BooleanSetting ("S2ECloseWindow", false);
	public BooleanSetting S32 = new BooleanSetting ("S32ConfirmTransaction", false);
	public BooleanSetting S40 = new BooleanSetting ("S40BanPacket", false); // :trole:
	public BooleanSetting S48 = new BooleanSetting ("S48ResourcePack", false);
	
	
	public PacketCanceller() {
		super("PacketCanceller", Keyboard.KEY_NONE, Category.DEV, "Allows you to choose what packets to cancel");
		this.addSettings(C00,C0F,C03,C05,C07,C08,C09,C0A,C0B,C0C,C0D,C0E,C13,C15,C16,C18,C19,
		S00,S08,S2F,S32,S32,S40,S40
				
				);
	}
	
	public void onEvent(Event e) {
		
		if(e instanceof EventSendPacket) {
			
			Packet p = e.getPacket();
			
			if(C00.isEnabled() && p instanceof C00PacketKeepAlive) e.setCancelled();
			if(C0F.isEnabled() && p instanceof C0FPacketConfirmTransaction) e.setCancelled();
			if(C03.isEnabled() && p instanceof C03PacketPlayer) e.setCancelled();
			if(C05.isEnabled() && p instanceof C03PacketPlayer.C05PacketPlayerLook) e.setCancelled();
			if(C07.isEnabled() && p instanceof C07PacketPlayerDigging) e.setCancelled();
			if(C08.isEnabled() && p instanceof C08PacketPlayerBlockPlacement) e.setCancelled();
			if(C0A.isEnabled() && p instanceof C0APacketAnimation) e.setCancelled();
			if(C0B.isEnabled() && p instanceof C0BPacketEntityAction) e.setCancelled();
			if(C0C.isEnabled() && p instanceof C0CPacketInput) e.setCancelled();
			if(C0D.isEnabled() && p instanceof C0DPacketCloseWindow) e.setCancelled();
			if(C0E.isEnabled() && p instanceof C0EPacketClickWindow) e.setCancelled();
			if(C13.isEnabled() && p instanceof C13PacketPlayerAbilities) e.setCancelled();
			if(C15.isEnabled() && p instanceof C15PacketClientSettings) e.setCancelled();
			if(C16.isEnabled() && p instanceof C16PacketClientStatus) e.setCancelled();
			if(C18.isEnabled() && p instanceof C18PacketSpectate) e.setCancelled();
			if(C19.isEnabled() && p instanceof C19PacketResourcePackStatus) e.setCancelled();
			
		}
		if(e instanceof EventReceivePacket) {
			
			Packet p = e.getPacket();
			
		}
		
	}

}
