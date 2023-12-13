package cn.hanabi.modules.modules.world;

import aLph4anTi1eaK_cN.Annotation.ObfuscationClass;
import cn.hanabi.events.EventPacket;
import cn.hanabi.events.EventPostMotion;
import cn.hanabi.events.EventPreMotion;
import cn.hanabi.events.EventUpdate;
import cn.hanabi.modules.Category;
import cn.hanabi.modules.Mod;
import cn.hanabi.utils.BlockUtils;
import cn.hanabi.utils.PlayerUtil;
import cn.hanabi.utils.TimeHelper;
import cn.hanabi.value.Value;
import com.darkmagician6.eventapi.EventTarget;
import net.minecraft.block.Block;
import net.minecraft.block.BlockAir;
import net.minecraft.block.BlockHopper;
import net.minecraft.item.ItemShears;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemSword;
import net.minecraft.item.ItemTool;
import net.minecraft.network.play.client.C03PacketPlayer;
import net.minecraft.network.play.client.C09PacketHeldItemChange;
import net.minecraft.network.play.client.C0BPacketEntityAction;
import net.minecraft.network.play.client.C0BPacketEntityAction.Action;
import net.minecraft.network.play.server.S2CPacketSpawnGlobalEntity;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.MathHelper;
import net.minecraft.util.Vec3;
import net.minecraft.world.WorldSettings.GameType;
import org.lwjgl.input.Keyboard;

@ObfuscationClass
public class UhcHelper extends Mod {
   public static int movement;
   public static int x;
   public static int y;
   public static int z;
   public Value sandbreak = new Value("UHCHelper", "Sand Breaker", false);
   public Value LightningCheck = new Value("UHCHelper", "Lightning Check", false);
   public Value noSandDamage = new Value("UHCHelper", "No Sand Damage", false);
   public Value noWaterDamage = new Value("UHCHelper", "No Water Damage", false);
   public Value lessPacket = new Value("UHCHelper", "Less Packet", false);
   public Value autojump = new Value("UHCHelper", "Stuck Jump", false);
   public Value autowater = new Value("UHCHelper", "Auto Water", false);
   public Value sneakily = new Value("UHCHelper", "Sneak Move", false);
   boolean sneak = false;
   TimeHelper sneakTimer = new TimeHelper();
   int clock;
   int delay;
   private final TimeHelper timer = new TimeHelper();
   private boolean reFill;
   private final TimeHelper refillTimer = new TimeHelper();

   public UhcHelper() {
      super("UHCHelper", Category.WORLD);
   }

   @EventTarget
   public void OnUpdate(EventUpdate e) {
      BlockPos sand = new BlockPos(new Vec3(mc.thePlayer.posX, mc.thePlayer.posY + 3.0, mc.thePlayer.posZ));
      Block sandblock = mc.theWorld.getBlockState(sand).getBlock();
      BlockPos forge = new BlockPos(new Vec3(mc.thePlayer.posX, mc.thePlayer.posY + 2.0, mc.thePlayer.posZ));
      Block forgeblock = mc.theWorld.getBlockState(forge).getBlock();
      BlockPos obsidianpos = new BlockPos(new Vec3(mc.thePlayer.posX, mc.thePlayer.posY + 1.0, mc.thePlayer.posZ));
      Block obsidianblock = mc.theWorld.getBlockState(obsidianpos).getBlock();
      BlockPos downpos;
      if (obsidianblock == Block.getBlockById(49)) {
         this.bestTool(mc.objectMouseOver.getBlockPos().getX(), mc.objectMouseOver.getBlockPos().getY(), mc.objectMouseOver.getBlockPos().getZ());
         downpos = new BlockPos(new Vec3(mc.thePlayer.posX, mc.thePlayer.posY - 1.0, mc.thePlayer.posZ));
         mc.playerController.onPlayerDamageBlock(downpos, EnumFacing.UP);
      }

      if (forgeblock == Block.getBlockById(61)) {
         this.bestTool(mc.objectMouseOver.getBlockPos().getX(), mc.objectMouseOver.getBlockPos().getY(), mc.objectMouseOver.getBlockPos().getZ());
         downpos = new BlockPos(new Vec3(mc.thePlayer.posX, mc.thePlayer.posY - 1.0, mc.thePlayer.posZ));
         mc.playerController.onPlayerDamageBlock(downpos, EnumFacing.UP);
      }

      if ((Boolean)this.sandbreak.getValueState() && (sandblock == Block.getBlockById(12) || sandblock == Block.getBlockById(13))) {
         this.bestTool(mc.objectMouseOver.getBlockPos().getX(), mc.objectMouseOver.getBlockPos().getY(), mc.objectMouseOver.getBlockPos().getZ());
         downpos = new BlockPos(new Vec3(mc.thePlayer.posX, mc.thePlayer.posY + 3.0, mc.thePlayer.posZ));
         PlayerUtil.tellPlayer("Sand On your Head. Care for it :D");
         mc.playerController.onPlayerDamageBlock(downpos, EnumFacing.UP);
      }

      if ((Boolean)this.lessPacket.getValueState()) {
         mc.thePlayer.setGameType(GameType.SURVIVAL);
         mc.thePlayer.setGameType(GameType.CREATIVE);
      }

      if ((Boolean)this.autojump.getValue() && PlayerUtil.isUnderBlock(mc.thePlayer) && PlayerUtil.isMoving2() && mc.thePlayer.onGround && Keyboard.isKeyDown(56)) {
         mc.thePlayer.jump();
      }

   }

   @EventTarget
   public void onPre(EventPreMotion e) {
      if ((Boolean)this.autowater.getValue() && mc.thePlayer.isBurning() && (this.getSlotWaterBucket() != -1 || this.reFill)) {
         e.setPitch(90.0F);
      }

      if (mc.gameSettings.keyBindSneak.isKeyDown() && (Boolean)this.sneakily.getValue()) {
         mc.getNetHandler().getNetworkManager().sendPacket(new C0BPacketEntityAction(mc.thePlayer, Action.STOP_SNEAKING));
      }

   }

   @EventTarget
   public void onPost(EventPostMotion post) {
      if (mc.gameSettings.keyBindSneak.isKeyDown() && (Boolean)this.sneakily.getValue()) {
         mc.getNetHandler().getNetworkManager().sendPacket(new C0BPacketEntityAction(mc.thePlayer, Action.START_SNEAKING));
      }

      if ((Boolean)this.autowater.getValue() && mc.thePlayer.isBurning()) {
         BlockPos pos;
         if (this.reFill) {
            pos = new BlockPos(mc.thePlayer.posX, mc.thePlayer.posY - 1.0, mc.thePlayer.posZ);
            PlayerUtil.debug("Re");
            this.placeWater(pos);
            this.reFill = false;
         } else if (this.refillTimer.isDelayComplete(1500L) && this.reFill) {
            this.reFill = false;
         }

         if (this.getSlotWaterBucket() != -1 && this.timer.isDelayComplete(500L)) {
            this.timer.reset();
            this.swapToWaterBucket(this.getSlotWaterBucket());
            pos = new BlockPos(mc.thePlayer.posX, mc.thePlayer.posY - BlockUtils.getDistanceToFall() - 1.0, mc.thePlayer.posZ);
            this.placeWater(pos);
            PlayerUtil.debug("Redo");
            this.reFill = true;
            this.refillTimer.reset();
         }
      }

   }

   private void placeWater(BlockPos pos) {
      ItemStack heldItem = mc.thePlayer.inventory.getCurrentItem();
      mc.playerController.onPlayerRightClick(mc.thePlayer, mc.theWorld, mc.thePlayer.inventory.getCurrentItem(), pos, EnumFacing.UP, new Vec3((double)pos.getX() + 0.5, (double)pos.getY() + 1.0, (double)pos.getZ() + 0.5));
      if (heldItem != null) {
         mc.playerController.sendUseItem(mc.thePlayer, mc.theWorld, heldItem);
         mc.entityRenderer.itemRenderer.resetEquippedProgress2();
      }

   }

   private void swapToWaterBucket(int blockSlot) {
      mc.thePlayer.inventory.currentItem = blockSlot;
      mc.thePlayer.sendQueue.getNetworkManager().sendPacket(new C09PacketHeldItemChange(blockSlot));
   }

   private int getSlotWaterBucket() {
      for(int i = 0; i < 8; ++i) {
         if (mc.thePlayer.inventory.mainInventory[i] != null && mc.thePlayer.inventory.mainInventory[i].getItem().getUnlocalizedName().contains("bucketWater")) {
            return i;
         }
      }

      return -1;
   }

   @EventTarget
   public void onPacketReceive(EventPacket packetEvent) {
      if ((Boolean)this.noSandDamage.getValueState() && packetEvent.packet instanceof C03PacketPlayer && this.isInsideBlock()) {
         packetEvent.setCancelled(true);
      }

      if ((Boolean)this.noWaterDamage.getValueState() && packetEvent.packet instanceof C03PacketPlayer && PlayerUtil.isInWater() && Keyboard.isKeyDown(42)) {
         packetEvent.setCancelled(true);
      }

      if ((Boolean)this.LightningCheck.getValueState() && packetEvent.packet instanceof S2CPacketSpawnGlobalEntity) {
         S2CPacketSpawnGlobalEntity packetIn = (S2CPacketSpawnGlobalEntity)packetEvent.packet;
         if (packetIn.func_149053_g() == 1) {
            x = (int)((double)packetIn.func_149051_d() / 32.0);
            y = (int)((double)packetIn.func_149050_e() / 32.0);
            z = (int)((double)packetIn.func_149049_f() / 32.0);
            PlayerUtil.tellPlayer("Found Lightning X:" + x + "-Y:" + y + "-Z:" + z);
         }
      }

   }

   public void onEnable() {
      if ((Boolean)this.noWaterDamage.getValueState()) {
         PlayerUtil.tellPlayer("Lshift In Water To Enable Water GodMode");
      }

      if ((Boolean)this.autojump.getValue()) {
         PlayerUtil.debug("LAlt to Toggle Stuck Jump");
      }

      this.sneakTimer.reset();
      super.onEnable();
   }

   public void onDisable() {
      if ((Boolean)this.lessPacket.getValueState()) {
         if (mc.thePlayer.capabilities.isCreativeMode) {
            mc.thePlayer.setGameType(GameType.CREATIVE);
         } else {
            mc.thePlayer.setGameType(GameType.SURVIVAL);
         }
      }

      this.sneakTimer.reset();
      super.onDisable();
   }

   private boolean isInsideBlock() {
      for(int x = MathHelper.floor_double(Phase.mc.thePlayer.getEntityBoundingBox().minX); x < MathHelper.floor_double(Phase.mc.thePlayer.getEntityBoundingBox().maxX) + 1; ++x) {
         for(int y = MathHelper.floor_double(Phase.mc.thePlayer.getEntityBoundingBox().minY); y < MathHelper.floor_double(Phase.mc.thePlayer.getEntityBoundingBox().maxY) + 1; ++y) {
            for(int z = MathHelper.floor_double(Phase.mc.thePlayer.getEntityBoundingBox().minZ); z < MathHelper.floor_double(Phase.mc.thePlayer.getEntityBoundingBox().maxZ) + 1; ++z) {
               Block block = Phase.mc.theWorld.getBlockState(new BlockPos(x, y, z)).getBlock();
               if (block != null && !(block instanceof BlockAir) && block.isFullBlock()) {
                  AxisAlignedBB boundingBox = block.getCollisionBoundingBox(Phase.mc.theWorld, new BlockPos(x, y, z), Phase.mc.theWorld.getBlockState(new BlockPos(x, y, z)));
                  if (block instanceof BlockHopper) {
                     boundingBox = new AxisAlignedBB((double)x, (double)y, (double)z, (double)(x + 1), (double)(y + 1), (double)(z + 1));
                  }

                  if (boundingBox != null && Phase.mc.thePlayer.getEntityBoundingBox().intersectsWith(boundingBox)) {
                     return true;
                  }
               }
            }
         }
      }

      return false;
   }

   public void bestTool(int x, int y, int z) {
      int blockId = Block.getIdFromBlock(mc.theWorld.getBlockState(new BlockPos(x, y, z)).getBlock());
      int bestSlot = 0;
      float f = -1.0F;

      for(int i1 = 36; i1 < 45; ++i1) {
         try {
            ItemStack curSlot = mc.thePlayer.inventoryContainer.getSlot(i1).getStack();
            if ((curSlot.getItem() instanceof ItemTool || curSlot.getItem() instanceof ItemSword || curSlot.getItem() instanceof ItemShears) && curSlot.getStrVsBlock(Block.getBlockById(blockId)) > f) {
               bestSlot = i1 - 36;
               f = curSlot.getStrVsBlock(Block.getBlockById(blockId));
            }
         } catch (Exception var9) {
         }
      }

      if (f != -1.0F) {
         mc.thePlayer.inventory.currentItem = bestSlot;
         mc.playerController.updateController();
      }

   }
}
