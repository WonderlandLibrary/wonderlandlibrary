package org.alphacentauri.modules;

import java.util.ArrayList;
import java.util.List;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.inventory.GuiChest;
import net.minecraft.client.multiplayer.WorldClient;
import net.minecraft.client.settings.KeyBinding;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.Slot;
import net.minecraft.network.play.client.C0DPacketCloseWindow;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityChest;
import net.minecraft.tileentity.TileEntityEnderChest;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.Vec3;
import org.alphacentauri.AC;
import org.alphacentauri.management.bypass.AntiCheat;
import org.alphacentauri.management.events.Event;
import org.alphacentauri.management.events.EventListener;
import org.alphacentauri.management.events.EventTick;
import org.alphacentauri.management.events.EventWorldChanged;
import org.alphacentauri.management.modules.Module;
import org.alphacentauri.management.properties.Property;
import org.alphacentauri.management.util.BlockUtils;
import org.alphacentauri.management.util.LatencyUtils;
import org.alphacentauri.management.util.RotationUtils;
import org.alphacentauri.modules.ModuleChestStealer;

public class ModuleChestAura extends Module implements EventListener {
   private Property range = new Property(this, "Range", Float.valueOf(4.0F));
   private Property onGround = new Property(this, "NeedsOnGround", Boolean.valueOf(true));
   private int cooldownaura;
   private BlockPos current;
   public List clicked = new ArrayList();
   private Property set_cooldown = new Property(this, "CoolDown", Integer.valueOf(0));
   private int cooldown;
   private boolean didSteal = false;

   public ModuleChestAura() {
      super("ChestAura", "Steals Chests around you", new String[]{"chestaura"}, Module.Category.World, 8559855);
   }

   public void setEnabledSilent(boolean enabled) {
      this.clicked.clear();
      super.setEnabledSilent(enabled);
   }

   public void setBypass(AntiCheat ac) {
      if(ac != AntiCheat.NCP && ac != AntiCheat.Reflex) {
         this.set_cooldown.value = Integer.valueOf(0);
      } else if(ac == AntiCheat.NCP) {
         this.set_cooldown.value = Integer.valueOf(1);
      }

      super.setBypass(ac);
   }

   private boolean canClick(TileEntity tileEntity) {
      return true;
   }

   private void chestStealer() {
      if(this.cooldown > 0) {
         --this.cooldown;
      } else {
         if(AC.getMC().currentScreen instanceof GuiChest) {
            EntityPlayerSP player = AC.getMC().getPlayer();
            Container chest = player.openContainer;
            boolean hasSpace = false;

            for(int i = chest.inventorySlots.size() - 36; i < chest.inventorySlots.size(); ++i) {
               Slot slot = chest.getSlot(i);
               if(slot == null || !slot.getHasStack()) {
                  hasSpace = true;
                  break;
               }
            }

            if(!hasSpace) {
               return;
            }

            while(this.cooldown == 0) {
               boolean item_found = false;

               for(int i = 0; i < chest.inventorySlots.size() - 36; ++i) {
                  Slot slot = chest.getSlot(i);
                  if(slot.getHasStack() && slot.getStack() != null) {
                     AC.getMC().playerController.windowClick(chest.windowId, i, 0, 1, player);
                     this.cooldown = ((Integer)this.set_cooldown.value).intValue();
                     item_found = true;
                     break;
                  }
               }

               if(!item_found) {
                  AC.getMC().displayGuiScreen((GuiScreen)null);
                  player.sendQueue.addToSendQueue(new C0DPacketCloseWindow(chest.windowId));
                  KeyBinding.fixKeybinds();
                  this.didSteal = true;
                  break;
               }

               hasSpace = false;

               for(int i = chest.inventorySlots.size() - 36; i < chest.inventorySlots.size(); ++i) {
                  Slot slot = chest.getSlot(i);
                  if(slot == null || !slot.getHasStack()) {
                     hasSpace = true;
                     break;
                  }
               }

               if(!hasSpace) {
                  return;
               }
            }
         }

      }
   }

   private boolean hasSpaceInInv() {
      boolean hasSpace = false;
      Container inv = AC.getMC().getPlayer().inventoryContainer;

      for(int i = inv.inventorySlots.size() - 36; i < inv.inventorySlots.size(); ++i) {
         Slot slot = inv.getSlot(i);
         if(slot == null || !slot.getHasStack()) {
            hasSpace = true;
            break;
         }
      }

      return hasSpace;
   }

   public void onEvent(Event event) {
      if(event instanceof EventTick) {
         if(!this.hasSpaceInInv()) {
            return;
         }

         this.chestStealer();
         AC.getModuleManager().get(ModuleChestStealer.class).setEnabledSilent(false);
         if(this.cooldownaura > 0) {
            --this.cooldownaura;
            return;
         }

         if(!this.didSteal) {
            this.clicked.remove(this.current);
            this.didSteal = true;
         }

         EntityPlayerSP player = AC.getMC().getPlayer();
         if(((Boolean)this.onGround.value).booleanValue() && !player.onGround) {
            return;
         }

         WorldClient world = AC.getMC().getWorld();
         if(AC.getMC().currentScreen != null) {
            return;
         }

         if(RotationUtils.isSet()) {
            return;
         }

         for(TileEntity tileEntity : world.loadedTileEntityList) {
            if((tileEntity instanceof TileEntityChest || tileEntity instanceof TileEntityEnderChest) && !this.clicked.contains(tileEntity.getPos())) {
               double dist = tileEntity.getDistanceSq(player.posX, player.posY + (double)player.getEyeHeight(), player.posZ);
               if(dist <= (double)(((Float)this.range.value).floatValue() * ((Float)this.range.value).floatValue()) && this.canClick(tileEntity)) {
                  BlockUtils.faceBlockPacket(tileEntity.getPos());
                  AC.getMC().playerController.onPlayerRightClick(player, world, player.getHeldItem(), tileEntity.getPos(), EnumFacing.UP, new Vec3(0.5D, 0.5D, 0.5D));
                  this.cooldownaura = LatencyUtils.getLatencyTicks() + 2;
                  this.current = tileEntity.getPos();
                  this.didSteal = false;
                  this.clicked.add(this.current);
                  break;
               }
            }
         }
      } else if(event instanceof EventWorldChanged) {
         this.clicked.clear();
      }

   }
}
