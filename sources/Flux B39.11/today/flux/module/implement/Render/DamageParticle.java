package today.flux.module.implement.Render;

import com.darkmagician6.eventapi.EventTarget;

import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.util.EnumChatFormatting;
import today.flux.Flux;
import today.flux.event.LivingUpdateEvent;
import today.flux.event.RespawnEvent;
import today.flux.event.TickEvent;
import today.flux.event.WorldRenderEvent;
import today.flux.module.Category;
import today.flux.module.Module;
import today.flux.utility.Location;
import today.flux.utility.MathUtils;
import today.flux.utility.WorldRenderUtils;

import org.lwjgl.opengl.GL11;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Random;

/**
 * Created by John on 2017/06/24.
 */
public class DamageParticle extends Module {
	public DamageParticle() {
		super("DMGParticle", Category.Render, false);
	}

	private HashMap<EntityLivingBase, Float> healthMap = new HashMap<>();
	private List<Particle> particles = new ArrayList<>();

	@EventTarget
	public void onRespawn(RespawnEvent event) {
		this.particles.clear();
		this.healthMap.clear();
	}

	@EventTarget
	public void onTick(TickEvent event) {
		// move particles
		particles.forEach(particle -> {
			particle.ticks++;

			if (particle.ticks <= 10) {
				particle.location.setY(particle.location.getY() + particle.ticks * 0.005);
			}

			if (particle.ticks > 20) {
				particles.remove(particle);
			}
		});
	}

	@EventTarget
	public void onLivingUpdate(LivingUpdateEvent event) {
		final EntityLivingBase entity = event.getEntity();

		if (entity == this.mc.thePlayer)
			return;

		// detect

		if (!healthMap.containsKey(entity))
			healthMap.put(entity, entity.getHealth());

		final float before = healthMap.get(entity);
		final float after = entity.getHealth();

		if (before != after) {
			String text;

			if ((before - after) < 0) {
				text = EnumChatFormatting.GREEN + "" + MathUtils.roundToPlace((before - after) * -1, 1);
			} else {
				text = EnumChatFormatting.YELLOW + "" + MathUtils.roundToPlace((before - after), 1);
			}

			Location location = new Location(entity);

			location.setY(entity.getEntityBoundingBox().minY
					+ ((entity.getEntityBoundingBox().maxY - entity.getEntityBoundingBox().minY) / 2));

			location.setX((location.getX() - 0.5) + (new Random(System.currentTimeMillis()).nextInt(5) * 0.1));
			location.setZ((location.getZ() - 0.5) + (new Random(System.currentTimeMillis() + 1).nextInt(5) * 0.1));

			particles.add(new Particle(location, text));

			healthMap.remove(entity);
			healthMap.put(entity, entity.getHealth());
		}
	}

	@EventTarget
	public void onRenderWorld(WorldRenderEvent event) {
		for (Particle particle : this.particles) {
			final double x = particle.location.getX() - this.mc.getRenderManager().getRenderPosX();
			final double y = particle.location.getY() - this.mc.getRenderManager().getRenderPosY();
			final double z = particle.location.getZ() - this.mc.getRenderManager().getRenderPosZ();

			GlStateManager.pushMatrix();

			GlStateManager.enablePolygonOffset();
			GlStateManager.doPolygonOffset(1.0F, -1500000.0F);

			GlStateManager.translate((float) x, (float) y, (float) z);
			GlStateManager.rotate(-this.mc.getRenderManager().playerViewY, 0.0F, 1.0F, 0.0F);
			float var10001 = this.mc.gameSettings.thirdPersonView == 2 ? -1.0F : 1.0F;
			GlStateManager.rotate(this.mc.getRenderManager().playerViewX, var10001, 0.0F, 0.0F);
			double scale = 0.03;
			GlStateManager.scale(-scale, -scale, scale);

			WorldRenderUtils.enableGL2D();
			WorldRenderUtils.disableGL2D();

			GL11.glDepthMask(false);
			Flux.font.drawStringWithShadow(particle.text,
					-(this.mc.fontRendererObj.getStringWidth(particle.text) / 2),
					-(this.mc.fontRendererObj.FONT_HEIGHT - 1), 0);
			GL11.glColor4f(1.0f, 1.0f, 1.0f, 1.0f);
			GL11.glDepthMask(true);

			GlStateManager.doPolygonOffset(1.0F, 1500000.0F);
			GlStateManager.disablePolygonOffset();

			GlStateManager.popMatrix();
		}
	}
}

class Particle {
	public Particle(Location location, String text) {
		this.location = location;
		this.text = text;
		this.ticks = 0;
	}

	public int ticks;
	public Location location;
	public String text;
}
