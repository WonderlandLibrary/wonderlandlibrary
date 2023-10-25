package fun.expensive.client.feature.impl.combat;

import com.mojang.realmsclient.gui.ChatFormatting;
import fun.rich.client.event.EventTarget;
import fun.rich.client.event.events.impl.player.EventUpdate;
import fun.rich.client.event.events.impl.render.EventRender2D;
import fun.expensive.client.feature.Feature;
import fun.expensive.client.feature.impl.FeatureCategory;

import fun.rich.client.ui.settings.impl.BooleanSetting;
import fun.rich.client.ui.settings.impl.NumberSetting;
import fun.rich.client.utils.math.TimerHelper;
import net.minecraft.client.gui.inventory.GuiInventory;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.entity.Entity;
import net.minecraft.entity.item.EntityEnderCrystal;
import net.minecraft.entity.item.EntityMinecartTNT;
import net.minecraft.entity.item.EntityTNTPrimed;
import net.minecraft.init.Items;
import net.minecraft.inventory.ClickType;
import net.minecraft.item.ItemAir;
import net.minecraft.item.ItemStack;
import net.minecraft.util.text.TextFormatting;

import java.util.ArrayList;
import java.util.List;

public class AutoTotem extends Feature {
    private final NumberSetting health;
    private final BooleanSetting countTotem;
    private final BooleanSetting checkCrystal;
    private final BooleanSetting checkTnt = new BooleanSetting("Check Tnt", true, () -> true);
    private final NumberSetting radiusTnt = new NumberSetting("Distance to Tnt", 6, 1, 8, 1, () -> true);
    private final BooleanSetting inventoryOnly;
    private final NumberSetting radiusCrystal;
    private final NumberSetting swapBackDelay;
    private final NumberSetting fallDistance;

    private final BooleanSetting switchBack = new BooleanSetting("Swap Back", "Возвращает прошлый предмет после сноса тотема.", true, () -> true);
    private final BooleanSetting checkFall;

    private final List<Integer> lastItem = new ArrayList<>();
    private final TimerHelper timerHelper = new TimerHelper();
    private boolean swap = false;

    public AutoTotem() {
        super("AutoTotem", "Автоматически берет в руку тотем при опредленном здоровье", FeatureCategory.Combat);
        health = new NumberSetting("Health Amount", 3.5f, 1.f, 20.f, 0.5F, () -> true);
        inventoryOnly = new BooleanSetting("Only Inventory", false, () -> true);
        swapBackDelay = new NumberSetting("Swap back delay", "Задержка между свапом прошлого предмета и тотема", 100, 10, 500, 5, switchBack::getBoolValue);
        countTotem = new BooleanSetting("Count Totem", true, () -> true);
        checkFall = new BooleanSetting("Check Fall", true, () -> true);
        fallDistance = new NumberSetting("Fall Distance", 15.0f, 5.0f, 125.0f, 5.0f, checkFall::getBoolValue);
        checkCrystal = new BooleanSetting("Check Crystal", true, () -> true);
        radiusCrystal = new NumberSetting("Distance to Crystal", 6, 1, 8, 1, checkCrystal::getBoolValue);
        addSettings(switchBack, swapBackDelay, health, inventoryOnly, countTotem, checkFall, fallDistance, checkCrystal, radiusCrystal, checkTnt, radiusTnt);
    }

    private int fountTotemCount() {
        int count = 0;
        for (int i = 0; i < mc.player.inventory.getSizeInventory(); i++) {
            ItemStack stack = mc.player.inventory.getStackInSlot(i);
            if (stack.getItem() == Items.Totem) {
                count++;
            }
        }
        return count;
    }


    @EventTarget
    public void onRender2D(EventRender2D event) {
        if (fountTotemCount() > 0 && countTotem.getBoolValue()) {
            mc.rubik_18.drawStringWithShadow(fountTotemCount() + "", (event.getResolution().getScaledWidth() / 2f + 19), (event.getResolution().getScaledHeight() / 2f), -1);
            for (int i = 0; i < mc.player.inventory.getSizeInventory(); i++) {
                ItemStack stack = mc.player.inventory.getStackInSlot(i);
                if (stack.getItem() == Items.Totem) {
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
        if (inventoryOnly.getBoolValue() && !(mc.currentScreen instanceof GuiInventory)) {
            return;
        }
        int tIndex = -1;
        int totemCount = 0;

        for (int i = 0; i < 45; i++) {
            if (mc.player.inventory.getStackInSlot(i).getItem() == Items.Totem && tIndex == -1) {
                tIndex = i;
            }
            if (mc.player.inventory.getStackInSlot(i).getItem() == Items.Totem) {
                totemCount++;
            }
        }

        if ((mc.player.getHealth() < health.getNumberValue() || checkCrystal() || checkTNT() || checkFall(fallDistance.getNumberValue())) && totemCount != 0 && tIndex != -1) {
            if (mc.player.getHeldItemOffhand().getItem() != Items.Totem) {
                mc.playerController.windowClick(0, tIndex < 9 ? tIndex + 36 : tIndex, 1, ClickType.PICKUP, mc.player);
                mc.playerController.windowClick(0, 45, 0, ClickType.PICKUP, mc.player);
                mc.playerController.windowClick(0, tIndex < 9 ? tIndex + 36 : tIndex, 0, ClickType.PICKUP, mc.player);
                swap = true;
                lastItem.add(tIndex);
            }
        } else if (switchBack.getBoolValue() && (swap || totemCount == 0) && lastItem.size() > 0) {
            if (!(mc.player.inventory.getStackInSlot(lastItem.get(0)).getItem() instanceof ItemAir)) {
                if (timerHelper.hasReached(swapBackDelay.getNumberValue())) {

                    mc.playerController.windowClick(0, lastItem.get(0) < 9 ? lastItem.get(0) + 36 : lastItem.get(0), 0, ClickType.PICKUP, mc.player);

                    mc.playerController.windowClick(0, 45, 0, ClickType.PICKUP, mc.player);
                    mc.playerController.windowClick(0, lastItem.get(0) < 9 ? lastItem.get(0) + 36 : lastItem.get(0), 0, ClickType.PICKUP, mc.player);
                    timerHelper.reset();

                }
            }
            swap = false;
            lastItem.clear();
        }
    }

    private boolean checkFall(float fallDist) {
        if (!checkFall.getBoolValue()) {
            return false;
        }
        return mc.player.fallDistance > fallDist;
    }

    private boolean checkTNT() {
        if (!checkTnt.getBoolValue()) {
            return false;
        }
        for (Entity entity : AutoTotem.mc.world.loadedEntityList) {
            if (entity instanceof EntityTNTPrimed && AutoTotem.mc.player.getDistanceToEntity(entity) <= radiusTnt.getNumberValue()) {
                return true;
            }
            if (!(entity instanceof EntityMinecartTNT) || !(AutoTotem.mc.player.getDistanceToEntity(entity) <= radiusTnt.getNumberValue()))
                continue;
            return true;
        }
        return false;
    }

    private boolean checkCrystal() {
        if (!checkCrystal.getBoolValue()) {
            return false;
        }
        for (Entity entity : AutoTotem.mc.world.loadedEntityList) {
            if (!(entity instanceof EntityEnderCrystal) || !(AutoTotem.mc.player.getDistanceToEntity(entity) <= radiusCrystal.getNumberValue()))
                continue;
            return true;
        }
        return false;
    }
}