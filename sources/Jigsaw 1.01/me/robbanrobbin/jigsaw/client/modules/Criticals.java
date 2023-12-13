package me.robbanrobbin.jigsaw.client.modules;

import org.lwjgl.input.Keyboard;

import me.robbanrobbin.jigsaw.client.events.PacketEvent;
import me.robbanrobbin.jigsaw.client.main.Jigsaw;
import me.robbanrobbin.jigsaw.client.module.state.Category;
import me.robbanrobbin.jigsaw.module.Module;
import net.minecraft.entity.Entity;
import net.minecraft.network.Packet;
import net.minecraft.network.play.client.CPacketPlayer;
import net.minecraft.network.play.client.CPacketUseEntity;

public class Criticals extends Module {

	int slotBefore;
	int bestSlot;
	float eating;
	public static boolean disable;

	public Criticals() {
		super("Criticals", Keyboard.KEY_NONE, Category.COMBAT, "Tries to critical every hit.");
	}

	@Override
	public void onPacketSent(PacketEvent event) {
		Packet packet = event.getPacket();
		if (disable) {
			return;
		}

		if (packet instanceof CPacketUseEntity
				&& ((CPacketUseEntity) packet).getAction() == CPacketUseEntity.Action.ATTACK) {
			critical(((CPacketUseEntity) packet).getEntityFromWorld(mc.world));
		}

		super.onPacketSent(event);
	}

	public static void critical(Entity en) {
		if (!mc.player.onGround) {
			return;
		}
		if ((Jigsaw.getModuleByName("Flight").isToggled()
				&& Jigsaw.getModuleByName("Flight").getCurrentMode().equals("Hypixel"))
				&& !mc.player.isCollidedVertically) {
			return;
		}

		double x = mc.player.posX;
		double y = mc.player.posY;
		double z = mc.player.posZ;
		mc.getConnection().getNetworkManager().sendPacketFinal(new CPacketPlayer.Position(x, y + 0.05, z, false));
		mc.getConnection().getNetworkManager().sendPacketFinal(new CPacketPlayer.Position(x, y, z, false));
		mc.getConnection().getNetworkManager().sendPacketFinal(new CPacketPlayer.Position(x, y + 0.015, z, false));
		mc.getConnection().getNetworkManager().sendPacketFinal(new CPacketPlayer.Position(x, y, z, false));
	}

	public static void crit(double xx, double yy, double zz) {

		if (!Jigsaw.getModuleByName("Criticals").isToggled()) {
			return;
		}
		if (!mc.player.onGround) {
			return;
		}
		double x = xx;
		double y = yy;
		double z = zz;

		mc.getConnection().getNetworkManager().sendPacketFinal(new CPacketPlayer.Position(x, y + 0.05, z, false));
		mc.getConnection().getNetworkManager().sendPacketFinal(new CPacketPlayer.Position(x, y, z, false));
		mc.getConnection().getNetworkManager().sendPacketFinal(new CPacketPlayer.Position(x, y + 0.015, z, false));
		mc.getConnection().getNetworkManager().sendPacketFinal(new CPacketPlayer.Position(x, y, z, false));
	}

}
