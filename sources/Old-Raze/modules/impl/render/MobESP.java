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

@ModuleInfo(name = "MobESP", category = Module.Category.RENDER)
public class MobESP extends Module{

	public ModeSetting mode = new ModeSetting("Mode", this, "Outline", "Outline", "Full");

	@EventHandler
	private final Listener<Render3DEvent> e = e -> {
		if (this.isEnabled() && mc.theWorld != null) {
			for (final Object theObject : mc.theWorld.loadedEntityList) {
				if (!(theObject instanceof EntityLivingBase))
					continue;

				final EntityLivingBase entity = (EntityLivingBase)theObject;
				if (entity instanceof EntityMob || entity instanceof EntityMagmaCube || entity instanceof EntityGhast || entity instanceof EntitySlime)
					this.mob(entity);
				
				if (entity instanceof EntityAnimal || entity instanceof EntityVillager || entity instanceof EntitySquid || entity instanceof EntityBat)
					this.animal(entity);

			}
		}

	};

	public void mob(final EntityLivingBase entity) {
		final float red = 1f;
		final float green = 0f;
		final float blue = 0.2f;
		final double xPos = entity.lastTickPosX + (entity.posX - entity.lastTickPosX) * this.mc.timer.renderPartialTicks - this.mc.getRenderManager().renderPosX;
		final double yPos = entity.lastTickPosY + (entity.posY - entity.lastTickPosY) * this.mc.timer.renderPartialTicks - this.mc.getRenderManager().renderPosY;
		final double zPos = entity.lastTickPosZ + (entity.posZ - entity.lastTickPosZ) * this.mc.timer.renderPartialTicks - this.mc.getRenderManager().renderPosZ;
		this.render(red, green, blue, xPos, yPos, zPos, entity.width, entity.height);
	}

	public void animal(final EntityLivingBase entity) {
		final float red = 0f;
		final float green = 1f;
		final float blue = 0.3f;
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
