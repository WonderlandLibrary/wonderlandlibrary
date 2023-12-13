// 
// Decompiled by Procyon v0.5.30
// 

package exhibition.module.impl.player;

import exhibition.event.RegisterEvent;
import net.minecraft.potion.PotionEffect;
import net.minecraft.network.Packet;
import net.minecraft.network.play.client.C03PacketPlayer;
import net.minecraft.potion.Potion;
import exhibition.util.PlayerUtil;
import exhibition.event.impl.EventMotion;
import exhibition.event.Event;
import exhibition.module.data.ModuleData;
import exhibition.module.Module;

public class Effects extends Module
{
    public Effects(final ModuleData data) {
        super(data);
    }
    
    @RegisterEvent(events = { EventMotion.class })
    @Override
    public void onEvent(final Event event) {
        final EventMotion em = (EventMotion)event;
        if (em.isPre()) {
            if (PlayerUtil.isOnLiquid()) {
                return;
            }
            if (Effects.mc.thePlayer.isPotionActive(Potion.blindness.getId())) {
                Effects.mc.thePlayer.removePotionEffect(Potion.blindness.getId());
            }
            if (Effects.mc.thePlayer.isPotionActive(Potion.confusion.getId())) {
                Effects.mc.thePlayer.removePotionEffect(Potion.confusion.getId());
            }
            if (Effects.mc.thePlayer.isPotionActive(Potion.digSlowdown.getId())) {
                Effects.mc.thePlayer.removePotionEffect(Potion.digSlowdown.getId());
            }
            if (Effects.mc.thePlayer.isBurning() && Effects.mc.thePlayer.isCollidedVertically) {
                for (int i = 0; i < 12; ++i) {
                    Effects.mc.getNetHandler().addToSendQueue(new C03PacketPlayer(true));
                }
            }
            Potion[] potionTypes;
            for (int length = (potionTypes = Potion.potionTypes).length, j = 0; j < length; ++j) {
                final Potion potion = potionTypes[j];
                if (potion != null && potion.isBadEffect() && Effects.mc.thePlayer.isPotionActive(potion)) {
                    final PotionEffect activePotionEffect = Effects.mc.thePlayer.getActivePotionEffect(potion);
                    for (int k = 0; k < activePotionEffect.getDuration() / 20; ++k) {
                        Effects.mc.getNetHandler().addToSendQueue(new C03PacketPlayer(true));
                    }
                }
            }
        }
    }
}
