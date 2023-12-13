package wtf.monsoon.impl.modules.visual;

import org.lwjgl.input.Keyboard;

import wtf.monsoon.api.event.EventTarget;
import wtf.monsoon.api.event.impl.EventRender3D;
import wtf.monsoon.api.module.Category;
import wtf.monsoon.api.module.Module;
import wtf.monsoon.api.util.render.RenderUtil;
import net.minecraft.tileentity.TileEntityChest;

public class ChestESP extends Module {
	public ChestESP() {
		super("ChestESP", "Show chests", Keyboard.KEY_NONE, Category.RENDER);
	}
	
	public void onEnable() {
		super.onEnable();
	}
	
	public void onDisable() {
		super.onDisable();
	}
	
	@EventTarget
	public void onRender3D(EventRender3D e) {
		for(Object o : mc.theWorld.loadedTileEntityList) {
			if(o instanceof TileEntityChest) {
				RenderUtil.drawBoxFromBlockpos(((TileEntityChest) o).getPos(), 1, 1, 1, 1);
			}
		}
	}
	
}
