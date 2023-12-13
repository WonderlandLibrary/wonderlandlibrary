package me.aidanmees.trivia.client.commands;

import java.util.List;

import me.aidanmees.trivia.client.main.trivia;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.network.play.client.C03PacketPlayer;

public class CommandJump extends Command {
	private boolean offGround;

	@Override
	public void run(String[] args) {
offGround = false;
        float direction = mc.thePlayer.rotationYaw + (
                mc.thePlayer.moveForward < 0.0F ? 180 : 0) + (
                mc.thePlayer.moveStrafing > 0.0F ? -90.0F * (mc.thePlayer.moveForward > 0.0F ? 0.5F : mc.thePlayer.moveForward < 0.0F ? -0.5F : 1.0F) : 0.0F) - (
                mc.thePlayer.moveStrafing < 0.0F ? -90.0F * (mc.thePlayer.moveForward > 0.0F ? 0.5F : mc.thePlayer.moveForward < 0.0F ? -0.5F : 1.0F) : 0.0F);
        float xDir = (float) Math.cos((direction + 90.0F) * Math.PI / 180.0D);
        float zDir = (float) Math.sin((direction + 90.0F) * Math.PI / 180.0D);

        mc.thePlayer.setSprinting(true);
        mc.thePlayer.onGround = true;
        if (mc.thePlayer.isCollidedVertically) {
            mc.thePlayer.jump();
        } else {
            offGround = true;
            mc.thePlayer.motionX = (xDir * 0.36D);
            mc.thePlayer.motionZ = (zDir * 0.36D);
        }
        if (offGround && mc.thePlayer.isCollidedVertically) {
            mc.thePlayer.setVelocity(0.0D, 0.0D, 0.0D);
        }
	}


	@Override
	public String getActivator() {
		return ".jump";
	}

	@Override
	public String getSyntax() {
		return ".jump";
	}

	@Override
	public String getDesc() {
		return "Jumps";
	}
}
