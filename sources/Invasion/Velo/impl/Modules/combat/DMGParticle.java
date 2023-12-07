package Velo.impl.Modules.combat;

import java.awt.Color;
import java.util.Random;
import java.util.concurrent.CopyOnWriteArrayList;

import org.lwjgl.input.Keyboard;
import org.lwjgl.opengl.GL11;

import Velo.api.Module.Module;
import Velo.api.Util.Other.ChatUtil;
import Velo.api.Util.Other.PlayerUtil;
import Velo.api.Util.Other.RandomObjectArraylist;
import Velo.api.Util.Other.other.EntityUtil;
import Velo.impl.Event.EventRender3D;
import Velo.impl.Event.EventSendPacket;
import Velo.impl.Modules.player.InventoryManager;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemSword;
import net.minecraft.item.ItemTool;
import net.minecraft.network.play.client.C02PacketUseEntity;
import net.minecraft.network.play.client.C02PacketUseEntity.Action;
import net.minecraft.util.MovingObjectPosition.MovingObjectType;

public class DMGParticle extends Module {
	
	private static transient CopyOnWriteArrayList<FX> eff = new CopyOnWriteArrayList<>();
	
	private static transient RandomObjectArraylist<Integer> c = new RandomObjectArraylist<Integer>(Color.GREEN.getRGB(), Color.YELLOW.getRGB(), Color.RED.getRGB());
	
	public int ticks = 0;
	
	public DMGParticle() {
		super("DMGParticle", "DMGParticle", Keyboard.KEY_NONE, Category.VISUALS);
	}
	
	public void onRender3DUpdate(EventRender3D event) {
		for (FX f : eff) {
			if (System.currentTimeMillis() > f.ttl && f.opacity <= 5) {
				eff.remove(f);
			}else {
				if (System.currentTimeMillis() > f.ttl) {
					f.opacity -= f.opacity/200;
				}
				f.x += (f.motX / 10);
				f.y += (f.motY / 10);
				f.z += (f.motZ / 10);
				f.motX -= f.motX / 20;
				f.motY -= f.motY / 20;
				f.motZ -= f.motZ / 10;
				
				String text = f.text;
				GlStateManager.pushMatrix();
				RenderHelper.enableStandardItemLighting();
				GlStateManager.enablePolygonOffset();
				GL11.glPolygonOffset(1.0f, -1100000.0f);
				GlStateManager.disableLighting();
				GlStateManager.disableDepth();
				GlStateManager.enableBlend();
				double scale = f.scale;
				GlStateManager.scale(1/scale, 1/scale, 1/scale);
				GlStateManager.rotate(180, 1, 0, 0);
				//GlStateManager.func_179098_w();
				GlStateManager.translate((f.x - mc.getRenderManager().renderPosX) * scale, (5.5 - mc.getRenderManager().renderPosY) * -scale, (f.z - mc.getRenderManager().renderPosZ) * -scale);
				//GlStateManager.rotate((float) f.yaw, 0, 1, 0);
				GlStateManager.rotate((float) f.pitch, 1, 0, 0);
				mc.fontRendererObj.drawString(text, (float) (f.x - (mc.fontRendererObj.getStringWidth(text) / 2) - mc.getRenderManager().renderPosX), 0f, f.color, true);
				GlStateManager.rotate(180, 0, 1, 0);
				GlStateManager.color(1, 1, 1, 1);
				GlStateManager.color(1, 1, 1, (float) (f.opacity/100));
				mc.fontRendererObj.drawString(text, (float) (f.x - (mc.fontRendererObj.getStringWidth(text) / 2) - mc.getRenderManager().renderPosX), 0f, f.color, true);
				GlStateManager.color(1, 1, 1, 1);
				GlStateManager.disableBlend();
				GlStateManager.enableDepth();
				GlStateManager.enableLighting();
				GlStateManager.disablePolygonOffset();
				RenderHelper.disableStandardItemLighting();
				GlStateManager.popMatrix();
			}
		}
	}
	
	public void onEventSendPacket(EventSendPacket event) {
		if(event.getPacket() instanceof C02PacketUseEntity) {
			ticks++;
			C02PacketUseEntity c02 = (C02PacketUseEntity) event.getPacket();
			if(c02.getAction() == c02.getAction().ATTACK) {
				try {
					if (mc.objectMouseOver.typeOfHit == MovingObjectType.ENTITY) {
						EntityLivingBase entity = (EntityLivingBase) mc.objectMouseOver.entityHit;
						for (short i = 0; i < 1; i++) {
							if(mc.thePlayer.inventory.getCurrentItem().getItem() instanceof ItemSword) {
								ItemSword item = (ItemSword) mc.thePlayer.inventory.getCurrentItem().getItem();
								ItemStack item2 = mc.thePlayer.inventory.getCurrentItem();
								int color = 0;
								if(item.getDamageVsEntity() + PlayerUtil.getEnchantment(item2, Enchantment.sharpness) > -1) {
									color = Color.green.getRGB();
								}
								if(item.getDamageVsEntity() + PlayerUtil.getEnchantment(item2, Enchantment.sharpness) > 3) {
									color = Color.yellow.getRGB();
								}
								if(item.getDamageVsEntity() + PlayerUtil.getEnchantment(item2, Enchantment.sharpness) > 5) {
									color = Color.red.getRGB();
								}
								if(ticks > 2) {
									//ChatUtil.addChatMessage(entity.hurtTime + "");
									ticks = 0;
										eff.add(new FX((long) 500,
										mc.objectMouseOver.entityHit.posX + new Random().nextInt(3) + new Random().nextDouble() - 1,
										mc.objectMouseOver.entityHit.posY + new Random().nextInt(2) + new Random().nextDouble() - 0.5,
										mc.objectMouseOver.entityHit.posZ + new Random().nextInt(3) + new Random().nextDouble() - 1,
										new Random().nextInt(360), new Random().nextInt(180) - 90, 1 / 0.03, item.getDamageVsEntity() + PlayerUtil.getEnchantment(item2, Enchantment.sharpness) + "", color));
								}
							}
							if(mc.thePlayer.inventory.getCurrentItem().getItem() instanceof ItemTool) {
								ItemTool item = (ItemTool) mc.thePlayer.inventory.getCurrentItem().getItem();
								ItemStack item2 = mc.thePlayer.inventory.getCurrentItem();
								
								
								int color = 0;
								if(item.getToolMaterial().getDamageVsEntity() + PlayerUtil.getEnchantment(item2, Enchantment.sharpness) > -1) {
									color = Color.green.getRGB();
								}
								if(item.getToolMaterial().getDamageVsEntity() + PlayerUtil.getEnchantment(item2, Enchantment.sharpness) > 3) {
									color = Color.yellow.getRGB();
								}
								if(item.getToolMaterial().getDamageVsEntity() + PlayerUtil.getEnchantment(item2, Enchantment.sharpness) > 5) {
									color = Color.red.getRGB();
								}
								if(ticks > 2) {
									ticks = 0;
										eff.add(new FX((long) 2000,
										mc.objectMouseOver.entityHit.posX + new Random().nextInt(3) + new Random().nextDouble() - 1,
										mc.objectMouseOver.entityHit.posY + new Random().nextInt(2) + new Random().nextDouble() - 0.5,
										mc.objectMouseOver.entityHit.posZ + new Random().nextInt(3) + new Random().nextDouble() - 1,
										new Random().nextInt(360), new Random().nextInt(180) - 90, 1 / 0.03, item.getToolMaterial().getDamageVsEntity() + PlayerUtil.getEnchantment(item2, Enchantment.sharpness) + "", color));
								}
							}
						}
					}
				} catch (Exception e2) {
				}
			}
		}
	}
	
	public class FX {
		public FX(long ttl, double x, double y, double z, double yaw, double pitch, double scale, String text, int color) {
			this.ttl = System.currentTimeMillis() + ttl;
			this.x = x;
			this.y = y;
			this.z = z;
			this.motX = ((double)(new Random().nextInt(3))) - 1.0;
			this.motY = new Random().nextDouble();
			this.motZ = ((double)(new Random().nextInt(3))) - 1.0;
			this.yaw = yaw;
			this.pitch = pitch;
			this.scale = scale;
			this.text = text;
			this.color = color;
			this.opacity = 100;
		}
		public long ttl;
		public double x, y, z, yaw, pitch, scale, motX, motY, motZ, opacity;
		public String text;
		public int color;
	}
}
