/*
 * Decompiled with CFR 0_115.
 */
package cow.milkgod.cheese.module.modules;

import com.darkmagician6.eventapi.EventTarget;
import cow.milkgod.cheese.events.EventTick;
import cow.milkgod.cheese.module.Category;
import cow.milkgod.cheese.module.Module;
import cow.milkgod.cheese.utils.Wrapper;
import java.util.Objects;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.client.network.NetHandlerPlayClient;
import net.minecraft.network.Packet;
import net.minecraft.network.play.client.C03PacketPlayer;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;

public class Firion
extends Module {
    private boolean safe = true;
    private boolean potion;

    public Firion() {
        super("Firion", 0, Category.EXPLOITS, 10027008, true, "May the Cheese lords cleanse your fire", new String[]{"fir", "f1r1on"});
    }

    @EventTarget
    public void onTick(EventTick event) {
        if (Wrapper.isOnLiquid()) {
            return;
        }
        if (Wrapper.mc.thePlayer.isPotionActive(Potion.blindness.getId())) {
            Wrapper.mc.thePlayer.removePotionEffect(Potion.blindness.getId());
        }
        if (Wrapper.mc.thePlayer.isPotionActive(Potion.confusion.getId())) {
            Wrapper.mc.thePlayer.removePotionEffect(Potion.confusion.getId());
        }
        if (Wrapper.mc.thePlayer.isPotionActive(Potion.digSlowdown.getId())) {
            Wrapper.mc.thePlayer.removePotionEffect(Potion.digSlowdown.getId());
        }
        if (Wrapper.mc.thePlayer.isBurning()) {
            int x2 = 0;
            while (x2 < 100) {
                Wrapper.mc.getNetHandler().addToSendQueue(new C03PacketPlayer());
                ++x2;
            }
        }
        Potion[] potionTypes = Potion.potionTypes;
        int length = potionTypes.length;
        int i2 = 0;
        while (i2 < length) {
            Potion potion = potionTypes[i2];
            if (Objects.nonNull(potion) && potion.isBadEffect() && Wrapper.mc.thePlayer.isPotionActive(potion)) {
                PotionEffect effect = Wrapper.mc.thePlayer.getActivePotionEffect(potion);
                int x3 = 0;
                while (x3 < effect.getDuration() / 20) {
                    Wrapper.mc.getNetHandler().addToSendQueue(new C03PacketPlayer());
                    ++x3;
                }
            }
            ++i2;
        }
    }
}

