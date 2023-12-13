/*
 * Decompiled with CFR 0_115.
 */
package cow.milkgod.cheese.module.modules;

import com.darkmagician6.eventapi.EventTarget;
import cow.milkgod.cheese.events.EventPreMotionUpdates;
import cow.milkgod.cheese.module.Category;
import cow.milkgod.cheese.module.Module;
import cow.milkgod.cheese.utils.Wrapper;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.client.network.NetHandlerPlayClient;
import net.minecraft.network.Packet;
import net.minecraft.network.play.client.C03PacketPlayer;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;

public class Zoot
extends Module {
    private boolean potion = true;

    public Zoot() {
        super("Zoot", 0, Category.EXPLOITS, 1209631361, true, "", new String[]{"zt"});
    }

    @EventTarget
    public void onPre(EventPreMotionUpdates event) {
        Potion[] potionTypes = Potion.potionTypes;
        int length = potionTypes.length;
        int k2 = 0;
        while (k2 < length) {
            PotionEffect effect;
            Potion potion = potionTypes[k2];
            if (potion != null && (effect = Wrapper.mc.thePlayer.getActivePotionEffect(potion)) != null && potion.isBadEffect() && this.potion) {
                int i2 = 0;
                while (i2 < effect.getDuration() / 20) {
                    Wrapper.mc.thePlayer.sendQueue.addToSendQueue(new C03PacketPlayer(true));
                    ++i2;
                }
            }
            ++k2;
        }
        if (Wrapper.mc.thePlayer.moveForward == 0.0f && Wrapper.mc.thePlayer.moveStrafing == 0.0f && Wrapper.mc.thePlayer.isBurning() && !Wrapper.isInLiquid() && !Wrapper.isOnLiquid()) {
            int j2 = 0;
            while (j2 < 50) {
                Wrapper.mc.thePlayer.sendQueue.addToSendQueue(new C03PacketPlayer(true));
                ++j2;
            }
        }
    }
}

