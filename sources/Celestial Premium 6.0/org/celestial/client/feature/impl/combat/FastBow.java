/*
 * Decompiled with CFR 0.150.
 */
package org.celestial.client.feature.impl.combat;

import net.minecraft.item.ItemBow;
import net.minecraft.network.play.client.CPacketPlayerDigging;
import net.minecraft.network.play.client.CPacketPlayerTryUseItem;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import org.celestial.client.event.EventTarget;
import org.celestial.client.event.events.impl.player.EventUpdate;
import org.celestial.client.feature.Feature;
import org.celestial.client.feature.impl.Type;
import org.celestial.client.settings.impl.NumberSetting;

public class FastBow
extends Feature {
    private final NumberSetting ticks = new NumberSetting("Bow Ticks", 1.5f, 1.5f, 10.0f, 0.5f, () -> true);

    public FastBow() {
        super("FastBow", "\u041f\u0440\u0438 \u0437\u0430\u0436\u0430\u0442\u0438\u0438 \u043d\u0430 \u041f\u041a\u041c \u0438\u0433\u0440\u043e\u043a \u0431\u044b\u0441\u0442\u0440\u043e \u0441\u0442\u0440\u0435\u043b\u044f\u0435\u0442 \u0438\u0437 \u043b\u0443\u043a\u0430", Type.Combat);
        this.addSettings(this.ticks);
    }

    @EventTarget
    public void onUpdate(EventUpdate e) {
        if (FastBow.mc.player.inventory.getCurrentItem().getItem() instanceof ItemBow && FastBow.mc.player.isHandActive() && (float)FastBow.mc.player.getItemInUseMaxCount() >= this.ticks.getCurrentValue()) {
            FastBow.mc.player.connection.sendPacket(new CPacketPlayerDigging(CPacketPlayerDigging.Action.RELEASE_USE_ITEM, BlockPos.ORIGIN, FastBow.mc.player.getHorizontalFacing()));
            FastBow.mc.player.connection.sendPacket(new CPacketPlayerTryUseItem(EnumHand.MAIN_HAND));
            FastBow.mc.player.stopActiveHand();
        }
    }
}

