package com.enjoytheban.module.modules.render;

import java.awt.Color;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

import org.lwjgl.opengl.GL11;

import com.enjoytheban.api.EventHandler;
import com.enjoytheban.api.events.rendering.EventRender3D;
import com.enjoytheban.api.value.Mode;
import com.enjoytheban.api.value.Numbers;
import com.enjoytheban.api.value.Option;
import com.enjoytheban.management.FriendManager;
import com.enjoytheban.module.Module;
import com.enjoytheban.module.ModuleType;
import com.enjoytheban.utils.render.RenderUtil;

import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.item.ItemArmor;
import net.minecraft.item.ItemBow;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemSword;
import net.minecraft.item.ItemTool;
import optifine.Config;

/**
 * A hack for NAMETAGS
 * 
 * @author Purity
 */

public class Nametags extends Module {

	private Mode<Enum> health = new Mode("Health Mode", "healthmode", HealthMode.values(), HealthMode.Hearts);
	private Option<Boolean> armor = new Option("Armor", "armor", true);
	private Option<Boolean> dura = new Option("Durability", "durability", true);
	private Numbers<Double> scale = new Numbers("Scale", "scale", 3.0, 1.0, 5.0, 0.1);
	private ArrayList<Entity> entities = new ArrayList<Entity>();
    private Option<Boolean> players = new Option("Players", "players", true);
    private Option<Boolean> animals = new Option("Animals", "animals", true);
    private Option<Boolean> mobs = new Option("Mobs", "mobs", false);
    private Option<Boolean> invis = new Option("Invisibles", "invisibles", false);

	public Nametags() {
		super("Nametags", new String[] { "tags" }, ModuleType.Render);
		setColor(new Color(29,187,102).getRGB());
		addValues(health, armor, dura, invis, scale);
	}

	private int i = 0;

	@EventHandler
	private void onRender(EventRender3D render) {
		ArrayList<EntityPlayer> validEnt = new ArrayList<>();
		if (validEnt.size() > 100)
			validEnt.clear();
		for (final EntityLivingBase player : mc.theWorld.playerEntities) {
			if (player.isEntityAlive()) {
				if (player.isInvisible() && !invis.getValue()) {
					if (validEnt.contains(player))
						validEnt.remove(player);
					continue;
				}
				if (player == mc.thePlayer) {
					if (validEnt.contains(player))
						validEnt.remove(player);
					continue;
				}
				if (validEnt.size() > 100)
					break;
				if (!validEnt.contains(player))
					validEnt.add((EntityPlayer) player);
			} else {
				if (validEnt.contains(player))
					validEnt.remove(player);
			}
		}
		validEnt.forEach(player -> {
			final float x = (float) (player.lastTickPosX
					+ (player.posX - player.lastTickPosX) * render.getPartialTicks() - RenderManager.renderPosX);
			final float y = (float) (player.lastTickPosY
					+ (player.posY - player.lastTickPosY) * render.getPartialTicks() - RenderManager.renderPosY);
			final float z = (float) (player.lastTickPosZ
					+ (player.posZ - player.lastTickPosZ) * render.getPartialTicks() - RenderManager.renderPosZ);
			renderNametag(player, x, y, z);
		});
	}

	private String getHealth(EntityPlayer player) {
		DecimalFormat numberFormat = new DecimalFormat("0.#");
		return health.getValue() == HealthMode.Percentage ? numberFormat.format(player.getHealth() * 5 + player.getAbsorptionAmount() * 5) : numberFormat.format(player.getHealth() / 2 + player.getAbsorptionAmount() / 2);
	}

	private void drawNames(EntityPlayer player) {
		float xP = 2.2f;
		float width = ((getWidth(getPlayerName(player))) / 2F) + xP;
		width += getWidth(" " + getHealth(player)) / 2 + 2.5;

		float w = width;
		float nw = -width - xP;
		float offset = (getWidth(getPlayerName(player)) + 4);
		if(health.getValue() == HealthMode.Percentage) {
		RenderUtil.drawBorderedRect(nw, -3, width, 10, 1, new Color(20, 20, 20, 180).getRGB(),
				new Color(10, 10, 10, 200).getRGB());
		} else {
			RenderUtil.drawBorderedRect(nw + 5, -3, width, 10, 1, new Color(20, 20, 20, 180).getRGB(),
					new Color(10, 10, 10, 200).getRGB());
		}
		GlStateManager.disableDepth();

		if(health.getValue() == HealthMode.Percentage) {
			offset += getWidth(getHealth(player)) + getWidth(" %") - 1;
		} else {
			offset += getWidth(getHealth(player)) + getWidth(" ") - 1;
		}
		drawString(getPlayerName(player), w - offset, 0, 0xFFFFFF);
		int color;
		if (player.getHealth() == 10)
			color = 0xFFFF00;
		if (player.getHealth() > 10)
			color = RenderUtil.blend(new Color(0xFF00FF00), new Color(0xFFFFFF00),
					(1 / player.getHealth() / 2) * (player.getHealth() - 10)).getRGB();
		else
			color = RenderUtil.blend(new Color(0xFFFFFF00), new Color(0xFFFF0000), (1 / 10f) * player.getHealth())
					.getRGB();
		
		if(health.getValue() == HealthMode.Percentage) {
			drawString(getHealth(player) + "%", w - getWidth(getHealth(player) + " %"), 0, color);
		} else {
			drawString(getHealth(player), w - getWidth(getHealth(player) + " ") , 0, color);
		}
		GlStateManager.enableDepth();
	}

	private void drawString(String text, float x, float y, int color) {
		mc.fontRendererObj.drawStringWithShadow(text, x, y, color);

	}

	private int getWidth(String text) {
		return mc.fontRendererObj.getStringWidth(text);
	}

	private void startDrawing(float x, float y, float z, EntityPlayer player) {
		float var10001 = mc.gameSettings.thirdPersonView == 2 ? -1.0F : 1.0F;
		double size = Config.zoomMode ? (getSize(player) / 10) * this.scale.getValue() * 0.5 : (getSize(player) / 10) * this.scale.getValue() * 1.5;
		GL11.glPushMatrix();
		RenderUtil.startDrawing();
		GL11.glTranslatef(x, y, z);
		GL11.glNormal3f(0.0F, 1.0F, 0.0F);
		GL11.glRotatef(-mc.getRenderManager().playerViewY, 0.0F, 1.0F, 0.0F);
		GL11.glRotatef(mc.getRenderManager().playerViewX, var10001, 0.0F, 0.0F);
		GL11.glScaled(-0.016666668F * size, -0.016666668F * size, 0.016666668F * size);

	}

	private void stopDrawing() {
		RenderUtil.stopDrawing();
		GlStateManager.color(1, 1, 1);
		GlStateManager.popMatrix();
	}

	private void renderNametag(EntityPlayer player, float x, float y, float z) {
		y += 1.55 + (player.isSneaking() ? 0.5D : 0.7D);
		startDrawing(x, y, z, player);
		drawNames(player);
		GL11.glColor4d(1, 1, 1, 1);
		if(armor.getValue())
		renderArmor(player);
		stopDrawing();
	}

	private void renderArmor(EntityPlayer player) {
		ItemStack[] renderStack = player.inventory.armorInventory;
		ItemStack armourStack;
		int length = renderStack.length;
		int xOffset = 0;
		for (ItemStack aRenderStack : renderStack) {
			armourStack = aRenderStack;
			if (armourStack != null)
				xOffset -= 8;
		}
		if (player.getHeldItem() != null) {
			xOffset -= 8;
			ItemStack stock = player.getHeldItem().copy();
			if (stock.hasEffect() && (stock.getItem() instanceof ItemTool || stock.getItem() instanceof ItemArmor))
				stock.stackSize = 1;
			renderItemStack(stock, xOffset, -20);
			xOffset += 16;
		}
		renderStack = player.inventory.armorInventory;
		for (int index = 3; index >= 0; index--) {
			armourStack = renderStack[index];
			if (armourStack != null) {
				renderItemStack(armourStack, xOffset, -20);
				xOffset += 16;
			}
		}
		GlStateManager.color(1, 1, 1, 1);
	}

	private String getPlayerName(EntityPlayer player) {
		String name = player.getDisplayName().getFormattedText();
		if (FriendManager.isFriend(player.getName()))
			name = "\247b" + FriendManager.getAlias(player.getName());
		return name;
	}

	private float getSize(EntityPlayer player) {
		return mc.thePlayer.getDistanceToEntity(player) / 4.0F <= 2.0F ? 2.0F
				: mc.thePlayer.getDistanceToEntity(player) / 4.0F;
	}

	private void renderItemStack(final ItemStack stack, int x, int y) {
		GlStateManager.pushMatrix();
		GlStateManager.depthMask(true);
		GlStateManager.clear(256);
		RenderHelper.enableStandardItemLighting();
		mc.getRenderItem().zLevel = -150.0f;
		GlStateManager.disableDepth();
		GlStateManager.func_179090_x();
		GlStateManager.enableBlend();
		GlStateManager.enableAlpha();
		GlStateManager.func_179098_w();
		GlStateManager.enableLighting();
		GlStateManager.enableDepth();
		mc.getRenderItem().func_180450_b(stack, x, y);
		mc.getRenderItem().func_175030_a(mc.fontRendererObj, stack, x, y);
		mc.getRenderItem().zLevel = 0.0f;
		RenderHelper.disableStandardItemLighting();
		GlStateManager.disableCull();
		GlStateManager.enableAlpha();
		GlStateManager.disableBlend();
		GlStateManager.disableLighting();
		double s = 0.5;
		GlStateManager.scale(s, s, s);

		GlStateManager.disableDepth();
		renderEnchantText(stack, x, y);
		GlStateManager.enableDepth();
		GlStateManager.scale(2.0f, 2.0f, 2.0f);
		GlStateManager.popMatrix();
	}

	/**
	 * @Author anodise
	 */
	private void renderEnchantText(final ItemStack stack, final int x, final int y) {
		int enchantmentY = y - 24;
		if (stack.getEnchantmentTagList() != null && stack.getEnchantmentTagList().tagCount() >= 6) {
			mc.fontRendererObj.drawStringWithShadow("god", x * 2, enchantmentY, 16711680);
			return;
		}
		if (stack.getItem() instanceof ItemArmor) {
			final int protectionLevel = EnchantmentHelper.getEnchantmentLevel(Enchantment.field_180310_c.effectId,
					stack);
			final int projectileProtectionLevel = EnchantmentHelper
					.getEnchantmentLevel(Enchantment.field_180308_g.effectId, stack);
			final int blastProtectionLevel = EnchantmentHelper.getEnchantmentLevel(Enchantment.blastProtection.effectId,
					stack);
			final int fireProtectionLevel = EnchantmentHelper.getEnchantmentLevel(Enchantment.fireProtection.effectId,
					stack);
			final int thornsLevel = EnchantmentHelper.getEnchantmentLevel(Enchantment.thorns.effectId, stack);
			final int unbreakingLevel = EnchantmentHelper.getEnchantmentLevel(Enchantment.unbreaking.effectId, stack);
			int damage = stack.getMaxDamage() - stack.getItemDamage();
			if(dura.getValue())
			mc.fontRendererObj.drawStringWithShadow("" + damage, x * 2, y, 0xFFFFFF);

			if (protectionLevel > 0) {
				mc.fontRendererObj.drawStringWithShadow("prot" + protectionLevel, x * 2, enchantmentY, -1);
				enchantmentY += 8;
			}
			if (projectileProtectionLevel > 0) {
				mc.fontRendererObj.drawStringWithShadow("proj" + projectileProtectionLevel, x * 2, enchantmentY, -1);
				enchantmentY += 8;
			}
			if (blastProtectionLevel > 0) {
				mc.fontRendererObj.drawStringWithShadow("bp" + blastProtectionLevel, x * 2, enchantmentY, -1);
				enchantmentY += 8;
			}
			if (fireProtectionLevel > 0) {
				mc.fontRendererObj.drawStringWithShadow("frp" + fireProtectionLevel, x * 2, enchantmentY, -1);
				enchantmentY += 8;
			}
			if (thornsLevel > 0) {
				mc.fontRendererObj.drawStringWithShadow("th" + thornsLevel, x * 2, enchantmentY, -1);
				enchantmentY += 8;
			}
			if (unbreakingLevel > 0) {
				mc.fontRendererObj.drawStringWithShadow("unb" + unbreakingLevel, x * 2, enchantmentY, -1);
				enchantmentY += 8;
			}
		}
		if (stack.getItem() instanceof ItemBow) {
			final int powerLevel = EnchantmentHelper.getEnchantmentLevel(Enchantment.power.effectId, stack);
			final int punchLevel = EnchantmentHelper.getEnchantmentLevel(Enchantment.punch.effectId, stack);
			final int flameLevel = EnchantmentHelper.getEnchantmentLevel(Enchantment.flame.effectId, stack);
			final int unbreakingLevel2 = EnchantmentHelper.getEnchantmentLevel(Enchantment.unbreaking.effectId, stack);
			if (powerLevel > 0) {
				mc.fontRendererObj.drawStringWithShadow("pow" + powerLevel, x * 2, enchantmentY, -1);
				enchantmentY += 8;
			}
			if (punchLevel > 0) {
				mc.fontRendererObj.drawStringWithShadow("pun" + punchLevel, x * 2, enchantmentY, -1);
				enchantmentY += 8;
			}
			if (flameLevel > 0) {
				mc.fontRendererObj.drawStringWithShadow("flame" + flameLevel, x * 2, enchantmentY, -1);
				enchantmentY += 8;
			}
			if (unbreakingLevel2 > 0) {
				mc.fontRendererObj.drawStringWithShadow("unb" + unbreakingLevel2, x * 2, enchantmentY, -1);
				enchantmentY += 8;
			}
		}
		if (stack.getItem() instanceof ItemSword) {
			final int sharpnessLevel = EnchantmentHelper.getEnchantmentLevel(Enchantment.field_180314_l.effectId,
					stack);
			final int knockbackLevel = EnchantmentHelper.getEnchantmentLevel(Enchantment.field_180313_o.effectId,
					stack);
			final int fireAspectLevel = EnchantmentHelper.getEnchantmentLevel(Enchantment.fireAspect.effectId, stack);
			final int unbreakingLevel2 = EnchantmentHelper.getEnchantmentLevel(Enchantment.unbreaking.effectId, stack);

			if (sharpnessLevel > 0) {
				mc.fontRendererObj.drawStringWithShadow("sh" + sharpnessLevel, x * 2, enchantmentY, -1);
				enchantmentY += 8;
			}
			if (knockbackLevel > 0) {
				mc.fontRendererObj.drawStringWithShadow("kb" + knockbackLevel, x * 2, enchantmentY, -1);
				enchantmentY += 8;
			}
			if (fireAspectLevel > 0) {
				mc.fontRendererObj.drawStringWithShadow("fire" + fireAspectLevel, x * 2, enchantmentY, -1);
				enchantmentY += 8;
			}
			if (unbreakingLevel2 > 0) {
				mc.fontRendererObj.drawStringWithShadow("unb" + unbreakingLevel2, x * 2, enchantmentY, -1);
			}
		}
		if (stack.getItem() instanceof ItemTool) {
			final int unbreakingLevel2 = EnchantmentHelper.getEnchantmentLevel(Enchantment.unbreaking.effectId, stack);
			final int efficiencyLevel = EnchantmentHelper.getEnchantmentLevel(Enchantment.efficiency.effectId, stack);
			final int fortuneLevel = EnchantmentHelper.getEnchantmentLevel(Enchantment.fortune.effectId, stack);
			final int silkTouch = EnchantmentHelper.getEnchantmentLevel(Enchantment.silkTouch.effectId, stack);
			if (efficiencyLevel > 0) {
				mc.fontRendererObj.drawStringWithShadow("eff" + efficiencyLevel, x * 2, enchantmentY, -1);
				enchantmentY += 8;
			}
			if (fortuneLevel > 0) {
				mc.fontRendererObj.drawStringWithShadow("fo" + fortuneLevel, x * 2, enchantmentY, -1);
				enchantmentY += 8;
			}
			if (silkTouch > 0) {
				mc.fontRendererObj.drawStringWithShadow("silk" + silkTouch, x * 2, enchantmentY, -1);
				enchantmentY += 8;
			}
			if (unbreakingLevel2 > 0) {
				mc.fontRendererObj.drawStringWithShadow("ub" + unbreakingLevel2, x * 2, enchantmentY, -1);
			}
		}
		if (stack.getItem() == Items.golden_apple && stack.hasEffect()) {
			mc.fontRendererObj.drawStringWithShadow("god", x * 2, enchantmentY, -1);
		}
	}
	
	enum HealthMode {
		Hearts, Percentage
	}
}