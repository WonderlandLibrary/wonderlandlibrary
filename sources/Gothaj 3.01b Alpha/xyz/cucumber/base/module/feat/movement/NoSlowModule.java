package xyz.cucumber.base.module.feat.movement;

import java.util.ArrayList;

import org.lwjgl.input.Keyboard;

import net.minecraft.network.Packet;
import net.minecraft.network.play.client.C02PacketUseEntity;
import net.minecraft.network.play.client.C02PacketUseEntity.Action;
import net.minecraft.network.play.client.C03PacketPlayer;
import net.minecraft.network.play.client.C07PacketPlayerDigging;
import net.minecraft.network.play.client.C08PacketPlayerBlockPlacement;
import net.minecraft.network.play.client.C09PacketHeldItemChange;
import net.minecraft.network.play.client.C0APacketAnimation;
import net.minecraft.network.play.client.C0DPacketCloseWindow;
import net.minecraft.network.play.client.C16PacketClientStatus;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import xyz.cucumber.base.Client;
import xyz.cucumber.base.events.EventListener;
import xyz.cucumber.base.events.EventType;
import xyz.cucumber.base.events.ext.EventMotion;
import xyz.cucumber.base.events.ext.EventMove;
import xyz.cucumber.base.events.ext.EventNoSlow;
import xyz.cucumber.base.events.ext.EventSendPacket;
import xyz.cucumber.base.module.Category;
import xyz.cucumber.base.module.Mod;
import xyz.cucumber.base.module.ModuleInfo;
import xyz.cucumber.base.module.feat.combat.KillAuraModule;
import xyz.cucumber.base.module.settings.ModeSettings;
import xyz.cucumber.base.module.settings.NumberSettings;
import xyz.cucumber.base.utils.MovementUtils;
import xyz.cucumber.base.utils.Timer;
import xyz.cucumber.base.utils.math.RotationUtils;

@ModuleInfo(category = Category.MOVEMENT, description = "Allows you to use items while running", name = "No Slow", key = Keyboard.KEY_NONE)
public class NoSlowModule extends Mod {

	public ArrayList<Packet> packets = new ArrayList<>();
	
	public ModeSettings mode = new ModeSettings("Mode",
			new String[] { "Vanilla", "Intave spoof","Intave new", "Intave speed", "AAC", "AAC5", "Hypixel", "Switch" });
	
	public NumberSettings intaveDelay = new NumberSettings("Intave delay", 2, 1, 10, 1);
	public NumberSettings intaveSpeed = new NumberSettings("Intave speed", 0.25, 0.01, 0.275, 0.01);

	public boolean attack, swing;

	public Timer timer = new Timer();

	public NoSlowModule() {
		this.addSettings(mode, intaveDelay, intaveSpeed);
	}

	@EventListener
	public void onNoSlow(EventNoSlow e) {
		if(mode.getMode().equalsIgnoreCase("Intave speed")) {
			mc.thePlayer.setSprinting(false);
			if(mc.thePlayer.ticksExisted % intaveDelay.getValue() != 0) {
				e.setCancelled(true);
			}
		}else {
			e.setCancelled(true);
		}
	}

	@EventListener
	public void onMotion(EventMotion e) {
		setInfo(mode.getMode());
		
		switch (mode.getMode().toLowerCase()) {
		case "vanilla":
			break;
		case "sg":
			
			if (MovementUtils.isMoving()
					&& (mc.thePlayer.isUsingItem() || mc.thePlayer.isEating() || mc.thePlayer.isBlocking())) {
				if (e.getType() == EventType.PRE) {
					 mc.thePlayer.sendQueue.addToSendQueue(new C07PacketPlayerDigging(C07PacketPlayerDigging.Action.RELEASE_USE_ITEM,BlockPos.ORIGIN, EnumFacing.DOWN));	
				}else {
					 mc.thePlayer.sendQueue.addToSendQueue(new C08PacketPlayerBlockPlacement(BlockPos.ORIGIN, 255, mc.thePlayer.getHeldItem(), 0, 0, 0));
				}
			}
			break;
		case "intave new":
			int slot = mc.thePlayer.inventory.currentItem;
			if (MovementUtils.isMoving()
					&& (mc.thePlayer.isUsingItem() || mc.thePlayer.isEating() || mc.thePlayer.isBlocking())) {
				if (e.getType() == EventType.PRE) {
					mc.getNetHandler().getNetworkManager()
					.sendPacketNoEvent(new C0DPacketCloseWindow(mc.thePlayer.openContainer.windowId));
				}else {

				}
			}
			break;
		case "intave spoof":
			if (e.getType() == EventType.PRE) {
				if (MovementUtils.isMoving()
						&& (mc.thePlayer.isUsingItem() || mc.thePlayer.isEating() || mc.thePlayer.isBlocking())) {
					
					KillAuraModule ka = (KillAuraModule) Client.INSTANCE.getModuleManager().getModule(KillAuraModule.class);
					
					mc.getNetHandler().getNetworkManager().sendPacketNoEvent(
							new C16PacketClientStatus(C16PacketClientStatus.EnumState.OPEN_INVENTORY_ACHIEVEMENT));
					mc.getNetHandler().getNetworkManager()
							.sendPacketNoEvent(new C0DPacketCloseWindow(mc.thePlayer.openContainer.windowId));
				}
			}
			break;
		case "switch":
			if (MovementUtils.isMoving()
					&& (mc.thePlayer.isUsingItem() || mc.thePlayer.isEating() || mc.thePlayer.isBlocking())) {
				if (e.getType() == EventType.PRE) {
					mc.getNetHandler().getNetworkManager().sendPacketNoEvent(new C09PacketHeldItemChange((mc.thePlayer.inventory.currentItem + 1) % 9));
					mc.getNetHandler().getNetworkManager().sendPacketNoEvent(new C09PacketHeldItemChange(mc.thePlayer.inventory.currentItem));
				}
			}
			break;
		case "hypixel":
			if (MovementUtils.isMoving()
					&& (mc.thePlayer.isUsingItem() || mc.thePlayer.isEating() || mc.thePlayer.isBlocking())) {
				if (e.getType() == EventType.PRE) {
                    mc.getNetHandler().getNetworkManager().sendPacketNoEvent(new C09PacketHeldItemChange((mc.thePlayer.inventory.currentItem + 1) % 9));
					mc.getNetHandler().getNetworkManager().sendPacketNoEvent(new C09PacketHeldItemChange(mc.thePlayer.inventory.currentItem));
				}
			}
			break;
		case "aac":
			if (MovementUtils.isMoving() && (mc.thePlayer.isUsingItem() || mc.gameSettings.keyBindUseItem.pressed
					|| mc.thePlayer.isEating() || mc.thePlayer.isBlocking())) {
				if (e.getType() == EventType.PRE) {
					mc.getNetHandler().getNetworkManager()
							.sendPacketNoEvent(new C09PacketHeldItemChange(mc.thePlayer.inventory.currentItem));
				}
			}
			break;
		case "aac5":
			if (e.getType() == EventType.POST) {
				if (MovementUtils.isMoving() && mc.thePlayer.isUsingItem() || mc.gameSettings.keyBindUseItem.pressed
						|| mc.thePlayer.isEating() || mc.thePlayer.isBlocking()) {
					mc.getNetHandler().getNetworkManager()
							.sendPacketNoEvent((Packet) new C08PacketPlayerBlockPlacement(new BlockPos(-1, -1, -1), 255,
									mc.thePlayer.inventory.getCurrentItem(), 0f, 0f, 0f));
				}
			}
			break;
		}
	}

	@EventListener
	public void onSendPacket(EventSendPacket e) {
		switch (mode.getMode().toLowerCase()) {
		case "aac":
			if (e.getPacket() instanceof C02PacketUseEntity) {
				C02PacketUseEntity packet = (C02PacketUseEntity) e.getPacket();

				if (packet.getAction() == Action.ATTACK) {
					if (MovementUtils.isMoving() && mc.thePlayer.isUsingItem() || mc.gameSettings.keyBindUseItem.pressed
							|| mc.thePlayer.isEating() || mc.thePlayer.isBlocking()) {
						mc.getNetHandler().getNetworkManager().sendPacketNoEvent(new C08PacketPlayerBlockPlacement(
								new BlockPos(-1, -1, -1), 255, mc.thePlayer.inventory.getCurrentItem(), 0f, 0f, 0f));
					}
				}
			}
			break;
		
		}
	}
}
