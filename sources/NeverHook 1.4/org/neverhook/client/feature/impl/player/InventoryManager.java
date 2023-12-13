/*     */ package org.neverhook.client.feature.impl.player;
/*     */ 
/*     */ import java.util.Arrays;
/*     */ import java.util.List;
/*     */ import java.util.Objects;
/*     */ import net.minecraft.block.Block;
/*     */ import net.minecraft.enchantment.Enchantment;
/*     */ import net.minecraft.enchantment.EnchantmentHelper;
/*     */ import net.minecraft.entity.player.EntityPlayer;
/*     */ import net.minecraft.init.Blocks;
/*     */ import net.minecraft.inventory.ClickType;
/*     */ import net.minecraft.item.Item;
/*     */ import net.minecraft.item.ItemBlock;
/*     */ import net.minecraft.item.ItemStack;
/*     */ import net.minecraft.item.ItemSword;
/*     */ import net.minecraft.item.ItemTool;
/*     */ import net.minecraft.potion.Potion;
/*     */ import net.minecraft.potion.PotionEffect;
/*     */ import net.minecraft.potion.PotionUtils;
/*     */ import org.neverhook.client.event.EventTarget;
/*     */ import org.neverhook.client.event.events.impl.player.EventPreMotion;
/*     */ import org.neverhook.client.feature.Feature;
/*     */ import org.neverhook.client.feature.impl.Type;
/*     */ import org.neverhook.client.helpers.misc.TimerHelper;
/*     */ import org.neverhook.client.helpers.player.InventoryHelper;
/*     */ import org.neverhook.client.helpers.player.MovementHelper;
/*     */ import org.neverhook.client.settings.Setting;
/*     */ import org.neverhook.client.settings.impl.BooleanSetting;
/*     */ import org.neverhook.client.settings.impl.NumberSetting;
/*     */ 
/*     */ public class InventoryManager
/*     */   extends Feature
/*     */ {
/*     */   public static NumberSetting cap;
/*     */   public static BooleanSetting archer;
/*     */   public static NumberSetting delay1;
/*     */   public static BooleanSetting food;
/*  38 */   public static int weaponSlot = 36; public static BooleanSetting sword; public static BooleanSetting cleaner; public static BooleanSetting openinv; public static BooleanSetting nomoveswap; public static int pickaxeSlot = 37; public static int axeSlot = 38; public static int shovelSlot = 39;
/*  39 */   public static List<Block> invalidBlocks = Arrays.asList(new Block[] { Blocks.ENCHANTING_TABLE, Blocks.FURNACE, Blocks.CARPET, Blocks.CRAFTING_TABLE, Blocks.TRAPPED_CHEST, (Block)Blocks.CHEST, Blocks.DISPENSER, Blocks.AIR, (Block)Blocks.WATER, (Block)Blocks.LAVA, (Block)Blocks.FLOWING_WATER, (Block)Blocks.FLOWING_LAVA, (Block)Blocks.SAND, Blocks.SNOW_LAYER, Blocks.TORCH, Blocks.ANVIL, Blocks.JUKEBOX, Blocks.STONE_BUTTON, Blocks.WOODEN_BUTTON, Blocks.LEVER, Blocks.NOTEBLOCK, Blocks.STONE_PRESSURE_PLATE, Blocks.LIGHT_WEIGHTED_PRESSURE_PLATE, Blocks.WOODEN_PRESSURE_PLATE, Blocks.HEAVY_WEIGHTED_PRESSURE_PLATE, (Block)Blocks.STONE_SLAB, (Block)Blocks.WOODEN_SLAB, (Block)Blocks.STONE_SLAB2, (Block)Blocks.RED_MUSHROOM, (Block)Blocks.BROWN_MUSHROOM, (Block)Blocks.YELLOW_FLOWER, (Block)Blocks.RED_FLOWER, Blocks.ANVIL, Blocks.GLASS_PANE, (Block)Blocks.STAINED_GLASS_PANE, Blocks.IRON_BARS, (Block)Blocks.CACTUS, Blocks.LADDER, Blocks.WEB });
/*  40 */   private final TimerHelper timer = new TimerHelper();
/*     */   
/*     */   public InventoryManager() {
/*  43 */     super("InventoryManager", "Чистит, сортирует инвентарь за вас", Type.Player);
/*  44 */     cap = new NumberSetting("Block Cap", 128.0F, 8.0F, 256.0F, 8.0F, () -> Boolean.valueOf(true));
/*  45 */     delay1 = new NumberSetting("Sort Delay", 1.0F, 0.0F, 10.0F, 0.1F, () -> Boolean.valueOf(true));
/*  46 */     archer = new BooleanSetting("Archer", false, () -> Boolean.valueOf(true));
/*  47 */     food = new BooleanSetting("Food", false, () -> Boolean.valueOf(true));
/*  48 */     sword = new BooleanSetting("Sword", true, () -> Boolean.valueOf(true));
/*  49 */     cleaner = new BooleanSetting("Inv Cleaner", true, () -> Boolean.valueOf(true));
/*  50 */     openinv = new BooleanSetting("Open Inv", true, () -> Boolean.valueOf(true));
/*  51 */     nomoveswap = new BooleanSetting("No Moving Swap", false, () -> Boolean.valueOf(true));
/*  52 */     addSettings(new Setting[] { (Setting)cap, (Setting)delay1, (Setting)archer, (Setting)food, (Setting)sword, (Setting)cleaner, (Setting)openinv, (Setting)nomoveswap });
/*     */   }
/*     */   
/*     */   @EventTarget
/*     */   public void onPreMotion(EventPreMotion eventPre) {
/*  57 */     long delay = (long)delay1.getNumberValue() * 50L;
/*  58 */     if (!(mc.currentScreen instanceof net.minecraft.client.gui.inventory.GuiInventory) && openinv.getBoolValue())
/*     */       return; 
/*  60 */     if (MovementHelper.isMoving() && nomoveswap.getBoolValue()) {
/*     */       return;
/*     */     }
/*  63 */     if (mc.currentScreen == null || mc.currentScreen instanceof net.minecraft.client.gui.inventory.GuiInventory || mc.currentScreen instanceof net.minecraft.client.gui.GuiChat) {
/*  64 */       if (this.timer.hasReached((float)delay) && weaponSlot >= 36) {
/*  65 */         if (!mc.player.inventoryContainer.getSlot(weaponSlot).getHasStack()) {
/*  66 */           getBestWeapon(weaponSlot);
/*     */         }
/*  68 */         else if (!isBestWeapon(mc.player.inventoryContainer.getSlot(weaponSlot).getStack())) {
/*  69 */           getBestWeapon(weaponSlot);
/*     */         } 
/*     */       }
/*     */       
/*  73 */       if (this.timer.hasReached((float)delay) && pickaxeSlot >= 36) {
/*  74 */         getBestPickaxe();
/*     */       }
/*  76 */       if (this.timer.hasReached((float)delay) && shovelSlot >= 36) {
/*  77 */         getBestShovel();
/*     */       }
/*  79 */       if (this.timer.hasReached((float)delay) && axeSlot >= 36) {
/*  80 */         getBestAxe();
/*     */       }
/*  82 */       if (this.timer.hasReached((float)delay) && cleaner.getBoolValue())
/*  83 */         for (int i = 9; i < 45; i++) {
/*  84 */           if (mc.player.inventoryContainer.getSlot(i).getHasStack()) {
/*  85 */             ItemStack is = mc.player.inventoryContainer.getSlot(i).getStack();
/*  86 */             if (shouldDrop(is, i)) {
/*  87 */               drop(i);
/*  88 */               if (delay == 0L) {
/*  89 */                 mc.player.closeScreen();
/*     */               }
/*  91 */               this.timer.reset();
/*  92 */               if (delay > 0L) {
/*     */                 break;
/*     */               }
/*     */             } 
/*     */           } 
/*     */         }  
/*     */     } 
/*     */   }
/*     */   
/*     */   public void swap(int slot, int hotbarSlot) {
/* 102 */     mc.playerController.windowClick(mc.player.inventoryContainer.windowId, slot, hotbarSlot, ClickType.SWAP, (EntityPlayer)mc.player);
/*     */   }
/*     */   
/*     */   public void drop(int slot) {
/* 106 */     mc.playerController.windowClick(mc.player.inventoryContainer.windowId, slot, 1, ClickType.THROW, (EntityPlayer)mc.player);
/*     */   }
/*     */   
/*     */   public boolean isBestWeapon(ItemStack stack) {
/* 110 */     float damage = getDamage(stack);
/* 111 */     for (int i = 9; i < 45; i++) {
/* 112 */       if (mc.player.inventoryContainer.getSlot(i).getHasStack()) {
/* 113 */         ItemStack is = mc.player.inventoryContainer.getSlot(i).getStack();
/* 114 */         if (getDamage(is) > damage && (is.getItem() instanceof ItemSword || !sword.getBoolValue()))
/* 115 */           return false; 
/*     */       } 
/*     */     } 
/* 118 */     return (stack.getItem() instanceof ItemSword || !sword.getBoolValue());
/*     */   }
/*     */   
/*     */   public void getBestWeapon(int slot) {
/* 122 */     for (int i = 9; i < 45; i++) {
/* 123 */       if (mc.player.inventoryContainer.getSlot(i).getHasStack()) {
/* 124 */         ItemStack is = mc.player.inventoryContainer.getSlot(i).getStack();
/* 125 */         if (isBestWeapon(is) && getDamage(is) > 0.0F && (is.getItem() instanceof ItemSword || !sword.getBoolValue())) {
/* 126 */           swap(i, slot - 36);
/* 127 */           this.timer.reset();
/*     */           break;
/*     */         } 
/*     */       } 
/*     */     } 
/*     */   }
/*     */   
/*     */   private float getDamage(ItemStack stack) {
/* 135 */     float damage = 0.0F;
/* 136 */     Item item = stack.getItem();
/* 137 */     if (item instanceof ItemTool) {
/* 138 */       ItemTool tool = (ItemTool)item;
/* 139 */       damage += tool.getDamageVsEntity();
/*     */     } 
/* 141 */     if (item instanceof ItemSword) {
/* 142 */       ItemSword sword = (ItemSword)item;
/* 143 */       damage += sword.getDamageVsEntity();
/*     */     } 
/* 145 */     damage += EnchantmentHelper.getEnchantmentLevel(Objects.<Enchantment>requireNonNull(Enchantment.getEnchantmentByID(16)), stack) * 1.25F + EnchantmentHelper.getEnchantmentLevel(Objects.<Enchantment>requireNonNull(Enchantment.getEnchantmentByID(20)), stack) * 0.01F;
/* 146 */     return damage;
/*     */   }
/*     */   
/*     */   public boolean shouldDrop(ItemStack stack, int slot) {
/* 150 */     if (stack.getDisplayName().toLowerCase().contains("/")) {
/* 151 */       return false;
/*     */     }
/* 153 */     if (stack.getDisplayName().toLowerCase().contains("предметы")) {
/* 154 */       return false;
/*     */     }
/* 156 */     if (stack.getDisplayName().toLowerCase().contains("§k||")) {
/* 157 */       return false;
/*     */     }
/* 159 */     if (stack.getDisplayName().toLowerCase().contains("kit")) {
/* 160 */       return false;
/*     */     }
/* 162 */     if (stack.getDisplayName().toLowerCase().contains("wool")) {
/* 163 */       return false;
/*     */     }
/* 165 */     if (stack.getDisplayName().toLowerCase().contains("лобби")) {
/* 166 */       return false;
/*     */     }
/* 168 */     if ((slot == weaponSlot && isBestWeapon(mc.player.inventoryContainer.getSlot(weaponSlot).getStack())) || (slot == pickaxeSlot && 
/* 169 */       isBestPickaxe(mc.player.inventoryContainer.getSlot(pickaxeSlot).getStack()) && pickaxeSlot >= 0) || (slot == axeSlot && 
/* 170 */       isBestAxe(mc.player.inventoryContainer.getSlot(axeSlot).getStack()) && axeSlot >= 0) || (slot == shovelSlot && 
/* 171 */       isBestShovel(mc.player.inventoryContainer.getSlot(shovelSlot).getStack()) && shovelSlot >= 0)) {
/* 172 */       return false;
/*     */     }
/* 174 */     if (stack.getItem() instanceof net.minecraft.item.ItemArmor)
/* 175 */       for (int type = 1; type < 5; type++) {
/* 176 */         if (mc.player.inventoryContainer.getSlot(4 + type).getHasStack()) {
/* 177 */           ItemStack is = mc.player.inventoryContainer.getSlot(4 + type).getStack();
/* 178 */           if (InventoryHelper.isBestArmor(is, type)) {
/*     */             continue;
/*     */           }
/*     */         } 
/* 182 */         if (InventoryHelper.isBestArmor(stack, type)) {
/* 183 */           return false;
/*     */         }
/*     */         continue;
/*     */       }  
/* 187 */     if (stack.getItem() instanceof ItemBlock && (getBlockCount() > cap.getNumberValue() || invalidBlocks.contains(((ItemBlock)stack.getItem()).getBlock()))) {
/* 188 */       return true;
/*     */     }
/* 190 */     if (stack.getItem() instanceof net.minecraft.item.ItemPotion && 
/* 191 */       isBadPotion(stack)) {
/* 192 */       return true;
/*     */     }
/*     */ 
/*     */     
/* 196 */     if (stack.getItem() instanceof net.minecraft.item.ItemFood && food.getBoolValue() && !(stack.getItem() instanceof net.minecraft.item.ItemAppleGold)) {
/* 197 */       return true;
/*     */     }
/* 199 */     if (stack.getItem() instanceof net.minecraft.item.ItemHoe || stack.getItem() instanceof ItemTool || stack.getItem() instanceof ItemSword || stack.getItem() instanceof net.minecraft.item.ItemArmor) {
/* 200 */       return true;
/*     */     }
/* 202 */     if ((stack.getItem() instanceof net.minecraft.item.ItemBow || stack.getItem().getUnlocalizedName().contains("arrow")) && archer.getBoolValue()) {
/* 203 */       return true;
/*     */     }
/*     */     
/* 206 */     return (stack.getItem().getUnlocalizedName().contains("tnt") || stack
/* 207 */       .getItem().getUnlocalizedName().contains("stick") || stack
/* 208 */       .getItem().getUnlocalizedName().contains("egg") || stack
/* 209 */       .getItem().getUnlocalizedName().contains("string") || stack
/* 210 */       .getItem().getUnlocalizedName().contains("cake") || stack
/* 211 */       .getItem().getUnlocalizedName().contains("mushroom") || stack
/* 212 */       .getItem().getUnlocalizedName().contains("flint") || stack
/* 213 */       .getItem().getUnlocalizedName().contains("dyePowder") || stack
/* 214 */       .getItem().getUnlocalizedName().contains("feather") || stack
/* 215 */       .getItem().getUnlocalizedName().contains("bucket") || (stack
/* 216 */       .getItem().getUnlocalizedName().contains("chest") && !stack.getDisplayName().toLowerCase().contains("collect")) || stack
/* 217 */       .getItem().getUnlocalizedName().contains("snow") || stack
/* 218 */       .getItem().getUnlocalizedName().contains("fish") || stack
/* 219 */       .getItem().getUnlocalizedName().contains("enchant") || stack
/* 220 */       .getItem().getUnlocalizedName().contains("exp") || stack
/* 221 */       .getItem().getUnlocalizedName().contains("shears") || stack
/* 222 */       .getItem().getUnlocalizedName().contains("anvil") || stack
/* 223 */       .getItem().getUnlocalizedName().contains("torch") || stack
/* 224 */       .getItem().getUnlocalizedName().contains("seeds") || stack
/* 225 */       .getItem().getUnlocalizedName().contains("leather") || stack
/* 226 */       .getItem().getUnlocalizedName().contains("reeds") || stack
/* 227 */       .getItem().getUnlocalizedName().contains("skull") || stack
/* 228 */       .getItem().getUnlocalizedName().contains("wool") || stack
/* 229 */       .getItem().getUnlocalizedName().contains("record") || stack
/* 230 */       .getItem().getUnlocalizedName().contains("snowball") || stack
/* 231 */       .getItem() instanceof net.minecraft.item.ItemGlassBottle || stack
/* 232 */       .getItem().getUnlocalizedName().contains("piston"));
/*     */   }
/*     */   
/*     */   private int getBlockCount() {
/* 236 */     int blockCount = 0;
/* 237 */     for (int i = 0; i < 45; i++) {
/* 238 */       if (mc.player.inventoryContainer.getSlot(i).getHasStack()) {
/* 239 */         ItemStack is = mc.player.inventoryContainer.getSlot(i).getStack();
/* 240 */         Item item = is.getItem();
/* 241 */         if (is.getItem() instanceof ItemBlock && !invalidBlocks.contains(((ItemBlock)item).getBlock())) {
/* 242 */           blockCount += is.stackSize;
/*     */         }
/*     */       } 
/*     */     } 
/* 246 */     return blockCount;
/*     */   }
/*     */   
/*     */   private void getBestPickaxe() {
/* 250 */     for (int i = 9; i < 45; i++) {
/* 251 */       if (mc.player.inventoryContainer.getSlot(i).getHasStack()) {
/* 252 */         ItemStack is = mc.player.inventoryContainer.getSlot(i).getStack();
/*     */         
/* 254 */         if (isBestPickaxe(is) && pickaxeSlot != i && 
/* 255 */           !isBestWeapon(is))
/* 256 */           if (!mc.player.inventoryContainer.getSlot(pickaxeSlot).getHasStack()) {
/* 257 */             swap(i, pickaxeSlot - 36);
/* 258 */             this.timer.reset();
/* 259 */             if (delay1.getNumberValue() > 0.0F)
/*     */               return; 
/* 261 */           } else if (!isBestPickaxe(mc.player.inventoryContainer.getSlot(pickaxeSlot).getStack())) {
/* 262 */             swap(i, pickaxeSlot - 36);
/* 263 */             this.timer.reset();
/* 264 */             if (delay1.getNumberValue() > 0.0F) {
/*     */               return;
/*     */             }
/*     */           }  
/*     */       } 
/*     */     } 
/*     */   }
/*     */   
/*     */   private void getBestShovel() {
/* 273 */     for (int i = 9; i < 45; i++) {
/* 274 */       if (mc.player.inventoryContainer.getSlot(i).getHasStack()) {
/* 275 */         ItemStack is = mc.player.inventoryContainer.getSlot(i).getStack();
/*     */         
/* 277 */         if (isBestShovel(is) && shovelSlot != i && 
/* 278 */           !isBestWeapon(is))
/* 279 */           if (!mc.player.inventoryContainer.getSlot(shovelSlot).getHasStack()) {
/* 280 */             swap(i, shovelSlot - 36);
/* 281 */             this.timer.reset();
/* 282 */             if (delay1.getNumberValue() > 0.0F)
/*     */               return; 
/* 284 */           } else if (!isBestShovel(mc.player.inventoryContainer.getSlot(shovelSlot).getStack())) {
/* 285 */             swap(i, shovelSlot - 36);
/* 286 */             this.timer.reset();
/* 287 */             if (delay1.getNumberValue() > 0.0F) {
/*     */               return;
/*     */             }
/*     */           }  
/*     */       } 
/*     */     } 
/*     */   }
/*     */   
/*     */   private void getBestAxe() {
/* 296 */     for (int i = 9; i < 45; i++) {
/* 297 */       if (mc.player.inventoryContainer.getSlot(i).getHasStack()) {
/* 298 */         ItemStack is = mc.player.inventoryContainer.getSlot(i).getStack();
/*     */         
/* 300 */         if (isBestAxe(is) && axeSlot != i && 
/* 301 */           !isBestWeapon(is))
/* 302 */           if (!mc.player.inventoryContainer.getSlot(axeSlot).getHasStack()) {
/* 303 */             swap(i, axeSlot - 36);
/* 304 */             this.timer.reset();
/* 305 */             if (delay1.getNumberValue() > 0.0F)
/*     */               return; 
/* 307 */           } else if (!isBestAxe(mc.player.inventoryContainer.getSlot(axeSlot).getStack())) {
/* 308 */             swap(i, axeSlot - 36);
/* 309 */             this.timer.reset();
/* 310 */             if (delay1.getNumberValue() > 0.0F) {
/*     */               return;
/*     */             }
/*     */           }  
/*     */       } 
/*     */     } 
/*     */   }
/*     */   
/*     */   private boolean isBestPickaxe(ItemStack stack) {
/* 319 */     Item item = stack.getItem();
/* 320 */     if (!(item instanceof net.minecraft.item.ItemPickaxe))
/* 321 */       return false; 
/* 322 */     float value = getToolEffect(stack);
/* 323 */     for (int i = 9; i < 45; i++) {
/* 324 */       if (mc.player.inventoryContainer.getSlot(i).getHasStack()) {
/* 325 */         ItemStack is = mc.player.inventoryContainer.getSlot(i).getStack();
/* 326 */         if (getToolEffect(is) > value && is.getItem() instanceof net.minecraft.item.ItemPickaxe) {
/* 327 */           return false;
/*     */         }
/*     */       } 
/*     */     } 
/* 331 */     return true;
/*     */   }
/*     */   
/*     */   private boolean isBestShovel(ItemStack stack) {
/* 335 */     Item item = stack.getItem();
/* 336 */     if (!(item instanceof net.minecraft.item.ItemSpade))
/* 337 */       return false; 
/* 338 */     float value = getToolEffect(stack);
/* 339 */     for (int i = 9; i < 45; i++) {
/* 340 */       if (mc.player.inventoryContainer.getSlot(i).getHasStack()) {
/* 341 */         ItemStack is = mc.player.inventoryContainer.getSlot(i).getStack();
/* 342 */         if (getToolEffect(is) > value && is.getItem() instanceof net.minecraft.item.ItemSpade) {
/* 343 */           return false;
/*     */         }
/*     */       } 
/*     */     } 
/* 347 */     return true;
/*     */   }
/*     */   
/*     */   private boolean isBestAxe(ItemStack stack) {
/* 351 */     Item item = stack.getItem();
/* 352 */     if (!(item instanceof net.minecraft.item.ItemAxe))
/* 353 */       return false; 
/* 354 */     float value = getToolEffect(stack);
/* 355 */     for (int i = 9; i < 45; i++) {
/* 356 */       if (mc.player.inventoryContainer.getSlot(i).getHasStack()) {
/* 357 */         ItemStack is = mc.player.inventoryContainer.getSlot(i).getStack();
/* 358 */         if (getToolEffect(is) > value && is.getItem() instanceof net.minecraft.item.ItemAxe && !isBestWeapon(stack)) {
/* 359 */           return false;
/*     */         }
/*     */       } 
/*     */     } 
/* 363 */     return true;
/*     */   }
/*     */   
/*     */   private float getToolEffect(ItemStack stack) {
/* 367 */     Item item = stack.getItem();
/* 368 */     if (!(item instanceof ItemTool))
/* 369 */       return 0.0F; 
/* 370 */     String name = item.getUnlocalizedName();
/* 371 */     ItemTool tool = (ItemTool)item;
/*     */     
/* 373 */     if (item instanceof net.minecraft.item.ItemPickaxe) {
/* 374 */       value = tool.getStrVsBlock(stack, Blocks.STONE.getDefaultState());
/* 375 */       if (name.toLowerCase().contains("gold")) {
/* 376 */         value -= 5.0F;
/*     */       }
/* 378 */     } else if (item instanceof net.minecraft.item.ItemSpade) {
/* 379 */       value = tool.getStrVsBlock(stack, Blocks.DIRT.getDefaultState());
/* 380 */       if (name.toLowerCase().contains("gold")) {
/* 381 */         value -= 5.0F;
/*     */       }
/* 383 */     } else if (item instanceof net.minecraft.item.ItemAxe) {
/* 384 */       value = tool.getStrVsBlock(stack, Blocks.LOG.getDefaultState());
/* 385 */       if (name.toLowerCase().contains("gold")) {
/* 386 */         value -= 5.0F;
/*     */       }
/*     */     } else {
/* 389 */       return 1.0F;
/* 390 */     }  float value = (float)(value + EnchantmentHelper.getEnchantmentLevel(Objects.<Enchantment>requireNonNull(Enchantment.getEnchantmentByID(32)), stack) * 0.0075D);
/* 391 */     value = (float)(value + EnchantmentHelper.getEnchantmentLevel(Objects.<Enchantment>requireNonNull(Enchantment.getEnchantmentByID(34)), stack) / 100.0D);
/* 392 */     return value;
/*     */   }
/*     */   
/*     */   private boolean isBadPotion(ItemStack stack) {
/* 396 */     if (stack != null && stack.getItem() instanceof net.minecraft.item.ItemPotion) {
/* 397 */       for (PotionEffect o : PotionUtils.getEffectsFromStack(stack)) {
/* 398 */         if (o.getPotion() == Potion.getPotionById(19) || o.getPotion() == Potion.getPotionById(7) || o.getPotion() == Potion.getPotionById(2) || o.getPotion() == Potion.getPotionById(18)) {
/* 399 */           return true;
/*     */         }
/*     */       } 
/*     */     }
/* 403 */     return false;
/*     */   }
/*     */ }


/* Location:              C:\Users\Admin\OneDrive\Рабочий стол\NeverHook Crack.jar!\org\neverhook\client\feature\impl\player\InventoryManager.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */