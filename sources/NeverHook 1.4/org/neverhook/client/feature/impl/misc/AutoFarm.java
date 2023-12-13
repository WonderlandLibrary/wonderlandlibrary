/*     */ package org.neverhook.client.feature.impl.misc;
/*     */ 
/*     */ import java.util.ArrayList;
/*     */ import java.util.Comparator;
/*     */ import net.minecraft.block.Block;
/*     */ import net.minecraft.block.BlockCrops;
/*     */ import net.minecraft.block.state.IBlockState;
/*     */ import net.minecraft.entity.Entity;
/*     */ import net.minecraft.entity.player.EntityPlayer;
/*     */ import net.minecraft.init.Blocks;
/*     */ import net.minecraft.inventory.ClickType;
/*     */ import net.minecraft.item.ItemStack;
/*     */ import net.minecraft.network.Packet;
/*     */ import net.minecraft.network.play.client.CPacketHeldItemChange;
/*     */ import net.minecraft.network.play.client.CPacketPlayerTryUseItemOnBlock;
/*     */ import net.minecraft.network.play.server.SPacketBlockChange;
/*     */ import net.minecraft.network.play.server.SPacketMultiBlockChange;
/*     */ import net.minecraft.util.EnumFacing;
/*     */ import net.minecraft.util.EnumHand;
/*     */ import net.minecraft.util.math.BlockPos;
/*     */ import net.minecraft.util.math.Vec3d;
/*     */ import net.minecraft.world.World;
/*     */ import org.neverhook.client.event.EventTarget;
/*     */ import org.neverhook.client.event.events.impl.packet.EventReceivePacket;
/*     */ import org.neverhook.client.event.events.impl.player.EventPreMotion;
/*     */ import org.neverhook.client.feature.Feature;
/*     */ import org.neverhook.client.feature.impl.Type;
/*     */ import org.neverhook.client.helpers.math.RotationHelper;
/*     */ import org.neverhook.client.helpers.misc.TimerHelper;
/*     */ import org.neverhook.client.helpers.world.BlockHelper;
/*     */ import org.neverhook.client.helpers.world.EntityHelper;
/*     */ import org.neverhook.client.settings.Setting;
/*     */ import org.neverhook.client.settings.impl.BooleanSetting;
/*     */ import org.neverhook.client.settings.impl.NumberSetting;
/*     */ 
/*     */ public class AutoFarm extends Feature {
/*     */   private final NumberSetting delay;
/*     */   private final NumberSetting radius;
/*  39 */   ArrayList<BlockPos> crops = new ArrayList<>(); private final BooleanSetting autoHoe; private final BooleanSetting autoFarm;
/*  40 */   ArrayList<BlockPos> check = new ArrayList<>();
/*  41 */   TimerHelper timerHelper = new TimerHelper();
/*  42 */   TimerHelper timerHelper2 = new TimerHelper();
/*     */   
/*     */   public AutoFarm() {
/*  45 */     super("Auto Farm", "Автоматически садит и ломает урожай", Type.Misc);
/*  46 */     this.autoFarm = new BooleanSetting("Auto Farm", true, () -> Boolean.valueOf(true));
/*  47 */     this.autoHoe = new BooleanSetting("Auto Hoe", false, () -> Boolean.valueOf(true));
/*  48 */     this.delay = new NumberSetting("Farm Delay", 2.0F, 0.0F, 10.0F, 0.1F, () -> Boolean.valueOf(true));
/*  49 */     this.radius = new NumberSetting("Farm Radius", 4.0F, 1.0F, 7.0F, 0.1F, () -> Boolean.valueOf(true));
/*  50 */     addSettings(new Setting[] { (Setting)this.autoFarm, (Setting)this.autoHoe, (Setting)this.delay, (Setting)this.radius });
/*     */   }
/*     */   
/*     */   public static boolean doesHaveSeeds() {
/*  54 */     for (int i = 0; i < 9; i++) {
/*  55 */       mc.player.inventory.getStackInSlot(i);
/*  56 */       if (mc.player.inventory.getStackInSlot(i).getItem() instanceof net.minecraft.item.ItemSeeds) {
/*  57 */         return true;
/*     */       }
/*     */     } 
/*  60 */     return false;
/*     */   }
/*     */   
/*     */   public static int searchSeeds() {
/*  64 */     for (int i = 0; i < 45; i++) {
/*  65 */       ItemStack itemStack = mc.player.inventoryContainer.getSlot(i).getStack();
/*  66 */       if (itemStack.getItem() instanceof net.minecraft.item.ItemSeeds) {
/*  67 */         return i;
/*     */       }
/*     */     } 
/*  70 */     return -1;
/*     */   }
/*     */   
/*     */   public static int getSlotWithSeeds() {
/*  74 */     for (int i = 0; i < 9; i++) {
/*  75 */       mc.player.inventory.getStackInSlot(i);
/*  76 */       if (mc.player.inventory.getStackInSlot(i).getItem() instanceof net.minecraft.item.ItemSeeds) {
/*  77 */         return i;
/*     */       }
/*     */     } 
/*  80 */     return 0;
/*     */   }
/*     */ 
/*     */   
/*     */   public void onEnable() {
/*  85 */     this.crops.clear();
/*  86 */     this.check.clear();
/*  87 */     super.onEnable();
/*     */   }
/*     */   
/*     */   private boolean isOnCrops() {
/*  91 */     for (double x = mc.player.boundingBox.minX; x < mc.player.boundingBox.maxX; x += 0.009999999776482582D) {
/*  92 */       for (double z = mc.player.boundingBox.minZ; z < mc.player.boundingBox.maxZ; z += 0.009999999776482582D) {
/*  93 */         Block block = mc.world.getBlockState(new BlockPos(x, mc.player.posY - 0.1D, z)).getBlock();
/*  94 */         if (!(block instanceof net.minecraft.block.BlockFarmland) && !(block instanceof net.minecraft.block.BlockCarrot) && !(block instanceof net.minecraft.block.BlockSoulSand) && !(block instanceof net.minecraft.block.BlockSand) && !(block instanceof net.minecraft.block.BlockAir)) {
/*  95 */           return false;
/*     */         }
/*     */       } 
/*     */     } 
/*  99 */     return true;
/*     */   }
/*     */   
/*     */   private boolean IsValidBlockPos(BlockPos pos) {
/* 103 */     IBlockState state = mc.world.getBlockState(pos);
/* 104 */     if (state.getBlock() instanceof net.minecraft.block.BlockFarmland || state.getBlock() instanceof net.minecraft.block.BlockSand || state.getBlock() instanceof net.minecraft.block.BlockSoulSand)
/* 105 */       return (mc.world.getBlockState(pos.up()).getBlock() == Blocks.AIR); 
/* 106 */     return false;
/*     */   }
/*     */   
/*     */   @EventTarget
/*     */   public void onPreMotion(EventPreMotion event) {
/* 111 */     if (mc.player == null && mc.world == null)
/* 112 */       return;  BlockPos pos = BlockHelper.getSphere(BlockHelper.getPlayerPos(), this.radius.getNumberValue(), 6, false, true).stream().filter(BlockHelper::IsValidBlockPos).min(Comparator.comparing(blockPos -> Double.valueOf(EntityHelper.getDistanceOfEntityToBlock((Entity)mc.player, blockPos)))).orElse(null);
/* 113 */     if (pos != null && mc.player.getHeldItemMainhand().getItem() instanceof net.minecraft.item.ItemHoe) {
/* 114 */       float[] rots = RotationHelper.getRotationVector(new Vec3d((pos.getX() + 0.5F), (pos.getY() + 0.5F), (pos.getZ() + 0.5F)), true, 2.0F, 2.0F, 360.0F);
/* 115 */       event.setYaw(rots[0]);
/* 116 */       event.setPitch(rots[1]);
/* 117 */       mc.player.renderYawOffset = rots[0];
/* 118 */       mc.player.rotationYawHead = rots[0];
/* 119 */       mc.player.rotationPitchHead = rots[1];
/* 120 */       if (this.timerHelper2.hasReached(this.delay.getNumberValue() * 100.0F)) {
/* 121 */         mc.player.connection.sendPacket((Packet)new CPacketPlayerTryUseItemOnBlock(pos, EnumFacing.UP, EnumHand.MAIN_HAND, 0.0F, 0.0F, 0.0F));
/* 122 */         mc.player.swingArm(EnumHand.MAIN_HAND);
/* 123 */         this.timerHelper2.reset();
/*     */       } 
/*     */     } 
/*     */ 
/*     */     
/* 128 */     if (!doesHaveSeeds() && 
/* 129 */       searchSeeds() != -1) {
/* 130 */       mc.playerController.windowClick(0, searchSeeds(), 1, ClickType.QUICK_MOVE, (EntityPlayer)mc.player);
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   @EventTarget
/*     */   public void onPre(EventPreMotion e) {
/* 137 */     if (this.autoFarm.getBoolValue()) {
/* 138 */       ArrayList<BlockPos> blockPositions = getBlocks(this.radius.getNumberValue(), this.radius.getNumberValue(), this.radius.getNumberValue());
/* 139 */       for (BlockPos blockPos : blockPositions) {
/* 140 */         IBlockState state = mc.world.getBlockState(blockPos);
/* 141 */         if (isCheck(Block.getIdFromBlock(state.getBlock()))) {
/* 142 */           if (!isCheck(0)) {
/* 143 */             this.check.add(blockPos);
/*     */           }
/* 145 */           Block block = mc.world.getBlockState(blockPos).getBlock();
/* 146 */           BlockPos downPos = blockPos.down(1);
/* 147 */           if (block instanceof BlockCrops || block instanceof net.minecraft.block.BlockCarrot) {
/* 148 */             BlockCrops crop = (BlockCrops)block;
/* 149 */             if (!crop.canGrow((World)mc.world, blockPos, state, true) && 
/* 150 */               this.timerHelper.hasReached(this.delay.getNumberValue() * 100.0F) && 
/* 151 */               blockPos != null) {
/* 152 */               float[] rots = RotationHelper.getRotationVector(new Vec3d((blockPos.getX() + 0.5F), (blockPos.getY() + 0.5F), (blockPos.getZ() + 0.5F)), true, 2.0F, 2.0F, 360.0F);
/* 153 */               e.setYaw(rots[0]);
/* 154 */               e.setPitch(rots[1]);
/* 155 */               mc.player.renderYawOffset = rots[0];
/* 156 */               mc.player.rotationYawHead = rots[0];
/* 157 */               mc.player.rotationPitchHead = rots[1];
/* 158 */               mc.playerController.onPlayerDamageBlock(blockPos, mc.player.getHorizontalFacing());
/* 159 */               mc.player.swingArm(EnumHand.MAIN_HAND);
/*     */               
/* 161 */               if (doesHaveSeeds()) {
/* 162 */                 mc.player.connection.sendPacket((Packet)new CPacketHeldItemChange(getSlotWithSeeds()));
/* 163 */                 mc.player.connection.sendPacket((Packet)new CPacketPlayerTryUseItemOnBlock(downPos, EnumFacing.UP, EnumHand.MAIN_HAND, 0.0F, 0.0F, 0.0F));
/* 164 */                 mc.player.swingArm(EnumHand.MAIN_HAND);
/*     */               } 
/*     */               
/* 167 */               this.timerHelper.reset();
/*     */             } 
/*     */           } 
/*     */         } 
/*     */       } 
/*     */     } 
/*     */ 
/*     */     
/* 175 */     BlockPos pos = BlockHelper.getSphere(BlockHelper.getPlayerPos(), this.radius.getNumberValue(), 6, false, true).stream().filter(this::IsValidBlockPos).min(Comparator.comparing(blockPos -> Double.valueOf(EntityHelper.getDistanceOfEntityToBlock((Entity)mc.player, blockPos)))).orElse(null);
/* 176 */     Vec3d vec = new Vec3d(0.0D, 0.0D, 0.0D);
/* 177 */     if (this.timerHelper.hasReached(this.delay.getNumberValue() * 100.0F) && 
/* 178 */       isOnCrops() && 
/* 179 */       pos != null && 
/* 180 */       doesHaveSeeds()) {
/* 181 */       float[] rots = RotationHelper.getRotationVector(new Vec3d((pos.getX() + 0.5F), (pos.getY() + 0.5F), (pos.getZ() + 0.5F)), true, 2.0F, 2.0F, 360.0F);
/* 182 */       e.setYaw(rots[0]);
/* 183 */       e.setPitch(rots[1]);
/* 184 */       mc.player.renderYawOffset = rots[0];
/* 185 */       mc.player.rotationYawHead = rots[0];
/* 186 */       mc.player.rotationPitchHead = rots[1];
/* 187 */       mc.player.connection.sendPacket((Packet)new CPacketHeldItemChange(getSlotWithSeeds()));
/* 188 */       mc.playerController.processRightClickBlock(mc.player, mc.world, pos, EnumFacing.VALUES[0].getOpposite(), vec, EnumHand.MAIN_HAND);
/* 189 */       this.timerHelper.reset();
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @EventTarget
/*     */   public void onReceivePacket(EventReceivePacket e) {
/* 198 */     if (this.autoFarm.getBoolValue()) {
/* 199 */       if (e.getPacket() instanceof SPacketBlockChange) {
/* 200 */         SPacketBlockChange p = (SPacketBlockChange)e.getPacket();
/*     */         
/* 202 */         if (isEnabled(Block.getIdFromBlock(p.getBlockState().getBlock()))) {
/* 203 */           this.crops.add(p.getBlockPosition());
/*     */         }
/* 205 */       } else if (e.getPacket() instanceof SPacketMultiBlockChange) {
/* 206 */         SPacketMultiBlockChange p = (SPacketMultiBlockChange)e.getPacket();
/*     */         
/* 208 */         for (SPacketMultiBlockChange.BlockUpdateData dat : p.getChangedBlocks()) {
/* 209 */           if (isEnabled(Block.getIdFromBlock(dat.getBlockState().getBlock()))) {
/* 210 */             this.crops.add(dat.getPos());
/*     */           }
/*     */         } 
/*     */       } 
/*     */     }
/*     */   }
/*     */   
/*     */   private boolean isCheck(int id) {
/* 218 */     int check = 0;
/* 219 */     if (id != 0) {
/* 220 */       check = 59;
/*     */     }
/* 222 */     if (id == 0) {
/* 223 */       return false;
/*     */     }
/* 225 */     return (id == check);
/*     */   }
/*     */   
/*     */   private boolean isEnabled(int id) {
/* 229 */     int check = 0;
/* 230 */     if (id != 0) {
/* 231 */       check = 59;
/*     */     }
/* 233 */     if (id == 0) {
/* 234 */       return false;
/*     */     }
/* 236 */     return (id == check);
/*     */   }
/*     */   
/*     */   private ArrayList<BlockPos> getBlocks(float x, float y, float z) {
/* 240 */     BlockPos min = new BlockPos(mc.player.posX - x, mc.player.posY - y, mc.player.posZ - z);
/* 241 */     BlockPos max = new BlockPos(mc.player.posX + x, mc.player.posY + y, mc.player.posZ + z);
/*     */     
/* 243 */     return BlockHelper.getAllInBox(min, max);
/*     */   }
/*     */ }


/* Location:              C:\Users\Admin\OneDrive\Рабочий стол\NeverHook Crack.jar!\org\neverhook\client\feature\impl\misc\AutoFarm.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */