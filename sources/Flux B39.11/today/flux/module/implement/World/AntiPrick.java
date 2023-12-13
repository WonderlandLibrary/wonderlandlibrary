package today.flux.module.implement.World;

import com.darkmagician6.eventapi.EventTarget;
import net.minecraft.block.BlockCactus;
import net.minecraft.util.AxisAlignedBB;
import today.flux.event.BBSetEvent;
import today.flux.module.Category;
import today.flux.module.Module;

public class AntiPrick extends Module {

	public AntiPrick() {
		super("AntiPrick", Category.World, false);
	}

	public void onEnable() {
		super.onEnable();
	}

	public void onDisable() {
		super.onDisable();
	}

	@EventTarget
	private void onBB(BBSetEvent event) {
		if (event.block instanceof BlockCactus) {
			event.boundingBox = new AxisAlignedBB(event.pos.getX(), event.pos.getY(), event.pos.getZ(), event.pos.getX() + 1, event.pos.getY() + 1, event.pos.getZ() + 1);
		}
	}

}
