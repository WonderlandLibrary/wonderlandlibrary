package markgg.modules.impl.render;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

import markgg.RazeClient;
import markgg.event.handler.EventHandler;
import markgg.modules.ModuleInfo;
import markgg.settings.ModeSetting;
import markgg.util.MathUtil;
import net.minecraft.client.entity.AbstractClientPlayer;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.ResourceLocation;
import markgg.event.handler.Listener;
import markgg.event.impl.MotionEvent;
import markgg.event.impl.Render2DEvent;
import markgg.modules.Module;
import markgg.modules.impl.combat.OldAura;

@ModuleInfo(name = "TargetHUD", category = Module.Category.RENDER)
public class TargetHUD extends Module {

	public ModeSetting mode = new ModeSetting("Style", this, "Raze", "Raze");
	public static EntityLivingBase kaTarget = null;
	public static List<EntityLivingBase> targets;

	public void onEnable() {
		kaTarget = null;
	}

	public void onDisable() {
		kaTarget = null;
	}


	@EventHandler
	private final Listener<Render2DEvent> e = e -> {
		ScaledResolution sr = new ScaledResolution(this.mc, this.mc.displayWidth, this.mc.displayHeight);
		FontRenderer fr = mc.fontRendererObj;
		
		if(RazeClient.INSTANCE.getModuleManager().getModule(OldAura.class).toggled && RazeClient.INSTANCE.getModuleManager().getModule(OldAura.class).target != null) {
			kaTarget = RazeClient.INSTANCE.getModuleManager().getModule(OldAura.class).target;

			Entity closestEntity = getClosestEntity();
			targets = (List<EntityLivingBase>)mc.theWorld.loadedEntityList.stream().filter(EntityLivingBase.class::isInstance).collect(Collectors.toList());
			targets = targets.stream().filter(entity -> entity.getDistanceToEntity(mc.thePlayer) < RazeClient.INSTANCE.getModuleManager().getModule(OldAura.class).range.getValue()  && entity != mc.thePlayer && !entity.isDead && entity.getHealth() > 0).collect(Collectors.toList());
			targets.sort(Comparator.comparingDouble(entity -> ((EntityLivingBase)entity).getDistanceToEntity(mc.thePlayer)));
			int primaryColor = 0xFFE44964;
			
			switch(mode.getMode()) {
			case "Raze":
				if (closestEntity instanceof EntityLivingBase) {
					EntityLivingBase entityLiving = (EntityLivingBase) kaTarget;

					if(!targets.isEmpty()) {
						int width = sr.getScaledWidth(), height = sr.getScaledHeight();
						int centerX = width / 2, centerY = height / 2;
						int boxWidth = 120, boxHeight = 40;

						int boxX = centerX - (boxWidth / 2) + 80, boxY = centerY - (boxHeight / 2) + 30;

						Gui.drawRect(boxX, boxY, boxX + boxWidth, boxY + boxHeight, -1879048192);
						Gui.drawRect(boxX + 5, boxY + 5, boxX + 4, boxY + boxHeight - 5, primaryColor);
						Gui.drawRect(boxX + 117, boxY + 5, boxX + 116, boxY + boxHeight - 5, primaryColor);

						double entHealth = entityLiving.getHealth();
						int roundHealth = (int) MathUtil.round(entHealth, 2.0D);

						double entDist = mc.thePlayer.getDistanceToEntity(kaTarget);
						int roundDist = (int) MathUtil.round(entDist, 2.0D);

						fr.drawStringWithShadow(entityLiving.getName(), boxX + (boxWidth / 2) - 50, boxY + 5, 0xFFFFFF);

						fr.drawStringWithShadow("Health: " + roundHealth, boxX + (boxWidth / 2) - 50, boxY + 15, 0xFFFFFF);

						fr.drawStringWithShadow("Distance: " + roundDist + "m", boxX + (boxWidth / 2) - 50, boxY + 25, 0xFFFFFF);

						if(closestEntity instanceof EntityPlayer) {
							ResourceLocation skinLoc1 = ((AbstractClientPlayer) targets.get(0)).getLocationSkin();
							mc.getTextureManager().bindTexture(skinLoc1);
							Gui.drawModalRectWithCustomSizedTexture(boxX + 80, boxY + 4, 32, 32, 32, 32, 256, 256);
						}else {
							mc.getTextureManager().bindTexture(new ResourceLocation("Raze/Head/steveHead.png"));
							Gui.drawModalRectWithCustomSizedTexture(boxX + 80, boxY + 4, 0, 0, 32, 32, 32, 32);
						}
					}
				}
				break;
			}
		}
	};

	private Entity getClosestEntity() {
		Entity closestEntity = null;
		double closestDistanceSq = Double.POSITIVE_INFINITY;
		EntityPlayerSP player = mc.thePlayer;

		for (Object obj : mc.theWorld.loadedEntityList) {
			if (obj instanceof Entity && obj != player) {
				Entity entity = (Entity) obj;
				double distanceSq = entity.getDistanceSq(player.posX, player.posY, player.posZ);
				if (distanceSq < closestDistanceSq) {
					closestEntity = entity;
					closestDistanceSq = distanceSq;
				}
			}
		}

		return closestEntity;
	}
}
