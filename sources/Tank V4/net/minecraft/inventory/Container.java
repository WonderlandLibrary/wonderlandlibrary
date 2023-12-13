package net.minecraft.inventory;

import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.MathHelper;

public abstract class Container {
   private final Set dragSlots = Sets.newHashSet();
   private int dragEvent;
   private Set playerList = Sets.newHashSet();
   protected List crafters = Lists.newArrayList();
   public List inventorySlots = Lists.newArrayList();
   private int dragMode = -1;
   public List inventoryItemStacks = Lists.newArrayList();
   private short transactionID;
   public int windowId;

   public Slot getSlot(int var1) {
      return (Slot)this.inventorySlots.get(var1);
   }

   public boolean canDragIntoSlot(Slot var1) {
      return true;
   }

   public static int calcRedstoneFromInventory(IInventory var0) {
      if (var0 == null) {
         return 0;
      } else {
         int var1 = 0;
         float var2 = 0.0F;

         for(int var3 = 0; var3 < var0.getSizeInventory(); ++var3) {
            ItemStack var4 = var0.getStackInSlot(var3);
            if (var4 != null) {
               var2 += (float)var4.stackSize / (float)Math.min(var0.getInventoryStackLimit(), var4.getMaxStackSize());
               ++var1;
            }
         }

         var2 /= (float)var0.getSizeInventory();
         return MathHelper.floor_float(var2 * 14.0F) + (var1 > 0 ? 1 : 0);
      }
   }

   public ItemStack transferStackInSlot(EntityPlayer var1, int var2) {
      Slot var3 = (Slot)this.inventorySlots.get(var2);
      return var3 != null ? var3.getStack() : null;
   }

   public ItemStack slotClick(int var1, int var2, int var3, EntityPlayer var4) {
      ItemStack var5 = null;
      InventoryPlayer var6 = var4.inventory;
      int var9;
      ItemStack var18;
      if (var3 == 5) {
         int var7 = this.dragEvent;
         this.dragEvent = getDragEvent(var2);
         if ((var7 != 1 || this.dragEvent != 2) && var7 != this.dragEvent) {
            this.resetDrag();
         } else if (var6.getItemStack() == null) {
            this.resetDrag();
         } else if (this.dragEvent == 0) {
            this.dragMode = extractDragMode(var2);
            int var10000 = this.dragMode;
            if (var4 != false) {
               this.dragEvent = 1;
               this.dragSlots.clear();
            } else {
               this.resetDrag();
            }
         } else if (this.dragEvent == 1) {
            Slot var8 = (Slot)this.inventorySlots.get(var1);
            if (var8 != null) {
               var6.getItemStack();
               if (true && var8.isItemValid(var6.getItemStack()) && var6.getItemStack().stackSize > this.dragSlots.size() && this.canDragIntoSlot(var8)) {
                  this.dragSlots.add(var8);
               }
            }
         } else if (this.dragEvent == 2) {
            if (!this.dragSlots.isEmpty()) {
               var18 = var6.getItemStack().copy();
               var9 = var6.getItemStack().stackSize;
               Iterator var11 = this.dragSlots.iterator();

               while(var11.hasNext()) {
                  Slot var10 = (Slot)var11.next();
                  if (var10 != null) {
                     var6.getItemStack();
                     if (true && var10.isItemValid(var6.getItemStack()) && var6.getItemStack().stackSize >= this.dragSlots.size() && this.canDragIntoSlot(var10)) {
                        ItemStack var12 = var18.copy();
                        int var13 = var10.getHasStack() ? var10.getStack().stackSize : 0;
                        computeStackSize(this.dragSlots, this.dragMode, var12, var13);
                        if (var12.stackSize > var12.getMaxStackSize()) {
                           var12.stackSize = var12.getMaxStackSize();
                        }

                        if (var12.stackSize > var10.getItemStackLimit(var12)) {
                           var12.stackSize = var10.getItemStackLimit(var12);
                        }

                        var9 -= var12.stackSize - var13;
                        var10.putStack(var12);
                     }
                  }
               }

               var18.stackSize = var9;
               if (var18.stackSize <= 0) {
                  var18 = null;
               }

               var6.setItemStack(var18);
            }

            this.resetDrag();
         } else {
            this.resetDrag();
         }
      } else if (this.dragEvent != 0) {
         this.resetDrag();
      } else {
         Slot var17;
         int var21;
         ItemStack var23;
         if (var3 != 0 && var3 != 1 || var2 != 0 && var2 != 1) {
            if (var3 == 2 && var2 >= 0 && var2 < 9) {
               var17 = (Slot)this.inventorySlots.get(var1);
               if (var17.canTakeStack(var4)) {
                  var18 = var6.getStackInSlot(var2);
                  boolean var22 = var18 == null || var17.inventory == var6 && var17.isItemValid(var18);
                  var21 = -1;
                  if (!var22) {
                     var21 = var6.getFirstEmptyStack();
                     var22 |= var21 > -1;
                  }

                  if (var17.getHasStack() && var22) {
                     var23 = var17.getStack();
                     var6.setInventorySlotContents(var2, var23.copy());
                     if ((var17.inventory != var6 || !var17.isItemValid(var18)) && var18 != null) {
                        if (var21 > -1) {
                           var6.addItemStackToInventory(var18);
                           var17.decrStackSize(var23.stackSize);
                           var17.putStack((ItemStack)null);
                           var17.onPickupFromSlot(var4, var23);
                        }
                     } else {
                        var17.decrStackSize(var23.stackSize);
                        var17.putStack(var18);
                        var17.onPickupFromSlot(var4, var23);
                     }
                  } else if (!var17.getHasStack() && var18 != null && var17.isItemValid(var18)) {
                     var6.setInventorySlotContents(var2, (ItemStack)null);
                     var17.putStack(var18);
                  }
               }
            } else if (var3 == 3 && var4.capabilities.isCreativeMode && var6.getItemStack() == null && var1 >= 0) {
               var17 = (Slot)this.inventorySlots.get(var1);
               if (var17 != null && var17.getHasStack()) {
                  var18 = var17.getStack().copy();
                  var18.stackSize = var18.getMaxStackSize();
                  var6.setItemStack(var18);
               }
            } else if (var3 == 4 && var6.getItemStack() == null && var1 >= 0) {
               var17 = (Slot)this.inventorySlots.get(var1);
               if (var17 != null && var17.getHasStack() && var17.canTakeStack(var4)) {
                  var18 = var17.decrStackSize(var2 == 0 ? 1 : var17.getStack().stackSize);
                  var17.onPickupFromSlot(var4, var18);
                  var4.dropPlayerItemWithRandomChoice(var18, true);
               }
            } else if (var3 == 6 && var1 >= 0) {
               var17 = (Slot)this.inventorySlots.get(var1);
               var18 = var6.getItemStack();
               if (var18 != null && (var17 == null || !var17.getHasStack() || !var17.canTakeStack(var4))) {
                  var9 = var2 == 0 ? 0 : this.inventorySlots.size() - 1;
                  var21 = var2 == 0 ? 1 : -1;

                  for(int var24 = 0; var24 < 2; ++var24) {
                     for(int var25 = var9; var25 >= 0 && var25 < this.inventorySlots.size() && var18.stackSize < var18.getMaxStackSize(); var25 += var21) {
                        Slot var26 = (Slot)this.inventorySlots.get(var25);
                        if (var26.getHasStack() && true && var26.canTakeStack(var4) && this.canMergeSlot(var18, var26) && (var24 != 0 || var26.getStack().stackSize != var26.getStack().getMaxStackSize())) {
                           int var14 = Math.min(var18.getMaxStackSize() - var18.stackSize, var26.getStack().stackSize);
                           ItemStack var15 = var26.decrStackSize(var14);
                           var18.stackSize += var14;
                           if (var15.stackSize <= 0) {
                              var26.putStack((ItemStack)null);
                           }

                           var26.onPickupFromSlot(var4, var15);
                        }
                     }
                  }
               }

               this.detectAndSendChanges();
            }
         } else if (var1 == -999) {
            if (var6.getItemStack() != null) {
               if (var2 == 0) {
                  var4.dropPlayerItemWithRandomChoice(var6.getItemStack(), true);
                  var6.setItemStack((ItemStack)null);
               }

               if (var2 == 1) {
                  var4.dropPlayerItemWithRandomChoice(var6.getItemStack().splitStack(1), true);
                  if (var6.getItemStack().stackSize == 0) {
                     var6.setItemStack((ItemStack)null);
                  }
               }
            }
         } else if (var3 == 1) {
            if (var1 < 0) {
               return null;
            }

            var17 = (Slot)this.inventorySlots.get(var1);
            if (var17 != null && var17.canTakeStack(var4)) {
               var18 = this.transferStackInSlot(var4, var1);
               if (var18 != null) {
                  Item var19 = var18.getItem();
                  var5 = var18.copy();
                  if (var17.getStack() != null && var17.getStack().getItem() == var19) {
                     this.retrySlotClick(var1, var2, true, var4);
                  }
               }
            }
         } else {
            if (var1 < 0) {
               return null;
            }

            var17 = (Slot)this.inventorySlots.get(var1);
            if (var17 != null) {
               var18 = var17.getStack();
               ItemStack var20 = var6.getItemStack();
               if (var18 != null) {
                  var5 = var18.copy();
               }

               if (var18 == null) {
                  if (var20 != null && var17.isItemValid(var20)) {
                     var21 = var2 == 0 ? var20.stackSize : 1;
                     if (var21 > var17.getItemStackLimit(var20)) {
                        var21 = var17.getItemStackLimit(var20);
                     }

                     if (var20.stackSize >= var21) {
                        var17.putStack(var20.splitStack(var21));
                     }

                     if (var20.stackSize == 0) {
                        var6.setItemStack((ItemStack)null);
                     }
                  }
               } else if (var17.canTakeStack(var4)) {
                  if (var20 == null) {
                     var21 = var2 == 0 ? var18.stackSize : (var18.stackSize + 1) / 2;
                     var23 = var17.decrStackSize(var21);
                     var6.setItemStack(var23);
                     if (var18.stackSize == 0) {
                        var17.putStack((ItemStack)null);
                     }

                     var17.onPickupFromSlot(var4, var6.getItemStack());
                  } else if (var17.isItemValid(var20)) {
                     if (var18.getItem() == var20.getItem() && var18.getMetadata() == var20.getMetadata() && ItemStack.areItemStackTagsEqual(var18, var20)) {
                        var21 = var2 == 0 ? var20.stackSize : 1;
                        if (var21 > var17.getItemStackLimit(var20) - var18.stackSize) {
                           var21 = var17.getItemStackLimit(var20) - var18.stackSize;
                        }

                        if (var21 > var20.getMaxStackSize() - var18.stackSize) {
                           var21 = var20.getMaxStackSize() - var18.stackSize;
                        }

                        var20.splitStack(var21);
                        if (var20.stackSize == 0) {
                           var6.setItemStack((ItemStack)null);
                        }

                        var18.stackSize += var21;
                     } else if (var20.stackSize <= var17.getItemStackLimit(var20)) {
                        var17.putStack(var20);
                        var6.setItemStack(var18);
                     }
                  } else if (var18.getItem() == var20.getItem() && var20.getMaxStackSize() > 1 && (!var18.getHasSubtypes() || var18.getMetadata() == var20.getMetadata()) && ItemStack.areItemStackTagsEqual(var18, var20)) {
                     var21 = var18.stackSize;
                     if (var21 > 0 && var21 + var20.stackSize <= var20.getMaxStackSize()) {
                        var20.stackSize += var21;
                        var18 = var17.decrStackSize(var21);
                        if (var18.stackSize == 0) {
                           var17.putStack((ItemStack)null);
                        }

                        var17.onPickupFromSlot(var4, var6.getItemStack());
                     }
                  }
               }

               var17.onSlotChanged();
            }
         }
      }

      return var5;
   }

   public void onCraftGuiOpened(ICrafting var1) {
      if (this.crafters.contains(var1)) {
         throw new IllegalArgumentException("Listener already listening");
      } else {
         this.crafters.add(var1);
         var1.updateCraftingInventory(this, this.getInventory());
         this.detectAndSendChanges();
      }
   }

   public static int getDragEvent(int var0) {
      return var0 & 3;
   }

   public Slot getSlotFromInventory(IInventory var1, int var2) {
      for(int var3 = 0; var3 < this.inventorySlots.size(); ++var3) {
         Slot var4 = (Slot)this.inventorySlots.get(var3);
         if (var4.isHere(var1, var2)) {
            return var4;
         }
      }

      return null;
   }

   public void onCraftMatrixChanged(IInventory var1) {
      this.detectAndSendChanges();
   }

   protected void resetDrag() {
      this.dragEvent = 0;
      this.dragSlots.clear();
   }

   public short getNextTransactionID(InventoryPlayer var1) {
      ++this.transactionID;
      return this.transactionID;
   }

   protected void retrySlotClick(int var1, int var2, boolean var3, EntityPlayer var4) {
      this.slotClick(var1, var2, 1, var4);
   }

   public static int extractDragMode(int var0) {
      return var0 >> 2 & 3;
   }

   public abstract boolean canInteractWith(EntityPlayer var1);

   public void putStacksInSlots(ItemStack[] var1) {
      for(int var2 = 0; var2 < var1.length; ++var2) {
         this.getSlot(var2).putStack(var1[var2]);
      }

   }

   public void onContainerClosed(EntityPlayer var1) {
      InventoryPlayer var2 = var1.inventory;
      if (var2.getItemStack() != null) {
         var1.dropPlayerItemWithRandomChoice(var2.getItemStack(), false);
         var2.setItemStack((ItemStack)null);
      }

   }

   public void setCanCraft(EntityPlayer var1, boolean var2) {
      if (var2) {
         this.playerList.remove(var1);
      } else {
         this.playerList.add(var1);
      }

   }

   public void putStackInSlot(int var1, ItemStack var2) {
      this.getSlot(var1).putStack(var2);
   }

   protected boolean mergeItemStack(ItemStack var1, int var2, int var3, boolean var4) {
      boolean var5 = false;
      int var6 = var2;
      if (var4) {
         var6 = var3 - 1;
      }

      Slot var7;
      ItemStack var8;
      if (var1.isStackable()) {
         while(var1.stackSize > 0 && (!var4 && var6 < var3 || var4 && var6 >= var2)) {
            var7 = (Slot)this.inventorySlots.get(var6);
            var8 = var7.getStack();
            if (var8 != null && var8.getItem() == var1.getItem() && (!var1.getHasSubtypes() || var1.getMetadata() == var8.getMetadata()) && ItemStack.areItemStackTagsEqual(var1, var8)) {
               int var9 = var8.stackSize + var1.stackSize;
               if (var9 <= var1.getMaxStackSize()) {
                  var1.stackSize = 0;
                  var8.stackSize = var9;
                  var7.onSlotChanged();
                  var5 = true;
               } else if (var8.stackSize < var1.getMaxStackSize()) {
                  var1.stackSize -= var1.getMaxStackSize() - var8.stackSize;
                  var8.stackSize = var1.getMaxStackSize();
                  var7.onSlotChanged();
                  var5 = true;
               }
            }

            if (var4) {
               --var6;
            } else {
               ++var6;
            }
         }
      }

      if (var1.stackSize > 0) {
         if (var4) {
            var6 = var3 - 1;
         } else {
            var6 = var2;
         }

         while(!var4 && var6 < var3 || var4 && var6 >= var2) {
            var7 = (Slot)this.inventorySlots.get(var6);
            var8 = var7.getStack();
            if (var8 == null) {
               var7.putStack(var1.copy());
               var7.onSlotChanged();
               var1.stackSize = 0;
               var5 = true;
               break;
            }

            if (var4) {
               --var6;
            } else {
               ++var6;
            }
         }
      }

      return var5;
   }

   public void detectAndSendChanges() {
      for(int var1 = 0; var1 < this.inventorySlots.size(); ++var1) {
         ItemStack var2 = ((Slot)this.inventorySlots.get(var1)).getStack();
         ItemStack var3 = (ItemStack)this.inventoryItemStacks.get(var1);
         if (!ItemStack.areItemStacksEqual(var3, var2)) {
            var3 = var2 == null ? null : var2.copy();
            this.inventoryItemStacks.set(var1, var3);

            for(int var4 = 0; var4 < this.crafters.size(); ++var4) {
               ((ICrafting)this.crafters.get(var4)).sendSlotContents(this, var1, var3);
            }
         }
      }

   }

   public List getInventory() {
      ArrayList var1 = Lists.newArrayList();

      for(int var2 = 0; var2 < this.inventorySlots.size(); ++var2) {
         var1.add(((Slot)this.inventorySlots.get(var2)).getStack());
      }

      return var1;
   }

   public boolean enchantItem(EntityPlayer var1, int var2) {
      return false;
   }

   public static void computeStackSize(Set var0, int var1, ItemStack var2, int var3) {
      switch(var1) {
      case 0:
         var2.stackSize = MathHelper.floor_float((float)var2.stackSize / (float)var0.size());
         break;
      case 1:
         var2.stackSize = 1;
         break;
      case 2:
         var2.stackSize = var2.getItem().getItemStackLimit();
      }

      var2.stackSize += var3;
   }

   public void removeCraftingFromCrafters(ICrafting var1) {
      this.crafters.remove(var1);
   }

   protected Slot addSlotToContainer(Slot var1) {
      var1.slotNumber = this.inventorySlots.size();
      this.inventorySlots.add(var1);
      this.inventoryItemStacks.add((Object)null);
      return var1;
   }

   public boolean canMergeSlot(ItemStack var1, Slot var2) {
      return true;
   }

   public boolean getCanCraft(EntityPlayer var1) {
      return !this.playerList.contains(var1);
   }

   public static int func_94534_d(int var0, int var1) {
      return var0 & 3 | (var1 & 3) << 2;
   }

   public void updateProgressBar(int var1, int var2) {
   }

   public static int calcRedstone(TileEntity var0) {
      return var0 instanceof IInventory ? calcRedstoneFromInventory((IInventory)var0) : 0;
   }
}
