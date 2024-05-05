package Velo.impl.Modules.player;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.lwjgl.input.Keyboard;

import Velo.api.Module.Module;
import Velo.api.Util.Other.PlayerUtil;
import Velo.api.Util.Other.Timer;
import Velo.impl.Event.EventReceivePacket;
import Velo.impl.Event.EventUpdate;
import Velo.impl.Settings.BooleanSetting;
import Velo.impl.Settings.ModeSetting;
import Velo.impl.Settings.NumberSetting;
import Velo.impl.Modules.movement.Scaffold;
import net.minecraft.block.Block;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiChat;
import net.minecraft.client.gui.inventory.GuiChest;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.client.gui.inventory.GuiInventory;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.init.Blocks;
import net.minecraft.inventory.ContainerChest;
import net.minecraft.inventory.Slot;
import net.minecraft.item.Item;
import net.minecraft.item.ItemAnvilBlock;
import net.minecraft.item.ItemAppleGold;
import net.minecraft.item.ItemArmor;
import net.minecraft.item.ItemAxe;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemBow;
import net.minecraft.item.ItemEgg;
import net.minecraft.item.ItemEnderPearl;
import net.minecraft.item.ItemExpBottle;
import net.minecraft.item.ItemFishingRod;
import net.minecraft.item.ItemFood;
import net.minecraft.item.ItemGlassBottle;
import net.minecraft.item.ItemHoe;
import net.minecraft.item.ItemPickaxe;
import net.minecraft.item.ItemPotion;
import net.minecraft.item.ItemSpade;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemSword;
import net.minecraft.item.ItemTool;
import net.minecraft.network.Packet;
import net.minecraft.network.play.client.C0DPacketCloseWindow;
import net.minecraft.network.play.client.C16PacketClientStatus;
import net.minecraft.network.play.client.C16PacketClientStatus.EnumState;
import net.minecraft.network.play.server.S2DPacketOpenWindow;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;

public class InventoryManager extends Module {
	
	public int[] bestArmorDamageReducement;
	public int[] bestArmorSlots;
	public float bestSwordDamage;
	public int bestSwordSlot;
	public boolean preferSwords = true;
	public List<Integer> trash = new ArrayList<>();
	public Timer timer = new Timer();
    public boolean inventoryOpen;
	

    public static int weaponSlot = 36, pickaxeSlot = 37, axeSlot = 38, shovelSlot = 39;
    ArrayList<Integer> whitelistedItems = new ArrayList<>();
    
    public static BooleanSetting Food = new BooleanSetting("Food", false), sort = new BooleanSetting("Sort", true), Bow = new BooleanSetting("Bow", false),
    		Sword = new BooleanSetting("Sword", true);
    
    
	public static NumberSetting delay = new NumberSetting("Delay", 0, 0, 1000, 1);
	public static NumberSetting BlockCap = new NumberSetting("MaxBlocks", 64, 64, 128, 1);
	public static ModeSetting mode = new ModeSetting("Mode", "Basic","Basic", "OpenInv");
	
	
	public InventoryManager() {
		super("InventoryManager", "InventoryManager", Keyboard.KEY_NONE, Category.PLAYER);
		this.loadSettings(delay, mode, Food, sort, Bow, Sword, BlockCap);
	}
	
	@Override
	public void onEventReceivePacket(EventReceivePacket e) {
        Packet packet =  e.getPacket();
        if (packet instanceof C16PacketClientStatus) {
            C16PacketClientStatus open = (C16PacketClientStatus) packet;
       
              
        }
        if (packet instanceof C0DPacketCloseWindow) {
         
        }
    

        if (packet instanceof S2DPacketOpenWindow) {
            //inventoryOpen = false;
        	}	
        

// TODO Auto-generated method stub
		super.onEventReceivePacket(e);
	}
	
	public void onUpdate(EventUpdate event) {
		if(mode.equalsIgnorecase("Basic") ) {
		searchForItems();
		
		
		for(int i = 0; i < bestArmorSlots.length; i++) {
			if(bestArmorSlots[i] != -1) {
				int bestSlot = bestArmorSlots[i];
				
				ItemStack oldArmor = mc.thePlayer.inventory.armorItemInSlot(i);
				
				if(oldArmor != null && oldArmor.getItem() != null) {
					if(timer.hasTimedElapsed((long) delay.getValue(), true)) {
					mc.playerController.windowClick(mc.thePlayer.inventoryContainer.windowId, 8 - i, 0, 1, mc.thePlayer);
					}
				}
				if(timer.hasTimedElapsed((long) delay.getValue(), true)) {
				mc.playerController.windowClick(mc.thePlayer.inventoryContainer.windowId, bestSlot < 9 ? bestSlot + 36 : bestSlot, 0, 1, mc.thePlayer);;
				}
			}
		}
		searchForTrash();
		/*
		if(bestSwordSlot != -1 && bestSwordDamage != -1) {
			if(timer.hasTimedElapsed(75, false)) {
			mc.playerController.windowClick(mc.thePlayer.inventoryContainer.windowId, bestSwordSlot < 9 ? bestSwordSlot + 36 : bestSwordSlot, 0, 2, mc.thePlayer);
			}
		}
		
*/
		
		for(Integer integer : trash) {
			if(timer.hasTimedElapsed((long) delay.getValue(), true)) {
			mc.playerController.windowClick(mc.thePlayer.inventoryContainer.windowId, integer < 9 ? integer + 36 : integer, 0, 4, mc.thePlayer);
			}
			
			
			
			
		}
	
	
	





if(mc.currentScreen == null) {
	getRidOfTrash();
}
	if(!(mc.currentScreen instanceof GuiChest)) {
		
    if ((mc.currentScreen instanceof GuiInventory   || (mc.currentScreen instanceof GuiContainer && ((GuiContainer) mc.currentScreen).inventorySlots == mc.thePlayer.inventoryContainer))&& timer.hasTimedElapsed(200l, true) && (!(mc.currentScreen instanceof GuiChest)) && getRidOfArmor() && getRidOfTools() && swordSlot()) {
        //Logger.ingameInfo("DONE");
    	timer.reset();
    	if(mc.currentScreen instanceof GuiChest)
    	
    		return;
        if (hotbarHasSpace() && (!(mc.currentScreen instanceof GuiChest)))
        	
            hotBar();
    
 
    }
	}
		}
		
		
		
		if(mode.equalsIgnorecase("OpenInv") && !(mc.currentScreen instanceof GuiInventory)) {
			return;
		}
	
	
	
	    if (mc.currentScreen == null || mc.currentScreen instanceof GuiInventory
                || mc.currentScreen instanceof GuiChat) {
            if (timer.delay((long)delay.getValue()) && weaponSlot >= 36) {

                if (!mc.thePlayer.inventoryContainer.getSlot(weaponSlot).getHasStack()) {
                    getBestWeapon(weaponSlot);

                } else {
                    if (!isBestWeapon(mc.thePlayer.inventoryContainer.getSlot(weaponSlot).getStack())) {
                        getBestWeapon(weaponSlot);
                    }
                }
            }
            if (sort.isEnabled()) {
                if (timer.delay((long)delay.getValue()) && pickaxeSlot >= 36) {
                    getBestPickaxe(pickaxeSlot);
                }
                if (timer.delay((long)delay.getValue()) && shovelSlot >= 36) {
                    getBestShovel(shovelSlot);
                }
                if (timer.delay((long)delay.getValue()) && axeSlot >= 36) {
                    getBestAxe(axeSlot);
                }
            }

            if (timer.delay((long)delay.getValue()))
                for (int i = 9; i < 45; i++) {
                    if (mc.thePlayer.inventoryContainer.getSlot(i).getHasStack()) {
                        ItemStack is = mc.thePlayer.inventoryContainer.getSlot(i).getStack();
                        if (shouldDrop(is, i)) {
                            drop(i);
                            timer.reset();
                            if (delay.getValue() > 0) {
                                break;
                            }
                        }
                    }
                }
        }
	}
	
	private void searchForItems() {
		bestArmorDamageReducement = new int[4];
		bestArmorSlots = new int[4];
		bestSwordDamage = -1;
		bestSwordSlot = -1;
		
		Arrays.fill(bestArmorDamageReducement, -1);
		Arrays.fill(bestArmorSlots, -1);
		
		for(int i = 0; i < bestArmorSlots.length; i++) {
			ItemStack itemStack = mc.thePlayer.inventory.armorItemInSlot(i);
			
			if(itemStack != null && itemStack.getItem() != null) {
				if(itemStack.getItem() instanceof ItemArmor) {
					ItemArmor armor = (ItemArmor) itemStack.getItem();
					
					bestArmorDamageReducement[i] = armor.damageReduceAmount;
				}
			}
		}
		
		for(int i = 0; i < 9 * 4; i++) {
			ItemStack itemStack = mc.thePlayer.inventory.getStackInSlot(i);
			
			if(itemStack == null || itemStack.getItem() == null) continue;
			
			if(itemStack.getItem() instanceof ItemArmor) {
				ItemArmor armor = (ItemArmor) itemStack.getItem();
				
				int armorType = 3 - armor.armorType;
				
				if(bestArmorDamageReducement[armorType] < armor.damageReduceAmount + PlayerUtil.getEnchantment(itemStack, Enchantment.protection)) {
					
					bestArmorDamageReducement[armorType] = armor.damageReduceAmount + PlayerUtil.getEnchantment(itemStack, Enchantment.protection);
					bestArmorSlots[armorType] = i;
				}
			}
			if(itemStack.getItem() instanceof ItemSword) {
				ItemSword sword = (ItemSword) itemStack.getItem();
				
				if(bestSwordDamage < sword.getDamageVsEntity() + PlayerUtil.getEnchantment(itemStack, Enchantment.sharpness) + PlayerUtil.getEnchantment(itemStack, Enchantment.fireAspect)) {
					
					bestSwordDamage = sword.getDamageVsEntity() + PlayerUtil.getEnchantment(itemStack, Enchantment.sharpness) + PlayerUtil.getEnchantment(itemStack, Enchantment.fireAspect);
					bestSwordSlot = i;
				}
			}
			if(itemStack.getItem() instanceof ItemTool) {
				ItemTool sword = (ItemTool) itemStack.getItem();
				
				float damage = sword.getToolMaterial().getDamageVsEntity() + PlayerUtil.getEnchantment(itemStack, Enchantment.sharpness) + PlayerUtil.getEnchantment(itemStack, Enchantment.fireAspect);
				
				if(preferSwords) {
					damage -= 1.0f;
				}
				
				if(bestSwordDamage < damage) {
					
					bestSwordDamage = damage;
					bestSwordSlot = i;
					
				}
			}
		}
	}
	private void searchForTrash() {
		trash.clear();
		bestArmorDamageReducement = new int[4];
		bestArmorSlots = new int[4];
		bestSwordDamage = -1;
		bestSwordSlot = -1;
		
		Arrays.fill(bestArmorDamageReducement, -1);
		Arrays.fill(bestArmorSlots, -1);
		
		List<Integer>[] allItems = new List[4];
		
		List<Integer> allSwords = new ArrayList<>();
		
		for(int i = 0; i < bestArmorSlots.length; i++) {
			ItemStack itemStack = mc.thePlayer.inventory.armorItemInSlot(i);
			
			allItems[i] = new ArrayList<>();
			
			if(itemStack != null && itemStack.getItem() != null) {
				if(itemStack.getItem() instanceof ItemArmor) {
					ItemArmor armor = (ItemArmor) itemStack.getItem();
					
					bestArmorDamageReducement[i] = armor.damageReduceAmount + PlayerUtil.getEnchantment(itemStack, Enchantment.protection);
					bestArmorSlots[i] = 8 + i;
				}
			}
		}
		
		for(int i = 0; i < 9 * 4; i++) {
			ItemStack itemStack = mc.thePlayer.inventory.getStackInSlot(i);
			
			if(itemStack == null || itemStack.getItem() == null) continue;
			
			if(itemStack.getItem() instanceof ItemArmor) {
				ItemArmor armor = (ItemArmor) itemStack.getItem();
				
				int armorType = 3 - armor.armorType;
				
				allItems[armorType].add(i);
				
				if(bestArmorDamageReducement[armorType] < armor.damageReduceAmount) {
					
					bestArmorDamageReducement[armorType] = armor.damageReduceAmount + PlayerUtil.getEnchantment(itemStack, Enchantment.protection);
					bestArmorSlots[armorType] = i;
				}
			}
			if(itemStack.getItem() instanceof ItemSword) {
				ItemSword sword = (ItemSword) itemStack.getItem();
				
				allSwords.add(i);
				
				if(bestSwordDamage < sword.getDamageVsEntity() + PlayerUtil.getEnchantment(itemStack, Enchantment.sharpness) + PlayerUtil.getEnchantment(itemStack, Enchantment.fireAspect)) {
					
					bestSwordDamage = sword.getDamageVsEntity() + PlayerUtil.getEnchantment(itemStack, Enchantment.sharpness) + PlayerUtil.getEnchantment(itemStack, Enchantment.fireAspect);
					bestSwordSlot = i;
				}
			}
			if(itemStack.getItem() instanceof ItemTool) {
				ItemTool sword = (ItemTool) itemStack.getItem();
				
				float damage = sword.getToolMaterial().getDamageVsEntity() + PlayerUtil.getEnchantment(itemStack, Enchantment.sharpness) + PlayerUtil.getEnchantment(itemStack, Enchantment.fireAspect);
				
				if(preferSwords) {
					damage -= 1.0f;
				}
				
				if(bestSwordDamage < damage) {
					
					bestSwordDamage = damage;
					bestSwordSlot = i;
					
				}
			}
			if(itemStack.getItem() instanceof ItemAnvilBlock) {
				ItemAnvilBlock ItemAnvilBlock = (ItemAnvilBlock) itemStack.getItem();
				trash.add(i);
			}
			if(itemStack.getItem() instanceof ItemBow) {
				ItemBow ItemAnvilBlock = (ItemBow) itemStack.getItem();
				trash.add(i);
			}
			if(itemStack.getItem() instanceof ItemFishingRod) {
				ItemFishingRod ItemAnvilBlock = (ItemFishingRod) itemStack.getItem();
				trash.add(i);
			}
			if(itemStack.getItem() instanceof ItemEgg) {
				ItemEgg ItemAnvilBlock = (ItemEgg) itemStack.getItem();
				trash.add(i);
			}
			if(itemStack.getItem() instanceof ItemExpBottle) {
				ItemExpBottle ItemExpBottle = (ItemExpBottle) itemStack.getItem();
				trash.add(i);
			}
		}
		for(int i = 0; i < allItems.length; i++) {
			List<Integer> allItem = allItems[i];
			int finalT = i;
			
			allItem.stream().filter(slot -> slot != bestArmorSlots[finalT]).forEach(trash::add);
		}
		allSwords.stream().filter(slot -> slot != bestSwordSlot).forEach(trash::add);
	}
	   public void hotBar() {
	        for (int i = 9; i < 45; i++) {
	            Slot slot = mc.thePlayer.inventoryContainer.getSlot(i);
	            ItemStack stack = slot.getStack();
	            boolean hotbar = i >= 36;

	            if (stack != null) {
	                Item item = stack.getItem();

	                if (!stack.getDisplayName().toLowerCase().contains("(right click)") &&
	                        ((item instanceof ItemPickaxe && needItems(ItemPickaxe.class)) || (item instanceof ItemAxe && needItems(ItemAxe.class)) || (item instanceof ItemSword && needItems(ItemSword.class)) ||
	                                (item instanceof ItemAppleGold ) || (item instanceof ItemEnderPearl && needItems(ItemEnderPearl.class))) 
	                         ) {
	                    moveToHotbar(i);
	                    timer.reset();
	                    return;
	                }
	            }
	        }
	        //for (int i = 9; i < 45; i++) {

	        //}
	    }
	    
	    public void moveToHotbar(int slot) {
	   
	            openInv();

	        mc.playerController.windowClick(mc.thePlayer.inventoryContainer.windowId, slot, 0, 1, mc.thePlayer);

	  
	            closeInv();
	    }
		public boolean getRidOfArmor() {
	        for (int i = 9; i < 45; i++) {
	            Slot slot = mc.thePlayer.inventoryContainer.getSlot(i);
	            ItemStack stack = slot.getStack();
	            boolean hotbar = i >= 36;

	            if (stack != null) {
	                Item item = stack.getItem();

	                if (item instanceof ItemArmor) {
	                    if (!isBestArmor(stack)) {
	                       trash.add(i);
	                        return false;
	                    }
	                }
	            }
	        }
	        return true;
	    
	}
		
	    public void switch1(int slot) {
	    
	           

	        mc.playerController.windowClick(mc.thePlayer.inventoryContainer.windowId, slot, 0, 2, mc.thePlayer);

	      
	    }
		
		
		
		
	    private boolean swordSlot() {
	        for (int i = 9; i < 45; i++) {
	            Slot slot = mc.thePlayer.inventoryContainer.getSlot(i);
	            ItemStack stack = slot.getStack();
	            boolean hotbar = i >= 36;

	            if (stack != null) {
	                Item item = stack.getItem();
	                if (!stack.getDisplayName().toLowerCase().contains("(right click)") && item instanceof ItemSword ) {
	                    switch1(i);
	                    return false;
	                }
	            }
	        }
	        return true;
	    }
	    
	    
	    
	    public void getRidOfTrash() {
	    	getRidOfArmor();
	    	getRidOfTools();
	    	swordSlot();
	    }
	    
	    
	    
	    public boolean getRidOfTools() {
	        for (int i = 9; i < 45; i++) {
	            Slot slot = mc.thePlayer.inventoryContainer.getSlot(i);
	            ItemStack stack = slot.getStack();
	            boolean hotbar = i >= 36;

	            if (stack != null) {
	                Item item = stack.getItem();

	                if (item instanceof ItemTool) {
	                    if (!isBestTool(stack)) {
	                    	getRidOf(i);
	                        return false;
	                    }
	                }
	                if (item instanceof ItemSword) {
	                    if (!isBestSword(stack) ) {
	                        getRidOf(i);
	                        return false;
	                    }
	                }
	            }
	        }

	        return true;
	    }
	    
	    
	    
	    
	    public boolean isBestTool(ItemStack compareStack) {
	        for (int i = 9; i < 45; i++) {
	            Slot slot = mc.thePlayer.inventoryContainer.getSlot(i);
	            ItemStack stack = slot.getStack();
	            boolean hotbar = i >= 36;

	            if (stack != null && compareStack != stack && stack.getItem() instanceof ItemTool) {
	                ItemTool item = (ItemTool) stack.getItem();
	                ItemTool compare = (ItemTool) compareStack.getItem();
	                if (item.getClass() == compare.getClass()) {
	                    if (compare.getStrVsBlock(stack, preferredBlock(item.getClass())) <= item.getStrVsBlock(compareStack, preferredBlock(compare.getClass())))
	                        return false;
	                }
	            }
	        }

	        return true;
	    }
	    
	    
	    public Block preferredBlock(Class clazz) {
	        return clazz == ItemPickaxe.class ? Blocks.cobblestone : clazz == ItemAxe.class ? Blocks.log : Blocks.dirt;
	    }
	    
	    
	    public boolean isBestArmor(ItemStack compareStack) {
	        for (int i = 0; i < 45; i++) {
	            Slot slot = mc.thePlayer.inventoryContainer.getSlot(i);
	            ItemStack stack = slot.getStack();
	            boolean hotbar = i >= 36;

	            if (stack != null && compareStack != stack && stack.getItem() instanceof ItemArmor) {
	                ItemArmor item = (ItemArmor) stack.getItem();
	                ItemArmor compare = (ItemArmor) compareStack.getItem();
	                if (item.armorType == compare.armorType) {
	                    if (protValue(compareStack) <= protValue(stack))
	                        return false;
	                }
	            }
	        }

	        return true;
	    }

	    
	    public static double protValue(ItemStack stack) {
	        return !(stack.getItem() instanceof ItemArmor) ? 0.0D : (double) ((ItemArmor) stack.getItem()).damageReduceAmount + (double) ((100 - ((ItemArmor) stack.getItem()).damageReduceAmount * 4) * EnchantmentHelper.getEnchantmentLevel(Enchantment.protection.effectId, stack) * 4) * 0.0075D;
	    }
	    
	    
	    public boolean needItems(Class<?> type) {
	        for (int i = 36; i < 45; i++) {
	            Slot slot = mc.thePlayer.inventoryContainer.getSlot(i);

	            if (type.isInstance(slot.getStack()))
	                return false;
	        }
	        return true;
	    }
	    
	    
	    public boolean hotbarHasSpace() {
	        for (int i = 36; i < 45; i++) {
	            Slot slot = mc.thePlayer.inventoryContainer.getSlot(i);

	            if (slot.getStack() == null)
	                return true;
	        }
	        return false;
	    }

	    
	    
		public boolean getBestTool() {
			 for (int i = 9; i < 45; i++) {
		            Slot slot = mc.thePlayer.inventoryContainer.getSlot(i);
		            ItemStack stack = slot.getStack();
		            boolean hotbar = i >= 36;
		            if (stack != null) {
		                Item item = stack.getItem();
		            	if (item instanceof ItemTool) {
		                    if (!stack.getDisplayName().toLowerCase().contains("(right click)") && !isBestSword(stack)) {
		                        getRidOf(i);
		                        return false;
		                    }
		        		}
		            }
			 }
			 return true;
		}
		public boolean getBestSword() {
			 for (int i = 9; i < 45; i++) {
		            Slot slot = mc.thePlayer.inventoryContainer.getSlot(i);
		            ItemStack stack = slot.getStack();
		            boolean hotbar = i >= 36;
		            if (stack != null) {
		                Item item = stack.getItem();

			if (item instanceof ItemSword) {
	            if (!stack.getDisplayName().toLowerCase().contains("(right click)") && !isBestSword(stack)) {
	                getRidOf(i);
	                
	                return false;
	            }
			}
		}
			 }
			 
		     return true;
			 }
		
	    public void openInv() {
	     


	       
	    }

	
	    public void closeInv() {
	 
	    }
		
	    public void getRidOf(int slot) { 
	    	openInv();
	        mc.playerController.windowClick(mc.thePlayer.inventoryContainer.windowId, slot, 1, 4, mc.thePlayer);
	    
	        closeInv();
	    }
		
		
		 public static float swordStrength(ItemStack stack) {
			 return (!(stack.getItem() instanceof ItemSword) ? 0.0F : (float) 
					 EnchantmentHelper.getEnchantmentLevel(Enchantment.sharpness.effectId, stack) * 1.25F) + (!(stack.getItem() instanceof ItemSword) ? 0.0F : (float) 
							 EnchantmentHelper.getEnchantmentLevel(Enchantment.fireAspect.effectId, stack) * 1F);
		}
		
		
	    public boolean isBestSword(ItemStack compareStack) {
	        for (int i = 9; i < 45; i++) {
	            Slot slot = mc.thePlayer.inventoryContainer.getSlot(i);
	            ItemStack stack = slot.getStack();
	            boolean hotbar = i >= 36;

	            if (stack != null && compareStack != stack && stack.getItem() instanceof ItemSword) {
	                ItemSword item = (ItemSword) stack.getItem();
	                ItemSword compare = (ItemSword) compareStack.getItem();
	                if (item.getClass() == compare.getClass()) {
	                    if (compare.attackDamage + swordStrength(compareStack) <= item.attackDamage + swordStrength(stack))
	                        return false;
	                }
	            }
	        }

	        return true;
	    }
	    
	    

	    public void shiftClick(int slot) {
	        mc.playerController.windowClick(mc.thePlayer.inventoryContainer.windowId, slot, 0, 1, mc.thePlayer);
	    }

	    public void swap(int slot1, int hotbarSlot) {
	        mc.playerController.windowClick(mc.thePlayer.inventoryContainer.windowId, slot1, hotbarSlot, 2, mc.thePlayer);
	    }

	    public void drop(int slot) {
	        mc.playerController.windowClick(mc.thePlayer.inventoryContainer.windowId, slot, 1, 4, mc.thePlayer);
	    }

	    public boolean isBestWeapon(ItemStack stack) {
	        float damage = getDamage(stack);
	        for (int i = 9; i < 45; i++) {
	            if (mc.thePlayer.inventoryContainer.getSlot(i).getHasStack()) {
	                ItemStack is = mc.thePlayer.inventoryContainer.getSlot(i).getStack();
	                if (getDamage(is) > damage && (is.getItem() instanceof ItemSword || !this.Sword.isEnabled()))
	                    return false;
	            }
	        }
	        if ((stack.getItem() instanceof ItemSword || !this.Sword.isEnabled())) {
	            return true;
	        } else {
	            return false;
	        }

	    }

	    public void getBestWeapon(int slot) {
	        for (int i = 9; i < 45; i++) {
	            if (mc.thePlayer.inventoryContainer.getSlot(i).getHasStack()) {
	                ItemStack is = mc.thePlayer.inventoryContainer.getSlot(i).getStack();
	                if (isBestWeapon(is) && getDamage(is) > 0
	                        && (is.getItem() instanceof ItemSword || !this.Sword.isEnabled())) {
	                    swap(i, slot - 36);
	                    timer.reset();
	                    break;
	                }
	            }
	        }
	    }

	    private float getDamage(ItemStack stack) {
	        float damage = 0;
	        Item item = stack.getItem();
	        if (item instanceof ItemTool) {
	            ItemTool tool = (ItemTool) item;
	            damage += tool.getDamage();
	        }
	        if (item instanceof ItemSword) {
	            ItemSword sword = (ItemSword) item;
	            damage += sword.getDamageVsEntity();
	        }
	        damage += EnchantmentHelper.getEnchantmentLevel(Enchantment.sharpness.effectId, stack) * 1.25f
	                + EnchantmentHelper.getEnchantmentLevel(Enchantment.fireAspect.effectId, stack) * 0.01f;
	        return damage;
	    }

	    public boolean shouldDrop(ItemStack stack, int slot) {

	        if (stack.getDisplayName().contains("���")) {
	            return false;
	        }
	        if (stack.getDisplayName().contains("����")) {
	            return false;
	        }
	        if (stack.getDisplayName().toLowerCase().contains("(right click)")) {
	            return false;
	        }
	      
	        

	        if ((slot == weaponSlot && isBestWeapon(mc.thePlayer.inventoryContainer.getSlot(weaponSlot).getStack()))
	                || (slot == pickaxeSlot
	                && isBestPickaxe(mc.thePlayer.inventoryContainer.getSlot(pickaxeSlot).getStack())
	                && pickaxeSlot >= 0)
	                || (slot == axeSlot && isBestAxe(mc.thePlayer.inventoryContainer.getSlot(axeSlot).getStack())
	                && axeSlot >= 0)
	                || (slot == shovelSlot && isBestShovel(mc.thePlayer.inventoryContainer.getSlot(shovelSlot).getStack())
	                && shovelSlot >= 0)) {
	            return false;
	        }
	        if (stack.getItem() instanceof ItemArmor) {
	            for (int type = 1; type < 5; type++) {
	                if (mc.thePlayer.inventoryContainer.getSlot(4 + type).getHasStack()) {
	                    ItemStack is = mc.thePlayer.inventoryContainer.getSlot(4 + type).getStack();
	                    if (this.isBestArmor(is, type)) {
	                        continue;
	                    }
	                }
	                if (this.isBestArmor(stack, type)) {
	                    return false;
	                }
	            }
	        }
	        if (stack.getItem() instanceof ItemBlock && (getBlockCount() > this.BlockCap.getValue()
	                || Scaffold.blacklistedBlocks.contains(((ItemBlock) stack.getItem()).getBlock()))) {
	            return true;
	        }
	        if (stack.getItem() instanceof ItemPotion) {
	            if (isBadPotion(stack)) {
	                return true;
	            }
	        }
	        if (stack.getItem() instanceof ItemFood && this.Food.isEnabled()
	                && !(stack.getItem() instanceof ItemAppleGold)) {
	            return true;
	        }
	        if (stack.getItem() instanceof ItemHoe || stack.getItem() instanceof ItemTool
	                || stack.getItem() instanceof ItemSword || stack.getItem() instanceof ItemArmor) {
	            return true;
	        }
	        if ((stack.getItem() instanceof ItemBow || stack.getItem().getUnlocalizedName().contains("arrow"))
	                && (Boolean) this.Bow.isEnabled()) {
	            return true;
	        }

	        return (stack.getItem().getUnlocalizedName().contains("tnt"))
	                || (stack.getItem().getUnlocalizedName().contains("stick"))
	                || (stack.getItem().getUnlocalizedName().contains("egg"))
	                || (stack.getItem().getUnlocalizedName().contains("string"))
	                || (stack.getItem().getUnlocalizedName().contains("cake"))
	                || (stack.getItem().getUnlocalizedName().contains("mushroom"))
	                || (stack.getItem().getUnlocalizedName().contains("flint"))
	                || (stack.getItem().getUnlocalizedName().contains("compass"))
	                || (stack.getItem().getUnlocalizedName().contains("dyePowder"))
	                || (stack.getItem().getUnlocalizedName().contains("feather"))
	                || (stack.getItem().getUnlocalizedName().contains("bucket"))
	                || (stack.getItem().getUnlocalizedName().contains("chest")
	                && !stack.getDisplayName().toLowerCase().contains("collect"))
	                || (stack.getItem().getUnlocalizedName().contains("snow"))
	                || (stack.getItem().getUnlocalizedName().contains("fish"))
	                || (stack.getItem().getUnlocalizedName().contains("enchant"))
	                || (stack.getItem().getUnlocalizedName().contains("exp"))
	                || (stack.getItem().getUnlocalizedName().contains("shears"))
	                || (stack.getItem().getUnlocalizedName().contains("anvil"))
	                || (stack.getItem().getUnlocalizedName().contains("torch"))
	                || (stack.getItem().getUnlocalizedName().contains("seeds"))
	                || (stack.getItem().getUnlocalizedName().contains("leather"))
	                || (stack.getItem().getUnlocalizedName().contains("reeds"))
	                || (stack.getItem().getUnlocalizedName().contains("skull"))
	                || (stack.getItem().getUnlocalizedName().contains("record"))
	                || (stack.getItem().getUnlocalizedName().contains("snowball"))
	                || (stack.getItem() instanceof ItemGlassBottle)
	                || (stack.getItem().getUnlocalizedName().contains("piston"));
	    }

	    public ArrayList<Integer> getWhitelistedItem() {
	        return whitelistedItems;
	    }

	    private int getBlockCount() {
	        int blockCount = 0;
	        for (int i = 0; i < 45; i++) {
	            if (mc.thePlayer.inventoryContainer.getSlot(i).getHasStack()) {
	                ItemStack is = mc.thePlayer.inventoryContainer.getSlot(i).getStack();
	                Item item = is.getItem();
	                if (is.getItem() instanceof ItemBlock
	                        && !Scaffold.blacklistedBlocks.contains(((ItemBlock) item).getBlock())) {
	                    blockCount += is.stackSize;
	                }
	            }
	        }
	        return blockCount;
	    }

	    private void getBestPickaxe(int slot) {
	        for (int i = 9; i < 45; i++) {
	            if (mc.thePlayer.inventoryContainer.getSlot(i).getHasStack()) {
	                ItemStack is = mc.thePlayer.inventoryContainer.getSlot(i).getStack();

	                if (isBestPickaxe(is) && pickaxeSlot != i) {
	                    if (!isBestWeapon(is)) {
	                        if (!mc.thePlayer.inventoryContainer.getSlot(pickaxeSlot).getHasStack()) {
	                            swap(i, pickaxeSlot - 36);
	                            timer.reset();
	                            if (this.delay.getValue() > 0) {
	                                return;
	                            }
	                        } else if (!isBestPickaxe(mc.thePlayer.inventoryContainer.getSlot(pickaxeSlot).getStack())) {
	                            swap(i, pickaxeSlot - 36);
	                            timer.reset();
	                            if (this.delay.getValue() > 0) {
	                                return;
	                            }
	                        }
	                    }

	                }
	            }
	        }
	    }

	    private void getBestShovel(int slot) {
	        for (int i = 9; i < 45; i++) {
	            if (mc.thePlayer.inventoryContainer.getSlot(i).getHasStack()) {
	                ItemStack is = mc.thePlayer.inventoryContainer.getSlot(i).getStack();

	                if (isBestShovel(is) && shovelSlot != i) {
	                    if (!isBestWeapon(is)) {
	                        if (!mc.thePlayer.inventoryContainer.getSlot(shovelSlot).getHasStack()) {
	                            swap(i, shovelSlot - 36);
	                            timer.reset();
	                            if (this.delay.getValue() > 0) {
	                                return;
	                            }
	                        } else if (!isBestShovel(mc.thePlayer.inventoryContainer.getSlot(shovelSlot).getStack())) {
	                            swap(i, shovelSlot - 36);
	                            timer.reset();
	                            if (this.delay.getValue() > 0) {
	                                return;
	                            }
	                        }
	                    }
	                }
	            }
	        }
	    }

	    private void getBestAxe(int slot) {

	        for (int i = 9; i < 45; i++) {
	            if (mc.thePlayer.inventoryContainer.getSlot(i).getHasStack()) {
	                ItemStack is = mc.thePlayer.inventoryContainer.getSlot(i).getStack();

	                if (isBestAxe(is) && axeSlot != i) {
	                    if (!isBestWeapon(is)) {
	                        if (!mc.thePlayer.inventoryContainer.getSlot(axeSlot).getHasStack()) {
	                            swap(i, axeSlot - 36);
	                            timer.reset();
	                            if (this.delay.getValue() > 0) {
	                                return;
	                            }
	                        } else if (!isBestAxe(mc.thePlayer.inventoryContainer.getSlot(axeSlot).getStack())) {
	                            swap(i, axeSlot - 36);
	                            timer.reset();
	                            if (this.delay.getValue() > 0) {
	                                return;
	                            }
	                        }
	                    }

	                }
	            }
	        }
	    }

	    private boolean isBestPickaxe(ItemStack stack) {
	        Item item = stack.getItem();
	        if (!(item instanceof ItemPickaxe)) {
	            return false;
	        }
	        float value = getToolEffect(stack);
	        for (int i = 9; i < 45; i++) {
	            if (mc.thePlayer.inventoryContainer.getSlot(i).getHasStack()) {
	                ItemStack is = mc.thePlayer.inventoryContainer.getSlot(i).getStack();
	                if (getToolEffect(is) > value && is.getItem() instanceof ItemPickaxe) {
	                    return false;
	                }

	            }
	        }
	        return true;
	    }

	    private boolean isBestShovel(ItemStack stack) {
	        Item item = stack.getItem();
	        if (!(item instanceof ItemSpade)) {
	            return false;
	        }
	        float value = getToolEffect(stack);
	        for (int i = 9; i < 45; i++) {
	            if (mc.thePlayer.inventoryContainer.getSlot(i).getHasStack()) {
	                ItemStack is = mc.thePlayer.inventoryContainer.getSlot(i).getStack();
	                if (getToolEffect(is) > value && is.getItem() instanceof ItemSpade) {
	                    return false;
	                }

	            }
	        }
	        return true;
	    }

	    private boolean isBestAxe(ItemStack stack) {
	        Item item = stack.getItem();
	        if (!(item instanceof ItemAxe))
	            return false;
	        float value = getToolEffect(stack);
	        for (int i = 9; i < 45; i++) {
	            if (mc.thePlayer.inventoryContainer.getSlot(i).getHasStack()) {
	                ItemStack is = mc.thePlayer.inventoryContainer.getSlot(i).getStack();
	                if (getToolEffect(is) > value && is.getItem() instanceof ItemAxe && !isBestWeapon(stack)) {
	                    return false;
	                }

	            }
	        }
	        return true;
	    }

	    private float getToolEffect(ItemStack stack) {
	        Item item = stack.getItem();
	        if (!(item instanceof ItemTool))
	            return 0;
	        String name = item.getUnlocalizedName();
	        ItemTool tool = (ItemTool) item;
	        float value = 1;
	        if (item instanceof ItemPickaxe) {
	            value = tool.getStrVsBlock(stack, Blocks.stone);
	            if (name.toLowerCase().contains("gold")) {
	                value -= 5;
	            }
	        } else if (item instanceof ItemSpade) {
	            value = tool.getStrVsBlock(stack, Blocks.dirt);
	            if (name.toLowerCase().contains("gold")) {
	                value -= 5;
	            }
	        } else if (item instanceof ItemAxe) {
	            value = tool.getStrVsBlock(stack, Blocks.log);
	            if (name.toLowerCase().contains("gold")) {
	                value -= 5;
	            }
	        } else
	            return 1f;
	        value += EnchantmentHelper.getEnchantmentLevel(Enchantment.efficiency.effectId, stack) * 0.0075D;
	        value += EnchantmentHelper.getEnchantmentLevel(Enchantment.unbreaking.effectId, stack) / 100d;
	        return value;
	    }

	    private boolean isBadPotion(ItemStack stack) {
	        if (stack != null && stack.getItem() instanceof ItemPotion) {
	            final ItemPotion potion = (ItemPotion) stack.getItem();
	            if (potion.getEffects(stack) == null)
	                return true;
	            for (final Object o : potion.getEffects(stack)) {
	                final PotionEffect effect = (PotionEffect) o;
	                if (effect.getPotionID() == Potion.poison.getId() || effect.getPotionID() == Potion.harm.getId()
	                        || effect.getPotionID() == Potion.moveSlowdown.getId()
	                        || effect.getPotionID() == Potion.weakness.getId()) {
	                    return true;
	                }
	            }
	        }
	        return false;
	    }

	    boolean invContainsType(int type) {

	        for (int i = 9; i < 45; i++) {
	            if (mc.thePlayer.inventoryContainer.getSlot(i).getHasStack()) {
	                ItemStack is = mc.thePlayer.inventoryContainer.getSlot(i).getStack();
	                Item item = is.getItem();
	                if (item instanceof ItemArmor) {
	                    ItemArmor armor = (ItemArmor) item;
	                    if (type == armor.armorType) {
	                        return true;
	                    }
	                }
	            }
	        }
	        return false;
	    }

	    public void getBestArmor() {
	        for (int type = 1; type < 5; type++) {
	            if (mc.thePlayer.inventoryContainer.getSlot(4 + type).getHasStack()) {
	                ItemStack is = mc.thePlayer.inventoryContainer.getSlot(4 + type).getStack();
	                if (this.isBestArmor(is, type)) {
	                    continue;
	                } else {
	                    drop(4 + type);
	                }
	            }
	            for (int i = 9; i < 45; i++) {
	                if (mc.thePlayer.inventoryContainer.getSlot(i).getHasStack()) {
	                    ItemStack is = mc.thePlayer.inventoryContainer.getSlot(i).getStack();
	                    if (this.isBestArmor(is, type) && this.getProtection(is) > 0) {
	                        shiftClick(i);
	                        timer.reset();
	                        if (this.delay.getValue() > 0)
	                            return;
	                    }
	                }
	            }
	        }
	    }
	    public static boolean isBestArmor(ItemStack stack, int type) {
			float prot = getProtection(stack);
			String strType = "";
			if (type == 1) {
				strType = "helmet";
			} else if (type == 2) {
				strType = "chestplate";
			} else if (type == 3) {
				strType = "leggings";
			} else if (type == 4) {
				strType = "boots";
			}
			if (!stack.getUnlocalizedName().contains(strType)) {
				return false;
			}
			for (int i = 5; i < 45; i++) {
				if (Minecraft.getMinecraft().thePlayer.inventoryContainer.getSlot(i).getHasStack()) {
					ItemStack is = Minecraft.getMinecraft().thePlayer.inventoryContainer.getSlot(i).getStack();
					if (getProtection(is) > prot && is.getUnlocalizedName().contains(strType))
						return false;
				}
			}
			return true;
		}
	    

		public static float getProtection(ItemStack stack) {
			float prot = 0;
			if ((stack.getItem() instanceof ItemArmor)) {
				ItemArmor armor = (ItemArmor) stack.getItem();
				prot += armor.damageReduceAmount + (100 - armor.damageReduceAmount)
						* EnchantmentHelper.getEnchantmentLevel(Enchantment.protection.effectId, stack) * 0.0075D;
				prot += EnchantmentHelper.getEnchantmentLevel(Enchantment.blastProtection.effectId, stack) / 100d;
				prot += EnchantmentHelper.getEnchantmentLevel(Enchantment.fireProtection.effectId, stack) / 100d;
				prot += EnchantmentHelper.getEnchantmentLevel(Enchantment.thorns.effectId, stack) / 100d;
				prot += EnchantmentHelper.getEnchantmentLevel(Enchantment.unbreaking.effectId, stack) / 50d;
				prot += EnchantmentHelper.getEnchantmentLevel(Enchantment.featherFalling.effectId, stack) / 100d;
			}
			return prot;
		}  
}
