package ru.smertnix.celestial.feature.impl.combat;

import net.minecraft.item.ItemBow;
import net.minecraft.network.play.client.CPacketEntityAction;
import net.minecraft.network.play.client.CPacketPlayer;
import net.minecraft.network.play.client.CPacketPlayerDigging;
import net.minecraft.util.EnumHand;
import ru.smertnix.celestial.event.EventTarget;
import ru.smertnix.celestial.event.events.Event;
import ru.smertnix.celestial.event.events.impl.packet.EventSendPacket;
import ru.smertnix.celestial.feature.Feature;
import ru.smertnix.celestial.feature.impl.FeatureCategory;
import ru.smertnix.celestial.ui.settings.impl.BooleanSetting;
import ru.smertnix.celestial.ui.settings.impl.NumberSetting;

public class SuperBow extends Feature {
    private NumberSetting shotPower = new NumberSetting("Shot Power", 70.0f, 1.0f, 100.0f, 5.0f, () -> true);

    public SuperBow() {
        super("SuperBow", "Увеличивает урон лука", FeatureCategory.Combat);
        addSettings(shotPower);
    }

    @EventTarget
    public void onUpdate(Event event) {
        if (event instanceof EventSendPacket) {
            if (((EventSendPacket) event).getPacket() instanceof CPacketPlayerDigging) {
                CPacketPlayerDigging packet = (CPacketPlayerDigging) ((EventSendPacket) event).getPacket();
                if (packet.getAction() == CPacketPlayerDigging.Action.RELEASE_USE_ITEM
                        && mc.player.getHeldItem(EnumHand.MAIN_HAND).getItem() instanceof ItemBow) {
                    mc.player.connection
                            .sendPacket(new CPacketEntityAction(mc.player, CPacketEntityAction.Action.START_SPRINTING));
                    for (int i = 0; i < shotPower.getNumberValue(); i++) {
                        mc.player.connection.sendPacket(new CPacketPlayer.Position(mc.player.posX,
                                mc.player.posY + 1.0E-10, mc.player.posZ, false));
                        mc.player.connection.sendPacket(new CPacketPlayer.Position(mc.player.posX,
                                mc.player.posY - 1.0E-10, mc.player.posZ, true));
                    }
                }
            }
        }
    }
}

