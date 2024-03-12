package xyz.cucumber.base.module.feat.player;

import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.ItemAxe;
import net.minecraft.item.ItemPickaxe;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemSword;
import xyz.cucumber.base.Client;
import xyz.cucumber.base.events.EventListener;
import xyz.cucumber.base.events.ext.EventAttack;
import xyz.cucumber.base.events.ext.EventGameLoop;
import xyz.cucumber.base.module.Category;
import xyz.cucumber.base.module.Mod;
import xyz.cucumber.base.module.ModuleInfo;
import xyz.cucumber.base.module.feat.combat.KillAuraModule;
import xyz.cucumber.base.module.feat.other.TeamsModule;
import xyz.cucumber.base.utils.Timer;
import xyz.cucumber.base.utils.game.EntityUtils;
import xyz.cucumber.base.utils.game.InventoryUtils;

@ModuleInfo(category = Category.PLAYER, description = "Automatically switch to best weapon", name = "Auto Weapon")
public class AutoWeaponModule extends Mod{
	public Timer timer = new Timer();
	
	@EventListener
	public void onGameLoop(EventGameLoop e) {
		EntityLivingBase entity = null;
    	
    	KillAuraModule killAura = ((KillAuraModule)Client.INSTANCE.getModuleManager().getModule(KillAuraModule.class));
    	        	
    	if (killAura.isEnabled()) {
        	entity = EntityUtils.getTarget(killAura.Range.getValue()+0.1, killAura.Targets.getMode(), "Single", (int) killAura.switchDelay.getValue(), Client.INSTANCE.getModuleManager().getModule(TeamsModule.class).isEnabled(), killAura.TroughWalls.isEnabled(), killAura.attackInvisible.isEnabled(), killAura.attackDead.isEnabled());
        }
    	
    	if(entity != null) {
            for (int i = 36; i < 45; i++)
            {
                if (mc.thePlayer.inventoryContainer.getSlot(i).getStack() != null && mc.thePlayer.inventoryContainer.getSlot(i).getStack() == bestWeapon() && mc.thePlayer.inventory.currentItem != i - 36)
                {
                    mc.thePlayer.inventory.currentItem = i - 36;
                    InventoryUtils.timer.reset();
                    break;
                }
            }
    	}
	}
	@EventListener
	public void onAttack(EventAttack e) {
		
		for (int i = 36; i < 45; i++)
        {
            if (mc.thePlayer.inventoryContainer.getSlot(i).getStack() != null && mc.thePlayer.inventoryContainer.getSlot(i).getStack() == bestWeapon() && mc.thePlayer.inventory.currentItem != i - 36)
            {
                mc.thePlayer.inventory.currentItem = i - 36;
                InventoryUtils.timer.reset();
                break;
            }
        }
	}
	
	public ItemStack bestWeapon()
    {
        ItemStack bestWeapon = null;
        float itemDamage = -1;

        for (int i = 36; i < 45; i++)
        {
            if (mc.thePlayer.inventoryContainer.getSlot(i).getHasStack())
            {
                final ItemStack is = mc.thePlayer.inventoryContainer.getSlot(i).getStack();

                if (is.getItem() instanceof ItemSword || is.getItem() instanceof ItemAxe || is.getItem() instanceof ItemPickaxe)
                {
                    float toolDamage = getItemDamage(is);

                    if (toolDamage >= itemDamage)
                    {
                        itemDamage = getItemDamage(is);
                        bestWeapon = is;
                    }
                }
            }
        }

        return bestWeapon;
    }

    public float getItemDamage(ItemStack itemStack)
    {
        float damage = InventoryUtils.getToolMaterialRating(itemStack, true);
        damage += EnchantmentHelper.getEnchantmentLevel(Enchantment.sharpness.effectId, itemStack) * 1.25F;
        damage += EnchantmentHelper.getEnchantmentLevel(Enchantment.fireAspect.effectId, itemStack) * 0.50F;
        damage += EnchantmentHelper.getEnchantmentLevel(Enchantment.unbreaking.effectId, itemStack) * 0.01F;
        damage += (itemStack.getMaxDamage() - itemStack.getItemDamage()) * 0.000000000001F;

        if (itemStack.getItem() instanceof ItemSword)
        {
            damage += 0.2;
        }

        return damage;
    }
}
