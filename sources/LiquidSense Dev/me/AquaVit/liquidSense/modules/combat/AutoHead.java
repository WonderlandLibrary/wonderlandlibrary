package me.AquaVit.liquidSense.modules.combat;

import net.ccbluex.liquidbounce.event.EventTarget;
import net.ccbluex.liquidbounce.features.module.Module;
import net.ccbluex.liquidbounce.features.module.ModuleCategory;
import net.ccbluex.liquidbounce.features.module.ModuleInfo;
import net.ccbluex.liquidbounce.value.BoolValue;
import net.ccbluex.liquidbounce.value.IntegerValue;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.client.settings.KeyBinding;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.network.play.client.C08PacketPlayerBlockPlacement;
import net.minecraft.network.play.client.C09PacketHeldItemChange;
import net.minecraft.potion.Potion;
import net.ccbluex.liquidbounce.event.MotionEvent;
import net.ccbluex.liquidbounce.utils.timer.TimeUtils;
import net.ccbluex.liquidbounce.value.FloatValue;
import org.lwjgl.input.Mouse;

import java.util.Random;

@ModuleInfo(name = "AutoHead", description = "AutoHead", category = ModuleCategory.COMBAT)
public class AutoHead extends Module {
    private boolean eatingApple;
    private int switched = -1;
    public static boolean doingStuff = false;
    private final TimeUtils timer = new TimeUtils();
    private final BoolValue eatHeads = new BoolValue("EatHead", true);
    private final BoolValue eatApples = new BoolValue("EatApples", true);
    private final FloatValue health = new FloatValue("Health", 10.0F, 1.0F, 20.0F);
    private final IntegerValue delay = new IntegerValue("Delay", 750, 100, 2000);

    @Override
    public void onEnable() {
        this.eatingApple = doingStuff = false;
        switched = -1;
        timer.reset();
        super.onEnable();
    }
    @Override
    public void onDisable() {
        doingStuff = false;
        if (eatingApple) {
            repairItemPress();
            repairItemSwitch();
        }
        super.onDisable();
    }

    private void repairItemPress() {
        if (mc.gameSettings != null) {
            final KeyBinding keyBindUseItem = mc.gameSettings.keyBindUseItem;
            if (keyBindUseItem != null) keyBindUseItem.pressed = false;
        }
    }


    @EventTarget
    public void onUpdate(MotionEvent event) {
        if (mc.thePlayer == null) return;
        final InventoryPlayer inventory = mc.thePlayer.inventory;
        if (inventory == null) return;
        doingStuff = false;
        if (!Mouse.isButtonDown(0) && !Mouse.isButtonDown(1)) {
            final KeyBinding useItem = mc.gameSettings.keyBindUseItem;

            if (!this.timer.hasReached(delay.get())) {
                eatingApple = false;
                repairItemPress();
                repairItemSwitch();
                return;
            }

            if (mc.thePlayer.capabilities.isCreativeMode || mc.thePlayer.isPotionActive(Potion.regeneration) ||mc.thePlayer.getHealth() >= health.get()) {
                this.timer.reset();
                if (eatingApple) {
                    eatingApple = false;
                    repairItemPress();
                    repairItemSwitch();
                }
                return;
            }

            for (int i = 0; i < 2; i++) {
                final boolean doEatHeads = i != 0;

                if (doEatHeads) {
                    if (!this.eatHeads.get()) continue;
                } else {
                    if (!this.eatApples.get()) {
                        eatingApple = false;
                        repairItemPress();
                        repairItemSwitch();
                        continue;
                    }
                }

                int slot;

                if (doEatHeads) {
                    slot = this.getItemFromHotbar(397);
                } else {
                    slot = this.getItemFromHotbar(322);
                }

                if (slot == -1) continue;

                final int tempSlot = inventory.currentItem;

                doingStuff = true;
                if (doEatHeads) {
                    mc.thePlayer.sendQueue.addToSendQueue(new C09PacketHeldItemChange(slot));
                    mc.thePlayer.sendQueue.addToSendQueue(new C08PacketPlayerBlockPlacement(inventory.getCurrentItem()));
                    mc.thePlayer.sendQueue.addToSendQueue(new C09PacketHeldItemChange(tempSlot));
                    timer.reset();
                } else {
                    inventory.currentItem = slot;
                    useItem.pressed = true;
                    if (eatingApple) continue; // no message spam
                    eatingApple = true;
                    this.switched = tempSlot;
                }


            }
        }
    }

    private void repairItemSwitch() {
        final EntityPlayerSP p = mc.thePlayer;
        if (p == null) return;
        final InventoryPlayer inventory = p.inventory;
        if (inventory == null) return;
        int switched = this.switched;
        if (switched == -1) return;
        inventory.currentItem = switched;
        switched = -1;
        this.switched = switched;
    }

    private int getItemFromHotbar(final int id) {
        for (int i = 0; i < 9; i++) {
            if (mc.thePlayer.inventory.mainInventory[i] != null) {
                final ItemStack is = mc.thePlayer.inventory.mainInventory[i];
                final Item item = is.getItem();
                if (Item.getIdFromItem(item) == id) {
                    return i;
                }
            }
        }
        return -1;
    }

}
