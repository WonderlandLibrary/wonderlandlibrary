package ru.smertnix.celestial.feature.impl.misc;

import net.minecraft.init.Items;
import net.minecraft.network.play.client.CPacketPlayerTryUseItem;
import net.minecraft.util.EnumHand;
import ru.smertnix.celestial.event.EventTarget;
import ru.smertnix.celestial.event.events.impl.input.EventMouse;
import ru.smertnix.celestial.feature.Feature;
import ru.smertnix.celestial.feature.impl.FeatureCategory;

public class PearlThrowBlock extends Feature {
    public PearlThrowBlock() {
        super("PearlThrowBlock", "Позволяет кидать пёрлы в блоки на серверах, где это запрещено", FeatureCategory.Movement);
    }

    @EventTarget
    public void onMouse(EventMouse event) {
        if (event.getKey() == 1 && mc.player.getHeldItemMainhand().getItem() == Items.ENDER_PEARL) {
            mc.player.connection.sendPacket(new CPacketPlayerTryUseItem(EnumHand.MAIN_HAND));
        }
    }
}