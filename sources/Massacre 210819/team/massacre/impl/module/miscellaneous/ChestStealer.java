package team.massacre.impl.module.miscellaneous;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.ThreadLocalRandom;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.client.gui.inventory.GuiChest;
import net.minecraft.item.ItemStack;
import net.minecraft.network.play.client.C0APacketAnimation;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityChest;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.Vec3;
import team.massacre.api.event.api.annotations.Handler;
import team.massacre.api.module.Category;
import team.massacre.api.module.Module;
import team.massacre.api.property.Property;
import team.massacre.api.property.Representation;
import team.massacre.api.property.impl.DoubleProperty;
import team.massacre.impl.event.EventUpdate;
import team.massacre.utils.ItemUtils;
import team.massacre.utils.PacketUtil;
import team.massacre.utils.TimerUtil;

public class ChestStealer extends Module {
   public Property<Boolean> titleCheck = new Property("Title Check", true);
   public Property<Boolean> autoClose = new Property("Auto Close", true);
   public Property<Boolean> aura = new Property("Aura", false);
   public Property<Boolean> filter = new Property("Filter", true);
   public Property<Boolean> silent = new Property("Silent", false);
   public DoubleProperty delay;
   public DoubleProperty range;
   public TimerUtil timer;
   public TimerUtil auraTimer;
   public final Set openedChests;

   public ChestStealer() {
      super("ChestStealer", 0, Category.MISCELLANEOUS);
      this.delay = new DoubleProperty("Delay", 4.0D, 1.0D, 10.0D, 1.0D, Representation.MILLISECONDS);
      this.range = new DoubleProperty("Aura Range", 5.0D, 1.0D, 6.0D, 0.1D, Representation.DISTANCE);
      this.timer = new TimerUtil();
      this.auraTimer = new TimerUtil();
      this.openedChests = new HashSet();
      this.addValues(new Property[]{this.autoClose, this.titleCheck, this.filter, this.aura, this.silent, this.range, this.delay});
   }

   @Handler
   public void onMotionUpdate(EventUpdate event) {
      int delay = ((Double)this.delay.getValue()).intValue() * 50;
      EntityPlayerSP player = this.mc.thePlayer;
      int index;
      if (this.mc.currentScreen instanceof GuiChest && event.isPre()) {
         GuiChest chest = (GuiChest)this.mc.currentScreen;
         boolean titleCheck = (Boolean)this.titleCheck.getValue() ? chest.getLowerChestInventory().getDisplayName().getUnformattedText().contains("Chest") || chest.getLowerChestInventory().getDisplayName().getUnformattedText().contains("Contai") || chest.getLowerChestInventory().getDisplayName().getUnformattedText().contains("Crate") || chest.getLowerChestInventory().getDisplayName().getUnformattedText().equalsIgnoreCase("LOW") : true;
         if (titleCheck) {
            if ((Boolean)this.autoClose.getValue() && (this.isChestEmpty(chest) || this.isInventoryFull())) {
               player.closeScreen();
               return;
            }

            for(index = 0; index < chest.getLowerChestInventory().getSizeInventory(); ++index) {
               ItemStack stack = chest.getLowerChestInventory().getStackInSlot(index);
               if (stack != null && this.timer.hasHit((float)(delay - ThreadLocalRandom.current().nextInt(0, 250)))) {
                  boolean trash = (Boolean)this.filter.getValue() ? !ItemUtils.isTrash(stack) : true;
                  if (trash) {
                     this.mc.playerController.windowClick(chest.inventorySlots.windowId, index, 0, 1, player);
                     this.timer.reset();
                     break;
                  }
               }
            }
         }
      }

      if (!event.isPre() && this.mc.currentScreen == null && (Boolean)this.aura.getValue()) {
         List loadedTileEntityList = this.mc.theWorld.loadedTileEntityList;
         index = 0;

         for(int loadedTileEntityListSize = loadedTileEntityList.size(); index < loadedTileEntityListSize; ++index) {
            TileEntity tile = (TileEntity)loadedTileEntityList.get(index);
            BlockPos pos = tile.getPos();
            if (tile instanceof TileEntityChest && this.getDistToPos(pos) < (Double)this.range.getValue() && !this.openedChests.contains(tile) && this.auraTimer.hasHit(500.0F) && this.mc.playerController.onPlayerRightClick(player, this.mc.theWorld, player.getHeldItem(), pos, EnumFacing.DOWN, this.getVec3(tile.getPos()))) {
               PacketUtil.sendPacketNoEvent(new C0APacketAnimation());
               this.set(this.openedChests, tile);
               this.auraTimer.reset();
               return;
            }
         }
      }

   }

   public void set(Set set, TileEntity chest) {
      if (set.size() > 128) {
         set.clear();
      }

      set.add(chest);
   }

   public Vec3 getVec3(BlockPos pos) {
      return new Vec3((double)pos.getX(), (double)pos.getY(), (double)pos.getZ());
   }

   public double getDistToPos(BlockPos pos) {
      return this.mc.thePlayer.getDistance((double)pos.getX(), (double)pos.getY(), (double)pos.getZ());
   }

   public boolean isChestEmpty(GuiChest chest) {
      for(int index = 0; index < chest.getLowerChestInventory().getSizeInventory(); ++index) {
         ItemStack stack = chest.getLowerChestInventory().getStackInSlot(index);
         if (stack != null && !ItemUtils.isTrash(stack)) {
            return false;
         }
      }

      return true;
   }

   public boolean isInventoryFull() {
      for(int index = 9; index <= 44; ++index) {
         ItemStack stack = this.mc.thePlayer.inventoryContainer.getSlot(index).getStack();
         if (stack == null) {
            return false;
         }
      }

      return true;
   }

   public void onEnable() {
      super.onEnable();
      this.auraTimer.reset();
      this.timer.reset();
   }

   public void onDisable() {
      super.onDisable();
   }
}
