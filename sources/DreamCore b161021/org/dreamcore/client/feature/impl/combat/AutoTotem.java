package org.dreamcore.client.feature.impl.combat;

import net.minecraft.client.gui.inventory.GuiInventory;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.entity.Entity;
import net.minecraft.entity.item.EntityEnderCrystal;
import net.minecraft.init.Items;
import net.minecraft.inventory.ClickType;
import net.minecraft.item.ItemStack;
import org.dreamcore.client.event.EventTarget;
import org.dreamcore.client.event.events.impl.player.EventUpdate;
import org.dreamcore.client.event.events.impl.render.EventRender2D;
import org.dreamcore.client.feature.Feature;
import org.dreamcore.client.feature.impl.Type;
import org.dreamcore.client.helpers.player.InventoryHelper;
import org.dreamcore.client.helpers.player.MovementHelper;
import org.dreamcore.client.settings.impl.BooleanSetting;
import org.dreamcore.client.settings.impl.NumberSetting;

public class AutoTotem extends Feature {

    public static BooleanSetting definiteHealth;
    public static NumberSetting health;
    public static BooleanSetting countTotem;
    public static BooleanSetting checkCrystal;
    public static BooleanSetting inventoryOnly;
    public static BooleanSetting noMoving;
    public static NumberSetting radiusCrystal;

    public AutoTotem() {
        super("AutoTotem", "Автоматически берет в руку тотем при опредленном здоровье", Type.Combat);
        definiteHealth = new BooleanSetting("Definite Health", false, () -> true);
        health = new NumberSetting("Health Amount", 10, 1, 20, 0.5F, () -> definiteHealth.getBoolValue());
        inventoryOnly = new BooleanSetting("Only Inventory", false, () -> true);
        noMoving = new BooleanSetting("No Moving Swap", false, () -> true);
        countTotem = new BooleanSetting("Count Totem", true, () -> true);
        checkCrystal = new BooleanSetting("Check Crystal", true, () -> true);
        radiusCrystal = new NumberSetting("Distance to Crystal", 6, 1, 8, 1, () -> checkCrystal.getBoolValue());
        addSettings(definiteHealth, health, inventoryOnly, noMoving, countTotem, checkCrystal, radiusCrystal);
    }

    private int fountTotemCount() {
        int count = 0;
        for (int i = 0; i < mc.player.inventory.getSizeInventory(); i++) {
            ItemStack stack = mc.player.inventory.getStackInSlot(i);
            if (stack.getItem() == Items.TOTEM_OF_UNDYING) {
                count++;
            }
        }
        return count;
    }

    private boolean checkCrystal() {
        for (Entity entity : mc.world.loadedEntityList) {
            if ((entity instanceof EntityEnderCrystal && mc.player.getDistanceToEntity(entity) <= radiusCrystal.getNumberValue())) {
                return true;
            }
        }
        return false;
    }

    @EventTarget
    public void onRender2D(EventRender2D event) {
        if (fountTotemCount() > 0 && countTotem.getBoolValue()) {
            mc.sfuiFontRender.drawStringWithShadow(fountTotemCount() + "", (event.getResolution().getScaledWidth() / 2f + 19), (event.getResolution().getScaledHeight() / 2f), -1);
            for (int i = 0; i < mc.player.inventory.getSizeInventory(); i++) {
                ItemStack stack = mc.player.inventory.getStackInSlot(i);
                if (stack.getItem() == Items.TOTEM_OF_UNDYING) {
                    GlStateManager.pushMatrix();
                    GlStateManager.disableBlend();
                    mc.getRenderItem().renderItemAndEffectIntoGUI(stack, event.getResolution().getScaledWidth() / 2 + 4, event.getResolution().getScaledHeight() / 2 - 7);
                    GlStateManager.popMatrix();
                }
            }
        }
    }

    @EventTarget
    public void onUpdate(EventUpdate event) {
        if (noMoving.getBoolValue() && MovementHelper.isMoving()) {
            return;
        }
        if (inventoryOnly.getBoolValue() && !(mc.currentScreen instanceof GuiInventory)) {
            return;
        }
        if (definiteHealth.getBoolValue() && mc.player.getHealth() > health.getNumberValue()) {
            return;
        }
        if (mc.player.getHeldItemOffhand().getItem() != Items.TOTEM_OF_UNDYING && InventoryHelper.getTotemAtHotbar() != -1) {
            mc.playerController.windowClick(0, InventoryHelper.getTotemAtHotbar(), 1, ClickType.PICKUP, mc.player);
            mc.playerController.windowClick(0, 45, 1, ClickType.PICKUP, mc.player);
        }
        if (checkCrystal() && checkCrystal.getBoolValue()) {
            if (mc.player.getHeldItemOffhand().getItem() != Items.TOTEM_OF_UNDYING && InventoryHelper.getTotemAtHotbar() != -1) {
                mc.playerController.windowClick(0, InventoryHelper.getTotemAtHotbar(), 1, ClickType.PICKUP, mc.player);
                mc.playerController.windowClick(0, 45, 1, ClickType.PICKUP, mc.player);
            }
        }
    }
}
