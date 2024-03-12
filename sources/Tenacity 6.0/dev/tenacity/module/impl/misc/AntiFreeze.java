// 
// Decompiled by Procyon v0.5.36
// 

package dev.tenacity.module.impl.misc;

import dev.tenacity.module.Category;
import net.minecraft.network.play.server.S02PacketChat;
import net.minecraft.network.play.server.S2DPacketOpenWindow;
import dev.tenacity.event.impl.network.PacketReceiveEvent;
import dev.tenacity.module.settings.impl.ModeSetting;
import dev.tenacity.module.Module;

public final class AntiFreeze extends Module
{
    private final ModeSetting mode;
    
    @Override
    public void onPacketReceiveEvent(final PacketReceiveEvent event) {
        if (event.getPacket() instanceof S2DPacketOpenWindow && ((S2DPacketOpenWindow)event.getPacket()).getWindowTitle().getUnformattedText().contains("frozen")) {
            event.cancel();
        }
        else if (event.getPacket() instanceof S02PacketChat && ((S02PacketChat)event.getPacket()).getChatComponent().getUnformattedText().contains("frozen")) {
            if (this.mode.is("Teleport")) {
                AntiFreeze.mc.thePlayer.posY = -999.0;
            }
            event.cancel();
        }
    }
    
    public AntiFreeze() {
        super("AntiFreeze", "Anti Freeze", Category.MISC, "prevents server plugins from freezing you");
        this.mode = new ModeSetting("Mode", "Normal", new String[] { "Normal", "Teleport" });
    }
}
