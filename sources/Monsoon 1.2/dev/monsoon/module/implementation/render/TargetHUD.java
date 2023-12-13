package dev.monsoon.module.implementation.render;

import dev.monsoon.Monsoon;
import dev.monsoon.event.Event;
import dev.monsoon.event.listeners.EventRender3D;
import dev.monsoon.module.base.Module;
import dev.monsoon.module.implementation.combat.Killaura;
import dev.monsoon.module.setting.impl.ModeSetting;
import dev.monsoon.module.setting.impl.NumberSetting;
import dev.monsoon.util.render.AnimationUtil;
import net.minecraft.client.entity.EntityOtherPlayerMP;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.gui.inventory.GuiInventory;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.WorldRenderer;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.util.MathHelper;
import org.lwjgl.input.Keyboard;

import java.awt.*;
import java.text.DecimalFormat;
import dev.monsoon.module.enums.Category;

public class TargetHUD extends Module {

	public ModeSetting mode = new ModeSetting("Mode", this,"Monsoon","Monsoon");
	public NumberSetting targetX = new NumberSetting("TargetHUD X", 508, 0, 1000, 1, this);
	public NumberSetting targetY = new NumberSetting("TargetHUD Y", 288, 0, 1000, 1, this);

	private EntityOtherPlayerMP target;
	private double healthBarWidth;
	private double hudHeight;
	public EntityLivingBase lastEnt;
	public float lastHealth;
	public float damageDelt;
	public float lastPlayerHealth;
	public float damageDeltToPlayer;
	public DecimalFormat format;
	public double animation;

	public TargetHUD() {
		super("TargetHUD", Keyboard.KEY_NONE, Category.RENDER);
		this.addSettings(mode,targetX,targetY);
	}
	
	public void onEnable() {
		
	}
	
	public void onDisable() {
		
	}


	public void renderExhi() {
		final Killaura aura = Monsoon.manager.killAura;
		ScaledResolution sr = new ScaledResolution(mc, mc.displayWidth, mc.displayHeight);
		final float scaledWidth = sr.getScaledWidth();
		final float scaledHeight = sr.getScaledHeight();
		if (aura.target != null && aura.isEnabled()) {
			if (aura.target instanceof EntityOtherPlayerMP) {
				float startX = 20;
				float renderX = (sr.getScaledWidth() / 2) + startX;
				float renderY = (sr.getScaledHeight() / 2) + 10;
				int maxX2 = 30;
				if (aura.target.getCurrentArmor(3) != null) {
					maxX2 += 15;
				}
				if (aura.target.getCurrentArmor(2) != null) {
					maxX2 += 15;
				}
				if (aura.target.getCurrentArmor(1) != null) {
					maxX2 += 15;
				}
				if (aura.target.getCurrentArmor(0) != null) {
					maxX2 += 15;
				}
				if (aura.target.getHeldItem() != null) {
					maxX2 += 15;
				}
				this.target = (EntityOtherPlayerMP) aura.target;
				final float width = 140.0f;
				final float height = 40.0f;
				final float xOffset = 40.0f;
				final float x = scaledWidth / 2.0f + 30.0f;
				final float y = scaledHeight / 2.0f + 30.0f;
				final float health = this.target.getHealth();
				double hpPercentage = health / this.target.getMaxHealth();
				hpPercentage = MathHelper.clamp_double(hpPercentage, 0.0, 1.0);
				final double hpWidth = 60.0 * hpPercentage;
				final int healthColor = ESP.getColorFromPercentage(this.target.getHealth(), this.target.getMaxHealth());
				final String healthStr = String.valueOf((int) this.target.getHealth() / 1.0f);
				int xAdd = 0;
				double multiplier = 0.85;
				GlStateManager.pushMatrix();
				GlStateManager.scale(multiplier, multiplier, multiplier);
				if (aura.target.getCurrentArmor(3) != null) {
					mc.getRenderItem().func_175042_a(aura.target.getCurrentArmor(3), (int) ((((sr.getScaledWidth() / 2) + startX + 33) + xAdd) / multiplier), (int) (((sr.getScaledHeight() / 2) + 56) / multiplier));
					xAdd += 15;
				}
				if (aura.target.getCurrentArmor(2) != null) {
					mc.getRenderItem().func_175042_a(aura.target.getCurrentArmor(2), (int) ((((sr.getScaledWidth() / 2) + startX + 33) + xAdd) / multiplier), (int) (((sr.getScaledHeight() / 2) + 56) / multiplier));
					xAdd += 15;
				}
				if (aura.target.getCurrentArmor(1) != null) {
					mc.getRenderItem().func_175042_a(aura.target.getCurrentArmor(1), (int) ((((sr.getScaledWidth() / 2) + startX + 33) + xAdd) / multiplier), (int) (((sr.getScaledHeight() / 2) + 56) / multiplier));
					xAdd += 15;
				}
				if (aura.target.getCurrentArmor(0) != null) {
					mc.getRenderItem().func_175042_a(aura.target.getCurrentArmor(0), (int) ((((sr.getScaledWidth() / 2) + startX + 33) + xAdd) / multiplier), (int) (((sr.getScaledHeight() / 2) + 56) / multiplier));
					xAdd += 15;
				}
				if (aura.target.getHeldItem() != null) {
					mc.getRenderItem().func_175042_a(aura.target.getHeldItem(), (int) ((((sr.getScaledWidth() / 2) + startX + 33) + xAdd) / multiplier), (int) (((sr.getScaledHeight() / 2) + 56) / multiplier));
				}
				GlStateManager.popMatrix();
				this.healthBarWidth = AnimationUtil.INSTANCE.animate(hpWidth, this.healthBarWidth, 0.1);
				drawGradientRect(x - 3.5, y - 3.5, x + 105.5f, y + 42.4f, new Color(10, 10, 10, 255).getRGB(), new Color(10, 10, 10, 255).getRGB());
				// RenderUtils.prepareScissorBox(x, y, x + 140.0f, (float) (y + this.hudHeight));
				drawGradientRect(x - 3, y - 3.2, x + 104.8f, y + 41.8f, new Color(40, 40, 40, 255).getRGB(), new Color(40, 40, 40, 255).getRGB());
				drawGradientRect(x - 1.4, y - 1.5, x + 103.5f, y + 40.5f, new Color(74, 74, 74, 255).getRGB(), new Color(74, 74, 74, 255).getRGB());
				drawGradientRect(x - 1, y - 1, x + 103.0f, y + 40.0f, new Color(32, 32, 32, 255).getRGB(), new Color(10, 10, 10, 255).getRGB());
				Gui.drawRect(x + 25.0f, y + 11.0f, x + 87f, y + 14.29f, new Color(105, 105, 105, 40).getRGB());
				Gui.drawRect(x + 25.0f, y + 11.0f, x + 27f + this.healthBarWidth, y + 14.29f, ESP.getColorFromPercentage(this.target.getHealth(), this.target.getMaxHealth()));
				mc.fontRendererObj.drawStringWithShadow(this.target.getName(), x + 24.8f, y + 1.9f, new Color(255, 255, 255).getRGB());
				mc.fontRendererObj.drawStringWithShadow("l   " + "l   " + "l   " + "l   ", x + 30.0f, y + 10.2f, new Color(50, 50, 50).getRGB());
				mc.fontRendererObj.drawStringWithShadow("HP:" + healthStr, x - 11.2f + 44.0f - mc.fontRendererObj.getStringWidth(healthStr) / 2.0f, y + 17.0f, -1);
				GuiInventory.drawEntityOnScreen((int) (x + 12f), (int) (y + 34.0f), 15, this.target.rotationYaw, this.target.rotationPitch, this.target);
			}
		} else {
			this.healthBarWidth = 92.0;
			this.hudHeight = 0.0;
			this.target = null;
		}
	}

	protected float zLevel;

	public void drawGradientRect(double left, double top, double right, double bottom, int startColor, int endColor)
	{
		float var7 = (float)(startColor >> 24 & 255) / 255.0F;
		float var8 = (float)(startColor >> 16 & 255) / 255.0F;
		float var9 = (float)(startColor >> 8 & 255) / 255.0F;
		float var10 = (float)(startColor & 255) / 255.0F;
		float var11 = (float)(endColor >> 24 & 255) / 255.0F;
		float var12 = (float)(endColor >> 16 & 255) / 255.0F;
		float var13 = (float)(endColor >> 8 & 255) / 255.0F;
		float var14 = (float)(endColor & 255) / 255.0F;
		GlStateManager.func_179090_x();
		GlStateManager.enableBlend();
		GlStateManager.disableAlpha();
		GlStateManager.tryBlendFuncSeparate(770, 771, 1, 0);
		GlStateManager.shadeModel(7425);
		Tessellator var15 = Tessellator.getInstance();
		WorldRenderer var16 = var15.getWorldRenderer();
		var16.startDrawingQuads();
		var16.func_178960_a(var8, var9, var10, var7);
		var16.addVertex((double)right, (double)top, (double)this.zLevel);
		var16.addVertex((double)left, (double)top, (double)this.zLevel);
		var16.func_178960_a(var12, var13, var14, var11);
		var16.addVertex((double)left, (double)bottom, (double)this.zLevel);
		var16.addVertex((double)right, (double)bottom, (double)this.zLevel);
		var15.draw();
		GlStateManager.shadeModel(7424);
		GlStateManager.disableBlend();
		GlStateManager.enableAlpha();
		GlStateManager.func_179098_w();
	}
	
	public void onEvent(Event e) {
		if(e instanceof EventRender3D) {}
	}
	
}
