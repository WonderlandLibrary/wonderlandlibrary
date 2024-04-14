package de.violence.module.modules.PLAYER;

import de.violence.module.Module;
import de.violence.module.ui.Category;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemSoup;
import net.minecraft.network.play.client.C08PacketPlayerBlockPlacement;
import net.minecraft.network.play.client.C09PacketHeldItemChange;

public class AutoSoup extends Module {
   private int lastSlot = 0;
   private boolean isHealing = false;
   private float delay = 0.0F;

   public AutoSoup() {
      super("AutoSoup", Category.PLAYER);
   }

   public void onUpdate() {
      this.delay += 0.5F;
      if(!this.containsInvSoups() && this.getRedMushrooms() > 0 && this.getBrownMushrooms() > 0 && this.getBowls() > 0) {
         this.recraftSoups();
      } else {
         this.lastSlot = this.mc.thePlayer.inventory.currentItem;
         this.isHealing = false;
         if(!this.isHealing) {
            if(this.isHotbarEmpty()) {
               this.refillSoups();
               return;
            }

            if(this.shouldEat()) {
               final int slot = this.getSoup();
               System.out.println(slot);
               this.mc.thePlayer.sendQueue.addToSendQueue(new C09PacketHeldItemChange(slot));
               this.isHealing = true;
               this.mc.thePlayer.sendQueue.addToSendQueue(new C08PacketPlayerBlockPlacement(this.mc.thePlayer.inventory.getStackInSlot(slot)));
               (new Thread(new Runnable() {
                  public void run() {
                     try {
                        if(AutoSoup.this.mc.thePlayer.inventoryContainer.getSlot(3).getStack() != null || AutoSoup.this.mc.thePlayer.inventoryContainer.getSlot(4).getStack() != null || AutoSoup.this.mc.thePlayer.inventoryContainer.getSlot(2) != null) {
                           AutoSoup.this.moveItemDown();
                        }

                        Thread.sleep(200L);
                        AutoSoup.this.dropSoup(36 + slot);
                        Thread.sleep(50L);
                        AutoSoup.this.mc.thePlayer.sendQueue.addToSendQueue(new C09PacketHeldItemChange(AutoSoup.this.lastSlot));
                        AutoSoup.this.isHealing = false;
                     } catch (InterruptedException var2) {
                        ;
                     }

                  }
               })).start();
            }
         }

         super.onUpdate();
      }
   }

   public void dropSoup(int slot) {
      this.mc.thePlayer.inventoryContainer.slotClick(slot, 0, 4, this.mc.thePlayer);
      this.mc.playerController.windowClick(this.mc.thePlayer.inventoryContainer.windowId, slot, 1, 4, this.mc.thePlayer);
   }

   public boolean shouldEat() {
      return this.mc.thePlayer.getHealth() <= 10.0F;
   }

   public boolean isHotbarEmpty() {
      for(int slots = 0; slots < 9; ++slots) {
         if(this.mc.thePlayer.inventoryContainer.getSlot(36 + slots).getStack() != null && this.mc.thePlayer.inventoryContainer.getSlot(36 + slots).getStack().getItem() instanceof ItemSoup) {
            return false;
         }
      }

      return true;
   }

   public boolean containsBowls() {
      for(int slots = 0; slots < 9; ++slots) {
         if(this.mc.thePlayer.inventoryContainer.getSlot(36 + slots).getStack() != null && this.mc.thePlayer.inventoryContainer.getSlot(36 + slots).getStack().getItem() == Items.bowl) {
            return true;
         }
      }

      return false;
   }

   public int getBowl() {
      int freeSlot = 0;

      for(int slots = 0; slots < 9; ++slots) {
         if(this.mc.thePlayer.inventoryContainer.getSlot(36 + slots).getStack() != null && this.mc.thePlayer.inventoryContainer.getSlot(36 + slots).getStack().getItem() == Items.bowl) {
            freeSlot = slots;
            break;
         }
      }

      return freeSlot;
   }

   public void refillSoups() {
      (new Thread(new Runnable() {
         public void run() {
            AutoSoup.this.isHealing = true;

            int slots;
            while(AutoSoup.this.containsBowls()) {
               slots = AutoSoup.this.getBowl();

               try {
                  Thread.sleep(10L);
                  AutoSoup.this.dropSoup(36 + slots);
               } catch (InterruptedException var5) {
                  ;
               }
            }

            for(slots = 8; slots < 36; ++slots) {
               if(AutoSoup.this.mc.thePlayer.inventoryContainer.getSlot(slots).getStack() != null && AutoSoup.this.mc.thePlayer.inventoryContainer.getSlot(slots).getStack().getItem() instanceof ItemSoup) {
                  int empty = AutoSoup.this.getEmptySpace();
                  if(AutoSoup.this.mc.thePlayer.inventoryContainer.getSlot(36 + empty).getStack() != null) {
                     break;
                  }

                  try {
                     Thread.sleep(10L);
                     AutoSoup.this.mc.playerController.windowClick(AutoSoup.this.mc.thePlayer.inventoryContainer.windowId, slots, empty, 2, AutoSoup.this.mc.thePlayer);
                  } catch (InterruptedException var4) {
                     ;
                  }
               }
            }

            AutoSoup.this.isHealing = false;
         }
      })).start();
   }

   public void recraftSoups() {
      this.isHealing = true;
      (new Thread(new Runnable() {
         public void run() {
            try {
               if(AutoSoup.this.getFreeSpaceAmount() > 3 && AutoSoup.this.getRedMushrooms() > 0 && AutoSoup.this.getBrownMushrooms() > 0 && AutoSoup.this.getBowls() > 0) {
                  AutoSoup.this.moveItemsUp();

                  while(AutoSoup.this.getFreeSpaceAmount() > 3 && AutoSoup.this.isToggled()) {
                     Thread.sleep(20L);
                     AutoSoup.this.craft();
                  }

                  Thread.sleep(10L);
                  AutoSoup.this.moveItemDown();
                  AutoSoup.this.isHealing = false;
               }
            } catch (Exception var2) {
               var2.printStackTrace();
            }

         }
      })).start();
   }

   public void craft() {
      this.moveRecraft(0, this.getFreeInvSpace());
   }

   public void moveItemsUp() throws InterruptedException {
      this.moveRecraft(this.getBowls(), 4);
      this.moveRecraft(this.getRedMushrooms(), 3);
      this.moveRecraft(this.getBrownMushrooms(), 2);
   }

   public void moveRecraft(int from, int to) {
      this.mc.playerController.windowClick(this.mc.thePlayer.inventoryContainer.windowId, from, 0, 0, this.mc.thePlayer);
      this.mc.playerController.windowClick(this.mc.thePlayer.inventoryContainer.windowId, to, 0, 0, this.mc.thePlayer);
   }

   public void moveItemDown() throws InterruptedException {
      this.moveRecraft(4, this.getFreeInvSpace());
      Thread.sleep(50L);
      this.moveRecraft(3, this.getFreeInvSpace());
      Thread.sleep(50L);
      this.moveRecraft(2, this.getFreeInvSpace());
   }

   public void swapItem(int inv, int slot) {
      this.mc.playerController.windowClick(this.mc.thePlayer.inventoryContainer.windowId, inv, slot, 0, this.mc.thePlayer);
   }

   public int getBrownMushrooms() {
      for(int slots = 8; slots < 45; ++slots) {
         if(this.mc.thePlayer.inventoryContainer.getSlot(slots).getStack() != null && Item.getIdFromItem(this.mc.thePlayer.inventoryContainer.getSlot(slots).getStack().getItem()) == 39) {
            return slots;
         }
      }

      return 0;
   }

   public int getRedMushrooms() {
      for(int slots = 0; slots < 45; ++slots) {
         if(this.mc.thePlayer.inventoryContainer.getSlot(slots).getStack() != null && Item.getIdFromItem(this.mc.thePlayer.inventoryContainer.getSlot(slots).getStack().getItem()) == 40) {
            return slots;
         }
      }

      return 0;
   }

   public int getBowls() {
      for(int slots = 8; slots < 45; ++slots) {
         if(this.mc.thePlayer.inventoryContainer.getSlot(slots).getStack() != null && this.mc.thePlayer.inventoryContainer.getSlot(slots).getStack().getItem() == Item.getItemById(281)) {
            return slots;
         }
      }

      return 0;
   }

   public int getSoup() {
      int freeSlot = 0;

      for(int slots = 0; slots < 9; ++slots) {
         if(this.mc.thePlayer.inventoryContainer.getSlot(36 + slots).getStack() != null && this.mc.thePlayer.inventoryContainer.getSlot(36 + slots).getStack().getItem() instanceof ItemSoup) {
            freeSlot = slots;
            break;
         }
      }

      return freeSlot;
   }

   public boolean containsInvSoups() {
      for(int slots = 0; slots < 36; ++slots) {
         if(this.mc.thePlayer.inventoryContainer.getSlot(slots).getStack() != null && this.mc.thePlayer.inventoryContainer.getSlot(slots).getStack().getItem() instanceof ItemSoup) {
            return true;
         }
      }

      return false;
   }

   public int getFreeSpaceAmount() {
      int amount = 0;

      for(int slots = 9; slots < 36; ++slots) {
         if(this.mc.thePlayer.inventoryContainer.getSlot(slots).getStack() == null) {
            ++amount;
         }
      }

      return amount;
   }

   public int getFreeInvSpace() {
      for(int slots = 9; slots < 45; ++slots) {
         if(this.mc.thePlayer.inventoryContainer.getSlot(slots).getStack() == null) {
            return slots;
         }
      }

      return 0;
   }

   public int getSlots() {
      for(int slots = 9; slots < 36; ++slots) {
         if(this.mc.thePlayer.inventoryContainer.getSlot(slots).getStack() == null) {
            return slots;
         }
      }

      return 0;
   }

   public int getEmptySpace() {
      int freeSlot = 0;

      for(int slots = this.mc.thePlayer.inventoryContainer.getSlot(37).getStack() != null?1:0; slots < 9; ++slots) {
         if(this.mc.thePlayer.inventoryContainer.getSlot(36 + slots).getStack() == null) {
            freeSlot = slots;
            break;
         }
      }

      return freeSlot;
   }
}
