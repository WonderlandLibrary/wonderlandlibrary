package me.robbanrobbin.jigsaw.client.modules;

import org.lwjgl.input.Keyboard;

import me.robbanrobbin.jigsaw.client.WaitTimer;
import me.robbanrobbin.jigsaw.client.events.PreMotionEvent;
import me.robbanrobbin.jigsaw.client.events.UpdateEvent;
import me.robbanrobbin.jigsaw.client.main.Jigsaw;
import me.robbanrobbin.jigsaw.client.module.state.Category;
import me.robbanrobbin.jigsaw.module.Module;
import net.minecraft.network.play.client.C07PacketPlayerDigging;
import net.minecraft.network.play.client.C08PacketPlayerBlockPlacement;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;

public class NoSlowdown extends Module {
	
	boolean xd = false;
	
	int timer = 0;

	public NoSlowdown() {
		super("NoSlowdown", Keyboard.KEY_NONE, Category.MOVEMENT, "When using items, you don't get slowed down.");
	}
	
	@Override
	public void onUpdate(UpdateEvent event) {
		if (!this.currentMode.equals("NCP")) {
			return;
		}
		xd = true;
		if(!mc.thePlayer.isBlocking() || !mc.thePlayer.isMovingXZ()) {
			xd = false;
		}
		if(xd) {
			this.mc.getNetHandler().addToSendQueue(new C07PacketPlayerDigging(
					C07PacketPlayerDigging.Action.RELEASE_USE_ITEM, BlockPos.ORIGIN, EnumFacing.UP));
		}
		super.onUpdate(event);
	}
	
	@Override
	public void onLateUpdate() {
		if (!this.currentMode.equals("NCP")) {
			return;
		}
		if(xd) {
			this.mc.getNetHandler().addToSendQueue(new C08PacketPlayerBlockPlacement(this.mc.thePlayer.inventory.getCurrentItem()));
			xd = false;
		}
		super.onLateUpdate();
	}

	@Override
	public String[] getModes() {
		return new String[] { "NCP", "Vanilla" };
	}

	@Override
	public String getAddonText() {
		return this.currentMode;
	}

}
