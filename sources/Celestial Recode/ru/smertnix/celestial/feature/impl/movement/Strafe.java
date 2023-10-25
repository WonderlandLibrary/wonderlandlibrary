package ru.smertnix.celestial.feature.impl.movement;

import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.init.MobEffects;
import net.minecraft.inventory.ClickType;
import net.minecraft.network.Packet;
import net.minecraft.network.play.client.CPacketEntityAction;
import net.minecraft.network.play.client.CPacketPlayer;
import ru.smertnix.celestial.Celestial;
import ru.smertnix.celestial.event.EventTarget;
import ru.smertnix.celestial.event.events.EventAction;
import ru.smertnix.celestial.event.events.impl.packet.EventSendPacket;
import ru.smertnix.celestial.event.events.impl.player.EventPreMotion;
import ru.smertnix.celestial.event.events.impl.player.EventUpdate;
import ru.smertnix.celestial.feature.Feature;
import ru.smertnix.celestial.feature.impl.FeatureCategory;
import ru.smertnix.celestial.ui.settings.impl.BooleanSetting;
import ru.smertnix.celestial.ui.settings.impl.ListSetting;
import ru.smertnix.celestial.ui.settings.impl.NumberSetting;
import ru.smertnix.celestial.utils.movement.MovementUtils;

public class Strafe extends Feature {
    public static boolean needSwap;
    public Strafe() {
        super("Strafe", "Позволяет стрейфиться", FeatureCategory.Movement);
        addSettings();
    }
    @EventTarget
    public void onUpdate(EventPreMotion e) {
        if (!mc.player.onGround && MovementUtils.getSpeed() <= 0.21f) {
            if (!mc.player.isUsingItem()) {
                MovementUtils.setStrafe(Math.max(MovementUtils.getSpeed(), 0.22f));
            } else if (mc.player.ticksExisted % 2 == 0) {
                needSwap = true;
                MovementUtils.setStrafe(MovementUtils.getSpeed() - 0.019);
            }
        }

        if (!mc.player.onGround && mc.player.motionY == -0.4448259643949201) {
            MovementUtils.setMotion((float) MovementUtils.getSpeed());
        }

    }

    @EventTarget
    public void onAction(EventAction eventAction) {
        if (needSwap) {
            eventAction.sprintState = (!mc.player.serverSprintState);
            needSwap = false;
        }
    }
}
