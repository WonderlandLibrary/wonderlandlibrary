/*     */ package org.neverhook.client.feature.impl.movement;
/*     */ 
/*     */ import java.awt.Color;
/*     */ import java.util.Arrays;
/*     */ import java.util.List;
/*     */ import net.minecraft.block.Block;
/*     */ import net.minecraft.client.renderer.GlStateManager;
/*     */ import net.minecraft.entity.Entity;
/*     */ import net.minecraft.entity.player.EntityPlayer;
/*     */ import net.minecraft.init.Blocks;
/*     */ import net.minecraft.inventory.ClickType;
/*     */ import net.minecraft.item.Item;
/*     */ import net.minecraft.item.ItemBlock;
/*     */ import net.minecraft.item.ItemStack;
/*     */ import net.minecraft.network.Packet;
/*     */ import net.minecraft.network.play.client.CPacketAnimation;
/*     */ import net.minecraft.network.play.client.CPacketEntityAction;
/*     */ import net.minecraft.util.EnumFacing;
/*     */ import net.minecraft.util.EnumHand;
/*     */ import net.minecraft.util.math.BlockPos;
/*     */ import net.minecraft.util.math.Vec3d;
/*     */ import org.neverhook.client.event.EventTarget;
/*     */ import org.neverhook.client.event.events.impl.motion.EventSafeWalk;
/*     */ import org.neverhook.client.event.events.impl.motion.EventStrafe;
/*     */ import org.neverhook.client.event.events.impl.player.EventPreMotion;
/*     */ import org.neverhook.client.event.events.impl.player.EventUpdate;
/*     */ import org.neverhook.client.event.events.impl.render.EventRender2D;
/*     */ import org.neverhook.client.event.events.impl.render.EventRender3D;
/*     */ import org.neverhook.client.feature.Feature;
/*     */ import org.neverhook.client.feature.impl.Type;
/*     */ import org.neverhook.client.helpers.math.MathematicHelper;
/*     */ import org.neverhook.client.helpers.math.RotationHelper;
/*     */ import org.neverhook.client.helpers.misc.TimerHelper;
/*     */ import org.neverhook.client.helpers.player.InventoryHelper;
/*     */ import org.neverhook.client.helpers.player.MovementHelper;
/*     */ import org.neverhook.client.helpers.render.RenderHelper;
/*     */ import org.neverhook.client.helpers.render.rect.RectHelper;
/*     */ import org.neverhook.client.settings.Setting;
/*     */ import org.neverhook.client.settings.impl.BooleanSetting;
/*     */ import org.neverhook.client.settings.impl.ListSetting;
/*     */ import org.neverhook.client.settings.impl.NumberSetting;
/*     */ 
/*     */ public class Scaffold extends Feature {
/*  44 */   public static List<Block> invalidBlocks = Arrays.asList(new Block[] { Blocks.ENCHANTING_TABLE, Blocks.FURNACE, Blocks.CARPET, Blocks.CRAFTING_TABLE, Blocks.TRAPPED_CHEST, (Block)Blocks.CHEST, Blocks.DISPENSER, Blocks.AIR, (Block)Blocks.WATER, (Block)Blocks.LAVA, (Block)Blocks.FLOWING_WATER, (Block)Blocks.FLOWING_LAVA, (Block)Blocks.SAND, Blocks.SNOW_LAYER, Blocks.TORCH, Blocks.ANVIL, Blocks.JUKEBOX, Blocks.STONE_BUTTON, Blocks.WOODEN_BUTTON, Blocks.LEVER, Blocks.NOTEBLOCK, Blocks.STONE_PRESSURE_PLATE, Blocks.LIGHT_WEIGHTED_PRESSURE_PLATE, Blocks.WOODEN_PRESSURE_PLATE, Blocks.HEAVY_WEIGHTED_PRESSURE_PLATE, (Block)Blocks.STONE_SLAB, (Block)Blocks.WOODEN_SLAB, (Block)Blocks.STONE_SLAB2, (Block)Blocks.RED_MUSHROOM, (Block)Blocks.BROWN_MUSHROOM, (Block)Blocks.YELLOW_FLOWER, (Block)Blocks.RED_FLOWER, Blocks.ANVIL, Blocks.GLASS_PANE, (Block)Blocks.STAINED_GLASS_PANE, Blocks.IRON_BARS, (Block)Blocks.CACTUS, Blocks.LADDER, Blocks.WEB, Blocks.PUMPKIN });
/*     */   public static BlockData data;
/*     */   public static boolean isSneaking;
/*     */   public static BooleanSetting down;
/*     */   public static BooleanSetting sprintoff;
/*  49 */   public static BooleanSetting rotationRandom = new BooleanSetting("Rotation Random", true, () -> Boolean.valueOf(true));
/*  50 */   public static NumberSetting rotationSpeed = new NumberSetting("Rotation Speed", 360.0F, 1.0F, 360.0F, 1.0F, () -> Boolean.valueOf(true));
/*  51 */   private final TimerHelper time = new TimerHelper();
/*     */   private final BooleanSetting jump;
/*     */   private final BooleanSetting swing;
/*     */   private final NumberSetting delay;
/*     */   private final NumberSetting delayRandom;
/*     */   private final NumberSetting chance;
/*     */   private final NumberSetting speed;
/*     */   private final BooleanSetting rotStrafe;
/*     */   private final BooleanSetting safewalk;
/*     */   private final ListSetting blockRotation;
/*     */   private final ListSetting towerMode;
/*  62 */   public NumberSetting rotPitchRandom = new NumberSetting("Rotation Pitch Random", 2.0F, 0.0F, 8.0F, 0.01F, () -> Boolean.valueOf(rotationRandom.getBoolValue()));
/*  63 */   public NumberSetting rotYawRandom = new NumberSetting("Rotation Yaw Random", 2.0F, 0.0F, 8.0F, 0.01F, () -> Boolean.valueOf(rotationRandom.getBoolValue()));
/*  64 */   public BooleanSetting airCheck = new BooleanSetting("Check Air", true, () -> Boolean.valueOf(true));
/*  65 */   public BooleanSetting sneak = new BooleanSetting("Sneak", true, () -> Boolean.valueOf(true));
/*  66 */   public NumberSetting sneakChance = new NumberSetting("Sneak Chance", 100.0F, 0.0F, 100.0F, 1.0F, () -> Boolean.valueOf(this.sneak.getBoolValue()));
/*  67 */   public NumberSetting sneakSpeed = new NumberSetting("Sneak Speed", 0.05F, 0.01F, 1.0F, 0.01F, () -> Boolean.valueOf(this.sneak.getBoolValue()));
/*  68 */   public ListSetting sneakMode = new ListSetting("Sneak Mode", "Packet", () -> Boolean.valueOf(this.sneak.getBoolValue()), new String[] { "Packet", "Client" });
/*  69 */   public NumberSetting rotationOffset = new NumberSetting("Rotation Offset", 0.25F, 0.0F, 1.0F, 0.01F, () -> Boolean.valueOf(true));
/*  70 */   public NumberSetting placeOffset = new NumberSetting("Place Offset", 0.2F, 0.01F, 0.3F, 0.01F, () -> Boolean.valueOf(true));
/*     */   private int slot;
/*     */   
/*     */   public Scaffold() {
/*  74 */     super("Scaffold", "Автоматически ставит под вас блоки", Type.Movement);
/*     */     
/*  76 */     this.blockRotation = new ListSetting("BlockRotation Mode", "Matrix", () -> Boolean.valueOf(true), new String[] { "Matrix", "None" });
/*     */     
/*  78 */     this.towerMode = new ListSetting("Tower Mode", "Matrix", () -> Boolean.valueOf(true), new String[] { "Matrix", "NCP", "Default" });
/*     */     
/*  80 */     this.chance = new NumberSetting("Chance", 100.0F, 0.0F, 100.0F, 1.0F, () -> Boolean.valueOf(true));
/*  81 */     this.delay = new NumberSetting("Min Delay", 0.0F, 0.0F, 300.0F, 1.0F, () -> Boolean.valueOf(true));
/*  82 */     this.delayRandom = new NumberSetting("Random Delay", 0.0F, 0.0F, 1000.0F, 1.0F, () -> Boolean.valueOf(true));
/*  83 */     this.speed = new NumberSetting("Speed", 0.6F, 0.05F, 1.2F, 0.01F, () -> Boolean.valueOf(true));
/*  84 */     sprintoff = new BooleanSetting("Stop Sprinting", true, () -> Boolean.valueOf(true));
/*  85 */     this.safewalk = new BooleanSetting("SafeWalk", true, () -> Boolean.valueOf(true));
/*  86 */     this.jump = new BooleanSetting("Jump", false, () -> Boolean.valueOf(true));
/*  87 */     down = new BooleanSetting("DownWard", false, () -> Boolean.valueOf(true));
/*  88 */     this.swing = new BooleanSetting("SwingHand", false, () -> Boolean.valueOf(true));
/*  89 */     this.rotStrafe = new BooleanSetting("Rotation Strafe", false, () -> Boolean.valueOf(true));
/*  90 */     addSettings(new Setting[] { (Setting)this.blockRotation, (Setting)this.towerMode, (Setting)this.chance, (Setting)this.delay, (Setting)this.delayRandom, (Setting)this.rotationOffset, (Setting)this.placeOffset, (Setting)rotationSpeed, (Setting)rotationRandom, (Setting)this.rotYawRandom, (Setting)this.rotPitchRandom, (Setting)this.speed, (Setting)this.sneak, (Setting)this.sneakMode, (Setting)this.sneakChance, (Setting)this.sneakSpeed, (Setting)sprintoff, (Setting)this.airCheck, (Setting)this.safewalk, (Setting)this.jump, (Setting)down, (Setting)this.swing, (Setting)this.rotStrafe });
/*     */   }
/*     */   
/*     */   public static int searchBlock() {
/*  94 */     for (int i = 0; i < 45; i++) {
/*  95 */       ItemStack itemStack = mc.player.inventoryContainer.getSlot(i).getStack();
/*  96 */       if (itemStack.getItem() instanceof ItemBlock) {
/*  97 */         return i;
/*     */       }
/*     */     } 
/* 100 */     return -1;
/*     */   }
/*     */   
/*     */   private boolean canPlace() {
/* 104 */     BlockPos bp1 = new BlockPos(mc.player.posX - this.placeOffset.getNumberValue(), mc.player.posY - this.placeOffset.getNumberValue(), mc.player.posZ - this.placeOffset.getNumberValue());
/* 105 */     BlockPos bp2 = new BlockPos(mc.player.posX - this.placeOffset.getNumberValue(), mc.player.posY - this.placeOffset.getNumberValue(), mc.player.posZ + this.placeOffset.getNumberValue());
/* 106 */     BlockPos bp3 = new BlockPos(mc.player.posX + this.placeOffset.getNumberValue(), mc.player.posY - this.placeOffset.getNumberValue(), mc.player.posZ + this.placeOffset.getNumberValue());
/* 107 */     BlockPos bp4 = new BlockPos(mc.player.posX + this.placeOffset.getNumberValue(), mc.player.posY - this.placeOffset.getNumberValue(), mc.player.posZ - this.placeOffset.getNumberValue());
/* 108 */     return (mc.player.world.getBlockState(bp1).getBlock() == Blocks.AIR && mc.player.world.getBlockState(bp2).getBlock() == Blocks.AIR && mc.player.world.getBlockState(bp3).getBlock() == Blocks.AIR && mc.player.world.getBlockState(bp4).getBlock() == Blocks.AIR);
/*     */   }
/*     */   
/*     */   private boolean canSneak() {
/* 112 */     BlockPos bp1 = new BlockPos(mc.player.posX - this.sneakSpeed.getNumberValue(), mc.player.posY - this.sneakSpeed.getNumberValue(), mc.player.posZ - this.sneakSpeed.getNumberValue());
/* 113 */     BlockPos bp2 = new BlockPos(mc.player.posX - this.sneakSpeed.getNumberValue(), mc.player.posY - this.sneakSpeed.getNumberValue(), mc.player.posZ + this.sneakSpeed.getNumberValue());
/* 114 */     BlockPos bp3 = new BlockPos(mc.player.posX + this.sneakSpeed.getNumberValue(), mc.player.posY - this.sneakSpeed.getNumberValue(), mc.player.posZ + this.sneakSpeed.getNumberValue());
/* 115 */     BlockPos bp4 = new BlockPos(mc.player.posX + this.sneakSpeed.getNumberValue(), mc.player.posY - this.sneakSpeed.getNumberValue(), mc.player.posZ - this.sneakSpeed.getNumberValue());
/* 116 */     return (mc.player.world.getBlockState(bp1).getBlock() == Blocks.AIR && mc.player.world.getBlockState(bp2).getBlock() == Blocks.AIR && mc.player.world.getBlockState(bp3).getBlock() == Blocks.AIR && mc.player.world.getBlockState(bp4).getBlock() == Blocks.AIR);
/*     */   }
/*     */ 
/*     */   
/*     */   public void onEnable() {
/* 121 */     this.slot = mc.player.inventory.currentItem;
/* 122 */     data = null;
/* 123 */     super.onEnable();
/*     */   }
/*     */ 
/*     */   
/*     */   public void onDisable() {
/* 128 */     mc.player.inventory.currentItem = this.slot;
/* 129 */     mc.timer.timerSpeed = 1.0F;
/* 130 */     mc.player.connection.sendPacket((Packet)new CPacketEntityAction((Entity)mc.player, CPacketEntityAction.Action.STOP_SNEAKING));
/* 131 */     mc.gameSettings.keyBindSneak.pressed = false;
/* 132 */     super.onDisable();
/*     */   }
/*     */   
/*     */   @EventTarget
/*     */   public void onStrafeMotion(EventStrafe eventStrafe) {
/* 137 */     if (this.rotStrafe.getBoolValue()) {
/* 138 */       eventStrafe.setCancelled(true);
/* 139 */       MovementHelper.calculateSilentMove(eventStrafe, RotationHelper.Rotation.packetYaw);
/*     */     } 
/*     */   }
/*     */   
/*     */   @EventTarget
/*     */   public void onSafe(EventSafeWalk eventSafeWalk) {
/* 145 */     if (this.safewalk.getBoolValue() && !isSneaking) {
/* 146 */       eventSafeWalk.setCancelled(mc.player.onGround);
/*     */     }
/*     */   }
/*     */   
/*     */   @EventTarget
/*     */   public void onPreMotion(EventPreMotion eventUpdate) {
/* 152 */     String tower = this.towerMode.getCurrentMode();
/* 153 */     setSuffix(this.blockRotation.getCurrentMode());
/* 154 */     if (tower.equalsIgnoreCase("Matrix")) {
/* 155 */       if (!MovementHelper.isMoving()) {
/* 156 */         if (mc.player.onGround && mc.gameSettings.keyBindJump.isKeyDown()) {
/* 157 */           mc.player.jump();
/*     */         }
/* 159 */         if (mc.player.motionY > 0.0D && !mc.player.onGround) {
/* 160 */           mc.player.motionY -= 0.00994D;
/*     */         } else {
/* 162 */           mc.player.motionY -= 0.00995D;
/*     */         } 
/*     */       } 
/* 165 */     } else if (tower.equalsIgnoreCase("NCP") && 
/* 166 */       !MovementHelper.isMoving()) {
/* 167 */       if (mc.player.onGround && mc.gameSettings.keyBindJump.isKeyDown()) {
/* 168 */         mc.player.jump();
/*     */       }
/* 170 */       float pos = -2.0F;
/* 171 */       if (mc.player.motionY < 0.1D && !(mc.world.getBlockState((new BlockPos(mc.player.posX, mc.player.posY, mc.player.posZ)).add(0.0D, pos, 0.0D)).getBlock() instanceof net.minecraft.block.BlockAir)) {
/* 172 */         mc.player.motionY -= 190.0D;
/*     */       }
/*     */     } 
/*     */ 
/*     */     
/* 177 */     if (mc.gameSettings.keyBindSneak.pressed && down.getBoolValue()) {
/* 178 */       mc.gameSettings.keyBindSneak.pressed = false;
/* 179 */       isSneaking = true;
/*     */     } else {
/* 181 */       isSneaking = false;
/*     */     } 
/*     */     
/* 184 */     mc.player.motionX *= this.speed.getNumberValue();
/* 185 */     mc.player.motionZ *= this.speed.getNumberValue();
/*     */     
/* 187 */     if (!InventoryHelper.doesHotbarHaveBlock() && 
/* 188 */       !(mc.player.getHeldItemOffhand().getItem() instanceof ItemBlock) && searchBlock() != -1) {
/* 189 */       mc.playerController.windowClick(0, searchBlock(), 1, ClickType.QUICK_MOVE, (EntityPlayer)mc.player);
/*     */     }
/*     */ 
/*     */     
/* 193 */     BlockPos blockPos = isSneaking ? (new BlockPos((Entity)mc.player)).add(1, -1, 0).down() : (new BlockPos((Entity)mc.player)).add(0, -1, 0); double posY;
/* 194 */     for (posY = mc.player.posY - 1.0D; posY > 0.0D; posY--) {
/* 195 */       BlockData newData = getBlockData(blockPos);
/* 196 */       if (newData != null) {
/* 197 */         double yDif = mc.player.posY - posY;
/* 198 */         if (yDif <= 7.0D) {
/* 199 */           data = newData;
/*     */         }
/*     */       } 
/*     */     } 
/* 203 */     if (sprintoff.getBoolValue()) {
/* 204 */       mc.player.setSprinting(false);
/*     */     }
/* 206 */     if (data != null && this.slot != -1 && !mc.player.isInLiquid()) {
/* 207 */       Vec3d hitVec = getVectorToRotate(data);
/* 208 */       if (this.blockRotation.getOptions().equalsIgnoreCase("Matrix")) {
/* 209 */         float[] rots = RotationHelper.getRotationVector(hitVec, rotationRandom.getBoolValue(), this.rotYawRandom.getNumberValue(), this.rotPitchRandom.getNumberValue(), rotationSpeed.getNumberValue());
/* 210 */         eventUpdate.setYaw(rots[0]);
/* 211 */         eventUpdate.setPitch(rots[1]);
/*     */         
/* 213 */         if (mc.world.getBlockState(blockPos).getBlock() == Blocks.AIR && !this.airCheck.getBoolValue()) {
/* 214 */           mc.player.renderYawOffset = rots[0];
/* 215 */           mc.player.rotationYawHead = rots[0];
/* 216 */           mc.player.rotationPitchHead = rots[1];
/*     */         } else {
/* 218 */           mc.player.renderYawOffset = rots[0];
/* 219 */           mc.player.rotationYawHead = rots[0];
/* 220 */           mc.player.rotationPitchHead = rots[1];
/*     */         } 
/*     */       } 
/*     */     } 
/*     */   }
/*     */   
/*     */   @EventTarget
/*     */   public void onUpdate(EventUpdate event) {
/* 228 */     if (InventoryHelper.doesHotbarHaveBlock() && 
/* 229 */       data != null) {
/* 230 */       int slot = -1;
/* 231 */       int lastItem = mc.player.inventory.currentItem;
/* 232 */       BlockPos pos = data.pos;
/* 233 */       Vec3d hitVec = getVectorToPlace(data);
/* 234 */       for (int i = 0; i < 9; i++) {
/* 235 */         ItemStack itemStack = mc.player.inventory.getStackInSlot(i);
/* 236 */         if (isValidItem(itemStack.getItem())) {
/* 237 */           slot = i;
/*     */         }
/*     */       } 
/* 240 */       if (slot != -1) {
/* 241 */         if (this.jump.getBoolValue() && 
/* 242 */           !mc.gameSettings.keyBindJump.isKeyDown() && 
/* 243 */           mc.player.onGround) {
/* 244 */           mc.player.jump();
/*     */         }
/*     */ 
/*     */         
/* 248 */         if (!this.jump.getBoolValue() && InventoryHelper.doesHotbarHaveBlock() && MovementHelper.isMoving() && !mc.gameSettings.keyBindJump.isKeyDown() && this.sneak.getBoolValue() && MathematicHelper.randomizeFloat(0.0F, 100.0F) <= this.sneakChance.getNumberValue() && 
/* 249 */           InventoryHelper.doesHotbarHaveBlock()) {
/* 250 */           if (canSneak()) {
/* 251 */             if (this.sneakMode.currentMode.equals("Packet")) {
/* 252 */               mc.player.connection.sendPacket((Packet)new CPacketEntityAction((Entity)mc.player, CPacketEntityAction.Action.START_RIDING_JUMP));
/* 253 */               mc.player.connection.sendPacket((Packet)new CPacketEntityAction((Entity)mc.player, CPacketEntityAction.Action.START_SNEAKING));
/* 254 */             } else if (this.sneakMode.currentMode.equals("Client")) {
/* 255 */               mc.gameSettings.keyBindSneak.pressed = true;
/*     */             }
/*     */           
/* 258 */           } else if (this.sneakMode.currentMode.equals("Packet")) {
/* 259 */             mc.player.connection.sendPacket((Packet)new CPacketEntityAction((Entity)mc.player, CPacketEntityAction.Action.STOP_SNEAKING));
/* 260 */           } else if (this.sneakMode.currentMode.equals("Client")) {
/* 261 */             mc.gameSettings.keyBindSneak.pressed = false;
/*     */           } 
/*     */         }
/*     */ 
/*     */         
/* 266 */         if (this.time.hasReached(this.delay.getNumberValue() + MathematicHelper.randomizeFloat(0.0F, this.delayRandom.getNumberValue())) && 
/* 267 */           canPlace()) {
/* 268 */           if (MathematicHelper.randomizeFloat(0.0F, 100.0F) <= this.chance.getNumberValue()) {
/* 269 */             mc.player.inventory.currentItem = slot;
/*     */           }
/* 271 */           mc.playerController.processRightClickBlock(mc.player, mc.world, pos, data.face, hitVec, EnumHand.MAIN_HAND);
/* 272 */           if (this.swing.getBoolValue()) {
/* 273 */             mc.player.swingArm(EnumHand.MAIN_HAND);
/*     */           } else {
/* 275 */             mc.player.connection.sendPacket((Packet)new CPacketAnimation(EnumHand.MAIN_HAND));
/*     */           } 
/* 277 */           mc.player.inventory.currentItem = lastItem;
/* 278 */           this.time.reset();
/*     */         } 
/*     */       } 
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   @EventTarget
/*     */   public void onRender(EventRender3D event) {
/* 288 */     if (data != null && this.slot != -1) {
/* 289 */       double x = mc.player.posX;
/* 290 */       double y = mc.player.posY;
/* 291 */       double z = mc.player.posZ;
/* 292 */       double yaw = mc.player.rotationYaw * 0.017453292D;
/* 293 */       BlockPos below = new BlockPos(x - Math.sin(yaw), y - 1.0D, z + Math.cos(yaw));
/* 294 */       RenderHelper.blockEsp(below, Color.WHITE, true);
/*     */     } 
/*     */   }
/*     */   
/*     */   @EventTarget
/*     */   public void onRender2D(EventRender2D render) {
/* 300 */     float width = render.getResolution().getScaledWidth();
/* 301 */     float height = render.getResolution().getScaledHeight();
/* 302 */     String blockString = getBlockCount() + " Blocks";
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 327 */     GlStateManager.pushMatrix();
/* 328 */     GlStateManager.translate(23.0F, 15.0F, 0.0F);
/* 329 */     RectHelper.drawSkeetRectWithoutBorder(width / 2.0F + 88.0F - mc.circleregular.getStringWidth(blockString), height / 2.0F - mc.circleregular.getStringHeight(blockString) + 59.0F, width / 2.0F + mc.circleregular.getStringHeight(blockString) + 2.0F, height / 2.0F - mc.circleregular.getStringHeight(blockString) / 2.0F - 55.0F);
/* 330 */     mc.circleregular.drawStringWithOutline(blockString, (width / 2.0F + 50.0F - mc.circleregular.getStringWidth(blockString)), (height / 2.0F - 9.0F), -1);
/* 331 */     GlStateManager.popMatrix();
/*     */   }
/*     */   
/*     */   private int getBlockCount() {
/* 335 */     int blockCount = 0;
/*     */     
/* 337 */     for (int i = 0; i < 45; i++) {
/* 338 */       if (mc.player.inventoryContainer.getSlot(i).getHasStack()) {
/*     */ 
/*     */ 
/*     */         
/* 342 */         ItemStack is = mc.player.inventoryContainer.getSlot(i).getStack();
/* 343 */         Item item = is.getItem();
/*     */         
/* 345 */         if (isValidItem(item))
/*     */         {
/*     */           
/* 348 */           blockCount += is.stackSize; } 
/*     */       } 
/* 350 */     }  return blockCount;
/*     */   }
/*     */   
/*     */   private boolean isValidItem(Item item) {
/* 354 */     if (item instanceof ItemBlock) {
/* 355 */       ItemBlock iBlock = (ItemBlock)item;
/* 356 */       Block block = iBlock.getBlock();
/* 357 */       return !invalidBlocks.contains(block);
/*     */     } 
/* 359 */     return false;
/*     */   }
/*     */   
/*     */   public BlockData getBlockData(BlockPos pos) {
/* 363 */     BlockData blockData = null;
/* 364 */     int i = 0;
/* 365 */     while (blockData == null && 
/* 366 */       i < 2) {
/*     */ 
/*     */       
/* 369 */       if (isBlockPosAir(pos.add(0, 0, 1))) {
/* 370 */         blockData = new BlockData(pos.add(0, 0, 1), EnumFacing.NORTH);
/*     */         break;
/*     */       } 
/* 373 */       if (isBlockPosAir(pos.add(0, 0, -1))) {
/* 374 */         blockData = new BlockData(pos.add(0, 0, -1), EnumFacing.SOUTH);
/*     */         break;
/*     */       } 
/* 377 */       if (isBlockPosAir(pos.add(1, 0, 0))) {
/* 378 */         blockData = new BlockData(pos.add(1, 0, 0), EnumFacing.WEST);
/*     */         break;
/*     */       } 
/* 381 */       if (isBlockPosAir(pos.add(-1, 0, 0))) {
/* 382 */         blockData = new BlockData(pos.add(-1, 0, 0), EnumFacing.EAST);
/*     */         break;
/*     */       } 
/* 385 */       if (mc.gameSettings.keyBindJump.isKeyDown() && 
/* 386 */         isBlockPosAir(pos.add(0, -1, 0))) {
/* 387 */         blockData = new BlockData(pos.add(0, -1, 0), EnumFacing.UP);
/*     */         
/*     */         break;
/*     */       } 
/* 391 */       if (isBlockPosAir(pos.add(0, 1, 0)) && isSneaking) {
/* 392 */         blockData = new BlockData(pos.add(0, 1, 0), EnumFacing.DOWN);
/*     */         break;
/*     */       } 
/* 395 */       if (isBlockPosAir(pos.add(0, 1, 1)) && isSneaking) {
/* 396 */         blockData = new BlockData(pos.add(0, 1, 1), EnumFacing.DOWN);
/*     */         break;
/*     */       } 
/* 399 */       if (isBlockPosAir(pos.add(0, 1, -1)) && isSneaking) {
/* 400 */         blockData = new BlockData(pos.add(0, 1, -1), EnumFacing.DOWN);
/*     */         break;
/*     */       } 
/* 403 */       if (isBlockPosAir(pos.add(1, 1, 0)) && isSneaking) {
/* 404 */         blockData = new BlockData(pos.add(1, 1, 0), EnumFacing.DOWN);
/*     */         break;
/*     */       } 
/* 407 */       if (isBlockPosAir(pos.add(-1, 1, 0)) && isSneaking) {
/* 408 */         blockData = new BlockData(pos.add(-1, 1, 0), EnumFacing.DOWN);
/*     */         break;
/*     */       } 
/* 411 */       if (isBlockPosAir(pos.add(1, 0, 1))) {
/* 412 */         blockData = new BlockData(pos.add(1, 0, 1), EnumFacing.NORTH);
/*     */         break;
/*     */       } 
/* 415 */       if (isBlockPosAir(pos.add(-1, 0, -1))) {
/* 416 */         blockData = new BlockData(pos.add(-1, 0, -1), EnumFacing.SOUTH);
/*     */         break;
/*     */       } 
/* 419 */       if (isBlockPosAir(pos.add(1, 0, 1))) {
/* 420 */         blockData = new BlockData(pos.add(1, 0, 1), EnumFacing.WEST);
/*     */         break;
/*     */       } 
/* 423 */       if (isBlockPosAir(pos.add(-1, 0, -1))) {
/* 424 */         blockData = new BlockData(pos.add(-1, 0, -1), EnumFacing.EAST);
/*     */         break;
/*     */       } 
/* 427 */       if (isBlockPosAir(pos.add(-1, 0, 1))) {
/* 428 */         blockData = new BlockData(pos.add(-1, 0, 1), EnumFacing.NORTH);
/*     */         break;
/*     */       } 
/* 431 */       if (isBlockPosAir(pos.add(1, 0, -1))) {
/* 432 */         blockData = new BlockData(pos.add(1, 0, -1), EnumFacing.SOUTH);
/*     */         break;
/*     */       } 
/* 435 */       if (isBlockPosAir(pos.add(1, 0, -1))) {
/* 436 */         blockData = new BlockData(pos.add(1, 0, -1), EnumFacing.WEST);
/*     */         break;
/*     */       } 
/* 439 */       if (isBlockPosAir(pos.add(-1, 0, 1))) {
/* 440 */         blockData = new BlockData(pos.add(-1, 0, 1), EnumFacing.EAST);
/*     */         break;
/*     */       } 
/* 443 */       pos = pos.down();
/* 444 */       i++;
/*     */     } 
/* 446 */     return blockData;
/*     */   }
/*     */   
/*     */   private Vec3d getVectorToPlace(BlockData data) {
/* 450 */     BlockPos pos = data.pos;
/* 451 */     EnumFacing face = data.face;
/* 452 */     double x = pos.getX() + 0.5D;
/* 453 */     double y = pos.getY() + 0.5D;
/* 454 */     double z = pos.getZ() + 0.5D;
/* 455 */     if (face == EnumFacing.UP || face == EnumFacing.DOWN) {
/* 456 */       x += 0.3D;
/* 457 */       z += 0.3D;
/*     */     } else {
/* 459 */       y += 0.5D;
/*     */     } 
/* 461 */     if (face == EnumFacing.WEST || face == EnumFacing.EAST) {
/* 462 */       z += this.placeOffset.getNumberValue();
/*     */     }
/* 464 */     if (face == EnumFacing.SOUTH || face == EnumFacing.NORTH) {
/* 465 */       x += this.placeOffset.getNumberValue();
/*     */     }
/* 467 */     return new Vec3d(x, y, z);
/*     */   }
/*     */   
/*     */   private Vec3d getVectorToRotate(BlockData data) {
/* 471 */     BlockPos pos = data.pos;
/* 472 */     EnumFacing face = data.face;
/* 473 */     double x = pos.getX() + 0.5D;
/* 474 */     double y = pos.getY() + 0.5D;
/* 475 */     double z = pos.getZ() + 0.5D;
/* 476 */     if (face == EnumFacing.UP || face == EnumFacing.DOWN) {
/* 477 */       x += 0.4D;
/* 478 */       z += 0.4D;
/*     */     } else {
/* 480 */       y += 0.4D;
/*     */     } 
/* 482 */     if (face == EnumFacing.WEST || face == EnumFacing.EAST) {
/* 483 */       z += this.rotationOffset.getNumberValue();
/*     */     }
/* 485 */     if (face == EnumFacing.SOUTH || face == EnumFacing.NORTH) {
/* 486 */       x += this.rotationOffset.getNumberValue();
/*     */     }
/* 488 */     return new Vec3d(x, y, z);
/*     */   }
/*     */   
/*     */   public boolean isBlockPosAir(BlockPos blockPos) {
/* 492 */     return (getBlockByPos(blockPos) != Blocks.AIR && !(getBlockByPos(blockPos) instanceof net.minecraft.block.BlockLiquid));
/*     */   }
/*     */   
/*     */   public Block getBlockByPos(BlockPos blockPos) {
/* 496 */     return mc.world.getBlockState(blockPos).getBlock();
/*     */   }
/*     */   
/*     */   private static class BlockData {
/*     */     public BlockPos pos;
/*     */     public EnumFacing face;
/*     */     
/*     */     private BlockData(BlockPos pos, EnumFacing face) {
/* 504 */       this.pos = pos;
/* 505 */       this.face = face;
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\Admin\OneDrive\Рабочий стол\NeverHook Crack.jar!\org\neverhook\client\feature\impl\movement\Scaffold.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */