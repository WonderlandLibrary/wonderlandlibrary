package best.azura.client.impl.module.impl.render.esp;

import best.azura.client.impl.Client;
import best.azura.client.impl.events.EventRender2D;
import best.azura.client.impl.events.EventRender3D;
import best.azura.eventbus.handler.EventHandler;
import best.azura.eventbus.handler.Listener;
import best.azura.client.impl.module.impl.render.BlurModule;
import best.azura.client.impl.module.impl.render.ESP;
import best.azura.client.impl.ui.font.Fonts;
import best.azura.client.util.color.ColorUtil;
import best.azura.client.util.render.DrawUtil;
import best.azura.client.util.render.RenderUtil;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.ActiveRenderInfo;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityCreature;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.passive.EntityAnimal;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.MathHelper;
import net.minecraft.util.ResourceLocation;
import org.lwjgl.util.glu.GLU;

import java.awt.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.HashMap;

public class ESP2D {
	private final Minecraft mc = Minecraft.getMinecraft();
	private final ArrayList<Entity> validEntities = new ArrayList<>();
	private final HashMap<Entity, float[]> coords2d = new HashMap<>();
	private ESP esp;
	@EventHandler
	public final Listener<EventRender3D> eventRender3DListener = e -> {
		if (esp == null) esp = (ESP) Client.INSTANCE.getModuleManager().getModule(ESP.class);
		validEntities.clear();
		for (Entity entity : mc.theWorld.loadedEntityList) {
			if (esp.checkPit.getObject() && entity.posY > 100
					&& entity.posX < 25 && entity.posX > -25 && entity.posZ < 25 && entity.posZ > -25) continue;
			if (entity.isInvisible()) continue;
			if (entity.getName().contains("[NPC]")) continue;
			if (!(esp.targetsCombo.isSelected("Players") && entity instanceof EntityPlayer) &&
					!(esp.targetsCombo.isSelected("Monsters") && entity instanceof EntityCreature) &&
					!(esp.targetsCombo.isSelected("Items") && entity instanceof EntityItem) &&
					!(esp.targetsCombo.isSelected("Animals") && entity instanceof EntityAnimal)) continue;
			validEntities.add(entity);
			final AxisAlignedBB interpolatedBB = new AxisAlignedBB(
					entity.lastTickPosX - entity.width / 2 + (entity.posX - entity.lastTickPosX) * mc.timer.renderPartialTicks,
					entity.lastTickPosY + (entity.posY - entity.lastTickPosY) * mc.timer.renderPartialTicks,
					entity.lastTickPosZ - entity.width / 2 + (entity.posZ - entity.lastTickPosZ) * mc.timer.renderPartialTicks,
					entity.lastTickPosX + entity.width / 2 + (entity.posX - entity.lastTickPosX) * mc.timer.renderPartialTicks,
					entity.lastTickPosY + entity.height + (entity.posY - entity.lastTickPosY) * mc.timer.renderPartialTicks,
					entity.lastTickPosZ + entity.width / 2 + (entity.posZ - entity.lastTickPosZ) * mc.timer.renderPartialTicks);
			double[][] vector2ds = new double[8][2];
			convertAABTo2D(interpolatedBB, vector2ds);
			final float[] coords0 = new float[4];
			convertVectorsToCoords(vector2ds, coords0);
			coords2d.put(entity, coords0);
		}
	};

	@EventHandler
	public final Listener<EventRender2D> eventRender2DListener = e -> {
		if (esp == null) esp = (ESP) Client.INSTANCE.getModuleManager().getModule(ESP.class);
		boolean blur = BlurModule.blurESP.getObject() && Client.INSTANCE.getModuleManager().getModule(BlurModule.class).isEnabled();
		for (Entity entity : validEntities) {
			final float[] coords0 = coords2d.get(entity);
			float minX = coords0[0], minY = coords0[1], maxX = coords0[2], maxY = coords0[3];
			if (esp.esp2DCombo.isSelected("Box")) renderBox(minX, minY, maxX, maxY, esp);
			if (blur) {
				float finalMinX = minX, finalMinY = minY, finalMaxX = maxX, finalMaxY = maxY;
				BlurModule.blurTasks.add(() -> {
					boolean darkMode = BlurModule.blurShader.isDarkMode();
					BlurModule.blurShader.setDarkMode(false);
					RenderUtil.INSTANCE.drawRect(finalMinX, finalMinY, finalMaxX, finalMaxY, -1);
					BlurModule.blurShader.setDarkMode(darkMode);
				});
			}
			if (!(entity instanceof EntityPlayer)) continue;
			final EntityPlayer p = (EntityPlayer) entity;

			if (esp.esp2DCombo.isSelected("Image")) {
				GlStateManager.resetColor();
				GlStateManager.color(1, 1, 1, 1);
				GlStateManager.enableAlpha();
				mc.getTextureManager().bindTexture(new ResourceLocation("custom/esp_image.png"));
				RenderUtil.INSTANCE.drawTexture(minX, minY, maxX, maxY);
			}
			if (esp.esp2DCombo.isSelected("Filled")) {
				final Color color = new Color(esp.espBoxColor.getObject().getColor().getRed(),
						esp.espBoxColor.getObject().getColor().getGreen(),
						esp.espBoxColor.getObject().getColor().getBlue(), esp.alphaValue.getObject());
				RenderUtil.INSTANCE.drawRect(minX, minY, maxX, maxY, color.getRGB());
			}

			final FontRenderer fr = mc.fontRendererObj;
			double scale = .5;

			if (esp.esp2DCombo.isSelected("Name tags")) {
				GlStateManager.scale(scale, scale, 1);
				minX *= 1. / scale;
				minY *= 1. / scale;
				maxX *= 1. / scale;
				maxY *= 1. / scale;
				if (maxY != 0) {
					RenderUtil.INSTANCE.drawRoundedRect(minX + (maxX - minX) / 2.0 - Fonts.INSTANCE.hudFont.getStringWidth(p.getName()) / 2.0 - 5,
							minY - 28, Fonts.INSTANCE.hudFont.getStringWidth(p.getName()) + 10, Fonts.INSTANCE.hudFont.FONT_HEIGHT + 5, 5, new Color(25, 25, 25, 150));

					Fonts.INSTANCE.hudFont.drawStringWithShadow(p.getName(), minX + (maxX - minX) / 2.0 - Fonts.INSTANCE.hudFont.getStringWidth(p.getName()) / 2.0,
							minY - 25, new Color(255, 255, 255, 200).getRGB());

				}
				GlStateManager.scale(1. / scale, 1. / scale, 1);
				minX *= scale;
				minY *= scale;
				maxX *= scale;
				maxY *= scale;
			}
			if (esp.esp2DCombo.isSelected("Held item")) {
				GlStateManager.scale(scale, scale, 1);
				minX *= 1. / scale;
				minY *= 1. / scale;
				maxX *= 1. / scale;
				maxY *= 1. / scale;
				String s = "";
				if (p.getHeldItem() != null) s = p.getHeldItem().getDisplayName();
				if (maxY != 0)
					fr.drawStringWithShadow(s, minX + (maxX - minX) / 2.0 - fr.getStringWidth(s) / 2.0, maxY + 5, -1);
				GlStateManager.scale(1. / scale, 1. / scale, 1);
				minX *= scale;
				minY *= scale;
				maxX *= scale;
				maxY *= scale;
			}
			final double dist0 = MathHelper.clamp_double(mc.getRenderManager().getDistanceToCamera(
					entity.lastTickPosX + (entity.posX - entity.lastTickPosX) * mc.timer.renderPartialTicks,
					entity.lastTickPosY + (entity.posY - entity.lastTickPosY) * mc.timer.renderPartialTicks,
					entity.lastTickPosZ + (entity.posZ - entity.lastTickPosZ) * mc.timer.renderPartialTicks) / 10, 1, 2) + 0.5;
			minX -= dist0;
			maxX += dist0;
			final float width = 1.5F;
			if (esp.esp2DCombo.isSelected("Health bar")) {
				DrawUtil.glDrawLine(minX, minY - 1, minX, maxY + 1, width * 2, true, Color.BLACK);
				DrawUtil.glDrawLine(minX, maxY - (maxY - minY) * Math.max(0, Math.min(1, (p.getHealth() / p.getMaxHealth()))), minX, maxY,
						width, true, ColorUtil.getColorFromHealth(p));

				if (esp.esp2DCombo.isSelected("Health text")) {
					GlStateManager.scale(scale, scale, 1);
					minX *= 1. / scale;
					minY *= 1. / scale;
					maxX *= 1. / scale;
					maxY *= 1. / scale;
					String s1 = String.format("%.1f", p.getHealth()).replace(',', '.') + "§c\u2764§r";

					fr.drawStringWithOutline(s1, minX - 75 / 2F, maxY - (maxY - minY) * (p.getHealth() / p.getMaxHealth()), -1);
					GlStateManager.scale(1. / scale, 1. / scale, 1);
					minY *= scale;
					maxX *= scale;
					maxY *= scale;
				}
			}

			if (esp.esp2DCombo.isSelected("Armor bar") && p.getTotalArmorValue() != 0 && maxY != 0) {
				DrawUtil.glDrawLine(maxX, minY - 1, maxX, maxY + 1, width * 2, true, Color.BLACK);
				DrawUtil.glDrawLine(maxX, maxY - (maxY - minY) * (p.getTotalArmorValue() / 20.0), maxX, maxY,
						width, true, new Color(20, 150, 255));
			}
		}
		GlStateManager.resetColor();
	};

	private void convertVectorsToCoords(double[][] vector2ds, float[] coords) {
		final float minW = (float) Arrays.stream(vector2ds).min(Comparator.comparingDouble(pos -> pos[2])).orElse(new double[]{0.5})[2];
		final float maxW = (float) Arrays.stream(vector2ds).max(Comparator.comparingDouble(pos -> pos[2])).orElse(new double[]{0.5})[2];
		if (maxW > 1 || minW < 0) return;
		final float minX = (float) Arrays.stream(vector2ds).min(Comparator.comparingDouble(pos -> pos[0])).orElse(new double[]{0})[0];
		final float maxX = (float) Arrays.stream(vector2ds).max(Comparator.comparingDouble(pos -> pos[0])).orElse(new double[]{0})[0];
		final float top = (mc.displayHeight / (float) new ScaledResolution(mc).getScaleFactor());
		final float minY = (float) (top - Arrays.stream(vector2ds).min(Comparator.comparingDouble(pos -> top - pos[1])).orElse(new double[]{0})[1]);
		final float maxY = (float) (top - Arrays.stream(vector2ds).max(Comparator.comparingDouble(pos -> top - pos[1])).orElse(new double[]{0})[1]);
		coords[0] = minX;
		coords[1] = minY;
		coords[2] = maxX;
		coords[3] = maxY;
	}


	private void renderBox(float minX, float minY, float maxX, float maxY, ESP esp) {
		switch (esp.esp2DBoxMode.getObject()) {
			case "Full":
				//Outline
				DrawUtil.glDrawLine(minX, minY, minX, maxY, 2.5F, true, Color.BLACK);
				DrawUtil.glDrawLine(maxX, minY, maxX, maxY, 2.5F, true, Color.BLACK);
				DrawUtil.glDrawLine(minX, minY, maxX, minY, 2.5F, true, Color.BLACK);
				DrawUtil.glDrawLine(maxX, maxY, minX, maxY, 2.5F, true, Color.BLACK);

				//Colored part of the ESP
				DrawUtil.glDrawLine(minX, minY, minX, maxY, 1.25F, true, esp.espColor.getObject().getColor());
				DrawUtil.glDrawLine(maxX, minY, maxX, maxY, 1.25F, true, esp.espColor.getObject().getColor());
				DrawUtil.glDrawLine(minX, minY, maxX, minY, 1.25F, true, esp.espColor.getObject().getColor());
				DrawUtil.glDrawLine(maxX, maxY, minX, maxY, 1.25F, true, esp.espColor.getObject().getColor());
				break;
			case "Corner":
				//Outline
				RenderUtil.INSTANCE.drawCorneredRect(minX, minY, maxX, maxY, 2.5F, Color.BLACK.getRGB());
				//Colored part of the ESP
				RenderUtil.INSTANCE.drawCorneredRect(minX, minY, maxX, maxY, 1.25F, esp.espColor.getObject().getColor().getRGB());
				break;
			case "Side":
				//Outline
				RenderUtil.INSTANCE.drawCorneredRect(minX, minY, maxX, maxY, 2.5F, Color.BLACK.getRGB());
				DrawUtil.glDrawLine(minX, minY, minX, maxY, 2.5F, true, Color.BLACK);
				DrawUtil.glDrawLine(maxX, minY, maxX, maxY, 2.5F, true, Color.BLACK);
				//Colored part of the ESP
				RenderUtil.INSTANCE.drawCorneredRect(minX, minY, maxX, maxY, 1.1f, esp.espColor.getObject().getColor().getRGB());
				DrawUtil.glDrawLine(minX, minY, minX, maxY, 1.25F, true, esp.espColor.getObject().getColor());
				DrawUtil.glDrawLine(maxX, minY, maxX, maxY, 1.25F, true, esp.espColor.getObject().getColor());
				break;
		}
	}

	private void convertAABTo2D(AxisAlignedBB interpolatedBB, double[][] vector2ds) {
		vector2ds[0] = convertTo2D(interpolatedBB.minX - RenderManager.renderPosX, interpolatedBB.minY - RenderManager.renderPosY,
				interpolatedBB.minZ - RenderManager.renderPosZ);
		vector2ds[1] = convertTo2D(interpolatedBB.minX - RenderManager.renderPosX, interpolatedBB.minY - RenderManager.renderPosY,
				interpolatedBB.maxZ - RenderManager.renderPosZ);
		vector2ds[2] = convertTo2D(interpolatedBB.minX - RenderManager.renderPosX, interpolatedBB.maxY - RenderManager.renderPosY,
				interpolatedBB.minZ - RenderManager.renderPosZ);
		vector2ds[3] = convertTo2D(interpolatedBB.maxX - RenderManager.renderPosX, interpolatedBB.minY - RenderManager.renderPosY,
				interpolatedBB.minZ - RenderManager.renderPosZ);
		vector2ds[4] = convertTo2D(interpolatedBB.maxX - RenderManager.renderPosX, interpolatedBB.maxY - RenderManager.renderPosY,
				interpolatedBB.minZ - RenderManager.renderPosZ);
		vector2ds[5] = convertTo2D(interpolatedBB.maxX - RenderManager.renderPosX, interpolatedBB.minY - RenderManager.renderPosY,
				interpolatedBB.maxZ - RenderManager.renderPosZ);
		vector2ds[6] = convertTo2D(interpolatedBB.minX - RenderManager.renderPosX, interpolatedBB.maxY - RenderManager.renderPosY,
				interpolatedBB.maxZ - RenderManager.renderPosZ);
		vector2ds[7] = convertTo2D(interpolatedBB.maxX - RenderManager.renderPosX, interpolatedBB.maxY - RenderManager.renderPosY,
				interpolatedBB.maxZ - RenderManager.renderPosZ);
	}


	private double[] convertTo2D(final double x, final double y, final double z) {
		boolean result = GLU.gluProject((float) x, (float) y, (float) z, ActiveRenderInfo.getModelView(), ActiveRenderInfo.getProjection(),
				ActiveRenderInfo.getViewPort(), ActiveRenderInfo.getObjectCords());
		final ScaledResolution sr = new ScaledResolution(mc);
		if (result)
			return new double[]{ActiveRenderInfo.getObjectCords().get(0) / sr.getScaleFactor(), ActiveRenderInfo.getObjectCords().get(1) / sr.getScaleFactor(),
					ActiveRenderInfo.getObjectCords().get(2)};
		return null;
	}
}
