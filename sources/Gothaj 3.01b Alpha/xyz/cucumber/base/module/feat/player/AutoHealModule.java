package xyz.cucumber.base.module.feat.player;

import java.util.HashMap;

import net.minecraft.client.gui.inventory.GuiInventory;
import net.minecraft.init.Items;
import net.minecraft.inventory.Container;
import net.minecraft.item.ItemPotion;
import net.minecraft.item.ItemStack;
import net.minecraft.network.play.client.C08PacketPlayerBlockPlacement;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.BlockPos;
import xyz.cucumber.base.Client;
import xyz.cucumber.base.events.EventListener;
import xyz.cucumber.base.events.EventType;
import xyz.cucumber.base.events.ext.EventMotion;
import xyz.cucumber.base.events.ext.EventMoveButton;
import xyz.cucumber.base.module.Category;
import xyz.cucumber.base.module.Mod;
import xyz.cucumber.base.module.ModuleInfo;
import xyz.cucumber.base.module.settings.BooleanSettings;
import xyz.cucumber.base.module.settings.ModeSettings;
import xyz.cucumber.base.module.settings.NumberSettings;
import xyz.cucumber.base.utils.Timer;
import xyz.cucumber.base.utils.game.InventoryUtils;

@ModuleInfo(category = Category.PLAYER, description = "Automatically heals you", name = "Auto Heal")
public class AutoHealModule extends Mod{
	public ModeSettings mode = new ModeSettings("Mode", new String[] {
    		"Open Inv", "Spoof"
    });
	public BooleanSettings intave = new BooleanSettings("Intave", true);
	public NumberSettings health = new NumberSettings("Health", 15, 5, 20, 1);
	public NumberSettings headHealth = new NumberSettings("Head Health", 4, 1, 20, 1);
	public NumberSettings speed = new NumberSettings("Speed", 150, 0, 500, 1);
	public BooleanSettings soups = new BooleanSettings("Soups", true);
	public BooleanSettings potions = new BooleanSettings("Potions", true);
	public BooleanSettings heads = new BooleanSettings("Heads", true);
	
	public int oldSlot;
	public boolean shouldSwitch;
	
	public Timer timer = new Timer();
	
	public AutoHealModule() {
		this.addSettings(mode,intave,health,headHealth,speed,soups,potions,heads);
	}
	public void onDisable() {
    	InventoryUtils.isInventoryOpen = false;
    }
	@EventListener
    public void onMoveButton(EventMoveButton event) {
    	if(InventoryUtils.isInventoryOpen && intave.isEnabled()) {
    		event.forward = false;
			event.backward =false;
			event.left = false;
			event.right = false;
			event.jump = false;
			event.sneak = false;
    	}
    }
	@EventListener
	public void onMotion(EventMotion e) {
		setInfo(mode.getMode());

		if(soups.isEnabled()) {
			int soupInInventory = findSoup(9, 36);
			int soupInHotbar = findSoup(36, 45);
			
			Container inventoryContainer = mc.thePlayer.inventoryContainer;
			
			if(soupInInventory != -1 || soupInHotbar != -1) {
				for(int i = 9; i < 45; i++)
				{
					ItemStack stack = inventoryContainer.getSlot(i).getStack();
					if(stack != null && stack.getItem() == Items.bowl)
					{
						/*if(mode.getMode().equalsIgnoreCase("Spoof") && mc.currentScreen == null)InventoryUtils.openInv(mode.getMode());
						if(mode.getMode().equalsIgnoreCase("Spoof") || mc.currentScreen instanceof GuiInventory)mc.playerController.windowClick(0, i, 0, 0, mc.thePlayer);
						if(mode.getMode().equalsIgnoreCase("Spoof") || mc.currentScreen instanceof GuiInventory)mc.playerController.windowClick(0, 18, 0, 0, mc.thePlayer);
						if(mode.getMode().equalsIgnoreCase("Spoof") || mc.currentScreen instanceof GuiInventory)InventoryUtils.timer.reset();
						if (InventoryUtils.timer.getTime() > speed.getValue()) {
							if(mode.getMode().equalsIgnoreCase("Spoof") && mc.currentScreen == null)InventoryUtils.closeInv(mode.getMode());
	                    }*/
						
						if(stack != null && stack.getItem() == Items.bowl) {
							if (InventoryUtils.timer.getTime() > speed.getValue()) {
								if(mode.getMode().equalsIgnoreCase("Spoof") && mc.currentScreen == null)InventoryUtils.openInv(mode.getMode());
								if(mode.getMode().equalsIgnoreCase("Spoof") || mc.currentScreen instanceof GuiInventory) {
									InventoryUtils.drop(i);
									InventoryUtils.timer.reset();
									return;
								}
							}
							if (InventoryUtils.timer.getTime() > 75) {
								if(mode.getMode().equalsIgnoreCase("Spoof") && mc.currentScreen == null)InventoryUtils.closeInv(mode.getMode());
							}
						}
						
						if(mode.getMode().equalsIgnoreCase("Spoof") || mc.currentScreen instanceof GuiInventory)return;
					}
				}
				if(soupInHotbar != -1)
				{
					if(!(mc.currentScreen instanceof GuiInventory) && mc.thePlayer.getHealth() <= health.getValue() && InventoryUtils.timer.hasTimeElapsed(speed.getValue(), false)) {
						
						oldSlot = mc.thePlayer.inventory.currentItem;
						shouldSwitch = true;
						mc.thePlayer.inventory.currentItem = soupInHotbar - 36;
						
						mc.playerController.updateController();
						mc.thePlayer.sendQueue.addToSendQueue(new C08PacketPlayerBlockPlacement(new BlockPos(-1, -1, -1), -1, inventoryContainer.getSlot(soupInHotbar).getStack(), 0.0F, 0.0F, 0.0F));
						mc.thePlayer.dropOneItem(false);
						InventoryUtils.timer.reset();
						
						return;
					}
				}
				
				if(!isFull() && soupInInventory != -1){
					if(InventoryUtils.timer.hasTimeElapsed(speed.getValue(), false)) {
						if(mode.getMode().equalsIgnoreCase("Spoof") && mc.currentScreen == null)InventoryUtils.openInv(mode.getMode());
						if(mode.getMode().equalsIgnoreCase("Spoof") || mc.currentScreen instanceof GuiInventory) {
							mc.playerController.windowClick(0, soupInInventory, 0, 1, mc.thePlayer);
							InventoryUtils.timer.reset();
						}
						return;
					}
					if (InventoryUtils.timer.getTime() > 75) {
						if(mode.getMode().equalsIgnoreCase("Spoof") && mc.currentScreen == null)InventoryUtils.closeInv(mode.getMode());
                    }
				}
			}
		}
		if(potions.isEnabled()) {
			int potionInInventory = findPotion(9, 36);
			int potionInHotbar = findPotion(36, 45);
			
			
			if(potionInInventory != -1 || potionInHotbar != -1) {
				
				Container inventoryContainer = mc.thePlayer.inventoryContainer;
				
				if(potionInHotbar != -1)
				{
					if(!(mc.currentScreen instanceof GuiInventory) && InventoryUtils.timer.hasTimeElapsed(speed.getValue(), false)) {
						
						oldSlot = mc.thePlayer.inventory.currentItem;
						shouldSwitch = true;
						if(e.getType() == EventType.POST)mc.thePlayer.inventory.currentItem = potionInHotbar - 36;
						
						mc.playerController.updateController();
						if(e.getType() == EventType.PRE)e.setPitch(90f);
						if(e.getType() == EventType.POST) {
							mc.thePlayer.sendQueue.addToSendQueue(new C08PacketPlayerBlockPlacement(new BlockPos(-1, -1, -1), -1, inventoryContainer.getSlot(potionInHotbar).getStack(), 0.0F, 0.0F, 0.0F));
							InventoryUtils.timer.reset();
							return;
						}
					}
				}
				
				if(!isFull() && potionInInventory != -1) {
					if(InventoryUtils.timer.hasTimeElapsed(speed.getValue(), false)) {
						if(mode.getMode().equalsIgnoreCase("Spoof") && mc.currentScreen == null)InventoryUtils.openInv(mode.getMode());
						if(mode.getMode().equalsIgnoreCase("Spoof") || mc.currentScreen instanceof GuiInventory) {
							mc.playerController.windowClick(0, potionInInventory, 0, 1, mc.thePlayer);
							InventoryUtils.timer.reset();
							return;
						}
					}
					if (InventoryUtils.timer.getTime() > 75) {
						if(mode.getMode().equalsIgnoreCase("Spoof") && mc.currentScreen == null)InventoryUtils.closeInv(mode.getMode());
                    }
				}
			}
		}
		if(heads.isEnabled()) {
			int headInInventory = findHead(9, 36);
			int headInHotbar = findHead(36, 45);
			
			
			if(headInInventory != -1 || headInHotbar != -1) {
				
				Container inventoryContainer = mc.thePlayer.inventoryContainer;
				
				if(headInHotbar != -1 && mc.thePlayer.getHealth() <= headHealth.getValue())
				{
					if(!(mc.currentScreen instanceof GuiInventory) && mc.thePlayer.getHealth() <= headHealth.getValue() && InventoryUtils.timer.hasTimeElapsed(speed.getValue(), false)) {
						
						oldSlot = mc.thePlayer.inventory.currentItem;
						shouldSwitch = true;
						mc.thePlayer.inventory.currentItem = headInHotbar - 36;
						
						mc.playerController.updateController();
						e.setPitch(90f);
						mc.thePlayer.sendQueue.addToSendQueue(new C08PacketPlayerBlockPlacement(new BlockPos(-1, -1, -1), -1, inventoryContainer.getSlot(headInHotbar).getStack(), 0.0F, 0.0F, 0.0F));
						
						InventoryUtils.timer.reset();
						
						return;
					}
				}
				
				if(!isFull() && headInInventory != -1){
					if(InventoryUtils.timer.hasTimeElapsed(speed.getValue(), false)) {
						if(mode.getMode().equalsIgnoreCase("Spoof") && mc.currentScreen == null)InventoryUtils.openInv(mode.getMode());
						if(mode.getMode().equalsIgnoreCase("Spoof") || mc.currentScreen instanceof GuiInventory) {
							mc.playerController.windowClick(0, headInInventory, 0, 1, mc.thePlayer);
							InventoryUtils.timer.reset();
							return;
						}
					}
					if (InventoryUtils.timer.getTime() > (speed.getValue()+75)) {
						if(mode.getMode().equalsIgnoreCase("Spoof") && mc.currentScreen == null)InventoryUtils.closeInv(mode.getMode());
                    }
				}
			}
		}
	}
	public int findPotion(int startSlot, int endSlot)
	{
		
		for(int i = startSlot; i < endSlot; i++)
		{
			ItemStack stack = mc.thePlayer.inventoryContainer.getSlot(i).getStack();
			if(stack != null && stack.getItem() == Items.potionitem) {
				
				ItemPotion itemPotion = (ItemPotion) stack.getItem();
                PotionEffect effect = itemPotion.getEffects(stack).get(0);
                
				if (ItemPotion.isSplash(stack.getMetadata()) && goodPotions.containsKey(effect.getPotionID())) {
					if((effect.getPotionID() == Potion.regeneration.id || effect.getPotionID() == Potion.heal.id || effect.getPotionID() == Potion.healthBoost.id) && mc.thePlayer.getHealth() > health.getValue()) {
						
					 }else if(mc.thePlayer.isPotionActive(effect.getPotionID())) {
						 
					 }else {
						 return i;
					 }
				}
			}
		}
		return -1;
	}
	public int findHead(int startSlot, int endSlot)
	{
		for(int i = startSlot; i < endSlot; i++)
		{
			ItemStack stack = mc.thePlayer.inventoryContainer.getSlot(i).getStack();
			if(stack != null && (stack.getItem() == Items.skull || stack.getItem() == Items.dye))return i;
		}
		return -1;
	}
	public int findSoup(int startSlot, int endSlot)
	{
		for(int i = startSlot; i < endSlot; i++)
		{
			ItemStack stack = mc.thePlayer.inventoryContainer.getSlot(i).getStack();
			if(stack != null && stack.getItem() == Items.mushroom_stew)return i;
		}
		return -1;
	}
	
	public boolean isFull()
	{
		for(int i = 36; i < 45; i++)
		{
			ItemStack stack = mc.thePlayer.inventoryContainer.getSlot(i).getStack();
			if(stack == null)return false;
		}
		return true;
	}
	
	private HashMap<Integer, Integer> goodPotions = new HashMap<Integer, Integer>() {{
        put(6, 1); // Instant Health
        put(10, 2); // Regeneration
        put(11, 3); // Resistance
        put(21, 4); // Health Boost
        put(22, 5); // Absorption
        put(23, 6); // Saturation
        put(5, 7); // Strength
        put(1, 8); // Speed
        put(12, 9); // Fire Resistance
        put(14, 10); // Invisibility
        put(3, 11); // Haste
        put(13, 12); // Water Breathing
    }};
}
