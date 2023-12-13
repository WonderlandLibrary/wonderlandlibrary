package com.enjoytheban.module.modules.render;

import java.awt.Color;
import java.util.ArrayList;

import org.lwjgl.opengl.GL11;

import com.enjoytheban.api.EventHandler;
import com.enjoytheban.api.events.rendering.EventRender2D;
import com.enjoytheban.api.value.Mode;
import com.enjoytheban.management.FriendManager;
import com.enjoytheban.module.Module;
import com.enjoytheban.module.ModuleType;
import com.enjoytheban.utils.math.Vec3f;
import com.enjoytheban.utils.render.RenderUtil;
import com.enjoytheban.utils.render.gl.GLUtils;

import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.AxisAlignedBB;

/**
 * A 2D ESP + Outline
 * 
 * @author Fth
 */

public class ESP extends Module {

	private ArrayList<Vec3f> points = new ArrayList<>();
	public Mode<Enum> mode = new Mode("Mode", "mode", ESPMode.values(), ESPMode.TwoDimensional);

	public ESP() {
		super("ESP", new String[] { "outline", "wallhack" }, ModuleType.Render);
		addValues(mode);
		for (int i = 0; i < 8; i++) {
			// Add a new empty vector
			points.add(new Vec3f());
		}
		setColor(new Color(random.nextInt(255), random.nextInt(255), random.nextInt(255)).getRGB());
	}

	@EventHandler
	public void onScreen(EventRender2D eventRender) {
		if (mode.getValue() == ESPMode.TwoDimensional) {
			GlStateManager.pushMatrix();
			ScaledResolution scaledRes = new ScaledResolution(mc);
			double twoDscale = scaledRes.getScaleFactor() / Math.pow(scaledRes.getScaleFactor(), 2.0);
			GlStateManager.scale(twoDscale, twoDscale, twoDscale);
			for (Object o : this.mc.theWorld.getLoadedEntityList()) {
				if ((o instanceof EntityLivingBase) && o != mc.thePlayer && o instanceof EntityPlayer) {
					EntityLivingBase ent = (EntityLivingBase) o;
					render(ent);
				}
			}
			GlStateManager.popMatrix();
		}
	}

	/**
	 * Handles the math and rendering, renders on the entity specified.
	 *
	 * @param entity
	 *            entity to render on
	 */
	private void render(Entity entity) {
		// Create a casted object to IEntity from the entity
		Entity extended = entity;
		// Get render manager
		RenderManager renderManager = mc.getRenderManager();

		// Interpolated position
		Vec3f offset = extended.interpolate(mc.timer.renderPartialTicks).sub(extended.getPos()).add(0, 0.1, 0);
		if(entity.isInvisible())
			return;
		// Bounding box using the interpolated position subtracted by the render
		// position
		AxisAlignedBB bb = entity.getEntityBoundingBox().offset(offset.getX() - renderManager.renderPosX,
				offset.getY() - renderManager.renderPosY, offset.getZ() - renderManager.renderPosZ);

		// Sets up all possible screen possibilities on the current bounding box
		points.get(0).setX(bb.minX).setY(bb.minY).setZ(bb.minZ);
		points.get(1).setX(bb.maxX).setY(bb.minY).setZ(bb.minZ);
		points.get(2).setX(bb.maxX).setY(bb.minY).setZ(bb.maxZ);
		points.get(3).setX(bb.minX).setY(bb.minY).setZ(bb.maxZ);
		points.get(4).setX(bb.minX).setY(bb.maxY).setZ(bb.minZ);
		points.get(5).setX(bb.maxX).setY(bb.maxY).setZ(bb.minZ);
		points.get(6).setX(bb.maxX).setY(bb.maxY).setZ(bb.maxZ);
		points.get(7).setX(bb.minX).setY(bb.maxY).setZ(bb.maxZ);

		// Initialise some required fields, left and top start at MAX_VALUE because we
		// will
		// decrease them for each screen point. If they would be 0 it would not find
		// the correct position
		float left = Float.MAX_VALUE, right = 0, top = Float.MAX_VALUE, bottom = 0;

		// For every point
		for (Vec3f point : points) {
			// Get the screen position
			Vec3f screen = point.toScreen();
			// If the position is not on screen
			if (screen.getZ() < 0 || screen.getZ() >= 1)
				// Continue to the next point
				continue;

			// Sets left, top, right and bottom according to the current point, at the last
			// point
			// it should be perfectly aligned with the entities bounding box.
			if (screen.getX() < left)
				left = (float) screen.getX();
			if (screen.getY() < top)
				top = (float) screen.getY();
			if (screen.getX() > right)
				right = (float) screen.getX();
			if (screen.getY() > bottom)
				bottom = (float) screen.getY();
		}

		// If bottom or right are greater than 1,
		// this will never occur normally
		if (!(bottom > 1 || right > 1))
			// Don't continue
			return;

		// If box is enabled
		// Render the box from the current positions
		box(left, top, right, bottom);

		// If name is enabled
		// Render the name from the current positions
		name(entity, left, top, right, bottom);

		// If the entity is not a living entity
		if (!(entity instanceof EntityLivingBase))
			// Don't continue
			return;
		// Create a casted object to EntityLivingBase from the entity
		EntityLivingBase living = (EntityLivingBase) entity;
		// If health is enabled
		// Render the health bar from the current positions
		health(living, left, top, right, bottom);
	}

	/**
	 * Renders a bounding box from left, top, right and bottom positions
	 *
	 * @param left
	 *            left position
	 * @param top
	 *            top position
	 * @param right
	 *            right position
	 * @param bottom
	 *            bottom position
	 */
	private void box(float left, float top, float right, float bottom) {
		// Set the color to white, this is the base bounding box color
		// TODO: changeable color
		GL11.glColor4d(1.0, 1.0, 1.0, 0.5);

		// Draws a rectangle from the positions with the 2 value
		RenderUtil.drawLine(left, top, right, top, 2); // Top
		RenderUtil.drawLine(left, bottom, right, bottom, 2); // Bottom
		RenderUtil.drawLine(left, top, left, bottom, 2); // Left
		RenderUtil.drawLine(right, top, right, bottom, 2); // Right

		// Set the color to dark gray, this is the border color
		// TODO: changeable color
		// GLUtils.glColor(Color.darkGray.hashCode());

		// Draws a inside border from the positions with the border 2 value
		RenderUtil.drawLine(left + 1, top + 1, right - 1, top + 1, 1); // Top
		RenderUtil.drawLine(left + 1, bottom - 1, right - 1, bottom - 1, 1); // Bottom
		RenderUtil.drawLine(left + 1, top + 1, left + 1, bottom - 1, 1); // Left
		RenderUtil.drawLine(right - 1, top + 1, right - 1, bottom - 1, 1); // Right

		// Draws a outside border from the positions with the border 2 value
		RenderUtil.drawLine(left - 1, top - 1, right + 1, top - 1, 1); // Top
		RenderUtil.drawLine(left - 1, bottom + 1, right + 1, bottom + 1, 1); // Bottom
		RenderUtil.drawLine(left - 1, top + 1, left - 1, bottom + 1, 1); // Left
		RenderUtil.drawLine(right + 1, top - 1, right + 1, bottom + 1, 1); // Right
	}

	/**
	 * Renders a name tag from the left, top, right and bottom positions TODO:
	 * friends implementation
	 *
	 * @param entity
	 *            entity to get the name from
	 * @param left
	 *            left position
	 * @param top
	 *            top position
	 * @param right
	 *            right position
	 * @param bottom
	 *            bottom position
	 */
	private void name(Entity entity, float left, float top, float right, float bottom) {
		// Render a string using the positions
		mc.fontRendererObj.drawCenteredString(FriendManager.isFriend(entity.getName()) ? "\247b" + FriendManager.getAlias(entity.getName()) : entity.getName(), (int)(left + right) / 2,
				(int) (top - mc.fontRendererObj.FONT_HEIGHT - 2 + 1), -1);
        if (((EntityPlayer)entity).getCurrentEquippedItem() != null)
        {
          String stack = ((EntityPlayer)entity).getCurrentEquippedItem().getDisplayName();
          this.mc.fontRendererObj.drawCenteredString(stack, (int)(left + right) / 2, (int) bottom, -1);
        }
	}

	/**
	 * Renders a health bar from an entity with left, top, right and bottom
	 * positions
	 *
	 * @param entity
	 *            entity to get the health from
	 * @param left
	 *            left position
	 * @param top
	 *            top position
	 * @param right
	 *            right position
	 * @param bottom
	 *            bottom position
	 */
	private void health(EntityLivingBase entity, float left, float top, float right, float bottom) {
		// Get total height
		float height = bottom - top;

		// The entities current health
		float currentHealth = entity.getHealth();
		// The entities maximum health
		float maxHealth = entity.getMaxHealth();
		// The entities current percentage of health
		float healthPercent = currentHealth / maxHealth;

		// Set the color to white
		// TODO: get color from health
		GLUtils.glColor(getHealthColor(entity));

		// Render a vertical line with the health
		/// TODO: outline
		RenderUtil.drawLine(left - (4 + 1), top + (height * (1 - healthPercent)) + 1, left - (4 + 1), bottom, 2);
		//mc.fontRendererObj.drawCenteredString("" + entity.getHealth() * 5, (int) (left - (19 + 1)), (int) (top + (height * (1 - healthPercent)) + 1), -1);
	}

	/**
	 * Returns the health color
	 * @param player
	 */
    private int getHealthColor(EntityLivingBase player) {
        float f = player.getHealth();
        float f1 = player.getMaxHealth();
        float f2 = Math.max(0.0F, Math.min(f, f1) / f1);
        return Color.HSBtoRGB(f2 / 3.0F, 1.0F, 1.0F) | 0xFF000000;
    }
	
	public enum ESPMode {
		Outline, TwoDimensional
	}
}