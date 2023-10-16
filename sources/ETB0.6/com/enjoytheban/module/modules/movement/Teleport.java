package com.enjoytheban.module.modules.movement;

import java.awt.Color;

import org.lwjgl.input.Mouse;

import com.enjoytheban.api.EventHandler;
import com.enjoytheban.api.events.world.EventPreUpdate;
import com.enjoytheban.module.Module;
import com.enjoytheban.module.ModuleType;

import net.minecraft.network.play.client.C03PacketPlayer;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.util.Vec3;

/*
 * Created by Jutting on Oct 10, 2018
 */

public class Teleport extends Module {

	public Teleport() {
		super("Teleport", new String[] { "teleport" }, ModuleType.Movement);
		setColor(new Color(random.nextInt(255), random.nextInt(255), random.nextInt(255)).getRGB());
	}

	@EventHandler
	private void onUpdate(EventPreUpdate event) {
		final MovingObjectPosition ray = this.rayTrace(500.0);
        if (ray == null) {
            return;
        }
        if (Mouse.isButtonDown(1)) {
	        final double x_new = ray.func_178782_a().getX() + 0.5;
	        final double y_new = ray.func_178782_a().getY() + 1;
	        final double z_new = ray.func_178782_a().getZ() + 0.5;
	        for (double distance = mc.thePlayer.getDistance(x_new, y_new, z_new), d = 0.0; d < distance; d += 2.0) {
	            this.setPos(mc.thePlayer.posX + (x_new - mc.thePlayer.func_174811_aO().getFrontOffsetX() - mc.thePlayer.posX) * d / distance, mc.thePlayer.posY + (y_new - mc.thePlayer.posY) * d / distance, mc.thePlayer.posZ + (z_new - mc.thePlayer.func_174811_aO().getFrontOffsetZ() - mc.thePlayer.posZ) * d / distance);
	        }
	        this.setPos(x_new, y_new, z_new);
	        mc.renderGlobal.loadRenderers();
}
	}

	public MovingObjectPosition rayTrace(final double blockReachDistance) {
		final Vec3 vec3 = mc.thePlayer.func_174824_e(1.0f);
		final Vec3 vec4 = mc.thePlayer.getLookVec();
		final Vec3 vec5 = vec3.addVector(vec4.xCoord * blockReachDistance, vec4.yCoord * blockReachDistance,
				vec4.zCoord * blockReachDistance);
		return mc.theWorld.rayTraceBlocks(vec3, vec5, !mc.thePlayer.isInWater(), false, false);
	}

	public void setPos(final double x, final double y, final double z) {
		mc.thePlayer.sendQueue.addToSendQueue(new C03PacketPlayer.C04PacketPlayerPosition(x, y, z, true));
		mc.thePlayer.setPosition(x, y, z);
	}
}
