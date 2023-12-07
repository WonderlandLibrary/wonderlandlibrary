package me.robbanrobbin.jigsaw.client.modules;

import org.lwjgl.input.Keyboard;

import me.robbanrobbin.jigsaw.client.module.state.Category;
import me.robbanrobbin.jigsaw.client.tools.RenderTools;
import me.robbanrobbin.jigsaw.module.Module;
import net.minecraft.util.BlockPos;
import net.minecraft.util.MovingObjectPosition;

public class ClickTeleport extends Module {

	private BlockPos hover;

	public ClickTeleport() {
		super("ClickTeleport", Keyboard.KEY_NONE, Category.MOVEMENT, "Teleports you forward when you right-click.");
	}

	@Override
	public void onUpdate() {
		hover = mc.thePlayer.rayTrace(8, mc.timer.renderPartialTicks).getBlockPos();
		super.onUpdate();
	}

	@Override
	public void onRightClick() {
		MovingObjectPosition rayTrace = mc.thePlayer.rayTrace(8, mc.timer.renderPartialTicks);
		BlockPos blockPos = rayTrace.getBlockPos();
		if (blockPos == null) {
			return;
		}
		mc.thePlayer.setPosition(blockPos.getX() + 0.5, blockPos.getY() + 1, blockPos.getZ() + 0.5);
		super.onRightClick();
	}

	@Override
	public void onRender() {
		if (hover == null) {
			return;
		}
		double x = hover.getX() + 0.5 - mc.getRenderManager().renderPosX;
		double y = hover.getY() + 1 - mc.getRenderManager().renderPosY;
		double z = hover.getZ() + 0.5 - mc.getRenderManager().renderPosZ;
		
		RenderTools.drawSolidEntityESP(x, y, z, mc.thePlayer.width / 2, mc.thePlayer.height, 0.1f, 1f, 0.1f, 0.3f);
		RenderTools.drawOutlinedEntityESP(x, y, z, mc.thePlayer.width / 2, mc.thePlayer.height, 0.1f, 1f, 0.1f, 1f);
		super.onRender();
	}

}
