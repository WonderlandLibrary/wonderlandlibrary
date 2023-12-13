package me.dev.legacy.features.modules.render;

import me.dev.legacy.features.modules.Module;
import me.dev.legacy.features.setting.Setting;
import net.minecraft.item.ItemSword;
import net.minecraft.util.EnumHand;

/**
 * @author Novola
 * @since 19/1/2020 (1/19/2020)
 */
public class Swing extends Module {
    private Setting<Hand> hand = register(new Setting("Hand", Hand.OFFHAND));
    public Swing() {
        super("Swing", "Changes the hand you swing with", Module.Category.RENDER, false, false, false);
    }

    public void onUpdate() {
        if (mc.world == null)
            return;
        if(hand.getValue().equals(Hand.OFFHAND)) {
            mc.player.swingingHand = EnumHand.OFF_HAND;
        }
        if(hand.getValue().equals(Hand.MAINHAND)) {
            mc.player.swingingHand = EnumHand.MAIN_HAND;
        }
        if(hand.getValue().equals(Hand.PACKETSWING)) {
            if (mc.player.getHeldItemMainhand().getItem() instanceof ItemSword && mc.entityRenderer.itemRenderer.prevEquippedProgressMainHand >= 0.9) {
                mc.entityRenderer.itemRenderer.equippedProgressMainHand = 1.0f;
                mc.entityRenderer.itemRenderer.itemStackMainHand = mc.player.getHeldItemMainhand();
            }
        }
    }

    public enum Hand {
        OFFHAND,
        MAINHAND,
        PACKETSWING
    }
}