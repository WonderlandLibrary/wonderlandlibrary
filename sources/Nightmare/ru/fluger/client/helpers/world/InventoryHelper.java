// 
// Decompiled by Procyon v0.5.36
// 

package ru.fluger.client.helpers.world;

import java.util.Objects;
import java.util.Iterator;
import ru.fluger.client.helpers.Helper;

public class InventoryHelper implements Helper
{
    public static int getInventoryItemSlot(final ain item, final boolean hotbar) {
        int n;
        for (int i = n = (hotbar ? 0 : 9); i < 39; ++i) {
            final aip stack = InventoryHelper.mc.h.bv.a(i);
            if (!stack.b() && stack.c() == item) {
                return (i < 9) ? (i + 36) : i;
            }
        }
        return -1;
    }
    
    public static void disabler(final int elytra) {
        InventoryHelper.mc.c.a(0, elytra, 0, afw.a, InventoryHelper.mc.h);
        InventoryHelper.mc.c.a(0, 6, 0, afw.a, InventoryHelper.mc.h);
        InventoryHelper.mc.h.d.a(new lq(InventoryHelper.mc.h, lq.a.i));
        InventoryHelper.mc.h.d.a(new lq(InventoryHelper.mc.h, lq.a.i));
        InventoryHelper.mc.c.a(0, 6, 0, afw.a, InventoryHelper.mc.h);
        InventoryHelper.mc.c.a(0, elytra, 0, afw.a, InventoryHelper.mc.h);
    }
    
    public static int getSlotWithElytra() {
        for (int i = 0; i < 45; ++i) {
            final aip itemStack = InventoryHelper.mc.h.bx.a(i).d();
            if (itemStack.c() == air.cS) {
                return (i < 9) ? (i + 36) : i;
            }
        }
        return -1;
    }
    
    public static void swapElytraToChestplate() {
        for (final aip stack : InventoryHelper.mc.h.bv.b) {
            if (stack.c() == air.cS) {
                int eIndex = -1;
                for (int i = 0; i < 45; ++i) {
                    if (InventoryHelper.mc.h.bv.a(i).c() == air.ag || InventoryHelper.mc.h.bv.a(i).c() == air.U || InventoryHelper.mc.h.bv.a(i).c() == air.ak) {
                        eIndex = i;
                    }
                }
                final int slot = (eIndex < 9) ? (eIndex + 36) : eIndex;
                if (eIndex == -1) {
                    continue;
                }
                InventoryHelper.mc.c.a(0, slot, 0, afw.a, InventoryHelper.mc.h);
                InventoryHelper.mc.c.a(6, slot, 1, afw.a, InventoryHelper.mc.h);
                InventoryHelper.mc.c.a(0, slot, 0, afw.a, InventoryHelper.mc.h);
            }
        }
    }
    
    public static int getAxe() {
        for (int i = 0; i < 9; ++i) {
            final aip itemStack = InventoryHelper.mc.h.bv.a(i);
            if (itemStack.c() instanceof agy) {
                return i;
            }
        }
        return 1;
    }
    
    public static int getCount(final ain item, final boolean offhand, final boolean hotbar) {
        int count = 0;
        if (offhand && InventoryHelper.mc.h.cp().c() == item) {
            ++count;
        }
        int n;
        for (int i = n = (hotbar ? 0 : 9); i < 39; ++i) {
            final aip stack = InventoryHelper.mc.h.bv.a((i < 9) ? (i + 36) : i);
            if (stack.c() == item) {
                ++count;
            }
        }
        return count;
    }
    
    public static void switchTo(final int slot, final boolean silent) {
        if (silent) {
            InventoryHelper.mc.h.d.a(new lv(slot));
        }
        else {
            InventoryHelper.mc.h.bv.d = slot;
        }
        InventoryHelper.mc.c.e();
    }
    
    public static boolean doesHotbarHaveAxe() {
        for (int i = 0; i < 9; ++i) {
            InventoryHelper.mc.h.bv.a(i);
            if (InventoryHelper.mc.h.bv.a(i).c() instanceof agy) {
                return true;
            }
        }
        return false;
    }
    
    public static int getSlot(final int slot) {
        if (bib.z().h.bv.a(slot).c() == ain.c(slot)) {
            return slot;
        }
        return 0;
    }
    
    public static aip getSlotStack(final int slot) {
        return InventoryHelper.mc.h.bv.a(slot);
    }
    
    public static int getSlotWithDuels() {
        for (int i = 0; i < 45; ++i) {
            if (bib.z().h.by.a(i).d().c() == air.x) {
                return i;
            }
        }
        return 0;
    }
    
    public static int getSlotBow() {
        for (int i = 0; i < 9; ++i) {
            if (bib.z().h.by.a(i).d().c() == air.g) {
                return i;
            }
        }
        return 0;
    }
    
    public static int getSlotWithFood() {
        for (int i = 0; i < 45; ++i) {
            if (bib.z().h.by.a(i).d().c() instanceof aij) {
                return i;
            }
        }
        return 0;
    }
    
    public static int getBlocksAtHotbar() {
        for (int i = 0; i < 45; ++i) {
            final aip itemStack = InventoryHelper.mc.h.bx.a(i).d();
            if (itemStack.c() instanceof ahb) {
                return i;
            }
        }
        return -1;
    }
    
    public static int getShieldAtHotbar() {
        for (int i = 0; i < 45; ++i) {
            final aip itemStack = InventoryHelper.mc.h.bx.a(i).d();
            if (itemStack.c() instanceof ajm) {
                return i;
            }
        }
        return -1;
    }
    
    public static int getEnderPearlAtHotbar() {
        for (int i = 0; i < 45; ++i) {
            final aip itemStack = InventoryHelper.mc.h.bx.a(i).d();
            if (itemStack.c() == air.bC) {
                return i;
            }
        }
        return -1;
    }
    
    public static int getSlotWithPot() {
        for (int i = 0; i < 9; ++i) {
            bib.z().h.bv.a(i);
            if (bib.z().h.bv.a(i).c() == air.bI) {
                return i;
            }
        }
        return 0;
    }
    
    public static int getCustomSlot(final aip stack) {
        for (int i = 0; i < 45; ++i) {
            bib.z().h.bv.a(i);
            if (bib.z().h.bv.a(i).r().equalsIgnoreCase(stack.r())) {
                return i;
            }
        }
        return 0;
    }
    
    public static boolean hotbarHasAir(final aed entity) {
        for (int i = 9; i < 45; ++i) {
            final aip itemStack = entity.bx.a(i).d();
            if (itemStack == null) {
                return true;
            }
        }
        return false;
    }
    
    public static boolean inventoryHasPotion() {
        for (int i = 0; i < 45; ++i) {
            final aip itemStack = InventoryHelper.mc.h.bx.a(i).d();
            if (itemStack.c() == air.bI) {
                return true;
            }
        }
        return false;
    }
    
    public static boolean inventoryHasAir() {
        for (int index = 0; index < InventoryHelper.mc.h.bv.w_() - 1; ++index) {
            if (InventoryHelper.mc.h.bv.a(index).c() == air.a) {
                return true;
            }
        }
        return false;
    }
    
    public static boolean hotbarHasAir() {
        for (int i = 36; i < 45; ++i) {
            final aip itemStack = InventoryHelper.mc.h.bx.a(i).d();
            if (itemStack == null) {
                return true;
            }
        }
        return false;
    }
    
    public static aip getItemStackFromItem(final ain item) {
        if (InventoryHelper.mc.h == null) {
            return null;
        }
        for (int slot = 0; slot <= 9; ++slot) {
            if (InventoryHelper.mc.h.bv.a(slot).c() == item) {
                return InventoryHelper.mc.h.bv.a(slot);
            }
        }
        return null;
    }
    
    public static boolean doesHotbarHaveSword() {
        for (int i = 0; i < 9; ++i) {
            InventoryHelper.mc.h.bv.a(i);
            if (InventoryHelper.mc.h.bv.a(i).c() instanceof ajy) {
                return true;
            }
        }
        return false;
    }
    
    public static boolean doesHotbarHavePot() {
        for (int i = 0; i < 9; ++i) {
            if (InventoryHelper.mc.h.bv.a(i).c() == air.bI) {
                return true;
            }
        }
        return false;
    }
    
    public static boolean doesHotbarHaveBlock() {
        for (int i = 0; i < 9; ++i) {
            if (InventoryHelper.mc.h.bv.a(i).c() instanceof ahb) {
                return true;
            }
        }
        return false;
    }
    
    public static int findFood() {
        for (int i = 0; i < 9; ++i) {
            InventoryHelper.mc.h.bv.a(i);
            if (InventoryHelper.mc.h.bv.a(i).c() instanceof aij) {
                return i;
            }
        }
        return -1;
    }
    
    public static int findWaterBucket() {
        for (int i = 0; i < 9; ++i) {
            InventoryHelper.mc.h.bv.a(i);
            if (InventoryHelper.mc.h.bv.a(i).c() == air.aA) {
                return i;
            }
        }
        return -1;
    }
    
    public static int getSwordAtHotbar() {
        for (int i = 0; i < 9; ++i) {
            final aip itemStack = InventoryHelper.mc.h.bv.a(i);
            if (itemStack.c() instanceof ajy) {
                return i;
            }
        }
        return -1;
    }
    
    public static int getTotemAtHotbar() {
        for (int i = 0; i < 45; ++i) {
            final aip itemStack = InventoryHelper.mc.h.bx.a(i).d();
            if (itemStack.c() == air.Totem) {
                return i;
            }
        }
        return -1;
    }
    
    public static int getSlotWithAir() {
        for (int i = 9; i < 45; ++i) {
            final aip itemStack = InventoryHelper.mc.h.bx.a(i).d();
            if (itemStack.c() == air.a) {
                return i;
            }
        }
        return -1;
    }
    
    public static boolean isBestArmor(final aip stack, final int type) {
        final float prot = getProtection(stack);
        String strType = "";
        if (type == 1) {
            strType = "helmet";
        }
        else if (type == 2) {
            strType = "chestplate";
        }
        else if (type == 3) {
            strType = "leggings";
        }
        else if (type == 4) {
            strType = "boots";
        }
        if (!stack.a().contains(strType)) {
            return false;
        }
        for (int i = 5; i < 45; ++i) {
            final aip is;
            if (InventoryHelper.mc.h.bx.a(i).e() && getProtection(is = InventoryHelper.mc.h.bx.a(i).d()) > prot && is.a().contains(strType)) {
                return false;
            }
        }
        return true;
    }
    
    public static float getProtection(final aip stack) {
        float prot = 0.0f;
        if (stack.c() instanceof agv) {
            final agv armor = (agv)stack.c();
            prot += (float)(armor.d + (100 - armor.d) * alm.a(Objects.requireNonNull(alk.c(0)), stack) * 0.0075);
            prot += (float)(alm.a(Objects.requireNonNull(alk.c(3)), stack) / 100.0);
            prot += (float)(alm.a(Objects.requireNonNull(alk.c(1)), stack) / 100.0);
            prot += (float)(alm.a(Objects.requireNonNull(alk.c(7)), stack) / 100.0);
            prot += (float)(alm.a(Objects.requireNonNull(alk.c(34)), stack) / 50.0);
            prot += (float)(alm.a(Objects.requireNonNull(alk.c(4)), stack) / 100.0);
        }
        return prot;
    }
}
