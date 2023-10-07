/**
 * Time: 8:42:48 PM
 * Date: Jan 3, 2017
 * Creator: cool1
 */
package info.sigmaclient.module.impl.player;

import info.sigmaclient.event.Event;
import info.sigmaclient.event.RegisterEvent;
import info.sigmaclient.event.impl.EventPacket;
import info.sigmaclient.module.Module;
import info.sigmaclient.module.data.ModuleData;
import info.sigmaclient.util.NetUtil;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.Entity;
import net.minecraft.item.ItemStack;
import net.minecraft.network.play.client.C02PacketUseEntity;
import net.minecraft.potion.Potion;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;

/**
 * @author cool1
 *
 */
public class ItemSpoof extends Module {

	private boolean send;
	private BlockPos block;
	private EnumFacing facing;
	private int placeDir;
	private ItemStack stack;
	private float facingX;
	private float facingY;
	private float facingZ;
	public static Entity target;

	/**
	 * @param data
	 */
	public ItemSpoof(ModuleData data) {
		super(data);
		// TODO Auto-generated constructor stub
	}

	private int getSlotForTool(final Block pos) {
		int slot = 9;
		float damage = 0.0f;
		for (int i = 9; i <= 35; ++i) {
			if (mc.thePlayer.inventoryContainer.getSlot(i).getHasStack()) {
				final ItemStack stack = mc.thePlayer.inventoryContainer.getSlot(i).getStack();
				final float curDamage = stack.getStrVsBlock(pos);
				if (curDamage >= damage) {
					damage = curDamage;
					slot = i;
					System.out.println(String.valueOf(stack.getDisplayName()) + " " + slot);
				}
			}
		}
		return slot;
	}

	public float damage(final Block p_180471_1_) {
		final ItemStack var4 = mc.thePlayer.inventoryContainer
				.getSlot(getSlotForTool(mc.theWorld.getBlockState(block).getBlock())).getStack();
		float var5 = var4.getStrVsBlock(mc.theWorld.getBlockState(block).getBlock());
		if (var5 > 1.0f) {
			final int var6 = EnchantmentHelper.getEfficiencyModifier(mc.thePlayer);
			if (var6 > 0 && var4 != null) {
				var5 += var6 * var6 + 1;
			}
		}
		if (mc.thePlayer.isPotionActive(Potion.digSpeed)) {
			var5 *= 1.0f + (mc.thePlayer.getActivePotionEffect(Potion.digSpeed).getAmplifier() + 1) * 0.2f;
		}
		if (mc.thePlayer.isPotionActive(Potion.digSlowdown)) {
			float var7 = 1.0f;
			switch (mc.thePlayer.getActivePotionEffect(Potion.digSlowdown).getAmplifier()) {
			case 0: {
				var7 = 0.3f;
				break;
			}
			case 1: {
				var7 = 0.09f;
				break;
			}
			case 2: {
				var7 = 0.0027f;
				break;
			}
			default: {
				var7 = 8.1E-4f;
				break;
			}
			}
			var5 *= var7;
		}
		if (mc.thePlayer.isInsideOfMaterial(Material.water)
				&& !EnchantmentHelper.getAquaAffinityModifier(mc.thePlayer)) {
			var5 /= 5.0f;
		}
		if (!mc.thePlayer.onGround) {
			var5 /= 5.0f;
		}
		return var5;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see EventListener#onEvent(Event)
	 */
	@RegisterEvent(events = { EventPacket.class})
	public void onEvent(Event event) {
		if (event instanceof EventPacket) {
			EventPacket e = (EventPacket) event;
			if (e.isOutgoing()) {
				if (send) {
					send = false;
					return;
				}
			}
			if ((e.isOutgoing() && e.getPacket() instanceof C02PacketUseEntity)) {
				C02PacketUseEntity attack = (C02PacketUseEntity) e.getPacket();
				if (attack.getAction() == C02PacketUseEntity.Action.ATTACK) {
					if(target == null){
						target = attack.getEntityFromWorld(mc.theWorld);
					}
					if (target != null) {
						mc.playerController.windowClick(mc.thePlayer.inventoryContainer.windowId, 9,mc.thePlayer.inventory.currentItem, 2, mc.thePlayer);
						mc.playerController.updateController();
						send = true;
						NetUtil.sendPacketNoEvents(new C02PacketUseEntity(target, C02PacketUseEntity.Action.ATTACK));
						mc.playerController.windowClick(mc.thePlayer.inventoryContainer.windowId, 9,mc.thePlayer.inventory.currentItem, 2, mc.thePlayer);
						mc.playerController.updateController();
						target = null;
					}
				}
			}
		}
	}

}
