package markgg.modules.impl.render;

import markgg.event.handler.EventHandler;
import markgg.event.handler.Listener;
import markgg.event.impl.Render3DEvent;
import markgg.modules.Module;
import markgg.modules.ModuleInfo;
import markgg.settings.NumberSetting;
import markgg.util.render.RenderUtil;
import net.minecraft.client.Minecraft;
import net.minecraft.tileentity.TileEntityChest;
import net.minecraft.util.BlockPos;

@ModuleInfo(name = "ChestESP", category = Module.Category.RENDER)
public class ChestESP extends Module {

	public NumberSetting lineThick = new NumberSetting("Thickness", this, 1, 0.1, 3, 0.1);
	public NumberSetting chsRed = new NumberSetting("Red", this, 0, 0, 1, 0.1);
	public NumberSetting chsGreen = new NumberSetting("Green", this, 0.5, 0, 1, 0.1);
	public NumberSetting chsBlue = new NumberSetting("Blue", this, 1, 0, 1, 0.1);

	@EventHandler
	private final Listener<Render3DEvent> e = e -> {
		if(this.isEnabled()) {
			for(Object o : mc.theWorld.loadedTileEntityList) {
				if(o instanceof TileEntityChest)
					this.chest(((TileEntityChest)o).getPos());
			}
		}
	};
	
	public void chest(BlockPos blockPos) {
		double x = 
				blockPos.getX()
				- Minecraft.getMinecraft().getRenderManager().renderPosX;
		double y = 
				blockPos.getY()
				- Minecraft.getMinecraft().getRenderManager().renderPosY;
		double z = 
				blockPos.getZ()
				- Minecraft.getMinecraft().getRenderManager().renderPosZ;

		float red = (float) chsRed.getValue();
		float green = (float) chsGreen.getValue();
		float blue = (float) chsBlue.getValue();
		final float thickness = (float) lineThick.getValue();
		double xPos = x;
		double yPos = y;
		double zPos = z;
		this.render(red, green, blue, xPos, yPos, zPos, 1, thickness);
	}

	public void render(float red, float green, float blue, double x, double y, double z, float alpha, float lineWidth) {
		RenderUtil.drawOutlinedBlockESP(x, y, z, red, green, blue, 1, lineWidth);
	}
}
