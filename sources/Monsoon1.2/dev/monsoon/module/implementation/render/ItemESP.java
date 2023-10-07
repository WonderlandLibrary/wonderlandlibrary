package dev.monsoon.module.implementation.render;

import dev.monsoon.event.Event;
import dev.monsoon.event.listeners.EventRender3D;
import dev.monsoon.module.base.Module;
import dev.monsoon.module.setting.impl.ModeSetting;
import dev.monsoon.util.render.RenderUtil;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.entity.Entity;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.Vec3;
import org.lwjgl.input.Keyboard;
import org.lwjgl.opengl.GL11;
import dev.monsoon.module.enums.Category;

import java.awt.*;

public class ItemESP extends Module {

	public ModeSetting mode = new ModeSetting("Mode", this, "Box",  "Box");
	public ModeSetting color = new ModeSetting("Color", this, "Blue",  "Red", "Blue", "Orange", "Green", "White", "Purple");

	public ItemESP() {
		super("ItemESP", Keyboard.KEY_NONE, Category.RENDER);
		this.addSettings(mode,color);
	}

	Color espColor;
	int opacityGradient;


	public void onEnable() {

	}

	public void onDisable() {

	}



	public void onEvent(Event event) {
		if(event instanceof EventRender3D && this.mode.is("Box")) {
			float viewerYaw = (mc.getRenderManager()).playerViewY;
			for(Object o : mc.theWorld.loadedEntityList) {
				if(o instanceof EntityItem) {
					espBox((Entity) o,0,1,1);

					if(this.color.is("Red")) {
						espBox((Entity) o,1,0,0);
					}
					if(this.color.is("Blue")) {
						espBox((Entity) o,0,1,1);
					}
					if(this.color.is("Orange")) {
						espBox((Entity) o,1,0.5f,0);
					}
					if(this.color.is("Green")) {
						espBox((Entity) o,0,1,0);
					}
					if(this.color.is("Black")) {
						espBox((Entity) o,0,0,0);
					}
					if(this.color.is("White")) {
						espBox((Entity) o,1,1,1);
					}
					if(this.color.is("Purple")) {
						espBox((Entity) o,1,0,1);
					}
				}
			}
		}
	}

	public static Vec3 getInterpolatedPos(Entity entity, float ticks) {
		return (new Vec3(entity.lastTickPosX, entity.lastTickPosY, entity.lastTickPosZ)).add(getInterpolatedAmount(entity, ticks));
	}

	public static Vec3 getInterpolatedAmount(Entity entity, double ticks) {
		return getInterpolatedAmount(entity, ticks, ticks, ticks);
	}

	public static Vec3 getInterpolatedAmount(Entity entity, double x, double y, double z) {
		return new Vec3((entity.posX - entity.lastTickPosX) * x, (entity.posY - entity.lastTickPosY) * y, (entity.posZ - entity.lastTickPosZ) * z);
	}

	public static void espBox(Entity entity, float r, float g, float b)
	{
		GL11.glBlendFunc(770, 771);
		GL11.glEnable(GL11.GL_BLEND);
		GL11.glLineWidth(2.0F);
		GL11.glDisable(GL11.GL_TEXTURE_2D);
		GL11.glDisable(GL11.GL_DEPTH_TEST);
		GL11.glDepthMask(false);
		Minecraft.getMinecraft().getRenderManager();
		RenderUtil.drawAxisAlignedBBFilled(
				new AxisAlignedBB(
						entity.boundingBox.minX
								- 0.05
								- entity.posX
								+ (entity.posX - Minecraft.getMinecraft()
								.getRenderManager().renderPosX),
						entity.boundingBox.minY
								- entity.posY
								+ (entity.posY - Minecraft.getMinecraft()
								.getRenderManager().renderPosY),
						entity.boundingBox.minZ
								- 0.05
								- entity.posZ
								+ (entity.posZ - Minecraft.getMinecraft()
								.getRenderManager().renderPosZ),
						entity.boundingBox.maxX
								+ 0.05
								- entity.posX
								+ (entity.posX - Minecraft.getMinecraft()
								.getRenderManager().renderPosX),
						entity.boundingBox.maxY
								+ 0.1
								- entity.posY
								+ (entity.posY - Minecraft.getMinecraft()
								.getRenderManager().renderPosY),
						entity.boundingBox.maxZ
								+ 0.05
								- entity.posZ
								+ (entity.posZ - Minecraft.getMinecraft()
								.getRenderManager().renderPosZ)),r,g,b,true);
		GL11.glEnable(GL11.GL_TEXTURE_2D);
		GL11.glEnable(GL11.GL_DEPTH_TEST);
		GL11.glDepthMask(true);
		GL11.glDisable(GL11.GL_BLEND);
	}
	
}
