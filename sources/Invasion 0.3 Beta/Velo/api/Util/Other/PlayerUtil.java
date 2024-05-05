package Velo.api.Util.Other;

import java.awt.Color;
import java.util.Iterator;
import java.util.Objects;
import java.util.regex.Pattern;

import net.minecraft.block.*;
import net.minecraft.block.material.Material;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.item.EntityArmorStand;
import net.minecraft.entity.monster.EntityGolem;
import net.minecraft.entity.monster.EntityMob;
import net.minecraft.entity.passive.EntityAnimal;
import net.minecraft.entity.passive.EntityBat;
import net.minecraft.entity.passive.EntityVillager;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemArmor;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemEgg;
import net.minecraft.item.ItemFood;
import net.minecraft.item.ItemGlassBottle;
import net.minecraft.item.ItemPotion;
import net.minecraft.item.ItemSnow;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemSword;
import net.minecraft.item.ItemTool;
import net.minecraft.nbt.JsonToNBT;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.BlockPos;
import net.minecraft.util.MathHelper;
import net.minecraft.util.ResourceLocation;

public class PlayerUtil {
	
	static Minecraft mc = Minecraft.getMinecraft();
	
	 public static boolean isBetterArmor(int slot, int[] armorType) {
	        if(Minecraft.getMinecraft().thePlayer.inventory.armorInventory[slot] != null) {
	            int currentIndex = 0;
	            int invIndex = 0;
	            int finalCurrentIndex = -1;
	            int finalInvIndex = -1;
	            int[] array;
	            int j = (array = armorType).length;
	            for(int i = 0; i < j; i++) {
	                int armor = array[i];
	                if(Item.getIdFromItem(Minecraft.getMinecraft().thePlayer.inventory.armorInventory[slot].getItem()) == armor) {
	                    finalCurrentIndex = currentIndex;
	                    break;
	                }
	                currentIndex++;
	            }
	            j = (array = armorType).length;
	            for(int i = 0; i < j; i++) {
	                int armor = array[i];
	                if(getItem(armor) != -1) {
	                    finalInvIndex = invIndex;
	                    break;
	                }
	                invIndex++;
	            }
	            if(finalInvIndex > -1)
	                return finalInvIndex < finalCurrentIndex;
	        }
	        return false;
	    }

	    public static int getItem(int id) {
	        for(int i = 9; i < 45; i++) {
	            ItemStack item = Minecraft.getMinecraft().thePlayer.inventoryContainer.getSlot(i).getStack();
	            if(item != null && Item.getIdFromItem(item.getItem()) == id)
	                return i;
	        }
	        return -1;
	    }
	    


	    public static void swap(int slot, int hotBarSlot) {
	       mc.playerController.windowClick(mc.thePlayer.inventoryContainer.windowId, slot, hotBarSlot, 2, mc.thePlayer);
	    }

	    public static boolean isValidItem(ItemStack itemStack) {
	       if (itemStack.getDisplayName().startsWith("§a")) {
	          return true;
	       } else {
	          return itemStack.getItem() instanceof ItemArmor || itemStack.getItem() instanceof ItemSword || itemStack.getItem() instanceof ItemTool || itemStack.getItem() instanceof ItemFood || itemStack.getItem() instanceof ItemPotion && !isBadPotion(itemStack) || itemStack.getItem() instanceof ItemBlock || itemStack.getDisplayName().contains("Play") || itemStack.getDisplayName().contains("Game") || itemStack.getDisplayName().contains("Right Click");
	       }
	    }

	    public static float getDamageLevel(ItemStack stack) {
	       if (stack.getItem() instanceof ItemSword) {
	          ItemSword sword = (ItemSword)stack.getItem();
	          float sharpness = (float)EnchantmentHelper.getEnchantmentLevel(Enchantment.sharpness.effectId, stack) * 1.25F;
	          float fireAspect = (float)EnchantmentHelper.getEnchantmentLevel(Enchantment.fireAspect.effectId, stack) * 1.5F;
	          return sword.getDamageVsEntity() + sharpness + fireAspect;
	       } else {
	          return 0.0F;
	       }
	    }
	    
		
		 public static boolean isTrash(ItemStack item) {
		        return ((item.getItem().getUnlocalizedName().contains("tnt")) || item.getDisplayName().contains("frog") ||
		                (item.getItem().getUnlocalizedName().contains("stick")) ||
		                (item.getItem().getUnlocalizedName().contains("string")) || (item.getItem().getUnlocalizedName().contains("flint")) ||
		                (item.getItem().getUnlocalizedName().contains("feather")) || (item.getItem().getUnlocalizedName().contains("bucket")) ||
		                (item.getItem().getUnlocalizedName().contains("snow")) || (item.getItem().getUnlocalizedName().contains("enchant")) ||
		                (item.getItem().getUnlocalizedName().contains("exp")) || (item.getItem().getUnlocalizedName().contains("shears")) ||
		                (item.getItem().getUnlocalizedName().contains("arrow")) || (item.getItem().getUnlocalizedName().contains("anvil")) ||
		                (item.getItem().getUnlocalizedName().contains("torch")) || (item.getItem().getUnlocalizedName().contains("seeds")) ||
		                (item.getItem().getUnlocalizedName().contains("leather")) || (item.getItem().getUnlocalizedName().contains("boat")) ||
		                (item.getItem().getUnlocalizedName().contains("fishing")) || (item.getItem().getUnlocalizedName().contains("wheat")) ||
		                (item.getItem().getUnlocalizedName().contains("flower")) || (item.getItem().getUnlocalizedName().contains("record")) ||
		                (item.getItem().getUnlocalizedName().contains("note")) || (item.getItem().getUnlocalizedName().contains("sugar")) ||
		                (item.getItem().getUnlocalizedName().contains("wire")) || (item.getItem().getUnlocalizedName().contains("trip")) ||
		                (item.getItem().getUnlocalizedName().contains("slime")) || (item.getItem().getUnlocalizedName().contains("web")) ||
		                ((item.getItem() instanceof ItemGlassBottle)) || (item.getItem().getUnlocalizedName().contains("piston")) ||
		                (item.getItem().getUnlocalizedName().contains("potion") && (isBadPotion(item))) ||
		                //   ((item.getItem() instanceof ItemArmor) && isBestArmor(item)) ||
		                (item.getItem() instanceof ItemEgg || item.getItem() instanceof ItemSnow || (item.getItem().getUnlocalizedName().contains("bow")) && !item.getDisplayName().contains("Kit")) ||
		                //   ((item.getItem() instanceof ItemSword) && !isBestSword(item)) ||
		                (item.getItem().getUnlocalizedName().contains("Raw")));
		    }

	
		    
		    
		    
		    
	    public static ItemStack createItem(String itemArguments) {
	        try {
	            itemArguments = itemArguments.replace('&', '§');
	            Item item = new Item();
	            String[] args = null;
	            int i = 1;
	            int j = 0;

	            for(int mode = 0; mode <= Math.min(12, itemArguments.length() - 2); ++mode) {
	                args = itemArguments.substring(mode).split(Pattern.quote(" "));
	                ResourceLocation resourcelocation = new ResourceLocation(args[0]);
	                item = (Item) Item.itemRegistry.getObject(resourcelocation);
	                if(item != null)
	                    break;
	            }

	            if(item == null)
	                return null;

	            if(Objects.requireNonNull(args).length >= 2 && args[1].matches("\\d+"))
	                i = Integer.parseInt(args[1]);
	            if(args.length >= 3 && args[2].matches("\\d+"))
	                j = Integer.parseInt(args[2]);

	            ItemStack itemstack = new ItemStack(item, i, j);
	            if(args.length >= 4) {
	                StringBuilder NBT = new StringBuilder();
	                for(int nbtcount = 3; nbtcount < args.length; ++nbtcount)
	                    NBT.append(" ").append(args[nbtcount]);
	                itemstack.setTagCompound(JsonToNBT.getTagFromJson(NBT.toString()));
	            }
	            return itemstack;
	        }catch(Exception exception) {
	            exception.printStackTrace();
	            return null;
	        }
	    }

	    public static int getEnchantment(final ItemStack itemStack, final Enchantment enchantment) {
	        if(itemStack == null || itemStack.getEnchantmentTagList() == null || itemStack.getEnchantmentTagList().hasNoTags())
	            return 0;

	        for(int i = 0; i < itemStack.getEnchantmentTagList().tagCount(); i++) {
	            final NBTTagCompound tagCompound = itemStack.getEnchantmentTagList().getCompoundTagAt(i);

	            if(tagCompound.getShort("ench") == enchantment.effectId || tagCompound.getShort("id") == enchantment.effectId)
	                return tagCompound.getShort("lvl");
	        }

	        return 0;
	    }
	    
	    
	    
	    public static int findItem(final int startSlot, final int endSlot, final Item item) {
	        for(int i = startSlot; i < endSlot; i++) {
	            final ItemStack stack = mc.thePlayer.inventoryContainer.getSlot(i).getStack();

	            if(stack != null && stack.getItem() == item)
	                return i;
	        }
	        return -1;
	    }


	    public static boolean isBadPotion(ItemStack stack) {
	       if (stack != null && stack.getItem() instanceof ItemPotion) {
	          ItemPotion potion = (ItemPotion)stack.getItem();
	          if (ItemPotion.isSplash(stack.getItemDamage())) {
	             Iterator var2 = potion.getEffects(stack).iterator();

	             while(var2.hasNext()) {
	                Object o = var2.next();
	                PotionEffect effect = (PotionEffect)o;
	                if (effect.getPotionID() == Potion.poison.getId() || effect.getPotionID() == Potion.harm.getId() || effect.getPotionID() == Potion.moveSlowdown.getId() || effect.getPotionID() == Potion.weakness.getId()) {
	                   return true;
	                }
	             }
	          }
	       }

	       return false;
	    }

	public static boolean isOnSameTeam(EntityLivingBase entity) {
        if (entity.getTeam() != null && mc.thePlayer.getTeam() != null) {
            char c1 = entity.getDisplayName().getFormattedText().charAt(1);
            char c2 = mc.thePlayer.getDisplayName().getFormattedText().charAt(1);
            return c1 == c2;
        } else {
            return false;
        }
	}

    public static EntityPlayerSP getLocalPlayer() {
        return Minecraft.getMinecraft().thePlayer;
    }

    public static boolean isValidBlock(Block block, boolean toPlace) {
        if (block instanceof BlockContainer)
            return false;
        if (toPlace) {
            return !(block instanceof BlockFalling) && block.isFullBlock() && block.isFullCube();
        } else {
            final Material material = block.getMaterial();
            return !material.isReplaceable() && !material.isLiquid();
        }
    }
	
	public static boolean isOnServer(final String serverName) {
		return !mc.isSingleplayer() && mc.getCurrentServerData().serverIP.contains(serverName);
	}
	
	public static void tpRel(double x, double y, double z) {
		final EntityPlayerSP player = mc.thePlayer;
		player.setPosition(mc.thePlayer.posX + x, mc.thePlayer.posY + y, mc.thePlayer.posZ + z);
	}

	public static boolean isValid(EntityLivingBase entity, boolean players, boolean monsters, boolean animals, boolean teams, boolean invisibles, boolean passives, double range) {
		if (entity instanceof EntityPlayer && !players) {
			return false;
		}
		if (entity instanceof EntityMob && !monsters) {
			return false;
		}
		if (entity instanceof EntityVillager || entity instanceof EntityGolem && !passives) {
			return false;
		}
		if (entity instanceof EntityAnimal && !animals) {
			return false;
		}
	
		if (entity == mc.thePlayer || entity.isDead || entity.getHealth() == 0 || mc.thePlayer.getDistanceToEntity(entity) > range) {
			return false;
		}
		if (entity.isInvisible() && !invisibles) {
			return false;
		}
		if (PlayerUtil.isOnSameTeam(entity) && teams) {
			return false;
		}
		if (entity instanceof EntityBat) {
			return false;
		}
		return !(entity instanceof EntityArmorStand);
	}

   

    public static boolean isOnLiquid() {
        if (mc.thePlayer == null) return false;
        boolean onLiquid = false;
        final int y = (int) mc.thePlayer.getEntityBoundingBox().offset(0.0D, -0.01D, 0.0D).minY;
        for (int x = MathHelper.floor_double(mc.thePlayer.getEntityBoundingBox().minX); x < MathHelper.floor_double(mc.thePlayer.getEntityBoundingBox().maxX) + 1; x++) {
            for (int z = MathHelper.floor_double(mc.thePlayer.getEntityBoundingBox().minZ); z < MathHelper.floor_double(mc.thePlayer.getEntityBoundingBox().maxZ) + 1; z++) {
                final Block block = mc.theWorld.getBlockState(new BlockPos(x, y, z)).getBlock();
                if (block != null && block.getMaterial() != Material.air) {
                    if (!(block instanceof BlockLiquid)) return false;
                    onLiquid = true;
                }
            }
        }
        return onLiquid;
    }
    public static boolean isOnLiquid(double profondeur)
	  {
	    boolean onLiquid = false;
	    
	    if(mc.theWorld.getBlockState(new BlockPos(mc.thePlayer.posX, mc.thePlayer.posY - profondeur, mc.thePlayer.posZ)).getBlock().getMaterial().isLiquid()) {
	      onLiquid = true;
	    }
	    return onLiquid;
	  }
    public static boolean isTotalOnLiquid(double profondeur)
	  {	    
	    for(double x = mc.thePlayer.boundingBox.minX; x < mc.thePlayer.boundingBox.maxX; x +=0.01f){
	    	
			for(double z = mc.thePlayer.boundingBox.minZ; z < mc.thePlayer.boundingBox.maxZ; z +=0.01f){
				Block block = mc.theWorld.getBlockState(new BlockPos(x, mc.thePlayer.posY - profondeur,z)).getBlock();
    			if(!(block instanceof BlockLiquid) && !(block instanceof BlockAir)){
    				return false;
    			}
    		}
		}
	    return true;
	  }

    public static boolean isInLiquid() {
        if (mc.thePlayer.isInWater()) {
            return true;
        }
        boolean inLiquid = false;
        final int y = (int) mc.thePlayer.getEntityBoundingBox().minY;
        for (int x = MathHelper.floor_double(mc.thePlayer.getEntityBoundingBox().minX); x < MathHelper.floor_double(mc.thePlayer.getEntityBoundingBox().maxX) + 1; x++) {
            for (int z = MathHelper.floor_double(mc.thePlayer.getEntityBoundingBox().minZ); z < MathHelper.floor_double(mc.thePlayer.getEntityBoundingBox().maxZ) + 1; z++) {
                final Block block = mc.theWorld.getBlockState(new BlockPos(x, y, z)).getBlock();
                if (block != null && block.getMaterial() != Material.air) {
                    if (!(block instanceof BlockLiquid)) return false;
                    inLiquid = true;
                }
            }
        }
        return inLiquid;
    }

    public static boolean isBlockUnder() {
        if(Minecraft.getMinecraft().thePlayer.posY < 0)
            return false;
        for(int off = 0; off < (int)Minecraft.getMinecraft().thePlayer.posY+2; off += 2){
            AxisAlignedBB bb = Minecraft.getMinecraft().thePlayer.getEntityBoundingBox().offset(0, -off, 0);
            if(!Minecraft.getMinecraft().theWorld.getCollidingBoundingBoxes(Minecraft.getMinecraft().thePlayer, bb).isEmpty()){
                return true;
            }
        }
        return false;
    }
}
