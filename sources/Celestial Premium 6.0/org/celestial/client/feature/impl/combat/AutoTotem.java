/*
 * Decompiled with CFR 0.150.
 */
package org.celestial.client.feature.impl.combat;

import com.mojang.realmsclient.gui.ChatFormatting;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.entity.Entity;
import net.minecraft.entity.item.EntityEnderCrystal;
import net.minecraft.entity.item.EntityMinecartTNT;
import net.minecraft.entity.item.EntityTNTPrimed;
import net.minecraft.init.Items;
import net.minecraft.inventory.ClickType;
import net.minecraft.item.ItemStack;
import org.celestial.client.event.EventTarget;
import org.celestial.client.event.events.impl.player.EventUpdate;
import org.celestial.client.event.events.impl.render.EventRender2D;
import org.celestial.client.feature.Feature;
import org.celestial.client.feature.impl.Type;
import org.celestial.client.helpers.player.MovementHelper;
import org.celestial.client.settings.impl.BooleanSetting;
import org.celestial.client.settings.impl.NumberSetting;
import org.celestial.client.ui.notification.NotificationManager;
import org.celestial.client.ui.notification.NotificationType;

public class AutoTotem
extends Feature {
    public static NumberSetting health;
    public static BooleanSetting countTotem;
    public static BooleanSetting checkCrystal;
    public static NumberSetting radiusToCrystal;
    public static BooleanSetting checkTNT;
    public static NumberSetting radiusToTNT;
    public static BooleanSetting noMoving;
    public static BooleanSetting checkFall;
    public static NumberSetting fallDistance;
    public static BooleanSetting saveEnchTotems;
    public static BooleanSetting swapBack;
    private int totemCount;
    private boolean returnOld;

    public AutoTotem() {
        super("AutoTotem", "\u0410\u0432\u0442\u043e\u043c\u0430\u0442\u0438\u0447\u0435\u0441\u043a\u0438 \u0431\u0435\u0440\u0435\u0442 \u0432 \u0440\u0443\u043a\u0443 \u0442\u043e\u0442\u0435\u043c \u043f\u0440\u0438 \u043e\u043f\u0440\u0435\u0434\u043b\u0435\u043d\u043d\u043e\u043c \u0437\u0434\u043e\u0440\u043e\u0432\u044c\u0435", Type.Combat);
        health = new NumberSetting("Health Amount", 20.0f, 1.0f, 20.0f, 0.5f, () -> true);
        noMoving = new BooleanSetting("No Moving Swap", false, () -> true);
        countTotem = new BooleanSetting("Count Totem", false, () -> true);
        checkCrystal = new BooleanSetting("Check Crystal", false, () -> true);
        radiusToCrystal = new NumberSetting("Distance to Crystal", 6.0f, 1.0f, 8.0f, 1.0f, () -> checkCrystal.getCurrentValue());
        checkTNT = new BooleanSetting("Check TNT", false, () -> true);
        radiusToTNT = new NumberSetting("Distance to TNT", 6.0f, 1.0f, 8.0f, 1.0f, () -> checkTNT.getCurrentValue());
        checkFall = new BooleanSetting("Check Fall", false, () -> true);
        fallDistance = new NumberSetting("Fall Distance", 15.0f, 5.0f, 125.0f, 5.0f, () -> checkFall.getCurrentValue());
        swapBack = new BooleanSetting("Swap Back", true, () -> true);
        saveEnchTotems = new BooleanSetting("Save Enchanted Totems", true, () -> true);
        this.addSettings(health, noMoving, countTotem, swapBack, saveEnchTotems, checkCrystal, radiusToCrystal, checkTNT, radiusToTNT, checkFall, fallDistance);
    }

    @Override
    public void onDisable() {
        this.returnOld = false;
        super.onDisable();
    }

    private int fountTotemCountNoDonat() {
        int count = 0;
        for (int i = 0; i < AutoTotem.mc.player.inventory.getSizeInventory(); ++i) {
            ItemStack stack = AutoTotem.mc.player.inventory.getStackInSlot(i);
            if (stack.getItem() != Items.TOTEM_OF_UNDYING || stack.isItemEnchanted()) continue;
            ++count;
        }
        return count;
    }

    private int fountTotemCount() {
        int count = 0;
        for (int i = 0; i < AutoTotem.mc.player.inventory.getSizeInventory(); ++i) {
            ItemStack stack = AutoTotem.mc.player.inventory.getStackInSlot(i);
            if (stack.getItem() != Items.TOTEM_OF_UNDYING) continue;
            ++count;
        }
        return count;
    }

    private boolean checkCrystal() {
        if (!checkCrystal.getCurrentValue()) {
            return false;
        }
        for (Entity entity : AutoTotem.mc.world.loadedEntityList) {
            if (!(entity instanceof EntityEnderCrystal) || !(AutoTotem.mc.player.getDistanceToEntity(entity) <= radiusToCrystal.getCurrentValue())) continue;
            return true;
        }
        return false;
    }

    private boolean checkTNT() {
        if (!checkTNT.getCurrentValue()) {
            return false;
        }
        for (Entity entity : AutoTotem.mc.world.loadedEntityList) {
            if (entity instanceof EntityTNTPrimed && AutoTotem.mc.player.getDistanceToEntity(entity) <= radiusToTNT.getCurrentValue()) {
                return true;
            }
            if (!(entity instanceof EntityMinecartTNT) || !(AutoTotem.mc.player.getDistanceToEntity(entity) <= radiusToTNT.getCurrentValue())) continue;
            return true;
        }
        return false;
    }

    private boolean checkFall(float fallDist) {
        if (!checkFall.getCurrentValue()) {
            return false;
        }
        return AutoTotem.mc.player.fallDistance > fallDist;
    }

    @EventTarget
    public void onRender2D(EventRender2D event) {
        if (this.fountTotemCount() > 0 && countTotem.getCurrentValue()) {
            AutoTotem.mc.comfortaa.drawStringWithShadow(this.fountTotemCount() + "", (float)event.getResolution().getScaledWidth() / 2.0f + 19.0f, (float)event.getResolution().getScaledHeight() / 2.0f, -1);
            for (int i = 0; i < AutoTotem.mc.player.inventory.getSizeInventory(); ++i) {
                ItemStack stack = AutoTotem.mc.player.inventory.getStackInSlot(i);
                if (stack.getItem() != Items.TOTEM_OF_UNDYING) continue;
                GlStateManager.pushMatrix();
                GlStateManager.disableBlend();
                mc.getRenderItem().renderItemAndEffectIntoGUI(stack, event.getResolution().getScaledWidth() / 2 + 4, event.getResolution().getScaledHeight() / 2 - 7);
                GlStateManager.popMatrix();
            }
        }
    }

    @EventTarget
    public void onUpdate(EventUpdate event) {
        int i;
        int t;
        if (AutoTotem.mc.currentScreen instanceof GuiContainer) {
            return;
        }
        if (noMoving.getCurrentValue() && MovementHelper.isMoving()) {
            return;
        }
        if (!(!this.returnOld || AutoTotem.mc.player.getHealth() <= health.getCurrentValue() || this.checkTNT() || this.checkCrystal() || this.checkFall(fallDistance.getCurrentValue()))) {
            t = -1;
            for (i = 0; i < 45; ++i) {
                if (!AutoTotem.mc.player.inventory.getStackInSlot(i).isEmpty()) continue;
                t = i;
                break;
            }
            if (t == -1) {
                return;
            }
            if (swapBack.getCurrentValue()) {
                AutoTotem.mc.playerController.windowClick(0, t < 9 ? t + 36 : t, 0, ClickType.QUICK_MOVE, AutoTotem.mc.player);
                AutoTotem.mc.playerController.windowClick(0, 45, 0, ClickType.PICKUP, AutoTotem.mc.player);
            }
            AutoTotem.mc.playerController.windowClick(0, t < 9 ? t + 36 : t, 0, ClickType.PICKUP, AutoTotem.mc.player);
            this.returnOld = false;
        }
        this.totemCount = AutoTotem.mc.player.inventory.mainInventory.stream().filter(itemStack -> itemStack.getItem() == Items.TOTEM_OF_UNDYING).mapToInt(ItemStack::getCount).sum();
        if (AutoTotem.mc.player.getHeldItemOffhand().getItem() == Items.TOTEM_OF_UNDYING) {
            ++this.totemCount;
        } else if (AutoTotem.mc.player.inventory.getItemStack().isEmpty()) {
            if (this.totemCount == 0) {
                return;
            }
            t = -1;
            for (i = 0; i < 45; ++i) {
                if (AutoTotem.mc.player.inventory.getStackInSlot(i).getItem() != Items.TOTEM_OF_UNDYING) continue;
                if (AutoTotem.mc.player.inventory.getStackInSlot(i).isItemEnchanted() && this.fountTotemCountNoDonat() > 0 && saveEnchTotems.getCurrentValue()) {
                    t = -1;
                    continue;
                }
                t = i;
                break;
            }
            if (t == -1) {
                return;
            }
            if (AutoTotem.mc.player.getHealth() <= health.getCurrentValue() || this.checkTNT() || this.checkCrystal() || this.checkFall(fallDistance.getCurrentValue())) {
                AutoTotem.mc.playerController.windowClick(0, t < 9 ? t + 36 : t, 1, ClickType.PICKUP, AutoTotem.mc.player);
                AutoTotem.mc.playerController.windowClick(0, 45, 1, ClickType.PICKUP, AutoTotem.mc.player);
                this.returnOld = true;
                NotificationManager.publicity("AutoTotem", (Object)((Object)ChatFormatting.GREEN) + "Successfully " + (Object)((Object)ChatFormatting.WHITE) + " equipped totem!", 3, NotificationType.SUCCESS);
            }
        }
    }
}

