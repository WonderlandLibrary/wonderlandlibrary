package sudo.module.render;

import java.awt.Color;

import org.lwjgl.opengl.GL11;

import com.mojang.blaze3d.systems.RenderSystem;

import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.client.render.BufferBuilder;
import net.minecraft.client.render.BufferRenderer;
import net.minecraft.client.render.Camera;
import net.minecraft.client.render.GameRenderer;
import net.minecraft.client.render.Tessellator;
import net.minecraft.client.render.VertexFormat;
import net.minecraft.client.render.VertexFormats;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.Entity;
import net.minecraft.entity.ItemEntity;
import net.minecraft.entity.mob.Monster;
import net.minecraft.entity.passive.PassiveEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.Formatting;
import net.minecraft.util.math.Matrix4f;
import net.minecraft.util.math.Vec3d;
import sudo.events.EventRender3D;
import sudo.module.Mod;
import sudo.module.settings.BooleanSetting;
import sudo.module.settings.ColorSetting;
import sudo.module.settings.ModeSetting;
import sudo.utils.render.RenderUtils;

public class ESP extends Mod {

	public static ModeSetting mode = new ModeSetting("Mode", "Rect", "Rect", "Box", "Glow");

	
	public BooleanSetting health = new BooleanSetting("Health", true);
	public BooleanSetting players = new BooleanSetting("Players", true);
	public BooleanSetting monsters = new BooleanSetting("Monsters", true);
	public BooleanSetting passives = new BooleanSetting("Passives", true);
	public BooleanSetting invisibles = new BooleanSetting("Invisibles", true);
	public BooleanSetting items = new BooleanSetting("Items", true);
	
	public ColorSetting color = new ColorSetting("Color", new Color(255, 0, 0));

	
	public ESP() {
		super("ESP", "Get the visual position of entities", Category.RENDER, 0);
		addSettings(mode, health, players,monsters,passives,invisibles,items, color);
	}
	
	private static final Formatting Gray = Formatting.GRAY;
	
	@Override
	public void onTick() {
		this.setDisplayName("ESP" + Gray + " ["+mode.getMode()+"] ");
		if (mode.is("Rect")) health.setVisible(true);
		else health.setVisible(false);
	}
	
	@Override
	public void onWorldRender(MatrixStack matrices) {
		if (this.isEnabled()) {
			for (Entity e  : mc.world.getEntities()) {
				if (!(e instanceof ClientPlayerEntity)) {
					if (shouldRenderEntity(e)) {
						if (mode.is("Rect")) {
							RenderUtils.renderOutlineRect(e, color.getColor(), matrices);
							if (health.isEnabled()) {
								for (PlayerEntity entity : mc.world.getPlayers()) {
									//health on the side
									if (!(entity instanceof ClientPlayerEntity)) {

										renderHealthBG(entity, new Color(0, 0, 0, 255), matrices);
										if (entity.getHealth()>13) renderHealth(entity, new Color(0, 255, 0), matrices);
										if (entity.getHealth()>8 && entity.getHealth()<=13) renderHealth(entity, new Color(255, 255, 0), matrices);
										if (entity.getHealth()<=8) renderHealth(entity, new Color(255, 0, 0), matrices);
										renderHealthOutline(entity, new Color(0, 0, 0), matrices);
									}
								}
							}
						}
						Vec3d renderPos = RenderUtils.getEntityRenderPosition(e, EventRender3D.getTickDelta());
						if (mode.is("Box")) {
							RenderUtils.drawEntityBox(matrices, e, renderPos.x, renderPos.y, renderPos.z, color.getColor());
						}
						if (mode.is("Glow")) {
							
						}
					}
				}
			}
		}
		super.onWorldRender(matrices);
	}
	
	@Override
	public void onEnable() {super.onEnable();}
	@Override
	public void onDisable() {super.onDisable();}
	
	public boolean shouldRenderEntity(Entity entity) {
		if (players.isEnabled() && entity instanceof PlayerEntity) return true;
		if (monsters.isEnabled() && entity instanceof Monster) return true;
		if (passives.isEnabled() && (entity instanceof PassiveEntity))return true;
		if (invisibles.isEnabled() && entity.isInvisible()) return true;
		if (items.isEnabled() && entity instanceof ItemEntity) return true;
		return false;
	}
	

	void renderHealthOutline(PlayerEntity e, Color color, MatrixStack stack) {
        float red = color.getRed() / 255f;
        float green = color.getGreen() / 255f;
        float blue = color.getBlue() / 255f;
        float alpha = color.getAlpha() / 255f;
        Camera c = mc.gameRenderer.getCamera();
        Vec3d camPos = c.getPos();
        Vec3d start = e.getPos().subtract(camPos);
        float x = (float) start.x;
        float y = (float) start.y;
        float z = (float) start.z;

        double r = Math.toRadians(-c.getYaw() + 90);
        float sin = (float) (Math.sin(r) * (e.getWidth() / 20));
        float cos = (float) (Math.cos(r) * (e.getWidth() / 20));
        stack.push();

        Matrix4f matrix = stack.peek().getPositionMatrix();
        BufferBuilder buffer = Tessellator.getInstance().getBuffer();
        RenderSystem.setShader(GameRenderer::getPositionColorShader);
        GL11.glDepthFunc(GL11.GL_ALWAYS);
        RenderSystem.setShaderColor(1f, 1f, 1f, 1f);
        RenderSystem.defaultBlendFunc();
        RenderSystem.enableBlend();
        buffer.begin(VertexFormat.DrawMode.DEBUG_LINES,
                VertexFormats.POSITION_COLOR);

        buffer.vertex(matrix, x + sin+ (float) (Math.sin(r) * (e.getWidth()-0.2)), y, z + cos + (float) (Math.cos(r) * (e.getWidth()-0.2))).color(red, green, blue, alpha).next();
        buffer.vertex(matrix, x - sin+ (float) (Math.sin(r) * (e.getWidth()-0.2)), y, z - cos + (float) (Math.cos(r) * (e.getWidth()-0.2))).color(red, green, blue, alpha).next();
        buffer.vertex(matrix, x - sin+ (float) (Math.sin(r) * (e.getWidth()-0.2)), y, z - cos + (float) (Math.cos(r) * (e.getWidth()-0.2))).color(red, green, blue, alpha).next();
        buffer.vertex(matrix, x - sin+ (float) (Math.sin(r) * (e.getWidth()-0.2)), y + e.getHeight(), z - cos + (float) (Math.cos(r) * (e.getWidth()-0.2))).color(red, green, blue, alpha).next();
        buffer.vertex(matrix, x - sin+ (float) (Math.sin(r) * (e.getWidth()-0.2)), y + e.getHeight(), z - cos + (float) (Math.cos(r) * (e.getWidth()-0.2))).color(red, green, blue, alpha).next();
        buffer.vertex(matrix, x + sin+ (float) (Math.sin(r) * (e.getWidth()-0.2)), y + e.getHeight(), z + cos + (float) (Math.cos(r) * (e.getWidth()-0.2))).color(red, green, blue, alpha).next();
        buffer.vertex(matrix, x + sin+ (float) (Math.sin(r) * (e.getWidth()-0.2)), y + e.getHeight(), z + cos + (float) (Math.cos(r) * (e.getWidth()-0.2))).color(red, green, blue, alpha).next();
        buffer.vertex(matrix, x + sin+ (float) (Math.sin(r) * (e.getWidth()-0.2)), y, z + cos + (float) (Math.cos(r) * (e.getWidth()-0.2))).color(red, green, blue, alpha).next();
        buffer.vertex(matrix, x + sin+ (float) (Math.sin(r) * (e.getWidth()-0.2)), y, z + cos + (float) (Math.cos(r) * (e.getWidth()-0.2))).color(red, green, blue, alpha).next();
        
        BufferRenderer.drawWithShader(buffer.end());
        GL11.glDepthFunc(GL11.GL_LEQUAL);
        RenderSystem.disableBlend();
        stack.pop();
    }
	
	void renderHealth(PlayerEntity e, Color color, MatrixStack stack) {
        float red = color.getRed() / 255f;
        float green = color.getGreen() / 255f;
        float blue = color.getBlue() / 255f;
        float alpha = color.getAlpha() / 255f;
        Camera c = mc.gameRenderer.getCamera();
        Vec3d camPos = c.getPos();
        Vec3d start = e.getPos().subtract(camPos);
        float x = (float) start.x;
        float y = (float) start.y;
        float z = (float) start.z;

        double r = Math.toRadians(-c.getYaw() + 90);
        float sin = (float) (Math.sin(r) * (e.getWidth() / 20));
        float cos = (float) (Math.cos(r) * (e.getWidth() / 20));
        stack.push();

        Matrix4f matrix = stack.peek().getPositionMatrix();
        BufferBuilder buffer = Tessellator.getInstance().getBuffer();
        RenderSystem.setShader(GameRenderer::getPositionColorShader);
        GL11.glDepthFunc(GL11.GL_ALWAYS);
        RenderSystem.setShaderColor(1f, 1f, 1f, (float) 0.50);
        RenderSystem.defaultBlendFunc();
        RenderSystem.enableBlend();   //debug lines
        buffer.begin(VertexFormat.DrawMode.QUADS,
                VertexFormats.POSITION_COLOR);

        buffer.vertex(matrix, x + sin+ (float) (Math.sin(r) * (e.getWidth()-0.2)), y, z + cos + (float) (Math.cos(r) * (e.getWidth()-0.2))).color(red, green, blue, alpha).next();
        buffer.vertex(matrix, x - sin+ (float) (Math.sin(r) * (e.getWidth()-0.2)), y, z - cos + (float) (Math.cos(r) * (e.getWidth()-0.2))).color(red, green, blue, alpha).next();
        buffer.vertex(matrix, x - sin+ (float) (Math.sin(r) * (e.getWidth()-0.2)), y, z - cos + (float) (Math.cos(r) * (e.getWidth()-0.2))).color(red, green, blue, alpha).next();
        buffer.vertex(matrix, x - sin+ (float) (Math.sin(r) * (e.getWidth()-0.2)), y + e.getHeight()*(e.getHealth()/20), z - cos + (float) (Math.cos(r) * (e.getWidth()-0.2))).color(red, green, blue, alpha).next();
        buffer.vertex(matrix, x - sin+ (float) (Math.sin(r) * (e.getWidth()-0.2)), y + e.getHeight()*(e.getHealth()/20), z - cos + (float) (Math.cos(r) * (e.getWidth()-0.2))).color(red, green, blue, alpha).next();
        buffer.vertex(matrix, x + sin+ (float) (Math.sin(r) * (e.getWidth()-0.2)), y + e.getHeight()*(e.getHealth()/20), z + cos + (float) (Math.cos(r) * (e.getWidth()-0.2))).color(red, green, blue, alpha).next();
        buffer.vertex(matrix, x + sin+ (float) (Math.sin(r) * (e.getWidth()-0.2)), y + e.getHeight()*(e.getHealth()/20), z + cos + (float) (Math.cos(r) * (e.getWidth()-0.2))).color(red, green, blue, alpha).next();
        buffer.vertex(matrix, x + sin+ (float) (Math.sin(r) * (e.getWidth()-0.2)), y, z + cos + (float) (Math.cos(r) * (e.getWidth()-0.2))).color(red, green, blue, alpha).next();
        BufferRenderer.drawWithShader(buffer.end());
        GL11.glDepthFunc(GL11.GL_LEQUAL);
        RenderSystem.disableBlend();
        stack.pop();
	}
	
	
	void renderHealthBG(PlayerEntity e, Color color, MatrixStack stack) {
		float red = color.getRed() / 255f;
        float green = color.getGreen() / 255f;
        float blue = color.getBlue() / 255f;
        float alpha = color.getAlpha() / 255f;
        Camera c = mc.gameRenderer.getCamera();
        Vec3d camPos = c.getPos();
        Vec3d start = e.getPos().subtract(camPos);
        float x = (float) start.x;
        float y = (float) start.y;
        float z = (float) start.z;

        double r = Math.toRadians(-c.getYaw() + 90);
        float sin = (float) (Math.sin(r) * (e.getWidth() / 20));
        float cos = (float) (Math.cos(r) * (e.getWidth() / 20));
        stack.push();

        Matrix4f matrix = stack.peek().getPositionMatrix();
        BufferBuilder buffer = Tessellator.getInstance().getBuffer();
        RenderSystem.setShader(GameRenderer::getPositionColorShader);
        GL11.glDepthFunc(GL11.GL_ALWAYS);
        RenderSystem.setShaderColor(1f, 1f, 1f, 0.7f);
        RenderSystem.defaultBlendFunc();
        RenderSystem.enableBlend();   //debug lines
        buffer.begin(VertexFormat.DrawMode.QUADS,
                VertexFormats.POSITION_COLOR);

        buffer.vertex(matrix, x + sin+ (float) (Math.sin(r) * (e.getWidth()-0.2)), y, z + cos + (float) (Math.cos(r) * (e.getWidth()-0.2))).color(red, green, blue, alpha).next();
        buffer.vertex(matrix, x - sin+ (float) (Math.sin(r) * (e.getWidth()-0.2)), y, z - cos + (float) (Math.cos(r) * (e.getWidth()-0.2))).color(red, green, blue, alpha).next();
        buffer.vertex(matrix, x - sin+ (float) (Math.sin(r) * (e.getWidth()-0.2)), y, z - cos + (float) (Math.cos(r) * (e.getWidth()-0.2))).color(red, green, blue, alpha).next();
        buffer.vertex(matrix, x - sin+ (float) (Math.sin(r) * (e.getWidth()-0.2)), y + e.getHeight(), z - cos + (float) (Math.cos(r) * (e.getWidth()-0.2))).color(red, green, blue, alpha).next();
        buffer.vertex(matrix, x - sin+ (float) (Math.sin(r) * (e.getWidth()-0.2)), y + e.getHeight(), z - cos + (float) (Math.cos(r) * (e.getWidth()-0.2))).color(red, green, blue, alpha).next();
        buffer.vertex(matrix, x + sin+ (float) (Math.sin(r) * (e.getWidth()-0.2)), y + e.getHeight(), z + cos + (float) (Math.cos(r) * (e.getWidth()-0.2))).color(red, green, blue, alpha).next();
        buffer.vertex(matrix, x + sin+ (float) (Math.sin(r) * (e.getWidth()-0.2)), y + e.getHeight(), z + cos + (float) (Math.cos(r) * (e.getWidth()-0.2))).color(red, green, blue, alpha).next();
        buffer.vertex(matrix, x + sin+ (float) (Math.sin(r) * (e.getWidth()-0.2)), y, z + cos + (float) (Math.cos(r) * (e.getWidth()-0.2))).color(red, green, blue, alpha).next();
        BufferRenderer.drawWithShader(buffer.end());
        GL11.glDepthFunc(GL11.GL_LEQUAL);
        RenderSystem.disableBlend();
        stack.pop();
	}
}
