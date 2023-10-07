// 
// Decompiled by Procyon v0.5.30
// 

package exhibition.module.impl.other;

import exhibition.event.RegisterEvent;
import exhibition.util.misc.ChatUtil;
import net.minecraft.network.play.server.S3APacketTabComplete;
import exhibition.event.impl.EventPacket;
import exhibition.event.Event;
import exhibition.module.data.ModuleData;
import exhibition.util.Timer;
import exhibition.module.Module;

public class PluginScanner extends Module
{
    boolean isListening;
    Timer timer;
    
    public PluginScanner(final ModuleData data) {
        super(data);
        this.timer = new Timer();
    }
    
    @Override
    public void onEnable() {
        this.isListening = true;
        this.timer.reset();
    }
    
    @Override
    public void onDisable() {
        this.isListening = false;
        this.timer.reset();
    }
    
    @RegisterEvent(events = { EventPacket.class })
    @Override
    public void onEvent(final Event event) {
        final EventPacket ep = (EventPacket)event;
        if (ep.isIncoming() && ep.getPacket() instanceof S3APacketTabComplete && this.isListening && this.timer.delay(20000.0f)) {
            final S3APacketTabComplete packet = (S3APacketTabComplete)ep.getPacket();
            final String[] pluginsFound = packet.func_149630_c();
            ChatUtil.printChat("§4[§cE§4]§8 §3Found §7[§3" + pluginsFound.length + "§7] §3plugin(s): §8" + pluginsFound + "§3.");
            this.toggle();
        }
    }
}
