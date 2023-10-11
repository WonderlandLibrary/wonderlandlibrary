package artemis.mods.Render;

import artemis.Category;
import artemis.module;
import artemis.util.RenderUtils;
import net.minecraft.tileentity.TileEntityChest;

public class ChestESP extends module{

	public ChestESP() {
		super("ChestESP", "Draws a box around chests", 0, 54, Category.RENDER);
	}
	
	public void onRender() {
		if(this.isToggled()){
			for(Object o: mc.theWorld.loadedTileEntityList){
				if(o instanceof TileEntityChest){
					RenderUtils.blockESPBox(((TileEntityChest)o).getPos());
				}
			}
		}
	}

}