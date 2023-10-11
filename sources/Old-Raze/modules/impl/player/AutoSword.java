package markgg.modules.impl.player;

import markgg.RazeClient;
import markgg.event.Event;
import markgg.event.handler.EventHandler;
import markgg.event.handler.Listener;
import markgg.event.impl.MotionEvent;
import markgg.modules.Module;
import markgg.modules.ModuleInfo;
import markgg.modules.impl.combat.OldAura;
import net.minecraft.block.Block;
import net.minecraft.block.BlockBush;
import net.minecraft.block.BlockLeaves;
import net.minecraft.block.BlockWeb;
import net.minecraft.client.Minecraft;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.monster.EntityPigZombie;
import net.minecraft.entity.monster.EntitySkeleton;
import net.minecraft.entity.monster.EntitySpider;
import net.minecraft.entity.monster.EntityZombie;
import net.minecraft.item.ItemPickaxe;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemSword;
import net.minecraft.util.BlockPos;
import net.minecraft.util.MovingObjectPosition;

@ModuleInfo(name = "AutoSword", category = Module.Category.PLAYER)
public class AutoSword extends Module {

	private int oldSlot;
	private static boolean hasSwitched;

	public void onEnable() {
		oldSlot = mc.thePlayer.inventory.currentItem;
		hasSwitched = false;
	}
	
	public void onDisable() {
		if (hasSwitched) {
			mc.thePlayer.inventory.currentItem = oldSlot;
			hasSwitched = false;
		}
	}

	@EventHandler
	private final Listener<MotionEvent> e = e -> {
		if (e.getType() == MotionEvent.Type.PRE) {
			if(RazeClient.INSTANCE.getModuleManager().getModule(OldAura.class).target != null) {
				getBestSword();
			} else if(mc.gameSettings.keyBindAttack.pressed) {
				MovingObjectPosition blockPos = mc.objectMouseOver;
				if (blockPos != null && blockPos.typeOfHit == MovingObjectPosition.MovingObjectType.BLOCK) {
					BlockPos pos = blockPos.getBlockPos();
					Block block = mc.theWorld.getBlockState(pos).getBlock();
					if(block instanceof BlockLeaves || block instanceof BlockWeb || block instanceof BlockBush)
						getBestSword();
				}
			} else {
				if (hasSwitched) {
					mc.thePlayer.inventory.currentItem = oldSlot;
					hasSwitched = false;
				}
			}
		}
	};

	public static void getBestSword() {
	    float damageModifier = 0;
	    int newItem = -1;
	    for (int slot = 0; slot < 9; slot++) {
	        ItemStack stack = Minecraft.getMinecraft().thePlayer.inventory.mainInventory[slot];
	        if (stack == null) {
	            continue;
	        }
	        if (stack.getItem() instanceof ItemSword) {
	        	ItemSword pickax = (ItemSword) stack.getItem();
	            float damage = pickax.getMaxDamage() + (pickax.hasEffect(stack) ? getEnchantment(stack) : 0);
	            if (damage >= damageModifier) {
	                newItem = slot;
	                damageModifier = damage;
	            }
	        }
	    }
	    if (newItem > -1) {
	        Minecraft.getMinecraft().thePlayer.inventory.currentItem = newItem;
	        hasSwitched = true;
	    }
	}

	public static int getEnchantment(ItemStack i) {
		return EnchantmentHelper.getEnchantmentLevel(Enchantment.field_180314_l.effectId, i);
	}

}
