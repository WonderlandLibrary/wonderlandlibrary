package markgg.modules.impl.render;

import markgg.event.handler.EventHandler;
import markgg.event.handler.Listener;
import markgg.event.impl.Render3DEvent;
import markgg.modules.Module;
import markgg.modules.Module.Category;
import markgg.modules.ModuleInfo;
import markgg.settings.ModeSetting;
import markgg.settings.NumberSetting;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import markgg.util.render.RenderUtil;

@ModuleInfo(name = "ItemESP", category = Module.Category.RENDER)
public class ItemESP extends Module {

	public ModeSetting mode = new ModeSetting("Mode", this, "Outline", "Outline", "Full");
	public NumberSetting lineThick = new NumberSetting("Thickness", this, 0.4, 0.1, 3, 0.1);
	public NumberSetting r = new NumberSetting("R", this, 0, 0, 1, 0.1);
	public NumberSetting g = new NumberSetting("G", this, 0.5, 0, 1, 0.1);
	public NumberSetting b = new NumberSetting("B", this, 1, 0, 1, 0.1);
	
	@EventHandler
	private final Listener<Render3DEvent> e = e -> {
		if(this.isEnabled()) {
			if (mc.theWorld != null) {
				for (final Object theObject : mc.theWorld.loadedEntityList) {
					if (!(theObject instanceof Entity))
						continue;
					
					final Entity entity = (Entity)theObject;
					if (entity instanceof EntityItem)
						this.item(entity);
				}
			}
		}
	};
	
	public void item(final Entity entity) {
		final float thickness = (float) lineThick.getValue();
		final float red = (float) r.getValue();
		final float green = (float) g.getValue();
		final float blue = (float) b.getValue();
		final double xPos = entity.lastTickPosX + (entity.posX - entity.lastTickPosX) * this.mc.timer.renderPartialTicks - this.mc.getRenderManager().renderPosX;
		final double yPos = entity.lastTickPosY + (entity.posY - entity.lastTickPosY) * this.mc.timer.renderPartialTicks - this.mc.getRenderManager().renderPosY;
		final double zPos = entity.lastTickPosZ + (entity.posZ - entity.lastTickPosZ) * this.mc.timer.renderPartialTicks - this.mc.getRenderManager().renderPosZ;
		this.render(red, green, blue, xPos, yPos, zPos, entity.width, entity.height, thickness);
	}

	public void render(final float red, final float green, final float blue, final double x, final double y, final double z, final float width, final float height, final float thickness) {
		switch(mode.getMode()) {
		case "Outline":
			RenderUtil.drawOutlinedEntityESP(x, y, z, width, height, red, green, blue, thickness);
			break;
		case "Full":
			RenderUtil.drawSolidEntityESP(x, y, z, width, height, red, green, blue, 0.45f);
			break;
		}
	}

}
