package tech.atani.client.feature.module.impl.misc;

import cn.muyang.nativeobfuscator.Native;
import com.google.common.base.Supplier;
import net.minecraft.network.Packet;
import net.minecraft.network.play.client.*;
import net.minecraft.network.play.server.S05PacketSpawnPosition;
import net.minecraft.network.play.server.S07PacketRespawn;
import net.minecraft.network.play.server.S2BPacketChangeGameState;
import tech.atani.client.feature.value.impl.StringBoxValue;
import tech.atani.client.listener.event.minecraft.network.PacketEvent;
import tech.atani.client.listener.event.minecraft.player.movement.UpdateMotionEvent;
import tech.atani.client.listener.radbus.Listen;
import tech.atani.client.feature.module.Module;
import tech.atani.client.feature.module.data.ModuleData;
import tech.atani.client.feature.module.data.enums.Category;
import tech.atani.client.utility.interfaces.Methods;
import tech.atani.client.feature.value.impl.CheckBoxValue;

@Native
@ModuleData(name = "Disabler", description = "Disable anti cheats", category = Category.MISCELLANEOUS)
public class Disabler extends Module {
	private final StringBoxValue mode = new StringBoxValue("Mode", "Which mode will the disabler use?", this, new String[] {"Custom", "Verus Combat", "Intave Timer", "Omni Sprint"});

	private final CheckBoxValue keepAlive = new CheckBoxValue("C00KeepAlive", "Should the module cancel C00KeepAlive?", this, false, new Supplier[]{() -> mode.is("Custom")}),
			c0fConfirm = new CheckBoxValue("C0FConfirmTransaction", "Should the module cancel C0FConfirmTransaction?", this, false, new Supplier[]{() -> mode.is("Custom")}),
			s2BChangeGameState = new CheckBoxValue("S2BChangeGameState", "Should the module cancel S2BChangeGameState?", this, false, new Supplier[]{() -> mode.is("Custom")}),
			s07Respawn = new CheckBoxValue("S07Respawn", "Should the module cancel S07Respawn?", this, false, new Supplier[]{() -> mode.is("Custom")}),
			c0bEntityAction = new CheckBoxValue("C0BEntityAction", "Should the module cancel C0BEntityAction?", this, false, new Supplier[]{() -> mode.is("Custom")}),
			s05SpawnPosition = new CheckBoxValue("S05SpawnPosition", "Should the module cancel S05SpawnPosition?", this, false, new Supplier[]{() -> mode.is("Custom")}),
			c0cInput = new CheckBoxValue("C0CInput", "Should the module cancel C0CInput?", this, false, new Supplier[]{() -> mode.is("Custom")}),
			c13PlayerAbilities = new CheckBoxValue("C13PlayerAbilities", "Should the module cancel C13PlayerAbilities?", this, false, new Supplier[]{() -> mode.is("Custom")});

	// Verus Combat
	private int verusCounter;

	@Override
	public String getSuffix() {
		return mode.getValue();
	}

	@Listen
	public void onPacketEvent(PacketEvent event) {
		if (Methods.mc.thePlayer == null || Methods.mc.theWorld == null)
			return;

		if (event.getType() == PacketEvent.Type.OUTGOING) {
			Packet<?> packet = event.getPacket();
			switch(mode.getValue()) {
				case "Custom":
					if (packet instanceof C00PacketKeepAlive && keepAlive.getValue()) {
						event.setCancelled(true);
					}

					if (packet instanceof C0FPacketConfirmTransaction && c0fConfirm.getValue()) {
						event.setCancelled(true);
					}

					if (packet instanceof S2BPacketChangeGameState && s2BChangeGameState.getValue()) {
						event.setCancelled(true);
					}

					if (packet instanceof S07PacketRespawn && s07Respawn.getValue()) {
						event.setCancelled(true);
					}

					if (packet instanceof C0BPacketEntityAction && c0bEntityAction.getValue()) {
						event.setCancelled(true);
					}

					if (packet instanceof S05PacketSpawnPosition && s05SpawnPosition.getValue()) {
						event.setCancelled(true);
					}

					if (packet instanceof C0CPacketInput && c0cInput.getValue()) {
						event.setCancelled(true);
					}

					if (packet instanceof C13PacketPlayerAbilities && c13PlayerAbilities.getValue()) {
						event.setCancelled(true);
					}
					break;
				case "Verus Combat":
					if(packet instanceof C0FPacketConfirmTransaction) {
						if(mc.thePlayer.isDead) {
							verusCounter = 0;
						}

						if(verusCounter != 0) {
							event.setCancelled(true);
						}

						verusCounter++;
					} else if(packet instanceof C0BPacketEntityAction) {
						event.setCancelled(true);
					}
					break;
				case "Intave Timer":
					if(packet instanceof C19PacketResourcePackStatus) {
						event.setCancelled(true);
					}
					break;
				case "Omni Sprint":
					if(packet instanceof C03PacketPlayer.C06PacketPlayerPosLook) {
						if(mc.thePlayer.moveForward < 0)
						((C03PacketPlayer.C06PacketPlayerPosLook) packet).setYaw(mc.thePlayer.rotationYaw + 180);
					}
					break;
			}
		}
	}

	@Listen
	public void onMotion(UpdateMotionEvent event) {
		switch (mode.getValue()) {
			case "Intave Timer":
				StringBuilder builder = new StringBuilder();
				for (int i = 32; i < 256; i++) builder.append((char)i);
				this.sendPacketUnlogged(new C19PacketResourcePackStatus(builder.toString(), C19PacketResourcePackStatus.Action.ACCEPTED));
				this.sendPacketUnlogged(new C19PacketResourcePackStatus(builder.toString(), C19PacketResourcePackStatus.Action.FAILED_DOWNLOAD));
				break;
		}
	}

	@Override
	public void onEnable() {
		verusCounter = 0;
	}

	@Override
	public void onDisable() {
		verusCounter = 0;
	}

}