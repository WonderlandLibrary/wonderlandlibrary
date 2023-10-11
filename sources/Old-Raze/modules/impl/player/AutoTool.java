package markgg.modules.impl.player;

import javax.vecmath.Vector3d;

import de.gerrygames.viarewind.utils.math.RayTracing;
import markgg.event.handler.EventHandler;
import markgg.event.handler.Listener;
import markgg.event.impl.MotionEvent;
import markgg.modules.Module;
import markgg.modules.ModuleInfo;
import net.minecraft.block.Block;
import net.minecraft.block.BlockAnvil;
import net.minecraft.block.BlockBookshelf;
import net.minecraft.block.BlockBush;
import net.minecraft.block.BlockCarpet;
import net.minecraft.block.BlockCauldron;
import net.minecraft.block.BlockChest;
import net.minecraft.block.BlockColored;
import net.minecraft.block.BlockDirt;
import net.minecraft.block.BlockDoor;
import net.minecraft.block.BlockDropper;
import net.minecraft.block.BlockEnderChest;
import net.minecraft.block.BlockFarmland;
import net.minecraft.block.BlockFence;
import net.minecraft.block.BlockFenceGate;
import net.minecraft.block.BlockFurnace;
import net.minecraft.block.BlockGlass;
import net.minecraft.block.BlockGrass;
import net.minecraft.block.BlockGravel;
import net.minecraft.block.BlockHardenedClay;
import net.minecraft.block.BlockHopper;
import net.minecraft.block.BlockLadder;
import net.minecraft.block.BlockLeaves;
import net.minecraft.block.BlockLog;
import net.minecraft.block.BlockObsidian;
import net.minecraft.block.BlockOre;
import net.minecraft.block.BlockPane;
import net.minecraft.block.BlockPlanks;
import net.minecraft.block.BlockPressurePlate;
import net.minecraft.block.BlockPumpkin;
import net.minecraft.block.BlockRedSandstone;
import net.minecraft.block.BlockSand;
import net.minecraft.block.BlockSandStone;
import net.minecraft.block.BlockSoulSand;
import net.minecraft.block.BlockStainedGlass;
import net.minecraft.block.BlockStainedGlassPane;
import net.minecraft.block.BlockStone;
import net.minecraft.block.BlockStoneBrick;
import net.minecraft.block.BlockTrapDoor;
import net.minecraft.block.BlockWeb;
import net.minecraft.block.BlockWorkbench;
import net.minecraft.client.Minecraft;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemAxe;
import net.minecraft.item.ItemPickaxe;
import net.minecraft.item.ItemShears;
import net.minecraft.item.ItemSpade;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemSword;
import net.minecraft.util.BlockPos;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.util.Vec3;
import net.minecraft.world.World;

@ModuleInfo(name = "AutoTool", category = Module.Category.PLAYER)
public class AutoTool extends Module {

	private int oldSlot;
	private static boolean hasSwitched;
	private static boolean hasSwitched2;
	private static boolean hasSwitched3;
	private static boolean hasSwitched4;

	public void onEnable() {
		oldSlot = mc.thePlayer.inventory.currentItem;
		hasSwitched = false;
		hasSwitched2 = false;
		hasSwitched3 = false;
		hasSwitched4 = false;
	}

	public void onDisable() {
		if (hasSwitched) {
			mc.thePlayer.inventory.currentItem = oldSlot;
			hasSwitched = false;
			hasSwitched2 = false;
			hasSwitched3 = false;
			hasSwitched4 = false;
		}
	}

	@EventHandler
	private final Listener<MotionEvent> e = e -> {
		if (e.getType() == MotionEvent.Type.PRE) {
			if(mc.gameSettings.keyBindAttack.pressed) {
				MovingObjectPosition blockPos = mc.objectMouseOver;
				if (blockPos != null && blockPos.typeOfHit == MovingObjectPosition.MovingObjectType.BLOCK) {
					BlockPos pos = blockPos.getBlockPos();
					Block block = mc.theWorld.getBlockState(pos).getBlock();
					if (block instanceof BlockDirt || block instanceof BlockGrass || block instanceof BlockSand || block instanceof BlockGravel || block instanceof BlockFarmland || block instanceof BlockSoulSand) {
						getBestShovel();
					} else if (block instanceof BlockStone || block instanceof BlockSandStone || block instanceof BlockDropper || block instanceof BlockFurnace || block instanceof BlockHopper || block instanceof BlockAnvil 
							|| block instanceof BlockCauldron  || block instanceof BlockEnderChest || block instanceof BlockHardenedClay || block instanceof BlockObsidian
							|| block instanceof BlockStoneBrick || block instanceof BlockOre || block instanceof BlockRedSandstone || block == Blocks.end_stone || block == Blocks.cobblestone
							|| block == Blocks.stained_hardened_clay) {
						getBestPickaxe();
					} else if (block instanceof BlockPlanks || block instanceof BlockLog || block instanceof BlockFence || block instanceof BlockChest || block instanceof BlockFenceGate 
							|| block instanceof BlockTrapDoor || block instanceof BlockDoor || block instanceof BlockPumpkin || block instanceof BlockBookshelf || block instanceof BlockWorkbench
							|| block instanceof BlockPressurePlate || block instanceof BlockGlass || block instanceof BlockPane || block instanceof BlockStainedGlass 
							|| block instanceof BlockStainedGlassPane || block instanceof BlockLadder) {
						getBestAxe();
					} else if (block instanceof BlockWeb || block instanceof BlockCarpet || block instanceof BlockBush || block instanceof BlockLeaves || block == Blocks.wool) {
						getShears();
					}
				}
			} else {
				if (hasSwitched || hasSwitched2|| hasSwitched3 || hasSwitched4) {
					mc.thePlayer.inventory.currentItem = oldSlot;
		            hasSwitched = false;
		            hasSwitched2 = false;
		            hasSwitched3 = false;
		            hasSwitched4 = false;
				}
	        }
		}
	};

	public static void getBestShovel() {
	    float damageModifier = 0;
	    int newItem = -1;
	    for (int slot = 0; slot < 9; slot++) {
	        ItemStack stack = Minecraft.getMinecraft().thePlayer.inventory.mainInventory[slot];
	        if (stack == null) {
	            continue;
	        }
	        if (stack.getItem() instanceof ItemSpade) {
	            ItemSpade spade = (ItemSpade) stack.getItem();
	            float damage = spade.getMaxDamage() + (spade.hasEffect(stack) ? getEnchantment(stack) : 0);
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
	
	public static void getBestPickaxe() {
	    float damageModifier = 0;
	    int newItem = -1;
	    for (int slot = 0; slot < 9; slot++) {
	        ItemStack stack = Minecraft.getMinecraft().thePlayer.inventory.mainInventory[slot];
	        if (stack == null) {
	            continue;
	        }
	        if (stack.getItem() instanceof ItemPickaxe) {
	        	ItemPickaxe pickax = (ItemPickaxe) stack.getItem();
	            float damage = pickax.getMaxDamage() + (pickax.hasEffect(stack) ? getEnchantment(stack) : 0);
	            if (damage >= damageModifier) {
	                newItem = slot;
	                damageModifier = damage;
	            }
	        }
	    }
	    if (newItem > -1) {
	        Minecraft.getMinecraft().thePlayer.inventory.currentItem = newItem;
	        hasSwitched2 = true;
	    }
	}
	
	public static void getBestAxe() {
	    float damageModifier = 0;
	    int newItem = -1;
	    for (int slot = 0; slot < 9; slot++) {
	        ItemStack stack = Minecraft.getMinecraft().thePlayer.inventory.mainInventory[slot];
	        if (stack == null) {
	            continue;
	        }
	        if (stack.getItem() instanceof ItemAxe) {
	        	ItemAxe axe = (ItemAxe) stack.getItem();
	            float damage = axe.getMaxDamage() + (axe.hasEffect(stack) ? getEnchantment(stack) : 0);
	            if (damage >= damageModifier) {
	                newItem = slot;
	                damageModifier = damage;
	            }
	        }
	    }
	    if (newItem > -1) {
	        Minecraft.getMinecraft().thePlayer.inventory.currentItem = newItem;
	        hasSwitched3 = true;
	    }
	}
	
	public static void getShears() {
	    float damageModifier = 0;
	    int newItem = -1;
	    for (int slot = 0; slot < 9; slot++) {
	        ItemStack stack = Minecraft.getMinecraft().thePlayer.inventory.mainInventory[slot];
	        if (stack == null) {
	            continue;
	        }
	        if (stack.getItem() instanceof ItemShears) {
	        	ItemShears shear = (ItemShears) stack.getItem();
	            float damage = shear.getMaxDamage() + (shear.hasEffect(stack) ? getEnchantment(stack) : 0);
	            if (damage >= damageModifier) {
	                newItem = slot;
	                damageModifier = damage;
	            }
	        }
	    }
	    if (newItem > -1) {
	        Minecraft.getMinecraft().thePlayer.inventory.currentItem = newItem;
	        hasSwitched4 = true;
	    }
	}

	public static int getEnchantment(ItemStack i) {
	    return EnchantmentHelper.getEnchantmentLevel(Enchantment.efficiency.effectId, i);
	}

}
