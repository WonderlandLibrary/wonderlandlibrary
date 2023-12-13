package me.robbanrobbin.jigsaw.client.modules;

import java.util.ArrayList;

import org.lwjgl.input.Keyboard;

import me.robbanrobbin.jigsaw.client.module.state.Category;
import me.robbanrobbin.jigsaw.client.tools.Utils;
import me.robbanrobbin.jigsaw.module.Module;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.network.play.client.CPacketUseEntity;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.Vec3d;

public class AutoStacker extends Module {

	public AutoStacker() {
		super("AutoStacker", Keyboard.KEY_NONE, Category.FUN,
				"Automatically stacks" + " close players in the Mineplex lobby.");
	}

	@Override
	public void onDisable() {

		super.onDisable();
	}

	@Override
	public void onEnable() {

		super.onEnable();
	}

	@Override
	public void onUpdate() {

		ArrayList<EntityLivingBase> ens = Utils.getClosestEntities(7f);
		for (EntityLivingBase en : ens) {
			if (en.isRiding()) {
				continue;
			}
			if (!(en instanceof EntityPlayer)) {
				continue;
			}
			Vec3d vec3 = new Vec3d(en.posX, en.posY, en.posZ);
			sendPacket(new CPacketUseEntity(en, EnumHand.OFF_HAND, vec3));
			if (this.currentMode.equals("Throw")) {
				mc.player.swingArm(EnumHand.MAIN_HAND);
				sendPacket(new CPacketUseEntity(en));
			}

		}
		super.onUpdate();
	}

	@Override
	public String[] getModes() {
		return new String[] { "Glue", "Throw" };
	}

}
