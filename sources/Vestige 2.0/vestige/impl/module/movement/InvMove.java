package vestige.impl.module.movement;

import java.util.ArrayList;

import org.lwjgl.input.Keyboard;

import net.minecraft.client.gui.inventory.GuiChest;
import net.minecraft.client.gui.inventory.GuiInventory;
import net.minecraft.client.settings.GameSettings;
import net.minecraft.client.settings.KeyBinding;
import net.minecraft.network.Packet;
import net.minecraft.network.play.client.C03PacketPlayer;
import net.minecraft.network.play.client.C0BPacketEntityAction;
import net.minecraft.network.play.client.C0BPacketEntityAction.Action;
import vestige.Vestige;
import vestige.api.event.Listener;
import vestige.api.event.impl.MotionEvent;
import vestige.api.event.impl.MoveEvent;
import vestige.api.event.impl.PacketSendEvent;
import vestige.api.event.impl.RenderEvent;
import vestige.api.event.impl.UpdateEvent;
import vestige.api.module.Category;
import vestige.api.module.Module;
import vestige.api.module.ModuleInfo;
import vestige.api.setting.impl.BooleanSetting;
import vestige.api.setting.impl.ModeSetting;
import vestige.impl.module.player.Nofall;
import vestige.util.misc.TimerUtil;
import vestige.util.network.PacketUtil;
import vestige.util.network.ServerUtils;

@ModuleInfo(name = "InvMove", category = Category.MOVEMENT)
public class InvMove extends Module {
	
	private final ModeSetting noSprint = new ModeSetting("No Sprint", this, "Disabled", "Disabled", "Enabled", "Spoof");
	private final BooleanSetting blink = new BooleanSetting("Blink", this, false);
	private final BooleanSetting allowRotating = new BooleanSetting("Allow Rotating", this, false);
	
	private final TimerUtil timer = new TimerUtil();
	
	private final ArrayList<Packet> packets = new ArrayList<>();
	
	private boolean wasSprinting, invOpened;
	
	public InvMove() {
		this.registerSettings(noSprint, blink, allowRotating);
	}
	
	public void onEnable() {
		wasSprinting = false;
		invOpened = mc.currentScreen instanceof GuiInventory || mc.currentScreen instanceof GuiChest;
		timer.reset();
	}
	
	public void onDisable() {
		this.sendPackets();
		
		boolean currentlyOpened = mc.currentScreen instanceof GuiInventory || mc.currentScreen instanceof GuiChest;
		
		if(noSprint.is("Spoof")) {
			if(invOpened && !currentlyOpened) {
				if(wasSprinting) {
					PacketUtil.startSprinting();
				}
			} else if(!invOpened && currentlyOpened) {
				if(wasSprinting) {
					PacketUtil.stopSprinting();
				}
			}
		}
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
	public void onRender(RenderEvent event) {
		if(mc.currentScreen instanceof GuiInventory || mc.currentScreen instanceof GuiChest) {
			this.allowMove();
			
			if(allowRotating.isEnabled()) {
				final double speed = 0.15F;
				
				if(Keyboard.isKeyDown(Keyboard.KEY_RIGHT)) mc.thePlayer.rotationYaw += speed * timer.getTimeElapsed();
				if(Keyboard.isKeyDown(Keyboard.KEY_LEFT)) mc.thePlayer.rotationYaw -= speed * timer.getTimeElapsed();
				if(Keyboard.isKeyDown(Keyboard.KEY_UP)) mc.thePlayer.rotationPitch -= speed * timer.getTimeElapsed();
				if(Keyboard.isKeyDown(Keyboard.KEY_DOWN)) mc.thePlayer.rotationPitch += speed * timer.getTimeElapsed();
				
				if(mc.thePlayer.rotationPitch > 90) {
					mc.thePlayer.rotationPitch = 90;
				} else if(mc.thePlayer.rotationPitch < -90) {
					mc.thePlayer.rotationPitch = -90;
				}
			}
		}
		
		timer.reset();
	}
	
	@Listener
	public void onUpdate(UpdateEvent event) {
		if(mc.currentScreen instanceof GuiInventory || mc.currentScreen instanceof GuiChest) {
			if(noSprint.is("Enabled")) {
				mc.thePlayer.setSprinting(false);
			}
			
			this.allowMove();
		}
	}
 	
	@Listener
	public void onMove(MoveEvent event) {
		if(mc.currentScreen instanceof GuiInventory || mc.currentScreen instanceof GuiChest) {
			this.allowMove();
		} else {
			wasSprinting = mc.thePlayer.isSprinting();
		}
	}
	
	@Listener
	public void onMotion(MotionEvent event) {
		if(mc.currentScreen instanceof GuiInventory || mc.currentScreen instanceof GuiChest) {
			this.allowMove();
		}
		
		boolean currentlyOpened = mc.currentScreen instanceof GuiInventory || mc.currentScreen instanceof GuiChest;
		
		if(noSprint.is("Spoof")) {
			if(invOpened && !currentlyOpened) {
				if(wasSprinting) {
					PacketUtil.startSprinting();
				}
			} else if(!invOpened && currentlyOpened) {
				if(wasSprinting) {
					PacketUtil.stopSprinting();
				}
			}
		}
		
		invOpened = mc.currentScreen instanceof GuiInventory || mc.currentScreen instanceof GuiChest;
	}
	
	@Listener
	public void onSend(PacketSendEvent event) {
		if(event.getPacket() instanceof C03PacketPlayer) {
			if((mc.currentScreen instanceof GuiInventory || mc.currentScreen instanceof GuiChest) 
					&& blink.isEnabled()
					&& !(mc.thePlayer.fallDistance > 1 && Vestige.getInstance().getModuleManager().getModule(Nofall.class).isEnabled())) {
				event.setCancelled(true);
				packets.add(event.getPacket());
			} else {
				this.sendPackets();
			}
		} else if(event.getPacket() instanceof C0BPacketEntityAction) {
			C0BPacketEntityAction packet = (C0BPacketEntityAction) event.getPacket();
			if(packet.getAction() == Action.START_SPRINTING || packet.getAction() == Action.STOP_SPRINTING) {
				if(noSprint.is("Spoof")) {
					event.setCancelled(true);
				}
			}
		}
	}
	
	public void allowMove() {
		GameSettings gs = mc.gameSettings;
		KeyBinding keys[] = {gs.keyBindForward, gs.keyBindBack, gs.keyBindLeft, gs.keyBindRight, gs.keyBindJump};
		
		for(KeyBinding key : keys) {
			key.pressed = Keyboard.isKeyDown(key.getKeyCode());
		}
	}
	
}
