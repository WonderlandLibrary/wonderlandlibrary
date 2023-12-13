package team.massacre.utils;

import java.util.Arrays;
import java.util.List;
import net.minecraft.block.Block;
import net.minecraft.client.Minecraft;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.MathHelper;
import net.minecraft.util.Vec3;

public class RapeUtils {
   private static List<Block> invalidBlocks;

   public static boolean canIItemBePlaced(Item item) {
      if (Item.getIdFromItem(item) == 116) {
         return false;
      } else if (Item.getIdFromItem(item) == 30) {
         return false;
      } else if (Item.getIdFromItem(item) == 31) {
         return false;
      } else if (Item.getIdFromItem(item) == 175) {
         return false;
      } else if (Item.getIdFromItem(item) == 28) {
         return false;
      } else if (Item.getIdFromItem(item) == 27) {
         return false;
      } else if (Item.getIdFromItem(item) == 66) {
         return false;
      } else if (Item.getIdFromItem(item) == 157) {
         return false;
      } else if (Item.getIdFromItem(item) == 31) {
         return false;
      } else if (Item.getIdFromItem(item) == 6) {
         return false;
      } else if (Item.getIdFromItem(item) == 31) {
         return false;
      } else if (Item.getIdFromItem(item) == 32) {
         return false;
      } else if (Item.getIdFromItem(item) == 140) {
         return false;
      } else if (Item.getIdFromItem(item) == 390) {
         return false;
      } else if (Item.getIdFromItem(item) == 37) {
         return false;
      } else if (Item.getIdFromItem(item) == 38) {
         return false;
      } else if (Item.getIdFromItem(item) == 39) {
         return false;
      } else if (Item.getIdFromItem(item) == 40) {
         return false;
      } else if (Item.getIdFromItem(item) == 69) {
         return false;
      } else if (Item.getIdFromItem(item) == 50) {
         return false;
      } else if (Item.getIdFromItem(item) == 75) {
         return false;
      } else if (Item.getIdFromItem(item) == 76) {
         return false;
      } else if (Item.getIdFromItem(item) == 54) {
         return false;
      } else if (Item.getIdFromItem(item) == 130) {
         return false;
      } else if (Item.getIdFromItem(item) == 146) {
         return false;
      } else if (Item.getIdFromItem(item) == 342) {
         return false;
      } else if (Item.getIdFromItem(item) == 12) {
         return false;
      } else if (Item.getIdFromItem(item) == 77) {
         return false;
      } else if (Item.getIdFromItem(item) == 143) {
         return false;
      } else {
         return Item.getIdFromItem(item) != 46;
      }
   }

   public static int getBlockSlot() {
      for(int i = 36; i < 45; ++i) {
         if (Minecraft.getMinecraft().thePlayer.inventoryContainer.getSlot(i).getStack() != null && Minecraft.getMinecraft().thePlayer.inventoryContainer.getSlot(i).getStack().getItem() instanceof ItemBlock && !invalidBlocks.contains(((ItemBlock)Minecraft.getMinecraft().thePlayer.inventoryContainer.getSlot(i).getStack().getItem()).getBlock())) {
            return i - 36;
         }
      }

      return -1;
   }

   public static boolean canItemBePlaced(ItemStack item) {
      item.getItem();
      if (Item.getIdFromItem(item.getItem()) == 116) {
         return false;
      } else {
         item.getItem();
         if (Item.getIdFromItem(item.getItem()) == 30) {
            return false;
         } else {
            item.getItem();
            if (Item.getIdFromItem(item.getItem()) == 31) {
               return false;
            } else {
               item.getItem();
               if (Item.getIdFromItem(item.getItem()) == 175) {
                  return false;
               } else {
                  item.getItem();
                  if (Item.getIdFromItem(item.getItem()) == 28) {
                     return false;
                  } else {
                     item.getItem();
                     if (Item.getIdFromItem(item.getItem()) == 27) {
                        return false;
                     } else {
                        item.getItem();
                        if (Item.getIdFromItem(item.getItem()) == 66) {
                           return false;
                        } else {
                           item.getItem();
                           if (Item.getIdFromItem(item.getItem()) == 157) {
                              return false;
                           } else {
                              item.getItem();
                              if (Item.getIdFromItem(item.getItem()) == 31) {
                                 return false;
                              } else {
                                 item.getItem();
                                 if (Item.getIdFromItem(item.getItem()) == 6) {
                                    return false;
                                 } else {
                                    item.getItem();
                                    if (Item.getIdFromItem(item.getItem()) == 31) {
                                       return false;
                                    } else {
                                       item.getItem();
                                       if (Item.getIdFromItem(item.getItem()) == 32) {
                                          return false;
                                       } else {
                                          item.getItem();
                                          if (Item.getIdFromItem(item.getItem()) == 140) {
                                             return false;
                                          } else {
                                             item.getItem();
                                             if (Item.getIdFromItem(item.getItem()) == 390) {
                                                return false;
                                             } else {
                                                item.getItem();
                                                if (Item.getIdFromItem(item.getItem()) == 37) {
                                                   return false;
                                                } else {
                                                   item.getItem();
                                                   if (Item.getIdFromItem(item.getItem()) == 38) {
                                                      return false;
                                                   } else {
                                                      item.getItem();
                                                      if (Item.getIdFromItem(item.getItem()) == 39) {
                                                         return false;
                                                      } else {
                                                         item.getItem();
                                                         if (Item.getIdFromItem(item.getItem()) == 40) {
                                                            return false;
                                                         } else {
                                                            item.getItem();
                                                            if (Item.getIdFromItem(item.getItem()) == 69) {
                                                               return false;
                                                            } else {
                                                               item.getItem();
                                                               if (Item.getIdFromItem(item.getItem()) == 50) {
                                                                  return false;
                                                               } else {
                                                                  item.getItem();
                                                                  if (Item.getIdFromItem(item.getItem()) == 75) {
                                                                     return false;
                                                                  } else {
                                                                     item.getItem();
                                                                     if (Item.getIdFromItem(item.getItem()) == 76) {
                                                                        return false;
                                                                     } else {
                                                                        item.getItem();
                                                                        if (Item.getIdFromItem(item.getItem()) == 54) {
                                                                           return false;
                                                                        } else {
                                                                           item.getItem();
                                                                           if (Item.getIdFromItem(item.getItem()) == 130) {
                                                                              return false;
                                                                           } else {
                                                                              item.getItem();
                                                                              if (Item.getIdFromItem(item.getItem()) == 146) {
                                                                                 return false;
                                                                              } else {
                                                                                 item.getItem();
                                                                                 if (Item.getIdFromItem(item.getItem()) == 342) {
                                                                                    return false;
                                                                                 } else {
                                                                                    item.getItem();
                                                                                    if (Item.getIdFromItem(item.getItem()) == 12) {
                                                                                       return false;
                                                                                    } else {
                                                                                       item.getItem();
                                                                                       if (Item.getIdFromItem(item.getItem()) == 77) {
                                                                                          return false;
                                                                                       } else {
                                                                                          item.getItem();
                                                                                          if (Item.getIdFromItem(item.getItem()) == 143) {
                                                                                             return false;
                                                                                          } else {
                                                                                             item.getItem();
                                                                                             if (Item.getIdFromItem(item.getItem()) == 46) {
                                                                                                return false;
                                                                                             } else {
                                                                                                item.getItem();
                                                                                                return Item.getIdFromItem(item.getItem()) != 145;
                                                                                             }
                                                                                          }
                                                                                       }
                                                                                    }
                                                                                 }
                                                                              }
                                                                           }
                                                                        }
                                                                     }
                                                                  }
                                                               }
                                                            }
                                                         }
                                                      }
                                                   }
                                                }
                                             }
                                          }
                                       }
                                    }
                                 }
                              }
                           }
                        }
                     }
                  }
               }
            }
         }
      }
   }

   public static int findBlockSlot() {
      int slot = -1;
      int highestStack = -1;

      for(int i = 36; i < 45; ++i) {
         ItemStack stack = Minecraft.getMinecraft().thePlayer.inventoryContainer.getSlot(i).getStack();
         if (stack != null && stack.getItem() instanceof ItemBlock && canIItemBePlaced(stack.getItem()) && stack.stackSize > 0 && stack.stackSize > highestStack) {
            slot = i;
            highestStack = stack.stackSize;
         }
      }

      return slot;
   }

   public static float[] getRotations(BlockPos block, EnumFacing face) {
      double x = (double)block.getX() + 0.5D - Minecraft.getMinecraft().thePlayer.posX + (double)face.getFrontOffsetX() / 2.0D;
      double z = (double)block.getZ() + 0.5D - Minecraft.getMinecraft().thePlayer.posZ + (double)face.getFrontOffsetZ() / 2.0D;
      double y = (double)block.getY() + 0.5D;
      double d1 = Minecraft.getMinecraft().thePlayer.posY + (double)Minecraft.getMinecraft().thePlayer.getEyeHeight() - y;
      double d2 = (double)MathHelper.sqrt_double(x * x + z * z);
      float yaw = (float)(Math.atan2(z, x) * 180.0D / 3.141592653589793D) - 90.0F;
      float pitch = (float)(Math.atan2(d1, d2) * 180.0D / 3.141592653589793D);
      if (yaw < 0.0F) {
         yaw += 360.0F;
      }

      return new float[]{yaw, pitch};
   }

   public static int grabBlockSlot() {
      int slot = -1;
      int highestStack = -1;
      boolean didGetHotbar = false;

      int i;
      for(i = 0; i < 36; ++i) {
         ItemStack itemStack = Minecraft.getMinecraft().thePlayer.inventory.mainInventory[i];
         if (itemStack != null && itemStack.getItem() instanceof ItemBlock && canItemBePlaced(itemStack) && itemStack.stackSize > 0) {
            if (Minecraft.getMinecraft().thePlayer.inventory.mainInventory[i].stackSize > highestStack && i < 9) {
               highestStack = Minecraft.getMinecraft().thePlayer.inventory.mainInventory[i].stackSize;
               slot = i;
               if (i == getLastHotbarSlot()) {
                  didGetHotbar = true;
               }
            }

            if (i > 8 && !didGetHotbar) {
               int hotbarNum = getFreeHotbarSlot();
               if (hotbarNum != -1 && Minecraft.getMinecraft().thePlayer.inventory.mainInventory[i].stackSize > highestStack) {
                  highestStack = Minecraft.getMinecraft().thePlayer.inventory.mainInventory[i].stackSize;
                  slot = i;
               }
            }
         }
      }

      if (slot > 8) {
         i = getFreeHotbarSlot();
         if (i == -1) {
            return -1;
         }

         Minecraft.getMinecraft().playerController.windowClick(Minecraft.getMinecraft().thePlayer.inventoryContainer.windowId, slot, i, 2, Minecraft.getMinecraft().thePlayer);
      }

      return slot;
   }

   public static int getLastHotbarSlot() {
      int hotbarNum = -1;

      for(int k = 0; k < 9; ++k) {
         ItemStack itemStack = Minecraft.getMinecraft().thePlayer.inventory.mainInventory[k];
         if (itemStack != null && itemStack.getItem() instanceof ItemBlock && canItemBePlaced(itemStack) && itemStack.stackSize > 1) {
            hotbarNum = k;
         }
      }

      return hotbarNum;
   }

   public static int getFreeHotbarSlot() {
      int hotbarNum = -1;

      for(int k = 0; k < 9; ++k) {
         if (Minecraft.getMinecraft().thePlayer.inventory.mainInventory[k] == null) {
            hotbarNum = k;
         } else {
            hotbarNum = 7;
         }
      }

      return hotbarNum;
   }

   private Vec3 grabPosition(BlockPos position, EnumFacing facing) {
      Vec3 offset = new Vec3((double)facing.getDirectionVec().getX() / 2.0D, (double)facing.getDirectionVec().getY() / 2.0D - 5.0D, (double)facing.getDirectionVec().getZ() / 2.0D);
      Vec3 point = new Vec3((double)position.getX() + 0.5D, (double)position.getY() + 0.5D, (double)position.getZ() + 0.5D);
      return point.add(offset);
   }

   public static float clampRotation() {
      float rotationYaw = Minecraft.getMinecraft().thePlayer.rotationYaw;
      float n = 1.0F;
      if (Minecraft.getMinecraft().thePlayer.movementInput.moveForward < 0.0F) {
         rotationYaw += 180.0F;
         n = -0.5F;
      } else if (Minecraft.getMinecraft().thePlayer.movementInput.moveForward > 0.0F) {
         n = 0.5F;
      }

      if (Minecraft.getMinecraft().thePlayer.movementInput.moveStrafe > 0.0F) {
         rotationYaw -= 90.0F * n;
      }

      if (Minecraft.getMinecraft().thePlayer.movementInput.moveStrafe < 0.0F) {
         rotationYaw += 90.0F * n;
      }

      return rotationYaw * 0.017453292F;
   }

   static {
      invalidBlocks = Arrays.asList(Blocks.enchanting_table, Blocks.carpet, Blocks.glass_pane, Blocks.stained_glass_pane, Blocks.iron_bars, Blocks.air, Blocks.water, Blocks.flowing_water, Blocks.lava, Blocks.flowing_lava, Blocks.snow_layer, Blocks.chest, Blocks.torch, Blocks.anvil, Blocks.trapped_chest, Blocks.noteblock, Blocks.jukebox, Blocks.wooden_pressure_plate, Blocks.stone_pressure_plate, Blocks.light_weighted_pressure_plate, Blocks.heavy_weighted_pressure_plate, Blocks.stone_button, Blocks.wooden_button, Blocks.lever, Blocks.crafting_table, Blocks.furnace, Blocks.stone_slab, Blocks.wooden_slab, Blocks.stone_slab2);
   }
}
