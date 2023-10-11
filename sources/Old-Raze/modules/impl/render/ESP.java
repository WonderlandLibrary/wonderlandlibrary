package markgg.modules.impl.render;

import markgg.modules.Module;
import markgg.event.handler.EventHandler;
import markgg.event.handler.Listener;
import markgg.event.impl.Render3DEvent;
import markgg.modules.Module;
import markgg.modules.ModuleInfo;
import markgg.settings.BooleanSetting;
import markgg.settings.NumberSetting;
import markgg.settings.ModeSetting;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.monster.EntityGhast;
import net.minecraft.entity.monster.EntityMagmaCube;
import net.minecraft.entity.monster.EntityMob;
import net.minecraft.entity.monster.EntitySlime;
import net.minecraft.entity.passive.EntityAnimal;
import net.minecraft.entity.passive.EntityBat;
import net.minecraft.entity.passive.EntitySquid;
import net.minecraft.entity.passive.EntityVillager;
import net.minecraft.entity.player.EntityPlayer;
import markgg.util.render.RenderUtil;

@ModuleInfo(name = "ESP", category = Module.Category.RENDER)
public class ESP extends Module{

	public ModeSetting mode = new ModeSetting("Mode", this, "Outline", "Outline", "Full");
	public NumberSetting plrRed = new NumberSetting("R", this, 0, 0, 1, 0.1);
	public NumberSetting plrGreen = new NumberSetting("G", this, 0.5, 0, 1, 0.1);
	public NumberSetting plrBlue = new NumberSetting("B", this, 1, 0, 1, 0.1);
	public BooleanSetting myself = new BooleanSetting("Myself", this, false);

	@EventHandler
	private final Listener<Render3DEvent> e = e -> {
		if (this.isEnabled() && mc.theWorld != null) {
			for (final Object theObject : mc.theWorld.loadedEntityList) {
				if (!(theObject instanceof EntityLivingBase))
					continue;

				final EntityLivingBase entity = (EntityLivingBase)theObject;
				if (entity instanceof EntityPlayer) {
					if(myself.getValue()) {
						this.player(entity);
					}else {
						if (entity == this.mc.thePlayer)
							continue;

						this.player(entity);
					}
				}

			}
		}

	};

	public void player(final EntityLivingBase entity) {
		final float red = (float) plrRed.getValue();
		final float green = (float) plrGreen.getValue();
		final float blue = (float) plrBlue.getValue();
		final double xPos = entity.lastTickPosX + (entity.posX - entity.lastTickPosX) * this.mc.timer.renderPartialTicks - this.mc.getRenderManager().renderPosX;
		final double yPos = entity.lastTickPosY + (entity.posY - entity.lastTickPosY) * this.mc.timer.renderPartialTicks - this.mc.getRenderManager().renderPosY;
		final double zPos = entity.lastTickPosZ + (entity.posZ - entity.lastTickPosZ) * this.mc.timer.renderPartialTicks - this.mc.getRenderManager().renderPosZ;
		this.render(red, green, blue, xPos, yPos, zPos, entity.width, entity.height);
	}

	public void render(final float red, final float green, final float blue, final double x, final double y, final double z, final float width, final float height) {
		switch(mode.getMode()) {
		case "Outline":
			RenderUtil.drawOutlinedEntityESP(x, y, z, width, height, red, green, blue, 1f);
			break;
		case "Full":
			RenderUtil.drawSolidEntityESP(x, y, z, width, height, red, green, blue, 0.45f);
			break;
		}
	}

}
