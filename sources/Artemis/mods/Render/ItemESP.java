package artemis.mods.Render;

import artemis.Category;
import artemis.module;
import artemis.util.RenderUtils;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.item.Item;
import net.minecraft.tileentity.TileEntityChest;

public class ItemESP extends module{

	public ItemESP() {
		super("ItemESP", "Draws a box around items", 0, 54, Category.RENDER);
	}
	
	public void onRender() {
		if(this.isToggled()){
			for(Object o: mc.theWorld.loadedEntityList){
				if(o instanceof EntityItem){
					RenderUtils.blockESPBox(((EntityItem)o).getPosition());
				}
			}
		}
	}

}