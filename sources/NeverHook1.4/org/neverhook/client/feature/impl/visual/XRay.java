/*     */ package org.neverhook.client.feature.impl.visual;
/*     */ 
/*     */ import java.util.ArrayList;
/*     */ import java.util.List;
/*     */ import java.util.concurrent.CopyOnWriteArrayList;
/*     */ import net.minecraft.block.Block;
/*     */ import net.minecraft.block.state.IBlockState;
/*     */ import net.minecraft.network.Packet;
/*     */ import net.minecraft.network.play.client.CPacketPlayerDigging;
/*     */ import net.minecraft.network.play.server.SPacketBlockChange;
/*     */ import net.minecraft.network.play.server.SPacketMultiBlockChange;
/*     */ import net.minecraft.util.EnumFacing;
/*     */ import net.minecraft.util.math.BlockPos;
/*     */ import net.minecraft.util.math.Vec3i;
/*     */ import org.neverhook.client.cmd.impl.XrayCommand;
/*     */ import org.neverhook.client.event.EventTarget;
/*     */ import org.neverhook.client.event.events.impl.packet.EventReceivePacket;
/*     */ import org.neverhook.client.event.events.impl.player.EventPreMotion;
/*     */ import org.neverhook.client.event.events.impl.render.EventRender3D;
/*     */ import org.neverhook.client.event.events.impl.render.EventRenderBlock;
/*     */ import org.neverhook.client.feature.Feature;
/*     */ import org.neverhook.client.feature.impl.Type;
/*     */ import org.neverhook.client.helpers.misc.ClientHelper;
/*     */ import org.neverhook.client.helpers.render.RenderHelper;
/*     */ import org.neverhook.client.helpers.world.BlockHelper;
/*     */ import org.neverhook.client.helpers.world.EntityHelper;
/*     */ import org.neverhook.client.settings.Setting;
/*     */ import org.neverhook.client.settings.impl.BooleanSetting;
/*     */ import org.neverhook.client.settings.impl.NumberSetting;
/*     */ 
/*     */ public class XRay
/*     */   extends Feature {
/*     */   public static int done;
/*     */   public static int all;
/*     */   public static BooleanSetting brutForce;
/*     */   public static BooleanSetting diamond;
/*     */   public static BooleanSetting gold;
/*     */   public static BooleanSetting iron;
/*     */   public static BooleanSetting emerald;
/*     */   public static BooleanSetting redstone;
/*     */   public static BooleanSetting lapis;
/*     */   public static BooleanSetting coal;
/*     */   private final NumberSetting checkSpeed;
/*     */   private final NumberSetting renderDist;
/*     */   private final NumberSetting rxz;
/*     */   private final NumberSetting ry;
/*  47 */   private final ArrayList<BlockPos> ores = new ArrayList<>();
/*  48 */   private final ArrayList<BlockPos> toCheck = new ArrayList<>();
/*  49 */   private final List<Vec3i> blocks = new CopyOnWriteArrayList<>();
/*     */   
/*     */   public XRay() {
/*  52 */     super("XRay", "Иксрей, позволяющий обойти анти-иксрей на серверах", Type.Misc);
/*  53 */     brutForce = new BooleanSetting("BrutForce", false, () -> Boolean.valueOf(true));
/*  54 */     this.renderDist = new NumberSetting("Render Distance", 35.0F, 15.0F, 150.0F, 5.0F, () -> Boolean.valueOf(!brutForce.getBoolValue()));
/*  55 */     diamond = new BooleanSetting("Diamond", true, () -> Boolean.valueOf(true));
/*  56 */     gold = new BooleanSetting("Gold", false, () -> Boolean.valueOf(true));
/*  57 */     iron = new BooleanSetting("Iron", false, () -> Boolean.valueOf(true));
/*  58 */     emerald = new BooleanSetting("Emerald", false, () -> Boolean.valueOf(true));
/*  59 */     redstone = new BooleanSetting("Redstone", false, () -> Boolean.valueOf(true));
/*  60 */     lapis = new BooleanSetting("Lapis", false, () -> Boolean.valueOf(true));
/*  61 */     coal = new BooleanSetting("Coal", false, () -> Boolean.valueOf(true));
/*  62 */     this.checkSpeed = new NumberSetting("CheckSpeed", 4.0F, 1.0F, 10.0F, 1.0F, brutForce::getBoolValue);
/*  63 */     this.rxz = new NumberSetting("Radius XZ", 20.0F, 5.0F, 200.0F, 1.0F, brutForce::getBoolValue);
/*  64 */     this.ry = new NumberSetting("Radius Y", 6.0F, 2.0F, 50.0F, 1.0F, brutForce::getBoolValue);
/*  65 */     addSettings(new Setting[] { (Setting)this.renderDist, (Setting)brutForce, (Setting)this.checkSpeed, (Setting)this.rxz, (Setting)this.ry, (Setting)diamond, (Setting)gold, (Setting)iron, (Setting)emerald, (Setting)redstone, (Setting)lapis, (Setting)coal });
/*     */   }
/*     */   
/*     */   private boolean isEnabledOre(int id) {
/*  69 */     int check = 0;
/*  70 */     int check1 = 0;
/*  71 */     int check2 = 0;
/*  72 */     int check3 = 0;
/*  73 */     int check4 = 0;
/*  74 */     int check5 = 0;
/*  75 */     int check6 = 0;
/*  76 */     int check7 = 0;
/*  77 */     if (diamond.getBoolValue() && id != 0) {
/*  78 */       check = 56;
/*     */     }
/*  80 */     if (gold.getBoolValue() && id != 0) {
/*  81 */       check1 = 14;
/*     */     }
/*  83 */     if (iron.getBoolValue() && id != 0) {
/*  84 */       check2 = 15;
/*     */     }
/*  86 */     if (emerald.getBoolValue() && id != 0) {
/*  87 */       check3 = 129;
/*     */     }
/*  89 */     if (redstone.getBoolValue() && id != 0) {
/*  90 */       check4 = 73;
/*     */     }
/*  92 */     if (coal.getBoolValue() && id != 0) {
/*  93 */       check5 = 16;
/*     */     }
/*  95 */     if (lapis.getBoolValue() && id != 0) {
/*  96 */       check6 = 21;
/*     */     }
/*  98 */     for (Integer integer : XrayCommand.blockIDS) {
/*  99 */       if (integer.intValue() != 0) {
/* 100 */         check7 = integer.intValue();
/*     */       }
/*     */     } 
/* 103 */     if (id == 0) {
/* 104 */       return false;
/*     */     }
/* 106 */     return (id == check || id == check1 || id == check2 || id == check3 || id == check4 || id == check5 || id == check6 || id == check7);
/*     */   }
/*     */   
/*     */   private ArrayList<BlockPos> getBlocks(int x, int y, int z) {
/* 110 */     BlockPos min = new BlockPos(mc.player.posX - x, mc.player.posY - y, mc.player.posZ - z);
/* 111 */     BlockPos max = new BlockPos(mc.player.posX + x, mc.player.posY + y, mc.player.posZ + z);
/*     */     
/* 113 */     return BlockHelper.getAllInBox(min, max);
/*     */   }
/*     */ 
/*     */   
/*     */   public void onEnable() {
/* 118 */     if (brutForce.getBoolValue()) {
/* 119 */       int radXZ = (int)this.rxz.getNumberValue();
/* 120 */       int radY = (int)this.ry.getNumberValue();
/*     */       
/* 122 */       ArrayList<BlockPos> blockPositions = getBlocks(radXZ, radY, radXZ);
/*     */       
/* 124 */       for (BlockPos pos : blockPositions) {
/* 125 */         IBlockState state = mc.world.getBlockState(pos);
/* 126 */         if (isCheckableOre(Block.getIdFromBlock(state.getBlock()))) {
/* 127 */           this.toCheck.add(pos);
/*     */         }
/*     */       } 
/* 130 */       all = this.toCheck.size();
/* 131 */       done = 0;
/*     */     } 
/* 133 */     super.onEnable();
/*     */   }
/*     */ 
/*     */   
/*     */   public void onDisable() {
/* 138 */     mc.renderGlobal.loadRenderers();
/* 139 */     super.onDisable();
/*     */   }
/*     */   
/*     */   @EventTarget
/*     */   public void onPreMotion(EventPreMotion event) {
/* 144 */     String allDone = (done == all) ? ("Done: " + all) : ("" + done + " / " + all);
/* 145 */     if (brutForce.getBoolValue()) {
/* 146 */       setSuffix(allDone);
/* 147 */       for (int i = 0; i < this.checkSpeed.getNumberValue(); i++) {
/* 148 */         if (this.toCheck.size() < 1)
/*     */           return; 
/* 150 */         BlockPos pos = this.toCheck.remove(0);
/* 151 */         done++;
/* 152 */         mc.player.connection.sendPacket((Packet)new CPacketPlayerDigging(CPacketPlayerDigging.Action.START_DESTROY_BLOCK, pos, EnumFacing.UP));
/*     */       } 
/*     */     } 
/*     */   }
/*     */   
/*     */   @EventTarget
/*     */   public void onReceivePacket(EventReceivePacket e) {
/* 159 */     if (brutForce.getBoolValue()) {
/* 160 */       if (e.getPacket() instanceof SPacketBlockChange) {
/* 161 */         SPacketBlockChange p = (SPacketBlockChange)e.getPacket();
/* 162 */         if (isEnabledOre(Block.getIdFromBlock(p.getBlockState().getBlock())) && 
/* 163 */           !mc.world.isAirBlock(p.getBlockPosition())) {
/* 164 */           this.ores.add(p.getBlockPosition());
/*     */         }
/*     */       }
/* 167 */       else if (e.getPacket() instanceof SPacketMultiBlockChange) {
/* 168 */         SPacketMultiBlockChange p = (SPacketMultiBlockChange)e.getPacket();
/* 169 */         for (SPacketMultiBlockChange.BlockUpdateData dat : p.getChangedBlocks()) {
/* 170 */           if (isEnabledOre(Block.getIdFromBlock(dat.getBlockState().getBlock())) && 
/* 171 */             !mc.world.isAirBlock(dat.getPos())) {
/* 172 */             this.ores.add(dat.getPos());
/*     */           }
/*     */         } 
/*     */       } 
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   @EventTarget
/*     */   public void onRenderBlock(EventRenderBlock event) {
/* 182 */     BlockPos pos = event.getPos();
/* 183 */     IBlockState blockState = event.getState();
/* 184 */     if (isEnabledOre(Block.getIdFromBlock(blockState.getBlock()))) {
/* 185 */       Vec3i vec3i = new Vec3i(pos.getX(), pos.getY(), pos.getZ());
/* 186 */       this.blocks.add(vec3i);
/*     */     } 
/*     */   }
/*     */   
/*     */   @EventTarget
/*     */   public void onRender3D(EventRender3D event) {
/* 192 */     if (brutForce.getBoolValue()) {
/* 193 */       for (BlockPos pos : this.ores) {
/* 194 */         IBlockState state = mc.world.getBlockState(pos);
/* 195 */         Block block = state.getBlock();
/*     */         
/* 197 */         if (this.toCheck.size() <= 0 || Block.getIdFromBlock(block) == 0) {
/*     */           continue;
/*     */         }
/* 200 */         switch (Block.getIdFromBlock(block)) {
/*     */           case 56:
/* 202 */             if (diamond.getBoolValue())
/* 203 */               RenderHelper.blockEspFrame(pos, 0.0F, 255.0F, 255.0F); 
/*     */             break;
/*     */           case 14:
/* 206 */             if (gold.getBoolValue())
/* 207 */               RenderHelper.blockEspFrame(pos, 255.0F, 215.0F, 0.0F); 
/*     */             break;
/*     */           case 15:
/* 210 */             if (iron.getBoolValue())
/* 211 */               RenderHelper.blockEspFrame(pos, 213.0F, 213.0F, 213.0F); 
/*     */             break;
/*     */           case 129:
/* 214 */             if (emerald.getBoolValue())
/* 215 */               RenderHelper.blockEspFrame(pos, 0.0F, 255.0F, 77.0F); 
/*     */             break;
/*     */           case 73:
/* 218 */             if (redstone.getBoolValue())
/* 219 */               RenderHelper.blockEspFrame(pos, 255.0F, 0.0F, 0.0F); 
/*     */             break;
/*     */           case 16:
/* 222 */             if (coal.getBoolValue())
/* 223 */               RenderHelper.blockEspFrame(pos, 0.0F, 0.0F, 0.0F); 
/*     */             break;
/*     */           case 21:
/* 226 */             if (lapis.getBoolValue()) {
/* 227 */               RenderHelper.blockEspFrame(pos, 38.0F, 97.0F, 156.0F);
/*     */             }
/*     */             break;
/*     */         } 
/* 231 */         for (Integer integer : XrayCommand.blockIDS) {
/* 232 */           if (Block.getIdFromBlock(block) == integer.intValue()) {
/* 233 */             RenderHelper.blockEspFrame(pos, ClientHelper.getClientColor().getRed() / 255.0F, ClientHelper.getClientColor().getGreen() / 255.0F, ClientHelper.getClientColor().getBlue() / 255.0F);
/*     */           }
/*     */         } 
/*     */       } 
/*     */     } else {
/* 238 */       for (Vec3i neededBlock : this.blocks) {
/* 239 */         BlockPos pos = new BlockPos(neededBlock);
/* 240 */         IBlockState state = mc.world.getBlockState(pos);
/* 241 */         Block stateBlock = state.getBlock();
/*     */         
/* 243 */         Block block = mc.world.getBlockState(pos).getBlock();
/*     */         
/* 245 */         if (block instanceof net.minecraft.block.BlockAir || Block.getIdFromBlock(block) == 0) {
/*     */           continue;
/*     */         }
/* 248 */         if (EntityHelper.getDistance(mc.player.posX, mc.player.posZ, neededBlock.getX(), neededBlock.getZ()) > this.renderDist.getNumberValue()) {
/* 249 */           this.blocks.remove(neededBlock);
/*     */           
/*     */           continue;
/*     */         } 
/* 253 */         switch (Block.getIdFromBlock(block)) {
/*     */           case 56:
/* 255 */             if (diamond.getBoolValue())
/* 256 */               RenderHelper.blockEspFrame(pos, 0.0F, 255.0F, 255.0F); 
/*     */             break;
/*     */           case 14:
/* 259 */             if (gold.getBoolValue())
/* 260 */               RenderHelper.blockEspFrame(pos, 255.0F, 215.0F, 0.0F); 
/*     */             break;
/*     */           case 15:
/* 263 */             if (iron.getBoolValue())
/* 264 */               RenderHelper.blockEspFrame(pos, 213.0F, 213.0F, 213.0F); 
/*     */             break;
/*     */           case 129:
/* 267 */             if (emerald.getBoolValue())
/* 268 */               RenderHelper.blockEspFrame(pos, 0.0F, 255.0F, 77.0F); 
/*     */             break;
/*     */           case 73:
/* 271 */             if (redstone.getBoolValue())
/* 272 */               RenderHelper.blockEspFrame(pos, 255.0F, 0.0F, 0.0F); 
/*     */             break;
/*     */           case 16:
/* 275 */             if (coal.getBoolValue())
/* 276 */               RenderHelper.blockEspFrame(pos, 0.0F, 0.0F, 0.0F); 
/*     */             break;
/*     */           case 21:
/* 279 */             if (lapis.getBoolValue()) {
/* 280 */               RenderHelper.blockEspFrame(pos, 38.0F, 97.0F, 156.0F);
/*     */             }
/*     */             break;
/*     */         } 
/* 284 */         for (Integer integer : XrayCommand.blockIDS) {
/* 285 */           if (Block.getIdFromBlock(stateBlock) != 0 && Block.getIdFromBlock(stateBlock) == integer.intValue()) {
/* 286 */             RenderHelper.blockEspFrame(pos, ClientHelper.getClientColor().getRed() / 255.0F, ClientHelper.getClientColor().getGreen() / 255.0F, ClientHelper.getClientColor().getBlue() / 255.0F);
/*     */           }
/*     */         } 
/*     */       } 
/*     */     } 
/*     */   }
/*     */   
/*     */   private boolean isCheckableOre(int id) {
/* 294 */     int check = 0;
/* 295 */     int check1 = 0;
/* 296 */     int check2 = 0;
/* 297 */     int check3 = 0;
/* 298 */     int check4 = 0;
/* 299 */     int check5 = 0;
/* 300 */     int check6 = 0;
/* 301 */     int check7 = 0;
/*     */     
/* 303 */     if (diamond.getBoolValue() && id != 0) {
/* 304 */       check = 56;
/*     */     }
/* 306 */     if (gold.getBoolValue() && id != 0) {
/* 307 */       check1 = 14;
/*     */     }
/* 309 */     if (iron.getBoolValue() && id != 0) {
/* 310 */       check2 = 15;
/*     */     }
/* 312 */     if (emerald.getBoolValue() && id != 0) {
/* 313 */       check3 = 129;
/*     */     }
/* 315 */     if (redstone.getBoolValue() && id != 0) {
/* 316 */       check4 = 73;
/*     */     }
/* 318 */     if (coal.getBoolValue() && id != 0) {
/* 319 */       check5 = 16;
/*     */     }
/* 321 */     if (lapis.getBoolValue() && id != 0) {
/* 322 */       check6 = 21;
/*     */     }
/* 324 */     for (Integer integer : XrayCommand.blockIDS) {
/* 325 */       if (integer.intValue() != 0) {
/* 326 */         check7 = integer.intValue();
/*     */       }
/*     */     } 
/* 329 */     if (id == 0) {
/* 330 */       return false;
/*     */     }
/* 332 */     return (id == check || id == check1 || id == check2 || id == check3 || id == check4 || id == check5 || id == check6 || id == check7);
/*     */   }
/*     */ }


/* Location:              C:\Users\Admin\OneDrive\Рабочий стол\NeverHook Crack.jar!\org\neverhook\client\feature\impl\visual\XRay.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */