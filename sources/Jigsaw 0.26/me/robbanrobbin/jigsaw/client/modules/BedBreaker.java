package me.robbanrobbin.jigsaw.client.modules;

import org.lwjgl.input.Keyboard;

import me.robbanrobbin.jigsaw.client.events.UpdateEvent;
import me.robbanrobbin.jigsaw.client.module.state.Category;
import me.robbanrobbin.jigsaw.client.tools.RenderTools;
import me.robbanrobbin.jigsaw.client.tools.Utils;
import me.robbanrobbin.jigsaw.module.Module;
import net.minecraft.block.Block;
import net.minecraft.block.BlockBed;
import net.minecraft.block.BlockDragonEgg;
import net.minecraft.network.play.client.C07PacketPlayerDigging;
import net.minecraft.network.play.client.C07PacketPlayerDigging.Action;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.Vec3;

public class BedBreaker extends Module {
	
	BlockPos target;
	
	BlockPos target2;

	public BedBreaker() {
		super("BedBreaker", Keyboard.KEY_NONE, Category.WORLD,
				"Destroys beds around you, designed for use in bedwars.");
	}

	@Override
	public void onUpdate(UpdateEvent event) {
		target = null;
		target2 = null;
		int radius = 4;

		for (int x = -radius; x < radius; x++) {
			for (int y = radius + 1; y > -radius + 1; y--) {
				for (int z = -radius; z < radius; z++) {

					double xBlock = (mc.thePlayer.posX + (double) x);
					double yBlock = (mc.thePlayer.posY + (double) y);
					double zBlock = (mc.thePlayer.posZ + (double) z);

					BlockPos blockPos = new BlockPos(xBlock, yBlock, zBlock);
					Block block = mc.theWorld.getBlockState(blockPos).getBlock();

					if (!(block instanceof BlockBed)) {
						continue;
					}
					if(target != null) {
						target2 = blockPos;
						return;
					}
					target = blockPos;
					
					float[] pos = Utils.getFacePos(Utils.getVec3(target));
					
					event.yaw = pos[0];
					event.pitch = pos[1];
				}
			}
		}

		super.onUpdate(event);
	}
	
	@Override
	public void onRender() {
		if(target != null) {
			Vec3 targetVec = RenderTools.getRenderPos(target.getX(), target.getY(), target.getZ());
			RenderTools.drawSolidBlockESP(targetVec.xCoord, targetVec.yCoord, targetVec.zCoord, 1f, 0.5f, 0.5f, 0.5f);
		}
		if(target2 != null) {
			Vec3 targetVec = RenderTools.getRenderPos(target2.getX(), target2.getY(), target2.getZ());
			RenderTools.drawSolidBlockESP(targetVec.xCoord, targetVec.yCoord, targetVec.zCoord, 1f, 0.5f, 0.5f, 0.5f);
		}
		super.onRender();
	}
	
	@Override
	public void onLateUpdate() {
		if(target != null) {
			mc.thePlayer.sendQueue.addToSendQueue(
					new C07PacketPlayerDigging(Action.START_DESTROY_BLOCK, target, EnumFacing.NORTH));
			mc.thePlayer.sendQueue.addToSendQueue(
					new C07PacketPlayerDigging(Action.STOP_DESTROY_BLOCK, target, EnumFacing.NORTH));
		}
		super.onLateUpdate();
	}

	@Override
	public String getAddonText() {
		return currentMode;
	}
}
