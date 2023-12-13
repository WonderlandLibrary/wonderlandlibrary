package wtf.monsoon.impl.modules.visual;

import static org.lwjgl.opengl.GL11.glPopMatrix;
import static org.lwjgl.opengl.GL11.glPushMatrix;

import java.awt.Color;
import java.nio.FloatBuffer;
import java.nio.IntBuffer;
import java.util.HashMap;
import java.util.Map;

import org.lwjgl.BufferUtils;
import org.lwjgl.input.Keyboard;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL14;
import org.lwjgl.util.glu.GLU;
import org.lwjgl.util.vector.Vector3f;
import org.lwjgl.util.vector.Vector4f;

import wtf.monsoon.api.event.EventTarget;
import wtf.monsoon.api.event.impl.EventRender2D;
import wtf.monsoon.api.event.impl.EventRender3D;
import wtf.monsoon.api.module.Category;
import wtf.monsoon.api.module.Module;
import wtf.monsoon.api.setting.impl.ModeSetting;
import wtf.monsoon.api.util.render.DrawUtil;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.GLAllocation;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.Vec3;
import wtf.monsoon.api.Wrapper;

public class ESP extends Module {

	public ModeSetting mode = new ModeSetting("Mode", this, "2D",  "2D");
	public ModeSetting color = new ModeSetting("Color", this, "Blue",  "Red", "Blue", "Orange", "Green", "White", "Purple", "On/Off Ground");

	public ESP() {
		super("ESP", "ESP", Keyboard.KEY_NONE, Category.RENDER);
		this.addSettings(mode,color);
	}

	private final FloatBuffer windowPosition = BufferUtils.createFloatBuffer(4);
	private final IntBuffer viewport = GLAllocation.createDirectIntBuffer(16);
	private final FloatBuffer modelMatrix = GLAllocation.createDirectFloatBuffer(16);
	private final FloatBuffer projectionMatrix = GLAllocation.createDirectFloatBuffer(16);
	private final Map<EntityLivingBase, float[]> entityPosMap = new HashMap<>();
	private static Map<EntityLivingBase, float[][]> entities = new HashMap<>();


	public void onEnable() {
		super.onEnable();
	}
	
	public void onDisable() {
		super.onDisable();
	}
	
	@EventTarget
	public void onRender3D(EventRender3D e) {
		ScaledResolution sr = new ScaledResolution(Wrapper.mc);
		entities.keySet().removeIf(player -> !Wrapper.mc.theWorld.playerEntities.contains(player));
		if (!entityPosMap.isEmpty())
			entityPosMap.clear();
		boolean nigger = true;
		//if (nigger) {
		int scaleFactor = sr.getScaleFactor();
		for (Object fuck : Wrapper.mc.theWorld.playerEntities) {
			if (fuck instanceof EntityLivingBase) {
				EntityLivingBase player = (EntityLivingBase) fuck;
				if (player.getDistanceToEntity(Wrapper.mc.thePlayer) < 1.0F)
					continue;
				glPushMatrix();
				Vec3 vec3 = getVec3(player);
				float posX = (float) (vec3.xCoord - RenderManager.viewerPosX);
				float posY = (float) (vec3.yCoord - RenderManager.viewerPosY);
				float posZ = (float) (vec3.zCoord - RenderManager.viewerPosZ);
				double halfWidth = player.width / 2.0D + 0.18F;
				AxisAlignedBB bb = new AxisAlignedBB(posX - halfWidth, posY, posZ - halfWidth, posX + halfWidth,
						posY + player.height + 0.18D, posZ + halfWidth);
				double[][] vectors = {{bb.minX, bb.minY, bb.minZ}, {bb.minX, bb.maxY, bb.minZ},
						{bb.minX, bb.maxY, bb.maxZ}, {bb.minX, bb.minY, bb.maxZ}, {bb.maxX, bb.minY, bb.minZ},
						{bb.maxX, bb.maxY, bb.minZ}, {bb.maxX, bb.maxY, bb.maxZ}, {bb.maxX, bb.minY, bb.maxZ}};
				Vector3f projection;
				Vector4f position = new Vector4f(Float.MAX_VALUE, Float.MAX_VALUE, -1.0F, -1.0F);
				for (double[] vec : vectors) {
					projection = project2D((float) vec[0], (float) vec[1], (float) vec[2], scaleFactor);
					if (projection != null && projection.z >= 0.0F && projection.z < 1.0F) {
						position.x = Math.min(position.x, projection.x);
						position.y = Math.min(position.y, projection.y);
						position.z = Math.max(position.z, projection.x);
						position.w = Math.max(position.w, projection.y);
					}
				}
				entityPosMap.put(player, new float[]{position.x, position.z, position.y, position.w});
				GL11.glPopMatrix();
			}
		}
	}
	
	@EventTarget
	public void onRender2D(EventRender2D e) {
		for (EntityLivingBase player : entityPosMap.keySet()) {
			if(!player.isInvisible()) {
				glPushMatrix();
				float[] positions = entityPosMap.get(player);
				float x = positions[0];
				float x2 = positions[1];
				float y = positions[2];
				float y2 = positions[3];
				Gui.drawRect(x - 2.5, y - 0.5F, x - 0.5F, y2 + 0.5F, 0x96000000);
				float health = player.getHealth();
				float maxHealth = player.getMaxHealth();
				float healthPercentage = health / maxHealth;
				boolean needScissor = health < maxHealth;
				float heightDif = y - y2;
				float healthBarHeight = heightDif * healthPercentage;
				if (needScissor)
					startScissorBox(((EventRender2D) e).sr, (int) x - 2, (int) (y2 + healthBarHeight), 2, (int) -healthBarHeight + 1);

				int col = getColorFromPercentage(health, maxHealth);
				Gui.drawRect(x - 2, y, x - 1, y2, col);

				if (needScissor)
					endScissorBox();

				GL11.glDisable(GL11.GL_TEXTURE_2D);
				enableAlpha();
				disableAlpha();
				DrawUtil.drawHollowRect(x - 0.5f, y - 0.5f, x2 - 0.5f, y2 - 0.5f, 0x96000000);
				DrawUtil.drawHollowRect(x + 0.5f, y + 0.5f, x2 + 0.5f, y2 + 0.5f, 0x96000000);
				DrawUtil.drawHollowRect(x, y, x2, y2, new Color(0, 140, 255).getRGB());
				//DrawUtil.drawHollowRect(x, y, x2 + 1, y2 + 1,0x96000000);
				GL11.glEnable(GL11.GL_TEXTURE_2D);

				glPopMatrix();
			}
		}
	}
	
	
	public static int getColorFromPercentage(float current, float max) {
		float percentage = (current / max) / 3;
		return Color.HSBtoRGB(percentage, 1.0F, 1.0F);
	}

	private Vector3f project2D(float x, float y, float z, int scaleFactor) {
		GL11.glGetFloat(GL11.GL_MODELVIEW_MATRIX, modelMatrix);
		GL11.glGetFloat(GL11.GL_PROJECTION_MATRIX, projectionMatrix);
		GL11.glGetInteger(GL11.GL_VIEWPORT, viewport);
		if (GLU.gluProject(x, y, z, modelMatrix, projectionMatrix, viewport, windowPosition)) {
			return new Vector3f(windowPosition.get(0) / scaleFactor,
					(Wrapper.mc.displayHeight - windowPosition.get(1)) / scaleFactor, windowPosition.get(2));
		}

		return null;
	}

	public static void enableAlpha() {
		GL11.glEnable(GL11.GL_BLEND);
		GL14.glBlendFuncSeparate(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA, 1, 0);
	}

	public static void disableAlpha() {
		GL11.glDisable(GL11.GL_BLEND);
	}

	public static void startScissorBox(ScaledResolution sr, int x, int y, int width, int height) {
		int sf = sr.getScaleFactor();
		GL11.glEnable(GL11.GL_SCISSOR_TEST);
		GL11.glScissor(x * sf, (sr.getScaledHeight() - (y + height)) * sf, width * sf, height * sf);
	}

	public static void endScissorBox() {
		GL11.glDisable(GL11.GL_SCISSOR_TEST);
	}

	private Vec3 getVec3(final EntityLivingBase var0) {
		final float timer = Wrapper.mc.timer.renderPartialTicks;
		final double x = var0.lastTickPosX + (var0.posX - var0.lastTickPosX) * timer;
		final double y = var0.lastTickPosY + (var0.posY - var0.lastTickPosY) * timer;
		final double z = var0.lastTickPosZ + (var0.posZ - var0.lastTickPosZ) * timer;
		return new Vec3(x, y, z);
	}

	public static Vec3 getInterpolatedAmount(Entity entity, double ticks) {
		return getInterpolatedAmount(entity, ticks, ticks, ticks);
	}

	public static Vec3 getInterpolatedAmount(Entity entity, double x, double y, double z) {
		return new Vec3((entity.posX - entity.lastTickPosX) * x, (entity.posY - entity.lastTickPosY) * y, (entity.posZ - entity.lastTickPosZ) * z);
	}
	
}
