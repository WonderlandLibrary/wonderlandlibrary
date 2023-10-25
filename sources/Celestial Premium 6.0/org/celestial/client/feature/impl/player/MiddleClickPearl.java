/*
 * Decompiled with CFR 0.150.
 */
package org.celestial.client.feature.impl.player;

import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.network.play.client.CPacketHeldItemChange;
import net.minecraft.network.play.client.CPacketPlayerTryUseItem;
import net.minecraft.util.EnumHand;
import org.celestial.client.event.EventTarget;
import org.celestial.client.event.events.impl.input.EventMouse;
import org.celestial.client.feature.Feature;
import org.celestial.client.feature.impl.Type;

public class MiddleClickPearl
extends Feature {
    public MiddleClickPearl() {
        super("MiddleClickPearl", "\u0410\u0432\u0442\u043e\u043c\u0430\u0442\u0438\u0447\u0435\u0441\u043a\u0438 \u043a\u0438\u0434\u0430\u0435\u0442 \u044d\u043d\u0434\u0435\u0440-\u043f\u0435\u0440\u043b \u043f\u0440\u0438 \u043d\u0430\u0436\u0430\u0442\u0438\u0438 \u043d\u0430 \u043a\u043e\u043b\u0435\u0441\u043e \u043c\u044b\u0448\u0438", Type.Player);
    }

    @EventTarget
    public void onMouseEvent(EventMouse event) {
        if (event.key == 2) {
            for (int i = 0; i < 9; ++i) {
                ItemStack itemStack = MiddleClickPearl.mc.player.inventory.getStackInSlot(i);
                if (itemStack.getItem() != Items.ENDER_PEARL) continue;
                MiddleClickPearl.mc.player.connection.sendPacket(new CPacketHeldItemChange(i));
                MiddleClickPearl.mc.player.connection.sendPacket(new CPacketPlayerTryUseItem(EnumHand.MAIN_HAND));
                MiddleClickPearl.mc.player.connection.sendPacket(new CPacketHeldItemChange(MiddleClickPearl.mc.player.inventory.currentItem));
            }
        }
    }

    @Override
    public void onDisable() {
        MiddleClickPearl.mc.player.connection.sendPacket(new CPacketHeldItemChange(MiddleClickPearl.mc.player.inventory.currentItem));
        super.onDisable();
    }
}

