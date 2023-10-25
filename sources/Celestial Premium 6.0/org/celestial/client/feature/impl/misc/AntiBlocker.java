/*
 * Decompiled with CFR 0.150.
 */
package org.celestial.client.feature.impl.misc;

import net.minecraft.init.Items;
import net.minecraft.network.play.client.CPacketPlayerTryUseItem;
import net.minecraft.util.EnumHand;
import org.celestial.client.event.EventTarget;
import org.celestial.client.event.events.impl.input.EventMouse;
import org.celestial.client.feature.Feature;
import org.celestial.client.feature.impl.Type;

public class AntiBlocker
extends Feature {
    public AntiBlocker() {
        super("AntiPearlBlocker", "\u041f\u043e\u0437\u0432\u043e\u043b\u044f\u0435\u0442 \u0431\u0440\u043e\u0441\u0430\u0442\u044c \u044d\u043d\u0434\u0435\u0440 \u043f\u0435\u0440\u043b \u043f\u043e\u0434 \u0441\u0435\u0431\u044f, \u043d\u0430 \u0441\u0435\u0440\u0432\u0435\u0440\u0430\u0445 \u0433\u0434\u0435 \u044d\u0442\u043e \u0437\u0430\u043f\u0440\u0435\u0449\u0435\u043d\u043e", Type.Player);
    }

    @EventTarget
    public void onMouse(EventMouse event) {
        if (event.key == 1 && AntiBlocker.mc.player.getHeldItemMainhand().getItem() == Items.ENDER_PEARL) {
            AntiBlocker.mc.player.connection.sendPacket(new CPacketPlayerTryUseItem(EnumHand.MAIN_HAND));
        }
    }
}

