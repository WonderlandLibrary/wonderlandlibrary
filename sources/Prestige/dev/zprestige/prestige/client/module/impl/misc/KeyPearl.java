/*
https://discord.gg/vzXzFpv2gk
*/
package dev.zprestige.prestige.client.module.impl.misc;

import dev.zprestige.prestige.client.event.EventListener;
import dev.zprestige.prestige.client.event.impl.Render2DEvent;
import dev.zprestige.prestige.client.module.Category;
import dev.zprestige.prestige.client.module.Module;
import dev.zprestige.prestige.client.setting.impl.DragSetting;
import dev.zprestige.prestige.client.setting.impl.IntSetting;
import dev.zprestige.prestige.client.util.impl.InventoryUtil;
import dev.zprestige.prestige.client.util.impl.PacketUtil;
import dev.zprestige.prestige.client.util.impl.TimerUtil;
import net.minecraft.item.Items;
import net.minecraft.network.packet.c2s.play.PlayerInteractItemC2SPacket;
import net.minecraft.util.Hand;

public class KeyPearl extends Module {

    public DragSetting delay;
    public IntSetting switchSlot;
    public TimerUtil timer;
    public boolean throwPearl;

    public KeyPearl() {
        super("Key Pearl", Category.Misc, "Swaps to pearl, throws it and then swaps back whenever enabled");
        delay = setting("Delay", 10, 30, 0, 50).description("Delay between each action");
        switchSlot = setting("Switch Back Slot", 1, 1, 9).description("Slot to switch back to after throwing pearl");
        timer = new TimerUtil();
    }

    @Override
    public void onEnable() {
        this.throwPearl = false;
        this.reset();
    }

    @EventListener
    public void event(Render2DEvent event) {
        if (getMc().currentScreen != null || !getMc().isWindowFocused() || !timer.delay(delay)) {
            return;
        }
        if (throwPearl) {
            InventoryUtil.INSTANCE.setCurrentSlot(switchSlot.getObject() - 1);
            toggle();
            return;
        }
        if (getMc().player != null && getMc().player.getMainHandStack().getItem() != Items.ENDER_PEARL) {
            int slot = getPearl();
            if (slot == -1) {
                toggle();
                return;
            }
            InventoryUtil.INSTANCE.setCurrentSlot(slot);
            reset();
            return;
        }
        PacketUtil.INSTANCE.sendPacket(new PlayerInteractItemC2SPacket(Hand.MAIN_HAND, 0));
        getMc().player.swingHand(Hand.MAIN_HAND);
        throwPearl = true;
        reset();
    }

    private int getPearl() {
        for (int i = 0; i < 9; i++) {
            if (getMc().player.getInventory().getStack(i).getItem() == Items.ENDER_PEARL) {
                return i;
            }
        }
        return -1;
    }

    public void reset() {
        this.delay.setValue();
        this.timer.reset();
    }
}