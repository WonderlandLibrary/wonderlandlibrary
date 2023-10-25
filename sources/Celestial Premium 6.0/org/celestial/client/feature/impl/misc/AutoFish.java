/*
 * Decompiled with CFR 0.150.
 */
package org.celestial.client.feature.impl.misc;

import net.minecraft.init.SoundEvents;
import net.minecraft.item.ItemFishingRod;
import net.minecraft.network.play.client.CPacketPlayerTryUseItem;
import net.minecraft.network.play.server.SPacketSoundEffect;
import net.minecraft.util.EnumHand;
import net.minecraft.util.SoundCategory;
import org.celestial.client.event.EventTarget;
import org.celestial.client.event.events.impl.packet.EventReceivePacket;
import org.celestial.client.feature.Feature;
import org.celestial.client.feature.impl.Type;

public class AutoFish
extends Feature {
    public AutoFish() {
        super("AutoFish", "\u0410\u0432\u0442\u043e\u043c\u0430\u0442\u0438\u0447\u0435\u0441\u043a\u0438 \u043b\u043e\u0432\u0438\u0442 \u0440\u044b\u0431\u0443 \u0437\u0430 \u0432\u0430\u0441", Type.Misc);
    }

    @EventTarget
    public void onReceivePacket(EventReceivePacket event) {
        SPacketSoundEffect packet;
        if (event.getPacket() instanceof SPacketSoundEffect && (packet = (SPacketSoundEffect)event.getPacket()).getCategory() == SoundCategory.NEUTRAL && packet.getSound() == SoundEvents.ENTITY_BOBBER_SPLASH && (AutoFish.mc.player.getHeldItemMainhand().getItem() instanceof ItemFishingRod || AutoFish.mc.player.getHeldItemOffhand().getItem() instanceof ItemFishingRod)) {
            AutoFish.mc.player.connection.sendPacket(new CPacketPlayerTryUseItem(EnumHand.MAIN_HAND));
            AutoFish.mc.player.swingArm(EnumHand.MAIN_HAND);
            AutoFish.mc.player.connection.sendPacket(new CPacketPlayerTryUseItem(EnumHand.MAIN_HAND));
            AutoFish.mc.player.swingArm(EnumHand.MAIN_HAND);
        }
    }
}

