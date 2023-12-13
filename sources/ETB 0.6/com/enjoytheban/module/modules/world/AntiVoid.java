package com.enjoytheban.module.modules.world;

import java.awt.Color;

import com.enjoytheban.api.EventHandler;
import com.enjoytheban.api.events.world.EventPreUpdate;
import com.enjoytheban.module.Module;
import com.enjoytheban.module.ModuleType;

import net.minecraft.block.BlockAir;
import net.minecraft.util.BlockPos;

public class AntiVoid extends Module {

	public AntiVoid() {
		super("AntiVoid", new String[] { "novoid", "antifall" }, ModuleType.World);
		setColor(new Color(223,233,233).getRGB());
	}

	@EventHandler
	private void onUpdate(EventPreUpdate e) {
		//variable to hold if a block is underneath us
		boolean blockUnderneath = false;
		//for the players posy
		for (int i = 0; i < mc.thePlayer.posY + 2; i++) {
			BlockPos pos = new BlockPos(mc.thePlayer.posX, i, mc.thePlayer.posZ);
			//if block underneath is air stop the code
			if (mc.theWorld.getBlockState(pos).getBlock() instanceof BlockAir)
				continue;
			//else set the boolean to true
			blockUnderneath = true;
		}
		//if blockunderneath return
		if (blockUnderneath)
			return;
		//if the fall distance is over 2
		if (mc.thePlayer.fallDistance < 2)
			return;
		//and if the player isnt onground or vertically colided put the player up
		if (!mc.thePlayer.onGround && !mc.thePlayer.isCollidedVertically) {
			mc.thePlayer.motionY += 0.07;
		}
	}
}