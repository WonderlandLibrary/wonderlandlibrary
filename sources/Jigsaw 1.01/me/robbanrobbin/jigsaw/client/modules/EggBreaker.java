package me.robbanrobbin.jigsaw.client.modules;

import org.lwjgl.input.Keyboard;

import me.robbanrobbin.jigsaw.client.events.UpdateEvent;
import me.robbanrobbin.jigsaw.client.module.state.Category;
import me.robbanrobbin.jigsaw.client.tools.RenderTools;
import me.robbanrobbin.jigsaw.client.tools.Utils;
import me.robbanrobbin.jigsaw.module.Module;
import net.minecraft.block.Block;
import net.minecraft.block.BlockDragonEgg;
import net.minecraft.network.play.client.CPacketPlayerDigging;
import net.minecraft.network.play.client.CPacketPlayerDigging.Action;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;

public class EggBreaker extends Module {
	
	BlockPos target;

	public EggBreaker() {
		super("EggBreaker", Keyboard.KEY_NONE, Category.WORLD,
				"Destroys eggs around you, designed for use in eggwars.");
	}

	@Override
	public void onUpdate(UpdateEvent event) {
		target = null;
		int radius = 4;

		for (int x = -radius; x < radius; x++) {
			for (int y = radius + 1; y > -radius + 1; y--) {
				for (int z = -radius; z < radius; z++) {

					double xBlock = (mc.player.posX + (double) x);
					double yBlock = (mc.player.posY + (double) y);
					double zBlock = (mc.player.posZ + (double) z);

					BlockPos blockPos = new BlockPos(xBlock, yBlock, zBlock);
					Block block = mc.world.getBlockState(blockPos).getBlock();

					if (!(block instanceof BlockDragonEgg)) {
						continue;
					}
					target = blockPos;
					
					float[] pos = Utils.getFacePos(Utils.getVec3d(target));
					
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
			Vec3d targetVec = RenderTools.getRenderPos(target.getX(), target.getY(), target.getZ());
			RenderTools.drawSolidBlockESP(targetVec.x, targetVec.y, targetVec.z, 1f, 0.5f, 0.5f, 0.5f);
		}
		super.onRender();
	}
	
	@Override
	public void onLateUpdate() {
		if(target != null) {
			mc.player.connection.sendPacket(
					new CPacketPlayerDigging(Action.START_DESTROY_BLOCK, target, EnumFacing.NORTH));
			mc.player.connection.sendPacket(
					new CPacketPlayerDigging(Action.STOP_DESTROY_BLOCK, target, EnumFacing.NORTH));
		}
		super.onLateUpdate();
	}

	@Override
	public String getAddonText() {
		return currentMode;
	}
}
