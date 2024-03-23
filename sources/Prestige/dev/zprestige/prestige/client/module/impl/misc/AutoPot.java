package dev.zprestige.prestige.client.module.impl.misc;

import dev.zprestige.prestige.client.event.EventListener;
import dev.zprestige.prestige.client.event.impl.TickEvent;
import dev.zprestige.prestige.client.module.Category;
import dev.zprestige.prestige.client.module.Module;
import dev.zprestige.prestige.client.setting.impl.FloatSetting;
import dev.zprestige.prestige.client.setting.impl.ModeSetting;
import dev.zprestige.prestige.client.util.impl.InventoryUtil;
import dev.zprestige.prestige.client.util.impl.RandomUtil;
import dev.zprestige.prestige.client.util.impl.TimerUtil;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.util.Hand;

public class AutoPot extends Module {

    public ModeSetting mode;
    public FloatSetting health;
    public FloatSetting pitch;
    public TimerUtil timer;

    public AutoPot() {
        super("Auto Pot", Category.Misc, "Automatically pots for you");
        String[] stringArray = new String[]{"Auto", "While Holding"};
        mode = setting("Mode", "Auto", stringArray).description("When to refill potions");
        health = setting("Health", 10.0f, 0.1f, 20.0f).description("Health % to pot at").invokeVisibility(arg_0 -> mode.getObject().equals("Auto"));
        pitch = setting("Pitch", 70.0f, 50.0f, 90.0f).description("Pitch to throw pearl at");
        timer = new TimerUtil();
    }

    @EventListener
    public void event(TickEvent event) {
        if (getMc().currentScreen != null || !getMc().isWindowFocused()) {
            return;
        }
        if (mode.getObject().equals("Auto")) {
            if (getMc().player.getHealth() > health.getObject()) {
                timer.reset();
                return;
            }
        }
        if (!timer.delay(300.0f)) {
            return;
        }
        int slot;
        if (mode.getObject().equals("Auto")) {
            Integer n2 = InventoryUtil.INSTANCE.findPotion(0, 9, StatusEffects.INSTANT_HEALTH);
            if (n2 == null) {
                return;
            }
            slot = n2;
        } else {
            slot = getMc().player.getInventory().selectedSlot;
        }
        float pitch = getMc().player.getPitch();
        int prevSlot = getMc().player.getInventory().selectedSlot;
        InventoryUtil.INSTANCE.setCurrentSlot(slot);
        getMc().player.setPitch(this.pitch.getObject() - 1.0f + RandomUtil.INSTANCE.getRandom().nextFloat());
        getMc().interactionManager.interactItem(getMc().player, Hand.MAIN_HAND);
        getMc().player.setPitch(pitch);
        InventoryUtil.INSTANCE.setCurrentSlot(prevSlot);
        timer.reset();
    }
}