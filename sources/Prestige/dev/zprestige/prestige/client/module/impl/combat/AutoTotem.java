package dev.zprestige.prestige.client.module.impl.combat;

import dev.zprestige.prestige.api.mixin.IHandledScreen;
import dev.zprestige.prestige.client.event.EventListener;
import dev.zprestige.prestige.client.event.impl.PacketReceiveEvent;
import dev.zprestige.prestige.client.event.impl.Render2DEvent;
import dev.zprestige.prestige.client.module.Category;
import dev.zprestige.prestige.client.module.Module;
import dev.zprestige.prestige.client.setting.impl.BooleanSetting;
import dev.zprestige.prestige.client.setting.impl.DragSetting;
import dev.zprestige.prestige.client.setting.impl.IntSetting;
import dev.zprestige.prestige.client.setting.impl.MultipleSetting;
import dev.zprestige.prestige.client.ui.drawables.gui.screens.impl.AutoTotemScreen;
import dev.zprestige.prestige.client.util.impl.InventoryUtil;
import dev.zprestige.prestige.client.util.impl.RandomUtil;
import dev.zprestige.prestige.client.util.impl.TimerUtil;

import java.util.ArrayList;

import net.minecraft.client.gui.screen.ingame.InventoryScreen;
import net.minecraft.entity.Entity;
import net.minecraft.item.Items;
import net.minecraft.network.packet.Packet;
import net.minecraft.network.packet.s2c.play.EntityStatusS2CPacket;
import net.minecraft.screen.slot.Slot;
import net.minecraft.screen.slot.SlotActionType;

public class AutoTotem extends Module {

    public DragSetting delay;
    public MultipleSetting multipleSetting;
    public BooleanSetting legit;
    public IntSetting totemSlot;
    public TimerUtil timer;
    public boolean bruh;

    public AutoTotem() {
        super("Auto Totem", Category.Combat, "Automatically re-totems when popped");
        delay = setting("Delay", 30.0f, 50.0f, 0.0f, 300.0f).description("Delay between each action");
        multipleSetting = setting("Actions", new String[]{"Double Hand", "Open Inventory", "Switch", "Close Inventory"}, new Boolean[]{true, true, true, true}).description("Actions to perform");
        legit = setting("Legit", false).description("Switches ONLY when hovering over a totem.");
        totemSlot = setting("Totem Slot", 8, 1, 9).description("Slot to put the totem in");
        timer = new TimerUtil();
    }

    @EventListener
    public void event(Render2DEvent event) {
        if (!getMc().isWindowFocused() || !timer.delay(delay) || !bruh) {
            return;
        }
        if (InventoryUtil.INSTANCE.isHoldingItem(Items.SHIELD) && getMc().player.isUsingItem()) {
            return;
        }
        if (InventoryUtil.INSTANCE.findItemSlot(Items.TOTEM_OF_UNDYING, true).isEmpty()) {
            bruh = false;
            return;
        }
        boolean hasItem = InventoryUtil.INSTANCE.findItemInHotbar(Items.TOTEM_OF_UNDYING) == null;
        if (!hasItem && !InventoryUtil.INSTANCE.isHoldingOffhandItem(Items.TOTEM_OF_UNDYING) && multipleSetting.getValue("Double Hand")) {
            InventoryUtil.INSTANCE.setCurrentSlot(InventoryUtil.INSTANCE.findItemInHotbar(Items.TOTEM_OF_UNDYING));
            if (reset()) {
                return;
            }
        }
        if (!InventoryUtil.INSTANCE.findItemSlot(Items.TOTEM_OF_UNDYING, true).isEmpty() && getMc().currentScreen == null && !InventoryUtil.INSTANCE.isHoldingOffhandItem(Items.TOTEM_OF_UNDYING) && multipleSetting.getValue("Open Inventory")) {
            getMc().getTutorialManager().onInventoryOpened();
            if (multipleSetting.getValue("Switch")) {
                if (getMc().player != null) {
                    getMc().setScreen(new AutoTotemScreen(this, getMc().player));
                }
            }
            else {
                if (getMc().player != null) {
                    getMc().setScreen(new InventoryScreen(getMc().player));
                }
            }
            if (reset()) {
                return;
            }
        }
        if (getMc().currentScreen instanceof InventoryScreen || getMc().currentScreen instanceof AutoTotemScreen) {
            if (!InventoryUtil.INSTANCE.isHoldingOffhandItem(Items.TOTEM_OF_UNDYING) && multipleSetting.getValue("Switch")) {
                if (legit.getObject()) {
                    Slot focusedSlot = ((IHandledScreen)getMc().currentScreen).getFocusedSlot();
                    if (focusedSlot != null && focusedSlot.getStack().getItem() == Items.TOTEM_OF_UNDYING && InventoryUtil.INSTANCE.performSlotAction(focusedSlot.id, 40, SlotActionType.SWAP) && reset()) {
                        return;
                    }
                }
                else {
                    ArrayList<Integer> list = InventoryUtil.INSTANCE.findItemSlot(Items.TOTEM_OF_UNDYING, false);
                    if (!list.isEmpty() && InventoryUtil.INSTANCE.performSlotAction(list.get(RandomUtil.INSTANCE.randomInRange(0, list.size() - 1)), 40, SlotActionType.SWAP) && reset()) {
                        return;
                    }
                }
            }
            if (hasItem && multipleSetting.getValue("Switch")) {
                ArrayList<Integer> list = InventoryUtil.INSTANCE.findItemSlot(Items.TOTEM_OF_UNDYING, false);
                if (!list.isEmpty() && !legit.getObject() && InventoryUtil.INSTANCE.performSlotAction(list.get(RandomUtil.INSTANCE.randomInRange(0, list.size() - 1)), totemSlot.getObject() - 1, SlotActionType.SWAP) && reset()) {
                    return;
                } else if (((IHandledScreen)getMc().currentScreen).getFocusedSlot().getStack().getItem() == Items.TOTEM_OF_UNDYING && InventoryUtil.INSTANCE.performSlotAction(((IHandledScreen)getMc().currentScreen).getFocusedSlot().id, totemSlot.getObject(), SlotActionType.SWAP) && reset()) {
                    return;
                }
            }
            if (multipleSetting.getValue("Close Inventory") && !hasItem && InventoryUtil.INSTANCE.isHoldingOffhandItem(Items.TOTEM_OF_UNDYING)) {
                if (getMc().player != null) {
                    getMc().player.closeScreen();
                }
                if (reset()) {
                    return;
                }
            }
        }
        if (!timer.delay(150)) {
            bruh = false;
        }
    }

    @EventListener
    public void event(PacketReceiveEvent event) {
        if (this.getMc().world == null || this.getMc().player == null) {
            return;
        }
        if (event.getPacket() instanceof EntityStatusS2CPacket) {
            Packet packet = event.getPacket();
            Entity entity = ((EntityStatusS2CPacket)packet).getEntity(this.getMc().world);
            if (entity != null && entity == getMc().player && ((EntityStatusS2CPacket)packet).getStatus() == 35) {
                bruh = true;
                reset();
            }
        }
    }

    boolean reset() {
        delay.setValue(delay.getRandomValue());
        timer.reset();
        return true;
    }
}
