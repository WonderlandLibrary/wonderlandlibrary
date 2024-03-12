// 
// Decompiled by Procyon v0.5.36
// 

package dev.tenacity.module.impl.misc;

import dev.tenacity.utils.player.ChatUtil;
import net.minecraft.network.play.server.S29PacketSoundEffect;
import dev.tenacity.event.impl.network.PacketReceiveEvent;
import dev.tenacity.module.Category;
import dev.tenacity.module.Module;

public final class LightningTracker extends Module
{
    public LightningTracker() {
        super("LightningTracker", "Lightning Tracker", Category.MISC, "detects lightning");
    }
    
    @Override
    public void onPacketReceiveEvent(final PacketReceiveEvent event) {
        if (event.getPacket() instanceof S29PacketSoundEffect) {
            final S29PacketSoundEffect soundPacket = (S29PacketSoundEffect)event.getPacket();
            if (soundPacket.getSoundName().equals("ambient.weather.thunder")) {
                ChatUtil.print(String.format("Lightning detected at (%s, %s, %s)", (int)soundPacket.getX(), (int)soundPacket.getY(), (int)soundPacket.getZ()));
            }
        }
    }
}
