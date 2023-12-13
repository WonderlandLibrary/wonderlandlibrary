package best.azura.client.impl.module.impl.render.esp;

import best.azura.client.impl.events.EventRender2D;
import best.azura.eventbus.handler.EventHandler;
import best.azura.eventbus.handler.Listener;
import best.azura.client.util.color.ColorUtil;
import best.azura.client.util.math.MathUtil;
import best.azura.client.util.player.RotationUtil;
import best.azura.client.util.render.RenderUtil;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityOtherPlayerMP;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.MathHelper;
import net.minecraft.util.Vec3;

import static org.lwjgl.opengl.GL11.*;

@SuppressWarnings("unused")
public class OffscreenESP {
	
	private final Minecraft mc = Minecraft.getMinecraft();
	
	@EventHandler
	public Listener<EventRender2D> eventRender2DListener = eventRender2D -> {
		for (final Entity entity : mc.theWorld.loadedEntityList) {
			if (!(entity instanceof EntityPlayer) || entity == mc.thePlayer || !(entity instanceof EntityOtherPlayerMP) || entity.isInvisibleToPlayer(mc.thePlayer))
				continue;
			final double interpolatedX = entity.lastTickPosX + (entity.posX - entity.lastTickPosX) * mc.timer.renderPartialTicks,
					interpolatedY = entity.lastTickPosY + (entity.posY - entity.lastTickPosY) * mc.timer.renderPartialTicks,
					interpolatedZ = entity.lastTickPosZ + (entity.posZ - entity.lastTickPosZ) * mc.timer.renderPartialTicks;
			final float interpolatedRotation = RotationUtil.getNeededRotations(new Vec3(interpolatedX, interpolatedY, interpolatedZ))[0];
			final float diff = MathUtil.getDifference(MathHelper.wrapAngleTo180_float(mc.thePlayer.rotationYaw), interpolatedRotation),
					difference = MathHelper.wrapAngleTo180_float(diff);
			final float rotation = mc.thePlayer.prevRotationYaw + (mc.thePlayer.rotationYaw - mc.thePlayer.prevRotationYaw) * mc.timer.renderPartialTicks;
			RenderUtil.INSTANCE.scaleFix(1.0);
			GlStateManager.translate(mc.displayWidth / 2.0, mc.displayHeight / 2.0, 0);
			final float endRotation = rotation - interpolatedRotation + 90 + 180;
			
			final double width = 4, length = 15, offsetRender = 100,
					x = -Math.cos(Math.toRadians(endRotation + width)) * offsetRender, y = Math.sin(Math.toRadians(endRotation + width)) * offsetRender,
					x1 = -Math.cos(Math.toRadians(endRotation)) * (offsetRender + length), y1 = Math.sin(Math.toRadians(endRotation)) * (offsetRender + length),
					x2 = -Math.cos(Math.toRadians(endRotation - width)) * offsetRender, y2 = Math.sin(Math.toRadians(endRotation - width)) * offsetRender;
			
			glEnable(GL_BLEND);
			glEnable(GL_POLYGON_SMOOTH);
			glHint(GL_POLYGON_SMOOTH_HINT, GL_NICEST);
			glLineWidth(1F);
			glDisable(GL_TEXTURE_2D);
			glColor4f(ColorUtil.getColorFromHealth((EntityLivingBase) entity).getRed() / 255F,
					ColorUtil.getColorFromHealth((EntityLivingBase) entity).getGreen() / 255F,
					ColorUtil.getColorFromHealth((EntityLivingBase) entity).getBlue() / 255F,
					MathHelper.clamp_float(1, 0.1F, mc.thePlayer.getDistanceToEntity(entity)));
			glLineWidth(5);
			glBegin(GL_TRIANGLE_STRIP);
			glVertex2d(x, y);
			
			glVertex2d(x1, y1);
			
			glVertex2d(x2, y2);
			
			glVertex2d(x1, y1);
			
			glVertex2d(x, y);
			
			glVertex2d(x2, y2);
			
			glEnd();
			glDisable(GL_BLEND);
			
			//Disable the Line Anti-alias
			glDisable(GL_POLYGON_SMOOTH);
			glHint(GL_POLYGON_SMOOTH_HINT, GL_DONT_CARE);
			glEnable(GL_TEXTURE_2D);
			
			GlStateManager.translate(
					-mc.displayWidth / 2.0,
					-mc.displayHeight / 2.0, 0);
			RenderUtil.INSTANCE.invertScaleFix(1.0);
		}
	};
}