package me.nyan.flush.module.impl.player;

import me.nyan.flush.event.SubscribeEvent;
import me.nyan.flush.event.impl.EventUpdate;
import me.nyan.flush.module.Module;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemBlock;

public class Eagle extends Module {
    public Eagle() {
        super("Eagle", Category.PLAYER);
    }

    @SubscribeEvent
    public void onUpdate(EventUpdate e) {
        if (mc.thePlayer.getCurrentEquippedItem().getItem() != null && mc.thePlayer.getCurrentEquippedItem().getItem() instanceof ItemBlock) {
            mc.gameSettings.keyBindSneak.pressed = mc.thePlayer.getPosition().add(0, -1, 0).getBlock() == Blocks.air;
        }
    }
}
