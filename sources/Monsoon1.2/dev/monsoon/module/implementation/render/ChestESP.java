package dev.monsoon.module.implementation.render;

import org.lwjgl.input.Keyboard;

import dev.monsoon.event.Event;
import dev.monsoon.event.listeners.EventRender3D;
import dev.monsoon.module.base.Module;
import dev.monsoon.util.render.RenderUtil;
import net.minecraft.tileentity.TileEntityChest;
import dev.monsoon.module.enums.Category;

public class ChestESP extends Module {
	public ChestESP() {
		super("ChestESP", Keyboard.KEY_NONE, Category.RENDER);
	}
	
	public void onEnable() {
		
	}
	
	public void onDisable() {
		
	}
	
	
	
	public void onEvent(Event e) {
		if(e instanceof EventRender3D) {
			for(Object o : mc.theWorld.loadedTileEntityList) {
				if(o instanceof TileEntityChest) {
					RenderUtil.drawBoxFromBlockpos(((TileEntityChest) o).getPos(), 1, 1, 1, 1);
				}
			}
		}
	}
}
